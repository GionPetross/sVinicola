package control;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.UtenteBean;
@WebFilter("/admin/*")
public class FiltroAdminServlet implements Filter {

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false); // Non crea una nuova sessione

        boolean isAdmin = false;

        // Check dalla session se admin o no
        if (session != null) {
            UtenteBean utente = (UtenteBean) session.getAttribute("utente");
            if (utente != null && utente.getRuolo().equals("admin")) {
                isAdmin = true;
            }
        }

        if (isAdmin) {
            chain.doFilter(request, response); //Fai passare solo se admin
        } else {
            if(session != null) {
            	session.setAttribute("feedbackErrore", "Accesso negato. L'area richiesta è riservata agli amministratori.");
            }
            // Reindirizza alla homepage
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/home");
        }
    }
}