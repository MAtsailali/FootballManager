package classes;

import java.util.HashMap;
import static classes.GameMechanics.*;


/**
 * Classe Objecte Lliga
 */
public class Lliga {
    private String codiLliga;
    private int jornades;
    private static int jornadasSimuladas;
    private int numEquips;
    private HashMap<String, Integer> registreEquips = new HashMap<>();

    /**
     * Parametres minims per el constructor de la clase lliga.
     *
     * @param codiLliga      String amb el nom o codi de la lliga
     * @param jornades       int amb el numero de Jornades
     * @param numEquips      int amb el numero de equips de la lliga
     * @param registreEquips HashMap amb el registre de equips participats nomEquip-puntuacio
     */
    public Lliga(String codiLliga, int jornades, int numEquips, HashMap<String, Integer> registreEquips) {
        this.codiLliga = codiLliga;
        this.jornades = jornades;
        this.numEquips = numEquips;
        this.registreEquips = registreEquips;
    }

    /**
     * Busca el atribut codi lliga en una Lliga
     *
     * @return atribut codi lliga
     */
    public String getCodiLliga() {
        return codiLliga;
    }

    /**
     * Registra el codi de una lliga amb el String facilitat
     *
     * @param codiLliga String amb el nom de la Lliga
     */
    public void setCodiLliga(String codiLliga) {
        this.codiLliga = codiLliga;
    }

    /**
     * Busca el atribut jornades en una Lliga
     *
     * @return atribut jornades
     */
    public int getJornades() {
        return jornades;
    }

    /**
     * Registra les jornades de una lliga amb el int facilitat
     *
     * @param jornades int amb el numero de jornades
     */
    public void setJornades(int jornades) {
        this.jornades = jornades;
    }

    /**
     * Busca el atribut num equips en una Lliga
     *
     * @return atribut num equips
     */
    public int getNumEquips() {
        return numEquips;
    }

    /**
     * Registra les jornades de una lliga amb el int facilitat
     *
     * @param numEquips int amb el numero de equips
     */
    public void setNumEquips(int numEquips) {
        this.numEquips = numEquips;
    }

    /**
     * Busca el atribut registre de Equips en una Lliga
     *
     * @return HashMap registre equips
     */
    public HashMap<String, Integer> getRegistreEquips() {
        return registreEquips;
    }

    /**
     * Registra el atribut registre equips de una Lliga
     *
     * @param registreEquips HashMap registre equips
     */
    public void setRegistreEquips(HashMap<String, Integer> registreEquips) {
        this.registreEquips = registreEquips;
    }

    /**
     *  Busca el atribut jornadas simuladas en una Lliga
     *
     * @return int de jornadas simuladas
     */
    public int getJornadasSimuladas() {
        return jornadasSimuladas;
    }


    /**
     *  Registra el atribut jornadas simuladas de una Lliga
     *
     * @param jornadasSimuladas int jornadas simuladas
     */
    public void setJornadasSimuladas(int jornadasSimuladas) {
        this.jornadasSimuladas = jornadasSimuladas;
    }

    public void reiniciarPuntsEquips() {
        for (String nomEquip : registreEquips.keySet()) {
            registreEquips.put(nomEquip, 0);
        }
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
     * Sobreescriu el metode de ToString que imprimeix dades lliga.
     *
     * @return text dades lliga
     */
    @Override
    public String toString() {
        return "Dades de la Lliga: \n" +
                "           Codi de la lliga: " + codiLliga + "\n" +
                "           Numero de jornades: " + jornades + "\n" +
                "           Numero de equips participants: " + numEquips + "\n" +
                "           Llistat de equips participants" + registreEquips;
    }
}
