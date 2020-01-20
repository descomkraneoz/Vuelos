/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.severo.vuelos.views;

import java.util.Scanner;

/**
 *
 * @author Usuario
 */
public class VistaPrincipal {

    public int elegirSistemaAlmacenamiento() {
        int opcion = -1; //opcio -1 indica opcion incorrecta
        while (opcion == -1) {
            System.out.println("Introduzca El tipo de sistema de almacenamiento");
            System.out.println("1.Archivo de texto");
            System.out.println("2.Archivo Binario");
            System.out.println("3.Base de datos relacional usando JDBC");
            System.out.println("4.Base de datos relacional usando Hibernate");
            /*System.out.println("3.Archivo XML usando DOM");
            System.out.println("4.Archivo XML usando SAX");
            System.out.println("5.Base de datos relacional usando JDBC");
            System.out.println("6.Base de datos relacional usando Hibernate");*/
            System.out.println("0.Salir");
            Scanner sc = new Scanner(System.in);
            String entrada = sc.nextLine();
            if (!esEntero(entrada)) {
                opcion = -1;
            } else {
                opcion = Integer.parseInt(entrada);
            }
            //El número debe cambiar cuando vayas implementando más DAO
            if (opcion > 4 || opcion < 0) {
                System.out.println("Opcion No valida");
                opcion = -1;

            }

        }
        return opcion;
    }
    
    public int menuPrincipal(){
        int opcion = -1; //opcio -1 indica opcion incorrecta
        while (opcion == -1) {
            System.out.println("¿Qué quiere gestionar?");
            System.out.println("1. Gestionar Vuelos");
            System.out.println("2. Gestionar reservas");
            System.out.println("3. Informes");
            System.out.println("0.Salir");
            Scanner sc = new Scanner(System.in);
            String entrada = sc.nextLine();
            if (!esEntero(entrada)) {
                opcion = -1;
            } else {
                opcion = Integer.parseInt(entrada);
            }
            //El numero aumentará cuando implementes los informes
            if (opcion > 4 || opcion < 0) {
                System.out.println("Opcion No valida");
                opcion = -1;

            }

        }
        return opcion;
    }
    
    private static boolean esEntero(String cadena) {
        try {
            Integer.parseInt(cadena);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
    
    public void mostrarError(String mensaje) {
        System.err.println(mensaje);
    }

}
