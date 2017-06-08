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
        <c:import url="../../menu.jsp" ></c:import>
        <h1>Associe outros documentos ao ${documento.nome}</h1>
        <c:if test="${mensagem != null}">
            <p style="color: red">${mensagem}</p>
        </c:if>
        <form action="frontController?controller=DocumentosController&method=associarDocumento" method="POST">
            <c:forEach items="${allDocumentos}" var="doc">
                <input type="checkbox" name="documentos" value="${doc.id}">${doc.nome} 
                <br />
            </c:forEach>
            <input type="hidden" name="id" value="${documento.id}" />
            <input type="submit" value="Associar" />
        </form>
    </body>
</html>
