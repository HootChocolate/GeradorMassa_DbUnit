package seuBarrigaPontoCom_estrategia4;

import static org.junit.Assert.*;

import java.sql.Connection;

import org.junit.Before;
import org.junit.Test;

import br.ce.wcaquino.dao.utils.ConnectionFactory;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.service.UsuarioService;

/**
 * Essa estratégia de gereniamento de massa de dados, antes de cada teste as tabelas necessárias para os testes
 * são limpas, e cada teste faz o que quer com o banco "novo".
 * Pontos negativos: Paralelismo e Controle dos dados
 * Pontos positivos: Independente, Automático e Manual
 * @author jay
 */
public class TestService_Usuario {

	private UsuarioService usuarioService = new UsuarioService();
	
	@Before
	public void setUp() throws Exception {
		
		Connection conexaoJDBC = ConnectionFactory.getConnection();
		
		conexaoJDBC.prepareStatement("WITH consulta AS (DELETE FROM contas) DELETE FROM usuarios").execute();
		
		ConnectionFactory.closeConnection();
	}
	
	/**
	 * Insere o 'Usuario inserido' no banco de dados, e depois se certifica que o usuario foi inserido pela classe de servico(Fazendo a consulta)
	 * @throws Exception
	 */
	@Test
	public void testInsercaoUsuario() throws Exception {
		Connection conexaoJDBC = ConnectionFactory.getConnection();
		
		conexaoJDBC.prepareStatement(""
				+ "INSERT INTO usuarios "
				+ "(id, nome, email, senha, conta_principal_id) "
				+ "VALUES (1, 'Usuario inserido', 'inserido@gmail.com)', '1234', 1)").execute();
		
		ConnectionFactory.closeConnection();
		
		Usuario usuario = usuarioService.findByNome("Usuario inserido");
		
		assertNotNull("[ERRO]Houve erro ao tentar inserir um usuario no banco de dados - TestService_Usuario", usuario);
	}
	/**
	 * Insere o 'Usuario para consulta' no banco de dados, e depois se certifica que o usuario é consultado pela classe de servico
	 * @throws Exception
	 */
	@Test
	public void testUsuarioConsultado() throws Exception{
		Connection conexaoJDBC = ConnectionFactory.getConnection();
		conexaoJDBC.prepareStatement(""
				+ "INSERT INTO usuarios "
				+ "(id, nome, email, senha, conta_principal_id) "
				+ "VALUES (1, 'Usuario para consulta', 'consulta@gmail.com)', '1234', 1)").execute();
		
		ConnectionFactory.closeConnection();
		
		Usuario usuario = usuarioService.findByNome("Usuario para consulta");
		
		assertNotNull("[ERRO]Houve erro ao tentar consultar um usuario - TestService_Usuario", usuario);
	}
	
	/**
	 * Insere o 'Usuario para alterar' no banco de dados, e depois se certifica que o usuario foi alterado pela classe de servico
	 * @throws Exception
	 */
	@Test
	public void testAlterarUsuario() throws Exception {
		Connection conexaoJDBC = ConnectionFactory.getConnection();
		
		conexaoJDBC.prepareStatement(
				"INSERT INTO usuarios "
				+ "(id, nome, email, senha, conta_principal_id) "
				+ "VALUES (1, 'Usuario para alterar', 'alterar@gmail.com)', '1234', 1)").execute();
		
		ConnectionFactory.closeConnection();
		
		Usuario usuarioConsultado = usuarioService.findByNome("Usuario para alterar");
		
		String novoNome = usuarioConsultado.getNome() + " Alterado!";
		
		usuarioConsultado.setNome(novoNome);
		
		usuarioService.salvar(usuarioConsultado);
		
		Usuario usuarioAlterado = usuarioService.findByNome(novoNome);

		assertNotNull("[ERRO]Houve erro ao tentar alterar um usuario no TestService_Usuario", usuarioAlterado);
	}
	
	/**
	 * Insere o 'Usuario para remover' no banco de dados, e depois se certifica que o usuario foi removido pela classe de servico
	 * @throws Exception
	 */
	@Test
	public void testRemoverUsuario() throws Exception{
		
		Connection conexaoJDBC = ConnectionFactory.getConnection();
		
		conexaoJDBC.prepareStatement(
				"INSERT INTO usuarios "
				+ "(id, nome, email, senha, conta_principal_id) "
				+ "VALUES (1, 'Usuario para remover', 'remover@gmail.com)', '1234', 1)").execute();
		
		ConnectionFactory.closeConnection();
		
		Usuario usuarioParaDeletar = usuarioService.findByNome("Usuario para remover");
		
		usuarioService.delete(usuarioParaDeletar);
		
		Usuario usuarioDeletado = usuarioService.findByNome(usuarioParaDeletar.getNome());

		assertNull("[ERRO]Houve erro ao tentar remover um usuario no TestService_Usuario", usuarioDeletado);
	}
}
