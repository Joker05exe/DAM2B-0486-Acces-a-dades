# Problemàtiques de l'ús de fitxers binaris amb `ObjectOutputStream`

---

## 1. Trencament de compatibilitat entre versions de la classe (`serialVersionUID`)

Java calcula automàticament un `serialVersionUID` a partir de l'estructura de la classe (atributs, mètodes, etc.) si no se n'especifica un explícitament. Si el fitxer `.dat` es va crear amb una versió de `Videojoc` i després es modifica la classe (afegir/eliminar un atribut, canviar-ne el tipus...), l'ID calculat canvia i, en llegir el fitxer antic, es llença una `InvalidClassException`, encara que el canvi sigui menor.

**Mitigació:** declarar sempre `private static final long serialVersionUID` de forma explícita i fixa a la classe, com es fa a `Videojoc.java`, perquè el control de compatibilitat quedi sota control del programador i no del càlcul automàtic.

## 2. Format no llegible ni editable

A diferència d'un `.txt` o `.csv`, un fitxer serialitzat és binari i inclou metadades internes de Java (noms de classe qualificats, tipus, `serialVersionUID`...). No es pot obrir, inspeccionar ni corregir amb un editor de text, la qual cosa dificulta la depuració, l'auditoria de dades o la recuperació manual davant d'un error de format.

## 3. No interoperable entre llenguatges ni plataformes

El format de serialització de Java és propietari: només una JVM sap desserialitzar-lo correctament. Un fitxer `videojocs.dat` no es pot llegir des de Python, JavaScript, una base de dades o un altre servei sense passar per Java. Això limita molt la integració amb altres sistemes, a diferència de formats estàndard com JSON o XML.

## 4. Risc de seguretat en desserialitzar fitxers no confiables

`readObject()` reconstrueix objectes directament a partir dels bytes del fitxer, executant constructors i, en alguns casos, codi personalitzat (`readObject` sobreescrit, `readResolve`, etc.). Si el fitxer prové d'una font no confiable, un atacant podria manipular els bytes per forçar la creació d'objectes maliciosos (atacs coneguts com *deserialization attacks* o *gadget chains*). Per això mai s'ha de desserialitzar un fitxer l'origen del qual no es controla.

## 5. Acoblament fort entre dades i codi

El fitxer binari només té sentit si es conserva (o és compatible amb) el `.class` original de `Videojoc`. Si es perd el codi font, o s'elimina/renombra la classe, les dades guardades queden pràcticament inservibles, ja que no hi ha manera d'interpretar-les sense la definició de classe corresponent.

## 6. Dificultat de control de versions i `diff`

En ser binari, un `git diff` sobre `videojocs.dat` no mostra canvis comprensibles (ni línia afegida/eliminada amb sentit), a diferència d'un fitxer de text pla. Això fa més difícil revisar l'evolució de les dades dins d'un repositori.

---

## Conclusió

La serialització nativa de Java (`ObjectOutputStream`/`ObjectInputStream`) és còmoda per a prototips ràpids o persistència interna d'una única aplicació Java, però no és adequada per a dades que han de perdurar molt de temps, compartir-se entre sistemes diferents o rebre's d'origens no confiables. Per a aquests casos són preferibles formats de text estàndard (JSON, XML, CSV) o una base de dades relacional.
