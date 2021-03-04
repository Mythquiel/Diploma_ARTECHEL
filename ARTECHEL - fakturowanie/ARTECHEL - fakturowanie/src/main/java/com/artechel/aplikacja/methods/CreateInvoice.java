package com.artechel.aplikacja.methods;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class CreateInvoice {

    public Date getToday() throws ParseException {
        LocalDate today = java.time.LocalDate.now();
        return new SimpleDateFormat("yyyy-MM-dd").parse(today.toString());
    }

    public Date getTwoWeeksFromToday() throws ParseException {
        LocalDate today = LocalDate.now();
        LocalDate twoWeeksFromNow = today.plus(2, ChronoUnit.WEEKS);
        return new SimpleDateFormat("yyyy-MM-dd").parse(twoWeeksFromNow.toString());
    }

    public String createPrintout(String invoice_number) {
        String path = null;

        return path;
    }

    public String getCurrentInvoiceNumber(String invoice) {
        String lastInvoiceNumber = invoice.substring(0, 2);
        String lastInvoiceYear = invoice.substring(3);
        LocalDate today = LocalDate.now();
        String thisYear = String.valueOf(today.getYear());
        if (!lastInvoiceYear.equals(thisYear)) {
            return "01/" + thisYear;
        } else {
            int newIndex = Integer.valueOf(lastInvoiceNumber) + 1;
            if (newIndex < 10) {
                return "0" + newIndex + "/" + thisYear;
            } else {
                return newIndex + "/" + thisYear;
            }
        }
    }

    public String getCurrentCorrectNumber(String invoice, String correctedInvoice) {
        String lastInvoiceNumber = invoice.substring(0,2);
        int newIndex =  Integer.valueOf(lastInvoiceNumber)+1;
        if(newIndex < 10){
            return "0"+newIndex+"/"+correctedInvoice+"KFV";
        } else {
            return newIndex+"/"+correctedInvoice+"KFV";
        }
    }


    public String getCurrentSavedInvoiceNumber(String invoice) {
        String lastInvoiceNumber = invoice.substring(0, 2);
        String end = invoice.substring(3);
        int newIndex = Integer.valueOf(lastInvoiceNumber) + 1;
        if (newIndex < 10) {
            return "0" + newIndex + "/" + end;
        } else {
            return newIndex + "/" + end;
        }
    }
}
