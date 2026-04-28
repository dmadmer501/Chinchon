package models;

/**
 * Define los rangos de las cartas para una baraja española de 40 cartas.
 * Incluye los valores numéricos del 1 al 7 y las figuras (Sota, Caballo, Rey).
 */
public enum Rank {
    UNO(1), 
    DOS(2), 
    TRES(3), 
    CUATRO(4), 
    CINCO(5), 
    SEIS(6), 
    SIETE(7), 
    SOTA(10), 
    CABALLO(11), 
    REY(12);

    /** Valor numérico nominal asignado al rango. */
    private final int value;

    /**
     * Constructor del rango con su valor correspondiente.
     * @param value El valor entero de la carta.
     */
    Rank(int value) { 
        this.value = value; 
    }

    /**
     * Obtiene el valor numérico del rango.
     * @return El valor entero asociado.
     */
    public int getValue() { 
        return value; 
    }
}