<%-- 
    Document   : adiciona
    Created on : 30/05/2017, 19:23:44
    Author     : luisr
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Funcionário</title>
    </head>
    <body>
        <c:import url="../../menu.jsp" ></c:import>
        <h1><c:out value="${acao}"></c:out> Funcionário</h1>
            <form action="frontController?controller=FuncionariosController&method=${method}" method="POST">
                <table>
                    <tbody>
                        <tr>
                            <td>
                                <label for="nome">Nome</label>
                            </td>
                            <td>
                                <input type="text" name="nome" value="${funcionario.nome}"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label for="nome">CPF</label>
                        </td>
                        <td>
                            <input type="text" name="cpf" value="${funcionario.cpf}"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label for="email">Email</label>
                        </td>
                        <td>
                            <input type="email" name="email" value="${funcionario.email}" />
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label for="horasTrabalhadas">Horas Trabalhadas</label>
                        </td>
                        <td>
                            <input type="number" name="horasTrabalhadas" min="1" value="${funcionario.horasTrabalhadas}"/> *Se o funcionario for horista preencher com o horario acordado mensal.
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label for="dataAdmissao">Data de Admissão</label>
                        </td>
                        <td>
                            <input type="date" name="dataAdmissao" max="8" min="8" value="${dataAdimissao}">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label for="tipo">Tipo</label>
                        </td>
                        <td>
                            <select name="tipo">
                                <option value="FuncionariosHoristas" <c:if test="${funcionario.tipo eq 'horista'}">selected</c:if>>Horista</option>
                                <option value="FuncionariosMensalista" <c:if test="${funcionario.tipo eq 'mensalista'}">selected</c:if>>Mensalista</option>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <label for="estado">Condição</label>
                            </td>
                            <td>
                                <select name="estado">
                                <option value="Ativo" <c:if test="${funcionario.estadoId.estado eq 'Ativo'}">selected</c:if>>Ativo</option>
                                    <option value="Férias" <c:if test="${funcionario.estadoId.estado eq 'Férias'}">selected</c:if>>Férias</option>
                                <option value="Licença" <c:if test="${funcionario.estadoId.estado eq 'Licença'}">selected</c:if>>Licença</option>
                                <option value="Desligado" <c:if test="${funcionario.estadoId.estado eq 'Desligado'}">selected</c:if>>Desligado</option>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <label for="setor">Cargo</label>
                            </td>
                            <td>
                                <select name="cargo">
                                <c:forEach items="${cargos}" var="cargo">
                                    <option value="${cargo.id}" <c:if test="${funcionario.cargoId.id eq cargo.id}">selected</c:if> >${cargo.nome}</option>
                                </c:forEach>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label for="setor">Gerente</label>
                        </td>
                        <td>
                            <select name="gerente">
                                <option value="0">----Selecione o gerente----</option>
                                <c:forEach items="${gerentes}" var="gerente">
                                    <option value="${gerente.id}" <c:if test="${funcionario.gerenteId.id eq gerente.id}">selected</c:if> >${gerente.nome}</option>
                                </c:forEach>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label for="setor">Empresa</label>
                        </td>
                        <td>
                            <select name="empresa">
                                <c:forEach items="${empresas}" var="empresa">
                                    <option value="${empresa.id}" <c:if test="${funcionario.empresaId.id eq empresa.id}">selected</c:if>  >${empresa.nome}</option>
                                </c:forEach>
                            </select>
                        </td>
                    </tr>

                </tbody>
            </table>
            <input type="hidden" name="id" value="${funcionario.id}" />
            <input type="submit" value="${acao}" />
        </form>

        <script>

        </script>
    </body>
</html>
