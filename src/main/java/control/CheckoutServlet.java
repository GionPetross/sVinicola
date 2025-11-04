package control;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Carrello;
import model.DettaglioOrdineBean;
import model.IndirizzoBean;
import model.IndirizzoDAO;
import model.OrdineBean;
import model.OrdineDAO;
import model.UtenteBean;
import model.VoceCarrello;
import model.VinoBean;
import model.VinoDAO;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private IndirizzoDAO indirizzoDAO = new IndirizzoDAO();	//indirizzo spedizione
	private OrdineDAO ordineDAO = new OrdineDAO();			//ordine da salvare
	private VinoDAO vinoDAO = new VinoDAO();				//cambia lo stock

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		HttpSession session = request.getSession(false);
		
		if (session == null || session.getAttribute("utente") == null) {
			response.sendRedirect("login.jsp");
			return;
		}
		
		UtenteBean utente = (UtenteBean) session.getAttribute("utente");
		Carrello carrello = (Carrello) session.getAttribute("carrello");
		
		if (carrello == null || carrello.isEmpty()) {
			response.sendRedirect("carrello");
			return;
		}

		try {
			List<IndirizzoBean> indirizzi = indirizzoDAO.doRetrieveByUtente(utente.getIdUtente());
			
			if (indirizzi == null || indirizzi.isEmpty()) {
				session.setAttribute("feedbackErrore", "Per procedere al checkout devi prima salvare almeno un indirizzo di spedizione.");
				response.sendRedirect("area-personale");
				return;
			}
			
			request.setAttribute("indirizziUtente", indirizzi);

		} catch (SQLException e) {
			System.err.println("Errore SQL recupero indirizzi per checkout: " + e.getMessage());
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore database.");
			return;
		}
		
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/checkout.jsp");
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		HttpSession session = request.getSession(false);
		
		if (session == null || session.getAttribute("utente") == null) {
			response.sendRedirect("login.jsp");
			return;
		}
		
		UtenteBean utente = (UtenteBean) session.getAttribute("utente");
		Carrello carrello = (Carrello) session.getAttribute("carrello");
		
		if (carrello == null || carrello.isEmpty()) {
			response.sendRedirect("carrello");
			return;
		}

		try {
			int idIndirizzoScelto = Integer.parseInt(request.getParameter("idIndirizzo"));
			IndirizzoBean indirizzoScelto = indirizzoDAO.doRetrieveByKey(idIndirizzoScelto);

			if (indirizzoScelto == null || indirizzoScelto.getIdUtente() != utente.getIdUtente()) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Indirizzo non valido.");
				return;
			}
			
			// === VERIFICA E PREPARAZIONE STOCK ===
			Collection<VoceCarrello> voci = carrello.getVoci();
			for (VoceCarrello voce : voci) {
				VinoBean vinoDb = vinoDAO.doRetrieveByKey(voce.getVino().getIdVino());
				if (vinoDb == null || vinoDb.getStock() < voce.getQuantita()) {
					request.setAttribute("messaggioErrore", "Stock insufficiente per il prodotto " + voce.getVino().getNome() + ".");
					doGet(request, response); // Torna al checkout se il numero supera lo stock
					return;
				}
			}
			// =======================================
			
			OrdineBean nuovoOrdine = new OrdineBean();
			nuovoOrdine.setIdUtente(utente.getIdUtente());
			nuovoOrdine.setTotaleComplessivo(carrello.getTotale());
			nuovoOrdine.setViaSpedizione(indirizzoScelto.getVia());
			nuovoOrdine.setCapSpedizione(indirizzoScelto.getCap());
			nuovoOrdine.setCittaSpedizione(indirizzoScelto.getCitta());
			nuovoOrdine.setProvinciaSpedizione(indirizzoScelto.getProvincia());
			nuovoOrdine.setStato("in attesa");
			
			Collection<DettaglioOrdineBean> dettagli = carrello.getDettagliOrdine();
			int idOrdineGenerato = ordineDAO.doSaveCompleto(nuovoOrdine, dettagli);
			
			// === DEDUZIONE DELLO STOCK ===
			for (VoceCarrello voce : voci) {
				VinoBean vinoDb = vinoDAO.doRetrieveByKey(voce.getVino().getIdVino());
				int nuovoStock = vinoDb.getStock() - voce.getQuantita();
				vinoDb.setStock(nuovoStock);
				if (nuovoStock <= 0) {
					vinoDb.setInVendita(false); //Smettila di vendere se è finito lo stock
				}
				vinoDAO.doUpdate(vinoDb); // Aggiorna lo stock nel DB
			}

			// Svuota il carrello e la sessione
			carrello.svuota();
			session.setAttribute("carrello", carrello); 
			
			// Reindirizza alla pagina di conferma
			session.setAttribute("idOrdineConfermato", idOrdineGenerato);
			response.sendRedirect("ordine-confermato");

		} catch (SQLException e) {
			System.err.println("Errore SQL durante la finalizzazione dell'ordine: " + e.getMessage());
			request.setAttribute("messaggioErrore", "Errore critico durante il salvataggio dell'ordine.");
			doGet(request, response); 
		} catch (NumberFormatException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID Indirizzo mancante o non valido.");
		}
	}
}