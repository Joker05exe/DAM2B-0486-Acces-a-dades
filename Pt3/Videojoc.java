import java.io.Serializable;

public class Videojoc implements Serializable {

    private static final long serialVersionUID = 1L;

    private String titol;
    private String genere;
    private int anyLlancament;
    private String plataforma;
    private double preu;

    public Videojoc(String titol, String genere, int anyLlancament, String plataforma, double preu) {
        this.titol = titol;
        this.genere = genere;
        this.anyLlancament = anyLlancament;
        this.plataforma = plataforma;
        this.preu = preu;
    }

    public String getTitol() {
        return titol;
    }

    public void setTitol(String titol) {
        this.titol = titol;
    }

    public String getGenere() {
        return genere;
    }

    public void setGenere(String genere) {
        this.genere = genere;
    }

    public int getAnyLlancament() {
        return anyLlancament;
    }

    public void setAnyLlancament(int anyLlancament) {
        this.anyLlancament = anyLlancament;
    }

    public String getPlataforma() {
        return plataforma;
    }

    public void setPlataforma(String plataforma) {
        this.plataforma = plataforma;
    }

    public double getPreu() {
        return preu;
    }

    public void setPreu(double preu) {
        this.preu = preu;
    }

    @Override
    public String toString() {
        return "Títol: " + titol
                + " | Gènere: " + genere
                + " | Any: " + anyLlancament
                + " | Plataforma: " + plataforma
                + " | Preu: " + preu + "€";
    }
}
