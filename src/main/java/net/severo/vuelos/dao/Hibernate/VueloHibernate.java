package net.severo.vuelos.dao.Hibernate;

import net.severo.vuelos.dao.DAOException;
import net.severo.vuelos.dao.IVuelosDAO;
import net.severo.vuelos.estructura.Vuelo;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Date;
import java.util.List;

public class VueloHibernate implements IVuelosDAO {
    Transaction tx = null;

    @Override
    public void crearVuelo(Vuelo vuelo) throws DAOException {
        Session sesion = null;
        try {

            sesion = SesionHibernate.getInstance().getSesion();

            sesion.save(vuelo);


        } catch (Exception e) {
            throw new DAOException("Ha habido un problema al insertar un nuevo vuelo", e);
        }

    }

    @Override
    public void modificarVuelo(Vuelo v) throws DAOException {
        try {
            //Vuelo j = this.obtenerVuelo(v.getCodigo());
            Session sesion = SesionHibernate.getInstance().getSesion();

            // Iniciamos una transaccion
            //Transaction tx = sesion.beginTransaction();

            // Hacemos los cambios
            sesion.update(v);
            //crearVuelo(j);

            // Cerramos la transaccion
            //tx.commit();

            // Cerramos la sesion
            //sesion.close();
        } catch (Exception e) {
            throw new DAOException("Ha habido un problema al modificar el vuelo", e);
        }
    }

    @Override
    public void eliminarVuelo(String codigo) throws DAOException {
        try {
            Vuelo j = this.obtenerVuelo(codigo);
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
            throw new DAOException("Ha habido un problema al eliminar el vuelo", e);
        }

    }

    @Override
    public Vuelo obtenerVuelo(String codigo) throws DAOException {
        Session sesion = SesionHibernate.getInstance().getSesion();
        try {

            // Variable que almacena la lista a devolver
            Vuelo j = new Vuelo();

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
            j = (Vuelo) sesion.get(Vuelo.class, codigo);
            //sesion.close();
            return j;

        } catch (Exception e) {
            throw new DAOException("Ha habido un problema al obtener el vuelo", e);
        }
    }

    @Override
    public List<Vuelo> obtenerVuelos(Date fechaSalida) throws DAOException {
        return null;
    }

    @Override
    public List<Vuelo> obtenerVuelos() throws DAOException {
        try {
            Session sesion = SesionHibernate.getInstance().getSesion();

            // Variable que almacena la lista a devolver
            List<Vuelo> lista;

            // Hacemos la consulta
            Query q = sesion.createQuery("from Vuelo");
            lista = q.list();
            for (Vuelo j : lista) {
                Hibernate.initialize(j.getCodigo());
                //¿Por qué hacemos esto? Es un poco mas complejo de entender
                // y lo explicaremos en clase
            }

            // Cerramos la sesión
            //sesion.close();

            // Devolvemos el resultado
            return lista;
        } catch (Exception e) {
            throw new DAOException("Ha habido un problema al obtener los vuelos", e);

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
