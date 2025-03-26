import classes.GameMechanics;

import static classes.GameMechanics.seleccioPathFitxerOriginalFitxatges;

/**
 * Main
 * @author Mohammed_Atsailali & Raul_Garcia
 * @version 1.0
 * @since 2025-02-24
 */
public class Main {
    /**
     * Main que ejecuta el programa
     *
     * @param args programa game
     */
    public static void main(String[] args) {
        game();


    }

    /**
     * Metodo que llama el inicio del programa desde el GameMechanics
     */
    public static void game(){
        GameMechanics.funcionsMenuPrincipal();
    }

}