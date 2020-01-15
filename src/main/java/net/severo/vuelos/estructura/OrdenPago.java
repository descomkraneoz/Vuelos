/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.severo.vuelos.estructura;
import java.util.Date;

/**
 *
 * @author Usuario
 */
public class OrdenPago {
    private int id;
    private Date fechaOrden;
    private String dniOrden;
    private String numTarjeta;
    private double importe;
    private Reserva reserva;

    public OrdenPago(int id, Date fechaOrden, String dniOrden, String numTarjeta, double importe, Reserva reserva) {
        this.id = id;
        this.fechaOrden = fechaOrden;
        this.dniOrden = dniOrden;
        this.numTarjeta = numTarjeta;
        this.importe = importe;
        this.reserva = reserva;
    }

    public OrdenPago(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getFechaOrden() {
        return fechaOrden;
    }

    public void setFechaOrden(Date fechaOrden) {
        this.fechaOrden = fechaOrden;
    }

    public String getDniOrden() {
        return dniOrden;
    }

    public void setDniOrden(String dniOrden) {
        this.dniOrden = dniOrden;
    }

    public String getNumTarjeta() {
        return numTarjeta;
    }

    public void setNumTarjeta(String numTarjeta) {
        this.numTarjeta = numTarjeta;
    }

    public double getImporte() {
        return importe;
    }

    public void setImporte(double importe) {
        this.importe = importe;
    }

    public Reserva getReserva() {
        return reserva;
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }
    
    
}
