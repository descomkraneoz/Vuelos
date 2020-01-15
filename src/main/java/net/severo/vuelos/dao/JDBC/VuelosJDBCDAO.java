package net.severo.vuelos.dao.JDBC;

import net.severo.vuelos.dao.DAOException;
import net.severo.vuelos.dao.IVuelosDAO;
import net.severo.vuelos.estructura.Vuelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VuelosJDBCDAO implements IVuelosDAO {

    static String getAllVuelos="SELECT * FROM vuelos;";
    static String getVuelos="SELECT * FROM vuelos WHERE codVuelo=?;";
    static String insertVuelos="INSERT INTO vuelos(codVuelo,origen,destino,precio,fecha,plazas) VALUES (?,?,?,?,?,?);";
    static String deleteVuelos="DELETE FROM vuelos WHERE codVuelo=?;";
    static String modVuelos="UPDATE FROM vuelos WHERE codVuelo=?;";

    public VuelosJDBCDAO() throws DAOException {

        //acedemos al singleton ahora por si hay fallos que salten aqui
        ConexionJDBC.getInstance().getConnection();

    }

    /**
     * private String codigo;
     *     private String origen;
     *     private String destino;
     *     private double precioPersona;
     *     private Date fechaVuelo;
     *     private int plazasDisponibles;
     *     private int terminal;
     *     private int puerta;
     */


    @Override
    public void crearVuelo(Vuelo vuelo) throws DAOException {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = ConexionJDBC.getInstance().getConnection();

            ps = conn.prepareStatement(insertVuelos);
            ps.setString(1, vuelo.getCodigo());
            ps.setString(2,vuelo.getOrigen());
            ps.setString(3,vuelo.getDestino());
            ps.setDouble(4,vuelo.getPrecioPersona());
            ps.setDate(5, new java.sql.Date(vuelo.getFechaVuelo().getTime()));
            ps.setInt(6, vuelo.getPlazasDisponibles());


            @SuppressWarnings("unused")
            int afectadas = ps.executeUpdate();
            //Este entero no lo vamos a usar pero devuelve el número de flas aceptadas
            //En otras ocasiones nos puede ser útil, aquí siempre debe devolver 1



        } catch (Exception ex) {
            throw new DAOException("Ha habido un problema al insertar el vuelo: ",ex);
        } finally {
            try {
                ps.close();

            } catch (SQLException sqlex) {
                throw new DAOException("Error al cerrar la sentencia",sqlex);
            }
        }
    }

    @Override
    public void modificarVuelo(Vuelo vuelo) throws DAOException {
        //eliminarVuelo(v.getCodigo());
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = ConexionJDBC.getInstance().getConnection();

            ps = conn.prepareStatement(modVuelos);
            ps.setString(1, vuelo.getCodigo());
            ps.setString(2,vuelo.getOrigen());
            ps.setString(3,vuelo.getDestino());
            ps.setDouble(4,vuelo.getPrecioPersona());
            ps.setDate(5, new java.sql.Date(vuelo.getFechaVuelo().getTime()));
            ps.setInt(6, vuelo.getPlazasDisponibles());
            ps.setInt(7, vuelo.getTerminal());
            ps.setInt(8, vuelo.getPuerta());


            @SuppressWarnings("unused")
            int afectadas = ps.executeUpdate();
            //Este entero no lo vamos a usar pero devuelve el número de flas aceptadas
            //En otras ocasiones nos puede ser útil, aquí siempre debe devolver 1



        } catch (Exception ex) {
            throw new DAOException("Ha habido un problema al modificar el vuelo: ",ex);
        } finally {
            try {
                ps.close();

            } catch (SQLException sqlex) {
                throw new DAOException("Error al cerrar la sentencia",sqlex);
            }
        }

    }

    @Override
    public void eliminarVuelo(String codigo) throws DAOException {
        //Elimina un vuelo de la BD

        Connection conn = null;
        PreparedStatement ps = null;

        try {


            ps = conn.prepareStatement(deleteVuelos);
            ps.setString(1, codigo);

            ps.executeUpdate();



        } catch (Exception ex) {
            throw new DAOException("Ha habido un problema al eliminar el vuelo: ",ex);
        } finally {
            try {
                ps.close();
                conn.close();
            } catch (SQLException ex) {
                throw new DAOException("Error al cerrar la base de datos",ex);
            }

        }

    }

    @Override
    public Vuelo obtenerVuelo(String codigo) throws DAOException {
        //Obtiene de la BD el vuelo con codigo pasado por parametro
        Vuelo j= new Vuelo();
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = ConexionJDBC.getInstance().getConnection();

            ps = conn.prepareStatement(getVuelos);
            ps.setString(1, codigo);

            ResultSet rs = ps.executeQuery(); //el string se transforma en una sentencia de la bd, un query, se guarda en rs
            if(!rs.next()){
                // Nos metemos aquí si la consulta no devuelve nada
                return null;
            }

            String codVuelo = rs.getString("codVuelo"); //es string codvuelo
            String origen = rs.getString("origen");
            String destino = rs.getString("destino");
            double precio=rs.getDouble("precio");
            java.util.Date fecha=new java.util.Date(rs.getDate("fecha").getTime());
            int plazas=rs.getInt("plazas");
            int terminal=rs.getInt("terminal");
            int puerta=rs.getInt("puerta");



            j.setCodigo(codVuelo);
            j.setOrigen(origen);
            j.setDestino(destino);
            j.setPrecioPersona(precio);
            j.setFechaVuelo(fecha);
            j.setPlazasDisponibles(plazas);
            j.setTerminal(terminal);
            j.setPuerta(puerta);


            return j; //devuelve el primer vuelo


        } catch (Exception e) {
            throw new DAOException("Ha habido un problema al obtener el vuelo: ",e);
        } finally {
            try {
                ps.close();

            } catch (SQLException ex) {
                throw new DAOException("Error al cerrar la base de datos",ex);
            }

        }
    }

    @Override
    public List<Vuelo> obtenerVuelos(Date fechaSalida) throws DAOException {
        return null;
    }



    @Override
    public List<Vuelo> obtenerVuelos() throws DAOException {

        //Obtiene de la BD la lista de todos las vuelos y devuelve dicha lista
        List<Vuelo> vuelos = new ArrayList<Vuelo>();
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = ConexionJDBC.getInstance().getConnection();

            ps = conn.prepareStatement(getAllVuelos);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Vuelo  j = new Vuelo();
                String codVuelo = rs.getString("codVuelo"); //es string codvuelo
                String origen = rs.getString("origen");
                String destino = rs.getString("destino");
                double precio=rs.getDouble("precio");
                java.util.Date fecha=new java.util.Date(rs.getDate("fecha").getTime());
                int plazas=rs.getInt("plazas");
                int terminal=rs.getInt("terminal");
                int puerta=rs.getInt("puerta");

                j.setCodigo(codVuelo);
                j.setOrigen(origen);
                j.setDestino(destino);
                j.setPrecioPersona(precio);
                j.setFechaVuelo(fecha);
                j.setPlazasDisponibles(plazas);
                j.setTerminal(terminal);
                j.setPuerta(puerta);

                vuelos.add(j);

            }
            return vuelos;


        } catch (Exception e) {
            throw new DAOException("Ha habido un problema al obtener la lista de vuelos: ",e);
        } finally {
            try {
                ps.close();

            } catch (SQLException ex) {
                throw new DAOException("Error al cerrar la base de datos",ex);
            }

        }
    }


    public void finalizar() throws  DAOException {
        try {
            ConexionJDBC.getInstance().getConnection().close();
        } catch (SQLException ex) {
            throw new DAOException("Error al finalizar la conexion con la base de datos", ex);
        }

    }

    public void iniciarTransaccion() throws DAOException {
        try {
            ConexionJDBC.getInstance().getConnection().setAutoCommit(false); //deja en espera a la base de datos para que no haga comit
        } catch (SQLException ex) {
            throw new DAOException("Error al inicair la transaccion", ex);
        }
    }

    public void finalizarTransaccion() throws DAOException {
        try {
            ConexionJDBC.getInstance().getConnection().commit();
        } catch (SQLException ex) {
            try {
                ConexionJDBC.getInstance().getConnection().rollback();
            } catch (SQLException ex1) {
                throw new DAOException("No se ha podido finalizar la transsaccion. Se han desecho los cambios", ex);
            }
        }
    }
}
