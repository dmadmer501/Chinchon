package models;

import core.GameManager;
import core.HandAnalyzer;
import java.util.List;

/**
 * Representa un jugador controlado por la inteligencia artificial (IA).
 * Lógica corregida para descarte inteligente y cierres automáticos.
 */
public class AIPlayer extends Player {

    public AIPlayer(String name) {
        super(name);
    }

    /**
     * La IA decide de dónde robar. Si la carta del descarte le sirve para formar
     * una combinación (mismo rango o continuidad), podría elegirla.
     * Para mantenerlo simple pero funcional: si ayuda a reducir puntos, la toma.
     */
    @Override
    public boolean chooseDrawSource(Card top) {
        // Estrategia base: si es un número bajo (menor o igual a 3), le puede servir.
        // De lo contrario, prefiere la sorpresa del mazo.
        return top.getPoints() > 3;
    }

    /**
     * CORREGIDO: La IA ya no tira siempre la carta 0.
     * Busca la peor carta suelta (la de mayor puntuación) y se deshace de ella.
     */
    @Override
    public Card discard() {
        HandAnalyzer analyzer = GameManager.getInstance().getAnalyzer();
        List<Card> unmatched = analyzer.findUnmatchedCards(this.hand);

        Card cardToDiscard;
        if (!unmatched.isEmpty()) {
            // Buscamos la carta suelta con más puntos para quitar peso
            cardToDiscard = unmatched.get(0);
            for (Card c : unmatched) {
                if (c.getPoints() > cardToDiscard.getPoints()) {
                    cardToDiscard = c;
                }
            }
        } else {
            // Si por algún motivo todo combina, descarta la primera de la mano
            cardToDiscard = hand.get(0);
        }

        this.hand.remove(cardToDiscard);
        return cardToDiscard;
    }

    /**
     * CORREGIDO: La IA ahora SÍ cierra la ronda si tiene una jugada ganadora.
     */
    @Override
    public boolean wantsToClose() {
        HandAnalyzer analyzer = GameManager.getInstance().getAnalyzer();
        // Si tiene 0 cartas sueltas o la que le queda es menor o igual a 3, cierra sin dudarlo
        List<Card> unmatched = analyzer.findUnmatchedCards(this.hand);
        return unmatched.isEmpty() || (unmatched.size() == 1 && unmatched.get(0).getPoints() <= 3);
    }
}