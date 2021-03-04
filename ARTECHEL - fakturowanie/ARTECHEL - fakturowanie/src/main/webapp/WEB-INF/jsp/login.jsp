<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <title>ARTECHEL - logowanie</title>
    <link href="${pageContext.request.contextPath}/resources/loginPage.css" rel="stylesheet"/>
</head>
<body>
<div class="login">
    <c:if test="${not empty SPRING_SECURITY_LAST_EXCEPTION}">
        <p id="message">Niepoprawna nazwa użytkownika bądź hasło.</p>
    </c:if>
    <form action="loginProcess" method="post" id="panel">
        <input type="text" name="username" placeholder="Nazwa użytkownika" id="userInput"/>
        <input type="password" placeholder="Hasło" name="password" id="passwordInput"/>
        <input type="submit" value="Zaloguj" id="submitButton">
        <p class="forgottenPass"><a href="${pageContext.request.contextPath}/forgottenPassword">Zapomniałem/am
            hasła.</a></p>
    </form>
</div>
</body>
</html>