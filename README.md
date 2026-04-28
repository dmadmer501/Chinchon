# Chinchón Java Core 🃏

Una implementación robusta, modular y extensible del clásico juego de cartas **Chinchón**, desarrollada íntegramente en Java. Este proyecto demuestra la aplicación de principios de ingeniería de software, patrones de diseño y una arquitectura orientada a objetos limpia.

## Características Principales

* **Motor de Juego (GameManager):** Control centralizado del ciclo de vida de la partida, gestión de turnos y barajado mediante el patrón Singleton.
* **Análisis Lógico de Manos:** Un sofisticado `HandAnalyzer` capaz de identificar grupos (mismo rango), escaleras (mismo palo) y calcular puntos de cartas no combinadas.
* **Sistema de Cierre:** Implementación de reglas reglamentarias para cerrar la ronda, incluyendo la bonificación de -10 puntos y la victoria por Chinchón.
* **IA Extensible:** Soporte para jugadores controlados por la computadora con lógica de decisión automática.
* **UI de Consola Enriquecida:** Interfaz visual con soporte para colores ANSI y Emojis para una experiencia de usuario moderna en terminal.

## Arquitectura y Patrones de Diseño

El proyecto ha sido diseñado siguiendo principios **SOLID** para garantizar la mantenibilidad:

| Patrón | Implementación | Propósito |
| :--- | :--- | :--- |
| **Singleton** | `GameManager` | Asegura una única instancia del motor de juego y el analizador para toda la partida. |
| **Builder** | `Builder` | Facilita la configuración paso a paso de `GameConfig` (puntos, mazos, jugadores). |
| **Polimorfismo** | `Player`, `HumanPlayer`, `AIPlayer` | Abstrae el comportamiento de los participantes, permitiendo que el motor gestione humanos e IAs indistintamente. |
| **Abstracción de Entrada** | `InputService` | Desvincula la lógica de negocio de la entrada de datos, facilitando el testing y la escalabilidad de la UI. |

## Estructura del Proyecto

El código se organiza en paquetes siguiendo una clara separación de responsabilidades:

* `core`: Contiene el núcleo del sistema, el motor de juego (`GameManager`) y la lógica de análisis de manos (`HandAnalyzer`).
* `models`: Define las entidades del dominio como `Card`, `Player` y los enums `Suit` y `Rank`.
* `ui`: Gestiona la interacción con el usuario y la entrada de datos por consola.

## Reglas del Juego Implementadas

1.  **Objetivo:** Acumular la menor cantidad de puntos posible.
2.  **Turnos:** Los jugadores pueden robar del mazo o de la pila de descartes.
3.  **Combinaciones:** Se consideran válidos los tríos/cuarteros del mismo valor o las escaleras del mismo palo.
4.  **Cierre:** Es posible cerrar la ronda si el jugador tiene todas las cartas combinadas o una sola carta suelta con un valor $\le 5$.
5.  **Chinchón:** Se logra al formar una escalera completa de 7 cartas, otorgando la victoria inmediata.

## Instalación y Uso

### Requisitos
* Java JDK 8 o superior.

### Ejecución
1.  **Clona el repositorio** o descarga los archivos fuente.
2.  **Compila el proyecto**:
    ```bash
    javac -d out src/**/*.java
    ```
3.  **Inicia el juego**:
    ```bash
    java -cp out ui.ConsoleApp
    ```

## Detalles de Calidad

* **Robustez:** El sistema incluye validaciones de entrada para evitar errores de ejecución por datos inválidos en la terminal.
* **Visualización:** El `HumanPlayer` resalta automáticamente las cartas combinadas en cian para ayudar en la toma de decisiones.
* **Flexibilidad:** Gracias al `Builder`, es sencillo modificar el límite de puntos o el número de barajas utilizadas en la sesión.

---
**Desarrollado por Daniel Madrid Mérida para 1-DAM**
