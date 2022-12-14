package org.passwordBreaker;

import org.passwordBreaker.domain.PasswordData;
import org.passwordBreaker.domain.UserCredentials;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.passwordBreaker.FileHandler.getUserCredentialsFromFile;
import static org.passwordBreaker.FileHandler.getWordsFromFile;
import static org.passwordBreaker.Utils.hashPassword;

// TODO: improve exception handling
// todo: maybe inject userCredentialsMap as a parameter or user ConcurrentHashMap

public class PasswordBreaker {

    private final Map<String, UserCredentials> userCredentialsMap;
    private final List<String> words;

    // todo: how to access file while running outside intellij
    public PasswordBreaker(Map<String, UserCredentials> userCredentialsMap, List<String> words) {
        this.userCredentialsMap = userCredentialsMap;
        this.words = words;
    }

    public Optional<PasswordData> findPasswordsWithPostfix(Integer start, Integer end) {
        return words.stream()
                .flatMap(word -> IntStream.range(start, end)
                        .mapToObj(number -> word + number)
                        .map(concatWord -> new PasswordData(hashPassword(concatWord), concatWord))
                )
                .filter(possiblePassword -> userCredentialsMap.containsKey(possiblePassword.getHashedValue()))
                .findFirst();
    }

    public Optional<PasswordData> findPasswordsWithPrefix(Integer start, Integer end) {
        return words.stream()
                .flatMap(word -> IntStream.range(start, end)
                        .mapToObj(number -> number + word)
                        .map(concatWord -> new PasswordData(hashPassword(concatWord), concatWord))
                )
                .filter(possiblePassword -> userCredentialsMap.containsKey(possiblePassword.getHashedValue()))
                .findFirst();
    }

    public Optional<PasswordData> findTwoWordPasswords(String separator) {
        return words.stream()
                .flatMap(firstWord -> words.stream()
                        .map(secondWord -> {
                            String concatWord = firstWord + separator + secondWord;
                            return new PasswordData(hashPassword(concatWord), concatWord);
                        })
                )
                .filter(possiblePassword -> userCredentialsMap.containsKey(possiblePassword.getHashedValue()))
                .findFirst();
    }

    public Optional<PasswordData> findPasswordsWithPrefixAndPostfix(Integer start, Integer end) {
        return words.stream()
                .flatMap(word -> IntStream.range(start, end)
                        .mapToObj(firstNumber -> firstNumber + word)
                        .flatMap(wordWithPrefix -> IntStream.range(start, end)
                                .mapToObj(secondNumber -> wordWithPrefix + secondNumber)
                                .map(concatWord -> new PasswordData(hashPassword(concatWord), concatWord))
                        )
                )
                .filter(possiblePassword -> userCredentialsMap.containsKey(possiblePassword.getHashedValue()))
                .findFirst();
    }

    public Optional<PasswordData> findSimplePasswords() {
        return words.stream()
                .map(word -> new PasswordData(hashPassword(word), word))
                .filter(possiblePassword -> userCredentialsMap.containsKey(possiblePassword.getHashedValue()))
                .findFirst();
    }

    public static void main(String[] args) {
        String currentWorkingDir = new File("").getAbsolutePath() + "/src/main/resources/";
        List<String> words = getWordsFromFile(currentWorkingDir + "mini-dictionary.txt");
        Map<String, UserCredentials> userCredentialsMap = getUserCredentialsFromFile(currentWorkingDir + "user-data1.txt");

        PasswordBreaker passwordBreaker = new PasswordBreaker(userCredentialsMap, words);

        while (!passwordBreaker.getUserCredentialsMap().isEmpty()) {
            passwordBreaker.findSimplePasswords()
                    .ifPresent(passwordData -> {
                        String hash = passwordData.getHashedValue();
                        String password = passwordData.getInputValue();

                        String result = "Password for " +
                                passwordBreaker.getUserCredentialsMap().get(hash).getMail() +
                                " is " + password;

                        System.out.println(result);

                        passwordBreaker.getUserCredentialsMap().remove(hash);
                        System.out.println("userCredentialsMap is now: " + passwordBreaker.getUserCredentialsMap());
                    });
        }
    }

    public synchronized Map<String, UserCredentials> getUserCredentialsMap() {
        return userCredentialsMap;
    }
}
