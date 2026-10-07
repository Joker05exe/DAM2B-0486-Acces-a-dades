import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

/**
 * Part 1: crea el fitxer zoo.xml amb arrel <zoo> i diversos <animal>.
 */
public class CrearZoo {

    public static void main(String[] args) {
        String[][] animals = {
                {"1", "Pingüí", "Aptenodytes forsteri", "Peix", "5"},
                {"2", "Lleó", "Panthera leo", "Carn", "8"},
                {"3", "Elefant", "Loxodonta africana", "Herba", "20"},
                {"4", "Girafa", "Giraffa camelopardalis", "Fulles", "12"},
                {"5", "Tigre", "Panthera tigris", "Carn", "7"}
        };

        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

            Element zoo = doc.createElement("zoo");
            doc.appendChild(zoo);

            for (String[] a : animals) {
                Element animal = doc.createElement("animal");
                animal.setAttribute("id", a[0]);
                zoo.appendChild(animal);

                afegirElement(doc, animal, "nom", a[1]);
                afegirElement(doc, animal, "especie", a[2]);
                afegirElement(doc, animal, "aliment", a[3]);
                afegirElement(doc, animal, "edat", a[4]);
            }

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.transform(new DOMSource(doc), new StreamResult(new File("zoo.xml")));

            System.out.println("Fitxer zoo.xml creat correctament amb " + animals.length + " animals.");
        } catch (Exception e) {
            System.out.println("Error creant l'XML: " + e.getMessage());
        }
    }

    private static void afegirElement(Document doc, Element pare, String nom, String valor) {
        Element element = doc.createElement(nom);
        element.setTextContent(valor);
        pare.appendChild(element);
    }
}
