package net.severo.vuelos.dao.Hibernate;

import net.severo.vuelos.dao.DAOException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


public class SesionHibernate {
    private static SesionHibernate instancia = null;
    private SessionFactory sessionFactory;
    Session sesion = null;

    private SesionHibernate() throws DAOException {
        try {
            //Una sesión es como una conexión de JDBC, para poder usar hibernate debemos estar
            //dentro de una sesión y cada sesión nos la facilita la factoría de sesiones a partir del
            //archivo de configuración de hibernate que hemos definido
            //crear la factoría de sesiones es un proceso costoso, y debemos de hacerlo una sola vez
            //En este caso en el constructor, En una aplicación web deberemos usar otras opciones

            //Inicializamos la factoria de sesiones
            sessionFactory = new Configuration().configure().buildSessionFactory();
            sesion = sessionFactory.openSession();


        } catch (Throwable e) {
            System.err.println("Ha fallado la creación de la factoría de sesiones." + e);
            throw new ExceptionInInitializerError(e);
        }
    }

    public static SesionHibernate getInstance() throws DAOException {
        if (instancia == null) {
            instancia = new SesionHibernate();
        }
        return instancia;
    }

    public Session getSesion() {
        return sesion;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
