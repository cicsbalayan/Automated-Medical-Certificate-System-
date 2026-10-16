package com.example.medcert.controller;

import com.example.medcert.model.MedicalCertificate;
import com.example.medcert.service.MedicalCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/certificates")
@CrossOrigin(origins = "*")
public class MedicalCertificateController {

    @Autowired
    private MedicalCertificateService medCertService;

    // Kunin ang lahat ng na-issue na med cert
    @GetMapping
    public ResponseEntity<List<MedicalCertificate>> getAllCertificates() {
        List<MedicalCertificate> certs = medCertService.getAllCertificates();
        return ResponseEntity.ok(certs);
    }

    // Kunin ang mga med cert ng isang partikular na pasyente
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<MedicalCertificate>> getCertificatesByPatient(@PathVariable Long patientId) {
        List<MedicalCertificate> certs = medCertService.getCertificatesByPatient(patientId);
        return ResponseEntity.ok(certs);
    }

    // Mag-isyu ng bagong Medical Certificate para sa isang pasyente
    @PostMapping("/issue/{patientId}")
    public ResponseEntity<MedicalCertificate> issueCertificate(
            @PathVariable Long patientId,
            @RequestBody MedicalCertificate medCertDetails) {
        try {
            MedicalCertificate issuedCert = medCertService.issueCertificate(patientId, medCertDetails);
            return ResponseEntity.ok(issuedCert);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
