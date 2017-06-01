package com.miguel.proyecto.web.usuarioRegistrado;

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
public class MostrarPuestoC implements Serializable{

	private static final EntityManagerFactory ent = 
            Persistence.createEntityManagerFactory("MiProyectoPU");

    private MapModel simpleModel;

    private String idC;

    private String nombrePuesto;

    private String descripcion;

    private List<Alimentos> listaAlimentos;

    private String usuario;
    private FacesContext faceContext;
    private HttpServletRequest httpServletRequest;
    private FacesMessage message;
    private int calPues;
    private int calificacionPuesto;

    private String comentarioEnt;

    private List<Comentarios> listComent;




    public void metodoPrincipal(){

    	//parte mapas
    	simpleModel = new DefaultMapModel();
    	double[] coor = cargaCoordenadas(Integer.parseInt(idC));
    	LatLng coordenada = new LatLng(coor[0], coor[1]);
    	simpleModel.addOverlay(new Marker(coordenada, "Puesto"));

    	darNomDesc(Integer.parseInt(idC));

    	darListaAlimentos(Integer.parseInt(idC));

    	calificacionTotal();

    	cargaComentarios();
    }

    private void cargaComentarios(){

    	ComentariosJpaController comenJpa = new ComentariosJpaController(ent);
    	listComent = new LinkedList();

    	List<Comentarios> temp = comenJpa.findComentariosEntities();

    	for(int i = temp.size()-1; i>=0; i--){
    		
    		Comentarios c = temp.get(i);

    		if(c.getIdPuesto().getIdPuesto()==Integer.parseInt(idC)){
    			listComent.add(c);
    		}
    	}
    }

    private double[] cargaCoordenadas(Integer puesto){

    	PuestoJpaController puesJoa = new PuestoJpaController(ent);
    	Puesto pues = puesJoa.findPuesto(puesto);
    	Localizacion loca = pues.getIdLocaliza();

    	double[] sal = new double[2];

    	sal[0]=loca.getPunto1();
    	sal[1]=loca.getPunto2();
    	return sal;

    }

    private void darNomDesc(Integer puesto){
    	PuestoJpaController puesJoa = new PuestoJpaController(ent);
    	Puesto pues = puesJoa.findPuesto(puesto);
    	this.nombrePuesto=pues.getNombre();
    	this.descripcion=pues.getDescripcion();
    }

    private void darListaAlimentos(Integer puesto){

    	listaAlimentos = new LinkedList();

    	PuestoJpaController puesJoa = new PuestoJpaController(ent);
    	Puesto pues = puesJoa.findPuesto(puesto);

    	AlimentosJpaController aliJpa = new AlimentosJpaController(ent);
    	List<Alimentos> aliTemp = aliJpa.findAlimentosEntities();

    	for(Alimentos a: aliTemp){

    		if(a.getIdPuesto().getIdPuesto()==puesto){
    			listaAlimentos.add(a);
    		}
    	}
    }

    private boolean yaCalifico(Integer puesto){

    	usuario = null;
    	FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale("es-Mx"));
        faceContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        if (httpServletRequest.getSession().getAttribute("sesion") != null) {
            usuario = httpServletRequest.getSession().getAttribute("sesion").toString();
        }

        CalificacionJpaController calJpa = new CalificacionJpaController(ent);
        CalificacionPK caliPk = new CalificacionPK();
        caliPk.setIdUsuario(Integer.parseInt(usuario));
        caliPk.setIdPuesto(Integer.parseInt(idC));
        Calificacion cal = calJpa.findCalificacion(caliPk);

        if(cal!=null){
        	return true;
        	
        }else{
        	return false;
        }
    }

    public void onrate(RateEvent rateEvent) {
        //FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Rate Event", "You rated:" + ((Integer) rateEvent.getRating()).intValue());
        //FacesContext.getCurrentInstance().addMessage(null, message);

    	//JOptionPane.showMessageDialog(null, idC);
    	if(!yaCalifico(Integer.parseInt(idC))){

    		CalificacionPK pk = new CalificacionPK();
    		pk.setIdUsuario(Integer.parseInt(usuario));
    		pk.setIdPuesto(Integer.parseInt(idC));

    		Calificacion cal = new Calificacion();
    		cal.setCalificacionPK(pk);
    		cal.setCalifica(((Integer) rateEvent.getRating()).intValue());
    		UsuarioJpaController usuJpa = new UsuarioJpaController(ent);
    		Usuario usu = usuJpa.findUsuario(Integer.parseInt(usuario));
    		cal.setUsuario(usu);
    		PuestoJpaController puesJpa = new PuestoJpaController(ent);
    		Puesto pues = puesJpa.findPuesto(Integer.parseInt(idC));
    		cal.setPuesto(pues);

    		try{
    			CalificacionJpaController calJpa = new CalificacionJpaController(ent);
    			calJpa.create(cal);
    			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, 
    				"", "Tu Calificación:" + ((Integer) rateEvent.getRating()).intValue());
        		FacesContext.getCurrentInstance().addMessage(null, message);
    		}catch(Exception e){

    		}
    		
    	}else{
    		CalificacionPK pkE = new CalificacionPK();
    		pkE.setIdUsuario(Integer.parseInt(usuario));
    		pkE.setIdPuesto(Integer.parseInt(idC));

    		Calificacion calE = new Calificacion();
    		calE.setCalificacionPK(pkE);
    		calE.setCalifica(((Integer) rateEvent.getRating()).intValue());
    		UsuarioJpaController usuJpaE = new UsuarioJpaController(ent);
    		Usuario usuE = usuJpaE.findUsuario(Integer.parseInt(usuario));
    		calE.setUsuario(usuE);
    		PuestoJpaController puesJpaE = new PuestoJpaController(ent);
    		Puesto puesE = puesJpaE.findPuesto(Integer.parseInt(idC));
    		calE.setPuesto(puesE);

    		try{
    			CalificacionJpaController calJpaE = new CalificacionJpaController(ent);
    			calJpaE.edit(calE);
    			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, 
    				"", "Cambiaste tu calificación:" + ((Integer) rateEvent.getRating()).intValue());
        		FacesContext.getCurrentInstance().addMessage(null, message);
    		}catch(Exception e){

    		}
    	}

    }
     
    public void oncancel() {
        //FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cancel Event", "Rate Reset");
        //FacesContext.getCurrentInstance().addMessage(null, message);

    	CalificacionPK pk = new CalificacionPK();
    	pk.setIdUsuario(Integer.parseInt(usuario));
    	pk.setIdPuesto(Integer.parseInt(idC));

    	try{
    		CalificacionJpaController calJpa = new CalificacionJpaController(ent);
    		calJpa.destroy(pk);
    		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, 
    			"", "Sin calificación");
        	FacesContext.getCurrentInstance().addMessage(null, message);
    	}catch(Exception e){

    	}

    }
 
    private void calificacionTotal(){

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
 

 	private Integer usuarioInt(){

    	String uuu = null;
    	FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale("es-Mx"));
        faceContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        if (httpServletRequest.getSession().getAttribute("sesion") != null) {
            uuu = httpServletRequest.getSession().getAttribute("sesion").toString();
        }

        Integer sal = 0;
        if(uuu!=null){
        	sal = Integer.parseInt(uuu);
        	return sal;
        }else{
        	return sal;
        }
        
        
    }

    public void agregaComentario(){

    	//JOptionPane.showMessageDialog(null, comentarioEnt);
    	
    	if(comentarioEnt!=null){
    		ComentariosJpaController comenJpa = new ComentariosJpaController(ent);
    		List<Comentarios> tt = comenJpa.findComentariosEntities();
    		int ultimo = tt.size();
    		Comentarios tt1 ;
    		Integer idCo = 1;

    		if(ultimo != 0){
    			tt1 = tt.get(tt.size()-1);
    			idCo = tt1.getIdComentarios()+1;
    		}
    		
    		//JOptionPane.showMessageDialog(null, String.valueOf(idCo));
    		Comentarios comen = new Comentarios();
    		comen.setComentario(comentarioEnt);

    		comen.setIdComentarios(idCo);

			Date fecha = new Date();
			comen.setFecha(fecha);
			UsuarioJpaController usuJpa = new UsuarioJpaController(ent);

			//JOptionPane.showMessageDialog(null, String.valueOf(usuarioInt()));
    		Usuario usu = usuJpa.findUsuario(usuarioInt());
			comen.setIdUsuario(usu);
			PuestoJpaController puesJpa = new PuestoJpaController(ent);
    		Puesto pues = puesJpa.findPuesto(Integer.parseInt(idC));
			comen.setIdPuesto(pues);

			try{

				
				comenJpa.create(comen);
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, 
    			"", "Comentario agregado");
        		FacesContext.getCurrentInstance().addMessage(null, message);
        		listComent.add(0, comen);

			}catch(Exception e){
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, 
    			"", "No se pudo agregar el comentario");
        		FacesContext.getCurrentInstance().addMessage(null, message);

			}

			
    	}

    	
    }


   

    public MapModel getSimpleModel() {
        return simpleModel;
    }

    public String getIdC(){
    	return this.idC;
    }

    public void setIdC(String idC){
    	this.idC=idC;
    }
		
    public String getNombrePuesto(){
    	return nombrePuesto;
    }

    public void setNombrePuesto(String nombrePuesto){
    	this.nombrePuesto=nombrePuesto;
    }

    public String getDescripcion(){
    	return descripcion;
    }

    public void setDescripcion(String descripcion){
    	this.descripcion=descripcion;
    }

    public List<Alimentos> getListaAlimentos(){
    	return listaAlimentos;
    }

    public void setListaAlimentos(List<Alimentos> listaAlimentos){
    	this.listaAlimentos=listaAlimentos;
    }

    public int getCalPues(){
    	return calPues;
    }

    public void setCalPues(int calPues){
    	this.calPues=calPues;
    }

    public int getCalificacionPuesto(){
    	return calificacionPuesto;
    }

    public void setCalificacionPuesto(int calificacionPuesto){
    	this.calificacionPuesto=calificacionPuesto;
    }

    public String getComentarioEnt(){
    	return comentarioEnt;
    }

    public void setComentarioEnt(String comentarioEnt){
    	this.comentarioEnt=comentarioEnt;
    }

    public List getListComent(){
    	return listComent;
    }

}