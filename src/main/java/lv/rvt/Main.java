package lv.rvt;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        
        try {
            Scanner scanner = new Scanner(System.in); // Skeneris lietotāja ievadei
            System.out.println("=== WELCOME TO WORDLE ===");
            
            // Līdz lietotājs neievada derīgu nickname, lūdz ievadīt nickname
            while (!Player.isNicknameSet()) {
                System.out.print("Enter your nickname: ");
                String input = scanner.nextLine(); // Lietotāja ievadītais nickname
                
                try {
                    Player.setNickname(input); // Iestata nickname
                    System.out.println("\nHello, " + Player.getNickname() + "!"); // Apstiprina nickname
                } catch (IllegalArgumentException e) {
                    System.out.println("Error: " + e.getMessage()); // Parāda kļūdas ziņojumu, ja segvārds nav derīgs
                }
            }
            
            // Izsauc galveno izvēlni pēc nickname iestatīšanas
            Menu.main(args);
        } catch (Exception e) {
            System.err.println("Critical error: " + e.getMessage()); // Parāda kļūdu, ja notiek kritiska kļūda
            e.printStackTrace(); // Izvērsta kļūdas informācija
        }
    }
}
