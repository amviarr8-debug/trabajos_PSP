/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package domain;
import java.time.Instant;

public class Job {
    private final long pid;
    private final String cmd;
    private final Instant start;
    private final Process process;
    private final String outPath, errPath;

    public Job(long pid, String cmd, Instant start, Process process, String outPath, String errPath) {
        this.pid = pid; this.cmd = cmd; this.start = start; this.process = process; this.outPath = outPath; this.errPath = errPath;
    }

    public long pid() { return pid; }
    public String cmd() { return cmd; }
    public Instant start() { return start; }
    public String outPath() { return outPath; }
    public String errPath() { return errPath; }

    public boolean isAlive() { return process.isAlive(); }

    public void kill() {
        process.destroy();
        if (process.isAlive()) process.destroyForcibly();
    }

    public ProcessResult toResult() {
        String estado = isAlive() ? "RUNNING" : "EXIT OK";
        int exit = isAlive() ? 0 : 0; // simple placeholder; puedes mejorar al capturar exitValue si terminó
        return new ProcessResult(pid, cmd, estado, exit, 0);
    }
}

