/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package infra;
import domain.ProcessResult;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Persistence {
    private final String path = "historial/historial_interprete.log"; // ryta para el log del historial  de resultados en la consola

    public void writeHistory(ProcessResult r) {
        try {
            File dir = new File("historial");
            if (!dir.exists()) dir.mkdirs();
            try (FileWriter fw = new FileWriter(path, true)) {
                fw.write(String.format("%s | PID=%d | CMD=\"%s\" | ESTADO=%s | EXIT=%d | DUR=%dms%n",
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
                        r.pid(), r.cmd(), r.estado(), r.exitCode(), r.duracion()));
            }
        } catch (IOException ignored) {}
    }

    public String getHistoryPath() { return path; }
}

