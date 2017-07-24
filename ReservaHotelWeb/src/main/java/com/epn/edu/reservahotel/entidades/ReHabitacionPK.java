/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epn.edu.reservahotel.entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author jefferson
 */
@Embeddable
public class ReHabitacionPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_reserva_habitacion")
    @Temporal(TemporalType.DATE)
    private Date fechaReservaHabitacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_habitacion")
    private int idHabitacion;

    public ReHabitacionPK() {
    }

    public ReHabitacionPK(Date fechaReservaHabitacion, int idHabitacion) {
        this.fechaReservaHabitacion = fechaReservaHabitacion;
        this.idHabitacion = idHabitacion;
    }

    public Date getFechaReservaHabitacion() {
        return fechaReservaHabitacion;
    }

    public void setFechaReservaHabitacion(Date fechaReservaHabitacion) {
        this.fechaReservaHabitacion = fechaReservaHabitacion;
    }

    public int getIdHabitacion() {
        return idHabitacion;
    }

    public void setIdHabitacion(int idHabitacion) {
        this.idHabitacion = idHabitacion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (fechaReservaHabitacion != null ? fechaReservaHabitacion.hashCode() : 0);
        hash += (int) idHabitacion;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ReHabitacionPK)) {
            return false;
        }
        ReHabitacionPK other = (ReHabitacionPK) object;
        if ((this.fechaReservaHabitacion == null && other.fechaReservaHabitacion != null) || (this.fechaReservaHabitacion != null && !this.fechaReservaHabitacion.equals(other.fechaReservaHabitacion))) {
            return false;
        }
        if (this.idHabitacion != other.idHabitacion) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.epn.edu.reservahotel.entidades.ReHabitacionPK[ fechaReservaHabitacion=" + fechaReservaHabitacion + ", idHabitacion=" + idHabitacion + " ]";
    }
    
}
