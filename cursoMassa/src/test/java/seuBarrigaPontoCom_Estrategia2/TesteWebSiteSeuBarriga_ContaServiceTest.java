package seuBarrigaPontoCom_Estrategia2;
/**Insere uma conta no banco Utilizando o ContaService e a estratégia dois de gerenciamento de dados
 * onde antes da execução de cada teste, é gerado o dado, podendo ser gerado várias vezes.
 * Isso garante a Independencia, ser executado de maneira Automatica e Manual, ser executado em Paralelo
 * e Sem o Controle dos Dados.
 * 
 * @author jay
 */

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.javafaker.Faker;

import Utils.BaseUtil;
import br.ce.wcaquino.entidades.Conta;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.service.ContaService;
import br.ce.wcaquino.service.UsuarioService;

public class TesteWebSiteSeuBarriga_ContaServiceTest extends BaseUtil{

	private ContaService contaService = new ContaService();
	private static UsuarioService usuarioService = new UsuarioService();
	private static Usuario usuarioGlobal;
	private Conta contaGlobal;
	private static Faker faker = new Faker();
	
	/**Configura um usuário uma única vez para poder fazer as operações mudando apenas as informações nas contas
	 * 
	 * @throws Exception
	 */
	@BeforeClass
	public static void beforeClass() throws Exception {
		String nome = faker.dragonBall().character();
		String email = faker.internet().emailAddress();
		String senha = faker.internet().password();
		
		usuarioGlobal = new Usuario(nome, email, senha);
		
		usuarioService.salvar(usuarioGlobal);
	}
	
	@Before
	public void setUp() throws Exception {
		String nomeConta = faker.backToTheFuture().character() + " - " +faker.number().randomDigit();
		
		contaGlobal = new Conta(nomeConta, usuarioGlobal);
	}
	
	@Test
	public void testInserirContaNoBanco() throws Exception {

		Conta conta = salvarConta();
		
		assertNotNull(conta.getId());
	}
	
	@Test
	public void testConsultarContaNoBanco() throws Exception {
		
		Conta contaSalva = salvarConta();
		Conta contaConsultada = contaService.findById(contaSalva.getId());
		
		assertEquals("[ERRO]Houveram erros ao tentar consultar uma conta no banco", contaSalva.getNome(), contaConsultada.getNome());
		
	}
	
	@Test
	public void testAlterarNomeDeUmaContaNoBanco() throws Exception {
		
		String novoNome =  contaGlobal.getNome() + " alterado!";
		
		contaGlobal.setNome(novoNome);
		
		Conta contaAlterada = contaService.salvar(contaGlobal);
		
		assertEquals("[ERRO] Houveram erros ao tentar editar uma conta no banco de dados", novoNome, contaAlterada.getNome());
	}
	
	@Test
	public void testRemoverContaDoBanco() throws Exception {
	
		Conta contaSalva = salvarConta();
		
		long idContaSalva = contaSalva.getId();
		
		contaService.delete(contaSalva);
		
		Conta contaConsultada = contaService.findById(idContaSalva);
		
		assertNull("[ERRO] Houveram erros ao tentar remover uma conta do banco de dados", contaConsultada);
	}
	
	public Conta salvarConta() throws Exception {
		return contaService.salvar(contaGlobal);
	}
}
