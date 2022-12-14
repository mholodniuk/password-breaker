package org.passwordBreaker;

import lombok.*;
import org.passwordBreaker.domain.DecodedPasswords;
import org.passwordBreaker.domain.UserCredentials;
import sun.misc.Signal;

import java.io.File;
import java.util.List;
import java.util.Map;

import static org.passwordBreaker.FileHandler.getUserCredentialsFromFile;
import static org.passwordBreaker.FileHandler.getWordsFromFile;


@AllArgsConstructor
class Producer0 extends Thread {
    private final PasswordBreaker passwordBreaker;
    private final DecodedPasswords decodedPasswords;

    @Override
    public void run() {
        while (!passwordBreaker.getUserCredentialsMap().isEmpty()) {
            passwordBreaker.findSimplePasswords().ifPresent(passwordData -> {
                String hash = passwordData.getHashedValue();
                String input = passwordData.getInputValue();

                decodedPasswords.addPassword(
                        "Decoded password " + input + " (" + hash + ")");

                passwordBreaker.getUserCredentialsMap().remove(hash);
            });
        }
    }
}

@AllArgsConstructor
class Producer1 extends Thread {
    private final PasswordBreaker passwordBreaker;
    private final DecodedPasswords decodedPasswords;

    @Override
    public void run() {
        while (!passwordBreaker.getUserCredentialsMap().isEmpty()) {
            passwordBreaker.findPasswordsWithPostfix(0, 1000).ifPresent(passwordData -> {
                String hash = passwordData.getHashedValue();
                String input = passwordData.getInputValue();

                decodedPasswords.addPassword(
                        "Decoded password " + input + " (" + hash + ")");

                passwordBreaker.getUserCredentialsMap().remove(hash);
            });
        }
    }
}

@AllArgsConstructor
class Producer2 extends Thread {
    private final PasswordBreaker passwordBreaker;
    private final DecodedPasswords decodedPasswords;

    @Override
    public void run() {
        while (!passwordBreaker.getUserCredentialsMap().isEmpty()) {
            passwordBreaker.findPasswordsWithPrefix(0, 10000).ifPresent(passwordData -> {
                String hash = passwordData.getHashedValue();
                String input = passwordData.getInputValue();

                decodedPasswords.addPassword(
                        "Decoded password " + input + " (" + hash + ")");

                passwordBreaker.getUserCredentialsMap().remove(hash);
            });
        }
    }
}

@AllArgsConstructor
class Producer3 extends Thread {
    private final PasswordBreaker passwordBreaker;
    private final DecodedPasswords decodedPasswords;

    @Override
    public void run() {
        while (!passwordBreaker.getUserCredentialsMap().isEmpty()) {
            passwordBreaker.findPasswordsWithPrefixAndPostfix(0, 10000).ifPresent(passwordData -> {
                String hash = passwordData.getHashedValue();
                String input = passwordData.getInputValue();

                decodedPasswords.addPassword(
                        "Decoded password " + input + " (" + hash + ")");

                passwordBreaker.getUserCredentialsMap().remove(hash);
            });
        }
    }
}

@AllArgsConstructor
class Consumer extends Thread {
    private final DecodedPasswords decodedPasswords;
    @Override
    public void run() {
        while (true) {
            decodedPasswords.getLastPassword();
        }
    }
}

public class Main {
    public static void main(String[] args) {
        String currentWorkingDirectory = new File("").getAbsolutePath() + "/src/main/resources/";
        List<String> words = getWordsFromFile(currentWorkingDirectory + "big-dictionary.txt");
        Map<String, UserCredentials> userCredentialsMap = getUserCredentialsFromFile(currentWorkingDirectory + "user-data-final.txt");
        int numberOfUsers = userCredentialsMap.size();
        System.out.println("Number of users: " + numberOfUsers);

        DecodedPasswords decodedPasswords = new DecodedPasswords();
        PasswordBreaker passwordBreaker = new PasswordBreaker(userCredentialsMap, words);

        Producer0 producer0 = new Producer0(passwordBreaker, decodedPasswords);
        Producer1 producer1 = new Producer1(passwordBreaker, decodedPasswords);
        Producer2 producer2 = new Producer2(passwordBreaker, decodedPasswords);
        Producer3 producer3 = new Producer3(passwordBreaker, decodedPasswords);
        Consumer consumer = new Consumer(decodedPasswords);

        producer0.start();
        producer1.start();
        producer2.start();
        producer3.start();
        consumer.start();

        Signal.handle(new Signal("HUP"), sig -> {
            System.out.println(sig.getName() + " (" + sig.getNumber() + ")");
            System.out.println("Cracked " + (numberOfUsers - decodedPasswords.getDecodedPasswords().size()) + " passwords");
            decodedPasswords.getPasswords();
        });
    }
}
