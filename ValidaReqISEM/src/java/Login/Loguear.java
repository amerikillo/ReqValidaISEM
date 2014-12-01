/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Login;

import conn.ConectionDB;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Americo
 */
public class Loguear extends HttpServlet {

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
        HttpSession sesion = request.getSession(true);
        ConectionDB con = new ConectionDB();
        try {
            /* TODO output your page here. You may use following sample code. */
            try {
                con.conectar();
                try {
                    String F_Nombre = "", F_Tipo = "";
                    int ban = 0, F_IdUsuario = 0;
                    ResultSet rset = con.consulta(" select nom_com, tipo, id_usuario from tb_usuarios_isem where nombre = '" + request.getParameter("nombre") + "' and contra = MD5('" + request.getParameter("pass") + "')  ");
                    while (rset.next()) {
                        ban = 1;
                        F_Nombre = rset.getString("nom_com");
                        F_Tipo = rset.getString("tipo");
                        F_IdUsuario = rset.getInt("id_usuario");
                    }
                    if (ban == 1) {//----------------------EL USUARIO ES VÁLIDO
                        sesion.setAttribute("F_Nombre", F_Nombre);
                        sesion.setAttribute("F_Tipo", F_Tipo);
                        sesion.setAttribute("F_IdUsuario", F_IdUsuario);
                        con.ejecutar("insert into tb_registroentradas values ('" + request.getParameter("nombre") + "',NOW(),1,0)");
                        response.sendRedirect("main_menu.jsp");
                    } else {//--------------------------EL USUARIO NO ES VÁLIDO
                        con.ejecutar("insert into tb_registroentradas values ('" + request.getParameter("nombre") + "',NOW(),0,0)");
                        sesion.setAttribute("mensaje", "Usuario no válido");
                        response.sendRedirect("index.jsp");
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                con.cierraConexion();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } finally {
            out.close();
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
