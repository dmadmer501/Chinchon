package models;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase abstracta que define la estructura y el comportamiento base de un jugador.
 */
public abstract class Player {
    
    /** Nombre identificador del jugador. */
    protected String name;
    
    /** Lista de objetos {@link Card} que el jugador posee actualmente. */
    protected List<Card> hand = new ArrayList<>();
    
    /** Puntuación total acumulada a lo largo de las rondas de la partida. */
    protected int totalPoints = 0;

    /**
     * Constructor para inicializar un jugador con su nombre.
     * * @param name Nombre del jugador.
     */
    public Player(String name) { 
        this.name = name; 
    }

    /**
     * Obtiene el nombre del jugador.
     * * @return El nombre del jugador.
     */
    public String getName() { 
        return name; 
    }

    /**
     * Proporciona acceso a la mano de cartas actual del jugador.
     * * @return Lista de cartas en posesión del jugador.
     */
    public List<Card> getHand() { 
        return hand; 
    }

    /**
     * Obtiene la puntuación total acumulada por el jugador hasta el momento.
     * * @return Entero con el total de puntos.
     */
    public int getTotalPoints() { 
        return totalPoints; 
    }

    /**
     * Incrementa o decrementa la puntuación total del jugador.
     * @param pts Puntos a añadir (pueden ser negativos en caso de bonificaciones).
     */
    public void addPoints(int pts) { 
        this.totalPoints += pts; 
    }

    /**
     * Añade una carta a la mano del jugador.
     * @param card El objeto {@link Card} obtenido tras el robo.
     */
    public void addCard(Card card) { 
        hand.add(card); 
    }
    
    /**
     * Método abstracto para decidir la fuente de la cual se desea robar una carta.
     * @param topDiscard La carta que se encuentra en la parte superior de la pila de descartes.
     * @return true si el jugador decide robar del mazo, false si decide robar del descarte.
     */
    public abstract boolean chooseDrawSource(Card topDiscard);

    /**
     * Método abstracto para gestionar la acción de descartar una carta de la mano.
     * @return La carta seleccionada para ser enviada a la pila de descartes.
     */
    public abstract Card discard();

    /**
     * Método abstracto para determinar si el jugador desea cerrar la ronda actual.
     * @return true si el jugador opta por cerrar la ronda, false en caso contrario.
     */
    public abstract boolean wantsToClose();
}