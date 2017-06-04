<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Empresas</title>
    </head>
    <body>
        <h1>Adicionar Nova Empresa</h1>
        <form action="frontController?controller=EmpresasController&method=salvar" method="POST">
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
                            <label for="cnpj">CNPJ</label>
                        </td>
                        <td>
                            <input type="text" name="cnpj"/>
                        </td>
                    </tr>
                </tbody>
            </table>
            <input type="submit" value="Salvar" />
        </form>
    </body>
</html>
