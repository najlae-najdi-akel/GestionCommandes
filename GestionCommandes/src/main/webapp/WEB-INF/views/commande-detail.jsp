<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Détail commande</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 30px; }
        table { border-collapse: collapse; width: 100%; margin-top: 15px; }
        th, td { border: 1px solid #ccc; padding: 8px; text-align: left; }
        th { background: #f0f0f0; }
        .ok { background: #dff0d8; padding: 10px; margin: 10px 0; }
        .btn { padding: 5px 10px; text-decoration: none; border: 1px solid #888;
               background: #fafafa; color: #000; cursor: pointer; }
    </style>
</head>
<body>
    <h1>Commande n° ${commande.id}</h1>
    <p><a href="${pageContext.request.contextPath}/commandes">&larr; Retour aux commandes</a></p>

    <c:if test="${not empty message}"><div class="ok"><c:out value="${message}"/></div></c:if>

    <p><strong>Client :</strong> <c:out value="${commande.clientNom}"/></p>
    <p><strong>Date :</strong> <fmt:formatDate value="${commande.dateCommande}" pattern="dd/MM/yyyy HH:mm"/></p>
    <p><strong>Statut :</strong> <c:out value="${commande.statut}"/></p>

    <form method="post" action="${pageContext.request.contextPath}/commandes">
        <input type="hidden" name="action" value="statut">
        <input type="hidden" name="id" value="${commande.id}">
        <select name="statut">
            <option>En attente</option>
            <option>Validée</option>
            <option>Expédiée</option>
            <option>Livrée</option>
            <option>Annulée</option>
        </select>
        <button class="btn" type="submit">Changer le statut</button>
    </form>

    <table>
        <tr><th>Produit</th><th>Prix unitaire</th><th>Quantité</th><th>Sous-total</th></tr>
        <c:set var="total" value="0"/>
        <c:forEach var="l" items="${commande.lignes}">
            <tr>
                <td><c:out value="${l.produitNom}"/></td>
                <td>${l.prixUnitaire}</td>
                <td>${l.quantite}</td>
                <td>${l.sousTotal}</td>
            </tr>
            <c:set var="total" value="${total + l.sousTotal}"/>
        </c:forEach>
        <tr><th colspan="3">Total</th><th>${total}</th></tr>
    </table>
</body>
</html>