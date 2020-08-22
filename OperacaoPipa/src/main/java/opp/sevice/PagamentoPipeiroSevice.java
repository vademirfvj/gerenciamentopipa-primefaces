package opp.sevice;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import opp.dao.CidadeDAO;
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
import opp.impl.CidadeDAOImpl;
import opp.impl.DistribuicaoPipeiroDAOImpl;
import opp.impl.InteracaoBusinessImpl;
import opp.impl.InteracaoDAOImpl;
import opp.relacionamento.InteracaoBusinessInterface;

@ManagedBean(name = "pagamentoPipeiroSevice")
@ApplicationScoped
public class PagamentoPipeiroSevice implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private CidadeDAO cidadeDAO = new  CidadeDAOImpl();
	private InteracaoDAO interacaoDAO = new  InteracaoDAOImpl();
	private DistribuicaoPipeiroDAO distribuicaoPipeiroDAO = new  DistribuicaoPipeiroDAOImpl();
	
	private InteracaoBusinessInterface interacaoBusinessInterface = new InteracaoBusinessImpl();
	
	public List<CidadeDTO> getCidades(){
		//inserir Log
		List<CidadeDTO> listaCidades = new ArrayList<CidadeDTO>();
		try {
			listaCidades = cidadeDAO.recuperaTodasCidadesAtivas();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listaCidades;
	}

	public CidadeDAO getCidadeDAO() {
		return cidadeDAO;
	}

	public void setCidadeDAO(CidadeDAO cidadeDAO) {
		this.cidadeDAO = cidadeDAO;
	}

	public InteracaoDAO getInteracaoDAO() {
		return interacaoDAO;
	}

	public void setInteracaoDAO(InteracaoDAO interacaoDAO) {
		this.interacaoDAO = interacaoDAO;
	}
	
	
	public DistribuicaoPipeiroDAO getDistribuicaoPipeiroDAO() {
		return distribuicaoPipeiroDAO;
	}

	public void setDistribuicaoPipeiroDAO(
			DistribuicaoPipeiroDAO distribuicaoPipeiroDAO) {
		this.distribuicaoPipeiroDAO = distribuicaoPipeiroDAO;
	}

	public List<PontoAbastecimentoDTO> getPontoAbastecimentoPorCidade(
			int cidadeSelecionada) throws Exception {
		
		//inserir Log
		List<PontoAbastecimentoDTO> listaPontoAbastecimento = new ArrayList<PontoAbastecimentoDTO>();
		try {
			listaPontoAbastecimento = interacaoDAO.recuperaPontoAbastecimentoPorCidade(cidadeSelecionada);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e);
		}
		return listaPontoAbastecimento;
		
	}

	public List<PipeiroDTO> getPipeiroPorCidade(int cidadeSelecionada) throws Exception {
		
		//inserir Log
		List<PipeiroDTO> listaPipeiro = new ArrayList<PipeiroDTO>();
		try {
			listaPipeiro = interacaoDAO.recuperaPipeiroPorCidade(cidadeSelecionada);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e);
		}
		return listaPipeiro;
		
	}

	public List<RotaPagamentoPipeiroDTO> calcularRotaPagamento(List<PontoAbastecimentoDTO> pontoAbastecimentos,List<PipeiroDTO> pipeiros, int codCidadeSelecionada, Calendar calendar, List<DataDTO> datasDistribuicao, int totalDias) throws Exception {
		
		//inserir Log
		List<RotaPagamentoPipeiroDTO> listaRotaPagamento = new ArrayList<RotaPagamentoPipeiroDTO>();
		CidadeDTO cidadeDTO = new CidadeDTO();
		
		try {
			
			cidadeDTO = cidadeDAO.recuperaCidadePorId(codCidadeSelecionada);
			
			listaRotaPagamento = interacaoBusinessInterface.calcularRotaPagamento(pontoAbastecimentos,pipeiros,cidadeDTO,calendar,datasDistribuicao,totalDias);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e);
		}
		return listaRotaPagamento;
		
	}

	public void calcularRotaPagamentoManualmente(List<RotaPagamentoPipeiroDTO> rotaPagamentoPipeiroDTOList, PontoAbastecimentoDTO pontoAbastecimentoSelecionado,PipeiroDTO pipeiroSelecionado, int codCidadeSelecionada, Calendar calendar, List<DataDTO> datasDistribuicao, int totalDias) throws Exception {

		
		//inserir Log
		CidadeDTO cidadeDTO = new CidadeDTO();
		
		try {
			
			cidadeDTO = cidadeDAO.recuperaCidadePorId(codCidadeSelecionada);
			
			interacaoBusinessInterface.calcularRotaPagamentoManualmente(rotaPagamentoPipeiroDTOList,pontoAbastecimentoSelecionado,pipeiroSelecionado,cidadeDTO,calendar,datasDistribuicao,totalDias);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e);
		}
	
	}

	public void removerPontoAbastecimentoSelecionado(RotaPagamentoPipeiroDTO rp, PontoAbastecimentoDTO pontoSelecionado) throws Exception {

		try {
				
				interacaoBusinessInterface.removerPontoAbastecimentoSelecionado(rp,pontoSelecionado);
			
		} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new Exception(e);
			}
		}

	public void definirDiasCarradas(RotaPagamentoPipeiroDTO rotaSelecionada) throws Exception {
		// TODO Auto-generated method stub
		
		try {
			
			interacaoBusinessInterface.definirDiasCarradas(rotaSelecionada);
		
		} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new Exception(e);
			}
	}

	public int getDistribuicaoOk(Integer ano, Integer mes, long codCidade) throws Exception {

		int retorno = 0;
		
		try {

			retorno = interacaoDAO.validarDistribuicao(ano,mes,codCidade);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e);
		}

		return retorno;
	}

	public int verificaExistenciaDefDias(Integer ano, Integer mes) throws Exception {	
		
		int retorno = 0;
	
	try {

		retorno = interacaoDAO.verificaExistenciaDefDias(ano,mes);

	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		throw new Exception(e);
	}

	return retorno;
	}

	public List<DataDTO> recuperarDataDTO(Integer ano, Integer mes) throws Exception {	
		
		List<DataDTO> dataDTO = new ArrayList();
	
	try {

		dataDTO = interacaoDAO.recuperarDataDTO(ano,mes);

	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		throw new Exception(e);
	}

	return dataDTO;
	}

	public int inserirHistoricoDistribuicao(Date dataAtual, int codUsuario, String obs) throws Exception {
		int retorno = 0;
		
		try {

			retorno = distribuicaoPipeiroDAO.inserirHistoricoDistribuicao(dataAtual,codUsuario,obs);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e);
		}

		return retorno;
	}

	public void inserirInteracaoUsuario(int idFUncionalidade,String acao, String osb, Date date, int idUsuario) throws Exception {
		
		try {

			interacaoDAO.inserirInteracaoUsuario(idFUncionalidade,acao,osb,date,idUsuario);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e);
		}		
	}
	
	public void inserirInteracaoDistribuicao(int cidade, long pipeiro, int veiculo,
			int pontoColeta, int codHistorico, int numDiasMes, int litrosPessoa,
			double valorTotalPA, int kmTotal, double totalViagensFormula,
			int totalViagensReal, int totalApontadores, int totalPessoas,
			double totalQtdAgua, int totalDistacia, int mes,
			int ano) throws Exception {

		try {

			interacaoDAO.inserirInteracaoDistribuicao(cidade,pipeiro,veiculo,pontoColeta,codHistorico,
					numDiasMes,litrosPessoa,valorTotalPA,kmTotal,totalViagensFormula,totalViagensReal,
					totalApontadores,totalPessoas,totalQtdAgua,totalDistacia,mes,ano);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e);
		}
		
	}

	public void inserirInteracaoPipeiroPa(long pipeiro, long pontoAbastec, double qtdAgua,
			double qtdViagensFormula, int qtdViagensReal, double valorBruto,
			int kmPercorridoPA, String obs, double indice, int codHistorico)throws Exception  {
		
		try {

			interacaoDAO.inserirInteracaoPipeiroPa( pipeiro,  pontoAbastec,  qtdAgua,
					 qtdViagensFormula,  qtdViagensReal,  valorBruto,
					 kmPercorridoPA,  obs,  indice,  codHistorico);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e);
		}
		
	}

	public void inserirInteracaoDatas(Date data, String datasExtenso,
			int indice, int dias, boolean ehFimDeSemana, boolean exibirColuna,
			int codHistorico, int mes, int ano, long pa, long pipeiro) throws Exception {

		try {

			interacaoDAO.inserirInteracaoDatas( data,  datasExtenso,
					 indice,  dias,  ehFimDeSemana,  exibirColuna,
					 codHistorico,  mes,  ano,  pa,  pipeiro);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e);
		}
		
	}

	public int selectCodHistorico(int codCidadeInicial,
			String mesSelecionado, String anoSelecionado) throws Exception {

		int retorno = 0;
		
		try {

			retorno = distribuicaoPipeiroDAO.selectCodHistorico(codCidadeInicial,mesSelecionado,anoSelecionado);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e);
		}

		return retorno;
	}

	public void deleteInteracaoDistribuicao(int codigoHistorico) throws Exception {

		try {

			interacaoDAO.deleteInteracaoDistribuicao(codigoHistorico);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	public void deleteInteracaoPipeiroPa(int codigoHistorico) throws Exception{

		try {

			interacaoDAO.deleteInteracaoPipeiroPa(codigoHistorico);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	public void deleteInteracaoDatas(int codigoHistorico) throws Exception{

		try {

			interacaoDAO.deleteInteracaoDatas(codigoHistorico);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	public List<RotaPagamentoPipeiroDTO> getVisualizarCidade(int cidadeSelecionada, String mes, String ano)throws Exception {
		try {
			
			List arrayVCidade = null;

			arrayVCidade = distribuicaoPipeiroDAO.getVisualizarCidade(cidadeSelecionada,mes,ano);

			return arrayVCidade;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	public int selectCodHistoricoPorPipeiro(int cidadeSelecionada,int pipeiroSelecionadoImpressao, String mesSelecionado,
			String anoSelecionado) throws Exception{

		int retorno = 0;
		
		try {

			retorno = distribuicaoPipeiroDAO.selectCodHistoricoPorPipeiro(cidadeSelecionada,pipeiroSelecionadoImpressao,mesSelecionado,anoSelecionado);

			
			if(retorno == 0){
				throw new Exception("Erro ao recuperar informacoes na interacao_ditribuicao");
			}
			
			return retorno;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e);
		}

	}

	public List<RelatorioDistribuicaoCarradasDTO> montarInfoPipeiro(int pipeiroSelecionadoImpressao,int codigoHistorico) throws Exception {

		try {
			
		List<RelatorioDistribuicaoCarradasDTO> dc = null;
			
		dc = distribuicaoPipeiroDAO.montarInfoPipeiro(pipeiroSelecionadoImpressao,codigoHistorico);

		return dc;
		
		} catch (Exception e) {
			// TODO: handle exception
			throw new Exception(e);
		}
	}

	public void montarInfoCarradas(int pipeiroSelecionadoImpressao,List<RelatorioDistribuicaoCarradasDTO> listaPontosAbastecimento, int codigoHistorico) throws Exception {

		try {
			
			distribuicaoPipeiroDAO.montarInfoCarradas(pipeiroSelecionadoImpressao,listaPontosAbastecimento,codigoHistorico);
			
		} catch (Exception e) {
			// TODO: handle exception
			throw new Exception(e);
		}
	}

	public void recalcularPontoAbastecimento(RotaPagamentoPipeiroDTO rp,PontoAbastecimentoDTO pontoSelecionado, int reducao, String obs)throws Exception  {
		
		//inserir Log
				
		try {
		
			interacaoBusinessInterface.recalcularPontoAbastecimento(rp,pontoSelecionado,reducao,obs);
		
		} catch (Exception e) {
			throw new Exception(e);
		}
		
	}

	public List<AssinaturasDTO> recuperaAssinaturas(int codigoFuncionalidade)throws Exception {
		List arrayAssinaturas = null;

		try {

			arrayAssinaturas = distribuicaoPipeiroDAO.recuperaAssinaturas(codigoFuncionalidade);

		} catch (Exception e) {
			throw new Exception(e);
		}
		return arrayAssinaturas;
	}

	public void atualizarAssinaturaDistrPieiro(AssinaturasDTO assinaturasDTO) throws Exception {
		
	try {
			
		interacaoDAO.atualizarAssinaturaDistrPieiro(assinaturasDTO);
		
		} catch (Exception e) {
			throw new Exception(e);
		}
		
	}

	public Map<String, Object> recuperarParametrosHist(int mes,int ano) throws Exception {
		Map map = null;

		try {

			map = distribuicaoPipeiroDAO.recuperarParametrosHist(mes,ano);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e);
		}
		return map;

	}

	public void atualizarHistorico(int bar,
			Date dataSorteio, Date dataBAR, int mes, int ano, int codigoHistorico) throws Exception {
		
		try {
			
			interacaoDAO.atualizarHistorico( bar,
					 dataSorteio,  dataBAR, mes, ano, codigoHistorico);
			
			} catch (Exception e) {
				throw new Exception(e);
			}
		
	}

	public Map<String, Object> recuperarParametrosInteraDistr(Integer mes,
			Integer ano, int pipeiroSelecionadoImpressao) throws Exception {
		Map map = null;
		try {
		
		map = distribuicaoPipeiroDAO.recuperarParametrosInteraDistr( mes,ano,pipeiroSelecionadoImpressao);
		
		} catch (Exception e) {
			throw new Exception(e);
		}
			
		return map;
	}

	public void atualizarInteracaoDistr(int os, int lacre, int mes,
			int ano, int pipeiroSelecionadoImpressao) throws Exception{

		try {
			
			interacaoDAO.atualizarInteracaoDistr( os,lacre, mes, ano, pipeiroSelecionadoImpressao);
			
			} catch (Exception e) {
				throw new Exception(e);
			}
	}

	public void calcularRotaPagamentoRestante(List<RotaPagamentoPipeiroDTO> rotaPagamentoPipeiroDTOList,
			List<PontoAbastecimentoDTO> pontoAbastecimentos,List<PipeiroDTO> pipeiros, int cidadeSelecionada,
			Calendar calendar, List<DataDTO> datasDistribuicao, int totalDias) throws Exception {
		
		CidadeDTO cidadeDTO = new CidadeDTO();
		
		try {
			
			cidadeDTO = cidadeDAO.recuperaCidadePorId(cidadeSelecionada);
			
			interacaoBusinessInterface.calcularRotaPagamentoRestante(rotaPagamentoPipeiroDTOList,pontoAbastecimentos,
					pipeiros,cidadeDTO,calendar,datasDistribuicao,totalDias);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new Exception(e);
		}
	}

	public List<InteracaoDistribuicaoDTO> recuperarInteracaoDistirbuicao(int cidadeSelecionada, int mes, int ano)  throws Exception{
		List InteracaoDistribuicao = null;

		try {

			InteracaoDistribuicao = distribuicaoPipeiroDAO.recuperarInteracaoDistirbuicao(cidadeSelecionada,mes,ano);

		} catch (Exception e) {
			throw new Exception(e);
		}
		return InteracaoDistribuicao;
	}

	public List<PontoAbastecimentoDTO> recuperarPontoAbastecimentoEdicao(int idPipeiro, int codHistorico) throws Exception{
		List pontoAbastecimentoList = null;

		try {

			pontoAbastecimentoList = distribuicaoPipeiroDAO.recuperarPontoAbastecimentoEdicao(idPipeiro,codHistorico);

		} catch (Exception e) {
			throw new Exception(e);
		}
		return pontoAbastecimentoList;
	}

	public void atribuirDiasDistribuicao(int idPipeiro, int codHistoricoDistr,PontoAbastecimentoDTO pa)throws Exception {
		try {

			distribuicaoPipeiroDAO.atribuirDiasDistribuicao(idPipeiro,codHistoricoDistr,pa);

		} catch (Exception e) {
			throw new Exception(e);
		}
		
	}

	public void atualizarDiasDistribuicao(int idPipeiro, int codHistoricoDistr, PontoAbastecimentoDTO pa)throws Exception {
		try {

			distribuicaoPipeiroDAO.atualizarDiasDistribuicao(idPipeiro,codHistoricoDistr,pa);

		} catch (Exception e) {
			throw new Exception(e);
		}
		
	}

	public List<InteracaoPipeiroPADTO> recuperarInteracaoPipeiroPA(int codHistoricoDistr, int idPipeiro)throws Exception {
		try {

			List InteracaoPipeiroPADTO = null;
			
			InteracaoPipeiroPADTO = distribuicaoPipeiroDAO.recuperarInteracaoPipeiroPA(codHistoricoDistr,idPipeiro);

			return InteracaoPipeiroPADTO;

		} catch (Exception e) {
			throw new Exception(e);
		}

	}

	public DistribuicaoDiasPipeiroDTO recuperarInteracaoDatas(int idPA, int codHistoricoDistr)throws Exception {
		try {

			DistribuicaoDiasPipeiroDTO distribuicaoDiasPipeiroDTO = null;
			
			distribuicaoDiasPipeiroDTO = distribuicaoPipeiroDAO.recuperarInteracaoDatas(idPA,codHistoricoDistr);

			return distribuicaoDiasPipeiroDTO;

		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	
}
