/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.severo.vuelos.controller;

import java.util.Date;
import net.severo.vuelos.estructura.Vuelo;
import java.util.List;
import net.severo.vuelos.dao.DAOException;
import net.severo.vuelos.estructura.Reserva;
import net.severo.vuelos.servicio.ServicioReserva;
import net.severo.vuelos.servicio.ServicioVuelo;
import net.severo.vuelos.servicio.ServiciosException;
import net.severo.vuelos.views.*;

/**
 *
 * @author Usuario
 */
public class ControladorVuelo {

    private VistaVuelo vv = null;

    public ControladorVuelo() {
        vv = new VistaVuelo();
    }

    public void iniciarVuelo() {
        do {
            int opcion = vv.menuPrincipal();
            if (opcion == 0) {
                return;
            }
            switch (opcion) {
                case 1:
                    this.nuevoVuelo(vv.crearVuelo());
                    break;
                case 2:
                    this.mostrarVuelos();
                    break;
                case 3:
                    this.modificarVuelo();
                    break;
                case 4:
                    this.eliminarVuelo();
                    break;
                case 5:
                    this.asignarPuertaYTerminal();
                    break;

            }
        } while (true);

    }

    public void nuevoVuelo(Vuelo v) {
        try {
            if (v != null) {
                ServicioVuelo.getServicio().nuevoVuelo(v);
            }
        } catch (DAOException ex) {
            vv.mostrarError("Ha habido un error al obtener los datos: " + ex.getMessage());
        } catch (ServiciosException ex) {
            vv.mostrarError("Ha habido un problema al insertar el vuelo: " + ex.getMessage());
        }
    }

    public void modificarVuelo() {
        //Comprobamos que no haya ninguna reserva con tarjeta de embarques que tengan este codigo de vuelo
        try {
            vv.mostrarVuelos(ServicioVuelo.getServicio().obtenerVuelos());
            String codigoVuelo = vv.pedirCodigoVuelo();
            if (codigoVuelo == null) {
                return;
            }
            ServicioVuelo.getServicio().comprobarTarjetasEnReservasConEsteVuelo(codigoVuelo);

            Integer opcion;
            opcion = vv.menuModificarVuelo();
            if (opcion != null) {
                switch (opcion) {
                    case 1:
                        Double precio = vv.pedirPrecioPersona();
                        if (precio != null) {
                            ServicioVuelo.getServicio().modificarVueloPrecio(codigoVuelo, precio);
                            //Actualizamos los importes de las reservas con el vuelo modificado
                            for (Reserva r : ServicioReserva.getServicio().obtenerReservas(codigoVuelo)) {
                                r.setImporte(r.calcularImporte());
                            }
                        }
                        break;
                    case 2:
                        Date fecha = vv.pedirFecha();
                        if (fecha != null) {
                            ServicioVuelo.getServicio().modificarVueloFecha(codigoVuelo, fecha);
                        }
                        break;
                    case 3:
                        ServicioVuelo.getServicio().comprobarReservas(codigoVuelo);
                        Integer plazas = vv.pedirPlazasDisponibles();
                        if (plazas != null) {
                            ServicioVuelo.getServicio().modificarVueloPlazas(codigoVuelo, plazas);
                        }
                        break;
                    case 4:
                        Integer terminal = vv.pedirTerminal();
                        if (terminal != null) {
                            ServicioVuelo.getServicio().modificarVueloTerminal(codigoVuelo, terminal);
                        }
                        break;
                    case 5:
                        Integer puerta = vv.pedirPuerta();
                        if (puerta != null) {
                            ServicioVuelo.getServicio().modificarVueloPuerta(codigoVuelo, puerta);
                        }
                        break;
                }

            }
        } catch (DAOException dao) {
            vv.mostrarError("Error al intentar obtener los datos: " + dao.getMessage());
        } catch (ServiciosException se) {
            vv.mostrarError("Error al modificar un vuelo: " + se.getMessage());
        }
    }

    public void eliminarVuelo() {
        try {
            vv.mostrarVuelos(ServicioVuelo.getServicio().obtenerVuelos());
            String codigo = vv.pedirCodigoVuelo();
            if (codigo == null) {
                return;
            }
            ServicioVuelo.getServicio().comprobarReservas(codigo);
            ServicioVuelo.getServicio().eliminarVuelo(codigo);
        } catch (DAOException dao) {
            vv.mostrarError("Error al intentar obtener los datos: " + dao.getMessage());
        } catch (ServiciosException se) {
            vv.mostrarError("Error al eliminar un vuelo: " + se.getMessage());
        }
    }

    public void obtenerVuelo(String vuelo) {
        if (vuelo != null) {
            try {
                vv.mostrarVuelo(ServicioVuelo.getServicio().obtenerVuelo(vuelo));
            } catch (ServiciosException ex) {
                vv.mostrarError("Error al intentar obtener el vuelo especificado: " + ex.getMessage());
            } catch (DAOException ex) {
                vv.mostrarError("Error al intentar obtener los datos: " + ex.getMessage());
            }
        }
    }

    public void mostrarVuelos(Date fecha) {
        if (fecha != null) {
            try {
                List<Vuelo> vuelos = ServicioVuelo.getServicio().obtenerVuelos(fecha);
                vv.mostrarVuelos(vuelos);
            } catch (DAOException ex) {
                vv.mostrarError("Error al intentar obtener los datos: " + ex);
            } catch (ServiciosException ex) {
                vv.mostrarError("Error al intentar mostrar los vuelos" + ex);
            }
        }
    }

    public void mostrarVuelos() {
        try {
            vv.mostrarVuelos(ServicioVuelo.getServicio().obtenerVuelos());
        } catch (DAOException ex) {
            vv.mostrarError("Error al intentar obtener los datos" + ex);
        } catch (ServiciosException ex) {
            vv.mostrarError("Error al intentar mostrar los datos: " + ex);
        }
    }

    private void asignarPuertaYTerminal() {
        try {
            vv.mostrarVuelos(ServicioVuelo.getServicio().obtenerVuelos());
            String codigoVuelo = vv.pedirCodigoVuelo();
            if (codigoVuelo == null) {
                return;
            }
            ServicioVuelo.getServicio().obtenerVuelo(codigoVuelo);
            Integer puerta = vv.pedirPuerta();
            if (puerta != null) {
                ServicioVuelo.getServicio().modificarVueloPuerta(codigoVuelo, puerta);
            }
            Integer terminal = vv.pedirTerminal();
            if (terminal != null) {
                ServicioVuelo.getServicio().modificarVueloTerminal(codigoVuelo, terminal);
            }
        } catch (DAOException dao) {
            vv.mostrarError("Error al intentar obtener los datos: " + dao.getMessage());
        } catch (ServiciosException se) {
            vv.mostrarError("Error al modificar un vuelo: " + se.getMessage());
        }
    }

}
