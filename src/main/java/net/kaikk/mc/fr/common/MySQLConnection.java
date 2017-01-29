package net.kaikk.mc.fr.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

public class MySQLConnection {
	protected String hostname, database, username, password;
	protected DataSource dataSource;
	protected ThreadLocal<Connection> conn = new ThreadLocal<Connection>();
	protected ThreadLocal<MySQLQueries> queries = new ThreadLocal<MySQLQueries>();

	public MySQLConnection(DataSource dataSource) throws SQLException {
		this.dataSource = dataSource;
		this.dbCheck();
	}

	public Statement statement() throws SQLException {
		this.dbCheck();
		return this.conn.get().createStatement();
	}
	
	public PreparedStatement prepareStatement(String sql) throws SQLException {
		this.dbCheck();
		return this.conn.get().prepareStatement(sql);
	}
	
	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
		this.dbCheck();
		return this.conn.get().prepareStatement(sql, autoGeneratedKeys);
	}
	
	public void dbCheck() throws SQLException {
		if(this.conn.get() == null || this.conn.get().isClosed()) {
			this.conn.set(this.dataSource.getConnection());
			this.queries.set(new MySQLQueries(this));
		}
	}
	
	public Connection getConnection() throws SQLException {
		this.dbCheck();
		return conn.get();
	}
	
	public MySQLQueries getQueries() throws SQLException {
		this.dbCheck();
		return queries.get();
	}
	
	public void dbClose()  {
		try {
			if (!this.conn.get().isClosed()) {
				this.conn.get().close();
				this.conn.set(null);
				this.queries.set(null);
			}
		} catch (SQLException e) {
			
		}
	}
}