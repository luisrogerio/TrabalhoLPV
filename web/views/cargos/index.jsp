<%-- 
    Document   : index
    Created on : 27/05/2017, 16:11:28
    Author     : luisr
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Cargos</title>
    </head>
    <body>
        <h1>Cargos e Salários</h1>
        <table border="1">
            <thead>
                <tr>
                    <th>Nome</th>
                    <th>Tipo</th>
                    <th>Valor da Hora</th>
                        <th>Opções</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${cargos}" var="cargo">
                    <tr>
                        <td>${cargo.nome}</td>
                        <td>${cargo.tipo}</td>
                        <td>${cargo.multiplicadorSalario}</td>
                        <td><a href="CargosController?metodo=deletar&id=${cargo.id}">Deletar Cargo</a></td>
                    </tr>
                </c:forEach>
                    <tr>
                        <td><a href="CargosController?metodo=adicionarExecutivo">Adicionar Cargo Executivo</a></td>
                        <td><a href="CargosController?metodo=adicionarTatico">Adicionar Cargo Tático</a></td>
                        <td><a href="CargosController?metodo=adicionarOperacional">Adicionar Cargo Operacional</a></td>
                    </tr>
            </tbody>
        </table>
    </body>
</html>
