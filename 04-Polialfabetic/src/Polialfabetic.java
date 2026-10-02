import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Polialfabetic {
    private static char[] alfabetOriginal = "AÁÀBCÇDEÉÈFGHIÍÌÏJKLMNÑOÓÒPQRSTUÚÙÜVWXYZ".toCharArray();
    private static char[] alfabetPermutat;
    private static Random random = new Random();
       private static String clauSecreta = "ITICBCN";

    public static void initRandom(String clau) {
        random = new Random(clau.hashCode());
    }

    public static void permutaAlfabet() {
        List<Character> lletres = new ArrayList<>();
        for (char c : alfabetOriginal) lletres.add(c);
        Collections.shuffle(lletres, random);
        char[] nou = new char[lletres.size()];
        for (int i = 0; i < lletres.size(); i++) nou[i] = lletres.get(i);
        alfabetPermutat = nou;
    }
    
    public static String xifraPoliAlfa(String cadena) {
        StringBuilder sb = new StringBuilder();
        for (char c : cadena.toCharArray()) {
            permutaAlfabet();
            boolean esMinuscula = Character.isLowerCase(c);
            char cMaj = Character.toUpperCase(c);
            int index = new String(alfabetOriginal).indexOf(cMaj);
            if (index >= 0) {
                char sustituta = alfabetPermutat[index];
                if (esMinuscula) {
                    sustituta = Character.toLowerCase(sustituta);
                }
                sb.append(sustituta);
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String desxifraPoliAlfa(String cadena) {
        StringBuilder sb = new StringBuilder();
        for (char c : cadena.toCharArray()) {
            permutaAlfabet();
            boolean esMinuscula = Character.isLowerCase(c);
            char cMaj = Character.toUpperCase(c);
            int index = new String(alfabetPermutat).indexOf(cMaj);
            if (index >= 0) {
                char sustituta = alfabetOriginal[index];
                if (esMinuscula) {
                    sustituta = Character.toLowerCase(sustituta);
                }
                sb.append(sustituta);
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String msgs[] = {"Test 01 àrbritre, coixí, Perímetre",
            "Test 02 Taüll, DÍA, año",
            "Test 03 Peça, Orrius, Bòvila"};
        String msgsXifrats[] = new String[msgs.length];
        System.out.println("Xifratge:\n--");
        for (int i = 0; i < msgs.length; i++) {
            initRandom(clauSecreta);
            msgsXifrats[i] = xifraPoliAlfa(msgs[i]);
            System.out.printf("%-34s -> %s%n", msgs[i], msgsXifrats[i]);
        }
        System.out.println("Desxifratge:\n------");
        for (int i = 0; i < msgs.length; i++){
            initRandom(clauSecreta);
            String msg = desxifraPoliAlfa(msgsXifrats[i]);
            System.out.printf("%-34s -> %s%n", msgsXifrats[i], msg);
        }
    }
}