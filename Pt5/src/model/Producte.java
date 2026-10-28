package model;

import java.math.BigDecimal;

public class Producte {
    private int id;
    private String nom;
    private BigDecimal preu;
    private int estoc;

    public Producte() { }

    public Producte(int id, String nom, BigDecimal preu, int estoc) {
        this.id = id;
        this.nom = nom;
        this.preu = preu;
        this.estoc = estoc;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public BigDecimal getPreu() { return preu; }
    public void setPreu(BigDecimal preu) { this.preu = preu; }

    public int getEstoc() { return estoc; }
    public void setEstoc(int estoc) { this.estoc = estoc; }

    @Override
    public String toString() {
        return id + " | " + nom + " | " + preu + " | " + estoc;
    }
}
