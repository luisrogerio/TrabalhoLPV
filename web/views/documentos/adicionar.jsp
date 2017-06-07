<%-- 
    Document   : adiciona
    Created on : 05/06/2017, 19:13:57
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
        <h1>Adicione Novos Documentos</h1>
        <form action="frontController?controller=DocumentosController&method=salvar" method="POST">
            <table>
                <tbody>
                    <tr>
                        <td>
                            <label for="nome">Nome</label>
                        </td>
                        <td>
                            <input type="text" name="nome"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label for="setor">Descrição</label>
                        </td>
                        <td>
                            <textarea name="descricao" rows="4" cols="50"></textarea>
                        </td>
                    </tr>
                </tbody>
            </table>
            <input type="submit" value="Criar" />
        </form>
    </body>
</html>
