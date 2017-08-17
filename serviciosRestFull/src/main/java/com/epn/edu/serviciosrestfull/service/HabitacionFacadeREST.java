/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epn.edu.serviciosrestfull.service;

import com.epn.edu.reservahotel.entidades.Habitacion;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
       
            List<Habitacion> results = em.createNamedQuery("Habitacion.findByIdTipoHabitacion", Habitacion.class)
                    .setParameter("idTipoHabitacion", id).getResultList();

            return (List<Habitacion>) results;

    }

    @GET
    @Path("/findHabitacionesDisponiblesUnDia/{fechaActual}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Habitacion> findHabitacionesDisponiblesUnDia(@PathParam("fechaActual") String fechaActual) throws ParseException {
            System.out.println("llego");
            SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MM-yyyy");
            Date d=dateFormat.parse(fechaActual);
            
            List<Habitacion> results = em.createNamedQuery("Habitacion.findDisponiblesByUnDia", Habitacion.class)
                    .setParameter("fechaReservaHabitacion", d).getResultList();

            return (List<Habitacion>) results;

    }
    
    @GET
    @Path("findHabitacionesDisponiblesUnDiaAndTipoHabitacion/{fechaActual}/{idTipoHabitacion}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Habitacion> findHabitacionesDisponiblesUnDiaAndTipoHabitacion(@PathParam("fechaActual") String fechaActual, @PathParam("idTipoHabitacion") Integer idTipoHabitacion) throws ParseException {
       
            Query query = em.createNamedQuery("Habitacion.findDisponiblesByUnDiaAndTipoHabitacion", Habitacion.class);
            SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MM-yyyy");
            Date d=dateFormat.parse(fechaActual);
            query.setParameter("fechaReservaHabitacion", d);
            query.setParameter("idTipoHabitacion", idTipoHabitacion);
            List<Habitacion> results = query.getResultList();

            return (List<Habitacion>) results;

    }
    
    @GET
    @Path("findHabitacionesDisponiblesRangoDias/{fechaInicio}/{fechaFin}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Habitacion> findHabitacionesDisponiblesRangoDias(@PathParam("fechaInicio") String fechaInicio, @PathParam("fechaFin") String fechaFin) throws ParseException {
        
            Query query = em.createNamedQuery("Habitacion.findDisponiblesByRangoDias", Habitacion.class);
            
            SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MM-yyyy");
            Date d=dateFormat.parse(fechaInicio);
            Date b=dateFormat.parse(fechaFin);
            query.setParameter("fechaReservaHabitacionInicio", d);
            query.setParameter("fechaReservaHabitacionfin", b);
            List<Habitacion> results = query.getResultList();

            return (List<Habitacion>) results;

    }
    
    @GET
    @Path("findHabitacionesDisponiblesRangoDiasAndTipoHabitacion/{fechaInicio}/{fechaFin}/{idTipoHabitacion}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Habitacion> findHabitacionesDisponiblesRangoDiasAndTipoHabitacion(@PathParam("fechaInicio") String fechaInicio, @PathParam("fechaFin") String fechaFin, @PathParam("idTipoHabitacion") Integer idTipoHabitacion) throws ParseException {
       
            Query query = em.createNamedQuery("Habitacion.findDisponiblesByRangoDiasAndTipoHabitacion", Habitacion.class);
            SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MM-yyyy");
            Date d=dateFormat.parse(fechaInicio);
            Date b=dateFormat.parse(fechaFin);
            query.setParameter("fechaReservaHabitacionInicio", d);
            query.setParameter("fechaReservaHabitacionfin", b);
            query.setParameter("idTipoHabitacion", idTipoHabitacion);
            List<Habitacion> results = query.getResultList();

            return (List<Habitacion>) results;

    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
