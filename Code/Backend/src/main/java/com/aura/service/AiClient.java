package com.aura.service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Map;

@Service
public class AiClient {
 @Value("${aura.ai.url}") String aiUrl;
 public Map predict(MultipartFile file) throws IOException {
   RestClient client=RestClient.create();
   ByteArrayResource resource=new ByteArrayResource(file.getBytes()) {
     @Override public String getFilename(){ return file.getOriginalFilename(); }
   };
   var body=new LinkedMultiValueMap<String,Object>();
   body.add("file",resource);
   return client.post().uri(aiUrl+"/predict")
     .contentType(MediaType.MULTIPART_FORM_DATA).body(body)
     .retrieve().body(Map.class);
 }
}