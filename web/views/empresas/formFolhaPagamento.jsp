<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Empresas</title>
    </head>
    <body>
        <c:import url="../../menu.jsp" ></c:import>
        <h1>${empresa.nome} - ${empresa.cnpj}</h1>
        <h2>Geração de Folha de Pagamentos dos Funcionários</h2>
        <form action="frontController?controller=EmpresasController&method=gerarFolhasDePagamento&id=${empresa.id}" method="POST">
            <table>
                <tbody>
                    <tr>
                        <td>
                            <label for="mes">Mês de Refêrencia</label>
                        </td>
                        <td>
                            <select name="mes">
                                <option value="01">Janeiro</option>
                                <option value="02">Fevereiro</option>
                                <option value="03">Março</option>
                                <option value="04">Abril</option>
                                <option value="05">Maio</option>
                                <option value="06">Junho</option>
                                <option value="07">Julho</option>
                                <option value="08">Agosto</option>
                                <option value="09">Setembro</option>
                                <option value="10">Outubro</option>
                                <option value="11">Novembro</option>
                                <option value="12">Dezembro</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label for="ano">Ano de Refêrencia</label>
                        </td>
                        <td>
                            <input type="number" min="1990" max="2060" value="2017" name="ano"/>
                        </td>
                    </tr>
                </tbody>
            </table>
            <input type="hidden" value="${empresa.id}" name="empresaId"/>
            <input type="submit" value="Salvar" />
        </form>
    </body>
</html>
