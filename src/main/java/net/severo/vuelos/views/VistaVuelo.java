/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.severo.vuelos.views;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import net.severo.vuelos.estructura.Vuelo;

/**
 *
 * @author Usuario
 */
public class VistaVuelo {

    public int menuPrincipal() {
        Scanner sc = new Scanner(System.in);
        String menu = "  1. Nuevo Vuelo. \n 2. Ver Vuelos \n 3. Modificar Vuelo \n 4.Eliminar Vuelo. \n 5. Asignar puerta de embarque y terminal a un vuelo \n 0)Salir \n ¿Que quiere hacer?";

        int opcion = -1; //opcio -1 indica opcion incorrecta
        while (opcion == -1) {
            System.out.println(menu);
            String entrada = sc.nextLine();
            if (!esEntero(entrada)) {
                opcion = -1;
            } else {
                opcion = Integer.parseInt(entrada);
            }
            if (opcion > 6 || opcion < 0) {
                System.out.println("Opción no valida");
                opcion = -1;
            }
        }
        return opcion;
    }

    public String pedirCodigoVuelo() {
        Scanner sc = new Scanner(System.in);
        String respuesta;
        do {
            System.out.println("Introduzca el código del vuelo:");
            respuesta = sc.nextLine();
            if (respuesta.equals("0")) {
                //Salimos
                return null;
            }
            if (respuesta.length() != 8) {
                System.err.println("El código de vuelo ha de tener 8 carácteres");
            }
            //Se establece que el codigo del vuelo ha de tener más de 3 carácteres y menos de 8
        } while (respuesta.length() != 8);
        return respuesta;
    }

    public String pedirOrigen() {
        String respuesta;
        Scanner sc = new Scanner(System.in);
        do {
            System.out.println("Introduzca el origen del vuelo:");
            respuesta = sc.nextLine();
            if (respuesta.equals("0")) {
                //Salimos
                return null;
            }
            if (respuesta.length() != 3) {
                System.err.println("El origen del vuelo ha de tener 3 carácteres");
            }
            //Se establece que el codigo del vuelo ha de tener más de 1 carácter y menos de 5
        } while (respuesta.length() != 3);
        return respuesta;
    }

    public String pedirDestino() {
        Scanner sc = new Scanner(System.in);
        String respuesta;
        do {
            System.out.println("Introduzca el destino del vuelo:");
            respuesta = sc.nextLine();
            if (respuesta.equals("0")) {
                //Salimos
                return null;
            }
            if (respuesta.length() < 1 || respuesta.length() > 5 || respuesta == null) {
                System.err.println("El destino del vuelo ha de tener entre 1 y 5 carácteres");
            }
            //Se establece que el destino del vuelo ha de tener más de 1 carácter y menos de 5
        } while (respuesta.length() < 1 || respuesta.length() > 5 || respuesta == null);
        return respuesta;
    }

    public Double pedirPrecioPersona() {
        String respuesta;
        Scanner sc = new Scanner(System.in);
        Double precio;
        do {
            System.out.println("Introduzca el precio por persona:");
            respuesta = sc.nextLine();
            if (respuesta.equals("0")) {
                return null;
            }
            if (!esDecimal(respuesta)) {
                System.err.println("El precio ha de ser un número decimal.");
            } else {
                //No se permiten vuelos gratuitos
                precio = Double.parseDouble(respuesta);
                if (precio <= 0) {
                    System.err.println("El precio ha de ser más de 0");
                } else {
                    return precio;
                }
            }
        } while (true);
    }

    public Date pedirFecha() {
        String respuesta;
        Scanner sc = new Scanner(System.in);
        Date fecha;
        do {
            System.out.println("Introduzca la fecha del vuelo (dd/MM/yyyy)");
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

    public Integer pedirPlazasDisponibles() {
        String respuesta;
        Scanner sc = new Scanner(System.in);
        Integer numEntero = null;
        do {
            System.out.println("Introduzca el número de plazas disponibles.");
            respuesta = sc.nextLine();
            if (!esEntero(respuesta)) {
                System.err.println("El número de plazas debe ser un número");
            } else {
                numEntero = Integer.parseInt(respuesta);
                if (numEntero == 0) {
                    //salimos 
                    return null;
                } else if (numEntero < 0) {
                    System.err.println("El número de plazas ha de ser positivo");
                } else {
                    return numEntero;
                }
            }
        } while (true);
    }

    public Integer pedirTerminal() {
        String respuesta;
        Scanner sc = new Scanner(System.in);
        do {
            System.out.println("Introduzca el número de terminal.");
            int numEntero;
            respuesta = sc.nextLine();
            if (!esEntero(respuesta)) {
                System.err.println("El terminal debe ser un número");
            } else {
                numEntero = Integer.parseInt(respuesta);
                if (numEntero == 0) {
                    //salimos 
                    return null;
                } else if (numEntero < 0) {
                    System.err.println("El número de terminal ha de ser positivo");
                } else {
                    return numEntero;
                }
            }

        } while (true);
    }

    public Integer pedirPuerta() {
        String respuesta;
        Scanner sc = new Scanner(System.in);
        do {
            System.out.println("Introduzca el número de puerta.");
            int numEntero;
            respuesta = sc.nextLine();
            if (!esEntero(respuesta)) {
                System.err.println("La puerta debe ser un número");
            } else {
                numEntero = Integer.parseInt(respuesta);
                if (numEntero == 0) {
                    //salimos
                    return null;
                } else if (numEntero < 0) {
                    System.err.println("El número de puerta ha de ser positivo");
                } else {
                    return numEntero;
                }
            }

        } while (true);
    }

    public Vuelo crearVuelo() {

        //Codigo del vuelo
        String codigo = this.pedirCodigoVuelo();
        if (codigo == null) {
            return null;
        }
        //Origen del vuelo
        String origen = this.pedirOrigen();
        if (origen == null) {
            return null;
        }

        //Destino del vuelo
        String destino = this.pedirDestino();
        if (destino == null) {
            return null;
        }

        //Precio por persona
        Double precio = this.pedirPrecioPersona();
        if (precio == null) {
            return null;
        }

        //Fecha del vuelo
        Date fecha = this.pedirFecha();
        if (fecha == null) {
            return null;
        }

        //Plazas disponibles del vuelo
        Integer plazasDisponibles = this.pedirPlazasDisponibles();
        if (plazasDisponibles == null) {
            return null;
        }

        System.out.println("");
        return new Vuelo(codigo, origen, destino, precio, fecha, plazasDisponibles);
    }

    public Integer menuModificarVuelo() {
        Scanner sc = new Scanner(System.in);
        String respuesta;
        int opcion;
        do {
            System.out.println(" 1.-Precio por persona \n 2.-Fecha \n 3.-Plazas disponibles \n 4.-Terminal \n 5.-Puerta");
            System.out.println("¿Qué quiere modificar?");
            respuesta = sc.nextLine();
            if (!esEntero(respuesta)) {
                continue;
            }
            opcion = Integer.parseInt(respuesta);
            if (opcion == 0) {
                return null;
            }
            if (opcion > 0 && opcion < 6) {
                return opcion;
            }
        } while (true);

    }

    private static Date obtenerFecha(String cadena) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date fechaNac = null;
        try {
            fechaNac = sdf.parse(cadena);
            return fechaNac;
        } catch (ParseException ex) {
            // si no es fecha devolvemos un null
            return null;
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

    public void mostrarVuelos(List<Vuelo> vuelos) {
        System.out.println("--------------- VUELOS -------------");
        System.out.println("CÓDIGO   ORIGEN   DESTINO    P/PERS   FECHA    PLAZAS_DISP    TERMINAL    PUERTA");
        String fechaStr;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        for (Vuelo v : vuelos) {
            fechaStr = sdf.format(v.getFechaVuelo());
            System.out.printf("%-8s %-8s %-10s %-6.2f %-10s %-14d %-11d %-8d \n", v.getCodigo(), v.getOrigen(), v.getDestino(), v.getPrecioPersona(), fechaStr, v.getPlazasDisponibles(), v.getTerminal(), v.getPuerta());
        }

    }

    public void mostrarVuelo(Vuelo v) {
        System.out.println("--------------- VUELOS -------------");
        System.out.println("CÓDIGO   ORIGEN   DESTINO    P/PERS   FECHA    PLAZAS_DISP    TERMINAL    PUERTA");
        String fechaStr;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        fechaStr = sdf.format(v.getFechaVuelo());
        System.out.printf("%-8s %-8s %-10s %-6.2f %-10s %-14d %-11d %-8d \n", v.getCodigo(), v.getOrigen(), v.getDestino(), v.getPrecioPersona(), fechaStr, v.getPlazasDisponibles(), v.getTerminal(), v.getPuerta());

    }

}
