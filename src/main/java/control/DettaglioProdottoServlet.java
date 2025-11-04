package control;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.OffertaBean;
import model.VinoBean;
import model.VinoDAO;

@WebServlet("/dettaglio-prodotto")
public class DettaglioProdottoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private VinoDAO vinoDAO = new VinoDAO();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		// 1. Recupera l'ID del vino dalla query string
		String idVinoStr = request.getParameter("id");

		if (idVinoStr == null || idVinoStr.isEmpty()) {
			response.sendRedirect("home"); // ID mancante, torna alla home
			return;
		}
		
		try {
			int idVino = Integer.parseInt(idVinoStr);
			VinoBean vino = vinoDAO.doRetrieveByKey(idVino);
			List<OffertaBean> offerte = vinoDAO.doRetrieveOfferteByVino(idVino);

			if (vino == null || !vino.isInVendita()) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "Prodotto non trovato o non in vendita.");
				return;
			}
			request.setAttribute("vino", vino);
			request.setAttribute("offerteAttive", offerte);

		} catch (NumberFormatException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID Prodotto non valido.");
			return;
		} catch (SQLException e) {
			System.err.println("Errore SQL dettaglio prodotto: " + e.getMessage());
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore database.");
			return;
		}

		// 5. Inoltra alla pagina JSP
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/dettaglio_prodotto.jsp");
		dispatcher.forward(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		doGet(request, response);
	}
}