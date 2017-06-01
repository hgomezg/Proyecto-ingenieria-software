
use  comidasCiencias;

CREATE TABLE usuario(idUsuario INTEGER PRIMARY KEY AUTO_INCREMENT ,  nombre VARCHAR(20) NOT NULL, 
	app VARCHAR(20) NOT NULL, apm VARCHAR(20) NOT NULL, correo VARCHAR(50) NOT NULL, contrasena VARCHAR(25) NOT NULL,
	activo BIT, imagen MEDIUMBLOB);


CREATE TABLE localizacion(idLocaliza INTEGER PRIMARY KEY  AUTO_INCREMENT, punto1 DOUBLE NOT NULL, 
	punto2 DOUBLE NOT NULL);


CREATE TABLE administrador (idAdministrador INTEGER PRIMARY KEY  AUTO_INCREMENT, idUsuario INT,
	FOREIGN KEY(idUsuario) REFERENCES usuario(idUsuario));


CREATE TABLE puesto(idPuesto INTEGER PRIMARY KEY  AUTO_INCREMENT, zona INT NOT NULL, nombre VARCHAR(20), descripcion VARCHAR(365),
	idLocaliza INT, 
	FOREIGN KEY(idLocaliza) REFERENCES localizacion(idLocaliza));


CREATE TABLE alimentos(idAlimentos INTEGER PRIMARY KEY  AUTO_INCREMENT, idPuesto INT, alimento VARCHAR(35),
	FOREIGN KEY(idPuesto) REFERENCES puesto(idPuesto));


CREATE TABLE comentarios(idComentarios INTEGER PRIMARY KEY  AUTO_INCREMENT,  idUsuario INT NOT NULL, 
	idPuesto INT NOT NULL, comentario VARCHAR(250) NOT NULL, fecha DATE NOT NULL,
	FOREIGN KEY(idUsuario) REFERENCES usuario(idUsuario),
	FOREIGN KEY(idPuesto) REFERENCES puesto(idPuesto));


CREATE TABLE calificacion (idUsuario INT NOT NULL,  idPuesto INT NOT NULL,
 	califica INT NOT NULL, 
 	PRIMARY KEY(idUsuario, idPuesto), 
 	FOREIGN KEY(idUsuario) REFERENCES usuario(idUsuario),
 	FOREIGN KEY(idPuesto) REFERENCES puesto(idPuesto));


alter table calificacion
 add constraint CK_calificacion
 check (califica>=0 and califica<=5);


alter table puesto
 add constraint CK_zona
 check (zona>=1 and zona<=3);
