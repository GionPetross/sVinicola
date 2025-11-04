package model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class VinoDAO implements DAOinterface<VinoBean> {

	private static final String TABLE_NAME = "Vino";
	private static final String APPLICATO_TABLE = "Applicato";
	private static final String OFFERTA_TABLE = "Offerta";
	
	@Override
	public synchronized void doSave(VinoBean vino) throws SQLException {
		Connection connection = null;
		PreparedStatement ps = null;

		String sql = "INSERT INTO " + TABLE_NAME + 
					 " (Nome, Annata, Tipo, Descrizione, Percentuale_Alcolica, Immagine, Prezzo, Stock, Formato, Origine, In_Vendita) " +
					 " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try {
			connection = DataSourceProvider.getConnection();
			ps = connection.prepareStatement(sql);
			
			ps.setString(1, vino.getNome());
			ps.setInt(2, vino.getAnnata());
			ps.setString(3, vino.getTipo());
			ps.setString(4, vino.getDescrizione());
			ps.setDouble(5, vino.getPercentualeAlcolica());
			ps.setString(6, vino.getImmagine());
			ps.setBigDecimal(7, vino.getPrezzo());
			ps.setInt(8, vino.getStock());
			ps.setString(9, vino.getFormato());
			ps.setString(10, vino.getOrigine());
			ps.setBoolean(11, vino.isInVendita());

			ps.executeUpdate();
		} finally {
			try {
				if (ps != null) ps.close();
			} finally {
				if (connection != null) connection.close();
			}
		}
	}

	@Override
	public synchronized VinoBean doRetrieveByKey(int idVino) throws SQLException {
		Connection connection = null;
		PreparedStatement ps = null;
		VinoBean bean = new VinoBean();

		String sql = "SELECT V.*, "
				   + "MAX(O.Percentuale) AS ScontoMax, "
				   + "(V.Prezzo * (1 - (MAX(O.Percentuale) / 100.0))) AS PrezzoScontato "
				   + "FROM " + TABLE_NAME + " AS V "
				   + "LEFT JOIN " + APPLICATO_TABLE + " AS A ON V.ID_Vino = A.ID_Vino "
				   + "LEFT JOIN " + OFFERTA_TABLE + " AS O ON A.ID_Offerta = O.ID_Offerta "
				   + "AND CURDATE() BETWEEN O.Data_Inizio AND O.Data_Fine "
				   + "WHERE V.ID_Vino = ? "
				   + "GROUP BY V.ID_Vino";

		try {
			connection = DataSourceProvider.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idVino);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				bean.setIdVino(rs.getInt("ID_Vino"));
				bean.setNome(rs.getString("Nome"));
				bean.setAnnata(rs.getInt("Annata"));
				bean.setTipo(rs.getString("Tipo"));
				bean.setDescrizione(rs.getString("Descrizione"));
				bean.setPercentualeAlcolica(rs.getDouble("Percentuale_Alcolica"));
				bean.setImmagine(rs.getString("Immagine"));
				bean.setPrezzo(rs.getBigDecimal("Prezzo")); // Prezzo base
				bean.setStock(rs.getInt("Stock"));
				bean.setFormato(rs.getString("Formato"));
				bean.setOrigine(rs.getString("Origine"));
				bean.setInVendita(rs.getBoolean("In_Vendita"));
				bean.setDataAggiunta(rs.getTimestamp("Data_Aggiunta"));
				
				int percentualeSconto = rs.getInt("ScontoMax");
				BigDecimal prezzoCalcolato = rs.getBigDecimal("PrezzoScontato");

				if (!rs.wasNull() && percentualeSconto > 0) {
					bean.setPrezzo(prezzoCalcolato.setScale(2, RoundingMode.HALF_UP));
					bean.setPercentualeSconto(percentualeSconto);
					bean.setPrezzoScontato(prezzoCalcolato.setScale(2, RoundingMode.HALF_UP));
				} else {
					bean.setPercentualeSconto(0);
					bean.setPrezzoScontato(null);
				}
				
			} else {
				return null;
			}

		} finally {
			try {
				if (ps != null) ps.close();
			} finally {
				if (connection != null) connection.close();
			}
		}
		return bean;
	}
	
	@Override
	public synchronized boolean doDelete(int idVino) throws SQLException {
		Connection connection = null;
		PreparedStatement ps = null;
		int result = 0;
		String sql = "UPDATE " + TABLE_NAME + " SET In_Vendita = false WHERE ID_Vino = ?";

		try {
			connection = DataSourceProvider.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idVino);

			result = ps.executeUpdate();

		} finally {
			try {
				if (ps != null) ps.close();
			} finally {
				if (connection != null) connection.close();
			}
		}
		return (result != 0);
	}

	@Override
	public synchronized Collection<VinoBean> doRetrieveAll(String order) throws SQLException {
		Connection connection = null;
		PreparedStatement ps = null;
		Collection<VinoBean> vini = new ArrayList<>();

		String sql = "SELECT * FROM " + TABLE_NAME + " WHERE In_Vendita = true";
		
		if (order != null && !order.isEmpty()) {
			sql += " ORDER BY " + order;
		}

		try {
			connection = DataSourceProvider.getConnection();
			ps = connection.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				VinoBean bean = new VinoBean();
				bean.setIdVino(rs.getInt("ID_Vino"));
				bean.setNome(rs.getString("Nome"));
				bean.setAnnata(rs.getInt("Annata"));
				bean.setTipo(rs.getString("Tipo"));
				bean.setDescrizione(rs.getString("Descrizione"));
				bean.setPercentualeAlcolica(rs.getDouble("Percentuale_Alcolica"));
				bean.setImmagine(rs.getString("Immagine"));
				bean.setPrezzo(rs.getBigDecimal("Prezzo"));
				bean.setStock(rs.getInt("Stock"));
				bean.setFormato(rs.getString("Formato"));
				bean.setOrigine(rs.getString("Origine"));
				bean.setInVendita(rs.getBoolean("In_Vendita"));
				bean.setDataAggiunta(rs.getTimestamp("Data_Aggiunta"));
				vini.add(bean);
			}

		} finally {
			try {
				if (ps != null) ps.close();
			} finally {
				if (connection != null) connection.close();
			}
		}
		return vini;
	}


	@Override
	public synchronized void doUpdate(VinoBean vino) throws SQLException {
		Connection connection = null;
		PreparedStatement ps = null;

		String sql = "UPDATE " + TABLE_NAME + 
					 " SET Nome = ?, Annata = ?, Tipo = ?, Descrizione = ?, Percentuale_Alcolica = ?, Immagine = ?, " +
					 " Prezzo = ?, Stock = ?, Formato = ?, Origine = ?, In_Vendita = ? " +
					 " WHERE ID_Vino = ?";

		try {
			connection = DataSourceProvider.getConnection();
			ps = connection.prepareStatement(sql);
			
			ps.setString(1, vino.getNome());
			ps.setInt(2, vino.getAnnata());
			ps.setString(3, vino.getTipo());
			ps.setString(4, vino.getDescrizione());
			ps.setDouble(5, vino.getPercentualeAlcolica());
			ps.setString(6, vino.getImmagine());
			ps.setBigDecimal(7, vino.getPrezzo());
			ps.setInt(8, vino.getStock());
			ps.setString(9, vino.getFormato());
			ps.setString(10, vino.getOrigine());
			ps.setBoolean(11, vino.isInVendita());
			ps.setInt(12, vino.getIdVino());

			ps.executeUpdate();

		} finally {
			try {
				if (ps != null) ps.close();
			} finally {
				if (connection != null) connection.close();
			}
		}
	}

	// ==================================================================
	// 							METODI SPECIFICI
	// ==================================================================

	public synchronized Collection<VinoBean> doRetrieveByTipo(String tipo) throws SQLException {
		return new ArrayList<>();
	}

	// ==================================================================
	// 					METODI PER GESTIONE OFFERTE
	// ==================================================================

	
	public synchronized List<OffertaBean> doRetrieveOfferteByVino(int idVino) throws SQLException {
		Connection connection = null;
		PreparedStatement ps = null;
		List<OffertaBean> offerte = new ArrayList<>();

		String sql = "SELECT O.* FROM " + OFFERTA_TABLE + " O JOIN " + APPLICATO_TABLE + " A " +
					 "ON O.ID_Offerta = A.ID_Offerta WHERE A.ID_Vino = ?";

		try {
			connection = DataSourceProvider.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idVino);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				OffertaBean bean = new OffertaBean();
				bean.setIdOfferta(rs.getInt("ID_Offerta"));
				bean.setDataInizio(rs.getDate("Data_Inizio"));
				bean.setDataFine(rs.getDate("Data_Fine"));
				bean.setPercentuale(rs.getInt("Percentuale"));
				bean.setImmaginePromozionale(rs.getString("Immagine_Promozionale"));
				offerte.add(bean);
			}

		} finally {
			try {
				if (ps != null) ps.close();
			} finally {
				if (connection != null) connection.close();
			}
		}
		return offerte;
	}

	public synchronized void doApplyOfferta(int idVino, int idOfferta) throws SQLException {
		Connection connection = null;
		PreparedStatement ps = null;

		String sql = "INSERT INTO " + APPLICATO_TABLE + " (ID_Vino, ID_Offerta) VALUES (?, ?)";

		try {
			connection = DataSourceProvider.getConnection();
			ps = connection.prepareStatement(sql);
			
			ps.setInt(1, idVino);
			ps.setInt(2, idOfferta);

			ps.executeUpdate();
			
		} finally {
			try {
				if (ps != null) ps.close();
			} finally {
				if (connection != null) connection.close();
			}
		}
	}
	
	public synchronized boolean doRemoveOfferta(int idVino, int idOfferta) throws SQLException {
		Connection connection = null;
		PreparedStatement ps = null;
		int result = 0;

		String sql = "DELETE FROM " + APPLICATO_TABLE + " WHERE ID_Vino = ? AND ID_Offerta = ?";

		try {
			connection = DataSourceProvider.getConnection();
			ps = connection.prepareStatement(sql);
			ps.setInt(1, idVino);
			ps.setInt(2, idOfferta);

			result = ps.executeUpdate();

		} finally {
			try {
				if (ps != null) ps.close();
			} finally {
				if (connection != null) connection.close();
			}
		}
		return (result != 0);
	}
	
	public synchronized Collection<VinoBean> doRetrieveByName(String search) throws SQLException {
		return new ArrayList<>();
	}
	
	public synchronized Collection<VinoBean> doRetrieveByOrigine(String origine) throws SQLException {
		return new ArrayList<>();
	}
	
	
	public synchronized Collection<VinoBean> doRetrieveByAlcol(double valore, String operatore) throws SQLException {
		return new ArrayList<>();
	}
	
	//FILTRO SUPER COOL
	public synchronized Collection<VinoBean> doRetrieveByFilters(String tipo, String search, String origine, String alcolOp, double alcolVal, boolean inPromozione) throws SQLException {
		Connection connection = null;
		PreparedStatement ps = null;
		Collection<VinoBean> vini = new ArrayList<>();
		
		List<Object> params = new ArrayList<>();
		

		String sql = "SELECT V.*, "
				   + "MAX(O.Percentuale) AS ScontoMax, "
				   + "(V.Prezzo * (1 - (MAX(O.Percentuale) / 100.0))) AS PrezzoScontato "
				   + "FROM " + TABLE_NAME + " AS V "
				   + "LEFT JOIN " + APPLICATO_TABLE + " AS A ON V.ID_Vino = A.ID_Vino "
				   + "LEFT JOIN " + OFFERTA_TABLE + " AS O ON A.ID_Offerta = O.ID_Offerta "
				   + "AND CURDATE() BETWEEN O.Data_Inizio AND O.Data_Fine";
		if (inPromozione) {
			sql = sql.replace("LEFT JOIN " + APPLICATO_TABLE, "JOIN " + APPLICATO_TABLE);
			sql = sql.replace("LEFT JOIN " + OFFERTA_TABLE, "JOIN " + OFFERTA_TABLE);
		}

		sql += " WHERE V.In_Vendita = true";

		// --- FILTRI DINAMICI ---
		if (tipo != null && !tipo.isEmpty()) {
			sql += " AND V.Tipo = ?";
			params.add(tipo);
		}
		if (search != null && !search.isEmpty()) {
			sql += " AND V.Nome LIKE ?";
			params.add("%" + search + "%");
		}
		if (origine != null && !origine.isEmpty()) {
			sql += " AND V.Origine LIKE ?";
			params.add("%" + origine + "%");
		}
		if (alcolOp != null && !alcolOp.isEmpty() && alcolVal > 0) {
			if (alcolOp.equals("gt")) {
				sql += " AND V.Percentuale_Alcolica >= ?";
				params.add(alcolVal);
			} else if (alcolOp.equals("lt")) {
				sql += " AND V.Percentuale_Alcolica <= ?";
				params.add(alcolVal);
			}
		}
		
		sql += " GROUP BY V.ID_Vino"; 
		sql += " ORDER BY V.Data_Aggiunta DESC";

		try {
			connection = DataSourceProvider.getConnection();
			ps = connection.prepareStatement(sql);
			
			for (int i = 0; i < params.size(); i++) {
				ps.setObject(i + 1, params.get(i));
			}

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				VinoBean bean = new VinoBean();
				bean.setIdVino(rs.getInt("ID_Vino"));
				bean.setNome(rs.getString("Nome"));
				bean.setAnnata(rs.getInt("Annata"));
				bean.setTipo(rs.getString("Tipo"));
				bean.setDescrizione(rs.getString("Descrizione"));
				bean.setPercentualeAlcolica(rs.getDouble("Percentuale_Alcolica"));
				bean.setImmagine(rs.getString("Immagine"));
				bean.setPrezzo(rs.getBigDecimal("Prezzo")); // Prezzo base
				bean.setStock(rs.getInt("Stock"));
				bean.setFormato(rs.getString("Formato"));
				bean.setOrigine(rs.getString("Origine"));
				bean.setInVendita(rs.getBoolean("In_Vendita"));
				bean.setDataAggiunta(rs.getTimestamp("Data_Aggiunta"));
				
				int percentualeSconto = rs.getInt("ScontoMax");
				BigDecimal prezzoCalcolato = rs.getBigDecimal("PrezzoScontato");

				if (!rs.wasNull() && percentualeSconto > 0) {
					bean.setPrezzo(prezzoCalcolato.setScale(2, RoundingMode.HALF_UP));
					bean.setPercentualeSconto(percentualeSconto);
					bean.setPrezzoScontato(prezzoCalcolato.setScale(2, RoundingMode.HALF_UP));
				} else {
					bean.setPercentualeSconto(0);
					bean.setPrezzoScontato(null);
				}
				
				vini.add(bean);
			}

		} finally {
			try {
				if (ps != null) ps.close();
			} finally {
				if (connection != null) connection.close();
			}
		}
		return vini;
	}
	
	public synchronized Collection<VinoBean> doRetrieveAllAdmin(String order) throws SQLException {
		Connection connection = null;
		PreparedStatement ps = null;
		Collection<VinoBean> vini = new ArrayList<>();

		String sql = "SELECT * FROM " + TABLE_NAME;
		
		if (order != null && !order.isEmpty()) {
			sql += " ORDER BY " + order;
		}

		try {
			connection = DataSourceProvider.getConnection();
			ps = connection.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				VinoBean bean = new VinoBean();
				bean.setIdVino(rs.getInt("ID_Vino"));
				bean.setNome(rs.getString("Nome"));
				bean.setAnnata(rs.getInt("Annata"));
				bean.setTipo(rs.getString("Tipo"));
				bean.setDescrizione(rs.getString("Descrizione"));
				bean.setPercentualeAlcolica(rs.getDouble("Percentuale_Alcolica"));
				bean.setImmagine(rs.getString("Immagine"));
				bean.setPrezzo(rs.getBigDecimal("Prezzo"));
				bean.setStock(rs.getInt("Stock"));
				bean.setFormato(rs.getString("Formato"));
				bean.setOrigine(rs.getString("Origine"));
				bean.setInVendita(rs.getBoolean("In_Vendita"));
				bean.setDataAggiunta(rs.getTimestamp("Data_Aggiunta"));
				vini.add(bean);
			}

		} finally {
			try {
				if (ps != null) ps.close();
			} finally {
				if (connection != null) connection.close();
			}
		}
		return vini;
	}
	
	//Così che per sbaglio non updato il prezzo quando compro
	public synchronized void doUpdateStock(int idVino, int nuovoStock, boolean inVendita) throws SQLException {
		Connection connection = null;
		PreparedStatement ps = null;

		String sql = "UPDATE " + TABLE_NAME + " SET Stock = ?, In_Vendita = ? WHERE ID_Vino = ?";

		try {
			connection = DataSourceProvider.getConnection();
			ps = connection.prepareStatement(sql);
			
			ps.setInt(1, nuovoStock);
			ps.setBoolean(2, inVendita);
			ps.setInt(3, idVino);

			ps.executeUpdate();

		} finally {
			try {
				if (ps != null) ps.close();
			} finally {
				if (connection != null) connection.close();
			}
		}
	}
}
	