package lv.rvt;

import java.time.*;
import java.util.*;
import java.util.Scanner;

public class Menu {
    private static final String[] OPTIONS = {"Start Game", "Statistics", "Rules", "Change Nickname", "Exit"}; // Pieejamās izvēles spēlē
    private static int selected = 0; // Pašreizējā izvēle
    private static final Scanner scanner = new Scanner(System.in); // Skeneris lietotāja ievadei

    // Galvenā izvēlne, kas tiek parādīta, kad spēle tiek palaista
    public static void main(String[] args) {
        while (true) {
            printMenu(); // Izvada izvēlni
            handleInput(); // Apstrādā lietotāja ievadi
        }
    }

    // Izvada galveno izvēlni
    private static void printMenu() {
        System.out.println("\n=== WORDLE GAME ===");
        System.out.println("Player: " + Player.getNickname()); // Parāda spēlētāja segvārdu
        System.out.println("Select option (1-5 + Enter):");
        
        // Izvada visus izvēles punktus un izceļ pašreizējo izvēli
        for (int i = 0; i < OPTIONS.length; i++) {
            System.out.println((i == selected ? "> " : "  ") + (i+1) + ". " + OPTIONS[i]);
        }
    }

    // Apstrādā lietotāja ievadi un izvēlas atbilstošu darbību
    private static void handleInput() {
        String input = scanner.nextLine();
        
        switch (input) {
            case "1": selected = 0; break;
            case "2": selected = 1; break;
            case "3": selected = 2; break;
            case "4": selected = 3; break;
            case "5": selected = 4; break;
            case "":
                executeSelectedOption(); // Izpilda izvēlēto darbību
                break;
            default:
                System.out.println("Invalid input. Use 1-5 to select options."); // Neatbilstoša ievade
        }
    }

    // Izpilda izvēlēto darbību
    private static void executeSelectedOption() {
        System.out.println("\nSelected: " + OPTIONS[selected]);
        
        switch (OPTIONS[selected]) {
            case "Start Game": startGame(); break; // Sāk spēli
            case "Statistics": showStatisticsMenu(); break; // Parāda statistikas izvēlni
            case "Rules": printRules(); break; // Parāda spēles noteikumus
            case "Change Nickname": changeNickname(); break; // Maina segvārdu
            case "Exit": System.exit(0); // Iznāk no spēles
        }
    }

    // Statistikas izvēlne
    private static void showStatisticsMenu() {
        while (true) {
            System.out.println("\n=== STATISTICS MENU ===");
            System.out.println("1. Show all results");
            System.out.println("2. Filter by attempts");
            System.out.println("3. Filter by date");
            System.out.println("4. Back to main menu");
            System.out.print("Select option: ");
            
            String choice = scanner.nextLine();
            
            switch (choice) {
                case "1":
                    printStatistics(Result.getStatistics()); // Parāda visus rezultātus
                    break;
                case "2":
                    filterByAttempts(); // Filtrē pēc mēģinājumu skaita
                    break;
                case "3":
                    filterByDate(); // Filtrē pēc datuma
                    break;
                case "4":
                    return; // Atgriežas uz galveno izvēlni
                default:
                    System.out.println("Invalid choice!"); // Nepareiza izvēle
            }
        }
    }

    // Filtrē rezultātus pēc mēģinājumu skaita
    private static void filterByAttempts() {
        System.out.print("Minimum attempts (1-6): ");
        int min = Integer.parseInt(scanner.nextLine()); // Minimālais mēģinājumu skaits
        System.out.print("Maximum attempts (1-6): ");
        int max = Integer.parseInt(scanner.nextLine()); // Maximālais mēģinājumu skaits
        
        List<String> filtered = Result.getFilteredStatistics(min, max, null, null);
        printStatistics(filtered); // Parāda filtrētos rezultātus
    }

    // Filtrē rezultātus pēc datuma
    private static void filterByDate() {
        System.out.print("Start date (YYYY-MM-DD or empty): ");
        String start = scanner.nextLine(); // Sākuma datums
        System.out.print("End date (YYYY-MM-DD or empty): ");
        String end = scanner.nextLine(); // Beigu datums
        
        LocalDate from = start.isEmpty() ? null : LocalDate.parse(start); // Ja datums nav ievadīts, iznāk null
        LocalDate to = end.isEmpty() ? null : LocalDate.parse(end);
        
        List<String> filtered = Result.getFilteredStatistics(1, 6, from, to); // Filtrē pēc datuma
        printStatistics(filtered); // Parāda filtrētos rezultātus
    }

    // Parāda spēles rezultātus
    private static void printStatistics(List<String> stats) {
        System.out.println("\n=== GAME STATISTICS ===");
        System.out.println("Player: " + Player.getNickname());
        System.out.println("Total games: " + stats.size()); // Kopējais spēļu skaits
        System.out.println("Wins: " + stats.stream().filter(s -> s.contains("WIN")).count()); // Uzvaras skaits
        
        System.out.println("\nDate       | Attempts | Result | Word");
        System.out.println("-------------------------------------");
        stats.forEach(s -> {
            String[] parts = s.split(",");
            System.out.printf("%s | %8s | %6s | %s\n",
                parts[0].substring(0, 10),
                parts[3],
                parts[2],
                parts[4]);
        });
    }

    // Maina spēlētāja nickname
    private static void changeNickname() {
        System.out.print("\nEnter new nickname: ");
        String newNickname = scanner.nextLine();
        
        try {
            Player.setNickname(newNickname); // Iestata jaunu nickname
            System.out.println("Nickname changed to: " + Player.getNickname());
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage()); // Parāda kļūdas ziņojumu
        }
    }

    // Sāk spēli
    private static void startGame() {
        try {
            String word = new RandomWordGenerator().getWord(); // Izvēlas nejaušu vārdu
            new Game().start(word); // Sāk spēli ar izvēlēto vārdu
        } catch (Exception e) {
            System.out.println("Error starting game: " + e.getMessage()); // Kļūda spēles sākšanā
        }
    }

    // Parāda spēles noteikumus
    private static void printRules() {
        System.out.println("\n=== GAME RULES ===");
        System.out.println("1. Guess the hidden 5-letter word");
        System.out.println("2. You have 6 attempts");
        System.out.println("3. After each guess, you get feedback:");
        System.out.println("   Green color - Correct letter in correct position");
        System.out.println("   Yellow color - Correct letter in wrong position");
        System.out.println("   Gray color - Letter not in the word");
    }
}
