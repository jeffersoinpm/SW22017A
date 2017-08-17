/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epn.edu.serviciosrestfull.service;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author danie
 */
@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(com.epn.edu.serviciosrestfull.service.ExtraHabitacionFacadeREST.class);
        resources.add(com.epn.edu.serviciosrestfull.service.HaHuespedFacadeREST.class);
        resources.add(com.epn.edu.serviciosrestfull.service.HabitacionFacadeREST.class);
        resources.add(com.epn.edu.serviciosrestfull.service.HuespedFacadeREST.class);
        resources.add(com.epn.edu.serviciosrestfull.service.MenuFacadeREST.class);
        resources.add(com.epn.edu.serviciosrestfull.service.PerfilFacadeREST.class);
        resources.add(com.epn.edu.serviciosrestfull.service.ReHabitacionFacadeREST.class);
        resources.add(com.epn.edu.serviciosrestfull.service.ReservaFacadeREST.class);
        resources.add(com.epn.edu.serviciosrestfull.service.ServicioFacadeREST.class);
        resources.add(com.epn.edu.serviciosrestfull.service.TipoHabitacionFacadeREST.class);
        resources.add(com.epn.edu.serviciosrestfull.service.UsuarioFacadeREST.class);
    }
    
}
