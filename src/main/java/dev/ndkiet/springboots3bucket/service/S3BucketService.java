package dev.ndkiet.springboots3bucket.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

/**
 * @author ADMIN
 * @created 26/02/2023 - 9:59 PM
 * @project springboot-s3bucket
 */

@Service
@RequiredArgsConstructor
public class S3BucketService {
    private final AmazonS3 s3Client;
    @Value("${spring.s3.bucket-name}")
    private String bucketName;

    public void uploadFileToS3(MultipartFile multipartFile) throws IOException {
        File file = transferMultipartFile2File(multipartFile);
        String fileName = String.format("%d_%s", System.currentTimeMillis(), multipartFile.getOriginalFilename());
        s3Client.putObject(new PutObjectRequest(bucketName, fileName, file));
    }

    public byte[] downloadFileFromS3(String fileName) {
        S3Object s3Object = s3Client.getObject(bucketName, fileName);
        // read content object
        S3ObjectInputStream s3ObjectInputStream = s3Object.getObjectContent();
        try {
            return IOUtils.toByteArray(s3ObjectInputStream);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public void deleteFile(String fileName) {
        s3Client.deleteObject(bucketName, fileName);
    }

    public File transferMultipartFile2File(MultipartFile multipartFile) {
        File convertedFile = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
//        multipartFile.transferTo(convertedFile);
        try (FileOutputStream fileOutputStream = new FileOutputStream(convertedFile)) {
            fileOutputStream.write(multipartFile.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return convertedFile;
    }
}
