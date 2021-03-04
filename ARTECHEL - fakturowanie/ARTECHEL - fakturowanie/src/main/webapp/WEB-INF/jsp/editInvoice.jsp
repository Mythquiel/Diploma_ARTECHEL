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

        function checkIfprinted(){
            var invoiceNo = document.getElementById("invoiceNo");
            //alert(invoiceNo.value);
            var i = invoiceNo.value;
            if(i.includes("/20")){
                alert("Nie można usunąć wystawionej faktury!");
                event.preventDefault();
            }
        }

        function addRow(tableID) {
            try {
                var table = document.getElementById(tableID);
                var rowCount = table.rows.length;
                var row = document.getElementById("row" + (rowCount - 2)); //1
                var clone = row.cloneNode(true);
                clone.id = "row" + (rowCount - 1); //3
                var id = clone.getElementsByTagName("input");
                for (var i = 0, cell; cell = clone.cells[i]; i++) {
                    cell.id = "cell" + row.rowIndex + cell.cellIndex;
                }
                for (var j = 0; j < id.length; j++) {
                    id.item(j).id = "input" + clone.id + j;
                    id.item(j).name = "input" + clone.id + j;
                    id.item(j).value = "";

                }
                id.item(0).value = rowCount;
                table.appendChild(clone);
            } catch (e) {
                alert(e);
            }
        }

        function deleteRow(tableID) {
            try {
                var table = document.getElementById(tableID);
                var rowCount = table.rows.length;
                var row = document.getElementById("row" + (rowCount - 2));

                var elements = row.getElementsByTagName("input");
                for (var j = 0; j < elements.length; j++) {
                    elements.item(j).id = "inputrow" + row.id + j;
                    elements.item(j).name = "inputrow" + row.id + j;

                }
                if (table.rows.length > 2) {
                    table.deleteRow(rowCount - 1);
                } else {
                    alert("Faktura musi mieć przynajmniej jeden produkt");
                }
                row.id = "row" + (rowCount - 2);
            } catch (e) {
                alert("Faktura musi mieć przynajmniej jeden produkt");
            }
        }

        function copy() {
            var table = document.getElementById("dataTable");
            for (var i = 0; i < table.rows.length - 1; i++) {
                var src = document.getElementById("inputrow" + i + "6"),
                    dst = document.getElementById("inputrow" + i + "4"),
                    vat = document.getElementById("inputrow" + i + "5");
                var count = src.value * (1 - (vat.value / 100));
                var fixedcount = count.toFixed(2)
                dst.value = fixedcount;
            }
        }

        function emptyUnit(id) {
            var unit = document.getElementById(id);
            unit.value = "";
        }

        function emptyVat(id) {
            var vat = document.getElementById(id);
            vat.value = "";
        }

        function emptyProduct(id) {
            var product = document.getElementById(id);
            product.value = "";
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
                    cell.innerHTML = cell.innerHTML + "<input type=\"date\" id=\"dueDate\" name=\"dueDate\" value=\"${invoice.payment_due}\" class=\"pay\">";
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

        function showCalendar() { //740
            var payment = document.getElementById("payments");
            var value = payment.value;
            var cell = document.getElementById("forma");
            //alert(cell.innerHTML.length);
            if (value === "przelew") {
                if (cell.innerHTML.length < 770) {
                    cell.innerHTML = cell.innerHTML + "<input type=\"date\" id=\"dueDate\" name=\"dueDate\" value=\"${invoice.payment_due}\" class=\"pay\">";
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

        function setPayment(id) {
            var payment = document.getElementById(id);
            payment.value = "";
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
            var sumNFixed = sumN.toFixed(2)
            var sumB = sumBrutto + diffBrutto;
            var sumBFixed = sumB.toFixed(2)
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
            for (var i = 0; i < table.rows.length - 2; i++) {
                var nettoValue = Number(document.getElementById("inputrow" + i + "7").value),
                    bruttoValue = Number(document.getElementById("inputrow" + i + "8").value);
                tempNetto = tempNetto + nettoValue;
                tempBrutto = tempBrutto + bruttoValue;
            }
            diffNetto = tempNetto - sumNetto;
            diffBrutto = tempBrutto - sumBrutto;
            var sumN = sumNetto + diffNetto;
            var sumNFixed = sumN.toFixed(2)
            var sumB = sumBrutto + diffBrutto;
            var sumBFixed = sumB.toFixed(2)
            var sumNettofield = document.getElementById("nettoSuminv");
            sumNettofield.value = sumNFixed;
            var sumBruttofield = document.getElementById("bruttoSuminv");
            sumBruttofield.value = sumBFixed;

        }

        function getNabywcafromAPI() {
            var nip = document.getElementById("nip");
            if (nip.value !== "") {
                var xhttp = new XMLHttpRequest();
                var today = new Date();
                var dd = String(today.getDate()).padStart(2, '0');
                var mm = String(today.getMonth() + 1).padStart(2, '0'); //January is 0!
                var yyyy = today.getFullYear();
                today = yyyy + '-' + mm + '-' + dd;
                xhttp.onreadystatechange = function () {
                    if (this.readyState === 4 && this.status === 200) {
                        //alert(this.responseText);
                        var obj = JSON.parse(this.responseText);
                        var odb = document.getElementById("odbiorca");
                        var ulica = document.getElementById("street");
                        var postcode = document.getElementById("postcode");
                        var town = document.getElementById("town");
                        var address;
                        if (obj.result.subject.residenceAddress === null) {
                            address = obj.result.subject.workingAddress;
                        } else {
                            address = obj.result.subject.residenceAddress;
                        }
                        odb.value = obj.result.subject.name;
                        ulica.value = address.substring(0, address.indexOf(","));
                        postcode.value = address.substring(address.indexOf(",") + 2, address.indexOf(",") + 8);
                        town.value = address.substring(address.indexOf(",") + 9);
                    }
                };
                //"https://wl-api.mf.gov.pl/api/search/nip/9251001384?date=2020-01-05"
                xhttp.open("GET", "https://wl-api.mf.gov.pl/api/search/nip/" + nip.value + "?date=" + today);
                xhttp.send();
            } else {
                alert("Podaj poprawny numer nip");
            }
        }

        function getOdbiorcafromAPI() {
            var nip = document.getElementById("nipPlatnik");
            if (nip.value !== "") {
                var xhttp = new XMLHttpRequest();
                var today = new Date();
                var dd = String(today.getDate()).padStart(2, '0');
                var mm = String(today.getMonth() + 1).padStart(2, '0'); //January is 0!
                var yyyy = today.getFullYear();
                today = yyyy + '-' + mm + '-' + dd;
                xhttp.onreadystatechange = function () {
                    if (this.readyState === 4 && this.status === 200) {
                        //alert(this.responseText);
                        var obj = JSON.parse(this.responseText);
                        var odb = document.getElementById("platnik");
                        var ulica = document.getElementById("streetPlatnik");
                        var postcode = document.getElementById("postcodePlatnik");
                        var town = document.getElementById("townPlatnik");
                        var address;
                        if (obj.result.subject.residenceAddress === null) {
                            address = obj.result.subject.workingAddress;
                        } else {
                            address = obj.result.subject.residenceAddress;
                        }
                        odb.value = obj.result.subject.name;
                        ulica.value = address.substring(0, address.indexOf(","));
                        postcode.value = address.substring(address.indexOf(",") + 2, address.indexOf(",") + 8);
                        town.value = address.substring(address.indexOf(",") + 9);
                    }
                };
                //"https://wl-api.mf.gov.pl/api/search/nip/9251001384?date=2020-01-05"
                xhttp.open("GET", "https://wl-api.mf.gov.pl/api/search/nip/" + nip.value + "?date=" + today);
                xhttp.send();
            } else {
                alert("Podaj poprawny numer nip");
            }
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
                                                           value="${customer.name}">
                                                    <br>
                                                    <input class="customerInput" type="text" placeholder="NIP" id="nip" name="nip"
                                                           value="${customer.nip}">
                                                    <br>
                                                    <input type="button" value="Szukaj" id="nipAPI"
                                                           onclick="getNabywcafromAPI()">
                                                    <br>
                                                    <br>

                                                </td>
                                                <td class="cust2">
                                                    <input class="customerInput" type="text" placeholder="Ulica" id="street" name="street"
                                                           value="${customer.address_line_1}">
                                                    <br>
                                                    <input class="customerInput" type="text" placeholder="kod pocztowy" id="postcode"
                                                           name="postcode" value="${customer.postcode}">
                                                    <input class="customerInput" type="text" placeholder="miejscowość" id="town" name="town"
                                                           value="${customer.address_line_3}">
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="cust1">
                                                    <h3>Odbiorca</h3>
                                                    <input class="customerInput" type="text" placeholder="Nazwa" id="platnik" name="platnik"
                                                           value="${payer.name}">
                                                    <br>
                                                    <input class="customerInput" type="text" placeholder="NIP" id="nipPlatnik"
                                                           name="nipPlatnik" value="${payer.nip}">
                                                    <br>
                                                    <input type="button" value="Szukaj" id="nipAPIplatnik"
                                                           name="nipAPIplatnik" value="${payer.nip}"
                                                           onclick="getOdbiorcafromAPI()">
                                                    <br>
                                                    <br>
                                                </td>
                                                <td class="cust2">
                                                    <input class="customerInput" type="text" placeholder="Ulica" id="streetPlatnik"
                                                           name="streetPlatnik" value="${payer.address_line_1}">
                                                    <br>
                                                    <input class="customerInput" type="text" placeholder="kod pocztowy" id="postcodePlatnik"
                                                           name="postcodePlatnik" value="${payer.postcode}">
                                                    <input class="customerInput" type="text" placeholder="miejscowość" id="townPlatnik"
                                                           name="townPlatnik" value="${payer.address_line_3}">
                                                    <br>
                                                </td>
                                            </tr>
                                            </tbody>
                                        </table>
                                    </td>
                                    <td class="invoiceButtons" style="width: 10%">
                                        <br>
                                        <input class="buttonI" type="submit" onclick="check()" formaction="printEdit" name="action"
                                               value="Drukuj"
                                               formtarget="_blank">
                                        <br>
                                        <br>
                                        <input class="buttonI" type="submit" onclick="check()" formaction="saveEdit" name="action"
                                               value="Zapisz">
                                        <br>
                                        <br>
                                        <input class="buttonI" type="submit" onclick="checkIfprinted()" formaction="deleteEdit" name="action" value="Usuń fakturę">
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
                                                    <INPUT class="adddelete" type="button" value="Dodaj produkt"
                                                           onclick="addRow('dataTable')">
                                                    <input class="adddelete" type="button" value="Usuń ostatni produkt"
                                                           onclick="deleteRow('dataTable')">
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
                                                                           value="${prod.lp}" class="lp2"></td>
                                                                <td id="cell${prod.lp-1}1" class="big">
                                                                    <input list="products2" name="inputrow${prod.lp-1}1"
                                                                           class="big2"
                                                                           id="inputrow${prod.lp-1}1"
                                                                           onclick="emptyProduct(this.id)"
                                                                           value="${prod.product_name}">
                                                                    <datalist id="products2">
                                                                        <select name="products2">
                                                                            <c:forEach items="${productList}"
                                                                                       var="products">
                                                                                <option value="${products.name}">${products.name}</option>
                                                                            </c:forEach>
                                                                        </select>
                                                                    </datalist>
                                                                </td>
                                                                <td id="cell${prod.lp-1}2" class="small">
                                                                    <input list="units2" name="inputrow${prod.lp-1}2"
                                                                           id="inputrow${prod.lp-1}2"
                                                                           value="${prod.unit}" onclick="emptyUnit(this.id)" class="small2">
                                                                    <datalist id="units2">
                                                                        <select name="units2">
                                                                            <option>szt.</option>
                                                                            <option>mb.</option>
                                                                        </select>
                                                                    </datalist>
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
                                                                           value="${prod.price_netto}">
                                                                </td>
                                                                <td id="cell${prod.lp-1}5" class="small">
                                                                    <input list="vats2" name="inputrow${prod.lp-1}5"
                                                                           class="small2"
                                                                           id="inputrow${prod.lp-1}5"
                                                                           onchange="copy();count()"
                                                                           onclick="emptyVat(this.id)"
                                                                           value="${prod.vat}">
                                                                    <datalist id="vats2">
                                                                        <select name="vats2">
                                                                            <option>0</option>
                                                                            <option>8</option>
                                                                            <option>23</option>
                                                                        </select>
                                                                    </datalist>
                                                                </td>
                                                                <td id="cell${prod.lp-1}6" class="medium">
                                                                    <input type="text" id="inputrow${prod.lp-1}6"
                                                                           name="inputrow${prod.lp-1}6"
                                                                           class="medium2"
                                                                           onchange="copy();count()"
                                                                           value="${prod.price_brutto}">
                                                                </td>
                                                                <td id="cell${prod.lp-1}7" class="medium">
                                                                    <input type="text" id="inputrow${prod.lp-1}7"
                                                                           class="medium2"
                                                                           name="inputrow${prod.lp-1}7"
                                                                           onchange="count();sum()"
                                                                           value="${prod.value_netto}">
                                                                </td>
                                                                <td id="cell${prod.lp-1}8" class="medium">
                                                                    <input type="text" id="inputrow${prod.lp-1}8"
                                                                           class="medium2"
                                                                           name="inputrow${prod.lp-1}8"
                                                                           onchange="count();sum()"
                                                                           value="${prod.value_brutto}">
                                                                </td>
                                                            </tr>
                                                        </c:forEach>
                                                        <tr id="row${listProducts.get(listProducts.size()-1).lp}">
                                                            <td id="cell${listProducts.get(listProducts.size()-1).lp}0" class="lp" >
                                                                <input type="text"
                                                                       name="inputrow${listProducts.get(listProducts.size()-1).lp}0"
                                                                       id="inputrow${listProducts.get(listProducts.size()-1).lp}0"
                                                                       class="lp2"
                                                                       value=${listProducts.get(listProducts.size()-1).lp+1}>
                                                            </td>
                                                            <td id="cell${listProducts.get(listProducts.size()-1).lp}1" class="lp" >
                                                                <input list="products"
                                                                       class="big2"
                                                                       name="inputrow${listProducts.get(listProducts.size()-1).lp}1"
                                                                       id="inputrow${listProducts.get(listProducts.size()-1).lp}1"
                                                                       onclick="emptyProduct(this.id)">
                                                                <datalist id="products">
                                                                    <select name="products">
                                                                        <c:forEach items="${productList}"
                                                                                   var="products">
                                                                            <option value="${products.name}">${products.name}</option>
                                                                        </c:forEach>
                                                                    </select>
                                                                </datalist>
                                                            </td>
                                                            <td id="cell${listProducts.get(listProducts.size()-1).lp}2" class="small">
                                                                <input list="units"
                                                                       name="inputrow${listProducts.get(listProducts.size()-1).lp}2"
                                                                       class="small2"
                                                                       id="inputrow${listProducts.get(listProducts.size()-1).lp}2" onclick="emptyUnit(this.id)">
                                                                <datalist id="units">
                                                                    <select name="units">
                                                                        <option>szt.</option>
                                                                        <option>mb.</option>
                                                                    </select>
                                                                </datalist>
                                                            </td>
                                                            <td id="cell${listProducts.get(listProducts.size()-1).lp}3" class="medium">
                                                                <input type="text" class="inputnewInvoice"
                                                                       id="inputrow${listProducts.get(listProducts.size()-1).lp}3"
                                                                       class="medium2"
                                                                       name="inputrow${listProducts.get(listProducts.size()-1).lp}3"
                                                                       onchange="count()">
                                                            </td>
                                                            <td id="cell${listProducts.get(listProducts.size()-1).lp}4" class="medium">
                                                                <input type="text"
                                                                       name="inputrow${listProducts.get(listProducts.size()-1).lp}4"
                                                                       class="medium2"
                                                                       id="inputrow${listProducts.get(listProducts.size()-1).lp}4">
                                                            </td>
                                                            <td id="cell${listProducts.get(listProducts.size()-1).lp}5" class="small">
                                                                <input list="vats"
                                                                       name="inputrow${listProducts.get(listProducts.size()-1).lp}5"
                                                                       class="small2"
                                                                       id="inputrow${listProducts.get(listProducts.size()-1).lp}5"
                                                                       onchange="copy();count()"
                                                                       onclick="emptyVat(this.id)">
                                                                <datalist id="vats">
                                                                    <select name="vats">
                                                                        <option>0</option>
                                                                        <option>8</option>
                                                                        <option>23</option>
                                                                    </select>
                                                                </datalist>
                                                            </td>
                                                            <td id="cell${listProducts.get(listProducts.size()-1).lp}6" class="medium">
                                                                <input type="text"
                                                                       id="inputrow${listProducts.get(listProducts.size()-1).lp}6"
                                                                       class="medium2"
                                                                       name="inputrow${listProducts.get(listProducts.size()-1).lp}6"
                                                                       onchange="copy();count()">
                                                            </td>
                                                            <td id="cell${listProducts.get(listProducts.size()-1).lp}7" class="medium">
                                                                <input type="text"
                                                                       id="inputrow${listProducts.get(listProducts.size()-1).lp}7"
                                                                       class="medium2"
                                                                       name="inputrow${listProducts.get(listProducts.size()-1).lp}7"
                                                                       onchange="count();sum()">
                                                            </td>
                                                            <td id="cell${listProducts.get(listProducts.size()-1).lp}8" class="medium">
                                                                <input type="text"
                                                                       id="inputrow${listProducts.get(listProducts.size()-1).lp}8"
                                                                       class="medium2"
                                                                       name="inputrow${listProducts.get(listProducts.size()-1).lp}8"
                                                                       onchange="count();sum()">
                                                            </td>
                                                        </tr>
                                                        </tbody>
                                                    </table>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td rowspan="2" id="forma">
                                                    FORMA PŁATNOŚCI:
                                                    <input class="pay" list="payment" name="payment" id="payments"
                                                           onchange="showCalendar(this.id)"
                                                           onclick="setPayment(this.id)" value="${invoice.payment}">
                                                    <datalist id="payment">
                                                        <select name="payment">
                                                            <option>gotówka</option>
                                                            <option>przelew</option>
                                                        </select>
                                                    </datalist>
                                                </td>
                                                <td id="sumN">
                                                    SUMA NETTO: <input class="pay2" type="text" id="nettoSuminv" name="nettoSuminv">
                                                </td>
                                            </tr>
                                            <tr>
                                                <td id="sumB">
                                                    SUMA BRUTTO: <input  class="pay2" type="text" id="bruttoSuminv"
                                                                        name="bruttoSuminv"
                                                                        value="${invoice.value_brutto}">
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