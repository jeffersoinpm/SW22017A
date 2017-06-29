/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epn.edu.reservahotel.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Daniela Ramos
 */
@Entity
@Table(name = "extra_habitacion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ExtraHabitacion.findAll", query = "SELECT e FROM ExtraHabitacion e"),
    @NamedQuery(name = "ExtraHabitacion.findByIdExtraHabitacion", query = "SELECT e FROM ExtraHabitacion e WHERE e.idExtraHabitacion = :idExtraHabitacion"),
    @NamedQuery(name = "ExtraHabitacion.findByJacuzzi", query = "SELECT e FROM ExtraHabitacion e WHERE e.jacuzzi = :jacuzzi"),
    @NamedQuery(name = "ExtraHabitacion.findByVistaAlMar", query = "SELECT e FROM ExtraHabitacion e WHERE e.vistaAlMar = :vistaAlMar"),
    @NamedQuery(name = "ExtraHabitacion.findByCostoTotal", query = "SELECT e FROM ExtraHabitacion e WHERE e.costoTotal = :costoTotal")})
public class ExtraHabitacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_extra_habitacion")
    private Integer idExtraHabitacion;
    private Boolean jacuzzi;
    @Column(name = "vista_al_mar")
    private Boolean vistaAlMar;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "costo_total")
    private BigDecimal costoTotal;
    @OneToMany(mappedBy = "idExtraHabitacion")
    private List<Habitacion> habitacionList;

    public ExtraHabitacion() {
    }

    public ExtraHabitacion(Integer idExtraHabitacion) {
        this.idExtraHabitacion = idExtraHabitacion;
    }

    public ExtraHabitacion(Integer idExtraHabitacion, BigDecimal costoTotal) {
        this.idExtraHabitacion = idExtraHabitacion;
        this.costoTotal = costoTotal;
    }

    public Integer getIdExtraHabitacion() {
        return idExtraHabitacion;
    }

    public void setIdExtraHabitacion(Integer idExtraHabitacion) {
        this.idExtraHabitacion = idExtraHabitacion;
    }

    public Boolean getJacuzzi() {
        return jacuzzi;
    }

    public void setJacuzzi(Boolean jacuzzi) {
        this.jacuzzi = jacuzzi;
    }

    public Boolean getVistaAlMar() {
        return vistaAlMar;
    }

    public void setVistaAlMar(Boolean vistaAlMar) {
        this.vistaAlMar = vistaAlMar;
    }

    public BigDecimal getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(BigDecimal costoTotal) {
        this.costoTotal = costoTotal;
    }

    @XmlTransient
    public List<Habitacion> getHabitacionList() {
        return habitacionList;
    }

    public void setHabitacionList(List<Habitacion> habitacionList) {
        this.habitacionList = habitacionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idExtraHabitacion != null ? idExtraHabitacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ExtraHabitacion)) {
            return false;
        }
        ExtraHabitacion other = (ExtraHabitacion) object;
        if ((this.idExtraHabitacion == null && other.idExtraHabitacion != null) || (this.idExtraHabitacion != null && !this.idExtraHabitacion.equals(other.idExtraHabitacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.epn.edu.reservahotel.entidades.ExtraHabitacion[ idExtraHabitacion=" + idExtraHabitacion + " ]";
    }
    
}
