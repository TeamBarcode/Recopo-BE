package com.barcode.recopo.member.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class LocalImageStorage implements ImageStorage {

    private final Path uploadDir;
    private final String baseUrl;

    public LocalImageStorage(
            @Value("${file.upload-dir}") String uploadDir,
            @Value("${file.base-url}") String baseUrl
    ) {
        this.uploadDir = Paths.get(uploadDir);
        this.baseUrl = baseUrl;
        try {
            Files.createDirectories(this.uploadDir);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public String upload(MultipartFile file, String key) {
        try {
            Files.copy(file.getInputStream(), uploadDir.resolve(key), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return baseUrl + "/uploads/" + key;
    }

    @Override
    public void delete(String url) {
        String key = url.substring(url.lastIndexOf('/') + 1);
        try {
            Files.deleteIfExists(uploadDir.resolve(key));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
