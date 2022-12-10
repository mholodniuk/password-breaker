package org.passwordBreaker;

import lombok.Getter;
import org.passwordBreaker.domain.PasswordData;
import org.passwordBreaker.domain.UserCredentials;
import org.passwordBreaker.exceptions.PasswordsNotFoundException;
import sun.misc.Signal;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.passwordBreaker.FileHandler.getUserCredentialsFromFile;
import static org.passwordBreaker.FileHandler.getWordsFromFile;
import static org.passwordBreaker.Utils.hashPassword;

// TODO: improve exception handling

public class PasswordBreaker {
    @Getter
    private final Map<String, UserCredentials> userCredentialsMap;
    private final List<String> words;

    public PasswordBreaker() {
        String currentWorkingDirectory = new File("./").getAbsolutePath() + "/src/main/resources/";
        words = getWordsFromFile(currentWorkingDirectory + "small-dictionary.txt");
        userCredentialsMap = getUserCredentialsFromFile(currentWorkingDirectory + "user-data4.txt");
    }

    public Optional<PasswordData> findPasswordsWithPostfix(Integer start, Integer end) {
        return words.stream()
                .flatMap(word -> IntStream.range(start, end)
                        .mapToObj(i -> new PasswordData(hashPassword(word + i).orElseThrow(), word + i))
                )
                .filter(hashedWord -> userCredentialsMap.get(hashedWord.getHashedValue()) != null)
                .findFirst();
    }

    public Optional<PasswordData> findTwoWordPasswords(String separator) {
        return words.stream()
                .flatMap(firstWord -> words.stream()
                        .map(secondWord -> new PasswordData(hashPassword(firstWord + separator + secondWord).orElseThrow(), firstWord + separator + secondWord))
                )
                .filter(hashedWord -> userCredentialsMap.get(hashedWord.getHashedValue()) != null)
                .findFirst();
    }

    public Optional<PasswordData> findPasswordsWithPrefix(Integer start, Integer end) {
        return words.stream()
                .flatMap(word -> IntStream.range(start, end)
                        .mapToObj(i -> new PasswordData(hashPassword(i + word).orElseThrow(), i + word))
                )
                .filter(hashedWord -> userCredentialsMap.get(hashedWord.getHashedValue()) != null)
                .findFirst();
    }

    public Optional<PasswordData> findPasswordsWithPrefixAndPostfix(Integer start, Integer end) {
        return words.stream()
                .flatMap(word -> IntStream.range(start, end)
                        .mapToObj(firstNumber -> firstNumber + word)
                        .flatMap(wordWithPrefix -> IntStream.range(start, end)
                                .mapToObj(secondNumber -> new PasswordData(hashPassword(wordWithPrefix + secondNumber).orElseThrow(), wordWithPrefix + secondNumber)))
                )
                .filter(hashedWord -> userCredentialsMap.get(hashedWord.getHashedValue()) != null)
                .findFirst();
    }

    public Optional<PasswordData> findSimplePasswords() {
        return words.stream()
                .map(word -> new PasswordData(hashPassword(word).orElseThrow(), word))
                .filter(hashedWord -> userCredentialsMap.get(hashedWord.getHashedValue()) != null)
                .findFirst();
    }

    public static void main(String[] args) {

//        Signal.handle(new Signal("HUP"), sig -> {
//            System.out.println(sig.getName() + " (" + sig.getNumber() + ")");
//        });
//
//        new Thread(() -> {
//            while (true) {
//                try {
//                    Thread.sleep(1000L);
//                    System.out.println("waiting");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();

        PasswordBreaker passwordBreaker = new PasswordBreaker();

        while (!passwordBreaker.getUserCredentialsMap().isEmpty()) {
            passwordBreaker.findTwoWordPasswords(" ")
                    .ifPresent(passwordData -> {
                        String hash = passwordData.getHashedValue();
                        String password = passwordData.getInputValue();

                        String result = "Password for " +
                                passwordBreaker.getUserCredentialsMap().get(hash).getMail() +
                                " is " + password;
                        System.out.println(result);

                        passwordBreaker.getUserCredentialsMap().put(hash, null);
                        System.out.println("userCredentialsMap is now: " + passwordBreaker.getUserCredentialsMap());
                    });
        }
    }
}
