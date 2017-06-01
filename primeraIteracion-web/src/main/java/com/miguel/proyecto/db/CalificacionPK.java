/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.miguel.proyecto.db;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
//import javax.validation.constraints.NotNull;

/**
 *
 * @author hugo
 */
@Embeddable
public class CalificacionPK implements Serializable {

    @Basic(optional = false)
    //@NotNull
    @Column(name = "idUsuario")
    private int idUsuario;
    @Basic(optional = false)
    //@NotNull
    @Column(name = "idPuesto")
    private int idPuesto;

    public CalificacionPK() {
    }

    public CalificacionPK(int idUsuario, int idPuesto) {
        this.idUsuario = idUsuario;
        this.idPuesto = idPuesto;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdPuesto() {
        return idPuesto;
    }

    public void setIdPuesto(int idPuesto) {
        this.idPuesto = idPuesto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idUsuario;
        hash += (int) idPuesto;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CalificacionPK)) {
            return false;
        }
        CalificacionPK other = (CalificacionPK) object;
        if (this.idUsuario != other.idUsuario) {
            return false;
        }
        if (this.idPuesto != other.idPuesto) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.primeraIteracion.db.CalificacionPK[ idUsuario=" + idUsuario + ", idPuesto=" + idPuesto + " ]";
    }
    
}
