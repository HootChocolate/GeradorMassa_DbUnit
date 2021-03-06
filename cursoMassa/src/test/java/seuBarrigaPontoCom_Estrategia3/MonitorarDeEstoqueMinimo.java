package seuBarrigaPontoCom_Estrategia3;

import java.sql.SQLException;

public class MonitorarDeEstoqueMinimo {

	private static Integer QUANTIDADE_MINIMA = 5;
	
	@SuppressWarnings("static-access")
	public static void monitorarQuantidadeDeContas() throws Exception {
		
		MassaDAOImpl massaDao = new MassaDAOImpl();
		GeradorDeMassa gerador = new GeradorDeMassa();
		
		while(true) {
			int quantidadeAtual = massaDao.obterQuantidadeDeContas(GeradorDeMassa.USUARIO);
			
			if(quantidadeAtual < QUANTIDADE_MINIMA) {
				gerador.gerarMassaDeDadosServico();
			} else {
				Thread.sleep(10000);
			}	
		}
	}
	
	public static void main(String[] args) throws Exception {
		monitorarQuantidadeDeContas();
	}
}
