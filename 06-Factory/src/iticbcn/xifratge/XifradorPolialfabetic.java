package iticbcn.xifratge;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class XifradorPolialfabetic implements Xifrador {
    private char[] alfabetOriginal = "AÁÀBCÇDEÉÈFGHIÍÌÏJKLMNÑOÓÒPQRSTUÚÙÜVWXYZ".toCharArray();
    private char[] alfabetPermutat;
    private Random random = new Random();

    private void initRandom(String clau) {
        random = new Random(clau.hashCode());
    }

    private void permutaAlfabet() {
        List<Character> lletres = new ArrayList<>();
        for (char c : alfabetOriginal) lletres.add(c);
        Collections.shuffle(lletres, random);
        char[] nou = new char[lletres.size()];
        for (int i = 0; i < lletres.size(); i++) nou[i] = lletres.get(i);
        alfabetPermutat = nou;
    }

    private String xifraPoliAlfa(String cadena) {
        StringBuilder sb = new StringBuilder();
        for (char c : cadena.toCharArray()) {
            permutaAlfabet();
            boolean esMinuscula = Character.isLowerCase(c);
            char cMaj = Character.toUpperCase(c);
            int index = new String(alfabetOriginal).indexOf(cMaj);
            if (index >= 0) {
                char sustituta = alfabetPermutat[index];
                if (esMinuscula) sustituta = Character.toLowerCase(sustituta);
                sb.append(sustituta);
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private String desxifraPoliAlfa(String cadena) {
        StringBuilder sb = new StringBuilder();
        for (char c : cadena.toCharArray()) {
            permutaAlfabet();
            boolean esMinuscula = Character.isLowerCase(c);
            char cMaj = Character.toUpperCase(c);
            int index = new String(alfabetPermutat).indexOf(cMaj);
            if (index >= 0) {
                char sustituta = alfabetOriginal[index];
                if (esMinuscula) sustituta = Character.toLowerCase(sustituta);
                sb.append(sustituta);
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private void parseiValida(String clau, String missatgeError) throws ClauNoSuportada {
        try {
            Long.parseLong(clau);
        } catch (NumberFormatException e) {
            throw new ClauNoSuportada(missatgeError);
        }
    }

    @Override
    public TextXifrat xifra(String msg, String clau) throws ClauNoSuportada {
        parseiValida(clau, "La clau per xifrat Polialfabètic ha de ser un String convertible a long");
        initRandom(clau);
        String xifrat = xifraPoliAlfa(msg);
        return new TextXifrat(xifrat.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String desxifra(TextXifrat xifrat, String clau) throws ClauNoSuportada {
        parseiValida(clau, "La clau de Polialfabètic ha de ser un String convertible a long");
        initRandom(clau);
        String text = new String(xifrat.getBytes(), StandardCharsets.UTF_8);
        return desxifraPoliAlfa(text);
    }
}