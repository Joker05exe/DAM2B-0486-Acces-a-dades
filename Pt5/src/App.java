import dao.ClientDAO;
import dao.ProducteDAO;
import model.Client;
import model.Producte;
import util.Connexio;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class App {
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        try (Connection connexio = Connexio.getConnection()) {
            ClientDAO clients = new ClientDAO(connexio);
            ProducteDAO productes = new ProducteDAO(connexio);
            boolean sortir = false;
            while (!sortir) {
                try {
                    System.out.println("\n===== BOTIGA ONLINE - PART 1 =====");
                    System.out.println("1. Gestionar productes\n2. Gestionar clients\n0. Sortir");
                    switch (enter("Opció: ", 0)) {
                        case 1 -> gestionarProductes(productes);
                        case 2 -> gestionarClients(clients);
                        case 0 -> sortir = true;
                        default -> System.out.println("Opció incorrecta.");
                    }
                } catch (SQLException e) {
                    errorSQL(e);
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (NoSuchElementException e) {
            System.out.println("\nEntrada finalitzada.");
        } catch (SQLException e) {
            System.out.println("No s'ha pogut obrir o tancar la connexió: " + e.getMessage());
        } finally {
            sc.close();
        }
    }

    private static int menu(String entitat) {
        System.out.println("\n--- " + entitat + " ---");
        System.out.println("1. Afegir\n2. Llistar\n3. Cercar per ID\n4. Actualitzar\n5. Eliminar\n0. Tornar");
        return enter("Opció: ", 0);
    }

    private static void gestionarClients(ClientDAO dao) throws SQLException {
        int opcio;
        while ((opcio = menu("Clients")) != 0) {
            switch (opcio) {
                case 1 -> {
                    Client client = dadesClient(0);
                    dao.inserir(client);
                    System.out.println("Client creat amb ID " + client.getId());
                }
                case 2 -> {
                    var clients = dao.llistar();
                    System.out.println("ID | Nom | Correu");
                    if (clients.isEmpty()) System.out.println("No hi ha clients.");
                    clients.forEach(System.out::println);
                }
                case 3 -> dao.cercarPerId(enter("ID: ", 1)).ifPresentOrElse(
                        System.out::println, () -> System.out.println("Client inexistent."));
                case 4 -> {
                    int id = enter("ID del client: ", 1);
                    if (dao.cercarPerId(id).isEmpty()) {
                        System.out.println("Client inexistent.");
                    } else {
                        System.out.println("Introdueix totes les dades noves.");
                        resultat(dao.actualitzar(dadesClient(id)), "Client actualitzat.");
                    }
                }
                case 5 -> resultat(dao.eliminar(enter("ID del client: ", 1)), "Client eliminat.");
                default -> System.out.println("Opció incorrecta.");
            }
        }
    }

    private static void gestionarProductes(ProducteDAO dao) throws SQLException {
        int opcio;
        while ((opcio = menu("Productes")) != 0) {
            switch (opcio) {
                case 1 -> {
                    Producte producte = dadesProducte(0);
                    dao.inserir(producte);
                    System.out.println("Producte creat amb ID " + producte.getId());
                }
                case 2 -> {
                    var productes = dao.llistar();
                    System.out.println("ID | Nom | Preu | Estoc");
                    if (productes.isEmpty()) System.out.println("No hi ha productes.");
                    productes.forEach(System.out::println);
                }
                case 3 -> dao.cercarPerId(enter("ID: ", 1)).ifPresentOrElse(
                        System.out::println, () -> System.out.println("Producte inexistent."));
                case 4 -> {
                    int id = enter("ID del producte: ", 1);
                    if (dao.cercarPerId(id).isEmpty()) {
                        System.out.println("Producte inexistent.");
                    } else {
                        System.out.println("Introdueix totes les dades noves.");
                        resultat(dao.actualitzar(dadesProducte(id)), "Producte actualitzat.");
                    }
                }
                case 5 -> resultat(dao.eliminar(enter("ID del producte: ", 1)), "Producte eliminat.");
                default -> System.out.println("Opció incorrecta.");
            }
        }
    }

    private static Client dadesClient(int id) {
        return new Client(id, text("Nom: "), text("Correu: "));
    }

    private static Producte dadesProducte(int id) {
        String nom = text("Nom: ");
        BigDecimal preu;
        while (true) {
            try {
                preu = new BigDecimal(text("Preu: ").replace(',', '.'));
                if (preu.signum() < 0 || preu.compareTo(new BigDecimal("99999999.99")) > 0
                        || preu.stripTrailingZeros().scale() > 2) throw new NumberFormatException();
                break;
            } catch (NumberFormatException e) {
                System.out.println("Preu invàlid: entre 0 i 99999999.99, amb un màxim de dos decimals.");
            }
        }
        return new Producte(id, nom, preu, enter("Estoc: ", 0));
    }

    private static String text(String missatge) {
        while (true) {
            System.out.print(missatge);
            String valor = sc.nextLine().trim();
            if (!valor.isEmpty()) return valor;
            System.out.println("El valor és obligatori.");
        }
    }

    private static int enter(String missatge, int minim) {
        while (true) {
            try {
                int valor = Integer.parseInt(text(missatge));
                if (valor >= minim) return valor;
            } catch (NumberFormatException e) {
                // Tornar a demanar el valor sense interrompre el menú.
            }
            System.out.println("Introdueix un enter igual o superior a " + minim + ".");
        }
    }

    private static void resultat(boolean modificat, String missatge) {
        System.out.println(modificat ? missatge : "No s'ha modificat cap registre. Comprova l'ID.");
    }

    private static void errorSQL(SQLException e) {
        if ("23000".equals(e.getSQLState()) && e.getErrorCode() == 1062) {
            System.out.println("Aquest correu ja està registrat.");
        } else if ("23000".equals(e.getSQLState()) && e.getErrorCode() == 1451) {
            System.out.println("No es pot eliminar: el registre té comandes, línies o descomptes associats.");
        } else {
            System.out.println("Error de base de dades: " + e.getMessage());
        }
    }
}
