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
  
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

import javax.swing.JOptionPane;

@ManagedBean
@ViewScoped
public class AñadePuestoA implements Serializable{

    private static final EntityManagerFactory ent = 
            Persistence.createEntityManagerFactory("MiProyectoPU");

	private MapModel emptyModel;
	private String title;
    private double lat;
    private double lng;
    private int zona ;
    private String descripcion;
    private String alimento;
    private List<String> listaAlimentos = new LinkedList();
    private List<Double> listaLocalizacion = new LinkedList();
    private boolean activadoRegistro = true;
    private boolean activadoRegistro1 = true;

    


    @PostConstruct
    public void init() {
        emptyModel = new DefaultMapModel();
    }

    public boolean getActivadoRegistro(){
        return this.activadoRegistro;
    }

    public void setActivadoRegistro(boolean activadoRegistro){
        this.activadoRegistro=activadoRegistro;
    }

    public String getAlimento(){
        return this.alimento;
    }

    public void setAlimento(String alimento){
        this.alimento=alimento;
    }

    public String getDescripcion(){
        return this.descripcion;
    }

    public void setDescripcion(String descripcion){
        this.descripcion=descripcion;
    }

    public int getZona(){
        return this.zona;
    }

    public void setZona(int zona){
        this.zona=zona;
    }

    public MapModel getEmptyModel() {
        return emptyModel;
    }
      
    public String getTitle() {
        return title;
    }
  
    public void setTitle(String title) {
        this.title = title;
    }
  
    public double getLat() {
        return lat;
    }
  
    public void setLat(double lat) {
        this.lat = lat;
    }
  
    public double getLng() {
        return lng;
    }
  
    public void setLng(double lng) {
        this.lng = lng;
    }

    private boolean verificaElementosVacios(){
        boolean regresa = true;
        
        if(this.zona==0){
            FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR,
            "Agrege la zona diferente de cero", ""));
            regresa=false;
        }
        if(this.descripcion==null || this.descripcion.equals("")){
            FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR,
            "Agrege la descripcion", ""));
            regresa=false;
        }
        return regresa;
    }

    public String registraPuesto(){
        
        LocalizacionJpaController locJpa = new LocalizacionJpaController(ent);
        Localizacion loc = new Localizacion();
        loc.setPunto1(listaLocalizacion.get(0));
        loc.setPunto2(listaLocalizacion.get(1));
        PuestoJpaController puesJpa = new PuestoJpaController(ent);
        Puesto pues = new Puesto();

        if(this.zona == 0 ){
            FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR,
            "Agrege la zona diferente de cero", ""));
            
            return null;
        }


        List<Puesto> listTemp = puesJpa.findPuestoEntities();
        int idPues = 1;

        if(listTemp.size()!=0){
            Puesto p = listTemp.get(listTemp.size()-1);
            idPues = p.getIdPuesto()+1;
        }

        pues.setIdPuesto(idPues);

        pues.setZona(this.zona);
        if(this.title.equals("")){
            title="zona "+String.valueOf(zona);
        }
        pues.setNombre(this.title);
        pues.setDescripcion(this.descripcion);
        
        AlimentosJpaController aliJpa = new AlimentosJpaController(ent);

        try{

            if(verificaElementosVacios()){

                locJpa.create(loc);
                List<Localizacion> l = locJpa.findLocalizacionEntities();
                if(l.size()>0){
                    pues.setIdLocaliza(l.get(l.size()-1));        
                }
                puesJpa.create(pues);
                List<Puesto> p = puesJpa.findPuestoEntities();
                List<Alimentos> temp = aliJpa.findAlimentosEntities();
                int numeroElementos = temp.size();
                Integer idPu = 1;
                if(numeroElementos!=0){
                    Alimentos a = temp.get(numeroElementos-1);
                    idPu = a.getIdAlimentos();
                    idPu++;
                }
                for(int i = 0; i<listaAlimentos.size();i++){
                    
                    Alimentos ali = new Alimentos();
                    ali.setIdAlimentos(idPu);
                    idPu++;
                    ali.setAlimento(listaAlimentos.get(i));
                    ali.setIdPuesto(p.get(p.size()-1));
                    aliJpa.create(ali);
                }
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, 
                        "Puesto agregado", ""));

            }
            

        }catch(Exception e){
            FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR,
            "No se pudo agregar el puesto", ""));
        }
        return "/seguridadAdm/principalA.xhtml";
    }

    public void añadeMarcador() {
        Marker marker = new Marker(new LatLng(lat, lng), title);
        emptyModel.addOverlay(marker);
        listaLocalizacion.add(lat);
        listaLocalizacion.add(lng);
        this.activadoRegistro = false;
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_INFO, 
                "Marker Added", "Lat:" + lat + ", Lng:" + lng));
    }

    public void añadeAlimento(){
        if(alimento!=null){
            listaAlimentos.add(alimento);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, 
                    "Alimento agregado", ""));
            
        }else{
            FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR,
            "Agrege un alimento a la lista por favor", ""));
        }
    }


}