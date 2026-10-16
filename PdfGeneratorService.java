package com.example.medcert.service;

import com.example.medcert.model.MedicalCertificate;
import com.example.medcert.model.Patient;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

@Service
public class PdfGeneratorService {

    public byte[] generateMedicalCertificatePdf(MedicalCertificate medCert) throws IOException {
        Patient patient = medCert.getPatient();
        
        // Gumawa ng bagong PDF Document
        try (PDDocument document = new PDDocument(); ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PDPage page = new PDPage(PDRectangle.LETTER); // Standard Letter size
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                
                // Fonts (PDFBox 3.x Standard 14 Fonts)
                PDType1Font fontBold = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
                PDType1Font fontRegular = new PDType1Font(Standard14Fonts.FontName.HELVETICA);

                // --- HEADER SECTION ---
                contentStream.beginText();
                contentStream.setFont(fontBold, 14);
                contentStream.setLeading(18.0f);
                contentStream.newLineAtAddress(50, 730);
                contentStream.showText("CLINIC MANAGEMENT OFFICE");
                contentStream.newLine();
                
                contentStream.setFont(fontRegular, 10);
                contentStream.showText("Official Medical Certificate");
                contentStream.newLine();
                contentStream.showText("---------------------------------------------------------------------------------------------------------");
                contentStream.endText();

                // --- CERTIFICATE DETAILS ---
                contentStream.beginText();
                contentStream.setFont(fontBold, 11);
                contentStream.newLineAtAddress(50, 670);
                contentStream.showText("Certificate No: " + medCert.getCertificateNumber());
                contentStream.newLine();
                
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy - hh:mm a");
                contentStream.setFont(fontRegular, 10);
                contentStream.showText("Date Issued: " + medCert.getConsultationDate().format(formatter));
                contentStream.endText();

                // --- PATIENT INFORMATION ---
                contentStream.beginText();
                contentStream.setFont(fontBold, 12);
                contentStream.newLineAtAddress(50, 610);
                contentStream.showText("PATIENT INFORMATION");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(fontRegular, 11);
                contentStream.setLeading(20.0f);
                contentStream.newLineAtAddress(50, 585);
                
                contentStream.showText("Full Name: " + patient.getLastName() + ", " + patient.getFirstName());
                contentStream.newLine();
                contentStream.showText("Student/Employee ID: " + patient.getStudentEmployeeId());
                contentStream.newLine();
                contentStream.showText("Department / Course: " + (patient.getDepartmentCourse() != null ? patient.getDepartmentCourse() : "N/A"));
                contentStream.endText();

                // --- MEDICAL FINDINGS & DIAGNOSIS ---
                contentStream.beginText();
                contentStream.setFont(fontBold, 12);
                contentStream.newLineAtAddress(50, 505);
                contentStream.showText("MEDICAL FINDINGS & RECOMMENDATIONS");
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(fontRegular, 11);
                contentStream.setLeading(20.0f);
                contentStream.newLineAtAddress(50, 480);
                contentStream.showText("Diagnosis: " + medCert.getDiagnosis());
                contentStream.newLine();
                contentStream.showText("Fitness Status: " + (medCert.getFitStatus() != null ? medCert.getFitStatus() : "Fit to return"));
                contentStream.newLine();
                contentStream.showText("Recommendations / Remarks:");
                contentStream.newLine();
                contentStream.showText("- " + (medCert.getRecommendations() != null ? medCert.getRecommendations() : "None"));
                contentStream.endText();

                // --- SIGNATURE SECTION ---
                contentStream.beginText();
                contentStream.setFont(fontRegular, 11);
                contentStream.newLineAtAddress(380, 250);
                contentStream.showText("Certified by:");
                contentStream.newLine();
                contentStream.newLine();
                contentStream.setFont(fontBold, 11);
                contentStream.showText("___________________________________");
                contentStream.newLine();
                contentStream.setFont(fontRegular, 10);
                contentStream.showText("Attending Nurse / Physician");
                contentStream.newLine();
                contentStream.showText("License No: _______________");
                contentStream.endText();
            }

            // Isulat ang document sa byte array para maibalik ng Controller bilang HTTP response
            document.save(baos);
            return baos.toByteArray();
        }
    }
}
