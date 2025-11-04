/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package infra;

import java.util.HashMap;
import java.util.Map;

public class EnvManager {
    // Copia editable de las variables de entorno del sistema
    private final Map<String, String> env = new HashMap<>(System.getenv());

    // Constructor por defecto (ya inicializa con System.getenv())

    public EnvManager() {
    }
    
    /* Crea o actualiza una variable de entorno. Este esel metodo con el que podemos modificar 
    las variables que no nos permite el System.getenv().*/
    
    public void set(String var, String value) {
        if (var == null || var.isBlank()) return;
        env.put(var, value);
    }

    // Obtiene el valor de una variable (o null si no existe)
    public String get(String var) {
        return env.get(var);
    }

    // Elimina una variable del entorno (opcional)
    public void remove(String var) {
        env.remove(var);
    }

    // Devuelve todas las variables como un String legible (KEY=VALUE\n)
    public String getAll() {
        StringBuilder sb = new StringBuilder();
        env.forEach((k, v) -> sb.append(k).append("=").append(v).append("\n")); // generamos un foreach para devolver los valores 
        return sb.toString();
    }

    // Devuelve el mapa para pasarlo a ProcessBuilder.environment().putAll(...)
    public Map<String, String> getMap() {
        return env;
    }
}


