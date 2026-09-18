import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public class AnalisiFitxer {

    public static void main(String[] args) {
        File fitxer = new File("text.txt");

        int numCaracters = 0;
        int numLinies = 0;
        int numParaules = 0;
        boolean dinsParaula = false;

        int[] frequencia = new int[65536];
        char ultimCaracter = 0;
        boolean fitxerBuit = true;

        try (FileReader fr = new FileReader(fitxer)) {
            int c;
            while ((c = fr.read()) != -1) {
                char caracter = (char) c;
                fitxerBuit = false;

                // Comptem caràcters (espais i tabulacions sí, salts no)
                if (caracter != '\n' && caracter != '\r') {
                    numCaracters++;
                }

                // Comptem salts de línia
                if (caracter == '\n') {
                    numLinies++;
                }

                // Detecció de paraules i freqüència
                if (caracter == ' ' || caracter == '\t' || caracter == '\n' || caracter == '\r') {
                    dinsParaula = false;
                } else {
                    if (!dinsParaula) {
                        numParaules++;
                        dinsParaula = true;
                    }
                    frequencia[caracter]++;
                }

                ultimCaracter = caracter;
            }

            // Si el fitxer no acaba en salt de línia, sumem l'última línia
            if (!fitxerBuit && ultimCaracter != '\n') {
                numLinies++;
            }

            // Busquem el caràcter que més es repeteix
            char caracterMesRepetit = ' ';
            int maxFrequencia = 0;

            for (int i = 0; i < frequencia.length; i++) {
                if (frequencia[i] > maxFrequencia) {
                    maxFrequencia = frequencia[i];
                    caracterMesRepetit = (char) i;
                }
            }

            // Resultats per pantalla
            System.out.println("Nombre de caràcters: " + numCaracters);
            System.out.println("Nombre de línies: " + numLinies);
            System.out.println("Nombre de paraules: " + numParaules);
            
            if (maxFrequencia > 0) {
                System.out.println("Caràcter més repetit: " + caracterMesRepetit);
            } else {
                System.out.println("Caràcter més repetit: Cap caràcter vàlid");
            }

        } catch (FileNotFoundException e) {
            System.out.println("El fitxer no existeix.");
        } catch (IOException e) {
            System.out.println("Error de lectura: " + e.getMessage());
        }
    }
}
