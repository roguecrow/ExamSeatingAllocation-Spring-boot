package com.chainsys.examease.dao;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

import org.json.JSONObject;
import org.springframework.context.annotation.Configuration;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Configuration
public class HallTicketGenerator {

    public byte[] generateHallTicket(JSONObject data, String serialNumber) throws DocumentException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        Document document = new Document();
        PdfWriter.getInstance(document, baos);
        document.open();

        String examName = data.optString("exam_name", "Exam");
        String studentName = data.optString("name", "Student");
        String rollNumber = data.optString("roll_no", "000000");
        String dob = formatDate(data.optString("dob", "01-01-1970"));
        String address = data.optString("address", "Address");
        String examDate = formatDate(data.optString("exam_date", "01-01-1970"));
        String examTime = data.optString("exam_time", "10:00 AM");
        String examCity = data.optString("city", "City");
        String examVenue = data.optString("venue_name", "Venue");
        String examHall = data.optString("hall_name", "Hall");
        String seatNo = data.optString("allocated_seat", "");
        String examLocationAddress = data.optString("location_address", "");
        String profileImageBase64 = data.optString("passport_size_photo", "");
        String signatureImageBase64 = data.optString("digital_signature", "");

        Paragraph title = new Paragraph("Hall Ticket");
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(10f); 
        document.add(title);

        if (profileImageBase64 != null && !profileImageBase64.isEmpty()) {
            byte[] profileImageBytes = Base64.getDecoder().decode(profileImageBase64);
            Image profileImage = Image.getInstance(profileImageBytes);
            profileImage.setAlignment(Element.ALIGN_RIGHT | Element.ALIGN_TOP); 
            profileImage.scaleToFit(100, 100);
            profileImage.setAbsolutePosition(document.getPageSize().getWidth() - 120, document.getPageSize().getHeight() - 120);
            document.add(profileImage);
        }

        Paragraph spacing = new Paragraph();
        spacing.setSpacingAfter(100f); 
        document.add(spacing);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(20f);
        table.setSpacingAfter(20f); 

        addTableCell(table, "Name:", studentName);
        addTableCell(table, "Roll Number:", rollNumber);
        addTableCell(table, "Date of Birth:", dob);
        addTableCell(table, "Address:", address);
        addTableCell(table, "Exam Name:", examName);
        addTableCell(table, "Exam Date:", examDate);
        addTableCell(table, "Exam Time:", examTime);
        addTableCell(table, "Allocated City:", examCity);
        addTableCell(table, "Venue:", examVenue);
        addTableCell(table, "Hall:", examHall);
        addTableCell(table, "Seat No:", seatNo);
        addTableCell(table, "Location:", examLocationAddress);
        addTableCell(table, "Serial Number:", serialNumber);

        table.getDefaultCell().setBorder(Rectangle.BOX); 
        document.add(table);
        
        PdfPTable signatureTable = new PdfPTable(1);
        signatureTable.setWidthPercentage(100);

        PdfPCell invigilatorCell = new PdfPCell();
        invigilatorCell.setBorder(Rectangle.NO_BORDER);
        invigilatorCell.setPadding(10f);

        Phrase invigilatorPhrase = new Phrase("Invigilator Signature\nDate: " + examDate);
        invigilatorCell.addElement(invigilatorPhrase);

        PdfPCell invigilatorSignatureCell = new PdfPCell();
        invigilatorSignatureCell.setFixedHeight(40f); 
        invigilatorSignatureCell.setBorder(Rectangle.BOX);
        invigilatorSignatureCell.setPadding(5f); 
        invigilatorSignatureCell.setPaddingBottom(20f);

        signatureTable.addCell(invigilatorCell);
        signatureTable.addCell(invigilatorSignatureCell);
        
        if (signatureImageBase64 != null && !signatureImageBase64.isEmpty()) {
            byte[] signatureImageBytes = Base64.getDecoder().decode(signatureImageBase64);
            Image signatureImage = Image.getInstance(signatureImageBytes);
            signatureImage.setAlignment(Element.ALIGN_RIGHT | Element.ALIGN_BOTTOM); 
            signatureImage.scaleToFit(100, 50);
            signatureImage.setAbsolutePosition(document.getPageSize().getWidth() - 120, 50);
            document.add(signatureImage);
        }
        
        document.add(signatureTable);
        document.close();
        return baos.toByteArray();
    }

    private void addTableCell(PdfPTable table, String label, String value) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label));
        labelCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        labelCell.setBorder(Rectangle.BOX); 
        labelCell.setPadding(10f); 

        PdfPCell valueCell = new PdfPCell(new Phrase(value));
        valueCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        valueCell.setBorder(Rectangle.BOX);
        valueCell.setPadding(10f); 

        table.addCell(labelCell);
        table.addCell(valueCell);
    }

    private String formatDate(String dateString) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = inputFormat.parse(dateString);
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
            return outputFormat.format(date);
        } catch (Exception e) {
            return dateString; 
        }
    }
}
