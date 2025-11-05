/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package domain;


// Entidad simple que representa el resultado de un proceso ejecutado.

public class ProcessResult {
    private final long pid;
    private final String command;
    private final int exitCode;
    private final String status;
    private final long durationMs;

    public ProcessResult(long pid, String command, int exitCode, String status, long durationMs) {
        this.pid = pid;
        this.command = command;
        this.exitCode = exitCode;
        this.status = status;
        this.durationMs = durationMs;
    }

    public long getPid() { return pid; }
    public String getCommand() { return command; }
    public int getExitCode() { return exitCode; }
    public String getStatus() { return status; }
    public long getDurationMs() { return durationMs; }

    @Override
    public String toString() {
        return String.format("PID=%d | CMD=\"%s\" | EXIT=%d | STATUS=%s | DUR=%dms",
                pid, command, exitCode, status, durationMs);
    }
}

