package seuBarrigaPontoCom_Estrategia3;

import java.sql.Connection;
import java.sql.SQLException;

import org.openqa.selenium.WebDriver;

import com.github.javafaker.Faker;

import Utils.BaseUtil;
import br.ce.wcaquino.dao.utils.ConnectionFactory;
import br.ce.wcaquino.entidades.Conta;
import br.ce.wcaquino.entidades.MassaDeDado;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.service.ContaService;
import br.ce.wcaquino.service.UsuarioService;
import pages.LoginPage;

/**Essa class acessa o servidor do Seu Barriga, gera dados e adiciona eles no banco de dados.
 * Essa classe seria o "Robozinho" gerador 
 * @author jay
 *
 */
public class GeradorDeMassa extends BaseUtil{
	
	protected static GeradorDeMassa gerador = new GeradorDeMassa();
	protected static String CONTA_SEU_BARRIGA = "CONTA_SrB";
	protected static String CONTA = "CONTA";
	protected static String USUARIO = "USUARIO";
	
	private static Usuario usuarioGlobal = new Usuario();
	private static MassaDAOImpl massaDao = new MassaDAOImpl();
	
	/**Método responsável por acessar o servidor do Seu Barriga, criar uma conta la
	 * e salvar a conta no banco.
	 * 
	 * @throws Exception
	 */
	public void acessarAplicacao() throws Exception {
		
		WebDriver navegador = getChromeWebDriver("https://seubarriga.wcaquino.me/login");
		
		Faker faker = new Faker();
		String nomeConta = faker.gameOfThrones().character() + " " + faker.gameOfThrones().dragon();
		
		String contaSalva = new LoginPage(navegador)
			.informarEmailESenha("jose@jose.com", "josejose")
			.clicaNoBotaoEntrar()
			.clicarNoMenuContas(navegador)
			.clicarNaOpcaoAdicionar(navegador)
			.inserirNome(nomeConta)
			.clicarNoBotaoSalvar(navegador)
			.consultaUmaContaNaListaDeContas(navegador, nomeConta);
		
		navegador.quit();
		
		MassaDAOImpl geradorDeMassa = new MassaDAOImpl();
		geradorDeMassa.inserirMassa(CONTA_SEU_BARRIGA, contaSalva);
		
	}
	
	/**Este método acessa a aplicação e gera a massa de dados
	 * 
	 * @param quantidade
	 * @throws Exception
	 */
	public static void gerarMassaDeDadosSeleniumSeuBarriga(int quantidade) throws Exception {
		
		for(int i = 0 ; i < quantidade ; i++) {
			gerador.acessarAplicacao();
		}
	}
	
	/**
	 * Este método acessa o banco de dados e pega o valor de uma conta no servidor do Seu Barriga
	 * @return
	 * @throws Exception
	 */
	public static MassaDeDado obterMassaDeDados() throws Exception {
		return new MassaDAOImpl().obterMassaDeDados(CONTA_SEU_BARRIGA);
	}
	
	
	public long obterIdDaConta(MassaDeDado nomeDaConta) throws ClassNotFoundException, SQLException {
		
		return new MassaDAOImpl().obterIdDaConta(nomeDaConta);
	}

	/**
	 * Em alguns casos pode ser feito uma consulta e não alterar o valor do dado mas marca-lo como utilizado,
	 * nesse caso ele pode ser inutilizado de novo
	 * @param id
	 * @throws Exception 
	 */
	public void alterarUsadoParaFalso(long id) throws Exception {
		new MassaDAOImpl().alterarUsadoParaFalso(id);
		
	}
	
	protected static void gerarMassaDeDadosServico() throws Exception {
		Faker faker = new Faker();

		MassaDAOImpl gerador = new MassaDAOImpl();

		ContaService contaService = new ContaService();
		
		String nomeConta = faker.gameOfThrones().character() + " - " + faker.number().randomDigit();
		
		Conta conta = new Conta(nomeConta, usuarioGlobal);

		contaService.salvar(conta);

		gerador.inserirMassa(CONTA, conta.getNome());		
	}
	
	public static void main(String[] args) throws Exception {
		
		inserirUsuario();
		
//		gerarMassaDeDadosSeleniumSeuBarriga(5);			
		
//		resetarValoresNaMassaDeDados();
		
		for(int i = 0 ; i < 5 ; i++) {
			gerarMassaDeDadosServico();
		}
		
	}

	private static void inserirUsuario() throws Exception {
		Faker faker = new Faker();
		
		UsuarioService usuarioService = new UsuarioService();
		MassaDAOImpl gerador = new MassaDAOImpl();
		
		String nome = faker.gameOfThrones().character() + " " + faker.gameOfThrones().dragon();
		String email = faker.internet().emailAddress();
		String senha = faker.internet().password();
		
		usuarioGlobal = new Usuario(nome, email, senha);

		usuarioService.salvar(usuarioGlobal);

		gerador.inserirMassa(GeradorDeMassa.USUARIO, usuarioGlobal.getNome());;
		
	}

	private static void resetarValoresNaMassaDeDados() throws ClassNotFoundException, SQLException {
		
		Connection conexao = ConnectionFactory.getConnection();
		
		conexao.createStatement().executeUpdate("DROP TABLE IF EXISTS public.massas");
		conexao.createStatement().executeUpdate("DROP SEQUENCE IF EXISTS public.massas_id_seq");
		conexao.createStatement().executeUpdate("CREATE SEQUENCE public.massas_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1");
		conexao.createStatement().executeUpdate("CREATE TABLE public.massas ("
				+ "id integer NOT NULL DEFAULT nextval('massas_id_seq'::regclass), "
				+ "tipo character varying(255) NOT NULL, "
				+ "valor character varying(255) NOT NULL, "
				+ "usada boolean NOT NULL DEFAULT false, "
				+ "CONSTRAINT massas_pkey PRIMARY KEY (id) )");
		
		ConnectionFactory.closeConnection();
		
	}

}
