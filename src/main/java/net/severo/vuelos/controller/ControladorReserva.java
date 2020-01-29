/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.severo.vuelos.controller;

import net.severo.vuelos.dao.DAOException;
import net.severo.vuelos.estructura.OrdenPago;
import net.severo.vuelos.estructura.Pasajero;
import net.severo.vuelos.estructura.Reserva;
import net.severo.vuelos.estructura.Vuelo;
import net.severo.vuelos.servicio.ServicioReserva;
import net.severo.vuelos.servicio.ServicioVuelo;
import net.severo.vuelos.servicio.ServiciosException;
import net.severo.vuelos.views.VistaPasajero;
import net.severo.vuelos.views.VistaReservas;
import net.severo.vuelos.views.VistaVuelo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Usuario
 */
public class ControladorReserva {

    VistaReservas vr = null;

    public ControladorReserva() {
        vr = new VistaReservas();
    }

    public void iniciarReservas() {
        do {
            int opcion = vr.menuPrincipal();
            if (opcion == 0) {
                return;
            }

            switch (opcion) {
                case 1:
                    this.nuevaReserva();
                    break;
                case 2:
                    this.mostrarReservas();
                    break;
                case 3: {
                    try {
                        new VistaVuelo().mostrarVuelos(ServicioVuelo.getServicio().obtenerVuelos());
                        this.mostrarReservas(vr.pedirCodigoVuelo());
                    } catch (ServiciosException ex) {
                        vr.mostrarError("Error al intentar obtener los vuelos: " + ex.getMessage());
                    } catch (DAOException ex) {
                        vr.mostrarError("Error al intentar obtener los datos: " + ex.getMessage());
                    }
                }
                break;

                case 4:
                    this.modificarReserva();
                    break;

                case 5:
                    this.generarTarjetasEmbarque();
                    break;
                case 6:
                    this.cancelarReserva();
                    break;

            }
        } while (true);
    }

    public void nuevaReserva() {
        try {
            ServicioReserva.getServicio().iniciarTransaccion();
        } catch (DAOException e) {
            e.printStackTrace();
        }

        //Id de la reserva
        Integer id = vr.pedirIdReserva();
        if (id == null) {
            return;
        }
        //Fecha de la reserva
        Date fecha = new Date();

        //Pasajeros del vuelo
        ArrayList<Pasajero> pasajerosAlCompleto = new ArrayList<>();
        try {
            pasajerosAlCompleto = ServicioReserva.getServicio().obtenerTodosLosPasajeros();
        } catch (DAOException ex) {
            vr.mostrarError("Error al intentar obtener los datos: " + ex.getMessage());
            return;
        } catch (ServiciosException ex) {
            vr.mostrarError("Error al intentar obtener los pasajeros:" + ex);
            return;
        }
        List<Pasajero> pasajeros = vr.pedirPasajeros(pasajerosAlCompleto);
        if (pasajeros == null) {
            return;
        }
        //Pedir Vuelo
        Vuelo v = null;
        do {
            try {
                new VistaVuelo().mostrarVuelos(ServicioVuelo.getServicio().obtenerVuelos());
            } catch (ServiciosException ex) {
                vr.mostrarError("Error al intentar obtener los vuelos: " + ex.getMessage());
                return;
            } catch (DAOException ex) {
                vr.mostrarError("Error al intentar obtener los datos: " + ex.getMessage());
                return;
            }

            String idVuelo = vr.pedirCodigoVuelo();
            if (idVuelo == null) {
                return;
            }

            try {
                v = ServicioVuelo.getServicio().obtenerVuelo(idVuelo);
                //Comprobación de que hay asientos libres para los pasajeros de la reservaServicioReserva.getServicio().hayAsientosDelVueloLibres(v, pasajeros);
                ServicioReserva.getServicio().hayAsientosDelVueloLibres(v, pasajeros);
            } catch (ServiciosException ex) {
                vr.mostrarError("Error al intentar obtener el vuelo especificado: " + ex.getMessage());
                v = null;
            } catch (DAOException ex) {
                vr.mostrarError("Error al intentar obtener los datos: " + ex.getMessage());
                return;
            }
        } while (v == null);

        Integer idPago = vr.pedirIdOrdenPago();
        if (idPago == null) {
            return;
        }
        String dniPago = vr.pedirDniOrdenDePago();
        if (dniPago == null) {
            return;
        }

        String tarjetaPago = vr.pedirTarjetaOrdenPago();
        if (tarjetaPago == null) {
            return;
        }

        Reserva r = new Reserva(id, fecha, pasajeros, v);
        OrdenPago orden = new OrdenPago(idPago, r.getFecha(), dniPago, tarjetaPago, r.getImporte(), r);


        try {
            ServicioReserva.getServicio().nuevaReserva(r);
        } catch (DAOException ex) {
            vr.mostrarError("Error al intentar acceder a los datos: " + ex.getMessage());

        } catch (ServiciosException ex) {
            vr.mostrarError("Error al intentar crear la reserva: " + ex.getMessage());
        }

        try {
            ServicioReserva.getServicio().nuevaOrdenPago(orden);
        } catch (DAOException ex) {
            vr.mostrarError("Error al intentar acceder a los datos: " + ex.getMessage());
            //this.eliminarReserva(r);
        } catch (ServiciosException ex) {
            vr.mostrarError("Error al intentar crear la reserva: " + ex.getMessage());
            //this.eliminarReserva(r);
        }

        try {
            ServicioReserva.getServicio().finalizarTransaccion();
        } catch (DAOException e) {
            e.printStackTrace();
        }


    }

    public void mostrarReservas() {
        try {
            vr.mostrarReservas(ServicioReserva.getServicio().obtenerReservas());
        } catch (DAOException ex) {
            vr.mostrarError("Error al intentar obtener los datos: " + ex);
        } catch (ServiciosException ex) {
            vr.mostrarError("Error al intentar mostrar las reservas: " + ex);
        }
    }

    public void mostrarReservas(String codigoVuelo) {
        try {
            vr.mostrarReservas(ServicioReserva.getServicio().obtenerReservas(codigoVuelo));
        } catch (DAOException ex) {
            vr.mostrarError("Error al intentar obtener los datos: " + ex);
        } catch (ServiciosException ex) {
            vr.mostrarError("Error al intentar mostrar las reservas: " + ex);
        }
    }

    private void modificarReserva() {
        try {
            ServicioReserva.getServicio().iniciarTransaccion();
        } catch (DAOException e) {
            e.printStackTrace();
        }
        //codigo vuelo
        //Añadir y quitar pasajeros

        try {
            //Comprobacion de que la reserva existe y que ningún pasajero tiene una tarjeta de embarque creada
            vr.mostrarReservas(ServicioReserva.getServicio().obtenerReservas());
            Integer idReserva = vr.pedirIdReserva();
            if (idReserva == null) {
                return;
            }
            Reserva r = ServicioReserva.getServicio().obtenerReserva(idReserva);
            double importeInicial = r.getImporte();
            ServicioReserva.getServicio().tieneTarjetaDeEmbarque(r);
            ServicioReserva.getServicio().comprobacionReservaCancelada(r);
            int opcion = vr.menuModificar();
            if (opcion == 0) {
                return;
            }
            switch (opcion) {
                case 1:
                    new VistaVuelo().mostrarVuelos(ServicioVuelo.getServicio().obtenerVuelos());
                    String codigo = vr.pedirCodigoVuelo();
                    if (codigo == null) {
                        return;
                    }
                    Vuelo v = ServicioVuelo.getServicio().obtenerVuelo(codigo);
                    //Comprobación de que hay asientos libres para los pasajeros de la reserva
                    ServicioReserva.getServicio().hayAsientosDelVueloLibres(v, r.getPasajeros());

                    Integer idPago = vr.pedirIdOrdenPago();
                    if (idPago == null) {
                        return;
                    }

                    String dniPago = vr.pedirDniOrdenDePago();
                    if (dniPago == null) {
                        return;
                    }

                    String tarjetaPago = vr.pedirTarjetaOrdenPago();
                    if (tarjetaPago == null) {
                        return;
                    }
                    ServicioReserva.getServicio().modificarReservaCodigoVuelo(r, ServicioVuelo.getServicio().obtenerVuelo(codigo));
                    double importeOrdenPago = ServicioReserva.getServicio().obtenerReserva(r.getId()).getImporte() - importeInicial;
                    try {
                        ServicioReserva.getServicio().nuevaOrdenPago(new OrdenPago(idPago, r.getFecha(), dniPago, tarjetaPago, importeOrdenPago, r));
                    } catch (DAOException ex) {
                        vr.mostrarError("Error al intentar obtener los datos,la reserva no será modificada: " + ex);
                        ServicioReserva.getServicio().modificarReservaCodigoVuelo(r, r.getVuelo());
                    } catch (ServiciosException ex) {
                        vr.mostrarError("Error al generar la orden de pago,la reserva no será modificada: " + ex);
                        ServicioReserva.getServicio().modificarReservaCodigoVuelo(r, r.getVuelo());
                    }
                    break;
                case 2:
                    VistaPasajero vp = new VistaPasajero();
                    Pasajero pasajero = vp.crearPasajero();
                    if (pasajero == null) {
                        return;
                    }
                    ServicioReserva.getServicio().pasajeroExiste(pasajero);

                    idPago = vr.pedirIdOrdenPago();
                    if (idPago == null) {
                        return;
                    }

                    dniPago = vr.pedirDniOrdenDePago();
                    if (dniPago == null) {
                        return;
                    }

                    tarjetaPago = vr.pedirTarjetaOrdenPago();
                    if (tarjetaPago == null) {
                        return;
                    }
                    ServicioReserva.getServicio().modificarReservaAnyadirPasajero(r, pasajero);
                    importeOrdenPago = ServicioReserva.getServicio().obtenerReserva(r.getId()).getImporte() - importeInicial;
                    try {
                        ServicioReserva.getServicio().nuevaOrdenPago(new OrdenPago(idPago, r.getFecha(), dniPago, tarjetaPago, importeOrdenPago, r));
                    } catch (DAOException ex) {
                        vr.mostrarError("Error al intentar obtener los datos,la reserva no será modificada: " + ex);
                        ServicioReserva.getServicio().modificarReservaBorrarPasajero(r, pasajero);
                    } catch (ServiciosException ex) {
                        vr.mostrarError("Error al generar la orden de pago,la reserva no será modificada: " + ex);
                        ServicioReserva.getServicio().modificarReservaBorrarPasajero(r, pasajero);
                    }

                    break;
                case 3:
                    ServicioReserva.getServicio().comprobarReservaVacia(r);
                    VistaPasajero vistaPasajero = new VistaPasajero();
                    vr.mostrarIdPasajeros(r.getPasajeros());
                    Integer id = vistaPasajero.pedirIdPasajero();
                    if (id == null) {
                        return;
                    }

                    idPago = vr.pedirIdOrdenPago();
                    if (idPago == null) {
                        return;
                    }

                    dniPago = vr.pedirDniOrdenDePago();
                    if (dniPago == null) {
                        return;
                    }

                    tarjetaPago = vr.pedirTarjetaOrdenPago();
                    if (tarjetaPago == null) {
                        return;
                    }
                    ServicioReserva.getServicio().modificarReservaBorrarPasajero(r, ServicioReserva.getServicio().obtenerPasajero(r, id));
                    importeOrdenPago = ServicioReserva.getServicio().obtenerReserva(r.getId()).getImporte() - importeInicial;
                    try {
                        ServicioReserva.getServicio().nuevaOrdenPago(new OrdenPago(idPago, r.getFecha(), dniPago, tarjetaPago, importeOrdenPago, r));
                    } catch (DAOException ex) {
                        vr.mostrarError("Error al intentar obtener los datos,la reserva no será modificada: " + ex);
                        ServicioReserva.getServicio().modificarReservaAnyadirPasajero(r, ServicioReserva.getServicio().obtenerPasajero(r, id));
                    } catch (ServiciosException ex) {
                        vr.mostrarError("Error al generar la orden de pago,la reserva no será modificada: " + ex);
                        ServicioReserva.getServicio().modificarReservaAnyadirPasajero(r, ServicioReserva.getServicio().obtenerPasajero(r, id));
                    }
                    break;
            }

        } catch (DAOException ex) {
            vr.mostrarError("Error al intentar obtener los datos: " + ex);
        } catch (ServiciosException ex) {
            vr.mostrarError("Error con la reserva: " + ex);
        }
        try {
            ServicioReserva.getServicio().finalizarTransaccion();
        } catch (DAOException e) {
            e.printStackTrace();
        }
    }

    public void generarTarjetasEmbarque() {
        try {
            ServicioReserva.getServicio().iniciarTransaccion();
        } catch (DAOException e) {
            e.printStackTrace();
        }
        try {
            vr.mostrarReservas(ServicioReserva.getServicio().obtenerReservas());
            Integer idReserva = vr.pedirIdReserva();
            if (idReserva == null) {
                return;
            }
            Reserva r = ServicioReserva.getServicio().obtenerReserva(idReserva);
            ServicioReserva.getServicio().generarTarjetasEmbarque(r);
        } catch (DAOException ex) {
            vr.mostrarError("Error al intentar generar las tarjetas de embarque: " + ex);
        } catch (ServiciosException ex) {
            vr.mostrarError("Error al intentar obtener los datos: " + ex);
        }
        try {
            ServicioReserva.getServicio().finalizarTransaccion();
        } catch (DAOException e) {
            e.printStackTrace();
        }
    }

    private void cancelarReserva() {
        try {
            ServicioReserva.getServicio().iniciarTransaccion();
        } catch (DAOException e) {
            e.printStackTrace();
        }
        try {
            vr.mostrarReservas(ServicioReserva.getServicio().obtenerReservas());
            Integer idReserva = vr.pedirIdReserva();
            if (idReserva == null) {
                return;
            }

            Integer idPago = vr.pedirIdOrdenPago();
            if (idPago == null) {
                return;
            }

            String dniPago = vr.pedirDniOrdenDePago();
            if (dniPago == null) {
                return;
            }

            String tarjetaPago = vr.pedirTarjetaOrdenPago();
            if (tarjetaPago == null) {
                return;
            }
            Reserva r = ServicioReserva.getServicio().obtenerReserva(idReserva);
            
            ServicioReserva.getServicio().cancelarReserva(r);
            try {
                ServicioReserva.getServicio().nuevaOrdenPago(new OrdenPago(idPago, r.getFecha(), dniPago, tarjetaPago, r.calcularCancelacion(), r));
            } catch (DAOException ex) {
                retirarCancelacion(r);
                vr.mostrarError("Error al intentar obtener los datos: " + ex);
            } catch (ServiciosException ex) {
                retirarCancelacion(r);
                vr.mostrarError("Error al intentar generar la reserva: " + ex);
            }

        } catch (DAOException ex) {
            vr.mostrarError("Error al intentar obtener los datos: " + ex);
        } catch (ServiciosException ex) {
            vr.mostrarError("Error al intentar generar la cancelación de la reserva: " + ex);
        }
        try {
            ServicioReserva.getServicio().finalizarTransaccion();
        } catch (DAOException e) {
            e.printStackTrace();
        }
    }

    private void eliminarReserva(Reserva r) {
        try {
            ServicioReserva.getServicio().iniciarTransaccion();
        } catch (DAOException e) {
            e.printStackTrace();
        }
        try {
            ServicioReserva.getServicio().eliminarReserva(r);
        } catch (DAOException ex) {
            vr.mostrarError("Error al intentar obtener los datos: " + ex);
        } catch (ServiciosException ex) {
            vr.mostrarError("Error al intentar eliminar la reserva: " + ex);
        }
        try {
            ServicioReserva.getServicio().finalizarTransaccion();
        } catch (DAOException e) {
            e.printStackTrace();
        }
    }

    private void retirarCancelacion(Reserva r) {
        try {
            ServicioReserva.getServicio().retirarCancelacion(r);
        } catch (DAOException ex) {
            vr.mostrarError("Error al intentar retirar la cancelación de la reserva "+ex);
        } catch (ServiciosException ex) {
            vr.mostrarError("Error al intentar retirar la cancelación de la reserva "+ex);
        }
        try {
            ServicioReserva.getServicio().finalizarTransaccion();
        } catch (DAOException e) {
            e.printStackTrace();
        }
    }

}
