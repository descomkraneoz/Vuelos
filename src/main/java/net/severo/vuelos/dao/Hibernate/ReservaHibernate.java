package net.severo.vuelos.dao.Hibernate;

import net.severo.vuelos.dao.DAOException;
import net.severo.vuelos.dao.IReservaDAO;
import net.severo.vuelos.estructura.OrdenPago;
import net.severo.vuelos.estructura.Reserva;

import java.util.Date;
import java.util.List;

public class ReservaHibernate implements IReservaDAO {
    @Override
    public void crearReserva(Reserva r) throws DAOException {

    }

    @Override
    public void crearOrdenPago(OrdenPago orden) throws DAOException {

    }

    @Override
    public void modificarReserva(Reserva r) throws DAOException {

    }

    @Override
    public Reserva obtenerReserva(int id) throws DAOException {
        return null;
    }

    @Override
    public OrdenPago obtenerOrdenPago(int id) throws DAOException {
        return null;
    }

    @Override
    public List<OrdenPago> obtenerOrdenPago() throws DAOException {
        return null;
    }

    @Override
    public boolean eliminarReserva(int id) throws DAOException {
        return false;
    }

    @Override
    public List<Reserva> obtenerTodasReservas() throws DAOException {
        return null;
    }

    @Override
    public List<Reserva> obtenerTodasReservas(Date d) throws DAOException {
        return null;
    }

    @Override
    public List<Reserva> obtenerTodasReservas(String idVuelo) throws DAOException {
        return null;
    }
}
