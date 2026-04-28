package ui;

/**
 * Interfaz que define los métodos necesarios para abstraer la entrada de datos del usuario.
 */
public interface InputService {
    /**
     * Lee una línea de texto completa desde la fuente de entrada.
     * @return Una cadena de caracteres (String) con el texto introducido.
     */
    String readLine();

    /**
     * Lee un valor numérico entero desde la fuente de entrada.
     * @return El número entero introducido por el usuario.
     */
    int readInt();
}
