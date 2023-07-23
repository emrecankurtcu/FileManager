package com.etstur.filemanager.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "user")
public class User {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "userId", nullable = false)
    private int userId;
    @Basic
    @Column(name = "email", length = 128)
    private String email;
    @Basic
    @Column(name = "password", length = 256)
    private String password;
    @Basic
    @Column(name = "firstName", length = 128)
    private String firstName;
    @Basic
    @Column(name = "lastName", length = 128)
    private String lastName;
    @Basic
    @Column(name = "token", length = 256)
    private String token;

}
