package models;

/**
 * Define los cuatro palos tradicionales de la baraja española.
 */
public enum Suit {
    COINS("Oros", "🟡"), 
    CUPS("Copas", "🏆"), 
    SWORDS("Espadas", "⚔️ "), 
    CLUBS("Bastos", "🌿");

    /** Nombre descriptivo del palo en español. */
    private final String name;
  
    /** Icono gráfico asociado al palo. */
    private final String icon;

    /**
     * Constructor para asociar un nombre e icono a cada palo.
     * @param name Nombre del palo en español.
     * @param icon Representación gráfica mediante Emoji.
     */
    Suit(String name, String icon) { 
        this.name = name; 
        this.icon = icon; 
    }
    
    /**
     * Devuelve una cadena con el icono y el nombre del palo.
     * @return Una representación formateada.
     */
    @Override 
    public String toString() { 
        return icon + " " + name; 
    }
}