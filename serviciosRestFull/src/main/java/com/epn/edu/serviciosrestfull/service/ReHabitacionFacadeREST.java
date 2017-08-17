/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epn.edu.serviciosrestfull.service;

import com.epn.edu.reservahotel.entidades.ReHabitacion;
import com.epn.edu.reservahotel.entidades.ReHabitacionPK;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.PathSegment;

/**
 *
 * @author danie
 */
@Stateless
@Path("com.epn.edu.reservahotel.entidades.rehabitacion")
public class ReHabitacionFacadeREST extends AbstractFacade<ReHabitacion> {

    @PersistenceContext(unitName = "com.epn.edu_ReservaHotelWeb_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    private ReHabitacionPK getPrimaryKey(PathSegment pathSegment) {
        /*
         * pathSemgent represents a URI path segment and any associated matrix parameters.
         * URI path part is supposed to be in form of 'somePath;fechaReservaHabitacion=fechaReservaHabitacionValue;idHabitacion=idHabitacionValue'.
         * Here 'somePath' is a result of getPath() method invocation and
         * it is ignored in the following code.
         * Matrix parameters are used as field names to build a primary key instance.
         */
        com.epn.edu.reservahotel.entidades.ReHabitacionPK key = new com.epn.edu.reservahotel.entidades.ReHabitacionPK();
        javax.ws.rs.core.MultivaluedMap<String, String> map = pathSegment.getMatrixParameters();
        java.util.List<String> fechaReservaHabitacion = map.get("fechaReservaHabitacion");
        if (fechaReservaHabitacion != null && !fechaReservaHabitacion.isEmpty()) {
            key.setFechaReservaHabitacion(new java.util.Date(fechaReservaHabitacion.get(0)));
        }
        java.util.List<String> idHabitacion = map.get("idHabitacion");
        if (idHabitacion != null && !idHabitacion.isEmpty()) {
            key.setIdHabitacion(new java.lang.Integer(idHabitacion.get(0)));
        }
        return key;
    }

    public ReHabitacionFacadeREST() {
        super(ReHabitacion.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(ReHabitacion entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") PathSegment id, ReHabitacion entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") PathSegment id) {
        com.epn.edu.reservahotel.entidades.ReHabitacionPK key = getPrimaryKey(id);
        super.remove(super.find(key));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public ReHabitacion find(@PathParam("id") PathSegment id) {
        com.epn.edu.reservahotel.entidades.ReHabitacionPK key = getPrimaryKey(id);
        return super.find(key);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<ReHabitacion> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<ReHabitacion> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
