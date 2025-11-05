package infra;

import domain.Job;
import java.util.*;


public class ProcessRegistry {
    // Lista interna de procesos en background
    private final List<Job> jobs = new ArrayList<>();

    // Añadir un nuevo proceso a la lista
    public void addJob(Job job) {
        jobs.add(job);
    }

    // Mostrar todos los procesos actuales
    public String listJobs() {
        if (jobs.isEmpty()) {
            return "No hay procesos en background.";
        }

        StringBuilder sb = new StringBuilder("PID\tESTADO\tCMD\n");
        for (Job j : jobs) {
            sb.append(j.getPid()).append("\t")
              .append(j.isAlive() ? "VIVO" : "MUERTO").append("\t")
              .append(j.getCommand()).append("\n");
        }
        return sb.toString();
    }

    // Matar un proceso concreto por PID
    public String kill(long pid) {
    try {
       
        Job encontrado = null;
        for (Job j : jobs) {
            if (j.getPid() == pid) {
                encontrado = j;
                break; 
            }
        }

        // Si no se encontró, devolvemos mensaje informativo
        if (encontrado == null) {
            return "PID no encontrado.";
        }

        encontrado.kill();
        return "Proceso " + pid + " terminado correctamente.";

    } catch (Exception e) {
        // Si algo va mal (por ejemplo, el proceso ya no existe)
        return "Error al intentar terminar el proceso: " + e.getMessage();
    }
}


    // Comprobar si quedan procesos activos antes de salir
   public String exitCheck() {
    try {
        // Creamos un acumulador para el texto que mostraremos al usuario
        StringBuilder sb = new StringBuilder();

        boolean hayVivos = false; // indicador de si encontramos procesos vivos

        for (Job j : jobs) {
            if (j.isAlive()) {  // comprobamos si el proceso sigue activo
                if (!hayVivos) {
                    sb.append("Procesos activos detectados:\n");
                    hayVivos = true;
                }
                sb.append("PID=").append(j.getPid())
                  .append(" CMD=").append(j.getCommand())
                  .append("\n");
            }
        }

        if (!hayVivos) {
            // Si no había procesos vivos, devolvemos cadena vacía
            return "";
        }

        // Si sí había, agregamos una advertencia final
        sb.append("Termina los procesos antes de salir.\n");
        return sb.toString();

    } catch (Exception e) {
        // Si algo sale mal, devolvemos un mensaje de error
        return "Error al comprobar procesos activos: " + e.getMessage();
    }
}
}
