/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epn.edu.reservahotelweb;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.transaction.UserTransaction;

/**
 *
 * @author jefferson
 */
@ViewScoped
@ManagedBean(name = "cancelacionReservasView")
public class CancelacionReservasView {
    @ManagedProperty("#{userloginView}")
    UserLoginView loginView;
    
    public String cabeceraTabla;
    
    
    @PersistenceUnit(unitName = "com.epn.edu_ReservaHotelWeb_war_1.0-SNAPSHOTPU")
    private EntityManagerFactory emf;
    @Resource
    private UserTransaction utx;

    
    @PostConstruct
    public void init() {
        cabeceraTabla = "Reservas";
    }
}