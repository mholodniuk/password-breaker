package org.passwordBreaker;

import lombok.AllArgsConstructor;
import org.passwordBreaker.domain.DecodedPasswords;
import org.passwordBreaker.domain.PasswordData;
import org.passwordBreaker.domain.UserCredentials;

import java.io.File;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.passwordBreaker.FileHandler.getUserCredentialsFromFile;
import static org.passwordBreaker.FileHandler.getWordsFromFile;
import static org.passwordBreaker.Utils.hashPassword;

// TODO: improve exception handling

@AllArgsConstructor
public class PasswordBreaker {
    private final ConcurrentMap<String, UserCredentials> userCredentialsMap;
    private final DecodedPasswords decodedPasswords;
    private final List<String> words;

    public enum WordModification {
        ALL_CAPS,
        NO_CAPS,
        FIRST_CAPS
    }

    public void findSimplePasswords(WordModification type) {
        words.stream()
                .map(word -> {
                    word = switch (type) {
                        case ALL_CAPS -> allLettersCaps(word);
                        case NO_CAPS -> word;
                        case FIRST_CAPS -> firstLetterCaps(word);
                    };
                    return new PasswordData(hashPassword(word), word);
                })
                .filter(possiblePassword -> userCredentialsMap.containsKey(possiblePassword.getHashedValue()))
                .forEach(decodedPasswords::addPassword);
    }

    public void findPasswordsWithNumberReplacement() {
        words.stream()
                .map(word -> {
                    String replacedWord =  word.chars()
                            .mapToObj(c -> (char) c)
                            .map(Object::toString)
                            .map(letter -> switch (letter.toLowerCase()) {
                                case "o" -> String.valueOf(0);
                                case "l" -> String.valueOf(1);
                                case "s" -> String.valueOf(5);
                                default -> letter;
                            })
                            .collect(Collectors.joining());
                    return new PasswordData(hashPassword(replacedWord), replacedWord);
                })
                .filter(possiblePassword -> userCredentialsMap.containsKey(possiblePassword.getHashedValue()))
                .peek(passwordData -> System.out.println(passwordData.getInputValue()))
                .forEach(decodedPasswords::addPassword);
    }

    public void findPasswordsWithPostfix(Integer start, Integer end, WordModification type) {
        words.stream()
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
                .forEach(decodedPasswords::addPassword);
    }

    public void findPasswordsWithPrefix(Integer start, Integer end, WordModification type) {
        words.stream()
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
                .forEach(decodedPasswords::addPassword);
    }

    public void findTwoWordPasswords(String separator) {
        words.stream()
                .flatMap(firstWord -> words.stream()
                        .map(secondWord -> {
                            String concatWord = firstWord + separator + secondWord;
                            return new PasswordData(hashPassword(concatWord), concatWord);
                        })
                )
                .filter(possiblePassword -> userCredentialsMap.containsKey(possiblePassword.getHashedValue()))
                .forEach(decodedPasswords::addPassword);
    }

    public void findPasswordsWithPrefixAndPostfix(Integer start, Integer end, WordModification type) {
        words.stream()
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
                .forEach(decodedPasswords::addPassword);
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
        ConcurrentMap<String, UserCredentials> userCredentialsMap = getUserCredentialsFromFile(currentWorkingDir + "user-data1.txt");

        DecodedPasswords decodedPasswords = new DecodedPasswords(userCredentialsMap);
        PasswordBreaker passwordBreaker = new PasswordBreaker(userCredentialsMap, decodedPasswords, words);

        passwordBreaker.findPasswordsWithNumberReplacement();
        decodedPasswords.getCrackedPasswords();
    }
}
