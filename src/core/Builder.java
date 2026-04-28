package core;

import models.*;
import ui.InputService;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase encargada de la construcción paso a paso de objetos {@link GameConfig}.
 * Implementa el patrón Builder
 */
public class Builder {
    private int maxPoints = 100;
    private int deckCount = 1;
    private final List<Player> players = new ArrayList<>();
    private final InputService input;

    /**
     * Inicializa un nuevo constructor de configuración.
     * @param input La entrada de datos para configurar jugadores humanos.
     */
    public Builder(InputService input) { 
        this.input = input; 
    }

    public Builder setMaxPoints(int pts) { 
        this.maxPoints = pts; 
        return this; 
    }

    public Builder setDeckCount(int count) { 
        this.deckCount = count; 
        return this; 
    }

    public Builder addHuman(String name) { 
        this.players.add(new HumanPlayer(name, input)); 
        return this; 
    }

    public Builder addAI(String name) { 
        this.players.add(new AIPlayer(name)); 
        return this; 
    }

    /**
     * Crea la instancia final de {@link GameConfig}.
     * @return Un nuevo objeto GameConfig con los parámetros establecidos.
     */
    public GameConfig build() { 
        return new GameConfig(maxPoints, deckCount, players); 
    }
}