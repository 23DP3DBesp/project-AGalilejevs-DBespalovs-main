package lv.rvt;

import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.time.format.*;
import java.util.*;
import java.util.stream.*;

public class Result {
    private static final String FILE_NAME = "wordle_results.csv"; // Faila nosaukums, kur tiks glabāti spēles rezultāti
    private static final DateTimeFormatter FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"); // Datuma un laika formāts, ko izmantos rezultātu saglabāšanai

    // Funkcija, lai saglabātu spēles rezultātu
    public static void saveResult(boolean success, int attempts, 
                               String correctWord, String lastGuess) {
        try {
            // Sagatavo rezultātu rindu ar datumu, spēlētāja vārdu, rezultātu, mēģinājumu skaitu utt.
            String record = String.join(",",
                LocalDateTime.now().format(FORMATTER), // Pašreizējais datums un laiks
                Player.getNickname(), // Spēlētāja segvārds
                success ? "WIN" : "LOSE", // Spēles rezultāts (uzvara vai zaudējums)
                String.valueOf(attempts), // Mēģinājumu skaits
                correctWord, // Pareizais vārds
                lastGuess // Pēdējais uzminētais vārds
            );
            
            // Saglabā rezultātu failā, pievienojot jaunu rindu
            try (PrintWriter out = new PrintWriter(new FileWriter(FILE_NAME, true))) {
                if (new File(FILE_NAME).length() == 0) { // Ja fails ir tukšs, pievieno virsrakstu
                    out.println("Timestamp,Player,Result,Attempts,Word,LastGuess");
                }
                out.println(record); // Saglabā rezultātu
            }
        } catch (IOException e) {
            System.err.println("Error saving result: " + e.getMessage()); // Kļūda, ja neizdodas saglabāt rezultātu
        }
    }

    // Funkcija, lai iegūtu statistiku par spēlēm
    public static List<String> getStatistics() {
        return getFilteredStatistics(1, 6, null, null); // Atgriež visus rezultātus bez filtrēšanas
    }

    // Funkcija, lai iegūtu filtrētu statistiku pēc mēģinājumu skaita un datuma
    public static List<String> getFilteredStatistics(int minAttempts, int maxAttempts, 
                                                   LocalDate fromDate, LocalDate toDate) {
        try {
            // Pārbauda, vai fails pastāv
            if (!Files.exists(Paths.get(FILE_NAME))) {
                return Collections.emptyList(); // Ja fails neeksistē, atgriež tukšu sarakstu
            }

            // Lasīt rezultātus no faila un piemērot filtrus pēc mēģinājumu skaita un datuma
            return Files.lines(Paths.get(FILE_NAME))
                    .skip(1) // Pārlaiž virsrakstu
                    .filter(line -> {
                        String[] parts = line.split(",");
                        if (!parts[1].equals(Player.getNickname())) return false; // Filtrē pēc spēlētāja segvārda
                        
                        int attempts = Integer.parseInt(parts[3]); // Iegūst mēģinājumu skaitu
                        LocalDateTime gameTime = LocalDateTime.parse(parts[0], FORMATTER); // Iegūst spēles laiku
                        
                        // Pārbauda, vai mēģinājumu skaits un datums atbilst filtriem
                        boolean attemptsMatch = attempts >= minAttempts && attempts <= maxAttempts;
                        boolean dateMatch = (fromDate == null || gameTime.toLocalDate().isAfter(fromDate)) &&
                                          (toDate == null || gameTime.toLocalDate().isBefore(toDate));
                        
                        return attemptsMatch && dateMatch; // Atgriež, ja abi filtri atbilst
                    })
                    .sorted(Comparator.comparingInt(line -> Integer.parseInt(line.split(",")[3]))) // Kārto pēc mēģinājumu skaita
                    .limit(20) // Ierobežo līdz 20 rezultātiem
                    .collect(Collectors.toList()); // Atgriež rezultātu sarakstu
        } catch (IOException e) {
            return Collections.emptyList(); // Kļūdas gadījumā atgriež tukšu sarakstu
        }
    }

    // Funkcija, lai iegūtu uzvaru skaitu
    public static int getWinCount() {
        return (int) getStatistics().stream()
            .filter(line -> line.split(",")[2].equals("WIN")) // Filtrē tikai uzvaras
            .count(); // Atgriež uzvaru skaitu
    }
}
