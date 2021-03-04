package com.artechel.aplikacja.methods;

import com.artechel.aplikacja.model.Customer;
import com.artechel.aplikacja.model.Invoice;
import com.artechel.aplikacja.model.Invoice_product;
import com.artechel.aplikacja.model.Payer;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import static java.lang.Math.abs;

public class ReportPDF extends AbstractPdfView {

    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter pdfWriter, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        List<Invoice> invoiceList = (List<Invoice>) model.get("invoicesList");

        addMetaData(document);
        addContent(document, invoiceList);
    }

    private void addMetaData(Document document) {
        document.addTitle("Zestawienie faktur");
        document.addAuthor("Artykuly Techniczne Motoryzacyjne i Elektryczne Dorota Świtała");
    }

    private void addContent(Document document, List<Invoice> invoices) throws DocumentException, ParseException {

        ArrayList<Invoice> korekty = new ArrayList<>();
        ArrayList<Invoice> wystawione = new ArrayList<>();
        ArrayList<Invoice> zapisane = new ArrayList<>();
        for (Invoice invoice : invoices) {
            if (invoice.getInvoice_number().endsWith("KFV")) {
                korekty.add(invoice);
            } else if (invoice.getInvoice_number().endsWith("zapis")) {
                zapisane.add(invoice);
            } else {
                wystawione.add(invoice);
            }
        }

        Paragraph pdfObject = new Paragraph();

        createHeaderTable(getFonts(), pdfObject);
        addEmptyLine(pdfObject, 3, getFonts());

        createDateAndPlace(getFonts(), pdfObject);
        addEmptyLine(pdfObject, 1, getFonts());

        createInvoicesHeadersTable(getFonts(), pdfObject);
        if (!wystawione.isEmpty()) {
            createInvoicesTable(getFonts(), pdfObject, wystawione, "Faktury VAT", invoices);
        }
        if (!korekty.isEmpty()) {
            createInvoicesTable(getFonts(), pdfObject, korekty, "Korekty faktur", invoices);
            addEmptyLine(pdfObject, 3, getFonts());
        }
        createSumTable(getFonts(), pdfObject, invoices);
        addEmptyLine(pdfObject, 6, getFonts());

        if (!zapisane.isEmpty()) {
            createInvoicesHeadersTable(getFonts(), pdfObject);
            createInvoicesTable(getFonts(), pdfObject, zapisane, "Zapisane faktury", invoices);
            addEmptyLine(pdfObject, 3, getFonts());
        }


        document.add(pdfObject);


    }

    private void createInvoicesHeadersTable(Hashtable<String, Font> fontList, Paragraph pdfObject) {
        PdfPTable tableInvoices = new PdfPTable(new float[]{5, 10, 10, 19, 19, 9, 10, 9, 9});//9
        tableInvoices.setWidthPercentage(100);
        PdfPCell lpLabel = new PdfPCell(new Phrase("Lp.", fontList.get("fontBold8")));
        lpLabel.setBackgroundColor(Color.LIGHT_GRAY);
        lpLabel.setHorizontalAlignment(Element.ALIGN_CENTER);
        lpLabel.setVerticalAlignment(Element.ALIGN_MIDDLE);
        lpLabel.setMinimumHeight(25);
        tableInvoices.addCell(lpLabel);

        PdfPCell invoiceNumberLabel = new PdfPCell(new Phrase("Nr faktury", fontList.get("fontBold8")));
        invoiceNumberLabel.setBackgroundColor(Color.LIGHT_GRAY);
        invoiceNumberLabel.setHorizontalAlignment(Element.ALIGN_CENTER);
        invoiceNumberLabel.setVerticalAlignment(Element.ALIGN_MIDDLE);
        tableInvoices.addCell(invoiceNumberLabel);

        PdfPCell datePrintedLabel = new PdfPCell(new Phrase("Data wystawienia", fontList.get("fontBold8")));
        datePrintedLabel.setBackgroundColor(Color.LIGHT_GRAY);
        datePrintedLabel.setHorizontalAlignment(Element.ALIGN_CENTER);
        datePrintedLabel.setVerticalAlignment(Element.ALIGN_MIDDLE);
        tableInvoices.addCell(datePrintedLabel);

        PdfPCell buyerLabel = new PdfPCell(new Phrase("Nabywca", fontList.get("fontBold8")));
        buyerLabel.setBackgroundColor(Color.LIGHT_GRAY);
        buyerLabel.setHorizontalAlignment(Element.ALIGN_CENTER);
        buyerLabel.setVerticalAlignment(Element.ALIGN_MIDDLE);
        tableInvoices.addCell(buyerLabel);

        PdfPCell receiverLabel = new PdfPCell(new Phrase("Odbiorca", fontList.get("fontBold8")));
        receiverLabel.setBackgroundColor(Color.LIGHT_GRAY);
        receiverLabel.setHorizontalAlignment(Element.ALIGN_CENTER);
        receiverLabel.setVerticalAlignment(Element.ALIGN_MIDDLE);
        tableInvoices.addCell(receiverLabel);

        PdfPCell paymentLabel = new PdfPCell(new Phrase("Forma płatności", fontList.get("fontBold8")));
        paymentLabel.setBackgroundColor(Color.LIGHT_GRAY);
        paymentLabel.setHorizontalAlignment(Element.ALIGN_CENTER);
        paymentLabel.setVerticalAlignment(Element.ALIGN_MIDDLE);
        tableInvoices.addCell(paymentLabel);

        PdfPCell paymentDueLabel = new PdfPCell(new Phrase("Termin płatności", fontList.get("fontBold8")));
        paymentDueLabel.setBackgroundColor(Color.LIGHT_GRAY);
        paymentDueLabel.setHorizontalAlignment(Element.ALIGN_CENTER);
        paymentDueLabel.setVerticalAlignment(Element.ALIGN_MIDDLE);
        tableInvoices.addCell(paymentDueLabel);

        PdfPCell quantityLabel = new PdfPCell(new Phrase("Wartość netto", fontList.get("fontBold8")));
        quantityLabel.setBackgroundColor(Color.LIGHT_GRAY);
        quantityLabel.setHorizontalAlignment(Element.ALIGN_CENTER);
        quantityLabel.setVerticalAlignment(Element.ALIGN_MIDDLE);
        tableInvoices.addCell(quantityLabel);

        PdfPCell valueBruttoLabel = new PdfPCell(new Phrase("Wartość brutto", fontList.get("fontBold8")));
        valueBruttoLabel.setBackgroundColor(Color.LIGHT_GRAY);
        valueBruttoLabel.setHorizontalAlignment(Element.ALIGN_CENTER);
        valueBruttoLabel.setVerticalAlignment(Element.ALIGN_MIDDLE);
        tableInvoices.addCell(valueBruttoLabel);

        pdfObject.add(tableInvoices);
    }

    private void createInvoicesTable(Hashtable<String, Font> fontList, Paragraph pdfObject, List<Invoice> invoicesList, String headers, List<Invoice> invoicesAll) {
        PdfPTable tableInvoices = new PdfPTable(new float[]{5, 10, 10, 19, 19, 9, 10, 9, 9});//9
        tableInvoices.setWidthPercentage(100);
        //--invoices--
        PdfPCell header = new PdfPCell(new Phrase(headers, fontList.get("font8")));
        header.setColspan(9);
        header.setHorizontalAlignment(Element.ALIGN_LEFT);
        header.setVerticalAlignment(Element.ALIGN_MIDDLE);
        header.setMinimumHeight(15);
        tableInvoices.addCell(header);

        for (int i = 0; i < invoicesList.size(); i++) {
            PdfPCell lp = new PdfPCell(new Phrase(String.valueOf(i + 1), fontList.get("font8")));
            lp.setHorizontalAlignment(Element.ALIGN_CENTER);
            lp.setVerticalAlignment(Element.ALIGN_MIDDLE);
            lp.setMinimumHeight(15);
            tableInvoices.addCell(lp);

            PdfPCell invoiceNo = new PdfPCell(new Phrase(invoicesList.get(i).getInvoice_number(), fontList.get("font8")));
            invoiceNo.setHorizontalAlignment(Element.ALIGN_CENTER);
            invoiceNo.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tableInvoices.addCell(invoiceNo);

            String dateP;
            if (invoicesList.get(i).getDate_printed() == null) {
                dateP = "";
            } else {
                dateP = invoicesList.get(i).getDate_printed().toString();
            }
            PdfPCell date = new PdfPCell(new Phrase(dateP, fontList.get("font8")));
            date.setHorizontalAlignment(Element.ALIGN_CENTER);
            date.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tableInvoices.addCell(date);

            PdfPCell buyer = new PdfPCell(new Phrase(invoicesList.get(i).getCustomer().getName(), fontList.get("font8")));
            buyer.setHorizontalAlignment(Element.ALIGN_CENTER);
            buyer.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tableInvoices.addCell(buyer);

            String customerO;
            if (invoicesList.get(i).getPayer() == null) {
                customerO = "";
            } else {
                customerO = invoicesList.get(i).getPayer().getName();
            }
            PdfPCell customer = new PdfPCell(new Phrase(customerO, fontList.get("font8")));
            customer.setHorizontalAlignment(Element.ALIGN_CENTER);
            customer.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tableInvoices.addCell(customer);


            PdfPCell payment = new PdfPCell(new Phrase(invoicesList.get(i).getPayment(), fontList.get("font8")));
            payment.setHorizontalAlignment(Element.ALIGN_CENTER);
            payment.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tableInvoices.addCell(payment);

            PdfPCell paymentDue = new PdfPCell(new Phrase(invoicesList.get(i).getPayment_due().toString(), fontList.get("font8")));
            paymentDue.setHorizontalAlignment(Element.ALIGN_CENTER);
            paymentDue.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tableInvoices.addCell(paymentDue);

            DecimalFormat df = new DecimalFormat("0.00");
            float valueNetto, valueBrutto;

            for (Invoice invoice : invoicesList) {
                valueNetto = 0;
                valueBrutto = 0;
                if (!invoice.getInvoice_number().endsWith("KFV") || !invoice.getInvoice_number().endsWith("zapis")) {
                    String number = invoice.getInvoice_number();
                    for (Invoice inv : invoicesAll) {
                        if (inv.getInvoice_number().contains(number) && inv.getInvoice_number().endsWith("KFV")) {
                            valueNetto = abs(inv.getValue_netto()) + invoice.getValue_netto();
                            valueBrutto = abs(inv.getValue_brutto()) + invoice.getValue_brutto();
                        }
                    }
                    if (valueNetto == 0) {
                        valueNetto = invoice.getValue_netto();
                    }
                    if (valueBrutto == 0) {
                        valueBrutto = invoice.getValue_brutto();
                    }
                }

                String nettoValueFixed = df.format(valueNetto);
                PdfPCell nettoValue = new PdfPCell(new Phrase(String.valueOf(nettoValueFixed), fontList.get("font8")));
                nettoValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                nettoValue.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tableInvoices.addCell(nettoValue);

                String bruttoValueFixed = df.format(valueBrutto);
                PdfPCell bruttoValue = new PdfPCell(new Phrase(String.valueOf(bruttoValueFixed), fontList.get("font8")));
                bruttoValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                bruttoValue.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tableInvoices.addCell(bruttoValue);

            }
            pdfObject.add(tableInvoices);

        }
    }

    private void createSumTable(Hashtable<String, Font> fontList, Paragraph
            pdfObject, List<Invoice> invoiceList) {

        float sumNetto = 0, sumBrutto = 0;
        DecimalFormat df = new DecimalFormat("0.00");

        for (Invoice invoice : invoiceList) {
            String number = invoice.getInvoice_number();
            if (number.contains("KFV") || number.contains("zapis")) {
            } else {
                sumNetto += invoice.getValue_netto();
                sumBrutto += invoice.getValue_brutto();
            }
        }

        PdfPTable tableSumLabel = new PdfPTable(1);
        tableSumLabel.setWidthPercentage(45);
        tableSumLabel.setHorizontalAlignment(Element.ALIGN_RIGHT);

        PdfPCell title = new PdfPCell(new Phrase("RAZEM", fontList.get("fontBold9")));
        title.setHorizontalAlignment(Element.ALIGN_CENTER);
        title.setVerticalAlignment(Element.ALIGN_MIDDLE);
        title.setMinimumHeight(20);
        tableSumLabel.addCell(title);

        //--nagłówki--
        PdfPTable tableSum = new PdfPTable(2);//5
        tableSum.setWidthPercentage(45);
        tableSum.setHorizontalAlignment(Element.ALIGN_RIGHT);


        PdfPCell nettoLabel = new PdfPCell(new Phrase("Netto", fontList.get("fontBold9")));
        nettoLabel.setHorizontalAlignment(Element.ALIGN_CENTER);
        nettoLabel.setBackgroundColor(Color.LIGHT_GRAY);
        nettoLabel.setMinimumHeight(20);
        tableSum.addCell(nettoLabel);

        PdfPCell bruttoLabel = new PdfPCell(new Phrase("Brutto", fontList.get("fontBold9")));
        bruttoLabel.setBackgroundColor(Color.LIGHT_GRAY);
        bruttoLabel.setHorizontalAlignment(Element.ALIGN_CENTER);
        bruttoLabel.setMinimumHeight(20);
        tableSum.addCell(bruttoLabel);

        //--wartości--
        String sumNettofixed = df.format(sumNetto);
        PdfPCell sumAllNetto = new PdfPCell(new Phrase(sumNettofixed, fontList.get("fontBold10")));
        sumAllNetto.setHorizontalAlignment(Element.ALIGN_CENTER);
        sumAllNetto.setMinimumHeight(20);
        tableSum.addCell(sumAllNetto);

        String sumVATfixed = df.format(sumBrutto);
        PdfPCell sumAllBrutto = new PdfPCell(new Phrase(sumVATfixed, fontList.get("fontBold10")));
        sumAllBrutto.setHorizontalAlignment(Element.ALIGN_CENTER);
        sumAllBrutto.setMinimumHeight(20);
        tableSum.addCell(sumAllBrutto);


        pdfObject.add(tableSumLabel);
        pdfObject.add(tableSum);

    }

    private void createDateAndPlace(Hashtable<String, Font> fontList, Paragraph pdfObject) throws ParseException {
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

        LocalDate today = java.time.LocalDate.now();
        PdfPCell date = new PdfPCell(new Phrase(String.valueOf(today), fontList.get("font10")));
        date.setVerticalAlignment(Element.ALIGN_MIDDLE);
        date.setBorder(Rectangle.NO_BORDER);
        date.setBorderWidthBottom(Float.valueOf("0.5"));
        date.setBorderWidthRight(Float.valueOf("0.5"));
        date.setBorderWidthTop(Float.valueOf("0.5"));
        table.addCell(date);

        pdfObject.add(table);
        addEmptyLine(pdfObject, 1, fontList);

    }

    private void createHeaderTable(Hashtable<String, Font> fonts, Paragraph pdfObject) {
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);

        PdfPCell header = new PdfPCell(new Phrase("ARTECHEL - ZESTAWIENIE FAKTUR", fonts.get("fontBold12")));
        header.setHorizontalAlignment(Element.ALIGN_CENTER);
        header.setMinimumHeight(60);
        header.setVerticalAlignment(Element.ALIGN_MIDDLE);
        header.setBorder(Rectangle.NO_BORDER);
        header.setBackgroundColor(Color.LIGHT_GRAY);
        table.addCell(header);

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
        fontList.put("fontBold8", new Font(helvetica, 8, Font.BOLD));
        fontList.put("font10", new Font(helvetica, 10));
        fontList.put("font9", new Font(helvetica, 9));
        fontList.put("font8", new Font(helvetica, 8));
        fontList.put("font5", new Font(helvetica, 1));

        return fontList;
    }
}
