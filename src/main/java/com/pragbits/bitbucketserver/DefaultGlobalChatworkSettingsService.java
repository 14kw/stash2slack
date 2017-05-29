package com.pragbits.bitbucketserver;


import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.google.common.base.Strings;

public class DefaultGlobalChatworkSettingsService implements ChatworkGlobalSettingsService {
    private static final String KEY_GLOBAL_SETTING_HOOK_URL = "stash2chatwork.globalsettings.hookurl";
    private static final String KEY_GLOBAL_SETTING_CHANNEL_NAME = "stash2chatwork.globalsettings.channelname";
    private static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_ENABLED = "stash2chatwork.globalsettings.chatworknotificationsenabled";
    private static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_OPENED_ENABLED = "stash2chatwork.globalsettings.chatworknotificationsopenedenabled";
    private static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_REOPENED_ENABLED = "stash2chatwork.globalsettings.chatworknotificationsreopenedenabled";
    private static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_UPDATED_ENABLED = "stash2chatwork.globalsettings.chatworknotificationsupdatedenabled";
    private static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_APPROVED_ENABLED = "stash2chatwork.globalsettings.chatworknotificationsapprovedenabled";
    private static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_UNAPPROVED_ENABLED = "stash2chatwork.globalsettings.chatworknotificationsunapprovedenabled";
    private static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_DECLINED_ENABLED = "stash2chatwork.globalsettings.chatworknotificationsdeclinedenabled";
    private static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_MERGED_ENABLED = "stash2chatwork.globalsettings.chatworknotificationsmergedenabled";
    private static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_COMMENTED_ENABLED = "stash2chatwork.globalsettings.chatworknotificationscommentedenabled";
    private static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_LEVEL = "stash2chatwork.globalsettings.chatworknotificationslevel";
    private static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_PR_LEVEL = "stash2chatwork.globalsettings.chatworknotificationsprlevel";
    private static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_PUSH_ENABLED = "stash2chatwork.globalsettings.chatworknotificationspushenabled";
    private static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_PERSONAL_ENABLED = "stash2chatwork.globalsettings.chatworknotificationspersonalenabled";
    private static final String KEY_GLOBAL_SETTING_USER_NAME = "stash2chatwork.globalsettings.username";
    private static final String KEY_GLOBAL_SETTING_ICON_URL = "stash2chatwork.globalsettings.iconurl";
    private static final String KEY_GLOBAL_SETTING_ICON_EMOJI = "stash2chatwork.globalsettings.iconemojil";

    private final PluginSettings pluginSettings;

    public DefaultGlobalChatworkSettingsService(PluginSettingsFactory pluginSettingsFactory) {
        this.pluginSettings = pluginSettingsFactory.createGlobalSettings();
    }

    @Override
    public String getWebHookUrl() {
        return getString(KEY_GLOBAL_SETTING_HOOK_URL);
    }

    @Override
    public void setWebHookUrl(String value) {
        if (!Strings.isNullOrEmpty(value)) {
            pluginSettings.put(KEY_GLOBAL_SETTING_HOOK_URL, value);
        } else {
            pluginSettings.put(KEY_GLOBAL_SETTING_HOOK_URL, null);
        }
    }

    @Override
    public String getChannelName() {
        return getString(KEY_GLOBAL_SETTING_CHANNEL_NAME);
    }

    @Override
    public void setChannelName(String value) {
        if (!Strings.isNullOrEmpty(value)) {
            pluginSettings.put(KEY_GLOBAL_SETTING_CHANNEL_NAME, value);
        } else {
            pluginSettings.put(KEY_GLOBAL_SETTING_CHANNEL_NAME, null);
        }
    }

    @Override
    public boolean getChatworkNotificationsEnabled() {
        return getBoolean(KEY_GLOBAL_SETTING_NOTIFICATIONS_ENABLED);
    }

    @Override
    public void setChatworkNotificationsEnabled(boolean value) {
        setBoolean(KEY_GLOBAL_SETTING_NOTIFICATIONS_ENABLED, value);
    }

    @Override
    public boolean getChatworkNotificationsOpenedEnabled() {
        return getBoolean(KEY_GLOBAL_SETTING_NOTIFICATIONS_OPENED_ENABLED);
    }

    @Override
    public void setChatworkNotificationsOpenedEnabled(boolean value) {
        setBoolean(KEY_GLOBAL_SETTING_NOTIFICATIONS_OPENED_ENABLED, value);
    }

    @Override
    public boolean getChatworkNotificationsReopenedEnabled() {
        return getBoolean(KEY_GLOBAL_SETTING_NOTIFICATIONS_REOPENED_ENABLED);
    }

    @Override
    public void setChatworkNotificationsReopenedEnabled(boolean value) {
        setBoolean(KEY_GLOBAL_SETTING_NOTIFICATIONS_REOPENED_ENABLED, value);
    }

    @Override
    public boolean getChatworkNotificationsUpdatedEnabled() {
        return getBoolean(KEY_GLOBAL_SETTING_NOTIFICATIONS_UPDATED_ENABLED);
    }

    @Override
    public void setChatworkNotificationsUpdatedEnabled(boolean value) {
        setBoolean(KEY_GLOBAL_SETTING_NOTIFICATIONS_UPDATED_ENABLED, value);
    }

    @Override
    public boolean getChatworkNotificationsApprovedEnabled() {
        return getBoolean(KEY_GLOBAL_SETTING_NOTIFICATIONS_APPROVED_ENABLED);
    }

    @Override
    public void setChatworkNotificationsApprovedEnabled(boolean value) {
        setBoolean(KEY_GLOBAL_SETTING_NOTIFICATIONS_APPROVED_ENABLED, value);
    }

    @Override
    public boolean getChatworkNotificationsUnapprovedEnabled() {
        return getBoolean(KEY_GLOBAL_SETTING_NOTIFICATIONS_UNAPPROVED_ENABLED);
    }

    @Override
    public void setChatworkNotificationsUnapprovedEnabled(boolean value) {
        setBoolean(KEY_GLOBAL_SETTING_NOTIFICATIONS_UNAPPROVED_ENABLED, value);
    }

    @Override
    public boolean getChatworkNotificationsDeclinedEnabled() {
        return getBoolean(KEY_GLOBAL_SETTING_NOTIFICATIONS_DECLINED_ENABLED);
    }

    @Override
    public void setChatworkNotificationsDeclinedEnabled(boolean value) {
        setBoolean(KEY_GLOBAL_SETTING_NOTIFICATIONS_DECLINED_ENABLED, value);
    }

    @Override
    public boolean getChatworkNotificationsMergedEnabled() {
        return getBoolean(KEY_GLOBAL_SETTING_NOTIFICATIONS_MERGED_ENABLED);
    }

    @Override
    public void setChatworkNotificationsMergedEnabled(boolean value) {
        setBoolean(KEY_GLOBAL_SETTING_NOTIFICATIONS_MERGED_ENABLED, value);
    }

    @Override
    public boolean getChatworkNotificationsCommentedEnabled() {
        return getBoolean(KEY_GLOBAL_SETTING_NOTIFICATIONS_COMMENTED_ENABLED);
    }

    @Override
    public void setChatworkNotificationsCommentedEnabled(boolean value) {
        setBoolean(KEY_GLOBAL_SETTING_NOTIFICATIONS_COMMENTED_ENABLED, value);
    }

    @Override
    public boolean getChatworkNotificationsEnabledForPush() {
        return getBoolean(KEY_GLOBAL_SETTING_NOTIFICATIONS_PUSH_ENABLED);
    }

    @Override
    public void setChatworkNotificationsEnabledForPush(boolean value) {
        setBoolean(KEY_GLOBAL_SETTING_NOTIFICATIONS_PUSH_ENABLED, value);
    }

    @Override
    public boolean getChatworkNotificationsEnabledForPersonal() {
        return getBoolean(KEY_GLOBAL_SETTING_NOTIFICATIONS_PERSONAL_ENABLED);
    }

    @Override
    public void setChatworkNotificationsEnabledForPersonal(boolean value) {
        setBoolean(KEY_GLOBAL_SETTING_NOTIFICATIONS_PERSONAL_ENABLED, value);
    }

    @Override
    public NotificationLevel getNotificationLevel() {
        String value = getString(KEY_GLOBAL_SETTING_NOTIFICATIONS_LEVEL);
        if (value.isEmpty()) {
            return NotificationLevel.VERBOSE;
        } else {
            return NotificationLevel.valueOf(value);
        }
    }

    @Override
    public void setNotificationLevel(String value) {
        if (!Strings.isNullOrEmpty(value)) {
            pluginSettings.put(KEY_GLOBAL_SETTING_NOTIFICATIONS_LEVEL, value);
        } else {
            pluginSettings.put(KEY_GLOBAL_SETTING_NOTIFICATIONS_LEVEL, null);
        }
    }

    @Override
    public NotificationLevel getNotificationPrLevel() {
        String value = getString(KEY_GLOBAL_SETTING_NOTIFICATIONS_PR_LEVEL);
        if (value.isEmpty()) {
            return NotificationLevel.VERBOSE;
        } else {
            return NotificationLevel.valueOf(value);
        }
    }

    @Override
    public void setNotificationPrLevel(String value) {
        if (!Strings.isNullOrEmpty(value)) {
            pluginSettings.put(KEY_GLOBAL_SETTING_NOTIFICATIONS_PR_LEVEL, value);
        } else {
            pluginSettings.put(KEY_GLOBAL_SETTING_NOTIFICATIONS_PR_LEVEL, null);
        }
    }

    @Override
    public String getUsername() {
        String userName = getString(KEY_GLOBAL_SETTING_USER_NAME);
        if (null == userName) {
            return "Stash";
        }
        return userName.toString();
    }

    @Override
    public void setUsername(String value) {
        if (!Strings.isNullOrEmpty(value)) {
            pluginSettings.put(KEY_GLOBAL_SETTING_USER_NAME, value);
        } else {
            pluginSettings.put(KEY_GLOBAL_SETTING_USER_NAME, null);
        }
    }

    @Override
    public String getIconUrl() {
        return getString(KEY_GLOBAL_SETTING_ICON_URL);
    }

    @Override
    public void setIconUrl(String value) {
        if (!Strings.isNullOrEmpty(value)) {
            pluginSettings.put(KEY_GLOBAL_SETTING_ICON_URL, value);
        } else {
            pluginSettings.put(KEY_GLOBAL_SETTING_ICON_URL, null);
        }
    }

    @Override
    public String getIconEmoji() {
        return getString(KEY_GLOBAL_SETTING_ICON_EMOJI);
    }

    @Override
    public void setIconEmoji(String value) {
        if (!Strings.isNullOrEmpty(value)) {
            pluginSettings.put(KEY_GLOBAL_SETTING_ICON_EMOJI, value);
        } else {
            pluginSettings.put(KEY_GLOBAL_SETTING_ICON_EMOJI, null);
        }
    }

    private String getString(String key) {
        Object value = pluginSettings.get(key);
        return null == value ? "" : value.toString();
    }

    private boolean getBoolean(String key) {
        return Boolean.parseBoolean((String)pluginSettings.get(key));
    }

    private void setBoolean(String key, Boolean value) {
        pluginSettings.put(key, value.toString());
    }
}