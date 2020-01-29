/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.severo.vuelos.estructura;

import java.util.Date;


public class Ninyo extends Pasajero {
    private boolean solo;
    private boolean silleta;

    public Ninyo( int id, String dni, String nombre, String apellidos, Date fecha_nacimiento, int num_maletas_facturar,boolean solo, boolean silleta) {
        super(id, dni, nombre, apellidos, fecha_nacimiento, num_maletas_facturar);
        this.solo = solo;
        this.silleta = silleta;
    }

    public Ninyo() {

    }

    public boolean getSolo() {
        return solo;
    }

    public void setSolo(boolean solo) {
        this.solo = solo;
    }

    public boolean getSilleta() {
        return silleta;
    }

    public void setSilleta(boolean silleta) {
        this.silleta = silleta;
    }
    
    
}
