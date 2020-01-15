/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.severo.vuelos.dao.txt;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import net.severo.vuelos.dao.DAOException;
import net.severo.vuelos.dao.IReservaDAO;
import net.severo.vuelos.estructura.Adulto;
import net.severo.vuelos.estructura.Configuracion;
import net.severo.vuelos.estructura.Descuentos;
import net.severo.vuelos.estructura.Ninyo;
import net.severo.vuelos.estructura.OrdenPago;
import net.severo.vuelos.estructura.Pasajero;
import net.severo.vuelos.estructura.Reserva;
import net.severo.vuelos.estructura.TarjetaEmbarque;
import net.severo.vuelos.estructura.Vuelo;

/**
 *
 * @author Rubén Más Almira
 */
public class ReservasTxtDAO implements IReservaDAO {

    private String archivoReservas = null;
    private String archivoPasajeros = null;
    private String archivoTarjetasEmbarque = null;
    private String archivoOrdenesPago = null;

    public ReservasTxtDAO() throws DAOException {
        Configuracion configuracion = new Configuracion("/configuracion.properties");
        this.archivoReservas = configuracion.getTxtReservas();
        this.archivoPasajeros = configuracion.getTxtPasajeros();
        this.archivoTarjetasEmbarque = configuracion.getTxtTarjetasEmbarque();
        this.archivoOrdenesPago = configuracion.getTxtOrdenesPago();

    }

    @Override
    public void crearReserva(Reserva r) throws DAOException {
        List<Reserva> reservas = this.obtenerTodasReservas();
        reservas.add(r);
        this.guardarReservasTxt(reservas);
    }

    @Override
    public void modificarReserva(Reserva r) throws DAOException {
        this.eliminarReserva(r.getId());
        this.crearReserva(r);
    }

    @Override
    public Reserva obtenerReserva(int id) throws DAOException {
        List<Reserva> reservas = this.obtenerTodasReservas();
        for (Reserva r : reservas) {
            if (id == r.getId()) {
                return r;
            }
        }
        return null;
    }

    @Override
    public boolean eliminarReserva(int id) throws DAOException {
        List<Reserva> reservas = this.obtenerTodasReservas();
        for (Reserva r : reservas) {
            if (id == r.getId()) {
                reservas.remove(r);
                this.guardarReservasTxt(reservas);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Reserva> obtenerTodasReservas() throws DAOException {
        return this.sacarReservasTxt();
    }

    @Override
    public List<Reserva> obtenerTodasReservas(Date d) {
        List<Reserva> reservas = new ArrayList<>();
        for (Reserva r : reservas) {
            if (d.equals(r.getFecha())) {
                reservas.add(r);
            }
        }
        return reservas;
    }

    @Override
    public List<Reserva> obtenerTodasReservas(String idVuelo) throws DAOException {
        List<Reserva> reservas = new ArrayList<>();
        for (Reserva r : this.obtenerTodasReservas()) {
            if (idVuelo.equals(r.getVuelo().getCodigo())) {
                reservas.add(r);
            }
        }
        return reservas;
    }

    public void guardarReservasTxt(List<Reserva> reservas) throws DAOException {
        try ( PrintWriter pwR = new PrintWriter(new FileWriter(archivoReservas, false))) {
            try ( PrintWriter pwP = new PrintWriter(new FileWriter(archivoPasajeros, false))) {
                try ( PrintWriter pwT = new PrintWriter(new FileWriter(archivoTarjetasEmbarque, false))) {
                    for (Reserva r : reservas) {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        String fechaStr = sdf.format(r.getFecha());
                        String reserva = r.getId() + "#" + fechaStr + "#" + r.isCancelada()
                                + "#" + r.getVuelo().getCodigo() + "#" + r.getImporte();

                        pwR.println(reserva);

                        for (Pasajero p : r.getPasajeros()) {
                            String fechaPasajero = sdf.format(p.getFecha_nacimiento());
                            String pasajero = r.getId() + "#" + p.getId() + "#" + p.getDni() + "#"
                                    + p.getNombre() + "#" + p.getApellidos() + "#"
                                    + fechaPasajero + "#" + p.getNum_maletas_facturar();
                            if (p instanceof Ninyo) {
                                pasajero += "#" + ((Ninyo) p).getSolo() + "#" + ((Ninyo) p).getSilleta();
                            } else {
                                pasajero += "#" + ((Adulto) p).getDescuento();
                            }
                            pwP.println(pasajero);
                            /*
                            private int id;
                            private String vuelo;
                            private String origen;
                            private String destino;
                            private Date fechaVuelo;
                            private int terminal;
                             private int puerta;
                             */
                            if (p.getTarjeta() != null) {
                                String tarjetaEmbarque = p.getTarjeta().getId() + "#" + p.getTarjeta().getVuelo();
                                pwT.println(tarjetaEmbarque);
                            }
                        }

                    }
                } catch (Exception e) {
                    throw new DAOException(
                            "Ha habido un problema al guardar los pasajeros en el archivo de texto:", (IOException) e);
                }
            } catch (Exception e) {
                throw new DAOException(
                        "Ha habido un problema al guardar las reservas en el archivo de texto:", (IOException) e);
            }
        } catch (IOException ex) {
            throw new DAOException(
                    "Ha habido un problema al guardar las reservas en el archivo de texto:", (IOException) ex);
        }
    }

    public List<Reserva> sacarReservasTxt() throws DAOException {
        List<Reserva> reservas = new ArrayList<>();

        try ( BufferedReader brR = new BufferedReader(new FileReader(archivoReservas))) {
            String reserva;
            while ((reserva = brR.readLine()) != null) {
                String[] datosReserva = reserva.split("#");
                //r.getId() + "#" + fechaStr + "#" + r.getCancelada() + "#" + r.getVuelo().getCodigo() + "#" + r.getImporte();
                int idReserva = Integer.parseInt(datosReserva[0]);

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date fechaReserva = sdf.parse(datosReserva[1]);

                Boolean cancelada = Boolean.parseBoolean(datosReserva[2]);

                String codigoVuelo = datosReserva[3];
                VueloTxtDAO daoVuelo = new VueloTxtDAO();
                Vuelo v = daoVuelo.obtenerVuelo(codigoVuelo);

                double importe = Double.parseDouble(datosReserva[4]);

                List<Pasajero> pasajeros = new ArrayList<>();

                try ( BufferedReader brP = new BufferedReader(new FileReader(archivoPasajeros))) {
                    String pasajero;
                    while ((pasajero = brP.readLine()) != null) {
                        String[] datosPasajero = pasajero.split("#");
                        int idReservaPasajero = Integer.parseInt(datosPasajero[0]);
                        if (idReserva == idReservaPasajero) {

                            int id = Integer.parseInt(datosPasajero[1]);

                            String dni = datosPasajero[2];

                            String nombre = datosPasajero[3];

                            String apellido = datosPasajero[4];

                            Date fechaNacimiento = sdf.parse(datosPasajero[5]);

                            int numeroMaletas = Integer.parseInt(datosPasajero[6]);

                            if (esNinyo(fechaNacimiento)) {
                                Boolean solo = Boolean.parseBoolean(datosPasajero[7]);
                                Boolean silleta = Boolean.parseBoolean(datosPasajero[8]);
                                pasajeros.add(new Ninyo(id, dni, nombre, apellido, fechaNacimiento, numeroMaletas, solo, silleta));
                            } else {
                                Descuentos d = Descuentos.NINGUNO;
                                if (Descuentos.RESIDENTE_ISLA.name().equals(datosPasajero[7])) {
                                    d = Descuentos.RESIDENTE_ISLA;
                                }
                                pasajeros.add(new Adulto(id, dni, nombre, apellido, fechaNacimiento, numeroMaletas, d));
                            }
                        }
                    }

                } catch (Exception e) {
                    throw new DAOException(
                            "Ha habido un problema al obtener los pasajeros de la reserva: " + idReserva, (IOException) e);
                }

                try ( BufferedReader brT = new BufferedReader(new FileReader(archivoTarjetasEmbarque))) {
                    String tarjeta;
                    while ((tarjeta = brT.readLine()) != null) {
                        String[] datosTarjeta = tarjeta.split("#");
                        Integer idTarjeta = Integer.parseInt(datosTarjeta[0]);
                        for (int i = 0; i < pasajeros.size(); i++) {
                            if (pasajeros.get(i).getId()==(idTarjeta)) {
                                String codigoVueloTarjeta = datosTarjeta[1];
                                Vuelo vueloTarjeta = daoVuelo.obtenerVuelo(codigoVueloTarjeta);
                                /*
                            private int id;
                            private String vuelo;
                            private String origen;
                            private String destino;
                            private Date fechaVuelo;
                            private int terminal;
                             private int puerta;
                                 */
                                pasajeros.get(i).setTarjeta(
                                        new TarjetaEmbarque(idTarjeta, vueloTarjeta.getCodigo(),
                                                vueloTarjeta.getOrigen(),
                                                vueloTarjeta.getDestino(),
                                                vueloTarjeta.getFechaVuelo(),
                                                vueloTarjeta.getTerminal(),
                                                vueloTarjeta.getPuerta()));
                                break;
                            }
                        }

                    }
                } catch (Exception e) {
                    throw new DAOException(
                            "Ha habido un problema al obtener los pasajeros de la reserva: " + idReserva, (IOException) e);
                }

                reservas.add(new Reserva(idReserva, fechaReserva, cancelada, pasajeros, v, importe));
            }
            return reservas;

        } catch (Exception e) {
            throw new DAOException("Ha habido un problema al obtener las reservas desde el archivo de texto:" + e.getMessage(), (IOException) e);
        }
    }

    private static boolean esNinyo(Date fechaNacimiento) {
        Calendar fechaDeCorte = Calendar.getInstance();
        Calendar fechaNacimientoCalendar = Calendar.getInstance();
        fechaNacimientoCalendar.setTime(fechaNacimiento);
        fechaDeCorte.setTime(new Date());
        fechaDeCorte.add(Calendar.YEAR, -12);
        return fechaDeCorte.before(fechaNacimientoCalendar);
    }

    @Override
    public void crearOrdenPago(OrdenPago orden) throws DAOException {
        //int id, Date fechaOrden, String dniOrdente, String numTarjeta, double importe, Reserva reserva
        try ( PrintWriter pw = new PrintWriter(new FileWriter(archivoOrdenesPago, true))) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String fechaStr = sdf.format(orden.getFechaOrden());
            String cadena = orden.getId() + "#" + fechaStr + "#" + orden.getDniOrden() + "#" + orden.getNumTarjeta()
                    + "#" + orden.getImporte() + "#" + orden.getReserva().getId();
            pw.println(cadena);
        } catch (Exception e) {
            throw new DAOException(
                    "Ha habido un problema al guardar los vuelos en el archivo de texto:", e);
        }
    }

    @Override
    public OrdenPago obtenerOrdenPago(int id) throws DAOException {
        try ( BufferedReader br = new BufferedReader(new FileReader(archivoOrdenesPago))) {
            String orden;
            while ((orden = br.readLine()) != null) {
                String[] datos = orden.split("#");
                Integer idOrden = Integer.parseInt(datos[0]);
                if (id == idOrden) {
                    //int id, Date fechaOrden, String dniOrdente, String numTarjeta, double importe, Reserva reserva
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    Date fechaOrden = sdf.parse(datos[1]);
                    String dni = datos[2];
                    String numTarjeta = datos[3];
                    Double importe = Double.parseDouble(datos[4]);
                    Reserva r = this.obtenerReserva(Integer.parseInt(datos[5]));
                    return new OrdenPago(id, fechaOrden, dni, numTarjeta, importe, r);
                }
            }
        } catch (Exception e) {
            throw new DAOException("Ha habido un problema al guardar las ordenes de pago en el archivo de texto:", e);
        }
        return null;
    }

    @Override
    public List<OrdenPago> obtenerOrdenPago() throws DAOException {
        List<OrdenPago> ordenesPago = new ArrayList<>();
        try ( BufferedReader br = new BufferedReader(new FileReader(archivoOrdenesPago))) {
            String orden;
            //int id, Date fechaOrden, String dniOrdente, String numTarjeta, double importe, Reserva reserva
            while ((orden = br.readLine()) != null) {
                String[] datos = orden.split("#");
                Integer idOrden = Integer.parseInt(datos[0]);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date fechaOrden = sdf.parse(datos[1]);
                String dni = datos[2];
                String numTarjeta = datos[3];
                Double importe = Double.parseDouble(datos[4]);
                Reserva r = this.obtenerReserva(Integer.parseInt(datos[5]));
                ordenesPago.add(new OrdenPago(idOrden, fechaOrden, dni, numTarjeta, importe, r));
            }
        } catch (Exception e) {
            throw new DAOException("Ha habido un problema al guardar las ordenes de pago en el archivo de texto:", e);
        }
        return ordenesPago;
    }
}
