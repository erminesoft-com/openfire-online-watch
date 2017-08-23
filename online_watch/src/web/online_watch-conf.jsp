<%@ page
        import="com.erminesoft.OnlineWatch,
                org.jivesoftware.openfire.XMPPServer,
                org.jivesoftware.openfire.container.PluginManager"
        errorPage="error.jsp" %>
<%@ page import="org.jivesoftware.util.ParamUtils" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%-- Define Administration Bean --%>
<jsp:useBean id="admin" class="org.jivesoftware.util.WebManager"/>
<c:set var="admin" value="${admin.manager}"/>
<%
    admin.init(request, response, session, application, out);
%>

<%
    // Get parameters
    boolean save = request.getParameter("save") != null;

    String cache_url = ParamUtils.getParameter(request, "cache_url");
    String cache_port = ParamUtils.getParameter(request, "cache_port");
    String cache_namespace = ParamUtils.getParameter(request, "cache_namespace");

    final PluginManager pluginManager = XMPPServer.getInstance().getPluginManager();
    final OnlineWatch pluginInstance = (OnlineWatch) pluginManager.getPlugin("online_watch");


    // Handle a save
    if (save) {

        boolean is2Reload = true;
        pluginInstance.setDatasourceUrl(cache_url);
        pluginInstance.setDatasourcePort(cache_port);
        pluginInstance.setCacheNamespace(cache_namespace);

//            if (is2Reload) {
//                String pluginDir = pluginManager.getPluginDirectory(pluginInstance).getName();
//                pluginManager.unloadPlugin(pluginDir);
//
//                response.sendRedirect("/plugin-admin.jsp?reloadsuccess=true");
//            }
        response.sendRedirect("online_watch-conf.jsp?success=true");
        return;
    }

    cache_url = pluginInstance.getDatasourceUrl();
    cache_port = pluginInstance.getDatasourcePort();
    cache_namespace = pluginInstance.getCacheNamespace();
%>

<html>
<head>
    <title>Online watch plugin settings</title>
    <meta name="pageID" content="online_watch-conf"/>
</head>
<body>

<p>Use settings below to connect to Redis. <b>(Important!) After settings were saved please reload plugin
    manually</b></p>
<p></p>

<form action="online_watch-conf.jsp?save" method="post">

    <fieldset>
        <legend>Settings :</legend>
        <div>
            <ul>
                <label style="padding-left: 25px" for="text_cache_url">URL
                    key:</label>
                <input type="text" name="cache_url" value="<%=cache_url%>"
                       id="text_cache_url">
                <br>
                <label style="padding-left: 25px" for="text_cache_port">Port
                    key:</label>
                <input type="text" name="cache_port" value="<%=cache_port%>"
                       id="text_cache_port">
                <br>
                <label style="padding-left: 25px" for="text_cache_namespace">Namespace
                    key:</label>
                <input type="text" name="cache_namespace" value="<%=cache_namespace%>"
                       id="text_cache_namespace">
            </ul>
            <p>You can find here detailed documentation over the plugin :
                <a
                        href="/plugin-admin.jsp?plugin=online_watch&showReadme=true&decorator=none">Online Watch service
                    ReadMe</a>
            </p>
        </div>
    </fieldset>

    <br> <br> <input type="submit" value="Save Settings">

</form>


</body>
</html>