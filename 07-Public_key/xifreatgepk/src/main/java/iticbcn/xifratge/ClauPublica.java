package iticbcn.xifratge;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;

public class ClauPublica {
    public KeyPair generaParellClausRSA() throws Exception {
        KeyPairGenerator generaClau = KeyPairGenerator.getInstance("RSA");
        generaClau.initialize(2048);
        KeyPair claus = generaClau.generateKeyPair();
        return claus;
    }

    public byte[] xifraRSA(String msg, PublicKey clauPublica) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(cipher.ENCRYPT_MODE, clauPublica);
        return cipher.doFinal(msg.getBytes("UTF-8"));
    }

    public String desxifraRSA(byte[] msgXifrat, PrivateKey ClauPrivada) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(cipher.DECRYPT_MODE, ClauPrivada);
        byte[] resultat = cipher.doFinal(msgXifrat);
        return new String(resultat, "UTF-8");
    }
}