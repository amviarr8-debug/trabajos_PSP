/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package infra;

import java.util.List;

/**
 *
 * @author Manolo
 */
// Con esta clase detectamos cual es el SO instalado para ejecutar los comandos por consola.
public class Platform {
    public static List<String> wrapForShell(String cmdline) {
        boolean win = System.getProperty("os.name").toLowerCase().contains("win");
        if (win) return List.of("cmd", "/c", cmdline);
        else return List.of("bash", "-lc", cmdline);
    }
}
