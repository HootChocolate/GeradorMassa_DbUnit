package seuBarrigaPontoCom_Estrategia2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.github.javafaker.Faker;

import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.service.UsuarioService;

/** Insere um Usuario no banco, utilizando o UsuarioService e a estratégia dois de gerenciamento de dados
 * onde antes da execução de cada teste, é gerado o dado, podendo ser gerado várias vezes.
 * 
 * Isso garante a Independencia, ser executado de maneira Automatica e Manual, ser executado em Paralelo
 * e Sem o Controle dos Dados.
 * @author jay
 */
public class UsuarioServiceTest {

	private UsuarioService usuarioService = new UsuarioService();
	private Usuario usuarioDeTeste;
	private static Faker faker = new Faker();
	

	/**Antes de cada teste, configura um usuário
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		
		String nome = faker.artist().name() + " - " + faker.number().randomNumber();
		String email = faker.internet().emailAddress();
		String senha = faker.internet().password();
		
		usuarioDeTeste = new Usuario(nome, email, senha);
	}
	
	@Test
	public void testInserirUsuarioNoBanco() throws Exception {

		Usuario usuarioCriado = criarUsuario();
		
		assertNotNull("[ERRO] Houveram erros ao tentar criar um usuário no banco", usuarioCriado);
	}
	
	@Test
	public void testConsultarUsuarioNoBanco() throws Exception {
		
		Usuario usuarioCriado = criarUsuario();

		Usuario usuarioConsultado = usuarioService.findById(usuarioCriado.getId());
		
		assertEquals("[ERRO] Houveram erros ao tentar consultar um usuário no banco", usuarioConsultado.getNome(), usuarioCriado.getNome());
	}
	
	@Test
	public void testEditarUsuarioNoBanco() throws Exception {
		
		Usuario usuarioCriado = criarUsuario();
		
		String novoNome = usuarioCriado.getNome() + " Alterado! ";
		
		usuarioDeTeste.setNome(novoNome);
		
		Usuario usuarioSalvo = usuarioService.salvar(usuarioDeTeste);
		
		assertEquals("[ERRO] Houveram erros ao tentar editar o nome de um usuário no banco de dados", novoNome, usuarioSalvo.getNome());
	}
	
	@Test
	public void testRemoverUsuarioDoBanco() throws Exception {
		
		Usuario usuarioCriado = criarUsuario();
		
		long idUsuarioCriado = usuarioCriado.getId();

		usuarioService.delete(usuarioCriado);
		
		Usuario usuarioProcurado = usuarioService.findById(idUsuarioCriado);
		
		assertNull("[ERRO] Houveram erros ao tentar remover um usuario do banco", usuarioProcurado);
	}
	
	public Usuario criarUsuario() throws Exception {
		return usuarioService.salvar(usuarioDeTeste);
	}
	
}
