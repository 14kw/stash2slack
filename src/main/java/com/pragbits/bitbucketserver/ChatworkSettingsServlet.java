package com.pragbits.bitbucketserver;

import com.atlassian.soy.renderer.SoyException;
import com.atlassian.soy.renderer.SoyTemplateRenderer;
import com.atlassian.bitbucket.AuthorisationException;
import com.atlassian.bitbucket.repository.Repository;
import com.atlassian.bitbucket.repository.RepositoryService;
import com.atlassian.bitbucket.permission.Permission;
import com.atlassian.bitbucket.permission.PermissionValidationService;
import com.atlassian.webresource.api.assembler.PageBuilderService;
import com.atlassian.bitbucket.i18n.I18nService;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.pragbits.bitbucketserver.soy.SelectFieldOptions;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class ChatworkSettingsServlet extends HttpServlet {
    private final PageBuilderService pageBuilderService;
    private final ChatworkSettingsService chatworkSettingsService;
    private final RepositoryService repositoryService;
    private final SoyTemplateRenderer soyTemplateRenderer;
    private final PermissionValidationService validationService;
    private final I18nService i18nService;
    
    private Repository repository = null;

    public ChatworkSettingsServlet(PageBuilderService pageBuilderService,
                                    ChatworkSettingsService chatworkSettingsService,
                                    RepositoryService repositoryService,
                                    SoyTemplateRenderer soyTemplateRenderer,
                                    PermissionValidationService validationService,
                                    I18nService i18nService) {
        this.pageBuilderService = pageBuilderService;
        this.chatworkSettingsService = chatworkSettingsService;
        this.repositoryService = repositoryService;
        this.soyTemplateRenderer = soyTemplateRenderer;
        this.validationService = validationService;
        this.i18nService = i18nService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {

        try {
            validationService.validateForRepository(this.repository, Permission.REPO_ADMIN);
        } catch (AuthorisationException e) {
            // Skip form processing
            doGet(req, res);
            return;
        }

        NotificationLevel notificationLevel = NotificationLevel.VERBOSE;
        if (null != req.getParameter("chatworkNotificationLevel")) {
            notificationLevel = NotificationLevel.valueOf(req.getParameter("chatworkNotificationLevel"));
        }

        NotificationLevel notificationPrLevel = NotificationLevel.VERBOSE;
        if (null != req.getParameter("chatworkNotificationPrLevel")) {
            notificationPrLevel = NotificationLevel.valueOf(req.getParameter("chatworkNotificationPrLevel"));
        }

        chatworkSettingsService.setChatworkSettings(
                repository,
                new ImmutableChatworkSettings(
                        "on".equals(req.getParameter("chatworkNotificationsOverrideEnabled")),
                        "on".equals(req.getParameter("chatworkNotificationsEnabled")),
                        "on".equals(req.getParameter("chatworkNotificationsOpenedEnabled")),
                        "on".equals(req.getParameter("chatworkNotificationsReopenedEnabled")),
                        "on".equals(req.getParameter("chatworkNotificationsUpdatedEnabled")),
                        "on".equals(req.getParameter("chatworkNotificationsApprovedEnabled")),
                        "on".equals(req.getParameter("chatworkNotificationsUnapprovedEnabled")),
                        "on".equals(req.getParameter("chatworkNotificationsDeclinedEnabled")),
                        "on".equals(req.getParameter("chatworkNotificationsMergedEnabled")),
                        "on".equals(req.getParameter("chatworkNotificationsCommentedEnabled")),
                        "on".equals(req.getParameter("chatworkNotificationsEnabledForPush")),
                        "on".equals(req.getParameter("chatworkNotificationsEnabledForPersonal")),
                        notificationLevel,
                        notificationPrLevel,
                        req.getParameter("chatworkChannelName"),
                        req.getParameter("chatworkWebHookUrl").trim(),
                        req.getParameter("chatworkUsername").trim(),
                        req.getParameter("chatworkIconUrl").trim(),
                        req.getParameter("chatworkIconEmoji").trim()
                )
        );

        doGet(req, res);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (Strings.isNullOrEmpty(pathInfo) || pathInfo.equals("/")) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        String[] pathParts = pathInfo.substring(1).split("/");
        if (pathParts.length != 4) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        String projectKey = pathParts[1];
        String repoSlug = pathParts[3];
        
        this.repository = repositoryService.getBySlug(projectKey, repoSlug);
        if (repository == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        doView(repository, response);

    }

    private void doView(Repository repository, HttpServletResponse response)
            throws ServletException, IOException {
        validationService.validateForRepository(repository, Permission.REPO_ADMIN);
        ChatworkSettings chatworkSettings = chatworkSettingsService.getChatworkSettings(repository);
        render(response,
                "bitbucketserver.page.chatwork.settings.viewChatworkSettings",
                ImmutableMap.<String, Object>builder()
                        .put("repository", repository)
                        .put("chatworkSettings", chatworkSettings)
                        .put("notificationLevels", new SelectFieldOptions(NotificationLevel.values()).toSoyStructure())
                        .build()
        );
    }

    private void render(HttpServletResponse response, String templateName, Map<String, Object> data)
            throws IOException, ServletException {
        pageBuilderService.assembler().resources().requireContext("plugin.page.chatwork");
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
}
