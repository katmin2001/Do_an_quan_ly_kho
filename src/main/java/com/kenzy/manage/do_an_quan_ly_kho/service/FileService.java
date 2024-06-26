package com.kenzy.manage.do_an_quan_ly_kho.service;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class FileService {
    public static String uploadFile(MultipartFile file, String uploadDir) throws IOException {
        if (!Files.exists(Paths.get(uploadDir))) {
            Files.createDirectories(Paths.get(uploadDir));
        }
        String fileName = file.getOriginalFilename();
        String[] name = fileName.split("\\.");
        fileName = name[0] + "_" + convertDate() + "." + name[1];
        Path filePath = Paths.get(uploadDir, fileName);
        Files.copy(file.getInputStream(), filePath);
        return filePath.toString();
    }

    public static void deleteFile(String imagePath) throws IOException {
        if (imagePath != null && !imagePath.isEmpty()) {
            Files.deleteIfExists(Paths.get(imagePath));
        }
    }

    public static String convertDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private boolean isValidFileExtension(String fileName) {
        // Danh sách đuôi file cho phép (ví dụ: jpg, jpeg, png)
        List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png");

        // Lấy đuôi file từ tên file
        String fileExtension = StringUtils.getFilenameExtension(fileName);

        // Kiểm tra xem đuôi file có nằm trong danh sách cho phép hay không
        return allowedExtensions.contains(fileExtension.toLowerCase());
    }
}
