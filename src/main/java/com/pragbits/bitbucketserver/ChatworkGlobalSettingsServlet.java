package com.pragbits.bitbucketserver;

import com.atlassian.soy.renderer.SoyException;
import com.atlassian.soy.renderer.SoyTemplateRenderer;
import com.atlassian.bitbucket.AuthorisationException;
import com.atlassian.bitbucket.permission.Permission;
import com.atlassian.bitbucket.permission.PermissionValidationService;
import com.atlassian.webresource.api.assembler.PageBuilderService;
import com.atlassian.bitbucket.i18n.I18nService;
import com.google.common.collect.ImmutableMap;
import com.pragbits.bitbucketserver.soy.SelectFieldOptions;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class ChatworkGlobalSettingsServlet extends HttpServlet {
    private final PageBuilderService pageBuilderService;
    private final ChatworkGlobalSettingsService chatworkGlobalSettingsService;
    private final SoyTemplateRenderer soyTemplateRenderer;
    private final PermissionValidationService validationService;
    private final I18nService i18nService;

    public ChatworkGlobalSettingsServlet(PageBuilderService pageBuilderService,
                                      ChatworkGlobalSettingsService chatworkGlobalSettingsService,
                                      SoyTemplateRenderer soyTemplateRenderer,
                                      PermissionValidationService validationService,
                                      I18nService i18nService) {
        this.pageBuilderService = pageBuilderService;
        this.chatworkGlobalSettingsService = chatworkGlobalSettingsService;
        this.soyTemplateRenderer = soyTemplateRenderer;
        this.validationService = validationService;
        this.i18nService = i18nService;

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        try {
            validationService.validateForGlobal(Permission.SYS_ADMIN);
        } catch (AuthorisationException e) {
            // Skip form processing
            doGet(req, res);
            return;
        }

        chatworkGlobalSettingsService.setWebHookUrl(req.getParameter("chatworkGlobalWebHookUrl").trim());
        chatworkGlobalSettingsService.setChannelName(req.getParameter("chatworkChannelName"));
        chatworkGlobalSettingsService.setUsername(req.getParameter("chatworkUsername"));
        chatworkGlobalSettingsService.setIconUrl(req.getParameter("chatworkIconUrl"));
        chatworkGlobalSettingsService.setIconEmoji(req.getParameter("chatworkIconEmoji"));

        chatworkGlobalSettingsService.setChatworkNotificationsEnabled(bool(req, "chatworkNotificationsEnabled"));
        chatworkGlobalSettingsService.setChatworkNotificationsOpenedEnabled(bool(req, "chatworkNotificationsOpenedEnabled"));
        chatworkGlobalSettingsService.setChatworkNotificationsReopenedEnabled(bool(req, "chatworkNotificationsReopenedEnabled"));
        chatworkGlobalSettingsService.setChatworkNotificationsUpdatedEnabled(bool(req, "chatworkNotificationsUpdatedEnabled"));
        chatworkGlobalSettingsService.setChatworkNotificationsApprovedEnabled(bool(req, "chatworkNotificationsApprovedEnabled"));
        chatworkGlobalSettingsService.setChatworkNotificationsUnapprovedEnabled(bool(req, "chatworkNotificationsUnapprovedEnabled"));
        chatworkGlobalSettingsService.setChatworkNotificationsDeclinedEnabled(bool(req, "chatworkNotificationsDeclinedEnabled"));
        chatworkGlobalSettingsService.setChatworkNotificationsMergedEnabled(bool(req, "chatworkNotificationsMergedEnabled"));
        chatworkGlobalSettingsService.setChatworkNotificationsCommentedEnabled(bool(req, "chatworkNotificationsCommentedEnabled"));

        NotificationLevel notificationLevel = NotificationLevel.VERBOSE;
        if (null != req.getParameter("chatworkNotificationLevel")) {
            notificationLevel = NotificationLevel.valueOf(req.getParameter("chatworkNotificationLevel"));
        }
        chatworkGlobalSettingsService.setNotificationLevel(notificationLevel.toString());

        NotificationLevel notificationPrLevel = NotificationLevel.VERBOSE;
        if (null != req.getParameter("chatworkNotificationPrLevel")) {
            notificationPrLevel = NotificationLevel.valueOf(req.getParameter("chatworkNotificationPrLevel"));
        }
        chatworkGlobalSettingsService.setNotificationPrLevel(notificationPrLevel.toString());

        chatworkGlobalSettingsService.setChatworkNotificationsEnabledForPush(bool(req, "chatworkNotificationsEnabledForPush"));
        chatworkGlobalSettingsService.setChatworkNotificationsEnabledForPersonal(bool(req, "chatworkNotificationsEnabledForPersonal"));

        doGet(req, res);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doView(response);

    }

    private void doView(HttpServletResponse response)
            throws ServletException, IOException {

        validationService.validateForGlobal(Permission.ADMIN);

        String webHookUrl = chatworkGlobalSettingsService.getWebHookUrl();
        String channelName = chatworkGlobalSettingsService.getChannelName();
        String userName = chatworkGlobalSettingsService.getUsername();
        String iconUrl = chatworkGlobalSettingsService.getIconUrl();
        String iconEmoji = chatworkGlobalSettingsService.getIconEmoji();
        Boolean chatworkNotificationsEnabled = chatworkGlobalSettingsService.getChatworkNotificationsEnabled();
        Boolean chatworkNotificationsOpenedEnabled = chatworkGlobalSettingsService.getChatworkNotificationsOpenedEnabled();
        Boolean chatworkNotificationsReopenedEnabled = chatworkGlobalSettingsService.getChatworkNotificationsReopenedEnabled();
        Boolean chatworkNotificationsUpdatedEnabled = chatworkGlobalSettingsService.getChatworkNotificationsUpdatedEnabled();
        Boolean chatworkNotificationsApprovedEnabled = chatworkGlobalSettingsService.getChatworkNotificationsApprovedEnabled();
        Boolean chatworkNotificationsUnapprovedEnabled = chatworkGlobalSettingsService.getChatworkNotificationsUnapprovedEnabled();
        Boolean chatworkNotificationsDeclinedEnabled = chatworkGlobalSettingsService.getChatworkNotificationsDeclinedEnabled();
        Boolean chatworkNotificationsMergedEnabled = chatworkGlobalSettingsService.getChatworkNotificationsMergedEnabled();
        Boolean chatworkNotificationsCommentedEnabled = chatworkGlobalSettingsService.getChatworkNotificationsCommentedEnabled();
        Boolean chatworkNotificationsEnabledForPush = chatworkGlobalSettingsService.getChatworkNotificationsEnabledForPush();
        Boolean chatworkNotificationsEnabledForPersonal = chatworkGlobalSettingsService.getChatworkNotificationsEnabledForPersonal();
        String notificationLevel = chatworkGlobalSettingsService.getNotificationLevel().toString();
        String notificationPrLevel = chatworkGlobalSettingsService.getNotificationPrLevel().toString();

        render(response,
                "bitbucketserver.page.chatwork.global.settings.viewGlobalChatworkSettings",
                ImmutableMap.<String, Object>builder()
                        .put("chatworkGlobalWebHookUrl", webHookUrl)
                        .put("chatworkChannelName", channelName)
                        .put("chatworkNotificationsEnabled", chatworkNotificationsEnabled)
                        .put("chatworkNotificationsOpenedEnabled", chatworkNotificationsOpenedEnabled)
                        .put("chatworkNotificationsReopenedEnabled", chatworkNotificationsReopenedEnabled)
                        .put("chatworkNotificationsUpdatedEnabled", chatworkNotificationsUpdatedEnabled)
                        .put("chatworkNotificationsApprovedEnabled", chatworkNotificationsApprovedEnabled)
                        .put("chatworkNotificationsUnapprovedEnabled", chatworkNotificationsUnapprovedEnabled)
                        .put("chatworkNotificationsDeclinedEnabled", chatworkNotificationsDeclinedEnabled)
                        .put("chatworkNotificationsMergedEnabled", chatworkNotificationsMergedEnabled)
                        .put("chatworkNotificationsCommentedEnabled", chatworkNotificationsCommentedEnabled)
                        .put("chatworkNotificationsEnabledForPush", chatworkNotificationsEnabledForPush)
                        .put("chatworkNotificationsEnabledForPersonal", chatworkNotificationsEnabledForPersonal)
                        .put("notificationLevel", notificationLevel)
                        .put("notificationPrLevel", notificationPrLevel)
                        .put("notificationLevels", new SelectFieldOptions(NotificationLevel.values()).toSoyStructure())
                        .put("chatworkUsername", userName)
                        .put("chatworkIconUrl", iconUrl)
                        .put("chatworkIconEmoji", iconEmoji)
                        .build()
        );
    }

    private void render(HttpServletResponse response, String templateName, Map<String, Object> data)
            throws IOException, ServletException {
        pageBuilderService.assembler().resources().requireContext("plugin.adminpage.chatwork");
        response.setContentType("text/html;charset=UTF-8");
        try {
            soyTemplateRenderer.render(response.getWriter(), PluginMetadata.getCompleteModuleKey("soy-templates"), templateName, data);
        } catch (SoyException e) {
            Throwable cause = e.getCause();
            if (cause instanceof IOException) {
                throw (IOException) cause;
            }
            throw new ServletException(e);
        }
    }

    private boolean bool(HttpServletRequest req, String name) {
        return "on".equals(req.getParameter(name));
    }
}
