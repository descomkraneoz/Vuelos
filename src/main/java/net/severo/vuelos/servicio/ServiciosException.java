package net.severo.vuelos.servicio;

/**
 *
 * @author Usuario
 */
public class ServiciosException extends Exception{
    private static final long serialVersionUID = 3249803612006121332L;
	public ServiciosException(String msg, Exception e) {
            super(msg,e);
        }
	public ServiciosException(String msg) {
        super(msg);
    }
    
}
