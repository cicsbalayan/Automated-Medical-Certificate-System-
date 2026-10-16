package com.example.medcert.controller;

import com.example.medcert.model.Patient;
import com.example.medcert.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "*") // Para sa frontend integration (kung hiwalay ang React/Vue o pwede rin para sa Postman)
public class PatientController {

    @Autowired
    private PatientService patientService;

    // Kunin ang lahat ng pasyente
    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        List<Patient> patients = patientService.getAllPatients();
        return ResponseEntity.ok(patients);
    }

    // Kunin ang pasyente gamit ang ID
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        return patientService.getPatientById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Kunin ang pasyente gamit ang Student/Employee ID
    @GetMapping("/search-id")
    public ResponseEntity<Patient> getPatientByStudentId(@RequestParam String studentId) {
        return patientService.getPatientByStudentId(studentId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Maghanap ng pasyente gamit ang pangalan (keyword)
    @GetMapping("/search")
    public ResponseEntity<List<Patient>> searchPatients(@RequestParam String keyword) {
        List<Patient> patients = patientService.searchPatients(keyword);
        return ResponseEntity.ok(patients);
    }

    // Mag-register ng bagong pasyente
    @PostMapping
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
        Patient savedPatient = patientService.savePatient(patient);
        return ResponseEntity.ok(savedPatient);
    }
}
