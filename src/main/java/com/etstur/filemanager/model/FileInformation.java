package com.etstur.filemanager.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "fileinformation")
public class FileInformation {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "fileInformationId", nullable = false)
    private int fileInformationId;
    @Basic
    @Column(name = "userId")
    private int userId;
    @Basic
    @Column(name = "path", length = 512)
    private String path;
    @Basic
    @Column(name = "size")
    private long size;
    @Basic
    @Column(name = "name", length = 128)
    private String name;
    @Basic
    @Column(name = "extension", length = 5)
    private String extension;
    @Basic
    @Column(name = "createdDatetime", insertable = false, updatable = false)
    private Timestamp createdDatetime;
}
