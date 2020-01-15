/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.severo.vuelos.views;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import net.severo.vuelos.estructura.Adulto;
import net.severo.vuelos.estructura.Ninyo;
import net.severo.vuelos.estructura.OrdenPago;
import net.severo.vuelos.estructura.Pasajero;
import net.severo.vuelos.estructura.Reserva;
import net.severo.vuelos.estructura.TarjetaEmbarque;

/**
 *
 * @author Usuario
 */
public class VistaReservas {

    public int menuPrincipal() {
        Scanner sc = new Scanner(System.in);
        String menu = "1. Nueva Reserva. \n "
                + "2.Ver Reserva \n "
                + "3.Ver reserva de un vuelo. \n "
                + "4.Modificar Reserva. \n "
                + "5.Generar tarjetas de embarque de una reserva. \n "
                + "6.Cancelar Reserva. \n "
                + "0)Salir \n "
                + "¿Que quiere hacer?";

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

    public void mostrarReservas(List<Reserva> reservas) {
        System.out.println("--------------- Reservas -------------");
        System.out.println("ID      FECHA       CANCELADA    N_PASAJEROS   CODIGO VUELO    IMPORTE");
        String fechaStr;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        for (Reserva r : reservas) {
            fechaStr = sdf.format(r.getFecha());
            System.out.printf("%-7d %-12s %-15s %-10s %-15s %-6.2f  \n", r.getId(), fechaStr, r.isCancelada() ? "Si" : "No", r.getPasajeros().size(), r.getVuelo().getCodigo(), r.getImporte());
        }

    }

    public String pedirCodigoVuelo() {
        return new VistaVuelo().pedirCodigoVuelo();
    }

    public List<Pasajero> pedirPasajeros(ArrayList<Pasajero> pasajerosCreados) {
        List<Pasajero> pasajeros = new ArrayList<>();
        VistaPasajero vp = new VistaPasajero();
        Scanner sc = new Scanner(System.in);
        boolean salir = false;
        System.out.println("PASAJEROS");
        do {
            Pasajero p = vp.crearPasajero();
            if (p == null) {
                return null;
            }
            if(pasajerosCreados.contains(p)){
                System.out.println("Ya existe un pasajero con el id del nuevo y por tanto no será admitido");
                continue;
            } else {
                    pasajeros.add(p);
                    pasajerosCreados.add(p);
            }

            do {
                System.out.println("¿Quieres añadir más pasajeros?(S/N)");
                String respuesta = sc.nextLine();
                if ("0".equals(respuesta)) {
                    return null;
                }
                if (("S".equals(respuesta.toUpperCase()))) {
                    salir = false;
                    break;
                }
                if (("N".equals(respuesta.toUpperCase()))) {
                    salir = true;
                    break;
                }
                System.out.println("Por favor,introduzca S para una respuesta afirmativa y N para una negativa");
            } while (true);
        } while (!salir);
        return pasajeros;
    }

    public Integer pedirIdReserva() {
        String respuesta;
        Scanner sc = new Scanner(System.in);
        do {
            System.out.println("Introduzca el id de la reserva.");
            int numEntero;
            respuesta = sc.nextLine();
            if (!esEntero(respuesta)) {
                System.err.println("La id debe ser un número");
            } else {
                numEntero = Integer.parseInt(respuesta);
                if (numEntero == 0) {
                    //salimos 
                    return null;
                } else if (numEntero < 0) {
                    System.err.println("La id de la reserva ha de ser un número positivo");
                } else {
                    return numEntero;
                }
            }

        } while (true);
    }

    public int menuModificar() {
        Scanner sc = new Scanner(System.in);
        String menu = "1. Modificar Vuelo. \n "
                + "2.Añadir Pasajero \n "
                + "3.Eliminar Pasajero. \n "
                + "0)Salir \n "
                + "¿Que quiere hacer?";

        int opcion = -1; //opcio -1 indica opcion incorrecta
        while (opcion == -1) {
            System.out.println(menu);
            String entrada = sc.nextLine();
            if (!esEntero(entrada)) {
                opcion = -1;
            } else {
                opcion = Integer.parseInt(entrada);
            }
            if (opcion > 3 || opcion < 0) {
                System.out.println("Opción no valida");
                opcion = -1;
            }
        }
        return opcion;
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

    public void mostrarIdPasajeros(List<Pasajero> pasajeros) {
        System.out.println("Id de los pasajeros de la reserva:");
        String salida = "";
        for (Pasajero p : pasajeros) {
            salida += "    " + p.getId();
        }
        System.out.println(salida);
    }

    public String pedirDniOrdenDePago() {
        Scanner sc = new Scanner(System.in);
        String respuesta;
        do {
            System.out.println("Introduzca el dni del pasajero que pagará la reserva:");
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

    public Integer pedirIdOrdenPago() {
        System.out.println("------ORDEN DE PAGO--------");
        String respuesta;
        Scanner sc = new Scanner(System.in);
        do {
            System.out.println("Introduzca el id de la orden de pago.");
            int numEntero;
            respuesta = sc.nextLine();
            if (!esEntero(respuesta)) {
                System.err.println("La id debe ser un número");
            } else {
                numEntero = Integer.parseInt(respuesta);
                if (numEntero == 0) {
                    //salimos 
                    return null;
                } else if (numEntero < 0) {
                    System.err.println("La id de la orden de pago ha de ser un número positivo");
                } else {
                    return numEntero;
                }
            }

        } while (true);
    }

    public String pedirTarjetaOrdenPago() {
        String respuesta;
        Scanner sc = new Scanner(System.in);
        Long numEntero = null;
        do {
            System.out.println("Introduzca la tarjeta para realizar la orden de pago.");

            respuesta = sc.nextLine();
            if (respuesta.length() != 16) {
                System.err.println("La tarjeta ha de tener 16 carácteres");
                continue;
            }
            try {
                numEntero = Long.parseLong(respuesta);
            } catch (NumberFormatException nfe) {
                System.out.println("La tarjeta ha de ser un número");
                continue;
            }
            if (numEntero == 0) {
                //salimos 
                return null;
            } else if (numEntero < 0) {
                System.err.println("La tarjeta de credito de la orden de pago ha de ser un número positivo");
                continue;
            }
            return numEntero.toString();
        } while (true);

    }


    public void mostrarReservaDetallada(Reserva r) {
        System.out.println("--------------- Reserva -------------");
        System.out.println("ID        FECHA       CANCELADA    N_PASAJEROS    IMPORTE");
        String fechaStr;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        fechaStr = sdf.format(r.getFecha());
        System.out.printf("%-7d %-13s %-13s %-13s %-6.2f  \n", r.getId(), fechaStr, r.isCancelada() ? "Si" : "No", r.getVuelo().getCodigo(), r.getImporte());
        this.mostrarPasjeros(r.getPasajeros());
    }

    private void mostrarPasjeros(List<Pasajero> pasajeros) {
        
        System.out.println("--------------- Pasajeros -------------");
        String fechaStr;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        List<Adulto> adultos = new ArrayList<>();
        List<Ninyo> ninyos = new ArrayList<>();
        for (Pasajero p : pasajeros) {
            if (p instanceof Adulto) {
                adultos.add((Adulto) p);
            } else {
                ninyos.add((Ninyo) p);
            }
        }
        //p.getId() + "#" + p.getDni() + "#"+ p.getNombre() + "#" + p.getApellidos() + "#"+ fechaPasajero + "#" + p.getNum_maletas_facturar();
        if (!(adultos.isEmpty())) {
            System.out.println("-----------------Adultos---------------");
            System.out.println("ID      DNI         NOMBRE_COMPLETO    FECHA_NACIMIENTO   N_MALETAS   DESCUENTOS");
            for (Pasajero p : adultos) {
                fechaStr = sdf.format(p.getFecha_nacimiento());
                // ((Adulto) p).getDescuento();
                System.out.printf("%-7d %-10s %-20s %-20s %-5d %10s  \n", p.getId(), p.getDni(), p.getNombre() + " " + p.getApellidos(), fechaStr, p.getNum_maletas_facturar(), ((Adulto) p).getDescuento().toString());
                
            }
        }
        if (!ninyos.isEmpty()) {
            System.out.println("-----------------Niños---------------");
            System.out.println("ID      DNI         NOMBRE_COMPLETO    FECHA_NACIMIENTO   N_MALETAS       SOLO    SILLETA");
            for (Pasajero p : ninyos) {
                fechaStr = sdf.format(p.getFecha_nacimiento());
                // p).getSolo() + "#" + ((Ninyo) p).getSilleta();
                System.out.printf("%-7d %-10s %-20s %-20s %-5d %10s %10s  \n", p.getId(), p.getDni(), p.getNombre() + " " + p.getApellidos(), fechaStr, p.getNum_maletas_facturar(), ((Ninyo) p).getSolo() ? "Sí" : "No", ((Ninyo) p).getSilleta() ? "Sí" : "No");

            }
        }
    }

    public void mostrarOrdenesPago(List<OrdenPago> ordenes) {
        //int id, Date fechaOrden, String dniOrdente, String numTarjeta, double importe, Reserva reserva
        System.out.println("--------------- Ordenes de Pago -------------");
        System.out.println("ID      FECHA       DNI            NUM_TARJETA           IMPORTE          ID_RESERVA");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String fechaStr;
        //todo Poner esto cuadrado
        for (OrdenPago op : ordenes) {
            fechaStr = sdf.format(op.getFechaOrden());
            System.out.printf("%-7d %-11s %-13s %-25s %-15s %-6d  \n", op.getId(), fechaStr, op.getDniOrden(), op.getNumTarjeta(), op.getImporte(), op.getReserva().getId());
        }
    }
    
    public void mostrarTarjetasEmbarque(List<TarjetaEmbarque> tarjetasEmbarque) {
        //int id,String vuelo,String origen,String destino,Date fecha_vuelo,int terminal,int puerta
        System.out.println("--------------- Tarjetas de Embarque -------------");
        System.out.println("ID      COD_VUELO       ORIGEN       DESTINO           FECHA_VUELO          TERMINAL        PUERTA");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String fechaStr;
        for (TarjetaEmbarque t : tarjetasEmbarque) {
            fechaStr = sdf.format(t.getFecha_vuelo());
            System.out.printf("%-7d %-16s %-13s %-15s %-21s %-10d %6d  \n", t.getId(),t.getVuelo(),t.getOrigen(),t.getDestino(),fechaStr,t.getTerminal(),t.getPuerta());
        }
    }
}
