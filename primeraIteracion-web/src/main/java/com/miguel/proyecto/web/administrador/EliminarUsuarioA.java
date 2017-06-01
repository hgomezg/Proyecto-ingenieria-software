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
public class EliminarUsuarioA implements Serializable{

	private static final EntityManagerFactory ent = 
            Persistence.createEntityManagerFactory("MiProyectoPU");

    private FacesContext faceContext;
    private HttpServletRequest httpServletRequest;
    private FacesMessage message;
    private List<Usuario> listaUsuarios;

    private boolean esAdmin(Integer idUsuario){

        AdministradorJpaController admJpa = new AdministradorJpaController(ent);

        List<Administrador> listaAdm = admJpa.findAdministradorEntities();

        for(Administrador a : listaAdm){
            if(a.getIdUsuario().getIdUsuario() == idUsuario){
                return true;
            }
        }
        return false;
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


    public void darUsuariosLista(){

        listaUsuarios = new LinkedList();
        UsuarioJpaController userJpa = new UsuarioJpaController(ent);
        List<Usuario> listaTemp = userJpa.findUsuarioEntities();
        int temp = usuarioInt();
        for(Usuario u : listaTemp){

            if(u.getIdUsuario() != temp){
                listaUsuarios.add(u);
            }
        }
    }


    public String eliminarUser(Integer idUsuario){

        try{
            eliminarCalificacion(idUsuario);
            eliminarComentarios(idUsuario);
            eliminarAdmin(idUsuario);
        }catch(Exception e){

        }

        UsuarioJpaController userJpa = new UsuarioJpaController(ent);
        
        try{
            userJpa.destroy(idUsuario);
            FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, 
                        "Usuario eliminado", ""));

            for(int i = 0; i<listaUsuarios.size();i++){
                Usuario u = listaUsuarios.get(i);
                if(u.getIdUsuario()==idUsuario){
                    listaUsuarios.remove(i);
                }
                
            }
            return "";
        }catch(Exception e){

        }
        return "";
    }

    private void eliminarAdmin(Integer idUsuario){

        if(esAdmin(idUsuario)){
            AdministradorJpaController admJpa = new AdministradorJpaController(ent);
            List<Administrador> listaAdmin = admJpa.findAdministradorEntities();
            for(Administrador a : listaAdmin){
                if(a.getIdUsuario().getIdUsuario()==idUsuario){
                    try{
                        admJpa.destroy(a.getIdAdministrador());
                    }catch(Exception e){

                    }
                }
            }
        }
    }

    private void eliminarCalificacion(Integer idUsuario){

        CalificacionJpaController caliJpa = new CalificacionJpaController(ent);
        List<Calificacion> listaCalificacion = caliJpa.findCalificacionEntities();

        for(Calificacion c : listaCalificacion){

            if(c.getCalificacionPK().getIdUsuario()==idUsuario){
                try{
                    caliJpa.destroy(c.getCalificacionPK());
                }catch(Exception e){

                }
            }
        }
    }

    private void eliminarComentarios(Integer idUsuario){

        ComentariosJpaController comenJpa = new ComentariosJpaController(ent);
        List<Comentarios> listaComentarios = comenJpa.findComentariosEntities();

        for(Comentarios c : listaComentarios){

            if(c.getIdUsuario().getIdUsuario()==idUsuario){

                try{
                    comenJpa.destroy(c.getIdComentarios());
                }catch(Exception e){
                }
            }
        }
    }



    public List<Usuario> getListaUsuarios(){
        return listaUsuarios;
    }


}