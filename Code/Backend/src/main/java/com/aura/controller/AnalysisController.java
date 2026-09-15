package com.aura.controller;

import com.aura.model.Analysis;
import com.aura.model.User;
import com.aura.repo.AnalysisRepository;
import com.aura.service.AiClient;
import com.aura.service.AuthService;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/api/analysis")
public class AnalysisController {

    private final AiClient ai;
    private final AnalysisRepository repo;
    private final AuthService authService;

    public AnalysisController(
            AiClient ai,
            AnalysisRepository repo,
            AuthService authService
    ) {
        this.ai = ai;
        this.repo = repo;
        this.authService = authService;
    }

    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public Map<String, Object> upload(
            @RequestParam("file") MultipartFile file,
            Authentication authentication
    ) throws Exception {

        if (file.isEmpty()) {
            throw new IllegalArgumentException("Empty file");
        }

        // Lấy user hiện tại từ JWT
        User user = (User) authentication.getPrincipal();

        // Gọi AI Core
        Map result = ai.predict(file);

        // Tạo Analysis
        Analysis analysis = new Analysis();

        analysis.setFilename(file.getOriginalFilename());
        analysis.setRiskLevel(
                (String) result.get("riskLevel")
        );
        analysis.setRiskScore(
                ((Number) result.get("riskScore")).doubleValue()
        );
        analysis.setModelVersion(
                (String) result.get("modelVersion")
        );
        analysis.setFindings(
                String.valueOf(result.get("findings"))
        );

        // Gắn analysis với user đang đăng nhập
        analysis.setUser(user);

        // Lưu database
        repo.save(analysis);

        // Response
        Map<String, Object> out =
                new LinkedHashMap<>(result);

        out.put("id", analysis.getId());
        out.put("filename", analysis.getFilename());
        out.put("createdAt", analysis.getCreatedAt());

        return out;
    }

    @GetMapping
    public List<Analysis> history(
            Authentication authentication
    ) {

        User user = (User) authentication.getPrincipal();

        return repo.findByUserOrderByCreatedAtDesc(user);
    }

    @GetMapping("/{id}")
    public Analysis get(
            @PathVariable Long id,
            Authentication authentication
    ) {

        User user = (User) authentication.getPrincipal();

        Analysis analysis = repo.findById(id)
                .orElseThrow();

        // Không cho user xem analysis của người khác
        if (!analysis.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        return analysis;
    }

    @GetMapping("/health")
    public Map<String, String> health() {

        return Map.of(
                "status", "ok",
                "service", "aura-backend"
        );
    }
}