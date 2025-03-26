package classes;


import java.util.Objects;
import java.util.Random;

import static classes.GameMechanics.*;
import static classes.GameMechanics.mostrarMissatgeDesotivacio1;

/**
 * Classe Objecte Professional deportivo.
 */
public abstract class ProfessionalDeportivo {

    protected final String nom;
    protected final String cognom;

    /**
     * Parametres per el contructor de Professional deportivo.
     *
     * @param nom     nom del Professional deportivo
     * @param cognom  cognom del Professional deportivo
     */
    public ProfessionalDeportivo(String nom, String cognom) {
        this.nom = nom;
        this.cognom = cognom;
    }

    /**
     * Busca el atribut nom.
     *
     * @return String nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * Busca el atribut cognom.
     *
     * @return String cognom
     */
    public String getCognom() {
        return this.cognom;
    }

    /**
     * Concatena el Nom i el Cognom per obtenir el nom complet
     * utilitza el getters de nom i cognom
     *
     * @return string nom+cognom
     */
    public String getNomComplet(){
        return getNom() + " " + getCognom();
    }


    /**
     * Metode que entrena els jugadors i entrenadors del mercat de fitxatges
     * Te overide en les clases child
     */
    public void entrenamentMercatFitxatges() {}

    /**
     * metode que aumenta o disminueix la motivacio del jugador o entrenador
     *
     * @param motivacio int nivell de motivacio original
     * @return int nivell de motivacio final
     */
    public static int incrementarODisminuirMotivacio(int motivacio) {
        Random random = new Random();
        int numR = random.nextInt(100);
        if (numR < 10) {
            motivacio += 2;
        } else if (numR < 50) {
            motivacio++;
        } else if (numR < 60) {
            motivacio -= 2;
        } else if (numR < 100) {
            motivacio--;

        }
        if (motivacio > 10) {
            motivacio = 10;
        } else if (motivacio < 0) {
            motivacio = 0;
        }
        return motivacio;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProfessionalDeportivo that = (ProfessionalDeportivo) o;
        return Objects.equals(getNomComplet().toLowerCase(), that.getNomComplet().toLowerCase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNomComplet().toLowerCase());
    }

   // public abstract int compareTo(ProfessionalDeportivo altre);
}




