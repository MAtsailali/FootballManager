package classes;
import java.util.ArrayList;
import static classes.GameMechanics.*;

/**
 * Classe Objecte Equip.
 */
public class Equip {
    private String nomEquip;
    private int anyFundacio;
    private String ciutat;
    private String nomEstadi;
    private President president;
    private ArrayList<Jugador> jugadors;
    private Entrenador entrenador;
    private int partitsGuanyats = 0;
    private int partitsPerduts = 0;
    private int golsMarcats = 0;
    private int golsRebuts = 0;


    /**
     *  Parametres minims per el contructor de Equip.
     *
     * @param nomEquip    nom del Equip
     * @param anyFundacio any de fundacio Equip
     * @param ciutat      ciutat del Equip
     * @param nomEstadi   nom del estadi del Equip
     * @param president   president del Equip
     */
    public Equip(String nomEquip, int anyFundacio, String ciutat, String nomEstadi, President president) {
        this.nomEquip = nomEquip;
        this.anyFundacio = anyFundacio;
        this.ciutat = ciutat;
        this.nomEstadi = nomEstadi;
        this.president = president;
        this.jugadors = null;
        this.entrenador = null;
    }

    /**
     * Parametres minims per el contructor de Equip.
     *
     * @param nomEquip    nom del Equip
     * @param anyFundacio any de fundacio Equip
     * @param ciutat      ciutat del Equip
     * @param jugadors    llistat de jugadors del Equip
     * @param entrenador  entrenador del Equip
     */
    public Equip(String nomEquip, int anyFundacio, String ciutat, ArrayList<Jugador> jugadors, Entrenador entrenador) {
        this.nomEquip = nomEquip;
        this.anyFundacio = anyFundacio;
        this.ciutat = ciutat;
        this.nomEstadi = "Sense Nom";
        this.president = null;
        this.jugadors = jugadors;
        this.entrenador = entrenador;
    }

    /**
     * Parametres per el contructor de Equip.
     *
     * @param nomEquip    nom del Equip
     * @param anyFundacio any de fundacio Equip
     * @param ciutat      ciutat del Equip
     * @param nomEstadi   nom del estadi del Equip
     * @param jugadors    llistat de jugadors del Equip
     * @param entrenador  entrenador del Equip
     */
    public Equip(String nomEquip, int anyFundacio, String ciutat, String nomEstadi, ArrayList<Jugador> jugadors, Entrenador entrenador) {
        this.nomEquip = nomEquip;
        this.anyFundacio = anyFundacio;
        this.ciutat = ciutat;
        this.nomEstadi = nomEstadi;
        this.president = null;
        this.jugadors = jugadors;
        this.entrenador = entrenador;
    }

    /**
     * Parametres per el contructor de Equip.
     *
     * @param nomEquip    nom del Equip
     * @param anyFundacio any de fundacio Equip
     * @param ciutat      ciutat del Equip
     * @param nomEstadi   nom del estadi del Equip
     * @param president   president del Equip
     * @param jugadors    llistat de jugadors del Equip
     * @param entrenador  entrenador del Equip
     */
    public Equip(String nomEquip, int anyFundacio, String ciutat, String nomEstadi, President president, ArrayList<Jugador> jugadors, Entrenador entrenador) {
        this.nomEquip = nomEquip;
        this.anyFundacio = anyFundacio;
        this.ciutat = ciutat;
        this.nomEstadi = nomEstadi;
        this.president = president;
        this.jugadors = jugadors;
        this.entrenador = entrenador;
    }


    /**
     * Busca l'atribut de nom del equip.
     *
     * @return String nom equip
     */
    public String getNomEquip() {
        return nomEquip;
    }

    /**
     * Registra l'atribut de nom del equip.
     *
     * @param nomEquip string nom equip
     */
    public void setNomEquip(String nomEquip) {
        this.nomEquip = nomEquip;
    }

    /**
     * Busca l'atribut de any de fundacio.
     *
     * @return int any fundacio
     */
    public int getAnyFundacio() {
        return anyFundacio;
    }

    /**
     * Registra l'atribut de any de fundacio.
     *
     * @param anyFundacio int any de fundacio
     */
    public void setAnyFundacio(int anyFundacio) {
        this.anyFundacio = anyFundacio;
    }

    /**
     * Busca l'atribut del nom de la ciutat.
     *
     * @return  string nom ciutat
     */
    public String getCiutat() {
        return ciutat;
    }

    /**
     * registra l'atribut del nom de la ciutat.
     *
     * @param ciutat string ciutat
     */
    public void setCiutat(String ciutat) {
        this.ciutat = ciutat;
    }

    /**
     * Busca l'atribut del nom del estadi.
     *
     * @return string nom del estadi
     */
    public String getNomEstadi() {
        return nomEstadi;
    }

    /**
     * Registra l'atribut del nom del estadi.
     *
     * @param nomEstadi string nom estadi
     */
    public void setNomEstadi(String nomEstadi) {
        this.nomEstadi = nomEstadi;
    }


    /**
     * Registra el president amb el objecte president que seleccionem
     *
     * @param president object classe President  president
     */
    public void setPresident(President president) {
        this.president = president;
    }

    /**
     * Buscar el president del equip
     *
     * @return object classe president
     */
    public President getPresident() {
        return president;
    }

    /**
     * Busca el llista de jugadors que conte el equip.
     *
     * @return jugadors Arraylist de objectes classe Jugador
     */
    public ArrayList<Jugador> getJugadors() {
        return jugadors;
    }

    /**
     * Registra la arraylist de jugadors amb la arraylist de objectes jugador que seleccionem
     *
     * @param jugadors Arraylist classe jugadors
     */
    public void setJugadors(ArrayList<Jugador> jugadors) {
        this.jugadors = jugadors;
    }

    /**
     * Registra un jugador a la arraylist de objectes jugador
     *
     * @param jugador objecte classe jugador
     */
    public void setJugador(Jugador jugador) {
        this.jugadors.add(jugador);
    }




    /**
     *  Buscar el entrenador del equip
     *
     * @return objecte classe entrenador
     */
    public Entrenador getEntrenador() {
        return entrenador;
    }

    /**
     * Registra el entrenador amb el objecte entrenador que seleccionem
     *
     * @param entrenador objecte classe entrenador
     */
    public void setEntrenador(Entrenador entrenador) {
        this.entrenador = entrenador;
    }

    /**
     * Busca els numero de gols marcats per l equip
     *
     * @return int gols marcats
     */
    public int getGolsMarcats() {
        return golsMarcats;
    }

    /**
     * Registra el numero de gols marcats per l equip
     *
     * @param golsMarcats int gols marcats
     */
    public void setGolsMarcats(int golsMarcats) {
        this.golsMarcats = golsMarcats;
    }

    /**
     * Busca els numero de gols rebuts per l equip
     *
     * @return int gols rebuts
     */
    public int getGolsRebuts() {
        return golsRebuts;
    }

    /**
     * Registra el numero de gols rebuts per l equip
     *
     * @param golsRebuts int gols rebuts
     */
    public void setGolsRebuts(int golsRebuts) {
        this.golsRebuts = golsRebuts;
    }

    /**
     * Busca el numero de partits guanyats per l equip
     *
     * @return int partits guanyats
     */
    public int getPartitsGuanyats() {
        return partitsGuanyats;
    }

    /**
     * Registra el numero de partits guanyats per l equip
     *
     * @param partitsGuanyats int partits guanyats
     */
    public void setPartitsGuanyats(int partitsGuanyats) {
        this.partitsGuanyats = partitsGuanyats;
    }

    /**
     * Busca el numero de partits perduts per l equip
     *
     * @return int partits perduts
     */
    public int getPartitsPerduts() {
        return partitsPerduts;
    }

    /**
     * Registra el numero de partits perduts per l equip
     *
     * @param partitsPerduts int partits perduts
     */
    public void setPartitsPerduts(int partitsPerduts) {
        this.partitsPerduts = partitsPerduts;
    }

    // Altres mètodes

    /**
     * metode que agrega un objecte classe jugador a la arraylist de jugadors
     * en cas de que no hi hagi arraylist de jugadors en crea una de nova
     *
     * @param jugador the jugador
     */
    public void addJugador(Jugador jugador) {
        if (jugadors == null) {
            jugadors = new ArrayList<>();
        }
        jugadors.add(jugador);
    }

    /**
     * Metode que demana el registre de un nou entrenador i l agrega al equip
     *
     */
    public void assignarEntrenadorAlEquip() {
        Entrenador entrenador = nouEntrenador();
        missatgeEntrenadorCanviTrue();
        this.setEntrenador(entrenador);

    }


    /**
     * Assignar un jugador al arraylist de jugadors del equip.
     *
     */
    public void assignarUnJugadorAlEquip() {
        Jugador Jugador = nouJugador();
        this.setJugador(Jugador);

    }


    /**
     * Imprimeix les dades del equip.
     *
     * @return dades equip
     */

    @Override
    public String toString() {
        String presidentNom;
        if (president != null) {
            presidentNom = president.getNomComplet();
        } else {
            presidentNom = "President desconegut";
        }

        String entrenadorNom;
        if (entrenador != null) {
            entrenadorNom = entrenador.getNomComplet();
        } else {
            entrenadorNom = "Entrenador desconegut";
        }

        String jugadorsInfo;
        if (jugadors != null) {
            StringBuilder sb = new StringBuilder();
            for (Jugador j : jugadors) {
                sb.append(j.getNomComplet()).append(", ");
            }
            jugadorsInfo = sb.toString();
        } else {
            jugadorsInfo = "Llista de jugadors buida";
        }

        return "Dades del equip: " +
                "Nom del equip: " + nomEquip + '\n' +
                "Any de Fundacio: " + anyFundacio + '\n' +
                "Ciutat: " + ciutat + '\n' +
                "Nom del Estadi: " + nomEstadi + '\n' +
                "Nom del President: " + presidentNom + '\n' +
                "Dades del Entrenador: " + entrenadorNom + '\n' +
                "Dades dels Jugadors: " + jugadorsInfo + '\n';

    }
}
