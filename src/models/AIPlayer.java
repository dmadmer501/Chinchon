package models;

/**
 * Representa un jugador controlado por la inteligencia artificial (IA).
 * Esta clase implementa la lógica de toma de decisiones automática para las fases
 * de robo, descarte y cierre de ronda.
 */
public class AIPlayer extends Player {
    
    /**
     * Crea una nueva instancia de un jugador controlado por la IA.
     * @param name Nombre que se le asignará al jugador artificial.
     */
    public AIPlayer(String name) { 
        super(name);
    }

    /**
     * Determina la fuente de la cual robará la IA al inicio de su turno.
     * @param top La carta que se encuentra actualmente en la parte superior de la pila de descartes.
     * @return true, indicando que siempre roba del mazo.
     */
    @Override 
    public boolean chooseDrawSource(Card top) { 
        return true; 
    }

    /**
     * Ejecuta la acción de descarte de la IA.
     * En esta implementación, la IA descarta automáticamente la primera carta de su mano.
     * @return La carta que ha sido extraída de la mano del jugador para ser descartada.
     */
    @Override 
    public Card discard() { 
        return hand.remove(0); 
    }

    /**
     * Determina si la IA desea cerrar la ronda actual cuando tiene una combinación válida.
     * En esta implementación, la IA nunca toma la iniciativa de cerrar la ronda.
     * @return false, indicando que la IA no cerrará la ronda por voluntad propia.
     */
    @Override 
    public boolean wantsToClose() { 
        return false; 
    }
}