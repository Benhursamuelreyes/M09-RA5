
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.text.PasswordView;

public class AES {
    public static final String ALGORISME_XIFRAT = "AES";
    public static final String ALGORISME_HASH = "SHA-256";
    public static final String FORMAT_AES = "AES/CBC/PKCS5Padding";
    private static final int MIDA_IV = 16;
    private static byte[] iv = new byte[MIDA_IV];
    private static final String CLAU = "LaClauSecretaQueVulguis";

    public static byte[] xifraAES(String msg, String password) {
        throws Exception {
            byte[] bMsg = msg.getBytes(StandardCharsets.UTF_8);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            MessageDigest md = MessageDigest.getInstance(ALGORISME_HASH);
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec clau = new SecretKeySpec(hash, ALGORISME_XIFRAT);
            Cipher cipher = Cipher.getInstance(FORMAT_AES);
            cipher.init(Cipher.ENCRYPT_MODE, clau, ivSpec);
            byte[] msgXifrat = cipher.doFinal(bMsg);
            byte[] resultat = new byte[iv.length + msgXifrat.length];
            System.arraycopy(iv, MIDA_IV, hash, MIDA_IV, MIDA_IV);

        }
    }
    public static void main(String[] args) {
        String msgs[] = {"Lorem ipsum dicet",
                    "Hola Andrés cómo está tu cuñado",
                    "Àgora illa Ôtto"};
        for (int i = 0; i < msgs.length; i++) {
            String msg = msgs[i];
            byte[] bXifrats = null;
            String desxifrat = "";
            try {
                bXifrats = xifraAES(msg, CLAU);
                desxifrat = desxifraAES(bXifrats, CLAU);
            } catch (Exception e) {
                System.out.println("el error de xifrat: "
                    + e.getLocalizedMessage());
            }
        }
        System.out.println("---- -");
        System.out.println("Msg: " + msg);
        System.out.println("Enc: " + new String(bXifrats));
        System.out.println("DEC: " + desxifrat);

    }
}