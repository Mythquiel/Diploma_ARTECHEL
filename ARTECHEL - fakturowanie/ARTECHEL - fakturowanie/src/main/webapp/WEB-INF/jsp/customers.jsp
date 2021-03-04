<%@ page contentType="text/html; charset=UTF-8" %>
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
                                <td class="menuSelectedButton">
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
                                <td class="menuButton">
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
                        <table class="searchInvoice">
                            <tr>
                                <td>
                                    <form action="searchCustomers" method="get">
                                        <table class="searchCriteria">
                                            <tr>
                                                <td style="width: 30%">
                                                    <label for="nazwa">Nazwa: </label>
                                                    <input type="text" id="nazwa" name="nazwa" class="inputSearch"/>
                                                </td>
                                                <td style="width: 30%">
                                                    <label for="nip">NIP: </label>
                                                    <input type="text" id="nip" name="nip" class="inputSearch"/>
                                                </td>
                                                <td style="width: 10%">
                                                    <input type="submit" value="Szukaj" id="submitButton" >
                                                </td>
                                            </tr>
                                        </table>
                                    </form>
                                </td>
                            </tr>
                        </table>
                        <table class=invoicesTab style="width: 95%">
                            <tr>
                                <th style="width: 26%" class="invoiceTabColumn">Nazwa</th>
                                <th style="width: 26%" class="invoiceTabColumn">Adres</th>
                                <th style="width: 12%" class="invoiceTabColumn">NIP</th>
                                <th style="width: 12%" class="invoiceTabColumn">Telefon</th>
                                <th style="width: 12%" class="invoiceTabColumn">Telefon komórkowy</th>
                                <th style="width: 12%" class="invoiceTabColumn">Adres e-mail</th>
                            </tr>
                            <c:forEach var="i" items="${finalList}" varStatus="status">
                                <tr class="tableEntries" onmouseover="this.className='selectedRow'" onmouseout="this.className='tableEntries'" onclick="editIvoice(number)">
                                    <td class="tableData">${i.name}</td>
                                    <td onclick="edytujFakture" class="tableData">${i.address_line_1} ${i.address_line_2}, ${i.postcode} ${address_line_3}</td>
                                    <td class="tableData">${i.nip}</td>
                                    <td class="tableData">${i.telephone}</td>
                                    <td class="tableData">${i.mobile}</td>
                                    <td class="tableData">${i.email_address}</td>
                                </tr>
                            </c:forEach>
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