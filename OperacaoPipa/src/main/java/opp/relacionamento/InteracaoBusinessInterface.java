package opp.relacionamento;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import opp.dto.CidadeDTO;
import opp.dto.DataDTO;
import opp.dto.PipeiroDTO;
import opp.dto.PontoAbastecimentoDTO;
import opp.dto.RotaPagamentoPipeiroDTO;

public interface InteracaoBusinessInterface {
	
	List<RotaPagamentoPipeiroDTO> calcularRotaPagamento(List<PontoAbastecimentoDTO> pontoAbastecimentos,List<PipeiroDTO> pipeiros, CidadeDTO cidadeDTO, Calendar calendar, List<DataDTO> datasDistribuicao, int totalDias);

	void calcularRotaPagamentoManualmente(List<RotaPagamentoPipeiroDTO> rotaPagamentoPipeiroDTOList,PontoAbastecimentoDTO pontoAbastecimentoSelecionado,PipeiroDTO pipeiroSelecionado, CidadeDTO cidadeDTO, Calendar calendar, List<DataDTO> datasDistribuicao, int totalDias);

	void removerPontoAbastecimentoSelecionado(RotaPagamentoPipeiroDTO rp,PontoAbastecimentoDTO pontoSelecionado);

	void definirDiasCarradas(RotaPagamentoPipeiroDTO rotaSelecionada);

	void recalcularPontoAbastecimento(RotaPagamentoPipeiroDTO rp,PontoAbastecimentoDTO pontoSelecionado, int reducao, String obs);

	void calcularRotaPagamentoRestante(List<RotaPagamentoPipeiroDTO> rotaPagamentoPipeiroDTOList,List<PontoAbastecimentoDTO> pontoAbastecimentos,List<PipeiroDTO> pipeiros, CidadeDTO cidadeDTO, Calendar calendar,List<DataDTO> datasDistribuicao, int totalDias);


}
