package dao;

import model.Client;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientDAO {
    private final Connection connexio;

    public ClientDAO(Connection connexio) {
        this.connexio = connexio;
    }

    public void inserir(Client valor) throws SQLException {
        validar(valor);
        String sql = "INSERT INTO Clients (nom, correu) VALUES (?, ?)";
        try (PreparedStatement ps = connexio.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, valor.getNom());
            ps.setString(2, valor.getCorreu());
            if (ps.executeUpdate() != 1) {
                throw new SQLException("No s'ha pogut inserir el registre.");
            }
            try (ResultSet claus = ps.getGeneratedKeys()) {
                if (!claus.next()) throw new SQLException("No s'ha retornat l'identificador.");
                valor.setId(claus.getInt(1));
            }
        }
    }

    public List<Client> llistar() throws SQLException {
        List<Client> resultat = new ArrayList<>();
        try (PreparedStatement ps = connexio.prepareStatement("SELECT id, nom, correu FROM Clients ORDER BY id");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) resultat.add(llegir(rs));
        }
        return resultat;
    }

    public Optional<Client> cercarPerId(int id) throws SQLException {
        try (PreparedStatement ps = connexio.prepareStatement("SELECT id, nom, correu FROM Clients WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(llegir(rs)) : Optional.empty();
            }
        }
    }

    public boolean actualitzar(Client valor) throws SQLException {
        validar(valor);
        try (PreparedStatement ps = connexio.prepareStatement("UPDATE Clients SET nom = ?, correu = ? WHERE id = ?")) {
            ps.setString(1, valor.getNom());
            ps.setString(2, valor.getCorreu());
            ps.setInt(3, valor.getId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int id) throws SQLException {
        try (PreparedStatement ps = connexio.prepareStatement("DELETE FROM Clients WHERE id = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Client llegir(ResultSet rs) throws SQLException {
        return new Client(rs.getInt("id"), rs.getString("nom"), rs.getString("correu"));
    }

    private void validar(Client valor) {
        if (valor == null || valor.getNom() == null || valor.getNom().isBlank() || valor.getNom().length() > 100) {
            throw new IllegalArgumentException("El nom és obligatori i admet fins a 100 caràcters.");
        }
        if (valor.getCorreu() == null || valor.getCorreu().isBlank() || valor.getCorreu().length() > 100
                || !valor.getCorreu().matches("[^\\s@]+@[^\\s@]+\\.[^\\s@]+")) {
            throw new IllegalArgumentException("Cal indicar un correu vàlid de fins a 100 caràcters.");
        }
    }
}
