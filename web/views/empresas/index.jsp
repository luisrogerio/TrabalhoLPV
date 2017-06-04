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
        <title>Empresas</title>
    </head>
    <body>
        <h1>Empresas</h1>
        <table border="1">
            <thead>
                <tr>
                    <th>Nome</th>
                    <th>CNPJ</th>
                    <th colspan="2">Opções</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${empresas}" var="empresa">
                    <tr>
                        <td>${empresa.nome}</td>
                        <td>${empresa.cnpj}</td>
                        <td><a href="frontController?controller=EmpresasController&method=deletar&id=${empresa.id}">Deletar Cargo</a></td>
                        <td><a href="frontController?controller=EmpresasController&method=pegarMesAno&id=${empresa.id}">Gerar Folhas de Pagamento dos Funcionários</a></td>
                    </tr>
                </c:forEach>
                <tr>
                    <td><a href="frontController?controller=EmpresasController&method=adicionar">Adicionar Empresa</a></td>
                </tr>
            </tbody>
        </table>
    </body>
</html>
