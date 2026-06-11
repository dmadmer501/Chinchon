package core;

import models.Card;
import models.Rank;
import models.Suit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Clase de prueba manual sin dependencias externas (JUnit).
 */
public class HandAnalyzerTestManual {

    public static void main(String[] args) {
        HandAnalyzerTestManual tester = new HandAnalyzerTestManual();
        
        System.out.println("=== INICIANDO PRUEBAS MANUALES ===");
        
        tester.testTrioDeSotas();
        tester.testEscaleraValida();
        
        System.out.println("=== PRUEBAS FINALIZADAS CON ÉXITO ===");
    }

    void testTrioDeSotas() {
        HandAnalyzer analyzer = new HandAnalyzer();
        
        // Configuración de la mano: 3 Sotas y un 2 de Oros
        List<Card> hand = new ArrayList<>(Arrays.asList(
            new Card(Suit.COINS, Rank.SOTA),
            new Card(Suit.CUPS, Rank.SOTA),
            new Card(Suit.SWORDS, Rank.SOTA),
            new Card(Suit.COINS, Rank.DOS)
        ));

        List<Card> unmatched = analyzer.findUnmatchedCards(hand);

        // Verificación manual
        if (unmatched.size() == 1 && unmatched.get(0).getRank() == Rank.DOS) {
            System.out.println("[OK] testTrioDeSotas: Detectado correctamente.");
        } else {
            System.err.println("[ERROR] testTrioDeSotas: Se esperaba 1 carta suelta (el 2).");
            System.exit(1); // Detiene la ejecución si falla
        }
    }

    void testEscaleraValida() {
        HandAnalyzer analyzer = new HandAnalyzer();
        
        // Escalera 1, 2, 3 de Espadas + Rey de Bastos
        List<Card> hand = new ArrayList<>(Arrays.asList(
            new Card(Suit.SWORDS, Rank.UNO),
            new Card(Suit.SWORDS, Rank.DOS),
            new Card(Suit.SWORDS, Rank.TRES),
            new Card(Suit.CLUBS, Rank.REY)
        ));

        List<Card> unmatched = analyzer.findUnmatchedCards(hand);

        if (unmatched.size() == 1 && unmatched.get(0).getRank() == Rank.REY) {
            System.out.println("[OK] testEscaleraValida: Detectada correctamente.");
        } else {
            System.err.println("[ERROR] testEscaleraValida: Fallo en la detección de escalera.");
            System.exit(1);
        }
    }
}