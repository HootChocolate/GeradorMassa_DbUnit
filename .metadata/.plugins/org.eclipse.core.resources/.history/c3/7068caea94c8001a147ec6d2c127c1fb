package seuBarrigaPontoCom_estrategia5;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import Utils.BaseUtil;
import pages.HomePage;
import pages.LoginPage;

/**
 * Durante este teste foi usado a estratégia 5, onde os dados são resetados no banco de dados do Seu Barriga 
 * no inicio da execução da classe.
 * Assim os testes irão ser executados sabendo com antecedencia dos dados que serão utilizados,
 * caso tivesse acesso ao banco de dados do Seu Barriga, poderia ser inserido no banco de dados.
 * 
 * @author jay
 */
public class TestSelenium_SeuBarriga extends BaseUtil{

	private static WebDriver navegador;
	
	/**
	 * Abre o navegador uma vez apenas
	 */
	@BeforeClass
	public static void beforeClass() {
		
		navegador = getChromeWebDriver("https://seubarriga.wcaquino.me/login");
		
		new LoginPage(navegador)
			.informarEmail("jose@jose.com")
			.informarSenha("josejose")
			.clicaNoBotaoEntrar()
			.clicarResetDadosBancoDeDados(navegador);
	}
	
	/**
	 * Consulta a conta "Conta para saldo" no servidor do Seu Barriga
	 */
	@Test
	public void testConsultarConta() {
		String consultaConta = "Conta para saldo";
		
		String conta =  consultarUmaConta(consultaConta);
		
		assertEquals("[ERRO]Houve erro ao tenttar consultar uma conta no banco do Seu Barriga", consultaConta, conta);
	}
	
	/**
	 * Altera uma conta "Conta para extrato" no servidor do Seu Barriga
	 */
	@Test
	public void testAlterarConta() {
					
		String nome = "Conta para extrato";
		String novoNome = "Conta para extrato - Altada!";
		
		new HomePage(navegador)
			.clicarNoMenuContas(navegador)
			.clicarNaOpcaoLista(navegador)
			.clicarNoBotaoEditarConta(navegador, nome)
			.inserirNovoValorNoCampoNome(navegador, novoNome)
			.clicarNoBotaoSalvar(navegador);
		
		String resultado = new HomePage(navegador)
			.clicarNoMenuContas(navegador)
			.clicarNaOpcaoLista(navegador)
			.clicarNoBotaoEditarConta(navegador, novoNome)
			.getValorCampoNome(navegador);
		
		assertEquals("[ERRO]Houve um erro ao tentar alterar uma conta no banco", resultado, novoNome);
	}
	
	/**
	 * Remove a conta "Conta mesmo nome" no servidor do Seu Barriga
	 */
	@Test
	public void testRemoverConta() {
		
		String nomeConta = "Conta mesmo nome";
		
		String resultado = new HomePage(navegador)
			.clicarNoMenuContas(navegador)
			.clicarNaOpcaoLista(navegador)
			.clicarNoBotaoRemoverConta(navegador, nomeConta)
			.getMensagemFeedback(navegador);
		
		assertEquals("[ERRO]Houve algum erro ao tentar remover uma conta no seu barriga", "Conta removida com sucesso!", resultado);
	
	}
	
	@AfterClass
	public static void tearDown() {
		navegador.quit();
	}
	
	private String consultarUmaConta(String nomeConta) {
		return new HomePage(navegador)
				.clicarNoMenuContas(navegador)
				.clicarNaOpcaoLista(navegador)
				.clicarNoBotaoEditarConta(navegador, nomeConta)
				.getValorCampoNome(navegador);
	}

}
