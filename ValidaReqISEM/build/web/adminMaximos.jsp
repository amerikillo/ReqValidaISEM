<%-- 
    Document   : index
    Created on : 17/02/2014, 03:34:46 PM
    Author     : Americo
--%>

<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="conn.*" %>
<!DOCTYPE html>
<%java.text.DateFormat df = new java.text.SimpleDateFormat("yyyyMMddhhmmss"); %>
<%java.text.DateFormat df2 = new java.text.SimpleDateFormat("yyyy-MM-dd"); %>
<%java.text.DateFormat df3 = new java.text.SimpleDateFormat("dd/MM/yyyy"); %>
<%

    HttpSession sesion = request.getSession();
    String usua = "", tipo = "";
    if (sesion.getAttribute("F_Nombre") != null) {
        usua = (String) sesion.getAttribute("F_Nombre");
    } else {
        response.sendRedirect("index.jsp");
    }
    ConectionDB con = new ConectionDB();

    String F_DesPro = "", F_ClaPro = "";

    int cantMax = 0;
    int cantSolPrev = 0;
    int banCaptura = 0, banFinalizar = 0, banCantExedida = 0, CantExe = 0;
    String F_ClaCli = request.getParameter("F_ClaCli");
    try {
        con.conectar();
        if (request.getParameter("accion").equals("BuscarInsumo")) {
            ResultSet rset = null;
            if (request.getParameter("F_ClaPro") != null && !request.getParameter("F_ClaPro").equals("")) {
                rset = con.consulta("select F_ClaPro, F_DesPro from tb_medica where F_ClaPro = '" + request.getParameter("F_ClaPro") + "' ");
            }
            if (request.getParameter("F_DesPro") != null && !request.getParameter("F_DesPro").equals("")) {
                rset = con.consulta("select F_ClaPro, F_DesPro from tb_medica where F_DesPro = '" + request.getParameter("F_DesPro") + "' ");
            }
            while (rset.next()) {
                F_ClaPro = rset.getString("F_ClaPro");
                F_DesPro = rset.getString("F_DesPro");
                ResultSet rset2 = con.consulta("select F_Cant from tb_maxdist where F_ClaPro = '" + rset.getString("F_ClaPro") + "' and F_ClaCli = '" + request.getParameter("F_ClaCli") + "' ");
                while (rset2.next()) {
                    cantMax = rset2.getInt("F_Cant");
                }

                rset2 = con.consulta("select SUM(F_Cant) as TotalSol from tb_detpedido d, tb_pedidos p where d.F_IdPed = p.F_IdPed and p.F_ClaCli = '" + request.getParameter("F_ClaCli") + "' and d.F_ClaPro = '" + F_ClaPro + "'  and d.F_StsPed!=500");
                while (rset2.next()) {
                    cantSolPrev = rset2.getInt("TotalSol");
                }

            }

        }
        con.cierraConexion();
    } catch (Exception e) {
        System.out.println(e.getMessage());
    }
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- Estilos CSS -->
        <link href="css/bootstrap.css" rel="stylesheet">
        <link rel="stylesheet" href="css/cupertino/jquery-ui-1.10.3.custom.css" />
        <link href="css/navbar-fixed-top.css" rel="stylesheet">
        <!---->
        <title>SIALSS</title>
    </head>
    <body>
        <div class="container">
            <h1>SIALSS</h1>
            <h4>Módulo - Requerimiento de Distribuidor</h4>
            <%@include file="jspf/MenuPrincipal.jspf" %>

            <h3>Administración de Máximos</h3>
            <form action="adminMaximos.jsp" method="post">
                <div class="row">
                    <h4 class="col-sm-2">
                        Distribuidor
                    </h4>
                    <div class="col-sm-4">
                        <select name="F_ClaCli" id="F_ClaCli" class="form-control" autofocus="" placeholder="Introduzca Nombre de Usuario">
                            <option>-Seleccione Distribuidor-</option>
                            <%
                                try {
                                    con.conectar();
                                    ResultSet rset = con.consulta("select F_ClaCli, F_NomCli from tb_uniatn");
                                    while (rset.next()) {
                            %>
                            <option value="<%=rset.getString("F_ClaCli")%>" 
                                    <%
                                        if (F_ClaCli != null && F_ClaCli.equals(rset.getString("F_ClaCli"))) {
                                            out.println("selected");
                                        }
                                    %>
                                    ><%=rset.getString("F_NomCli")%></option>
                            <%
                                    }
                                    con.cierraConexion();
                                } catch (Exception e) {

                                }
                            %>
                        </select>
                    </div>
                </div>
                <div class="row">
                    <h4 class="col-sm-1">Clave:</h4>
                    <div class="col-sm-2">
                        <input class="form-control" placeholder="Por Clave" name="F_ClaPro" value="<%=F_ClaPro%>" />
                    </div>
                    <h4 class="col-sm-2">Descripción:</h4>
                    <div class="col-sm-6">
                        <input class="form-control" placeholder="Por Descripción" id="buscarDescrip" name="F_DesPro" value="<%=F_DesPro%>" />
                    </div>
                    <div class="col-lg-1">
                        <button class="btn btn-primary btn-block" name="accion" value="BuscarInsumo">Buscar</button>
                    </div>
                </div>
            </form>

            <form action="Maximos" method="post">
                <input class="hidden" name="F_ClaCli" value="<%=F_ClaCli%>" />
                <input class="hidden" name="F_ClaPro" value="<%=F_ClaPro%>" />
                <div class="row">
                    <h4 class="col-sm-2">Cantidad Solicitada:</h4>
                    <div class="col-sm-2">
                        <input class="form-control text-right" value="<%=cantSolPrev%>" readonly/>
                    </div>
                    <h4 class="col-sm-2">Cantidad Máxima:</h4>
                    <div class="col-sm-2">
                        <input type="number" class="form-control text-right" name="F_CantMax" min="<%=cantSolPrev%>" value="<%=cantMax%>" />
                    </div>
                    <div class="col-sm-2">
                        <button class="btn btn-success btn-block" name="accion" value="ActualizarMax">Actualizar</button>
                    </div>
                </div>
            </form>
            <%@include file="jspf/piePagina.jspf" %>
    </body>
    <!-- 
    ================================================== -->
    <!-- Se coloca al final del documento para que cargue mas rapido -->
    <!-- Se debe de seguir ese orden al momento de llamar los JS -->
    <script src="js/jquery-1.9.1.js"></script>
    <script src="js/bootstrap.js"></script>
    <script src="js/funcInvCiclico.js"></script>
    <script src="js/jquery-ui.js"></script>
</html>

