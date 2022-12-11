package org.passwordBreaker;

import lombok.Getter;
import org.passwordBreaker.domain.PasswordData;
import org.passwordBreaker.domain.UserCredentials;
import sun.misc.Signal;

import java.io.File;
import java.util.ArrayList;
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
    @Getter
    private final Map<String, UserCredentials> userCredentialsMap;
    @Getter
    private final List<String> decodedPasswords;
    private final List<String> words;

    // todo: how to access file while running outside of intellij
    public PasswordBreaker() {
        decodedPasswords = new ArrayList<>();
        // here ask user for location of password and word files
        String currentWorkingDirectory = new File("").getAbsolutePath() + "/src/main/resources/";
        userCredentialsMap = getUserCredentialsFromFile(currentWorkingDirectory + "user-data2.txt");
        words = getWordsFromFile(currentWorkingDirectory + "small-dictionary.txt");
    }

    public void addDecodedPassword(String message) {
        System.out.println(message);
        decodedPasswords.add(message);
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
        PasswordBreaker passwordBreaker = new PasswordBreaker();

        Signal.handle(new Signal("HUP"), sig -> {
            System.out.println(sig.getName() + " (" + sig.getNumber() + ")");
            passwordBreaker.getDecodedPasswords().forEach(System.out::println);
        });


        while (!passwordBreaker.getUserCredentialsMap().isEmpty()) {
            passwordBreaker.findPasswordsWithPrefixAndPostfix(0, 100)
                    .ifPresent(passwordData -> {
                        String hash = passwordData.getHashedValue();
                        String password = passwordData.getInputValue();

                        String result = "Password for " +
                                passwordBreaker.getUserCredentialsMap().get(hash).getMail() +
                                " is " + password;
                        passwordBreaker.addDecodedPassword(result);

                        passwordBreaker.getUserCredentialsMap().remove(hash);
                        System.out.println("userCredentialsMap is now: " + passwordBreaker.getUserCredentialsMap());
                    });
        }
    }
}
