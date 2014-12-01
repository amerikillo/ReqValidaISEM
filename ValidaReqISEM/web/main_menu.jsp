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


            <h3>Requerimiento</h3>

            <h3>Requerimientos Pendientes por Revisar</h3>

            <table class="table table-condensed table-bordered table-striped">
                <tr>
                    <td>Distribuidor</td>
                    <td>ID Pedido</td>
                    <td>Status</td>
                    <td>Fecha de Captura</td>
                    <td width="100px"></td>
                </tr>
                <%                    try {
                        con.conectar();
                        ResultSet rset = con.consulta("select u.F_NomCli, p.F_IdPed, p.F_StsPed, DATE_FORMAT(p.F_FecCap,'%d/%m/%Y %H:%i:%s')as F_FecCap from tb_pedidos p, tb_uniatn u where p.F_ClaCli = u.F_ClaCli and F_StsPed=3");
                        while (rset.next()) {
                            String color = "warning";
                            String status = "En Revisión";
                            if (rset.getString("F_StsPed").equals("1")) {
                                status = "Eliminado";
                                color = "danger";
                            }
                            if (rset.getString("F_StsPed").equals("2")) {
                                status = "Capturado";
                                color = "success";
                            }
                %>
                <tr class="<%//=color%>">
                    <td><%=rset.getString("F_NomCli")%></td>
                    <td><%=rset.getString("F_IdPed")%></td>
                    <td><%=status%></td>
                    <td><%=rset.getString("F_FecCap")%></td>
                    <td>
                        <div class="row">
                            <form action="verRequerimentoEsp.jsp" method="post" class="col-sm-6">
                                <input value="<%=rset.getString("F_IdPed")%>" name="F_IdPed"  class="hidden" />
                                <input value="No" name="Validado"  class="hidden" />
                                <button class="btn btn-primary btn-sm" name="accion" value="EliminarInsumo"><span class="glyphicon glyphicon-search"></span></button>
                            </form>
                            <form action="Capturar?F_IdPed=<%=rset.getString("F_IdPed")%>" method="post" class="col-sm-6">
                                <button class="btn btn-danger btn-sm" name="accion" onclick="return confirm('Seguro que desea eliminar el pedido?')" value="EliminarPedido">X</button>
                            </form>
                        </div>
                    </td>
                </tr>
                <%
                        }
                        con.cierraConexion();
                    } catch (Exception e) {

                    }
                %>
                </tr>
            </table>

            <h3>Requerimientos Revisados</h3>

            <table class="table table-condensed table-bordered table-striped">
                <tr>
                    <td>Distribuidor</td>
                    <td>ID Pedido</td>
                    <td>Status</td>
                    <td>Fecha de Captura</td>
                    <td width="100px"></td>
                </tr>
                <%                    try {
                        con.conectar();
                        ResultSet rset = con.consulta("select u.F_NomCli, p.F_IdPed, p.F_StsPed, DATE_FORMAT(p.F_FecCap,'%d/%m/%Y %H:%i:%s')as F_FecCap from tb_pedidos p, tb_uniatn u where p.F_ClaCli = u.F_ClaCli and F_StsPed=4");
                        while (rset.next()) {
                            String color = "warning";
                            String status = "En Revisión";
                            if (rset.getString("F_StsPed").equals("1")) {
                                status = "Eliminado";
                                color = "danger";
                            }
                            if (rset.getString("F_StsPed").equals("2")) {
                                status = "Capturado";
                                color = "success";
                            }
                %>
                <tr class="<%//=color%>">
                    <td><%=rset.getString("F_NomCli")%></td>
                    <td><%=rset.getString("F_IdPed")%></td>
                    <td><%=status%></td>
                    <td><%=rset.getString("F_FecCap")%></td>
                    <td>
                        <div class="row">
                            <form action="verRequerimentoEsp.jsp" method="post" class="col-sm-6">
                                <input value="<%=rset.getString("F_IdPed")%>" name="F_IdPed"  class="hidden" />
                                <input value="Si" name="Validado"  class="hidden" />
                                <button class="btn btn-primary btn-sm" name="accion" value="EliminarInsumo"><span class="glyphicon glyphicon-search"></span></button>
                            </form>
                            <!--form action="Capturar?F_IdPed=<%=rset.getString("F_IdPed")%>" method="post" class="col-sm-6">
                                <button class="btn btn-danger btn-sm" name="accion" onclick="return confirm('Seguro que desea eliminar el pedido?')" value="EliminarPedido">X</button>
                            </form-->
                        </div>
                    </td>
                </tr>
                <%
                        }
                        con.cierraConexion();
                    } catch (Exception e) {

                    }
                %>
                </tr>
            </table>

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

