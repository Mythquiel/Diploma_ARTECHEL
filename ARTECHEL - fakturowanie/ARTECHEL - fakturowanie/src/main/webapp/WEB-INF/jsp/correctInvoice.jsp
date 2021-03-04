<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>ARTECHEL</title>
    <link href="${pageContext.request.contextPath}/resources/invoices.css" rel="stylesheet"/>
    <script>
        function check() {
            var showAlert = 0;
            var odbiorca = document.getElementById("nip"),
                nazwa = document.getElementById("odbiorca"),
                ulica = document.getElementById("street"),
                kod = document.getElementById("postcode"),
                miejscowosc = document.getElementById("town"),
                forma = document.getElementById("payments");
            if ((odbiorca.value === "") || (forma.value === "") || (nazwa.value === "") || (ulica.value === "" || kod.value === "") || (miejscowosc.value === "")) {
                showAlert = 1;
            }
            var table = document.getElementById("dataTable");
            for (var i = 0; i < table.rows.length - 1; i++) {
                var nazwaP = document.getElementById("inputrow" + i + "1"),
                    jm = document.getElementById("inputrow" + i + "2"),
                    ilosc = document.getElementById("inputrow" + i + "3"),
                    vat = document.getElementById("inputrow" + i + "5"),
                    brutto = document.getElementById("inputrow" + i + "6");
                if ((nazwaP.value === "") || (jm.value === "") || (ilosc.value === "" || (vat.value === "") || (brutto.value === ""))) {
                    showAlert = 1;
                }
            }
            if (showAlert === 1) {
                alert("Uzupełnij wszystkie niezbędne dane, zanim wydrukujesz fakturę.");
                event.preventDefault();
            }
        }

        function copy() {
            var table = document.getElementById("dataTable");
            for (var i = 0; i < table.rows.length - 1; i++) {
                var src = document.getElementById("inputrow" + i + "6"),
                    dst = document.getElementById("inputrow" + i + "4"),
                    vat = document.getElementById("inputrow" + i + "5");
                var count = src.value * (1 - (vat.value / 100));
                dst.value = count.toFixed(2);
            }
        }

        function count() {
            var table = document.getElementById("dataTable");
            for (var i = 0; i < table.rows.length - 1; i++) {
                var quantity = document.getElementById("inputrow" + i + "3"),
                    nettoPrice = document.getElementById("inputrow" + i + "4"),
                    bruttoPrice = document.getElementById("inputrow" + i + "6"),
                    nettoValue = document.getElementById("inputrow" + i + "7"),
                    bruttoValue = document.getElementById("inputrow" + i + "8");

                var nettoTemp = nettoPrice.value * quantity.value;
                nettoValue.value = nettoTemp.toFixed(2);

                var bruttoTemp = bruttoPrice.value * quantity.value;
                bruttoValue.value = bruttoTemp.toFixed(2);
            }

        }

        function showCalendarOnStart() { //740
            var payment = document.getElementById("payments");
            var value = payment.value;
            var cell = document.getElementById("forma");
            //alert(cell.innerHTML.length);
            if (value === "przelew") {
                if (cell.innerHTML.length < 770) {
                    cell.innerHTML = cell.innerHTML + "<input type=\"date\" id=\"dueDate\" name=\"dueDate\" value=\"${invoice.payment_due}\" class=\"pay\" readonly>";
                    //alert(cell.innerHTML.length);
                }
                var payments = document.getElementById("payments");
                payments.value = "przelew";
            } else if (value === "gotówka") { //705
                if (cell.innerHTML.length === 847) {
                    cell.innerHTML = cell.innerHTML.substring(0, 769);
                    payments = document.getElementById("payments");
                    payments.value = "gotówka";
                }
            }
        }

        function sum() {
            var table = document.getElementById("dataTable"),
                sumNetto = Number(document.getElementById("nettoSuminv").value),
                sumBrutto = Number(document.getElementById("bruttoSuminv").value),
                tempNetto = 0,
                tempBrutto = 0,
                diffNetto,
                diffBrutto;
            for (var i = 0; i < table.rows.length - 1; i++) {
                var nettoValue = Number(document.getElementById("inputrow" + i + "7").value),
                    bruttoValue = Number(document.getElementById("inputrow" + i + "8").value);
                tempNetto = tempNetto + nettoValue;
                tempBrutto = tempBrutto + bruttoValue;
            }
            diffNetto = tempNetto - sumNetto;
            diffBrutto = tempBrutto - sumBrutto;
            var sumN = sumNetto + diffNetto;
            var sumNFixed = sumN.toFixed(2);
            var sumB = sumBrutto + diffBrutto;
            var sumBFixed = sumB.toFixed(2);
            var sumNettofield = document.getElementById("nettoSuminv");
            sumNettofield.value = sumNFixed;
            var sumBruttofield = document.getElementById("bruttoSuminv");
            sumBruttofield.value = sumBFixed;

        }

        function sumAtStart() {
            var table = document.getElementById("dataTable"),
                sumNetto = Number(document.getElementById("nettoSuminv").value),
                sumBrutto = Number(document.getElementById("bruttoSuminv").value),
                tempNetto = 0,
                tempBrutto = 0,
                diffNetto,
                diffBrutto;
            for (var i = 0; i < table.rows.length - 1; i++) {
                var nettoValue = Number(document.getElementById("inputrow" + i + "7").value),
                    bruttoValue = Number(document.getElementById("inputrow" + i + "8").value);
                tempNetto = tempNetto + nettoValue;
                tempBrutto = tempBrutto + bruttoValue;
            }
            diffNetto = tempNetto - sumNetto;
            diffBrutto = tempBrutto - sumBrutto;
            var sumN = sumNetto + diffNetto;
            var sumNFixed = sumN.toFixed(2);
            var sumB = sumBrutto + diffBrutto;
            var sumBFixed = sumB.toFixed(2);
            var sumNettofield = document.getElementById("nettoSuminv");
            sumNettofield.value = sumNFixed;
            var sumBruttofield = document.getElementById("bruttoSuminv");
            sumBruttofield.value = sumBFixed;

        }
    </script>


</head>
<body onload="sumAtStart(); showCalendarOnStart()">
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
                                <td class="menuSelectedButton">
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
                    <td class="mainTab">


                        <form method="GET" accept-charset="UTF-8">
                            <table class="newInvoicetab" style="width: 95%">
                                <tbody>
                                <tr>
                                    <td>
                                        <table class="clientTab">
                                            <tbody>
                                            <tr style="width: 100%">
                                                <td class="cust1">
                                                    <h3>Nabywca</h3>
                                                    <input class="customerInput" type="text" placeholder="Nazwa" id="odbiorca" name="odbiorca"
                                                           value="${customer.name}" readonly>
                                                    <br>
                                                    <input class="customerInput" type="text" placeholder="NIP" id="nip" name="nip"
                                                           value="${customer.nip}" readonly>
                                                    <br>
                                                    <br>

                                                </td>
                                                <td class="cust2">
                                                    <input class="customerInput" type="text" placeholder="Ulica" id="street" name="street"
                                                           value="${customer.address_line_1}"  readonly>
                                                    <br>
                                                    <input class="customerInput" type="text" placeholder="kod pocztowy" id="postcode"
                                                           name="postcode" value="${customer.postcode}"  readonly>
                                                    <input class="customerInput" type="text" placeholder="miejscowość" id="town" name="town"
                                                           value="${customer.address_line_3}"  readonly>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="cust1">
                                                    <h3>Odbiorca</h3>
                                                    <input class="customerInput" type="text" placeholder="Nazwa" id="platnik" name="platnik"
                                                           value="${payer.name}"  readonly>
                                                    <br>
                                                    <input class="customerInput" type="text" placeholder="NIP" id="nipPlatnik"
                                                           name="nipPlatnik" value="${payer.nip}"  readonly>
                                                    <br>
                                                    <br>
                                                </td>
                                                <td class="cust2">
                                                    <input class="customerInput" type="text" placeholder="Ulica" id="streetPlatnik"
                                                           name="streetPlatnik" value="${payer.address_line_1}"  readonly>
                                                    <br>
                                                    <input class="customerInput" type="text" placeholder="kod pocztowy" id="postcodePlatnik"
                                                           name="postcodePlatnik" value="${payer.postcode}" readonly>
                                                    <input class="customerInput" type="text" placeholder="miejscowość" id="townPlatnik"
                                                           name="townPlatnik" value="${payer.address_line_3}" readonly>
                                                    <br>
                                                </td>
                                            </tr>
                                            </tbody>
                                        </table>
                                    </td>
                                    <td class="invoiceButtons" style="width: 10%">
                                        <br>
                                        <input class="buttonI" type="submit" onclick="check()" formaction="correctEdit" name="action"
                                               value="Wystaw"
                                               formtarget="_blank">
                                        <br>
                                        <br>
                                    </td>
                                    <td>

                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <table class="invoiceDetailstab">
                                            <tbody>
                                            <tr class="inNumber">
                                                <td colspan="2" class="inNutd">
                                                    <input class="inNumber" type="text" id="invoiceNo" name="invoiceNo" value="${invoice.invoice_number}" readonly>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td colspan="2">
                                                    <br>
                                                    <br>
                                                    <table class="productsTab" onchange="sum()">
                                                        <tbody id="dataTable">
                                                        <tr>
                                                            <th>Lp.</th>
                                                            <th>Towar/Usługa</th>
                                                            <th>J.m.</th>
                                                            <th>Ilość</th>
                                                            <th>Cena netto</th>
                                                            <th>VAT</th>
                                                            <th>Cena brutto</th>
                                                            <th>Wartość netto</th>
                                                            <th>Wartość brutto</th>
                                                        </tr>
                                                        <c:forEach items="${listProducts}" var="prod">
                                                            <tr id="row${prod.lp-1}">
                                                                <td id="cell${prod.lp-1}0" class="lp">
                                                                    <input type="text" name="inputrow${prod.lp-1}0"
                                                                           id="inputrow${prod.lp-1}0"
                                                                           value="${prod.lp}" class="lp2" readonly></td>
                                                                <td id="cell${prod.lp-1}1" class="big">
                                                                    <input name="inputrow${prod.lp-1}1"
                                                                           class="big2"
                                                                           id="inputrow${prod.lp-1}1"
                                                                           value="${prod.product_name}" readonly>
                                                                </td>
                                                                <td id="cell${prod.lp-1}2" class="small">
                                                                    <input name="inputrow${prod.lp-1}2"
                                                                           id="inputrow${prod.lp-1}2"
                                                                           value="${prod.unit}" class="small2" readonly>
                                                                </td>
                                                                <td id="cell${prod.lp-1}3" class="medium">
                                                                    <input type="text"
                                                                           class="medium2"
                                                                           id="inputrow${prod.lp-1}3"
                                                                           name="inputrow${prod.lp-1}3"
                                                                           onchange="count()" value="${prod.quantity}">
                                                                </td>
                                                                <td id="cell${prod.lp-1}4" class="medium">
                                                                    <input type="text" name="inputrow${prod.lp-1}4"
                                                                           class="medium2"
                                                                           id="inputrow${prod.lp-1}4"
                                                                           value="${prod.price_netto}" readonly>
                                                                </td>
                                                                <td id="cell${prod.lp-1}5" class="small">
                                                                    <input  name="inputrow${prod.lp-1}5"
                                                                           class="small2"
                                                                           id="inputrow${prod.lp-1}5"
                                                                           onchange="copy();count()"
                                                                           value="${prod.vat}" readonly>
                                                                </td>
                                                                <td id="cell${prod.lp-1}6" class="medium">
                                                                    <input type="text" id="inputrow${prod.lp-1}6"
                                                                           name="inputrow${prod.lp-1}6"
                                                                           class="medium2"
                                                                           onchange="copy();count()"
                                                                           value="${prod.price_brutto}" readonly>
                                                                </td>
                                                                <td id="cell${prod.lp-1}7" class="medium">
                                                                    <input type="text" id="inputrow${prod.lp-1}7"
                                                                           class="medium2"
                                                                           name="inputrow${prod.lp-1}7"
                                                                           onchange="count();sum()"
                                                                           value="${prod.value_netto}" readonly>
                                                                </td>
                                                                <td id="cell${prod.lp-1}8" class="medium">
                                                                    <input type="text" id="inputrow${prod.lp-1}8"
                                                                           class="medium2"
                                                                           name="inputrow${prod.lp-1}8"
                                                                           onchange="count();sum()"
                                                                           value="${prod.value_brutto}" readonly>
                                                                </td>
                                                            </tr>
                                                        </c:forEach>
                                                        </tbody>
                                                    </table>
                                                    <br>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td rowspan="2" id="forma">
                                                    FORMA PŁATNOŚCI:
                                                    <input class="pay" name="payment" id="payments" value="${invoice.payment}" readonly>
                                                </td>
                                                <td id="sumN">
                                                    SUMA NETTO: <input class="pay2" type="text" id="nettoSuminv" name="nettoSuminv" readonly>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td id="sumB">
                                                    SUMA BRUTTO: <input  class="pay2" type="text" id="bruttoSuminv"
                                                                        name="bruttoSuminv"
                                                                        value="${invoice.value_brutto}" readonly>
                                                </td>
                                            </tr>
                                            </tbody>
                                        </table>
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
</body>
</html>