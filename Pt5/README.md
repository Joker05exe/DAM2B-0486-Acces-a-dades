# PT5 - Botiga online: part 1

CRUD de **clients** i **productes** en Java 17 amb JDBC i MySQL. Permet afegir,
llistar, cercar per ID, actualitzar i eliminar registres des d'un menú de consola.
Totes les consultes dels DAO utilitzen `PreparedStatement` i els recursos JDBC
es tanquen amb `try-with-resources`. Els preus es gestionen amb `BigDecimal`.

## Requisits

- JDK 17 o superior i Maven 3.9 o superior.
- MySQL 8.0.16 o superior en execució.
- Usuari MySQL amb permisos per crear l'esquema i gestionar les dades.

## Preparar la base de dades

Des de la carpeta `Pt5`, obre el client de MySQL:

```text
mysql -u root -p
```

Al client, executa (fes servir rutes absolutes amb `/` si cal):

```sql
SOURCE sql/schema.sql;
SOURCE sql/dades_prova.sql;
```

El segon script és opcional i s'ha d'executar una sola vegada sobre una BD nova.
L'esquema inclou les cinc taules de l'enunciat per continuar les parts següents.
Els productes Ratolí i Pantalla de les dades de prova tenen descomptes associats:
MySQL impedirà eliminar-los mentre existeixin aquestes referències. Pots provar
l'eliminació amb un producte nou o amb Teclat.

## Configurar i executar

En PowerShell, des de `Pt5`:

```powershell
$env:BOTIGA_DB_URL = 'jdbc:mysql://localhost:3306/botiga'
$env:BOTIGA_DB_USER = 'root'
$env:BOTIGA_DB_PASSWORD = 'la_teva_contrasenya'
mvn compile exec:java
```

En Linux/macOS:

```bash
export BOTIGA_DB_URL='jdbc:mysql://localhost:3306/botiga'
export BOTIGA_DB_USER='root'
export BOTIGA_DB_PASSWORD='la_teva_contrasenya'
mvn compile exec:java
```

No cal descarregar manualment el connector JDBC: Maven el resol amb el `pom.xml`.
Sense variables, la connexió usa `localhost:3306/botiga`, usuari `root` i contrasenya buida.
Les credencials reals es configuren a l'entorn i no es desen al repositori.

## Ús

1. Tria **Gestionar productes** o **Gestionar clients**.
2. Tria afegir, llistar, cercar, actualitzar o eliminar.
3. En actualitzar, indica l'ID i totes les dades noves.
4. `0` torna al menú anterior o surt de l'aplicació.

S'admet punt o coma als preus, amb un màxim de dos decimals. No s'admeten noms
buits, correus invàlids, preus negatius ni estoc negatiu. El correu és únic a la BD.
Els IDs inexistents i els errors de base de dades es mostren al menú.

## Estructura

```text
sql/schema.sql          Esquema MySQL de l'enunciat
sql/dades_prova.sql     Dades opcionals
src/util/Connexio.java Connexió amb DriverManager
src/model/             POJO Client i Producte
src/dao/               ClientDAO i ProducteDAO
src/App.java           Menú de consola
tests/CrudTest.java    Proves d'integració JDBC
```

## Comprovar

```text
mvn test
```

Les proves executen els DAO contra H2 en mode MySQL: CRUD de les dues entitats,
IDs generats i inexistents, precisió monetària, correus duplicats, dades invàlides,
claus foranes i noms amb apòstrofs o contingut SQL. No substitueixen una prova
amb un servidor MySQL real. Per comprovar-lo manualment, crea un client i un
producte, llista'ls, cerca'ls per ID, modifica'ls i elimina'ls; reinicia el programa
entre operacions per verificar la persistència.

Les transaccions de comandes, els savepoints i les consultes JOIN corresponen a
les parts 2, 3 i 4 i queden fora d'aquesta entrega.
