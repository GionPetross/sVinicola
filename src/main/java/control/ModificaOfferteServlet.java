package control;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.OffertaBean;
import model.OffertaDAO;
import model.VinoBean;
import model.VinoDAO;

@WebServlet("/admin/modifica_offerte")
public class ModificaOfferteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private OffertaDAO offertaDAO = new OffertaDAO();
	private VinoDAO vinoDAO = new VinoDAO();
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		String action = request.getParameter("action");
		String idOffertaStr = request.getParameter("id");

		try {
			if ("modifica".equals(action) && idOffertaStr != null) {
				int idOfferta = Integer.parseInt(idOffertaStr);
				
				OffertaBean offerta = offertaDAO.doRetrieveByKey(idOfferta);
				
				Collection<VinoBean> tuttiIvini = vinoDAO.doRetrieveAll(null);
				List<VinoBean> viniAssociati = offertaDAO.doRetrieveViniByOfferta(idOfferta);
				List<Integer> idViniAssociati = viniAssociati.stream()
														 .map(VinoBean::getIdVino)
														 .collect(Collectors.toList());
				
				request.setAttribute("offertaDaModificare", offerta);
				request.setAttribute("tuttiIvini", tuttiIvini);
				request.setAttribute("idViniAssociati", idViniAssociati);
				
			} else {
				Collection<OffertaBean> tutteLeOfferte = offertaDAO.doRetrieveAll("Data_Inizio DESC");
				request.setAttribute("tutteLeOfferte", tutteLeOfferte);
			}

		} catch (SQLException | NumberFormatException e) {
			System.err.println("Errore Admin Offerte (GET): " + e.getMessage());
			session.setAttribute("feedbackErrore", "Errore nel caricamento dei dati delle offerte.");
		}

		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin_modifica_offerta.jsp");
		dispatcher.forward(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		
		try {
			// Recupera i dati base dell'offerta
			int idOfferta = Integer.parseInt(request.getParameter("idOfferta"));
			String dataInizioStr = request.getParameter("data_inizio");
			String dataFineStr = request.getParameter("data_fine");
			int percentuale = Integer.parseInt(request.getParameter("percentuale"));
			String immaginePromo = request.getParameter("immagine_promozionale");
			
			// Recupera la lista dei vini selezionati
			String[] idViniSelezionati = request.getParameterValues("viniSelezionati");

			// Aggiorna i dati dell'offerta
			OffertaBean offerta = offertaDAO.doRetrieveByKey(idOfferta); // Carica l'offerta esistente
			offerta.setDataInizio(Date.valueOf(dataInizioStr));
			offerta.setDataFine(Date.valueOf(dataFineStr));
			offerta.setPercentuale(percentuale);
			offerta.setImmaginePromozionale(immaginePromo);
			
			offertaDAO.doUpdate(offerta); 

			//Quando è meglio rimuovere tutte le offere e reinserilre
			offertaDAO.doRemoveAllViniFromOfferta(idOfferta);
			if (idViniSelezionati != null) {
				for (String idVinoStr : idViniSelezionati) {
					int idVino = Integer.parseInt(idVinoStr);
					vinoDAO.doApplyOfferta(idVino, idOfferta);
				}
			}
			
			session.setAttribute("feedbackSuccesso", "Offerta #" + idOfferta + " aggiornata con successo.");

		} catch (SQLException | IllegalArgumentException e) {
			System.err.println("Errore Admin Offerte (POST): " + e.getMessage());
			session.setAttribute("feedbackErrore", "Errore durante il salvataggio: " + e.getMessage());
		}
		
		response.sendRedirect(request.getContextPath() + "/admin/modifica_offerte");
	}
}