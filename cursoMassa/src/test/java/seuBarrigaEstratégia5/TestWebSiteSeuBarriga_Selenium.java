package seuBarrigaEstratégia5;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import Utils.BaseUtil;
import pages.HomePage;
import pages.LoginPage;

/**Essa estratégia de teste consiste em gerar os dados que serão necessários para as os testes.
 * Em cada teste é utilizado um dado, dos que foram gerados no inicio.
 * 
 * @author jay
 */
public class TestWebSiteSeuBarriga_Selenium extends BaseUtil{

	private static WebDriver navegador;
	
	/**
	 * Reseta os dados do banco de dados da aplicaçao, antes de todos os testes.
	 */
	@BeforeClass
	public static void beforeClass() {
		navegador = getChromeWebDriver("https://seubarriga.wcaquino.me/login");
		
		String resultado = new LoginPage(navegador)
			.informarEmail("jose@jose.com")
			.informarSenha("josejose")
			.clicaNoBotaoEntrar()
			.clicarResetDadosBancoDeDados(navegador)
			.getMensagemFeedback(navegador);
		
		assertEquals("[ERRO]Houve erro ao tentar realizar o login na aplicação", "Bem vindo, jose!", resultado);
	}
	
	/**
	 * A partir dos dados gerados previamente, utilizando a estratégia 5,
	 * consulta a conta 'Conta para extrato' e faz o assert ao final
	 */
	@Test
	public void testConsultarConta() {
		
		String contaParaConsulta = "Conta para extrato";
		
		String resultadoConsulta = new HomePage(navegador)
				.clicarNoMenuContas(navegador)
				.clicarNaOpcaoLista(navegador)
				.consultaUmaContaNaListaDeContas(navegador, contaParaConsulta);
		
		assertEquals("[ERRO]Houve erro ao tentar consultar a conta "+ contaParaConsulta + ", pré configurado no BeforeClass",
				contaParaConsulta, resultadoConsulta);
	}

	/**
	 * A partir dos dados gerados previamente, utilizando a estratégia 5,
	 * altera a conta 'Conta para alterar' para 'Conta para alterar Ok'
	 */
	@Test
	public void testAlteraConta() {

		String nomeConta = "Conta para alterar";
		String nomeContaAlterado = nomeConta + " Ok";
		
		String consultaContaNoBanco = new HomePage(navegador)
			.clicarNoMenuContas(navegador)
			.clicarNaOpcaoLista(navegador)
			.clicarNoBotaoEditarConta(navegador, nomeConta)
			.inserirNovoValorNoCampoNome(navegador, nomeContaAlterado)
			.clicarNoBotaoSalvar(navegador)
			.consultaUmaContaNaListaDeContas(navegador, nomeContaAlterado);
		
		assertEquals("[ERRO]Houve erro ao tentar alterar uma conta no banco, pré configurada no BeforeClass",
				nomeContaAlterado, consultaContaNoBanco);
	}
	
	/**
	 * A partir dos dados gerados previamente, utilizando a estratégia 5,
	 * remove uma 'Conta mesmo nome' a conta do banco.
	 */
	@Test
	public void testRemoverConta() {
		String contaToRemove = "Conta mesmo nome";
		
		String resultado = new HomePage(navegador)
			.clicarNoMenuContas(navegador)
			.clicarNaOpcaoLista(navegador)
			.clicarNoBotaoRemoverConta(navegador, contaToRemove)
			.getMensagemFeedback(navegador);
		
		assertEquals("[ERRO]Houve erro ao tentar remover uma conta no banco", "Conta removida com sucesso!", resultado);
	}
	
	@AfterClass
	public static void teardown() {
		navegador.quit();
	}
}
