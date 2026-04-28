package ui;

import core.*;

public class ConsoleApp {
    public static void main(String[] args) {
        InputService input = new ScannerInputService();
        
        Builder builder = new Builder(input);

        System.out.println("=== CHINCHÓN SETUP ===");
        System.out.print("Limite de puntos: ");
        builder.setMaxPoints(input.readInt());
        
        System.out.print("Numero de barajas: ");
        builder.setDeckCount(input.readInt());

        System.out.print("Numero de humanos: ");
        int h = input.readInt();
        int i = 0;
        while (i < h) { 
            System.out.print("Nombre de humano " + (i + 1) + ": "); 
            builder.addHuman(input.readLine()); 
            i++;
        }

        System.out.print("Número de IAs: ");
        int a = input.readInt();
        int j = 0;
        while (j < a) { 
            System.out.print("Nombre de IAs " + (j + 1) + ": "); 
            builder.addAI(input.readLine()); 
            j++;
        }

        GameManager engine = GameManager.getInstance();
        engine.init(builder.build());
        engine.run();
    }
}