package com.miguel.proyecto.web.usuario;

import com.miguel.proyecto.db.controller.*;
import com.miguel.proyecto.db.controller.exceptions.*;
import com.miguel.proyecto.db.*;

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
public class ListarPuestoS implements Serializable{

	private static final EntityManagerFactory ent = 
            Persistence.createEntityManagerFactory("MiProyectoPU");

	private String zona;
	private List<Puesto> listaPuestos;

	public void puestos(){
		
		listaPuestos = new LinkedList();

		PuestoJpaController puesJpa = new PuestoJpaController(ent);

		List<Puesto> listaTemporal = puesJpa.findPuestoEntities();

		for(Puesto p : listaTemporal){

			if(p.getZona()==Integer.parseInt(zona)){

				listaPuestos.add(p);
			}

		}		

	}

	public List<Puesto> getListaPuestos(){
		return listaPuestos;
	}

	public void setListaPuestos(List<Puesto> listaPuestos){
		this.listaPuestos=listaPuestos;
	}

	public String getZona(){
		return zona;
	}

	public void setZona(String zona){
		this.zona=zona;
	}


}