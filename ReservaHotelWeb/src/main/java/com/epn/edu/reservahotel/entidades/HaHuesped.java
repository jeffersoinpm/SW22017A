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
 * @author Daniela Ramos
 */
@Entity
@Table(name = "ha_huesped")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "HaHuesped.findAll", query = "SELECT h FROM HaHuesped h"),
    @NamedQuery(name = "HaHuesped.findByFechaHabitacionHuesped", query = "SELECT h FROM HaHuesped h WHERE h.haHuespedPK.fechaHabitacionHuesped = :fechaHabitacionHuesped"),
    @NamedQuery(name = "HaHuesped.findByIdHabitacion", query = "SELECT h FROM HaHuesped h WHERE h.haHuespedPK.idHabitacion = :idHabitacion"),
    @NamedQuery(name = "HaHuesped.findByIdHuesped", query = "SELECT h FROM HaHuesped h WHERE h.haHuespedPK.idHuesped = :idHuesped")})
public class HaHuesped implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected HaHuespedPK haHuespedPK;
    @JoinColumn(name = "id_habitacion", referencedColumnName = "id_habitacion", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Habitacion habitacion;
    @JoinColumn(name = "id_huesped", referencedColumnName = "id_huesped", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Huesped huesped;

    public HaHuesped() {
    }

    public HaHuesped(HaHuespedPK haHuespedPK) {
        this.haHuespedPK = haHuespedPK;
    }

    public HaHuesped(Date fechaHabitacionHuesped, int idHabitacion, String idHuesped) {
        this.haHuespedPK = new HaHuespedPK(fechaHabitacionHuesped, idHabitacion, idHuesped);
    }

    public HaHuespedPK getHaHuespedPK() {
        return haHuespedPK;
    }

    public void setHaHuespedPK(HaHuespedPK haHuespedPK) {
        this.haHuespedPK = haHuespedPK;
    }

    public Habitacion getHabitacion() {
        return habitacion;
    }

    public void setHabitacion(Habitacion habitacion) {
        this.habitacion = habitacion;
    }

    public Huesped getHuesped() {
        return huesped;
    }

    public void setHuesped(Huesped huesped) {
        this.huesped = huesped;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (haHuespedPK != null ? haHuespedPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HaHuesped)) {
            return false;
        }
        HaHuesped other = (HaHuesped) object;
        if ((this.haHuespedPK == null && other.haHuespedPK != null) || (this.haHuespedPK != null && !this.haHuespedPK.equals(other.haHuespedPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.epn.edu.reservahotel.entidades.HaHuesped[ haHuespedPK=" + haHuespedPK + " ]";
    }
    
}
