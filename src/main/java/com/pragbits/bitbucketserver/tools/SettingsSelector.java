package com.pragbits.bitbucketserver.tools;

import com.atlassian.bitbucket.repository.Repository;
import com.pragbits.bitbucketserver.ImmutableChatworkSettings;
import com.pragbits.bitbucketserver.ChatworkGlobalSettingsService;
import com.pragbits.bitbucketserver.ChatworkSettings;
import com.pragbits.bitbucketserver.ChatworkSettingsService;

public class SettingsSelector {

    private ChatworkGlobalSettingsService chatworkGlobalSettingsService;
    private ChatworkSettings chatworkSettings;
    private ChatworkSettings resolvedChatworkSettings;

    public SettingsSelector(ChatworkSettingsService chatworkSettingsService, ChatworkGlobalSettingsService chatworkGlobalSettingsService, Repository repository) {
        this.chatworkGlobalSettingsService = chatworkGlobalSettingsService;
        this.chatworkSettings = chatworkSettingsService.getChatworkSettings(repository);
        this.setResolvedChatworkSettings();
    }

    public ChatworkSettings getResolvedChatworkSettings() {
        return this.resolvedChatworkSettings;
    }

    private void setResolvedChatworkSettings() {
        resolvedChatworkSettings = new ImmutableChatworkSettings(
                chatworkSettings.isChatworkNotificationsOverrideEnabled(),
                chatworkSettings.isChatworkNotificationsOverrideEnabled() ? chatworkSettings.isChatworkNotificationsEnabled() : chatworkGlobalSettingsService.getChatworkNotificationsEnabled(),
                chatworkSettings.isChatworkNotificationsOverrideEnabled() ? chatworkSettings.isChatworkNotificationsOpenedEnabled() : chatworkGlobalSettingsService.getChatworkNotificationsOpenedEnabled(),
                chatworkSettings.isChatworkNotificationsOverrideEnabled() ? chatworkSettings.isChatworkNotificationsReopenedEnabled() : chatworkGlobalSettingsService.getChatworkNotificationsReopenedEnabled(),
                chatworkSettings.isChatworkNotificationsOverrideEnabled() ? chatworkSettings.isChatworkNotificationsUpdatedEnabled() : chatworkGlobalSettingsService.getChatworkNotificationsUpdatedEnabled(),
                chatworkSettings.isChatworkNotificationsOverrideEnabled() ? chatworkSettings.isChatworkNotificationsApprovedEnabled() : chatworkGlobalSettingsService.getChatworkNotificationsApprovedEnabled(),
                chatworkSettings.isChatworkNotificationsOverrideEnabled() ? chatworkSettings.isChatworkNotificationsUnapprovedEnabled() : chatworkGlobalSettingsService.getChatworkNotificationsUnapprovedEnabled(),
                chatworkSettings.isChatworkNotificationsOverrideEnabled() ? chatworkSettings.isChatworkNotificationsDeclinedEnabled() : chatworkGlobalSettingsService.getChatworkNotificationsDeclinedEnabled(),
                chatworkSettings.isChatworkNotificationsOverrideEnabled() ? chatworkSettings.isChatworkNotificationsMergedEnabled() : chatworkGlobalSettingsService.getChatworkNotificationsMergedEnabled(),
                chatworkSettings.isChatworkNotificationsOverrideEnabled() ? chatworkSettings.isChatworkNotificationsCommentedEnabled() : chatworkGlobalSettingsService.getChatworkNotificationsCommentedEnabled(),
                chatworkSettings.isChatworkNotificationsOverrideEnabled() ? chatworkSettings.isChatworkNotificationsEnabledForPush() : chatworkGlobalSettingsService.getChatworkNotificationsEnabledForPush(),
                chatworkSettings.isChatworkNotificationsOverrideEnabled() ? chatworkSettings.isChatworkNotificationsEnabledForPersonal() : chatworkGlobalSettingsService.getChatworkNotificationsEnabledForPersonal(),
                chatworkSettings.isChatworkNotificationsOverrideEnabled() ? chatworkSettings.getNotificationLevel() : chatworkGlobalSettingsService.getNotificationLevel(),
                chatworkSettings.isChatworkNotificationsOverrideEnabled() ? chatworkSettings.getNotificationPrLevel() : chatworkGlobalSettingsService.getNotificationPrLevel(),
                chatworkSettings.isChatworkNotificationsOverrideEnabled() ? chatworkSettings.getChatworkChannelName() : chatworkGlobalSettingsService.getChannelName(),
                chatworkSettings.isChatworkNotificationsOverrideEnabled() ? chatworkSettings.getChatworkWebHookUrl() : chatworkGlobalSettingsService.getWebHookUrl(),
                chatworkSettings.isChatworkNotificationsOverrideEnabled() ? chatworkSettings.getChatworkUsername() : chatworkGlobalSettingsService.getUsername(),
                chatworkSettings.isChatworkNotificationsOverrideEnabled() ? chatworkSettings.getChatworkIconUrl() : chatworkGlobalSettingsService.getIconUrl(),
                chatworkSettings.isChatworkNotificationsOverrideEnabled() ? chatworkSettings.getChatworkIconEmoji() : chatworkGlobalSettingsService.getIconEmoji()
        );
    }

}
