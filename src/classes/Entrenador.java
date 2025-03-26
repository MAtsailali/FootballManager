package classes;


import java.text.DecimalFormat;

import static classes.GameMechanics.numRandom100;

/**
 * Classe Objecte Entrenador.
 */
public class Entrenador extends ProfessionalDeportivo implements Comparable<Entrenador>{
    private final String dataNeixament;
    private int nivellMotivacio;
    private double souAnual;
    private int numTorneigsGuanyats;
    private boolean selecionadorNacional;

    private static final int PROBABILITAT_CANVI = 5;

    /**
     * Parametres minims per el contructor de Entrenador.
     *
     * @param nom                  nom del entrenador
     * @param cognom               cognom del entrenador
     * @param dataNeixament        data neixament del entrenador
     * @param nivellMotivacio      nivell motivacio del entrenador
     * @param souAnual             sou anual del entrenador
     * @param numTorneigsGuanyats  numero de torneigs guanyats que te el entrenador
     * @param selecionadorNacional es selecionador nacional
     */
    public Entrenador(String nom, String cognom, String dataNeixament, int nivellMotivacio, double souAnual, int numTorneigsGuanyats, boolean selecionadorNacional) {
        super(nom, cognom);
        this.dataNeixament = dataNeixament;
        this.nivellMotivacio = nivellMotivacio;
        this.souAnual = souAnual;
        this.numTorneigsGuanyats = numTorneigsGuanyats;
        this.selecionadorNacional = selecionadorNacional;

    }


    /**
     * Busca l'atribut de data neixament
     *
     * @return String data neixament
     */
    public String getDataNeixament() {
        return this.dataNeixament;
    }

    /**
     * Busca l'atribut de nivell motivacio
     *
     * @return int nivell motivacio
     */
    public int getNivellMotivacio() {
        return this.nivellMotivacio;
    }

    /**
     * Registra el atribut de nivell motivacio amb el int facilitat
     *
     * @param nivellMotivacio int nivell motivacio
     */
    public void setNivellMotivacio(int nivellMotivacio) {
        this.nivellMotivacio = nivellMotivacio;
    }

    /**
     * Busca l'atribut del sou anual del entrenador
     *
     * @return double sou anual
     */
    public double getSouAnual() {
        return this.souAnual;
    }

    /**
     * Registra l'atribut el numero del sou anual del entrenador
     *
     * @param souAnual double souAnual
     */
    public void setSouAnual(double souAnual) {
        this.souAnual = souAnual;
    }

    /**
     * Busca l'atribut de numero de torneigs guanyats
     *
     * @return int numero torneigs guanyats
     */
    public int getNumTorneigsGuanyats() {
        return this.numTorneigsGuanyats;
    }

    /**
     * Registra l'atribut de numero de torneigs guanyats
     *
     * @param numTorneigsGuanyats int numero de torneigs guanyats
     */
    public void setNumTorneigsGuanyats(int numTorneigsGuanyats) {
        this.numTorneigsGuanyats = numTorneigsGuanyats;
    }

    /**
     * Busca l'atribut de selecionador nacional boolean
     *
     * @return boolean selecionador nacional
     */
    public boolean isSelecionadorNacional() {
        return this.selecionadorNacional;
    }

    /**
     * Registra l'atribut de selecionador nacional boolean
     *
     * @param selecionadorNacional boolean selecionador nacional
     */
    public void setSelecionadorNacional(boolean selecionadorNacional) {
        this.selecionadorNacional = selecionadorNacional;
    }


    /**
     * Metode que calcular el increment del 0.5% de sou actual i el retorna en un double
     *
     * @return double increment del sou
     */
    public double calcularIncrement() {
        return (this.souAnual * 0.5);
    }


    /**
     * Aplica el increment del sou del 0.5% calculat previament i el suma al sou actual
     *
     * @param increment double increment del sou
     * @return double total = sou actual + increment
     */
    public double aumentarSouAnual(double increment) {
        this.setSouAnual(souAnual + increment);
        return souAnual;
    }


    /**
     * Metode que retorna un boolean del augment del sou
     * si el numero random es mes petit que la probabilitat de canvi es realitzara el aument
     *
     *
     * @return  boolean augment sou
     */
    public static boolean esAumentSou() {
        int random = numRandom100();
        return random < PROBABILITAT_CANVI;
    }

    @Override
    public void entrenamentMercatFitxatges(){
        this.setNivellMotivacio(incrementarODisminuirMotivacio(this.getNivellMotivacio()));
        if (esAumentSou()) {
            this.aumentarSouAnual(calcularIncrement());
        }

    }




    /**
     * Sobreescriu el metode de ToString que imprimeix dades del entrenador
     * S'utilitza en metodes per guardar fitxers
     *
     * @return  txt dades entrenador
     */

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("#.##");
        return "E;" + nom + ";" + cognom + ";" + dataNeixament + ";" + nivellMotivacio + ";" +
                df.format(souAnual) + ";" + numTorneigsGuanyats + ";" + selecionadorNacional;
    }



    @Override
    public int compareTo(Entrenador altre) {
        return Integer.compare(altre.numTorneigsGuanyats, this.numTorneigsGuanyats);
    }

}
