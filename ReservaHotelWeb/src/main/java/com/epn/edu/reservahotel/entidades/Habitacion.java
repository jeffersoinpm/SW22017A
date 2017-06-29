/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epn.edu.reservahotel.entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jefferson
 */
@Entity
@Table(name = "habitacion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Habitacion.findAll", query = "SELECT h FROM Habitacion h"),
    @NamedQuery(name = "Habitacion.findByIdHabitacion", query = "SELECT h FROM Habitacion h WHERE h.idHabitacion = :idHabitacion"),
    @NamedQuery(name = "Habitacion.findDisponiblesByUnDia", query = "SELECT h FROM Habitacion h WHERE h.idHabitacion NOT IN (SELECT p.reHabitacionPK.idHabitacion FROM ReHabitacion p WHERE p.reHabitacionPK.fechaReservaHabitacion=:fechaReservaHabitacion)"),
    @NamedQuery(name = "Habitacion.findByIdTipoHabitacion", query = "SELECT h FROM Habitacion h WHERE h.idTipoHabitacion.idTipoHabitacion = :idTipoHabitacion"),
    @NamedQuery(name = "Habitacion.findDisponiblesByRangoDias", query = "SELECT h FROM Habitacion h WHERE h.idHabitacion NOT IN (SELECT p.reHabitacionPK.idHabitacion FROM ReHabitacion p WHERE p.reHabitacionPK.fechaReservaHabitacion between :fechaReservaHabitacionInicio and :fechaReservaHabitacionfin)"),
    @NamedQuery(name = "Habitacion.findDisponiblesByRangoDiasAndTipoHabitacion", query = "SELECT h FROM Habitacion h WHERE h.idHabitacion NOT IN (SELECT p.reHabitacionPK.idHabitacion FROM ReHabitacion p WHERE p.reHabitacionPK.fechaReservaHabitacion between :fechaReservaHabitacionInicio and :fechaReservaHabitacionfin) and h.idTipoHabitacion.idTipoHabitacion = :idTipoHabitacion"),
    @NamedQuery(name = "Habitacion.findByNumeroPiso", query = "SELECT h FROM Habitacion h WHERE h.numeroPiso = :numeroPiso")})
public class Habitacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_habitacion")
    private Integer idHabitacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "numero_piso")
    private int numeroPiso;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "habitacion")
    private List<ReHabitacion> reHabitacionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "habitacion")
    private List<HaHuesped> haHuespedList;
    @JoinColumn(name = "id_extra_habitacion", referencedColumnName = "id_extra_habitacion")
    @ManyToOne
    private ExtraHabitacion idExtraHabitacion;
    @JoinColumn(name = "id_tipo_habitacion", referencedColumnName = "id_tipo_habitacion")
    @ManyToOne(optional = false)
    private TipoHabitacion idTipoHabitacion;

    public Habitacion() {
    }

    public Habitacion(Integer idHabitacion) {
        this.idHabitacion = idHabitacion;
    }

    public Habitacion(Integer idHabitacion, int numeroPiso) {
        this.idHabitacion = idHabitacion;
        this.numeroPiso = numeroPiso;
    }

    public Integer getIdHabitacion() {
        return idHabitacion;
    }

    public void setIdHabitacion(Integer idHabitacion) {
        this.idHabitacion = idHabitacion;
    }

    public int getNumeroPiso() {
        return numeroPiso;
    }

    public void setNumeroPiso(int numeroPiso) {
        this.numeroPiso = numeroPiso;
    }

    @XmlTransient
    public List<ReHabitacion> getReHabitacionList() {
        return reHabitacionList;
    }

    public void setReHabitacionList(List<ReHabitacion> reHabitacionList) {
        this.reHabitacionList = reHabitacionList;
    }

    @XmlTransient
    public List<HaHuesped> getHaHuespedList() {
        return haHuespedList;
    }

    public void setHaHuespedList(List<HaHuesped> haHuespedList) {
        this.haHuespedList = haHuespedList;
    }

    public ExtraHabitacion getIdExtraHabitacion() {
        return idExtraHabitacion;
    }

    public void setIdExtraHabitacion(ExtraHabitacion idExtraHabitacion) {
        this.idExtraHabitacion = idExtraHabitacion;
    }

    public TipoHabitacion getIdTipoHabitacion() {
        return idTipoHabitacion;
    }

    public void setIdTipoHabitacion(TipoHabitacion idTipoHabitacion) {
        this.idTipoHabitacion = idTipoHabitacion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idHabitacion != null ? idHabitacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Habitacion)) {
            return false;
        }
        Habitacion other = (Habitacion) object;
        if ((this.idHabitacion == null && other.idHabitacion != null) || (this.idHabitacion != null && !this.idHabitacion.equals(other.idHabitacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.epn.edu.reservahotel.entidades.Habitacion[ idHabitacion=" + idHabitacion + " ]";
    }
    
}
