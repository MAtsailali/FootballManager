package classes;

import java.text.DecimalFormat;

/**
 * Classe Objecte President.
 */
public class President extends ProfessionalDeportivo {
    private static int capital;


    /**
     * Parametres minims per el contructor de President.
     *
     * @param nom    nom del President
     * @param cognom cognom del President
     */
    public President(String nom, String cognom) {
        super(nom, cognom);
        capital = 200000000;
    }

    /**
     * Parametres per el contructor de President.
     *
     * @param nom     nom del President
     * @param cognom  cognom del President
     * @param capital capital del President
     */
    public President(String nom, String cognom, int capital) {
        super(nom, cognom);
        President.capital = capital;
    }

    /**
     * Busca el atribut capital.
     *
     * @return int capital
     */
    public static int getCapital() {
        return capital;
    }

    /**
     * Registra el atribut capital amb el valor facilitat.
     *
     * @param capital int del capital
     */
    public static void setCapital(int capital) {
        President.capital = capital;
    }

    /**
     * Sobreescriu el metode de ToString que imprimeix dades del president.
     * S'utilitza en metodes per guardar fitxers
     *
     * @return text dades president
     */
    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("#.##");
        return nom + "_" + cognom + "_" + df.format(capital);
    }
}
