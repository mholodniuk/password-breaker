package org.passwordBreaker;

import org.passwordBreaker.domain.UserCredentials;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.passwordBreaker.FileHandler.getUserCredentialsFromFile;
import static org.passwordBreaker.FileHandler.getWordsFromFile;
import static org.passwordBreaker.Utils.hashPassword;

// TODO: improve exception handling

public class PasswordBreaker {

    public static void main(String[] args) throws IOException {
        String currentWorkingDirectory = new File("./").getAbsolutePath() + "/src/main/resources/";

        Map<String, UserCredentials> userCredentialsMap = getUserCredentialsFromFile(currentWorkingDirectory + "test1.txt");
        List<String> words = getWordsFromFile(currentWorkingDirectory + "passwords1.txt");

        words.forEach((word) -> {
            String hashedPassword = hashPassword(word).orElseThrow();
            if(userCredentialsMap.get(hashedPassword) != null) {
                String sb = "password for " +
                        userCredentialsMap.get(hashedPassword).getMail() +
                        " is " +
                        word;

                System.out.println(sb);
            }
        });
    }
}
