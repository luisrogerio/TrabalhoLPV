<%-- 
    Document   : index
    Created on : 30/05/2017, 19:23:25
    Author     : luisr
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Funcionários</title>
    </head>
    <body>
        <h1>Funcionários</h1>
        <c:if test="${mensagemEstado != null}">
            <div>
                <p>${mensagemEstado}</p>
            </div>
        </c:if>
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
                            <a href="frontController?controller=FuncionariosController&method=desligar&id=${funcionario.id}">Desligar</a> | 
                            <a href="frontController?controller=FuncionariosController&method=visualizar&id=${funcionario.id}">Visualizar</a> | 
                            <a href="frontController?controller=FuncionariosController&method=adicionar&acao=Alterar&id=${funcionario.id}">Editar</a> |
                            <a href="frontController?controller=FuncionariosController&method=callAssociarResponsabilidade&id=${funcionario.id}">Delegar Responsabilidade</a> 
                        </td>
                    </tr>
                </c:forEach>
                <tr>
                    <td colspan="4"><a href="frontController?controller=FuncionariosController&method=adicionar&acao=Contratar">Contratar Funcionário</a></td>
                    <td ><a href="frontController?controller=FuncionariosController&method=listar">Listar Responsabilidades</a></td>
                </tr>
                
            </tbody>
        </table>
    </body>
</html>
