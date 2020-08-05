package seuBarrigaPontoCom_estrategia5;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.service.UsuarioService;
import dbUnit.ImportExport;

/**
 * Antes de executar os testes, gera todos os dados no banco, utilizando dados vindo do arquivo.xml, através do DbUnit
 * 
 * @author jay
 *
 */
public class TestServiceDbUnit_Usuario {
	
	private UsuarioService usuarioService = new UsuarioService();

	@BeforeClass
	public static void beforeClass() throws Exception {
		
		ImportExport.importarDadoParaOBanco("est5_inserirUsuarios.xml");
	}
	
	/**
	 * Testa a consulta de um usuário no banco de dados, que foi inserido utilizando o arquivo.xml no before class
	 * @throws Exception 
	 */
	@Test
	public void testConsultarUsuario() throws Exception {
	
		Usuario usuario = usuarioService.findByNome("Usuario XML: Consultar");
		
		assertNotNull("[ERRO]Houve um erro ao tentar consultar um usuário no banco de dados, via DbUnit", usuario);
		
	}
	
	/**
	 * Testa a alteração de um usuário no banco de dados, que foi inserido utilizando o arquivo.xml no before class
	 * @throws Exception 
	 */
	@Test
	public void testAlterarUsuario() throws Exception {
		Usuario usuarioAlterar = usuarioService.findByNome("Usuario XML: Alterar");
		
		String novoNome = usuarioAlterar.getNome() + ", Alterado!";
		
		usuarioAlterar.setNome(novoNome);
		
		usuarioService.salvar(usuarioAlterar);
		
		assertNotNull("[ERRO]Houve algum erro ao tentar alterar um usuário, utilizando DbUnit", usuarioService.findByNome(novoNome));
	}
	
	/**
	 * Testa a remoção de um arquivo no banco de dados, que foi inserido utilizando o arquivo.xml no before class
	 * @throws Exception 
	 */
	@Test
	public void testRemoverUsuario() throws Exception {
		Usuario remover = usuarioService.findByNome("Usuario XML: Remover");
		
		usuarioService.delete(remover);
		
		Usuario removido = usuarioService.findByNome("Usuario XML: Remover");
		
		assertNull("[ERRO]Houve erro ao tentar remover um usuário do banco", removido);
	}
	
	
}
