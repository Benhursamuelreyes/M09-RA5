package estudi;

import java.util.ArrayList;
import java.util.List;

/**
 * ENTORN D'ESTUDI ACTIU — Criptografia (RA5)
 * ------------------------------------------
 * Aquí és on ESTUDIES. No llegeixes: implementes.
 *
 * Cada mètode té un bloc "TODO" i una pista. La teva feina és substituir
 * la línia  throw new UnsupportedOperationException(...)  pel codi correcte.
 *
 * Quan acabis un mètode, executa el corrector:
 *      ./estudia.sh
 * Et dirà, en verd o vermell, què funciona i què no, amb una pista.
 *
 * Alfabet de treball: les 26 lletres majúscules A..Z (com als exemples de
 * les diapositives). Al teu repositori (01-Rot13, 03-Monoalfabetic...) es fa
 * servir l'alfabet català complet; aquí el simplifiquem per centrar-nos en la
 * IDEA de cada xifrat.
 */
public class Exercicis {

    public static final String ALFABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; // 26 lletres

    // =====================================================================
    // NIVELL 1 — DESPLAÇAMENT (César / RotX)
    // Concepte: cada lletra se substitueix per la que està 'desp' posicions
    // més endavant, de forma circular (en arribar a la Z, torna a la A).
    // =====================================================================

    /**
     * Xifra 'msg' desplaçant cada lletra A..Z 'desp' posicions cap endavant.
     * Els caràcters que no siguin A..Z (espais, signes) es deixen igual.
     * Exemple: xifraCesar("ABC", 3) -> "DEF" ; xifraCesar("XYZ", 3) -> "ABC"
     *
     * PISTA: recorre msg lletra a lletra. Per a cada lletra c:
     *   int i = ALFABET.indexOf(c);
     *   si i < 0  -> no és lletra A..Z, afegeix c tal qual.
     *   si i >= 0 -> la nova posició és (i + desp) % 26, i afegeix ALFABET.charAt(...)
     */
    public static String xifraCesar(String msg, int desp) {
        // TODO Nivell 1a
        throw new UnsupportedOperationException("Implementa xifraCesar");
    }

    /**
     * Desxifra: l'operació inversa de xifraCesar amb el mateix 'desp'.
     * Ha de complir: desxifraCesar(xifraCesar(x, d), d).equals(x)
     *
     * PISTA: desplaçar cap enrere. Nova posició = (i - desp + 26) % 26.
     * El "+ 26" evita índexs negatius. (Pots també reutilitzar xifraCesar.)
     */
    public static String desxifraCesar(String msg, int desp) {
        // TODO Nivell 1b
        throw new UnsupportedOperationException("Implementa desxifraCesar");
    }

    // =====================================================================
    // NIVELL 2 — FORÇA BRUTA (per què el desplaçament és feble)
    // =====================================================================

    /**
     * Donat un text xifrat amb César, retorna els 26 desxifrats possibles
     * (desp = 0, 1, ..., 25), en ordre. L'atacant només ha de mirar quin té
     * sentit. Això demostra per què el desplaçament és feble.
     *
     * La llista retornada ha de tenir 26 elements:
     *   resultat.get(d) == desxifraCesar(textXifrat, d)
     *
     * PISTA: un bucle de d=0 fins d<26 que vagi afegint desxifraCesar(textXifrat, d).
     */
    public static List<String> forcaBruta(String textXifrat) {
        List<String> candidats = new ArrayList<>();
        // TODO Nivell 2
        throw new UnsupportedOperationException("Implementa forcaBruta");
    }

    // =====================================================================
    // NIVELL 3 — SUBSTITUCIÓ MONOALFABÈTICA
    // Concepte: cada lletra se substitueix SEMPRE per la mateixa lletra d'un
    // alfabet permutat (desordenat). La permutació sencera és la clau.
    // =====================================================================

    /**
     * Xifra 'msg' amb substitució monoalfabètica.
     * 'clau' és un alfabet permutat de 26 lletres (la lletra que ocupa cada
     * posició). Exemple de clau: "QWERTYUIOPASDFGHJKLZXCVBNM".
     * Llavors A->Q, B->W, C->E, ...
     * Les lletres fora de A..Z es deixen igual.
     *
     * PISTA: per a cada lletra c, i = ALFABET.indexOf(c);
     *   si i >= 0 -> afegeix clau.charAt(i);  si no -> afegeix c.
     */
    public static String xifraMonoalfabetic(String msg, String clau) {
        // TODO Nivell 3
        throw new UnsupportedOperationException("Implementa xifraMonoalfabetic");
    }

    // =====================================================================
    // NIVELL 4 — TRANSPOSICIÓ (files per columnes)
    // Concepte: les lletres NO canvien, només es REORDENEN. S'escriu el text
    // per files en una graella de 'columnes' columnes i es llegeix per columnes.
    // =====================================================================

    /**
     * Xifra per transposició: escriu 'msg' per files en una graella amb
     * 'columnes' columnes i el llegeix per columnes (de dalt a baix, esquerra
     * a dreta). Suposa que msg.length() és múltiple de 'columnes' (els tests
     * ho garanteixen) i que msg no té espais.
     *
     * Exemple: xifraTransposicio("HOLAMONS", 4)
     *   Graella (2 files x 4 columnes):   H O L A
     *                                     M O N S
     *   Llegit per columnes -> "HMOOLNAS"
     *
     * PISTA: files = msg.length() / columnes.
     *   Per a cada columna c (0..columnes-1):
     *     per a cada fila f (0..files-1):
     *       afegeix msg.charAt(f * columnes + c)
     */
    public static String xifraTransposicio(String msg, int columnes) {
        // TODO Nivell 4
        throw new UnsupportedOperationException("Implementa xifraTransposicio");
    }

    // =====================================================================
    // NIVELL 5 — HASH (xifratge unidireccional)
    // Concepte: transforma dades en un resum de mida fixa i NO es pot desfer.
    // És DETERMINISTA (mateixa entrada -> mateix hash) i té EFECTE AVALANTXA
    // (un canvi mínim canvia el hash completament).
    // =====================================================================

    /**
     * Retorna el hash SHA-256 de 'text' en hexadecimal (64 caràcters).
     *
     * PISTA: Java ja porta l'algorisme. Fes servir:
     *   java.security.MessageDigest md = MessageDigest.getInstance("SHA-256");
     *   byte[] h = md.digest(text.getBytes(java.nio.charset.StandardCharsets.UTF_8));
     *   return java.util.HexFormat.of().formatHex(h);
     * (El corrector comprovarà que és determinista i que fa efecte avalantxa.)
     */
    public static String sha256(String text) {
        // TODO Nivell 5
        throw new UnsupportedOperationException("Implementa sha256");
    }

    // =====================================================================
    // NIVELL 6 — TAXONOMIA (simètric / asimètric / hash)
    // Concepte: classificar un algorisme segons la família a què pertany.
    // =====================================================================

    /**
     * Classifica l'algorisme pel seu nom. Ha de retornar EXACTAMENT una
     * d'aquestes cadenes: "simetric", "asimetric" o "hash".
     *   - simetric : AES, DES, 3DES, RC4, RotX, Cesar
     *   - asimetric: RSA, ECC
     *   - hash     : SHA-256, SHA-2, SHA-3, MD5, MD4
     *
     * PISTA: compara 'algorisme' (pots fer algorisme.toUpperCase()) i retorna
     * la família corresponent. Pensa en la regla de cada família:
     *   mateixa clau = simètric; dues claus = asimètric; sense tornada = hash.
     */
    public static String classifica(String algorisme) {
        // TODO Nivell 6
        throw new UnsupportedOperationException("Implementa classifica");
    }
}
