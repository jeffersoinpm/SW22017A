/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epn.edu.reservahotel.entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jefferson
 */
@Entity
@Table(name = "re_habitacion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ReHabitacion.findAll", query = "SELECT r FROM ReHabitacion r"),
    @NamedQuery(name = "ReHabitacion.findByFechaReservaHabitacion", query = "SELECT r FROM ReHabitacion r WHERE r.reHabitacionPK.fechaReservaHabitacion = :fechaReservaHabitacion"),
    @NamedQuery(name = "ReHabitacion.findByIdHabitacion", query = "SELECT r FROM ReHabitacion r WHERE r.reHabitacionPK.idHabitacion = :idHabitacion")})
public class ReHabitacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ReHabitacionPK reHabitacionPK;
    @JoinColumn(name = "id_habitacion", referencedColumnName = "id_habitacion", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Habitacion habitacion;
    @JoinColumn(name = "id_reserva", referencedColumnName = "id_reserva")
    @ManyToOne(optional = false)
    private Reserva idReserva;

    public ReHabitacion() {
    }

    public ReHabitacion(ReHabitacionPK reHabitacionPK) {
        this.reHabitacionPK = reHabitacionPK;
    }

    public ReHabitacion(Date fechaReservaHabitacion, int idHabitacion) {
        this.reHabitacionPK = new ReHabitacionPK(fechaReservaHabitacion, idHabitacion);
    }

    public ReHabitacionPK getReHabitacionPK() {
        return reHabitacionPK;
    }

    public void setReHabitacionPK(ReHabitacionPK reHabitacionPK) {
        this.reHabitacionPK = reHabitacionPK;
    }

    public Habitacion getHabitacion() {
        return habitacion;
    }

    public void setHabitacion(Habitacion habitacion) {
        this.habitacion = habitacion;
    }

    public Reserva getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(Reserva idReserva) {
        this.idReserva = idReserva;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reHabitacionPK != null ? reHabitacionPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ReHabitacion)) {
            return false;
        }
        ReHabitacion other = (ReHabitacion) object;
        if ((this.reHabitacionPK == null && other.reHabitacionPK != null) || (this.reHabitacionPK != null && !this.reHabitacionPK.equals(other.reHabitacionPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.epn.edu.reservahotel.entidades.ReHabitacion[ reHabitacionPK=" + reHabitacionPK + " ]";
    }
    
}
