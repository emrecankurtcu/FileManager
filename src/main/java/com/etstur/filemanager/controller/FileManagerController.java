package com.etstur.filemanager.controller;

import com.etstur.filemanager.dto.response.MessageResponseDTO;
import com.etstur.filemanager.model.User;
import com.etstur.filemanager.service.FileInformationService;
import com.etstur.filemanager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.AuthenticationException;
import javax.naming.SizeLimitExceededException;
import javax.transaction.NotSupportedException;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class FileManagerController {
    private final UserService userService;
    private final FileInformationService fileInformationService;

    /***
     * Upload file
     * @param file MultipartFile
     * @return ResponseEntity<MessageResponseDTO>
     */
    @PostMapping(value = "/upload-file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageResponseDTO> uploadPhoto(@RequestParam("file") MultipartFile file)  {
        try {
            User user = userService.getAuthenticatedUser();
            fileInformationService.uploadFile(user,file);
            return ResponseEntity.ok(MessageResponseDTO.builder().message("File uploaded.").build());
        }
        catch (AuthenticationException | IOException | NotSupportedException | SizeLimitExceededException exception){
            return ResponseEntity.badRequest().body(MessageResponseDTO.builder().message(exception.getMessage()).build());
        }
    }

    /***
     * Get user files informations
     * @return ResponseEntity<FileInformationResponseDTO> || ResponseEntity<MessageResponseDTO>
     */
    @GetMapping(value = "/get-file-information")
    public ResponseEntity<?> getFileInformation()  {
        try {
            User user = userService.getAuthenticatedUser();
            return ResponseEntity.ok().body(fileInformationService.getFileInformation(user));
        }
        catch (AuthenticationException exception){
            return ResponseEntity.badRequest().body(MessageResponseDTO.builder().message(exception.getMessage()).build());
        }
    }

    /***
     * Get user file by file information id
     * @param fileInformationId int
     * @return ResponseEntity<ByteArrayResource> || ResponseEntity<MessageResponseDTO>
     */
    @GetMapping(value = "/get-file-by-id")
    public ResponseEntity<?> getFileById(@RequestParam("fileInformationId") int fileInformationId)  {
        try {
            User user = userService.getAuthenticatedUser();
            return fileInformationService.getFileById(user,fileInformationId);
        }
        catch (AuthenticationException | IOException  exception){
            return ResponseEntity.badRequest().body(MessageResponseDTO.builder().message(exception.getMessage()).build());
        }
    }

    /***
     * Delete user file by file information id
     * @param fileInformationId int
     * @return ResponseEntity<MessageResponseDTO>
     */
    @DeleteMapping(value = "/delete-file-by-id")
    public ResponseEntity<MessageResponseDTO> deleteFileById(@RequestParam("fileInformationId") int fileInformationId)  {
        try {
            User user = userService.getAuthenticatedUser();
            fileInformationService.deleteFile(user,fileInformationId);
            return ResponseEntity.ok().body(MessageResponseDTO.builder().message("File deleted.").build());
        }
        catch (AuthenticationException | IOException  exception){
            return ResponseEntity.badRequest().body(MessageResponseDTO.builder().message(exception.getMessage()).build());
        }
    }
}
