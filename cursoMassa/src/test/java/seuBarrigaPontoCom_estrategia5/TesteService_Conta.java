package seuBarrigaPontoCom_estrategia5;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.BeforeClass;
import org.junit.Test;

import br.ce.wcaquino.dao.utils.ConnectionFactory;
import br.ce.wcaquino.entidades.Conta;
import br.ce.wcaquino.service.ContaService;

/**
 * Neste tipo de estratégia, é gerado apenas uma vez a massa de dados no início de todos os testes,
 * depois cada teste utiliza os dados que foram gerados.
 * Pontos negativos: Paralelismo e Controle dos dados
 * Pontos positivos: Independente, Automático e Manual
 * 
 * @author jay
 */
public class TesteService_Conta {
	
	private ContaService contaService = new ContaService();

	/**
	 * Limpa o banco e insere as informações antes da execução dos testes.
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	@BeforeClass
	public static void setUp() throws ClassNotFoundException, SQLException {
		
		Connection JDBC = ConnectionFactory.getConnection();
		
		JDBC.prepareStatement("WITH primeiro AS ("
				+ "DELETE FROM contas"
				+ ") DELETE FROM usuarios").execute();
				
		JDBC.prepareStatement(
				"WITH primeiro AS ( " 
				+ "INSERT INTO usuarios (id, nome, email, senha, conta_principal_id) "
				+ "VALUES (1, 'Usuario de teste', 'teste@gmail.com', '123', 1)" 
				+ ") INSERT INTO contas (id, nome, usuario_id)" 
				+ "	VALUES(1 , 'Conta para consultar', 1),"  
				+ "	(2 , 'Conta para alterar', 1),"  
				+ "	(3 , 'Conta para remover', 1);").execute();
		
		ConnectionFactory.closeConnection();		
	}
	
	/**
	 * Testa a inserção da conta "Conta inserida teste" no banco, depois conferindo se ela foi inserida utilizando contaService.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testInserirConta() throws Exception {
		Connection conenxaoJDBC = ConnectionFactory.getConnection();
		
		conenxaoJDBC.prepareStatement(
				"INSERT INTO contas (id, nome, usuario_id)"
				+ "VALUES (4, 'Conta inserida teste', 1)").execute();
		
		ConnectionFactory.closeConnection();
		
		Conta contaProcurada = contaService.findByName("Conta inserida teste");

		assertNotNull("[ERRO]Houve erro durante o teste de inserção de uma conta no banco", contaProcurada);
	}
	/**
	 * Consulta a conta "Conta para consultar" no banco, utilizando contaService
	 * @throws Exception
	 */
	@Test
	public void testConsultarConta() throws Exception {
		
		Conta contaConsultada = contaService.findByName("Conta para consultar");
		
		assertNotNull("[ERRO] Houve erro ao tentar consultar a conta no banco", contaConsultada);
	}
	
	/**
	 * Testa a alteração da "Conta para alterar" para "Conta para alterar - Alterado!" no banco, utilizando contaService
	 * @throws Exception
	 */
	@Test
	public void testAlteraConta() throws Exception {
		
		String novoNome = "Conta para alterar - Alterado!";
		
		ConnectionFactory.getConnection().prepareStatement(
				"UPDATE contas "
				+ "SET nome = '"+ novoNome + "' "
				+ "WHERE id = 2").executeUpdate();
		
		ConnectionFactory.closeConnection();
		
		Conta contaAlterada = contaService.findByName(novoNome);
		
		assertEquals("[ERRO]Houve erro ao tentar remover uma conta no banco", novoNome, contaAlterada.getNome());
	}
	
	/**
	 * Testa a remoção da "Conta para remover" no banco, utilizando contaService
	 * @throws Exception
	 */
	@Test
	public void testRemoverConta() throws Exception {
		
		Conta contaParaRemover = contaService.findByName("Conta para remover");
		
		contaService.delete(contaParaRemover);
		
		Conta contaRemovida = contaService.findByName("Conta para remover");
		
		assertNull("[ERRO]Houve erro ao tentar remove uma conta no banco", contaRemovida);
	}
	
}
