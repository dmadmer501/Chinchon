package models;

/**
 * Representa una carta individual de la baraja española.
 * Cada carta posee un palo (Suit) y un rango (Rank). Incluye soporte para 
 * iconos con ANSI
 */
public class Card {
 
    /** El palo al que pertenece la carta (Oros, Copas, Espadas o Bastos). */
    private final Suit suit;
 
    /** El rango o valor numérico/figura de la carta. */
    private final Rank rank;

    /**
     * Construye una nueva carta con el palo y rango especificados.
     * @param suit El palo de la carta.
     * @param rank El rango de la carta.
     */
    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    /**
     * Obtiene el palo de la carta.
     * @return El objeto Suit correspondiente.
     */
    public Suit getSuit() { 
        return suit; 
    }

    /**
     * Obtiene el rango de la carta.
     * @return El objeto Rank correspondiente.
     */
    public Rank getRank() { 
        return rank; 
    }
    
    /**
     * Devuelve el valor nominal de la carta utilizado para el recuento de puntos.
     * @return El valor entero asociado al rango de la carta.
     */
    public int getPoints() { 
        return rank.getValue(); 
    }

    /**
     * Devuelve una representación textual de la carta, con opción de resaltado.
     * @param highlighted Si es true, la salida incluirá el código de color azul/cian.
     * @return Una cadena formateada con el rango y el palo de la carta.
     */
    public String toString(boolean highlighted) {
        String ANSI_BLUE = "\u001B[36m";
        String ANSI_RESET = "\u001B[0m";
        String content = "[" + rank + " de " + suit + " " + "]";
        return highlighted ? ANSI_BLUE + content + ANSI_RESET : content;
    }

    /**
     * Devuelve la representación textual estándar de la carta sin resaltado.
     * @return Cadena con el formato [Rango de Palo].
     */
    @Override
    public String toString() {
        return toString(false);
    }
}