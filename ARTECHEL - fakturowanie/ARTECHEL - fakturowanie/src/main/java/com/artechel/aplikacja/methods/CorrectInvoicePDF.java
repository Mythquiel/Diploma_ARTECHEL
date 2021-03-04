package com.artechel.aplikacja.methods;

import com.artechel.aplikacja.model.Customer;
import com.artechel.aplikacja.model.Invoice;
import com.artechel.aplikacja.model.Invoice_product;
import com.artechel.aplikacja.model.Payer;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.*;
import java.util.List;

import static java.lang.Math.abs;


public class CorrectInvoicePDF extends AbstractPdfView {


    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter pdfWriter, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        Invoice oldInvoice = (Invoice) model.get("oldInvoice");
        List<Invoice_product> oldProductsList = (List<Invoice_product>) model.get("oldProducts");
        Customer customer = (Customer) model.get("customer");
        Payer payer = (Payer) model.get("payer");
        Invoice thisInvoice = (Invoice) model.get("thisInvoice");
        List<Invoice_product> thisProductsList = (List<Invoice_product>) model.get("thisProducts");
        Invoice newInvoice = (Invoice) model.get("newInvoice");
        List<Invoice_product> newProductsList = (List<Invoice_product>) model.get("newProducts");

        addMetaData(document, oldInvoice);
        addContent(document, oldInvoice, oldProductsList, thisInvoice, thisProductsList, newInvoice, newProductsList, customer, payer);
    }

    private void addMetaData(Document document, Invoice invoice) {
        document.addTitle("Faktura korekcyjna nr " + invoice.getInvoice_number());
        document.addAuthor("Artykuly Techniczne Motoryzacyjne i Elektryczne Dorota Świtała");
    }

    private void addContent(Document document, Invoice oldInvoice, List<Invoice_product> oldProductsList, Invoice thisInvoice, List<Invoice_product> thisProductsList, Invoice invoice, List<Invoice_product> newProductsList, Customer customer, Payer payer) throws DocumentException, ParseException {

        ArrayList<String> sellerList = new ArrayList<>();
        sellerList.add("SPRZEDAWCA");
        sellerList.add("Artykuły Techniczne Motoryzacyjne i Elektryczne Dorota Świtała");
        sellerList.add("ul. Kościelna 1 A , 67-124 Nowe Miasteczko");
        sellerList.add("NIP: 925-100-13-84");
        sellerList.add("Bank Spółdzielczy w Kożuchowie o/ Nowe Miasteczko");
        sellerList.add("Nr rachunku bankowego: 50 9673 1017 0010 0113 5887 0001");

        ArrayList<String> buyerList = new ArrayList<>();
        buyerList.add("NABYWCA");
        buyerList.add(customer.getName());
        buyerList.add(customer.getAddress_line_1() + " , " + customer.getPostcode() + " " + customer.getAddress_line_3());
        buyerList.add("NIP: " + customer.getNip());

        ArrayList<String> receiverList = new ArrayList<>();
        if (payer != null) {
            receiverList.add("ODBIORCA");
            receiverList.add(payer.getName());
            receiverList.add(payer.getAddress_line_1() + " , " + payer.getPostcode() + " " + payer.getAddress_line_3());
            receiverList.add("NIP: " + payer.getNip());
        }


        Paragraph pdfObject = new Paragraph();

        createHeaderTable(getFonts(), pdfObject);
        addEmptyLine(pdfObject, 1, getFonts());
        createInvoiceDataTable(getFonts(), pdfObject, thisInvoice, oldInvoice);
        addEmptyLine(pdfObject, 1, getFonts());
        createInvoiceRecipientsTable(getFonts(), pdfObject, sellerList);
        addEmptyLine(pdfObject, 1, getFonts());
        createInvoiceRecipientsTable(getFonts(), pdfObject, buyerList);

        if (receiverList.size() != 0) {
            addEmptyLine(pdfObject, 1, getFonts());
            createInvoiceRecipientsTable(getFonts(), pdfObject, receiverList);
        }
        addEmptyLine(pdfObject, 1, getFonts());
        createPaymentTable(getFonts(), pdfObject, thisInvoice);
        addEmptyLine(pdfObject, 1, getFonts());
        createProductsTable(getFonts(), pdfObject, oldProductsList,thisProductsList,newProductsList);
        addEmptyLine(pdfObject, 1, getFonts());
        createSumTable(getFonts(), pdfObject, thisProductsList);
        addEmptyLine(pdfObject, 1, getFonts());
        createValueTable(getFonts(), pdfObject, thisInvoice);
        addEmptyLine(pdfObject, 7, getFonts());
        createSignatureTable(getFonts(), pdfObject);

        document.add(pdfObject);


    }

    private void createProductsTable(Hashtable<String, Font> fontList, Paragraph pdfObject, List<Invoice_product> oldProductsList, List<Invoice_product> thisProductsList, List<Invoice_product> newProductsList) {
        PdfPTable tableProducts = new PdfPTable(new float[]{5, 24, 5, 5, 8, 11, 5, 11, 13, 13});//10
        tableProducts.setWidthPercentage(100);
        PdfPCell lpLabel = new PdfPCell(new Phrase("Lp.", fontList.get("fontBold9")));
        lpLabel.setBackgroundColor(Color.LIGHT_GRAY);
        lpLabel.setHorizontalAlignment(Element.ALIGN_CENTER);
        lpLabel.setVerticalAlignment(Element.ALIGN_MIDDLE);
        lpLabel.setMinimumHeight(25);
        tableProducts.addCell(lpLabel);

        PdfPCell productLabel = new PdfPCell(new Phrase("Towar / Usługa", fontList.get("fontBold9")));
        productLabel.setBackgroundColor(Color.LIGHT_GRAY);
        productLabel.setHorizontalAlignment(Element.ALIGN_CENTER);
        productLabel.setVerticalAlignment(Element.ALIGN_MIDDLE);
        tableProducts.addCell(productLabel);

        PdfPCell codeLabel = new PdfPCell(new Phrase("Kod", fontList.get("fontBold9")));
        codeLabel.setBackgroundColor(Color.LIGHT_GRAY);
        codeLabel.setHorizontalAlignment(Element.ALIGN_CENTER);
        codeLabel.setVerticalAlignment(Element.ALIGN_MIDDLE);
        tableProducts.addCell(codeLabel);

        PdfPCell unitLabel = new PdfPCell(new Phrase("Jedn.", fontList.get("fontBold9")));
        unitLabel.setBackgroundColor(Color.LIGHT_GRAY);
        unitLabel.setHorizontalAlignment(Element.ALIGN_CENTER);
        unitLabel.setVerticalAlignment(Element.ALIGN_MIDDLE);
        tableProducts.addCell(unitLabel);

        PdfPCell quantityLabel = new PdfPCell(new Phrase("Ilość", fontList.get("fontBold9")));
        quantityLabel.setBackgroundColor(Color.LIGHT_GRAY);
        quantityLabel.setHorizontalAlignment(Element.ALIGN_CENTER);
        quantityLabel.setVerticalAlignment(Element.ALIGN_MIDDLE);
        tableProducts.addCell(quantityLabel);

        PdfPCell nettoPriceLabel = new PdfPCell(new Phrase("Cena netto", fontList.get("fontBold9")));
        nettoPriceLabel.setBackgroundColor(Color.LIGHT_GRAY);
        nettoPriceLabel.setHorizontalAlignment(Element.ALIGN_CENTER);
        nettoPriceLabel.setVerticalAlignment(Element.ALIGN_MIDDLE);
        tableProducts.addCell(nettoPriceLabel);

        PdfPCell vatLabel = new PdfPCell(new Phrase("VAT", fontList.get("fontBold9")));
        vatLabel.setBackgroundColor(Color.LIGHT_GRAY);
        vatLabel.setHorizontalAlignment(Element.ALIGN_CENTER);
        vatLabel.setVerticalAlignment(Element.ALIGN_MIDDLE);
        tableProducts.addCell(vatLabel);

        PdfPCell bruttoPriceLabel = new PdfPCell(new Phrase("Cena brutto", fontList.get("fontBold9")));
        bruttoPriceLabel.setBackgroundColor(Color.LIGHT_GRAY);
        bruttoPriceLabel.setHorizontalAlignment(Element.ALIGN_CENTER);
        bruttoPriceLabel.setVerticalAlignment(Element.ALIGN_MIDDLE);
        tableProducts.addCell(bruttoPriceLabel);

        PdfPCell valueNettoLabel = new PdfPCell(new Phrase("Wartość netto", fontList.get("fontBold9")));
        valueNettoLabel.setBackgroundColor(Color.LIGHT_GRAY);
        valueNettoLabel.setHorizontalAlignment(Element.ALIGN_CENTER);
        valueNettoLabel.setVerticalAlignment(Element.ALIGN_MIDDLE);
        tableProducts.addCell(valueNettoLabel);

        PdfPCell valueBruttoLabel = new PdfPCell(new Phrase("Wartość brutto", fontList.get("fontBold9")));
        valueBruttoLabel.setBackgroundColor(Color.LIGHT_GRAY);
        valueBruttoLabel.setHorizontalAlignment(Element.ALIGN_CENTER);
        valueBruttoLabel.setVerticalAlignment(Element.ALIGN_MIDDLE);
        tableProducts.addCell(valueBruttoLabel);

        PdfPCell oldHeader = new PdfPCell(new Phrase("Przed korektą", fontList.get("font9")));
        oldHeader.setColspan(10);
        tableProducts.addCell(oldHeader);

        //--products in invoice--

        for (int i = 0; i < oldProductsList.size(); i++) {
            addProduct(i,fontList,tableProducts,oldProductsList.get(i));
        }

        PdfPCell newHeader = new PdfPCell(new Phrase("Po korekcie", fontList.get("font9")));
        newHeader.setColspan(10);
        tableProducts.addCell(newHeader);

        //--products in invoice--

        for (int i = 0; i < newProductsList.size(); i++) {
            addProduct(i,fontList,tableProducts,newProductsList.get(i));
        }

        PdfPCell thisHeader = new PdfPCell(new Phrase("Saldo", fontList.get("font9")));
        thisHeader.setColspan(10);
        tableProducts.addCell(thisHeader);

        //--products in invoice--

        for (int i = 0; i < thisProductsList.size(); i++) {
            addProduct(i,fontList,tableProducts,thisProductsList.get(i));
        }

        pdfObject.add(tableProducts);

    }

    private void createSumTable(Hashtable<String, Font> fontList, Paragraph pdfObject, List<Invoice_product> thisProductsList) {

        Float netto0 = (float) 0, netto8 = (float) 0, netto23 = (float) 0;
        Float brutto0 = (float) 0, brutto8 = (float) 0, brutto23 = (float) 0;
        Float vat0 = (float) 0, vat8 = (float) 0, vat23 = (float) 0;
        Float sumNetto, sumVAT, sumBrutto;
        DecimalFormat df = new DecimalFormat("0.00");

        for (int i = 0; i <= thisProductsList.size() - 1; i++) {
            if (thisProductsList.get(i).getVat() == 0) {
                netto0 = netto0 + thisProductsList.get(i).getValue_netto();
                brutto0 = brutto0 + thisProductsList.get(i).getValue_brutto();
                vat0 = vat0 + (brutto0 - netto0);
            } else if (thisProductsList.get(i).getVat() == 8) {
                netto8 += thisProductsList.get(i).getValue_netto();
                brutto8 += thisProductsList.get(i).getValue_brutto();
                vat8 += (brutto8 - netto8);
            } else if (thisProductsList.get(i).getVat() == 23) {
                netto23 += thisProductsList.get(i).getValue_netto();
                brutto23 += thisProductsList.get(i).getValue_brutto();
                vat23 += (brutto23 - netto23);
            }
        }
        sumNetto = netto0 + netto8 + netto23;
        sumBrutto = brutto0 + brutto8 + brutto23;
        sumVAT = vat0 + vat8 + vat23;


        PdfPTable tableSumLabel = new PdfPTable(1);
        tableSumLabel.setWidthPercentage(Float.valueOf("36.9"));
        tableSumLabel.setHorizontalAlignment(Element.ALIGN_RIGHT);

        PdfPCell title = new PdfPCell(new Phrase("SUMA WEDŁUG STAWEK VAT", fontList.get("font9")));
        title.setHorizontalAlignment(Element.ALIGN_CENTER);
        title.setVerticalAlignment(Element.ALIGN_MIDDLE);
        tableSumLabel.addCell(title);

        //--nagłówki--
        PdfPTable tableSum = new PdfPTable(new float[]{18, 24, 10, 24, 24});//5
        tableSum.setWidthPercentage(45);
        tableSum.setHorizontalAlignment(Element.ALIGN_RIGHT);

        PdfPCell blank = new PdfPCell(new Phrase("", fontList.get("fontBold9")));
        blank.setBorder(Rectangle.NO_BORDER);
        tableSum.addCell(blank);

        PdfPCell nettoLabel = new PdfPCell(new Phrase("Netto", fontList.get("fontBold9")));
        nettoLabel.setHorizontalAlignment(Element.ALIGN_CENTER);
        nettoLabel.setBackgroundColor(Color.LIGHT_GRAY);
        tableSum.addCell(nettoLabel);

        PdfPCell vatLabel = new PdfPCell(new Phrase("VAT", fontList.get("fontBold9")));
        vatLabel.setHorizontalAlignment(Element.ALIGN_CENTER);
        vatLabel.setBackgroundColor(Color.LIGHT_GRAY);
        tableSum.addCell(vatLabel);

        PdfPCell vatValueLabel = new PdfPCell(new Phrase("Kwota VAT", fontList.get("fontBold9")));
        vatValueLabel.setBackgroundColor(Color.LIGHT_GRAY);
        vatValueLabel.setHorizontalAlignment(Element.ALIGN_CENTER);
        tableSum.addCell(vatValueLabel);

        PdfPCell bruttoLabel = new PdfPCell(new Phrase("Brutto", fontList.get("fontBold9")));
        bruttoLabel.setBackgroundColor(Color.LIGHT_GRAY);
        bruttoLabel.setHorizontalAlignment(Element.ALIGN_CENTER);
        tableSum.addCell(bruttoLabel);

        if (netto0 != 0) {

            tableSum.addCell(blank);

            String netto0fixed = df.format(netto0);
            PdfPCell netto0c = new PdfPCell(new Phrase(netto0fixed, fontList.get("font10")));
            netto0c.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableSum.addCell(netto0c);

            PdfPCell vat0c = new PdfPCell(new Phrase("0", fontList.get("font10")));
            vat0c.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableSum.addCell(vat0c);

            String vat0fixed = df.format(vat0);
            PdfPCell vatValue0c = new PdfPCell(new Phrase(vat0fixed, fontList.get("font10")));
            vatValue0c.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableSum.addCell(vatValue0c);

            String brutto0fixed = df.format(brutto0);
            PdfPCell brutto0c = new PdfPCell(new Phrase(brutto0fixed, fontList.get("font10")));
            brutto0c.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableSum.addCell(brutto0c);
        }
        if (netto8 != 0) {
            tableSum.addCell(blank);

            String netto8fixed = df.format(netto8);
            PdfPCell netto8c = new PdfPCell(new Phrase(netto8fixed, fontList.get("font10")));
            netto8c.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableSum.addCell(netto8c);


            PdfPCell vat8c = new PdfPCell(new Phrase("8", fontList.get("font10")));
            vat8c.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableSum.addCell(vat8c);

            String vat8fixed = df.format(vat8);
            PdfPCell vatValue8c = new PdfPCell(new Phrase(vat8fixed, fontList.get("font10")));
            vatValue8c.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableSum.addCell(vatValue8c);

            String brutto8fixed = df.format(brutto8);
            PdfPCell brutto8c = new PdfPCell(new Phrase(brutto8fixed, fontList.get("font10")));
            brutto8c.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableSum.addCell(brutto8c);
        }
        if (netto23 != 0) {
            tableSum.addCell(blank);

            String netto23fixed = df.format(netto23);
            PdfPCell netto23c = new PdfPCell(new Phrase(netto23fixed, fontList.get("font10")));
            netto23c.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableSum.addCell(netto23c);

            PdfPCell vat23c = new PdfPCell(new Phrase("23", fontList.get("font10")));
            vat23c.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableSum.addCell(vat23c);

            String vat23fixed = df.format(vat23);
            PdfPCell vatValue23c = new PdfPCell(new Phrase(vat23fixed, fontList.get("font10")));
            vatValue23c.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableSum.addCell(vatValue23c);

            String brutto23fixed = df.format(brutto23);
            PdfPCell brutto23c = new PdfPCell(new Phrase(brutto23fixed, fontList.get("font10")));
            brutto23c.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableSum.addCell(brutto23c);
        }

        PdfPCell labelSum = new PdfPCell(new Phrase("RAZEM:", fontList.get("font10")));
        labelSum.setHorizontalAlignment(Element.ALIGN_CENTER);
        tableSum.addCell(labelSum);

        String sumNettofixed = df.format(sumNetto);
        PdfPCell sumAllNetto = new PdfPCell(new Phrase(sumNettofixed, fontList.get("fontBold10")));
        sumAllNetto.setHorizontalAlignment(Element.ALIGN_CENTER);
        tableSum.addCell(sumAllNetto);

        PdfPCell vatSum = new PdfPCell(new Phrase("", fontList.get("fontBold10")));
        tableSum.addCell(vatSum);

        String sumVATfixed = df.format(sumVAT);
        PdfPCell sumVatValue = new PdfPCell(new Phrase(sumVATfixed, fontList.get("fontBold10")));
        sumVatValue.setHorizontalAlignment(Element.ALIGN_CENTER);
        tableSum.addCell(sumVatValue);

        String sumBruttofixed = df.format(sumBrutto);
        PdfPCell sumAllBrutto = new PdfPCell(new Phrase(sumBruttofixed, fontList.get("fontBold10")));
        sumAllBrutto.setHorizontalAlignment(Element.ALIGN_CENTER);
        tableSum.addCell(sumAllBrutto);

        pdfObject.add(tableSumLabel);
        pdfObject.add(tableSum);

    }

    private void createValueTable(Hashtable<String, Font> fontList, Paragraph pdfObject, Invoice invoice) {

        DecimalFormat df = new DecimalFormat("0.00");
        String invoiceValue = df.format(abs(invoice.getValue_brutto()));
        PdfPTable tableSumValue = new PdfPTable(2);
        tableSumValue.setWidthPercentage(40);
        tableSumValue.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell labelValue = new PdfPCell(new Phrase("Do zwrotu:", fontList.get("font10")));
        labelValue.setMinimumHeight(25);
        labelValue.setVerticalAlignment(Element.ALIGN_MIDDLE);
        labelValue.setHorizontalAlignment(Element.ALIGN_CENTER);
        labelValue.setBorder(Rectangle.NO_BORDER);
        //labelValue.setBorderWidthBottom(Float.valueOf("0.5"));
        labelValue.setBorderWidthLeft(Float.valueOf("0.5"));
        labelValue.setBorderWidthTop(Float.valueOf("0.5"));
        tableSumValue.addCell(labelValue);

        PdfPCell value = new PdfPCell(new Phrase(invoiceValue + " zł", fontList.get("fontBold12")));
        value.setVerticalAlignment(Element.ALIGN_MIDDLE);
        value.setHorizontalAlignment(Element.ALIGN_CENTER);
        value.setBorder(Rectangle.NO_BORDER);
        //value.setBorderWidthBottom(Float.valueOf("0.5"));
        value.setBorderWidthRight(Float.valueOf("0.5"));
        value.setBorderWidthTop(Float.valueOf("0.5"));
        tableSumValue.addCell(value);

        //--written
        PdfPTable tableWritten = new PdfPTable(1);
        tableWritten.setWidthPercentage(100);

        PdfPCell valueWritten = new PdfPCell(new Phrase("        Słownie: " + createWrittenValue(invoiceValue), fontList.get("font10")));
        valueWritten.setMinimumHeight(20);
        valueWritten.setVerticalAlignment(Element.ALIGN_MIDDLE);
        //valueWritten.setHorizontalAlignment(Element.ALIGN_CENTER);
        tableWritten.addCell(valueWritten);

        pdfObject.add(tableSumValue);
        pdfObject.add(tableWritten);


    }



    private void addProduct(int i, Hashtable<String, Font> fontList, PdfPTable tableProducts, Invoice_product invoice_product){
        PdfPCell lp = new PdfPCell(new Phrase(String.valueOf(i + 1), fontList.get("font10")));
        lp.setHorizontalAlignment(Element.ALIGN_CENTER);
        lp.setVerticalAlignment(Element.ALIGN_MIDDLE);
        lp.setMinimumHeight(15);
        tableProducts.addCell(lp);

        PdfPCell product = new PdfPCell(new Phrase(invoice_product.getProduct_name(), fontList.get("font10")));
        product.setHorizontalAlignment(Element.ALIGN_CENTER);
        product.setVerticalAlignment(Element.ALIGN_MIDDLE);
        tableProducts.addCell(product);

        PdfPCell kod = new PdfPCell(new Phrase(""));
        tableProducts.addCell(kod);

        PdfPCell jm = new PdfPCell(new Phrase(invoice_product.getUnit(), fontList.get("font10")));
        jm.setHorizontalAlignment(Element.ALIGN_CENTER);
        jm.setVerticalAlignment(Element.ALIGN_MIDDLE);
        tableProducts.addCell(jm);

        DecimalFormat df = new DecimalFormat("0.00");
        String quantityFixed = df.format(invoice_product.getQuantity());
        PdfPCell quantity = new PdfPCell(new Phrase(String.valueOf(quantityFixed), fontList.get("font10")));
        quantity.setHorizontalAlignment(Element.ALIGN_CENTER);
        quantity.setVerticalAlignment(Element.ALIGN_MIDDLE);
        tableProducts.addCell(quantity);

        String nettoPriceFixed = df.format(invoice_product.getPrice_netto());
        PdfPCell nettoPrice = new PdfPCell(new Phrase(String.valueOf(nettoPriceFixed), fontList.get("font10")));
        nettoPrice.setHorizontalAlignment(Element.ALIGN_CENTER);
        nettoPrice.setVerticalAlignment(Element.ALIGN_MIDDLE);
        tableProducts.addCell(nettoPrice);

        PdfPCell vat = new PdfPCell(new Phrase(String.valueOf(invoice_product.getVat()), fontList.get("font10")));
        vat.setHorizontalAlignment(Element.ALIGN_CENTER);
        vat.setVerticalAlignment(Element.ALIGN_MIDDLE);
        tableProducts.addCell(vat);

        String bruttoPriceFixed = df.format(invoice_product.getPrice_brutto());
        PdfPCell bruttoPrice = new PdfPCell(new Phrase(String.valueOf(bruttoPriceFixed), fontList.get("font10")));
        bruttoPrice.setHorizontalAlignment(Element.ALIGN_CENTER);
        bruttoPrice.setVerticalAlignment(Element.ALIGN_MIDDLE);
        tableProducts.addCell(bruttoPrice);


        String nettoValueFixed = df.format(invoice_product.getValue_netto());
        PdfPCell nettoValue = new PdfPCell(new Phrase(String.valueOf(nettoValueFixed), fontList.get("font10")));
        nettoValue.setHorizontalAlignment(Element.ALIGN_CENTER);
        nettoValue.setVerticalAlignment(Element.ALIGN_MIDDLE);
        tableProducts.addCell(nettoValue);

        String bruttoValueFixed = df.format(invoice_product.getValue_brutto());
        PdfPCell bruttoValue = new PdfPCell(new Phrase(String.valueOf(bruttoValueFixed), fontList.get("font10")));
        bruttoValue.setHorizontalAlignment(Element.ALIGN_CENTER);
        bruttoValue.setVerticalAlignment(Element.ALIGN_MIDDLE);
        tableProducts.addCell(bruttoValue);
    }

    private void createSignatureTable(Hashtable<String, Font> fontList, Paragraph pdfObject) {
        PdfPTable tableSingature = new PdfPTable(3);
        tableSingature.setWidthPercentage(100);

        PdfPCell invoiceFromDot = new PdfPCell(new Phrase(""));
        invoiceFromDot.setBorder(Rectangle.NO_BORDER);
        invoiceFromDot.setBorderWidthBottom(Float.valueOf("0.3"));
        invoiceFromDot.setBorderColorBottom(Color.GRAY);
        tableSingature.addCell(invoiceFromDot);

        PdfPCell blank = new PdfPCell(new Phrase(""));
        blank.setBorder(Rectangle.NO_BORDER);
        tableSingature.addCell(blank);

        PdfPCell invoiceToDot = new PdfPCell(new Phrase(""));
        invoiceToDot.setBorder(Rectangle.NO_BORDER);
        invoiceToDot.setBorderWidthBottom(Float.valueOf("0.3"));
        invoiceToDot.setBorderColorBottom(Color.GRAY);
        tableSingature.addCell(invoiceToDot);

        PdfPCell invoiceFrom = new PdfPCell(new Phrase("Fakturę wystawił", fontList.get("font9")));
        invoiceFrom.setBorder(Rectangle.NO_BORDER);
        invoiceFrom.setVerticalAlignment(Element.ALIGN_MIDDLE);
        invoiceFrom.setHorizontalAlignment(Element.ALIGN_CENTER);
        tableSingature.addCell(invoiceFrom);

        tableSingature.addCell(blank);

        PdfPCell invoiceTo = new PdfPCell(new Phrase("Fakturę odebrał", fontList.get("font9")));
        invoiceTo.setBorder(Rectangle.NO_BORDER);
        invoiceTo.setVerticalAlignment(Element.ALIGN_MIDDLE);
        invoiceTo.setHorizontalAlignment(Element.ALIGN_CENTER);
        tableSingature.addCell(invoiceTo);

        pdfObject.add(tableSingature);


    }

    private void createPaymentTable(Hashtable<String, Font> fontList, Paragraph pdfObject, Invoice invoice) throws ParseException {
        PdfPTable tablePayment = new PdfPTable(new float[]{24, 24, 4, 24, 24});
        tablePayment.setWidthPercentage(60);
        tablePayment.setHorizontalAlignment(Element.ALIGN_LEFT);

        PdfPCell paymentLabel = new PdfPCell(new Phrase("Forma płatności: ", fontList.get("font9")));
        paymentLabel.setBackgroundColor(Color.LIGHT_GRAY);
        paymentLabel.setVerticalAlignment(Element.ALIGN_MIDDLE);
        paymentLabel.setHorizontalAlignment(Element.ALIGN_CENTER);
        paymentLabel.setBorder(Rectangle.NO_BORDER);
        paymentLabel.setBorderWidthTop(Float.valueOf("0.5"));
        paymentLabel.setBorderWidthLeft(Float.valueOf("0.5"));
        paymentLabel.setBorderWidthBottom(Float.valueOf("0.5"));
        tablePayment.addCell(paymentLabel);

        PdfPCell payment = new PdfPCell(new Phrase(invoice.getPayment(), fontList.get("font10")));
        payment.setBorder(Rectangle.NO_BORDER);
        payment.setVerticalAlignment(Element.ALIGN_MIDDLE);
        payment.setHorizontalAlignment(Element.ALIGN_CENTER);
        payment.setBorderWidthTop(Float.valueOf("0.5"));
        payment.setBorderWidthRight(Float.valueOf("0.5"));
        payment.setBorderWidthBottom(Float.valueOf("0.5"));
        tablePayment.addCell(payment);

        PdfPCell blank = new PdfPCell(new Phrase(""));
        blank.setBorder(Rectangle.NO_BORDER);
        tablePayment.addCell(blank);


        PdfPCell paymentDueLabel = new PdfPCell(new Phrase("Termin płatności: ", fontList.get("font9")));
        paymentDueLabel.setBackgroundColor(Color.LIGHT_GRAY);
        paymentDueLabel.setBorder(Rectangle.NO_BORDER);
        paymentDueLabel.setVerticalAlignment(Element.ALIGN_MIDDLE);
        paymentDueLabel.setHorizontalAlignment(Element.ALIGN_CENTER);
        paymentDueLabel.setBorderWidthLeft(Float.valueOf("0.5"));
        paymentDueLabel.setBorderWidthTop(Float.valueOf("0.5"));
        paymentDueLabel.setBorderWidthBottom(Float.valueOf("0.5"));
        tablePayment.addCell(paymentDueLabel);

        PdfPCell paymentDue = new PdfPCell(new Phrase(String.valueOf(invoice.getPayment_due()), fontList.get("font10")));
        paymentDue.setBorder(Rectangle.NO_BORDER);
        paymentDue.setVerticalAlignment(Element.ALIGN_MIDDLE);
        paymentDue.setHorizontalAlignment(Element.ALIGN_CENTER);
        paymentDue.setBorderWidthTop(Float.valueOf("0.5"));
        paymentDue.setBorderWidthRight(Float.valueOf("0.5"));
        paymentDue.setBorderWidthBottom(Float.valueOf("0.5"));
        tablePayment.addCell(paymentDue);

        pdfObject.add(tablePayment);


    }

    private void createInvoiceRecipientsTable(Hashtable<String, Font> fontList, Paragraph pdfObject, ArrayList<String> list) {
        PdfPTable tableSellerLabel = new PdfPTable(5);
        tableSellerLabel.setWidthPercentage(100);
        String header = list.get(0);

        PdfPCell sellerLabel = new PdfPCell(new Phrase(header, fontList.get("font9")));
        sellerLabel.setHorizontalAlignment(Element.ALIGN_CENTER);
        sellerLabel.setVerticalAlignment(Element.ALIGN_MIDDLE);
        sellerLabel.setBackgroundColor(Color.LIGHT_GRAY);
        tableSellerLabel.addCell(sellerLabel);

        PdfPCell blank = new PdfPCell(new Phrase(""));
        blank.setBorder(Rectangle.NO_BORDER);
        tableSellerLabel.addCell(blank);
        tableSellerLabel.addCell(blank);
        tableSellerLabel.addCell(blank);
        tableSellerLabel.addCell(blank);

        PdfPTable sellerTable = new PdfPTable(1);
        sellerTable.setWidthPercentage(100);

        String name, address, nip, bank = null, account = null;
        if (list.size() == 6) {
            name = list.get(1);
            address = list.get(2);
            nip = list.get(3);
            bank = list.get(4);
            account = list.get(5);
        } else {
            name = list.get(1);
            address = list.get(2);
            nip = list.get(3);
        }

        PdfPCell seller1 = new PdfPCell(new Phrase(name, fontList.get("fontBold10")));
        seller1.setBorder(Rectangle.NO_BORDER);
        seller1.setBorderWidthTop(Float.valueOf("0.5"));
        seller1.setBorderWidthLeft(Float.valueOf("0.5"));
        seller1.setBorderWidthRight(Float.valueOf("0.5"));

        PdfPCell seller2 = new PdfPCell(new Phrase(address, fontList.get("fontBold10")));
        seller2.setBorder(Rectangle.NO_BORDER);
        seller2.setBorderWidthLeft(Float.valueOf("0.5"));
        seller2.setBorderWidthRight(Float.valueOf("0.5"));

        PdfPCell seller3 = new PdfPCell(new Phrase(nip, fontList.get("fontBold10")));
        seller3.setBorder(Rectangle.NO_BORDER);
        seller3.setBorderWidthLeft(Float.valueOf("0.5"));
        seller3.setBorderWidthRight(Float.valueOf("0.5"));

        if (list.size() == 4) {
            seller3.setBorderWidthBottom(Float.valueOf("0.5"));
        }

        sellerTable.addCell(seller1);
        sellerTable.addCell(seller2);
        sellerTable.addCell(seller3);

        if (list.size() == 6) {
            PdfPCell seller4 = new PdfPCell(new Phrase(bank, fontList.get("fontBold10")));
            seller4.setBorder(Rectangle.NO_BORDER);
            seller4.setBorderWidthLeft(Float.valueOf("0.5"));
            seller4.setBorderWidthRight(Float.valueOf("0.5"));
            sellerTable.addCell(seller4);

            PdfPCell seller5 = new PdfPCell(new Phrase(account, fontList.get("fontBold10")));
            seller5.setBorder(Rectangle.NO_BORDER);
            seller5.setBorderWidthLeft(Float.valueOf("0.5"));
            seller5.setBorderWidthRight(Float.valueOf("0.5"));
            seller5.setBorderWidthBottom(Float.valueOf("0.5"));
            sellerTable.addCell(seller5);
        }

        pdfObject.add(tableSellerLabel);
        pdfObject.add(sellerTable);
    }

    private void createInvoiceDataTable(Hashtable<String, Font> fontList, Paragraph pdfObject, Invoice invoiceObj, Invoice invoiceObj2) throws ParseException {
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);

        PdfPCell where = new PdfPCell(new Phrase("Miejsce wystawienia:", fontList.get("font9")));
        where.setMinimumHeight(20);
        where.setBorder(Rectangle.NO_BORDER);
        where.setBorderWidthBottom(Float.valueOf("0.5"));
        where.setBorderWidthLeft(Float.valueOf("0.5"));
        where.setBorderWidthTop(Float.valueOf("0.5"));
        where.setVerticalAlignment(Element.ALIGN_MIDDLE);
        where.setHorizontalAlignment(Element.ALIGN_CENTER);
        where.setBackgroundColor(Color.LIGHT_GRAY);
        table.addCell(where);

        PdfPCell town = new PdfPCell(new Phrase("Nowe Miasteczko", fontList.get("font10")));
        town.setBorder(Rectangle.NO_BORDER);
        town.setBorderWidthBottom(Float.valueOf("0.5"));
        town.setBorderWidthRight(Float.valueOf("0.5"));
        town.setBorderWidthTop(Float.valueOf("0.5"));
        town.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(town);

        PdfPCell blank = new PdfPCell(new Phrase(""));
        blank.setBorder(Rectangle.NO_BORDER);
        table.addCell(blank);

        PdfPCell when = new PdfPCell(new Phrase("Data wystawienia:", fontList.get("font9")));
        when.setVerticalAlignment(Element.ALIGN_MIDDLE);
        when.setHorizontalAlignment(Element.ALIGN_CENTER);
        when.setBackgroundColor(Color.LIGHT_GRAY);
        when.setBorder(Rectangle.NO_BORDER);
        when.setBorderWidthBottom(Float.valueOf("0.5"));
        when.setBorderWidthLeft(Float.valueOf("0.5"));
        when.setBorderWidthTop(Float.valueOf("0.5"));
        table.addCell(when);

        PdfPCell date = new PdfPCell(new Phrase(String.valueOf(invoiceObj.getDate_printed()), fontList.get("font10")));
        date.setVerticalAlignment(Element.ALIGN_MIDDLE);
        date.setBorder(Rectangle.NO_BORDER);
        date.setBorderWidthBottom(Float.valueOf("0.5"));
        date.setBorderWidthRight(Float.valueOf("0.5"));
        date.setBorderWidthTop(Float.valueOf("0.5"));
        table.addCell(date);

        PdfPTable table2 = new PdfPTable(4);
        table2.setWidthPercentage(100);

        table2.addCell(blank);

        PdfPCell invoice = new PdfPCell(new Phrase("KOREKTA FAKTURY VAT nr", fontList.get("fontBold11")));
        invoice.setMinimumHeight(25);
        invoice.setBorder(Rectangle.NO_BORDER);
        invoice.setBorderWidthBottom(Float.valueOf("0.5"));
        invoice.setBorderWidthLeft(Float.valueOf("0.5"));
        invoice.setBorderWidthTop(Float.valueOf("0.5"));
        invoice.setVerticalAlignment(Element.ALIGN_MIDDLE);
        invoice.setHorizontalAlignment(Element.ALIGN_CENTER);
        invoice.setBackgroundColor(Color.LIGHT_GRAY);
        table2.addCell(invoice);

        PdfPCell invoiceNo = new PdfPCell(new Phrase(invoiceObj.getInvoice_number(), fontList.get("fontBold11")));
        invoiceNo.setVerticalAlignment(Element.ALIGN_MIDDLE);
        invoiceNo.setHorizontalAlignment(Element.ALIGN_CENTER);
        invoiceNo.setBorder(Rectangle.NO_BORDER);
        invoiceNo.setBorderWidthBottom(Float.valueOf("0.5"));
        invoiceNo.setBorderWidthRight(Float.valueOf("0.5"));
        invoiceNo.setBorderWidthTop(Float.valueOf("0.5"));
        table2.addCell(invoiceNo);

        table2.addCell(blank);
        /*table2.addCell(blank);

        PdfPCell invoiceOld = new PdfPCell(new Phrase("DO FAKTURY VAT nr", fontList.get("font11")));
        invoiceOld.setMinimumHeight(25);
        invoiceOld.setBorder(Rectangle.NO_BORDER);
        invoiceOld.setBorderWidthBottom(Float.valueOf("0.5"));
        invoiceOld.setBorderWidthLeft(Float.valueOf("0.5"));
        invoiceOld.setBorderWidthTop(Float.valueOf("0.5"));
        invoiceOld.setVerticalAlignment(Element.ALIGN_MIDDLE);
        invoiceOld.setHorizontalAlignment(Element.ALIGN_CENTER);
        table2.addCell(invoiceOld);

        PdfPCell invoiceNoOld = new PdfPCell(new Phrase(invoiceObj2.getInvoice_number(), fontList.get("fontBold11")));
        invoiceNoOld.setVerticalAlignment(Element.ALIGN_MIDDLE);
        invoiceNoOld.setHorizontalAlignment(Element.ALIGN_CENTER);
        invoiceNoOld.setBorder(Rectangle.NO_BORDER);
        invoiceNoOld.setBorderWidthBottom(Float.valueOf("0.5"));
        invoiceNoOld.setBorderWidthRight(Float.valueOf("0.5"));
        invoiceNoOld.setBorderWidthTop(Float.valueOf("0.5"));
        table2.addCell(invoiceNoOld);

        table2.addCell(blank);*/

        pdfObject.add(table);
        addEmptyLine(pdfObject, 1, fontList);
        pdfObject.add(table2);

    }

    private void createHeaderTable(Hashtable<String, Font> fonts, Paragraph pdfObject) {
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);

        PdfPCell company = new PdfPCell(new Phrase("Artykuły Techniczne Motoryzacyjne i Elektryczne Dorota Świtała", fonts.get("fontBold11")));
        company.setHorizontalAlignment(Element.ALIGN_CENTER);
        company.setBorder(Rectangle.NO_BORDER);
        company.setBackgroundColor(Color.LIGHT_GRAY);
        table.addCell(company);

        PdfPCell address = new PdfPCell(new Phrase("ul. Kościelna 1 A ,                67-124 Nowe Miasteczko,      NIP: 925-100-13-84", fonts.get("fontBold11")));
        address.setHorizontalAlignment(Element.ALIGN_CENTER);
        address.setBorder(Rectangle.NO_BORDER);
        address.setBackgroundColor(Color.LIGHT_GRAY);
        table.addCell(address);

        PdfPCell contact = new PdfPCell(new Phrase("tel. 68 388 84 54, kom. 507 036 056    							     email: artechel@wp.pl						", fonts.get("fontBold11")));
        contact.setHorizontalAlignment(Element.ALIGN_CENTER);
        contact.setBorder(Rectangle.NO_BORDER);
        contact.setBackgroundColor(Color.LIGHT_GRAY);
        table.addCell(contact);

        pdfObject.add(table);

    }

    private void addEmptyLine(Paragraph paragraph, int number, Hashtable<String, Font> list) {
        PdfPTable table = new PdfPTable(1);
        for (int i = 0; i < number; i++) {
            table.setWidthPercentage(100);
            PdfPCell blank = new PdfPCell(new Phrase(" ", list.get("font5")));
            blank.setFixedHeight(10);
            blank.setBorder(Rectangle.NO_BORDER);
            table.addCell(blank);
        }
        paragraph.add(table);
    }

    private Hashtable<String, Font> getFonts() {
        BaseFont helvetica = null;
        try {
            helvetica = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);

        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
        Hashtable<String, Font> fontList = new Hashtable<>();
        fontList.put("fontBold12", new Font(helvetica, 12, Font.BOLD));
        fontList.put("fontBold11", new Font(helvetica, 11, Font.BOLD));
        fontList.put("fontBold10", new Font(helvetica, 10, Font.BOLD));
        fontList.put("fontBold9", new Font(helvetica, 9, Font.BOLD));
        fontList.put("font10", new Font(helvetica, 10));
        fontList.put("font9", new Font(helvetica, 9));
        fontList.put("font5", new Font(helvetica, 1));

        return fontList;
    }

    private String createWrittenValue(String invoiceValue) {
        String valueObj = invoiceValue;
        ArrayList<String> list = new ArrayList<>();
        String letters = String.valueOf(invoiceValue.charAt(0));
        String character;
        if(invoiceValue.contains(",")){
            character = ",";
        } else {
            character = ".";
        }
        if (letters.equals("-")) {
            list.add(invoiceValue.substring(1, invoiceValue.indexOf(character)));
            list.add(invoiceValue.substring(invoiceValue.indexOf(character) + 1));
        } else {
            list.add(invoiceValue.substring(0, invoiceValue.indexOf(character)));
            list.add(invoiceValue.substring(invoiceValue.indexOf(character) + 1));
        }

        JSONObject object = new JSONObject("{\n" +
                "  \"0\": \"\",\n" +
                "  \"1\": \"jeden\",\n" +
                "  \"2\": \"dwa\",\n" +
                "  \"3\": \"trzy\",\n" +
                "  \"4\": \"cztery\",\n" +
                "  \"5\": \"pięć\",\n" +
                "  \"6\": \"sześć\",\n" +
                "  \"7\": \"siedem\",\n" +
                "  \"8\": \"osiem\",\n" +
                "  \"9\": \"dziewięć\",\n" +
                "  \"00\": \"\",\n" +
                "  \"10\": \"dziesięć\",\n" +
                "  \"11\": \"jedenaście\",\n" +
                "  \"12\": \"dwanaście\",\n" +
                "  \"13\": \"trzynaście\",\n" +
                "  \"14\": \"czternaście\",\n" +
                "  \"15\": \"piętnaście\",\n" +
                "  \"16\": \"szesnaście\",\n" +
                "  \"17\": \"siedemnaście\",\n" +
                "  \"18\": \"osiemnaście\",\n" +
                "  \"19\": \"dziewiętnaście\",\n" +
                "  \"20\": \"dwadzieścia\",\n" +
                "  \"30\": \"trzydzieści\",\n" +
                "  \"40\": \"czterdzieści\",\n" +
                "  \"50\": \"pięćdziesiąt\",\n" +
                "  \"60\": \"sześćdziesiąt\",\n" +
                "  \"70\": \"siedemdziesiąt\",\n" +
                "  \"80\": \"osiemdziesiąt\",\n" +
                "  \"90\": \"dziewięćdziesiąt\",\n" +
                "  \"100\": \"sto\",\n" +
                "  \"200\": \"dwieście\",\n" +
                "  \"300\": \"trzysta\",\n" +
                "  \"400\": \"czterysta\",\n" +
                "  \"500\": \"pięćset\",\n" +
                "  \"600\": \"sześćset\",\n" +
                "  \"700\": \"siedemset\",\n" +
                "  \"800\": \"osiemset\",\n" +
                "  \"900\": \"dziewięćset\",\n" +
                "  \"1000\": \"tysiąc\",\n" +
                "  \"2000\": \"dwa tysiące\",\n" +
                "  \"3000\": \"trzy tysiące\",\n" +
                "  \"4000\": \"cztery tysiące\",\n" +
                "  \"5000\": \"pięć tysięcy\",\n" +
                "  \"6000\": \"sześć tysięcy\",\n" +
                "  \"7000\": \"siedem tysięcy\",\n" +
                "  \"8000\": \"osiem tysięcy\",\n" +
                "  \"9000\": \"dziewięć tysięcy\",\n" +
                "  \"10000\": \"dziesięć tysięcy\"\n" +
                "}");


        /*
        System.out.println(valueObj.indexOf("."));
        System.out.println(list.get(0));
        System.out.println(list.get(1) + "/100");
        System.out.println("--------------");
         */
        String letter;
        if (list.get(0).length() > 1) {
            letter = String.valueOf(list.get(0).charAt(list.get(0).length() - 2));
        } else {
            letter = "0";
        }
        StringBuilder writtenValue = null;

        if (letter.equals("1")) {
            for (int i = 0; i < list.get(0).length() - 2; i++) {
                writtenValue = getStringBuilder(list, object, writtenValue, i);
            }
            String end = list.get(0).substring(list.get(0).length() - 2);
            assert writtenValue != null;
            writtenValue.append(object.get(end)).append(" ");
            //System.out.println(end);
        } else {
            for (int i = 0; i < list.get(0).length(); i++) {
                writtenValue = getStringBuilder(list, object, writtenValue, i);
            }
        }
        assert writtenValue != null;
        writtenValue.append("zł ").append(list.get(1)).append("/100");
        //System.out.println(writtenValue);
        return String.valueOf(writtenValue);
    }

    private StringBuilder getStringBuilder(ArrayList<String> list, JSONObject object, StringBuilder writtenValue, int i) {
        String number = list.get(0).charAt(i) + StringUtils.repeat("0", (list.get(0).length() - 1) - i);
        System.out.println(number);
        if (writtenValue == null) {
            writtenValue = new StringBuilder(object.get(number) + " ");
        } else {
            writtenValue.append(object.get(number)).append(" ");
        }
        return writtenValue;
    }

}
