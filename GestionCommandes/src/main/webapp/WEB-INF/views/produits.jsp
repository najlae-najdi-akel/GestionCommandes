<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Produits</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 30px; }
        table { border-collapse: collapse; width: 100%; margin-top: 15px; }
        th, td { border: 1px solid #ccc; padding: 8px; text-align: left; }
        th { background: #f0f0f0; }
        .ok { background: #dff0d8; padding: 10px; margin: 10px 0; }
        .ko { background: #f2dede; padding: 10px; margin: 10px 0; }
        .btn { padding: 5px 10px; text-decoration: none; border: 1px solid #888;
               background: #fafafa; color: #000; cursor: pointer; }
        form.inline { display: inline; }
    </style>
</head>
<body>
    <h1>Gestion des produits</h1>
    <p><a href="${pageContext.request.contextPath}/index.jsp">Accueil</a></p>

    <c:if test="${not empty message}"><div class="ok"><c:out value="${message}"/></div></c:if>
    <c:if test="${not empty erreur}"><div class="ko"><c:out value="${erreur}"/></div></c:if>

    <a class="btn" href="${pageContext.request.contextPath}/produits?action=nouveau">+ Nouveau produit</a>

    <table>
        <tr>
            <th>ID</th><th>Nom</th><th>Description</th><th>Prix</th><th>Stock</th><th>Actions</th>
        </tr>
        <c:forEach var="p" items="${produits}">
            <tr>
                <td>${p.id}</td>
                <td><c:out value="${p.nom}"/></td>
                <td><c:out value="${p.description}"/></td>
                <td>${p.prix}</td>
                <td>${p.stock}</td>
                <td>
                    <a class="btn" href="${pageContext.request.contextPath}/produits?action=modifier&id=${p.id}">Modifier</a>
                    <form class="inline" method="post" action="${pageContext.request.contextPath}/produits"
                          onsubmit="return confirm('Supprimer ce produit ?');">
                        <input type="hidden" name="action" value="supprimer">
                        <input type="hidden" name="id" value="${p.id}">
                        <button class="btn" type="submit">Supprimer</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty produits}">
            <tr><td colspan="6">Aucun produit pour le moment.</td></tr>
        </c:if>
    </table>
</body>
</html>