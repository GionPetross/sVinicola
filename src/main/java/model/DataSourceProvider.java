package model;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DataSourceProvider {

	private static DataSource ds;

	static {
		try {
			Context initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");

			// Esegue il lookup del DataSource configurato in context.xml
			ds = (DataSource) envCtx.lookup("jdbc/svinicola");

		} catch (NamingException e) {
			System.err.println("Errore JNDI durante il lookup del DataSource: " + e.getMessage());
			// Lancia un'eccezione per bloccare l'avvio se il DS non è trovato
			throw new RuntimeException("Impossibile trovare il DataSource JNDI", e);
		}
	}

	public static Connection getConnection() throws SQLException {
		// Il DataSource gestisce il pool (efficienza, validazione, ecc.)
		return ds.getConnection();
	}
	
	// ricorda di chiamare 'connection.close()' nel blocco finally del DAO.
}