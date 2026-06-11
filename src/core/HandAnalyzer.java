package core;

import models.Card;
import models.Rank;
import models.Suit;
import java.util.*;

public class HandAnalyzer {

    public List<Card> findUnmatchedCards(List<Card> hand) {
        List<Card> remaining = new ArrayList<>(hand);

        // 1. ELIMINAR GRUPOS
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
            if (group.size() >= 3) {
                remaining.removeAll(group);
            }
            r++;
        }

        // 2. ELIMINAR ESCALERAS
        Suit[] suits = Suit.values();
        int s = 0;
        while (s < suits.length) {
            Suit currentSuit = suits[s];
            List<Card> suitCards = new ArrayList<>();
            int j = 0;
            while (j < remaining.size()) {
                if (remaining.get(j).getSuit() == currentSuit) {
                    suitCards.add(remaining.get(j));
                }
                j++;
            }
            suitCards.sort(Comparator.comparingInt(c -> c.getRank().getValue()));
            removeStraightSequences(remaining, suitCards);
            s++;
        }
        return remaining;
    }

    private void removeStraightSequences(List<Card> totalRemaining, List<Card> suitCards) {
        if (suitCards.size() < 3) return;
        int i = 0;
        while (i <= suitCards.size() - 3) {
            List<Card> potential = new ArrayList<>();
            potential.add(suitCards.get(i));
            int k = i + 1;
            boolean sequenceBroken = false;
            while (k < suitCards.size() && !sequenceBroken) {
                if (suitCards.get(k).getRank().getValue() == potential.get(potential.size() - 1).getRank().getValue() + 1) {
                    potential.add(suitCards.get(k));
                } else {
                    sequenceBroken = true;
                }
                k++;
            }
            if (potential.size() >= 3) {
                totalRemaining.removeAll(potential);
                i += potential.size();
            } else {
                i++;
            }
        }
    }

    public boolean canClose(List<Card> hand) {
        List<Card> unmatched = findUnmatchedCards(hand);
        return (unmatched.isEmpty()) || (unmatched.size() == 1 && unmatched.get(0).getPoints() <= 5);
    }

    public int calculatePoints(List<Card> hand) {
        List<Card> unmatched = findUnmatchedCards(hand);
        int total = 0;
        for (Card c : unmatched) total += c.getPoints();
        return total;
    }

    /**
     * Un chinchón ocurre si las 7 cartas de la mano
     * están completamente combinadas en una única escalera o grupo continuo.
     */
    public boolean isChinchon(List<Card> hand) {
        return hand.size() == 7 && findUnmatchedCards(hand).isEmpty();
    }
}