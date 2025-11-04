package control;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.OffertaBean;
import model.OffertaDAO; // Assicurati che questo DAO esista

@WebServlet("/admin/aggiungi_offerta")
public class AggiungiOffertaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private OffertaDAO offertaDAO = new OffertaDAO();


	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin_aggiungi_offerta.jsp");
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		
		try {
			String dataInizioStr = request.getParameter("data_inizio");
			String dataFineStr = request.getParameter("data_fine");
			int percentuale = Integer.parseInt(request.getParameter("percentuale"));
			String immaginePromo = request.getParameter("immagine_promozionale");
			
			Date dataInizio = Date.valueOf(dataInizioStr);
			Date dataFine = Date.valueOf(dataFineStr);

			if (dataFine.before(dataInizio)) {
				session.setAttribute("feedbackErrore", "Errore: La data di fine non può essere precedente alla data di inizio.");
				response.sendRedirect(request.getContextPath() + "/admin/aggiungi_offerta");
				return;
			}
			
			OffertaBean offerta = new OffertaBean();
			offerta.setDataInizio(dataInizio);
			offerta.setDataFine(dataFine);
			offerta.setPercentuale(percentuale);
			offerta.setImmaginePromozionale(immaginePromo);
			offertaDAO.doSave(offerta); 
			
			session.setAttribute("feedbackSuccesso", "Nuova offerta del " + percentuale + "% creata con successo.");

		} catch (SQLException e) {
			System.err.println("Errore SQL AggiungiOfferta: " + e.getMessage());
			session.setAttribute("feedbackErrore", "Errore del database durante il salvataggio.");
		} catch (Exception e) {
			System.err.println("Errore Formato AggiungiOfferta: " + e.getMessage());
			session.setAttribute("feedbackErrore", "Errore nei dati: Assicurati che percentuale e date siano nel formato corretto.");
		}
		
		response.sendRedirect(request.getContextPath() + "/admin/dashboard");
	}

}