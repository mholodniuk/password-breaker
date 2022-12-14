package org.passwordBreaker.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class DecodedPasswords {
    private List<String> decodedPasswords = new ArrayList<>();
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
        while (!valueSet) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
        decodedPasswords.forEach(System.out::println);
        valueSet = false;
        notifyAll();
    }
}
