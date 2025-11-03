/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package infra;
import java.io.*;

import util.Time;

public class Logging {
    public void logSession(String message) {
        try {
            String ts = Time.timestamp(); // utilizamos el modelo timestamp creado desde la clase Time para simplificar el codigo
            File dir = new File("logs");
            if (!dir.exists()) dir.mkdirs(); // si el directorio no existe lo crea 
            File f = new File(dir, "log_" + ts + ".log");
            try (FileWriter fw = new FileWriter(f, true)) {
                fw.write(message + "\n");
            }
        } catch (IOException ignored) {}
    }
}
