package com.aura.controller;

import com.aura.model.Analysis;
import com.aura.model.DoctorAssessment;
import com.aura.model.RetinalImage;
import com.aura.model.User;
import com.aura.repo.AnalysisRepository;
import com.aura.repo.DoctorAssessmentRepository;
import com.aura.repo.RetinalImageRepository;
import com.aura.repo.UserRepository;
import com.aura.service.AiClient;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AnalysisController {

    private final AiClient ai;
    private final AnalysisRepository analysisRepo;
    private final RetinalImageRepository images;
    private final DoctorAssessmentRepository assessments;
    private final UserRepository users;

    public AnalysisController(
            AiClient ai,
            AnalysisRepository analysisRepo,
            RetinalImageRepository images,
            DoctorAssessmentRepository assessments,
            UserRepository users) {

        this.ai = ai;
        this.analysisRepo = analysisRepo;
        this.images = images;
        this.assessments = assessments;
        this.users = users;
    }

    // =========================
    // AI ANALYSIS
    // =========================

    @PostMapping(
            value = "/analysis/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public Map<String, Object> uploadForAnalysis(
            @RequestParam("file") MultipartFile file,
            Authentication authentication) throws Exception {

        if (file.isEmpty()) {
            throw new IllegalArgumentException("Empty file");
        }

        User user = (User) authentication.getPrincipal();

        Map result = ai.predict(file);

        Analysis analysis = new Analysis();

        analysis.setFilename(file.getOriginalFilename());
        analysis.setRiskLevel((String) result.get("riskLevel"));
        analysis.setRiskScore(
                ((Number) result.get("riskScore")).doubleValue()
        );
        analysis.setModelVersion(
                (String) result.get("modelVersion")
        );
        analysis.setFindings(
                String.valueOf(result.get("findings"))
        );

        analysis.setUser(user);

        analysisRepo.save(analysis);

        Map<String, Object> out = new LinkedHashMap<>(result);

        out.put("id", analysis.getId());
        out.put("filename", analysis.getFilename());
        out.put("createdAt", analysis.getCreatedAt());

        return out;
    }

    @GetMapping("/analysis")
    public List<Analysis> history(Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        return analysisRepo.findByUserOrderByCreatedAtDesc(user);
    }

    @GetMapping("/analysis/{id}")
    public Analysis getAnalysis(
            @PathVariable Long id,
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        Analysis analysis = analysisRepo.findById(id)
                .orElseThrow();

        if (!analysis.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        return analysis;
    }

    @GetMapping("/analysis/health")
    public Map<String, String> health() {

        return Map.of(
                "status", "ok",
                "service", "aura-backend"
        );
    }

    // =========================
    // RETINAL IMAGE
    // =========================

    @PostMapping(
            value = "/images/upload",
            consumes = "multipart/form-data"
    )
    @ResponseStatus(HttpStatus.CREATED)
    public RetinalImage uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam Long patientId) throws Exception {

        if (file.isEmpty()) {
            throw new IllegalArgumentException("Empty image");
        }

        User patient = users.findById(patientId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Patient not found"));

        if (file.getContentType() == null ||
                !file.getContentType().startsWith("image/")) {

            throw new IllegalArgumentException(
                    "Only image files are allowed"
            );
        }

        Path uploadDir = Paths.get("uploads");

        Files.createDirectories(uploadDir);

        String originalName = file.getOriginalFilename();

        String safeName =
                System.currentTimeMillis() + "_" + originalName;

        Path path = uploadDir.resolve(safeName);

        Files.copy(
                file.getInputStream(),
                path,
                StandardCopyOption.REPLACE_EXISTING
        );

        RetinalImage image = new RetinalImage();

        image.setPatient(patient);
        image.setFileName(originalName);
        image.setFilePath(path.toString());
        image.setStatus("WAITING_FOR_DOCTOR");

        return images.save(image);
    }

    @GetMapping("/images/{imageId}/file")
    public ResponseEntity<Resource> file(
            @PathVariable Long imageId) throws Exception {

        RetinalImage image = images.findById(imageId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Image not found"));

        Path path = Paths.get(image.getFilePath())
                .toAbsolutePath()
                .normalize();

        Resource resource = new UrlResource(path.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            return ResponseEntity.notFound().build();
        }

        MediaType mediaType = MediaType.IMAGE_JPEG;

        String name = image.getFileName().toLowerCase();

        if (name.endsWith(".png")) {
            mediaType = MediaType.IMAGE_PNG;
        }

        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(resource);
    }

    @GetMapping("/patients/{patientId}/images")
    public List<RetinalImage> patientImages(
            @PathVariable Long patientId) {

        return images.findByPatientIdOrderByUploadedAtDesc(patientId);
    }

    @GetMapping("/patients/{patientId}/assessments")
    public List<DoctorAssessment> patientAssessments(
            @PathVariable Long patientId) {

        return assessments.findByImagePatientIdOrderByAssessedAtDesc(
                patientId
        );
    }
}