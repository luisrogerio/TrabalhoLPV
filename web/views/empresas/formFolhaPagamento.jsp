<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Empresas</title>
    </head>
    <body>
        <h1>${empresa.nome} - ${empresa.cnpj}</h1>
        <h2>Geração de Folha de Pagamentos dos Funcionários</h2>
        <form action="frontController?controller=EmpresasController&method=gerarFolhasDePagamento" method="POST">
            <table>
                <tbody>
                    <tr>
                        <td>
                            <label for="mesAno">Mês e Ano de Refêrencia</label>
                        </td>
                        <td>
                            <input type="month" name="mesAno"/>
                        </td>
                    </tr>
                </tbody>
            </table>
            <input type="hidden" value="${empresa.id}" />
            <input type="submit" value="Salvar" />
        </form>
    </body>
</html>
