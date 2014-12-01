/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Requerimientos;

import Correos.CorreoValidaISEM;
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
public class ValidarReq extends HttpServlet {

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
        ConectionDB con = new ConectionDB();
        PrintWriter out = response.getWriter();
        HttpSession sesion = request.getSession(true);
        try {
            if (request.getParameter("accion").equals("ValidarConExce")) {
                CorreoValidaISEM correo = new CorreoValidaISEM();
                con.conectar();
                String F_ClaCli = "";
                ResultSet rset = con.consulta("select F_ClaCli from tb_pedidos where F_IdPed='" + request.getParameter("F_IdPed") + "'");
                while (rset.next()) {
                    F_ClaCli = rset.getString("F_ClaCli");
                }
                rset = con.consulta("select F_Id, F_Cant, F_ClaPro from tb_detpedido where F_IdPed = '" + request.getParameter("F_IdPed") + "' ");
                while (rset.next()) {
                    int cantSolPrev = 0;
                    ResultSet rset2 = con.consulta("select SUM(F_Cant) as TotalSol from tb_detpedido d, tb_pedidos p where d.F_IdPed = p.F_IdPed and p.F_ClaCli = '" + F_ClaCli + "' and d.F_ClaPro = '" + rset.getString("F_ClaPro") + "' and d.F_StsPed!=500 ");
                    while (rset2.next()) {
                        cantSolPrev = rset2.getInt("TotalSol");
                    }

                    con.ejecutar("update tb_maxdist set F_Cant = '" + cantSolPrev + "' where F_ClaPro = '" + rset.getString("F_ClaPro") + "' and F_ClaCli = '" + F_ClaCli + "'  ");
                }
                con.ejecutar("update tb_pedidos set F_StsPed = '4', F_FecEnt=CURDATE() where F_IdPed = '" + request.getParameter("F_IdPed") + "'");
                con.ejecutar("update tb_detpedido set F_StsPed = '4', F_FecSur=CURDATE() where F_IdPed = '" + request.getParameter("F_IdPed") + "'");
                con.ejecutar("insert into tb_validaisem values('" + request.getParameter("F_IdPed") + "','" + sesion.getAttribute("F_IdUsuario") + "','0')");
                correo.enviaCorreo(request.getParameter("F_IdPed"));
                con.cierraConexion();
                response.sendRedirect("main_menu.jsp");
            }
            if (request.getParameter("accion").equals("ValidarSinExce")) {
                con.conectar();
                String F_ClaCli = "";
                ResultSet rset = con.consulta("select F_ClaCli from tb_pedidos where F_IdPed='" + request.getParameter("F_IdPed") + "'");
                while (rset.next()) {
                    F_ClaCli = rset.getString("F_ClaCli");
                }
                rset = con.consulta("select F_Id, F_Cant, F_ClaPro from tb_detpedido where F_IdPed = '" + request.getParameter("F_IdPed") + "' ");
                while (rset.next()) {
                    int cantMax = 0;
                    int cantSol = rset.getInt("F_Cant");
                    int cantSolPrev = 0;
                    ResultSet rset2 = con.consulta("select F_Cant from tb_maxdist where F_ClaPro='" + rset.getString("F_ClaPro") + "' and F_ClaCli = '" + F_ClaCli + "'");
                    while (rset2.next()) {
                        cantMax = rset2.getInt("F_Cant");
                    }

                    rset2 = con.consulta("select SUM(F_Cant) as TotalSol from tb_detpedido d, tb_pedidos p where d.F_IdPed = p.F_IdPed and p.F_ClaCli = '" + F_ClaCli + "' and d.F_ClaPro = '" + rset.getString("F_ClaPro") + "' and d.F_StsPed!=500 ");
                    while (rset2.next()) {
                        cantSolPrev = rset2.getInt("TotalSol");
                    }

                    cantMax = cantMax - cantSolPrev;

                    if (cantMax < 0) {
                        cantMax = (cantSolPrev - (-cantMax));
                        con.ejecutar("update tb_detpedido set F_StsPed = '4' , F_Cant = '" + cantMax + "', F_FecSur=CURDATE() where F_Id = '" + rset.getString("F_Id") + "'");
                    } else {
                        con.ejecutar("update tb_detpedido set F_StsPed = '4' , F_Cant = '" + cantSol + "', F_FecSur=CURDATE() where F_Id = '" + rset.getString("F_Id") + "'");
                    }

                }
                con.ejecutar("update tb_pedidos set F_StsPed = '4', F_FecEnt=CURDATE()  where F_IdPed = '" + request.getParameter("F_IdPed") + "'");
                con.ejecutar("insert into tb_validaisem values('" + request.getParameter("F_IdPed") + "','" + sesion.getAttribute("F_IdUsuario") + "','0')");
                con.cierraConexion();
                response.sendRedirect("main_menu.jsp");
            }
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
