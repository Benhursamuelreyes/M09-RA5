package estudi;

import java.util.List;

/**
 * CORRECTOR AUTOMÀTIC — el motor de l'estudi actiu.
 * Executa proves sobre la teva implementació d'Exercicis i et dona feedback
 * immediat: verd (correcte) o vermell (amb pista). No cal tocar aquest fitxer.
 *
 *   ./estudia.sh            -> corregeix la teva feina (src/)
 *   ./estudia.sh --solucio  -> executa la solució de referència (tot verd)
 */
public class Corrector {

    static int ok = 0, total = 0;
    static String nivellActual = "";
    static boolean nivellTrencat = false;

    // Colors ANSI (si la terminal no els suporta, es veuen com a text).
    static final String V = "\u001B[32m", R = "\u001B[31m",
            B = "\u001B[1m", G = "\u001B[90m", Z = "\u001B[0m";

    public static void main(String[] args) {
        System.out.println(B + "\n╔════════════════════════════════════════════════╗");
        System.out.println("║   ENTORN D'ESTUDI ACTIU — Criptografia (RA5)   ║");
        System.out.println("╚════════════════════════════════════════════════╝" + Z);

        nivell("NIVELL 1 — Desplaçament (César / RotX)");
        prova("xifraCesar(\"ABC\", 3) = \"DEF\"",
                () -> Exercicis.xifraCesar("ABC", 3).equals("DEF"),
                "Nova posició = (i + desp) % 26. Deixa igual el que no sigui A..Z.");
        prova("xifraCesar(\"XYZ\", 3) = \"ABC\"  (dona la volta)",
                () -> Exercicis.xifraCesar("XYZ", 3).equals("ABC"),
                "El % 26 fa que després de la Z es torni a la A.");
        prova("xifraCesar respecta espais i signes",
                () -> Exercicis.xifraCesar("HOLA, MON!", 1).equals("IPMB, NPO!"),
                "Si ALFABET.indexOf(c) < 0, afegeix c sense tocar-lo.");
        prova("desxifraCesar desfà xifraCesar",
                () -> Exercicis.desxifraCesar(Exercicis.xifraCesar("SECRET", 5), 5).equals("SECRET"),
                "Desplaça enrere: (i - desp + 26) % 26.");

        nivell("NIVELL 2 — Força bruta");
        prova("forcaBruta retorna 26 candidats",
                () -> Exercicis.forcaBruta("DEF").size() == 26,
                "Un bucle de d=0 a 25.");
        prova("entre els candidats hi ha el text clar",
                () -> Exercicis.forcaBruta(Exercicis.xifraCesar("ATAC", 7)).contains("ATAC"),
                "candidats.get(d) = desxifraCesar(text, d).");

        nivell("NIVELL 3 — Substitució monoalfabètica");
        String clau = "QWERTYUIOPASDFGHJKLZXCVBNM"; // A->Q, B->W, C->E...
        prova("xifraMonoalfabetic(\"ABC\", clau) = \"QWE\"",
                () -> Exercicis.xifraMonoalfabetic("ABC", clau).equals("QWE"),
                "Per a la lletra c: i = ALFABET.indexOf(c); afegeix clau.charAt(i).");
        prova("la mateixa lletra dona sempre el mateix símbol",
                () -> {
                    String x = Exercicis.xifraMonoalfabetic("EE", clau);
                    return x.length() == 2 && x.charAt(0) == x.charAt(1);
                },
                "És 'mono': una lletra -> sempre la mateixa substituta (per això és feble a l'atac estadístic).");

        nivell("NIVELL 4 — Transposició");
        prova("xifraTransposicio(\"HOLAMONS\", 4) = \"HMOOLNAS\"",
                () -> Exercicis.xifraTransposicio("HOLAMONS", 4).equals("HMOOLNAS"),
                "Llegeix per columnes: msg.charAt(f*columnes + c).");
        prova("la transposició NO canvia les lletres, només l'ordre",
                () -> {
                    String o = "ABCDEFGH", x = Exercicis.xifraTransposicio(o, 2);
                    return x.length() == 8 && ordenat(x).equals(ordenat(o));
                },
                "Mateixes lletres, posició diferent. Comprova l'índex f*columnes + c.");

        nivell("NIVELL 5 — Hash (unidireccional)");
        prova("sha256 retorna 64 caràcters hexadecimals",
                () -> { String h = Exercicis.sha256("hola"); return h != null && h.matches("[0-9a-f]{64}"); },
                "MessageDigest.getInstance(\"SHA-256\") + HexFormat.of().formatHex(...).");
        prova("és DETERMINISTA (mateixa entrada, mateix hash)",
                () -> Exercicis.sha256("hola").equals(Exercicis.sha256("hola")),
                "Si falla, revisa que no afegeixis res aleatori.");
        prova("EFECTE AVALANTXA ('hola' vs 'Hola' donen hash molt diferent)",
                () -> {
                    String a = Exercicis.sha256("hola"), b = Exercicis.sha256("Hola");
                    return !a.equals(b) && diferencia(a, b) > 20;
                },
                "Un canvi mínim ha de canviar molts caràcters del hash.");

        nivell("NIVELL 6 — Taxonomia (simètric / asimètric / hash)");
        prova("AES és simètric",   () -> "simetric".equals(Exercicis.classifica("AES")),  "Mateixa clau per xifrar i desxifrar.");
        prova("RSA és asimètric",  () -> "asimetric".equals(Exercicis.classifica("RSA")), "Dues claus: pública i privada.");
        prova("SHA-256 és hash",   () -> "hash".equals(Exercicis.classifica("SHA-256")),  "Sense camí de tornada.");
        prova("RotX és simètric",  () -> "simetric".equals(Exercicis.classifica("RotX")), "Els clàssics reversibles són simètrics.");

        resum();
    }

    // ---- motor de proves -------------------------------------------------
    interface Test { boolean run() throws Exception; }

    static void nivell(String nom) {
        nivellActual = nom;
        nivellTrencat = false;
        System.out.println("\n" + B + nom + Z);
    }

    static void prova(String desc, Test t, String pista) {
        total++;
        boolean passa;
        String motiu = null;
        try {
            passa = t.run();
        } catch (UnsupportedOperationException e) {
            passa = false; motiu = "encara no implementat";
        } catch (Exception e) {
            passa = false; motiu = e.getClass().getSimpleName() + " " + e.getMessage();
        }
        if (passa) {
            ok++;
            System.out.println("  " + V + "✓" + Z + " " + desc);
        } else {
            System.out.println("  " + R + "✗ " + desc + Z);
            if (motiu != null) System.out.println("      " + G + "(" + motiu + ")" + Z);
            // Primera pista del nivell només, per no omplir de text la pantalla.
            if (!nivellTrencat) {
                System.out.println("      " + G + "pista: " + pista + Z);
                nivellTrencat = true;
            }
        }
    }

    static void resum() {
        System.out.println("\n" + B + "───────────────────────────────" + Z);
        String color = (ok == total) ? V : (ok == 0 ? R : "\u001B[33m");
        System.out.println(B + "Resultat: " + color + ok + "/" + total + Z + B + " proves superades" + Z);
        int barres = total == 0 ? 0 : (ok * 20 / total);
        StringBuilder bar = new StringBuilder("  [");
        for (int i = 0; i < 20; i++) bar.append(i < barres ? "█" : "░");
        bar.append("]");
        System.out.println(color + bar + Z);
        if (ok == total) System.out.println(V + "\n  Tot verd. Has interioritzat els 6 conceptes. 🎉" + Z);
        else System.out.println(G + "\n  Obre src/estudi/Exercicis.java, implementa el següent ✗ i torna a executar." + Z);
        System.out.println();
    }

    static String ordenat(String s) {
        char[] c = s.toCharArray(); java.util.Arrays.sort(c); return new String(c);
    }
    static int diferencia(String a, String b) {
        int d = 0, n = Math.min(a.length(), b.length());
        for (int i = 0; i < n; i++) if (a.charAt(i) != b.charAt(i)) d++;
        return d;
    }
}
