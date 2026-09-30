import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Xifrador {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Introdueix la clau de xifrat (desplaçament Cèsar): ");
        int clau = sc.nextInt();
        sc.close();

        xifrar("entrada.txt", "xifrat.txt", clau);
        desxifrar("xifrat.txt", "desxifrat.txt", clau);
    }

    // Inverteix el contingut d'una línia
    private static String invertirLinia(String linia) {
        return new StringBuilder(linia).reverse().toString();
    }

    // Desplaça cada caràcter de la línia N posicions en Unicode
    private static String desplacarCesar(String linia, int desplacament) {
        StringBuilder sb = new StringBuilder();
        for (char c : linia.toCharArray()) {
            sb.append((char) (c + desplacament));
        }
        return sb.toString();
    }

    // Llegeix entrada, inverteix cada línia, aplica el xifrat Cèsar i escriu el resultat
    public static void xifrar(String fitxerEntrada, String fitxerSortida, int clau) {
        System.out.println("Xifrant \"" + fitxerEntrada + "\" -> \"" + fitxerSortida + "\"...");

        try (
            BufferedReader br = new BufferedReader(new FileReader(fitxerEntrada));
            BufferedWriter bw = new BufferedWriter(new FileWriter(fitxerSortida))
        ) {
            String linia;
            int numLinies = 0;

            while ((linia = br.readLine()) != null) {
                String invertida = invertirLinia(linia);
                String xifrada = desplacarCesar(invertida, clau);
                bw.write(xifrada);
                bw.newLine();
                numLinies++;
            }

            System.out.println("Xifrat complet: " + numLinies + " línies processades.");

        } catch (FileNotFoundException e) {
            System.out.println("Error: no s'ha trobat el fitxer \"" + fitxerEntrada + "\".");
        } catch (IOException e) {
            System.out.println("Error d'entrada/sortida: " + e.getMessage());
        }
    }

    // Llegeix el fitxer xifrat, desfà el desplaçament, torna a invertir la línia i escriu el missatge original
    public static void desxifrar(String fitxerXifrat, String fitxerSortida, int clau) {
        System.out.println("Desxifrant \"" + fitxerXifrat + "\" -> \"" + fitxerSortida + "\"...");

        try (
            BufferedReader br = new BufferedReader(new FileReader(fitxerXifrat));
            BufferedWriter bw = new BufferedWriter(new FileWriter(fitxerSortida))
        ) {
            String linia;
            int numLinies = 0;

            while ((linia = br.readLine()) != null) {
                String desplacada = desplacarCesar(linia, -clau);
                String original = invertirLinia(desplacada);
                bw.write(original);
                bw.newLine();
                numLinies++;
            }

            System.out.println("Desxifrat complet: " + numLinies + " línies processades.");

        } catch (FileNotFoundException e) {
            System.out.println("Error: no s'ha trobat el fitxer \"" + fitxerXifrat + "\".");
        } catch (IOException e) {
            System.out.println("Error d'entrada/sortida: " + e.getMessage());
        }
    }
}
