package dev.ndkiet.springboots3bucket.controller;

import dev.ndkiet.springboots3bucket.service.S3BucketService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author ADMIN
 * @created 26/02/2023 - 10:30 PM
 * @project springboot-s3bucket
 */

@RestController
@RequestMapping("/api/aws-s3")
@RequiredArgsConstructor
public class S3BucketController {
    private final S3BucketService service;

    @PostMapping("/upload-file")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile multipartFile) {
        try {
            service.uploadFileToS3(multipartFile);
            return ResponseEntity.ok("upload successful");
        } catch (IOException exception) {
            return ResponseEntity.ok(exception.getMessage());
        }
    }

    @GetMapping("/download-file/{fileName}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable("fileName") String fileName) {
        byte[] response = service.downloadFileFromS3(fileName);
        ByteArrayResource resource = new ByteArrayResource(response);
        return ResponseEntity
                .ok()
                .contentLength(response.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; fileName\"" + fileName + "\"")
                .body(resource);
    }

    @DeleteMapping("/delete-file/{fileName}")
    public ResponseEntity<String> deleteFile(@PathVariable("fileName") String fileName) {
        service.deleteFile(fileName);
        return ResponseEntity.ok("deleted file");
    }
}
