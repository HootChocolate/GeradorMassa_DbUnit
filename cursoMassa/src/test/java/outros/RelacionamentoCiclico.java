package outros;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Test;

import br.ce.wcaquino.dao.utils.ConnectionFactory;
import config.ConfiguracaoPostgres;

/**
 * Quando tabelas que se relacionam têm uma id da outra, como uma chave estrangeira,
 * é um problemas fazer as inserções e deleções de dados.
 * No Postgres, a cada execução na tabela ele usa triggers para verificar o relacionamento
 * entre as tabelas, essas triggers podem ser desabilitadas durante a operação necessárias.
 * Deve-se tomar cuidado quando for fazer isso, por causa do desligamento das triggers 
 * o que for colocado no xml vai ser aceito.
 * 
 * @author jay
 */
public class RelacionamentoCiclico {

	@Test
	public void testRelacionamentoCiclico() throws Exception {
		
		DatabaseConnection dbUCon = new DatabaseConnection(ConnectionFactory.getConnection());
		
		FlatXmlDataSetBuilder builderXml = new FlatXmlDataSetBuilder();
		
		IDataSet dataSet = builderXml.build(new FileInputStream("massaDeDados" + File.separator + "relacionamentoCiclicos.xml"));
				
		ReplacementDataSet replacementDataSet = new ReplacementDataSet(dataSet);
		
		replacementDataSet.addReplacementObject("[hoje]", new Date());
		replacementDataSet.addReplacementObject("[ontem]", getDataAtualMaisDias(-1));
		replacementDataSet.addReplacementObject("[amanha]", getDataAtualMaisDias(1));
		
		List<String> listaDeTabelasBanco = ConfiguracaoPostgres.obterListaDeTabelasBanco();
		
		// desativar triggers
		ConfiguracaoPostgres.desativarTriggersPostgres(listaDeTabelasBanco);
		 
		DatabaseOperation.CLEAN_INSERT.execute(dbUCon, replacementDataSet);
		
		// habilitar triggers
		ConfiguracaoPostgres.habilitarTriggersPostgres(listaDeTabelasBanco);
		
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
