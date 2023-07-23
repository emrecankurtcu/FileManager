package com.etstur.filemanager.dto.response;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class LoginResponseDTO {
    private String firstName;
    private String lastName;
    private String jwt;
}
