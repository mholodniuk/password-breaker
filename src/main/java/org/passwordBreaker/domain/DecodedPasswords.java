package org.passwordBreaker.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;


public class DecodedPasswords {
    private final List<String> decodedPasswords = new ArrayList<>();
    private final ConcurrentMap<String, UserCredentials> userCredentialsMap;
    private String lastDecodedPassword;
    private boolean valueSet = false;

    public DecodedPasswords(ConcurrentMap<String, UserCredentials> userCredentialsMap) {
        this.userCredentialsMap = userCredentialsMap;
    }

    public synchronized void addPassword(PasswordData passwordData) {
        while (valueSet) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
        UserCredentials decodedUserData = userCredentialsMap.remove(passwordData.getHashedValue());
        decodedPasswords.add(passwordData.getHashedValue());
        lastDecodedPassword = "Password for" + decodedUserData.getMail() + " is " + passwordData.getInputValue();
        valueSet = true;
        notify();
    }

    public synchronized void getLastPassword() {
        while (!valueSet) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(lastDecodedPassword);
        valueSet = false;
        notifyAll();
    }

    public synchronized void getCrackedPasswords() {
        System.out.println("Cracked " + decodedPasswords.size() + " passwords");
        valueSet = false;
        notifyAll();
    }
}
