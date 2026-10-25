package ma.fstt.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ma.fstt.entities.Commande;
import ma.fstt.entities.LigneDeCommande;
import ma.fstt.service.ClientService;
import ma.fstt.service.CommandeService;
import ma.fstt.service.ProduitService;

@WebServlet("/commandes")
public class CommandeServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final CommandeService commandeService = new CommandeService();
    private final ClientService clientService = new ClientService();
    private final ProduitService produitService = new ProduitService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            if ("nouvelle".equals(action)) {
                chargerFormulaire(req);
                req.getRequestDispatcher("/WEB-INF/views/commande-form.jsp").forward(req, resp);

            } else if ("detail".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                Commande c = commandeService.trouverParId(id);
                if (c == null) {
                    resp.sendRedirect(req.getContextPath() + "/commandes");
                    return;
                }
                HttpSession session = req.getSession();
                req.setAttribute("message", session.getAttribute("message"));
                session.removeAttribute("message");
                req.setAttribute("commande", c);
                req.getRequestDispatcher("/WEB-INF/views/commande-detail.jsp").forward(req, resp);

            } else {
                HttpSession session = req.getSession();
                req.setAttribute("message", session.getAttribute("message"));
                req.setAttribute("erreur", session.getAttribute("erreur"));
                session.removeAttribute("message");
                session.removeAttribute("erreur");

                req.setAttribute("commandes", commandeService.listerToutes());
                req.getRequestDispatcher("/WEB-INF/views/commandes.jsp").forward(req, resp);
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        HttpSession session = req.getSession();

        try {
            if ("statut".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                commandeService.modifierStatut(id, req.getParameter("statut"));
                session.setAttribute("message", "Statut mis à jour.");
                resp.sendRedirect(req.getContextPath() + "/commandes?action=detail&id=" + id);

            } else { // création
                try {
                    int clientId = Integer.parseInt(req.getParameter("clientId"));
                    Commande commande = new Commande(clientId, "En attente");

                    // Les champs quantite_<idProduit> : une quantité > 0 = produit commandé
                    for (var p : produitService.listerTous()) {
                        String q = req.getParameter("quantite_" + p.getId());
                        if (q != null && !q.trim().isEmpty()) {
                            int quantite = Integer.parseInt(q.trim());
                            if (quantite > 0) {
                                commande.getLignes().add(new LigneDeCommande(p.getId(), quantite, p.getPrix()));
                            }
                        }
                    }

                    commandeService.creer(commande);
                    session.setAttribute("message", "Commande n° " + commande.getId() + " créée.");
                    resp.sendRedirect(req.getContextPath() + "/commandes?action=detail&id=" + commande.getId());

                } catch (NumberFormatException e) {
                    erreurFormulaire(req, resp, "Client ou quantité invalide.");
                } catch (IllegalArgumentException e) {
                    erreurFormulaire(req, resp, e.getMessage());
                } catch (SQLException e) {
                    // Stock insuffisant, produit introuvable, etc.
                    erreurFormulaire(req, resp, e.getMessage());
                }
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private void chargerFormulaire(HttpServletRequest req) throws SQLException {
        req.setAttribute("clients", clientService.listerTous());
        req.setAttribute("produits", produitService.listerTous());
    }

    private void erreurFormulaire(HttpServletRequest req, HttpServletResponse resp, String message)
            throws ServletException, IOException {
        try {
            chargerFormulaire(req);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
        req.setAttribute("erreur", message);
        req.getRequestDispatcher("/WEB-INF/views/commande-form.jsp").forward(req, resp);
    }
}