<%-- 
    Document   : index
    Created on : 30/05/2017, 19:23:25
    Author     : luisr
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Funcionários</h1>
        <table border="1">
            <thead>
                <tr>
                    <th>Nome</th>
                    <th>Tipo</th>
                    <th>Estado</th>
                    <th>Cargo</th>
                    <th>Opções</th>
                </tr>
            </thead>
            <tbody>
            <c:forEach items="${funcionarios}" var="funcionario">
                <tr>
                    <td>${funcionario.nome}</td>
                    <td>${funcionario.tipo}</td>
                    <td>${funcionario.estadoId.estado}</td>
                    <td>${funcionario.cargoId.nome}</td>
                    <td>
                        <a href="frontController?controller=CargosController&method=deletar&id=${funcionario.id}">Desligar</a> | 
                        <a href="frontController?controller=CargosController&method=deletar&id=${funcionario.id}">Visualizar</a>
                    </td>
                </tr>
            </c:forEach>
            <tr>
                <td><a href="frontController?controller=CargosController&method=adicionar&tipo=Executivo">Contratar Funcionário</a></td>
                <td><a href="frontController?controller=CargosController&method=adicionar&tipo=Tatico">Adicionar Cargo Tático</a></td>
                <td><a href="frontController?controller=CargosController&method=adicionar&tipo=Operacional">Adicionar Cargo Operacional</a></td>
            </tr>
        </tbody>
    </table>
</body>
</html>
