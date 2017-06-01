package com.miguel.proyecto.db;

import com.miguel.proyecto.db.Administrador;
import com.miguel.proyecto.db.Calificacion;
import com.miguel.proyecto.db.Comentarios;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-06-01T14:06:17")
@StaticMetamodel(Usuario.class)
public class Usuario_ { 

    public static volatile SingularAttribute<Usuario, String> app;
    public static volatile CollectionAttribute<Usuario, Comentarios> comentariosCollection;
    public static volatile CollectionAttribute<Usuario, Administrador> administradorCollection;
    public static volatile SingularAttribute<Usuario, Integer> idUsuario;
    public static volatile SingularAttribute<Usuario, String> correo;
    public static volatile SingularAttribute<Usuario, byte[]> imagen;
    public static volatile SingularAttribute<Usuario, String> contrasena;
    public static volatile SingularAttribute<Usuario, String> nombre;
    public static volatile CollectionAttribute<Usuario, Calificacion> calificacionCollection;
    public static volatile SingularAttribute<Usuario, String> apm;
    public static volatile SingularAttribute<Usuario, Boolean> activo;

}