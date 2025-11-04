package control;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.OrdineBean;
import model.OrdineDAO;
import model.UtenteBean;
import model.UtenteDAO;

// Mappata su /admin/dashboard (protetta dal filtro)
@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private OrdineDAO ordineDAO = new OrdineDAO();
	private UtenteDAO utenteDAO = new UtenteDAO();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		try {
			// 1. Carica gli ordini "In Attesa" (come da requisito)
			List<String> statiAttesa = Arrays.asList("in attesa", "in preparazione");
			List<OrdineBean> ordiniInAttesa = ordineDAO.doRetrieveByStati(statiAttesa);
			
			// 2. Carica gli ordini "Completi" (come da requisito)
			List<String> statiCompleti = Arrays.asList("consegnato", "annullato");
			List<OrdineBean> ordiniCompleti = ordineDAO.doRetrieveByStati(statiCompleti);

			// 3. Carica TUTTI gli utenti (per mappare ID -> Nome Utente)
			Collection<UtenteBean> tuttiGliUtenti = utenteDAO.doRetrieveAll(null);
			
			// Convertiamo la lista di utenti in una Mappa per un accesso facile nella JSP
			Map<Integer, UtenteBean> mappaUtenti = tuttiGliUtenti.stream()
					.collect(Collectors.toMap(UtenteBean::getIdUtente, utente -> utente));

			// 4. Salva tutto nella request
			request.setAttribute("ordiniInAttesa", ordiniInAttesa);
			request.setAttribute("ordiniCompleti", ordiniCompleti);
			request.setAttribute("mappaUtenti", mappaUtenti);

		} catch (SQLException e) {
			System.err.println("Errore SQL caricamento Dashboard Admin: " + e.getMessage());
			request.getSession().setAttribute("feedbackErrore", "Errore nel caricamento dei dati.");
		}
		
		// 5. Inoltra alla pagina "comune"
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin_dashboard.jsp");
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		String action = request.getParameter("action");
		
		// Gestore per l'aggiornamento dello stato di un ordine
		if (action != null && action.equals("updateStato")) {
			try {
				int idOrdine = Integer.parseInt(request.getParameter("idOrdine"));
				String nuovoStato = request.getParameter("nuovoStato");
				
				// Controlla che lo stato sia valido (per sicurezza)
				if (nuovoStato.equals("consegnato") || nuovoStato.equals("annullato") || nuovoStato.equals("in preparazione")) {
					ordineDAO.doUpdateStato(idOrdine, nuovoStato);
					request.getSession().setAttribute("feedbackSuccesso", "Stato dell'ordine #" + idOrdine + " aggiornato a " + nuovoStato + ".");
				} else {
					request.getSession().setAttribute("feedbackErrore", "Stato non valido.");
				}

			} catch (SQLException | NumberFormatException e) {
				System.err.println("Errore Aggiornamento Stato Ordine: " + e.getMessage());
				request.getSession().setAttribute("feedbackErrore", "Errore durante l'aggiornamento.");
			}
		}
		
		// Dopo ogni POST, ricarica la dashboard
		response.sendRedirect("dashboard");
	}
}