<%-- 
    Document   : associarDocumentos
    Created on : 06/06/2017, 10:49:17
    Author     : Gabriel
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Associar Documentos</title>
    </head>
    <body>
        <h1>Associe os Documentos ao ${funcionario.nome}</h1>
        <c:if test="${mensagem != null}">
            <p>${mensagem}</p>
        </c:if>
        <form action="frontController?controller=FuncionariosController&method=associarResponsabilidades" method="POST">
            <c:forEach items="${documentos}" var="documento">
                <input type="checkbox" name="documentos" value="${documento.id}">${documento.nome} 
                <br />
            </c:forEach>
            <input type="hidden" name="id" value="${funcionario.id}" />
            <input type="submit" value="Associar" />
        </form>
    </body>
</html>
