package com.tsix_hack.spam_ai_detection.controller;

import com.tsix_hack.spam_ai_detection.service.BucketService;
import io.jsonwebtoken.io.IOException;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;

@RestController
@AllArgsConstructor
public class BucketController {
    private BucketService bucketService;

    @PostMapping(path = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String upload(@RequestParam("file") MultipartFile file) throws IOException, java.io.IOException {
        String fileName = file.getOriginalFilename();
        return bucketService.upload( file);
    }
}
