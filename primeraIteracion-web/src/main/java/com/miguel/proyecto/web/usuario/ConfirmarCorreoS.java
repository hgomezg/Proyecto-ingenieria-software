package com.miguel.proyecto.web.usuario;

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
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import javax.swing.JOptionPane;

@ManagedBean
@ViewScoped
public class ConfirmarCorreoS{

	private static final EntityManagerFactory ent = 
            Persistence.createEntityManagerFactory("MiProyectoPU");

	private String mail;


	public void confirmaCorreo(){
		UsuarioJpaController useJpa = new UsuarioJpaController(ent);

		List<Usuario> lista = useJpa.findUsuarioEntities();

		for(Usuario u : lista){
			
			if(mail.equals(u.getCorreo())){
				u.setActivo(true);
				try{
					useJpa.edit(u);
				}catch(Exception e){
					
				}
				
			}
		}
	}

	public String getMail(){
		return mail;
	}

	public void setMail(String mail){
		this.mail=mail;
	}
}