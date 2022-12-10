package org.passwordBreaker;


import org.passwordBreaker.domain.UserCredentials;
import org.passwordBreaker.exceptions.FileFormatException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.passwordBreaker.Utils.createUserCredentials;

// TODO: improve exception handling

public class FileHandler {
    public static List<String> getWordsFromFile(String filename) throws FileFormatException {
        List<String> words = new ArrayList<>();

        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            stream.forEach(words::add);
        } catch (IOException e) {
            throw new FileFormatException(e.getMessage());
        }

        return words;
    }

    public static Map<String, UserCredentials> getUserCredentialsFromFile(String filename) throws FileFormatException {
        Map<String, UserCredentials> userCredentialsMap = new HashMap<>();

        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            stream.forEach((line) -> {
                String[] userData = line.split("\\s+");
                UserCredentials userCredentials = createUserCredentials(userData);

                userCredentialsMap.put(userCredentials.getHashedPassword(), userCredentials);
            });
        } catch (IOException e) {
            throw new FileFormatException(e.getMessage());
        }

        return userCredentialsMap;
    }
}
