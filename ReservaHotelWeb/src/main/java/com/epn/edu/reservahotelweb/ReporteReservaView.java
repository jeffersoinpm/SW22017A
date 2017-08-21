/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epn.edu.reservahotelweb;

import com.epn.edu.reservahotel.entidades.ReHabitacion;
import com.epn.edu.reservahotel.entidades.Reserva;
import com.epn.edu.reservahotel.jpacontroller.ReservaJpaController;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.transaction.UserTransaction;

/**
 *
 * @author mathcrap
 */

@SessionScoped
@ManagedBean(name = "reporteReservaView")
public class ReporteReservaView {
    @PersistenceUnit(unitName = "com.epn.edu_ReservaHotelWeb_war_1.0-SNAPSHOTPU")
    private EntityManagerFactory emf;
    @Resource
    private UserTransaction utx;
    
    
    ReservaJpaController reservaController;
    
    List<ReHabitacion> listReHabitacion = new ArrayList<>();

    public ReporteReservaView(){
        
    }
    
    
    @PostConstruct
    public void init() {
        reservaController = new ReservaJpaController(utx, emf);
    }
    public List<ReHabitacion> getListReHabitacion() {
        return listReHabitacion;
    }

    public void setListReHabitacion(List<ReHabitacion> listReHabitacion) {
        this.listReHabitacion = listReHabitacion;
    }
    
    public void consultarReHabitaciones(){
        
        Date date = new Date();
        Integer idUser = 1;
        List<Reserva> reservas = reservaController.findAllReservaByFechaInicio(date, idUser);
        
        for (Reserva reserva : reservas) { 
            listReHabitacion.addAll(reserva.getReHabitacionList());
        }
    }
    
    
}
