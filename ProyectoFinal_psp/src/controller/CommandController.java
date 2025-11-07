package controller;

import infra.*;
import domain.*;


/*
  Interpreta los comandos escritos por el usuario y coordina las acciones de
  las clases del paquete infra.
 */
public class CommandController {

    private final ProcessRunner processRunner = new ProcessRunner();
    private final Persistence persistence = new Persistence();
    private final Logger logger = new Logger();
    private final EnvManager evn = new EnvManager();
    private final ProcessRegistry registry = new ProcessRegistry();

    // Tiempo por defecto (5 segundos)
    private long defaultTimeoutMs = 60000;

    public String execute(String input) throws Exception {
        input = input.trim();
        if (input.isEmpty()) {
            return "";
        }

        // Comandos principales
        if (input.startsWith("runbg")) {return runBackground(input.substring(6).trim()); }
        if (input.startsWith("ejecuta")) {return handleEjecuta(input);}
        if (input.startsWith("run")) {return runForeground(input.substring(4).trim());}
        if (input.startsWith("jobs")) {return registry.listJobs();}
        if (input.startsWith("timeout")) {return handleTimeout(input);}
        if (input.startsWith("getenv")) {return evn.getAll();}
        if (input.startsWith("setenv")) {return setEnv(input);}
        if (input.equals("history")) {return "Historial en: " + persistence.getPath();}
        if (input.equals("directory")) {return processRunner.getCurrentDirectory();}
        if (input.equals("setdir")) {return processRunner.setCurrentDirectory(input.substring(6).trim());}
        if (input.equals("logs")) {return "Logs en: " + logger.getPath();}
        if (input.startsWith("kill")) return kill(input);
        if (input.equals("exit")) {return handleExit();}
        if (input.equals("help")){ return "Lista de comandos: run, runbg, ejecuta, jobs, kill, getenv, setenv, timeout, history, directory, setdir, logs, kill, exit ";}
        return "Comando no reconocido. Introduce help.";
    }

    
    private String runForeground(String args) throws Exception {
        ProcessResult result = processRunner.runForeground(args, defaultTimeoutMs);
        persistence.write(result);
        logger.log("RUN PID=" + result.getPid() + " CMD=" + result.getCommand());
        return result.toString();
    }

    private String runBackground(String args) throws Exception {
        Job job = processRunner.runBackground(args);
        registry.addJob(job);
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
    
    private String handleExit() {
    String report = registry.exitCheck();
    if (!report.isEmpty()) {
        return report; // hay procesos vivos, mostrar advertencia
    }
    return "EXIT"; // seguro salir
}
    
    private String kill(String line) {
    String[] parts = line.split("\\s+");
    if (parts.length < 2) return "Uso: kill <pid>";

    try {
        long pid = Long.parseLong(parts[1]);
        return registry.kill(pid);
    } catch (NumberFormatException e) {
        return "PID inválido: " + parts[1];
    }
}
    private String handleEjecuta(String input) {
    try {
                
        String commandArgs = input.substring(7).trim();
        ProcessResult result = processRunner.runEjecuta(commandArgs);
        persistence.write(result);
        return result.toString();
    } catch (Exception e) {
        return "Error al ejecutar el comando: " + e.getMessage();
    }
}
}
