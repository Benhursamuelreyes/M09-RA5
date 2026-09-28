import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Monoalfabetic {
    private static char[] alfabetOriginal = "AÁÀBCÇDEÉÈFGHIÍÌÏJKLMNÑOÓÒPQRSTUÚÙÜVWXYZ".toCharArray();
    private static char[] alfabetPermutat = permutaAlfabet(alfabetOriginal);

    public static char[] permutaAlfabet(char[] alfabet) {
        ArrayList<Character> lletres = new ArrayList<>();
        for (char c : alfabet) {
            lletres.add(c);
        }
        Collections.shuffle(lletres);
        char[] alfabetPermutat = new char[lletres.size()];
        for (int i = 0; i < lletres.size(); i++) {
            alfabetPermutat[i] = lletres.get(i);
        }
        return alfabetPermutat;
    }

    public static String xifraMonoAlfa(String cadena) {
        StringBuilder sb = new StringBuilder();
        for (char c : cadena.toCharArray()) {
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

    public static String desxifraMonoAlfa(String cadena) {
        StringBuilder sb = new StringBuilder();
        for (char c : cadena.toCharArray()) {
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
        String proves[] = {
            "Test 01 àrbitre, coixí, Perímetre",
            "Test 02 Taüll, DÍA, año",
            "Test 03 Peça, Òrrius, Bòvila"
        };
        String xifrats[] = new String[proves.length];
        for (char c : alfabetOriginal) {
            System.out.print(c + " ");
        }
        System.out.println();
        for (char c : alfabetPermutat) {
            System.out.print(c + " ");
        }
        System.out.println();
        System.out.println("Xifratge:");
        for (int i = 0; i < proves.length; i++) {
            xifrats[i] = xifraMonoAlfa(proves[i]);
            System.out.printf("%-35s -> %s%n", proves[i], xifrats[i]);
        }
        System.out.println("Desxifratge:");
        for (int i = 0; i < xifrats.length; i++) {
            System.out.printf("%-35s -> %s%n", xifrats[i], desxifraMonoAlfa(xifrats[i]));
        }
    }
}