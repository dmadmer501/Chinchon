package core;

import models.*;
import java.util.*;

/**
 * Gestiona el mazo, la pila de descartes, los turnos de los jugadores y el recuento de puntos.
 */
public class GameManager {
   
    /** Configuración actual de la partida (puntos, barajas, jugadores). */
    private GameConfig config;
   
    /** Lista que representa el mazo de cartas para robar. */
    private final List<Card> deck = new ArrayList<>();
   
    /** Lista que representa el montón de cartas descartadas. */
    private final List<Card> discardPile = new ArrayList<>();
   
    /** Analizador lógico para validar combinaciones y cierres. */
    private final HandAnalyzer analyzer = new HandAnalyzer();
   
    /** Estado que indica si la partida completa ha finalizado. */
    private boolean gameEnded = false;
   
    /** Contador de turnos transcurridos en la ronda actual. */
    private int currentRoundTurn = 1;

    /**
     * Constructor privado para evitar instanciación externa (Patrón Singleton).
     */
    private GameManager() {}

    /**
     * Clase interna estática para la inicialización segura del Singleton (Bill Pugh Singleton).
     */
    private static class Holder {
        private static final GameManager INSTANCE = new GameManager();
    }

    /**
     * Obtiene la instancia única del administrador del juego.
     * @return Instancia de GameManager.
     */
    public static GameManager getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * Proporciona acceso al analizador de combinaciones de la partida.
     * @return El objeto HandAnalyzer configurado.
     */
    public HandAnalyzer getAnalyzer() {
        return this.analyzer;
    }

    /**
     * Inicializa el juego con una configuración específica y prepara la primera ronda.
     * @param config Objeto GameConfig con los parámetros de la partida.
     */
    public void init(GameConfig config) {
        this.config = config;
        this.gameEnded = false;
        prepareRound();
    }

    /**
     * Prepara una nueva ronda: limpia el mazo, baraja, reparte cartas y establece el primer descarte.
     */
    private void prepareRound() {
        deck.clear();
        discardPile.clear();
        currentRoundTurn = 1;
        
        int d = 0;
        while (d < config.getDeckCount()) {
            for (Suit s : Suit.values()) {
                for (Rank r : Rank.values()) deck.add(new Card(s, r));
            }
            d++;
        }
        Collections.shuffle(deck);

        List<Player> players = config.getPlayers();
        int p = 0;
        while (p < players.size()) {
            players.get(p).getHand().clear();
            int c = 0;
            while (c < 7) {
                players.get(p).addCard(drawFromDeck());
                c++;
            }
            p++;
        }
        discardPile.add(drawFromDeck());
    }

    /**
     * Inicia el ciclo principal de la partida hasta que se cumpla la condición de fin.
     */
    public void run() {
        while (!gameEnded) {
            playRound();
        }
        announceFinalWinner();
    }

    /**
     * Controla el ciclo de turnos de una ronda individual hasta que un jugador cierra.
     */
    private void playRound() {
        boolean roundActive = true;
        int playerIndex = 0;
        List<Player> players = config.getPlayers();

        while (roundActive && !gameEnded) {
            Player p = players.get(playerIndex);
            boolean closed = executeTurn(p);

            if (closed) {
                roundActive = false; 
                processEndRound(p);  
            } else {
                playerIndex = (playerIndex + 1) % players.size();
                currentRoundTurn++;
            }
        }
    }

    /**
     * Ejecuta las fases del turno de un jugador: robo, verificación de cierre y descarte.
     * @param p El jugador que posee el turno actual.
     * @return true si el jugador ha cerrado la ronda con éxito, false en caso contrario.
     */
    private boolean executeTurn(Player p) {
        Card top = discardPile.get(discardPile.size() - 1);
        Card drawn;
        if (p.chooseDrawSource(top)) {
            drawn = drawFromDeck();
            System.out.println("\n-> Carta cogida del mazo: " + drawn.toString());
        } else {
            drawn = discardPile.remove(discardPile.size() - 1);
            System.out.println("\n-> Carta cogida del descarte: " + drawn.toString());
        }
        p.addCard(drawn);

        boolean canPhysicallyClose = currentRoundTurn > config.getPlayers().size() 
                                     && p.getTotalPoints() < config.getMaxPoints() 
                                     && canCloseAfterDiscard(p.getHand());

        boolean closeSuccess = false;
        
        if (canPhysicallyClose && p.wantsToClose()) {
            Card discarded = p.discard(); 
            if (analyzer.canClose(p.getHand())) {
                closeSuccess = true;
                discardPile.add(discarded);
                System.out.println("\n*** " + p.getName().toUpperCase() + " HA CERRADO LA RONDA ***");
            } else {
                System.out.println("Cierre inválido: El descarte elegido no permite cerrar.");
                p.addCard(discarded);
                discardPile.add(p.discard());
            }
        } else {
            discardPile.add(p.discard());
        }

        return closeSuccess;
    }

    /**
     * Simula el descarte de cada una de las 8 cartas disponibles para determinar si es posible cerrar.
     * @param hand8 Lista de 8 cartas tras el robo.
     * @return true si al menos un descarte posible resulta en una mano válida para cerrar.
     */
    private boolean canCloseAfterDiscard(List<Card> hand8) {
        boolean possible = false;
        int i = 0;
        while (i < hand8.size() && !possible) {
            List<Card> testHand = new ArrayList<>(hand8);
            testHand.remove(i);
            if (analyzer.canClose(testHand)) {
                possible = true;
            }
            i++;
        }
        return possible;
    }

    /**
     * Procesa el final de una ronda: calcula puntos, aplica bonificaciones y verifica si alguien superó el límite.
     * @param winner El jugador que ha provocado el cierre de la ronda.
     */
    private void processEndRound(Player winner) {
        System.out.println("\n--- RECUENTO DE PUNTOS ---");
        
        if (analyzer.isChinchon(winner.getHand())) {
            System.out.println("¡CHINCHÓN! " + winner.getName() + " gana la partida automáticamente.");
            gameEnded = true;
        } else {
            if (analyzer.findUnmatchedCards(winner.getHand()).isEmpty()) {
                winner.addPoints(-10);
                System.out.println(winner.getName() + " recibe -10 puntos de bonificación.");
            }

            boolean thresholdReached = false;
            List<Player> allPlayers = config.getPlayers();
            int i = 0;
            while (i < allPlayers.size()) {
                Player p = allPlayers.get(i);
                if (p != winner) {
                    int roundPoints = analyzer.calculatePoints(p.getHand());
                    p.addPoints(roundPoints);
                    System.out.println(p.getName() + " suma " + roundPoints + " pts. Total: " + p.getTotalPoints());
                } else {
                    System.out.println(p.getName() + " (Cerró) mantiene sus puntos: " + p.getTotalPoints());
                }
                
                if (p.getTotalPoints() >= config.getMaxPoints()) {
                    thresholdReached = true;
                }
                i++;
            }

            if (thresholdReached) {
                gameEnded = true;
            } else {
                System.out.println("\nNadie ha llegado a " + config.getMaxPoints() + ". Siguiente ronda...");
                prepareRound();
            }
        }
    }

    /**
     * Extrae una carta del mazo. Si el mazo está vacío, recicla la pila de descartes.
     * @return La carta extraída del mazo.
     */
    private Card drawFromDeck() {
        if (deck.isEmpty()) {
            System.out.println("! Mazo vacío. Barajando pila de descartes...");
            Card top = discardPile.remove(discardPile.size() - 1);
            deck.addAll(discardPile);
            discardPile.clear();
            discardPile.add(top);
            Collections.shuffle(deck);
        }
        return deck.remove(deck.size() - 1);
    }

    /**
     * Compara las puntuaciones de todos los jugadores y anuncia al ganador final (menor puntuación).
     */
    private void announceFinalWinner() {
        System.out.println("\n========================================");
        System.out.println("         FIN DE LA PARTIDA              ");
        System.out.println("========================================");
        
        List<Player> players = config.getPlayers();
        Player winner = players.get(0);
        int i = 1;
        while (i < players.size()) {
            if (players.get(i).getTotalPoints() < winner.getTotalPoints()) {
                winner = players.get(i);
            }
            i++;
        }
        System.out.println("EL GANADOR ES: " + winner.getName().toUpperCase() + " con " + winner.getTotalPoints() + " puntos.");
    }
}