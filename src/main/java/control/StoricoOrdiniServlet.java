package control;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.DettaglioOrdineBean;
import model.OrdineBean;
import model.OrdineDAO;
import model.UtenteBean;
import model.VinoBean;
import model.VinoDAO;

@WebServlet("/storico-ordini")
public class StoricoOrdiniServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private OrdineDAO ordineDAO = new OrdineDAO();
	private VinoDAO vinoDAO = new VinoDAO();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		HttpSession session = request.getSession(false);
		
		// 1. Verifica Autenticazione (solo utenti loggati)
		if (session == null || session.getAttribute("utente") == null) {
			response.sendRedirect("login.jsp");
			return;
		}
		
		UtenteBean utente = (UtenteBean) session.getAttribute("utente");
		
		// Struttura dati per unire Ordine e Dettagli: Map<ID_Ordine, List<DettaglioOrdineBean>>
		Map<Integer, List<DettaglioOrdineBean>> dettagliOrdini = new HashMap<>();
		// Mappa per memorizzare i dati del Vino una volta sola: Map<ID_Vino, VinoBean>
		Map<Integer, VinoBean> cacheVini = new HashMap<>();
		
		List<OrdineBean> ordiniUtente;
		
		try {
			// 2. Recupera tutti gli ordini dell'utente
			ordiniUtente = ordineDAO.doRetrieveByUtente(utente.getIdUtente());

			// 3. Per ogni ordine, recupera i dettagli (articoli)
			for (OrdineBean ordine : ordiniUtente) {
				int idOrdine = ordine.getIdOrdine();
				
				// Recupera i dettagli dell'ordine specifico (DettaglioOrdineBean)
				List<DettaglioOrdineBean> dettagli = ordineDAO.doRetrieveDettagli(idOrdine);
				dettagliOrdini.put(idOrdine, dettagli);
				
				// Popola la cache dei vini (per non chiamare il DB ripetutamente nella JSP)
				for (DettaglioOrdineBean dettaglio : dettagli) {
					int idVino = dettaglio.getIdVino();
					if (!cacheVini.containsKey(idVino)) {
						VinoBean vino = vinoDAO.doRetrieveByKey(idVino);
						if (vino != null) {
							cacheVini.put(idVino, vino);
						}
					}
				}
			}

			// 4. Salva i dati nella request
			request.setAttribute("ordini", ordiniUtente);
			request.setAttribute("dettagliOrdini", dettagliOrdini);
			request.setAttribute("cacheVini", cacheVini);

		} catch (SQLException e) {
			System.err.println("Errore SQL durante il recupero storico ordini: " + e.getMessage());
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore database.");
			return;
		}

		// 5. Inoltra alla pagina JSP
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/storico_ordini.jsp");
		dispatcher.forward(request, response);
	}
}