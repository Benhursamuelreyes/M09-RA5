package iticbcn.xifratge;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;

public class XifradorMonoalfabetic implements Xifrador {
    private char[] alfabetOriginal = "AÁÀBCÇDEÉÈFGHIÍÌÏJKLMNÑOÓÒPQRSTUÚÙÜVWXYZ".toCharArray();
    private char[] alfabetPermutat;

    public XifradorMonoalfabetic() {
        alfabetPermutat = permutaAlfabet(alfabetOriginal);
    }

    private char[] permutaAlfabet(char[] alfabet) {
        ArrayList<Character> lletres = new ArrayList<>();
        for (char c : alfabet) lletres.add(c);
        Collections.shuffle(lletres);
        char[] nou = new char[lletres.size()];
        for (int i = 0; i < lletres.size(); i++) nou[i] = lletres.get(i);
        return nou;
    }

    private String xifraMonoAlfa(String cadena) {
        StringBuilder sb = new StringBuilder();
        for (char c : cadena.toCharArray()) {
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

    private String desxifraMonoAlfa(String cadena) {
        StringBuilder sb = new StringBuilder();
        for (char c : cadena.toCharArray()) {
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

    private void validaClau(String clau) throws ClauNoSuportada {
        if (clau != null) {
            throw new ClauNoSuportada("Xifratxe monoalfabètic no suporta clau != null");
        }
    }

    @Override
    public TextXifrat xifra(String msg, String clau) throws ClauNoSuportada {
        validaClau(clau);
        String xifrat = xifraMonoAlfa(msg);
        return new TextXifrat(xifrat.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String desxifra(TextXifrat xifrat, String clau) throws ClauNoSuportada {
        validaClau(clau);
        String text = new String(xifrat.getBytes(), StandardCharsets.UTF_8);
        return desxifraMonoAlfa(text);
    }
}