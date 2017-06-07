<%-- 
    Document   : opcoes
    Created on : 01/06/2017, 14:05:49
    Author     : Gabriel
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>

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

        <a href="frontController?controller=FolhasDePagamentoController&method=adicionarFolha&id=${funcionario.id}">Adicionar Horas Extras</a> <br />
        <c:if test="${not empty ultimoEstado}">
            <a href="frontController?controller=FuncionariosController&method=desfazerEstado&id=${funcionario.id}">Desfazer para último estado - ${ultimoEstado}</a>
        </c:if>
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
        <table>
            <thead>
                <tr>
                    <td>Código</td>
                    <th>Horas Extras</th>
                    <th>Data de Referência</th>
                    <th>Opções</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${funcionario.folhasDePagamentoCollection}" var="folha">
                    <tr>
                        <td><c:if test="${folha.id == funcionario.novaFolha}">Novo* </c:if>${folha.id}</td>
                        <td>${folha.horasExtras} horas</td>
                        <td><f:formatDate value="${folha.mesAnoDeReferencia}" pattern="MMMM 'de' yyyy" type="date" ></f:formatDate></td>
                        <td><a href="frontController?controller=FolhasDePagamentoController&method=gerarFolhaIndividual&id=${folha.id}">Gerar Folha PDF</a></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

    </body>
</html>
