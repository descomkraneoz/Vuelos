/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.severo.vuelos.estructura;

import java.util.Calendar;
import java.util.List;
import java.util.Date;

/**
 *
 * @author
 */
public class Reserva {

    private int id;
    private Date fecha;
    private boolean cancelada;
    private List<Pasajero> pasajeros;
    private Vuelo vuelo;
    private double importe;

    public Reserva(int id, Date fecha, boolean cancelada, List<Pasajero> pasajeros, Vuelo vuelo) {
        this(id, fecha, cancelada, pasajeros, vuelo, 0);
        this.setImporte(this.calcularImporte());
    }

    public Reserva(int id, Date fecha, List<Pasajero> pasajeros, Vuelo vuelo) {
        this(id, fecha, false, pasajeros, vuelo, 0);
        this.setImporte(this.calcularImporte());
    }

    public Reserva(int id, Date fecha, boolean cancelada, List<Pasajero> pasajeros, Vuelo vuelo, double importe) {
        this.id = id;
        this.fecha = fecha;
        this.cancelada = cancelada;
        this.pasajeros = pasajeros;
        this.vuelo = vuelo;
        this.importe = importe;
    }

    public Reserva() {
    }

    public double getImporte() {
        return importe;
    }

    public void setImporte(double importe) {
        this.importe = importe;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public boolean isCancelada() {
        return cancelada;
    }

    public void setCancelada(boolean cancelada) {
        this.cancelada = cancelada;
    }

    public List<Pasajero> getPasajeros() {
        return pasajeros;
    }

    public void setPasajeros(List<Pasajero> pasajeros) {
        this.pasajeros = pasajeros;
    }

    public Vuelo getVuelo() {
        return vuelo;
    }

    public void setVuelo(Vuelo vuelo) {
        this.vuelo = vuelo;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Reserva other = (Reserva) obj;
        return this.id == other.id;
    }

    public double calcularImporte() {
        double importe = 0;

        Configuracion configuracion = new Configuracion("/configuracion.properties");
        double importePersona = vuelo.getPrecioPersona();
        for (Pasajero p : this.pasajeros) {
            importePersona = vuelo.getPrecioPersona();
            if (p instanceof Ninyo) {
                if (((Ninyo) p).getSolo()) {
                    importePersona += configuracion.getCosteNinyoSolo();
                }
                if (((Ninyo) p).getSilleta()) {
                    importePersona += configuracion.getCosteSilleta();
                }
                importe += importePersona * (1 - (configuracion.getDescuentoNinyo() / 100));
            } else if (p instanceof Adulto) {
                if (((Adulto) p).getDescuento().equals(Descuentos.RESIDENTE_ISLA)) {
                    importe += importePersona * (1 - (configuracion.getDescuentoResidenteIsla() / 100));
                } else if (this.tieneDescuentoPorAnterioridad()) {
                    importe += importePersona * (1 - (configuracion.getDescuentoReservaConAnterioridad() / 100));
                } else {
                    importe += importePersona;
                }
            }
        }
        return importe;
    }

    private boolean tieneDescuentoPorAnterioridad() {
        Calendar fechaDeCorte = Calendar.getInstance();
        Calendar fechaVuelo = Calendar.getInstance();
        fechaVuelo.setTime(this.getVuelo().getFechaVuelo());
        fechaDeCorte.setTime(new Date());
        fechaDeCorte.add(Calendar.MONTH, -2);
        return fechaDeCorte.before(fechaVuelo);
    }

    public void generarTarjetas() {
        for (Pasajero p : pasajeros) {
            p.setTarjeta(new TarjetaEmbarque(p.getId(), vuelo.getCodigo(), vuelo.getOrigen(), vuelo.getDestino(), vuelo.getFechaVuelo(), vuelo.getTerminal(), vuelo.getPuerta()));
        }
    }

    public double calcularCancelacion() {
        Configuracion configuracion = new Configuracion("/configuracion.properties");
        if(cancelado15Dias(this.getFecha())){
            return importe*(configuracion.getGastosCancelacionAntesDe15Dias()/100);
        
        }else if(cancelado1Mes(this.getFecha())){
            return importe*(configuracion.getGastosCancelacionAntesDeUnMes()/100);
        }else{
            return 0;
        }
    }
    
    private boolean cancelado15Dias(Date fechaReserva){
        Calendar fechaReservaCalendario=Calendar.getInstance();
        fechaReservaCalendario.setTime(fechaReserva);
        Calendar fechaCorte=Calendar.getInstance();
        fechaCorte.setTime(new Date());
        fechaCorte.add(Calendar.DAY_OF_MONTH, -15);
        return fechaCorte.before(fechaReservaCalendario);
    }

    private boolean cancelado1Mes(Date fechaReserva) {
        Calendar fechaReservaCalendario=Calendar.getInstance();
        fechaReservaCalendario.setTime(fechaReserva);
        Calendar fechaCorte=Calendar.getInstance();
        fechaCorte.setTime(new Date());
        fechaCorte.add(Calendar.MONTH, -1);
        return fechaCorte.before(fechaReservaCalendario);
    }
}
