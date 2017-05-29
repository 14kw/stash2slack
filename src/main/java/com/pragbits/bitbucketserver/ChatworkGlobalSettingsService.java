package com.pragbits.bitbucketserver;

public interface ChatworkGlobalSettingsService {

    // hook and channel name
    String getWebHookUrl();
    void setWebHookUrl(String value);

    String getChannelName();
    void setChannelName(String value);

    // pull requests are enabled and pr events
    boolean getChatworkNotificationsEnabled();
    void setChatworkNotificationsEnabled(boolean value);

    boolean getChatworkNotificationsOpenedEnabled();
    void setChatworkNotificationsOpenedEnabled(boolean value);

    boolean getChatworkNotificationsReopenedEnabled();
    void setChatworkNotificationsReopenedEnabled(boolean value);

    boolean getChatworkNotificationsUpdatedEnabled();
    void setChatworkNotificationsUpdatedEnabled(boolean value);

    boolean getChatworkNotificationsApprovedEnabled();
    void setChatworkNotificationsApprovedEnabled(boolean value);

    boolean getChatworkNotificationsUnapprovedEnabled();
    void setChatworkNotificationsUnapprovedEnabled(boolean value);

    boolean getChatworkNotificationsDeclinedEnabled();
    void setChatworkNotificationsDeclinedEnabled(boolean value);

    boolean getChatworkNotificationsMergedEnabled();
    void setChatworkNotificationsMergedEnabled(boolean value);

    boolean getChatworkNotificationsCommentedEnabled();
    void setChatworkNotificationsCommentedEnabled(boolean value);

    // push notifications are enabled and push options
    boolean getChatworkNotificationsEnabledForPush();
    void setChatworkNotificationsEnabledForPush(boolean value);

    NotificationLevel getNotificationLevel();
    void setNotificationLevel(String value);

    NotificationLevel getNotificationPrLevel();
    void setNotificationPrLevel(String value);

    boolean getChatworkNotificationsEnabledForPersonal();
    void setChatworkNotificationsEnabledForPersonal(boolean value);

    String getUsername();
    void setUsername(String value);

    String getIconUrl();
    void setIconUrl(String value);

    String getIconEmoji();
    void setIconEmoji(String value);

}
