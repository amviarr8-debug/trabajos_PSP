/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package domain;
import java.time.Duration;
import java.time.Instant;

// Entidad que representa un proceso en ejecución (background).

public class Job {
    private final long pid;
    private final String command;
    private final Instant startTime;
    private final Process process;
    private final String outFile;
    private final String errFile;

    public Job(long pid, String command, Instant startTime, Process process, String outFile, String errFile) {
        this.pid = pid;
        this.command = command;
        this.startTime = startTime;
        this.process = process;
        this.outFile = outFile;
        this.errFile = errFile;
    }

    public long getPid() { return pid; }
    public String getCommand() { return command; }
    public Instant getStartTime() { return startTime; }
    public String getOutFile() { return outFile; }
    public String getErrFile() { return errFile; }

    public boolean isAlive() {
        return process.isAlive();
    }

    public void kill() {
        process.destroy();
        if (process.isAlive()) process.destroyForcibly();
    }

//    public ProcessResult toResult() {
//      boolean vivo = isAlive();
//    String estado = vivo ? "RUNNING" : "EXIT OK";
//    int exit = vivo ? -1 : 0;
//    long dur = Duration.between(start, Instant.now()).toMillis(); // define exactamente l tiempo que el proceso lleva corriendo
//    return new ProcessResult(pid, cmd, estado, exit, dur);
//    }
//    public ProcessResult toResult() {
//    String estado = isAlive() ? "RUNNING" : "EXIT OK";
//    int exit = isAlive() ? -1 : 0;
//    return new ProcessResult(pid, cmd, estado, exit, 0);
//}
}

