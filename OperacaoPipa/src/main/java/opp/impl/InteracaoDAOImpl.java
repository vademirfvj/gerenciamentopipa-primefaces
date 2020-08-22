package opp.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import opp.connection.ConexaoMySQL;
import opp.dao.InteracaoDAO;
import opp.dto.AssinaturasDTO;
import opp.dto.DataDTO;
import opp.dto.PipeiroDTO;
import opp.dto.PontoAbastecimentoDTO;
import opp.util.Constantes;

public class InteracaoDAOImpl implements InteracaoDAO {
	
	protected PreparedStatement statement;
	protected ResultSet resultSet;
	protected Connection conecction = ConexaoMySQL.getConnection();
	private static final Logger logger = Logger.getLogger(CidadeDAOImpl.class);
  
	@Override
	public List<PontoAbastecimentoDTO> recuperaPontoAbastecimentoPorCidade(int cidadeSelecionada) throws Exception {
		logger.info("Iniciando  recuperaPontoAbastecimentoPorCidade(int cidadeSelecionada)" + " cidadeSelecionada: " + cidadeSelecionada);
		
		try {
			
			List listaPAs = null;
			
			conecction = ConexaoMySQL.getConnection();

			StringBuffer queryPA = new StringBuffer();

			queryPA.append("\n SELECT p.*, i.INDICE as INDICE, i.DSC_INDICE as MOMENTOTRANSPORTE "
					+ "\n FROM pontoabastecimento p, indice i "
					+ "\n where p.CodCidade = ? and p.status = 'ATIVO' AND p.COD_INDICE = i.id order by comunidade asc");

			logger.info("Query: " + queryPA.toString());
			statement = conecction.prepareStatement(queryPA.toString());
			
			//Atribuindo parametro necessario para a query
			statement.setInt(1, cidadeSelecionada);

			resultSet = statement.executeQuery();
			
			listaPAs = popularPA(resultSet);
				
			return listaPAs;

		}finally {
			//Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			//Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}
		
		
	}

	private List popularPA(ResultSet rs) {
		List lista = new ArrayList();
		PontoAbastecimentoDTO pontoAbastecimentoDTO;
		
		try {
			
			while(rs.next()){
				
				pontoAbastecimentoDTO = new PontoAbastecimentoDTO();
				
				pontoAbastecimentoDTO.setId(new Integer(rs.getInt("ID")));
				pontoAbastecimentoDTO.setComunidade(rs.getString("COMUNIDADE"));
				pontoAbastecimentoDTO.setPopulacao(rs.getInt("POPULACAO"));
				pontoAbastecimentoDTO.setDistancia(rs.getDouble("DISTANCIA"));
				pontoAbastecimentoDTO.setMomento(rs.getString(("INDICE")));
				pontoAbastecimentoDTO.setCodcidade(rs.getInt("CODCIDADE"));
				pontoAbastecimentoDTO.setGeoreferenciamento(rs.getString("GEOREFERENCIAMENTO"));
				pontoAbastecimentoDTO.setVolume(rs.getDouble("VOLUME"));
				pontoAbastecimentoDTO.setAsfalto(rs.getDouble("ASFALTO"));
				pontoAbastecimentoDTO.setTerra(rs.getDouble("TERRA"));
				pontoAbastecimentoDTO.setIndice(rs.getDouble("INDICE"));
				pontoAbastecimentoDTO.setApontador(rs.getString("APONTADOR"));
				pontoAbastecimentoDTO.setSubstituto(rs.getString("SUBSTITUTO"));
				pontoAbastecimentoDTO.setStatus(rs.getString("STATUS"));
				pontoAbastecimentoDTO.setLitrosDiario(rs.getInt("LITROS_DIARIO"));
				
				
				lista.add(pontoAbastecimentoDTO);
				
			}
		} catch (SQLException e) {
			// Colocar LOG
			e.printStackTrace();
		}
		
		return lista;
	}

	@Override
	public List<PipeiroDTO> recuperaPipeiroPorCidade(int cidadeSelecionada)throws Exception {
		logger.info("Iniciando  recuperaPipeiroPorCidade(int cidadeSelecionada)" + " cidadeSelecionada: " + cidadeSelecionada);

		try {
			
			List listaPiperos = null;
			
			conecction = ConexaoMySQL.getConnection();

			StringBuffer queryPipeiro = new StringBuffer();

			queryPipeiro.append("\n select *, v.ID as ID_VEICULO from pipeiro p, veiculo v ");
			queryPipeiro.append("\n where p.CODVEICULO = v.ID and p.CodCidade = ? and v.status = 'ATIVO'");
			
			logger.info("Query: " + queryPipeiro.toString());
			statement = conecction.prepareStatement(queryPipeiro.toString());
			
			//Atribuindo parametro necessario para a query
			statement.setInt(1, cidadeSelecionada);

			resultSet = statement.executeQuery();
			
			listaPiperos = popularPipeiro(resultSet);
				
			return listaPiperos;

		}finally {
			//Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			//Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}
		
		
	}

	private List popularPipeiro(ResultSet rs) {
		List lista = new ArrayList();
		PipeiroDTO pipeiroDTO;
		
		try {
			
			while(rs.next()){
				
				pipeiroDTO = new PipeiroDTO();
				
				pipeiroDTO.setId(new Integer(rs.getInt("ID")));
				pipeiroDTO.setNome(rs.getString("NOME"));
				pipeiroDTO.setCodCidade(rs.getInt("CODCIDADE"));
				pipeiroDTO.setCnh(rs.getInt("CNH"));
				pipeiroDTO.setValidadeCnh(rs.getDate("VALIDADECNH"));
				pipeiroDTO.setIdentidade(rs.getInt("IDENTIDADE"));
				pipeiroDTO.setOrgaoEmissor(rs.getString("ORGAOEMISSORUF"));
				pipeiroDTO.setNit(rs.getString("NIT"));
				pipeiroDTO.setCpf(rs.getString("CPF"));
				pipeiroDTO.setCodBanco(rs.getString("CODBANCO"));
				pipeiroDTO.setBanco(rs.getString("BANCO"));
				pipeiroDTO.setAgencia(rs.getString("AGENCIA"));
				pipeiroDTO.setConta(rs.getString("CONTA"));
				pipeiroDTO.setCodVeiculo(rs.getInt("CODVEICULO"));
				pipeiroDTO.setSaldoSiaf(rs.getDouble(("SALDOSIAF")));
				pipeiroDTO.setRecursoAno(rs.getDouble("RECURSOANO"));
				pipeiroDTO.setRestoPagar(rs.getDouble("RESTOPAGAR"));
				pipeiroDTO.setStatus(rs.getString("STATUS"));
				
				pipeiroDTO.getVeiculoDTO().setId(rs.getInt("ID_VEICULO"));
				pipeiroDTO.getVeiculoDTO().setPlaca(rs.getString("PLACA"));
				pipeiroDTO.getVeiculoDTO().setCapacidadePipa(rs.getDouble("CAPACIDADEPIPA"));
				
				lista.add(pipeiroDTO);
				
			}
		} catch (SQLException e) {
			// Colocar LOG
			e.printStackTrace();
		}
		
		return lista;
	}

	@Override
	public int validarDistribuicao(Integer ano, Integer mes, long codCidade) throws Exception {
		logger.info("Iniciando  validarDistribuicao((Integer ano, Integer mes, long codCidade)" 
	   + " ano: " + ano + " mes: " + mes + " codCidade: " + codCidade);

		try {
			
			conecction = ConexaoMySQL.getConnection();

			StringBuffer queryPA = new StringBuffer();

			queryPA.append("SELECT * FROM interacao_ditribuicao where MES = ? and ANO = ? and COD_CIDADE = ?");

			logger.info("Query: " + queryPA.toString());
			statement = conecction.prepareStatement(queryPA.toString());
			
			//Atribuindo parametro necessario para a query
			statement.setInt(1, mes);
			statement.setInt(2, ano);
			statement.setLong(3, codCidade);

			resultSet = statement.executeQuery();
			
			if(resultSet.next()){
				return 1;
			}else{
				return 0;
			}
			

		}finally {
			//Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			//Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}
		
		
	}

	@Override
	public int verificaExistenciaDefDias(Integer ano, Integer mes) throws Exception {
		logger.info("Iniciando  verificaExistenciaDefDias((Integer ano, Integer mes)" 
				   + " ano: " + ano + " mes: " + mes);
		try {
			int retornoQuery;
			
			conecction = ConexaoMySQL.getConnection();

			StringBuffer queryPA = new StringBuffer();

			queryPA.append("SELECT QTD_DIAS FROM interacao_ditribuicao where MES = ? and ANO = ?");

			logger.info("Query: " + queryPA.toString());
			statement = conecction.prepareStatement(queryPA.toString());
			
			//Atribuindo parametro necessario para a query
			statement.setInt(1, mes);
			statement.setInt(2, ano);

			resultSet = statement.executeQuery();
			
			if(resultSet.next()){
				
				retornoQuery = resultSet.getInt("QTD_DIAS");
				
				return retornoQuery;
			}else{
				return 1;
			}
			

		}finally {
			//Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			//Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}
	
	}

	@Override
	public List<DataDTO> recuperarDataDTO(Integer ano, Integer mes)throws Exception {
		logger.info("Iniciando  recuperarDataDTO((Integer ano, Integer mes)" 
				   + " ano: " + ano + " mes: " + mes);
		
		try {
			
			List listaDatas = new ArrayList();
			
			conecction = ConexaoMySQL.getConnection();

			StringBuffer queryPA = new StringBuffer();

			queryPA.append("SELECT * FROM interacao_datas where MES = ? and ANO = ? order by Indice");

			logger.info("Query: " + queryPA.toString());
			statement = conecction.prepareStatement(queryPA.toString());
			
			//Atribuindo parametro necessario para a query
			statement.setInt(1, mes);
			statement.setInt(2, ano);

			resultSet = statement.executeQuery();
			
			listaDatas = popularDatas(resultSet);
				
			return listaDatas;

		}finally {
			//Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			//Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}
		
		
	}

	private List popularDatas(ResultSet rs) {
		List<DataDTO> lista = new ArrayList();
		DataDTO dataDTO;
		
		try {
			
			while(rs.next()){
				
				dataDTO = new DataDTO();
				
				dataDTO.setData(rs.getDate("data"));
				dataDTO.setDatasExtenso(rs.getString("data_extenso"));
				dataDTO.setIndice(rs.getInt("indice"));
				dataDTO.setDias(rs.getInt("dias"));
				dataDTO.setEhFimDeSemana(rs.getInt("eh_fim_de_semana") == 1?true:false);
				dataDTO.setExibirColuna(rs.getInt("exibir_coluna") == 1?true:false);
				
				lista.add(dataDTO);
				
			}
		} catch (SQLException e) {
			// Colocar LOG
			e.printStackTrace();
		}
		
		return lista;
	}

	@Override
	public void inserirInteracaoUsuario(int idFUncionalidade,String acao, String osb, Date date, int idUsuario) throws Exception {
		logger.info("Iniciando  inserirInteracaoUsuario(int idFUncionalidade,String acao, String osb, Date date)" 
				   + " idFUncionalidade: " + idFUncionalidade + " acao: " + acao + " osb: " + osb + " date: " + date);
		
		try {
			
			conecction = ConexaoMySQL.getConnection();
			
			java.sql.Date sqlDate = new java.sql.Date(date.getTime());

			StringBuffer queryInsert = new StringBuffer();

			queryInsert.append("\n INSERT INTO interacao_usuario ");
			queryInsert.append("\n (funcionalidade,acao,obs,data,usuario) ");
			queryInsert.append("\n VALUES(?,?,?,?,?)");

			logger.info("Query: " + queryInsert.toString());
			statement = conecction.prepareStatement(queryInsert.toString());
			
			//Atribuindo parametro necessario para a query
			statement.setInt(1, idFUncionalidade);
			statement.setString(2, acao);
			statement.setString(3, osb);
			statement.setDate(4, sqlDate);
			statement.setInt(5, idUsuario);
			
			statement.execute();
			
				
		}catch(Exception e) {
			
		      throw new Exception(e);
		}finally {
			//Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			//Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}
		
	}
 
	@Override
	public void inserirInteracaoDistribuicao(int cidade, long pipeiro,int veiculo, int pontoColeta, int codHistorico, int numDiasMes,
			int litrosPessoa, double valorTotalPA, int kmTotal,double totalViagensFormula, int totalViagensReal,
			int totalApontadores, int totalPessoas, double totalQtdAgua,int totalDistacia, int mes, int ano) throws Exception {
		logger.info("Iniciando  inserirInteracaoDistribuicao(Parametros)\n");
		
		
		try {
			
			conecction = ConexaoMySQL.getConnection();
			
			StringBuffer queryInsert = new StringBuffer();

			queryInsert.append("\n INSERT INTO interacao_ditribuicao ");
			queryInsert.append("\n (COD_CIDADE,COD_PIPEIRO,COD_VEICULO,COD_PC,COD_HISTORICO_DISTRIBUICAO, ");
			queryInsert.append("\n QTD_DIAS,QTD_LITROS_PESSOA,VALOR_TOTAL,KM_TOTAL,TOTAL_VIAGEM_FORMULA, ");
			queryInsert.append("\n TOTAL_VIAGEM_REAL,TOTAL_APONTADORES,TOTAL_PESSOAS,TOTAL_QTD_AGUA,TOTAL_DISTANCIA,MES,ANO) ");
			queryInsert.append("\n VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

			logger.info("Query: " + queryInsert.toString());
			statement = conecction.prepareStatement(queryInsert.toString());
			
			
		
			//Atribuindo parametro necessario para a query
			statement.setInt(1, cidade);
			statement.setLong(2, pipeiro);
			statement.setInt(3, veiculo);
			statement.setInt(4, pontoColeta);
			statement.setInt(5, codHistorico);
			statement.setInt(6, numDiasMes);
			statement.setInt(7, litrosPessoa);
			statement.setDouble(8, valorTotalPA);
			statement.setInt(9, kmTotal);
			statement.setDouble(10, totalViagensFormula);
			statement.setInt(11, totalViagensReal);
			statement.setInt(12, totalApontadores);
			statement.setInt(13, totalPessoas);
			statement.setDouble(14, totalQtdAgua);
			statement.setInt(15, totalDistacia);
			statement.setInt(16, mes);
			statement.setInt(17, ano);

			statement.execute();
			
				
		}catch(Exception e) {
			
		      throw new Exception(e);
		}finally {
			//Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			//Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}

		
	}

	@Override
	public void inserirInteracaoPipeiroPa(long pipeiro, long pontoAbastec,double qtdAgua, double qtdViagensFormula, int qtdViagensReal,
			double valorBruto, int kmPercorridoPA, String obs, double indice,int codHistorico) throws Exception {
		logger.info("Iniciando  inserirInteracaoPipeiroPa(Parametros)\n");
		try {
			
			conecction = ConexaoMySQL.getConnection();
			
			StringBuffer queryInsert = new StringBuffer();

			queryInsert.append("INSERT INTO interacao_pipeiro_pa ");
			queryInsert.append("(COD_PIPEIRO,COD_PA,TOTAL_QTD_AGUA,VIAGEM_FORMULA,VIAGEM_REAL, ");
			queryInsert.append("VALOR_BRUTO,KM_PERCORRIDO,OBS,COD_HISTORICO_DISTRIBUICAO,INDICE) ");
			queryInsert.append("VALUES (?,?,?,?,?,?,?,?,?,?)   ");
			
			logger.info("Query: " + queryInsert.toString());
			statement = conecction.prepareStatement(queryInsert.toString());
			
			
		
			//Atribuindo parametro necessario para a query
			statement.setLong(1, pipeiro);
			statement.setLong(2, pontoAbastec);
			statement.setDouble(3, qtdAgua);
			statement.setDouble(4, qtdViagensFormula);
			statement.setInt(5, qtdViagensReal);
			statement.setDouble(6, valorBruto);
			statement.setInt(7, kmPercorridoPA);
			statement.setString(8, obs);
			statement.setInt(9, codHistorico);
			statement.setDouble(10, indice);

			statement.execute();
			
				
		}catch(Exception e) {
			
		      throw new Exception(e);
		}finally {
			//Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			//Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}
		
	}

	@Override
	public void inserirInteracaoDatas(Date data, String datasExtenso,int indice, int dias, 
			boolean ehFimDeSemana, boolean exibirColuna,int codHistorico, int mes, int ano, long pa, long pipeiro)throws Exception {
		logger.info("Iniciando  inserirInteracaoDatas(Parametros)\n");

		try {
			
			java.sql.Date sqlDate = new java.sql.Date(data.getTime());
			
			int fimDeSemana;
			if(ehFimDeSemana){
				fimDeSemana = 1;
			}else{
				fimDeSemana = 0;
			}
			
			int exibirColum;
			if(exibirColuna){
				exibirColum = 1;
			}else{
				exibirColum = 0;
			}
			
			conecction = ConexaoMySQL.getConnection();
			
			StringBuffer queryInsert = new StringBuffer();

			queryInsert.append("\nINSERT INTO interacao_datas  ");
			queryInsert.append("\n(data,data_extenso,indice,dias,eh_fim_de_semana,exibir_coluna, ");
			queryInsert.append("\ncod_historico_distribuicao,mes,ano,cod_pa,cod_pipeiro) ");
			queryInsert.append("\nVALUES (?,?,?,?,?,?,?,?,?,?,?) ");
			
			logger.info("Query: " + queryInsert.toString());
			statement = conecction.prepareStatement(queryInsert.toString());
			
			
		
			//Atribuindo parametro necessario para a query
			statement.setDate(1, sqlDate);
			statement.setString(2, datasExtenso);
			statement.setInt(3, indice);
			statement.setInt(4, dias);
			statement.setInt(5, fimDeSemana);
			statement.setInt(6, exibirColum);
			statement.setInt(7, codHistorico);
			statement.setInt(8, mes);
			statement.setInt(9, ano);
			statement.setLong(10, pa);
			statement.setLong(11, pipeiro);

			statement.execute();
			
				
		}catch(Exception e) {
			
		      throw new Exception(e);
		}finally {
			//Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			//Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}
		
	}

	@Override
	public void deleteInteracaoDistribuicao(int codigoHistorico)throws Exception {
		logger.info("Iniciando  deleteInteracaoDistribuicao(int codigoHistorico) codigoHistorico: "+ codigoHistorico);

		try {
			
			conecction = ConexaoMySQL.getConnection();
			
			StringBuffer queryDelete = new StringBuffer();

			queryDelete.append("DELETE FROM interacao_ditribuicao WHERE cod_historico_distribuicao = ?");

			logger.info("Query: " + queryDelete.toString());
			statement = conecction.prepareStatement(queryDelete.toString());
			
			//Atribuindo parametro necessario para a query
			statement.setInt(1, codigoHistorico);

			statement.execute();
			
				
		}catch(Exception e) {
			
		      throw new Exception(e);
		}finally {
			//Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			//Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}

	}

	@Override
	public void deleteInteracaoPipeiroPa(int codigoHistorico) throws Exception {
		logger.info("Iniciando  deleteInteracaoPipeiroPa(int codigoHistorico) codigoHistorico: "+ codigoHistorico);

		try {
			
			conecction = ConexaoMySQL.getConnection();
			
			StringBuffer queryDelete = new StringBuffer();

			queryDelete.append("DELETE FROM interacao_pipeiro_pa WHERE cod_historico_distribuicao = ?");

			logger.info("Query: " + queryDelete.toString());
			statement = conecction.prepareStatement(queryDelete.toString());
			
			//Atribuindo parametro necessario para a query
			statement.setInt(1, codigoHistorico);

			statement.execute();
			
				
		}catch(Exception e) {
			
		      throw new Exception(e);
		}finally {
			//Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			//Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}
	}

	@Override
	public void deleteInteracaoDatas(int codigoHistorico) throws Exception {
		logger.info("Iniciando  deleteInteracaoDatas(int codigoHistorico) codigoHistorico: "+ codigoHistorico);

		try {
			
			conecction = ConexaoMySQL.getConnection();
			
			StringBuffer queryDelete = new StringBuffer();

			queryDelete.append("DELETE FROM interacao_datas WHERE cod_historico_distribuicao = ?");

			logger.info("Query: " + queryDelete.toString());
			statement = conecction.prepareStatement(queryDelete.toString());
			
			//Atribuindo parametro necessario para a query
			statement.setInt(1, codigoHistorico);

			statement.execute();
			
				
		}catch(Exception e) {
			
		      throw new Exception(e);
		}finally {
			//Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			//Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}
	}
 
	@Override
	public void atualizarAssinaturaDistrPieiro(AssinaturasDTO assinaturasDTO) throws Exception {
		logger.info("Iniciando  atualizarAssinaturaDistrPieiro(AssinaturasDTO assinaturasDTO) assinaturasDTO: "+ assinaturasDTO.getNome());

		try {
			
			String abrev = assinaturasDTO.getAbrev();
			
			int codFuncionalidade = Constantes.DISTRIBUICAO_PIPEIRO_PA;
			
			conecction = ConexaoMySQL.getConnection();
			
			StringBuffer queryUpdate = new StringBuffer();

			queryUpdate.append("UPDATE assinaturas SET nome = ? WHERE funcionalidade = ? AND abrev = ?");

			logger.info("Query: " + queryUpdate.toString());
			statement = conecction.prepareStatement(queryUpdate.toString());
			
			//Atribuindo parametro necessario para a query
			statement.setString(1, assinaturasDTO.getNome());
			statement.setInt(2, codFuncionalidade);
			statement.setString(3, abrev);

			statement.executeUpdate();
			
//			ConexaoMySQL.commitConexao(conecction);
				
		}catch(Exception e) {
			
		      throw new Exception(e);
		}finally {
			//Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			//Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}
		
	}

	@Override
	public void atualizarHistorico(int bar,Date dataSorteio, Date dataBAR, int mes, int ano, int codigoHistorico)throws Exception {
		logger.info("Iniciando  atualizarHistorico(int codigoHistorico) codigoHistorico: "+ codigoHistorico);

		try {
			
			conecction = ConexaoMySQL.getConnection();
			
			java.sql.Date sqlDate1 = new java.sql.Date(dataSorteio.getTime());
			java.sql.Date sqlDate2 = new java.sql.Date(dataBAR.getTime());
			
			StringBuffer queryUpdate = new StringBuffer();

			queryUpdate.append("\n UPDATE hitorico_distribuicao SET "
					+ "\n NUM_BAR = ?, DATA_SORTEIO = ?, DATA_BAR = ?, MES = ?, ANO = ? "
					+ "\n WHERE ID = ? ");

			logger.info("Query: " + queryUpdate.toString());
			statement = conecction.prepareStatement(queryUpdate.toString());
			
			//Atribuindo parametro necessario para a query
			statement.setInt(1, bar);
			statement.setDate(2, sqlDate1);
			statement.setDate(3, sqlDate2);
			statement.setInt(4, mes);
			statement.setInt(5, ano);
			statement.setInt(6, codigoHistorico);

			statement.executeUpdate();
			
//			ConexaoMySQL.commitConexao(conecction);
				
		}catch(Exception e) {
			
		      throw new Exception(e);
		}finally {
			//Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			//Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}
		
	}

	@Override
	public void atualizarInteracaoDistr(int os, int lacre, int mes, int ano,
			int pipeiroSelecionadoImpressao) throws Exception {
		logger.info("Iniciando  atualizarInteracaoDistr(int pipeiroSelecionadoImpressao) pipeiroSelecionadoImpressao: "+ pipeiroSelecionadoImpressao);

try {
			
			conecction = ConexaoMySQL.getConnection();
			
			StringBuffer queryUpdate = new StringBuffer();

			queryUpdate.append("\n UPDATE interacao_ditribuicao SET "
					+ "\n NUM_LACRE = ?, NUM_OS = ? "
					+ "\n WHERE cod_pipeiro = ? and mes = ? and ano = ? ");

			logger.info("Query: " + queryUpdate.toString());
			statement = conecction.prepareStatement(queryUpdate.toString());
			
			//Atribuindo parametro necessario para a query
			statement.setInt(1, lacre);
			statement.setInt(2, os);
			statement.setInt(3, pipeiroSelecionadoImpressao);
			statement.setInt(4, mes);
			statement.setInt(5, ano);

			statement.executeUpdate();
			
//			ConexaoMySQL.commitConexao(conecction);
				
		}catch(Exception e) {
			
		      throw new Exception(e);
		}finally {
			//Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			//Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}

		
	}
 

}