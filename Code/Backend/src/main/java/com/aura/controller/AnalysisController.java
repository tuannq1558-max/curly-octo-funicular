package com.aura.controller;

import com.aura.model.DoctorAssessment;
import com.aura.model.RetinalImage;
import com.aura.model.User;
import com.aura.repo.DoctorAssessmentRepository;
import com.aura.repo.RetinalImageRepository;
import com.aura.repo.UserRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AnalysisController {
    private final RetinalImageRepository images;
    private final DoctorAssessmentRepository assessments;
    private final UserRepository users;

    public AnalysisController(RetinalImageRepository images,
                              DoctorAssessmentRepository assessments,
                              UserRepository users) {
        this.images = images;
        this.assessments = assessments;
        this.users = users;
    }

    @PostMapping(value = "/images/upload", consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.CREATED)
    public RetinalImage upload(@RequestParam("file") MultipartFile file,
                               @RequestParam Long patientId) throws Exception {
        if (file.isEmpty()) throw new IllegalArgumentException("Empty image");
        User patient = users.findById(patientId).orElseThrow(() -> new IllegalArgumentException("Patient not found"));
        if (!"PATIENT".equalsIgnoreCase(patient.getRole())) {
            throw new IllegalArgumentException("Only patient accounts can upload images");
        }
        if (file.getContentType() == null || !file.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("Only image files are allowed");
        }

        Path uploadDir = Paths.get("uploads");
        Files.createDirectories(uploadDir);
        String safeName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path path = uploadDir.resolve(safeName);
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        RetinalImage image = new RetinalImage();
        image.setPatient(patient);
        image.setFileName(file.getOriginalFilename());
        image.setFilePath(path.toString());
        image.setStatus("WAITING_FOR_DOCTOR");
        return images.save(image);
    }


    @GetMapping("/images/{imageId}/file")
    public org.springframework.http.ResponseEntity<Resource> file(@PathVariable Long imageId) throws Exception {
        RetinalImage image = images.findById(imageId)
                .orElseThrow(() -> new IllegalArgumentException("Image not found"));
        Path path = Paths.get(image.getFilePath()).toAbsolutePath().normalize();
        Resource resource = new UrlResource(path.toUri());
        if (!resource.exists() || !resource.isReadable()) {
            return org.springframework.http.ResponseEntity.notFound().build();
        }
        MediaType mediaType = MediaType.IMAGE_JPEG;
        String name = image.getFileName().toLowerCase();
        if (name.endsWith(".png")) mediaType = MediaType.IMAGE_PNG;
        return org.springframework.http.ResponseEntity.ok().contentType(mediaType).body(resource);
    }

    @GetMapping("/patients/{patientId}/images")
    public List<RetinalImage> patientImages(@PathVariable Long patientId) {
        return images.findByPatientIdOrderByUploadedAtDesc(patientId);
    }

    @GetMapping("/patients/{patientId}/assessments")
    public List<DoctorAssessment> patientAssessments(@PathVariable Long patientId) {
        return assessments.findByImagePatientIdOrderByAssessedAtDesc(patientId);
    }
}
