/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.severo.vuelos.estructura;


public enum Descuentos {
    NINGUNO,
    RESIDENTE_ISLA,
    RES_ANT;

    @Override
    public String toString() {
        return this.name();
    }
    
}


