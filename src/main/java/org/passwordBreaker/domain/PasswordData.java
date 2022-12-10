package org.passwordBreaker.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PasswordData {
    private String hashedValue;
    private String inputValue;
}
