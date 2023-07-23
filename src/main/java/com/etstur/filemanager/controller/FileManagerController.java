package com.etstur.filemanager.controller;

import com.etstur.filemanager.dto.response.FileInformationResponse;
import com.etstur.filemanager.dto.response.MessageResponse;
import com.etstur.filemanager.model.User;
import com.etstur.filemanager.service.FileInformationService;
import com.etstur.filemanager.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.AuthenticationException;
import javax.naming.SizeLimitExceededException;
import javax.transaction.NotSupportedException;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class FileManagerController {
    private final UserService userService;
    private final FileInformationService fileInformationService;

    /***
     * Upload file
     * @param file MultipartFile
     * @return ResponseEntity<MessageResponse>
     */
    @Operation(summary = "Upload file")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File uploaded",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageResponse.class)) }) })
    @PostMapping(value = "/upload-file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<MessageResponse> uploadPhoto(@RequestParam("file") MultipartFile file) throws SizeLimitExceededException, IOException, NotSupportedException, AuthenticationException {
        User user = userService.getAuthenticatedUser();
        fileInformationService.uploadFile(user,file);
        return ResponseEntity.ok(MessageResponse.builder().message("File uploaded.").build());
    }

    /***
     * Get user files informations
     * @return ResponseEntity<List<FilesInformationsResponse>>
     */
    @Operation(summary = "Get files informations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Files informations",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = FileInformationResponse.class))) }) })
    @GetMapping(value = "/get-files-informations")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<FileInformationResponse>> getFilesInformations() throws AuthenticationException {
        User user = userService.getAuthenticatedUser();
        return ResponseEntity.ok().body(fileInformationService.getFilesInformations(user));
    }

    /***
     * Get user file by file information id
     * @param fileInformationId int
     * @return ResponseEntity<ByteArrayResource>
     */
    @Operation(summary = "Get file by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = ByteArrayResource.class)) }) })
    @GetMapping(value = "/get-file-by-id")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ByteArrayResource> getFileById(@RequestParam("fileInformationId") int fileInformationId) throws AuthenticationException, IOException {
        User user = userService.getAuthenticatedUser();
        return fileInformationService.getFileById(user,fileInformationId);
    }

    /***
     * Delete user file by file information id
     * @param fileInformationId int
     * @return ResponseEntity<MessageResponse>
     */
    @Operation(summary = "Delete file by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File deleted",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageResponse.class)) }) })
    @DeleteMapping(value = "/delete-file-by-id")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<MessageResponse> deleteFileById(@RequestParam("fileInformationId") int fileInformationId) throws AuthenticationException, IOException {
        User user = userService.getAuthenticatedUser();
        fileInformationService.deleteFile(user,fileInformationId);
        return ResponseEntity.ok().body(MessageResponse.builder().message("File deleted.").build());
    }
}
