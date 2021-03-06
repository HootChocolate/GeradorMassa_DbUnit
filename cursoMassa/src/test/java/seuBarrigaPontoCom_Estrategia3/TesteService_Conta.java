package seuBarrigaPontoCom_Estrategia3;

import static org.junit.Assert.*;

import org.junit.Test;

import br.ce.wcaquino.entidades.Conta;
import br.ce.wcaquino.entidades.MassaDeDado;
import br.ce.wcaquino.service.ContaService;

public class TesteService_Conta {
	
	private MassaDAOImpl massaDao = new MassaDAOImpl();
	private ContaService contaService = new ContaService();
	
	
	/**
	 * Consulta se uma conta existe na tabela "Contas", utilizando o gerador de massa de dados
	 * para verificar dados disponíveis.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testConsultarConta() throws Exception {

		MassaDeDado massaConta = massaDao.obterMassaDeDados(GeradorDeMassa.CONTA);
		Conta contaConsulta = contaService.findByName(massaConta.getValor());
	
		assertNotNull("[ERRO]Houveram erros ao tentar consultar uma conta no catalogo de contas\n", contaConsulta);
	}
	
	/**
	 * Edita uma conta no banco, da tabela "Contas", utilizando a massa de dados do gerador, se houver massa disponível.
	 * @throws Exception
	 */
	@Test
	public void testEditarUmaConta() throws Exception {
		
		MassaDeDado massaConta = massaDao.obterMassaDeDados(GeradorDeMassa.CONTA);
		
		String nomeOriginal = massaConta.getValor();

		String novoValor = nomeOriginal + " Alterado!";
		
		Conta contaParaEditar = contaService.findByName(massaConta.getValor());
		
		contaParaEditar.setNome(novoValor);
		
		Conta contaEditada  = contaService.salvar(contaParaEditar);
		
		assertEquals("[ERRO]Houve erro ao tentar alterar o nome de uma conta utilizando a massa de dado", novoValor, contaEditada.getNome());
	}
	
	/**
	 * Remove uma conta da tabela contas, utilizando como dado o dado gerado pelo gerador, se houver um valor disponível.
	 * @throws Exception
	 */
	@Test
	public void testRemoverUmaConta() throws Exception {
		
		MassaDeDado massaDeDado = massaDao.obterMassaDeDados(GeradorDeMassa.CONTA);
		
		Conta contaParaRemover = contaService.findByName(massaDeDado.getValor());
		
		contaService.delete(contaParaRemover);
		
		Conta contaRemovida = contaService.findById(contaParaRemover.getId());
		
		assertNull("[ERRO]Houve erro ao tentar remover uma conta da tabela de contas, utilizando a massa de dado", contaRemovida);
		
	}
	

}
