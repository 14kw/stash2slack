package com.pragbits.bitbucketserver.components;

import com.atlassian.bitbucket.commit.*;
import com.atlassian.bitbucket.util.PageRequestImpl;
import com.atlassian.event.api.EventListener;
import com.atlassian.bitbucket.event.repository.RepositoryPushEvent;
import com.atlassian.bitbucket.nav.NavBuilder;
import com.atlassian.bitbucket.repository.RefChange;
import com.atlassian.bitbucket.repository.RefChangeType;
import com.atlassian.bitbucket.repository.Repository;
import com.atlassian.bitbucket.util.Page;
import com.atlassian.bitbucket.util.PageRequest;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.pragbits.bitbucketserver.ColorCode;
import com.pragbits.bitbucketserver.ChatworkGlobalSettingsService;
import com.pragbits.bitbucketserver.ChatworkSettings;
import com.pragbits.bitbucketserver.ChatworkSettingsService;
import com.pragbits.bitbucketserver.tools.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RepositoryPushActivityListener {
    private static final Logger log = LoggerFactory.getLogger(RepositoryPushActivityListener.class);

    private final ChatworkGlobalSettingsService chatworkGlobalSettingsService;
    private final ChatworkSettingsService chatworkSettingsService;
    private final CommitService commitService;
    private final NavBuilder navBuilder;
    private final ChatworkNotifier chatworkNotifier;
    private final Gson gson = new Gson();

    public RepositoryPushActivityListener(ChatworkGlobalSettingsService chatworkGlobalSettingsService,
                                          ChatworkSettingsService chatworkSettingsService,
                                          CommitService commitService,
                                          NavBuilder navBuilder,
                                          ChatworkNotifier chatworkNotifier) {
        this.chatworkGlobalSettingsService = chatworkGlobalSettingsService;
        this.chatworkSettingsService = chatworkSettingsService;
        this.commitService = commitService;
        this.navBuilder = navBuilder;
        this.chatworkNotifier = chatworkNotifier;
    }

    @EventListener
    public void NotifyChatworkChannel(RepositoryPushEvent event) {
        // find out if notification is enabled for this repo
        Repository repository = event.getRepository();
        ChatworkSettings chatworkSettings = chatworkSettingsService.getChatworkSettings(repository);
        String globalHookUrl = chatworkGlobalSettingsService.getWebHookUrl();

        SettingsSelector settingsSelector = new SettingsSelector(chatworkSettingsService,  chatworkGlobalSettingsService, repository);
        ChatworkSettings resolvedChatworkSettings = settingsSelector.getResolvedChatworkSettings();

        if (resolvedChatworkSettings.isChatworkNotificationsEnabledForPush()) {
            String localHookUrl = chatworkSettings.getChatworkWebHookUrl();
            WebHookSelector hookSelector = new WebHookSelector(globalHookUrl, localHookUrl);
            ChannelSelector channelSelector = new ChannelSelector(chatworkGlobalSettingsService.getChannelName(), chatworkSettings.getChatworkChannelName());

            if (!hookSelector.isHookValid()) {
                log.error("There is no valid configured Web hook url! Reason: " + hookSelector.getProblem());
                return;
            }

            if (repository.isFork() && !resolvedChatworkSettings.isChatworkNotificationsEnabledForPersonal()) {
                // simply return silently when we don't want forks to get notifications unless they're explicitly enabled
                return;
            }

            String repoName = repository.getSlug();
            String projectName = repository.getProject().getKey();

            String repoPath = projectName + "/" + event.getRepository().getName();

            for (RefChange refChange : event.getRefChanges()) {
                String text;
                String ref = refChange.getRef().getId();
                NavBuilder.Repo repoUrlBuilder = navBuilder
                        .project(projectName)
                        .repo(repoName);
                String url = repoUrlBuilder
                        .commits()
                        .until(refChange.getRef().getId())
                        .buildAbsolute();

                List<Commit> myCommits = new LinkedList<Commit>();

                boolean isNewRef = refChange.getFromHash().equalsIgnoreCase("0000000000000000000000000000000000000000");
                boolean isDeleted = refChange.getToHash().equalsIgnoreCase("0000000000000000000000000000000000000000")
                    && refChange.getType() == RefChangeType.DELETE;
                if (isDeleted) {
                    // issue#4: if type is "DELETE" and toHash is all zero then this is a branch delete
                    if (ref.indexOf("refs/tags") >= 0) {
                        text = String.format("Tag [%s] deleted from repository `%s`.",
                                ref.replace("refs/tags/", ""),
                                repoPath);
                    } else {
                        text = String.format("Branch [%s] deleted from repository `%s`.",
                                ref.replace("refs/heads/", ""),
                                repoPath);
                    }
                } else if (isNewRef) {
                    // issue#3 if fromHash is all zero (meaning the beginning of everything, probably), then this push is probably
                    // a new branch or tag, and we want only to display the latest commit, not the entire history

                    if (ref.indexOf("refs/tags") >= 0) {
                        text = String.format("Tag [%s] pushed on `%s`.",
                                ref.replace("refs/tags/", ""),
                                repoPath
                                );
                    } else {
                        text = String.format("Branch [%s] pushed on `%s`.",
                                ref.replace("refs/heads/", ""),
                                repoPath
                                );
                    }
                } else {
                    PageRequest pRequest = new PageRequestImpl(0, PageRequest.MAX_PAGE_LIMIT);
                    CommitsBetweenRequest commitsBetween = new CommitsBetweenRequest.Builder(repository).exclude(refChange.getFromHash()).include(refChange.getToHash()).build();
                    Page<Commit> commitList = commitService.getCommitsBetween(commitsBetween, pRequest);
                    myCommits.addAll(Lists.newArrayList(commitList.getValues()));

                    int commitCount = myCommits.size();
                    String commitStr = commitCount == 1 ? "commit" : "commits";

                    String branch = ref.replace("refs/heads/", "");
                    text = String.format("Push on [%s] branch [%s] \n by `%s %s` (%d %s).",
                            repoPath,
                            branch,
                            event.getUser() != null ? event.getUser().getDisplayName() : "unknown user",
                            event.getUser() != null ? event.getUser().getEmailAddress() : "unknown email",
                            commitCount, commitStr
                            );
                }

                // Figure out what type of change this is:

                ChatworkPayload payload = new ChatworkPayload();

                if (!resolvedChatworkSettings.getChatworkIconEmoji().isEmpty()) {
                    text = resolvedChatworkSettings.getChatworkIconEmoji() + " " + text;
                }
                payload.setText(text);
                payload.setReqType("Push");
                payload.setUsername(resolvedChatworkSettings.getChatworkUsername());
                payload.setIconUrl(resolvedChatworkSettings.getChatworkIconUrl());
                payload.setIconEmoji(resolvedChatworkSettings.getChatworkIconEmoji());

                switch (resolvedChatworkSettings.getNotificationLevel()) {
                    case COMPACT:
                        compactCommitLog(event, refChange, payload, repoUrlBuilder, myCommits);
                        break;
                    case VERBOSE:
                        verboseCommitLog(event, refChange, payload, repoUrlBuilder, text, myCommits);
                        break;
                    case MINIMAL:
                    default:
                        break;
                }

                // chatworkSettings.getChatworkChannelName might be:
                // - empty
                // - single channel value
                // - comma separated list of pairs (pattern, channel) eg: bugfix/.*->#test-bf,master->#test-master

                if (channelSelector.isEmptyOrSingleValue()) {
                    log.debug("#sending message to: " + payload.getChannel());
                    if (channelSelector.getSelectedChannel() != "") {
                        payload.setChannel(channelSelector.getSelectedChannel());
                    }
                    chatworkNotifier.SendChatworkNotification(hookSelector.getSelectedHook(), gson.toJson(payload));
                } else {
                    Map<String, String> patterns = channelSelector.getChannels();
                    for (String pattern: patterns.keySet()) {
                        if (ref.replace("refs/heads/", "").matches(pattern)) {
                            payload.setChannel(patterns.get(pattern));
                            log.debug("#sending message to: " + payload.getChannel());
                            chatworkNotifier.SendChatworkNotification(hookSelector.getSelectedHook(), gson.toJson(payload));
                            break;
                        }
                    }
                }
            }
        }
    }

    private void compactCommitLog(RepositoryPushEvent event, RefChange refChange, ChatworkPayload payload, NavBuilder.Repo urlBuilder, List<Commit> myCommits) {
        if (myCommits.size() == 0) {
            // If there are no commits, no reason to add anything
        }
        ChatworkAttachment commits = new ChatworkAttachment();
        commits.setColor(ColorCode.GRAY.getCode());
        // Since the branch is now in the main commit line, title is not needed
        //commits.setTitle(String.format("[%s:%s]", event.getRepository().getName(), refChange.getRefId().replace("refs/heads", "")));
        StringBuilder attachmentFallback = new StringBuilder();
        StringBuilder commitListBlock = new StringBuilder();
        for (Commit c : myCommits) {
            String commitUrl = urlBuilder.commit(c.getId()).buildAbsolute();
            String firstCommitMessageLine = c.getMessage().split("\n")[0];

            // Note that we changed this to put everything in one attachment because otherwise it
            // doesn't get collapsed in chatwork (the see more... doesn't appear)
            commitListBlock.append(String.format("`%s`: %s - _%s_\n%s\n",
                    ch.getDisplayId(), firstCommitMessageLine, ch.getAuthor().getName(), commitUrl));

            attachmentFallback.append(String.format("%s: %s\n", c.getDisplayId(), firstCommitMessageLine));
        }
        commits.setText(commitListBlock.toString());
        commits.setFallback(attachmentFallback.toString());

        payload.addAttachment(commits);
    }

    private void verboseCommitLog(RepositoryPushEvent event, RefChange refChange, ChatworkPayload payload, NavBuilder.Repo urlBuilder, String text, List<Commit> myCommits) {
        for (Commit c : myCommits) {
            ChatworkAttachment attachment = new ChatworkAttachment();
            attachment.setFallback(text);
            attachment.setColor(ColorCode.GRAY.getCode());
            ChatworkAttachmentField field = new ChatworkAttachmentField();

            attachment.setTitle(String.format("[%s:%s] - %s", event.getRepository().getName(), refChange.getRefId().replace("refs/heads", ""), c.getId()));
            attachment.setTitle_link(urlBuilder.commit(c.getId()).buildAbsolute());

            field.setTitle("comment");
            field.setValue(c.getMessage());
            field.setShort(false);
            attachment.addField(field);
            payload.addAttachment(attachment);
        }
    }
}
