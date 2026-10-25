package ma.fstt.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ma.fstt.entities.Client;
import ma.fstt.service.ClientService;

@WebServlet("/clients")
public class ClientServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final ClientService service = new ClientService();

    // GET : affichage (liste, formulaire d'ajout, formulaire de modification)
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            if ("nouveau".equals(action)) {
                req.setAttribute("client", new Client());
                req.getRequestDispatcher("/WEB-INF/views/client-form.jsp").forward(req, resp);

            } else if ("modifier".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                Client c = service.trouverParId(id);
                if (c == null) {
                    resp.sendRedirect(req.getContextPath() + "/clients");
                    return;
                }
                req.setAttribute("client", c);
                req.getRequestDispatcher("/WEB-INF/views/client-form.jsp").forward(req, resp);

            } else {
                // Messages "flash" stockés en session par le POST précédent
                HttpSession session = req.getSession();
                req.setAttribute("message", session.getAttribute("message"));
                req.setAttribute("erreur", session.getAttribute("erreur"));
                session.removeAttribute("message");
                session.removeAttribute("erreur");

                req.setAttribute("clients", service.listerTous());
                req.getRequestDispatcher("/WEB-INF/views/clients.jsp").forward(req, resp);
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    // POST : enregistrement et suppression
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        HttpSession session = req.getSession();

        try {
            if ("supprimer".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                try {
                    service.supprimer(id);
                    session.setAttribute("message", "Client supprimé.");
                } catch (SQLException e) {
                    // Probablement un client qui a déjà des commandes (clé étrangère)
                    session.setAttribute("erreur",
                            "Impossible de supprimer ce client : il a des commandes.");
                }
                resp.sendRedirect(req.getContextPath() + "/clients");

            } else { // enregistrer (ajout ou modification)
                String idStr = req.getParameter("id");
                Client c = new Client();
                c.setId(idStr == null || idStr.isEmpty() ? 0 : Integer.parseInt(idStr));
                c.setNom(req.getParameter("nom"));
                c.setEmail(req.getParameter("email"));
                c.setTelephone(req.getParameter("telephone"));

                try {
                    if (c.getId() == 0) {
                        service.ajouter(c);
                        session.setAttribute("message", "Client ajouté.");
                    } else {
                        service.modifier(c);
                        session.setAttribute("message", "Client modifié.");
                    }
                    resp.sendRedirect(req.getContextPath() + "/clients");

                } catch (IllegalArgumentException e) {
                    // Données invalides : on réaffiche le formulaire avec le message
                    req.setAttribute("erreur", e.getMessage());
                    req.setAttribute("client", c);
                    req.getRequestDispatcher("/WEB-INF/views/client-form.jsp").forward(req, resp);
                }
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
