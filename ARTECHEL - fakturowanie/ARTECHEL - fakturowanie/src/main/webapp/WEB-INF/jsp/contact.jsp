<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>ARTECHEL</title>
    <link href="${pageContext.request.contextPath}/resources/browserStyle.css" rel="stylesheet"/>

</head>
<body>
<table style="width: 100%">
    <tbody>
    <tr>
        <td>
            <table style="width: 100%" class="topPanel">
                <tbody>
                <tr>
                    <td style="width: 80%">
                        <label id="companyName">Artykuły Techniczne Motoryzacyjne i Elektryczne<br>Dorota Świtała
                        </label>
                    </td>
                </tr>
                </tbody>
            </table>
        </td>
    </tr>
    <tr>
        <td>
            <table style="width: 100%" class="mainPanel">
                <tbody>
                <tr>
                    <td class="menuPanel">
                        <table class="menuPanel">
                            <tr>
                                <td class="menuButton">
                                    <a href="${pageContext.request.contextPath}/main">
                                        <img class="menuImage"
                                             src="${pageContext.request.contextPath}/resources/img/home.png"
                                             alt="Faktury">
                                        <br>
                                        Faktury</a>
                                    <br>
                                    <br>
                                </td>
                            </tr>
                            <tr>
                                <td class="menuButton">
                                    <a href="${pageContext.request.contextPath}/newInvoice">
                                        <img class="menuImage"
                                             src="${pageContext.request.contextPath}/resources/img/plus.png"
                                             alt="Nowa faktura">
                                        <br>
                                        Nowa Faktura</a>
                                    <br>
                                    <br>
                                </td>
                            </tr>
                            <tr>
                                <td class="menuButton">
                                    <a href="${pageContext.request.contextPath}/customers">
                                        <img class="menuImage"
                                             src="${pageContext.request.contextPath}/resources/img/customer.png"
                                             alt="Kontrahenci">
                                        <br>
                                        Kontrahenci
                                        <br>
                                        <br>
                                    </a>
                                </td>
                            </tr>
                            <tr>
                                <td class="menuSelectedButton">
                                    <a href="${pageContext.request.contextPath}/contact">
                                        <img class="menuImage"
                                             src="${pageContext.request.contextPath}/resources/img/contact.png"
                                             alt="Kontakt">
                                        <br>
                                        Kontakt
                                        <br>
                                        <br>
                                    </a>
                                </td>
                            </tr>
                            <tr>
                                <td class="menuButton">
                                    <a href="<c:url value="/logout" />">
                                        <img class="menuImage"
                                             src="${pageContext.request.contextPath}/resources/img/logout.png"
                                             alt="Wyloguj">
                                        <br>
                                        Wyloguj
                                        <br>
                                        <br>
                                    </a>
                                </td>
                            </tr>
                        </table>
                    </td>
                    <td class="invoicesTab">
                        <table class="contactTab">
                            <tbody>
                            <tr>
                                <td>
                                    <h2 style="text-align: center"> POMOC TECHNICZNA</h2>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <form action="sendMail" method="post" id="mailForm">
                                        <table class="emailTab">
                                            <tbody>
                                            <tr>
                                                <td>
                                            <p id="message">${message}</p>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td rowspan="3">
                                                    <textarea form="mailForm" class="emailBody" id="emailBody"
                                                              name="body"
                                                              placeholder="Wpisz treść wiadomości"></textarea>
                                                </td>
                                                <td>
                                                    <input type="text" placeholder="Temat" class="emailData"
                                                           id="emailTitle" name="header">
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    <input type="text" placeholder="Imię" class="emailData"
                                                           id="emailSender" name="name">
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    <input type="submit" value="Wyślij" id="sendEmailButton">
                                                </td>
                                            </tr>
                                            </tbody>
                                        </table>
                                    </form>
                                </td>
                            </tr>
                            </tbody>
                        </table>

                    </td>
                </tr>
                </tbody>
            </table>
        </td>
    </tr>
    </tbody>
</table>
</body>
</html>