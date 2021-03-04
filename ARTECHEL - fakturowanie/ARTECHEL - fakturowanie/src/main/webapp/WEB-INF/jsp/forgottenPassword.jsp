<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--
  Created by IntelliJ IDEA.
  User: Mythquiel
  Date: 23.10.2019
  Time: 18:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <title>ARTECHEL - logowanie</title>
    <link href="${pageContext.request.contextPath}/resources/loginPage.css" rel="stylesheet"/>
</head>
<body>
<div class="login">
    <p id="message">${message}</p>
    <form action="remindPassword" method="post" id="panel">
        <input type="text" name="username" placeholder="Nazwa użytkownika" id="userInput"/>
            <input type="text" placeholder="Adres e-mail" name="email" id="passwordInput" />
        <input type="submit" value="Zresetuj hasło" id="resetButton">
        <p class="forgottenPass"><a href="${pageContext.request.contextPath}/">Logowanie</a></p>
    </form>
</div>
</body>
</html>