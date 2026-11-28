package com.example.farmatec.model;

public class SesionManager {
    private static Sesion sesionActual;

    public static Sesion getSesionActual() {
        return sesionActual;
    }

    public static void setSesionActual(Sesion sesion) {
        sesionActual = sesion;
    }

    public static void limpiar() {
        sesionActual = null;
    }
}