package soapOperaScenario;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.util.Calendar;
import java.util.Date;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Test;

import br.ce.wcaquino.dao.SaldoDAO;
import br.ce.wcaquino.dao.impl.SaldoDAOImpl;
import br.ce.wcaquino.dao.utils.ConnectionFactory;

public class TestGetSaldoConta_DbUnit {

	/**
	 * Neste teste foi é utilizado a massa de dados gerado pelo teste com Soap Opera Scenario,
	 * como têm datas dentro do arquivo xml, foi utilizado uma variaveis no arquivo.
	 * 
	 * @throws Exception
	 */
	
	/**
	 * Como precisa adicionar as variaveis no arquivo xml, foi reconstruido a conexão com DbUnit
	 * e geração do arquivo xml, para poder setar as variveis foi utilizado o ReplacementDataSet 
	 * antes de executar a operação.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetSaldoTotalXmlVar() throws Exception {
		    
		SaldoDAO saldoDao = new SaldoDAOImpl();
		
		DatabaseConnection dbConn = new DatabaseConnection(ConnectionFactory.getConnection());
		
		FlatXmlDataSetBuilder xmlBuilder = new FlatXmlDataSetBuilder();
		
		FlatXmlDataSet xmlDataSet = xmlBuilder.build(new FileInputStream("massaDeDados" + File.separator + "contaComTransacoes.xml"));
		
		// Para substituição das variaveis
		ReplacementDataSet replacementXmlDataSet = new ReplacementDataSet(xmlDataSet);
		
		replacementXmlDataSet.addReplacementObject("[ontem]", getDataAtualMaisDias(-1));
		replacementXmlDataSet.addReplacementObject("[hoje]", new Date());
		replacementXmlDataSet.addReplacementObject("[amanha]", getDataAtualMaisDias(1));		

		DatabaseOperation.CLEAN_INSERT.execute(dbConn, replacementXmlDataSet);
			
		assertEquals("[ERRO]Houve erro ao validar a conta Principal do Usuario principal", Double.valueOf(24d), saldoDao.getSaldoConta(104L));
		assertEquals("[ERRO]Houve erro ao validar a conta Secundaria do usuario Principal", Double.valueOf(4d), saldoDao.getSaldoConta(105L));
		assertEquals("[ERRO]Houve erro ao validar a conta Principal do Usuario Secundario", Double.valueOf(64d), saldoDao.getSaldoConta(106L));
		assertEquals("[ERRO]Houve erro ao validar a conta Secundario do Usuario Secundario", Double.valueOf(128d), saldoDao.getSaldoConta(107L));
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
