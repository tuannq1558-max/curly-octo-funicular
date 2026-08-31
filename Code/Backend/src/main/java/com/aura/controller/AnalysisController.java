package com.aura.controller;
import com.aura.model.Analysis;
import com.aura.repo.AnalysisRepository;
import com.aura.service.AiClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;
@RestController
@RequestMapping("/api/analysis")
public class AnalysisController {
 private final AiClient ai; private final AnalysisRepository repo;
 public AnalysisController(AiClient ai, AnalysisRepository repo){this.ai=ai;this.repo=repo;}

 @PostMapping(value="/upload", consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
 public Map<String,Object> upload(@RequestParam("file") MultipartFile file) throws Exception {
   if(file.isEmpty()) throw new IllegalArgumentException("Empty file");
   Map result=ai.predict(file);
   Analysis a=new Analysis();
   a.setFilename(file.getOriginalFilename());
   a.setRiskLevel((String)result.get("riskLevel"));
   a.setRiskScore(((Number)result.get("riskScore")).doubleValue());
   a.setModelVersion((String)result.get("modelVersion"));
   a.setFindings(String.valueOf(result.get("findings")));
   repo.save(a);
   Map<String,Object> out=new LinkedHashMap<>(result);
   out.put("id",a.getId()); out.put("filename",a.getFilename()); out.put("createdAt",a.getCreatedAt());
   return out;
 }
 @GetMapping public List<Analysis> history(){ return repo.findAll(); }
 @GetMapping("/{id}") public Analysis get(@PathVariable Long id){ return repo.findById(id).orElseThrow(); }
 @GetMapping("/health") public Map health(){return Map.of("status","ok","service","aura-backend");}
}