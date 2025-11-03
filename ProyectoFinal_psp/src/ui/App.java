/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ui;

import infra.EnvManager;
import infra.Platform;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException{
        try {
            // TODO code application logic here
            EnvManager em = new EnvManager();
            em.set("MIVAR", "hola_mundo");
            
            // Comando para imprimir variable (Linux/mac): bash -lc "echo $MIVAR"
            List<String> cmd = Platform.wrapForShell("echo %MIVAR%");
            ProcessBuilder pb = new ProcessBuilder(cmd);
            pb.environment().putAll(em.getMap()); // <-- aquí pasamos el entorno modificado
            Process p = pb.start();
            
            try (BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    System.out.println("Salida proceso: " + line);
                }
            } catch (IOException ex) {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            }

            p.waitFor();
            
        } catch (InterruptedException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
