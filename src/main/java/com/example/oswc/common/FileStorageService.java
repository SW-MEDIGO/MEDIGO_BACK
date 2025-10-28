package com.example.oswc.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class FileStorageService {

    private final Path rootDir;

    public FileStorageService(@Value("${app.upload.dir:uploads}") String uploadDir) {
        this.rootDir = Path.of(uploadDir).toAbsolutePath().normalize();
    }

    public String saveUserAvatar(String userId, MultipartFile file) throws IOException {
        if (file.isEmpty()) throw new IllegalArgumentException("EMPTY_FILE");
        long size = file.getSize();
        if (size <= 0 || size > 2 * 1024 * 1024) throw new IllegalArgumentException("FILE_TOO_LARGE");

        String contentType = file.getContentType() != null ? file.getContentType().toLowerCase() : "";
        String ext;
        if (contentType.contains("png")) ext = "png";
        else if (contentType.contains("jpeg") || contentType.contains("jpg")) ext = "jpg";
        else if (contentType.contains("webp")) ext = "webp";
        else throw new IllegalArgumentException("UNSUPPORTED_FILE_TYPE");

        Path avatarDir = rootDir.resolve("avatars");
        Files.createDirectories(avatarDir);

        Path target = avatarDir.resolve(userId + "." + ext);
        Files.write(target, file.getBytes());

        // 정적 리소스 핸들러에서 /files/** → file:{rootDir}/ 매핑할 것
        return "/files/avatars/" + userId + "." + ext;
    }
}
