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
import javax.swing.JOptionPane;

import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.primefaces.event.RateEvent;
import javax.servlet.http.HttpServletRequest;

import java.util.Date;


@ManagedBean
@ViewScoped
public class MostrarPuestoS implements Serializable{

	private static final EntityManagerFactory ent = 
            Persistence.createEntityManagerFactory("MiProyectoPU");

    private MapModel simpleModel;

    private String idC;

    private String nombre;
    private String descripcion;
    private Integer calificacionPuesto;
    private List<Alimentos> listaAlimentos;
    private List<Comentarios> listaComentarios;




    public void metodoPrincipal(){
    	
    	//mapas
    	simpleModel = new DefaultMapModel();
    	double[] coor = cargaCoordenadas();
    	LatLng coordenada = new LatLng(coor[0], coor[1]);
    	simpleModel.addOverlay(new Marker(coordenada, "Puesto"));

    	//darNombre del puesto y descripcion
    	darNombreYDescripcion();

    	//dar Calificacion del puesto
    	darCalificacionPuesto();

    	//dar lista de alimentos
    	darListaAlimentos();

    	//dar lista de comentarios
    	darListaComentarios();

    }


    private void darListaComentarios(){
    	ComentariosJpaController comenJpa = new ComentariosJpaController(ent);
    	listaComentarios = new LinkedList();

    	List<Comentarios> temp = comenJpa.findComentariosEntities();

    	for(int i = temp.size()-1; i>=0; i--){
    		
    		Comentarios c = temp.get(i);

    		if(c.getIdPuesto().getIdPuesto()==Integer.parseInt(idC)){
    			listaComentarios.add(c);
    		}
    	}
    }

    private void darListaAlimentos(){
    	listaAlimentos = new LinkedList();

    	AlimentosJpaController aliJpa = new AlimentosJpaController(ent);
    	List<Alimentos> aliTemp = aliJpa.findAlimentosEntities();

    	for(Alimentos a: aliTemp){

    		if(a.getIdPuesto().getIdPuesto()==Integer.parseInt(idC)){
    			listaAlimentos.add(a);
    		}
    	}
    }

    private void darCalificacionPuesto(){
    	CalificacionJpaController caliJpa = new CalificacionJpaController(ent);
    	List<Calificacion> listTemp= caliJpa.findCalificacionEntities();
    	int salida = 0;
    	int contador = 0;
    	for(Calificacion c : listTemp){

    		if(c.getPuesto().getIdPuesto()==Integer.parseInt(idC)){
    			salida = salida+c.getCalifica();
    			contador++;
    		}
    	}

    	if(contador==0){
    		calificacionPuesto=salida;
    	}else{
    		calificacionPuesto = salida/contador;
    	}
    }

    private void darNombreYDescripcion(){
    	PuestoJpaController puesJoa = new PuestoJpaController(ent);
    	Puesto pues = puesJoa.findPuesto(Integer.parseInt(idC));
    	this.nombre=pues.getNombre();
    	this.descripcion=pues.getDescripcion();
    }



    private double[] cargaCoordenadas(){

    	PuestoJpaController puesJoa = new PuestoJpaController(ent);
    	Puesto pues = puesJoa.findPuesto(Integer.parseInt(idC));
    	Localizacion loca = pues.getIdLocaliza();

    	double[] sal = new double[2];

    	sal[0]=loca.getPunto1();
    	sal[1]=loca.getPunto2();
    	return sal;
    }

    public String getIdC(){
    	return idC;
    }

    public void setIdC(String idC){
    	this.idC=idC;
    }

    public String getNombre(){
    	return nombre;
    }

    public String getDescripcion(){
    	return descripcion;
    }

    public Integer getCalificacionPuesto(){
    	return calificacionPuesto;
    }

    public List<Alimentos> getListaAlimentos(){
    	return listaAlimentos;
    }

    public MapModel getSimpleModel() {
        return simpleModel;
    }

    public List<Comentarios> getListaComentarios(){
    	return listaComentarios;
    }
}