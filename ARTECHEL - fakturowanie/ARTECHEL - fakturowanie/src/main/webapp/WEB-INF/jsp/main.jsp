<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
                                <td class="menuSelectedButton">
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
                                    <form method="GET">
                                        <table class="searchCriteria">
                                            <tr>
                                                <td style="width: 20%">
                                                    <br>
                                                    <label for="numer">Numer faktury: </label>
                                                    <input type="text" id="numer" name="numer" class="inputSearch"/>
                                                </td>
                                                <td style="width: 25%">
                                                    <br>
                                                    <label for="dataAfter">Data utworzenia po: </label>
                                                    <input type="date" id="dataAfter" name="dataAfter"
                                                           class="inputSearch"/>
                                                </td>
                                                <td style="width: 25%">
                                                    <br>
                                                    <label for="dataB4">Data utworzenia przed: </label>
                                                    <input type="date" id="dataB4" name="dataB4" class="inputSearch"/>
                                                </td>
                                                <td style="width: 20%">
                                                    <br>
                                                    <label for="kontrahent">Kontrahent: </label>
                                                    <input type="text" id="kontrahent" name="kontrahent"
                                                           class="inputSearch"/>
                                                </td>
                                                <td style="width: 10%">
                                                    <br>
                                                    <input type="submit" value="Szukaj" id="submitButton" formaction="searchInvoices">

                                                </td>
                                                <td style="width: 10%">
                                                    <br>
                                                    <input type="submit" value="  Zestawienie faktur  " id="submitButton2" formaction="generateReport" formtarget="_blank">
                                                </td>
                                            </tr>
                                        </table>
                                    </form>
                                </td>
                            </tr>
                        </table>
                        <form method="POST">
                            <table class=invoicesTab style="width: 95%">
                                <tr>
                                    <th style="width: 12%" class="invoiceTabColumn">Numer faktury</th>
                                    <th style="width: 32%" class="invoiceTabColumn">Nabywca</th>
                                    <th style="width: 32%" class="invoiceTabColumn">Obiorca</th>
                                    <th style="width: 12%" class="invoiceTabColumn">Data wystawienia</th>
                                    <th style="width: 12%" class="invoiceTabColumn">Wartość brutto</th>
                                    <th style="width: 12%" class="invoiceTabColumn">Korekta</th>
                                </tr>
                                <c:forEach var="i" items="${finalList}" varStatus="status">
                                    <tr class="tableEntries" onmouseover="this.className='selectedRow'"
                                        onmouseout="this.className='tableEntries'">
                                        <td class="tableData">
                                            <input name="invoiceNumber" id="invoiceNumber" class="buttns" type="submit"
                                                   formaction="openInEdit"
                                                   value="${i.invoice_number}" readonly>
                                        </td>
                                        <td class="tableData">
                                                ${i.customer.name}
                                        </td>
                                        <td class="tableData">
                                                ${i.payer.name}</td>
                                        <td class="tableData">
                                                ${i.date_created}</td>
                                        <td class="tableData">
                                                ${i.value_brutto}</td>
                                        <td class="tableData" >
                                            <form id="newForm" method="post">
                                            <c:if test="${fn:contains(i.invoice_number,'/20')}">
                                                <input type="hidden" name="number"  id="number"  value="${i.invoice_number}">
                                                <input name="correct" id="correctBtn" class="buttns" type="submit"
                                                       value="Wystaw korektę" formaction="openInCorrect" readonly>
                                            </c:if>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
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
</body>
</html>