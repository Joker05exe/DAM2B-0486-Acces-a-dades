import dao.ClientDAO;
import dao.ProducteDAO;
import model.Client;
import model.Producte;
import org.junit.jupiter.api.*;
import java.math.BigDecimal;
import java.sql.*;
import static org.junit.jupiter.api.Assertions.*;

class CrudTest {
    private Connection connexio;
    private ClientDAO clients;
    private ProducteDAO productes;

    @BeforeEach
    void preparar() throws SQLException {
        connexio = DriverManager.getConnection("jdbc:h2:mem:botiga;MODE=MySQL;DATABASE_TO_LOWER=TRUE");
        try (PreparedStatement ps = connexio.prepareStatement("CREATE TABLE Clients (id INT AUTO_INCREMENT PRIMARY KEY, nom VARCHAR(100) NOT NULL, correu VARCHAR(100) NOT NULL UNIQUE)")) {
            ps.execute();
        }
        try (PreparedStatement ps = connexio.prepareStatement("CREATE TABLE Productes (id INT AUTO_INCREMENT PRIMARY KEY, nom VARCHAR(100) NOT NULL, preu DECIMAL(10,2) NOT NULL, estoc INT NOT NULL)")) {
            ps.execute();
        }
        clients = new ClientDAO(connexio);
        productes = new ProducteDAO(connexio);
    }

    @AfterEach
    void tancar() throws SQLException { connexio.close(); }

    @Test
    void crudClientsIApostrofs() throws SQLException {
        assertTrue(clients.llistar().isEmpty());
        Client client = new Client(0, "D'Angelo", "dangelo@example.com");
        clients.inserir(client);
        assertTrue(client.getId() > 0);
        assertEquals("D'Angelo", clients.cercarPerId(client.getId()).orElseThrow().getNom());
        assertEquals(1, clients.llistar().size());
        client.setNom("Anna");
        client.setCorreu("anna@example.com");
        assertTrue(clients.actualitzar(client));
        assertEquals("anna@example.com", clients.cercarPerId(client.getId()).orElseThrow().getCorreu());
        assertTrue(clients.eliminar(client.getId()));
        assertTrue(clients.cercarPerId(client.getId()).isEmpty());
        assertFalse(clients.eliminar(client.getId()));
        assertFalse(clients.actualitzar(client));
    }

    @Test
    void crudProductesIPrecisioDecimal() throws SQLException {
        assertTrue(productes.llistar().isEmpty());
        Producte p = new Producte(0, "Ratolí", new BigDecimal("19.99"), 20);
        productes.inserir(p);
        assertTrue(p.getId() > 0);
        assertEquals(new BigDecimal("19.99"), productes.cercarPerId(p.getId()).orElseThrow().getPreu());
        p.setNom("Teclat"); p.setPreu(new BigDecimal("0.10")); p.setEstoc(0);
        assertTrue(productes.actualitzar(p));
        Producte llegit = productes.llistar().get(0);
        assertEquals("Teclat", llegit.getNom());
        assertEquals(new BigDecimal("0.10"), llegit.getPreu());
        assertEquals(0, llegit.getEstoc());
        assertTrue(productes.eliminar(p.getId()));
        assertTrue(productes.cercarPerId(p.getId()).isEmpty());
        assertFalse(productes.actualitzar(p));
        assertFalse(productes.eliminar(p.getId()));
    }

    @Test
    void correuUnic() throws SQLException {
        clients.inserir(new Client(0, "Anna", "anna@example.com"));
        assertThrows(SQLException.class, () -> clients.inserir(new Client(0, "Pere", "anna@example.com")));
        assertEquals(1, clients.llistar().size());
    }

    @Test
    void nomsAmbSqlSonDades() throws SQLException {
        String nom = "x'); DROP TABLE Productes; --";
        Producte p = new Producte(0, nom, BigDecimal.ZERO, 0);
        productes.inserir(p);
        assertEquals(nom, productes.cercarPerId(p.getId()).orElseThrow().getNom());
        assertEquals(1, productes.llistar().size());
    }

    @Test
    void rebutjarDadesInvalides() {
        assertThrows(IllegalArgumentException.class, () -> clients.inserir(new Client(0, " ", "a@b.cat")));
        assertThrows(IllegalArgumentException.class, () -> clients.inserir(new Client(0, "Anna", "incorrecte")));
        assertThrows(IllegalArgumentException.class, () -> productes.inserir(new Producte(0, "P", new BigDecimal("-1"), 1)));
        assertThrows(IllegalArgumentException.class, () -> productes.inserir(new Producte(0, "P", new BigDecimal("1.999"), 1)));
        assertThrows(IllegalArgumentException.class, () -> productes.inserir(new Producte(0, "P", new BigDecimal("100000000"), 1)));
        assertThrows(IllegalArgumentException.class, () -> productes.inserir(new Producte(0, "P", BigDecimal.ONE, -1)));
    }

    @Test
    void impedirEliminarClientReferenciat() throws SQLException {
        Client c = new Client(0, "Anna", "anna@example.com");
        clients.inserir(c);
        try (PreparedStatement ps = connexio.prepareStatement("CREATE TABLE Comandes (id INT PRIMARY KEY, client_id INT REFERENCES Clients(id))")) { ps.execute(); }
        try (PreparedStatement ps = connexio.prepareStatement("INSERT INTO Comandes VALUES (1, ?)")) {
            ps.setInt(1, c.getId()); ps.executeUpdate();
        }
        assertThrows(SQLException.class, () -> clients.eliminar(c.getId()));
        assertTrue(clients.cercarPerId(c.getId()).isPresent());
    }
}
