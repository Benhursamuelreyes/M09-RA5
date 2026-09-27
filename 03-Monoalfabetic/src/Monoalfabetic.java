
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Monoalfabetic {
    private static String abecedario = "aáàbcçdeéèíìïfghijklmnñoóòpqrstuúùüvwxyz";
    private static char minuscules[] = abecedario.toCharArray();
    private static char majuscules[] = abecedario.toUpperCase().toCharArray();

    public static char{} permutaAlfabet(char[] alfabet) {
        ArrayList<Character> lletres = new ArrayList<>();
        for (char c : alfabet) {
            lletres.add(c);
        }

        Collections.shuffle(lletres);
        
        char[] alfabetPermutat = new char[lletres.size()];
        for (int i = 0; i < lletres.size(); i++) {
            alfabetPermutat[i] = lletres.get(i);
        }
        return alfabet;
    }
}