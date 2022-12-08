package org.passwordBreaker;

import org.passwordBreaker.domain.UserCredentials;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Utils {
    public static Optional<String> hashPassword(String possiblePassword) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(possiblePassword.getBytes());
            byte[] bytes = md.digest();

            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            return Optional.of(sb.toString());
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public static UserCredentials createUserCredentials(String[] userData) {
        List<String> listOfUserData = Arrays.stream(userData).toList();
        UserCredentials userCredentials = new UserCredentials();
        userCredentials.setId(Long.valueOf(listOfUserData.get(0)));
        userCredentials.setHashedPassword(listOfUserData.get(1));
        userCredentials.setMail(listOfUserData.get(2));
        String username = String.join(" ", listOfUserData
                .subList(3, listOfUserData.size()));
        userCredentials.setUsername(username);

        return userCredentials;
    }
}
