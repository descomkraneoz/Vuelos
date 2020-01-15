/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.severo.vuelos.controller;

import net.severo.vuelos.dao.DAOException;
import net.severo.vuelos.servicio.ServicioReserva;
import net.severo.vuelos.servicio.ServicioVuelo;
import net.severo.vuelos.views.VistaPrincipal;

/**
 *
 * @author Usuario
 */
public class ControladorPrincipal {

    VistaPrincipal vp = null;

    public ControladorPrincipal() {
        vp = new VistaPrincipal();
    }

    public void iniciarAplicacion() {
        int sistemaAl = vp.elegirSistemaAlmacenamiento();
        if (sistemaAl == 0) {
            //salimos de la aplicacion
            return;

        } else {

            try {
                ServicioVuelo.getServicio().elegirSistemaAlmacenamiento(sistemaAl);
                ServicioReserva.getServicio().elegirSistemaAlmacenamiento(sistemaAl);
            } catch (DAOException ex) {
                vp.mostrarError("Ha habido un error al iniciar el sistema de almacenamiento " + ex.getMessage());
            }

            this.iniciarMenuPrincipal();
        }
    }

    public void iniciarMenuPrincipal() {
        do {
            int opcion = vp.menuPrincipal();
            switch (opcion) {
                case 0:
                    //Salimos
                    return;
                case 1:
                    new ControladorVuelo().iniciarVuelo();
                    break;
                case 2:
                    new ControladorReserva().iniciarReservas();
                    break;
                case 3:
                    new ControladorInformes().iniciarInformes();
                    break;
            }
        } while (true);
    }
}
