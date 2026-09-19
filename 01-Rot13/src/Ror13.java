public class Ror13 {
    private static String lletres_catalanes = "aáàbcçdeéèíìïfghijklmnñoóòpqrstuúùüvwxyz";
    private static char minuscules[] = lletres_catalanes.toCharArray();
    private static char majuscules[] = lletres_catalanes.toUpperCase().toCharArray();

    public static String xifraRor13(String msg) {
        StringBuilder sb = new StringBuilder();
        for (char c : msg.toCharArray()) {
            int min = lletres_catalanes.indexOf(c);
            int maj = String.valueOf(majuscules).indexOf(c);
            if (min >= 0) sb.append(minuscules[(min + 13) % minuscules.length]);
            else if (maj >= 0) sb.append(majuscules[(maj + 13) % majuscules.length]);
            else sb.append(c);
        }
        return sb.toString();
    }
 
    public static String desxifraRor13(String msg) {
        StringBuilder sb = new StringBuilder();
        for (char c : msg.toCharArray()) {
            int min = lletres_catalanes.indexOf(c);
            int maj = String.valueOf(majuscules).indexOf(c);
            if (min >= 0) sb.append(minuscules[(min - 13 + minuscules.length) % minuscules.length]);
            else if (maj >= 0) sb.append(majuscules[(maj - 13 + majuscules.length) % majuscules.length]);
            else sb.append(c);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String msgs[] = {"ABC", "XYZ", "Hola, Mr. calçot", "Perdó, per tu què és?"};
        String msgsXifrats[] = new String[msgs.length];

        System.out.println("\nXifrat\n------");

        for (int i = 0; i < msgs.length; i++) {
            msgsXifrats[i] = xifraRor13(msgs[i]);
            System.out.printf("%-23s => %s%n", msgs[i], msgsXifrats[i]);
        }
        System.out.println("\nDesxifrat\n----------");

        for (String msg : msgsXifrats) {
            System.out.printf("%-23s => %s%n", msgs, desxifraRor13(msg));
        }
    }
}