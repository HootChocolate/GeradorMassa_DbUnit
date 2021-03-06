package seuBarrigaPontoCom_Estratégia1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import br.ce.wcaquino.dao.impl.ContaDAOImpl;
import br.ce.wcaquino.dao.impl.UsuarioDAOImpl;
import br.ce.wcaquino.entidades.Conta;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.service.ContaService;
import br.ce.wcaquino.service.UsuarioService;

/**Durante este teste foi utilizada a estratégia de ordenação dos métodos de testes,
 * garantindo uma correta ordem entre a criação, consulta, alteração e remoção do dado
 * no banco de dados.
 * Os testes foram realizados apenas no nivel de serviço, sem nenhuma interface ser mostrada.
 * Esses métodos não são independentes, e não permitem o paralelismo,
 * mas garantem que sejam executados manual e automaticamente, e sem o controle dos dados 
 * 
 * @author jay
 * @date 12/07/2020
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TesteServiceSeuBarriga_Conta {

	private static ContaService contaService = new ContaService();
	private static UsuarioDAOImpl usuarioDao;
	
	private static Usuario usuarioGlobal;
	private static Conta contaGlobal;
	
	/**Testa a criação de um usuário, e posteriormente a inserção de uma conta
	 * atrelada a este usuário.
	 * 
	 * @throws Exception
	 * @author jay
	 */
	@Test
	public void test_1_inserirUmaContaNoBanco() throws Exception {
		
		usuarioDao = new UsuarioDAOImpl();
		
		usuarioGlobal = new Usuario("nome", "email@email.com", "122333");
		usuarioDao.save(usuarioGlobal);
		
		contaGlobal = new Conta("Conta 01", usuarioGlobal);
		
		Conta contaInserida = contaService.salvar(contaGlobal);
		
		assertNotNull("[ERRO] Houveram problemas ao tentar salvar uma conta no banco", contaInserida);
		
	}
	
	/**Este teste consulta uma conta no banco de dados
	 * 
	 * @author jay
	 * @throws Exception
	 */
	@Test
	public void test_2_consultaUmaContaNoBanco() throws Exception {
		
		Conta contaInserida = contaService.findById(usuarioGlobal.getId());
		
		assertNotNull("[ERRO] Houveram problemas ao tentar consultar uma conta no banco", contaInserida);
	}
	
	/**Este teste edita a conta do usuario global no banco de dados
	 * 
	 * @throws Exception
	 * @author jay
	 */
	@Test
	public void test_3_editaUmaContaNoBanco() throws Exception {
		
		ContaDAOImpl contaDao = new ContaDAOImpl();
		String novoNome = contaGlobal.getNome() + " Alterada!";
		
		contaGlobal.setNome(novoNome);
		
		contaDao.edit(contaGlobal);
		
		assertEquals("[ERRO] Houveram problemas ao tentar editar uma conta no banco!", novoNome, contaGlobal.getNome());
		
	}
	
	/**Este teste remove uma conta do banco de dados, e o usuário atrelado a ela.
	 * 
	 * @throws Exception
	 * @author jay
	 */
	@Test
	public void test_4_removeUmaContaNoBanco() throws Exception {
		contaService.delete(contaGlobal);
		usuarioDao.delete(usuarioGlobal);
		
		Conta contaRemovida = contaService.findById(contaGlobal.getId());
		Usuario usuarioRemovido = usuarioDao.findById(usuarioGlobal.getId());
		
		assertNull("[ERRO] Houveram problemas ao tentar remover uma conta no banco!", contaRemovida);
		assertNull("[ERRO] Houveram problemas ao tentar remover um usuario no banco!", usuarioRemovido);
		
	}
	
}
