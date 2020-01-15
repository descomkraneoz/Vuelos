/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.severo.vuelos.dao;

/**
 *
 * @author Usuario
 */
public class DAOException extends Exception{
    public DAOException(String msg, Exception e) {
	        super(msg,e);
	    }
}
