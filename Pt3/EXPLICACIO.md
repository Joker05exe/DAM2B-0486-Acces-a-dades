# Pt3 – Gestió de videojocs amb persistència binària

## Què fa el programa

És una aplicació de consola que porta un catàleg de videojocs. A través d'un menú es poden
**afegir, llistar, cercar per títol, modificar i eliminar** videojocs (el CRUD). Quan es tanca i
es torna a obrir, el catàleg continua allà perquè es guarda en un fitxer.

Està dividit en dues classes:

| Classe | Paper |
|---|---|
| `Videojoc` | Representa un videojoc: títol, gènere, any, plataforma i preu. Implementa `Serializable`. |
| `GestioVideojocs` | Conté el `main`, el menú, les operacions del CRUD i la càrrega/desat del fitxer. |

## Com es guarden les dades

Les dades van a `videojocs.dat`, un **fitxer binari**: no conté text, sinó els bytes de l'objecte
Java. Si l'obrim amb el Bloc de notes només es veuen símbols estranys.

- **Per desar**, `ObjectOutputStream` escriu tota la llista (`ArrayList<Videojoc>`) amb un únic
  `writeObject`.
- **Per carregar**, `ObjectInputStream` llegeix la llista amb `readObject` en arrencar.
- Perquè això funcioni, `Videojoc` ha d'implementar `Serializable`. Li he posat un
  `serialVersionUID` fix per identificar la versió de la classe.

Cada vegada que es fa un canvi (afegir, modificar o eliminar) es torna a desar el fitxer, de manera
que no es perd res encara que el programa es tanqui de cop.

## Decisions que he pres

- **Guardo la llista sencera i no objecte per objecte.** Així el fitxer sempre és coherent i no
  m'he de preocupar de l'*append* amb `ObjectOutputStream`, que escriu una capçalera nova a cada
  obertura i fa que el fitxer es pugui llegir malament.
- **`try-with-resources`** per tancar els fluxos sempre, tant si va bé com si falla.
- **Si el fitxer no existeix o està malmès**, el programa comença amb una llista buida i avisa de
  l'error, en lloc de petar.
- **Validació d'entrada:** els números es llegeixen en una línia i es tornen a demanar si no són
  vàlids (també accepta la coma als decimals). Així el `Scanner` no es queda amb salts de línia
  pendents.

## Limitacions de la serialització binària

1. **Va lligada a la classe.** Si canvio `Videojoc` (per exemple, el tipus d'un camp), un
   `videojocs.dat` antic pot no llegir-se.
2. **No es pot editar a mà.** Per veure o corregir una dada cal passar pel programa.
3. **Només per a Java.** Un programa en Python, per exemple, no entendria el fitxer.
4. **Segur només si el fitxer és de confiança.** Deserialitzar un fitxer d'un origen desconegut
   pot ser perillós.
5. **Un fitxer tallat a mitges queda inservible.** Per això es desa sencer i amb fluxos tancats.

Si el catàleg s'hagués de compartir amb altres sistemes o es volgués poder llegir amb un editor,
seria millor un format de text com XML o JSON (que és el que es treballa a la Pt4).

## Com executar-lo

```bash
javac Videojoc.java GestioVideojocs.java
java GestioVideojocs
```

El fitxer `videojocs.dat` es crea a la carpeta des d'on s'executa el programa.
