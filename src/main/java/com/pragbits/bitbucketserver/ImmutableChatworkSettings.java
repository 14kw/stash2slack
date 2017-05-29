package com.pragbits.bitbucketserver;

public class ImmutableChatworkSettings implements ChatworkSettings {

    private final boolean chatworkNotificationsOverrideEnabled;
    private final boolean chatworkNotificationsEnabled;
    private final boolean chatworkNotificationsOpenedEnabled;
    private final boolean chatworkNotificationsReopenedEnabled;
    private final boolean chatworkNotificationsUpdatedEnabled;
    private final boolean chatworkNotificationsApprovedEnabled;
    private final boolean chatworkNotificationsUnapprovedEnabled;
    private final boolean chatworkNotificationsDeclinedEnabled;
    private final boolean chatworkNotificationsMergedEnabled;
    private final boolean chatworkNotificationsCommentedEnabled;
    private final boolean chatworkNotificationsEnabledForPush;
    private final boolean chatworkNotificationsEnabledForPersonal;
    private final NotificationLevel notificationLevel;
    private final NotificationLevel notificationPrLevel;
    private final String chatworkChannelName;
    private final String chatworkWebHookUrl;
    private final String chatworkUsername;
    private final String chatworkIconUrl;
    private final String chatworkIconEmoji;


    public ImmutableChatworkSettings(boolean chatworkNotificationsOverrideEnabled,
                                  boolean chatworkNotificationsEnabled,
                                  boolean chatworkNotificationsOpenedEnabled,
                                  boolean chatworkNotificationsReopenedEnabled,
                                  boolean chatworkNotificationsUpdatedEnabled,
                                  boolean chatworkNotificationsApprovedEnabled,
                                  boolean chatworkNotificationsUnapprovedEnabled,
                                  boolean chatworkNotificationsDeclinedEnabled,
                                  boolean chatworkNotificationsMergedEnabled,
                                  boolean chatworkNotificationsCommentedEnabled,
                                  boolean chatworkNotificationsEnabledForPush,
                                  boolean chatworkNotificationsEnabledForPersonal,
                                  NotificationLevel notificationLevel,
                                  NotificationLevel notificationPrLevel,
                                  String chatworkChannelName,
                                  String chatworkWebHookUrl,
                                  String chatworkUsername,
                                  String chatworkIconUrl,
                                  String chatworkIconEmoji) {
        this.chatworkNotificationsOverrideEnabled = chatworkNotificationsOverrideEnabled;
        this.chatworkNotificationsEnabled = chatworkNotificationsEnabled;
        this.chatworkNotificationsOpenedEnabled = chatworkNotificationsOpenedEnabled;
        this.chatworkNotificationsReopenedEnabled = chatworkNotificationsReopenedEnabled;
        this.chatworkNotificationsUpdatedEnabled = chatworkNotificationsUpdatedEnabled;
        this.chatworkNotificationsApprovedEnabled = chatworkNotificationsApprovedEnabled;
        this.chatworkNotificationsUnapprovedEnabled = chatworkNotificationsUnapprovedEnabled;
        this.chatworkNotificationsDeclinedEnabled = chatworkNotificationsDeclinedEnabled;
        this.chatworkNotificationsMergedEnabled = chatworkNotificationsMergedEnabled;
        this.chatworkNotificationsCommentedEnabled = chatworkNotificationsCommentedEnabled;
        this.chatworkNotificationsEnabledForPush = chatworkNotificationsEnabledForPush;
        this.chatworkNotificationsEnabledForPersonal = chatworkNotificationsEnabledForPersonal;
        this.notificationLevel = notificationLevel;
        this.notificationPrLevel = notificationPrLevel;
        this.chatworkChannelName = chatworkChannelName;
        this.chatworkWebHookUrl = chatworkWebHookUrl;
        this.chatworkUsername = chatworkUsername;
        this.chatworkIconUrl = chatworkIconUrl;
        this.chatworkIconEmoji = chatworkIconEmoji;
    }

    public boolean isChatworkNotificationsOverrideEnabled() {
        return chatworkNotificationsOverrideEnabled;
    }

    public boolean isChatworkNotificationsEnabled() {
        return chatworkNotificationsEnabled;
    }

    public boolean isChatworkNotificationsOpenedEnabled() {
        return chatworkNotificationsOpenedEnabled;
    }

    public boolean isChatworkNotificationsReopenedEnabled() {
        return chatworkNotificationsReopenedEnabled;
    }

    public boolean isChatworkNotificationsUpdatedEnabled() {
        return chatworkNotificationsUpdatedEnabled;
    }

    public boolean isChatworkNotificationsApprovedEnabled() {
        return chatworkNotificationsApprovedEnabled;
    }

    public boolean isChatworkNotificationsUnapprovedEnabled() {
        return chatworkNotificationsUnapprovedEnabled;
    }

    public boolean isChatworkNotificationsDeclinedEnabled() {
        return chatworkNotificationsDeclinedEnabled;
    }

    public boolean isChatworkNotificationsMergedEnabled() {
        return chatworkNotificationsMergedEnabled;
    }

    public boolean isChatworkNotificationsCommentedEnabled() {
        return chatworkNotificationsCommentedEnabled;
    }

    public boolean isChatworkNotificationsEnabledForPush() {
        return chatworkNotificationsEnabledForPush;
    }

    public boolean isChatworkNotificationsEnabledForPersonal() { return chatworkNotificationsEnabledForPersonal; }

    public NotificationLevel getNotificationLevel() {
        return notificationLevel;
    }

    public NotificationLevel getNotificationPrLevel() {
        return notificationPrLevel;
    }

    public String getChatworkChannelName() {
        return chatworkChannelName;
    }

    public String getChatworkWebHookUrl() {
        return chatworkWebHookUrl;
    }

    public String getChatworkUsername() {
        return chatworkUsername;
    }

    public String getChatworkIconUrl() {
        return chatworkIconUrl;
    }

    public String getChatworkIconEmoji() {
        return chatworkIconEmoji;
    }

    @Override
    public String toString() {
        return "ImmutableChatworkSettings {" + "chatworkNotificationsOverrideEnabled=" + chatworkNotificationsOverrideEnabled +
                ", chatworkNotificationsEnabled=" + chatworkNotificationsEnabled +
                ", chatworkNotificationsOpenedEnabled=" + chatworkNotificationsOpenedEnabled +
                ", chatworkNotificationsReopenedEnabled=" + chatworkNotificationsReopenedEnabled +
                ", chatworkNotificationsUpdatedEnabled=" + chatworkNotificationsUpdatedEnabled +
                ", chatworkNotificationsApprovedEnabled=" + chatworkNotificationsApprovedEnabled +
                ", chatworkNotificationsUnapprovedEnabled=" + chatworkNotificationsUnapprovedEnabled +
                ", chatworkNotificationsDeclinedEnabled=" + chatworkNotificationsDeclinedEnabled +
                ", chatworkNotificationsMergedEnabled=" + chatworkNotificationsMergedEnabled +
                ", chatworkNotificationsCommentedEnabled=" + chatworkNotificationsCommentedEnabled +
                ", chatworkNotificationsEnabledForPush=" + chatworkNotificationsEnabledForPush +
                ", chatworkNotificationsEnabledForPersonal=" + chatworkNotificationsEnabledForPersonal +
                ", notificationLevel=" + notificationLevel +
                ", notificationPrLevel=" + notificationPrLevel +
                ", chatworkChannelName=" + chatworkChannelName +
                ", chatworkWebHookUrl=" + chatworkWebHookUrl +
                ", chatworkUsername=" + chatworkUsername +
                ", chatworkIconUrl=" + chatworkIconUrl +
                ", chatworkIconEmoji=" + chatworkIconEmoji +
                "}";
    }

}
