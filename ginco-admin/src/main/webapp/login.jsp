<!DOCTYPE html>
<html lang="fr-FR">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>GINCO LOGIN</title>
    <script src="extjs/${ginco.ext.js.file}"></script>
    <link rel="stylesheet" href="extjs/resources/css/ext-all.css">
    <link rel="stylesheet" href="css/ginco.css">
    <link rel="stylesheet" href="css/ux/CheckHeader.css">
    <link rel="icon" href="images/ginco-favicon.ico">
    <script type="text/javascript" src="ext-locale-loader.js"></script>
    <script type="text/javascript" src="ext-accessibility.js"></script>
    <script type="text/javascript" src="ext-custom-comp.js"></script>
    <script type="text/javascript" src="login.js"></script>
    <script type="text/javascript" src="extjs/locale/ext-lang-fr.js"></script>
    <% if (session!=null && session.getAttribute("SPRING_SECURITY_LAST_EXCEPTION") instanceof org.springframework.security.core.AuthenticationException) { %>
    <script type="text/javascript">window.authenticationException=true;</script>
    <% } %>
    <% if (session!=null && session.getAttribute("SPRING_SECURITY_LAST_EXCEPTION") instanceof org.springframework.security.authentication.LockedException) { %>
    <script type="text/javascript">window.accountLocked=true;</script>
    <% } %>
</head>
<body>
<!-- OWASP CSRFGuard JavaScript Support -->
<script src="./JavaScriptServlet"></script>
</body>
</html>