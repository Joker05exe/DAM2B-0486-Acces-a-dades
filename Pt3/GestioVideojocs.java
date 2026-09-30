import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class GestioVideojocs {

    private static final String FITXER = "videojocs.dat";
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        ArrayList<Videojoc> videojocs = carregarVideojocs();

        int opcio;
        do {
            System.out.println("\n--- Gestió de Videojocs ---");
            System.out.println("1. Afegir videojoc");
            System.out.println("2. Llistar tots els videojocs");
            System.out.println("3. Cercar videojocs per títol");
            System.out.println("4. Actualitzar un videojoc");
            System.out.println("5. Eliminar un videojoc");
            System.out.println("6. Sortir");
            System.out.print("Tria una opció: ");

            opcio = llegirEnter();

            switch (opcio) {
                case 1:
                    afegirVideojoc(videojocs);
                    break;
                case 2:
                    llistarVideojocs(videojocs);
                    break;
                case 3:
                    cercarPerTitol(videojocs);
                    break;
                case 4:
                    actualitzarVideojoc(videojocs);
                    break;
                case 5:
                    eliminarVideojoc(videojocs);
                    break;
                case 6:
                    desarVideojocs(videojocs);
                    System.out.println("Canvis desats. Sortint del programa...");
                    break;
                default:
                    System.out.println("Opció incorrecta. Torna-ho a provar.");
            }
        } while (opcio != 6);

        sc.close();
    }

    // ---------- Operacions CRUD ----------

    private static void afegirVideojoc(ArrayList<Videojoc> videojocs) {
        System.out.print("Títol: ");
        String titol = sc.nextLine();
        System.out.print("Gènere: ");
        String genere = sc.nextLine();
        System.out.print("Any de llançament: ");
        int any = llegirEnter();
        System.out.print("Plataforma: ");
        String plataforma = sc.nextLine();
        System.out.print("Preu: ");
        double preu = llegirDecimal();

        videojocs.add(new Videojoc(titol, genere, any, plataforma, preu));
        desarVideojocs(videojocs);
        System.out.println("Videojoc afegit correctament.");
    }

    private static void llistarVideojocs(ArrayList<Videojoc> videojocs) {
        if (videojocs.isEmpty()) {
            System.out.println("No hi ha videojocs desats.");
            return;
        }
        System.out.println("--- Llista de videojocs ---");
        for (int i = 0; i < videojocs.size(); i++) {
            System.out.println("[" + i + "] " + videojocs.get(i));
        }
    }

    private static void cercarPerTitol(ArrayList<Videojoc> videojocs) {
        System.out.print("Text a cercar en el títol: ");
        String text = sc.nextLine().toLowerCase();

        boolean trobat = false;
        for (Videojoc v : videojocs) {
            if (v.getTitol().toLowerCase().contains(text)) {
                System.out.println(v);
                trobat = true;
            }
        }
        if (!trobat) {
            System.out.println("No s'ha trobat cap videojoc amb aquest títol.");
        }
    }

    private static void actualitzarVideojoc(ArrayList<Videojoc> videojocs) {
        llistarVideojocs(videojocs);
        if (videojocs.isEmpty()) {
            return;
        }
        System.out.print("Índex del videojoc a actualitzar: ");
        int index = llegirEnter();

        if (index < 0 || index >= videojocs.size()) {
            System.out.println("Índex no vàlid.");
            return;
        }

        Videojoc v = videojocs.get(index);

        System.out.print("Nou títol (" + v.getTitol() + "): ");
        v.setTitol(sc.nextLine());
        System.out.print("Nou gènere (" + v.getGenere() + "): ");
        v.setGenere(sc.nextLine());
        System.out.print("Nou any de llançament (" + v.getAnyLlancament() + "): ");
        v.setAnyLlancament(llegirEnter());
        System.out.print("Nova plataforma (" + v.getPlataforma() + "): ");
        v.setPlataforma(sc.nextLine());
        System.out.print("Nou preu (" + v.getPreu() + "): ");
        v.setPreu(llegirDecimal());

        desarVideojocs(videojocs);
        System.out.println("Videojoc actualitzat correctament.");
    }

    private static void eliminarVideojoc(ArrayList<Videojoc> videojocs) {
        llistarVideojocs(videojocs);
        if (videojocs.isEmpty()) {
            return;
        }
        System.out.print("Índex del videojoc a eliminar: ");
        int index = llegirEnter();

        if (index < 0 || index >= videojocs.size()) {
            System.out.println("Índex no vàlid.");
            return;
        }

        videojocs.remove(index);
        desarVideojocs(videojocs);
        System.out.println("Videojoc eliminat correctament.");
    }

    // ---------- Persistència ----------

    @SuppressWarnings("unchecked")
    private static ArrayList<Videojoc> carregarVideojocs() {
        File fitxer = new File(FITXER);
        if (!fitxer.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fitxer))) {
            return (ArrayList<Videojoc>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error carregant videojocs: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private static void desarVideojocs(ArrayList<Videojoc> videojocs) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FITXER))) {
            oos.writeObject(videojocs);
        } catch (IOException e) {
            System.out.println("Error desant videojocs: " + e.getMessage());
        }
    }

    // ---------- Utilitats d'entrada ----------

    // Llegim sempre línia a línia i parsegem manualment: Scanner.hasNextInt()/
    // hasNextDouble() depenen del locale del sistema (p. ex. esperen "69,99"
    // en comptes de "69.99"), cosa que provocava lectures desincronitzades.
    private static int llegirEnter() {
        while (true) {
            String linia = sc.nextLine().trim();
            try {
                return Integer.parseInt(linia);
            } catch (NumberFormatException e) {
                System.out.print("Introdueix un número enter vàlid: ");
            }
        }
    }

    private static double llegirDecimal() {
        while (true) {
            String linia = sc.nextLine().trim().replace(',', '.');
            try {
                return Double.parseDouble(linia);
            } catch (NumberFormatException e) {
                System.out.print("Introdueix un número vàlid: ");
            }
        }
    }
}
