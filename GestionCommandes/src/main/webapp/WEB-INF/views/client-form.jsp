<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Formulaire client</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 30px; }
        label { display: block; margin-top: 12px; }
        input[type=text], input[type=email] { width: 300px; padding: 6px; }
        .ko { background: #f2dede; padding: 10px; margin: 10px 0; max-width: 400px; }
        .btn { padding: 6px 12px; text-decoration: none; border: 1px solid #888;
               background: #fafafa; color: #000; cursor: pointer; }
    </style>
</head>
<body>
    <h1>
        <c:choose>
            <c:when test="${client.id == 0}">Nouveau client</c:when>
            <c:otherwise>Modifier le client</c:otherwise>
        </c:choose>
    </h1>

    <c:if test="${not empty erreur}"><div class="ko"><c:out value="${erreur}"/></div></c:if>

    <form method="post" action="${pageContext.request.contextPath}/clients">
        <input type="hidden" name="action" value="enregistrer">
        <input type="hidden" name="id" value="${client.id}">

        <label>Nom *
            <input type="text" name="nom" value="<c:out value='${client.nom}'/>" required>
        </label>
        <label>Email *
            <input type="email" name="email" value="<c:out value='${client.email}'/>" required>
        </label>
        <label>Téléphone
            <input type="text" name="telephone" value="<c:out value='${client.telephone}'/>">
        </label>

        <p>
            <button class="btn" type="submit">Enregistrer</button>
            <a class="btn" href="${pageContext.request.contextPath}/clients">Annuler</a>
        </p>
    </form>
</body>
</html>