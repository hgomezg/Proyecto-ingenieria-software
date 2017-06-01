package com.miguel.proyecto.db;

import com.miguel.proyecto.db.Puesto;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-06-01T14:06:17")
@StaticMetamodel(Localizacion.class)
public class Localizacion_ { 

    public static volatile SingularAttribute<Localizacion, Integer> idLocaliza;
    public static volatile CollectionAttribute<Localizacion, Puesto> puestoCollection;
    public static volatile SingularAttribute<Localizacion, Double> punto2;
    public static volatile SingularAttribute<Localizacion, Double> punto1;

}