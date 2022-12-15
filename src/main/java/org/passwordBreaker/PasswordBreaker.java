package org.passwordBreaker;

import org.passwordBreaker.domain.PasswordData;
import org.passwordBreaker.domain.UserCredentials;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.IntStream;

import static org.passwordBreaker.FileHandler.getUserCredentialsFromFile;
import static org.passwordBreaker.FileHandler.getWordsFromFile;
import static org.passwordBreaker.Utils.hashPassword;

// TODO: improve exception handling

public class PasswordBreaker {
    private final ConcurrentMap<String, UserCredentials> userCredentialsMap;
    private final List<String> words;

    public enum WordModification {
        ALL_CAPS,
        NO_CAPS,
        FIRST_CAPS
    }

    // todo: how to access file while running outside intellij
    public PasswordBreaker(ConcurrentMap<String, UserCredentials> userCredentialsMap, List<String> words) {
        this.userCredentialsMap = userCredentialsMap;
        this.words = words;
    }

    public Optional<PasswordData> findSimplePasswords(WordModification type) {
        return words.stream()
                .map(word -> {
                    word = switch (type) {
                        case ALL_CAPS -> allLettersCaps(word);
                        case NO_CAPS -> word;
                        case FIRST_CAPS -> firstLetterCaps(word);
                    };
                    return new PasswordData(hashPassword(word), word);
                })
                .filter(possiblePassword -> userCredentialsMap.containsKey(possiblePassword.getHashedValue()))
                .findFirst();
    }

    public Optional<PasswordData> findPasswordsWithPostfix(Integer start, Integer end, WordModification type) {
        return words.stream()
                .map(word -> switch (type) {
                    case ALL_CAPS -> allLettersCaps(word);
                    case NO_CAPS -> word;
                    case FIRST_CAPS -> firstLetterCaps(word);
                })
                .flatMap(word -> IntStream.range(start, end)
                        .mapToObj(number -> word + number)
                        .map(concatWord -> new PasswordData(hashPassword(concatWord), concatWord))
                )
                .filter(possiblePassword -> userCredentialsMap.containsKey(possiblePassword.getHashedValue()))
                .findFirst();
    }

    public Optional<PasswordData> findPasswordsWithPrefix(Integer start, Integer end, WordModification type) {
        return words.stream()
                .map(word -> switch (type) {
                    case ALL_CAPS -> allLettersCaps(word);
                    case NO_CAPS -> word;
                    case FIRST_CAPS -> firstLetterCaps(word);
                })
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

    public Optional<PasswordData> findPasswordsWithPrefixAndPostfix(Integer start, Integer end, WordModification type) {
        return words.stream()
                .map(word -> switch (type) {
                    case ALL_CAPS -> allLettersCaps(word);
                    case NO_CAPS -> word;
                    case FIRST_CAPS -> firstLetterCaps(word);
                })
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

    private String firstLetterCaps(String word) {
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }

    private String allLettersCaps(String word) {
        return word.toUpperCase();
    }

    public static void main(String[] args) {
        String currentWorkingDir = new File("").getAbsolutePath() + "/src/main/resources/";
        List<String> words = getWordsFromFile(currentWorkingDir + "mini-dictionary.txt");
        ConcurrentMap<String, UserCredentials> userCredentialsMap = getUserCredentialsFromFile(currentWorkingDir + "user-data3.txt");

        PasswordBreaker passwordBreaker = new PasswordBreaker(userCredentialsMap, words);

        while (!passwordBreaker.getUserCredentialsMap().isEmpty()) {
            passwordBreaker.findPasswordsWithPrefixAndPostfix(0, 100, WordModification.FIRST_CAPS)
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

    public ConcurrentMap<String, UserCredentials> getUserCredentialsMap() {
        return userCredentialsMap;
    }
}
