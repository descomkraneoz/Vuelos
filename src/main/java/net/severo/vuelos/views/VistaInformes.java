/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.severo.vuelos.views;

import java.util.Date;
import java.util.Scanner;

/**
 *
 * @author Rubén Más Almira
 */
public class VistaInformes {

    public int menuPrincipal() {
        
        Scanner sc = new Scanner(System.in);
        String menu = "1.Vuelos entre fechas. \n "
                + "2.Vuelos sin plazas libres.\n "
                + "3.Reservas de un vuelo. \n "
                + "4.Ordenes de pago de una reserva. \n "
                + "5.Detalles de una reserva.Con todos sus pasajeros. \n "
                + "6.Tarjetas de embarque de una reserva. \n "
                + "7.Dinero ingresado por un vuelo hasta la fecha de hoy. \n "
                + "8.Número de vuelo junto con plazas libres,número de adultos y número de niños. \n "
                + "9.Número de pasajeros de un vuelo que aun no han impreso su tarjeta de embarque. \n "
                + "10.Número de reservas canceladas de un vuelo. \n "
                + "0)Salir \n "
                + "¿Que quiere hacer?";

        int opcion = -1; //opcio -1 indica opcion incorrecta
        while (opcion == -1) {
            System.out.println(menu);
            String entrada = sc.nextLine();
            if (!esEntero(entrada)) {
                opcion = -1;
            } else {
                opcion = Integer.parseInt(entrada);
            }
            if (opcion > 10 || opcion < 0) {
                System.out.println("Opción no valida");
                opcion = -1;
            }
        }
        return opcion;
    }

    public static boolean esEntero(String numero) {
        try {
            Integer.parseInt(numero);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    public void mostrarError(String mensaje) {
        System.err.println(mensaje);
    }

    public void mostrarNumPlazasNinyosYAdultos(int plazasDisponibles, int numNinyos, int numAdultos) {
        System.out.println("Número de Plazas disponibles: "+plazasDisponibles);
        System.out.println("Número de niños: "+numNinyos);
        System.out.println("Número de adultos: "+numAdultos);
    }

    public void mostrarDineroIngresado(double cantidad) {
        System.out.println("El dinero ingresado ha sido: "+cantidad);
    }

    public void mostrarNumPasajerosSinTarjeta(int obtenerNumPasajerosSinTarjeta) {
        System.out.println("El número de pasajeros sin tarjeta es:"+obtenerNumPasajerosSinTarjeta);
    }
}
