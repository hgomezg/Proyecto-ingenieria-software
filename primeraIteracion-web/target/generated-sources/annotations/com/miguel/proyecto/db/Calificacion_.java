package com.miguel.proyecto.db;

import com.miguel.proyecto.db.CalificacionPK;
import com.miguel.proyecto.db.Puesto;
import com.miguel.proyecto.db.Usuario;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-06-01T14:06:17")
@StaticMetamodel(Calificacion.class)
public class Calificacion_ { 

    public static volatile SingularAttribute<Calificacion, Puesto> puesto;
    public static volatile SingularAttribute<Calificacion, CalificacionPK> calificacionPK;
    public static volatile SingularAttribute<Calificacion, Usuario> usuario;
    public static volatile SingularAttribute<Calificacion, Integer> califica;

}