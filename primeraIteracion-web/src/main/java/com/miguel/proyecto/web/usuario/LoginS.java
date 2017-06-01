package com.miguel.proyecto.web.usuario;

import com.miguel.proyecto.db.controller.*;
import com.miguel.proyecto.db.controller.exceptions.*;
import com.miguel.proyecto.db.*;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import javax.annotation.PostConstruct; 
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.bean.RequestScoped;
import java.util.*;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.JOptionPane;

@ManagedBean
@RequestScoped

public class LoginS implements Serializable{

	private String correo;
	private String contraseña;

	private final HttpServletRequest httpServletRequest; // Obtiene información de todas las peticiones de usuario.
    private final FacesContext faceContext; // Obtiene información de la aplicación
    private FacesMessage message;
    private static final EntityManagerFactory ent = 
            Persistence.createEntityManagerFactory("MiProyectoPU");
    private boolean esAdministrador = false;
    private Usuario usuaGuar = null;

    public LoginS() {
        faceContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
    }

    public boolean valida(String correo, String password){
        
        UsuarioJpaController useJpa = new UsuarioJpaController(ent);
        List<Usuario> listaU = useJpa.findUsuarioEntities();
        boolean esUsuario = false;
        int idU = -1;

        for(int i = 0; i<listaU.size();i++){

        	String co = listaU.get(i).getCorreo();
        	String con = listaU.get(i).getContrasena();
        	Boolean activ = listaU.get(i).getActivo();

        	if(co.equals(correo) && con.equals(password) && 
        		activ){
        		idU = listaU.get(i).getIdUsuario();
        		usuaGuar = listaU.get(i);
        		esUsuario=true;
        	}
        }

        AdministradorJpaController admJpa = new AdministradorJpaController(ent);
        List<Administrador> listaA = admJpa.findAdministradorEntities();

        for(int i= 0; i<listaA.size();i++){
        	Integer te = listaA.get(i).getIdUsuario().getIdUsuario();
        	if(te==idU){
        		esAdministrador = true;
        	}
        }

        return esUsuario;
    }

     public String login() throws Exception {
        boolean valid = valida(correo, contraseña);
        if(valid && esAdministrador){
            httpServletRequest.getSession().setAttribute("sesionAdm", String.valueOf(usuaGuar.getIdUsuario()));

            FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, 
				"Acceso correcto como administrador", ""));
            
            return "seguridadAdm/principalA.xhtml?faces-redirect=true";
        }
        if (valid){
            httpServletRequest.getSession().setAttribute("sesion", String.valueOf(usuaGuar.getIdUsuario()));
            
            FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, 
				"Acceso correcto", ""));

            return "seguridad/principalC.xhtml?faces-redirect=true";
        }
        
        FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Acceso denegado", "")); 

        return "registroUsuarioS?faces-redirect=true";
        
    }

	public String getContraseña(){
		return contraseña;
	}

	public void setContraseña(String contraseña){
		this.contraseña=contraseña;
	}

	public String getCorreo(){
		return correo;
	}

	public void setCorreo(String correo){
		this.correo=correo;
	}

}