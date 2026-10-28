package dao;

import model.Producte;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProducteDAO {
    private final Connection connexio;

    public ProducteDAO(Connection connexio) {
        this.connexio = connexio;
    }

    public void inserir(Producte valor) throws SQLException {
        validar(valor);
        String sql = "INSERT INTO Productes (nom, preu, estoc) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connexio.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, valor.getNom());
            ps.setBigDecimal(2, valor.getPreu());
            ps.setInt(3, valor.getEstoc());
            if (ps.executeUpdate() != 1) {
                throw new SQLException("No s'ha pogut inserir el registre.");
            }
            try (ResultSet claus = ps.getGeneratedKeys()) {
                if (!claus.next()) throw new SQLException("No s'ha retornat l'identificador.");
                valor.setId(claus.getInt(1));
            }
        }
    }

    public List<Producte> llistar() throws SQLException {
        List<Producte> resultat = new ArrayList<>();
        try (PreparedStatement ps = connexio.prepareStatement("SELECT id, nom, preu, estoc FROM Productes ORDER BY id");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) resultat.add(llegir(rs));
        }
        return resultat;
    }

    public Optional<Producte> cercarPerId(int id) throws SQLException {
        try (PreparedStatement ps = connexio.prepareStatement("SELECT id, nom, preu, estoc FROM Productes WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(llegir(rs)) : Optional.empty();
            }
        }
    }

    public boolean actualitzar(Producte valor) throws SQLException {
        validar(valor);
        try (PreparedStatement ps = connexio.prepareStatement("UPDATE Productes SET nom = ?, preu = ?, estoc = ? WHERE id = ?")) {
            ps.setString(1, valor.getNom());
            ps.setBigDecimal(2, valor.getPreu());
            ps.setInt(3, valor.getEstoc());
            ps.setInt(4, valor.getId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int id) throws SQLException {
        try (PreparedStatement ps = connexio.prepareStatement("DELETE FROM Productes WHERE id = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Producte llegir(ResultSet rs) throws SQLException {
        return new Producte(rs.getInt("id"), rs.getString("nom"), rs.getBigDecimal("preu"), rs.getInt("estoc"));
    }

    private void validar(Producte valor) {
        if (valor == null || valor.getNom() == null || valor.getNom().isBlank() || valor.getNom().length() > 100) {
            throw new IllegalArgumentException("El nom és obligatori i admet fins a 100 caràcters.");
        }
        if (valor.getPreu() == null || valor.getPreu().signum() < 0
                || valor.getPreu().compareTo(new java.math.BigDecimal("99999999.99")) > 0
                || valor.getPreu().stripTrailingZeros().scale() > 2) {
            throw new IllegalArgumentException("El preu ha de ser entre 0 i 99999999.99, amb un màxim de dos decimals.");
        }
        if (valor.getEstoc() < 0) throw new IllegalArgumentException("L'estoc no pot ser negatiu.");
    }
}
