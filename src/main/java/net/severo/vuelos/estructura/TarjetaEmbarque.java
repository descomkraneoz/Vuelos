/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.severo.vuelos.estructura;

import java.util.Date;

/**
 *
 * @author
 */
public class TarjetaEmbarque {
   private int id;
   private String vuelo;
   private String origen;
   private String destino;
   private Date fechaVuelo;
   private int terminal;
   private int puerta;
   
    public TarjetaEmbarque(int id,String vuelo,String origen,String destino,Date fecha_vuelo,int terminal,int puerta){
        this.id=id;
        this.vuelo=vuelo;
        this.origen=origen;
        this.destino=destino;
        this.fechaVuelo=fecha_vuelo;
        this.terminal=terminal;
        this.puerta=puerta;
};
    public int getId(){
        return id;
    }
    public String getVuelo(){
        return vuelo;
    }
    public String getOrigen(){
        return origen;
    }
    public String getDestino(){
        return destino;
    }

    public Date getFechaVuelo() {
        return fechaVuelo;
    }
    public int getTerminal(){
        return terminal;
    }
    public int getPuerta(){
        return puerta;
    }
    
    public void setId(int id){
        this.id=id;
    }

    public void setVuelo(String vuelo) {
        this.vuelo = vuelo;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public void setFechaVuelo(Date fecha_vuelo) {
        this.fechaVuelo = fecha_vuelo;
    }

    public void setTerminal(int terminal) {
        this.terminal = terminal;
    }

    public void setPuerta(int puerta) {
        this.puerta = puerta;
    }
   
   

}
