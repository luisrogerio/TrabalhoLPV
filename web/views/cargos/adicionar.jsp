<%-- 
    Document   : adicionar
    Created on : 28/05/2017, 11:31:32
    Author     : luisr
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Cargos e Salários</title>
    </head>
    <body>
        <h1>Adicionar novo cargo <c:out>${tipo}</c:out></h1>
        <form action="frontController?controller=CargosController&method=salvar" method="POST">
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
                            <label for="setor">Setor</label>
                        </td>
                        <td>
                            <input type="text" name="setor"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label for="multiplicadorSalario">Valor da hora trabalhada</label>
                        </td>
                        <td>
                            <input type="number" name="multiplicadorSalario"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label for="descontoINSS">Taxa do INSS (Escrever em decimal)</label>
                        </td>
                        <td>
                            <input type="number" name="descontoINSS"/>
                        </td>
                    </tr>
                </tbody>
            </table>
            <input type="hidden" name="tipo" value="${tipo}" />
            <input type="submit" value="Criar" />
        </form>
    </body>
</html>
