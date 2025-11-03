/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package domain;

public class ProcessResult {
    private final long pid;
    private final String cmd;
    private final String estado;
    private final int exitCode;
    private final long duracion;

    public ProcessResult(long pid, String cmd, String estado, int exitCode, long duracion) {
        this.pid = pid; this.cmd = cmd; this.estado = estado; this.exitCode = exitCode; this.duracion = duracion;
    }

    public long pid() { return pid; }
    public String cmd() { return cmd; }
    public String estado() { return estado; }
    public int exitCode() { return exitCode; }
    public long duracion() { return duracion; }

    @Override
    public String toString() {
        return String.format("PID=%d CMD=\"%s\" EXIT=%d ESTADO=%s DUR=%dms", pid, cmd, exitCode, estado, duracion);
    }
}

