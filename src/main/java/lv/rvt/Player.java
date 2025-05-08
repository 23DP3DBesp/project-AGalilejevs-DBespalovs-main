package lv.rvt;

public class Player {
    private static String nickname; // Spēlētāja nickname
    
    // Iestata nickname ar validāciju
    public static void setNickname(String nickname) {
        if (nickname == null || nickname.trim().isEmpty()) { // Pārbauda, vai nickname nav tukšs
            throw new IllegalArgumentException("Nickname cannot be empty");
        }
        if (nickname.length() > 20) { // Pārbauda, vai nickname nav pārāk garš (maksimāli 20 simboli)
            throw new IllegalArgumentException("Nickname too long (max 20 chars)");
        }
        Player.nickname = nickname.trim(); // Saglabā nickname, noņemot liekās atstarpes
    }
    
    // Atgriež spēlētāja segvārdu
    public static String getNickname() {
        return nickname;
    }
    
    // Pārbauda, vai nickname ir iestatīts
    public static boolean isNicknameSet() {
        return nickname != null && !nickname.isEmpty(); // Atgriež true, ja nickname ir iestatīts
    }
}
