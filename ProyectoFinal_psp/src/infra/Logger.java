package infra;

import util.Time;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


//  Clase encargada de registrar salidas de sesión o mensajes de procesos en background.
//  Guarda los logs en la carpeta "logs/" con nombres únicos.
 
public class Logger {

    private final File logFile;

    public Logger() {
        File dir = new File("logs");
        if (!dir.exists()) dir.mkdirs();  // crea carpeta logs si no existe

        // Nombre único para cada sesión: log_YYYYMMDD_HHMMSS.log
        String fileName = "log_" + Time.timestamp() + ".log";
        this.logFile = new File(dir, fileName);
    }

    // Escribe un mensaje de log con salto de línea.     
    public void log(String message) {
        try (FileWriter fw = new FileWriter(logFile, true)) {
            fw.write(Time.now() + " | " + message + System.lineSeparator());
        } catch (IOException e) {
            System.err.println("[ERROR] No se pudo escribir en el log: " + e.getMessage());
        }
    }
    //  Devuelve la ruta del archivo de log actual.
    public String getPath() {
        return logFile.getAbsolutePath();
    }
}
