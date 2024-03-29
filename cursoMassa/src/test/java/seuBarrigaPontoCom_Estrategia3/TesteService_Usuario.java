package seuBarrigaPontoCom_Estrategia3;

import static org.junit.Assert.*;

import org.junit.Test;

import br.ce.wcaquino.entidades.MassaDeDado;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.service.UsuarioService;

public class TesteService_Usuario {
	
	private MassaDAOImpl massaDao = new MassaDAOImpl();
	private UsuarioService service = new UsuarioService();
	
	/**
	 * Consulta um usuário na tabela de usuários, utilizando dados vindo do gerador de dados.
	 * Não inutiliza o dado, pois não houve alteração.
	 * @throws Exception
	 */
//	@Test
	public void testConsultarUsuario() throws Exception {
		
		MassaDeDado massaDeDado = massaDao.obterMassaDeDados(GeradorDeMassa.USUARIO);
		
		Usuario usuarioConsultado = service.findByNome(massaDeDado.getValor());
		
		assertEquals(massaDeDado.getValor(), usuarioConsultado.getNome());
		
		massaDao.alterarUsadoParaFalso(massaDeDado.getId());
	}
	
	/**
	 * Edita um usuario na tabela "Usuarios", utilizando dados vindo do gerador de dados.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testEditarUsuario() throws Exception {
		MassaDeDado massaDeDado = massaDao.obterMassaDeDados(GeradorDeMassa.USUARIO);
		
		Usuario usuario = service.findByNome(massaDeDado.getValor());
		
		String novoNome = usuario.getNome() + " Alterado!";
		
		usuario.setNome(novoNome);
		
		service.salvar(usuario);
		
		assertEquals("[ERRO]Houve erro ao tentar editar um usuario do banco", novoNome, usuario.getNome());
	}
	
}
