package com.etstur.filemanager.dto.response;


import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class FileInformationResponseDTO {
    @Id
    private int fileInformationId;
    private String path;
    private long size;
    private String name;
    private String extension;
}
