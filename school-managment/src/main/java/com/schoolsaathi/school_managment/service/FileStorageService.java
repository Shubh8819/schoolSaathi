package com.schoolsaathi.school_managment.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * Stores uploaded student documents on local disk under:
 *   {upload.base-dir}/{schoolId}/students/{studentAdmissionOrTempId}/{docType}-{uuid}.{ext}
 *
 * Swap this out for S3 / Azure Blob later without touching StudentService —
 * only this class needs to change.
 */
@Service
@Slf4j
public class FileStorageService {

    @Value("${app.upload.base-dir:uploads}")
    private String baseDir;

    public String store(MultipartFile file, UUID schoolId, String studentRefId, String docType) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        try {
            String originalName = StringUtils.cleanPath(file.getOriginalFilename() != null ? file.getOriginalFilename() : "file");
            String extension = originalName.contains(".") ? originalName.substring(originalName.lastIndexOf('.')) : "";
            String fileName = docType + "-" + UUID.randomUUID() + extension;

            Path targetDir = Paths.get(baseDir, schoolId.toString(), "students", studentRefId);
            Files.createDirectories(targetDir);

            Path targetPath = targetDir.resolve(fileName);
            try (InputStream in = file.getInputStream()) {
                Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);
            }

            // Relative path stored in DB; serve via a controller/static mapping in production
            return targetDir.resolve(fileName).toString();
        } catch (IOException e) {
            log.error("Failed to store file for school {} student {} docType {}", schoolId, studentRefId, docType, e);
            throw new RuntimeException("Failed to store uploaded file: " + docType, e);
        }
    }
}
