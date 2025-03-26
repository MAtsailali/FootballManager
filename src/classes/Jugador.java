package classes;
import java.text.DecimalFormat;
import java.util.Random;
import static classes.GameMechanics.*;


/**
 * Classe Objecte Jugador.
 */
public class Jugador extends ProfessionalDeportivo implements Comparable<Jugador> {
    private final String dataNeixament;
    private int nivellMotivacio;
    private final double preuJugador;
    private final int numDorsal;
    private String posicio;
    private int rendiment;


    private static final String[] POSICIONS = {"DAV", "MIG", "DEF", "POR"};
    private static final int PROBABILITAT_CANVI = 5;


    /**
     *  Parametres minims per el contructor de Jugador.
     *
     * @param nom           nom del jugador
     * @param cognom        cognom del jugador
     * @param dataNeixament data neixament del jugador
     * @param preuJugador   preu del jugador
     * @param numDorsal     num dorsal del jugador
     * @param posicio       posicio del jugador
     */
    public Jugador(String nom, String cognom, String dataNeixament, double preuJugador, int numDorsal,
                   String posicio) {
        super(nom, cognom);
        this.dataNeixament = dataNeixament;
        nivellMotivacio = 6;
        this.preuJugador = preuJugador;
        this.numDorsal = numDorsal;
        this.posicio = posicio;
        rendiment = 60;
    }


    /**
     * Parametres per el contructor de Jugador.
     *
     * @param nom             nom del jugador
     * @param cognom          cognom del jugador
     * @param dataNeixament   data neixament del jugador
     * @param nivellMotivacio nivell motivacio del jugador
     * @param preuJugador     preu del jugador
     * @param numDorsal       numero de dorsal del jugador
     * @param posicio         posicio del jugador
     * @param rendiment       rendiment del jugador
     */
    public Jugador(String nom, String cognom, String dataNeixament, int nivellMotivacio, double preuJugador,
                   int numDorsal, String posicio, int rendiment) {
        super(nom, cognom);
        this.dataNeixament = dataNeixament;
        this.nivellMotivacio = nivellMotivacio;
        this.preuJugador = preuJugador;
        this.numDorsal = numDorsal;
        this.posicio = posicio;
        this.rendiment = rendiment;
    }




    /**
     * Busca l'atribut de data neixament
     *
     * @return String data neixament
     */
    public String getDataNeixament() {
        return dataNeixament;
    }

    /**
     * Busca l'atribut de nivell motivacio
     *
     * @return int nivell motivacio
     */
    public  int getNivellMotivacio() {
        return nivellMotivacio;
    }

    /**
     * Registra el atribut de nivell motivacio amb el int facilitat
     *
     * @param nivellMotivacio int nivell motivacio
     */
    public  void setNivellMotivacio(int nivellMotivacio) {
        this.nivellMotivacio = nivellMotivacio;
    }

    /**
     * Busca l'atribut del preu del jugador
     *
     * @return double preu
     */
    public  double getPreu() {
        return preuJugador;
    }

    /**
     * Busca l'atribut el numero de dorsal
     *
     * @return int numero de dorsal
     */
    public int getNumDorsal() {
        return numDorsal;
    }

    /**
     * Busca l'atribut de posicio del jugador
     *
     * @return String posicio
     */
    public String getPosicio() {
        return posicio;
    }

    /**
     * Registra l'atribut de posicio amb la String facilitada.
     *
     * @param posicio String posicio
     */
    public void setPosicio(String posicio) {
        Jugador.this.posicio = posicio;
    }

    /**
     * Busca l'atribut de rendiment.
     *
     * @return int rendiment
     */
    public  int getRendiment() {
        return rendiment;
    }

    /**
     *  Registra l'atribut de rendiment amb el int facilitat
     *
     * @param rendiment int rendiment
     */
    public  void setRendiment(int rendiment) {
        this.rendiment = rendiment;
    }


    /**
     * metode que aumenta o disminueix el rendiment del jugador
     *
     * @param rendiment  int rendiment original
     * @return  int rendiment final
     */
    public static int incrementarODisminuirRendiment(int rendiment) {
        Random random = new Random();
        int numR = random.nextInt(100);
        if (numR < 10) {
            rendiment = rendiment + 20;
        } else if (numR < 50) {
            rendiment = rendiment + 10;
        } else if (numR < 60) {
            rendiment = rendiment - 20;
        } else if (numR < 100) {
            rendiment = rendiment + 10;

        }
        if (rendiment > 100) {
            rendiment = 100;
        } else if (rendiment < 0) {
            rendiment = 0;
        }
        return rendiment;
    }



    /**
     * Metode que retorna un boolean del canvi de posicio
     * si el numero random es mes petit que la probabilitat de canvi es realitzara el canvi
     *
     * @return boolean canvi de posicio
     */
    public static boolean esCanviDePosicio() {
        int random = numRandom100();
        return random < PROBABILITAT_CANVI;
    }


    /**
     * Metode per obtenir nova posicio string.
     * agafa la Array amb les posicions disponibles i seleciona la nova posicio
     *
     * @return string amb la nova posicio
     */
    public String canviNovaPosicio() {
        Random random = new Random();
        String novaPosicio;
        do {
            novaPosicio = POSICIONS[random.nextInt(POSICIONS.length)];
        } while (novaPosicio.equals(posicio));
        this.setPosicio(novaPosicio);
        return novaPosicio;
    }


    /**
     * Mostra la nova posicio en pantalla.
     *
     */
    public void mostrarNovaPosicio() {
        System.out.println("El jugador " + this.getNomComplet() + " ha canviat de posicio");
        System.out.println("La nova posició es " + this.posicio);
    }

    /**
     * Metode que engloba el canvi de posicio
     *
     */
    public void canviDePosicio() {
        if (esCanviDePosicio()) {
            mostrarNovaPosicio();
            canviNovaPosicio();
        }
    }

    @Override
    public void entrenamentMercatFitxatges(){
       this.setNivellMotivacio(incrementarODisminuirMotivacio(this.getNivellMotivacio()));
       this.setRendiment(incrementarODisminuirRendiment(this.getRendiment()));
       this.canviDePosicio();
    }




    /**
     * metode que imprimeix en detall les dades Jugador.
     *
     */
    public void imprimirDades() {
        System.out.println("Num Dorsal : " + this.getNumDorsal());
        System.out.println("Nom : " + this.getNom());
        System.out.println("Cognom : " + this.getCognom());
        System.out.println("Data de neixament : " + this.getDataNeixament());
        System.out.println("Preu del Jugador: " + this.getPreu());
        System.out.println("Posicio : " + this.getPosicio());
        System.out.println("Nivell de motivacio : " + this.getNivellMotivacio());
        System.out.println("Rendiment : " + this.getRendiment());
    }



    /**
     * Sobreescriu el metode de ToString que imprimeix dades del jugador.
     * S'utilitza en metodes per guardar fitxers
     *
     * @return  txt dades jugador
     */
    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("#.##");
        return "J;" + nom + ";" + cognom + ";" + dataNeixament + ";" + nivellMotivacio + ";" +
                df.format(preuJugador) + ";" + numDorsal + ";" + posicio + ";" + rendiment;
    }

    @Override
    public int compareTo(Jugador altre) {
        int compPosicio = this.posicio.compareTo(altre.posicio);
        if (compPosicio != 0) {
            return compPosicio;
        } else {
            return Integer.compare(this.numDorsal, altre.numDorsal);
        }

    }



}
