package config;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.ce.wcaquino.dao.utils.ConnectionFactory;

public class ConfiguracaoPostgres {
	
	public static List<String> obterListaDeTabelasBanco() throws ClassNotFoundException, SQLException {
		
		List<String> tables = new ArrayList<String>();
		
		ResultSet resultSet = ConnectionFactory.getConnection().createStatement().executeQuery(
				"SELECT table_name FROM information_schema.tables WHERE table_schema = 'public'");
		
		while(resultSet.next()) {
			tables.add(resultSet.getString("table_name"));
		}
		
		return tables;
	}

	public static void habilitarTriggersPostgres(List<String> listaDeTabelasBanco) throws ClassNotFoundException, SQLException {
		
//		Statement stmt = ConnectionFactory.getConnection().createStatement();
		
//		stmt.executeUpdate("SET session_replication_role = DEFAULT");
//		stmt.close();
		
		for(int i = 0; i < listaDeTabelasBanco.size(); i++) {
			Statement stmt = ConnectionFactory.getConnection().createStatement();
			
			stmt.executeUpdate("SET session_replication_role = DEFAULT");
			stmt.close();
		}
	}
	
	public static void desativarTriggersPostgres(List<String> listaDeTabelasBanco) throws ClassNotFoundException, SQLException {
			
//		Statement stmt = ConnectionFactory.getConnection().createStatement();
//		
//		stmt.executeUpdate("SET session_replication_role = replica");
//		stmt.close();
		
		for(int i = 0; i < listaDeTabelasBanco.size(); i++) {
			Statement stmt = ConnectionFactory.getConnection().createStatement();
			
			stmt.executeUpdate("SET session_replication_role = replica");
			stmt.close();
		}		
	}
}
