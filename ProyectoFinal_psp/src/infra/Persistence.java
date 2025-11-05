/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package infra;
import domain.ProcessResult;
import java.io.*;
import util.Time;

public class Persistence {
    
    private final File historyFile;

    public Persistence() {
        File dir = new File("historial");
        if (!dir.exists()) dir.mkdirs();  // crea la carpeta si no existe
        this.historyFile = new File(dir, "mi_interprete_historial.log");
    }

    public void write(ProcessResult result) {
        try (FileWriter fw = new FileWriter(historyFile, true)) {
            fw.write(String.format(
                "%s | PID=%d | CMD=\"%s\" | EXIT=%d | STATUS=%s | DUR=%dms%n",
                Time.now(),
                result.getPid(),
                result.getCommand(),
                result.getExitCode(),
                result.getStatus(),
                result.getDurationMs()
            ));
        } catch (IOException e) {
            System.err.println("[ERROR] No se pudo escribir en el historial: " + e.getMessage());
        }
    }
     
    public String getPath() {
        return historyFile.getAbsolutePath();
    }

}

