package com.miguel.proyecto.db;

import com.miguel.proyecto.db.Alimentos;
import com.miguel.proyecto.db.Calificacion;
import com.miguel.proyecto.db.Comentarios;
import com.miguel.proyecto.db.Localizacion;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-06-01T14:06:17")
@StaticMetamodel(Puesto.class)
public class Puesto_ { 

    public static volatile SingularAttribute<Puesto, String> descripcion;
    public static volatile SingularAttribute<Puesto, Integer> zona;
    public static volatile SingularAttribute<Puesto, Localizacion> idLocaliza;
    public static volatile CollectionAttribute<Puesto, Comentarios> comentariosCollection;
    public static volatile SingularAttribute<Puesto, Integer> idPuesto;
    public static volatile CollectionAttribute<Puesto, Alimentos> alimentosCollection;
    public static volatile SingularAttribute<Puesto, String> nombre;
    public static volatile CollectionAttribute<Puesto, Calificacion> calificacionCollection;

}