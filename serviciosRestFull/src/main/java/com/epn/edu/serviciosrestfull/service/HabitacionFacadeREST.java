/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epn.edu.serviciosrestfull.service;

import com.epn.edu.reservahotel.entidades.Habitacion;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author danie
 */
@Stateless
@Path("com.epn.edu.reservahotel.entidades.habitacion")
public class HabitacionFacadeREST extends AbstractFacade<Habitacion> {

    @PersistenceContext(unitName = "com.epn.edu_ReservaHotelWeb_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    public HabitacionFacadeREST() {
        super(Habitacion.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Habitacion entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Habitacion entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Habitacion find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Habitacion> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Habitacion> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }
    
    @GET
    @Path("findHabitacionbyTypeHabitacionId/{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Habitacion> findHabitacionbyTypeHabitacionId(@PathParam("id") Integer id) {
        EntityManager em = getEntityManager();
        try {
            List<Habitacion> results = em.createNamedQuery("Habitacion.findByIdTipoHabitacion", Habitacion.class)
                    .setParameter("idTipoHabitacion", id).getResultList();

            return (List<Habitacion>) results;

        } finally {
            em.close();
        }
    }

    @GET
    @Path("findHabitacionesDisponiblesUnDia/{fechaActual}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Habitacion> findHabitacionesDisponiblesUnDia(@PathParam("fechaActual") Date fechaActual) {
        EntityManager em = getEntityManager();
        try {

            List<Habitacion> results = em.createNamedQuery("Habitacion.findDisponiblesByUnDia", Habitacion.class)
                    .setParameter("fechaReservaHabitacion", fechaActual).getResultList();

            return (List<Habitacion>) results;

        } finally {
            em.close();
        }
    }
    
    @GET
    @Path("findHabitacionesDisponiblesUnDiaAndTipoHabitacion/{fechaActual}/{idTipoHabitacion}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Habitacion> findHabitacionesDisponiblesUnDiaAndTipoHabitacion(@PathParam("fechaActual") Date fechaActual, @PathParam("idTipoHabitacion") Integer idTipoHabitacion) {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createNamedQuery("Habitacion.findDisponiblesByUnDiaAndTipoHabitacion", Habitacion.class);
            query.setParameter("fechaReservaHabitacion", fechaActual);
            query.setParameter("idTipoHabitacion", idTipoHabitacion);
            List<Habitacion> results = query.getResultList();

            return (List<Habitacion>) results;

        } finally {
            em.close();
        }
    }
    
    @GET
    @Path("findHabitacionesDisponiblesRangoDias/{fechaInicio}/{fechaFin}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Habitacion> findHabitacionesDisponiblesRangoDias(@PathParam("fechaInicio") Date fechaInicio, @PathParam("fechaFin") Date fechaFin) {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createNamedQuery("Habitacion.findDisponiblesByRangoDias", Habitacion.class);
            query.setParameter("fechaReservaHabitacionInicio", fechaInicio);
            query.setParameter("fechaReservaHabitacionfin", fechaFin);
            List<Habitacion> results = query.getResultList();

            return (List<Habitacion>) results;

        } finally {
            em.close();
        }
    }
    
    @GET
    @Path("findHabitacionesDisponiblesRangoDiasAndTipoHabitacion/{fechaInicio}/{fechaFin}/{idTipoHabitacion}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Habitacion> findHabitacionesDisponiblesRangoDiasAndTipoHabitacion(@PathParam("fechaInicio") Date fechaInicio, @PathParam("fechaFin") Date fechaFin, @PathParam("idTipoHabitacion") Integer idTipoHabitacion) {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createNamedQuery("Habitacion.findDisponiblesByRangoDiasAndTipoHabitacion", Habitacion.class);
            query.setParameter("fechaReservaHabitacionInicio", fechaInicio);
            query.setParameter("fechaReservaHabitacionfin", fechaFin);
            query.setParameter("idTipoHabitacion", idTipoHabitacion);
            List<Habitacion> results = query.getResultList();

            return (List<Habitacion>) results;

        } finally {
            em.close();
        }
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
