package com.etstur.filemanager.dto.request;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class LoginRequest {
    @NonNull
    private String email;
    @NonNull
    private String password;
}
