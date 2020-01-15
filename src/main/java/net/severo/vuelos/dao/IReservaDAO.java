/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.severo.vuelos.dao;

import java.util.Date;
import java.util.List;
import net.severo.vuelos.estructura.OrdenPago;
import net.severo.vuelos.estructura.Reserva;

/**
 *
 * @author Usuario
 */
public interface IReservaDAO {
    void crearReserva(Reserva r)throws DAOException ;
    void crearOrdenPago(OrdenPago orden) throws DAOException ;
    void modificarReserva(Reserva r)throws DAOException ;
    Reserva obtenerReserva(int id)throws DAOException ;
    OrdenPago obtenerOrdenPago(int id)throws DAOException ;
    List<OrdenPago> obtenerOrdenPago()throws DAOException ;
    boolean eliminarReserva(int id)throws DAOException ;
    List<Reserva> obtenerTodasReservas()throws DAOException ;
    List<Reserva> obtenerTodasReservas(Date d)throws DAOException ;
    List<Reserva> obtenerTodasReservas(String idVuelo)throws DAOException ;
    
}
