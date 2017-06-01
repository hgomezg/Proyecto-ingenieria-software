package com.miguel.proyecto.db;

import com.miguel.proyecto.db.Puesto;
import com.miguel.proyecto.db.Usuario;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-06-01T14:06:17")
@StaticMetamodel(Comentarios.class)
public class Comentarios_ { 

    public static volatile SingularAttribute<Comentarios, Date> fecha;
    public static volatile SingularAttribute<Comentarios, Usuario> idUsuario;
    public static volatile SingularAttribute<Comentarios, Puesto> idPuesto;
    public static volatile SingularAttribute<Comentarios, Integer> idComentarios;
    public static volatile SingularAttribute<Comentarios, String> comentario;

}