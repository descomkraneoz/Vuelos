/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.severo.vuelos.dao;

import net.severo.vuelos.estructura.Vuelo;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Usuario
 */
public interface IVuelosDAO {
    void crearVuelo (Vuelo vuelo)  throws DAOException ;
    void modificarVuelo(Vuelo v)  throws DAOException ;
    void eliminarVuelo(String codigo)  throws DAOException ;
    Vuelo obtenerVuelo(String codigo)  throws DAOException ;
    List<Vuelo> obtenerVuelos(Date fechaSalida)  throws DAOException ;
    List<Vuelo> obtenerVuelos()  throws DAOException ;
    
    
    
}
