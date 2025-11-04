package control;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Collection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.VinoBean;
import model.VinoDAO;

@WebServlet("/admin/modifica_vino")
public class ModificaVinoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private VinoDAO vinoDAO = new VinoDAO();
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		String action = request.getParameter("action");
		
		try {
			if (action != null) {
				int idVino = Integer.parseInt(request.getParameter("id"));
				
				if (action.equals("delete")) {
					//Toglie vendita
					vinoDAO.doDelete(idVino); // Imposta In_Vendita = false
					session.setAttribute("feedbackSuccesso", "Prodotto (ID: " + idVino + ") disattivato.");
				
				} else if (action.equals("restore")) {
					//Mette in vendita
					VinoBean vino = vinoDAO.doRetrieveByKey(idVino); // Recupera il vino
					if (vino != null) {
						vino.setInVendita(true);
						vinoDAO.doUpdate(vino);
						session.setAttribute("feedbackSuccesso", "Prodotto (ID: " + idVino + ") riattivato nel catalogo.");
					}
				}
			}

			Collection<VinoBean> catalogoCompleto = vinoDAO.doRetrieveAllAdmin("In_Vendita DESC");
			request.setAttribute("catalogoCompleto", catalogoCompleto);

		} catch (SQLException e) {
			System.err.println("Errore SQL ModificaVino (GET): " + e.getMessage());
			session.setAttribute("feedbackErrore", "Errore database: " + e.getMessage());
		} catch (NumberFormatException e) {
			System.err.println("Errore ID ModificaVino (GET): " + e.getMessage());
			session.setAttribute("feedbackErrore", "ID prodotto non valido.");
		}

		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin_modifica_vino.jsp");
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		
		try {
			VinoBean vino = new VinoBean();
			
			vino.setIdVino(Integer.parseInt(request.getParameter("idVino")));
			
			vino.setNome(request.getParameter("nome"));
			vino.setAnnata(Integer.parseInt(request.getParameter("annata")));
			vino.setTipo(request.getParameter("tipo"));
			vino.setDescrizione(request.getParameter("descrizione"));
			vino.setPercentualeAlcolica(Double.parseDouble(request.getParameter("alcol")));
			vino.setImmagine(request.getParameter("immagine"));
			vino.setPrezzo(new BigDecimal(request.getParameter("prezzo")));
			vino.setStock(Integer.parseInt(request.getParameter("stock")));
			vino.setFormato(request.getParameter("formato"));
			vino.setOrigine(request.getParameter("origine"));
			vino.setInVendita(request.getParameter("in_vendita") != null);

			vinoDAO.doUpdate(vino);
			
			session.setAttribute("feedbackSuccesso", "Prodotto (ID: " + vino.getIdVino() + ") aggiornato con successo.");

		} catch (SQLException e) {
			System.err.println("Errore SQL ModificaVino (POST): " + e.getMessage());
			session.setAttribute("feedbackErrore", "Errore database durante l'aggiornamento.");
		} catch (NumberFormatException e) {
			System.err.println("Errore Dati ModificaVino (POST): " + e.getMessage());
			session.setAttribute("feedbackErrore", "Errore nei dati: assicurati che ID, Prezzo, Stock, Annata e Alcol siano numeri validi.");
		}
		
		response.sendRedirect(request.getContextPath() + "/admin/modifica_vino");
	}
}