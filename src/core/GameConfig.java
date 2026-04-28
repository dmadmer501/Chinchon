package core;

import models.Player;
import java.util.List;

/**
 * Representa la configuración final y el estado de los parámetros del juego Chinchón.
 * Almacena los límites de puntuación, el número de barajas y la lista de participantes.
 * * @author Gemini
 * @version 1.1
 */
public class GameConfig {
    private final int maxPoints;
    private final int deckCount;
    private final List<Player> players;

    /**
     * Constructor que inicializa la configuración. 
     * Se utiliza desde la clase {@link GameConfigBuilder}.
     * @param maxPoints Puntos límite para terminar la partida.
     * @param deckCount Número de barajas de 40 cartas en juego.
     * @param players Lista de jugadores que participarán.
     */
    public GameConfig(int maxPoints, int deckCount, List<Player> players) {
        this.maxPoints = maxPoints;
        this.deckCount = deckCount;
        this.players = players;
    }

    public int getMaxPoints() { 
        return maxPoints; 
    }

    public int getDeckCount() { 
        return deckCount; 
    }

    public List<Player> getPlayers() { 
        return players; 
    }
}