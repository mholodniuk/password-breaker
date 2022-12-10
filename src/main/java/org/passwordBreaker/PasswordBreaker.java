package org.passwordBreaker;

import org.passwordBreaker.domain.PasswordData;
import org.passwordBreaker.domain.UserCredentials;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static org.passwordBreaker.FileHandler.getUserCredentialsFromFile;
import static org.passwordBreaker.FileHandler.getWordsFromFile;
import static org.passwordBreaker.Utils.hashPassword;

// TODO: improve exception handling

public class PasswordBreaker {
    private final Map<String, UserCredentials> userCredentialsMap;
    private final List<String> words;

    public PasswordBreaker() {
        String currentWorkingDirectory = new File("./").getAbsolutePath() + "/src/main/resources/";
        userCredentialsMap = getUserCredentialsFromFile(currentWorkingDirectory + "user-data4.txt");
        words = getWordsFromFile(currentWorkingDirectory + "small-dictionary.txt");
    }

    public List<PasswordData> findPasswordsWithPostfix(Integer start, Integer end) {
        return words.stream()
                .flatMap(word -> IntStream.range(start, end)
                        .mapToObj(i -> new PasswordData(hashPassword(word + i).orElseThrow(), word + i))
                )
                .filter(hashedWord -> userCredentialsMap.get(hashedWord.getHashedValue()) != null)
                .toList();
    }

    public List<PasswordData> findTwoWordPasswords(String separator) {
        return words.stream()
                .flatMap(firstWord -> words.stream()
                        .map(secondWord -> new PasswordData(hashPassword(firstWord + separator + secondWord).orElseThrow(), firstWord + separator + secondWord))
                )
                .filter(hashedWord -> userCredentialsMap.get(hashedWord.getHashedValue()) != null)
                .toList();
    }

    public List<PasswordData> findPasswordsWithPrefix(Integer start, Integer end) {
        return words.stream()
                .flatMap(word -> IntStream.range(start, end)
                        .mapToObj(i -> new PasswordData(hashPassword(i + word).orElseThrow(), i + word))
                )
                .filter(hashedWord -> userCredentialsMap.get(hashedWord.getHashedValue()) != null)
                .toList();
    }

    public List<PasswordData> findPasswordsWithPrefixAndPostfix(Integer start, Integer end) {
        return words.stream()
                .flatMap(word -> IntStream.range(start, end)
                        .mapToObj(firstNumber -> firstNumber + word)
                        .flatMap(wordWithPrefix -> IntStream.range(start, end)
                                .mapToObj(secondNumber -> new PasswordData(hashPassword(wordWithPrefix + secondNumber).orElseThrow(), wordWithPrefix + secondNumber)))
                )
                .filter(hashedWord -> userCredentialsMap.get(hashedWord.getHashedValue()) != null)
                .toList();
    }

    public List<PasswordData> findSimplePasswords() {
        return words.stream()
                .map(word -> new PasswordData(hashPassword(word).orElseThrow(), word))
                .filter(hashedWord -> userCredentialsMap.get(hashedWord.getHashedValue()) != null)
                .toList();
    }

    public static void main(String[] args) {

        PasswordBreaker passwordBreaker = new PasswordBreaker();

        passwordBreaker.findTwoWordPasswords(" ").forEach(hashedPassword -> {
            String hash = hashedPassword.getHashedValue();
            String password = hashedPassword.getInputValue();

            String result = "Password for " +
                    passwordBreaker.userCredentialsMap.get(hash).getMail() +
                    " is " + password;

            System.out.println(result);
        });
    }
}
