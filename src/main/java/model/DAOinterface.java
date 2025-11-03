package model;

import java.sql.SQLException;
import java.util.Collection;

/**
 * Interfaccia DAO Generica
 * Definisce le operazioni CRUD (Create, Retrieve, Update, Delete) di base.
 */
public interface DAOinterface<T> {


	//Salva un nuovo bean nel database.
	public void doSave(T bean) throws SQLException;

	
	//Aggiorna un bean esistente nel database.
	public void doUpdate(T bean) throws SQLException;

	
	//Cancella un bean dal database usando la sua chiave primaria (ID).
	public boolean doDelete(int code) throws SQLException;

	
	//Recupera un bean dal database usando la sua chiave primaria (ID).
	public T doRetrieveByKey(int code) throws SQLException;
	
	
	//Recupera tutti i bean di un tipo dal database.
	public Collection<T> doRetrieveAll(String order) throws SQLException;
	
}