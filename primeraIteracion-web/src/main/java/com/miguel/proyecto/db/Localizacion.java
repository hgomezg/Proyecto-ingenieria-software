/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.miguel.proyecto.db;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
//import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author hugo
 */
@Entity
@Table(name = "localizacion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Localizacion.findAll", query = "SELECT l FROM Localizacion l"),
    @NamedQuery(name = "Localizacion.findByIdLocaliza", query = "SELECT l FROM Localizacion l WHERE l.idLocaliza = :idLocaliza"),
    @NamedQuery(name = "Localizacion.findByPunto1", query = "SELECT l FROM Localizacion l WHERE l.punto1 = :punto1"),
    @NamedQuery(name = "Localizacion.findByPunto2", query = "SELECT l FROM Localizacion l WHERE l.punto2 = :punto2")})
public class Localizacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    //@NotNull
    @Column(name = "idLocaliza")
    private Integer idLocaliza;
    @Basic(optional = false)
    //@NotNull
    @Column(name = "punto1")
    private double punto1;
    @Basic(optional = false)
    //@NotNull
    @Column(name = "punto2")
    private double punto2;
    @OneToMany(mappedBy = "idLocaliza")
    private Collection<Puesto> puestoCollection;

    public Localizacion() {
    }

    public Localizacion(Integer idLocaliza) {
        this.idLocaliza = idLocaliza;
    }

    public Localizacion(Integer idLocaliza, double punto1, double punto2) {
        this.idLocaliza = idLocaliza;
        this.punto1 = punto1;
        this.punto2 = punto2;
    }

    public Integer getIdLocaliza() {
        return idLocaliza;
    }

    public void setIdLocaliza(Integer idLocaliza) {
        this.idLocaliza = idLocaliza;
    }

    public double getPunto1() {
        return punto1;
    }

    public void setPunto1(double punto1) {
        this.punto1 = punto1;
    }

    public double getPunto2() {
        return punto2;
    }

    public void setPunto2(double punto2) {
        this.punto2 = punto2;
    }

    @XmlTransient
    public Collection<Puesto> getPuestoCollection() {
        return puestoCollection;
    }

    public void setPuestoCollection(Collection<Puesto> puestoCollection) {
        this.puestoCollection = puestoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idLocaliza != null ? idLocaliza.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Localizacion)) {
            return false;
        }
        Localizacion other = (Localizacion) object;
        if ((this.idLocaliza == null && other.idLocaliza != null) || (this.idLocaliza != null && !this.idLocaliza.equals(other.idLocaliza))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.primeraIteracion.db.Localizacion[ idLocaliza=" + idLocaliza + " ]";
    }
    
}
