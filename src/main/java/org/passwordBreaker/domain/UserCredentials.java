package org.passwordBreaker.domain;

import lombok.Data;

@Data
public class UserCredentials {
    private Long id;
    private String hashedPassword;
    private String mail;
    private String username;
}
