/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

/**
 *
 * @author Manolo
 */
public class Time {
   
    /**
     * Devuelve la fecha y hora actual formateada (ej: 2025-11-04 18:23:00)
     */
    public static String now() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    /**
     * Devuelve una marca temporal corta (para nombres de archivos).
     * Ejemplo: 20251104_182300
     */
    public static String timestamp() {
        return new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    }

    /**
     * Calcula la duración en milisegundos entre dos instantes.
     */
    public static long duration(Instant start, Instant end) {
        return Duration.between(start, end).toMillis();
    }
}
