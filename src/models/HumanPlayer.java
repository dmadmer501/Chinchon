package models;

import java.util.List;
import core.GameManager;
import core.HandAnalyzer;
import ui.InputService;

/**
 * Representa a un jugador humano en el juego de Chinchón.
 */
public class HumanPlayer extends Player {
   
    /** Servicio encargado de la lectura de la entrada del usuario desde la terminal. */
    private final InputService input;

    /**
     * Crea una instancia de un jugador humano.
     * @param name Nombre del jugador.
     * @param input Implementación del servicio de entrada para capturar las acciones del usuario.
     */
    public HumanPlayer(String name, InputService input) {
        super(name);
        this.input = input;
    }

    /**
     * Permite al jugador decidir la fuente de donde robará su octava carta.
     * @param topDiscard La carta que se encuentra visible en la pila de descartes.
     * @return true si el usuario elige robar del mazo (opción 1), false si elige el descarte (opción 2).
     */
    @Override
    public boolean chooseDrawSource(Card topDiscard) {
        System.out.println("\n========================================");
        System.out.println("TURNO DE: " + name.toUpperCase() + " (Puntos: " + totalPoints + ")");
        
        // UX: Ver mano antes de decidir el robo
        displayHandStatus("TU MANO ACTUAL (7 cartas)");
        
        System.out.println("\nCARTA EN DESCARTE: " + topDiscard.toString());
        System.out.print("¿Robar de (1) MAZO o (2) DESCARTE?: ");
        return !"2".equals(input.readLine());
    }

    /**
     * Gestiona la fase de descarte del jugador.
     * * @return La carta seleccionada que será enviada a la pila de descartes.
     */
    @Override
    public Card discard() {
        // Ver la mano de 8 cartas tras el robo antes de descartar
        displayHandStatus("MANO TRAS EL ROBO (8 cartas)");
        
        List<Card> currentHand = getHand();
        System.out.print("Selecciona el índice (0-7) para descartar: ");
        int idx = input.readInt();
        
        // Validación básica para evitar errorr de desbordamiento del índice
        if (idx < 0 || idx >= currentHand.size()) idx = 0;
        
        Card discarded = currentHand.remove(idx);
        System.out.println("Descartado: " + discarded.toString());
        return discarded;
    }

    /**
     * Calcula los puntos de las cartas sueltas y aplica resaltado de color a las 
     * cartas que ya forman tríos o escaleras según el HandAnalyzer.
     * @param title Título descriptivo para la sección de la interfaz (ej. "MANO TRAS EL ROBO").
     */
    private void displayHandStatus(String title) {
        List<Card> currentHand = getHand();
        HandAnalyzer analyzer = GameManager.getInstance().getAnalyzer();
        List<Card> unmatched = analyzer.findUnmatchedCards(currentHand);
        
        int currentPoints = 0;
        int p = 0;
        while (p < unmatched.size()) {
            currentPoints += unmatched.get(p).getPoints();
            p++;
        }

        System.out.println("\n--- " + title + " ---");
        System.out.println("Puntos en cartas sueltas: " + currentPoints);
        
        int i = 0;
        while (i < currentHand.size()) {
            Card card = currentHand.get(i);
            boolean isCombined = true;
            int j = 0;
            boolean found = false;
            while (j < unmatched.size()) {
                if (unmatched.get(j) == card) found = true;
                j++;
            }
            isCombined = !found;
            // Llama al método toString(boolean) de Card para aplicar color azul si isCombined es true
            System.out.println(i + ": " + card.toString(isCombined));
            i++;
        }
    }

    /**
     * Solicita confirmación al usuario humano para cerrar la ronda.
     * @return true si el usuario introduce "s" (o "S"), indicando que desea cerrar la ronda.
     */
    @Override
    public boolean wantsToClose() {
        System.out.print("\n¡TIENES COMBINACIÓN VÁLIDA! ¿Deseas cerrar la ronda? (s/n): ");
        return input.readLine().equalsIgnoreCase("s");
    }
}