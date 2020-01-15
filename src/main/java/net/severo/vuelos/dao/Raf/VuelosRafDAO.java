/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.severo.vuelos.dao.Raf;

import java.io.*;
import java.nio.DoubleBuffer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import net.severo.vuelos.dao.DAOException;
import net.severo.vuelos.dao.IVuelosDAO;
import net.severo.vuelos.estructura.Configuracion;
import net.severo.vuelos.estructura.Vuelo;

/**
 * @author
 */
public class VuelosRafDAO implements IVuelosDAO {

    private static final int SIZE_CODIGO = 16; // 8 caracteres
    private static final int SIZE_ORIGEN = 10; // 5 caracteres
    private static final int SIZE_DESTINO = 10; // 5 caracteres
    private static final int SIZE_PRECIO_PERSONA = 8; // double
    private static final int SIZE_FECHA = 20; // 10 caracteres
    private static final int SIZE_PLAZAS_TERMINAL_PUERTA = 4; // int
    private static final int SIZE_TOTAL = SIZE_CODIGO + SIZE_ORIGEN + SIZE_DESTINO + SIZE_PRECIO_PERSONA + SIZE_FECHA
            + SIZE_PLAZAS_TERMINAL_PUERTA * 3;
    //Tamaño total 72

    private File archivoVuelo = null;
    private File archivoDeReferencias = null;

    public VuelosRafDAO() throws DAOException {
        try {
            Configuracion configuracion = new Configuracion("/configuracion.properties");
            this.archivoDeReferencias=new File(configuracion.getRafVuelosReferencias());
            this.archivoVuelo=new File(configuracion.getRafVuelos());
        } catch (NullPointerException ex) {
            throw new DAOException("No ha sido posible abrir el archivo de acceso aleatorio", ex);
        }
    }
    /*private String codigo;
    private String origen;
    private String destino;
    private double precioPersona;
    private Date fechaVuelo;
    private int plazasDisponibles;
    private int terminal;
    private int puerta;*/

    @Override
    public void crearVuelo(Vuelo v) throws DAOException {
        try (RandomAccessFile file = new RandomAccessFile(archivoVuelo, "rw"); PrintWriter pw = new PrintWriter(new FileWriter(archivoDeReferencias, true))) {
            Map<String,Integer> mapa=obteneMapaConPosiciones();
            int posicion=mapa.size();
            int posicionSeek = posicion * SIZE_TOTAL;
            file.seek(posicionSeek); // Me situo en la posición adecuada

            //Inserto el código de vuelo
            StringBuffer buffer = new StringBuffer(v.getCodigo());
            buffer.setLength(SIZE_CODIGO/2);
            file.writeChars(buffer.toString());

            //Inserto el destino del vuelo
            buffer = new StringBuffer(v.getDestino());
            buffer.setLength(SIZE_DESTINO/2);
            file.writeChars(buffer.toString());


            //Inserto el origen del vuelo
            buffer = new StringBuffer(v.getOrigen());
            buffer.setLength(SIZE_ORIGEN/2);
            file.writeChars(buffer.toString());



            //Inserto la fecha del vuelo
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String fecha=sdf.format(v.getFechaVuelo());
            buffer = new StringBuffer(fecha);
            buffer.setLength(SIZE_FECHA/2);
            file.writeChars(buffer.toString());

            //Inserto el precio por persona
            file.writeDouble(v.getPrecioPersona());

            //Inserto las plazas disponibles
            file.writeInt(v.getPlazasDisponibles());

            //Inserto la puerta del vuelo
            file.writeInt(v.getPuerta());

            //Inserto la terminal del vuelo
            file.writeInt(v.getTerminal());

            pw.println(v.getCodigo() + "#" +posicion );
        } catch (FileNotFoundException ex) {
            throw new DAOException("No existe el Fichero aleatorio.", ex);
        } catch (IOException ex) {
            throw new DAOException("Al leer o escribir en el fichero hubo un error.", ex);
        }
    }


    @Override
    public void modificarVuelo(Vuelo v) throws DAOException {
        try (RandomAccessFile file = new RandomAccessFile(archivoVuelo, "rw")) {
            Map<String,Integer> mapa=obteneMapaConPosiciones();
            int posicion=mapa.get(v.getCodigo());
            int posicionSeek = posicion * SIZE_TOTAL;
            file.seek(posicionSeek); // Me situo en la posición adecuada

            //Inserto el código de vuelo
            StringBuffer buffer = new StringBuffer(v.getCodigo());
            buffer.setLength(SIZE_CODIGO/2);
            file.writeChars(buffer.toString());

            //Inserto el destino del vuelo
            buffer = new StringBuffer(v.getDestino());
            buffer.setLength(SIZE_DESTINO/2);
            file.writeChars(buffer.toString());


            //Inserto el origen del vuelo
            buffer = new StringBuffer(v.getOrigen());
            buffer.setLength(SIZE_ORIGEN/2);
            file.writeChars(buffer.toString());

            //Inserto la fecha del vuelo
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String fecha=sdf.format(v.getFechaVuelo());
            buffer = new StringBuffer(fecha);
            buffer.setLength(SIZE_FECHA/2);
            file.writeChars(buffer.toString());

            //Inserto el precio por persona
            file.writeDouble(v.getPrecioPersona());

            //Inserto las plazas disponibles
            file.writeInt(v.getPlazasDisponibles());

            //Inserto la puerta del vuelo
            file.writeInt(v.getPuerta());

            //Inserto la terminal del vuelo
            file.writeInt(v.getTerminal());

        } catch (FileNotFoundException ex) {
            throw new DAOException("No existe el Fichero aleatorio.", ex);
        } catch (IOException ex) {
            throw new DAOException("Al leer o escribir en el fichero hubo un error.", ex);
        }
    }

    @Override
    public void eliminarVuelo(String codigo) throws DAOException {
        Map<String, Integer> mapa = obteneMapaConPosiciones();
        int posicion = mapa.get(codigo);
        try (RandomAccessFile raf = new RandomAccessFile(archivoVuelo, "rw");
             PrintWriter pw = new PrintWriter(new FileWriter(archivoDeReferencias,false))) {
            raf.seek(posicion*SIZE_TOTAL);
            StringBuffer buffer = new StringBuffer("0");
            buffer.setLength(SIZE_CODIGO/2);
            raf.writeChars(buffer.toString());
            //Vaciamos el fichero y escribimos el mapa de nuevo,sin el vuelo anterior
            archivoDeReferencias.delete();
            archivoDeReferencias.createNewFile();
            mapa.remove(codigo);
            for(String clave:mapa.keySet()){
                pw.println(clave+"#"+mapa.get(clave));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

        @Override
    public Vuelo obtenerVuelo(String codigo) throws DAOException {
        try (RandomAccessFile raf = new RandomAccessFile(archivoVuelo, "r")) {
            Map<String, Integer> posiciones = obteneMapaConPosiciones();
            int posicion;
            try {
                posicion=posiciones.get(codigo);
            }catch (NullPointerException nfe){
                return null;
            }
            raf.seek(SIZE_TOTAL * posicion);
            //codigo
            char[] lectorString = new char[SIZE_CODIGO/2];
            for (int i = 0; i < lectorString.length; i++) {
                lectorString[i] = raf.readChar();
            }
            String codigoVuelo = new String(lectorString);
            codigoVuelo=codigoVuelo.trim();
            try {
                if (Integer.parseInt(codigoVuelo) == 0) {
                    return null;
                }
            } catch (NumberFormatException nfe) {
            }
            //origen
            lectorString = new char[SIZE_ORIGEN/2];
            for (int i = 0; i < lectorString.length; i++) {
                lectorString[i] = raf.readChar();
            }
            String origen = new String(lectorString);
            origen=origen.trim();
            //destino
            lectorString = new char[SIZE_DESTINO/2];
            for (int i = 0; i < lectorString.length; i++) {
                lectorString[i] = raf.readChar();
            }
            String destino = new String(lectorString);
            destino=destino.trim();
            //fechaVuelo
            lectorString = new char[SIZE_FECHA/2];
            for (int i = 0; i < lectorString.length; i++) {
                lectorString[i] = raf.readChar();
            }
            String cadenaFecha=new String(lectorString);
            cadenaFecha=cadenaFecha.trim();
            Date fecha = new SimpleDateFormat("dd/MM/yyyy").parse(cadenaFecha);
            //precioPersona
            double precioPersona = raf.readDouble();
            //plazas
            int plazas = raf.readInt();
            //terminal
            int terminal = raf.readInt();
            //puerta
            int puerta = raf.readInt();
            return new Vuelo(codigoVuelo, origen, destino, precioPersona, fecha, plazas, puerta, terminal);
        } catch (FileNotFoundException e) {
            throw new DAOException("No se ha encontrado el fichero binario", e);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
            throw new DAOException("Error al intentar leer la fecha", e);
        }
return null;

    }

    @Override
    public List<Vuelo> obtenerVuelos(Date fechaSalida) throws DAOException {
        List<Vuelo> vuelos=new ArrayList<>();
        for(Vuelo v:obtenerVuelos()){
            if(fechaSalida.equals(v.getFechaVuelo())){
                vuelos.add(v);
            }
        }
        return vuelos;
    }

    @Override
    public List<Vuelo> obtenerVuelos() throws DAOException {
        List<Vuelo> vuelos=new ArrayList<>();
        for(String cod:obteneMapaConPosiciones().keySet()){
            Vuelo v=obtenerVuelo(cod);
            if(v!=null) {
                vuelos.add(v);
            }
        }
        return vuelos;
    }

    public Map<String,Integer> obteneMapaConPosiciones(){
        Map<String, Integer> posiciones = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(archivoDeReferencias))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos=linea.split("#");
                posiciones.put(datos[0], Integer.parseInt(datos[1]));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return posiciones;
    }
}
