package org.passwordBreaker;

import lombok.*;
import org.passwordBreaker.domain.DecodedPasswords;
import org.passwordBreaker.domain.UserCredentials;
import sun.misc.Signal;

import java.io.File;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

import static org.passwordBreaker.FileHandler.getUserCredentialsFromFile;
import static org.passwordBreaker.FileHandler.getWordsFromFile;
import static org.passwordBreaker.PasswordBreaker.WordModification.*;


@AllArgsConstructor
class Producer0_NC extends Thread {
    private final PasswordBreaker passwordBreaker;
    private final DecodedPasswords decodedPasswords;

    @Override
    public void run() {
        while (!passwordBreaker.getUserCredentialsMap().isEmpty()) {
            passwordBreaker.findSimplePasswords(NO_CAPS).ifPresent(passwordData -> {
                String hash = passwordData.getHashedValue();
                String input = passwordData.getInputValue();

                decodedPasswords.addPassword("Decoded password " + input + " (" + hash + ")");
                passwordBreaker.getUserCredentialsMap().remove(hash);
            });
        }
    }
}

@AllArgsConstructor
class Producer0_AC extends Thread {
    private final PasswordBreaker passwordBreaker;
    private final DecodedPasswords decodedPasswords;

    @Override
    public void run() {
        while (!passwordBreaker.getUserCredentialsMap().isEmpty()) {
            passwordBreaker.findSimplePasswords(ALL_CAPS).ifPresent(passwordData -> {
                String hash = passwordData.getHashedValue();
                String input = passwordData.getInputValue();

                decodedPasswords.addPassword("Decoded password " + input + " (" + hash + ")");
                passwordBreaker.getUserCredentialsMap().remove(hash);
            });
        }
    }
}

@AllArgsConstructor
class Producer0_FC extends Thread {
    private final PasswordBreaker passwordBreaker;
    private final DecodedPasswords decodedPasswords;

    @Override
    public void run() {
        while (!passwordBreaker.getUserCredentialsMap().isEmpty()) {
            passwordBreaker.findSimplePasswords(FIRST_CAPS).ifPresent(passwordData -> {
                String hash = passwordData.getHashedValue();
                String input = passwordData.getInputValue();

                decodedPasswords.addPassword("Decoded password " + input + " (" + hash + ")");
                passwordBreaker.getUserCredentialsMap().remove(hash);
            });
        }
    }
}

@AllArgsConstructor
class Producer1_NC extends Thread {
    private final PasswordBreaker passwordBreaker;
    private final DecodedPasswords decodedPasswords;

    @Override
    public void run() {
        while (!passwordBreaker.getUserCredentialsMap().isEmpty()) {
            passwordBreaker.findPasswordsWithPostfix(0, 100, NO_CAPS).ifPresent(passwordData -> {
                String hash = passwordData.getHashedValue();
                String input = passwordData.getInputValue();

                decodedPasswords.addPassword("Decoded password " + input + " (" + hash + ")");
                passwordBreaker.getUserCredentialsMap().remove(hash);
            });
        }
    }
}

@AllArgsConstructor
class Producer1_AC extends Thread {
    private final PasswordBreaker passwordBreaker;
    private final DecodedPasswords decodedPasswords;

    @Override
    public void run() {
        while (!passwordBreaker.getUserCredentialsMap().isEmpty()) {
            passwordBreaker.findPasswordsWithPostfix(0, 100, ALL_CAPS).ifPresent(passwordData -> {
                String hash = passwordData.getHashedValue();
                String input = passwordData.getInputValue();

                decodedPasswords.addPassword("Decoded password " + input + " (" + hash + ")");
                passwordBreaker.getUserCredentialsMap().remove(hash);
            });
        }
    }
}

@AllArgsConstructor
class Producer1_FC extends Thread {
    private final PasswordBreaker passwordBreaker;
    private final DecodedPasswords decodedPasswords;

    @Override
    public void run() {
        while (!passwordBreaker.getUserCredentialsMap().isEmpty()) {
            passwordBreaker.findPasswordsWithPostfix(0, 100, FIRST_CAPS).ifPresent(passwordData -> {
                String hash = passwordData.getHashedValue();
                String input = passwordData.getInputValue();

                decodedPasswords.addPassword("Decoded password " + input + " (" + hash + ")");
                passwordBreaker.getUserCredentialsMap().remove(hash);
            });
        }
    }
}

@AllArgsConstructor
class Producer2_NC extends Thread {
    private final PasswordBreaker passwordBreaker;
    private final DecodedPasswords decodedPasswords;

    @Override
    public void run() {
        while (!passwordBreaker.getUserCredentialsMap().isEmpty()) {
            passwordBreaker.findPasswordsWithPrefix(0, 100, NO_CAPS).ifPresent(passwordData -> {
                String hash = passwordData.getHashedValue();
                String input = passwordData.getInputValue();

                decodedPasswords.addPassword("Decoded password " + input + " (" + hash + ")");
                passwordBreaker.getUserCredentialsMap().remove(hash);
            });
        }
    }
}

@AllArgsConstructor
class Producer2_AC extends Thread {
    private final PasswordBreaker passwordBreaker;
    private final DecodedPasswords decodedPasswords;

    @Override
    public void run() {
        while (!passwordBreaker.getUserCredentialsMap().isEmpty()) {
            passwordBreaker.findPasswordsWithPrefix(0, 100, ALL_CAPS).ifPresent(passwordData -> {
                String hash = passwordData.getHashedValue();
                String input = passwordData.getInputValue();

                decodedPasswords.addPassword("Decoded password " + input + " (" + hash + ")");
                passwordBreaker.getUserCredentialsMap().remove(hash);
            });
        }
    }
}

@AllArgsConstructor
class Producer2_FC extends Thread {
    private final PasswordBreaker passwordBreaker;
    private final DecodedPasswords decodedPasswords;

    @Override
    public void run() {
        while (!passwordBreaker.getUserCredentialsMap().isEmpty()) {
            passwordBreaker.findPasswordsWithPrefix(0, 100, FIRST_CAPS).ifPresent(passwordData -> {
                String hash = passwordData.getHashedValue();
                String input = passwordData.getInputValue();

                decodedPasswords.addPassword("Decoded password " + input + " (" + hash + ")");
                passwordBreaker.getUserCredentialsMap().remove(hash);
            });
        }
    }
}

@AllArgsConstructor
class Producer3_NC extends Thread {
    private final PasswordBreaker passwordBreaker;
    private final DecodedPasswords decodedPasswords;

    @Override
    public void run() {
        while (!passwordBreaker.getUserCredentialsMap().isEmpty()) {
            passwordBreaker.findPasswordsWithPrefixAndPostfix(0, 100, NO_CAPS).ifPresent(passwordData -> {
                String hash = passwordData.getHashedValue();
                String input = passwordData.getInputValue();

                decodedPasswords.addPassword("Decoded password " + input + " (" + hash + ")");
                passwordBreaker.getUserCredentialsMap().remove(hash);
            });
        }
    }
}

@AllArgsConstructor
class Producer3_AC extends Thread {
    private final PasswordBreaker passwordBreaker;
    private final DecodedPasswords decodedPasswords;

    @Override
    public void run() {
        while (!passwordBreaker.getUserCredentialsMap().isEmpty()) {
            passwordBreaker.findPasswordsWithPrefixAndPostfix(0, 100, ALL_CAPS).ifPresent(passwordData -> {
                String hash = passwordData.getHashedValue();
                String input = passwordData.getInputValue();

                decodedPasswords.addPassword("Decoded password " + input + " (" + hash + ")");
                passwordBreaker.getUserCredentialsMap().remove(hash);
            });
        }
    }
}

@AllArgsConstructor
class Producer3_FC extends Thread {
    private final PasswordBreaker passwordBreaker;
    private final DecodedPasswords decodedPasswords;

    @Override
    public void run() {
        while (!passwordBreaker.getUserCredentialsMap().isEmpty()) {
            passwordBreaker.findPasswordsWithPrefixAndPostfix(0, 100, FIRST_CAPS).ifPresent(passwordData -> {
                String hash = passwordData.getHashedValue();
                String input = passwordData.getInputValue();

                decodedPasswords.addPassword("Decoded password " + input + " (" + hash + ")");
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
        long start = System.currentTimeMillis();
        String currentWorkingDirectory = new File("").getAbsolutePath() + "/src/main/resources/";
        List<String> words = getWordsFromFile(currentWorkingDirectory + "big-dictionary.txt");
        ConcurrentMap<String, UserCredentials> userCredentialsMap = getUserCredentialsFromFile(currentWorkingDirectory + "user-data-final.txt");
        int numberOfUsers = userCredentialsMap.size();
        System.out.println("Number of users: " + numberOfUsers);

        DecodedPasswords decodedPasswords = new DecodedPasswords();
        PasswordBreaker passwordBreaker = new PasswordBreaker(userCredentialsMap, words);

        Producer0_NC producer0_nc = new Producer0_NC(passwordBreaker, decodedPasswords);
        Producer0_AC producer0_ac = new Producer0_AC(passwordBreaker, decodedPasswords);
        Producer0_FC producer0_fc = new Producer0_FC(passwordBreaker, decodedPasswords);

        Producer1_NC producer1_nc = new Producer1_NC(passwordBreaker, decodedPasswords);
        Producer1_AC producer1_ac = new Producer1_AC(passwordBreaker, decodedPasswords);
        Producer1_FC producer1_fc = new Producer1_FC(passwordBreaker, decodedPasswords);

        Producer2_NC producer2_nc = new Producer2_NC(passwordBreaker, decodedPasswords);
        Producer2_AC producer2_ac = new Producer2_AC(passwordBreaker, decodedPasswords);
        Producer2_FC producer2_fc = new Producer2_FC(passwordBreaker, decodedPasswords);

        Producer3_NC producer3_nc = new Producer3_NC(passwordBreaker, decodedPasswords);
        Producer3_AC producer3_ac = new Producer3_AC(passwordBreaker, decodedPasswords);
        Producer3_FC producer3_fc = new Producer3_FC(passwordBreaker, decodedPasswords);

        Consumer consumer = new Consumer(decodedPasswords);

        producer0_nc.start();
        producer0_ac.start();
        producer0_fc.start();

        producer1_nc.start();
        producer1_ac.start();
        producer1_fc.start();

        producer2_nc.start();
        producer2_ac.start();
        producer2_fc.start();

        producer3_nc.start();
        producer3_ac.start();
        producer3_fc.start();

        consumer.start();

        Signal.handle(new Signal("HUP"), sig -> {
            System.out.println(sig.getName() + " (" + sig.getNumber() + ")");
            System.out.println("Time of working: " + (System.currentTimeMillis() - start) + " [ms]");
            decodedPasswords.getPasswords();
        });

        System.out.println("Number of threads " + Thread.activeCount());
    }
}
