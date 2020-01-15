/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.severo.vuelos.views;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import net.severo.vuelos.estructura.Adulto;
import net.severo.vuelos.estructura.Descuentos;
import net.severo.vuelos.estructura.Ninyo;
import net.severo.vuelos.estructura.Pasajero;

/**
 *
 * @author Usuario
 */
public class VistaPasajero {

    public Integer pedirIdPasajero() {
        Scanner sc = new Scanner(System.in);
        Integer id;
        do {
            String respuesta;
            System.out.println("Introduzca el id del pasajero:");
            respuesta = sc.nextLine();
            if (respuesta.equals("0")) {
                //Salimos
                return null;
            }
            if (esEntero(respuesta)) {
                id = Integer.parseInt(respuesta);
                if (id > 0) {
                    return id;
                }
            }

        } while (true);
    }

    public String pedirDniPasajero() {
        Scanner sc = new Scanner(System.in);
        String respuesta;
        do {
            System.out.println("Introduzca el dni del pasajero:");
            respuesta = sc.nextLine();
            if (respuesta.equals("0")) {
                //Salimos
                return null;
            }
            if (respuesta.length() != 9) {
                System.err.println("El dni del pasajero ha de tener 9 carácteres");
            }
        } while (respuesta.length() != 9);
        return respuesta;
    }

    public String pedirNombreYApellidos() {
        Scanner sc = new Scanner(System.in);
        String respuesta;
        do {
            System.out.println("Introduzca el nombre y apellidos del pasajero:");
            respuesta = sc.nextLine();
            if (respuesta.equals("0")) {
                //Salimos
                return null;
            }
            if (respuesta.length() > 100 || respuesta.length() < 0) {
                System.err.println("El nomber y apellidos del pasajero ha de tener entre 1 y 100 carácteres");
            }
        } while (respuesta.length() > 100 || respuesta.length() < 0);
        return respuesta;
    }

    public Date pedirFecha() {
        String respuesta;
        Scanner sc = new Scanner(System.in);
        Date fecha;
        do {
            System.out.println("Introduzca la fecha de nacimiento(dd/MM/yyyy)");
            respuesta = sc.nextLine();
            if (respuesta.equals("0")) {
                //salimos 
                return null;
            }
            fecha = obtenerFecha(respuesta);
            if (fecha == null) {
                System.err.println("El formato de fecha no es valido");
            }
        } while (fecha == null);
        return fecha;
    }

    public Integer pedirNumMaletasFacturar() {
        Scanner sc = new Scanner(System.in);
        Integer numMaletasFacturar;
        do {
            String respuesta;
            System.out.println("Introduzca el numero de maletas del pasajero:(Si no introduce nada será tomado como que no lleva maleta)");
            respuesta = sc.nextLine();
            if (respuesta.equals("0")) {
                //Salimos
                return null;
            }
            if(respuesta.isEmpty()){
                respuesta="0";
            }
            if (esEntero(respuesta)) {
                numMaletasFacturar = Integer.parseInt(respuesta);
                if (numMaletasFacturar >= 0) {
                    return numMaletasFacturar;
                }
            }

        } while (true);
    }


    public Boolean pedirSolo() {
        Scanner sc = new Scanner(System.in);
        do {
            System.out.println("¿El pasajero viaja solo?(S/N)");
            String respuesta = sc.nextLine();
            if("0".equals(respuesta)){
                return null;
            }
            if ( ("S".equals(respuesta.toUpperCase()))) {
                return true;
            }
            if ( ("N".equals(respuesta.toUpperCase()))) {
                return false;
            }
            System.out.println("Por favor,introduzca S para una respuesta afirmativa y N para una negativa");
        } while (true);
    }

    public Boolean pedirSilleta() {
        Scanner sc = new Scanner(System.in);
        do {
            System.out.println("¿El pasajero tiene silleta?(S/N)");
            String respuesta = sc.nextLine();
            if("0".equals(respuesta)){
                return null;
            }
            if ( ("S".equals(respuesta.toUpperCase()))) {
                return true;
            }
            if ( ("N".equals(respuesta.toUpperCase()))) {
                return false;
            }
            System.out.println("Por favor,introduzca S para una respuesta afirmativa y N para una negativa");
        } while (true);
    }
    
    public Descuentos pedirDescuento(){
        Descuentos [] descuentos=Descuentos.values();
        List<Descuentos> descuentosAElegir=new ArrayList<>();
        for(Descuentos d:descuentos){
            if(d.equals(Descuentos.RES_ANT)){
                continue;
            }
            descuentosAElegir.add(d);
        }
        String respuesta;
        Scanner sc=new Scanner(System.in);
        do{
        System.out.println("¿Que tipo de descuento tiene?");
        for(int i=1;i<=descuentosAElegir.size();i++){
            System.out.println(i+"-"+descuentosAElegir.get(i-1));
        }
        respuesta=sc.nextLine();
        if(esEntero(respuesta)){
            int opcion=Integer.parseInt(respuesta);
            if(opcion==0){
                return null;
            }
            if(opcion==1 || opcion==2){
                return descuentosAElegir.get(opcion-1);
            }
        }
        }while(true);
    }
    

    public Pasajero crearPasajero() {
        Integer id = this.pedirIdPasajero();
        if (id == null) {
            return null;
        }
        String nombre=this.pedirNombreYApellidos();
        if(nombre==null){
            return null;
        }
        String apellidos="";
        if(nombre.indexOf(" ")>0){
            apellidos=nombre.substring(nombre.indexOf(" ")+1,nombre.length());
            nombre=nombre.substring(0,nombre.indexOf(" "));
        }
        
        String dni = this.pedirDniPasajero();
        if (dni == null) {
            return null;
        }
        Date fechaNacimiento = this.pedirFecha();
        new SimpleDateFormat("dd/MM/yyyy").format(fechaNacimiento);
        if (fechaNacimiento == null) {
            return null;
        }
        
        Integer numeroMaletas=this.pedirNumMaletasFacturar();
        if(numeroMaletas==null){
            return null;
        }

        if (esNinyo(fechaNacimiento)) {
            System.out.println("El pasajero será tomado como un niño ya que es menor de 12 años");
            Boolean solo = this.pedirSolo();
            if (solo == null) {
            return null;
            }
            Boolean silleta = this.pedirSilleta();
            if (silleta == null) {
            return null;
            }
            return new Ninyo(id,dni,nombre,apellidos,fechaNacimiento,numeroMaletas,solo,silleta);
            
        } else {
            System.out.println("El pasajero será tomado como un adulto ya que es mayor de 12 años");
            Descuentos descuento=this.pedirDescuento();
            if(descuento==null){
                return null;
            }
            return new Adulto(id,dni,nombre,apellidos,fechaNacimiento,numeroMaletas,descuento);
        }
    }

    public static boolean esDecimal(String numero) {
        try {
            Double.parseDouble(numero);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    public static boolean esEntero(String numero) {
        try {
            Integer.parseInt(numero);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    public void mostrarError(String mensaje) {
        System.err.println(mensaje);
    }

    private static Date obtenerFecha(String cadena) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date fechaNac;
        try {
            fechaNac = sdf.parse(cadena);
            return fechaNac;
        } catch (ParseException ex) {
            // si no es fecha devolvemos un null
            return null;
        }
    }

    private static boolean esNinyo(Date fechaNacimiento) {
        Calendar fechaDeCorte = Calendar.getInstance();
        Calendar fechaNacimientoCalendar = Calendar.getInstance();
        fechaNacimientoCalendar.setTime(fechaNacimiento);
        fechaDeCorte.setTime(new Date());
        fechaDeCorte.add(Calendar.YEAR, -12);
        return fechaDeCorte.before(fechaNacimientoCalendar);
    }

}
