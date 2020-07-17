package seuBarrigaPontoCom_Estrategia3;

import java.sql.SQLException;

import br.ce.wcaquino.entidades.MassaDeDado;

public interface MassaDAO {

	public void inserirMassa(String tipo, String valor) throws Exception;
	
	public MassaDeDado obterMassaDeDados(String valor) throws Exception;
	
	public boolean alterarUsadoParaFalso(long id) throws Exception;

	long obterIdDaConta(MassaDeDado contaSeuBarriga) throws ClassNotFoundException, SQLException;

}
