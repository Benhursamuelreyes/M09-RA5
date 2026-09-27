public class RorX {
    private static String abecedario = "aáàbcçdeéèíìïfghijklmnñoóòpqrstuúùüvwxyz";
    private static char minuscules[] = abecedario.toCharArray();
    private static char majuscules[] = abecedario.toUpperCase().toCharArray();

    public static String xifraRorX(String msg, int desplaçament) {
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
 
    public static String desxifraRorX(String msg, int desplaçament) {
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

    public static void forcaBrutaRotX(String textXifrat) {
        System.out.println("Missatge xifrat: " + textXifrat);
        System.out.println("----------------");
        for (int d = 0; d < minuscules.length; d++) {
            System.out.printf("(%d)->%s%n", d, desxifraRorX(textXifrat, d));
        }
    }

    public static void main(String[] args) {
        String msgs[] = {"ABC", "XYZ", "Hola, Mr. calçot", "Perdó, per tu què és?"};
        String msgsXifrats[] = new String[msgs.length];

        System.out.println("\nXifrat\n------");

        for (int i = 0; i < msgs.length; i++) {
            int desplaçament = i * 2;
            msgsXifrats[i] = xifraRorX(msgs[i], desplaçament);
            System.out.printf("(%d)%-23s => %s%n", desplaçament, msgs[i], msgsXifrats[i]);
        }
        System.out.println("\nDesxifrat\n----------");

        for (int i = 0; i < msgsXifrats.length; i++) {
            int desplaçament =i * 2;
            System.out.printf("(%d)%-23s => %s%n", desplaçament, msgsXifrats[i], desxifraRorX(msgsXifrats[i], desplaçament));
        }

        forcaBrutaRotX(msgsXifrats[3]);
    }
}  