package classes;
import java.io.*;
import java.nio.file.*;
import java.io.BufferedReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Clase que conte tots los metodes del programa y metodes relacionats con les demes clases.
 */
public class GameMechanics {

////////////////////////////////////////////////////LLIGA//////////////////////////////////////////////////////////////
    ///Metodes Relacionats amb la Clase Lliga

    /**
     * GUARDAR @param Lliga, afaga el objecte liga i el guarda en un txt
     * en la capeta de ficheros < FicherosPartida < Lliga_partida.txt
     */

    public static void guardarLliga(Lliga lliga) {
        String contingut = lligaToTxt(lliga);
        guardarEnFitxer(contingut);
    }

    /**
     * Passa a format String els artibuts de la lliga i els dona un format de csv
     * format txt separat per ";"
     *
     * @param lliga la lliga
     * @return the string
     */
    public static String lligaToTxt(Lliga lliga) {
        return lliga.getCodiLliga() + ";" +
                lliga.getJornades() + ";" +
                lliga.getNumEquips() + ";" +
                equipsToTxt(lliga.getRegistreEquips()) + ";";
    }


    /**
     * metode que agafa el hashmap dels equips i el pasa a un format ;equip1-punts,equip2-punts;
     *
     * @param registreEquips hashmap registre equips
     * @return dades txt string
     */
    public static String equipsToTxt(Map<String, Integer> registreEquips) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;

        for (Map.Entry<String, Integer> entry : registreEquips.entrySet()) {
            if (!first) {
                sb.append(",");
            } else {
                first = false;
            }
            sb.append(entry.getKey()).append("-").append(entry.getValue());
        }
        return sb.toString();
    }

    /**
     * metode que s encarrega de guardar les dades el document selecionat per la path
     *
     * @param contingut string contingut (dades que guardar)
     */
    public static void guardarEnFitxer(String contingut) {
        Path rutaFitxer = seleccioPathFitxerPartidaLliga();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaFitxer.toFile()))) {
            writer.write(contingut);
        } catch (IOException e) {
            System.out.println("Error al guardar el fitxer: " + e.getMessage());
        }
    }

    /**
     * Selecciona el path fitxer partida lliga path.
     *
     * @return  path on es troba el fitxer
     */
    public static Path seleccioPathFitxerPartidaLliga() {
        return Paths.get("ficheros", "FicheroPartida", "Lliga_partida.txt");
    }


    /**
     * CARGAR @return the lliga, afaga les dades guradades en el txt i les retorna en un nou objecte Lliga
     * desde la capeta de ficheros < FicherosPartida < Lliga_partida.txt
     */
    public static Lliga carregarLliga() {
        Path  rutaFitxer = seleccioPathFitxerPartidaLliga();
        Lliga lliga;

        if (!fitxerExisteix(rutaFitxer)) {
            lliga= null;
        } else {
            String linea = llegirLinia(rutaFitxer);
            if (linea == null) {
                lliga= null;
            } else {
                lliga = cargarLliga(linea);
            }
        }
        return lliga;
    }


    /**
     * Metode que comprobar que el fitxer existeix.
     *
     * @param rutaFitxer path ruta fitxer
     * @return boolean existeix el fitxer en la ruta
     */
    public static boolean fitxerExisteix(Path rutaFitxer) {
        if (!Files.exists(rutaFitxer)) {
            System.out.println("El fitxer '" + rutaFitxer + "' no existeix.");
            return false;
        }
        return true;
    }

    /**
     * metode que llegeix la linea del fitxer
     *
     * @param rutaFitxer path ruta fitxer
     * @return string amb les dades del fitxer
     */
    public static String llegirLinia(Path rutaFitxer) {
        try (BufferedReader reader = new BufferedReader(new FileReader(rutaFitxer.toFile()))) {
            return reader.readLine();
        } catch (IOException e) {
            System.out.println("Error al llegir el fitxer: " + e.getMessage());
            return null;
        }
    }

    /**
     * Carrega i genera la Lliga a partir de les dades del fitxer
     *
     * @param linea dades linea fitxer
     * @return Objecte Classe lliga
     */
    public static Lliga cargarLliga(String linea) {
        String[] dades = linea.split(";");
        if (!dadesSonValides(dades)) {
            return null;
        }

        String codiLliga = dades[0];
        int jornades = parsejarEnter(dades[1], "jornades");
        int numEquips = parsejarEnter(dades[2], "numEquips");

        if (!valorEsPositiu(jornades)) {
            return null;
        }

        HashMap<String, Integer> registreEquips = (HashMap<String, Integer>) parsejarEquips(dades);

        if (!nombreEquipsCoincideix(registreEquips, numEquips)) {
            return null;
        }

        System.out.println("Lliga carregada correctament");
        return new Lliga(codiLliga, jornades, numEquips, registreEquips);
    }

    /**
     * Metode que comproba el format de les dades
     *
     * @param dades String dades fitxer
     * @return boolean si el format es correcte
     */
    public static boolean dadesSonValides(String[] dades) {
        if (dades.length < 3) {
            System.out.println("Format de fitxer incorrecte.");
            return false;
        }
        return true;
    }

    /**
     * metode que transforma un String a un numero int
     *
     * @param valor   String amb valor que volem parsejar
     * @param nomCamp String amb el nom camp
     * @return int amb el valor
     */
    public static int parsejarEnter(String valor, String nomCamp) {
        try {
            return Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            System.out.println("Error al parsejar " + nomCamp + ": " + e.getMessage());
            return -1;
        }
    }

    /**
     * metode que transforma les dades tipo string dels equips de la lliga en un HashMap
     *
     * @param dades string dades fitxer
     * @return HashMap dades lliga
     */
    public static Map<String, Integer> parsejarEquips(String[] dades) {
        if (dades.length <= 3) {
            return new HashMap<>();
        }

        Map<String, Integer> registreEquips = new HashMap<>();
        String dadesEquips = dades[3];

        if (!dadesEquips.isEmpty()) {
            String[] equips = dadesEquips.split(",");
            for (String equip : equips) {
                if (!equip.isEmpty()) {
                    String[] infoEquip = equip.split("-");
                    int punts = parsejarEnter(infoEquip[1], "punts de l'equip " + infoEquip[0]);
                    if (punts != -1) {
                        registreEquips.put(infoEquip[0], punts);
                    }
                }
            }
        }
        return registreEquips;
    }


    /**
     * comproba que els valos sempre son positius
     *
     * @param valor int valor a comprobar
     * @return boolean si es un numero positiu
     */
    public static boolean valorEsPositiu(int valor) {
        if (valor <= 0) {
            System.out.println("Els han de ser valors positius.");
            return false;
        }
        return true;
    }

    /**
     * comproba que el numero de equips es igual al numero de resistre de equip en la lliga
     *
     * @param registreEquips hashmap amb registre equips
     * @param numEquips      numero de equips de la lliga
     * @return boolean si coincideix
     */
    public static boolean nombreEquipsCoincideix(Map<String, Integer> registreEquips, int numEquips) {
        if (registreEquips.size() != numEquips) {
            System.out.println("El numero de equips registrats a la lliga no coincideix amb el numero de equips actius.");
            return false;
        }
        return true;
    }

    /**
     * Metode per la creacio de una nova lliga
     *
     * @return Objecte Classe lliga
     */
    public static Lliga crearNovaLliga() {
        missatgeCrearNovaLligaNovaPartida();

        missatgeCrearNovaLligaCodiLliga();
        String codiLliga = scanString();

        int jornades = registrarJornades();

        int numEquips = 0;
        HashMap<String, Integer> registreEquips = new HashMap<>();
        return new Lliga(codiLliga, jornades, numEquips, registreEquips);

    }

    /**
     * registrar l atribut jornada i comproba que sigui un nombre positu
     *
     * @return int numero de jornades
     */
    public static int registrarJornades(){
        int jornades;
        do {
            missatgeCrearNovaLligaNumJornades();
            jornades = scanInt();
            return jornades;
        } while (!valorEsPositiu(jornades));
    }


    /**
     * Missatge creacio de una nova lliga en una nova partida.
     */
    public static void missatgeCrearNovaLligaNovaPartida() {
        System.out.println("Per iniciar una nova patida s'ha de crear una lliga");
    }

    /**
     * Missatge intoduccio atribut codi lliga.
     */
    public static void missatgeCrearNovaLligaCodiLliga() {
        System.out.print("Introdueix el nom o codi per la lliga: ");
    }


    /**
     *Missatge intoduccio atribut numero de jornades de la lliga
     */
    public static void missatgeCrearNovaLligaNumJornades() {
        System.out.print("Introdueix el número de jornades: ");
    }


///////////////////////////////////////////////////MERCAT FITXATGES///////////////////////////////////////////////////
    ///metodes relacionats amb el fitxer mercat_fitxatges.txt

    /**
     * Seleccio path fitxer original del mercat de fitxatges.
     *
     * @return path fitxer marcat de fitxatges original
     */
    public static Path seleccioPathFitxerOriginalFitxatges() {
        return Paths.get("ficheros/ficheroOriginal/mercat_fitxatges.txt"); // Correcte
    }


    /**
     * Seleccio path fitxer en partida del mercat de fitxatges.
     *
     * @return path fitxer marcat de fitxatges en partida
     */
    public static Path seleccioPathFitxerPartidaFitxatges() {
        return Paths.get("ficheros", "FicheroPartida", "mercat_fitxatges_partida.txt");
    }

    /**
     * GUARDAR @param ArrayList unificada de Jugadors i Entrenadors
     * a ficheros < FicheroPartida < mercat_fitxatges_partida
     *
     * @param pd Arraylist de clase ProfesionalDeportivo que conte objectes Clase Jugador i Entrenador
     */
    public static void guardarPartidaFitxerFitxatges(ArrayList<ProfessionalDeportivo> pd) {
        Path pathFitxer = seleccioPathFitxerPartidaFitxatges();
        System.out.println("Ubicació del fitxer: " + pathFitxer.toAbsolutePath());

        try (BufferedWriter writer = Files.newBufferedWriter(pathFitxer, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            for (ProfessionalDeportivo registre : pd) {
                writer.write(registre.toString());
                writer.newLine();
            }
            missatgeGuardatTrue();

        } catch (IOException e) {
            missatgeGuardatFalse(e);
        }
    }


    /**
     * Missatge si el fitxer s'ha guardat correctament.
     */
    public static void missatgeGuardatTrue(){
        System.out.println("Fitxer guardat correctament!");
    }

    /**
     * Missatge si el fitxer no s'ha guardat.
     *
     * @param e IOException
     */
    public static void missatgeGuardatFalse(IOException e){
        System.err.println("Error al guardar el fitxer: " + e.getMessage());
    }


    /**
     * metode que carrega les dades desde el mercat de fitxatges i
     * crea una Arraylist de jugadors i entrenadors
     *
     * @param path ruta del fitxer
     * @return Arraylist Class ProfesionalDeportivo
     */
    public static ArrayList<ProfessionalDeportivo> cargarArraylistFitxatges(Path path){
        ArrayList<ProfessionalDeportivo> pd = new ArrayList<>();
        try (BufferedReader mercat = new BufferedReader(new FileReader(path.toFile()))) {
            String lineaMercat;
            while ((lineaMercat = mercat.readLine()) != null) {
                String[] atributs = lineaMercat.split(";");
                if (atributs[0].equals("J")) {
                    try {
                        Jugador j = new Jugador(
                                atributs[1],
                                atributs[2],
                                atributs[3],
                                Integer.parseInt(atributs[4]),
                                Double.parseDouble(atributs[5]),
                                Integer.parseInt(atributs[6]),
                                atributs[7],
                                Integer.parseInt(atributs[8])
                        );
                        pd.add(j);
                    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                        System.err.println("Error en processar dades del fitxer: " + path);

                    }
                } else if (atributs[0].equals("E")) {
                    try {
                        Entrenador e = new Entrenador(
                                atributs[1],
                                atributs[2],
                                atributs[3],
                                Integer.parseInt(atributs[4]),
                                Double.parseDouble(atributs[5]),
                                Integer.parseInt(atributs[6]),
                                Boolean.parseBoolean(atributs[7])
                        );
                        pd.add(e);
                    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                        System.err.println("Error en processar dades del fitxer: " + path);
                    }

                } else {
                    System.err.println("Error de lectura, formato de la linea incorrecto.");
                }
            }
        } catch (IOException e) {
            System.err.println("Error en processar dades del fitxer: " + path);
        }
        return pd;
    }




/////////////////////////////////////////////////////EQUIPS//////////////////////////////////////////////////////////
    ///Metodes relacionats amb la Clase Equip

    /**
     * Seleccio de la path del fitxer dades equips en partida
     *
     * @return path fitxer
     */
    public static Path seleccioPathFitxerPartidaEquips() {
        return Paths.get("ficheros/FicheroPartida/dades_equips_partida.txt");
    }

    /**
     * GUARDAR @param equips, metode que guarda la arraylist amb totes les dades dels equips que
     * paricipan en la lliga a la ruta a ficheros < FicheroPartida < dades_equips_partida.txt
     *
     */
    public static void guardarEquipsEnFitxer(ArrayList<Equip> equips) {
        Path path = seleccioPathFitxerPartidaEquips();

        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            for (Equip equip : equips) {
                String linia = serialitzarEquip(equip);
                writer.write(linia);
                writer.newLine();
            }
            System.out.println("Equips guardats correctament al fitxer!");
        }
        catch (IOException e) {
            System.err.println("Error al guardar els equips: " + e.getMessage());
        }
    }

    /**
     * CARGAR @return the array list, Metode per cargar les dades en el fitxer en una nova
     * arraylist del objectes Equips desde ficheros < FicheroPartida < dades_equips_partida.txt
     */
    public static ArrayList<Equip> carregarEquipsDeFitxer() {
        ArrayList<Equip> equips = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(String.valueOf(seleccioPathFitxerPartidaEquips())))) {
            String linia;
            while ((linia = reader.readLine()) != null) {
                Equip equip = deserialitzarEquip(linia);
                equips.add(equip);
            }
            System.out.println("Equips carregats correctament!");
        } catch (IOException e) {
            System.out.println("Error al carregar els equips: " + e.getMessage());
        }
        return equips;
    }

    /**
     * Metode que transforma els atributs de la Clase Equip a string en format csv
     * String de dades sparades per ";"
     *
     * @param equip equip al que aplicar el metode
     * @return string amb les dades
     */
    public static String serialitzarEquip(Equip equip) {
        StringBuilder sb = new StringBuilder();
        sb.append(equip.getNomEquip()).append(";")
                .append(equip.getAnyFundacio()).append(";")
                .append(equip.getCiutat()).append(";")
                .append(equip.getNomEstadi()).append(";")
                .append(serialitzarPresident(equip.getPresident())).append(";")
                .append(serialitzarEntrenador(equip.getEntrenador())).append(";");

        if (equip.getJugadors() != null) {
            for (int i = 0; i < equip.getJugadors().size(); i++) {
                sb.append(serialitzarJugador(equip.getJugadors().get(i)));
                if (i != equip.getJugadors().size() - 1) {
                    sb.append(",");
                }
            }
        }
        sb.append(";");

        return sb.toString();
    }

    /**
     * Metode que transforma les dades string en format csv a atributs de la Clase Equip
     * String de dades sparades per ";"
     *
     * @param linia  String dades del equip
     * @return objecte classe Equip
     */
    public static Equip deserialitzarEquip(String linia) {
        String[] dades = linia.split(";");
        String nomEquip = dades[0];
        int anyFundacio = Integer.parseInt(dades[1]);
        String ciutat = dades[2];
        String estadi = dades[3];
        President president = deserialitzarPresident(dades[4]);
        Entrenador entrenador = deserialitzarEntrenador(dades[5]);
        ArrayList<Jugador> jugadors = deserialitzarJugadors(dades.length > 6 ? dades[6] : "");
        return new Equip(nomEquip, anyFundacio, ciutat, estadi, president, jugadors, entrenador);
    }

    /**
     * Metode que transforma els atributs de la Clase President a string en format txt
     * String de dades sparades per "_"
     *
     * @param president atribut president del Equip
     * @return string dades president
     */
    public static String serialitzarPresident(President president) {
        if (president != null) {
            return president.getNom() + "_" + president.getCognom() + "_" + President.getCapital();
        }
        return "null";
    }

    /**
     * Metode que transforma les dades string a atributs de la Clase President
     * String de dades sparades per "_"
     *
     * @param dadesPresident String dades president
     * @return Object classe President
     */
    public static President deserialitzarPresident(String dadesPresident) {
        if (!dadesPresident.equals("null")) {
            String[] presidentInfo = dadesPresident.split("_");
            return new President(presidentInfo[0], presidentInfo[1], Integer.parseInt(presidentInfo[2]));
        }
        return null;
    }

    /**
     * Metode que transforma els atributs de la Clase Entrenador a string en format txt
     * String de dades sparades per "_"
     *
     * @param entrenador Ojecte clase Entrenador
     * @return string dades entrenador
     */
    public static String serialitzarEntrenador(Entrenador entrenador) {
        if (entrenador != null) {
            return entrenador.getNom() + "_" + entrenador.getCognom() + "_" + entrenador.getDataNeixament()
                    + "_" + entrenador.getNivellMotivacio() + "_" + entrenador.getSouAnual()
                    + "_" + entrenador.getNumTorneigsGuanyats() + "_" + entrenador.isSelecionadorNacional();
        }
        return "null";
    }

    /**
     * Metode que transforma les dades string a atributs de la Clase Entrenador
     * String de dades sparades per "_"
     *
     * @param dadesEntrenador string dades entrenador
     * @return Objecte classe Entrenador
     */
    public static Entrenador deserialitzarEntrenador(String dadesEntrenador) {
        if (!dadesEntrenador.equals("null")) {
            String[] entrenadorInfo = dadesEntrenador.split("_");
            return new Entrenador(entrenadorInfo[0], entrenadorInfo[1],entrenadorInfo[2], Integer.parseInt(entrenadorInfo[3]),
                    Double.parseDouble(entrenadorInfo[4]),Integer.parseInt(entrenadorInfo[5]),Boolean.parseBoolean(entrenadorInfo[6]));
        }
        return null;
    }

    /**
     * Metode que transforma els atributs de la Clase Jugador a string en format txt
     * String de dades sparades per "_"
     *
     * @param jugador Objecte clase jugador
     * @return string dades jugador
     */
    public static String serialitzarJugador(Jugador jugador) {
        if (jugador != null) {
            return jugador.getNom() + "_" + jugador.getCognom() + "_" + jugador.getDataNeixament() + "_"
                    + jugador.getNivellMotivacio() + "_" + jugador.getPreu() + "_" + jugador.getNumDorsal() + "_"
                    + jugador.getPosicio() + "_" + jugador.getRendiment();
        } else {
            return "null";
        }
    }

    /**
     * Metode que transforma les dades string la Arraylist de Jugadors i els atributs de cada Jugador
     * Sepraracio entre jugadors ","
     * Spararacio atributs jugadors "_"
     *
     * @param dadesJugadors string dades arraylist Jugadors
     * @return Arraylist Clase Jugador
     */
    public static ArrayList<Jugador> deserialitzarJugadors(String dadesJugadors) {
        ArrayList<Jugador> jugadors = new ArrayList<>();
        if (!dadesJugadors.equals("null")) {
            String[] jugadorsArray = dadesJugadors.split(",");
            for (String j : jugadorsArray) {
                if (!j.isEmpty()) {
                    String[] info = j.split("_");
                    Jugador jugador = new Jugador(info[0], info[1], info[2], Integer.parseInt(info[3]),
                            Double.parseDouble(info[4]), Integer.parseInt(info[5]), info[6], Integer.parseInt(info[7]));
                    jugadors.add(jugador);
                }
            }
        }
        return jugadors;
    }

    public static String[] indexacioISeleccioLlistaEquips(ArrayList<Equip> dadesEquips) {
        String[] selectEquip = new String[2];

        System.out.println("\n--- Equips disponibles ---");
        for (int i = 0; i < dadesEquips.size(); i++) {
            System.out.println((i + 1) + ". " + dadesEquips.get(i).getNomEquip());
        }

        System.out.print("\nSelecciona el número de l'equip: ");
        int seleccion = scanInt();

        if (seleccion < 1 || seleccion > dadesEquips.size()) {
            System.out.println("Selecció no vàlida!");
            return null;
        }

        int index = seleccion - 1;
        selectEquip[0] = String.valueOf(index);
        selectEquip[1] = dadesEquips.get(index).getNomEquip();

        return selectEquip;
    }



//////////////////////////////////////////////////////JUGADORS//////////////////////////////////////////////////////////
    ///Metodes relacionats amb la Clase Jugador

    /**
     * Mostrar missatge sense canvi.
     */
    public static void mostrarMissatgeSenseCanvi() {
        System.out.println("El jugador no ha canviat de posició.");
    }

    /**
     * Mostrar missatge motivacio 1.
     */
    public static void mostrarMissatgeMotivacio1() {
        System.out.println("La seva motivació a augmentat en 1 punt.");
    }

    /**
     * Mostrar missatge motivacio 2.
     */
    public static void mostrarMissatgeMotivacio2() {
        System.out.println("La seva motivació a augmentat en 2 punts.");
    }

    /**
     * Mostrar missatge desotivacio 1.
     */
    public static void mostrarMissatgeDesotivacio1() {
        System.out.println("La seva motivació a disminuit en 1 punt.");
    }

    /**
     * Mostrar missatge desotivacio 2.
     */
    public static void mostrarMissatgeDesotivacio2() {
        System.out.println("La seva motivació a disminuit en 2 punt.");
    }

    /**
     * De la Array del mercat de jugadors en crea una nova array amb nomes el
     * llistat de Jugadors
     *
     * @param pd Arraylist ProfesionalDeportivo mercat fitxatges
     * @return Arraylist Jugadors
     */
    public static ArrayList<Jugador> seleccionMercatSoloJugadores(ArrayList<ProfessionalDeportivo> pd){
        ArrayList<Jugador> mercatJugadors = new ArrayList<>();
        for (int i = 0; i < pd.size(); i++) {
            if (pd.get(i) instanceof Jugador){
                mercatJugadors.add((Jugador) pd.get(i));
            }
        }
        return mercatJugadors;
    }




    /**
     * Metode que introdueix i registra les dades de un nou jugador
     *
     * @return object Clase jugador
     */

    public static Jugador nouJugador() {
        String nomJugador = pedirNomJugador();
        String cognomJugador = pedirCognomJugador();
        String dataNeixementJugador = pedirDataNaixementJugador();
        String posicioJugador = pedirPosicioJugador();
        double souAnual = pedirSouAnualJugador();
        int numDorsal = pedirNumDorsalJugador();
        int nivellMotivacio = numRandom10();
        int rendiment = numRandom100();

        return new Jugador(nomJugador, cognomJugador, dataNeixementJugador, nivellMotivacio,
                souAnual, numDorsal, posicioJugador,rendiment);
    }



    /**
     * Mostrar missatge jugador fitxat.
     */
    public static void mostrarMissatgeJugadorFitxat() {
        System.out.println("Jugador fitxat correctament.");
    }

    /**
     * Demanar nom del jugador
     *
     * @return string nom del jugador
     */
    public static String pedirNomJugador() {
        System.out.print("Nom del jugador: ");
        return scanString();
    }

    /**
     * Demanar cognom del jugador
     *
     * @return string cognom del jugador
     */
    public static String pedirCognomJugador() {
        System.out.print("Cognom del jugador: ");
        return scanString();
    }


    /**
     * Demanar data naixement del jugador
     *
     * @return  string data naixement del jugador
     */
    public static String pedirDataNaixementJugador() {
        System.out.print("Data de naixement del jugador: ");
        return validaDataNeixament();
    }

    /**
     * Demanar i valida la posicio del jugador
     *
     * @return string posicio del jugador
     */
    public static String pedirPosicioJugador() {
        System.out.print("Posició del jugador [DAV][MIG][DEF][POR]: ");
        return validarPosicions();
    }

    /**
     * Validar posicio que pot tenir un jugador a nomes una d'aquestes
     *
     * @return string pocicio validada
     */
    public static String validarPosicions() {
        String[] posicions = {"DAV", "MIG", "DEF", "POR"};
        String posicio;
        do {
            posicio = scanString().toUpperCase();

            for (String pos : posicions) {
                if (pos.equals(posicio)) {
                    return posicio;
                }
            }
            System.out.println("Posició invàlida. Si us plau, introdueix una posició vàlida (DAV, MIG, DEF, POR).");
        } while (true);
    }

    /**
     * Demanar sou anual del jugador
     *
     * @return  double sou Anual
     */
    public static double pedirSouAnualJugador() {
        System.out.print("Sou anual del jugador: ");
        return scanDouble();
    }

    /**
     * Demanar numero de dorsal del jugador
     *
     * @return int numero de dorsal
     */
    public static int pedirNumDorsalJugador() {
        System.out.print("Número de dorsal del jugador: ");
        return scanInt();
    }

    /**
     * Mostrar missatge jugador alta true.
     */
    public static void mostrarMissatgeJugadorAltaTrue() {
        System.out.println("Jugador donat d'alta correctament!.");
    }

    /**
     * Mostrar missatge jugador alta false.
     */
    public static void mostrarMissatgeJugadorAltaFalse() {
        System.out.println("Error al donar d'alta al jugador.");
    }



/////////////////////////////////////////////////ENTRENADORS//////////////////////////////////////////////////////////
    ///Metodes relacionats amb la Clase Entrenador


    /**
     * Metode que calcula i aumenta el sou anual del entrenador.
     *
     * @param e1 Object classe Entrenador
     */
    public static void aumentarSouAnualEntrenador(Entrenador e1){
        double increment = e1.calcularIncrement();
        System.out.println("L'aument seria de " + increment);
        System.out.println("El nou sou anual es de " + e1.aumentarSouAnual(increment));
    }


    /**
     * De la Array del mercat de jugadors en crea una nova array amb nomes el
     * llistat de entrenadors
     *
     * @param pd Arraylist ProfesionalDeportivo mercat fitxatges
     * @return Arraylist Entrenadors
     */
    public static ArrayList<Entrenador> seleccionMercatSoloEntrenadores(ArrayList<ProfessionalDeportivo> pd){
        ArrayList<Entrenador> mercatEntrenadors = new ArrayList<>();
        for (int i = 0; i < pd.size(); i++) {
            if (pd.get(i) instanceof Entrenador){
                mercatEntrenadors.add((Entrenador) pd.get(i));
            }
        }
        return mercatEntrenadors;
    }



    /**
     * Metode que introdueix i registra les dades de un nou entrenador
     *
     * @return object Clase Entrenador
     */
    public static Entrenador nouEntrenador() {
        String nomEntrenador = pedirNomEntrenador();
        String cognomEntrenador = pedirCognomEntrenador();
        String dataNeixementEntrenador = pedirDataNaixementEntrenador();
        int nivellMotivacio = numRandom10();
        double souAnual = pedirSouAnualEntrenador();
        int numTorneigsGuanyats = pedirNumTorneigsGuanyats();
        boolean seleccionadorNacional = esSeleccionadorNacional();

        return new Entrenador(nomEntrenador, cognomEntrenador, dataNeixementEntrenador, nivellMotivacio, souAnual, numTorneigsGuanyats, seleccionadorNacional);
    }


    /**
     * Missatge de entrenador destituit.
     */
    public static void missatgeEntrenadorDestituit(){
        System.out.println("Entrenador destituït correctament.");
    }

    /**
     * Missatge registrar nou entrenador.
     */
    public static void missatgeRegistrarNouEntrenador(){
        System.out.println("Registrar nou Entrenador");
    }


    /**
     * Missatge entrenador canvi true.
     */
    public static void missatgeEntrenadorCanviTrue(){
        System.out.println("entrenador modificat correctament.");
    }

    /**
     * Missatge registre de jugador complet true
     */
    public static void missatgeRegistreNouJugadorTrue(){
        System.out.println("Jugador registrat correctament.");
    }

    /**
     * Missatge entrenador canvi true.
     */
    public static void missatgeFitxarNouJugadorTrue(){
        System.out.println("Jugador fitxat correctament.");
        System.out.println();
    }

    /**
     * Missatge entrenador canvi false.
     */
    public static void missatgeEntrenadorCanviFalse(){
        System.out.println("Error al canviar el nom del entrenador.");
    }


    /**
     * Demanar el nom del entrenador
     *
     * @return string nom del entrenador
     */
    public static String pedirNomEntrenador() {
        System.out.print("Nom de l'entrenador: ");
        return scanString();
    }

    /**
     * Demanar el cognom del entrenador
     *
     * @return string cognom del entrenador
     */
    public static String pedirCognomEntrenador() {
        System.out.print("Cognom de l'entrenador: ");
        return scanString();
    }

    /**
     * Demanar data naixement del entrenador
     *
     * @return string data naixement
     */
    public static String pedirDataNaixementEntrenador() {
        System.out.print("Data de naixement de l'entrenador: ");
        return validaDataNeixament();
    }

    /**
     * Demanar nivell de motivacio del entrenador
     *
     * @return int nivell de motivacio
     */
    public static int pedirNivellMotivacio() {
        System.out.print("Nivell de motivació: ");
        return scanInt();
    }

    /**
     * Demanar sou anual del entrenador
     *
     * @return double sou anual del entrenador
     */
    public static double pedirSouAnualEntrenador() {
        System.out.print("Sou anual: ");
        return scanDouble();
    }

    /**
     * Demanar el numero de torneigs guanyats
     *
     * @return int numero de torneigs guanyats
     */
    public static int pedirNumTorneigsGuanyats() {
        System.out.print("Número de torneigs guanyats: ");
        return scanInt();
    }

    /**
     * Demanar si el entrenador es un seleccionador nacional
     *
     * @return boolean selecionardor nacional
     */
    public static boolean esSeleccionadorNacional() {
        System.out.print("Va ser seleccionador nacional? (sí/no): ");
        return scanString().equalsIgnoreCase("sí");
    }

    /**
     * Mostrar missatge entrenador alta true.
     */
    public static void mostrarMissatgeEntrenadorAltaTrue(){
        System.out.println("Entrenador donat d'alta correctament!.");
    }

    /**
     * Mostrar missatge entrenador alta false.
     */
    public static void mostrarMissatgeEntrenadorAltaFalse(){
        System.out.println("Error al donar d'alta al entrenador.");
    }



    ///////////////////////////////////////////////////ALTRES METODES///////////////////////////////////////////////////

    /**
     * Metode multius per mostrar un text de opcio
     */
    public static void introduirInput(){
        System.out.print("Opció: ");
    }


    /**
     * Scanner String
     *
     * @return string
     */
    public static String scanString(){
        return new Scanner(System.in).nextLine();
    }

    /**
     * Scanner int que valida que el numero sigui enter i el retorna
     *
     * @return int
     */
    public static int scanInt(){
        Scanner scanner = new Scanner(System.in);
        int n;

        while (true) {
            if (scanner.hasNextInt()) {
                n = scanner.nextInt();
                scanner.nextLine();
                break;
            } else {
                System.out.println("Si us plau, introdueix un número enter.");
                scanner.nextLine();
                System.out.print("Numero: ");
            }
        }

        return n;
    }

    /**
     * Scanner double
     *
     * @return double
     */
    public static double scanDouble(){
        Scanner scanner = new Scanner(System.in);
        double n;

        while (true) {
            if (scanner.hasNextDouble()) {
                n = scanner.nextDouble();
                scanner.nextLine();
                break;
            } else {
                System.out.println("Si us plau, introdueix un número decimal.");
                scanner.nextLine();
                System.out.print("Numero: ");
            }
        }

        return n;
    }

    /**
     * Escanneja una string i la converteix a boolean
     *
     * @return boolean resposta
     */
    public static boolean opcioSiNo(){
        String input = scanString();
        return input.equalsIgnoreCase("si");
    }

    /**
     * Metode que genera un numero int random del 0 al 10.
     *
     * @return un int numero random
     */
    public static int numRandom10() {
        Random random = new Random();
        return random.nextInt(10);
    }

    /**
     * Metode que genera un numero int random del 0 al 100.
     *
     * @return un int numero random
     */
    public static int numRandom100() {
        Random random = new Random();
        return random.nextInt(100);
    }

    /**
     * Valida el format de la data de naixament
     *
     * @return string data validat
     */
    public static String validaDataNeixament(){
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        Date fechaNacimiento = null;
        boolean valido = false;

        do {
            String fecha = scanString();
            try {
                fechaNacimiento = formato.parse(fecha);
                if (!fechaNacimiento.after(new Date())) {
                    valido = true;
                } else {
                    System.err.println("Data no valida");
                }
            } catch (ParseException e) {
                System.err.println("Format incorrecte, utiliza el format dd/MM/yyyy.");
                System.out.print("Data de neixament: ");
            }
        } while (!valido);

        return formato.format(fechaNacimiento);
    }




    /////////////////////////////////////////////////MENU 1/////////////////////////////////////////////////////////////
    ///Metodes relacionats amb el primer menu disponible

    /**
     * Visualitzacio menu principal.
     */
//Menu Principal
    public static void visualitzacioMenuPrincipal() {
        System.out.println("Menu inicial Football Manager!");
        System.out.println("1. Nova partida");
        System.out.println("2. Cargar partida");
        System.out.println("3. Sortir");
    }

    /**
     * Funcions menu principal.
     */
    public static void funcionsMenuPrincipal(){
        visualitzacioMenuPrincipal();
        introduirInput();
        int opcion = scanInt();
        if (opcion == 1){
            Lliga lliga = crearNovaLliga();
            guardarLliga(lliga);
            ArrayList<Equip> dadesEquips = new ArrayList<>();
            ArrayList<ProfessionalDeportivo> mercatFitxatges = cargarArraylistFitxatges(seleccioPathFitxerOriginalFitxatges());
            funcionsMenuEnPartida(lliga,dadesEquips, mercatFitxatges);

        } if (opcion == 2){
            Lliga lliga = carregarLliga();
            ArrayList<Equip> dadesEquips = carregarEquipsDeFitxer();
            ArrayList<ProfessionalDeportivo> mercatFitxatges = cargarArraylistFitxatges(seleccioPathFitxerPartidaFitxatges());
            funcionsMenuEnPartida(lliga, dadesEquips, mercatFitxatges);

        } if (opcion == 3){
            System.exit(0);
        }
    }


    /////////////////////////////////////////////////MENU 2/////////////////////////////////////////////////////////////
    ///Metodes relacionats amb el segon menu disponible

    /**
     * Visualitzar menu en partida.
     */
    public static void visualitzarMenuEnPartida(){
        System.out.println();
        System.out.println("Menu Football Manager");
        System.out.println("1- Veure classificació lliga actual");
        System.out.println("2- Gestionar equip");
        System.out.println("3- Donar d'alta equip");
        System.out.println("4- Donar d'alta jugador/a o entrenador/a");
        System.out.println("5- Consultar dades equip");
        System.out.println("6- Consultar dades jugador/a equip");
        System.out.println("7- Gestionar lliga");
        System.out.println("8- Realitzar sessió entrenament");
        System.out.println("9- Transferir jugador/a");
        System.out.println("10 - Desar dades equips");
        System.out.println("0- Sortir");
    }

    /**
     * Funcions menu en partida.
     *
     * @param lliga             Object Class lliga
     * @param dadesEquips       ArrayList Class Equip
     * @param mercatFitxatges    ArrayList Class ProfesionalDeportivo
     */
    public static void funcionsMenuEnPartida(Lliga lliga, ArrayList<Equip> dadesEquips,
                                             ArrayList<ProfessionalDeportivo> mercatFitxatges) {
        int opcion;
        do {
            visualitzarMenuEnPartida();
            introduirInput();
            opcion = scanInt();
            switch (opcion) {
                case 1:
                    mostraClasificacioLiga(lliga);
                    break;
                case 2:
                    if(dadesEquips.isEmpty()){
                        noEquiposRegistrados();
                        break;
                    } else {
                        funcionsSubMenuEquip(lliga, dadesEquips, mercatFitxatges);
                        break;
                    }

                case 3:
                    registrarEquip(lliga,dadesEquips,mercatFitxatges);
                    break;
                case 4:
                    if(dadesEquips.isEmpty()){
                        noEquiposRegistrados();
                        break;
                    } else {
                        eleccioDonarAlta(mercatFitxatges);
                        break;
                    }
                case 5:
                    if(dadesEquips.isEmpty()){
                        noEquiposRegistrados();
                        break;
                    } else {
                        mostrarDadesEquips(lliga, dadesEquips);
                        break;
                    }
                case 6:
                    if(dadesEquips.isEmpty()){
                        noEquiposRegistrados();
                        break;
                    } else {
                        mostrarDadesJugador(lliga, dadesEquips);
                        break;
                    }
                case 7:
                    gestionarLliga(lliga, dadesEquips, mercatFitxatges);
                    break;
                case 8:
                    if(dadesEquips.isEmpty()){
                        noEquiposRegistrados();
                        break;
                    } else {
                        fucionsElecioMercatfitxatges(lliga, dadesEquips, mercatFitxatges);
                        break;
                    }
                case 9:
                    if(dadesEquips.isEmpty()){
                        noEquiposRegistrados();
                        break;
                    } else {
                        transferirJugador(dadesEquips);
                        break;
                    }
                case 10:
                    guardarPartida(lliga, dadesEquips, mercatFitxatges);
                    break;
                case 0:
                    missatgeSortir();

                    break;
                default:

            }
        } while (opcion != 0);
    }


    /**
     * Missatge si no hi ha equips registrats.
     */
    public static void noEquiposRegistrados(){
        System.out.println("La lliga no conte equips");
        System.out.println("Registra primer un equip amb la opció 3 del menu");
    }

    /**
     * Missatge sortir del menu.
     */
    public static void missatgeSortir(){
        System.out.println("Sortint del menú principal.");
    }

    /////////////////////////////////////////////////////OPCIO1////////////////////////////////////////////////////

    /**
     * Imprimeix per pantalla la classificacio de la lliga de forma ordenada per la puntuacio
     * imprimeix el nom del equip i la puntuacio
     *
     * @param lliga the lliga
     */
    public static void mostraRankingLiga(Lliga lliga){
        List<Map.Entry<String, Integer>> ranking = new ArrayList<>(lliga.getRegistreEquips().entrySet());

        ranking.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        System.out.println("\n CLASSIFICACIÓ DE LA LLIGA: ");
        System.out.println("-------------------------------------------------");
        System.out.printf("%-5s %-20s %s%n", "Pos.", "Equip", "Punts");
        System.out.println("-------------------------------------------------");

        int posicio = 1;
        for (Map.Entry<String, Integer> equip : ranking) {
            System.out.printf("%-5d %-20s %d%n", posicio, equip.getKey(), equip.getValue());
            posicio++;
        }
    }

    /**
     * Metode que impirmeix les dades de la lliga i les dades de classificacio
     *
     * @param lliga Object class Lliga
     */
    public static void mostraClasificacioLiga(Lliga lliga){
        System.out.println(lliga);
        mostraRankingLiga(lliga);
    }


    /////////////////////////////////////////////////SUBMENU OPCIO 2/////////////////////////////////////////////////////////////
    ///Metodes relacionats amb el sub menu de gestionar equips

    /**
     * Visualitzar sub menu gestio del equip.
     */
    public static void visualitzarSubMenuEquip(){
        System.out.println("Menu Gestionar Equip");
        System.out.println("1- Donar de baixa equip");
        System.out.println("2- Modificar president/a");
        System.out.println("3- Destituir entrenador/a");
        System.out.println("4- Fitxar jugador/a o entrenador/a");
        System.out.println("5- Canviar posicio de un Jugador");
        System.out.println("6- Aumentar sou Entrenador");
        System.out.println("0- Sortir");
        System.out.print("Opcio: ");
    }


    /**
     * Funcions sub menu gestio del equip.
     *
     * @param lliga       Object class Lliga
     * @param dadesEquips  Arraylist de equips
     */
    public static void funcionsSubMenuEquip(Lliga lliga, ArrayList<Equip> dadesEquips,
                                            ArrayList<ProfessionalDeportivo> mercatFitxatges ) {
        int opcion;
        do {
            visualitzarSubMenuEquip();
            opcion = scanInt();
            if (opcion == 1) {
                donarBaixaEquip(lliga, dadesEquips);

            } else if (opcion == 2) {
                modificarPresident(lliga,dadesEquips);

            } else if (opcion == 3) {
                destituirIRegistrarNouEntrenador(lliga, dadesEquips, mercatFitxatges);

            } else if (opcion == 4) {
                registrarNouJugadorIEntrenador(lliga, dadesEquips, mercatFitxatges);

            } else if (opcion == 5) {
                canviarPosiciojugador(lliga,dadesEquips);

            } else if (opcion == 6) {
                aumentarSouEntrenador(lliga,dadesEquips);
            }
        } while (opcion != 0);
    }


    /**
     * Donar baixa equip de la lliga
     *
     * @param lliga       Object class Lliga
     * @param dadesEquips Arraylist de equips
     */
    public static void donarBaixaEquip(Lliga lliga, ArrayList<Equip> dadesEquips ){
        String[] selectEquip = indexacioISeleccioLlistaEquips(dadesEquips);
        if (comprobarEquipEnLliga(lliga, selectEquip[1])) {
            borrarEquip(dadesEquips,Integer.parseInt(selectEquip[0]));
            lliga.getRegistreEquips().remove(selectEquip[1]);
            missategBorrarEquipTrue();
        } else {
            missategBorrarEquipFalse();
        }
    }


    /**
     * Modificar president de un equip
     *
     * @param lliga       Object class Lliga
     * @param dadesEquips Arraylist de equips
     */
    public static void modificarPresident(Lliga lliga, ArrayList<Equip> dadesEquips ){
        String[] selectEquip = indexacioISeleccioLlistaEquips(dadesEquips);
        if (comprobarEquipEnLliga(lliga, selectEquip[1])) {
            missatgePresidenNom();
            String nomPresident = scanString();
            missatgePresidenCognom();
            String cognomPresident = scanString();
            President newPresident = new President(nomPresident, cognomPresident);
            dadesEquips.get(Integer.parseInt(selectEquip[0])).setPresident(newPresident);
            missatgePresidenCanviTrue();
        } else {
            missatgePresidenCanviFalse();
        }
    }

    /**
     * Destitueix i registrar nou entrenador.
     *
     * @param lliga       Object class Lliga
     * @param dadesEquips Arraylist de equips
     */
    public static void destituirIRegistrarNouEntrenador(Lliga lliga, ArrayList<Equip> dadesEquips,
                                                        ArrayList<ProfessionalDeportivo> mercatFitxatges ){
        String[] selectEquip = indexacioISeleccioLlistaEquips(dadesEquips);
        if (comprobarEquipEnLliga(lliga, selectEquip[1])) {
            missatgeEntrenadorDestituit();
            registrarOFitxarNouEntrenador(lliga,dadesEquips,mercatFitxatges,selectEquip);

        } else {
            missatgeEntrenadorCanviFalse();
        }
    }
    public static void menuOpcionsfitxar(){
        System.out.println("Opcions per registrar:");
        System.out.println("1- Registrar manualment");
        System.out.println("2- Fitxar desde el mercat de fitxatges");
        System.out.print("Opcio: ");

    }
    /**
     * Metode per registrar o fitxar nou entrenador.
     *
     * @param lliga           Object classe lliga
     * @param dadesEquips     Arraylist Equips amb les dades dels equips
     * @param mercatFitxatges Arraylist Profesional deportivo mercat fitxatges
     * @param selectEquip     Array amb la posicio i el nom del equip
     */
    public static void registrarOFitxarNouEntrenador(Lliga lliga, ArrayList<Equip> dadesEquips,
                                                     ArrayList<ProfessionalDeportivo> mercatFitxatges, String[] selectEquip){
        tornarEntrenadorAlMercart(selectEquip,dadesEquips,mercatFitxatges);
        menuOpcionsfitxar();

        int opcion = scanInt();
            switch (opcion) {
                case 1:
                    Entrenador nouEntrenador = nouEntrenador();
                    dadesEquips.get(Integer.parseInt(selectEquip[0])).setEntrenador(nouEntrenador);
                    missatgeEntrenadorCanviTrue();
                    break;
                case 2:
                    mercatFitxatgesEntrenadors(lliga, mercatFitxatges, seleccionMercatSoloEntrenadores(mercatFitxatges), dadesEquips);
                    missatgeEntrenadorCanviTrue();
                    break;

                default:
                    System.out.println("Opcion invalida");
            }

    }


    /**
     * Metode per registrar o fitxar un nou jugador
     *
     * @param mercatFitxatges Arraylist Profesional deportivo mercat fitxatges
     * @param equip           Object Class Equip  seleccionat
     */
    public static void registrarOFitxarNouJugadorEnNouEquip(ArrayList<ProfessionalDeportivo> mercatFitxatges,
                                                            Equip equip){
        menuOpcionsfitxar();

        int opcion = scanInt();
            switch (opcion) {
                case 1:
                    equip.assignarUnJugadorAlEquip();
                    break;
                case 2:
                    mercatFitxatgesJugadorsEnNouEquip(mercatFitxatges,seleccionMercatSoloJugadores(mercatFitxatges), equip);
                    break;

                default:
                    System.out.println("Opcion invalida");
            }

    }

    public static void tornarEntrenadorAlMercart(String[] equip, ArrayList<Equip> dadesEquips,
                                                 ArrayList<ProfessionalDeportivo> mercatFitxatges){
        Entrenador entrenador = dadesEquips.get(Integer.parseInt(equip[0])).getEntrenador();
        mercatFitxatges.add(entrenador);
    }


    /**
     * Metode per registrar o fitxar un nou Entrenador
     *
     * @param mercatFitxatges Arraylist Profesional deportivo mercat fitxatges
     * @param equip           Object Class Equip seleccionat
     */
    public static void registrarOFitxarNouEntrenadorNouEquip(ArrayList<ProfessionalDeportivo> mercatFitxatges, Equip equip){
        menuOpcionsfitxar();
        int opcion = scanInt();
            switch (opcion) {
                case 1:
                    equip.assignarEntrenadorAlEquip();
                    break;
                case 2:
                    mercatFitxatgesEntrenadorsEnNouEquip(mercatFitxatges, seleccionMercatSoloEntrenadores(mercatFitxatges), equip);
                    break;


                default:
                    System.out.println("Opcion invalida");
                    break;

        }
    }

    /**
     * Retorna el equip selecionat a partir de la posicio de aquest
     *
     * @param selectEquip Array amb la posicio i el nom del equip
     * @param dadesEquips Arraylist Equips amb les dades dels equips
     * @return Object classe Equip selecionat
     */
    public static Equip selecionarEquipDeIndexat(String[] selectEquip, ArrayList<Equip> dadesEquips){
        return dadesEquips.get(Integer.parseInt(selectEquip[0]));

    }


    /**
     * metode que registra un nou jugador i entrenador depenen de la opcio selecionada
     *
     * @param lliga       Object class Lliga
     * @param dadesEquips Arraylist de equips
     */
    public static void registrarNouJugadorIEntrenador(Lliga lliga, ArrayList<Equip> dadesEquips,
                                                      ArrayList<ProfessionalDeportivo> mercatFitxatges ){
        String[] selectEquip = indexacioISeleccioLlistaEquips(dadesEquips);
        Equip equip = selecionarEquipDeIndexat(selectEquip, dadesEquips);

        if (comprobarEquipEnLliga(lliga, selectEquip[1])) {
            mostrarMensajeEleccioFitxatge();
            int eleccio = scanInt();
                if (eleccio == 1) {
                    registrarOFitxarNouJugadorEnNouEquip(mercatFitxatges, equip);
                } else if (eleccio == 2) {
                    registrarOFitxarNouEntrenadorNouEquip(mercatFitxatges, equip);
                } else {
                    mostrarMissatgeOpcioInvalida();
                }

        } else {
            missatgeErrorRegistrarEntrenadorJugador();
        }
    }




    /**
     * Metode que borra el equip selecionat
     *
     * @param dadesEquips Arraylist de equips
     * @param index       equip selecionat
     */
    public static void borrarEquip(ArrayList<Equip> dadesEquips, int index) {
        dadesEquips.remove(index);
    }


    /**
     * Metode que comproba que el equip es troba en la lliga
     *
     * @param lliga Object class lliga
     * @param input equip a buscar
     * @return boolean si exiteix o no
     */
    public static boolean comprobarEquipEnLliga(Lliga lliga, String input){
        if (lliga.getRegistreEquips().containsKey(input)) {
            System.out.println("Equip seleccionat: " + input);
            return true;
        } else {
            System.out.println("L'equip no existeix en la lliga.");
            return false;
        }
    }

    /**
     * Metode que busca un jugador en un equip i canviar la poscicio de forma random del jugador
     *
     * @param lliga       Object class lliga
     * @param dadesEquips Arraylist class equip amb les dades equips
     */
    public static void canviarPosiciojugador(Lliga lliga, ArrayList<Equip> dadesEquips){
            Equip e = trobarEquipJugador(lliga, dadesEquips);
            Jugador j= mostrarDatosJugadorSeleccionado(e.getJugadors());
            j.canviNovaPosicio();

    }


    /**
     * Busca i seleciona un equip concret i aumenta el sou del entrenador.
     *
     * @param lliga       the lliga
     * @param dadesEquips the dades equips
     */
    public static void aumentarSouEntrenador(Lliga lliga, ArrayList<Equip> dadesEquips){
            Equip e = trobarEquipJugador(lliga, dadesEquips);
            Entrenador entr = e.getEntrenador();
            aumentarSouAnualEntrenador(entr);
            missatgeEquipNoTrobat();
    }


    /**
     * Missatge borrar equip true.
     */
    public static void missategBorrarEquipTrue(){
        System.out.println("L'equip ha estat donat de baixa.");
    }

    /**
     * Missatge borrar equip false.
     */
    public static void missategBorrarEquipFalse(){
        System.out.println("Error al donar de baixa l'equip.");
    }

    /**
     * Missatge registre nom del president
     */
    public static void missatgePresidenNom(){
        System.out.print("Introdueix el nom del president: ");
    }

    /**
     * Missatge registre cognom del president
     */
    public static void missatgePresidenCognom(){
        System.out.print("Introdueix el cognom del president: ");
    }

    /**
     * Missatge president canvi = true.
     */
    public static void missatgePresidenCanviTrue(){
        System.out.println("President modificat correctament.");
    }

    /**
     * Missatge president canvi = false.
     */
    public static void missatgePresidenCanviFalse(){
        System.out.println("Error al canviar el nom del President.");
    }

    /**
     * Mostrar menssatge eleccio tipus de fitxatge.
     */
    public static void mostrarMensajeEleccioFitxatge() {
        System.out.println("Quin tipus de Profesional vols fitxar?");
        System.out.println("1- Jugador");
        System.out.println("2- Entrenador");
        System.out.println("0- Sortir");
        System.out.print("Opcio: ");
    }

    /**
     * Mostrar missatge opcio invalida.
     */
    public static void mostrarMissatgeOpcioInvalida() {
        System.out.println("Opció no vàlida.");
    }

    /**
     * Missatge error registrar entrenador jugador.
     */
    public static void missatgeErrorRegistrarEntrenadorJugador() {
        System.out.println("Error al registrar l'Entrenador o Jugador.");
    }


    /////////////////////////////////////////////////////OPCIO3////////////////////////////////////////////////////


    /**
     * Metode que registra, registra i agrega un nou equip a la lliga
     *
     * @param lliga       Object Classe lliga
     * @param dadesEquips Arraylist dades equips
     */
    public static void registrarEquip(Lliga lliga, ArrayList<Equip> dadesEquips,
                                      ArrayList<ProfessionalDeportivo> mercatFitxatges) {
        System.out.println("Registrar nou equip:");

        Equip nouEquip = crearNouEquip();

        System.out.print("Vols registrar ara el Entrenador? (Si/No): ");
        if (opcioSiNo()) {
            agregarEntrenador(nouEquip,mercatFitxatges);
        } else {
            noAgregarEntrenador(nouEquip);
        }

        System.out.print("Vols registrar ara els Jugadors del equip? (Si/No): ");
        if (opcioSiNo()) {
            agregarJugadors(nouEquip, mercatFitxatges);
        } else {
            noAgregarJugadors(nouEquip);
        }


        dadesEquips.add(nouEquip);
        lliga.getRegistreEquips().put(nouEquip.getNomEquip(), 0);
        lliga.setNumEquips(lliga.getNumEquips()+1);

        System.out.println("Equip registrat correctament!");
    }

    /**
     * Metode que registra i crea un nou equip sense jugadors o entrenador
     *
     * @return the equip
     */
    public static Equip crearNouEquip() {
        missatgeRegistreNomEquip();
        String nomEquip = scanString();

        missatgeRegistreAnyEquip();
        int anyFundacio = scanInt();

        missatgeRegistreNomCiutatEquip();
        String ciutat = scanString();

        missatgeRegistreNomEstadiEquip();
        String estadi = scanString();

        missatgePresidenNom();
        String nomPresident = scanString();
        missatgePresidenCognom();
        String cognomPresident = scanString();
        System.out.print("El President disposa de capital? (Si/No): ");
        if (opcioSiNo()) {
            missatgeCapitalPresident();
            int capital = scanInt();
            President newPresident = new President(nomPresident, cognomPresident, capital);
            return new Equip(nomEquip, anyFundacio, ciutat, estadi, newPresident);
        } else {
            President newPresident = new President(nomPresident, cognomPresident);
            return new Equip(nomEquip, anyFundacio, ciutat, estadi, newPresident);
        }


    }

    /**
     * Missatge registre nom equip.
     */
    public static void missatgeRegistreNomEquip() {
        System.out.print("Introdueix el nom del equip: ");
    }

    /**
     * Missatge registre any equip.
     */
    public static void missatgeRegistreAnyEquip() {
        System.out.print("Introdueix l'any de fundació: ");
    }

    /**
     * Missatge registre nom ciutat equip.
     */
    public static void missatgeRegistreNomCiutatEquip() {
        System.out.print("Introdueix la ciutat: ");
    }

    /**
     * Missatge registre nom estadi equip.
     */
    public static void missatgeRegistreNomEstadiEquip() {
        System.out.print("Introdueix el nom de l'estadi: ");
    }


    /**
     * Missatge registre capital del president.
     */
    public static void missatgeCapitalPresident() {
        System.out.print("De quan capital disposa el President: ");
    }


    /**
     * Metode que crea i agrega el entrenador creat al equip
     *
     * @param equip Object Class equip selecionat
     */
    public static void agregarEntrenador(Equip equip, ArrayList<ProfessionalDeportivo> mercatFitxatges) {
        System.out.println("Afegint entrenador a l'equip...");
        registrarOFitxarNouEntrenadorNouEquip(mercatFitxatges, equip);
    }

    /**
     * Metode que deixa el entrenador com a null dintre l'equip
     *
     * @param equip Object Class equip = null
     */
    public static void noAgregarEntrenador(Equip equip) {
        System.out.println("No s'ha registrat un Entrenador al equip.");
        equip.setEntrenador(null);
    }



    /**
     * Metode que crea i agrega els Jugadors creats al equip
     *
     * @param equip Object Class equip selecionat
     */
    public static void agregarJugadors(Equip equip, ArrayList<ProfessionalDeportivo> mercatFitxatges) {
        System.out.println("Afegint jugadors a l'equip...");


        System.out.print("Quants jugadors vols afegir? ");
        int numJugadors = scanInt();


        for (int i = 0; i < numJugadors; i++) {
            System.out.println("Registre jugador " + (i + 1) + ":");
            registrarOFitxarNouJugadorEnNouEquip(mercatFitxatges, equip);
        }


        System.out.println("Jugadors afegits correctament!");
    }

    /**
     * Metode que deixa els jugadors com a null
     *
     * @param equip Object Class equip selecionat
     */
    public static void noAgregarJugadors(Equip equip) {
        System.out.println("No s'ha registrat cap Jugador al equip.");
        equip.setJugadors(null);
    }


    /////////////////////////////////////////////////////OPCIO4////////////////////////////////////////////////////

    /**
     * Metode que registra i dona de alta un entrenador o un jugador i els agrega al mercat de fitxatges
     *
     * @param mercat    Arraylist Class ProfesionalDeportivo amb el mercat de fitxatges
     */
    public static void eleccioDonarAlta (ArrayList<ProfessionalDeportivo> mercat) {
        mostrarMensajeEleccioFitxatge();
        introduirInput();
        int opcio = scanInt();
        if (opcio == 1) {
            Jugador j = nouJugador();
            if (mercat.add(j)) {
                mostrarMissatgeJugadorAltaTrue();
            } else {
                mostrarMissatgeJugadorAltaFalse();
            }
        } else if (opcio == 2) {
            Entrenador e = nouEntrenador();
            if (mercat.add(e)) {
                mostrarMissatgeEntrenadorAltaTrue();
            } else {
                mostrarMissatgeEntrenadorAltaFalse();
            }
        }

    }


/////////////////////////////////////////////////////OPCIO5////////////////////////////////////////////////////
    /**
     * Metode que seleciona un equip en la lliga i imprimeix les dades
     *
     * @param lliga       Object Class Lliga
     * @param dadesEquips Arraylist de Equips amb les dades de tots els equips
     */
    public static void mostrarDadesEquips(Lliga lliga, ArrayList<Equip> dadesEquips) {
        String[] selectEquip = indexacioISeleccioLlistaEquips(dadesEquips);
            if (comprobarEquipEnLliga(lliga, selectEquip[1])) {
                System.out.println(dadesEquips.get(Integer.parseInt(selectEquip[0])));
            } else {
                missatgeErrorRegistrarEntrenadorJugador();
            }
    }



    /////////////////////////////////////////////////////OPCIO6////////////////////////////////////////////////////


    /**
     * Imprimeix les dades de un jugador en especific
     *
     * @param lliga       Object Class Lliga
     * @param dadesEquips Arraylist de Equips amb les dades de tots els equips
     */
    public static void mostrarDadesJugador(Lliga lliga, ArrayList<Equip> dadesEquips) {
        Equip e = trobarEquipJugador(lliga, dadesEquips);


        if (e.getJugadors().isEmpty()) {
            System.out.println("Aquest equip no té jugadors.");
        } else {
            System.out.println("Llista de jugadors de " + e.getNomEquip() + ":");

            for (int i = 0; i < e.getJugadors().size(); i++) {
                Jugador j = e.getJugadors().get(i);
                System.out.println((i + 1) + ". " + j.getNom() + " " + j.getCognom());
            }

            Scanner scanner = new Scanner(System.in);
            System.out.print("Selecciona un jugador: ");
            int seleccion = scanner.nextInt();


            if (seleccion >= 1 && seleccion <= e.getJugadors().size()) {
                Jugador j = e.getJugadors().get(seleccion - 1);
                j.imprimirDades();
            } else {
                System.out.println("Selecció no vàlida.");
            }
        }
    }


    /**
     * metode que busca i seleciona un equip especific
     *
     * @param lliga       Object Class Lliga
     * @param dadesEquips Arraylist de Equips amb les dades de tots els equips
     * @return Object Class Equip selecionat
     */
    public static Equip trobarEquipJugador(Lliga lliga, ArrayList<Equip> dadesEquips) {
        String[] selectEquip = indexacioISeleccioLlistaEquips(dadesEquips);
        if (comprobarEquipEnLliga(lliga, selectEquip[1])) {
            int indexEquip = Integer.parseInt(selectEquip[0]);
            if (indexEquip >= 0 && indexEquip < dadesEquips.size()) {
                return dadesEquips.get(indexEquip);
            } else {
                missatgeEquipNoTrobat();
                return null;
            }

        } else {
            missatgeErrorRegistrarEntrenadorJugador();
            return null;
        }
    }

    /**
     * Buscar i retorna un jugador per numero de dorsal
     *
     * @param dorsal   int numero de dorsal
     * @param jugadors Arraylist Jugadors
     * @return Object classe jugador selecionat
     */
    public static Jugador buscarJugadorPerDorsal(int dorsal, ArrayList<Jugador> jugadors) {
        for (Jugador jugador : jugadors) {
            if (jugador.getNumDorsal() == dorsal) {
                return jugador;
            }
        }
        return null;
    }

    /**
     * Missatge equip no trobat.
     */
    public static void missatgeEquipNoTrobat(){
        System.out.println("L'índex de l'equip no és vàlid.");
    }

    /////////////////////////////////////////////////////OPCIO7////////////////////////////////////////////////////

    /**
     * Visualitza les opcions del sub menu gestionar lliga.
     */
    public static void mostrarMenuGestionarLliga() {
        System.out.println();
        System.out.println("=== Menu de Gestió de la Liga ===");
        System.out.println("1. Avançar Jornades");
        System.out.println("2. Finalitzar Lliga");
        System.out.println("3. Disputar una nova Lliga");
        System.out.println("0. Salir");
        System.out.print("Selecciona una opcio: ");
    }


    /**
     * Genera un int Random del 1 al 3
     *
     * @return int
     */
    public static int simularPartido() {
        Random random = new Random();
        return random.nextInt(3);
    }


    /**
     * Actualitzar punts de un equip a la classificacio de la lliga
     *
     * @param lliga    Object Class lliga
     * @param nomEquip String nom del equip
     * @param punts    int nova puntuacio
     */
    public static void actualitzarPuntsEquip(Lliga lliga, String nomEquip, int punts) {
        if (lliga.getRegistreEquips().containsKey(nomEquip)) {
            int puntsActuals = lliga.getRegistreEquips().get(nomEquip);
            lliga.getRegistreEquips().put(nomEquip, puntsActuals + punts);
        } else {
            System.out.println("Error: L'equip " + nomEquip + " no existeix a la lliga.");
        }
    }


    /**
     * Simula els partits de una jornada esportiva i actualitza els punts depenen
     * del resultat que obtenen els equips
     *
     * @param lliga      Object classe Lliga
     * @param numJornada Int numero de la jornada
     */
    public static void simularJornada(Lliga lliga, int numJornada) {
        List<String> equipos = new ArrayList<>(lliga.getRegistreEquips().keySet());

        System.out.println("Jornada " + (numJornada + 1) + ":");
        for (int i = 0; i < equipos.size() / 2; i++) {
            String equipLocal = equipos.get(i);
            String equipVisitant = equipos.get(equipos.size() - 1 - i);

            int resultat = simularPartido();

            System.out.println(equipLocal + " vs " + equipVisitant + ": ");

            if (resultat == 0) {
                actualitzarPuntsEquip(lliga, equipLocal, 3);
                System.out.println("Victòria de " + equipLocal);
            } else if (resultat == 1) {
                actualitzarPuntsEquip(lliga, equipLocal, 1);
                actualitzarPuntsEquip(lliga, equipVisitant, 1);
                System.out.println("Empat");
            } else {
                actualitzarPuntsEquip(lliga, equipVisitant, 3);
                System.out.println("Victòria de " + equipVisitant);
            }
        }
    }


    /**
     * Metode que simula diverses jornades
     *
     * @param lliga       Object classe Lliga
     * @param numJornades Int numero de jornades
     */
    public static void simularJornadas(Lliga lliga, int numJornades) {

        for (int i = 0; i < numJornades; i++) {
            simularJornada(lliga, lliga.getJornadasSimuladas());
            lliga.setJornadasSimuladas(lliga.getJornadasSimuladas() + 1);
            mostrarClasificacioLliga(lliga);
        }
    }


    /**
     * Metode que finalitzar i imprimeix els resultats de una lliga
     *
     * @param lliga Object Classe Lliga
     */
    public static void finalizarLliga(Lliga lliga) {
        if (lliga.getJornadasSimuladas() < lliga.getJornades()) {
            System.out.println("No es pot finalitzar la lliga. Encara queden " +lliga.getJornades()+" jornades per simular.");
            System.out.println("Vols finalitzar totes les jornades restants ara?");
            System.out.print("Opcio (Si/No): ");
            if (opcioSiNo()){
                simularJornadas(lliga,lliga.getJornadasSimuladas());
            }
        }

        String equipGuanyador = calcularEquipGuanyador(lliga);
        System.out.println("LA LLIGA " + lliga.getCodiLliga() + " HA FINALITZAT!!!");
        System.out.println("EL GUANYADOR ÉS: " + equipGuanyador);
    }


    /**
     * metode que retorna el nom del equip guanyador
     *
     * @param lliga Object Classe lliga
     * @return string equip guanyador
     */
    public static String calcularEquipGuanyador(Lliga lliga) {
        String equipGuanyador = "";
        int puntsGuanyador = -1;
        for (Map.Entry<String, Integer> equip : lliga.getRegistreEquips().entrySet()) {
            if (equip.getValue() > puntsGuanyador) {
                puntsGuanyador = equip.getValue();
                equipGuanyador = equip.getKey();
            }
        }
        return equipGuanyador;
    }


    /**
     * Imprimerix la classificaio actual de la lliga
     *
     * @param lliga Object class Lliga
     */
    public static void mostrarClasificacioLliga(Lliga lliga) {
        System.out.println("Classificació actual:");
        mostraClasificacioLiga(lliga);
    }


    /**
     * funcions sub menu lliga
     *
     * @param lliga object class lliga
     */
    public static void gestionarLliga(Lliga lliga, ArrayList<Equip> dadesEquips,
                                      ArrayList<ProfessionalDeportivo> mercatFitxatges) {
        int opcion;
        do {
            mostrarMenuGestionarLliga();
            opcion = scanInt();
            switch (opcion) {

                case 1:
                    if (dadesEquips.size() > 1) {
                        if (totsElsEquipsTenenJugadors(dadesEquips)) {
                            System.out.println("Quantes jornades vols avançar? ");
                            System.out.print("Jornades: ");
                            int jornades = scanInt();
                            if ((jornades + lliga.getJornadasSimuladas()) <= lliga.getJornades()) {
                                simularJornadas(lliga, jornades);
                            } else {
                                System.out.println("El numero de Jornades introduit supera el \n" +
                                        "numero de jornades de la lliga disponibles");
                            }
                        } else {
                            System.out.println("Tots els equips han de tenir com a minim 1 Jugador \n" +
                                    "per poder simular les jornades");
                        }
                    } else {
                        misatgeNumEquipsInsuficient();
                    }
                    break;
                case 2:
                    if (dadesEquips.size() > 2) {
                        finalizarLliga(lliga);
                    } else {
                        misatgeNumEquipsInsuficient();
                    }
                    break;
                case 3:
                    disputarNuevaLliga(lliga, dadesEquips, mercatFitxatges);
                    break;
                case 0:
                    System.out.println("Tornant al menú principal...");
                    break;
                default:
                    System.out.println("Opció no vàlida.");
            }
        } while (opcion != 0);
    }

    public static void misatgeNumEquipsInsuficient(){
        System.out.println("Tens que tenir com a minim 2 equips registarts \n " +
                "per poder avançar la lliga");
    }

    /**
     * Metode que genera una nova lliga
     *
     * @param lliga object Class lliga
     */
    public static void disputarNuevaLliga(Lliga lliga,ArrayList<Equip> dadesEquips,
                                          ArrayList<ProfessionalDeportivo> mercatFitxatges) {
        mantenirEquipsLliga(lliga,dadesEquips,mercatFitxatges);



        System.out.println("Nova lliga creada correctament.");
    }

    public static void mantenirEquipsLliga(Lliga lliga, ArrayList<Equip> dadesEquips,
                                           ArrayList<ProfessionalDeportivo> mercatFitxatges){
        System.out.println("Vols mantenir els Equips actuals per la nova lliga?");
        System.out.print("Opcio (Si/No): ");
        if (opcioSiNo()){
            Lliga novaLliga = crearNovaLliga();
            lliga.setCodiLliga(novaLliga.getCodiLliga());
            lliga.setJornades(novaLliga.getJornades());
            lliga.setJornadasSimuladas(0);
            lliga.setRegistreEquips(lliga.getRegistreEquips());
            lliga.reiniciarPuntsEquips();
        } else {
            tornatTotsElsPDAlMercat(dadesEquips,mercatFitxatges);
            Lliga novaLliga = crearNovaLliga();
            lliga.setCodiLliga(novaLliga.getCodiLliga());
            lliga.setJornades(novaLliga.getJornades());
            lliga.setJornadasSimuladas(0);
        }
        guardarPartida(lliga,dadesEquips,mercatFitxatges);
    }


    public static void tornatTotsElsPDAlMercat(ArrayList<Equip> dadesEquips,
                                                        ArrayList<ProfessionalDeportivo> mercatFitxatges){

        tornarTotsElsEntrenadorAlMercart(dadesEquips,mercatFitxatges);
        tornatTotsElsJugadorsAlMercat(dadesEquips,mercatFitxatges);

    }


    public static void tornarTotsElsEntrenadorAlMercart(ArrayList<Equip> dadesEquips,
                                                 ArrayList<ProfessionalDeportivo> mercatFitxatges){
        for (int i = 0; i < dadesEquips.size(); i++) {
            Entrenador entrenador = dadesEquips.get(i).getEntrenador();
            if (!(entrenador == null)) {
                mercatFitxatges.add(entrenador);
            }
        }

    }


    public static void tornatTotsElsJugadorsAlMercat(ArrayList<Equip> dadesEquips,
                                                        ArrayList<ProfessionalDeportivo> mercatFitxatges){
        for (int i = 0; i < dadesEquips.size(); i++) {
            for (int j = 0; j < dadesEquips.get(i).getJugadors().size() ; j++) {
                Jugador jugador = dadesEquips.get(i).getJugadors().get(j);
                if (!(jugador == null)) {
                    mercatFitxatges.add(jugador);
                }
            }
        }
    }



    public static boolean totsElsEquipsTenenJugadors(ArrayList<Equip> dadesEquips) {
        boolean j = true;
        for (Equip equip : dadesEquips) {
            if (equip.getJugadors() == null || equip.getJugadors().isEmpty()) {
                System.out.println(equip.getNomEquip() + " no té jugadors");
                j = false;
            }
        }
        return j;
    }




    /////////////////////////////////////////////////////OPCIO8////////////////////////////////////////////////////

    /**
     * Visualitzar opcions mercat de fitxatges.
     */
    public static void visualitzarElecioMercatfitxatges(){
        System.out.println("Benvingut al Mercat de fitxatges");
        System.out.println("1- Veure mercat de Jugadors");
        System.out.println("2- Veure mercat de Entrenador");
        System.out.println("3- Entrenar mercat de fitxatges");
        System.out.println("0- Sortir");
        System.out.print("Selecciona una opcio: ");
    }

    /**
     * Visualitzar opcions disponibles mercat de fitxatges.
     */
    public static void visualitzarOpcionsMercatFitxatges(){
        System.out.println("Opcions disponibles");
        System.out.println("1- Fitxar");
        System.out.println("2- Entrenar");
        System.out.println("0- Sortir");
        System.out.print("Selecciona una opcio: ");
    }


    /**
     * Fucions gestio mercat de fitxatges
     *
     * @param lliga             Object Class lliga
     * @param dadesEquips       ArrayList Class Equip
     * @param mercatFitxatges    ArrayList Class ProfesionalDeportivo
     */
    public static void fucionsElecioMercatfitxatges(Lliga lliga, ArrayList<Equip> dadesEquips,
                                                    ArrayList<ProfessionalDeportivo> mercatFitxatges){
        int opcio;
        do {
            visualitzarElecioMercatfitxatges();
            opcio = scanInt();

            if (opcio == 1) {
                mercatFitxatgesJugadors(lliga, mercatFitxatges, seleccionMercatSoloJugadores(mercatFitxatges), dadesEquips);
            } else if (opcio == 2) {
                mercatFitxatgesEntrenadors(lliga, mercatFitxatges, seleccionMercatSoloEntrenadores(mercatFitxatges), dadesEquips);
            } else if (opcio == 3) {
                entrenarMercatDeFitxatges(mercatFitxatges);
            }
        } while (opcio != 0);
    }


    public static void entrenarMercatDeFitxatges(ArrayList<ProfessionalDeportivo> mercatFitxatges){
        for (int i = 0; i < mercatFitxatges.size(); i++) {
            mercatFitxatges.get(i).entrenamentMercatFitxatges();
        }
        missatgeEntrenament();
    }

    public static void missatgeEntrenament(){
        System.out.println("Entrenament finalitzat");
    }

    /** por aqui
     * Comporba i mostrar el mercat de jugadors indexat
     *
     * @param mercatJugadors ArrayList Jugadors disponibles
     */
    public static void mostrarMercatJugadorsIndexat(ArrayList<Jugador> mercatJugadors) {
        if (mercatJugadors.isEmpty()) {
            mostrarMissatgeMercatBuit();
        } else {
            Collections.sort(mercatJugadors);
            mostrarLlistaJugadorsIndexada(mercatJugadors);
        }
    }

    /**
     * Comporba i mostrar el mercat de entrenadors indexat
     *
     * @param mercatEntrenadors ArrayList Entrenadors disponibles
     */
    public static void mostrarMercatEntrenadorsIndexat(ArrayList<Entrenador> mercatEntrenadors) {
        if (mercatEntrenadors.isEmpty()) {
            mostrarMissatgeMercatBuit();
        } else {
            Collections.sort(mercatEntrenadors);
            mostrarLlistaEntrenadorsIndexada(mercatEntrenadors);
        }
    }


    /**
     * Mostrar missatge mercat buit.
     */
    public static void mostrarMissatgeMercatBuit() {
        System.out.println("El mercat està buit.");
    }

    /**
     * Mostrar i indexat el llistat de jugadors
     *
     * @param mercatJugadors ArrayList Jugadors indexat
     */
    public static void mostrarLlistaJugadorsIndexada(ArrayList<Jugador> mercatJugadors) {
        System.out.println("Jugadors disponibles al mercat:");
        for (int i = 0; i < mercatJugadors.size(); i++) {
            mostrarJugadorIndexat(mercatJugadors.get(i), i + 1);
        }
    }

    /**
     * Mostrar i indexat el llistat de entrenadors
     *
     * @param mercatEntrenadors ArrayList entrenadors indexat
     */
    public static void mostrarLlistaEntrenadorsIndexada(ArrayList<Entrenador> mercatEntrenadors) {
        System.out.println("Entrenadors disponibles al mercat:");
        for (int i = 0; i < mercatEntrenadors.size(); i++) {
            mostrarEntrenadorIndexat(mercatEntrenadors.get(i), i + 1);
        }
    }

    /**
     * Imprimeix les dades del jugador selecionat
     *
     * @param jugador Object Class Jugador
     * @param index   int index selecionat
     */
    public static void mostrarJugadorIndexat(Jugador jugador, int index) {
        System.out.println(index + ". " + jugador.getNom() + " " + jugador.getCognom()  + " (Dorsal: " + jugador.getNumDorsal() + ") Preu: " + jugador.getPreu() );
    }

    /**
     * Imprimeix les dades del entrenador selecionat
     *
     * @param entrenador Object Class Entrenador
     * @param index      int index selecionat
     */
    public static void mostrarEntrenadorIndexat(Entrenador entrenador, int index) {
        System.out.println(index + ". " + entrenador.getNom() + " " + entrenador.getCognom() + " Preu: " + entrenador.getSouAnual());
    }


    /**
     * Funcions fitxatge dels jugadors del mercat de fitxatges
     *
     * @param lliga          Object class Lliga
     * @param mercatJugadors Arraylist Jugadors mercat de fitxatges
     * @param dadesEquips    Arraylist dades Equips
     * @param pd             Arraylist ProfesionalDeportivo mercat de fitxatges
     */
    public static void mercatFitxatgesJugadors(Lliga lliga, ArrayList<ProfessionalDeportivo> pd, ArrayList<Jugador> mercatJugadors, ArrayList<Equip> dadesEquips){
        int opcio;
        do {
            mostrarMercatJugadorsIndexat(mercatJugadors);
            visualitzarOpcionsMercatFitxatges();
            opcio = scanInt();
            if (opcio == 1) {
                Jugador fitxat = mostrarDatosJugadorSeleccionado(mercatJugadors);
                missatgeFitxar();
                if(opcioSiNo()){
                    String[] selectEquip = indexacioISeleccioLlistaEquips(dadesEquips);
                    if (comprobarEquipEnLliga(lliga, selectEquip[1])) {
                        mercatJugadors.remove(fitxat);
                        borrarDeMercatFitxatges(fitxat,pd);
                        dadesEquips.get(Integer.parseInt(selectEquip[0])).addJugador(fitxat);
                    } else {
                        missategBorrarEquipFalse();
                    }

                }
            } else if (opcio == 2) {
                Jugador fitxat = mostrarDatosJugadorSeleccionado(mercatJugadors);
                fitxat.entrenamentMercatFitxatges();
                mostrarInfoJugador(fitxat);
            }
        }while (opcio != 0);
    }


    /**
     * Metode que agafa un jugador o entrenador i el borra del mercat de fitxtges
     * utilitzan una coincidencia de nom i cognom
     *
     * @param select  Object Classe PrfesionalDeportivo fitxat
     * @param pd      Arraylist Mercat de fitxatges
     */
    public static void borrarDeMercatFitxatges(ProfessionalDeportivo select, ArrayList<ProfessionalDeportivo> pd){
        for (int i = 0; i < pd.size(); i++) {
            if (select.equals(pd.get(i))){
                pd.remove(i);
            }
        }
    }


    /**
     * Funcions fitxatge dels entrenadors del mercat de fitxatges
     *
     * @param lliga             Object class Lliga
     * @param mercatEntrenadors Arraylist Entrenadors mercat de fitxatges
     * @param dadesEquips       Arraylist dades Equips
     * @param pd                Arraylist ProfesionalDeportivo mercat de fitxatges
     */
    public static void mercatFitxatgesEntrenadors(Lliga lliga,ArrayList<ProfessionalDeportivo> pd, ArrayList<Entrenador> mercatEntrenadors, ArrayList<Equip> dadesEquips){
        int opcio;
        do {
            mostrarMercatEntrenadorsIndexat(mercatEntrenadors);
            visualitzarOpcionsMercatFitxatges();
            opcio = scanInt();
            if (opcio == 1) {
                Entrenador fitxat = mostrarDatosEntrenadorSeleccionado(mercatEntrenadors);
                missatgeFitxar();
                if(opcioSiNo()){
                    String[] selectEquip = indexacioISeleccioLlistaEquips(dadesEquips);
                    if (comprobarEquipEnLliga(lliga, selectEquip[1])) {
                        mercatEntrenadors.remove(fitxat);
                        borrarDeMercatFitxatges(fitxat,pd);
                        dadesEquips.get(Integer.parseInt(selectEquip[0])).setEntrenador(fitxat);
                    } else {
                        missategBorrarEquipFalse();
                    }
                }
            }  else if (opcio == 2) {
            Entrenador fitxat = mostrarDatosEntrenadorSeleccionado(mercatEntrenadors);
            fitxat.entrenamentMercatFitxatges();
            mostrarInfoEntrenador(fitxat);
        }

        }while (opcio != 0);
    }

    /**
     * Metode que mostra, fitxa un jugador i borra el jugador del mercat de fitxatges
     *
     * @param pd                Arraylist ProfesionalDeportivo mercat de fitxatges
     * @param mercatJugadors    Arraylist mercat jugadors
     * @param equip             Equip selecionat
     */
    public static void mercatFitxatgesJugadorsEnNouEquip(ArrayList<ProfessionalDeportivo> pd, ArrayList<Jugador> mercatJugadors, Equip equip){
        int opcio;
            mostrarMercatJugadorsIndexat(mercatJugadors);
            visualitzarOpcionsMercatFitxatges();
            opcio = scanInt();
            if (opcio == 1) {
                Jugador fitxat = mostrarDatosJugadorSeleccionado(mercatJugadors);
                missatgeFitxar();
                if(opcioSiNo()){
                    mercatJugadors.remove(fitxat);
                    borrarDeMercatFitxatges(fitxat,pd);
                    equip.addJugador(fitxat);
                    missatgeFitxarNouJugadorTrue();
                } else {
                    missategBorrarEquipFalse();
                }

            } else if (opcio == 2) {
        Jugador fitxat = mostrarDatosJugadorSeleccionado(mercatJugadors);
        fitxat.entrenamentMercatFitxatges();
        mostrarInfoJugador(fitxat);
    }

    }


    /**
     * Metode que mostra, fitxa un entrenador i borra el entrenador del mercat de fitxatges
     *
     * @param pd                Arraylist ProfesionalDeportivo mercat de fitxatges
     * @param mercatEntrenadors Arraylist mercat entrenadors
     * @param equip             equip selecionat
     */
    public static void mercatFitxatgesEntrenadorsEnNouEquip(ArrayList<ProfessionalDeportivo> pd, ArrayList<Entrenador> mercatEntrenadors, Equip equip){
        int opcio;
        do {
            mostrarMercatEntrenadorsIndexat(mercatEntrenadors);
            visualitzarOpcionsMercatFitxatges();
            opcio = scanInt();
            if (opcio == 1) {
                Entrenador fitxat = mostrarDatosEntrenadorSeleccionado(mercatEntrenadors);
                missatgeFitxar();
                if(opcioSiNo()){
                        mercatEntrenadors.remove(fitxat);
                        borrarDeMercatFitxatges(fitxat,pd);
                        equip.setEntrenador(fitxat);
                        missatgeEntrenadorCanviTrue();
                    } else {
                        missategBorrarEquipFalse();
                    }

            }  else if (opcio == 2) {
                Entrenador fitxat = mostrarDatosEntrenadorSeleccionado(mercatEntrenadors);
                fitxat.entrenamentMercatFitxatges();
                mostrarInfoEntrenador(fitxat);
            }

        }while (opcio !=0);
    }



    /**
     * Missatge fitxar.
     */
    public static void missatgeFitxar(){
    System.out.print("Fitxar (Si/No): ");
    }


    /**
     * Demana el index del jugador
     *
     * @param mercatJugadors Arraylist mercat jugadors indexat
     * @return int index jugador
     */
    public static int pedirIndiceJugador(ArrayList<Jugador> mercatJugadors) {
        mostrarMensajePedirIndice();
        return obtenerIndiceValidoJ(mercatJugadors);
    }

    /**
     * Demana el index del entrenador
     *
     * @param mercatEntrenadors Arraylist mercat entrenadors indexat
     * @return int index entrenador
     */
    public static int pedirIndiceEntrenador(ArrayList<Entrenador> mercatEntrenadors) {
        mostrarMensajePedirIndice();
        return obtenerIndiceValidoE(mercatEntrenadors);
    }

    /**
     * Mostrar mensaje del index.
     */
    public static void mostrarMensajePedirIndice() {
        System.out.print("Introdueix el numero del profesional: ");
    }

    /**
     * Valida i retorna el index en la arraylist del mercat de jugadors
     *
     * @param mercatJugadors Arraylist mercat jugadors
     * @return int index a validar
     */
    public static int obtenerIndiceValidoJ(ArrayList<Jugador> mercatJugadors) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            if (scanner.hasNextInt()) {
                int indice = scanner.nextInt() - 1;
                if (esIndiceValidoJ(indice, mercatJugadors)) {
                    return indice;
                } else {
                    mostrarMensajeIndiceFueraDeRangoJ(mercatJugadors);
                }
            } else {
                mostrarMensajeEntradaInvalida();
                scanner.next();
            }
        }
    }

    /**
     * Demana, valida i retorna el index en la arraylist del mercat de entrenadors
     *
     * @param mercatEntrenadors Arraylist mercat entrenadors
     * @return int index a validar
     */
    public static int obtenerIndiceValidoE(ArrayList<Entrenador> mercatEntrenadors) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            if (scanner.hasNextInt()) {
                int indice = scanner.nextInt() - 1;
                if (esIndiceValidoE(indice, mercatEntrenadors)) {
                    return indice;
                } else {
                    mostrarMensajeIndiceFueraDeRangoE(mercatEntrenadors);
                }
            } else {
                mostrarMensajeEntradaInvalida();
                scanner.next();
            }
        }
    }

    /**
     * Comprova que el index sigui valid en el mercat de jugadors
     *
     * @param indice         int a validar
     * @param mercatJugadors Arraylist mercat jugadors
     * @return boolean si es valid
     */
    public static boolean esIndiceValidoJ(int indice, ArrayList<Jugador> mercatJugadors) {
        return indice >= 0 && indice < mercatJugadors.size();
    }

    /**
     * Comprova que el index sigui valid en el mercat de entrenadors
     *
     * @param indice            int a validar
     * @param mercatEntrenadors Arraylist mercat entrenadors
     * @return boolean si es valid
     */
    public static boolean esIndiceValidoE(int indice, ArrayList<Entrenador> mercatEntrenadors) {
        return indice >= 0 && indice < mercatEntrenadors.size();
    }

    /**
     * Mostrar mensaje si el index esta fora del numero de registres en el mercat de jugadors
     *
     * @param mercatJugadors arraylist mercat jugadors
     */
    public static void mostrarMensajeIndiceFueraDeRangoJ(ArrayList<Jugador> mercatJugadors) {
        System.out.println("Índice fuera de rango. Introduce un número entre 1 y " + mercatJugadors.size() + ".");
    }

    /**
     * Mostrar mensaje si el index esta fora del numero de registres en el mercat de entrenadors
     *
     * @param mercatEntrenadors arraylist mercat entrenadors
     */
    public static void mostrarMensajeIndiceFueraDeRangoE(ArrayList<Entrenador> mercatEntrenadors) {
        System.out.println("Índice fuera de rango. Introduce un número entre 1 y " + mercatEntrenadors.size() + ".");
    }

    /**
     * Mostrar mensaje entrada invalida.
     */
    public static void mostrarMensajeEntradaInvalida() {
        System.out.println("Entrada inválida. Introduce un número.");
    }

    /**
     * Metode que retorna e imprimeix les dades de un jugador en concret del mercat de jugadors
     *
     * @param mercatJugadors Arraylist mercat jugadors
     * @return Jugador a fitxar
     */
    public static Jugador mostrarDatosJugadorSeleccionado(ArrayList<Jugador> mercatJugadors) {
        if (esMercadoVacioJ(mercatJugadors)) {
            mostrarMensajeMercadoVacioJ();
            return null;
        }
        mostrarMercatJugadorsIndexat(mercatJugadors);
        Jugador fitxat = obtenerJugadorSeleccionado(mercatJugadors);
        mostrarInfoJugador(fitxat);

        return fitxat;
    }

    /**
     * Metode que retorna e imprimeix les dades de un entrenador en concret del mercat de entrenadors
     *
     * @param mercatEntrenadors Arraylist mercat entrenadors
     * @return Entrenador a fitxar
     */
    public static Entrenador mostrarDatosEntrenadorSeleccionado(ArrayList<Entrenador> mercatEntrenadors) {
        if (esMercadoVacioE(mercatEntrenadors)) {
            mostrarMensajeMercadoVacioE();
            return null;
        }
        mostrarMercatEntrenadorsIndexat(mercatEntrenadors);
        Entrenador fitxat = obtenerEntrenadorSeleccionado(mercatEntrenadors);
        mostrarInfoEntrenador(fitxat);

        return fitxat;
    }


    /**
     * Comproba si el mercat de jugadors esta vuit
     *
     * @param mercatJugadors arraylist mercat jugadors
     * @return boolean
     */
    public static boolean esMercadoVacioJ(ArrayList<Jugador> mercatJugadors) {
        return mercatJugadors.isEmpty();
    }

    /**
     *  Comproba si el mercat de entrenadors esta vuit
     *
     * @param mercatEntrenadors arraylist mercat entrenadors
     * @return the boolean
     */
    public static boolean esMercadoVacioE(ArrayList<Entrenador> mercatEntrenadors) {
        return mercatEntrenadors.isEmpty();
    }


    /**
     * Missatge mercat de jugadors buit
     */
    public static void mostrarMensajeMercadoVacioJ() {
        System.out.println("El mercat de jugadors está buit.");
    }

    /**
     * Missatge mercat de entrenadors buit
     */
    public static void mostrarMensajeMercadoVacioE() {
        System.out.println("El mercat de entrenadors está buit.");
    }

    /**
     * Retorna el index del jugador seleccionat dintre el mercat de jugadors
     *
     * @param mercatJugadors arraylist mercat jugadors
     * @return Object Class Jugador seleccionat
     */
    public static Jugador obtenerJugadorSeleccionado(ArrayList<Jugador> mercatJugadors) {
        int indice = pedirIndiceJugador(mercatJugadors);
        return mercatJugadors.get(indice);
    }

    /**
     * Retorna el index del entrenador seleccionat dintre el mercat de entrenadors
     *
     * @param mercatEntrenadors arraylist mercat entrenadors
     * @return Object Class Entrenador seleccionat
     */
    public static Entrenador obtenerEntrenadorSeleccionado(ArrayList<Entrenador> mercatEntrenadors) {
        int indice = pedirIndiceEntrenador(mercatEntrenadors);
        return mercatEntrenadors.get(indice);
    }

    /**
     * Imprimeix les dades del jugador
     *
     * @param j Jugador seleccionat
     */
    public static void mostrarInfoJugador(Jugador j) {
        System.out.println("--------------------------------------------------");
        System.out.println("Nom: " + j.getNom());
        System.out.println("Cognom: " + j.getCognom());
        System.out.println("Data de neixament: " + j.getDataNeixament());
        System.out.println("Numero de Dorsal: " + j.getNumDorsal());
        System.out.println("Preu del Jugador: " + j.getPreu());
        System.out.println("Posicio: " + j.getPosicio());
        System.out.println("Nivell de motivacio: " + j.getNivellMotivacio());
        System.out.println("Rendiment: " + j.getRendiment());
        System.out.println("--------------------------------------------------");
    }

    /**
     * Imprimeix les dades del entrenador
     *
     * @param e entrenador selecionat
     */
    public static void mostrarInfoEntrenador(Entrenador e) {
        System.out.println("--------------------------------------------------");
        System.out.println("Nom: " + e.getNom());
        System.out.println("Cognom: " + e.getCognom());
        System.out.println("Data de neixament: " + e.getDataNeixament());
        System.out.println("Preu del Entrenador: " + e.getSouAnual());
        System.out.println("Nivell de motivacio: " + e.getNivellMotivacio());
        System.out.println("Numero de torneigos guanyats: " + e.getNumTorneigsGuanyats());
        System.out.println("Es selecionador Nacional: " + e.isSelecionadorNacional());
        System.out.println("--------------------------------------------------");
    }






    /////////////////////////////////////////////////////OPCIO9////////////////////////////////////////////////////

    /**
     * Metode de tranferencia de un jugador d un equip a un altre equip
     *
     @param dadesEquips Arraylist Equips amb les dades de tots els equips
     */
    public static void transferirJugador(ArrayList<Equip> dadesEquips) {
        System.out.println("\n--- Equips disponibles ---");
        for (int i = 0; i < dadesEquips.size(); i++) {
            Equip equip = dadesEquips.get(i);
            System.out.println((i + 1) + ". " + equip.getNomEquip());
        }

        System.out.print("\nSelecciona el número de l'equip d'origen (l'equip que té el jugador): ");
        int seleccionEquipOrigen = scanInt();

        if (seleccionEquipOrigen < 1 || seleccionEquipOrigen > dadesEquips.size()) {
            System.out.println("¡Selecció no vàlida!");
            return;
        }

        Equip equipOrigen = dadesEquips.get(seleccionEquipOrigen - 1);

        System.out.println("\n--- Jugadors de l'equip " + equipOrigen.getNomEquip() + " ---");
        ArrayList<Jugador> jugadorsEquipOrigen = equipOrigen.getJugadors();
        for (int i = 0; i < jugadorsEquipOrigen.size(); i++) {
            Jugador jugador = jugadorsEquipOrigen.get(i);
            System.out.println((i + 1) + ". " + jugador.getNom() + " " + jugador.getCognom() +
                    " (" + jugador.getPosicio() + ") - Rendiment: " + jugador.getRendiment());
        }

        System.out.print("\nSelecciona el número del jugador que vols transferir: ");
        int seleccionJugador = scanInt();

        if (seleccionJugador < 1 || seleccionJugador > jugadorsEquipOrigen.size()) {
            System.out.println("¡Selecció no vàlida!");
            return;
        }

        Jugador jugadorSeleccionado = jugadorsEquipOrigen.get(seleccionJugador - 1);

        System.out.println("\n--- Equips disponibles per transferir ---");
        for (int i = 0; i < dadesEquips.size(); i++) {
            if (i != seleccionEquipOrigen - 1) {
                Equip equip = dadesEquips.get(i);
                System.out.println((i + 1) + ". " + equip.getNomEquip());
            }
        }

        System.out.print("\nSelecciona el número de l'equip de destí: ");
        int seleccionEquipDesti = scanInt();

        if (seleccionEquipDesti < 1 || seleccionEquipDesti > dadesEquips.size() || seleccionEquipDesti == seleccionEquipOrigen) {
            System.out.println("¡Selecció no vàlida!");
            return;
        }

        Equip equipDesti = dadesEquips.get(seleccionEquipDesti - 1);

        equipOrigen.getJugadors().remove(jugadorSeleccionado);
        equipDesti.addJugador(jugadorSeleccionado);

        System.out.println("\n¡Transferència realitzada amb èxit!");
        System.out.println("Jugador: " + jugadorSeleccionado.getNom() + " " + jugadorSeleccionado.getCognom());
        System.out.println("Transferit de l'equip " + equipOrigen.getNomEquip() + " a l'equip" + equipDesti.getNomEquip());

        guardarEquipsEnFitxer(dadesEquips);
    }


/////////////////////////////////////////////////////OPCIO10////////////////////////////////////////////////////

    /**
     * Metode que crida tots els metodes de guardar del programa per guardar una partida
     *
     * @param lliga             Object Class lliga dades de la lliga
     * @param dadesEquips       ArrayList Class Equip dades de tots els equips
     * @param mercatFitxatges   ArrayList Class ProfesionalDeportivo amb les dades del mercat de jugadors i entrenadors
     */
    public static void guardarPartida(Lliga lliga, ArrayList<Equip> dadesEquips,
                                      ArrayList<ProfessionalDeportivo> mercatFitxatges){
        guardarLliga(lliga);
        guardarEquipsEnFitxer(dadesEquips);
        guardarPartidaFitxerFitxatges(mercatFitxatges);
    }

    /////////////////////////////////////////////////GAME/////////////////////////////////////////////////////////////


}
