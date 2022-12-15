package org.passwordBreaker.domain;

import java.util.ArrayList;
import java.util.List;


public class DecodedPasswords {
    private final List<String> decodedPasswords = new ArrayList<>();
    private String lastDecodedPassword;
    private boolean valueSet = false;

    public synchronized void addPassword(String message) {
        while (valueSet) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
        decodedPasswords.add(message);
        lastDecodedPassword = message;
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

    public synchronized void getPasswords() {
//        decodedPasswords.forEach(System.out::println);
        System.out.println("Cracked " + decodedPasswords.size() + " passwords");
        valueSet = false;
        notifyAll();
    }
}
