package org.passwordBreaker.domain;

import lombok.Data;

@Data
public class UserCredentials {
    Long id;
    String hashedPassword;
    String mail;
    String username;
}
