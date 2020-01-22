/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.severo.vuelos.dao.txt;

import net.severo.vuelos.dao.DAOException;
import net.severo.vuelos.dao.IVuelosDAO;
import net.severo.vuelos.estructura.Configuracion;
import net.severo.vuelos.estructura.Vuelo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Usuario
 */
public class VueloTxtDAO implements IVuelosDAO {

    private String archivoVuelo = null;

    public VueloTxtDAO() throws DAOException{
        Configuracion configuracion=new Configuracion("/configuracion.properties");
        this.archivoVuelo=configuracion.getTxtVuelos();
    }

    /*@Override
    public void crearVuelo(Vuelo vuelo)  throws DAOException {
        List<Vuelo> vuelos=sacarVuelosTxt();
        vuelos.add(vuelo);
        this.guardarVuelosTxt(vuelos);
    }*/
    
    @Override
    public void crearVuelo(Vuelo v)  throws DAOException {
        try ( PrintWriter pw = new PrintWriter(new FileWriter(archivoVuelo,true))) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String fechaStr = sdf.format(v.getFechaVuelo());
                String cadena = v.getCodigo() + "#" + v.getOrigen() + "#" + v.getDestino() + "#" + v.getPrecioPersona()
                        + "#" + fechaStr + "#" + v.getPlazasDisponibles() + "#" +v.getPuerta()  + "#" + v.getTerminal();
                pw.println(cadena);
        } catch (Exception e) {
            throw new DAOException(
                    "Ha habido un problema al guardar los vuelos en el archivo de texto:", e);
        }
    }

    @Override
    public void modificarVuelo(Vuelo v) throws DAOException {
        this.eliminarVuelo(v.getCodigo());
        this.crearVuelo(v);
    }

    @Override
    public void eliminarVuelo(String codigo) throws DAOException{
        List<Vuelo> vuelos=this.sacarVuelosTxt();
        Vuelo vueloAEliminar=this.obtenerVuelo(codigo);
        vuelos.remove(vueloAEliminar);
        this.guardarVuelosTxt(vuelos);
    }

    @Override
    public Vuelo obtenerVuelo(String codigo)throws DAOException{
        List<Vuelo> vuelos=sacarVuelosTxt();
        Vuelo vuelo=new Vuelo();
        vuelo.setCodigo(codigo);
        if(vuelos.contains(vuelo)){
            for(Vuelo v:vuelos){
                if(v.equals(vuelo)){
                return v;
                }
            }
        }
        return null;
    }

    @Override
    public List<Vuelo> obtenerVuelos(Date fechaSalida) throws DAOException {
        List<Vuelo> vuelosEnFecha=new ArrayList<>();
        for(Vuelo v:obtenerVuelos()){
            if(v.getFechaVuelo().equals(fechaSalida)){
                vuelosEnFecha.add(v);
            }
        }
        return vuelosEnFecha;
    }

    @Override
    public List<Vuelo> obtenerVuelos() throws DAOException{
        return this.sacarVuelosTxt();
    }

    @Override
    public void finalizar() throws DAOException {

    }

    @Override
    public void iniciarTransaccion() throws DAOException {

    }

    @Override
    public void finalizarTransaccion() throws DAOException {

    }

    public void guardarVuelosTxt(List<Vuelo> vuelos) throws DAOException {

        try ( PrintWriter pw = new PrintWriter(new FileWriter(archivoVuelo, false))) {
            for (Vuelo v : vuelos) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String fechaStr = sdf.format(v.getFechaVuelo());
                String cadena = v.getCodigo() + "#" + v.getOrigen() + "#" + v.getDestino() + "#" + v.getPrecioPersona()
                        + "#" + fechaStr + "#" + v.getPlazasDisponibles() + "#" + v.getPuerta() + "#" + v.getTerminal();
                pw.println(cadena);
            }

        } catch (Exception e) {
            throw new DAOException(
                    "Ha habido un problema al guardar los vuelos en el archivo de texto:", e);
        }

    }

    public List<Vuelo> sacarVuelosTxt() throws DAOException {
        List<Vuelo> vuelos = new ArrayList<>();

        try ( BufferedReader br = new BufferedReader(new FileReader(archivoVuelo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split("#");
                String codigo = datos[0];
                String origen = datos[1];
                String destino = datos[2];
                Double precioPersona = Double.parseDouble(datos[3]);

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date fechaVuelo = sdf.parse(datos[4]);

                int plazasDisponibles = Integer.parseInt(datos[5]);
                int terminal = Integer.parseInt(datos[7]);
                int puerta = Integer.parseInt(datos[6]);
                Vuelo v=new Vuelo(codigo, origen, destino, precioPersona, fechaVuelo, plazasDisponibles);   
                v.setTerminal(terminal);
                v.setPuerta(puerta);
                
                vuelos.add(v);

            }

            return vuelos;

        } catch (Exception e) {
            throw new DAOException(
                    "Ha habido un problema al obtener los vuelos desde el archivo de texto:",e);
        }
    }
}
