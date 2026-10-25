<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Nouvelle commande</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 30px; }
        table { border-collapse: collapse; margin-top: 15px; }
        th, td { border: 1px solid #ccc; padding: 8px; text-align: left; }
        th { background: #f0f0f0; }
        select, input[type=number] { padding: 6px; }
        input[type=number] { width: 80px; }
        .ko { background: #f2dede; padding: 10px; margin: 10px 0; max-width: 500px; }
        .btn { padding: 6px 12px; text-decoration: none; border: 1px solid #888;
               background: #fafafa; color: #000; cursor: pointer; }
    </style>
</head>
<body>
    <h1>Nouvelle commande</h1>

    <c:if test="${not empty erreur}"><div class="ko"><c:out value="${erreur}"/></div></c:if>

    <form method="post" action="${pageContext.request.contextPath}/commandes">
        <input type="hidden" name="action" value="creer">

        <p>
            <label>Client *
                <select name="clientId" required>
                    <option value="">-- Choisir un client --</option>
                    <c:forEach var="cl" items="${clients}">
                        <option value="${cl.id}"><c:out value="${cl.nom}"/></option>
                    </c:forEach>
                </select>
            </label>
        </p>

        <p>Indique la quantité des produits à commander (laisse 0 pour les autres) :</p>
        <table>
            <tr><th>Produit</th><th>Prix</th><th>Stock</th><th>Quantité</th></tr>
            <c:forEach var="p" items="${produits}">
                <tr>
                    <td><c:out value="${p.nom}"/></td>
                    <td>${p.prix}</td>
                    <td>${p.stock}</td>
                    <td><input type="number" name="quantite_${p.id}" min="0" value="0"></td>
                </tr>
            </c:forEach>
        </table>

        <p>
            <button class="btn" type="submit">Valider la commande</button>
            <a class="btn" href="${pageContext.request.contextPath}/commandes">Annuler</a>
        </p>
    </form>
</body>
</html>