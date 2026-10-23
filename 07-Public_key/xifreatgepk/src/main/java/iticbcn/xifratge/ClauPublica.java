package iticbcn.xifratge;

import java.security.*;
import javax.crypto.*;

public class ClauPublica {
    public KeyPair generaParellClausRSA() throws Exception {
        KeyPairGenerator generaClau = KeyPairGenerator.getInstance("RSA");
        generaClau.initialize(2048);
        KeyPair claus = generaClau.generateKeyPair();
        return claus;
    }

    public byte[] xifraRSA(String msg, PublicKey clauPublica) throws Exception {

    }

    public String desxifraRSA(byte[] msgXifrat, PrivateKey ClauPrivada) {
        
    }
}