package dbUnit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.DatabaseSequenceFilter;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.FilteredDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.filter.ITableFilter;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;

import br.ce.wcaquino.dao.utils.ConnectionFactory;
import config.ConfiguracaoPostgres;

/**
 * Classe responsável por gerenciar a geração de arquivs XML utilizando DbUnit
 * 
 * @author jay
 */
public class ImportExport {

	public static void main(String[] args) throws Exception {

		exportarDadosDoBanco("contaComTransacoes.xml");
//		importarDadoParaOBanco("");
	}

	/**
	 * Limpa os dados, e insere os valores passado no arquivo.xml dentro do banco de dados.
	 * 
	 * @param massaDeDados
	 * @throws Exception
	 */
	public static void importarDadoParaOBanco(String massaDeDados) throws Exception {
		
		IDatabaseConnection dbConn = new DatabaseConnection(ConnectionFactory.getConnection());
		
		FlatXmlDataSetBuilder construtorXML = new FlatXmlDataSetBuilder();		// Cria o construtor de arquivo xml
		
		// Passa o arquivo XML para o construtor
		IDataSet arquivoXML = construtorXML.build(new FileInputStream("massaDeDados" + File.separator + massaDeDados));
				
		// Executa a operação MySql
		DatabaseOperation.CLEAN_INSERT.execute(dbConn, arquivoXML);	
	}
	
	/**
	 * Pega os valores do banco de dados e salva em um arquivo .xlm
	 * 
	 * @throws Exception
	 */
	public static void exportarDadosDoBanco(String massaDeDados) throws Exception {
		
		DatabaseConnection dbUnitCon = new DatabaseConnection(ConnectionFactory.getConnection());
		
		ITableFilter filter = new DatabaseSequenceFilter(dbUnitCon);	// Filtro para ordenação de PK
		
		IDataSet dadosBanco = new FilteredDataSet(filter, dbUnitCon.createDataSet());	// Dados do banco filtrando PKs
		
		// Cria um arquivo XML para gravar os dados do banco (dataSet)
		FileOutputStream arquivoXML = new FileOutputStream("massaDeDados" + File.separator + massaDeDados);

		FlatXmlDataSet.write(dadosBanco, arquivoXML);		// Escreve no arquivo os dados XML
	}

	/**
	 * Pega uma conexão de dados DbUnit, e retorna o estado do banco, com os dados das tabelas.
	 * @return DataSet
	 * @throws Exception
	 */
	public static IDataSet estadoAtualBanco() throws Exception {

		DatabaseConnection dbConn = new DatabaseConnection(ConnectionFactory.getConnection());
		
		return dbConn.createDataSet();
	}

	/**
	 * A partir da massa de dados, constroi e devolve um novo estado do banco de dados (FlatXmlDataSet)
	 * @param massaDeDado
	 * @return FlatXmlDataSet
	 * @throws DataSetException
	 * @throws FileNotFoundException
	 */
	public static FlatXmlDataSet estadoEsperadoBanco(String massaDeDado) throws DataSetException, FileNotFoundException {
		FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
		
		return builder.build(new FileInputStream("massaDeDados" + File.separator + massaDeDado));
	}

	public static void atualizarSequences() throws Exception {
		
		IDataSet banco = estadoAtualBanco();
		
		String[] tabelas = banco.getTableNames();		
		
		// pega o maior id da tabela em questão
		for(String tabela : tabelas) {
			Statement stmt = ConnectionFactory.getConnection().createStatement();
		
			ResultSet rs = stmt.executeQuery("SELECT MAX(id) FROM public." + tabela);
			if(rs.next()) {
				
				Long maiorId = rs.getLong(1);
				
				if(maiorId > 0) {
					stmt.executeUpdate("ALTER SEQUENCE " + tabela + "_id_seq RESTART WITH " + (maiorId + 1));
				}
			}
			
			stmt.close();
			rs.close();
		}
	}
}
