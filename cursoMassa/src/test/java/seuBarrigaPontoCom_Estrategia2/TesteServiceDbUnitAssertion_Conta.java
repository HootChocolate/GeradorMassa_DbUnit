package seuBarrigaPontoCom_Estrategia2;

import static org.junit.Assert.*;

import java.util.List;

import org.dbunit.Assertion;
import org.dbunit.assertion.DiffCollectingFailureHandler;
import org.dbunit.assertion.Difference;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.filter.DefaultColumnFilter;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.junit.Test;

import br.ce.wcaquino.entidades.Conta;
import br.ce.wcaquino.service.ContaService;
import dbUnit.ImportExport;

/**
 * Faz os testes de serviços relacionados a conta,
 * durante esse teste foi utilizado DbUnit, e as Assertion do DbUnit
 * para comparar os resultados dos bancos.
 * Nesse teste, os dados necessários para cada execução de cada teste,
 * são inseridos antes de cada teste.
 * 
 * @date 21/07/2020
 * @author jay
 */
public class TesteServiceDbUnitAssertion_Conta {

	private ContaService contaService = new ContaService();
	
	/**
	 * Testa a inserção de uma conta, fazendo o Assertion utilizando DbUnit 	 
	 * @throws Exception
	 */
//	@Test
	public void testInserirConta_AssertionDbUnit() throws Exception {
		
		ImportExport.importarDadoParaOBanco("est3_umUsuario.xml");
		
		Conta conta = new Conta("Conta para teste", 1L);
		
		Conta contaSalva = contaService.salvar(conta);
		
		IDataSet bancoAtual = ImportExport.estadoAtualBanco();
		
		FlatXmlDataSet bancoEsperado = ImportExport.estadoEsperadoBanco("est3_umaConta.xml");
		
		// Erros de comparação, como comparação de id.
		DiffCollectingFailureHandler diferencas = new DiffCollectingFailureHandler();
		
		Assertion.assertEquals(bancoEsperado, bancoAtual, diferencas);
		
		List<Difference> listDiferencas = diferencas.getDiffList();
		
		boolean erroDeInsercao = false;
		
		for(Difference diff: listDiferencas) {
			if(diff.getActualTable().getTableMetaData().getTableName().equals("contas")) {
				if(diff.getColumnName().equals("id")) {

					// Se o id for diferente do que deveria ter salvo, da erroDeInsercao
					if(diff.getActualValue().toString().equals(contaSalva.getId().toString())) {
						continue;
					} else {
						erroDeInsercao = true;
						}
					} else {
						erroDeInsercao = true;
						}
				}
			}
		assertFalse("[ERRO]Houve erro ao tentar inserir uma conta no banco", erroDeInsercao);
	}	

	
	/**
	 * Testa a alteração de uma conta no banco, em seguida faz uma Assertion
	 * utilizando DbUnit para comparar o banco atual com o esperado(xml).
	 * No final foi exluido uma coluna para ser ignorada durante a asserção.
	 * 
	 * @throws Exception 
	 */
	@Test
	public void testAlterarConta_AssertionDbUnit() throws Exception {
		// Prepara o banco
		ImportExport.importarDadoParaOBanco("est3_umaConta.xml");
		
		Conta contaAlterar = contaService.findByName("Conta para teste");
		
		String novoNome = contaAlterar.getNome() + ", Alterado!";
		
		contaAlterar.setNome(novoNome);
		
		contaService.salvar(contaAlterar);
		
		IDataSet estadoAtualBanco = ImportExport.estadoAtualBanco();
		FlatXmlDataSet estadoEsperadoBanco = ImportExport.estadoEsperadoBanco("est3_umaContaAlterada.xml");
		
		ITable estadoAtualBancoFiltrado = DefaultColumnFilter
				.excludedColumnsTable(estadoAtualBanco.getTable("usuarios"), new String[] {"conta_principal_id"});
		
		ITable resultadoEsperadoBancoFilter = DefaultColumnFilter
				.excludedColumnsTable(estadoEsperadoBanco.getTable("Usuarios"), new String[] {"conta_principal_id"});

		Assertion.assertEquals(resultadoEsperadoBancoFilter, estadoAtualBancoFiltrado);

	}
	
//	@Test
	public void testRemoverConta_AssertionDbUnit() {
		
	}
}
