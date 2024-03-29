package seuBarrigaPontoCom_Estratégia1;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.WebDriver;

import com.github.javafaker.Faker;

import Utils.BaseUtil;
import pages.EditarContaPage;
import pages.HomePage;
import pages.LoginPage;
/**Durante este não foi utilizada a estratégia de ordenação dos testes por ordem alfabética,
 * garantindo a sequência correta das operações de inserção, consulta, alteração e remoção de um dado no servidor do Seu Barriga.
 * Essa estratégia não garante a independencia do teste, nem o paralelismo, pois eles estão dependente de testes anteriores.
 * Não é possível fazer uma consulta se o dado não tiver sido inserido antes.
 * 
 * 
 * @author jay
 *@date 
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TesteSelenium_SeuBarriga extends BaseUtil{

	private WebDriver navegador;
	private static String nomeConta = new Faker().harryPotter().character();
	
	@Before
	public void setUp() {
	
		navegador = getChromeWebDriver("https://seubarriga.wcaquino.me/login");
		
		String mensagemRecebida  = new LoginPage(navegador)
		.informarEmail("jose@jose.com")
		.informarSenha("josejose")
		.clicaNoBotaoEntrar()
		.getMensagemFeedback(navegador);
		
		assertEquals("[Erro] Durante a tentativa de realizar login", "Bem vindo, jose!", mensagemRecebida);
		
	}
	
	/**
	 * Primeiro teste, insere uma conta no servidor do Seu Barriga 
	 * 
	 * @author jay
	 */
	@Test
	public void test_1_inserirContaTest() {
		
		String alert = new HomePage(navegador)
			.clicarNoMenuContas(navegador)
			.clicarNaOpcaoAdicionar(navegador)
			.inserirNome(nomeConta)
			.clicarNoBotaoSalvar(navegador)
			.getMensagemFeedback(navegador);
				
		assertEquals("[ERRO] Houveram erros ao tentar inserir uma conta no banco de dados", "Conta adicionada com sucesso!", alert);
	}

	/**Edita uma conta no servidor do Seu Barriga
	 * 
	 * @author jay
	 */
	@Test
	public void test_2_editarContaTest() {
		
		new HomePage(navegador)
			.clicarNoMenuContas(navegador)
			.clicarNaOpcaoLista(navegador)
			.clicarNoBotaoEditarConta(navegador, nomeConta);
		
		nomeConta = nomeConta + " Alterado!";
		
		String alert = new EditarContaPage(navegador)
			.inserirNovoValorNoCampoNome(navegador, nomeConta)
			.clicarNoBotaoSalvar(navegador)
			.getMensagemFeedback(navegador);

		assertEquals("[ERRO] Houveram problemas ao tentar alterar o nome de uma conta", "Conta alterada com sucesso!", alert);
	}
	
	/**Consulta uma conta do servidor do Seu Barriga
	 * 
	 * @author jay
	 */
	@Test
	public void test_3_consultaContaTest() {
		
		String resultado = new HomePage(navegador)
			.clicarNoMenuContas(navegador)
			.clicarNaOpcaoLista(navegador)
			.consultaUmaContaNaListaDeContas(navegador, nomeConta);
		
		System.out.println(resultado);
		assertEquals("[ERRO] Houveram erros ao tentar consultar uma conta no servidor.", nomeConta, resultado);
	}

	/**Remove uma conta do servidor do Seu Barriga
	 * 
	 * @author jay
	 */
	@Test
	public void test_4_removerContaTest() {
		String alert = new HomePage(navegador)
			.clicarNoMenuContas(navegador)
			.clicarNaOpcaoLista(navegador)
			.clicarNoBotaoRemoverConta(navegador, nomeConta)
			.getMensagemFeedback(navegador);
		
		assertEquals("[ERRO] Houveram erros ao tentar remover uma conta", "Conta removida com sucesso!", alert);
	}
	
	@After
	public void teardown() {
		navegador.quit();
	}
}