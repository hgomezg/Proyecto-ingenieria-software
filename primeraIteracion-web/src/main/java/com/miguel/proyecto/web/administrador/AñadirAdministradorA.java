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
public class AñadirAdministradorA implements Serializable{

	private static final EntityManagerFactory ent = 
            Persistence.createEntityManagerFactory("MiProyectoPU");
    private FacesContext faceContext;
    private HttpServletRequest httpServletRequest;
    private FacesMessage message;
    
    private List<Usuario> listaUsuarios;

    public void principal(){
        llenaListaUsuarios();
    }

    public void agregaAdministradores(Integer idUsuario){

        AdministradorJpaController adminJpa = new AdministradorJpaController(ent);
        UsuarioJpaController userJpa = new UsuarioJpaController(ent);

        Usuario use = userJpa.findUsuario(idUsuario);

        List<Administrador> listaAdminTemp = adminJpa.findAdministradorEntities();
        Administrador admin = new Administrador();

        Integer idAdmin = listaAdminTemp.get(listaAdminTemp.size()-1).getIdAdministrador();
        admin.setIdAdministrador(idAdmin+1);
        admin.setIdUsuario(use);

        try{
            adminJpa.create(admin);
        }catch(Exception e){

        }

    }

    public boolean esAdmin(Integer idUsuario){

        AdministradorJpaController adminJpa = new AdministradorJpaController(ent);

        List<Administrador> listaTemp = adminJpa.findAdministradorEntities();

        for(Administrador a : listaTemp){
            if(a.getIdUsuario().getIdUsuario()==idUsuario){
                return true;
            }
        }

        return false;
    }

    private void llenaListaUsuarios(){

       UsuarioJpaController useJpa = new UsuarioJpaController(ent);

       listaUsuarios = useJpa.findUsuarioEntities();
   }

    public List<Usuario> getListaUsuarios(){
        return listaUsuarios;
    }
}