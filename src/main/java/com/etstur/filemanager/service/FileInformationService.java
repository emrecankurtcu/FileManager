package com.etstur.filemanager.service;

import com.etstur.filemanager.dto.response.FileInformationResponseDTO;
import com.etstur.filemanager.model.FileInformation;
import com.etstur.filemanager.model.User;
import com.etstur.filemanager.other_services.FileService;
import com.etstur.filemanager.repository.FileInformationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.SizeLimitExceededException;
import javax.transaction.NotSupportedException;
import javax.transaction.Transactional;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service("fileInformationService")
@RequiredArgsConstructor
public class FileInformationService {
    private final FileInformationRepository fileInformationRepository;
    private final FileService fileService;

    private final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    /***
     * Upload file to uploads/files, allowed types png,jpg,jpeg,docx,pdf,xlsx, max file size 5MB
     * @param user User
     * @param file MultipartFile
     * @throws IOException
     * @throws NotSupportedException
     * @throws SizeLimitExceededException
     */
    @Transactional
    public void uploadFile(User user, MultipartFile file) throws IOException, NotSupportedException, SizeLimitExceededException {
        String fileExtension = fileService.getFileExtension(file.getOriginalFilename());
        List<String> allowedTypes = List.of("png","jpg","jpeg","docx","pdf","xlsx");
        if(!allowedTypes.contains(fileExtension)){
            throw new NotSupportedException("File type not supported.");
        }
        if(file.getSize() > MAX_FILE_SIZE){
            throw new SizeLimitExceededException("Max File Size : 5MB");
        }
        File dbFile = fileService.uploadFile(user.getToken(),file);
        FileInformation fileDB = FileInformation.builder()
                .path(dbFile.getPath())
                .userId(user.getUserId())
                .name(dbFile.getName())
                .size(file.getSize())
                .extension(fileExtension)
                .build();
        fileInformationRepository.save(fileDB);
    }

    /***
     * Return file informations
     * @param user User
     * @return List<FileInformationResponseDTO>
     */
    public List<FileInformationResponseDTO> getFileInformation(User user){
        return fileInformationRepository.getFileInformation(user.getUserId());
    }

    /***
     * Return file as byte array, set content type by extension
     * @param user User
     * @param fileInformationId int
     * @return ResponseEntity<ByteArrayResource>
     * @throws IOException
     */
    public ResponseEntity<ByteArrayResource> getFileById(User user, int fileInformationId) throws IOException {
        FileInformation fileInformation = fileInformationRepository.findById(fileInformationId).orElseThrow();
        if(fileInformation.getUserId() != user.getUserId()){
            throw new FileNotFoundException("File not found.");
        }
        if(fileService.fileExists(fileInformation.getPath())){
            Path filePath = Paths.get(fileInformation.getPath());
            byte[] fileArr =  Files.readAllBytes(filePath);
            String contentType;
            ByteArrayResource resource = new ByteArrayResource(fileArr);
            if (fileInformation.getName().toLowerCase().endsWith(".jpg")
                    || fileInformation.getName().toLowerCase().endsWith(".jpeg")
                    ||  fileInformation.getName().toLowerCase().endsWith(".png")) {
                contentType = MediaType.IMAGE_JPEG_VALUE;
            } else if (fileInformation.getName().toLowerCase().endsWith(".png")) {
                contentType = MediaType.IMAGE_PNG_VALUE;
            } else if (fileInformation.getName().toLowerCase().endsWith(".pdf")) {
                contentType = MediaType.APPLICATION_PDF_VALUE;
            } else if (fileInformation.getName().toLowerCase().endsWith(".docx")) {
                contentType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            } else if (fileInformation.getName().toLowerCase().endsWith(".xlsx")) {
                contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            } else {
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileInformation.getName())
                    .contentType(MediaType.parseMediaType(contentType))
                    .contentLength(fileArr.length)
                    .body(resource);
        }
        throw new FileNotFoundException("File not found.");
    }

    /***
     * Delete file by fileInformationId
     * @param user User
     * @param fileInformationId int
     * @throws IOException
     */
    @Transactional
    public void deleteFile(User user, int fileInformationId) throws IOException {
        FileInformation fileInformation = fileInformationRepository.findById(fileInformationId).orElseThrow();
        if(fileInformation.getUserId() != user.getUserId()){
            throw new FileNotFoundException("File not found.");
        }

        fileService.removeFile(fileInformation.getPath());
        fileInformationRepository.delete(fileInformation);
    }
}
