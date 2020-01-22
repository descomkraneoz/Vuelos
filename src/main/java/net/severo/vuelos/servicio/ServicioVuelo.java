/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.severo.vuelos.servicio;

import net.severo.vuelos.dao.DAOException;
import net.severo.vuelos.dao.Hibernate.VueloHibernate;
import net.severo.vuelos.dao.IVuelosDAO;
import net.severo.vuelos.dao.JDBC.VuelosJDBCDAO;
import net.severo.vuelos.dao.Raf.VuelosRafDAO;
import net.severo.vuelos.dao.txt.VueloTxtDAO;
import net.severo.vuelos.estructura.Reserva;
import net.severo.vuelos.estructura.Vuelo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Usuario
 */
public class ServicioVuelo {

    private IVuelosDAO dao = null;

    private static ServicioVuelo servicioVuelo = null;

    public static ServicioVuelo getServicio() throws DAOException {
        if (servicioVuelo == null) {
            servicioVuelo = new ServicioVuelo();
        }
        return servicioVuelo;
    }

    public void elegirSistemaAlmacenamiento(int opcion) throws DAOException {

        if (opcion == 1) {
            dao = new VueloTxtDAO();
        }
        if (opcion == 2) {

            dao=new VuelosRafDAO();
        }
        if (opcion == 3) {

            dao=new VuelosJDBCDAO();
        }
        if (opcion == 4) {

            dao = new VueloHibernate();
        }
        if (opcion == 5) {

            //No implentado;
        }
        if (opcion == 6) {

            //No implentado
        }

    }

    public void nuevoVuelo(Vuelo v) throws ServiciosException, DAOException {
        if (dao.obtenerVuelo(v.getCodigo()) != null) {
            throw new ServiciosException("El vuelo ya existe.");
        }

        dao.iniciarTransaccion();

        dao.crearVuelo(v);

        dao.finalizarTransaccion();

    }

    public Vuelo obtenerVuelo(String codigoVuelo) throws DAOException, ServiciosException {
        List<Vuelo> vuelos = dao.obtenerVuelos();
        for (Vuelo v : vuelos) {
            if (v.getCodigo().equals(codigoVuelo)) {
                return v;
            }
        }
        throw new ServiciosException("No hay ningún vuelo con el codigo especificado");
    }

    public void eliminarVuelo(String codigoVuelo) throws DAOException, ServiciosException {
        this.obtenerVuelo(codigoVuelo);
        dao.eliminarVuelo(codigoVuelo);

    }

    public List<Vuelo> obtenerVuelos() throws DAOException, ServiciosException {
        List<Vuelo> vuelos = dao.obtenerVuelos();
        if (vuelos.isEmpty()) {
            throw new ServiciosException("No hay ningún vuelo");
        }
        return vuelos;
    }

    public List<Vuelo> obtenerVuelos(Date fecha) throws DAOException, ServiciosException {
        List<Vuelo> vuelos = dao.obtenerVuelos(fecha);
        if (vuelos.isEmpty()) {
            throw new ServiciosException("No hay ningún vuelo con esa fecha");
        }
        return vuelos;
    }
    
    public List<Vuelo> obtenerVuelos(Date fechaInicio,Date fechaFin) throws DAOException, ServiciosException {
        List<Vuelo> vuelos = new ArrayList<>();
        for (Vuelo v:dao.obtenerVuelos()){
            if((v.getFechaVuelo().before(fechaFin)) && (v.getFechaVuelo().before(fechaInicio))){
                vuelos.add(v);
            }
        }
        if (vuelos.isEmpty()) {
            throw new ServiciosException("No hay ningún vuelo entre esas fechas");
        }
        return vuelos;
    }

    public void modificarVueloPrecio(String codigoVuelo, Double precio) throws DAOException, ServiciosException {
        Vuelo v = this.obtenerVuelo(codigoVuelo);
        v.setPrecioPersona(precio);
        dao.modificarVuelo(v);
    }

    public void modificarVueloFecha(String codigoVuelo, Date fecha) throws DAOException, ServiciosException {
        Vuelo v = this.obtenerVuelo(codigoVuelo);
        v.setFechaVuelo(fecha);
        dao.modificarVuelo(v);
    }

    public void modificarVueloPlazas(String codigoVuelo, Integer plazas) throws DAOException, ServiciosException {
        Vuelo v = this.obtenerVuelo(codigoVuelo);
        v.setPlazasDisponibles(plazas);
        dao.modificarVuelo(v);
    }

    public void modificarVueloTerminal(String codigoVuelo, Integer terminal) throws DAOException, ServiciosException {
        Vuelo v = this.obtenerVuelo(codigoVuelo);
        v.setTerminal(terminal);
        dao.modificarVuelo(v);
    }

    public void modificarVueloPuerta(String codigoVuelo, Integer puerta) throws DAOException, ServiciosException {
        Vuelo v = this.obtenerVuelo(codigoVuelo);
        v.setPuerta(puerta);
        dao.modificarVuelo(v);
    }

    public void comprobarTarjetasEnReservasConEsteVuelo(String codigoVuelo) throws DAOException, ServiciosException {
        List<Reserva> reservas=new ArrayList<>();
        //Hacemos esto para evitar que se lancen las excepciones de servicio las cuales no tiene sentido aqui
        try{
        reservas=ServicioReserva.getServicio().obtenerReservas(codigoVuelo);
        }catch(ServiciosException se){}
        for (Reserva r : reservas) {
            ServicioReserva.getServicio().tieneTarjetaDeEmbarque(r);
        }
    }

    public void comprobarReservas(String codigo) throws DAOException, ServiciosException {
        List<Reserva> reservas=new ArrayList<>();
        //Hacemos esto para evitar que se lancen las excepciones de servicio las cuales no tiene sentido aqui
        try{
        reservas=ServicioReserva.getServicio().obtenerReservas(codigo);
        }catch(ServiciosException se){}
       if(!(reservas.isEmpty())){
           throw new ServiciosException("Este vuelo tiene reservas hechas");
       }
    }

    public List<Vuelo> obtenerVuelosSinPlazas() throws DAOException, ServiciosException {
        List<Vuelo> vuelos=new ArrayList<>();
        for(Vuelo v:this.obtenerVuelos()){
            if(v.getPlazasDisponibles()==0){
                vuelos.add(v);
            }
        }
        if(vuelos.isEmpty()){
            throw new ServiciosException("No hay vuelos vacios");
        }
        return vuelos;
    }

    public void finalizar() throws DAOException {
        //cierra las conexiones  e BD en caso de que fuera necesario
        //en el caso de archivos de texto o bianrios no es necesario hacer nada
        //ya que los abrimos y cerramos en cada operacion
        dao.finalizar();
    }
}
