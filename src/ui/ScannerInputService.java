package ui;

import java.util.Scanner;

/**
 * Implementación concreta de {@link InputService} utilizando la clase {@link Scanner} de Java.
 */
public class ScannerInputService implements InputService {
    
    /** * Instancia única de Scanner para gestionar System.in de forma centralizada. 
     */
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Lee una cadena de texto completa introducida por el usuario.
     * @return La línea de texto capturada desde la terminal.
     */
    @Override
    public String readLine() {
        return scanner.nextLine();
    }

    /**
     * Lee un valor entero desde la terminal.
     * @return El número entero validado introducido por el usuario.
     * @throws NumberFormatException Si la entrada no es convertible a entero (manejada internamente).
     */
    @Override
    public int readInt() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("numero invalido, prueba otra vez: ");
            return readInt(); // Reintento recursivo
        }
    }
}