<%-- 
    Document   : listaDocumentos
    Created on : 06/06/2017, 15:52:43
    Author     : Gabriel
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Listar Responsabilidades</title>
    </head>
    <body>
        <h1>Listar Responsabilidades</h1>
        <c:forEach items="${funcionarios}" var="funcionario">
            ${funcionario.nome}
            <ul>
                <c:forEach items="${funcionario.documentosCollection}" var="documento">
                    <li>
                        ${documento.nome}
                    </li>
                </c:forEach>
            </ul>
        </c:forEach>
    </body>
</html>
