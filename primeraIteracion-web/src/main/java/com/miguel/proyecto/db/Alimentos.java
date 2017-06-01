/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.miguel.proyecto.db;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
//import javax.validation.constraints.NotNull;
//import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author hugo
 */
@Entity
@Table(name = "alimentos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Alimentos.findAll", query = "SELECT a FROM Alimentos a"),
    @NamedQuery(name = "Alimentos.findByIdAlimentos", query = "SELECT a FROM Alimentos a WHERE a.idAlimentos = :idAlimentos"),
    @NamedQuery(name = "Alimentos.findByAlimento", query = "SELECT a FROM Alimentos a WHERE a.alimento = :alimento")})
public class Alimentos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    //@NotNull
    @Column(name = "idAlimentos")
    private Integer idAlimentos;
    //@Size(max = 35)
    @Column(name = "alimento")
    private String alimento;
    @JoinColumn(name = "idPuesto", referencedColumnName = "idPuesto")
    @ManyToOne
    private Puesto idPuesto;

    public Alimentos() {
    }

    public Alimentos(Integer idAlimentos) {
        this.idAlimentos = idAlimentos;
    }

    public Integer getIdAlimentos() {
        return idAlimentos;
    }

    public void setIdAlimentos(Integer idAlimentos) {
        this.idAlimentos = idAlimentos;
    }

    public String getAlimento() {
        return alimento;
    }

    public void setAlimento(String alimento) {
        this.alimento = alimento;
    }

    public Puesto getIdPuesto() {
        return idPuesto;
    }

    public void setIdPuesto(Puesto idPuesto) {
        this.idPuesto = idPuesto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAlimentos != null ? idAlimentos.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Alimentos)) {
            return false;
        }
        Alimentos other = (Alimentos) object;
        if ((this.idAlimentos == null && other.idAlimentos != null) || (this.idAlimentos != null && !this.idAlimentos.equals(other.idAlimentos))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.primeraIteracion.db.Alimentos[ idAlimentos=" + idAlimentos + " ]";
    }
    
}
