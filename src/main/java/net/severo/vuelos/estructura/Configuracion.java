
package net.severo.vuelos.estructura;

import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author Rubén Más Almira
 */
public class Configuracion{
    public static Properties propiedades=new Properties();
    
    public Configuracion(String direccionArchivoPropiedades)throws IllegalArgumentException{
        try {
            propiedades.load(this.getClass().getResourceAsStream(direccionArchivoPropiedades));
        } catch (IOException ex) {
            throw new IllegalArgumentException("Error al intentar acceder a la ruta: "+direccionArchivoPropiedades);
        }
    }
    /*
GastosCancelacionAntesDeUnMes=10%
GastosCancelacionAntesDe15Dias=50% 
CosteMaleta=50
CosteSilleta=40
CosteNinyoSolo=100 
DescuentoNinyo=10%
DescuentoResidenteIsla=40%
DescuentoReservaConAnterioridad=20% */
    
    public String getTxtVuelos(){
        return propiedades.getProperty("txtVuelo");
    }
    public String getRafVuelos(){
        return propiedades.getProperty("rafVuelo");
    }
    public String getRafVuelosReferencias() {return propiedades.getProperty("rafVueloReferencias");}
    
    public String getTxtReservas(){
        return propiedades.getProperty("txtReservas");
    }
    public String getRafReservas(){
        return propiedades.getProperty("rafReservas");
    }
    
    public String getTxtPasajeros(){
        return propiedades.getProperty("txtPasajeros");
    }
    public String getRafPasajeros(){
        return propiedades.getProperty("rafPasajeros");
    }

    public String getTxtTarjetasEmbarque() {
        return propiedades.getProperty("txtTarjetasEmbarque");
    }
    public String getRafTarjetasEmbarque() {
        return propiedades.getProperty("rafTarjetasEmbarque");
    }

    public String getTxtOrdenesPago() {
        return propiedades.getProperty("txtOrdenesPago");
    }
    public String getRafOrdenesPago() {
        return propiedades.getProperty("rafOrdenesPago");
    }

    public Integer getGastosCancelacionAntesDeUnMes(){
        return Integer.parseInt(propiedades.getProperty("GastosCancelacionAntesDeUnMes"));
    }
    
    public Integer getGastosCancelacionAntesDe15Dias(){
        return Integer.parseInt(propiedades.getProperty("GastosCancelacionAntesDe15Dias"));
    }
    
    public Integer getCosteMaleta(){
        return Integer.parseInt(propiedades.getProperty("CosteMaleta"));
    }
    
    public Integer getCosteSilleta(){
        return Integer.parseInt(propiedades.getProperty("CosteSilleta"));
    }
    
    public Integer getCosteNinyoSolo(){
        return Integer.parseInt(propiedades.getProperty("CosteNinyoSolo"));
    }
    
    public Integer getDescuentoNinyo(){
        return Integer.parseInt(propiedades.getProperty("DescuentoNinyo"));
    }
    
    public Integer getDescuentoResidenteIsla(){
        return Integer.parseInt(propiedades.getProperty("DescuentoResidenteIsla"));
    }
    
    public Integer getDescuentoReservaConAnterioridad(){
        return Integer.parseInt(propiedades.getProperty("DescuentoReservaConAnterioridad"));
    }



}
