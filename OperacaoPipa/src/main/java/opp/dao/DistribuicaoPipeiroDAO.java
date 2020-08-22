package opp.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import opp.dto.CidadeDTO;
import opp.dto.DataDTO;
import opp.dto.DistribuicaoDiasPipeiroDTO;
import opp.dto.PipeiroDTO;
import opp.dto.PontoAbastecimentoDTO;
import opp.dto.RelatorioDistribuicaoCarradasDTO;



public interface DistribuicaoPipeiroDAO {

	int inserirHistoricoDistribuicao(Date dataAtual, int codUsuario, String obs) throws Exception;

	int selectCodHistorico(int codCidadeInicial, String mesSelecionado,String anoSelecionado) throws Exception;

	int selectCodHistoricoPorPipeiro(int cidadeSelecionada,int pipeiroSelecionadoImpressao, String mesSelecionado,String anoSelecionado)throws Exception;

	List<RelatorioDistribuicaoCarradasDTO> montarInfoPipeiro(int pipeiroSelecionadoImpressao,int codigoHistorico)throws Exception;

	void montarInfoCarradas(int pipeiroSelecionadoImpressao,List<RelatorioDistribuicaoCarradasDTO> listaPontosAbastecimento,int codigoHistorico)throws Exception;

	List getVisualizarCidade(int cidadeSelecionada, String mes, String ano) throws Exception;

	List recuperaAssinaturas(int codigoFuncionalidade) throws SQLException, Exception;

	Map recuperarParametrosHist(int mes, int ano) throws Exception;

	Map recuperarParametrosInteraDistr(Integer mes, Integer ano,
			int pipeiroSelecionadoImpressao) throws Exception;

	List recuperarInteracaoDistirbuicao(int cidadeSelecionada, int mes, int ano) throws Exception;

	List recuperarPontoAbastecimentoEdicao(int idPipeiro, int codHistorico) throws Exception;

	void atribuirDiasDistribuicao(int idPipeiro, int codHistoricoDistr,PontoAbastecimentoDTO pa)throws Exception;

	void atualizarDiasDistribuicao(int idPipeiro, int codHistoricoDistr,PontoAbastecimentoDTO pa)throws Exception;

	List recuperarInteracaoPipeiroPA(int codHistoricoDistr, int idPipeiro)throws Exception;

	DistribuicaoDiasPipeiroDTO recuperarInteracaoDatas(int idPA, int codHistoricoDistr)throws Exception;




}
