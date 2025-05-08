package lv.rvt;

import java.util.Scanner;

public class Game {
    private static final int MAX_ATTEMPTS = 6;
    private final Scanner scanner = new Scanner(System.in);

    // ANSI krāsu kodi termināļa tekstam
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_GRAY = "\u001B[90m";

    // Spēles sākšana
    public void start(String correctWord) {
        System.out.println("\n=== NEW GAME ===");
        System.out.println("Guess the 5-letter word. You have " + MAX_ATTEMPTS + " attempts.");
        
        for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
            String guess = getValidGuess(attempt); // Iegūst piecu burtu vārdu
            
            if (processGuess(correctWord, guess, attempt)) { // Apstrādā mēģinājumu
                return; // Ja uzminēts pareizi, spēle beidzas
            }
        }
        
        endGame(correctWord); // Ja neizdodas, parāda beigu ziņojumu
    }

    // Prasa lietotājam ievadīt derīgu minējumu
    private String getValidGuess(int attempt) {
        while (true) {
            System.out.printf("Attempt %d/%d: ", attempt, MAX_ATTEMPTS);
            String guess = scanner.nextLine().toLowerCase().trim();
            
            if (guess.length() == 5) {
                return guess; // Ja ievade ir 5 burti, atgriež to
            }
            System.out.println("Please enter exactly 5 letters."); // Pretējā gadījumā - kļūdas ziņojums
        }
    }

    // Pārbauda, vai minējums ir pareizs, un parāda atgriezenisko saiti
    private boolean processGuess(String correct, String guess, int attempt) {
        if (guess.equals(correct)) {
            System.out.println("\u001B[32mCorrect! You won in " + attempt + " attempts!\u001B[0m");
            Result.saveResult(true, attempt, correct, guess); // Saglabā rezultātu
            return true; // Uzvara
        }
    
        showFeedback(correct, guess); // Parāda krāsainu atgriezenisko saiti
        return false;
    }
    
    // Veido krāsainu atgriezenisko saiti par minējumu
    private void showFeedback(String correct, String guess) {
        StringBuilder feedback = new StringBuilder();
    
        for (int i = 0; i < correct.length(); i++) {
            char c = guess.charAt(i);
            if (c == correct.charAt(i)) {
                feedback.append("\u001B[32m").append(c).append("\u001B[0m"); // Zaļš – pareizā vietā
            } else if (correct.indexOf(c) >= 0) {
                feedback.append("\u001B[33m").append(c).append("\u001B[0m"); // Dzeltens – citā vietā
            } else {
                feedback.append("\u001B[37m").append(c).append("\u001B[0m"); // Balts – nav vārdā
            }
        }
    
        System.out.println("Feedback: " + feedback); // Izvada atgriezenisko saiti
    }

    // Spēles beigas, ja nav uzminēts
    private void endGame(String correctWord) {
        System.out.println("\nGame over! The word was: " + ANSI_GREEN + correctWord + ANSI_RESET);
        Result.saveResult(false, MAX_ATTEMPTS, correctWord, "-"); // Saglabā zaudējuma rezultātu
    }
}
