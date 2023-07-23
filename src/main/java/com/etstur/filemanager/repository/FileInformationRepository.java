package com.etstur.filemanager.repository;

import com.etstur.filemanager.dto.response.FileInformationResponse;
import com.etstur.filemanager.model.FileInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("fileInformationRepository")
public interface FileInformationRepository extends JpaRepository<FileInformation, Integer> {

    @Query("SELECT new FileInformationResponse(fi.fileInformationId,fi.path,fi.size,fi.name,fi.extension) FROM FileInformation fi WHERE fi.userId=:userId")
    List<FileInformationResponse> getFilesInformations(@Param("userId") int userId);

    FileInformation findByFileInformationIdAndUserId(@Param("fileInformationId") int fileInformationId,@Param("userId") int userId);
}