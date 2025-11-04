/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.util.List;

// Con esta clase detectamos cual es el SO instalado para ejecutar los comandos por consola.
public class Platform {

    public static List<String> wrapForShell(String cmdline) {
        boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");

        // Evita duplicar "cmd /c" si el usuario ya lo incluyó o bash -lc
        if (isWindows) {
            if (cmdline.trim().toLowerCase().startsWith("cmd /c")) {
                return List.of("cmd", "/c", cmdline.substring(6).trim());
            }
            return List.of("cmd", "/c", cmdline);
        } else {
            if (cmdline.trim().toLowerCase().startsWith("bash -lc")) {
                return List.of("bash", "-lc", cmdline.substring(8).trim());
            }
            return List.of("bash", "-lc", cmdline);
        }
    }
}
