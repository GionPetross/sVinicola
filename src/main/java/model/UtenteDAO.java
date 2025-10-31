package model;

import java.sql.*;


public class UtenteDAO {
	
	private Connection getConnection() throws SQLException {
		//Non ho ancora configurato questa roba e sinceramente non ho molta voglia
		//Non dimenticare di farlo più tardi
		return DriverManager.getConnection("jdbc:mysql://localhost:3306/ecommerce", "root", "password");
	}

	//Metodo che ritorna l'utente dal database se l'username e password matchano
	public Utente trovaUtente(String username, String password) {
		String sql = "SELECT * FROM utente WHERE username=? AND password=?";
		try (Connection con = getConnection();
			 PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, username);
			ps.setString(2, password);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					Utente u = new Utente();
					u.setId(rs.getInt("id"));
					u.setUsername(rs.getString("username"));
					u.setPassword(rs.getString("password"));
					u.setIndirizzo(rs.getString("indirizzo"));
					u.setEmail(rs.getString("email"));
					u.setAdmin(rs.getBoolean("admin"));
					return u;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//Metodo che inserisce un nuovo utente nel db
	public boolean save(Utente u) {
		String sql = "INSERT INTO utente(username,password,indirizzo,email,admin) VALUES(?,?,?,?,?)";
		try (Connection con = getConnection();
			 PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, u.getUsername());
			ps.setString(2, u.getPassword());
			ps.setString(3, u.getIndirizzo());
			ps.setString(4, u.getEmail());
			ps.setBoolean(5, u.isAdmin());
			ps.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

}
