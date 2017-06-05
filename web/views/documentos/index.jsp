<%-- 
    Document   : index
    Created on : 05/06/2017, 19:13:51
    Author     : luisr
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Documentos</title>
    </head>
    <body>
        <h1>Lista Documentos</h1>
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
                        <td><a href="frontController?controller=DocumentosController&method=deletar&id=${documento.id}">Deletar Documento</a></td>
                    </tr>
                </c:forEach>
                <tr>
                    <td><a href="frontController?controller=DocumentosController&method=adicionar">Criar documento</a></td>
                </tr>
            </tbody>
        </table>
    </body>
</html>
