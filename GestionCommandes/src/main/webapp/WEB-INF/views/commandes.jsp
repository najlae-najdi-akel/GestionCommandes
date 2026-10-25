<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Commandes</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 30px; }
        table { border-collapse: collapse; width: 100%; margin-top: 15px; }
        th, td { border: 1px solid #ccc; padding: 8px; text-align: left; }
        th { background: #f0f0f0; }
        .ok { background: #dff0d8; padding: 10px; margin: 10px 0; }
        .ko { background: #f2dede; padding: 10px; margin: 10px 0; }
        .btn { padding: 5px 10px; text-decoration: none; border: 1px solid #888;
               background: #fafafa; color: #000; cursor: pointer; }
    </style>
</head>
<body>
    <h1>Gestion des commandes</h1>
    <p><a href="${pageContext.request.contextPath}/index.jsp">Accueil</a></p>

    <c:if test="${not empty message}"><div class="ok"><c:out value="${message}"/></div></c:if>
    <c:if test="${not empty erreur}"><div class="ko"><c:out value="${erreur}"/></div></c:if>

    <a class="btn" href="${pageContext.request.contextPath}/commandes?action=nouvelle">+ Nouvelle commande</a>

    <table>
        <tr><th>N°</th><th>Date</th><th>Client</th><th>Statut</th><th>Actions</th></tr>
        <c:forEach var="c" items="${commandes}">
            <tr>
                <td>${c.id}</td>
                <td><fmt:formatDate value="${c.dateCommande}" pattern="dd/MM/yyyy HH:mm"/></td>
                <td><c:out value="${c.clientNom}"/></td>
                <td><c:out value="${c.statut}"/></td>
                <td><a class="btn" href="${pageContext.request.contextPath}/commandes?action=detail&id=${c.id}">Détail</a></td>
            </tr>
        </c:forEach>
        <c:if test="${empty commandes}">
            <tr><td colspan="5">Aucune commande pour le moment.</td></tr>
        </c:if>
    </table>
</body>
</html>