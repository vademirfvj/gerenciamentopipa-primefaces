package opp.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import opp.connection.ConexaoMySQL;
import opp.dao.DistribuicaoPipeiroDAO;
import opp.dao.InteracaoDAO;
import opp.dto.AssinaturasDTO;
import opp.dto.CidadeDTO;
import opp.dto.DataDTO;
import opp.dto.DistribuicaoDiasPipeiroDTO;
import opp.dto.InteracaoDistribuicaoDTO;
import opp.dto.InteracaoPipeiroPADTO;
import opp.dto.PipeiroDTO;
import opp.dto.PontoAbastecimentoDTO;
import opp.dto.RelatorioDistribuicaoCarradasDTO;
import opp.dto.RotaPagamentoPipeiroDTO;

public class DistribuicaoPipeiroDAOImpl implements DistribuicaoPipeiroDAO {
	
	protected PreparedStatement statement;
	protected ResultSet resultSet;
	protected Connection conecction = ConexaoMySQL.getConnection();
	private static final Logger logger = Logger.getLogger(CidadeDAOImpl.class);

	@Override
	public int inserirHistoricoDistribuicao(Date dataAtual, int codUsuario,String obs) throws Exception {
		logger.info("Iniciando  inserirHistoricoDistribuicao(Date dataAtual, int codUsuario,String obs)" + " dataAtual: " + dataAtual
				+ " codUsuario: " + codUsuario + " obs: " + obs);
		
		try {
			int retornoQuery;
			
			conecction = ConexaoMySQL.getConnection();

			StringBuffer queryInsert = new StringBuffer();
			
			java.sql.Date sqlDate = new java.sql.Date(dataAtual.getTime());

			queryInsert.append("\n INSERT INTO hitorico_distribuicao ");
			queryInsert.append("\n (DATA_INTERACAO,COD_USUARIO,OBSERVACAO) ");
			queryInsert.append("\n VALUES(?,?,?)");

			logger.info("Query: " + queryInsert.toString());
			
			statement = conecction.prepareStatement(queryInsert.toString(),Statement.RETURN_GENERATED_KEYS);
			
			//Atribuindo parametro necessario para a query
			statement.setDate(1, sqlDate);
			statement.setInt(2, codUsuario);
			statement.setString(3, obs);

			statement.executeUpdate();
			
			resultSet = statement.getGeneratedKeys();
			
			
			if(resultSet.next()){
				
				retornoQuery = resultSet.getInt(1);
				
				return retornoQuery;
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
	public int selectCodHistorico(int cidadeInicial,
			String mesSelecionado, String anoSelecionado) throws Exception {
		logger.info("Iniciando  selectCodHistorico(int cidadeInicial,"
				+ "String mesSelecionado, String anoSelecionado)" + " cidadeInicial: " + cidadeInicial
				+ " mesSelecionado: " + mesSelecionado + " anoSelecionado: " + anoSelecionado);
		try {
			int retornoQuery;
			
			conecction = ConexaoMySQL.getConnection();

			StringBuffer query = new StringBuffer();
			

			query.append("\n SELECT cod_historico_distribuicao FROM interacao_ditribuicao ");
			query.append("\n where COD_CIDADE = ? AND mes = ? AND ano = ? LIMIT 1 ");

			logger.info("Query: " + query.toString());
			statement = conecction.prepareStatement(query.toString());
			
			//Atribuindo parametro necessario para a query
			statement.setInt(1, cidadeInicial);
			statement.setInt(2, Integer.valueOf(mesSelecionado));
			statement.setInt(3, Integer.valueOf(anoSelecionado));

			
			resultSet = statement.executeQuery();
			
			
			if(resultSet.next()){
				
				retornoQuery = resultSet.getInt("cod_historico_distribuicao");
				
				return retornoQuery;
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
	public List getVisualizarCidade(int cidadeSelecionada,String mes, String ano) throws Exception {
		logger.info("Iniciando  getVisualizarCidade(int cidadeSelecionada,String mes, String ano)" + " cidadeSelecionada: " + cidadeSelecionada
				+ " mes: " + mes + " ano: " + ano);
		try {
			
			List listaCidades = null;
			
			conecction = ConexaoMySQL.getConnection();

			StringBuffer queryPA = new StringBuffer();

			queryPA.append("\n SELECT pi.NOME, intera.COD_PIPEIRO, intera.TOTAL_APONTADORES as TOTAL_PA, intera.TOTAL_VIAGEM_REAL, intera.VALOR_TOTAL ");
			queryPA.append("\n FROM interacao_ditribuicao intera, pipeiro pi ");
			queryPA.append("\n where intera.COD_PIPEIRO = pi.ID AND intera.COD_CIDADE = ? AND intera.mes = ? AND intera.ano = ?");
			
			logger.info("Query: " + queryPA.toString());
			statement = conecction.prepareStatement(queryPA.toString());
			
			//Atribuindo parametro necessario para a query
			statement.setInt(1, cidadeSelecionada);
			statement.setString(2, mes);
			statement.setString(3, ano);

			resultSet = statement.executeQuery();
			
			listaCidades = popularVCidade(resultSet);
				
			return listaCidades;

		}finally {
			//Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			//Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}
	}


	private List popularVCidade(ResultSet rs) {
		List lista = new ArrayList();
		RotaPagamentoPipeiroDTO rp;
		
		try {
			
			while(rs.next()){
				
				rp = new RotaPagamentoPipeiroDTO();
				
				rp.getPipeiroDTO().setId(rs.getInt("COD_PIPEIRO"));
				rp.getPipeiroDTO().setNome(rs.getString("NOME"));
				rp.setTotalApontadores(rs.getInt("TOTAL_PA"));
				rp.setTotalViagensReal(rs.getInt("TOTAL_VIAGEM_REAL"));
				rp.setValorTotalPA(rs.getDouble("VALOR_TOTAL"));
				
				
				lista.add(rp);
				
			}
		} catch (SQLException e) {
			// Colocar LOG
			e.printStackTrace();
		}
		
		return lista;
	}


	@Override
	public int selectCodHistoricoPorPipeiro(int cidadeSelecionada,int pipeiroSelecionadoImpressao, String mesSelecionado,String anoSelecionado) throws Exception {
		logger.info("Iniciando  selectCodHistoricoPorPipeiro(int cidadeSelecionada,int pipeiroSelecionadoImpressao, String mesSelecionado,String anoSelecionado)"
				+ "" + " cidadeSelecionada: " + cidadeSelecionada
				+ " pipeiroSelecionadoImpressao: " + pipeiroSelecionadoImpressao + " mesSelecionado: " + mesSelecionado + " anoSelecionado: " + anoSelecionado);
		try {
			int retornoQuery;
			
			conecction = ConexaoMySQL.getConnection();

			StringBuffer query = new StringBuffer();
			

			query.append("\n SELECT cod_historico_distribuicao FROM interacao_ditribuicao ");
			query.append("\n where COD_CIDADE = ? AND cod_pipeiro = ? AND mes = ? AND ano = ? LIMIT 1 ");

			logger.info("Query: " + query.toString());
			statement = conecction.prepareStatement(query.toString());
			
			//Atribuindo parametro necessario para a query
			statement.setInt(1, cidadeSelecionada);
			statement.setInt(2, pipeiroSelecionadoImpressao);
			statement.setInt(3, Integer.valueOf(mesSelecionado));
			statement.setInt(4, Integer.valueOf(anoSelecionado));

			
			resultSet = statement.executeQuery();
			
			
			if(resultSet.next()){
				
				retornoQuery = resultSet.getInt("cod_historico_distribuicao");
				
				return retornoQuery;
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
	public List<RelatorioDistribuicaoCarradasDTO> montarInfoPipeiro(int pipeiroSelecionadoImpressao, int codigoHistorico)throws Exception {
		logger.info("Iniciando  montarInfoPipeiro(int pipeiroSelecionadoImpressao, int codigoHistorico)"
				+ "" + " pipeiroSelecionadoImpressao: " + pipeiroSelecionadoImpressao
				+ " codigoHistorico: " + codigoHistorico);
		try {
			
			List<RelatorioDistribuicaoCarradasDTO> dp = null;
			
			conecction = ConexaoMySQL.getConnection();

			StringBuffer queryPA = new StringBuffer();

			queryPA.append("\n SELECT pipeiro.VIAGEM_REAL, pipeiro.COD_PA, pa.DISTANCIA, pa.POPULACAO, pa.APONTADOR, pa.COMUNIDADE,pipeiro.INDICE,pa.CODGCDA, "
					+ "\n pipeiro.TOTAL_QTD_AGUA, pipeiro.VIAGEM_FORMULA,pa.DISTANCIA, pipeiro.VALOR_BRUTO "
					+ "\n FROM interacao_pipeiro_pa pipeiro, pontoabastecimento pa ");
			queryPA.append("\n WHERE pipeiro.cod_pa = pa.id AND pipeiro.cod_pipeiro = ? AND COD_HISTORICO_DISTRIBUICAO = ? ");
			
			logger.info("Query: " + queryPA.toString());
			statement = conecction.prepareStatement(queryPA.toString());
			
			//Atribuindo parametro necessario para a query
			statement.setInt(1, pipeiroSelecionadoImpressao);
			statement.setInt(2, codigoHistorico);

			resultSet = statement.executeQuery();
			
			dp = popularMontarInfoPipeiro(resultSet);
				
			return dp;

		}finally {
			//Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			//Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}
	}


	private List<RelatorioDistribuicaoCarradasDTO> popularMontarInfoPipeiro(ResultSet rs) {
		List lista = new ArrayList();
		RelatorioDistribuicaoCarradasDTO rp;
		
		try {
			
			while(rs.next()){
				
				rp = new RelatorioDistribuicaoCarradasDTO();
				
				rp.setPontoAbastecimento(rs.getString("COMUNIDADE"));
				rp.setCodPA(rs.getInt("COD_PA"));
				rp.setApontador(rs.getString("APONTADOR"));
				rp.setPopulacao(rs.getInt("POPULACAO"));
				rp.setQtdViagensReal(rs.getInt("VIAGEM_REAL"));
				rp.setDistancia(rs.getInt("DISTANCIA"));
				
				rp.setMomento(rs.getDouble("INDICE"));
				rp.setQtdAgua(rs.getInt("TOTAL_QTD_AGUA"));
				rp.setQtdViagensForm(rs.getDouble("VIAGEM_FORMULA"));
				rp.setValorBruto(rs.getDouble("VALOR_BRUTO"));
				
				rp.setGcda(rs.getInt("CODGCDA"));
				
				
				lista.add(rp);
				
			}
		} catch (SQLException e) {
			// Colocar LOG
			e.printStackTrace();
		}
		
		return lista;
	}


	@Override
	public void montarInfoCarradas(int pipeiroSelecionadoImpressao,List<RelatorioDistribuicaoCarradasDTO> listaPontosAbastecimento,int codigoHistorico) throws Exception {

		RelatorioDistribuicaoCarradasDTO relatorio;
		for (int i = 0; i < listaPontosAbastecimento.size(); i++) {
			relatorio = listaPontosAbastecimento.get(i);
			
			recuperarDias(relatorio,pipeiroSelecionadoImpressao,codigoHistorico);
		}
		
	}


	private void recuperarDias(RelatorioDistribuicaoCarradasDTO relatorio,int pipeiroSelecionadoImpressao, int codigoHistorico) throws Exception {
		logger.info("Iniciando  recuperarDias(RelatorioDistribuicaoCarradasDTO relatorio,int pipeiroSelecionadoImpressao, int codigoHistorico)"
				+ "" + " relatorio: " + relatorio
				+ " pipeiroSelecionadoImpressao: " + pipeiroSelecionadoImpressao
				+ " codigoHistorico: " + codigoHistorico);
		
		try {
			
			
			conecction = ConexaoMySQL.getConnection();

			StringBuffer queryPA = new StringBuffer();

			queryPA.append("SELECT * FROM interacao_datas where cod_pipeiro  = ? and cod_pa = ? and COD_HISTORICO_DISTRIBUICAO = ? ");

			logger.info("Query: " + queryPA.toString());
			statement = conecction.prepareStatement(queryPA.toString());
			
			//Atribuindo parametro necessario para a query
			statement.setInt(1, pipeiroSelecionadoImpressao);
			statement.setInt(2, relatorio.getCodPA());
			statement.setInt(3, codigoHistorico);

			resultSet = statement.executeQuery();
			
			while(resultSet.next()){
				
				if(resultSet.getInt("indice") == 1){
					if(resultSet.getInt("eh_fim_de_semana") == 1)
					relatorio.setEhFimdeSemana1("S");
					relatorio.setDiaExibir1("S");
					relatorio.setDiaExt1(resultSet.getString("data_extenso"));
					if(resultSet.getInt("dias") != 0)
					relatorio.setDia1(String.valueOf(resultSet.getInt("dias")));
				}else if(resultSet.getInt("indice") == 2){
					if(resultSet.getInt("eh_fim_de_semana") == 1)
					relatorio.setEhFimdeSemana2("S");
					relatorio.setDiaExibir2("S");
					relatorio.setDiaExt2(resultSet.getString("data_extenso"));
					if(resultSet.getInt("dias") != 0)
					relatorio.setDia2(String.valueOf(resultSet.getInt("dias")));
				}else if(resultSet.getInt("indice") == 3){
					if(resultSet.getInt("eh_fim_de_semana") == 1)
					relatorio.setEhFimdeSemana3("S");
					relatorio.setDiaExibir3("S");
					relatorio.setDiaExt3(resultSet.getString("data_extenso"));
					if(resultSet.getInt("dias") != 0)
					relatorio.setDia3(String.valueOf(resultSet.getInt("dias")));
				}else if(resultSet.getInt("indice") == 4){
					if(resultSet.getInt("eh_fim_de_semana") == 1)
					relatorio.setEhFimdeSemana4("S");
					relatorio.setDiaExibir4("S");
					relatorio.setDiaExt4(resultSet.getString("data_extenso"));
					if(resultSet.getInt("dias") != 0)
					relatorio.setDia4(String.valueOf(resultSet.getInt("dias")));
				}else if(resultSet.getInt("indice") == 5){
					if(resultSet.getInt("eh_fim_de_semana") == 1)
					relatorio.setEhFimdeSemana5("S");
					relatorio.setDiaExibir5("S");
					relatorio.setDiaExt5(resultSet.getString("data_extenso"));
					if(resultSet.getInt("dias") != 0)
					relatorio.setDia5(String.valueOf(resultSet.getInt("dias")));
				}else if(resultSet.getInt("indice") == 6){
					if(resultSet.getInt("eh_fim_de_semana") == 1)
					relatorio.setEhFimdeSemana6("S");
					relatorio.setDiaExibir6("S");
					relatorio.setDiaExt6(resultSet.getString("data_extenso"));
					if(resultSet.getInt("dias") != 0)
					relatorio.setDia6(String.valueOf(resultSet.getInt("dias")));
				}else if(resultSet.getInt("indice") == 7){
					if(resultSet.getInt("eh_fim_de_semana") == 1)
					relatorio.setEhFimdeSemana7("S");
					relatorio.setDiaExibir7("S");
					relatorio.setDiaExt7(resultSet.getString("data_extenso"));
					if(resultSet.getInt("dias") != 0)
					relatorio.setDia7(String.valueOf(resultSet.getInt("dias")));
				}else if(resultSet.getInt("indice") == 8){
					if(resultSet.getInt("eh_fim_de_semana") == 1)
					relatorio.setEhFimdeSemana8("S");
					relatorio.setDiaExibir8("S");
					relatorio.setDiaExt8(resultSet.getString("data_extenso"));
					if(resultSet.getInt("dias") != 0)
					relatorio.setDia8(String.valueOf(resultSet.getInt("dias")));
				}else if(resultSet.getInt("indice") == 9){
					if(resultSet.getInt("eh_fim_de_semana") == 1)
					relatorio.setEhFimdeSemana9("S");
					relatorio.setDiaExibir9("S");
					relatorio.setDiaExt9(resultSet.getString("data_extenso"));
					if(resultSet.getInt("dias") != 0)
					relatorio.setDia9(String.valueOf(resultSet.getInt("dias")));
				}else if(resultSet.getInt("indice") == 10){
					if(resultSet.getInt("eh_fim_de_semana") == 1)
					relatorio.setEhFimdeSemana10("S");
					relatorio.setDiaExibir10("S");
					relatorio.setDiaExt10(resultSet.getString("data_extenso"));
					if(resultSet.getInt("dias") != 0)
					relatorio.setDia10(String.valueOf(resultSet.getInt("dias")));
				}else if(resultSet.getInt("indice") == 11){
					if(resultSet.getInt("eh_fim_de_semana") == 1)
					relatorio.setEhFimdeSemana11("S");
					relatorio.setDiaExibir11("S");
					relatorio.setDiaExt11(resultSet.getString("data_extenso"));
					if(resultSet.getInt("dias") != 0)
					relatorio.setDia11(String.valueOf(resultSet.getInt("dias")));
				}else if(resultSet.getInt("indice") == 12){
					if(resultSet.getInt("eh_fim_de_semana") == 1)
					relatorio.setEhFimdeSemana12("S");
					relatorio.setDiaExibir12("S");
					relatorio.setDiaExt12(resultSet.getString("data_extenso"));
					if(resultSet.getInt("dias") != 0)
					relatorio.setDia12(String.valueOf(resultSet.getInt("dias")));
				}else if(resultSet.getInt("indice") == 13){
					if(resultSet.getInt("eh_fim_de_semana") == 1)
					relatorio.setEhFimdeSemana13("S");
					relatorio.setDiaExibir13("S");
					relatorio.setDiaExt13(resultSet.getString("data_extenso"));
					if(resultSet.getInt("dias") != 0)
					relatorio.setDia13(String.valueOf(resultSet.getInt("dias")));
				}else if(resultSet.getInt("indice") == 14){
					if(resultSet.getInt("eh_fim_de_semana") == 1)
					relatorio.setEhFimdeSemana14("S");
					relatorio.setDiaExibir14("S");
					relatorio.setDiaExt14(resultSet.getString("data_extenso"));
					if(resultSet.getInt("dias") != 0)
					relatorio.setDia14(String.valueOf(resultSet.getInt("dias")));
				}else if(resultSet.getInt("indice") == 15){
					
					relatorio.setEhFimdeSemana15("S");
					relatorio.setDiaExibir15("S");
					relatorio.setDiaExt15(resultSet.getString("data_extenso"));
					if(resultSet.getInt("dias") != 0)
					relatorio.setDia15(String.valueOf(resultSet.getInt("dias")));
				}else if(resultSet.getInt("indice") == 16){
					if(resultSet.getInt("eh_fim_de_semana") == 1)
					relatorio.setEhFimdeSemana16("S");
					relatorio.setDiaExibir16("S");
					relatorio.setDiaExt16(resultSet.getString("data_extenso"));
					if(resultSet.getInt("dias") != 0)
					relatorio.setDia16(String.valueOf(resultSet.getInt("dias")));
				}else if(resultSet.getInt("indice") == 17){
					if(resultSet.getInt("eh_fim_de_semana") == 1)
					relatorio.setEhFimdeSemana17("S");
					relatorio.setDiaExibir17("S");
					relatorio.setDiaExt17(resultSet.getString("data_extenso"));
					if(resultSet.getInt("dias") != 0)
					relatorio.setDia17(String.valueOf(resultSet.getInt("dias")));
				}else if(resultSet.getInt("indice") == 18){
					if(resultSet.getInt("eh_fim_de_semana") == 1)
					relatorio.setEhFimdeSemana18("S");
					relatorio.setDiaExibir18("S");
					relatorio.setDiaExt18(resultSet.getString("data_extenso"));
					if(resultSet.getInt("dias") != 0)
					relatorio.setDia18(String.valueOf(resultSet.getInt("dias")));
				}else if(resultSet.getInt("indice") == 19){
					if(resultSet.getInt("eh_fim_de_semana") == 1)
					relatorio.setEhFimdeSemana19("S");
					relatorio.setDiaExibir19("S");
					relatorio.setDiaExt19(resultSet.getString("data_extenso"));
					if(resultSet.getInt("dias") != 0)
					relatorio.setDia19(String.valueOf(resultSet.getInt("dias")));
				}else if(resultSet.getInt("indice") == 20){
					if(resultSet.getInt("eh_fim_de_semana") == 1)
					relatorio.setEhFimdeSemana20("S");
					relatorio.setDiaExibir20("S");
					relatorio.setDiaExt20(resultSet.getString("data_extenso"));
					if(resultSet.getInt("dias") != 0)
					relatorio.setDia20(String.valueOf(resultSet.getInt("dias")));
				}else if(resultSet.getInt("indice") == 21){
					if(resultSet.getInt("eh_fim_de_semana") == 1)
					relatorio.setEhFimdeSemana21("S");
					relatorio.setDiaExibir21("S");
					relatorio.setDiaExt21(resultSet.getString("data_extenso"));
					if(resultSet.getInt("dias") != 0)
					relatorio.setDia21(String.valueOf(resultSet.getInt("dias")));
				}else if(resultSet.getInt("indice") == 22){
					if(resultSet.getInt("eh_fim_de_semana") == 1)
					relatorio.setEhFimdeSemana22("S");
					relatorio.setDiaExibir22("S");
					relatorio.setDiaExt22(resultSet.getString("data_extenso"));
					if(resultSet.getInt("dias") != 0)
					relatorio.setDia22(String.valueOf(resultSet.getInt("dias")));
				}else if(resultSet.getInt("indice") == 23){
					if(resultSet.getInt("eh_fim_de_semana") == 1)
					relatorio.setEhFimdeSemana23("S");
					relatorio.setDiaExibir23("S");
					relatorio.setDiaExt23(resultSet.getString("data_extenso"));
					if(resultSet.getInt("dias") != 0)
					relatorio.setDia23(String.valueOf(resultSet.getInt("dias")));
				}else if(resultSet.getInt("indice") == 24){
					if(resultSet.getInt("eh_fim_de_semana") == 1)
					relatorio.setEhFimdeSemana24("S");
					relatorio.setDiaExibir24("S");
					relatorio.setDiaExt24(resultSet.getString("data_extenso"));
					if(resultSet.getInt("dias") != 0)
					relatorio.setDia24(String.valueOf(resultSet.getInt("dias")));
				}else if(resultSet.getInt("indice") == 25){
					if(resultSet.getInt("eh_fim_de_semana") == 1)
					relatorio.setEhFimdeSemana25("S");
					relatorio.setDiaExibir25("S");
					relatorio.setDiaExt25(resultSet.getString("data_extenso"));
					if(resultSet.getInt("dias") != 0)
					relatorio.setDia25(String.valueOf(resultSet.getInt("dias")));
				}else if(resultSet.getInt("indice") == 26){
					if(resultSet.getInt("eh_fim_de_semana") == 1)
					relatorio.setEhFimdeSemana26("S");
					relatorio.setDiaExibir26("S");
					relatorio.setDiaExt26(resultSet.getString("data_extenso"));
					if(resultSet.getInt("dias") != 0)
					relatorio.setDia26(String.valueOf(resultSet.getInt("dias")));
				}else if(resultSet.getInt("indice") == 27){
					if(resultSet.getInt("eh_fim_de_semana") == 1)
					relatorio.setEhFimdeSemana27("S");
					relatorio.setDiaExibir27("S");
					relatorio.setDiaExt27(resultSet.getString("data_extenso"));
					if(resultSet.getInt("dias") != 0)
					relatorio.setDia27(String.valueOf(resultSet.getInt("dias")));
				}else if(resultSet.getInt("indice") == 28){
					if(resultSet.getInt("eh_fim_de_semana") == 1)
					relatorio.setEhFimdeSemana28("S");
					relatorio.setDiaExibir28("S");
					relatorio.setDiaExt28(resultSet.getString("data_extenso"));
					if(resultSet.getInt("dias") != 0)
					relatorio.setDia28(String.valueOf(resultSet.getInt("dias")));
				}else if(resultSet.getInt("indice") == 29){
					if(resultSet.getInt("eh_fim_de_semana") == 1)
					relatorio.setEhFimdeSemana29("S");
					relatorio.setDiaExibir29("S");
					relatorio.setDiaExt29(resultSet.getString("data_extenso"));
					if(resultSet.getInt("dias") != 0)
					relatorio.setDia29(String.valueOf(resultSet.getInt("dias")));
				}else if(resultSet.getInt("indice") == 30){
					if(resultSet.getInt("eh_fim_de_semana") == 1)
					relatorio.setEhFimdeSemana30("S");
					relatorio.setDiaExibir30("S");
					relatorio.setDiaExt30(resultSet.getString("data_extenso"));
					if(resultSet.getInt("dias") != 0)
					relatorio.setDia30(String.valueOf(resultSet.getInt("dias")));
				}else if(resultSet.getInt("indice") == 31){
					if(resultSet.getInt("eh_fim_de_semana") == 1)
					relatorio.setEhFimdeSemana31("S");
					relatorio.setDiaExibir31("S");
					relatorio.setDiaExt31(resultSet.getString("data_extenso"));
					if(resultSet.getInt("dias") != 0)
					relatorio.setDia31(String.valueOf(resultSet.getInt("dias")));
				}
			}

		}finally {
			//Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			//Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}
		
	}


	@Override
	public List recuperaAssinaturas(int codigoFuncionalidade) throws Exception {
		logger.info("Iniciando  recuperaAssinaturas(int codigoFuncionalidade)"
				+ "" + " codigoFuncionalidade: " + codigoFuncionalidade);	
		try {
			
			List<AssinaturasDTO> assinaturas = new ArrayList();
			AssinaturasDTO ass;
			
			conecction = ConexaoMySQL.getConnection();

			StringBuffer queryPA = new StringBuffer();

			queryPA.append("SELECT * FROM assinaturas where funcionalidade = ? ");
			
			logger.info("Query: " + queryPA.toString());
			statement = conecction.prepareStatement(queryPA.toString());
			
			//Atribuindo parametro necessario para a query
			statement.setInt(1, codigoFuncionalidade);

			resultSet = statement.executeQuery();
			
			while(resultSet.next()){
				ass = new AssinaturasDTO();
				
				ass.setCargo(resultSet.getString("cargo"));
				ass.setNome(resultSet.getString("nome"));
				ass.setAbrev(resultSet.getString("abrev"));
				
				assinaturas.add(ass);
			
			}
			
			return assinaturas;

		}finally {
			//Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			//Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}

	}


	@Override
	public Map recuperarParametrosHist(int mes, int ano) throws Exception {
		logger.info("Iniciando  recuperarParametrosHist(int mes, int ano)"
				+ "" + " mes: " + mes + " ano: " + ano);
		try {
			
			Map<String, Object> parametros = null;
			
			conecction = ConexaoMySQL.getConnection();

			StringBuffer queryPA = new StringBuffer();

			queryPA.append("SELECT * FROM hitorico_distribuicao where mes = ? AND ano = ? order by Id desc ");
			
			logger.info("Query: " + queryPA.toString());
			statement = conecction.prepareStatement(queryPA.toString());
			
			//Atribuindo parametro necessario para a query
			statement.setInt(1, mes);
			statement.setInt(2, ano);

			resultSet = statement.executeQuery();
			
			while(resultSet.next()){
				
				parametros = new HashMap();
				parametros.put("NUM_BAR", resultSet.getInt("NUM_BAR"));
				parametros.put("DATA_SORTEIO", resultSet.getDate("DATA_SORTEIO"));
				parametros.put("DATA_BAR", resultSet.getDate("DATA_BAR"));
			
				break;
			}
			
			return parametros;

		}finally {
			//Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			//Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}

	}


	@Override
	public Map recuperarParametrosInteraDistr(Integer mes, Integer ano,
			int pipeiroSelecionadoImpressao) throws Exception {
		logger.info("Iniciando  recuperarParametrosInteraDistr(Integer mes, Integer ano,int pipeiroSelecionadoImpressao)"
				+ "" + " mes: " + mes + " ano: " + ano + " pipeiroSelecionadoImpressao: " + pipeiroSelecionadoImpressao);
		
		try {
			
		Map<String, Object> parametros = null;
		
		conecction = ConexaoMySQL.getConnection();

		StringBuffer queryPA = new StringBuffer();

		queryPA.append("SELECT * FROM operacaopipa.interacao_ditribuicao where cod_pipeiro = ? and mes = ? and ano = ? and num_lacre is not null");
		
		logger.info("Query: " + queryPA.toString());
		statement = conecction.prepareStatement(queryPA.toString());
		
		//Atribuindo parametro necessario para a query
		statement.setInt(1, pipeiroSelecionadoImpressao);
		statement.setInt(2, mes);
		statement.setInt(3, ano);

		resultSet = statement.executeQuery();
		
		while(resultSet.next()){
			
			parametros = new HashMap();
			parametros.put("NUM_LACRE", resultSet.getInt("NUM_LACRE"));
			parametros.put("NUM_OS", resultSet.getInt("NUM_OS"));
		
			break;
		}
		
		return parametros;

	}finally {
		//Fechar ResultSet e Statement
		ConexaoMySQL.finalizarSql(resultSet, statement);
		//Fechar conexao
		ConexaoMySQL.fecharConexao(conecction);
	}

	}


	@Override
	public List recuperarInteracaoDistirbuicao(int cidadeSelecionada, int mes, int ano) throws Exception {
		logger.info("Iniciando  recuperarInteracaoDistirbuicao(int cidadeSelecionada, int mes, int ano)"
				+ "" + " cidadeSelecionada: " + cidadeSelecionada + " mes: " + mes
				+ " ano: " + ano);	
		try {
			
			List<InteracaoDistribuicaoDTO> intList = new ArrayList();
			InteracaoDistribuicaoDTO intObj;
			
			conecction = ConexaoMySQL.getConnection();

			StringBuffer queryPA = new StringBuffer();

			queryPA.append("\n SELECT pipe.NOME as NOME_PIPEIRO, pipe.CPF, cid.NOME as NOME_CIDADE, vei.PLACA, vei.CAPACIDADEPIPA, inte.* FROM interacao_ditribuicao inte, pipeiro pipe, cidade cid, veiculo vei"
					+ "\n where inte.cod_cidade = ? and inte.MES = ? AND inte.ano = ? AND inte.COD_PIPEIRO = pipe.ID AND inte.COD_CIDADE = cid.ID AND inte.COD_VEICULO = vei.ID ");
			
			logger.info("Query: " + queryPA.toString());
			statement = conecction.prepareStatement(queryPA.toString());
			
			//Atribuindo parametro necessario para a query
			statement.setInt(1, cidadeSelecionada);
			statement.setInt(2, mes);
			statement.setInt(3, ano);

			resultSet = statement.executeQuery();
			
			while(resultSet.next()){
				intObj = new InteracaoDistribuicaoDTO();
				
				intObj.getPipeiroDTO().setNome(resultSet.getString("NOME_PIPEIRO"));
				intObj.getPipeiroDTO().setId(resultSet.getInt("COD_PIPEIRO"));
				intObj.getPipeiroDTO().setCpf(resultSet.getString("CPF"));
				intObj.setTotalApontadores(resultSet.getInt("TOTAL_APONTADORES"));
				intObj.setCodHistoricoDistr(resultSet.getInt("COD_HISTORICO_DISTRIBUICAO"));
				
				intObj.getCidadeDTO().setCidade(resultSet.getString("NOME_CIDADE"));
				intObj.getCidadeDTO().setId(resultSet.getInt("COD_CIDADE"));
				intObj.getPontoColetaDTO().setId(resultSet.getInt("COD_PC"));
				intObj.getVeiculoDTO().setId(resultSet.getInt("COD_VEICULO"));
				intObj.getVeiculoDTO().setPlaca(resultSet.getString("PLACA"));
				intObj.getVeiculoDTO().setCapacidadePipa(resultSet.getDouble("CAPACIDADEPIPA"));
				intObj.setQtdDias(resultSet.getInt("QTD_DIAS"));
				intObj.setQtdLitrosPessoa(resultSet.getInt("QTD_LITROS_PESSOA"));
				intObj.setValorTotal(resultSet.getDouble("VALOR_TOTAL"));
				intObj.setKmTotal(resultSet.getInt("KM_TOTAL"));
				intObj.setTotalViagensFormula(resultSet.getDouble("TOTAL_VIAGEM_FORMULA"));
				intObj.setTotalViagensReal(resultSet.getInt("TOTAL_VIAGEM_REAL"));
				intObj.setTotalPessoas(resultSet.getInt("TOTAL_PESSOAS"));
				intObj.setTotalQdtAgua(resultSet.getInt("TOTAL_QTD_AGUA"));
				intObj.setTotalDistancia(resultSet.getInt("TOTAL_DISTANCIA"));
				
				intList.add(intObj);
			
			}
			
			return intList;

		}finally {
			//Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			//Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}

	}


	@Override
	public List recuperarPontoAbastecimentoEdicao(int idPipeiro,int codHistorico) throws Exception {
		logger.info("Iniciando  recuperarPontoAbastecimentoEdicao(int idPipeiro,int codHistorico)"
				+ "" + " idPipeiro: " + idPipeiro + " codHistorico: " + codHistorico);	
		try {
			
			List<PontoAbastecimentoDTO> paList = new ArrayList();
			PontoAbastecimentoDTO paObj;
			
			conecction = ConexaoMySQL.getConnection();

			StringBuffer queryPA = new StringBuffer();

			queryPA.append("SELECT ponto.COMUNIDADE, ponto.id, intePI.VIAGEM_REAL FROM interacao_pipeiro_pa intePI, pontoabastecimento ponto where"
					+ " intePI.cod_pipeiro = ? and intePI. COD_PA = ponto.id and intePI.COD_HISTORICO_DISTRIBUICAO =  ?");
			
			logger.info("Query: " + queryPA.toString());
			statement = conecction.prepareStatement(queryPA.toString());
			
			//Atribuindo parametro necessario para a query
			statement.setInt(1, idPipeiro);
			statement.setInt(2, codHistorico);

			resultSet = statement.executeQuery();
			
			while(resultSet.next()){
				paObj = new PontoAbastecimentoDTO();
				
				paObj.setComunidade(resultSet.getString("COMUNIDADE"));
				paObj.setId(resultSet.getInt("ID"));
				paObj.setQtdViagensReal(resultSet.getInt("VIAGEM_REAL"));
				
				paList.add(paObj);
			
			}
			
			return paList;

		}finally {
			//Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			//Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}
	}


	@Override
	public void atribuirDiasDistribuicao(int idPipeiro, int codHistoricoDistr,PontoAbastecimentoDTO pa) throws Exception {
		logger.info("Iniciando  atribuirDiasDistribuicao(int idPipeiro,int codHistorico,PontoAbastecimentoDTO pa)"
				+ "" + " idPipeiro: " + idPipeiro + " codHistoricoDistr: " + codHistoricoDistr + " pa: " + pa.getId());	
		try {
			
			conecction = ConexaoMySQL.getConnection();

			StringBuffer queryPA = new StringBuffer();

			queryPA.append("SELECT * FROM operacaopipa.interacao_datas where cod_pipeiro = ? and COD_HISTORICO_DISTRIBUICAO = ? and cod_pa = ?");
			
			logger.info("Query: " + queryPA.toString());
			statement = conecction.prepareStatement(queryPA.toString());
			
			//Atribuindo parametro necessario para a query
			statement.setInt(1, idPipeiro);
			statement.setInt(2, codHistoricoDistr);
			statement.setInt(3, pa.getId());

			resultSet = statement.executeQuery();
			
			while(resultSet.next()){
				
				if(resultSet.getInt("indice") == 1){
					pa.getDistribuicaoDiasPipeiroDTO().getDia1().setData(resultSet.getDate(("data")));
					pa.getDistribuicaoDiasPipeiroDTO().getDia1().setDatasExtenso(resultSet.getString("data_extenso"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia1().setIndice(resultSet.getInt("indice"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia1().setDias(resultSet.getInt("dias"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia1().setEhFimDeSemana(resultSet.getInt("eh_fim_de_semana") == 1?true:false);
					pa.getDistribuicaoDiasPipeiroDTO().getDia1().setExibirColuna(resultSet.getInt("exibir_coluna") == 1?true:false);
				}else if(resultSet.getInt("indice") == 2){
					pa.getDistribuicaoDiasPipeiroDTO().getDia2().setData(resultSet.getDate(("data")));
					pa.getDistribuicaoDiasPipeiroDTO().getDia2().setDatasExtenso(resultSet.getString("data_extenso"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia2().setIndice(resultSet.getInt("indice"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia2().setDias(resultSet.getInt("dias"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia2().setEhFimDeSemana(resultSet.getInt("eh_fim_de_semana") == 1?true:false);
					pa.getDistribuicaoDiasPipeiroDTO().getDia2().setExibirColuna(resultSet.getInt("exibir_coluna") == 1?true:false);
				}else if(resultSet.getInt("indice") == 3){
					pa.getDistribuicaoDiasPipeiroDTO().getDia3().setData(resultSet.getDate(("data")));
					pa.getDistribuicaoDiasPipeiroDTO().getDia3().setDatasExtenso(resultSet.getString("data_extenso"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia3().setIndice(resultSet.getInt("indice"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia3().setDias(resultSet.getInt("dias"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia3().setEhFimDeSemana(resultSet.getInt("eh_fim_de_semana") == 1?true:false);
					pa.getDistribuicaoDiasPipeiroDTO().getDia3().setExibirColuna(resultSet.getInt("exibir_coluna") == 1?true:false);
				}else if(resultSet.getInt("indice") == 4){
					pa.getDistribuicaoDiasPipeiroDTO().getDia4().setData(resultSet.getDate(("data")));
					pa.getDistribuicaoDiasPipeiroDTO().getDia4().setDatasExtenso(resultSet.getString("data_extenso"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia4().setIndice(resultSet.getInt("indice"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia4().setDias(resultSet.getInt("dias"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia4().setEhFimDeSemana(resultSet.getInt("eh_fim_de_semana") == 1?true:false);
					pa.getDistribuicaoDiasPipeiroDTO().getDia4().setExibirColuna(resultSet.getInt("exibir_coluna") == 1?true:false);
				}else if(resultSet.getInt("indice") == 5){
					pa.getDistribuicaoDiasPipeiroDTO().getDia5().setData(resultSet.getDate(("data")));
					pa.getDistribuicaoDiasPipeiroDTO().getDia5().setDatasExtenso(resultSet.getString("data_extenso"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia5().setIndice(resultSet.getInt("indice"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia5().setDias(resultSet.getInt("dias"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia5().setEhFimDeSemana(resultSet.getInt("eh_fim_de_semana") == 1?true:false);
					pa.getDistribuicaoDiasPipeiroDTO().getDia5().setExibirColuna(resultSet.getInt("exibir_coluna") == 1?true:false);
				}else if(resultSet.getInt("indice") == 6){
					pa.getDistribuicaoDiasPipeiroDTO().getDia6().setData(resultSet.getDate(("data")));
					pa.getDistribuicaoDiasPipeiroDTO().getDia6().setDatasExtenso(resultSet.getString("data_extenso"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia6().setIndice(resultSet.getInt("indice"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia6().setDias(resultSet.getInt("dias"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia6().setEhFimDeSemana(resultSet.getInt("eh_fim_de_semana") == 1?true:false);
					pa.getDistribuicaoDiasPipeiroDTO().getDia6().setExibirColuna(resultSet.getInt("exibir_coluna") == 1?true:false);
				}else if(resultSet.getInt("indice") == 7){
					pa.getDistribuicaoDiasPipeiroDTO().getDia7().setData(resultSet.getDate(("data")));
					pa.getDistribuicaoDiasPipeiroDTO().getDia7().setDatasExtenso(resultSet.getString("data_extenso"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia7().setIndice(resultSet.getInt("indice"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia7().setDias(resultSet.getInt("dias"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia7().setEhFimDeSemana(resultSet.getInt("eh_fim_de_semana") == 1?true:false);
					pa.getDistribuicaoDiasPipeiroDTO().getDia7().setExibirColuna(resultSet.getInt("exibir_coluna") == 1?true:false);
				}else if(resultSet.getInt("indice") == 8){
					pa.getDistribuicaoDiasPipeiroDTO().getDia8().setData(resultSet.getDate(("data")));
					pa.getDistribuicaoDiasPipeiroDTO().getDia8().setDatasExtenso(resultSet.getString("data_extenso"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia8().setIndice(resultSet.getInt("indice"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia8().setDias(resultSet.getInt("dias"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia8().setEhFimDeSemana(resultSet.getInt("eh_fim_de_semana") == 1?true:false);
					pa.getDistribuicaoDiasPipeiroDTO().getDia8().setExibirColuna(resultSet.getInt("exibir_coluna") == 1?true:false);
				}else if(resultSet.getInt("indice") == 9){
					pa.getDistribuicaoDiasPipeiroDTO().getDia9().setData(resultSet.getDate(("data")));
					pa.getDistribuicaoDiasPipeiroDTO().getDia9().setDatasExtenso(resultSet.getString("data_extenso"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia9().setIndice(resultSet.getInt("indice"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia9().setDias(resultSet.getInt("dias"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia9().setEhFimDeSemana(resultSet.getInt("eh_fim_de_semana") == 1?true:false);
					pa.getDistribuicaoDiasPipeiroDTO().getDia9().setExibirColuna(resultSet.getInt("exibir_coluna") == 1?true:false);
				}else if(resultSet.getInt("indice") == 10){
					pa.getDistribuicaoDiasPipeiroDTO().getDia10().setData(resultSet.getDate(("data")));
					pa.getDistribuicaoDiasPipeiroDTO().getDia10().setDatasExtenso(resultSet.getString("data_extenso"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia10().setIndice(resultSet.getInt("indice"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia10().setDias(resultSet.getInt("dias"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia10().setEhFimDeSemana(resultSet.getInt("eh_fim_de_semana") == 1?true:false);
					pa.getDistribuicaoDiasPipeiroDTO().getDia10().setExibirColuna(resultSet.getInt("exibir_coluna") == 1?true:false);
				}else if(resultSet.getInt("indice") == 11){
					pa.getDistribuicaoDiasPipeiroDTO().getDia11().setData(resultSet.getDate(("data")));
					pa.getDistribuicaoDiasPipeiroDTO().getDia11().setDatasExtenso(resultSet.getString("data_extenso"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia11().setIndice(resultSet.getInt("indice"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia11().setDias(resultSet.getInt("dias"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia11().setEhFimDeSemana(resultSet.getInt("eh_fim_de_semana") == 1?true:false);
					pa.getDistribuicaoDiasPipeiroDTO().getDia11().setExibirColuna(resultSet.getInt("exibir_coluna") == 1?true:false);
				}else if(resultSet.getInt("indice") == 12){
					pa.getDistribuicaoDiasPipeiroDTO().getDia12().setData(resultSet.getDate(("data")));
					pa.getDistribuicaoDiasPipeiroDTO().getDia12().setDatasExtenso(resultSet.getString("data_extenso"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia12().setIndice(resultSet.getInt("indice"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia12().setDias(resultSet.getInt("dias"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia12().setEhFimDeSemana(resultSet.getInt("eh_fim_de_semana") == 1?true:false);
					pa.getDistribuicaoDiasPipeiroDTO().getDia12().setExibirColuna(resultSet.getInt("exibir_coluna") == 1?true:false);
				}else if(resultSet.getInt("indice") == 13){
					pa.getDistribuicaoDiasPipeiroDTO().getDia13().setData(resultSet.getDate(("data")));
					pa.getDistribuicaoDiasPipeiroDTO().getDia13().setDatasExtenso(resultSet.getString("data_extenso"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia13().setIndice(resultSet.getInt("indice"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia13().setDias(resultSet.getInt("dias"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia13().setEhFimDeSemana(resultSet.getInt("eh_fim_de_semana") == 1?true:false);
					pa.getDistribuicaoDiasPipeiroDTO().getDia13().setExibirColuna(resultSet.getInt("exibir_coluna") == 1?true:false);
				}else if(resultSet.getInt("indice") == 14){
					pa.getDistribuicaoDiasPipeiroDTO().getDia14().setData(resultSet.getDate(("data")));
					pa.getDistribuicaoDiasPipeiroDTO().getDia14().setDatasExtenso(resultSet.getString("data_extenso"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia14().setIndice(resultSet.getInt("indice"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia14().setDias(resultSet.getInt("dias"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia14().setEhFimDeSemana(resultSet.getInt("eh_fim_de_semana") == 1?true:false);
					pa.getDistribuicaoDiasPipeiroDTO().getDia14().setExibirColuna(resultSet.getInt("exibir_coluna") == 1?true:false);
				}else if(resultSet.getInt("indice") == 15){
					pa.getDistribuicaoDiasPipeiroDTO().getDia15().setData(resultSet.getDate(("data")));
					pa.getDistribuicaoDiasPipeiroDTO().getDia15().setDatasExtenso(resultSet.getString("data_extenso"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia15().setIndice(resultSet.getInt("indice"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia15().setDias(resultSet.getInt("dias"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia15().setEhFimDeSemana(resultSet.getInt("eh_fim_de_semana") == 1?true:false);
					pa.getDistribuicaoDiasPipeiroDTO().getDia15().setExibirColuna(resultSet.getInt("exibir_coluna") == 1?true:false);
				}else if(resultSet.getInt("indice") == 16){
					pa.getDistribuicaoDiasPipeiroDTO().getDia16().setData(resultSet.getDate(("data")));
					pa.getDistribuicaoDiasPipeiroDTO().getDia16().setDatasExtenso(resultSet.getString("data_extenso"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia16().setIndice(resultSet.getInt("indice"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia16().setDias(resultSet.getInt("dias"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia16().setEhFimDeSemana(resultSet.getInt("eh_fim_de_semana") == 1?true:false);
					pa.getDistribuicaoDiasPipeiroDTO().getDia16().setExibirColuna(resultSet.getInt("exibir_coluna") == 1?true:false);
				}else if(resultSet.getInt("indice") == 17){
					pa.getDistribuicaoDiasPipeiroDTO().getDia17().setData(resultSet.getDate(("data")));
					pa.getDistribuicaoDiasPipeiroDTO().getDia17().setDatasExtenso(resultSet.getString("data_extenso"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia17().setIndice(resultSet.getInt("indice"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia17().setDias(resultSet.getInt("dias"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia17().setEhFimDeSemana(resultSet.getInt("eh_fim_de_semana") == 1?true:false);
					pa.getDistribuicaoDiasPipeiroDTO().getDia17().setExibirColuna(resultSet.getInt("exibir_coluna") == 1?true:false);
				}else if(resultSet.getInt("indice") == 18){
					pa.getDistribuicaoDiasPipeiroDTO().getDia18().setData(resultSet.getDate(("data")));
					pa.getDistribuicaoDiasPipeiroDTO().getDia18().setDatasExtenso(resultSet.getString("data_extenso"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia18().setIndice(resultSet.getInt("indice"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia18().setDias(resultSet.getInt("dias"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia18().setEhFimDeSemana(resultSet.getInt("eh_fim_de_semana") == 1?true:false);
					pa.getDistribuicaoDiasPipeiroDTO().getDia18().setExibirColuna(resultSet.getInt("exibir_coluna") == 1?true:false);
				}else if(resultSet.getInt("indice") == 19){
					pa.getDistribuicaoDiasPipeiroDTO().getDia19().setData(resultSet.getDate(("data")));
					pa.getDistribuicaoDiasPipeiroDTO().getDia19().setDatasExtenso(resultSet.getString("data_extenso"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia19().setIndice(resultSet.getInt("indice"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia19().setDias(resultSet.getInt("dias"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia19().setEhFimDeSemana(resultSet.getInt("eh_fim_de_semana") == 1?true:false);
					pa.getDistribuicaoDiasPipeiroDTO().getDia19().setExibirColuna(resultSet.getInt("exibir_coluna") == 1?true:false);
				}else if(resultSet.getInt("indice") == 20){
					pa.getDistribuicaoDiasPipeiroDTO().getDia20().setData(resultSet.getDate(("data")));
					pa.getDistribuicaoDiasPipeiroDTO().getDia20().setDatasExtenso(resultSet.getString("data_extenso"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia20().setIndice(resultSet.getInt("indice"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia20().setDias(resultSet.getInt("dias"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia20().setEhFimDeSemana(resultSet.getInt("eh_fim_de_semana") == 1?true:false);
					pa.getDistribuicaoDiasPipeiroDTO().getDia20().setExibirColuna(resultSet.getInt("exibir_coluna") == 1?true:false);
				}else if(resultSet.getInt("indice") == 21){
					pa.getDistribuicaoDiasPipeiroDTO().getDia21().setData(resultSet.getDate(("data")));
					pa.getDistribuicaoDiasPipeiroDTO().getDia21().setDatasExtenso(resultSet.getString("data_extenso"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia21().setIndice(resultSet.getInt("indice"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia21().setDias(resultSet.getInt("dias"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia21().setEhFimDeSemana(resultSet.getInt("eh_fim_de_semana") == 1?true:false);
					pa.getDistribuicaoDiasPipeiroDTO().getDia21().setExibirColuna(resultSet.getInt("exibir_coluna") == 1?true:false);
				}else if(resultSet.getInt("indice") == 22){
					pa.getDistribuicaoDiasPipeiroDTO().getDia22().setData(resultSet.getDate(("data")));
					pa.getDistribuicaoDiasPipeiroDTO().getDia22().setDatasExtenso(resultSet.getString("data_extenso"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia22().setIndice(resultSet.getInt("indice"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia22().setDias(resultSet.getInt("dias"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia22().setEhFimDeSemana(resultSet.getInt("eh_fim_de_semana") == 1?true:false);
					pa.getDistribuicaoDiasPipeiroDTO().getDia22().setExibirColuna(resultSet.getInt("exibir_coluna") == 1?true:false);
				}else if(resultSet.getInt("indice") == 23){
					pa.getDistribuicaoDiasPipeiroDTO().getDia23().setData(resultSet.getDate(("data")));
					pa.getDistribuicaoDiasPipeiroDTO().getDia23().setDatasExtenso(resultSet.getString("data_extenso"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia23().setIndice(resultSet.getInt("indice"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia23().setDias(resultSet.getInt("dias"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia23().setEhFimDeSemana(resultSet.getInt("eh_fim_de_semana") == 1?true:false);
					pa.getDistribuicaoDiasPipeiroDTO().getDia23().setExibirColuna(resultSet.getInt("exibir_coluna") == 1?true:false);
				}else if(resultSet.getInt("indice") == 24){
					pa.getDistribuicaoDiasPipeiroDTO().getDia24().setData(resultSet.getDate(("data")));
					pa.getDistribuicaoDiasPipeiroDTO().getDia24().setDatasExtenso(resultSet.getString("data_extenso"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia24().setIndice(resultSet.getInt("indice"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia24().setDias(resultSet.getInt("dias"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia24().setEhFimDeSemana(resultSet.getInt("eh_fim_de_semana") == 1?true:false);
					pa.getDistribuicaoDiasPipeiroDTO().getDia24().setExibirColuna(resultSet.getInt("exibir_coluna") == 1?true:false);
				}else if(resultSet.getInt("indice") == 25){
					pa.getDistribuicaoDiasPipeiroDTO().getDia25().setData(resultSet.getDate(("data")));
					pa.getDistribuicaoDiasPipeiroDTO().getDia25().setDatasExtenso(resultSet.getString("data_extenso"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia25().setIndice(resultSet.getInt("indice"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia25().setDias(resultSet.getInt("dias"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia25().setEhFimDeSemana(resultSet.getInt("eh_fim_de_semana") == 1?true:false);
					pa.getDistribuicaoDiasPipeiroDTO().getDia25().setExibirColuna(resultSet.getInt("exibir_coluna") == 1?true:false);
				}else if(resultSet.getInt("indice") == 26){
					pa.getDistribuicaoDiasPipeiroDTO().getDia26().setData(resultSet.getDate(("data")));
					pa.getDistribuicaoDiasPipeiroDTO().getDia26().setDatasExtenso(resultSet.getString("data_extenso"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia26().setIndice(resultSet.getInt("indice"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia26().setDias(resultSet.getInt("dias"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia26().setEhFimDeSemana(resultSet.getInt("eh_fim_de_semana") == 1?true:false);
					pa.getDistribuicaoDiasPipeiroDTO().getDia26().setExibirColuna(resultSet.getInt("exibir_coluna") == 1?true:false);
				}else if(resultSet.getInt("indice") == 27){
					pa.getDistribuicaoDiasPipeiroDTO().getDia27().setData(resultSet.getDate(("data")));
					pa.getDistribuicaoDiasPipeiroDTO().getDia27().setDatasExtenso(resultSet.getString("data_extenso"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia27().setIndice(resultSet.getInt("indice"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia27().setDias(resultSet.getInt("dias"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia27().setEhFimDeSemana(resultSet.getInt("eh_fim_de_semana") == 1?true:false);
					pa.getDistribuicaoDiasPipeiroDTO().getDia27().setExibirColuna(resultSet.getInt("exibir_coluna") == 1?true:false);
				}else if(resultSet.getInt("indice") == 28){
					pa.getDistribuicaoDiasPipeiroDTO().getDia28().setData(resultSet.getDate(("data")));
					pa.getDistribuicaoDiasPipeiroDTO().getDia28().setDatasExtenso(resultSet.getString("data_extenso"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia28().setIndice(resultSet.getInt("indice"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia28().setDias(resultSet.getInt("dias"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia28().setEhFimDeSemana(resultSet.getInt("eh_fim_de_semana") == 1?true:false);
					pa.getDistribuicaoDiasPipeiroDTO().getDia28().setExibirColuna(resultSet.getInt("exibir_coluna") == 1?true:false);
				}else if(resultSet.getInt("indice") == 29){
					pa.getDistribuicaoDiasPipeiroDTO().getDia29().setData(resultSet.getDate(("data")));
					pa.getDistribuicaoDiasPipeiroDTO().getDia29().setDatasExtenso(resultSet.getString("data_extenso"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia29().setIndice(resultSet.getInt("indice"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia29().setDias(resultSet.getInt("dias"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia29().setEhFimDeSemana(resultSet.getInt("eh_fim_de_semana") == 1?true:false);
					pa.getDistribuicaoDiasPipeiroDTO().getDia29().setExibirColuna(resultSet.getInt("exibir_coluna") == 1?true:false);
				}else if(resultSet.getInt("indice") == 30){
					pa.getDistribuicaoDiasPipeiroDTO().getDia30().setData(resultSet.getDate(("data")));
					pa.getDistribuicaoDiasPipeiroDTO().getDia30().setDatasExtenso(resultSet.getString("data_extenso"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia30().setIndice(resultSet.getInt("indice"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia30().setDias(resultSet.getInt("dias"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia30().setEhFimDeSemana(resultSet.getInt("eh_fim_de_semana") == 1?true:false);
					pa.getDistribuicaoDiasPipeiroDTO().getDia30().setExibirColuna(resultSet.getInt("exibir_coluna") == 1?true:false);
				}else if(resultSet.getInt("indice") == 31){
					pa.getDistribuicaoDiasPipeiroDTO().getDia31().setData(resultSet.getDate(("data")));
					pa.getDistribuicaoDiasPipeiroDTO().getDia31().setDatasExtenso(resultSet.getString("data_extenso"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia31().setIndice(resultSet.getInt("indice"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia31().setDias(resultSet.getInt("dias"));
					pa.getDistribuicaoDiasPipeiroDTO().getDia31().setEhFimDeSemana(resultSet.getInt("eh_fim_de_semana") == 1?true:false);
					pa.getDistribuicaoDiasPipeiroDTO().getDia31().setExibirColuna(resultSet.getInt("exibir_coluna") == 1?true:false);
				}
			
			}
			
		}finally {
			//Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			//Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}
		
	}


	@Override
	public void atualizarDiasDistribuicao(int idPipeiro, int codHistoricoDistr,PontoAbastecimentoDTO pa) throws Exception {
		
		atualizarDiaPA(idPipeiro,codHistoricoDistr, pa.getId(), pa.getDistribuicaoDiasPipeiroDTO().getDia1());
		atualizarDiaPA(idPipeiro,codHistoricoDistr, pa.getId(), pa.getDistribuicaoDiasPipeiroDTO().getDia2());
		atualizarDiaPA(idPipeiro,codHistoricoDistr, pa.getId(), pa.getDistribuicaoDiasPipeiroDTO().getDia3());
		atualizarDiaPA(idPipeiro,codHistoricoDistr, pa.getId(), pa.getDistribuicaoDiasPipeiroDTO().getDia4());
		atualizarDiaPA(idPipeiro,codHistoricoDistr, pa.getId(), pa.getDistribuicaoDiasPipeiroDTO().getDia5());
		atualizarDiaPA(idPipeiro,codHistoricoDistr, pa.getId(), pa.getDistribuicaoDiasPipeiroDTO().getDia6());
		atualizarDiaPA(idPipeiro,codHistoricoDistr, pa.getId(), pa.getDistribuicaoDiasPipeiroDTO().getDia7());
		atualizarDiaPA(idPipeiro,codHistoricoDistr, pa.getId(), pa.getDistribuicaoDiasPipeiroDTO().getDia8());
		atualizarDiaPA(idPipeiro,codHistoricoDistr, pa.getId(), pa.getDistribuicaoDiasPipeiroDTO().getDia9());
		atualizarDiaPA(idPipeiro,codHistoricoDistr, pa.getId(), pa.getDistribuicaoDiasPipeiroDTO().getDia10());
		atualizarDiaPA(idPipeiro,codHistoricoDistr, pa.getId(), pa.getDistribuicaoDiasPipeiroDTO().getDia11());
		atualizarDiaPA(idPipeiro,codHistoricoDistr, pa.getId(), pa.getDistribuicaoDiasPipeiroDTO().getDia12());
		atualizarDiaPA(idPipeiro,codHistoricoDistr, pa.getId(), pa.getDistribuicaoDiasPipeiroDTO().getDia13());
		atualizarDiaPA(idPipeiro,codHistoricoDistr, pa.getId(), pa.getDistribuicaoDiasPipeiroDTO().getDia14());
		atualizarDiaPA(idPipeiro,codHistoricoDistr, pa.getId(), pa.getDistribuicaoDiasPipeiroDTO().getDia15());
		atualizarDiaPA(idPipeiro,codHistoricoDistr, pa.getId(), pa.getDistribuicaoDiasPipeiroDTO().getDia16());
		atualizarDiaPA(idPipeiro,codHistoricoDistr, pa.getId(), pa.getDistribuicaoDiasPipeiroDTO().getDia17());
		atualizarDiaPA(idPipeiro,codHistoricoDistr, pa.getId(), pa.getDistribuicaoDiasPipeiroDTO().getDia18());
		atualizarDiaPA(idPipeiro,codHistoricoDistr, pa.getId(), pa.getDistribuicaoDiasPipeiroDTO().getDia19());
		atualizarDiaPA(idPipeiro,codHistoricoDistr, pa.getId(), pa.getDistribuicaoDiasPipeiroDTO().getDia20());
		atualizarDiaPA(idPipeiro,codHistoricoDistr, pa.getId(), pa.getDistribuicaoDiasPipeiroDTO().getDia21());
		atualizarDiaPA(idPipeiro,codHistoricoDistr, pa.getId(), pa.getDistribuicaoDiasPipeiroDTO().getDia22());
		atualizarDiaPA(idPipeiro,codHistoricoDistr, pa.getId(), pa.getDistribuicaoDiasPipeiroDTO().getDia23());
		atualizarDiaPA(idPipeiro,codHistoricoDistr, pa.getId(), pa.getDistribuicaoDiasPipeiroDTO().getDia24());
		atualizarDiaPA(idPipeiro,codHistoricoDistr, pa.getId(), pa.getDistribuicaoDiasPipeiroDTO().getDia25());
		atualizarDiaPA(idPipeiro,codHistoricoDistr, pa.getId(), pa.getDistribuicaoDiasPipeiroDTO().getDia26());
		atualizarDiaPA(idPipeiro,codHistoricoDistr, pa.getId(), pa.getDistribuicaoDiasPipeiroDTO().getDia27());
		atualizarDiaPA(idPipeiro,codHistoricoDistr, pa.getId(), pa.getDistribuicaoDiasPipeiroDTO().getDia28());
		atualizarDiaPA(idPipeiro,codHistoricoDistr, pa.getId(), pa.getDistribuicaoDiasPipeiroDTO().getDia29());
		atualizarDiaPA(idPipeiro,codHistoricoDistr, pa.getId(), pa.getDistribuicaoDiasPipeiroDTO().getDia30());
		atualizarDiaPA(idPipeiro,codHistoricoDistr, pa.getId(), pa.getDistribuicaoDiasPipeiroDTO().getDia31());
		
	}


	private void atualizarDiaPA(int idPipeiro, int codHistoricoDistr, int idPA, DataDTO dataDTO) throws Exception {
		logger.info("Iniciando  atualizarDiaPA(int idPipeiro, int codHistoricoDistr, int idPA, int indice)" + " idPipeiro: " + idPipeiro
				+ " codHistoricoDistr: " + codHistoricoDistr + " idPA: " + idPA + " dataDTO: " + dataDTO);
		
		try {
			
			conecction = ConexaoMySQL.getConnection();

			StringBuffer queryUpdate = new StringBuffer();
			

			queryUpdate.append("\n UPDATE interacao_datas SET dias = ? "
					+ "\n WHERE cod_pipeiro = ? AND COD_HISTORICO_DISTRIBUICAO = ? and cod_pa = ? and indice = ? ");

			logger.info("Query: " + queryUpdate.toString());
			
			statement = conecction.prepareStatement(queryUpdate.toString());
			
			//Atribuindo parametro necessario para a query
			statement.setInt(1, dataDTO.getDias());
			statement.setInt(2, idPipeiro);
			statement.setInt(3, codHistoricoDistr);
			statement.setInt(4, idPA);
			statement.setInt(5, dataDTO.getIndice());

			statement.executeUpdate();
			

		}finally {
			//Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			//Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}
	}


	@Override
	public List recuperarInteracaoPipeiroPA(int codHistoricoDistr, int idPipeiro)
			throws Exception {
		logger.info("Iniciando  recuperarInteracaoPipeiroPA(int idPipeiro,int codHistorico)"
				+ "" + " idPipeiro: " + idPipeiro + " codHistorico: " + codHistoricoDistr);	
		try {
			
			List paList = new ArrayList();
			InteracaoPipeiroPADTO paObj;
			
			conecction = ConexaoMySQL.getConnection();

			StringBuffer queryPA = new StringBuffer();

			queryPA.append("\n SELECT intePI.*, pa.COMUNIDADE, pa.APONTADOR, pa.Populacao, ind.INDICE as INDICE_PA FROM interacao_pipeiro_pa intePI, pontoabastecimento pa, indice ind where "
					+ "\n intePI.cod_pipeiro = ? and intePI.COD_HISTORICO_DISTRIBUICAO = ? AND intePI.COD_PA = pa.ID AND pa.COD_INDICE = ind.ID");
			
			logger.info("Query: " + queryPA.toString());
			statement = conecction.prepareStatement(queryPA.toString());
			
			//Atribuindo parametro necessario para a query
			statement.setInt(1, idPipeiro);
			statement.setInt(2, codHistoricoDistr);

			resultSet = statement.executeQuery();
			
			while(resultSet.next()){
				paObj = new InteracaoPipeiroPADTO();
				
				paObj.getPontoAbastecimentoDTO().setId(resultSet.getInt("COD_PA"));
				paObj.getPontoAbastecimentoDTO().setComunidade(resultSet.getString("COMUNIDADE"));
				paObj.getPontoAbastecimentoDTO().setApontador(resultSet.getString("APONTADOR"));
				paObj.getPontoAbastecimentoDTO().setPopulacao(resultSet.getInt("Populacao"));
				paObj.getPontoAbastecimentoDTO().setMomento(String.valueOf(resultSet.getDouble("INDICE_PA")));
				paObj.setTotalQtdAgua(resultSet.getDouble("TOTAL_QTD_AGUA"));
				paObj.setTotalViagemFormula(resultSet.getDouble("VIAGEM_FORMULA"));
				paObj.setTotalviagemReal(resultSet.getInt("VIAGEM_REAL"));
				paObj.setValorBruto(resultSet.getDouble("VALOR_BRUTO"));
				paObj.setKmPecorrido(resultSet.getInt("KM_PERCORRIDO"));
				paObj.setObs(resultSet.getString("KM_PERCORRIDO"));
				paObj.setIndice(resultSet.getDouble("INDICE_PA"));
				
				paList.add(paObj);
			
			}
			
			return paList;

		}finally {
			//Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			//Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}
		
	}


	@Override
	public DistribuicaoDiasPipeiroDTO recuperarInteracaoDatas(int idPA, int codHistoricoDistr) throws Exception {
		logger.info("Iniciando  recuperarInteracaoDatas(int idPipeiro,int codHistorico)"
				+ "" + " idPA: " + idPA + " codHistorico: " + codHistoricoDistr);	
		try {
			
			DistribuicaoDiasPipeiroDTO distribuicaoDiasPipeiroDTO = new DistribuicaoDiasPipeiroDTO();
			DataDTO dia;
			conecction = ConexaoMySQL.getConnection();

			StringBuffer queryPA = new StringBuffer();

			queryPA.append("SELECT * FROM operacaopipa.interacao_datas where cod_pa = ? and COD_HISTORICO_DISTRIBUICAO = ? ");
			
			logger.info("Query: " + queryPA.toString());
			statement = conecction.prepareStatement(queryPA.toString());
			
			//Atribuindo parametro necessario para a query
			statement.setInt(1, idPA);
			statement.setInt(2, codHistoricoDistr);

			resultSet = statement.executeQuery();
			
			while(resultSet.next()){
				
				dia = new DataDTO();
				
				dia.setData(resultSet.getDate("data"));
				dia.setDatasExtenso(resultSet.getString("data_extenso"));
				dia.setIndice(resultSet.getInt("indice"));
				dia.setDias(resultSet.getInt("dias"));
				dia.setEhFimDeSemana(resultSet.getInt("eh_fim_de_semana") == 1?true:false);
				dia.setExibirColuna(resultSet.getInt("exibir_coluna") == 1?true:false);
				
				if(dia.getIndice() == 1){
					distribuicaoDiasPipeiroDTO.setDia1(dia);
				}else if(dia.getIndice() == 2){
					distribuicaoDiasPipeiroDTO.setDia2(dia);
				}else if(dia.getIndice() == 3){
					distribuicaoDiasPipeiroDTO.setDia3(dia);
				}else if(dia.getIndice() == 4){
					distribuicaoDiasPipeiroDTO.setDia4(dia);
				}else if(dia.getIndice() == 5){
					distribuicaoDiasPipeiroDTO.setDia5(dia);
				}else if(dia.getIndice() == 6){
					distribuicaoDiasPipeiroDTO.setDia6(dia);
				}else if(dia.getIndice() == 7){
					distribuicaoDiasPipeiroDTO.setDia7(dia);
				}else if(dia.getIndice() == 8){
					distribuicaoDiasPipeiroDTO.setDia8(dia);
				}else if(dia.getIndice() == 9){
					distribuicaoDiasPipeiroDTO.setDia9(dia);
				}else if(dia.getIndice() == 10){
					distribuicaoDiasPipeiroDTO.setDia10(dia);
				}else if(dia.getIndice() == 11){
					distribuicaoDiasPipeiroDTO.setDia11(dia);
				}else if(dia.getIndice() == 12){
					distribuicaoDiasPipeiroDTO.setDia12(dia);
				}else if(dia.getIndice() == 13){
					distribuicaoDiasPipeiroDTO.setDia13(dia);
				}else if(dia.getIndice() == 14){
					distribuicaoDiasPipeiroDTO.setDia14(dia);
				}else if(dia.getIndice() == 15){
					distribuicaoDiasPipeiroDTO.setDia15(dia);
				}else if(dia.getIndice() == 16){
					distribuicaoDiasPipeiroDTO.setDia16(dia);
				}else if(dia.getIndice() == 17){
					distribuicaoDiasPipeiroDTO.setDia17(dia);
				}else if(dia.getIndice() == 18){
					distribuicaoDiasPipeiroDTO.setDia18(dia);
				}else if(dia.getIndice() == 19){
					distribuicaoDiasPipeiroDTO.setDia19(dia);
				}else if(dia.getIndice() == 20){
					distribuicaoDiasPipeiroDTO.setDia20(dia);
				}else if(dia.getIndice() == 21){
					distribuicaoDiasPipeiroDTO.setDia21(dia);
				}else if(dia.getIndice() == 22){
					distribuicaoDiasPipeiroDTO.setDia22(dia);
				}else if(dia.getIndice() == 23){
					distribuicaoDiasPipeiroDTO.setDia23(dia);
				}else if(dia.getIndice() == 24){
					distribuicaoDiasPipeiroDTO.setDia24(dia);
				}else if(dia.getIndice() == 25){
					distribuicaoDiasPipeiroDTO.setDia25(dia);
				}else if(dia.getIndice() == 26){
					distribuicaoDiasPipeiroDTO.setDia26(dia);
				}else if(dia.getIndice() == 27){
					distribuicaoDiasPipeiroDTO.setDia27(dia);
				}else if(dia.getIndice() == 28){
					distribuicaoDiasPipeiroDTO.setDia28(dia);
				}else if(dia.getIndice() == 29){
					distribuicaoDiasPipeiroDTO.setDia29(dia);
				}else if(dia.getIndice() == 30){
					distribuicaoDiasPipeiroDTO.setDia30(dia);
				}else if(dia.getIndice() == 31){
					distribuicaoDiasPipeiroDTO.setDia31(dia);
				}
				
			}
			
			return distribuicaoDiasPipeiroDTO;

		}finally {
			//Fechar ResultSet e Statement
			ConexaoMySQL.finalizarSql(resultSet, statement);
			//Fechar conexao
			ConexaoMySQL.fecharConexao(conecction);
		}
	}

}
