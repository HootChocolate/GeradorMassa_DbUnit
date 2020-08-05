package seuBarrigaPontoCom_estrategia4;			

import static org.junit.Assert.*;

import javax.annotation.meta.When;

import org.junit.Test;

import br.ce.wcaquino.dao.utils.ConnectionFactory;
import br.ce.wcaquino.entidades.Conta;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.service.ContaService;
import br.ce.wcaquino.service.UsuarioService;
import dbUnit.ImportExport;

/**
 * Teste utilizando a estratégia de gerenciamento de massa de dados #4 com Geração de dados do DbUnit
 * foi utilizado a configuração de sequences neste teste.
 * 
 * @author jay
 */
public class TestServiceDbUnitSequence_Conta {

	private ContaService contaService = new ContaService();
	private UsuarioService usuarioService = new UsuarioService();
	
//	@Test
	public void testInserirConta() throws Exception {
		
		ImportExport.atualizarSequences();
		
		ImportExport.importarDadoParaOBanco("est4_inserirContaSequence.xml"); // Define o cenário que o banco terá
		
		String nomeConta = "Conta nova";
		
		Usuario usuario = usuarioService.findById(1L); // Esse id, está definido no arquivo acima
		
		Conta contaNova = new Conta(nomeConta, usuario);
		
		contaService.salvar(contaNova);	
		
		Conta contaSalva = contaService.findByName(nomeConta);
		
		assertEquals("[ERRO]Houve erro ao tentar inserir uma conta utilizando DbUnit", nomeConta, contaSalva.getNome());
		
	}

	/**
	 * Este teste de serviço, consulta uma conta do banco,
	 * utilizando DbUnit com arquivo XML.
	 * 
	 * @throws Exception
	 */
//	@Test
	public void testConsultaConta() throws Exception {
		ImportExport.importarDadoParaOBanco("est4_umaConta.xml"); // Pega um banco com uma conta
		
		String nomeConsulta = "Conta para teste";
		
		Conta contaConsultada = contaService.findByName(nomeConsulta);
		
		assertNotNull("[ERRO]Houve um erro ao tentar consultar um conta no banco de dados", contaConsultada);
	}
	

	/**
	 * Este teste de serviço, altera uma conta do banco,
	 * utilizando DbUnit com arquivo XML.
	 * 
	 * @throws Exception
	 */
//	@Test
	public void testAlterarConta() throws Exception {
		ImportExport.importarDadoParaOBanco("est4_umaConta.xml");

		String nomeRemover = "Conta para teste";
		String novoNome = nomeRemover + ": Alterado!";
		
		Conta conta = contaService.findByName(nomeRemover);
		
		conta.setNome(novoNome); 
		
		contaService.salvar(conta);
		
		Conta novaConta = contaService.findByName(novoNome);
		
		assertNotNull("[ERRO]Houve um erro ao tentar alterar um conta no banco de dados", novaConta);
	}
	
	/**
	 * Este teste de serviço, remove uma conta do banco,
	 * utilizando DbUnit com arquivo XML.
	 * 
	 * @throws Exception
	 */
//	@Test
	public void testRemoverConta() throws Exception {
		ImportExport.importarDadoParaOBanco("est4_umaConta.xml");	
		
		String remover = "Conta para teste";
		
		Conta contaParaRemover = contaService.findByName(remover);
		
		contaService.delete(contaParaRemover);
		
		Conta contaRemovida = contaService.findByName(remover);
		
		assertNull("[ERRO]Houve algum erro ao tentar remover uma conta do banco", contaRemovida);
	}
	
}
