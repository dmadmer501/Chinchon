package core;

import models.Card;
import models.Rank;
import models.Suit;

import java.util.*;

/**
 * Clase encargada de analizar las combinaciones de cartas en la mano de un jugador.
 * Proporciona lógica para identificar grupos de cartas iguales, escaleras y 
 * calcular la puntuación de las cartas no combinadas.
 */
public class HandAnalyzer {

    /**
     * Identifica y devuelve una lista con las cartas que no forman parte de ninguna combinación.
     * El algoritmo primero busca grupos de cartas con el mismo valor y luego escaleras 
     * del mismo palo.
     * @param hand La lista de cartas a analizar (la mano del jugador).
     * @return Una sublista de cartas que no están combinadas (cartas "sueltas").
     */
    public List<Card> findUnmatchedCards(List<Card> hand) {

        // Copiamos las referencias para no destruir la mano real del jugador
        List<Card> remaining = new ArrayList<>(hand);
        
        // 1. ELIMINAR GRUPOS (Igual número)
        // Buscamos cada rango (As, Dos, Tres...)
        Rank[] ranks = Rank.values();
        int r = 0;
        while (r < ranks.length) {
            Rank currentRank = ranks[r];
            List<Card> group = new ArrayList<>();
            
            int i = 0;
            while (i < remaining.size()) {
                if (remaining.get(i).getRank() == currentRank) {
                    group.add(remaining.get(i));
                }
                i++;
            }
            
            // Si hay 3 o más cartas del mismo rango, las eliminamos de la lista de "sueltas"
            if (group.size() >= 3) {
                remaining.removeAll(group);
            }
            r++;
        }

        // 2. ELIMINAR ESCALERAS (Mismo palo, números consecutivos)
        Suit[] suits = Suit.values();
        int s = 0;
        while (s < suits.length) {
            Suit currentSuit = suits[s];
            List<Card> suitCards = new ArrayList<>();
            
            // Filtramos cartas del palo actual
            int j = 0;
            while (j < remaining.size()) {
                if (remaining.get(j).getSuit() == currentSuit) {
                    suitCards.add(remaining.get(j));
                }
                j++;
            }
            
            // Ordenamos por valor nominal para buscar la escalera
            suitCards.sort(Comparator.comparingInt(c -> c.getRank().getValue()));
            
            // Buscamos secuencias de 3 o más cartas consecutivas
            removeStraightSequences(remaining, suitCards);
            s++;
        }
        
        return remaining;
    }

    /**
     * Método auxiliar que identifica y elimina secuencias consecutivas (escaleras) de una lista de cartas.
     * @param totalRemaining Lista global de cartas restantes donde se aplicará la eliminación.
     * @param suitCards Lista de cartas de un mismo palo previamente filtradas y ordenadas.
     */
    private void removeStraightSequences(List<Card> totalRemaining, List<Card> suitCards) {
        if (suitCards.size() < 3) return;

        int i = 0;
        while (i <= suitCards.size() - 3) {
            List<Card> potential = new ArrayList<>();
            potential.add(suitCards.get(i));
            
            int k = i + 1;
            boolean sequenceBroken = false;
            while (k < suitCards.size() && !sequenceBroken) {
                // Comprobamos si es el siguiente número consecutivo
                if (suitCards.get(k).getRank().getValue() == potential.get(potential.size() - 1).getRank().getValue() + 1) {
                    potential.add(suitCards.get(k));
                } else {
                    sequenceBroken = true;
                }
                k++;
            }

            // Si la secuencia es de 3 o más cartas, se considera escalera y se elimina
            if (potential.size() >= 3) {
                totalRemaining.removeAll(potential);
                i += potential.size(); // Saltamos las cartas ya usadas en la escalera
            } else {
                i++;
            }
        }
    }

    /**
     * Determina si la mano actual cumple con las condiciones reglamentarias para cerrar la ronda.
     * Las condiciones son: tener todas las cartas combinadas o que la única carta suelta 
     * tenga un valor igual o inferior a 5 puntos.
     * @param hand La mano del jugador a evaluar.
     * @return true si el jugador puede cerrar la ronda, false en caso contrario.
     */
    public boolean canClose(List<Card> hand) {
        List<Card> unmatched = findUnmatchedCards(hand);
        return (unmatched.isEmpty()) || (unmatched.size() == 1 && unmatched.get(0).getPoints() <= 5);
    }

    /**
     * Calcula la suma total de puntos de las cartas que no forman ninguna combinación.
     * * @param hand La mano del jugador.
     * @return La suma de los valores nominales de las cartas no combinadas.
     */
    public int calculatePoints(List<Card> hand) {
        List<Card> unmatched = findUnmatchedCards(hand);
        int total = 0;
        for (Card c : unmatched) total += c.getPoints();
        return total;
    }

    /**
     * Verifica si el jugador ha conseguido "Chinchón".
     * Se considera Chinchón cuando las 7 cartas de la mano forman una única escalera consecutiva.
     * @param hand La mano del jugador.
     * @return true si se ha conseguido Chinchón, false en caso contrario.
     */
    public boolean isChinchon(List<Card> hand) {
        // Un chinchón es una escalera de 7 cartas
        List<Card> remaining = new ArrayList<>(hand);
        // Si tras buscar combinaciones la mano queda vacía, es xq las 7 cartas estaban combinadas
        return remaining.isEmpty(); 
    }
}