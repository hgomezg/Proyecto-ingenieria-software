package com.miguel.proyecto.web.usuario;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class CorreoS {


	public void registro(String correo){

        try{

            Properties props = new Properties();
            props.setProperty("mail.smtp.host", "smtp.gmail.com");
            props.setProperty("mail.smtp.starttls.enable", "true");
            props.setProperty("mail.smtp.port", "587");
            props.setProperty("mail.smtp.user", "comiditasCiencias@gmail.com");
            props.setProperty("mail.smtp.auth", "true");
            Session session = Session.getDefaultInstance(props);
            
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(correo));
            message.addRecipient(
                    Message.RecipientType.TO,
                    new InternetAddress(correo));
            message.setSubject("[Comiditas Ciencias]Confirmación de cuenta");
            //message.setText("Tu contraseña es:\n "+ contra +"\n Atentamente Soporte de Préstamo de Libros");
            message.setText("Te damos la bienvinida a Comiditas Ciencias. Para que el registro finalice por"+
                " favor ingresa a la siguiente liga:\n"+"http://localhost:8080/mi-primer-aplicacion-web"+
                "/confirmarCorreoS.xhtml?mail="+correo+"\nSi usted no realizo el registro"+
                "ignore este mensaje.");
            // Lo enviamos.
            Transport t = session.getTransport("smtp");
            //soporte.prestamo.libros.riesgo@gmail.com
            //riesgo12345
            t.connect("comiditasCiencias@gmail.com", "ricarica");
            t.sendMessage(message, message.getAllRecipients());

            // Cierre.
            t.close();
        }catch(Exception e){

        }

    }



}