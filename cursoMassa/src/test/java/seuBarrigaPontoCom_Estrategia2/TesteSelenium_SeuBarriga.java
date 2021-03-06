package seuBarrigaPontoCom_Estrategia2;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import com.github.javafaker.Faker;

import Utils.BaseUtil;
import pages.HomePage;
import pages.ListaDeContasPage;
import pages.LoginPage;


/**Durante está estratégia de teste, os testes são independentes, podem ser executados em paralelo
 * podem ser executados de maneira manual ou estratégica.
 * Nessa abordagem antes de cada tipo de teste o dado necessário para o teste é inserido antes, todas as vezes.
 * 
 * @author jay
 */
public class TesteSelenium_SeuBarriga extends BaseUtil{

	private WebDriver navegador;
	private Faker faker = new Faker();
	private static String nomeConta;
	
	@Before
	public void setUp() {
		navegador = getChromeWebDriver("http://seubarriga.wcaquino.me/login");
		nomeConta = faker.dragonBall().character();
		
		String alert = new LoginPage(navegador)
				.informarEmail("jose@jose.com")
				.informarSenha("josejose")
				.clicaNoBotaoEntrar()
				.getMensagemFeedback(navegador);
			
		assertEquals("[ERRO] Houveram erros durante a tentativa de login no Seu Barriga", "Bem vindo, jose!", alert);		
	}
	
	
	/**Insere uma conta no servidor do Seu Barriga
	 * 
	 * @author jay
	 */
	@Test
	public void testInserirContaNoSeuBarriga() {
		
		String alert = inserirUmaContaNoSeuBarriga(nomeConta)
				.getMensagemFeedback(navegador);
		
		assertEquals("[ERRO] Houveram erros durante a tentativa de inserir uma conta", "Conta adicionada com sucesso!", alert);
	}
	
	/**Insere uma conta no servidor do Seu Barriga e depois consulta se ela existe
	 * 
	 * @author jay
	 */
	@Test
	public void testConsultarContaNoSeuBarriga() {

		String resultadoDaConsulta = inserirUmaContaNoSeuBarriga(nomeConta)
			.consultaUmaContaNaListaDeContas(navegador, nomeConta);
		
		assertEquals("[ERRO] Houveram erros ao tentar consultar uma conta no Seu Barriga", resultadoDaConsulta, nomeConta);
	}
	
	/**Insere uma conta no servidor do Seu Barriga e depois edita ela
	 * 
	 * @author jay
	 */
	@Test
	public void testEditarContaNoSeuBarriga() {
		
		String nomeContaAlterado = nomeConta + " Alterado!";
		
		String nomeNaListaDoSeuBarriga = inserirUmaContaNoSeuBarriga(nomeConta)
			.clicarNoBotaoEditarConta(navegador, nomeConta)
			.inserirNovoValorNoCampoNome(navegador, nomeContaAlterado)
			.clicarNoBotaoSalvar(navegador)
			.consultaUmaContaNaListaDeContas(navegador, nomeContaAlterado);

		assertEquals("[ERRO] Houveram erros ao tentar editar uma conta no Seu Barriga", nomeNaListaDoSeuBarriga, nomeContaAlterado);
		
	}
	
	/**Insere uma conta no servidor do Seu Barriga e depois remove ela
	 * 
	 * @author jay
	 */
	@Test
	public void testRemoverContaNoSeuBarriga() {
		String resultadoDaOperacao = inserirUmaContaNoSeuBarriga(nomeConta)
			.clicarNoBotaoRemoverConta(navegador, nomeConta)
			.getMensagemFeedback(navegador);

		assertEquals("[ERRO] Houveram erros ao tentar remover uma conta no Seu Barriga", "Conta removida com sucesso!", resultadoDaOperacao);
	}

	public ListaDeContasPage inserirUmaContaNoSeuBarriga(String nomeConta) {
		return new HomePage(navegador)
			.clicarNoMenuContas(navegador)
			.clicarNaOpcaoAdicionar(navegador)
			.inserirNome(nomeConta)
			.clicarNoBotaoSalvar(navegador);
	}
	
	@After
	public void tearDown() {
		navegador.quit();
	}
	
}
