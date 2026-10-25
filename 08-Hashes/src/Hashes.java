import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class Hashes {
    public  int npass = 0;
    public String getSHA512AmbSalt (String pw, String salt) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        byte[] hash = (salt + pw).getBytes(StandardCharsets.UTF_8);
        byte[] hashBytes = md.digest(hash);
        HexFormat hex = HexFormat.of();
        return hex.formatHex(hashBytes);
    }

    public String getPBKDF2AmbSalt (String pw, String salt) throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        PBEKeySpec spec = new PBEKeySpec(pw.toCharArray(), salt.getBytes(StandardCharsets.UTF_8), 65536, 128);
        SecretKey key = factory.generateSecret(spec);
        byte[] bytes = key.getEncoded();
        HexFormat hex = HexFormat.of();
        return hex.formatHex(bytes);
    }

    public String forcaBruta(String alg, String hash, String salt) throws Exception {
        String charset = "abcdefABCDEF1234567890!";
        char[] chars = charset.toCharArray();
        npass = 0;
        char[] pw = new char[6];
        for (int i0 = 0; i0 < chars.length; i0++) {
            pw[0] = chars[i0];
            String c1 = new String(pw, 0, 1);
            npass++;
            if (coincideix(alg, c1, salt, hash)) return c1;
            for (int i1 = 0; i1 < chars.length; i1++) {
                pw[1] = chars[i1];
                String c2 = new String(pw, 0, 2);
                npass++;
                if (coincideix(alg, c2, salt, hash)) return c2;
                for (int i2 = 0; i2 < chars.length; i2++) {
                    pw[2] = chars[i2];
                    String c3 = new String(pw, 0, 3);
                    npass++;
                    if (coincideix(alg, c3, salt, hash)) return c3;
                    for (int i3 = 0; i3 < chars.length; i3++) {
                        pw[3] = chars[i3];
                        String c4 = new String(pw, 0, 4);
                        npass++;
                        if (coincideix(alg, c4, salt, hash)) return c4;
                        for (int i4 = 0; i4 < chars.length; i4++) {
                            pw[4] = chars[i4];
                            String c5 = new String(pw, 0, 5);
                            npass++;
                            if (coincideix(alg, c5, salt, hash)) return c5;
                            for (int i5 = 0; i5 < chars.length; i5++) {
                                pw[5] = chars[i5];
                                String c6 = new String(pw, 0, 6);
                                npass++;
                                if (coincideix(alg, c6, salt, hash)) return c6;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private boolean coincideix(String alg, String candidat, String salt, String hashObjectiu) throws Exception {
        String hashCandidat = alg.equals("SHA-512")
                ? getSHA512AmbSalt(candidat, salt)
                : getPBKDF2AmbSalt(candidat, salt);
        return hashCandidat.equals(hashObjectiu);
    }

    public String getInterval(long t1, long t2) throws Exception {
        long diff = t2 - t1;
        long millis = diff % 1000;
        long segons = (diff / 1000) % 60;
        long minuts = (diff / (1000 * 60)) % 60;
        long hores = (diff / (1000 * 60 * 60)) % 24;
        long dies = diff / (1000L * 60 * 60 * 24);
        return String.format("%d dies / %d hores / %d minuts / %d segons / %d millis",
                dies, hores, minuts, segons, millis);
    }

    public static void main(String[] args) throws Exception {
        String salt = "qpoweiruañslkdfjz";
        String pw = "aaabF!";
        Hashes h = new Hashes();
        String[] aHashes = { h.getSHA512AmbSalt(pw, salt),
                h.getPBKDF2AmbSalt(pw, salt) };
        String pwTrobat = null;
        String[] algorismes = {"SHA-512", "PBKDF2"};
        for(int i=0; i< aHashes.length; i++){
            System.out.printf("============================\n");
            System.out.printf("Algorisme: %s\n", algorismes[i]);
            System.out.printf("Hash: %s\n",aHashes[i]);
            System.out.printf("---------------------------\n");
            System.out.printf("-- Inici de força bruta ---\n");
            long t1 = System.currentTimeMillis();
            pwTrobat = h.forcaBruta(algorismes[i], aHashes[i], salt);
            long t2 = System.currentTimeMillis();
            System.out.printf("Pass : %s\n", pwTrobat);
            System.out.printf("Provats: %d\n", h.npass);
            System.out.printf("Temps : %s\n", h.getInterval(t1, t2));
            System.out.printf("---------------------------\n\n");
        }
    }
}
