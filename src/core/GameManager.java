package core;

import models.*;
import java.util.*;

/**
 * Motor del juego corregido para evitar bugs de descarte duplicado y cierres inválidos.
 */
public class GameManager {

    private GameConfig config;
    private final List<Card> deck = new ArrayList<>();
    private final List<Card> discardPile = new ArrayList<>();
    private final HandAnalyzer analyzer = new HandAnalyzer();
    private boolean gameEnded = false;
    private int currentRoundTurn = 1;

    private GameManager() {}

    private static class Holder {
        private static final GameManager INSTANCE = new GameManager();
    }

    public static GameManager getInstance() {
        return Holder.INSTANCE;
    }

    public HandAnalyzer getAnalyzer() {
        return this.analyzer;
    }

    public void init(GameConfig config) {
        this.config = config;
        this.gameEnded = false;
        prepareRound();
    }

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

    public void run() {
        while (!gameEnded) {
            playRound();
        }
        announceFinalWinner();
    }

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
     * CORREGIDO: Estructura de fase de descarte controlada.
     */
    private boolean executeTurn(Player p) {
        Card top = discardPile.get(discardPile.size() - 1);
        Card drawn;
        if (p.chooseDrawSource(top)) {
            drawn = drawFromDeck();
            System.out.println("\n-> " + p.getName() + " coge del mazo.");
        } else {
            drawn = discardPile.remove(discardPile.size() - 1);
            System.out.println("\n-> " + p.getName() + " coge del descarte: " + drawn.toString());
        }
        p.addCard(drawn);

        // Comprobamos si cumple las condiciones del juego para poder cerrar
        boolean canPhysicallyClose = currentRoundTurn > config.getPlayers().size()
                && p.getTotalPoints() < config.getMaxPoints()
                && canCloseAfterDiscard(p.getHand());

        boolean closeSuccess = false;

        // Si puede y el jugador (Humano o IA) quiere cerrar
        if (canPhysicallyClose && p.wantsToClose()) {
            // El jugador elige su descarte para cerrar
            Card discarded = p.discard();
            if (analyzer.canClose(p.getHand())) {
                closeSuccess = true;
                discardPile.add(discarded);
                System.out.println("\n*** " + p.getName().toUpperCase() + " HA CERRADO LA RONDA ***");
            } else {
                System.out.println("Cierre inválido: Ese descarte no te permite cerrar. Turno ordinario continuo.");
                // Devolvemos la carta para que la mano siga teniendo 8 antes del descarte normal
                p.getHand().add(discarded);
                // Forzamos un descarte normal sin cerrar
                discardPile.add(p.discard());
            }
        } else {
            // Turno normal: Descarte ordinario sin cerrar
            discardPile.add(p.discard());
        }

        return closeSuccess;
    }

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

    private void processEndRound(Player winner) {
        System.out.println("\n--- RECUENTO DE PUNTOS ---");

        if (analyzer.isChinchon(winner.getHand())) {
            System.out.println("¡CHINCHÓN! " + winner.getName() + " gana la partida automáticamente.");
            gameEnded = true;
        } else {
            if (analyzer.findUnmatchedCards(winner.getHand()).isEmpty()) {
                winner.addPoints(-10);
                System.out.println(winner.getName() + " recibe -10 puntos de bonificación por cerrar con 0 puntos sueltos.");
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