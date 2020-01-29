package net.severo.vuelos.dao.Hibernate;

import net.severo.vuelos.dao.DAOException;
import net.severo.vuelos.dao.IReservaDAO;
import net.severo.vuelos.estructura.OrdenPago;
import net.severo.vuelos.estructura.Reserva;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReservaHibernate implements IReservaDAO {
    Transaction tx = null;


    @Override
    public void crearReserva(Reserva r) throws DAOException {
        Session sesion = null;
        try {

            sesion = SesionHibernate.getInstance().getSesion();

            sesion.save(r);


        } catch (Exception e) {
            throw new DAOException("Ha habido un problema al insertar una nueva reserva", e);
        }

    }

    @Override
    public void crearOrdenPago(OrdenPago orden) throws DAOException {
        Session sesion = null;
        try {

            sesion = SesionHibernate.getInstance().getSesion();

            sesion.save(orden);


        } catch (Exception e) {
            throw new DAOException("Ha habido un problema al insertar una nueva orden de pago", e);
        }

    }

    @Override
    public void modificarReserva(Reserva r) throws DAOException {
        try {
            Reserva j = this.obtenerReserva(r.getId());
            Session sesion = SesionHibernate.getInstance().getSesion();

            // Iniciamos una transaccion
            //Transaction tx = sesion.beginTransaction();

            // Hacemos los cambios
            sesion.delete(j);
            crearReserva(j);

            // Cerramos la transaccion
            //tx.commit();

            // Cerramos la sesion
            //sesion.close();
        } catch (Exception e) {
            throw new DAOException("Ha habido un problema al modificar el vuelo", e);
        }

    }

    @Override
    public Reserva obtenerReserva(int id) throws DAOException {
        Session sesion = SesionHibernate.getInstance().getSesion();
        try {

            // Variable que almacena la lista a devolver
            Reserva j = new Reserva();

            //Hemos realizado esta operación de dos formas posibles, la primera está comentada
            //En esa primera opción,a al usar Hibernate ya no usaremos el lenguaje SQL normal sino que
            //usaremos un lenguaje parecido pero adapatado a tratar con objetos, no con tablas ese lenguaje es el
            // HQL  (hiebernate query Languague)como ves no es difícilde entender pero si prestas atención a la consulta
            // no dice "from jugadores " dice "from Jugador", aquí ya no usa las tablas de la BD sino los objetos del modelo,
            //aquí también cambiamos los ? de JDBC por :
            //La segunda opción es sin duda más sencilla pero a veces tendrás que usar la primera


		/*OPCION 1 CON UNA CONSULTA HQL
		// Hacemos la consulta
		String consulta = "FROM Jugador AS J WHERE J.id = :idJugador";
		Query q = sesion.createQuery(consulta).setInteger("idJugador", idJugador);
		@SuppressWarnings("unchecked")
		List<Jugador>lista = q.list();
		if(lista.size()==0){
			j=null;
		}
		else{
			j=lista.get(0);
		}
		// Cerramos la sesi�n
		sesion.close();

		// Devolvemos el resultado
		return j;
		*/


            //OPCION 2 USANDO GET SOLO SIRVE PARA BUSCAR POR EL ID
            j = (Reserva) sesion.get(Reserva.class, id);
            //sesion.close();
            return j;

        } catch (Exception e) {
            throw new DAOException("Ha habido un problema al obtener la reserva", e);
        }
    }

    @Override
    public OrdenPago obtenerOrdenPago(int id) throws DAOException {
        Session sesion = SesionHibernate.getInstance().getSesion();
        try {

            // Variable que almacena la lista a devolver
            OrdenPago j = new OrdenPago();

            //Hemos realizado esta operación de dos formas posibles, la primera está comentada
            //En esa primera opción,a al usar Hibernate ya no usaremos el lenguaje SQL normal sino que
            //usaremos un lenguaje parecido pero adapatado a tratar con objetos, no con tablas ese lenguaje es el
            // HQL  (hiebernate query Languague)como ves no es difícilde entender pero si prestas atención a la consulta
            // no dice "from jugadores " dice "from Jugador", aquí ya no usa las tablas de la BD sino los objetos del modelo,
            //aquí también cambiamos los ? de JDBC por :
            //La segunda opción es sin duda más sencilla pero a veces tendrás que usar la primera


		/*OPCION 1 CON UNA CONSULTA HQL
		// Hacemos la consulta
		String consulta = "FROM Jugador AS J WHERE J.id = :idJugador";
		Query q = sesion.createQuery(consulta).setInteger("idJugador", idJugador);
		@SuppressWarnings("unchecked")
		List<Jugador>lista = q.list();
		if(lista.size()==0){
			j=null;
		}
		else{
			j=lista.get(0);
		}
		// Cerramos la sesi�n
		sesion.close();

		// Devolvemos el resultado
		return j;
		*/


            //OPCION 2 USANDO GET SOLO SIRVE PARA BUSCAR POR EL ID
            j = (OrdenPago) sesion.get(OrdenPago.class, id);
            //sesion.close();
            return j;

        } catch (Exception e) {
            throw new DAOException("Ha habido un problema al obtener la orden de pago", e);
        }
    }

    @Override
    public List<OrdenPago> obtenerOrdenPago() throws DAOException {
        try {
            Session sesion = SesionHibernate.getInstance().getSesion();

            // Variable que almacena la lista a devolver
            List<OrdenPago> lista;

            // Hacemos la consulta
            Query q = sesion.createQuery("from orden_pago");
            lista = q.list();
            for (OrdenPago j : lista) {
                Hibernate.initialize(j.getId());
                //¿Por qué hacemos esto? Es un poco mas complejo de entender
                // y lo explicaremos en clase
            }

            // Cerramos la sesión
            //sesion.close();

            // Devolvemos el resultado
            return lista;
        } catch (Exception e) {
            throw new DAOException("Ha habido un problema al obtener la lista de ordenes de pago", e);

        }
    }

    @Override
    public boolean eliminarReserva(int id) throws DAOException {
        try {
            Reserva j = this.obtenerReserva(id);
            Session sesion = SesionHibernate.getInstance().getSesion();

            // Iniciamos una transaccion
            //Transaction tx = sesion.beginTransaction();

            // Hacemos los cambios
            sesion.delete(j);

            // Cerramos la transaccion
            //tx.commit();

            // Cerramos la sesion
            //sesion.close();
        } catch (Exception e) {
            throw new DAOException("Ha habido un problema al eliminar la reserva", e);
        }
        return true;
    }


    @Override
    public List<Reserva> obtenerTodasReservas() throws DAOException {

        try {
            Session sesion = SesionHibernate.getInstance().getSesion();

            // Variable que almacena la lista a devolver
            List<Reserva> lista;

            // Hacemos la consulta
            Query q = sesion.createQuery("from Reserva");
            lista = q.list();
            if (lista == null) {
                lista = new ArrayList<>();
            } else {
                for (Reserva j : lista) {
                    Hibernate.initialize(j.getId());
                }

            }


            // Cerramos la sesión
            //sesion.close();

            // Devolvemos el resultado
            return lista;
        } catch (Exception e) {
            throw new DAOException("Ha habido un problema al obtener las reservas", e);

        }
    }

    @Override
    public List<Reserva> obtenerTodasReservas(Date d) throws DAOException {
        return null;
    }

    @Override
    public List<Reserva> obtenerTodasReservas(String idVuelo) throws DAOException {
        Session sesion = SesionHibernate.getInstance().getSesion();
        try {

            // Variable que almacena la lista a devolver
            List<Reserva> lista;


            // Hacemos la consulta
            Query q = sesion.createQuery("from Reserva");
            lista = q.list();
            for (Reserva j : lista) {
                Hibernate.initialize(idVuelo);

            }

            //sesion.close();
            return lista;

        } catch (Exception e) {
            throw new DAOException("Ha habido un problema al obtener el vuelo", e);
        }
    }

    public void finalizar() throws DAOException {
        // Antes de salir de la plicación tendremos que lamar a este método para que cierre la factoría
        //Para añadir este método habrá que modificar la interfaz
        // una vez modificada deberás añadir este método vacio a todos
        SesionHibernate.getInstance().getSesion().close();
        SesionHibernate.getInstance().getSessionFactory().close();

    }

    public void iniciarTransaccion() throws DAOException {

        this.tx = SesionHibernate.getInstance().getSesion().beginTransaction();
    }

    public void finalizarTransaccion() throws DAOException {
        this.tx.commit();
    }
}
