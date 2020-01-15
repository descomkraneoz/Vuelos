/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.severo.vuelos.dao.Raf;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.severo.vuelos.dao.DAOException;
import net.severo.vuelos.dao.IReservaDAO;
import net.severo.vuelos.estructura.*;

/**
 * @author
 */
public class ReservasRafDAO implements IReservaDAO {


    private String archivoReservas = null;
    private String archivoPasajeros = null;
    private String archivoTarjetasEmbarque = null;
    private String archivoOrdenesPago = null;
    //Reservas
    //int id-Date fecha-boolean cancelada-List<Pasajero> pasajeros-Vuelo vuelo-double importe;
    private static final int SIZE_ID_RESERVA = 4;
    private static final int SIZE_FECHA_RESERVA = 20;
    private static final int SIZE_IMPORTE_RESERVA = 8;
    private static final int SIZE_VUELO_RESERVA = 16;
    private static final int SIZE_CANCELADA_RESERVA = 1;
    private static final int SIZE_TOTAL_RESERVA = SIZE_ID_RESERVA + SIZE_FECHA_RESERVA +
            SIZE_IMPORTE_RESERVA + SIZE_VUELO_RESERVA + SIZE_CANCELADA_RESERVA;

    //Pasajeros
    private static final int SIZE_ID_PASAJERO = 4;
    private static final int SIZE_DNI_PASAJERO = 18;
    private static final int SIZE_NOMBRE_PASAJERO = 100;
    private static final int SIZE_APELLIDO_PASAJERO = 100;
    private static final int SIZE_FECHA_PASAJERO = 20;
    private static final int SIZE_MALETAS_PASAJERO = 4;
    private static final int SIZE_DESCUENTO_PASAJERO = 30;
    private static final int SIZE_SOLO_SILLETA_PASAJERO = 1;
    private static final int SIZE_TOTAL_PASAJERO = SIZE_ID_RESERVA + SIZE_ID_PASAJERO + SIZE_DNI_PASAJERO + SIZE_NOMBRE_PASAJERO
            + SIZE_APELLIDO_PASAJERO + SIZE_FECHA_PASAJERO + SIZE_MALETAS_PASAJERO + SIZE_DESCUENTO_PASAJERO + SIZE_SOLO_SILLETA_PASAJERO + SIZE_SOLO_SILLETA_PASAJERO;
    //Tarjeta de embarque
    //int id,String vuelo,String origen,String destino,Date fecha_vuelo,int terminal,int puerta
    private static int SIZE_ID_TARJETA = 4;
    private static int SIZE_VUELO_TARJETA = 16;
    private static int SIZE_ORIGEN_TARJETA = 10;
    private static int SIZE_DESTINO_TARJETA = 10;
    private static int SIZE_FECHA_TARJETA = 20;
    private static int SIZE_TERMINAL_TARJETA = 4;
    private static int SIZE_PUERTA_TARJETA = 4;
    private static int SIZE_TOTAL_TARJETA = SIZE_ID_TARJETA + SIZE_VUELO_TARJETA + SIZE_ORIGEN_TARJETA + SIZE_DESTINO_TARJETA
            + SIZE_FECHA_TARJETA + SIZE_TERMINAL_TARJETA + SIZE_PUERTA_TARJETA;
    //Ordenes de pago
    //int id, Date fechaOrden, String dniOrdente, String numTarjeta, double importe, Reserva reserva
    private final int SIZE_ORDEN_PAGO_ID = 4;
    private final int SIZE_ORDEN_PAGO_FECHA = 20;
    private final int SIZE_ORDEN_PAGO_DNI = 18;
    private final int SIZE_ORDEN_PAGO_TARJETA = 40;
    private final int SIZE_ORDEN_PAGO_IMPORTE = 8;
    private final int SIZE_ORDEN_PAGO_ID_RESERVA = 4;
    private final int SIZE_ORDEN_PAGO_TOTAL = SIZE_ORDEN_PAGO_ID + SIZE_ORDEN_PAGO_FECHA + SIZE_ORDEN_PAGO_DNI
            + SIZE_ORDEN_PAGO_TARJETA + SIZE_ORDEN_PAGO_IMPORTE + SIZE_ORDEN_PAGO_ID_RESERVA;

    public ReservasRafDAO() throws DAOException {
        Configuracion configuracion = new Configuracion("/configuracion.properties");
        this.archivoReservas = configuracion.getRafReservas();
        this.archivoPasajeros = configuracion.getRafPasajeros();
        this.archivoTarjetasEmbarque = configuracion.getRafTarjetasEmbarque();
        this.archivoOrdenesPago = configuracion.getRafOrdenesPago();
    }

    @Override
    public void crearReserva(Reserva r) throws DAOException {
        //Reservas,pasajeros y tarjetas de embarque
        try (RandomAccessFile rafReservas = new RandomAccessFile(archivoReservas, "rw");
             RandomAccessFile rafPasajeros = new RandomAccessFile(archivoPasajeros, "rw");
             RandomAccessFile rafTarjetasEmbarque = new RandomAccessFile(archivoTarjetasEmbarque, "rw")) {
            /**Reservas*/
            int posicionDeEscritura = SIZE_TOTAL_RESERVA * r.getId();
            rafReservas.seek(posicionDeEscritura);
            //id
            rafReservas.writeInt(r.getId());
            //fecha
            StringBuffer buffer;
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String fecha = sdf.format(r.getFecha());
            buffer = new StringBuffer(fecha);
            buffer.setLength(SIZE_FECHA_RESERVA / 2);
            rafReservas.writeChars(buffer.toString());
            //importe
            rafReservas.writeDouble(r.getImporte());
            //vuelo
            buffer = new StringBuffer(r.getVuelo().getCodigo());
            buffer.setLength(SIZE_VUELO_RESERVA / 2);
            rafReservas.writeChars(buffer.toString());
            //cancelada
            rafReservas.writeBoolean(r.isCancelada());

            /**Pasajeros*/
            List<Pasajero> pasajeros = r.getPasajeros();
            for (Pasajero p : pasajeros) {
                posicionDeEscritura = SIZE_TOTAL_PASAJERO * p.getId();
                rafPasajeros.seek(posicionDeEscritura);
                //id Reserva
                rafPasajeros.writeInt(r.getId());
                //id
                rafPasajeros.writeInt(p.getId());
                //dni
                buffer = new StringBuffer(p.getDni());
                buffer.setLength(SIZE_DNI_PASAJERO / 2);
                rafPasajeros.writeChars(buffer.toString());
                //nombre
                buffer = new StringBuffer(p.getNombre());
                buffer.setLength(SIZE_NOMBRE_PASAJERO / 2);
                rafPasajeros.writeChars(buffer.toString());
                //apellidos
                buffer = new StringBuffer(p.getApellidos());
                buffer.setLength(SIZE_APELLIDO_PASAJERO / 2);
                rafPasajeros.writeChars(buffer.toString());
                //fecha
                buffer = new StringBuffer(sdf.format(p.getFecha_nacimiento()));
                buffer.setLength(SIZE_FECHA_PASAJERO / 2);
                rafPasajeros.writeChars(buffer.toString());
                //maletas
                rafPasajeros.writeInt(p.getNum_maletas_facturar());
                if (p instanceof Adulto) {
                    buffer = new StringBuffer(((Adulto) p).getDescuento().toString());
                } else {
                    buffer = new StringBuffer();
                }
                buffer.setLength(SIZE_DESCUENTO_PASAJERO / 2);
                rafPasajeros.writeChars(buffer.toString());
                if (p instanceof Ninyo) {
                    rafPasajeros.writeBoolean(((Ninyo) p).getSolo());
                    rafPasajeros.writeBoolean(((Ninyo) p).getSilleta());
                } else {
                    buffer = new StringBuffer();
                    //Se escribe un char que son dos bytes,lo mismo que dos bool
                    buffer.setLength(SIZE_SOLO_SILLETA_PASAJERO);
                    rafPasajeros.writeChars(buffer.toString());
                }
                if (p.getTarjeta() != null) {
                    TarjetaEmbarque tarjeta = p.getTarjeta();
                    posicionDeEscritura = SIZE_TOTAL_TARJETA * tarjeta.getId();
                    rafTarjetasEmbarque.seek(posicionDeEscritura);
                    //id
                    rafTarjetasEmbarque.writeInt(tarjeta.getId());
                    //vuelo
                    buffer = new StringBuffer(tarjeta.getVuelo());
                    buffer.setLength(SIZE_VUELO_TARJETA / 2);
                    rafTarjetasEmbarque.writeChars(buffer.toString());
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void modificarReserva(Reserva r) throws DAOException {
        this.crearReserva(r);
    }

    @Override
    public Reserva obtenerReserva(int id) throws DAOException {
        try (RandomAccessFile rafReservas = new RandomAccessFile(archivoReservas, "r");
             RandomAccessFile rafPasajeros = new RandomAccessFile(archivoPasajeros, "r");
             RandomAccessFile rafTarjetas = new RandomAccessFile(archivoTarjetasEmbarque, "r")) {
            long posicionDeLectua = SIZE_TOTAL_RESERVA * id;
            rafReservas.seek(posicionDeLectua);
            //id
            //Puede que me pasen una id la cual no exista,en cuyo caso a ir a la posicion y intentar leer nos saltará una excepcion
            //la controlamos y devolvemos null,es decir,no existe esa reserva
            Integer idReserva;
            try {
                idReserva = rafReservas.readInt();
            } catch (EOFException eofe) {
                return null;
            }
            //fecha
            char[] lectorString = new char[SIZE_FECHA_RESERVA / 2];
            for (int i = 0; i < lectorString.length; i++) {
                lectorString[i] = rafReservas.readChar();
            }
            String cadenaFecha = new String(lectorString);
            cadenaFecha = cadenaFecha.trim();
            Date fechaReserva = new SimpleDateFormat("dd/MM/yyyy").parse(cadenaFecha);
            //importe
            double importeReserva = rafReservas.readDouble();
            //Vuelo
            lectorString = new char[SIZE_VUELO_RESERVA / 2];
            for (int i = 0; i < lectorString.length; i++) {
                lectorString[i] = rafReservas.readChar();
            }
            Vuelo vueloReserva = new VuelosRafDAO().obtenerVuelo(new String(lectorString).trim());
            //cancelada
            boolean canceladaReserva = rafReservas.readBoolean();
            /** Pasajeros*/
            List<Pasajero> pasajeros = new ArrayList<>();
            posicionDeLectua = 0;
            rafPasajeros.seek(posicionDeLectua);
            while (rafPasajeros.getFilePointer() != rafPasajeros.length()) {
                int idReservaPasajero = rafPasajeros.readInt();
                if (idReservaPasajero == idReserva) {
                    //id
                    int idPasajero = rafPasajeros.readInt();
                    //dni
                    lectorString = new char[SIZE_DNI_PASAJERO / 2];
                    for (int i = 0; i < lectorString.length; i++) {
                        lectorString[i] = rafPasajeros.readChar();
                    }
                    String dniPasajero = new String(lectorString).trim();
                    //nombre
                    lectorString = new char[SIZE_NOMBRE_PASAJERO / 2];
                    for (int i = 0; i < lectorString.length; i++) {
                        lectorString[i] = rafPasajeros.readChar();
                    }
                    String nombrePasajero = new String(lectorString).trim();
                    //apellidos
                    lectorString = new char[SIZE_APELLIDO_PASAJERO / 2];
                    for (int i = 0; i < lectorString.length; i++) {
                        lectorString[i] = rafPasajeros.readChar();
                    }
                    String apellidosPasajero = new String(lectorString).trim();
                    //fechaNacimiento
                    lectorString = new char[SIZE_FECHA_PASAJERO / 2];
                    for (int i = 0; i < lectorString.length; i++) {
                        lectorString[i] = rafPasajeros.readChar();
                    }
                    cadenaFecha=new String(lectorString).trim();
                    Date fechaPasajero = new SimpleDateFormat("dd/MM/yyyy").parse(cadenaFecha);
                    //numMaletas
                    int numMaletas = rafPasajeros.readInt();
                    //Adulto y ninyo
                    lectorString = new char[SIZE_DESCUENTO_PASAJERO / 2];
                    for (int i = 0; i < lectorString.length; i++) {
                        lectorString[i] = rafPasajeros.readChar();
                    }
                    String descuento = new String(lectorString).trim();
                    if (descuento.isEmpty()) {
                        boolean solo = rafPasajeros.readBoolean();
                        boolean silleta = rafPasajeros.readBoolean();
                        pasajeros.add(new Ninyo(idPasajero, dniPasajero, nombrePasajero, apellidosPasajero, fechaPasajero, numMaletas, solo, silleta));
                    } else {
                        Descuentos d = Descuentos.NINGUNO;
                        if (Descuentos.RESIDENTE_ISLA.name().equals(descuento)) {
                            d = Descuentos.RESIDENTE_ISLA;
                        }
                        pasajeros.add(new Adulto(idPasajero, dniPasajero, nombrePasajero, apellidosPasajero, fechaPasajero, numMaletas, d));
                        rafPasajeros.skipBytes(SIZE_SOLO_SILLETA_PASAJERO);
                        rafPasajeros.skipBytes(SIZE_SOLO_SILLETA_PASAJERO);
                    }
                } else {
                    posicionDeLectua = rafPasajeros.getFilePointer() + (SIZE_TOTAL_PASAJERO - SIZE_ID_RESERVA);
                    rafPasajeros.seek(posicionDeLectua);
                }
            }
            posicionDeLectua = 0;
            rafTarjetas.seek(posicionDeLectua);
            while (rafTarjetas.getFilePointer() != rafTarjetas.length()) {
                    int idTarjeta = rafTarjetas.readInt();
                if (idTarjeta == 0) {
                    posicionDeLectua = rafTarjetas.getFilePointer() + (SIZE_TOTAL_TARJETA - SIZE_ID_TARJETA);
                    rafTarjetas.seek(posicionDeLectua);
                } else {
                    lectorString = new char[SIZE_VUELO_TARJETA / 2];
                    for (int i = 0; i < lectorString.length; i++) {
                        lectorString[i] = rafTarjetas.readChar();
                    }
                    String codigoVuelo = new String(lectorString).trim();
                    Vuelo v = new VuelosRafDAO().obtenerVuelo(codigoVuelo);

                    for (int j = 0; j < pasajeros.size(); j++) {
                        if (pasajeros.get(j).getId() == (idTarjeta)) {
                            pasajeros.get(j).setTarjeta(new TarjetaEmbarque(idTarjeta, v.getCodigo(), v.getOrigen(), v.getDestino(), v.getFechaVuelo(), v.getTerminal(), v.getPuerta()));
                            break;
                        }
                    }
                }
            }
            return new Reserva(idReserva, fechaReserva, canceladaReserva, pasajeros, vueloReserva, importeReserva);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void crearOrdenPago(OrdenPago orden) throws DAOException {
        try (RandomAccessFile rafOrdenesPago = new RandomAccessFile(archivoOrdenesPago, "rw")) {
            rafOrdenesPago.seek(orden.getId()*SIZE_ORDEN_PAGO_TOTAL);
            //id
            rafOrdenesPago.writeInt(orden.getId());
            //fecha
            StringBuffer buffer;
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            buffer = new StringBuffer(sdf.format(orden.getFechaOrden()));
            buffer.setLength(SIZE_ORDEN_PAGO_FECHA/2);
            rafOrdenesPago.writeChars(buffer.toString());
            //dni
            buffer=new StringBuffer(orden.getDniOrden());
            buffer.setLength(SIZE_ORDEN_PAGO_DNI/2);
            rafOrdenesPago.writeChars(buffer.toString());
            //tarjeta
            buffer=new StringBuffer(orden.getNumTarjeta());
            buffer.setLength(SIZE_ORDEN_PAGO_TARJETA/2);
            rafOrdenesPago.writeChars(buffer.toString());
            //importe
            rafOrdenesPago.writeDouble(orden.getImporte());
            //idReserva
            rafOrdenesPago.writeInt(orden.getReserva().getId());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public OrdenPago obtenerOrdenPago(int id) throws DAOException {
        try(RandomAccessFile rafOrdenesPago = new RandomAccessFile(archivoOrdenesPago, "r")){
            rafOrdenesPago.seek(id*SIZE_ORDEN_PAGO_TOTAL);
            //id
            int idOrden;
            try {
                idOrden= rafOrdenesPago.readInt();
            }catch (EOFException eofe){
                return null;
            }
            if(idOrden==0){
                return null;
            }
            //fecha
            char[] lectorString=new char[SIZE_ORDEN_PAGO_FECHA/2];
            for (int i = 0; i < lectorString.length; i++) {
                lectorString[i] = rafOrdenesPago.readChar();
            }
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date fecha=sdf.parse(new String(lectorString).trim());
            //dni
            lectorString=new char[SIZE_ORDEN_PAGO_DNI/2];
            for (int i = 0; i < lectorString.length; i++) {
                lectorString[i] = rafOrdenesPago.readChar();
            }
            String dni=new String(lectorString).trim();
            //tarjeta
            lectorString=new char[SIZE_ORDEN_PAGO_TARJETA/2];
            for (int i = 0; i < lectorString.length; i++) {
                lectorString[i] = rafOrdenesPago.readChar();
            }
            String tarjeta=new String(lectorString).trim();
            //importe
            double importe=rafOrdenesPago.readDouble();
            //idReserva
            int idReserva=rafOrdenesPago.readInt();
            Reserva r=this.obtenerReserva(idReserva);
            return new OrdenPago(idOrden,fecha,dni,tarjeta,importe,r);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<OrdenPago> obtenerOrdenPago() throws DAOException {
        List<OrdenPago> ordenes=new ArrayList<>();
        try(RandomAccessFile rafOrdenesPago = new RandomAccessFile(archivoOrdenesPago, "r")){
            rafOrdenesPago.seek(0);
            while (rafOrdenesPago.getFilePointer() != rafOrdenesPago.length()) {
                int id=rafOrdenesPago.readInt();
                if(id==0){
                    long posicionDeLectura=(rafOrdenesPago.getFilePointer()+(SIZE_ORDEN_PAGO_TOTAL-SIZE_ORDEN_PAGO_ID));
                    rafOrdenesPago.seek(posicionDeLectura);
                }else{
                    ordenes.add(this.obtenerOrdenPago(id));
                    long posicionDeLectura=(rafOrdenesPago.getFilePointer()+(SIZE_ORDEN_PAGO_TOTAL-SIZE_ORDEN_PAGO_ID));
                    rafOrdenesPago.seek(posicionDeLectura);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  ordenes;
    }

    @Override
    public boolean eliminarReserva(int id) throws DAOException {
        try (RandomAccessFile rafReservas = new RandomAccessFile(archivoReservas, "rw");
             RandomAccessFile rafPasajeros = new RandomAccessFile(archivoPasajeros, "rw");
             RandomAccessFile rafTarjetasEmbarque = new RandomAccessFile(archivoTarjetasEmbarque, "rw")) {
            /**Reservas*/
            int posicionDeEscritura = SIZE_TOTAL_RESERVA * id;
            rafReservas.seek(posicionDeEscritura);
            //id
            rafReservas.writeInt(0);
            /**Pasajeros*/
            Reserva r=this.obtenerReserva(id);
            List<Pasajero> pasajeros = r.getPasajeros();
            for (Pasajero p : pasajeros) {
                posicionDeEscritura = SIZE_TOTAL_PASAJERO * p.getId();
                rafPasajeros.seek(posicionDeEscritura);
                //id Reserva
                rafPasajeros.writeInt(0);
                //id
                rafPasajeros.writeInt(0);
                /**Tarjeta embarque*/
                if (p.getTarjeta() != null) {
                    TarjetaEmbarque tarjeta = p.getTarjeta();
                    posicionDeEscritura = SIZE_TOTAL_TARJETA * tarjeta.getId();
                    rafTarjetasEmbarque.seek(posicionDeEscritura);
                    //id
                    rafTarjetasEmbarque.writeInt(0);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Miramos a ver si se ha borrado
        if(this.obtenerReserva(id)==null){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public List<Reserva> obtenerTodasReservas() throws DAOException {
        List<Reserva> reservas = new ArrayList<>();
        try (RandomAccessFile rafReservas = new RandomAccessFile(archivoReservas, "r")) {
            rafReservas.seek(0);
            while (rafReservas.getFilePointer() != rafReservas.length()) {
                Integer idReserva = rafReservas.readInt();
                if (idReserva == 0) {
                    long posicionDeLectua = rafReservas.getFilePointer() + (SIZE_TOTAL_RESERVA - SIZE_ID_RESERVA);
                    rafReservas.seek(posicionDeLectua);
                } else {
                    reservas.add(this.obtenerReserva(idReserva));
                    long posicionDeLectua = rafReservas.getFilePointer() + (SIZE_TOTAL_RESERVA - SIZE_ID_RESERVA);
                    rafReservas.seek(posicionDeLectua);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reservas;
    }

    @Override
    public List<Reserva> obtenerTodasReservas(Date d) throws DAOException {
        List<Reserva> reservas = new ArrayList<>();
        for (Reserva r : this.obtenerTodasReservas()) {
            if (r.getFecha().equals(d)) {
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
}
