/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.severo.vuelos.controller;

import java.util.Date;
import net.severo.vuelos.dao.DAOException;
import net.severo.vuelos.estructura.Vuelo;
import net.severo.vuelos.servicio.ServicioReserva;
import net.severo.vuelos.servicio.ServicioVuelo;
import net.severo.vuelos.servicio.ServiciosException;
import net.severo.vuelos.views.VistaInformes;
import net.severo.vuelos.views.VistaReservas;
import net.severo.vuelos.views.VistaVuelo;

/**
 *
 * @author Rubén Más Almira
 */
public class ControladorInformes {

    VistaReservas vr = null;
    VistaVuelo vv = null;
    VistaInformes vi = null;

    public ControladorInformes() {
        vr = new VistaReservas();
        vv = new VistaVuelo();
        vi = new VistaInformes();

    }

    void iniciarInformes() {
        do {
            int opcion = vi.menuPrincipal();
            if (opcion == 0) {
                return;
            }
            /*1. Vuelos entre fechas 
            2. Vuelos sin plazas libres 
            3. Reservas de un vuelo 
            4.Ordenes de pago de una reserva 
            5. Detalle de una reserva. Con todos sus pasajeros 
            6. Tarjetas de embarque de una reserva. 
            7. Dinero Ingresado por un vuelo hasta la fecha de hoy. 
            8. Número  de vuelo junto con plazas libres, número de adultos y número de niños. 
            9. Número de pasajeros de un vuelo que aun no han impreso su tarjeta de embarque. 
            10. Número de reservas canceladas de un vuelo.*/

            switch (opcion) {
                case 1:
                    this.vuelosEntreFechas();
                    break;
                case 2:
                    this.vuelosSinPlazas();
                    break;
                case 3:
                    this.reservasDeUnVuelo();
                    break;
                case 4:
                    this.ordenesDePagoDeUnaReserva();
                    break;
                case 5:
                    this.detallesReserva();
                    break;
                case 6:
                    this.tarjetasEmbarqueReserva();
                    break;
                case 7:
                    this.dineroIngresadoDeUnVuelo();
                    break;
                case 8:
                    this.numeroDeVueloPlazasLibresNinyosAdultos();
                    break;
                case 9:
                    this.numPasajerosSinTarjetaDeUnVuelo();
                    break;
                case 10:
                    this.reservasCanceladasDeUnVuelo();
                    break;

            }

        } while (true);
    }

    private void vuelosEntreFechas() {
        Date fechaComienzo = vv.pedirFecha();
        if (fechaComienzo == null) {
            return;
        }
        Date fechaFin = vv.pedirFecha();
        if (fechaFin == null) {
            return;
        }
        try {
            vv.mostrarVuelos(ServicioVuelo.getServicio().obtenerVuelos(fechaComienzo, fechaFin));
        } catch (DAOException ex) {
            vi.mostrarError("Error al intentar obtener los datos: " + ex);
        } catch (ServiciosException ex) {
            vi.mostrarError("Error al intentar mostrar los vuelos: " + ex);
        }
    }

    private void vuelosSinPlazas() {

        try {
            vv.mostrarVuelos(ServicioVuelo.getServicio().obtenerVuelosSinPlazas());
        } catch (DAOException ex) {
            vi.mostrarError("Error al intentar obtener los datos: " + ex);
        } catch (ServiciosException ex) {
            vi.mostrarError("Error al intentar mostrar los vuelos: " + ex);
        }
    }

    private void reservasDeUnVuelo() {
        try {
            vv.mostrarVuelos(ServicioVuelo.getServicio().obtenerVuelos());
            String codigoVuelo = vv.pedirCodigoVuelo();
            if (codigoVuelo == null) {
                return;
            }
            ServicioVuelo.getServicio().obtenerVuelo(codigoVuelo);
            vr.mostrarReservas(ServicioReserva.getServicio().obtenerReservas(codigoVuelo));
        } catch (DAOException ex) {
            vi.mostrarError("Error al intentar obtener los datos: " + ex);
        } catch (ServiciosException ex) {
            vi.mostrarError("Error al intentar mostrar las reservas: " + ex);
        }

    }

    private void ordenesDePagoDeUnaReserva() {
        try {
            vr.mostrarReservas(ServicioReserva.getServicio().obtenerReservas());
            Integer idReserva = vr.pedirIdReserva();
            if(idReserva==null){
                return;
            }
            ServicioReserva.getServicio().obtenerReserva(idReserva);
            vr.mostrarOrdenesPago(ServicioReserva.getServicio().obtenerOrdenesPago(idReserva));
        } catch (DAOException ex) {
            vi.mostrarError("Error al intentar obtener los datos: " + ex);
        } catch (ServiciosException ex) {
            vi.mostrarError("Error al intentar mostrar las reservas: " + ex);
        }
    }

    private void detallesReserva() {
        try {
            vr.mostrarReservas(ServicioReserva.getServicio().obtenerReservas());
            Integer idReserva = vr.pedirIdReserva();
            if(idReserva==null){
                return;
            }
            ServicioReserva.getServicio().obtenerReserva(idReserva);
            vr.mostrarReservaDetallada(ServicioReserva.getServicio().obtenerReserva(idReserva));
        } catch (DAOException ex) {
            vi.mostrarError("Error al intentar obtener los datos: " + ex);
        } catch (ServiciosException ex) {
            vi.mostrarError("Error al intentar mostrar las reservas: " + ex);
        }
    }

    private void tarjetasEmbarqueReserva() {
       try {
            vr.mostrarReservas(ServicioReserva.getServicio().obtenerReservas());
            Integer idReserva = vr.pedirIdReserva();
            if(idReserva==null){
                return;
            }
            ServicioReserva.getServicio().obtenerReserva(idReserva);
            vr.mostrarTarjetasEmbarque(ServicioReserva.getServicio().obtenerTarjetasEmbarque(idReserva));
        } catch (DAOException ex) {
            vi.mostrarError("Error al intentar obtener los datos: " + ex);
        } catch (ServiciosException ex) {
            vi.mostrarError("Error al intentar mostrar las tarjetas de embarque: " + ex);
        }
    }

    private void dineroIngresadoDeUnVuelo() {
        try {
            vv.mostrarVuelos(ServicioVuelo.getServicio().obtenerVuelos());
            String codigoVuelo = vv.pedirCodigoVuelo();
            if (codigoVuelo == null) {
                return;
            }
            ServicioVuelo.getServicio().obtenerVuelo(codigoVuelo);
            
            vi.mostrarDineroIngresado(ServicioReserva.getServicio().obtenerDineroDeOrdenesDePagoDelVuelo(codigoVuelo));
        } catch (DAOException ex) {
            vi.mostrarError("Error al intentar obtener los datos: " + ex);
        } catch (ServiciosException ex) {
            vi.mostrarError("Error al intentar mostrar el dinero ingresado" + ex);
        }
    }

    private void numeroDeVueloPlazasLibresNinyosAdultos() {
        try {
            vv.mostrarVuelos(ServicioVuelo.getServicio().obtenerVuelos());
            String codigoVuelo = vv.pedirCodigoVuelo();
            if (codigoVuelo == null) {
                return;
            }
            Vuelo v=ServicioVuelo.getServicio().obtenerVuelo(codigoVuelo);
            int numNinyos=ServicioReserva.getServicio().obtenerNumeroDeNinyosDelVuelo(codigoVuelo);
            int numAdultos=ServicioReserva.getServicio().obtenerNumeroDeAdultosDelVuelo(codigoVuelo);
            
            vi.mostrarNumPlazasNinyosYAdultos(v.getPlazasDisponibles(),numNinyos,numAdultos);
        } catch (DAOException ex) {
            vi.mostrarError("Error al intentar obtener los datos: " + ex);
        } catch (ServiciosException ex) {
            vi.mostrarError("Error al intentar mostrar el numero de plazas,niños y adultos: " + ex);
        }
    }

    private void numPasajerosSinTarjetaDeUnVuelo() {
        try {
            vv.mostrarVuelos(ServicioVuelo.getServicio().obtenerVuelos());
            String codigoVuelo = vv.pedirCodigoVuelo();
            if (codigoVuelo == null) {
                return;
            }
            ServicioVuelo.getServicio().obtenerVuelo(codigoVuelo);
            vi.mostrarNumPasajerosSinTarjeta(ServicioReserva.getServicio().obtenerNumPasajerosSinTarjeta(codigoVuelo));
        } catch (DAOException ex) {
            vr.mostrarError("Error al intentar obtener los datos: " + ex);
        } catch (ServiciosException ex) {
            vr.mostrarError("Error al intentar mostrar el número de pasajeros sin tarjeta: " + ex);
        }
    }

    private void reservasCanceladasDeUnVuelo() {
       try {
            vv.mostrarVuelos(ServicioVuelo.getServicio().obtenerVuelos());
            String codigoVuelo = vv.pedirCodigoVuelo();
            if (codigoVuelo == null) {
                return;
            }
            ServicioVuelo.getServicio().obtenerVuelo(codigoVuelo);
            
            vr.mostrarReservas(ServicioReserva.getServicio().reservasCanceladas(codigoVuelo));
        } catch (DAOException ex) {
            vr.mostrarError("Error al intentar obtener los datos: " + ex);
        } catch (ServiciosException ex) {
            vr.mostrarError("Error al intentar mostrar las reservas: " + ex);
        }
    }

}
