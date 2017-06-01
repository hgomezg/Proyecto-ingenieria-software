package com.miguel.proyecto.web.administrador;

import com.miguel.proyecto.db.controller.*;
import com.miguel.proyecto.db.controller.exceptions.*;
import com.miguel.proyecto.db.*;

import java.io.Serializable;
import javax.annotation.PostConstruct; 
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.bean.ViewScoped;
import java.util.*;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.JOptionPane;

import javax.servlet.http.HttpServletRequest;

import java.util.Date;

@ManagedBean
@ViewScoped
public class EliminarPuestoA implements Serializable{

    private static final EntityManagerFactory ent = 
            Persistence.createEntityManagerFactory("MiProyectoPU");

    private List<Puesto> listaPuesto;
    private FacesContext faceContext;
    private HttpServletRequest httpServletRequest;
    private FacesMessage message;

    public void cargarLista(){

        PuestoJpaController puestoJpa = new PuestoJpaController(ent);

        listaPuesto = puestoJpa.findPuestoEntities();
    }

    private Integer usuarioInt(){

        String uuu = null;
        FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale("es-Mx"));
        faceContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        if (httpServletRequest.getSession().getAttribute("sesionAdm") != null) {
            uuu = httpServletRequest.getSession().getAttribute("sesionAdm").toString();
        }

        Integer sal = 0;
        if(uuu!=null){
            sal = Integer.parseInt(uuu);
            return sal;
        }else{
            return sal;
        }
    }

    private void eliminarComentarios(int idPuesto){
        
        ComentariosJpaController comJpa = new ComentariosJpaController(ent);
        List<Comentarios> listTemp = comJpa.findComentariosEntities();
        try{

            for(Comentarios c : listTemp){
                if(c.getIdPuesto().getIdPuesto()==idPuesto){
                    comJpa.destroy(c.getIdComentarios());        
                }
            }

            
        }catch(Exception e){

        }
    }

    private void eliminarAlimentos(int idPuesto){

        AlimentosJpaController aliJpa = new AlimentosJpaController(ent);
        List<Alimentos> listTemp = aliJpa.findAlimentosEntities();
        try{

            for(Alimentos a: listTemp){

                if(a.getIdPuesto().getIdPuesto()==idPuesto){
                    aliJpa.destroy(a.getIdAlimentos());        
                }
            }

            
        }catch(Exception e){

        }
    }

    private void eliminarCalificacion(int idPuesto){

        CalificacionPK pk = new CalificacionPK(usuarioInt(), idPuesto);
        CalificacionJpaController calJpa = new CalificacionJpaController(ent);
        List<Calificacion> listTemp = calJpa.findCalificacionEntities();

        try{

            for(Calificacion c : listTemp){

                if(c.getCalificacionPK().getIdUsuario()==idPuesto){
                    calJpa.destroy(c.getCalificacionPK());        
                }
            }

            
        }catch(Exception e){

        }
    }

    public void eliminarPuesto(int idPuesto){

        PuestoJpaController puesJpa =new  PuestoJpaController(ent);
        try{
            eliminarCalificacion(idPuesto);
        }catch(Exception e){}
        try{
            eliminarAlimentos(idPuesto);
        }catch(Exception e){}
        try{
            eliminarComentarios(idPuesto);
        }catch(Exception e){}


        try{
            
            
            
            //JOptionPane.showMessageDialog(null, String.valueOf(idPuesto));
            puesJpa.destroy(idPuesto);
            int i = 0;
            for(Puesto p : listaPuesto){

                if(p.getIdPuesto()==idPuesto){
                    listaPuesto.remove(i);
                }
                i++;
            }

            FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, 
                        "Puesto eliminado", ""));


        }catch(Exception e){

        }

    }


    public List<Puesto> getListaPuesto(){
        return listaPuesto;
    }

    public void setListaPuesto(List<Puesto> listaPuesto){
        this.listaPuesto=listaPuesto;
    }

}