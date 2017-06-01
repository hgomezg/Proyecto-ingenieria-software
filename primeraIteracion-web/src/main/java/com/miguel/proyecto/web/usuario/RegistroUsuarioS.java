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


import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

@ManagedBean
@ViewScoped
public class RegistroUsuarioS implements Serializable{

	private static final EntityManagerFactory ent = 
            Persistence.createEntityManagerFactory("MiProyectoPU");

    Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@ciencias+(\\.+)unam+(\\.+)mx$");

	private String nombre;
	private String app;
	private String apm;
	private String correo;
	private String contraseña;
	private String confirmacionContraseña;
	private UploadedFile archivo;
	private byte[] ima=null;
 
    

	public void regitroUsuario(){

		if(validaDatos()){

			upload();
			Usuario user = new Usuario();
			user.setNombre(nombre);
			user.setApp(app);
			user.setApm(apm);
			user.setCorreo(correo);
			user.setContrasena(contraseña);
			user.setActivo(false);
			user.setImagen(ima);
			//if(archivo!=null){
			//JOptionPane.showMessageDialog(null, String.valueOf(archivo.getSize()));
			//user.setImagen(archivo.getContents());
			//}
			UsuarioJpaController userJpa = new UsuarioJpaController(ent);

			try{
				CorreoS mail = new CorreoS();
				userJpa.create(user);
				FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, 
				"Felicidades, se registro correctamente", ""));
				mail.registro(correo);
			}catch(Exception e){
				FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Fallo de registro: error a la hora de registrar", "")); 
			}
			

		}
		
	}

	private boolean validaDatos(){

		boolean datos = true;

		if(confirmacionContraseña==null || confirmacionContraseña.equals("")){
			FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR,
            "Agrege la confirmación de la contraseña", ""));
            datos=false;
		}

		if(contraseña==null || contraseña.equals("")){
			FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR,
            "Agrege contraseña", ""));
            datos=false;
		}

		if(correo==null || correo.equals("")){
			FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR,
            "Agrege corro", ""));
            datos=false;
		}

		if(!contraseña.equals(confirmacionContraseña)) {
			FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR,
            "Las contraseñas no son iguales", ""));
            datos=false;
		}

		Matcher mather = pattern.matcher(correo);
		if(mather.find() == false){
			FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR,
            "El correo no es valido", ""));
            datos=false;
		}

		if(apm==null || apm.equals("")){
			FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR,
            "Agrege apellido materno", ""));
            datos=false;
		}

		if(nombre==null || nombre.equals("")){
			FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR,
            "Agrege nombre", ""));
            datos=false;
		}

		if(app==null || app.equals("")){
			FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR,
            "Agrege apellido paterno", ""));
            datos=false;
		}

		UsuarioJpaController userJpa = new UsuarioJpaController(ent);
		List<Usuario> listaUsuarios =  userJpa.findUsuarioEntities();

		for(Usuario u : listaUsuarios){

			if(correo.equals(u.getCorreo())){
				FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR,
            "Este correo ya ha sido registrado", ""));
				datos = false;
			}
		}

		return datos;
	}

	public void upload() {
        if(archivo != null) {
            ima = archivo.getContents();
            
        }else{
        	FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR,
            "No se pudo subir la imagen", ""));
        }
    }



	public String getConfirmacionContraseña(){
		return this.confirmacionContraseña;
	}

	public void setConfirmacionContraseña(String confirmacionContraseña){
		this.confirmacionContraseña=confirmacionContraseña;
	}

	public String getContraseña(){
		return this.contraseña;
	}

	public void setContraseña(String contraseña){
		this.contraseña=contraseña;
	}

	public String getCorreo(){
		return this.correo;
	}

	public void setCorreo(String correo){
		this.correo=correo;
	}

	public String getApm(){
		return this.apm;
	}

	public void setApm(String apm){
		this.apm=apm;
	}

	public String getApp(){
		return this.app;
	}

	public void setApp(String app){
		this.app=app;
	}

	public String getNombre(){
		return this.nombre;
	}

	public void setNombre(String nombre){
		this.nombre=nombre;
	}

	public UploadedFile getArchivo() {
        return this.archivo;
    }
 
    public void setArchivo(UploadedFile archivo) {
        this.archivo = archivo;
    }

}