package ma.fstt.servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ma.fstt.entities.Produit;
import ma.fstt.service.ProduitService;

@WebServlet("/produits")
public class ProduitServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final ProduitService service = new ProduitService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            if ("nouveau".equals(action)) {
                req.setAttribute("produit", new Produit());
                req.getRequestDispatcher("/WEB-INF/views/produit-form.jsp").forward(req, resp);

            } else if ("modifier".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                Produit p = service.trouverParId(id);
                if (p == null) {
                    resp.sendRedirect(req.getContextPath() + "/produits");
                    return;
                }
                req.setAttribute("produit", p);
                req.getRequestDispatcher("/WEB-INF/views/produit-form.jsp").forward(req, resp);

            } else {
                HttpSession session = req.getSession();
                req.setAttribute("message", session.getAttribute("message"));
                req.setAttribute("erreur", session.getAttribute("erreur"));
                session.removeAttribute("message");
                session.removeAttribute("erreur");

                req.setAttribute("produits", service.listerTous());
                req.getRequestDispatcher("/WEB-INF/views/produits.jsp").forward(req, resp);
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
            if ("supprimer".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                try {
                    service.supprimer(id);
                    session.setAttribute("message", "Produit supprimé.");
                } catch (SQLException e) {
                    session.setAttribute("erreur",
                            "Impossible de supprimer ce produit : il figure dans des commandes.");
                }
                resp.sendRedirect(req.getContextPath() + "/produits");

            } else {
                String idStr = req.getParameter("id");
                Produit p = new Produit();
                p.setId(idStr == null || idStr.isEmpty() ? 0 : Integer.parseInt(idStr));
                p.setNom(req.getParameter("nom"));
                p.setDescription(req.getParameter("description"));

                try {
                    p.setPrix(new BigDecimal(req.getParameter("prix").trim().replace(',', '.')));
                    p.setStock(Integer.parseInt(req.getParameter("stock").trim()));

                    if (p.getId() == 0) {
                        service.ajouter(p);
                        session.setAttribute("message", "Produit ajouté.");
                    } else {
                        service.modifier(p);
                        session.setAttribute("message", "Produit modifié.");
                    }
                    resp.sendRedirect(req.getContextPath() + "/produits");

                } catch (NumberFormatException e) {
                    req.setAttribute("erreur", "Le prix et le stock doivent être des nombres valides.");
                    req.setAttribute("produit", p);
                    req.getRequestDispatcher("/WEB-INF/views/produit-form.jsp").forward(req, resp);
                } catch (IllegalArgumentException e) {
                    req.setAttribute("erreur", e.getMessage());
                    req.setAttribute("produit", p);
                    req.getRequestDispatcher("/WEB-INF/views/produit-form.jsp").forward(req, resp);
                }
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}