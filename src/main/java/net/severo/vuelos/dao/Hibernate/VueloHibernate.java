package net.severo.vuelos.dao.Hibernate;

import net.severo.vuelos.dao.DAOException;
import net.severo.vuelos.dao.IVuelosDAO;
import net.severo.vuelos.estructura.Vuelo;

import java.util.Date;
import java.util.List;

public class VueloHibernate implements IVuelosDAO {
    @Override
    public void crearVuelo(Vuelo vuelo) throws DAOException {

    }

    @Override
    public void modificarVuelo(Vuelo v) throws DAOException {

    }

    @Override
    public void eliminarVuelo(String codigo) throws DAOException {

    }

    @Override
    public Vuelo obtenerVuelo(String codigo) throws DAOException {
        return null;
    }

    @Override
    public List<Vuelo> obtenerVuelos(Date fechaSalida) throws DAOException {
        return null;
    }

    @Override
    public List<Vuelo> obtenerVuelos() throws DAOException {
        return null;
    }
}
