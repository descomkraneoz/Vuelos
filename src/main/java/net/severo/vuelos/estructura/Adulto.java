/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.severo.vuelos.estructura;
import java.util.Date;


public class Adulto extends Pasajero{
    private Descuentos descuento;

    public Adulto( int id, String dni, String nombre, String apellidos, Date fecha_nacimiento, int num_maletas_facturar,Descuentos descuento) {
        super(id, dni, nombre, apellidos, fecha_nacimiento, num_maletas_facturar);
        this.descuento = descuento;
    }

    public Descuentos getDescuento() {
        return descuento;
    }

    public void setDescuento(Descuentos descuento) {
        this.descuento = descuento;
    }


}

