<%-- 
    Document   : index
    Created on : 05/06/2017, 19:13:51
    Author     : luisr
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Documentos</title>
    </head>
    <body>       
        <c:import url="../../menu.jsp" ></c:import>
            <h1>Lista Documentos</h1>
        <c:if test="${mensagem != null}">
            <div>
                <p>${mensagem}</p>
                <ul>
                    <c:forEach items="${funcionariosResponsaveis}" var="funcionarioR">
                        <li>Nome: ${funcionarioR.nome} - Cargo: ${funcionarioR.cargoId.nome}</li>
                        </c:forEach>
                </ul>
                <c:if test="${docAssociados != null}">
                    <p>Lista de documentos que você deve assinar:</p>
                    ${docAssociados}
                </c:if>
            </div>
        </c:if>
        <table border="1">
            <thead>
                <tr>
                    <th>Nome</th>
                    <th>Descrição</th>
                    <th>Opções</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${documentos}" var="documento">
                    <tr>
                        <td>${documento.nome}</td>
                        <td>${documento.descricao}</td>
                        <td>
                            <a href="frontController?controller=DocumentosController&method=deletar&id=${documento.id}">Deletar Documento</a> |
                            <a href="frontController?controller=DocumentosController&method=assinar&id=${documento.id}">Assinar</a> |
                            <a href="frontController?controller=DocumentosController&method=callAssociar&id=${documento.id}">Associar Documentos</a> 
                        </td>
                    </tr>
                </c:forEach>
                <tr>
                    <td colspan="3"><a href="frontController?controller=DocumentosController&method=adicionar">Criar documento</a></td>
                </tr>
            </tbody>
        </table>

    </body>
</html>
