package infra;

import domain.Job;
import domain.ProcessResult;
import util.Platform;
import util.Time;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ProcessRunner {

    public long defaultTimeoutMs;
    private Path currentDirectory = Paths.get(System.getProperty("user.dir"));

    public void setDefaultTimeout(long timeoutMs) {
        this.defaultTimeoutMs = timeoutMs;
    }

    public long getDefaultTimeout() {
        return defaultTimeoutMs;
    }

    // ---------------------------------------------------------------------
    // 🔹 Método 1: Foreground (usa consola)
    // ---------------------------------------------------------------------
    public ProcessResult runForeground(String command, long timeoutMs) throws IOException, InterruptedException {

        List<String> cmd = Platform.wrapForShell(command);
        ProcessBuilder pb = new ProcessBuilder(cmd);

        pb.directory(currentDirectory.toFile());
        pb.redirectInput(ProcessBuilder.Redirect.INHERIT);
        pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        pb.redirectError(ProcessBuilder.Redirect.INHERIT);

        return executeProcess(pb, command, timeoutMs);
    }

    // ---------------------------------------------------------------------
    // 🔹 Método 2: Background
    // ---------------------------------------------------------------------
    public Job runBackground(String command) throws IOException {
        List<String> cmd = Platform.wrapForShell(command);
        String ts = Time.timestamp();

        File dir = new File("logs");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File outFile = new File(dir, "bg_out_" + ts + ".log");
        File errFile = new File(dir, "bg_err_" + ts + ".log");

        ProcessBuilder pb = new ProcessBuilder(cmd);
        pb.directory(currentDirectory.toFile());
        pb.redirectOutput(outFile);
        pb.redirectError(errFile);

        Process p = pb.start();
        return new Job(p.pid(), command, Instant.now(), p,
                outFile.getAbsolutePath(), errFile.getAbsolutePath());
    }

    // ---------------------------------------------------------------------
    // 🔹 Método 3: Ejecuta (redirige IN/OUT/ERR + timeout)
    // ---------------------------------------------------------------------
    public ProcessResult runEjecuta(String args) throws IOException, InterruptedException {

        String[] tokens = args.split("\\s+");
        String inFile = null, outFile = null, errFile = null;
        long timeout = defaultTimeoutMs;
        StringBuilder cmdBuilder = new StringBuilder();

        for (int i = 0; i < tokens.length; i++) {
            switch (tokens[i].toUpperCase()) {
                case "IN" ->
                    inFile = tokens[++i];
                case "OUT" ->
                    outFile = tokens[++i];
                case "ERR" ->
                    errFile = tokens[++i];
                case "TIMEOUT" ->
                    timeout = Long.parseLong(tokens[++i]);
                default ->
                    cmdBuilder.append(tokens[i]).append(" ");
            }
        }

        String command = cmdBuilder.toString().trim();
        List<String> cmd = Platform.wrapForShell(command);
        ProcessBuilder pb = new ProcessBuilder(cmd);

        // Redirecciones configurables
        setRedirects(pb, inFile, outFile, errFile);

        return executeProcess(pb, command, timeout);
    }

    // ---------------------------------------------------------------------
    // 🔹 MÉTODOS AUXILIARES PRIVADOS
    // ---------------------------------------------------------------------
    /**
     * Ejecuta un ProcessBuilder con timeout y devuelve el resultado.
     */
    private ProcessResult executeProcess(ProcessBuilder pb, String command, long timeoutMs) throws IOException, InterruptedException {

        Instant start = Instant.now();
        Process p = pb.start();
        pb.directory(currentDirectory.toFile());
        boolean ok = p.waitFor(timeoutMs, TimeUnit.MILLISECONDS);

        int exitCode;
        String status;
        if (!ok) {
            p.destroy();
            if (p.isAlive()) {
                p.destroyForcibly();
            }
            exitCode = -1;
            status = "TIMEOUT";
        } else {
            exitCode = p.exitValue();
            status = "EXIT OK";
        }

        long duration = Duration.between(start, Instant.now()).toMillis();
        return new ProcessResult(p.pid(), command, exitCode, status, duration);
    }

    /**
     * Aplica redirecciones IN, OUT, ERR según el valor pasado.
     */
    private void setRedirects(ProcessBuilder pb, String in, String out, String err) {
        // Entrada
        if (in == null || in.equalsIgnoreCase("null")) {
            pb.redirectInput(ProcessBuilder.Redirect.INHERIT);
        } else {
            pb.redirectInput(ProcessBuilder.Redirect.from(new File(in)));
        }

        // Salida
        if (out == null || out.equalsIgnoreCase("null")) {
            pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        } else {
            pb.redirectOutput(ProcessBuilder.Redirect.to(new File(out)));
        }

        // Error
        if (err == null || err.equalsIgnoreCase("null")) {
            pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        } else {
            pb.redirectError(ProcessBuilder.Redirect.to(new File(err)));
        }
    }

    public String getCurrentDirectory() {
        return "Directorio actual: " + Paths.get(System.getProperty("user.dir")).toAbsolutePath();
    }

    public String setCurrentDirectory(String dir) {
        Path newDir = Paths.get(dir);
        if (!Files.isDirectory(newDir)) {
            return "No existe el directorio: " + dir;
        }
        currentDirectory = newDir.toAbsolutePath();
        return "Directorio cambiado a: " + currentDirectory;
    }

}
