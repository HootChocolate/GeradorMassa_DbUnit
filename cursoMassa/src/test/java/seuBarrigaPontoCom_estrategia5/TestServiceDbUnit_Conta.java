package seuBarrigaPontoCom_estrategia5;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import br.ce.wcaquino.entidades.Conta;
import br.ce.wcaquino.service.ContaService;
import dbUnit.ImportExport;

/**
 * Durante este teste os dados são todos inseridos antes dos testes.
 * A inserção dos dados é feito via DbUnit, com um arquivo.xml.
 * @author jay
 */
public class TestServiceDbUnit_Conta {

	private ContaService contaService = new ContaService();
	
	@BeforeClass
	public static void setUp() throws Exception  {
		ImportExport.importarDadoParaOBanco("est5_inserirContas.xml");
	}
	
	/**
	 * Consulta a conta "Conta para consultar" no banco de dados
	 * @throws Exception
	 */
	@Test
	public void testConsultarConta() throws Exception {
		
		Conta contaConsultada = contaService.findByName("Conta para consultar");
		
		assertNotNull("[ERRO]Houve algum erro ao tentar consultar uma conta no banco", contaConsultada);
	}
	
	/**
	 * Remove a conta "Conta para remover" no banco de dados
	 * @throws Exception
	 */
	@Test
	public void testRemoverConta() throws Exception {
		String nomeConta = "Conta para remover";
		
		Conta contaRemover = contaService.findByName(nomeConta);
		
		contaService.delete(contaRemover);
		
		Conta contaRemovida = contaService.findByName(nomeConta);
		
		assertNull("[ERRO]Houve algum erro ao tentar remover uma conta do banco", contaRemovida);
	}
}
