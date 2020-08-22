package opp.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import opp.dto.AssinaturasDTO;
import opp.dto.DataDTO;
import opp.dto.PipeiroDTO;
import opp.dto.PontoAbastecimentoDTO;



public interface InteracaoDAO {


	List<PontoAbastecimentoDTO> recuperaPontoAbastecimentoPorCidade(int cidadeSelecionada) throws Exception;

	List<PipeiroDTO> recuperaPipeiroPorCidade(int cidadeSelecionada) throws Exception;
	
	int validarDistribuicao(Integer ano, Integer mes, long codCidade) throws Exception;

	int verificaExistenciaDefDias(Integer ano, Integer mes) throws Exception;

	List<DataDTO> recuperarDataDTO(Integer ano, Integer mes)throws Exception;

	void inserirInteracaoUsuario(int idFUncionalidade,String acao, String osb, Date date, int idUsuario)throws Exception;

	void inserirInteracaoDistribuicao(int cidade, long pipeiro, int veiculo,int pontoColeta, int codHistorico, int numDiasMes,
			int litrosPessoa, double valorTotalPA, int kmTotal,double totalViagensFormula, int totalViagensReal,
			int totalApontadores, int totalPessoas, double totalQtdAgua,int totalDistacia, int mes, int ano)throws Exception;

	void inserirInteracaoPipeiroPa(long pipeiro, long pontoAbastec, double qtdAgua, double qtdViagensFormula, int qtdViagensReal,
			double valorBruto, int kmPercorridoPA, String obs, double indice, int codHistorico)throws Exception;

	void inserirInteracaoDatas(Date data, String datasExtenso, int indice,int dias, boolean ehFimDeSemana, boolean exibirColuna,
			int codHistorico, int mes, int ano, long pa, long pipeiro)throws Exception;

	void deleteInteracaoDistribuicao(int codigoHistorico)throws Exception;

	void deleteInteracaoPipeiroPa(int codigoHistorico)throws Exception;

	void deleteInteracaoDatas(int codigoHistorico)throws Exception;

	void atualizarAssinaturaDistrPieiro(AssinaturasDTO assinaturasDTO)throws Exception;

	void atualizarHistorico(int bar, Date dataSorteio,
			Date dataBAR, int mes, int ano, int codigoHistorico)throws Exception;

	void atualizarInteracaoDistr(int os, int lacre, int mes, int ano,
			int pipeiroSelecionadoImpressao)throws Exception;

}
