package seuBarrigaPontoCom_Estrategia3;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import com.github.javafaker.Faker;

import Utils.BaseUtil;
import br.ce.wcaquino.entidades.MassaDeDado;
import pages.HomePage;
import pages.LoginPage;

/**
 * Essa classe é responsável por acessar o servidor do Seu Barriga,
 * pega um valor do banco para ser utilizado no teste, (seria o catálogo).
 * 
 * @author jay
 */
public class TesteSelenium_SeuBarriga extends BaseUtil {
	public WebDriver navegador;

	private static String CONTA_SEU_BARRIGA = "CONTA_SrB";
	private static Faker faker = new Faker();

	@Before
	public void setUp() throws Exception {
		navegador = getChromeWebDriver("https://seubarriga.wcaquino.me/login");

		new LoginPage(navegador)
			.informarEmailESenha("jose@jose.com", "josejose")
			.clicaNoBotaoEntrar();
	}

	/**
	 * Insere uma conta na aplicação e no banco de dados
	 * 
	 * @throws Exception
	 */
	@Test
	public void testInsereUmaContaNoSeuBarriga() throws Exception {

		String nomeConta = faker.gameOfThrones().character() + " " + faker.gameOfThrones().dragon();

		String resultado = new HomePage(navegador)
				.clicarNoMenuContas(navegador)
				.clicarNaOpcaoAdicionar(navegador)
				.inserirNome(nomeConta)
				.clicarNoBotaoSalvar(navegador)
				.getMensagemFeedback(navegador);

		assertEquals(
				"[ERRO]Houve algum erro ao tentar adicionar uma conta no seu barriga utilizando informação do banco.",
				"Conta adicionada com sucesso!", resultado);

		inserirConta(nomeConta);
	}

	/**
	 * Consulta uma conta no Seu Barriga através de um dado do banco, e ao final 
	 * altera a variavel de usada para false, pois não houve mudança no dado.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	@Test
	public void testConsultaContaNoSeuBarriga() throws Exception {

		GeradorDeMassa gerador = new GeradorDeMassa();
		
		MassaDeDado contaSeuBarriga = gerador.obterMassaDeDados();
		
		String resultado = new HomePage(navegador)
			.clicarNoMenuContas(navegador)
			.clicarNaOpcaoLista(navegador)
			.consultaUmaContaNaListaDeContas(navegador, contaSeuBarriga.getValor());
		
		assertEquals("[ERRO]Houve algum erro ao tentar consultar uma conta no seu barriga utilizando informação do banco."
				, contaSeuBarriga.getValor(), resultado);

		gerador.alterarUsadoParaFalso(contaSeuBarriga.getId());
	}

	/**
	 * Esse teste acessa o servidor do Seu Barriga, e remove uma conta com o dado
	 * pegado do banco de dados.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testRemoverUmaContaNoSeubarriga() throws Exception {
		
		GeradorDeMassa gerador = new GeradorDeMassa();
		
		MassaDeDado contaSeuBarriga = gerador.obterMassaDeDados();
		
		assertNotNull("[ERRO]Houve erro ao obter um dado do banco de dados", contaSeuBarriga);
		
		String resultado = new HomePage(navegador)
			.clicarNoMenuContas(navegador)
			.clicarNaOpcaoLista(navegador)
			.clicarNoBotaoRemoverConta(navegador, contaSeuBarriga.getValor())
			.getMensagemFeedback(navegador);
		
		assertEquals("[ERRO]Houve algum erro ao tentar remover um conta no seu barriga, utilizando a informação do dado"
				,"Conta removida com sucesso!", resultado);
	}
	
	/**
	 * Insere uma conta no banco de dados com um nome de conta do Seu Barriga
	 * @param nomeConta
	 * @throws Exception
	 */
	private void inserirConta(String nomeConta) throws Exception {
		MassaDAOImpl massaDao = new MassaDAOImpl();
		massaDao.inserirMassa(CONTA_SEU_BARRIGA, nomeConta);
	}
	
	/**
	 * Esse teste edita uma conta no Seu Barriga, com um dado vindo do banco de dados.
	 * @throws Exception
	 */
	@Test
	public void testEditarContaNoSeuBarriga() throws Exception {
		
		GeradorDeMassa gerador = new GeradorDeMassa();

		MassaDeDado contaSeuBarriga = gerador.obterMassaDeDados();
		
		assertNotNull("[ERRO]Houve erro ao obter um dado do banco de dados", contaSeuBarriga);
		
		String novoValor = contaSeuBarriga.getValor() + " Alterado";
		
		String resultado = new HomePage(navegador)
			.clicarNoMenuContas(navegador)
			.clicarNaOpcaoLista(navegador)
			.clicarNoBotaoEditarConta(navegador, contaSeuBarriga.getValor())
			.inserirNovoValorNoCampoNome(navegador, novoValor)
			.clicarNoBotaoSalvar(navegador)
			.getMensagemFeedback(navegador);
		
		assertEquals("[ERRO]Houve erro ao tentar editar um nome no banco", "Conta alterada com sucesso!", resultado);
				
		String contaConsultada = new HomePage(navegador)
			.clicarNoMenuContas(navegador)
			.clicarNaOpcaoLista(navegador)
			.consultaUmaContaNaListaDeContas(navegador, novoValor);
		
		assertNotNull("[ERRO] Houve algum erro ao tentar editar um valor no banco de dados", contaConsultada);
	}
	
	@After
	public void teardown() {
		navegador.quit();
	}
	
}
