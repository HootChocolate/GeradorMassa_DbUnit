package soapOperaScenario;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import br.ce.wcaquino.dao.SaldoDAO;
import br.ce.wcaquino.dao.impl.SaldoDAOImpl;
import br.ce.wcaquino.entidades.Conta;
import br.ce.wcaquino.entidades.TipoTransacao;
import br.ce.wcaquino.entidades.Transacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.service.ContaService;
import br.ce.wcaquino.service.TransacaoService;
import br.ce.wcaquino.service.UsuarioService;

/**
 * Durante esse teste foi realizado vários testes, com dois usuários cada um com duas contas.
 * Esse teste testa a interação entre as operações, fazendo todas em apenas uma classe de teste.
 * As regras do teste são descrita no inicio da class.
 * @author jay
 */
public class TestGetSaldoConta {
	/**  REGRAS:
	 * + Deve considerar uma transação do usuario principal
	 * + Deve considerar uma transação da conta principal
	 * + Deve considerar uma transação de uma conta secundária do usuario principal
	 * Deve considerar o dia da transação
	 * 		+ ontem / + hoje / + amanha
	 * + Deve considerar o status da transação
	 * + Deve considerar soma das despesas
	 * + Deve considerar soma de receita
	 * 
	 * + Deve considerar uma transação de outro usuario
	 * + Deve considerar uma transação de outra conta do outro usuario
	 */
	
	private UsuarioService usuarioService = new UsuarioService();
	private ContaService contaService = new ContaService();
	private TransacaoService transacaoService = new TransacaoService();
	private SaldoDAO saldoDao = new SaldoDAOImpl(); 

	@SuppressWarnings("deprecation")
	/**
	 * Testa transações entre duas contas de dois usuarios.
	 * Envolvendo regras descrita no inicio da classe.
	 * @throws Exception
	 */
	@Test
	public void deveDevolverOSaldoCorreto() throws Exception {
		Usuario usuarioPrincipal = new Usuario("Usuario Principal", "usuario@principal.com", "123");
		Usuario usuarioSecundario = new Usuario("Usuario Secundario", "usuario@secundario.com", "123");
		
		usuarioService.salvar(usuarioPrincipal);
		usuarioService.salvar(usuarioSecundario);
		
		Conta contaPrincipal = new Conta("Conta01, principal", usuarioPrincipal.getId());
		Conta contaPrincipalDois = new Conta("Conta02, principal", usuarioPrincipal.getId());
		Conta contaSecundaria = new Conta("Conta01, secundaria", usuarioSecundario.getId());
		Conta contaSecundariaDois= new Conta("Conta02, secundaria", usuarioSecundario.getId());
		
		contaService.salvar(contaPrincipal);
		contaService.salvar(contaPrincipalDois);
		contaService.salvar(contaSecundaria);
		contaService.salvar(contaSecundariaDois);
		
		// contaPrincipal - SaldoPrincipal = 2		
		transacaoService.salvar(new Transacao("Transação principal, Conta01", "Envolvido", TipoTransacao.RECEITA, 
				new Date(), 2d, true, contaPrincipal, usuarioPrincipal));
		
		// contaSecundaria - SaldoPrincipal = 2
		transacaoService.salvar(new Transacao("Transação principal, Conta02", "Envolvido", TipoTransacao.RECEITA, 
				new Date(), 4d, true, contaPrincipalDois, usuarioPrincipal));
		
		// contaPrincipal - SaldoPrincipal = 8
		transacaoService.salvar(new Transacao("Transação principal ontem, Conta01", "Envolvido", TipoTransacao.RECEITA, 
				getDataAtualMaisDias(-1), 6d, true, contaPrincipal, usuarioPrincipal));
		
		// contaPrincipal - SaldoPrincipal = 8
		transacaoService.salvar(new Transacao("Transação principal amanha, Conta01", "Envolvido", TipoTransacao.RECEITA, 
				getDataAtualMaisDias(1), 8d, false, contaPrincipal, usuarioPrincipal));
		
		// contaPrincipal - SaldoPrincipal = -8 
		transacaoService.salvar(new Transacao("Transação principal despesa, Conta01", "Envolvido", TipoTransacao.DESPESA, 
				new Date(), 16d, true, contaPrincipal, usuarioPrincipal));
		
		// conta com saldo negativo da azar - SaldoPrincipal = 24
		transacaoService.salvar(new Transacao("Transação principal da Sorte, Conta01", "Envolvido", TipoTransacao.RECEITA, 
				new Date(), 32d, true, contaPrincipal, usuarioPrincipal));
		
		// contaSecundaria - Saldo: 64 
		transacaoService.salvar(new Transacao("Transacao Secundaria, Conta01", "Envolvido", TipoTransacao.RECEITA,
				new Date(), 64d, true, contaSecundaria, usuarioSecundario));

		// contaSecundariaDois - Saldo: 128 
		transacaoService.salvar(new Transacao("Transacao Secundaria, Conta02", "Envolvido", TipoTransacao.RECEITA,
				new Date(), 128d, true, contaSecundariaDois, usuarioSecundario));
		
		assertEquals("[ERRO]Houve erro ao pegar saldo da conta do Usuario Principal, Conta Principal"
				,new Double(24d), saldoDao.getSaldoConta(contaPrincipal.getId()));
		assertEquals("[ERRO]Houve erro ao pegar saldo da conta do Usuario Principal, Conta Secundaria"
				,new Double(4d), saldoDao.getSaldoConta(contaPrincipalDois.getId()));
		assertEquals("[ERRO]Houve erro ao pegar saldo da conta do Usuario Secundario, Conta 01"
				,new Double(64d), saldoDao.getSaldoConta(contaSecundaria.getId()));
		assertEquals("[ERRO]Houve erro ao pegar saldo da conta do Usuario Secundario, Conta 02"
				,new Double(128d), saldoDao.getSaldoConta(contaSecundariaDois.getId()));		
	}
	
	public Date getDataAtualMaisDias(int dias) {
		Date hoje = new Date();
		Calendar calendar = Calendar.getInstance();
		
		calendar.setTime(hoje);
		calendar.add(Calendar.DATE, dias);
		hoje = calendar.getTime();
		
		return hoje;
	}
}
