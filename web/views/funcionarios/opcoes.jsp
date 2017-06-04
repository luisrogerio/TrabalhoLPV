<%-- 
    Document   : opcoes
    Created on : 01/06/2017, 14:05:49
    Author     : Gabriel
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Funcionário</title>
    </head>
    <body>
        <h3>Visualizar dados Funcionário - ${funcionario.nome}</h3>
        <c:if test="${! empty mensagem }">
            <p>${mensagem}</p>
        </c:if>
        <ul>
            <li>CPF: ${funcionario.cpf}</li>
            <li>Email: ${funcionario.email}</li>
            <li>Tipo: ${funcionario.tipo}</li>
            <li>Estado: ${funcionario.estadoId.estado}</li>
            <li>Data de Admissão: ${dataAdimissao}</li>
            <li>Cargo: ${funcionario.cargoId.nome}</li>
                <c:if test="${funcionario.gerenteId eq null or ''}">
                <li>Gerente: ${funcionario.gerenteId.nome}</li>
                </c:if>
            <li>Empresa: ${funcionario.empresaId.nome}</li>
        </ul>

        <c:if test="${funcionario.estadoId.estado ne'Férias'}">
            <a href="frontController?controller=FuncionariosController&method=alterarEstado&novoEstado=ferias&id=${funcionario.id}">Colocar funcionario de férias</a>
        </c:if>
        <c:if test="${funcionario.estadoId.estado ne 'Licença'}">
            | <a href="frontController?controller=FuncionariosController&method=alterarEstado&novoEstado=licenca&id=${funcionario.id}">Colocar funcionario de Licença</a>
        </c:if>
        <c:if test="${funcionario.estadoId.estado ne 'Desligado'}">
            | <a href="frontController?controller=FuncionariosController&method=alterarEstado&novoEstado=desligar&id=${funcionario.id}">Desligar</a>
        </c:if>
        <c:if test="${funcionario.estadoId.estado ne 'Ativo'}">
            | <a href="frontController?controller=FuncionariosController&method=alterarEstado&novoEstado=ativar&id=${funcionario.id}">Ativar</a> 
        </c:if>
    </body>
</html>
