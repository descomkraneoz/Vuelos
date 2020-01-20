/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.severo.vuelos.estructura;

import java.util.Date;


public class Pasajero {
    protected int id;
    protected String dni;
    protected String nombre;
    protected String apellidos;
    protected Date fechaNacimiento;
    protected int numMaletasFacturar;
    protected String tipo;
    protected boolean solo;
    protected boolean silleta;
    protected String descuento;
    protected TarjetaEmbarque tarjeta;

    Pasajero(int id, String dni, String nombre, String apellidos, Date fecha_nacimiento, int num_maletas_facturar) {
        this.id = id;
        this.dni = dni;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.fechaNacimiento = fecha_nacimiento;
        this.numMaletasFacturar = num_maletas_facturar;
        this.tarjeta=null;
    }

    public Pasajero() {

    }


    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public boolean isSolo() {
        return solo;
    }

    public void setSolo(boolean solo) {
        this.solo = solo;
    }

    public boolean isSilleta() {
        return silleta;
    }

    public void setSilleta(boolean silleta) {
        this.silleta = silleta;
    }

    public String getDescuento() {
        return descuento;
    }

    public void setDescuento(String descuento) {
        this.descuento = descuento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public Date getFecha_nacimiento() {
        return fechaNacimiento;
    }

    public void setFecha_nacimiento(Date fecha_nacimiento) {
        this.fechaNacimiento = fecha_nacimiento;
    }

    public int getNum_maletas_facturar() {
        return numMaletasFacturar;
    }

    public void setNum_maletas_facturar(int num_maletas_facturar) {
        this.numMaletasFacturar = num_maletas_facturar;
    }

    public TarjetaEmbarque getTarjeta() {
        return tarjeta;
    }

    public void setTarjeta(TarjetaEmbarque tarjeta) {
        this.tarjeta = tarjeta;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + this.id;
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
        final Pasajero other = (Pasajero) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
    
   
    
    
}
