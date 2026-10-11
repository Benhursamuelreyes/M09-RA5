package iticbcn.xifratge;

import java.nio.charset.StandardCharsets;

public class XifradorRotX implements Xifrador {
    private String abecedario = "aáàbcçdeéèíìïfghijklmnñoóòpqrstuúùüvwxyz";
    private char minuscules[] = abecedario.toCharArray();
    private char majuscules[] = abecedario.toUpperCase().toCharArray();

    private  String xifraRorX(String msg, int desplaçament) {
        StringBuilder sb = new StringBuilder();
        for (char c : msg.toCharArray()) {
            int min = abecedario.indexOf(c);
            int maj = String.valueOf(majuscules).indexOf(c);
            if (min >= 0) sb.append(minuscules[(min + desplaçament) % minuscules.length]);
            else if (maj >= 0) sb.append(majuscules[(maj + desplaçament) % majuscules.length]);
            else sb.append(c);
        }
        return sb.toString();
    }

    private String desxifraRorX(String msg, int desplaçament) {
        StringBuilder sb = new StringBuilder();
        for (char c : msg.toCharArray()) {
            int min = abecedario.indexOf(c);
            int maj = String.valueOf(majuscules).indexOf(c);
            if (min >= 0) sb.append(minuscules[(min - desplaçament + minuscules.length) % minuscules.length]);
            else if (maj >= 0) sb.append(majuscules[(maj - desplaçament + majuscules.length) % majuscules.length]);
            else sb.append(c);
        }
        return sb.toString();
    }

    private int parseiValida(String clau) throws ClauNoSuportada {
        int despl;
        try {
            despl = Integer.parseInt(clau);
        } catch (NumberFormatException e) {
            throw new ClauNoSuportada("Clau de RotX ha de ser un sencer de 0 a 40");
        }
        if (despl < 0 || despl > 40) {
            throw new ClauNoSuportada("Clau de RotX ha de ser un sencer de 0 a 40");
        }
        return despl;
    }

    @Override
    public TextXifrat xifra(String msg, String clau) throws ClauNoSuportada {
        int despl = parseiValida(clau);
        String xifrat = xifraRorX(msg, despl);
        return new TextXifrat(xifrat.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String desxifra(TextXifrat xifrat, String clau) throws ClauNoSuportada {
        int despl = parseiValida(clau);
        String text = new String(xifrat.getBytes(), StandardCharsets.UTF_8);
        return desxifraRorX(text, despl);
    }
}  