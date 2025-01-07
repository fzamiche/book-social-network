package com.fzamiche.back_book_social_network.file;


import com.fzamiche.back_book_social_network.user.User;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.io.File.separator;
import static java.lang.System.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileStorageService {

    @Value("${application.file.upload.images-output-path}")
    private String fileUploadPath;

    public String saveFile(
            @NotNull MultipartFile sourceFile,
            @NotNull User userId)
    {
        final String fileUploadSubPath = "users" + separator + userId;
        return uploadFile(sourceFile, fileUploadSubPath);
    }

    private String uploadFile(
            @NotNull MultipartFile sourceFile,
            @NotNull String fileUpdloadSubPath)
    {
        final String finalUploadPath = fileUploadPath + separator + fileUpdloadSubPath;
        File targetFolder = new File(finalUploadPath);
        if(!targetFolder.exists()){
            boolean folderCreated = targetFolder.mkdirs();
            if(!folderCreated){
                log.warn("Failed to create the targer folder");
                return null;
            }
        }
        final String fileExtension = getFileExtension(sourceFile.getOriginalFilename());
        String targetFilePath = finalUploadPath + separator + currentTimeMillis() + "." + fileExtension;
        Path targetPath = Paths.get(targetFilePath);
        try{
            Files.write(targetPath, sourceFile.getBytes());
            log.info("Fichier sauvegardé dans : " + targetFilePath);
            return targetFilePath;
        } catch (IOException e) {
            log.error("Fichier n'a pas été sauvegardé", e);
        }
        return null;
    }

    private String getFileExtension(String fileName) {
        if(fileName == null || fileName.isEmpty()){
            return "";
        }
        int lastDotIndex = fileName.lastIndexOf(".");
        // fileName ne contient pas de "."
        if(lastDotIndex == -1){
            return "";
        }
        return fileName.substring(lastDotIndex + 1).toLowerCase();
    }
}
