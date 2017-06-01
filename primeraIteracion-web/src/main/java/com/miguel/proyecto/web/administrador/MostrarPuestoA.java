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

import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.primefaces.event.RateEvent;

import javax.servlet.http.HttpServletRequest;

import java.util.Date;

@ManagedBean
@ViewScoped
public class MostrarPuestoA implements Serializable{

	private static final EntityManagerFactory ent = 
            Persistence.createEntityManagerFactory("MiProyectoPU");

    private String idC;
    private MapModel simpleModel;
    private String nombre;
    private String descripcion;
    private int calificacionDada;
    private String usuario;
    private FacesContext faceContext;
    private HttpServletRequest httpServletRequest;
    private FacesMessage message;
    private int calificacionPuesto;
    private List<Alimentos> listaAlimentos;
    private String comentarioEntrada;
    private List<Comentarios> listComent;
    private String alimentoN;

    public String getAlimentoN(){
        return alimentoN;
    }

    public void setAlimentoN(String alimentoN){
        this.alimentoN=alimentoN;
    }




    public void metodoPrincipal(){

    	//parte mapas
    	simpleModel = new DefaultMapModel();
    	double[] coor = cargaCoordenadas();
    	LatLng coordenada = new LatLng(coor[0], coor[1]);
    	simpleModel.addOverlay(new Marker(coordenada, "Puesto"));

    	//dar nombre y descripcion
    	darNombreDescripcion();

    	//dar calificacion al puesto
    	calificacionTotal();

    	//lista alimentos
		darListaAlimentos();

		//comentarios
		cargaComentarios();
	}
    
    public void anadeAlimento(){

        AlimentosJpaController aliJpa = new AlimentosJpaController(ent);
        PuestoJpaController puesJpa = new PuestoJpaController(ent);

        List<Alimentos> listTemp = aliJpa.findAlimentosEntities();
        Integer idAli = 1;
        Puesto pp = puesJpa.findPuesto(Integer.parseInt(idC));

        if(listTemp.size()!=0){
            idAli = listTemp.get(listTemp.size()-1).getIdAlimentos()+1;
        }

        Alimentos aa = new Alimentos();

        aa.setIdAlimentos(idAli);
        aa.setAlimento(alimentoN);
        aa.setIdPuesto(pp);

        try{
            aliJpa.create(aa);
            
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, 
                "", "alimento agregado");

        }catch(Exception e){
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, 
                "", "error");

        }


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

    public String eliminaPuesto(){
        /*
        try{

            eliminaComentarios();
            eliminaAlimentos();
            eliminaCalificacion();

        }catch(Exception e){
            return "principalA";
        }*/

        PuestoJpaController puesJpa = new PuestoJpaController(ent);

        try{
            puesJpa.destroy(Integer.parseInt(idC));
            return "principalA.xhtml";
        }catch(Exception e){
            return "principalA";
        }
        
    }

    private void eliminaComentarios(){

        ComentariosJpaController comenJpa = new ComentariosJpaController(ent);
        List<Comentarios> listaComentarios = comenJpa.findComentariosEntities();

        for(Comentarios c: listaComentarios){

            if(c.getIdPuesto().getIdPuesto()==Integer.parseInt(idC)){

                try{

                    comenJpa.destroy(c.getIdComentarios());
                }catch(Exception e){

                }
                
            }
        }
    }

    private void eliminaAlimentos(){

        AlimentosJpaController aliJpa = new AlimentosJpaController(ent);
        List<Alimentos> listaAlimentos = aliJpa.findAlimentosEntities();

        for(Alimentos a : listaAlimentos){

            if(a.getIdPuesto().getIdPuesto()==Integer.parseInt(idC)){
                try{

                    aliJpa.destroy(a.getIdAlimentos());
                }catch(Exception e){

                }
            }
        }
    }

    private void eliminaCalificacion(){

        CalificacionJpaController caliJpa = new CalificacionJpaController(ent);
        List<Calificacion> listaCalificaciones = caliJpa.findCalificacionEntities();

        for(Calificacion c : listaCalificaciones){

            if(c.getPuesto().getIdPuesto()==Integer.parseInt(idC)){
                int idUsuario = c.getUsuario().getIdUsuario();
                int idPuesto = Integer.parseInt(idC);
                CalificacionPK pk = new CalificacionPK(idUsuario, idPuesto);

                try{
                    caliJpa.destroy(pk);
                }catch(Exception e){

                }
            }
        }
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

	public void agregaComentario(){
    	
    	if(comentarioEntrada!=null){
    		ComentariosJpaController comenJpa = new ComentariosJpaController(ent);
    		List<Comentarios> tt = comenJpa.findComentariosEntities();
    		int ultimo = tt.size();
    		Comentarios tt1;
    		Integer idCo = 1;

    		if(ultimo != 0){
    			tt1 = tt.get(tt.size()-1);
    			idCo = tt1.getIdComentarios()+1;
    		}
    		
            //JOptionPane.showMessageDialog(null, String.valueOf(idCo));

    		Comentarios comen = new Comentarios();
    		comen.setComentario(comentarioEntrada);

    		comen.setIdComentarios(idCo);

			Date fecha = new Date();
			comen.setFecha(fecha);
			UsuarioJpaController usuJpa = new UsuarioJpaController(ent);
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

    public void borrarComentarios(Integer idComen){
		ComentariosJpaController comenJpa = new ComentariosJpaController(ent);

		try{
			comenJpa.destroy(idComen);
			for(int i= 0; i<listComent.size(); i++){
				Comentarios comen = listComent.get(i);
				if(idComen== comen.getIdComentarios()){
					listComent.remove(i);
				}
			}
		}catch(Exception e){

		}

		
	}

	public void borrarAlimento(Integer idAli){
		AlimentosJpaController aliJpa = new AlimentosJpaController(ent);

		try{
			aliJpa.destroy(idAli);
			for(int i= 0; i<listaAlimentos.size(); i++){
				Alimentos ali = listaAlimentos.get(i);
				if(idAli== ali.getIdAlimentos()){
					listaAlimentos.remove(i);
				}
			}
		}catch(Exception e){

		}

		
	}
	


	private void darListaAlimentos(){

    	listaAlimentos = new LinkedList();

    	PuestoJpaController puesJoa = new PuestoJpaController(ent);
    	Puesto pues = puesJoa.findPuesto(Integer.parseInt(idC));

    	AlimentosJpaController aliJpa = new AlimentosJpaController(ent);
    	List<Alimentos> aliTemp = aliJpa.findAlimentosEntities();

    	for(Alimentos a: aliTemp){
            //JOptionPane.showMessageDialog(null, String.valueOf(idC));
    		if(a.getIdPuesto().getIdPuesto()==Integer.parseInt(idC)){
    			listaAlimentos.add(a);
    		}
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

	private void darNombreDescripcion(){
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

    private boolean yaCalifico(Integer puesto){

    	usuario = null;
    	FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale("es-Mx"));
        faceContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        if (httpServletRequest.getSession().getAttribute("sesionAdm") != null) {
            usuario = httpServletRequest.getSession().getAttribute("sesionAdm").toString();
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

    public String getComentarioEntrada(){
    	return comentarioEntrada;
    }

    public void setComentarioEntrada(String comentarioEntrada){
    	this.comentarioEntrada=comentarioEntrada;
    }

    public List<Alimentos> getListaAlimentos(){
    	return listaAlimentos;
    }

    public int getCalificacionDada(){
    	return calificacionDada;
    }

    public void setCalificacionDada(int calificacionDada){
    	this.calificacionDada = calificacionDada;
    }

    public String getDescripcion(){
    	return descripcion;
    }
    
    public String getNombre(){
    	return nombre;
    }

    public MapModel getSimpleModel(){
    	return simpleModel;
    }

    public List getListComent(){
    	return listComent;
    }

    public String getIdC(){
    	return idC;
    }
    
    public void setIdC(String idC){
    	this.idC = idC;
    }

    public int getCalificacionPuesto(){
    	return calificacionPuesto;
    }

    public void setCalificacionPuesto(int calificacionPuesto){
    	this.calificacionPuesto=calificacionPuesto;
    }


}