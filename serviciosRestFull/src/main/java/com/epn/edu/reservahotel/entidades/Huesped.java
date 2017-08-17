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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jefferson
 */
@Entity
@Table(name = "huesped")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Huesped.findAll", query = "SELECT h FROM Huesped h"),
    @NamedQuery(name = "Huesped.findByIdHuesped", query = "SELECT h FROM Huesped h WHERE h.idHuesped = :idHuesped"),
    @NamedQuery(name = "Huesped.findByNombreHuesped", query = "SELECT h FROM Huesped h WHERE h.nombreHuesped = :nombreHuesped")})
public class Huesped implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "id_huesped")
    private String idHuesped;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nombre_huesped")
    private String nombreHuesped;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "huesped")
    private List<HaHuesped> haHuespedList;

    public Huesped() {
    }

    public Huesped(String idHuesped) {
        this.idHuesped = idHuesped;
    }

    public Huesped(String idHuesped, String nombreHuesped) {
        this.idHuesped = idHuesped;
        this.nombreHuesped = nombreHuesped;
    }

    public String getIdHuesped() {
        return idHuesped;
    }

    public void setIdHuesped(String idHuesped) {
        this.idHuesped = idHuesped;
    }

    public String getNombreHuesped() {
        return nombreHuesped;
    }

    public void setNombreHuesped(String nombreHuesped) {
        this.nombreHuesped = nombreHuesped;
    }

    @XmlTransient
    public List<HaHuesped> getHaHuespedList() {
        return haHuespedList;
    }

    public void setHaHuespedList(List<HaHuesped> haHuespedList) {
        this.haHuespedList = haHuespedList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idHuesped != null ? idHuesped.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Huesped)) {
            return false;
        }
        Huesped other = (Huesped) object;
        if ((this.idHuesped == null && other.idHuesped != null) || (this.idHuesped != null && !this.idHuesped.equals(other.idHuesped))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.epn.edu.reservahotel.entidades.Huesped[ idHuesped=" + idHuesped + " ]";
    }
    
}
