package seuBarrigaPontoCom_Estrategia3;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.ce.wcaquino.dao.utils.ConnectionFactory;
import br.ce.wcaquino.entidades.MassaDeDado;


/** As operações dessa classe dizem respeito a estratégia 3, onde os dados são
 *  inseridos em uma tabela do banco, utilizado e marcado como utilizado.
 * 
 * @author jay
 */
public class MassaDAOImpl implements MassaDAO{
	
	/**Insere um valor de uma conta no banco de dados na tabela "Massas". 
	 * 
	 * @author jay
	 */ 
	@SuppressWarnings("static-access")
	@Override
	public void inserirMassa(String tipo, String valor) throws Exception {
		
		PreparedStatement stmt = new ConnectionFactory().getConnection()
				.prepareStatement("INSERT INTO massas (tipo, valor) VALUES (?, ?)");
		
		stmt.setString(1, tipo);
		stmt.setString(2, valor);		
		stmt.execute();
		stmt.close();
	}	
	
	/**Obtem um valor da massa de dados, que não tenha sido utilizado.
	 * Retorna null se não encontrar um dado que ainda nao tenha sido utilizado
	 * 
	 * @author jay
	 * @return String valor
	 */
	@Override
	public MassaDeDado obterMassaDeDados(String tipoConta) throws Exception {

		MassaDeDado contaSeuBarriga = new MassaDeDado();
		
		PreparedStatement stmt = ConnectionFactory.getConnection()
				.prepareStatement(
						"WITH primeiraConsulta AS ( \n" + 
						"	SELECT id, valor \n" + 
						"	FROM massas \n" + 
						"	WHERE usada = false \n" + 
						"	AND tipo = ? \n" + 
						"	ORDER BY id\n" + 
						"	LIMIT 1) \n" + 
						"UPDATE massas segundaConsulta \n" + 
						"SET usada = true \n" + 
						"FROM primeiraConsulta \n" + 
						"WHERE primeiraConsulta.id = segundaConsulta.id \n" + 
						"RETURNING  primeiraConsulta.id, primeiraConsulta.valor ");
		
		stmt.setString(1, tipoConta);
		
		ResultSet resultSet = stmt.executeQuery();
		
		if(!resultSet.next()) return null;		// Se não tem encontra nada retorna null
		
		contaSeuBarriga.setId(resultSet.getLong("id"));
		contaSeuBarriga.setValor(resultSet.getString("valor"));
		
		return contaSeuBarriga;
	}
	
	/**
	 * Recupera o id de uma conta pelo seu valor
	 */
	@Override
	public long obterIdDaConta(MassaDeDado contaSeuBarriga) throws ClassNotFoundException, SQLException {
		
		PreparedStatement stmt = ConnectionFactory.getConnection()
				.prepareStatement("SELECT id FROM massas WHERE valor = ?");
		
		stmt.setString(1, contaSeuBarriga.getValor());
		
		System.out.println("Query de consulta para obter id da conta:\n" + stmt);
		
		ResultSet rs = stmt.executeQuery();
		
		long id = rs.getLong("id");
		
		return id;
	}
	
	/**
	 * Reutiliza a massa de dado
	 */
	@Override
	public boolean alterarUsadoParaFalso(long id) throws Exception {
		
		boolean temResultado = true;
		
		PreparedStatement stmt = ConnectionFactory.getConnection()
				.prepareStatement("UPDATE massas SET usada = false WHERE id = ?");
		
		stmt.setLong(1, id);
		int alterado = stmt.executeUpdate();
		
		stmt.close();
		
		if(alterado == 0) {
			temResultado =  false;
		}
		
		return temResultado;
	}

	public int obterQuantidadeDeContas(String tipoConta) throws ClassNotFoundException, SQLException {
		PreparedStatement stmt = ConnectionFactory
				.getConnection().prepareStatement("SELECT COUNT(*) FROM massas WHERE tipo = ? AND usada = false");
		
		stmt.setString(1, tipoConta);
		
		ResultSet rs = stmt.executeQuery();
		
		if(!rs.next()) return 0;
		
		int quantidade = rs.getInt(1);
		
		stmt.close();
		
		return quantidade;
		
	}	
	
	
	
	
	
	
	
	
	
	
	
	
	
}