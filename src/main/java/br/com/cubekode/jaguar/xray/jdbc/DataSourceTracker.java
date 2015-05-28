package br.com.cubekode.jaguar.xray.jdbc;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import br.com.cubekode.jaguar.xray.CodeTrace;
import br.com.cubekode.jaguar.xray.ThreadTracker;

public class DataSourceTracker implements DataSource {

	public static DataSource track(DataSource ds) {
		if (ds instanceof DataSourceTracker) {
			return ds;
		}
		return new DataSourceTracker(ds);
	}

	private DataSource ds;

	private DataSourceTracker(DataSource ds) {
		this.ds = ds;
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return ds.getLogWriter();
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		ds.setLogWriter(out);
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		ds.setLoginTimeout(seconds);
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		return ds.getLoginTimeout();
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return ds.unwrap(iface);
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return ds.isWrapperFor(iface);
	}

	@Override
	public Connection getConnection() throws SQLException {
		CodeTrace trace = ThreadTracker.trace("JDBC-getConnection()");
		try {
			return trace.finishReturn(new ConnectionTracker(ds.getConnection()));
		} catch (SQLException e) {
			trace.finish(e);
			throw e;
		} catch (RuntimeException e) {
			trace.finish(e);
			throw e;
		}
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		CodeTrace trace = ThreadTracker.trace("JDBC-getConnection");
		try {
			return trace.finishReturn(new ConnectionTracker(ds.getConnection(username, password)));
		} catch (SQLException e) {
			trace.finish(e);
			throw e;
		} catch (RuntimeException e) {
			trace.finish(e);
			throw e;
		}
	}
}
