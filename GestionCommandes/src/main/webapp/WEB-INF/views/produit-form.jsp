<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Formulaire produit</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 30px; }
        label { display: block; margin-top: 12px; }
        input[type=text], input[type=number], textarea { width: 300px; padding: 6px; }
        .ko { background: #f2dede; padding: 10px; margin: 10px 0; max-width: 400px; }
        .btn { padding: 6px 12px; text-decoration: none; border: 1px solid #888;
               background: #fafafa; color: #000; cursor: pointer; }
    </style>
</head>
<body>
    <h1>
        <c:choose>
            <c:when test="${produit.id == 0}">Nouveau produit</c:when>
            <c:otherwise>Modifier le produit</c:otherwise>
        </c:choose>
    </h1>

    <c:if test="${not empty erreur}"><div class="ko"><c:out value="${erreur}"/></div></c:if>

    <form method="post" action="${pageContext.request.contextPath}/produits">
        <input type="hidden" name="action" value="enregistrer">
        <input type="hidden" name="id" value="${produit.id}">

        <label>Nom *
            <input type="text" name="nom" value="<c:out value='${produit.nom}'/>" required>
        </label>
        <label>Description
            <textarea name="description" rows="3"><c:out value="${produit.description}"/></textarea>
        </label>
        <label>Prix *
            <input type="number" name="prix" step="0.01" min="0" value="${produit.prix}" required>
        </label>
        <label>Stock *
            <input type="number" name="stock" min="0" value="${produit.stock}" required>
        </label>

        <p>
            <button class="btn" type="submit">Enregistrer</button>
            <a class="btn" href="${pageContext.request.contextPath}/produits">Annuler</a>
        </p>
    </form>
</body>
</html>