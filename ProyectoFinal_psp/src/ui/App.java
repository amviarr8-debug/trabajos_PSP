package ui;

import controller.CommandController;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        CommandController controller = new CommandController();
        Scanner sc = new Scanner(System.in);

        System.out.println("=== Mini Intérprete de Comandos ===");
        System.out.println("Escribe un comando, help o 'exit' para salir.\n");

        while (true) {
            System.out.print("> ");  // prompt del intérprete
            String line = sc.nextLine().trim();

            // si el usuario no escribe nada, continuamos
            if (line.isEmpty()) continue;

            // enviamos la línea al controlador para ejecutar el comando
            String result = controller.execute(line);

            // mostramos el resultado
            System.out.println(result);

            // si el controlador devuelve "EXIT", salimos del bucle
            if ("EXIT".equals(result)) break;
        }

        sc.close();
        System.out.println("Intérprete finalizado.");
    }
}
