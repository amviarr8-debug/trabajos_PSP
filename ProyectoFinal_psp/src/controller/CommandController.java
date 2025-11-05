package controller;

import infra.*;
import domain.*;
import java.util.*;

/**
 * Interpreta los comandos escritos por el usuario y coordina las acciones de
 * las clases del paquete infra.
 */
public class CommandController {

    private final ProcessRunner processRunner = new ProcessRunner();
    private final Persistence persistence = new Persistence();
    private final Logger logger = new Logger();
    private final EnvManager evn = new EnvManager();

    // Tiempo por defecto (5 segundos)
    private long defaultTimeoutMs = 5000;

    public String execute(String input) throws Exception {
        input = input.trim();
        if (input.isEmpty()) {
            return "";
        }

        // Comandos principales
        if (input.startsWith("runbg")) {return runBackground(input.substring(6).trim()); }
        if (input.startsWith("run")) {return runForeground(input.substring(4).trim());}
        if (input.startsWith("timeout")) {return handleTimeout(input);}
        if (input.startsWith("getenv")) {return evn.getAll();}
        if (input.startsWith("setenv")) {return setEnv(input);}
        if (input.equals("history")) {return "Historial en: " + persistence.getPath();}
        if (input.equals("logs")) {return "Logs en: " + logger.getPath();}
        
        if (input.equals("exit")) {return "EXIT";}

        return "Comando no reconocido.";
    }

    // ------------------------------------------------------------------------
    private String runForeground(String args) throws Exception {
        ProcessResult result = processRunner.runForeground(args, defaultTimeoutMs);
        persistence.write(result);
        logger.log("RUN PID=" + result.getPid() + " CMD=" + result.getCommand());
        return String.format("Exit=%d  Status=%s  Dur=%dms",
                result.getExitCode(), result.getStatus(), result.getDurationMs());
    }

    private String runBackground(String args) throws Exception {
        Job job = processRunner.runBackground(args);
        persistence.write(new ProcessResult(job.getPid(), job.getCommand(), 0, "RUNNING", 0));
        logger.log("BG PID=" + job.getPid() + " CMD=" + job.getCommand());
        return "BG PID=" + job.getPid() + " OUT=" + job.getOutFile() + " ERR=" + job.getErrFile();
    }

    private String handleTimeout(String line) {
        String[] parts = line.split("\\s+");
        if (parts.length == 1) {
            return "Timeout por defecto: " + (defaultTimeoutMs / 1000) + "s";
        }
        defaultTimeoutMs = Long.parseLong(parts[1]) * 1000;
        processRunner.setDefaultTimeout(defaultTimeoutMs);
        return "Nuevo timeout: " + (defaultTimeoutMs / 1000) + "s";
    }

    private String setEnv(String line) {
        // formato esperado: setenv <clave> <valor>
        String[] parts = line.split("\\s+", 3); // divide en 3 trozos como máximo
        if (parts.length < 3) {
            return "Uso: setenv <clave> <valor>";
        }
        String key = parts[1];
        String value = parts[2];
        evn.set(key, value);
        return "Variable " + key + "=" + value + " guardada.";
    }
}
