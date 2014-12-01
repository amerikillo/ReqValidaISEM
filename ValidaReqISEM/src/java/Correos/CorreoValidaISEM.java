/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Correos;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import conn.*;
import java.sql.ResultSet;

/**
 *
 * @author Americo
 */
public class CorreoValidaISEM extends HttpServlet {



    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

    }

    public void enviaCorreo(String F_IdPed) {
        try {
            ConectionDB con = new ConectionDB();
            con.conectar();
            /* TODO output your page here. You may use following sample code. */
            Properties props = new Properties();
            props.setProperty("mail.smtp.host", "smtp.gmail.com");
            props.setProperty("mail.smtp.starttls.enable", "true");
            props.setProperty("mail.smtp.port", "587");
            props.setProperty("mail.smtp.user", "ricardo.wence@gnkl.mx");
            props.setProperty("mail.smtp.auth", "true");

            // Preparamos la sesion
            Session session = Session.getDefaultInstance(props);

            // Construimos el mensaje
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("ricardo.wence@gnkl.mx"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress("ricardo.wence@gnkl.mx"));//Aqui se pone la direccion a donde se enviara el correo
            //message.addRecipient(Message.RecipientType.TO,new InternetAddress("anibal.rincon@gnkl.mx"));//Aqui se pone la direccion a donde se enviara el correo
            message.addRecipient(Message.RecipientType.TO, new InternetAddress("americo.guzman@gnkl.mx"));//Aqui se pone la direccion a donde se enviara el correo
            //message.addRecipient(Message.RecipientType.TO,new InternetAddress("omar_23sh@hotmail.com"));//Aqui se pone la direccion a donde se enviara el correo
            /*message.addRecipient(Message.RecipientType.TO, new InternetAddress("marioreyesflores22@gmail.com"));//Aqui se pone la direccion a donde se enviara el correo
             message.addRecipient(Message.RecipientType.TO, new InternetAddress("irisolmorales1@gmail.com"));//Aqui se pone la direccion a donde se enviara el correo
             message.addRecipient(Message.RecipientType.TO, new InternetAddress("mgarduno418@gmail.com"));//Aqui se pone la direccion a donde se enviara el correo
             message.addRecipient(Message.RecipientType.TO, new InternetAddress("gerardo.morales@gnkl.mx"));//Aqui se pone la direccion a donde se enviara el correo
             message.addRecipient(Message.RecipientType.TO, new InternetAddress("oscargnkl@gmail.com"));//Aqui se pone la direccion a donde se enviara el correo
             message.addRecipient(Message.RecipientType.TO, new InternetAddress("joseluis.chavez@gnkl.mx"));//Aqui se pone la direccion a donde se enviara el correo
             message.addRecipient(Message.RecipientType.TO, new InternetAddress("javier.calero@gnkl.mx"));//Aqui se pone la direccion a donde se enviara el correo
             message.addRecipient(Message.RecipientType.TO, new InternetAddress("mario.garcia@gnkl.mx"));//Aqui se pone la direccion a donde se enviara el correo
             message.addRecipient(Message.RecipientType.TO, new InternetAddress("yolanda.orozco@gnkl.mx"));//Aqui se pone la direccion a donde se enviara el correo
             message.addRecipient(Message.RecipientType.TO, new InternetAddress("vicente.flores@gnkl.mx"));//Aqui se pone la direccion a donde se enviara el correo
             message.addRecipient(Message.RecipientType.TO, new InternetAddress("msergio0505@gmail.com"));//Aqui se pone la direccion a donde se enviara el correo
             //message.addRecipient(Message.RecipientType.TO,new InternetAddress("javier.calero@gnkl.mx"));//Aqui se pone la direccion a donde se enviara el correo
             //message.addRecipient(Message.RecipientType.TO,new InternetAddress("mario.garcia@gnkl.mx"));//Aqui se pone la direccion a donde se enviara el correo
             //message.addRecipient(Message.RecipientType.TO,new InternetAddress("joseluis.chavez@gnkl.mx"));//Aqui se pone la direccion a donde se enviara el correo*/
            String F_NomCli = "";
            ResultSet rset = con.consulta("select F_NomCli from tb_pedidos p, tb_uniatn u where p.F_ClaCli = u.F_ClaCli and F_IdPed = '" + F_IdPed + "'");
            while (rset.next()) {
                F_NomCli = rset.getString("F_NomCli");
            }

            message.setSubject("Requerimiento Validado: " + F_NomCli);
            String mensaje = "Se acaba de aceptar el siguiente Requerimiento: " + F_IdPed + "\n";
            mensaje = mensaje + "Distribuidor: " + F_NomCli + "\n";

            mensaje = mensaje + "\n\n";
            rset = con.consulta("select * from v_requerimientos where F_IdPed = '" + F_IdPed + "' ");
            while (rset.next()) {
                mensaje = mensaje + "Clave:\t" + rset.getString("F_ClaPro") + "\tCantidad: " + rset.getString("F_Cant") + "\n";
                mensaje = mensaje + rset.getString("F_DesPro")+"\n";
            }

            System.out.println(mensaje);
            message.setText(mensaje);

            // Lo enviamos.
            Transport t = session.getTransport("smtp");
            t.connect("ricardo.wence@gnkl.mx", "ricardo.wence+111");
            t.sendMessage(message, message.getAllRecipients());

            // Cierre.
            t.close();
            con.cierraConexion();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
