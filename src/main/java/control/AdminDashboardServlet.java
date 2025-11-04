package control;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.OrdineBean;
import model.OrdineDAO;
import model.UtenteBean;
import model.UtenteDAO;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private OrdineDAO ordineDAO = new OrdineDAO();
	private UtenteDAO utenteDAO = new UtenteDAO();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		HttpSession session = request.getSession(); 
		
		try {
			// Carica gli ordini "In Attesa"
			List<String> statiAttesa = Arrays.asList("in attesa"); // Corretto: solo "in attesa"
			List<OrdineBean> ordiniInAttesa = ordineDAO.doRetrieveByStati(statiAttesa);
			
			// Carica il Resto
			List<String> statiCompleti = Arrays.asList("consegnato", "annullato");
			List<OrdineBean> ordiniCompleti = ordineDAO.doRetrieveByStati(statiCompleti);

			// Carica TUTTI gli utenti
			Collection<UtenteBean> tuttiGliUtenti = utenteDAO.doRetrieveAll(null);
			
			Map<Integer, UtenteBean> mappaUtenti = tuttiGliUtenti.stream()
					.collect(Collectors.toMap(UtenteBean::getIdUtente, utente -> utente));

			// Salva tutto nella request
			request.setAttribute("ordiniInAttesa", ordiniInAttesa);
			request.setAttribute("ordiniCompleti", ordiniCompleti);
			request.setAttribute("mappaUtenti", mappaUtenti);

		} catch (SQLException e) {
			System.err.println("Errore SQL caricamento Dashboard Admin: " + e.getMessage());
			session.setAttribute("feedbackErrore", "Errore nel caricamento dei dati.");
		}
		
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/admin_dashboard.jsp");
		dispatcher.forward(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		
		Enumeration<String> parametri = request.getParameterNames();
		
		try {
			boolean almenoUnAggiornamento = false;
			
			while (parametri.hasMoreElements()) {
				String paramName = parametri.nextElement();
				
				if (paramName.startsWith("stato-")) {
					
					try {
						int idOrdine = Integer.parseInt(paramName.substring(6)); 
						String nuovoStato = request.getParameter(paramName);
						

						if (nuovoStato.equals("consegnato") || nuovoStato.equals("annullato")) {
							ordineDAO.doUpdateStato(idOrdine, nuovoStato);
							almenoUnAggiornamento = true;
						} else if (!nuovoStato.equals("in attesa")) {
							session.setAttribute("feedbackErrore", "Stato non valido rilevato: " + nuovoStato);
						}

					} catch (NumberFormatException e) {
						System.err.println("Ignorato parametro malformato: " + paramName);
					}
				}
			}
			
			if (almenoUnAggiornamento) {
				session.setAttribute("feedbackSuccesso", "Stati degli ordini aggiornati con successo.");
			}

		} catch (SQLException e) {
			System.err.println("Errore Aggiornamento Stato Ordine: " + e.getMessage());
			session.setAttribute("feedbackErrore", "Errore durante l'aggiornamento.");
		}
		
		response.sendRedirect(request.getContextPath() + "/admin/dashboard");
	}
}