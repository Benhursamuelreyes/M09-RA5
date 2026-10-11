package iticbcn.xifratge;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class XifradorAES implements Xifrador {
    public static final String ALGORISME_XIFRAT = "AES";
    public static final String ALGORISME_HASH = "SHA-256";
    public static final String FORMAT_AES = "AES/CBC/PKCS5Padding";
    private static final int MIDA_IV = 16;
    private byte[] iv = new byte[MIDA_IV];

    private byte[] generaIv() {
        SecureRandom sr = new SecureRandom();
        sr.nextBytes(iv);
        return iv;
    }

    private byte[] xifraAES(String msg, String password) throws Exception {
        generaIv();
        byte[] bMsg = msg.getBytes(StandardCharsets.UTF_8);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        MessageDigest md = MessageDigest.getInstance(ALGORISME_HASH);
        byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
        SecretKeySpec clau = new SecretKeySpec(hash, ALGORISME_XIFRAT);
        Cipher cipher = Cipher.getInstance(FORMAT_AES);
        cipher.init(Cipher.ENCRYPT_MODE, clau, ivSpec);
        byte[] msgXifrat = cipher.doFinal(bMsg);
        byte[] resultat = new byte[iv.length + msgXifrat.length];
        System.arraycopy(iv, 0, resultat, 0, iv.length);
        System.arraycopy(msgXifrat, 0, resultat, iv.length, msgXifrat.length);
        return resultat;
    }

    private byte[] extreureIv(byte[] bIvMsgXifrat) {
        return Arrays.copyOfRange(bIvMsgXifrat, 0, MIDA_IV);
    }

    private byte[] getBytesXifrats(byte[] bIvMsgXifrat) {
        return Arrays.copyOfRange(bIvMsgXifrat, MIDA_IV, bIvMsgXifrat.length);
    }

    private String desxifraAES(byte[] bIvIMsgXifrat, String clau) throws Exception {
        byte[] ivExtret = extreureIv(bIvIMsgXifrat);
        byte[] bXifrats = getBytesXifrats(bIvIMsgXifrat);
        IvParameterSpec ivSpec = new IvParameterSpec(ivExtret);
        MessageDigest md = MessageDigest.getInstance(ALGORISME_HASH);
        byte[] hash = md.digest(clau.getBytes(StandardCharsets.UTF_8));
        SecretKeySpec clauAES = new SecretKeySpec(hash, ALGORISME_XIFRAT);
        Cipher cipher = Cipher.getInstance(FORMAT_AES);
        cipher.init(Cipher.DECRYPT_MODE, clauAES, ivSpec);
        byte[] bDesxifrat = cipher.doFinal(bXifrats);
        return new String(bDesxifrat, StandardCharsets.UTF_8);
    }

    @Override
    public TextXifrat xifra(String msg, String clau) {
        try {
            return new TextXifrat(xifraAES(msg, clau));
        } catch (Exception e) {
            System.err.println("Error de xifrat: " + e.getLocalizedMessage());
            System.exit(1);
            return null;
        }
    }

    @Override
    public String desxifra(TextXifrat xifrat, String clau) {
        try {
            return desxifraAES(xifrat.getBytes(), clau);
        } catch (Exception e) {
            System.err.println("Error de xifrat: " + e.getLocalizedMessage());
            System.exit(1);
            return null;
        }
    }
}