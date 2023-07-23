package com.etstur.filemanager.other_services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service("fileService")
public class FileService {

    private final String folder1 = "uploads";
    private final String folder2 = "files";

    /***
     * Upload file folder1/folder2/userToken/
     * @param userToken String
     * @param file MultipartFile
     * @return File
     * @throws IOException
     */
    public File uploadFile(String userToken, MultipartFile file) throws IOException {
        if (!directoryExists(folder1)) {
            createDirectory(folder1);
        }
        if (!directoryExists(folder1+"/"+folder2)) {
            createDirectory(folder1+"/"+folder2);
        }
        if (!directoryExists(folder1+"/"+folder2+"/" + userToken)) {
            createDirectory(folder1+"/"+folder2+"/" + userToken);
        }
        String newFilename = getUniqueFilename(file.getOriginalFilename(),folder1+"/"+folder2+"/" + userToken + "/");
        byte[] bytes = file.getBytes();
        Path path = Paths.get(folder1+"/"+folder2+"/" + userToken + "/" + newFilename);
        Files.write(path, bytes);

        return new File(folder1+"/"+folder2+"/" + userToken + "/" + newFilename);
    }

    /***
     * Is directory exists
     * @param path String
     * @return boolean
     */
    private boolean directoryExists(String path) {
        Path path2 = Paths.get(path);
        return Files.exists(path2);
    }

    /***
     * Create directory
     * @param path String
     */
    private void createDirectory(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    /***
     * Remove file if exists
     * @param path String
     * @throws IOException
     */
    public void removeFile(String path) throws IOException {
        Path path2 = Paths.get(path);
        Files.deleteIfExists(path2);
    }

    /***
     * Is file exists
     * @param path String
     * @return boolean
     */
    public boolean fileExists(String path) {
        File f = new File(path);
        return f.exists() && !f.isDirectory();
    }

    /***
     * Get file extension by path
     * @param filePath String
     * @return extension String
     */
    public String getFileExtension(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return "";
        }

        int lastDotIndex = filePath.lastIndexOf(".");
        if (lastDotIndex != -1 && lastDotIndex < filePath.length() - 1) {
            return filePath.substring(lastDotIndex + 1);
        } else {
            return "";
        }
    }

    /***
     * Rename when file with same name is uploaded
     * @param filename String
     * @param path String
     * @return newName String
     */
    private String getUniqueFilename(String filename,String path) {
        String baseName = filename.substring(0, filename.lastIndexOf("."));
        String extension = filename.substring(filename.lastIndexOf("."));
        String uniqueFilename = filename;

        int count = 1;
        while (Files.exists(Paths.get(path + uniqueFilename))) {
            uniqueFilename = baseName + "(" + count + ")" + extension;
            count++;
        }

        return uniqueFilename;
    }
}
