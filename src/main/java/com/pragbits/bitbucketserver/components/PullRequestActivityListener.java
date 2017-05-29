package com.pragbits.bitbucketserver.components;

import com.atlassian.event.api.EventListener;
import com.atlassian.bitbucket.comment.Comment;
import com.atlassian.bitbucket.event.pull.PullRequestActivityEvent;
import com.atlassian.bitbucket.event.pull.PullRequestCommentActivityEvent;
import com.atlassian.bitbucket.event.pull.PullRequestRescopeActivityEvent;
import com.atlassian.bitbucket.nav.NavBuilder;
import com.atlassian.bitbucket.pull.PullRequestParticipant;
import com.atlassian.bitbucket.repository.Repository;
import com.atlassian.bitbucket.avatar.AvatarService;
import com.atlassian.bitbucket.avatar.AvatarRequest;
import com.google.gson.Gson;
import com.pragbits.bitbucketserver.*;
import com.pragbits.bitbucketserver.tools.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PullRequestActivityListener {
    private static final Logger log = LoggerFactory.getLogger(PullRequestActivityListener.class);

    private final ChatworkGlobalSettingsService chatworkGlobalSettingsService;
    private final ChatworkSettingsService chatworkSettingsService;
    private final NavBuilder navBuilder;
    private final ChatworkNotifier chatworkNotifier;
    private final AvatarService avatarService;
    private final AvatarRequest avatarRequest = new AvatarRequest(true, 16, true);
    private final Gson gson = new Gson();

    public PullRequestActivityListener(ChatworkGlobalSettingsService chatworkGlobalSettingsService,
                                             ChatworkSettingsService chatworkSettingsService,
                                             NavBuilder navBuilder,
                                             ChatworkNotifier chatworkNotifier,
                                             AvatarService avatarService) {
        this.chatworkGlobalSettingsService = chatworkGlobalSettingsService;
        this.chatworkSettingsService = chatworkSettingsService;
        this.navBuilder = navBuilder;
        this.chatworkNotifier = chatworkNotifier;
        this.avatarService = avatarService;
    }

    @EventListener
    public void NotifyChatworkChannel(PullRequestActivityEvent event) {
        // find out if notification is enabled for this repo
        Repository repository = event.getPullRequest().getToRef().getRepository();
        ChatworkSettings chatworkSettings = chatworkSettingsService.getChatworkSettings(repository);
        String globalHookUrl = chatworkGlobalSettingsService.getWebHookUrl();


        SettingsSelector settingsSelector = new SettingsSelector(chatworkSettingsService,  chatworkGlobalSettingsService, repository);
        ChatworkSettings resolvedChatworkSettings = settingsSelector.getResolvedChatworkSettings();

        if (resolvedChatworkSettings.isChatworkNotificationsEnabled()) {

            String localHookUrl = resolvedChatworkSettings.getChatworkWebHookUrl();
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
            long pullRequestId = event.getPullRequest().getId();
            String userName = event.getUser() != null ? event.getUser().getDisplayName() : "unknown user";
            String activity = event.getActivity().getAction().name();
            String avatar = event.getUser() != null ? avatarService.getUrlForPerson(event.getUser(), avatarRequest) : "";

            NotificationLevel resolvedLevel = resolvedChatworkSettings.getNotificationPrLevel();

            // Ignore RESCOPED PR events
            if (activity.equalsIgnoreCase("RESCOPED") && event instanceof PullRequestRescopeActivityEvent) {
                return;
            }

            if (activity.equalsIgnoreCase("OPENED") && !resolvedChatworkSettings.isChatworkNotificationsOpenedEnabled()) {
                return;
            }

            if (activity.equalsIgnoreCase("REOPENED") && !resolvedChatworkSettings.isChatworkNotificationsReopenedEnabled()) {
                return;
            }

            if (activity.equalsIgnoreCase("UPDATED") && !resolvedChatworkSettings.isChatworkNotificationsUpdatedEnabled()) {
                return;
            }

            if (activity.equalsIgnoreCase("APPROVED") && !resolvedChatworkSettings.isChatworkNotificationsApprovedEnabled()) {
                return;
            }

            if (activity.equalsIgnoreCase("UNAPPROVED") && !resolvedChatworkSettings.isChatworkNotificationsUnapprovedEnabled()) {
                return;
            }

            if (activity.equalsIgnoreCase("DECLINED") && !resolvedChatworkSettings.isChatworkNotificationsDeclinedEnabled()) {
                return;
            }

            if (activity.equalsIgnoreCase("MERGED") && !resolvedChatworkSettings.isChatworkNotificationsMergedEnabled()) {
                return;
            }

            if (activity.equalsIgnoreCase("COMMENTED") && !resolvedChatworkSettings.isChatworkNotificationsCommentedEnabled()) {
                return;
            }

            NavBuilder.PullRequest pullRequestUrlBuilder = navBuilder
                    .project(projectName)
                    .repo(repoName)
                    .pullRequest(pullRequestId);

            String url = pullRequestUrlBuilder
                    .overview()
                    .buildAbsolute();

            ChatworkPayload payload = new ChatworkPayload();
            payload.setMrkdwn(true);
            payload.setLinkNames(true);
            payload.setUsername(resolvedChatworkSettings.getChatworkUsername());
            payload.setIconUrl(resolvedChatworkSettings.getChatworkIconUrl());
            payload.setIconEmoji(resolvedChatworkSettings.getChatworkIconEmoji());

            ChatworkAttachment attachment = new ChatworkAttachment();
            attachment.setAuthorName(userName);
            attachment.setAuthorIcon(avatar);

            switch (event.getActivity().getAction()) {
                case OPENED:
                    attachment.setColor(ColorCode.BLUE.getCode());
                    attachment.setFallback(String.format("%s opened pull request \"%s\". <%s|(open)>",
                                                            userName,
                                                            event.getPullRequest().getTitle(),
                                                            url));
                    attachment.setText(String.format("opened pull request <%s|#%d: %s>",
                                                            url,
                                                            event.getPullRequest().getId(),
                                                            event.getPullRequest().getTitle()));


                    if (resolvedLevel == NotificationLevel.COMPACT) {
                        this.addField(attachment, "Description", event.getPullRequest().getDescription());
                    }

                    if (resolvedLevel == NotificationLevel.VERBOSE) {
                        this.addReviewers(attachment, event.getPullRequest().getReviewers());
                    }
                    break;

                case REOPENED:
                    attachment.setColor(ColorCode.BLUE.getCode());
                    attachment.setFallback(String.format("%s reopened pull request \"%s\". <%s|(open)>",
                                                            userName,
                                                            event.getPullRequest().getTitle(),
                                                            url));
                    attachment.setText(String.format("reopened pull request <%s|#%d: %s>",
                                                            url,
                                                            event.getPullRequest().getId(),
                                                            event.getPullRequest().getTitle()));

                    if (resolvedLevel == NotificationLevel.COMPACT) {
                        this.addField(attachment, "Description", event.getPullRequest().getDescription());
                    }
                    if (resolvedLevel == NotificationLevel.VERBOSE) {
                        this.addReviewers(attachment, event.getPullRequest().getReviewers());
                    }
                    break;

                case UPDATED:
                    attachment.setColor(ColorCode.PURPLE.getCode());
                    attachment.setFallback(String.format("%s updated pull request \"%s\". <%s|(open)>",
                                                            userName,
                                                            event.getPullRequest().getTitle(),
                                                            url));
                    attachment.setText(String.format("updated pull request <%s|#%d: %s>",
                                                            url,
                                                            event.getPullRequest().getId(),
                                                            event.getPullRequest().getTitle()));

                    if (resolvedLevel == NotificationLevel.COMPACT) {
                        this.addField(attachment, "Description", event.getPullRequest().getDescription());
                    }
                    if (resolvedLevel == NotificationLevel.VERBOSE) {
                        this.addReviewers(attachment, event.getPullRequest().getReviewers());
                    }
                    break;

                case APPROVED:
                    attachment.setColor(ColorCode.GREEN.getCode());
                    attachment.setFallback(String.format("%s approved pull request \"%s\". <%s|(open)>",
                                                            userName,
                                                            event.getPullRequest().getTitle(),
                                                            url));
                    attachment.setText(String.format("approved pull request <%s|#%d: %s>",
                                                            url,
                                                            event.getPullRequest().getId(),
                                                            event.getPullRequest().getTitle()));
                    break;

                case UNAPPROVED:
                    attachment.setColor(ColorCode.RED.getCode());
                    attachment.setFallback(String.format("%s unapproved pull request \"%s\". <%s|(open)>",
                                                            userName,
                                                            event.getPullRequest().getTitle(),
                                                            url));
                    attachment.setText(String.format("unapproved pull request <%s|#%d: %s>",
                                                            url,
                                                            event.getPullRequest().getId(),
                                                            event.getPullRequest().getTitle()));
                    break;

                case DECLINED:
                    attachment.setColor(ColorCode.DARK_RED.getCode());
                    attachment.setFallback(String.format("%s declined pull request \"%s\". <%s|(open)>",
                                                            userName,
                                                            event.getPullRequest().getTitle(),
                                                            url));
                    attachment.setText(String.format("declined pull request <%s|#%d: %s>",
                                                            url,
                                                            event.getPullRequest().getId(),
                                                            event.getPullRequest().getTitle()));
                    break;

                case MERGED:
                    attachment.setColor(ColorCode.DARK_GREEN.getCode());
                    attachment.setFallback(String.format("%s merged pull request \"%s\". <%s|(open)>",
                                                            userName,
                                                            event.getPullRequest().getTitle(),
                                                            url));
                    attachment.setText(String.format("merged pull request <%s|#%d: %s>",
                                                            url,
                                                            event.getPullRequest().getId(),
                                                            event.getPullRequest().getTitle()));
                    break;

                case RESCOPED:
                    attachment.setColor(ColorCode.PURPLE.getCode());
                    attachment.setFallback(String.format("%s rescoped on pull request \"%s\". <%s|(open)>",
                                                            userName,
                                                            event.getPullRequest().getTitle(),
                                                            url));
                    attachment.setText(String.format("rescoped on pull request <%s|#%d: %s>",
                                                            url,
                                                            event.getPullRequest().getId(),
                                                            event.getPullRequest().getTitle()));
                    break;

                case COMMENTED:
                    Comment comment = ((PullRequestCommentActivityEvent) event).getActivity().getComment();
                    String commentUrl = pullRequestUrlBuilder
                            .comment(comment.getId())
                            .buildAbsolute();

                    attachment.setColor(ColorCode.PALE_BLUE.getCode());
                    attachment.setFallback(String.format("%s commented on pull request \"%s\". <%s|(open)>",
                                                            userName,
                                                            event.getPullRequest().getTitle(),
                                                            commentUrl));
                    if (resolvedLevel == NotificationLevel.MINIMAL) {
                        attachment.setText(String.format("commented on pull request <%s|#%d: %s>",
                                commentUrl,
                                event.getPullRequest().getId(),
                                event.getPullRequest().getTitle()));
                    }
                    if (resolvedLevel == NotificationLevel.COMPACT || resolvedLevel == NotificationLevel.VERBOSE) {
                        attachment.setText(String.format("commented on pull request <%s|#%d: %s>\n%s",
                                commentUrl,
                                event.getPullRequest().getId(),
                                event.getPullRequest().getTitle(),
                                ((PullRequestCommentActivityEvent) event).getActivity().getComment().getText()));
                    }
                    break;
            }

            if (resolvedLevel == NotificationLevel.VERBOSE) {
                ChatworkAttachmentField projectField = new ChatworkAttachmentField();
                projectField.setTitle("Source");
                projectField.setValue(String.format("_%s — %s_\n`%s`",
                        event.getPullRequest().getFromRef().getRepository().getProject().getName(),
                        event.getPullRequest().getFromRef().getRepository().getName(),
                        event.getPullRequest().getFromRef().getDisplayId()));
                projectField.setShort(true);
                attachment.addField(projectField);

                ChatworkAttachmentField repoField = new ChatworkAttachmentField();
                repoField.setTitle("Destination");
                repoField.setValue(String.format("_%s — %s_\n`%s`",
                        event.getPullRequest().getToRef().getRepository().getProject().getName(),
                        event.getPullRequest().getToRef().getRepository().getName(),
                        event.getPullRequest().getToRef().getDisplayId()));
                repoField.setShort(true);
                attachment.addField(repoField);
            }

            payload.addAttachment(attachment);

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
                    if (event.getPullRequest().getToRef().getDisplayId().replace("refs/heads/", "").matches(pattern)) {
                        payload.setChannel(patterns.get(pattern));
                        log.debug("#sending message to: " + payload.getChannel());
                        chatworkNotifier.SendChatworkNotification(hookSelector.getSelectedHook(), gson.toJson(payload));
                        break;
                    }
                }
            }
        }

    }

    private void addField(ChatworkAttachment attachment, String title, String message) {
        ChatworkAttachmentField field = new ChatworkAttachmentField();
        field.setTitle(title);
        field.setValue(message);
        field.setShort(false);
        attachment.addField(field);
    }

    private void addReviewers(ChatworkAttachment attachment, Set<PullRequestParticipant> reviewers) {
        if (reviewers.isEmpty()) {
            return;
        }
        String names = "";
        for(PullRequestParticipant p : reviewers) {
            names += String.format("@%s ", p.getUser().getSlug());
        }
        this.addField(attachment, "Reviewers", names);
    }
}
