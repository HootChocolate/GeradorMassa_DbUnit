package seuBarrigaPontoCom_estrategia4;			

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

import br.ce.wcaquino.dao.utils.ConnectionFactory;
import br.ce.wcaquino.entidades.Conta;
import br.ce.wcaquino.service.ContaService;

/**
 * Teste utilizando a estratégia de gerenciamento de massa de dados #4
 * 
 * Antes de cada teste os valores do banco de dados são limpos, então em cada teste é inserido um valor,
 * o qual é utilizado para fazer o teste.
 * Caso eu tivesse acesso ao banco de dados do Seu Barriga, eu poderia fazer isso via web
 * 
 * @author jay
 */
public class TestService_Conta {

	private ContaService contaService = new ContaService();
	
	/**
	 * Antes de cada teste limpa os dados do banco.
	 * Deve-se respeitar a ordem de remoção dos dados 
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	@Before
	public void setUp() throws ClassNotFoundException, SQLException {
		Connection conexaoJBDC= ConnectionFactory.getConnection();
		conexaoJBDC.prepareStatement("DELETE FROM transacoes").executeUpdate();
		conexaoJBDC.prepareStatement("DELETE FROM contas").executeUpdate();
		conexaoJBDC.prepareStatement("DELETE FROM usuarios").executeUpdate();
		
		ConnectionFactory.closeConnection();
	}
	
	/**
	 * Após limpar o banco de dados no before, insere uma conta e um usuário no banco
	 * e testa a inserçao da conta
	 * @throws Exception
	 */
	@Test
	public void testInserirConta() throws Exception {
		
		String nomeConta = "Inserir Conta";
		
		inserirUsuarioEConta(nomeConta);
		
		Conta consultaConta = contaService.findByName(nomeConta);
		
		assertEquals("[ERRO]Houver algum erro no teste de inserção de dados, no teste de serviço da estratégia 4"
				, nomeConta, consultaConta.getNome());
	}

	/**
	 * Após limpar o banco de dados no before, insere uma conta e um usuário no banco
	 * e testa a consulta da conta
	 * @throws Exception
	 */
	@Test
	public void testConsultaConta() throws Exception {
				
		String nomeConta= "Test Consultar";
		
		inserirUsuarioEConta(nomeConta);
		
		Conta contaConsultada = contaService.findByName(nomeConta);		
		
		assertEquals("[ERRO]Houve algum erro ao consultar uma conta, no teste de serviço da estratégia 4"
				, nomeConta, contaConsultada.getNome());
	}
	
	/**
	 * Após limpar o banco de dados no before, insere uma conta e um usuário no banco
	 * e testa a alteração da conta
	 * @throws Exception
	 */
	@Test
	public void testAlterarConta() throws Exception {
				
		String nomeConta = "Test Alterar";
		String nomeContaAlterada = "Test Alterar OK";
		
		inserirUsuarioEConta(nomeConta);
		
		Connection conexaoJDBC = ConnectionFactory.getConnection();
		
		conexaoJDBC.prepareStatement(
				"UPDATE contas "
				+ "SET nome = '" + nomeContaAlterada 
				+ "' WHERE id = 1").execute();
		
		ConnectionFactory.closeConnection();
		
		Conta contaAlterada = contaService.findByName(nomeContaAlterada);
		
		assertEquals("[ERRO]Houve erro ao tentar editar uma conta no banco, no teste de serviço da estratégia 4"
				, nomeContaAlterada, contaAlterada.getNome());
	}
	
	/**
	 * Após limpar o banco de dados no before, insere uma conta e um usuário no banco
	 * e testa a remoção da conta
	 * @throws Exception
	 */
	@Test
	public void testRemoverConta() throws Exception {
				
		String nomeConta = "Test Remover";
		
		inserirUsuarioEConta(nomeConta);
		
		Connection conexaoJDBC = ConnectionFactory.getConnection();
		
		conexaoJDBC.prepareStatement("DELETE FROM contas WHERE nome = '" + nomeConta + "'").execute();
		
		ConnectionFactory.closeConnection();
		
		Conta contaRemovida = contaService.findByName(nomeConta);
		
		assertNull("[ERRO]Houve algum erro ao tentar remover uma conta, no teste de serviço utilizando a estratégia 4"
				, contaRemovida);
	}
	
	/**
	 * Insere um usuário e uma conta no banco.
	 * @param nomeConta
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	private void inserirUsuarioEConta(String nomeConta) throws ClassNotFoundException, SQLException {
		
		Connection conexaoJDBC = ConnectionFactory.getConnection();
		
		conexaoJDBC.prepareStatement(
				"INSERT INTO usuarios "
				+ "(id, nome, email, senha) "
				+ "VALUES (1, 'Usuario de Teste', 'teste@email.com', 'passwd')").execute();
		
		conexaoJDBC.prepareStatement(
				"INSERT INTO contas "
				+ "(id, nome, usuario_id) "
				+ "VALUES (1,  '" + nomeConta + "', 1)").execute();		
		
		ConnectionFactory.closeConnection();
	}
}
