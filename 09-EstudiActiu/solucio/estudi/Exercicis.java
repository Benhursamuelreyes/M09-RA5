package estudi;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;

/**
 * SOLUCIÓ DE REFERÈNCIA (Nivells 1-6).
 * No la miris fins haver-ho intentat! S'executa amb:  ./estudia.sh --solucio
 */
public class Exercicis {

    public static final String ALFABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String xifraCesar(String msg, int desp) {
        StringBuilder sb = new StringBuilder();
        for (char c : msg.toCharArray()) {
            int i = ALFABET.indexOf(c);
            if (i >= 0) sb.append(ALFABET.charAt((i + desp % 26 + 26) % 26));
            else sb.append(c);
        }
        return sb.toString();
    }

    public static String desxifraCesar(String msg, int desp) {
        return xifraCesar(msg, 26 - (desp % 26));
    }

    public static List<String> forcaBruta(String textXifrat) {
        List<String> candidats = new ArrayList<>();
        for (int d = 0; d < 26; d++) candidats.add(desxifraCesar(textXifrat, d));
        return candidats;
    }

    public static String xifraMonoalfabetic(String msg, String clau) {
        StringBuilder sb = new StringBuilder();
        for (char c : msg.toCharArray()) {
            int i = ALFABET.indexOf(c);
            if (i >= 0) sb.append(clau.charAt(i));
            else sb.append(c);
        }
        return sb.toString();
    }

    public static String xifraTransposicio(String msg, int columnes) {
        int files = msg.length() / columnes;
        StringBuilder sb = new StringBuilder();
        for (int c = 0; c < columnes; c++)
            for (int f = 0; f < files; f++)
                sb.append(msg.charAt(f * columnes + c));
        return sb.toString();
    }

    public static String sha256(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] h = md.digest(text.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(h);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String classifica(String algorisme) {
        String a = algorisme.toUpperCase().replace("-", "");
        return switch (a) {
            case "AES", "DES", "3DES", "RC4", "ROTX", "CESAR" -> "simetric";
            case "RSA", "ECC" -> "asimetric";
            case "SHA256", "SHA2", "SHA3", "MD5", "MD4" -> "hash";
            default -> "desconegut";
        };
    }
}
