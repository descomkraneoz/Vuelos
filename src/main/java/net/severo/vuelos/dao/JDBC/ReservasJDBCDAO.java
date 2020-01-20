package net.severo.vuelos.dao.JDBC;

import net.severo.vuelos.dao.DAOException;
import net.severo.vuelos.dao.IReservaDAO;
import net.severo.vuelos.estructura.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReservasJDBCDAO implements IReservaDAO {
    static String getAllReservas="SELECT * FROM reservas;";
    static String getReserva="SELECT * FROM reservas WHERE idReserva=?;";
    static String getReservaVuelo = "SELECT * FROM reservas WHERE vuelo=?;";
    static String insertReserva="INSERT INTO reservas(idReserva,fecha,importe,vuelo,cancelada) VALUES (?,?,?,?,?);";
    static String deleteReserva="DELETE FROM reservas WHERE idReserva=?;";
    static String modReserva="UPDATE FROM reservas WHERE idReserva=?;";

    static String insertPasajero = "INSERT INTO pasajeros(idPasajero, dni, nombre, apellidos, fechaNacimiento, numMaletas, reserva, tipo, solo, silleta, descuento) VALUES (?,?,?,?,?,?,?,?,?,?,?);";
    static String getAllPasajero = "SELECT * FROM pasajeros WHERE idPasajero=?;";

    static String getAllOrdenPago="SELECT * FROM orden_pago;";
    static String getOrdenPago="SELECT * FROM orden_pago WHERE idOrden=?;";
    static String insertOrdenPago="INSERT INTO orden_pago(idOrden,fecha,dni,numTarjeta,importe,reserva) VALUES (?,?,?,?,?,?);";

    static String insertTarjeta = "INSERT INTO tarjetas(idTarjeta, vuelo, origen, destino, fecha, terminal, puerta) VALUES (?,?,?,?,?,?,?);";

    public ReservasJDBCDAO() throws DAOException {

        //acedemos al singleton ahora por si hay fallos que salten aqui
        ConexionJDBC.getInstance().getConnection();

    }

    /**
     * idReserva,--->int
     * fecha,---->date
     * importe,----->double
     * vuelo,----> object
     * cancelada---->boolean
     */


    @Override
    public void crearReserva(Reserva r) throws DAOException {
        Connection conn = null;
        PreparedStatement ps = null;
        ArrayList<Pasajero> pasajeros;

        try {
            conn = ConexionJDBC.getInstance().getConnection();
            ps = conn.prepareStatement(insertReserva);

            //reserva
            ps.setInt(1, r.getId());
            ps.setDate(2, new java.sql.Date(r.getFecha().getTime()));
            ps.setDouble(3,r.getImporte());
            ps.setObject(4,r.getVuelo());
            ps.setBoolean(5, r.isCancelada());

            //pasajero
            pasajeros = new ArrayList<>();
            pasajeros = (ArrayList<Pasajero>) r.getPasajeros();
            for (Pasajero p : pasajeros) {
                ps = conn.prepareStatement(insertPasajero);
                //id Reserva
                ps.setInt(7, r.getId());
                //id
                ps.setInt(1, p.getId());
                //dni
                ps.setString(2, p.getDni());
                //nombre
                ps.setString(3, p.getNombre());
                //apellidos
                ps.setString(4, p.getApellidos());
                //fecha
                ps.setDate(5, new java.sql.Date(p.getFecha_nacimiento().getTime()));
                //maletas
                ps.setInt(6, p.getNum_maletas_facturar());

                if (p instanceof Adulto) {
                    ps.setObject(11, ((Adulto) p).getDescuento());
                } else {

                }

                if (p instanceof Ninyo) {
                    ps.setObject(9, ((Ninyo) p).getSolo());
                    ps.setObject(10, ((Ninyo) p).getSilleta());
                } else {

                }

                //tarjeta de embarque
                ps = conn.prepareStatement(insertTarjeta);
                if (p.getTarjeta() != null) {
                    TarjetaEmbarque tarjeta = p.getTarjeta();
                    //id
                    ps.setInt(1, tarjeta.getId());
                    //vuelo
                    ps.setString(2, tarjeta.getVuelo());
                    //origen
                    ps.setString(3, tarjeta.getOrigen());
                    //destino
                    ps.setString(4, tarjeta.getDestino());
                    //fecha
                    ps.setDate(5, new java.sql.Date(tarjeta.getFecha_vuelo().getTime()));
                    //terminal
                    ps.setInt(6, tarjeta.getTerminal());
                    //puerta
                    ps.setInt(7, tarjeta.getPuerta());
                }
            }

            @SuppressWarnings("unused")
            int afectadas = ps.executeUpdate();
            //Este entero no lo vamos a usar pero devuelve el número de flas aceptadas
            //En otras ocasiones nos puede ser útil, aquí siempre debe devolver 1



        } catch (Exception ex) {
            throw new DAOException("Ha habido un problema al insertar la reserva: ",ex);
        } finally {
            try {
                ps.close();

            } catch (SQLException sqlex) {
                throw new DAOException("Error al cerrar la sentencia",sqlex);
            }
        }

    }


    /**
     * idOrden=int 4
     * fecha=date
     * dni=string 9
     * numTarjeta= string 16
     * importe=double
     * reserva=int 4
     */

    @Override
    public void crearOrdenPago(OrdenPago orden) throws DAOException {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = ConexionJDBC.getInstance().getConnection();

            ps = conn.prepareStatement(insertOrdenPago);
            ps.setInt(1, orden.getId());
            ps.setDate(2, new java.sql.Date(orden.getFechaOrden().getTime()));
            ps.setString(3,orden.getDniOrden());
            ps.setString(4,orden.getNumTarjeta());
            ps.setDouble(5,orden.getImporte());
            ps.setObject(6,orden.getReserva());


            @SuppressWarnings("unused")
            int afectadas = ps.executeUpdate();
            //Este entero no lo vamos a usar pero devuelve el número de flas aceptadas
            //En otras ocasiones nos puede ser útil, aquí siempre debe devolver 1



        } catch (Exception ex) {
            throw new DAOException("Ha habido un problema al insertar la orden de pago: ",ex);
        } finally {
            try {
                ps.close();

            } catch (SQLException sqlex) {
                throw new DAOException("Error al cerrar la sentencia",sqlex);
            }
        }

    }

    @Override
    public void modificarReserva(Reserva r) throws DAOException {

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = ConexionJDBC.getInstance().getConnection();

            ps = conn.prepareStatement(modReserva);
            ps.setInt(1, r.getId());
            ps.setDate(2, new java.sql.Date(r.getFecha().getTime()));
            ps.setDouble(3,r.getImporte());
            ps.setObject(4,r.getVuelo());
            ps.setBoolean(5,r.isCancelada());


            @SuppressWarnings("unused")
            int afectadas = ps.executeUpdate();
            //Este entero no lo vamos a usar pero devuelve el número de flas aceptadas
            //En otras ocasiones nos puede ser útil, aquí siempre debe devolver 1



        } catch (Exception ex) {
            throw new DAOException("Ha habido un problema al modificar la reserva: ",ex);
        } finally {
            try {
                ps.close();

            } catch (SQLException sqlex) {
                throw new DAOException("Error al cerrar la sentencia",sqlex);
            }
        }

    }

    @Override
    public Reserva obtenerReserva(int id) throws DAOException {
        //Obtiene de la BD la reserva con id
        Reserva e= new Reserva();
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = ConexionJDBC.getInstance().getConnection();

            ps = conn.prepareStatement(getReserva);
            ps.setInt(1,id); //porque es el primer interrogante o dato que necesito

            ResultSet rs = ps.executeQuery();
            if(!rs.next()){
                // Nos metemos aquí si la consulta no devuelve nada
                return null;
            }



            int idReserva = rs.getInt("idReserva"); //campo de la base de datos
            Date fecha=rs.getDate("fecha");
            double importe=rs.getDouble("importe");
            Vuelo vuelo= (Vuelo) rs.getObject("vuelo");
            boolean cancelada=rs.getBoolean("cancelada");

            e.setId(idReserva);
            e.setFecha(fecha);
            e.setImporte(importe);
            e.setVuelo(vuelo);
            e.setCancelada(cancelada);

            // TO DO obtener pasajeros de la reserva
            obtenerPasajeros(e);


            return e;


        } catch (Exception ex) {
            throw new DAOException("Ha habido un problema al obtener la reserva: ",ex);
        } finally {
            try {
                ps.close();

            } catch (SQLException ex) {
                throw new DAOException("Error al cerrar la base de datos",ex);
            }

        }
    }

    @Override
    public OrdenPago obtenerOrdenPago(int id) throws DAOException {
        //Obtiene de la BD la orden de pago con id
        OrdenPago e= new OrdenPago();
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = ConexionJDBC.getInstance().getConnection();

            ps = conn.prepareStatement(getOrdenPago);
            ps.setInt(1,id); //porque es el primer interrogante o dato que necesito

            ResultSet rs = ps.executeQuery();
            if(!rs.next()){
                // Nos metemos aquí si la consulta no devuelve nada
                return null;
            }

            int idOrden=rs.getInt("idOrden");
            Date fecha=rs.getDate("fecha");
            String dni=rs.getString("dni");
            String numTarjeta=rs.getString("numTarjeta");
            double importe=rs.getDouble("importe");
            Reserva reserva= (Reserva) rs.getObject("reserva");


            e.setId(idOrden);
            e.setFechaOrden(fecha);
            e.setDniOrden(dni);
            e.setNumTarjeta(numTarjeta);
            e.setImporte(importe);
            e.setReserva(reserva);

            return e;


        } catch (Exception ex) {
            throw new DAOException("Ha habido un problema al obtener la orden de pago: ",ex);
        } finally {
            try {
                ps.close();

            } catch (SQLException ex) {
                throw new DAOException("Error al cerrar la base de datos",ex);
            }

        }
    }

    @Override
    public List<OrdenPago> obtenerOrdenPago() throws DAOException {
        //Obtiene de la BD la lista de todos las vuelos y devuelve dicha lista
        List<OrdenPago> or = new ArrayList<OrdenPago>();
        Connection conn = null;
        PreparedStatement ps = null;
        ReservasJDBCDAO reservaDAO=new ReservasJDBCDAO();


        try {
            conn = ConexionJDBC.getInstance().getConnection();

            ps = conn.prepareStatement(getAllOrdenPago);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                OrdenPago  j = new OrdenPago();
                int id=rs.getInt("idOrden");
                java.util.Date fecha=new java.util.Date(rs.getDate("fecha").getTime());
                String dni=rs.getString("dni");
                String numTarjeta=rs.getString("numTarjeta");
                double importe=rs.getDouble("importe");
                Reserva r=reservaDAO.obtenerReserva(rs.getInt("reserva"));



                j.setId(id);
                j.setFechaOrden(fecha);
                j.setDniOrden(dni);
                j.setNumTarjeta(numTarjeta);
                j.setImporte(importe);
                j.setReserva(r);


                or.add(j);

            }
        } catch (Exception e) {
            throw new DAOException("Ha habido un problema al obtener la lista de ordenes de pago: ",e);
        } finally {
            try {
                ps.close();

            } catch (SQLException ex) {
                throw new DAOException("Error al cerrar la base de datos",ex);
            }

        }
        return or;
    }

    @Override
    public boolean eliminarReserva(int id) throws DAOException {
        //Elimina un vuelo de la BD

        Connection conn = null;
        PreparedStatement ps = null;

        try {


            ps = conn.prepareStatement(deleteReserva);
            ps.setInt(1, id);

            ps.executeUpdate();



        } catch (Exception ex) {
            throw new DAOException("Ha habido un problema al eliminar la reserva: ",ex);
        } finally {
            try {
                ps.close();
                conn.close();
            } catch (SQLException ex) {
                throw new DAOException("Error al cerrar la base de datos",ex);
            }

        }
        return true;
    }

    @Override
    public List<Reserva> obtenerTodasReservas() throws DAOException {
        //Obtiene de la BD la lista de todos las vuelos y devuelve dicha lista
        List<Reserva> reservas = new ArrayList<Reserva>();


        Connection conn = null;
        PreparedStatement ps = null;
        VuelosJDBCDAO vueloDAO=new VuelosJDBCDAO();


        try {
            conn = ConexionJDBC.getInstance().getConnection();

            ps = conn.prepareStatement(getAllReservas);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Reserva  j = new Reserva();
                int idReserva=rs.getInt("idReserva");
                java.util.Date fecha=new java.util.Date(rs.getDate("fecha").getTime());
                double importe=rs.getDouble("importe");
                Vuelo v=vueloDAO.obtenerVuelo(rs.getString("vuelo"));
                boolean cancelada=rs.getBoolean("cancelada");



                j.setId(idReserva);
                j.setFecha(fecha);
                j.setImporte(importe);
                j.setVuelo(v);
                j.setCancelada(cancelada);

                // TO DO Obtener los pasajeros de esa reserva con un metodo
                //obtenerTodosPasajeros(reservas);


                reservas.add(j);

            }
        } catch (Exception e) {
            throw new DAOException("Ha habido un problema al obtener la lista de reservas: ",e);
        } finally {
            try {
                ps.close();

            } catch (SQLException ex) {
                throw new DAOException("Error al cerrar la base de datos",ex);
            }

        }
        return reservas;
    }

    @Override
    public List<Reserva> obtenerTodasReservas(Date d) throws DAOException {
        return null;
    }

    @Override
    public List<Reserva> obtenerTodasReservas(String idVuelo) throws DAOException {
        //Obtiene de la BD el vuelo con codigo pasado por parametro
        Reserva j = new Reserva();
        List<Reserva> reservas = new ArrayList<Reserva>();
        Connection conn = null;
        PreparedStatement ps = null;
        VuelosJDBCDAO vueloDAO = new VuelosJDBCDAO();

        try {
            conn = ConexionJDBC.getInstance().getConnection();

            ps = conn.prepareStatement(getReservaVuelo);
            ps.setString(1, idVuelo);

            ResultSet rs = ps.executeQuery(); //el string se transforma en una sentencia de la bd, un query, se guarda en rs
            if (!rs.next()) {
                // Nos metemos aquí si la consulta no devuelve nada
                return null;
            }

            int idReserva = rs.getInt("idReserva");
            java.util.Date fecha = new java.util.Date(rs.getDate("fecha").getTime());
            double importe = rs.getDouble("importe");
            Vuelo v = vueloDAO.obtenerVuelo(rs.getString("vuelo"));
            boolean cancelada = rs.getBoolean("cancelada");


            j.setId(idReserva);
            j.setFecha(fecha);
            j.setImporte(importe);
            j.setVuelo(v);
            j.setCancelada(cancelada);

            // vrear y obtner la lista de pasajeros




            reservas.add(j);


            return reservas; //devuelve el primer vuelo


        } catch (Exception e) {
            throw new DAOException("Ha habido un problema al obtener el vuelo: ", e);
        } finally {
            try {
                ps.close();

            } catch (SQLException ex) {
                throw new DAOException("Error al cerrar la base de datos", ex);
            }

        }
    }

    private void obtenerPasajeros(Reserva e) {
        e.getPasajeros();

    }

    /**
     * idPasajero, dni, nombre, apellidos, fechaNacimiento,
     * numMaletas, reserva, tipo, solo, silleta, descuento
     */

    private List<Pasajero> obtenerTodosPasajeros(Reserva reserva) throws DAOException {
        //Obtiene de la BD el vuelo con codigo pasado por parametro
        Pasajero p = new Pasajero();
        List<Pasajero> pa = new ArrayList<Pasajero>();
        Connection conn = null;
        PreparedStatement ps = null;
        ReservasJDBCDAO reservaDAO = new ReservasJDBCDAO();

        try {
            conn = ConexionJDBC.getInstance().getConnection();

            ps = conn.prepareStatement(getAllPasajero);
            ps.setInt(1, reserva.getId());

            ResultSet rs = ps.executeQuery(); //el string se transforma en una sentencia de la bd, un query, se guarda en rs
            if (!rs.next()) {
                // Nos metemos aquí si la consulta no devuelve nada
                return null;
            }

            int idPasajero = rs.getInt("idPasajero");
            String dni = rs.getString("dni");
            String nombre = rs.getString("nombre");
            String apellidos = rs.getString("apellidos");
            java.util.Date fecha = new java.util.Date(rs.getDate("fechaNacimiento").getTime());
            int numMaletas = rs.getInt("numMaletas");
            Reserva r = reservaDAO.obtenerReserva(rs.getInt("reserva"));
            String tipo = rs.getString("tipo");
            boolean solo = rs.getBoolean("solo");
            boolean silleta = rs.getBoolean("silleta");
            String descuento = rs.getString("descuento");


            p.setId(idPasajero);
            p.setDni(dni);
            p.setNombre(nombre);
            p.setApellidos(apellidos);
            p.setFecha_nacimiento(fecha);
            p.setNum_maletas_facturar(numMaletas);
            p.setTipo(tipo);
            p.setSolo(solo);
            p.setSilleta(silleta);
            //p.setDescuento(descuento);
            p.setTarjeta(null);


            // crear y obtner la lista de pasajeros

            pa.add(p);


            return pa;


        } catch (Exception e) {
            throw new DAOException("Ha habido un problema al obtener el vuelo: ", e);
        } finally {
            try {
                ps.close();

            } catch (SQLException ex) {
                throw new DAOException("Error al cerrar la base de datos", ex);
            }

        }


    }
}
