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
import javax.validation.constraints.Size;

/**
 *
 * @author jefferson
 */
@Embeddable
public class HaHuespedPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_habitacion_huesped")
    @Temporal(TemporalType.DATE)
    private Date fechaHabitacionHuesped;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_habitacion")
    private int idHabitacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "id_huesped")
    private String idHuesped;

    public HaHuespedPK() {
    }

    public HaHuespedPK(Date fechaHabitacionHuesped, int idHabitacion, String idHuesped) {
        this.fechaHabitacionHuesped = fechaHabitacionHuesped;
        this.idHabitacion = idHabitacion;
        this.idHuesped = idHuesped;
    }

    public Date getFechaHabitacionHuesped() {
        return fechaHabitacionHuesped;
    }

    public void setFechaHabitacionHuesped(Date fechaHabitacionHuesped) {
        this.fechaHabitacionHuesped = fechaHabitacionHuesped;
    }

    public int getIdHabitacion() {
        return idHabitacion;
    }

    public void setIdHabitacion(int idHabitacion) {
        this.idHabitacion = idHabitacion;
    }

    public String getIdHuesped() {
        return idHuesped;
    }

    public void setIdHuesped(String idHuesped) {
        this.idHuesped = idHuesped;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (fechaHabitacionHuesped != null ? fechaHabitacionHuesped.hashCode() : 0);
        hash += (int) idHabitacion;
        hash += (idHuesped != null ? idHuesped.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HaHuespedPK)) {
            return false;
        }
        HaHuespedPK other = (HaHuespedPK) object;
        if ((this.fechaHabitacionHuesped == null && other.fechaHabitacionHuesped != null) || (this.fechaHabitacionHuesped != null && !this.fechaHabitacionHuesped.equals(other.fechaHabitacionHuesped))) {
            return false;
        }
        if (this.idHabitacion != other.idHabitacion) {
            return false;
        }
        if ((this.idHuesped == null && other.idHuesped != null) || (this.idHuesped != null && !this.idHuesped.equals(other.idHuesped))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.epn.edu.reservahotel.entidades.HaHuespedPK[ fechaHabitacionHuesped=" + fechaHabitacionHuesped + ", idHabitacion=" + idHabitacion + ", idHuesped=" + idHuesped + " ]";
    }
    
}
