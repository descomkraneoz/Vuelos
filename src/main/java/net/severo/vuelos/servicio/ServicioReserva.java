/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.severo.vuelos.servicio;

import net.severo.vuelos.dao.DAOException;
import net.severo.vuelos.dao.IReservaDAO;
import net.severo.vuelos.dao.JDBC.ReservasJDBCDAO;
import net.severo.vuelos.dao.Raf.ReservasRafDAO;
import net.severo.vuelos.dao.txt.ReservasTxtDAO;
import net.severo.vuelos.estructura.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class ServicioReserva {

    private IReservaDAO dao = null;

    private static ServicioReserva servicioReserva = null;

    public static ServicioReserva getServicio() throws DAOException {
        if (servicioReserva == null) {
            servicioReserva = new ServicioReserva();
        }
        return servicioReserva;
    }

    public void elegirSistemaAlmacenamiento(int opcion) throws DAOException {

        if (opcion == 1) {
            dao = new ReservasTxtDAO();
        }
        if (opcion == 2) {

            dao=new ReservasRafDAO();
        }
        if (opcion == 3) {

            dao = new ReservasJDBCDAO();
        }
        if (opcion == 4) {

            //No implentado
        }
        if (opcion == 5) {

            //No implentado;
        }
        if (opcion == 6) {

            //No implentado
        }

    }

    public void nuevaReserva(Reserva r) throws ServiciosException, DAOException {
        if (dao.obtenerReserva(r.getId()) != null) {
            throw new ServiciosException("La reserva ya existe.");
        }
        ServicioVuelo.getServicio().modificarVueloPlazas(r.getVuelo().getCodigo(), r.getVuelo().getPlazasDisponibles() - r.getPasajeros().size());
        dao.crearReserva(r);
    }

    public List<Reserva> obtenerReservas(String idVuelo) throws DAOException, ServiciosException {
        //Comprobacion de que el vuelo existe,si no existe se llama a una ServiciosException
        ServicioVuelo.getServicio().obtenerVuelo(idVuelo);
        List<Reserva> reservas = dao.obtenerTodasReservas(idVuelo);
        if (reservas.isEmpty()) {
            throw new ServiciosException("No hay ninguna reserva con ese id de vuelo");
        }

        return reservas;
    }

    public List<Reserva> obtenerReservas() throws DAOException, ServiciosException {
        List<Reserva> reservas = dao.obtenerTodasReservas();
        if (reservas.isEmpty()) {
            throw new ServiciosException("No hay ninguna reserva");
        }

        return reservas;
    }

    public void tieneTarjetaDeEmbarque(Reserva r) throws ServiciosException {
        for (Pasajero p : r.getPasajeros()) {
            if (p.getTarjeta() != null) {
                throw new ServiciosException("Esta reserva tiene tarjetas de embarque generadas");
            }
        }
    }

    public Reserva obtenerReserva(Integer idReserva) throws DAOException, ServiciosException {
        Reserva r = null;
        r = dao.obtenerReserva(idReserva);
        if (r == null) {
            throw new ServiciosException("No hay ninguna reserva con el id introducido");
        }
        return r;
    }

    public void modificarReservaCodigoVuelo(Reserva r, Vuelo v) throws DAOException, ServiciosException {
        //Sumo plazas al vuelo antiguo,el cual lo obtengo antes de cambiar el vuelo de la reserva
        ServicioVuelo.getServicio().modificarVueloPlazas(r.getVuelo().getCodigo(), r.getVuelo().getPlazasDisponibles() + r.getPasajeros().size());
        //Le pongo a la reserva el nuevo Vuelo
        r.setVuelo(v);
        //Resto las plazas al nuevo vuelo
        ServicioVuelo.getServicio().modificarVueloPlazas(r.getVuelo().getCodigo(), r.getVuelo().getPlazasDisponibles() - r.getPasajeros().size());
        //Calculo el nuevo importe de la reserva
        r.setImporte(r.calcularImporte());
        //Modifico la reserva
        dao.modificarReserva(r);
    }

    public void modificarReservaAnyadirPasajero(Reserva r, Pasajero p) throws DAOException, ServiciosException {
        //Le sumo los pasajeros que habían antes
        ServicioVuelo.getServicio().modificarVueloPlazas(r.getVuelo().getCodigo(), r.getVuelo().getPlazasDisponibles() + r.getPasajeros().size());
        r.getPasajeros().add(p);
        //Le resto los pasajeros que hay ahora
        ServicioVuelo.getServicio().modificarVueloPlazas(r.getVuelo().getCodigo(), r.getVuelo().getPlazasDisponibles() - r.getPasajeros().size());
        //Calculo el importe de la reserva
        r.setImporte(r.calcularImporte());
        //Guardo la reserva
        dao.modificarReserva(r);
    }

    public void modificarReservaBorrarPasajero(Reserva r, Pasajero p) throws DAOException, ServiciosException {
        //Le sumo los pasajeros que habían antes
        ServicioVuelo.getServicio().modificarVueloPlazas(r.getVuelo().getCodigo(), r.getVuelo().getPlazasDisponibles() + r.getPasajeros().size());
        r.getPasajeros().remove(p);
        //Le resto los pasajeros que hay ahora
        ServicioVuelo.getServicio().modificarVueloPlazas(r.getVuelo().getCodigo(), r.getVuelo().getPlazasDisponibles() - r.getPasajeros().size());
        //Calculo el importe de la reserva
        r.setImporte(r.calcularImporte());
        //Guardo la reserva
        dao.modificarReserva(r);
    }

    public Pasajero obtenerPasajero(Reserva r, Integer id) throws ServiciosException {
        for (Pasajero p : r.getPasajeros()) {
            if (id == p.getId()) {
                return p;
            }
        }
        throw new ServiciosException("No hay ningún pasajero con el ese id en la reserva introducida");
    }

    public void cancelarReserva(Reserva r) throws DAOException, ServiciosException {
        this.tieneTarjetaDeEmbarque(r);
        if(sePuedeCancelar(r)){
        r.setCancelada(true);
        //Ponemos el importe de la reserva a negativo
        r.setImporte(r.calcularImporte() * -1);
        //Sumo las plazas canceladas al vuelo de la reserva
        ServicioVuelo.getServicio().modificarVueloPlazas(r.getVuelo().getCodigo(), r.getVuelo().getPlazasDisponibles() + r.getPasajeros().size());
        dao.modificarReserva(r);
        }else{
            throw new ServiciosException("La reserva no puede ser cancelada,se ha acabado el plazo");
        }
    }
    
    public void retirarCancelacion(Reserva r) throws DAOException, ServiciosException {
        r.setCancelada(false);
        r.setImporte(r.getImporte()*-1);
        ServicioVuelo.getServicio().modificarVueloPlazas(r.getVuelo().getCodigo(), r.getVuelo().getPlazasDisponibles() - r.getPasajeros().size());
        dao.modificarReserva(r);
    }

    public void generarTarjetasEmbarque(Reserva r) throws ServiciosException, DAOException {
        //se supone que ni la puerta ni el terminal van a ser 0
        if (r.getVuelo().getPuerta() == 0 || r.getVuelo().getTerminal() == 0) {
            throw new ServiciosException("No se ha asignado ni una puerta ni un terminal al vuelo");
        }
        if (r.isCancelada() == true) {
            throw new ServiciosException("Esta reserva esta cancelada,no se pueden generar tarjetas de embarque");
        }
        //Comprobacion de si tiene tarjetas de embarque
        this.tieneTarjetaDeEmbarque(r);
        r.generarTarjetas();
        //método para guardar tarjetas de embarque en txt
        dao.modificarReserva(r);

    }

    public void hayAsientosDelVueloLibres(Vuelo v, List<Pasajero> pasajeros) throws ServiciosException{
        if (v.getPlazasDisponibles() < pasajeros.size()) {
            throw new ServiciosException("No hay asientos libres en este vuelo");
        }
    }

    public void comprobacionVueloLleno(Vuelo vuelo) throws ServiciosException {
        if (vuelo.getPlazasDisponibles() <= 0) {
            throw new ServiciosException("El vuelo esta lleno.");
        }
    }

    public void comprobarReservaVacia(Reserva r) throws ServiciosException {
        if (r.getPasajeros().size() <= 1) {
            throw new ServiciosException("No puede haber ninguna reserva sin pasajeros.");
        }
    }
    
    private boolean sePuedeCancelar(Reserva r){
        Calendar fechaDeCorte = Calendar.getInstance();
        Calendar fechaReserva = Calendar.getInstance();
        fechaReserva.setTime(r.getFecha());
        fechaDeCorte.setTime(r.getVuelo().getFechaVuelo());
        fechaDeCorte.add(Calendar.MONTH, -1);
        return fechaReserva.before(fechaDeCorte);
    }

    public void nuevaOrdenPago(OrdenPago orden) throws DAOException, ServiciosException {
        if(!(this.obtenerReservas().contains(orden.getReserva()))){
            throw new ServiciosException("La reserva de esta orden de pago no existe");
        }
        else if(dao.obtenerOrdenPago(orden.getId())!=null){
            throw new ServiciosException("La id de esta orden de pago coincide con otra");
        }
        dao.crearOrdenPago(orden);
        
    }
    public OrdenPago obtenerOrdenPago(Integer idPago) throws DAOException,ServiciosException{
        OrdenPago pago=null;
        pago=dao.obtenerOrdenPago(idPago);
        if(pago==null){
            throw new ServiciosException("La id de esta orden de pago no existe");
        }
        return pago;
    }
    
    public List<OrdenPago> obtenerOrdenPago() throws DAOException,ServiciosException{
        List<OrdenPago> ordenes=dao.obtenerOrdenPago();
        if(ordenes.isEmpty()){
            throw new ServiciosException("No hay ordenes de pago");
        }
        return ordenes;
    }

    public void comprobacionReservaCancelada(Reserva r) throws ServiciosException{
        if(r.isCancelada()){
            throw new ServiciosException("La reserva esta cancelada");
        }
    }

    public ArrayList<Pasajero> obtenerTodosLosPasajeros() throws DAOException, ServiciosException {

        List<Reserva> reservas = dao.obtenerTodasReservas();
        ArrayList<Pasajero> totalPasajeros=new ArrayList<>();
        for(Reserva r:reservas){
            for(Pasajero p:r.getPasajeros()){
                totalPasajeros.add(p);
            }
        }
        return totalPasajeros;
    }

    public void pasajeroExiste(Pasajero pasajero) throws DAOException, ServiciosException {
        if (this.obtenerTodosLosPasajeros().contains(pasajero)) {
            throw new ServiciosException("Este pasajero ya existe");
        }
    }

    public void eliminarReserva(Reserva r) throws DAOException, ServiciosException {
        if(!(dao.obtenerTodasReservas().contains(r))){
            throw new ServiciosException("La reserva no existe");
        }
        dao.eliminarReserva(r.getId());
    }

    public List<OrdenPago> obtenerOrdenesPago(Integer idReserva) throws DAOException, ServiciosException {
        List<OrdenPago> ordenes=new ArrayList<>();
        for(OrdenPago orden:dao.obtenerOrdenPago()){
            if(orden.getReserva().getId()==idReserva){
                ordenes.add(orden);
            }
        }
        if(ordenes.isEmpty()){
            throw new ServiciosException("No hay ordenes de esa reserva");
        }
        return ordenes;
    }

    public List<TarjetaEmbarque> obtenerTarjetasEmbarque(Integer idReserva) throws DAOException, ServiciosException {
        List<TarjetaEmbarque> tarjetas=new ArrayList<>();
        for(Pasajero p:this.obtenerReserva(idReserva).getPasajeros()){
            if(p.getTarjeta()!=null){
                tarjetas.add(p.getTarjeta());
            }
        }
        if(tarjetas.isEmpty()){
            throw new ServiciosException("No hay ninguna tarjeta de embarque generada en esta reserva");
        }
        return tarjetas;
    }

    public double obtenerDineroDeOrdenesDePagoDelVuelo(String codigoVuelo) throws ServiciosException, DAOException {
        double cantidad=0;
        for(Reserva r:this.obtenerReservas(codigoVuelo)){
            for(OrdenPago op:this.obtenerOrdenesPago(r.getId())){
                cantidad+=op.getImporte();
            }
        }
        return cantidad;
    }

    public int obtenerNumeroDeNinyosDelVuelo(String codigoVuelo) throws DAOException, ServiciosException {
        int contador=0;
        for(Reserva r:this.obtenerReservas(codigoVuelo)){
            for(Pasajero p:r.getPasajeros()){
                if(p instanceof Ninyo){
                    contador++;
                }
            }
        }
        return contador;
    }

    public int obtenerNumeroDeAdultosDelVuelo(String codigoVuelo) throws DAOException, ServiciosException {
        int contador=0;
        for(Reserva r:this.obtenerReservas(codigoVuelo)){
            for(Pasajero p:r.getPasajeros()){
                if(p instanceof Adulto){
                    contador++;
                }
            }
        }
        return contador;
    }

    public int obtenerNumPasajerosSinTarjeta(String codigoVuelo) throws DAOException, ServiciosException {
        int contador=0;
        for(Reserva r:this.obtenerReservas(codigoVuelo)){
            for(Pasajero p:r.getPasajeros()){
                if(p.getTarjeta()==null){
                    contador++;
                }
            }
        }
        return contador;
    }

    public List<Reserva> reservasCanceladas(String codigoVuelo) throws DAOException, ServiciosException {
        List<Reserva> reservas=new ArrayList<>();
        for(Reserva r:this.obtenerReservas(codigoVuelo)){
            if(r.isCancelada()){
                reservas.add(r);
            }
        }
        return reservas;
    }

    
    
    
}
