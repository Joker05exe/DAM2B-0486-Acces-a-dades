package model;

public class Client {
    private int id;
    private String nom;
    private String correu;

    public Client() { }

    public Client(int id, String nom, String correu) {
        this.id = id;
        this.nom = nom;
        this.correu = correu;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getCorreu() { return correu; }
    public void setCorreu(String correu) { this.correu = correu; }

    @Override
    public String toString() {
        return id + " | " + nom + " | " + correu;
    }
}
