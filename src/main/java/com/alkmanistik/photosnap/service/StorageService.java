package com.alkmanistik.photosnap.service;

import com.alkmanistik.photosnap.exception.InvalidFileException;
import com.alkmanistik.photosnap.exception.StorageException;
import com.alkmanistik.photosnap.util.AppProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StorageService {

    private final AppProperties appProperties;

    public String store(MultipartFile file, String subDirectory) {
        try {

            String fileExtension = getFileExtension(file.getOriginalFilename());
            if (!Arrays.asList(appProperties.getStorage().getAllowedExtensions().split(","))
                    .contains(fileExtension.toLowerCase())) {
                throw new InvalidFileException("File type not allowed");
            }

            Path uploadPath = Paths.get(appProperties.getStorage().getUploadDir(), subDirectory);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String fileName = UUID.randomUUID() + "." + fileExtension;
            Path filePath = uploadPath.resolve(fileName);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return subDirectory + "/" + fileName;
        } catch (IOException e) {
            throw new StorageException("Failed to store file", e);
        }
    }

    private String getFileExtension(String filename) {
        if (filename == null) {
            return "";
        }
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex == -1) ? "" : filename.substring(dotIndex + 1);
    }

}
