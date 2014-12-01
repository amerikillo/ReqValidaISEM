<%-- 
    Document   : index
    Created on : 01-oct-2013, 12:05:12
    Author     : americo
--%>

<%@page import="java.sql.ResultSet"%>
<%@page import="conn.ConectionDB"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    HttpSession sesion = request.getSession();
    ConectionDB con = new ConectionDB();
%>
<html>
    <head>
        <title>Ingreso Valida ISEM</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- Bootstrap -->
        <link href="css/bootstrap.css" rel="stylesheet" media="screen">
        <link href="css/login.css" rel="stylesheet" media="screen">
        <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    </head>
    <body>
        <form name ="form" id="forma-login" class="marco" action="Loguear" method="post" >
            <!--label for="username" class="uname" data-icon="u" > Your email or username </label-->
            <div class="row">
                <div class="col-md-4"><img src="imagenes/GNKL_Small.JPG" ></div>
                <div class="col-md-8"><h2>Validar Usuario</h2></div>

            </div>


            <div class="input-group">
                <span class="input-group-addon"><label class="glyphicon glyphicon-user"></label>
                </span>
                <input name="nombre" id="nombre" class="form-control" autofocus="" placeholder="Introduzca Nombre de Usuario" />
            </div>
            <div class="input-group">
                <span class="input-group-addon"><label class="glyphicon glyphicon-hand-right"></label>
                </span>

                <input type="password" name="pass" id="pass" class="form-control"  placeholder="Introduzca Contrase&ntilde;a V&aacute;lida">
            </div>
            <br>              
            <button name="envio" class="btn btn-primary btn-lg btn-block" type="submit">Entrar</button>
            <br>
        </form>
    </body>

    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="js/jquery.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="js/bootstrap.js"></script>
</html>
<%
                        sesion.invalidate();
%>