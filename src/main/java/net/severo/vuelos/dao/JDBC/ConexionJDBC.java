package net.severo.vuelos.dao.JDBC;

import net.severo.vuelos.dao.DAOException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 * clase singleton, se hace una vez y se accede desde cualquier sitio de la aplicacion
 */
public class ConexionJDBC {

    private Connection conn = null;
    private String bd = null;
    private String login = null;
    private String password = null;
    private String url =null;
    private static  ConexionJDBC instancia=null;

    private ConexionJDBC() throws DAOException { //constructor privado, es su caracteristica mas destacada
        try {

            Properties pro = new Properties();
            pro.load(this.getClass().getResourceAsStream("/configuracion.properties"));
            this.bd = pro.getProperty("bdJDBC");
            this.login = pro.getProperty("loginJDBC");
            this.password = pro.getProperty("passwordJDBC");
            this.url = "jdbc:mysql://localhost/" + bd; //siempre q sea en local, sino hay k poner la ip

            Class.forName("com.mysql.jdbc.Driver").newInstance();

            conn = DriverManager.getConnection(url, login, password); //conexión a la bd, viene a ser la llamada básica



        } catch (InstantiationException ex) {
            throw new DAOException("Hubo un problema al iniciar la base de datos: ", ex);
        } catch (IllegalAccessException iae) {
            throw new DAOException("Hubo un problema al acceder a la base de datos.", iae);
        } catch (SQLException sqle) {
            throw new DAOException("Hubo un problema al intentar conectarse con la base de datos ", sqle);

        } catch (ClassNotFoundException cnfe) {
            throw new DAOException("No se encuentra el driver JDBC.", cnfe);
        } catch (IOException ioe) {
            throw new DAOException("Error al accerder al archivo de configuración: ", ioe);
        }
    }
    public static ConexionJDBC getInstance() throws DAOException{
        if(instancia==null){ //si inicializo la clase y el objeto es nulo, creo uno nuevo
            instancia=new ConexionJDBC();
        }
        return instancia; //si no regresa el que ya hay
    }

    public Connection getConnection() {
        return conn;
    }





}