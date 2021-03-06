package seuBarrigaPontoCom_estrategia5;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.BeforeClass;
import org.junit.Test;

import br.ce.wcaquino.dao.utils.ConnectionFactory;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.service.UsuarioService;

public class TestService_Usuario {
	
	private UsuarioService usuarioService = new UsuarioService();
	
	@BeforeClass
	public static void beforeClass() throws ClassNotFoundException, SQLException {
		Connection JDBC = ConnectionFactory.getConnection();
		
		JDBC.prepareStatement(
				"DELETE FROM contas").execute();
		JDBC.prepareStatement(
				"DELETE FROM usuarios").execute();
		JDBC.prepareStatement(
				"INSERT INTO usuarios"
				+ "(id, nome, email, senha, conta_principal_id) VALUES "
				+ "(1, 'Usuario para consulta', 'consulta@gmail.com', '123', 1),"
				+ "(2, 'Usuario para alterar', 'alteracao@gmail.com', '123', 2),"
				+ "(3, 'Usuario para remover', 'remover@gmail.com', '123', 3)").execute();
		
		ConnectionFactory.closeConnection();
	}
	
	@Test
	public void testInserirUsuario() throws Exception {
		
		String nomeInserido = "Usuario para insercao";
		
		Connection JDBC = ConnectionFactory.getConnection();
		
		JDBC.prepareStatement(
				"INSERT INTO usuarios"
						+ "(id, nome, email, senha, conta_principal_id) VALUES "
						+ "(4, '"+ nomeInserido + "', 'insercao@gmail.com', '123', 4)").execute();
		
		ConnectionFactory.closeConnection();
		
		Usuario usuario = usuarioService.findByNome(nomeInserido);
		
		assertEquals("[ERRO]Houve erro ao tentar inserir um usuario no banco", nomeInserido, usuario.getNome());
	}
	
	@Test
	public void testConsultarUsuario() throws Exception {
		Usuario usuario = usuarioService.findByNome("Usuario para consulta");
		
		assertNotNull("[ERRO]Houve erro ao tentar consultar um usuario no banco", usuario);
	}
	
	@Test
	public void testAlterarUsuario() throws Exception {
		
		String novoNome = "Usuario para alterar - Alterado!";
		
		Usuario usuario = usuarioService.findByNome("Usuario para alterar");
		
		usuario.setNome(novoNome);
		
		usuarioService.salvar(usuario);
		
		Usuario usuarioAlterado = usuarioService.findByNome(novoNome);
		
		assertEquals("[ERRO]Houve erro ao tentar alterar um usuario no banco", novoNome, usuarioAlterado.getNome());
	}
	
	@Test
	public void testRemoverUsuario() throws Exception {
		Usuario usuarioParaRemover = usuarioService.findByNome("Usuario para remover");
		
		usuarioService.delete(usuarioParaRemover);
		
		Usuario usuarioRemovido = usuarioService.findByNome("Usuario para remover");
		
		assertNull("[ERRO]Houve um erro ao tentar remover um usuario", usuarioRemovido);
	}
}
