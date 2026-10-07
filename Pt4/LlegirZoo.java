import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

/**
 * Part 2: llegeix zoo.xml i mostra els animals. Inclou els bonus:
 * comptar animals i mostrar només els que mengen "Carn".
 */
public class LlegirZoo {

    public static void main(String[] args) {
        try {
            Document doc = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder().parse(new File("zoo.xml"));
            doc.getDocumentElement().normalize();

            NodeList animals = doc.getElementsByTagName("animal");

            for (int i = 0; i < animals.getLength(); i++) {
                mostrarAnimal((Element) animals.item(i));
            }

            // Bonus 1: comptar animals
            System.out.println("Total d'animals al zoo: " + animals.getLength());
            System.out.println();

            // Bonus 2: només els que mengen carn
            System.out.println("Animals que mengen Carn:");
            for (int i = 0; i < animals.getLength(); i++) {
                Element animal = (Element) animals.item(i);
                if (text(animal, "aliment").equalsIgnoreCase("Carn")) {
                    mostrarAnimal(animal);
                }
            }
        } catch (Exception e) {
            System.out.println("Error llegint l'XML (has executat abans CrearZoo?): " + e.getMessage());
        }
    }

    private static void mostrarAnimal(Element animal) {
        System.out.println("Animal #" + animal.getAttribute("id"));
        System.out.println("Nom: " + text(animal, "nom"));
        System.out.println("Espècie: " + text(animal, "especie"));
        System.out.println("Aliment: " + text(animal, "aliment"));
        System.out.println("Edat: " + text(animal, "edat"));
        System.out.println();
    }

    private static String text(Element pare, String etiqueta) {
        return pare.getElementsByTagName(etiqueta).item(0).getTextContent();
    }
}
