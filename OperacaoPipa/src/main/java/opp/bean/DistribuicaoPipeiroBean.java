package opp.bean;
 
import java.io.Serializable;
import java.security.Timestamp;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
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
import opp.dto.UsuarioDTO;
import opp.sevice.CidadeService;
import opp.sevice.PagamentoPipeiroSevice;
import opp.sevice.PipeiroService;
import opp.util.Constantes;
import opp.util.Funcoes;
import opp.util.Propriedades;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.FlowEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;

 
@ViewScoped
@ManagedBean(name = "distribuicaoPipeiroBean", eager = true)
@SessionScoped
public class DistribuicaoPipeiroBean implements Serializable {
 
	private static Propriedades rpcProperties = Propriedades.getInstance();
	private static final Logger logger = Logger.getLogger(CadastrarVeiculoBean.class);
	//Atributos da Primeira Tela
	private String mesSelecionado;
	private String anoSelecionado;
	private String mesAnoSelecionadoExtenso;
    private Date dateInicial;
    private Date dateFinal;
    private List<DataDTO> datas;
    private List<Date> selectedDatas = new ArrayList();
    private int totalDias;
    private List<DataDTO> datasDistribuicao;
    private String msgDefDias;
    
    private int cidadeSelecionada;
    private List<CidadeDTO> cidades;
    private CidadeDTO cidadeInicial;
    private List<PontoAbastecimentoDTO> pontoAbastecimentos;
    private List<PontoAbastecimentoDTO> pontoAbastecimentosClone  = new ArrayList();
    private List<PipeiroDTO> pipeiros;
    private List<PipeiroDTO> pipeirosClone  = new ArrayList();
    private List<PipeiroDTO> pipeirosRestante  = new ArrayList();
    
    private String tipoDistribuicao;
    
    private int totalViagensReal;
	private double totalValor;
	private int totalViagensRealPendente;
	private double totalValorPendente;
	private List<RotaPagamentoPipeiroDTO> rotaPagamentoPipeiroDTOList = new ArrayList();
	private List<RotaPagamentoPipeiroDTO> rotaPagamentoPipeiroDTOListManual = new ArrayList();
	private String pipeiroSelecionado;
	private List<PontoAbastecimentoDTO> pontoAbastecimentosManual = new ArrayList();
	private List<RotaPagamentoPipeiroDTO> rotaPagamentoSelecionada;
	private DistribuicaoDiasPipeiroDTO distribuicaoDias = new DistribuicaoDiasPipeiroDTO();
	private List<PontoAbastecimentoDTO> pontoAbastecimentoPendente  = new ArrayList();
	private int pontoAbastecimentoPendenteSelecionado;
	private double valorTotalRota;
	private double diferencaMaoirMenor;
	private boolean flagRecuperarUltimaDistribuicao;
	
	//Atributos do dialog
	private RotaPagamentoPipeiroDTO rotaSelecionada;
	private PontoAbastecimentoDTO pontoAbastecimentoSelecionado = new PontoAbastecimentoDTO();
	private List<PontoAbastecimentoDTO> palist;
	private List<RotaPagamentoPipeiroDTO> rpCidade = new ArrayList();
	private List<RotaPagamentoPipeiroDTO> rpCidadeSelectMode = new ArrayList();
	private int pipeiroSelecionadoImpressao;
	private int reducaoViagens;
	private String obsReducao;
	private List<AssinaturasDTO> assinaturas = new ArrayList();
	private int numOS;
	private int numBAR;
	private int numLacre;
	private Date dataSorteio;
	private Date dataBAR;
	private List<InteracaoDistribuicaoDTO> interacaoDistribuicao = new ArrayList();
	private InteracaoDistribuicaoDTO interacaoDistribuicaoSelecionado;
	private List<PontoAbastecimentoDTO> pontoAbastecimentosEdicao;
	
	
	//flags de renderizacao e habilitacao de campos
	private int flagExibirTabela;
	private int flagExibirTabelaCidadeInicial;
	private int flagExibirDefinirDias;
	private boolean halibitarCampoAno;
	
    
    @ManagedProperty("#{pagamentoPipeiroSevice}")
    private PagamentoPipeiroSevice service;
    
    @ManagedProperty(value = "#{pipeiroService}")
    private PipeiroService servicePipeiro;
    
    @ManagedProperty(value = "#{cidadeService}")
    private CidadeService serviceCidade;
    
    @PostConstruct
    public void init() {
    	
    	logger.info("Inicio da tela de Distribuição de Pipeiro X Ponto de Abastecimento.");
    	
    	servicePipeiro = new PipeiroService();
    	serviceCidade = new CidadeService();
    	
    	cidades = service.getCidades();
    	
    	try {
			
    	assinaturas = service.recuperaAssinaturas(Constantes.DISTRIBUICAO_PIPEIRO_PA);
    	
    	} catch (Exception e) {
			// TODO: handle exception
    		logger.error(e);
		}
    	
    	flagExibirTabelaCidadeInicial = new Integer(0); 
    	flagExibirDefinirDias = new Integer(0); 
    	halibitarCampoAno = true;
//    	totalPopulacao = new Integer(0);
//    	totalValor = new Double(0);
    	
    	msgDefDias = rpcProperties.buscaPropriedade("mensagem.distDias");
    }
    
    
    public void validaDistribuicaoDiasOK(){
    	
    	PontoAbastecimentoDTO pontoSelecionado;
    	int totalDistribuido = 0;
    	boolean distribuicaoOK = true;
    	
    	for (int i = 0; i < rotaSelecionada.getPontoAbastecimentoDTOList().size(); i++) {
    		pontoSelecionado = (PontoAbastecimentoDTO) rotaSelecionada.getPontoAbastecimentoDTOList().get(i);
    		
    		totalDistribuido += pontoSelecionado.getDistribuicaoDiasPipeiroDTO().getDia1().getDias();
    		totalDistribuido += pontoSelecionado.getDistribuicaoDiasPipeiroDTO().getDia2().getDias();
    		totalDistribuido += pontoSelecionado.getDistribuicaoDiasPipeiroDTO().getDia3().getDias();
    		totalDistribuido += pontoSelecionado.getDistribuicaoDiasPipeiroDTO().getDia4().getDias();
    		totalDistribuido += pontoSelecionado.getDistribuicaoDiasPipeiroDTO().getDia5().getDias();
    		totalDistribuido += pontoSelecionado.getDistribuicaoDiasPipeiroDTO().getDia6().getDias();
    		totalDistribuido += pontoSelecionado.getDistribuicaoDiasPipeiroDTO().getDia7().getDias();
    		totalDistribuido += pontoSelecionado.getDistribuicaoDiasPipeiroDTO().getDia8().getDias();
    		totalDistribuido += pontoSelecionado.getDistribuicaoDiasPipeiroDTO().getDia9().getDias();
    		totalDistribuido += pontoSelecionado.getDistribuicaoDiasPipeiroDTO().getDia10().getDias();
    		totalDistribuido += pontoSelecionado.getDistribuicaoDiasPipeiroDTO().getDia11().getDias();
    		totalDistribuido += pontoSelecionado.getDistribuicaoDiasPipeiroDTO().getDia12().getDias();
    		totalDistribuido += pontoSelecionado.getDistribuicaoDiasPipeiroDTO().getDia13().getDias();
    		totalDistribuido += pontoSelecionado.getDistribuicaoDiasPipeiroDTO().getDia14().getDias();
    		totalDistribuido += pontoSelecionado.getDistribuicaoDiasPipeiroDTO().getDia15().getDias();
    		totalDistribuido += pontoSelecionado.getDistribuicaoDiasPipeiroDTO().getDia16().getDias();
    		totalDistribuido += pontoSelecionado.getDistribuicaoDiasPipeiroDTO().getDia17().getDias();
    		totalDistribuido += pontoSelecionado.getDistribuicaoDiasPipeiroDTO().getDia18().getDias();
    		totalDistribuido += pontoSelecionado.getDistribuicaoDiasPipeiroDTO().getDia19().getDias();
    		totalDistribuido += pontoSelecionado.getDistribuicaoDiasPipeiroDTO().getDia20().getDias();
    		totalDistribuido += pontoSelecionado.getDistribuicaoDiasPipeiroDTO().getDia21().getDias();
    		totalDistribuido += pontoSelecionado.getDistribuicaoDiasPipeiroDTO().getDia22().getDias();
    		totalDistribuido += pontoSelecionado.getDistribuicaoDiasPipeiroDTO().getDia23().getDias();
    		totalDistribuido += pontoSelecionado.getDistribuicaoDiasPipeiroDTO().getDia24().getDias();
    		totalDistribuido += pontoSelecionado.getDistribuicaoDiasPipeiroDTO().getDia25().getDias();
    		totalDistribuido += pontoSelecionado.getDistribuicaoDiasPipeiroDTO().getDia26().getDias();
    		totalDistribuido += pontoSelecionado.getDistribuicaoDiasPipeiroDTO().getDia27().getDias();
    		totalDistribuido += pontoSelecionado.getDistribuicaoDiasPipeiroDTO().getDia28().getDias();
    		totalDistribuido += pontoSelecionado.getDistribuicaoDiasPipeiroDTO().getDia29().getDias();
    		totalDistribuido += pontoSelecionado.getDistribuicaoDiasPipeiroDTO().getDia30().getDias();
    		totalDistribuido += pontoSelecionado.getDistribuicaoDiasPipeiroDTO().getDia31().getDias();
    		
    		if(pontoSelecionado.getQtdViagensReal() != totalDistribuido){
    			distribuicaoOK = false;
    		}
    		
    		totalDistribuido = 0;
		}
    	
    	if(distribuicaoOK){
    		rotaSelecionada.setDistribuicaoDiasOK(1);
    	}
    	
    }
    
    public void distribuirPARestante(){
    	logger.info("Inicio do método distribuirPARestante()");
    	
    	try {
			
    	
    	if(pontoAbastecimentosClone.isEmpty()){
    		FacesContext facesContext = FacesContext.getCurrentInstance();  
    		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Não existe Pontos de abastecimento pendentes"));
    		logger.error("Não existe Pontos de abastecimento pendentes");
    		return;
    	}
    		
    	Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Integer.valueOf(getAnoSelecionado()), Integer.valueOf(getMesSelecionado()), 1);
		
		service.calcularRotaPagamentoRestante(rotaPagamentoPipeiroDTOList,pontoAbastecimentosClone,pipeirosRestante,getCidadeSelecionada(),
				calendar,datasDistribuicao,totalDias);
		
		pipeirosRestante.clear();
		pontoAbastecimentosClone.clear();
		setTotaisValorDiferenca(rotaPagamentoPipeiroDTOList);
		
    	} catch (Exception e) {
			// TODO: handle exception
    		logger.error(e);
		}
    	
		logger.info("Fim do método distribuirPARestante()");
    }
    
    
    public void reduzirViagens(){
    	logger.info("Inicio do método reduzirViagens()");
    	
		
    	try {
			
    	PontoAbastecimentoDTO pontoSelecionado = pontoAbastecimentoSelecionado;
		
		int reducao = getReducaoViagens();
		
		String obs = getObsReducao();
		
		if(obs.equalsIgnoreCase("")){
			
			FacesContext facesContext = FacesContext.getCurrentInstance();  
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "O Campo observação deve ser preenchido."));
		
		}else{
		
			for (int i = 0; i < rotaPagamentoPipeiroDTOList.size(); i++) {
				RotaPagamentoPipeiroDTO rp = rotaPagamentoPipeiroDTOList.get(i);
				
				if(rp == rotaSelecionada){
					
					for (int j = 0; j < rp.getPontoAbastecimentoDTOList().size(); j++) {
						PontoAbastecimentoDTO pa = (PontoAbastecimentoDTO) rp.getPontoAbastecimentoDTOList().get(j);
						
						if(pa == pontoSelecionado){
							service.recalcularPontoAbastecimento(rp,pontoSelecionado,reducao,obs);
						}
					}
				}
			}
			
			RequestContext context = RequestContext.getCurrentInstance();
			context.update(":form:rotaDetail");
			context.execute("PF('reducaoDialog').hide();");
		
	}	
		
    	} catch (Exception e) {
			// TODO: handle exception
    		logger.error(e);
		}
    	
		logger.info("Fim do método reduzirViagens()");
    }
    
    
    public void atribuirReducaoViagens(){
    	
    	setReducaoViagens(pontoAbastecimentoSelecionado.getQtdViagensReal());
    	setObsReducao("");
    }
    
    public void recuperarCidadeTelaInicial(){
    	
    	logger.info("Inicio do método recuperarCidadeTelaInicial()");
    	
    	try {
    	
    	flagExibirTabelaCidadeInicial = new Integer(1);
    	
    	setTotalDias(0);
    	if(!Funcoes.isListaNulaOuVazia(datas))
    	datas.clear();
    	
    	CidadeDTO cidadeObj;
    	
    	//Verifica se já houve distribuicao para cada cidade
    	for (int i = 0; i < cidades.size(); i++) {
    		cidadeObj = cidades.get(i);
    		
    		cidadeObj.setFlagDistribuicaoOk(service.getDistribuicaoOk(Integer.valueOf(getAnoSelecionado()), 
    				Integer.valueOf(getMesSelecionado()),cidadeObj.getId()));
		}
    	
    	//Verifica se os dias já foram definidos
    	flagExibirDefinirDias = service.verificaExistenciaDefDias(Integer.valueOf(getAnoSelecionado()), 
				Integer.valueOf(getMesSelecionado()));
    	
    	if(flagExibirDefinirDias > 1){
    		//setar total de dias
    		setTotalDias(flagExibirDefinirDias);
    		
    		//setar datas da distribuição
    		datas = service.recuperarDataDTO(Integer.valueOf(getAnoSelecionado()), 
    				Integer.valueOf(getMesSelecionado()));
    	}
    	
    	} catch (Exception e) {
			// TODO: handle exception
    		logger.error(e);
		}
    	
    	logger.info("Fim do método recuperarCidadeTelaInicial()");
    	
    }
    
    public void habilitarAno(){
    	
    	setTotalDias(0);
    	
    	if(!Funcoes.isListaNulaOuVazia(datas))
    	datas.clear();
    	
    	halibitarCampoAno = false;
    	
    	if(anoSelecionado != null){
    		recuperarCidadeTelaInicial();
    	}
    	
    }
    
//    public void onRowSelect(SelectEvent event) {
//        FacesMessage msg = new FacesMessage("Car Selected", "");
//        FacesContext.getCurrentInstance().addMessage(null, msg);
//    }
    
//    public void recuperaPAs() {
//		
//		//Recupera Pontos de Coleta
//		pontoAbastecimentos = service.getPontoAbastecimentoPorCidade(getCidadeSelecionada());
//		pontoAbastecimentosClone.addAll(pontoAbastecimentos);
//		
//		//Recupera Pipeios
//		pipeiros = service.getPipeiroPorCidade(getCidadeSelecionada());
//		pipeirosClone.addAll(pipeiros);
//    	
//    }


	public void save() {
		
		logger.info("Inicio do método save()");
		
		if(!Funcoes.isListaNulaOuVazia(pontoAbastecimentosClone) && tipoDistribuicao.equalsIgnoreCase("manual")){
			FacesContext facesContext = FacesContext.getCurrentInstance();  
    		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Não foi possível salvar interação. Existe Pontos pendentes de distribuição."));
		}else{
		
		
		List<RotaPagamentoPipeiroDTO> rota = rotaPagamentoPipeiroDTOList;
		
		try {
			
			//Salvar dados em hitorico_distribuicao
			//OBS.: Falta buscar dados do Usuário
			int codHistorico;
			codHistorico = service.inserirHistoricoDistribuicao(new Date(),1,"Insercao de dados da cidade "+rota.get(0).getCidadeDTO().getCidade());
			
			
			//Salvar dados em interacao_usuario
			UsuarioDTO usuarioNaSessao = (UsuarioDTO) Funcoes.getSession().getAttribute(Constantes.USUARIO_LOGADO);
			
			service.inserirInteracaoUsuario(Constantes.DISTRIBUICAO_PIPEIRO_PA,Constantes.INSERIRDADOS,null,new Date(),usuarioNaSessao.getId());
			
			int mes = Integer.valueOf(getMesSelecionado());
			int ano = Integer.valueOf(getAnoSelecionado());
			List dataDTO = new ArrayList();
			
			RotaPagamentoPipeiroDTO rp;
			
			int totalRota = rotaPagamentoPipeiroDTOList.size();
			
			int porcentagem = 100/totalRota;
			
			for (int i = 0; i < rotaPagamentoPipeiroDTOList.size(); i++) {
				rp = rotaPagamentoPipeiroDTOList.get(i);
				
				
				//Salvar dados em interacao_ditribuicao
				service.inserirInteracaoDistribuicao(rp.getCidadeDTO().getId(),
				rp.getPipeiroDTO().getId(),rp.getPipeiroDTO().getVeiculoDTO().getId(),
				rp.getCidadeDTO().getCodPontoColeta(),codHistorico,
				rp.getNumDiasMes(),rp.getLitrosPessoa(),rp.getValorTotalPA(),rp.getKmTotal(),
				rp.getTotalViagensFormula(),rp.getTotalViagensReal(),rp.getTotalApontadores(),
				rp.getTotalPessoas(),rp.getTotalQtdAgua(),rp.getTotalDistacia(),
				mes,ano);
				
				
				//Salvar dados em interacao_pipeiro_pa
				for (int j = 0; j < rp.getPontoAbastecimentoDTOList().size(); j++) {
					
					PontoAbastecimentoDTO pa = (PontoAbastecimentoDTO) rp
							.getPontoAbastecimentoDTOList().get(j);
					
					service.inserirInteracaoPipeiroPa(rp.getPipeiroDTO().getId(),pa.getId(),
							pa.getQtdAgua(),pa.getQtdViagensFormula(),pa.getQtdViagensReal(),
							pa.getValorBruto(),pa.getKmPercorridoPA(),pa.getObs(),pa.getIndice(),
							codHistorico);
					
					
					//Salvar dados em interacao_datas
					dataDTO.add(pa.getDistribuicaoDiasPipeiroDTO().getDia1());
					dataDTO.add(pa.getDistribuicaoDiasPipeiroDTO().getDia2());
					dataDTO.add(pa.getDistribuicaoDiasPipeiroDTO().getDia3());
					dataDTO.add(pa.getDistribuicaoDiasPipeiroDTO().getDia4());
					dataDTO.add(pa.getDistribuicaoDiasPipeiroDTO().getDia5());
					dataDTO.add(pa.getDistribuicaoDiasPipeiroDTO().getDia6());
					dataDTO.add(pa.getDistribuicaoDiasPipeiroDTO().getDia7());
					dataDTO.add(pa.getDistribuicaoDiasPipeiroDTO().getDia8());
					dataDTO.add(pa.getDistribuicaoDiasPipeiroDTO().getDia9());
					dataDTO.add(pa.getDistribuicaoDiasPipeiroDTO().getDia10());
					dataDTO.add(pa.getDistribuicaoDiasPipeiroDTO().getDia11());
					dataDTO.add(pa.getDistribuicaoDiasPipeiroDTO().getDia12());
					dataDTO.add(pa.getDistribuicaoDiasPipeiroDTO().getDia13());
					dataDTO.add(pa.getDistribuicaoDiasPipeiroDTO().getDia14());
					dataDTO.add(pa.getDistribuicaoDiasPipeiroDTO().getDia15());
					dataDTO.add(pa.getDistribuicaoDiasPipeiroDTO().getDia16());
					dataDTO.add(pa.getDistribuicaoDiasPipeiroDTO().getDia17());
					dataDTO.add(pa.getDistribuicaoDiasPipeiroDTO().getDia18());
					dataDTO.add(pa.getDistribuicaoDiasPipeiroDTO().getDia19());
					dataDTO.add(pa.getDistribuicaoDiasPipeiroDTO().getDia20());
					dataDTO.add(pa.getDistribuicaoDiasPipeiroDTO().getDia21());
					dataDTO.add(pa.getDistribuicaoDiasPipeiroDTO().getDia22());
					dataDTO.add(pa.getDistribuicaoDiasPipeiroDTO().getDia23());
					dataDTO.add(pa.getDistribuicaoDiasPipeiroDTO().getDia24());
					dataDTO.add(pa.getDistribuicaoDiasPipeiroDTO().getDia25());
					dataDTO.add(pa.getDistribuicaoDiasPipeiroDTO().getDia26());
					dataDTO.add(pa.getDistribuicaoDiasPipeiroDTO().getDia27());
					dataDTO.add(pa.getDistribuicaoDiasPipeiroDTO().getDia28());
					dataDTO.add(pa.getDistribuicaoDiasPipeiroDTO().getDia29());
					dataDTO.add(pa.getDistribuicaoDiasPipeiroDTO().getDia30());
					dataDTO.add(pa.getDistribuicaoDiasPipeiroDTO().getDia31());
					
					for (int k = 0; k < dataDTO.size(); k++) {
						DataDTO data = (DataDTO) dataDTO.get(k);
						
						service.inserirInteracaoDatas(data.getData(),data.getDatasExtenso(),
								data.getIndice(),data.getDias(),data.isEhFimDeSemana(),
								data.isExibirColuna(),codHistorico,mes,ano,pa.getId(),rp.getPipeiroDTO().getId());
						
						
					}
					
					dataDTO.clear();
				}
				
			}
			
			RequestContext context = RequestContext.getCurrentInstance();
			//reinicar wizard
	//		context.execute("PF('wizardDialog').hide();");
			//fechar wizard
			context.execute("PF('alertConfirm').show();");
			context.execute("PF('wiz').loadStep('tipoDistribuicao',false);");

		} catch (Exception e) {
			
			//Criar metodo de rollback
			
			FacesContext facesContext = FacesContext.getCurrentInstance();  
    		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Erro ao salvar distribuição. ["
    				+e.toString()+ " ]"));
    		logger.error("Erro ao salvar distribuição. ["
    				+e.toString()+ " ]");
		}

		}
		
		logger.info("Fim do método save()");
    }
	
	public void imprimirPlanilhaPagamento() throws Exception{
		
		logger.info("Inicio do método imprimirPlanilhaPagamento()");
		try {
			
		
		List<RelatorioDistribuicaoCarradasDTO> listaPontosAbastecimento = new ArrayList<RelatorioDistribuicaoCarradasDTO>();
		Map<String, Object> params = new HashMap<String, Object>();
		
		int totalPessoas = 0;
		int totalViagens = 0;
		int totalDistacia = 0;
		int totalAgua = 0;
		double totalViagensForm = 0;
		double totalValorBruto = 0;
		int totalApontador = 0;
		Date dataAtual = new Date(System.currentTimeMillis());
		String litrosPessoa = rpcProperties.buscaPropriedade("litrosPessoa");
		NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance(); 
		
		//Buscar código histórico distribuição
		int codigoHistorico = service.selectCodHistoricoPorPipeiro(getCidadeSelecionada(),getPipeiroSelecionadoImpressao(),getMesSelecionado(),getAnoSelecionado());
					
		PipeiroDTO pipeiro = servicePipeiro.recuperaPipeiroPorId(getPipeiroSelecionadoImpressao());

		params.put("nomePipeiro", pipeiro.getNome());
		params.put("cpfPipeiro", pipeiro.getCpf());
		params.put("placa", pipeiro.getVeiculoDTO().getPlaca());
		params.put("CAPPIPA", ""+pipeiro.getVeiculoDTO().getCapacidadePipa());
		params.put("dataAtual", Funcoes.dataExtenso(dataAtual));
		
		AssinaturasDTO ass;
		for (int i = 0; i < assinaturas.size(); i++) {
			ass = assinaturas.get(i);
			
			if(ass.getAbrev().equalsIgnoreCase("COP")){
				params.put("COP", ass.getNome());
			}
		}
		
		
		//Montar info Pipeiro
		listaPontosAbastecimento = service.montarInfoPipeiro(getPipeiroSelecionadoImpressao(),codigoHistorico);
		
		RelatorioDistribuicaoCarradasDTO rdc;
		for (int i = 0; i < listaPontosAbastecimento.size(); i++) {
			rdc = listaPontosAbastecimento.get(i);
			
			totalPessoas += rdc.getPopulacao();
			totalViagens += rdc.getQtdViagensReal();
			totalDistacia += rdc.getQtdViagensReal()*rdc.getDistancia();
			totalAgua += rdc.getQtdAgua();
			totalViagensForm += rdc.getQtdViagensForm();
			totalValorBruto += rdc.getValorBruto();
			totalApontador += 1;
			
			rdc.setCapacidadePipa(pipeiro.getVeiculoDTO().getCapacidadePipa());
			rdc.setValorBrutoExtenso("R"+formatoMoeda.format(rdc.getValorBruto()));
		}
		
		params.put("TTPESSOAS", totalPessoas);
		params.put("TTVIAGENS", totalViagens);
		params.put("TTDISTANCIA", totalDistacia);
		params.put("TTQTDAGUA", totalAgua);
		int arredondado = (int) Math.floor(totalViagensForm);
		params.put("TTVIAGENSFORM", arredondado);

		
		params.put("TTVALORBRUTO", "R"+formatoMoeda.format(totalValorBruto));
		params.put("TTAPONTADOR", totalApontador);
		
		params.put("TTDIAS", totalDias);
		params.put("LITROSPESSOA", litrosPessoa);
		params.put("dataDistribuicao", Funcoes.obterAnoMesExtenso(Integer.valueOf(getMesSelecionado()), Integer.valueOf(getAnoSelecionado())).toUpperCase());
		
		CidadeDTO cidade = serviceCidade.recuperaCidadePorId(getCidadeSelecionada());
		
		params.put("municipio", cidade.getCidade()+"-PB");
		params.put("PontoColeta", cidade.getPontoColetaDTO().getPontoColeta()+" ("+cidade.getPontoColetaDTO().getCodGcda()+")");
		
		
		// compilacao do JRXML
		String caminhoRelatorio = rpcProperties.buscaPropriedade("caminho.relatorios");
		String caminhoImpressao = rpcProperties.buscaPropriedade("caminho.impressao");
		
		JasperReport report = JasperCompileManager.compileReport(caminhoRelatorio+Constantes.PLANILHAPAGAMENTO);
		JasperPrint print = JasperFillManager.fillReport(report, params,new JRBeanCollectionDataSource(listaPontosAbastecimento));

		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat formatadorData = new SimpleDateFormat("yyyyMMddHHmmss");
		String timeStamp =	formatadorData.format(calendar.getTime()); 
				
		String arquivo = "PlanilhaPagamento_"+timeStamp+".pdf";
		// exportacao do relatorio para outro formato, no caso PDF
		JasperExportManager.exportReportToPdfFile(print,caminhoImpressao+arquivo);
		
		} catch (Exception e) {
			System.out.println(e.toString());
			FacesContext facesContext = FacesContext.getCurrentInstance();  
    		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Erro ao imprimir Planilha Pagamento ["+e.toString()+"]"));
    		logger.error("Erro ao imprimir Planilha Pagamento ["+e.toString()+"]");
		}
		
		logger.info("Fim do método imprimirPlanilhaPagamento()");
	}
	public void imprimirRelatorioCarradas() throws JRException,Exception{
		logger.info("Inicio do método imprimirRelatorioCarradas()");
		
		try {
			
		
		List<RelatorioDistribuicaoCarradasDTO> listaPontosAbastecimento = new ArrayList<RelatorioDistribuicaoCarradasDTO>();
		Map<String, Object> params = new HashMap<String, Object>();
//		
		
		int totalPessoas = 0;
		int totalViagens = 0;
		int totalDistacia = 0;
		int tt1 = 0;
		int tt2 = 0;
		int tt3 = 0;
		int tt4 = 0;
		int tt5 = 0;
		int tt6 = 0;
		int tt7 = 0;
		int tt8 = 0;
		int tt9 = 0;
		int tt10 = 0;
		int tt11 = 0;
		int tt12 = 0;
		int tt13 = 0;
		int tt14 = 0;
		int tt15 = 0;
		int tt16 = 0;
		int tt17 = 0;
		int tt18 = 0;
		int tt19 = 0;
		int tt20 = 0;
		int tt21 = 0;
		int tt22 = 0;
		int tt23 = 0;
		int tt24 = 0;
		int tt25 = 0;
		int tt26 = 0;
		int tt27 = 0;
		int tt28 = 0;
		int tt29 = 0;
		int tt30 = 0;
		int tt31 = 0;
		
		
		//Informações do Título
		int os = getNumOS();
		int lacre = getNumLacre();
		int bar = getNumBAR();
		Date dataSorteio = getDataSorteio();
		Date dataBAR = getDataBAR();
		
		if((os == 0 || Funcoes.isNuloOuVazio(os)) ||
		(lacre == 0 || Funcoes.isNuloOuVazio(lacre)) ||
		(bar == 0 || Funcoes.isNuloOuVazio(bar)) || 
		Funcoes.isNuloOuVazio(dataSorteio) ||
		Funcoes.isNuloOuVazio(dataBAR) ){
			
			FacesContext facesContext = FacesContext.getCurrentInstance();  
    		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Todos os parâmetros devem ser preenchidos"));
			return;
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String dataSorteioFormat = sdf.format(dataSorteio);
		
		params.put("numOS", os);
		params.put("dataDistribuicao", getAnoSelecionado()+" - "+Funcoes.obterAnoMesExtenso(Integer.valueOf(getMesSelecionado()), Integer.valueOf(getAnoSelecionado())).toUpperCase());
		params.put("dataSorteio", dataSorteioFormat);
		params.put("numBAR", bar);
		params.put("dataBAR", Funcoes.dataExtenso(dataBAR));
		params.put("numLacre", lacre);
		
		
		//Buscar código histórico distribuição
		int codigoHistorico = service.selectCodHistoricoPorPipeiro(getCidadeSelecionada(),getPipeiroSelecionadoImpressao(),getMesSelecionado(),getAnoSelecionado());
			
		//Montar info Pipeiro
		listaPontosAbastecimento = service.montarInfoPipeiro(getPipeiroSelecionadoImpressao(),codigoHistorico);
		
		//Montar info Carradas
		service.montarInfoCarradas(getPipeiroSelecionadoImpressao(),listaPontosAbastecimento,codigoHistorico);
		
		PipeiroDTO pipeiro = servicePipeiro.recuperaPipeiroPorId(getPipeiroSelecionadoImpressao());

		params.put("nomePipeiro", pipeiro.getNome());
		params.put("cpfPipeiro", pipeiro.getCpf());
		params.put("placa", pipeiro.getVeiculoDTO().getPlaca());
		params.put("cubagem", ""+pipeiro.getVeiculoDTO().getCapacidadePipa()+"00");

		CidadeDTO cidade = serviceCidade.recuperaCidadePorId(getCidadeSelecionada());
		
		params.put("municipio", cidade.getCidade()+"-PB");
		params.put("PontoColeta", cidade.getPontoColetaDTO().getPontoColeta()+" ("+cidade.getPontoColetaDTO().getCodGcda()+")");
		
		AssinaturasDTO ass;
		for (int i = 0; i < assinaturas.size(); i++) {
			ass = assinaturas.get(i);
			
			if(ass.getAbrev().equalsIgnoreCase("COP")){
				params.put("COP", ass.getNome());
			}
			
			if(ass.getAbrev().equalsIgnoreCase("OD")){
				params.put("OD", ass.getNome());
			}
		}
		
		
		RelatorioDistribuicaoCarradasDTO rdc;
		for (int i = 0; i < listaPontosAbastecimento.size(); i++) {
			rdc = listaPontosAbastecimento.get(i);
			
			totalPessoas += rdc.getPopulacao();
			totalViagens += rdc.getQtdViagensReal();
			totalDistacia += rdc.getDistancia();
			
			 tt1 += Integer.valueOf("0"+rdc.getDia1());
			 tt2 += Integer.valueOf("0"+rdc.getDia2());
			 tt3 += Integer.valueOf("0"+rdc.getDia3());
			 tt4 += Integer.valueOf("0"+rdc.getDia4());
			 tt5 += Integer.valueOf("0"+rdc.getDia5());
			 tt6 += Integer.valueOf("0"+rdc.getDia6());
			 tt7 += Integer.valueOf("0"+rdc.getDia7());
			 tt8 += Integer.valueOf("0"+rdc.getDia8());
			 tt9 += Integer.valueOf("0"+rdc.getDia9());
			 tt10 += Integer.valueOf("0"+rdc.getDia10());
			 tt11 += Integer.valueOf("0"+rdc.getDia11());
			 tt12 += Integer.valueOf("0"+rdc.getDia12());
			 tt13 += Integer.valueOf("0"+rdc.getDia13());
			 tt14 += Integer.valueOf("0"+rdc.getDia14());
			 tt15 += Integer.valueOf("0"+rdc.getDia15());
			 tt16 += Integer.valueOf("0"+rdc.getDia16());
			 tt17 += Integer.valueOf("0"+rdc.getDia17());
			 tt18 += Integer.valueOf("0"+rdc.getDia18());
			 tt19 += Integer.valueOf("0"+rdc.getDia19());
			 tt20 += Integer.valueOf("0"+rdc.getDia20());
			 tt21 += Integer.valueOf("0"+rdc.getDia21());
			 tt22 += Integer.valueOf("0"+rdc.getDia22());
			 tt23 += Integer.valueOf("0"+rdc.getDia23());
			 tt24 += Integer.valueOf("0"+rdc.getDia24());
			 tt25 += Integer.valueOf("0"+rdc.getDia25());
			 tt26 += Integer.valueOf("0"+rdc.getDia26());
			 tt27 += Integer.valueOf("0"+rdc.getDia27());
			 tt28 += Integer.valueOf("0"+rdc.getDia28());
			 tt29 += Integer.valueOf("0"+rdc.getDia29());
			 tt30 += Integer.valueOf("0"+rdc.getDia30());
			 tt31 += Integer.valueOf("0"+rdc.getDia31());
			 
				 params.put("DIAEXT1",rdc.getDiaExt1());
				 params.put("DIAEXT2",rdc.getDiaExt2());
				 params.put("DIAEXT3",rdc.getDiaExt3());
				 params.put("DIAEXT4",rdc.getDiaExt4());
				 params.put("DIAEXT5",rdc.getDiaExt5());
				 params.put("DIAEXT6",rdc.getDiaExt6());
				 params.put("DIAEXT7",rdc.getDiaExt7());
				 params.put("DIAEXT8",rdc.getDiaExt8());
				 params.put("DIAEXT9",rdc.getDiaExt9());
				 params.put("DIAEXT10",rdc.getDiaExt10());
				 params.put("DIAEXT11",rdc.getDiaExt11());
				 params.put("DIAEXT12",rdc.getDiaExt12());
				 params.put("DIAEXT13",rdc.getDiaExt13());
				 params.put("DIAEXT14",rdc.getDiaExt14());
				 params.put("DIAEXT15",rdc.getDiaExt15());
				 params.put("DIAEXT16",rdc.getDiaExt16());
				 params.put("DIAEXT17",rdc.getDiaExt17());
				 params.put("DIAEXT18",rdc.getDiaExt18());
				 params.put("DIAEXT19",rdc.getDiaExt19());
				 params.put("DIAEXT20",rdc.getDiaExt20());
				 params.put("DIAEXT21",rdc.getDiaExt21());
				 params.put("DIAEXT22",rdc.getDiaExt22());
				 params.put("DIAEXT23",rdc.getDiaExt23());
				 params.put("DIAEXT24",rdc.getDiaExt24());
				 params.put("DIAEXT25",rdc.getDiaExt25());
				 params.put("DIAEXT26",rdc.getDiaExt26());
				 params.put("DIAEXT27",rdc.getDiaExt27());
				 params.put("DIAEXT28",rdc.getDiaExt28());
				 params.put("DIAEXT29",rdc.getDiaExt29());
				 params.put("DIAEXT30",rdc.getDiaExt30());
				 params.put("DIAEXT31",rdc.getDiaExt31());
			 
				 params.put("IMPRIME16",rdc.getDiaExibir16());
				 params.put("IMPRIME17",rdc.getDiaExibir17());
				 params.put("IMPRIME18",rdc.getDiaExibir18());
				 params.put("IMPRIME19",rdc.getDiaExibir19());
				 params.put("IMPRIME20",rdc.getDiaExibir20());
				 params.put("IMPRIME21",rdc.getDiaExibir21());
				 params.put("IMPRIME22",rdc.getDiaExibir22());
				 params.put("IMPRIME23",rdc.getDiaExibir23());
				 params.put("IMPRIME24",rdc.getDiaExibir24());
				 params.put("IMPRIME25",rdc.getDiaExibir25());
				 params.put("IMPRIME26",rdc.getDiaExibir26());
				 params.put("IMPRIME27",rdc.getDiaExibir27());
				 params.put("IMPRIME28",rdc.getDiaExibir28());
				 params.put("IMPRIME29",rdc.getDiaExibir29());
				 params.put("IMPRIME30",rdc.getDiaExibir30());
				 params.put("IMPRIME31",rdc.getDiaExibir31());
				 
					 params.put("EFIMDESEMANA1",rdc.getEhFimdeSemana1());
					 params.put("EFIMDESEMANA2",rdc.getEhFimdeSemana2());
					 params.put("EFIMDESEMANA3",rdc.getEhFimdeSemana3());
					 params.put("EFIMDESEMANA4",rdc.getEhFimdeSemana4());
					 params.put("EFIMDESEMANA5",rdc.getEhFimdeSemana5());
					 params.put("EFIMDESEMANA6",rdc.getEhFimdeSemana6());
					 params.put("EFIMDESEMANA7",rdc.getEhFimdeSemana7());
					 params.put("EFIMDESEMANA8",rdc.getEhFimdeSemana8());
					 params.put("EFIMDESEMANA9",rdc.getEhFimdeSemana9());
					 params.put("EFIMDESEMANA10",rdc.getEhFimdeSemana10());
					 params.put("EFIMDESEMANA11",rdc.getEhFimdeSemana11());
					 params.put("EFIMDESEMANA12",rdc.getEhFimdeSemana12());
					 params.put("EFIMDESEMANA13",rdc.getEhFimdeSemana13());
					 params.put("EFIMDESEMANA14",rdc.getEhFimdeSemana14());
					 params.put("EFIMDESEMANA15",rdc.getEhFimdeSemana15());
					 params.put("EFIMDESEMANA16",rdc.getEhFimdeSemana16());
					 params.put("EFIMDESEMANA17",rdc.getEhFimdeSemana17());
					 params.put("EFIMDESEMANA18",rdc.getEhFimdeSemana18());
					 params.put("EFIMDESEMANA19",rdc.getEhFimdeSemana19());
					 params.put("EFIMDESEMANA20",rdc.getEhFimdeSemana20());
					 params.put("EFIMDESEMANA21",rdc.getEhFimdeSemana21());
					 params.put("EFIMDESEMANA22",rdc.getEhFimdeSemana22());
					 params.put("EFIMDESEMANA23",rdc.getEhFimdeSemana23());
					 params.put("EFIMDESEMANA24",rdc.getEhFimdeSemana24());
					 params.put("EFIMDESEMANA25",rdc.getEhFimdeSemana25());
					 params.put("EFIMDESEMANA26",rdc.getEhFimdeSemana26());
					 params.put("EFIMDESEMANA27",rdc.getEhFimdeSemana27());
					 params.put("EFIMDESEMANA28",rdc.getEhFimdeSemana28());
					 params.put("EFIMDESEMANA29",rdc.getEhFimdeSemana29());
					 params.put("EFIMDESEMANA30",rdc.getEhFimdeSemana30());
					 params.put("EFIMDESEMANA31",rdc.getEhFimdeSemana31());
			 
		}
		
		params.put("TTPESSOAS",String.valueOf(totalPessoas));
		params.put("TTVIAGENS",String.valueOf(totalViagens));
		params.put("TTDISTANCIA",String.valueOf(totalDistacia));
		params.put("TT1",String.valueOf(tt1));
		params.put("TT2",String.valueOf(tt2));
		params.put("TT3",String.valueOf(tt3));
		params.put("TT4",String.valueOf(tt4));
		params.put("TT5",String.valueOf(tt5));
		params.put("TT6",String.valueOf(tt6));
		params.put("TT7",String.valueOf(tt7));
		params.put("TT8",String.valueOf(tt8));
		params.put("TT9",String.valueOf(tt9));
		params.put("TT10",String.valueOf(tt10));
		params.put("TT11",String.valueOf(tt11));
		params.put("TT12",String.valueOf(tt12));
		params.put("TT13",String.valueOf(tt13));
		params.put("TT14",String.valueOf(tt14));
		params.put("TT15",String.valueOf(tt15));
		params.put("TT16",String.valueOf(tt16));
		params.put("TT17",String.valueOf(tt17));
		params.put("TT18",String.valueOf(tt18));
		params.put("TT19",String.valueOf(tt19));
		params.put("TT20",String.valueOf(tt20));
		params.put("TT21",String.valueOf(tt21));
		params.put("TT22",String.valueOf(tt22));
		params.put("TT23",String.valueOf(tt23));
		params.put("TT24",String.valueOf(tt24));
		params.put("TT25",String.valueOf(tt25));
		params.put("TT26",String.valueOf(tt26));
		params.put("TT27",String.valueOf(tt27));
		params.put("TT28",String.valueOf(tt28));
		params.put("TT29",String.valueOf(tt29));
		params.put("TT30",String.valueOf(tt30));
		params.put("TT31",String.valueOf(tt31));
		
		// compilacao do JRXML
		String caminhoRelatorio = rpcProperties.buscaPropriedade("caminho.relatorios");
		String caminhoImpressao = rpcProperties.buscaPropriedade("caminho.impressao");
		
		JasperReport report = JasperCompileManager.compileReport(caminhoRelatorio+Constantes.PLANILHADISTRIBUICAO);

		JasperPrint print = JasperFillManager.fillReport(report, params,
				new JRBeanCollectionDataSource(listaPontosAbastecimento));

		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat formatadorData = new SimpleDateFormat("yyyyMMddHHmmss");
		String timeStamp =	formatadorData.format(calendar.getTime()); 
		
		String arquivo = "RelatorioCarradas_"+timeStamp+".pdf";
		// exportacao do relatorio para outro formato, no caso PDF
		JasperExportManager.exportReportToPdfFile(print,caminhoImpressao+arquivo);
		
		//exibe o resultado
//		JasperViewer viewer = new JasperViewer( print , true );
//		viewer.show();

		service.atualizarHistorico(bar,dataSorteio,dataBAR,Integer.valueOf(getMesSelecionado()),Integer.valueOf(getAnoSelecionado()),codigoHistorico);
		
		service.atualizarInteracaoDistr(os,lacre,Integer.valueOf(getMesSelecionado()),Integer.valueOf(getAnoSelecionado()),getPipeiroSelecionadoImpressao());
		
		setNumOS(0);
		setNumLacre(0);
		setNumBAR(0);
		setDataSorteio(null);
		setDataBAR(null);
		
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('parametrosDialog').hide();");

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
			FacesContext facesContext = FacesContext.getCurrentInstance();  
    		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Erro ao imprimir Relatório ["+e.toString()+"]"));
    		logger.error("Erro ao imprimir Relatório ["+e.toString()+"]");
		}
		
		logger.info("Fim do método imprimirRelatorioCarradas()");
	}
	
	public void fecharAlertConfirm(){
		
		recuperarCidadeTelaInicial();
		
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('alertConfirm').hide();");
	}
     
	public void iniciarWizadDistribuicao(){
		logger.info("Inicio do método iniciarWizadDistribuicao()");
		
		RequestContext context = RequestContext.getCurrentInstance();

		try {
			
			//Definir Cidade
			Long longCidade = Long.valueOf(getCidadeInicial().getId());
			
			Integer inteiroCidade = Integer.valueOf((int) longCidade.longValue());    
			
			setCidadeSelecionada(inteiroCidade);
			
			//Recupera Pontos de Abastecimento
	    	pontoAbastecimentos = service.getPontoAbastecimentoPorCidade(getCidadeSelecionada());
	    	pontoAbastecimentosClone.addAll(pontoAbastecimentos);
	    			
	    	//Recupera Pipeios
	    	pipeiros = service.getPipeiroPorCidade(getCidadeSelecionada());
	    	pipeirosClone.addAll(pipeiros);
	    	pipeirosRestante.addAll(pipeiros);
	    	
	    	if(Funcoes.isListaNulaOuVazia(pontoAbastecimentos)){
	    		
	    		FacesContext facesContext = FacesContext.getCurrentInstance();  
	    		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Nenhum Ponto de Abastecimento Ativo "
	    				+ "foi encontrado para esta cidade "));
	    		context.execute("PF('wizardDialog').hide();");
	    	}else if(Funcoes.isListaNulaOuVazia(pipeiros)){
	    		FacesContext facesContext = FacesContext.getCurrentInstance();  
	    		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Nenhum Pipeiro Ativo "
	    				+ "foi encontrado para esta cidade "));
	    		context.execute("PF('wizardDialog').hide();");
	    	}else if(Funcoes.isNuloOuVazio(dateInicial)){
	    		FacesContext facesContext = FacesContext.getCurrentInstance();  
	    		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "O campo 'Início' deve ser preenchido"));
	    		context.execute("PF('wizardDialog').hide();");
	    	}else if(Funcoes.isNuloOuVazio(dateFinal)){
	    		FacesContext facesContext = FacesContext.getCurrentInstance();  
	    		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "O campo 'Fim' deve ser preenchido"));
	    		context.execute("PF('wizardDialog').hide();");
	    	}else{	
	    	
			
			//Definir Datas
			Calendar periodo1; 
			boolean ehFimDeSemana;
			datasDistribuicao = new ArrayList<DataDTO>(); 

			if(Funcoes.isListaNulaOuVazia(datas)){
				FacesContext facesContext = FacesContext.getCurrentInstance();  
	    		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Erro ao recuperar definições de dias."));
	    		context.execute("PF('wizardDialog').hide();");
			}
			
			//Se não tiver erro o Modal exibe
			if(!Funcoes.isListaNulaOuVazia(datas) && !Funcoes.isListaNulaOuVazia(pontoAbastecimentos) 
					&& !Funcoes.isListaNulaOuVazia(pipeiros)){
				context.execute("PF('wizardDialog').show();");
			}
			
			for (int i = 0; i < datas.size(); i++) {
				
				DataDTO dataDTO = datas.get(i);
				
				Date dataSelecionada = dataDTO.getData();
				ehFimDeSemana = false;

				periodo1 = Calendar.getInstance(); 
				periodo1.setTime(dataSelecionada);
				
				String dataExtenso = periodo1.get(periodo1.DATE)+ "/" + (periodo1.get(periodo1.MONTH) + 1);
				
//				if(periodo1.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY ||
//						periodo1.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
//					ehFimDeSemana = true;  
//		    	}
				
				
				if(!selectedDatas.contains(dataDTO.getData())){
					ehFimDeSemana = true;
				}
				
				datasDistribuicao.add(new DataDTO(dataSelecionada,dataExtenso,i+1,ehFimDeSemana,true));
				
				
			}
			
			
			distribuirDiasCarrada(distribuicaoDias,datasDistribuicao);
			
	    }
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			FacesContext facesContext = FacesContext.getCurrentInstance();  
    		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Erro genérico"));
			//colocar LOG
    		logger.error("Erro genérico: "+e.toString());
		}
		
		logger.info("Fim do método iniciarWizadDistribuicao()");
	}

	public void visualizarCidadeDistribuicao(){
		logger.info("Inicio do método visualizarCidadeDistribuicao()");
		try {
			
			//Recuperar parametros lacre/os
			recuperarParametros();
			
			mesAnoSelecionadoExtenso = Funcoes.obterAnoMesExtenso(Integer.valueOf(getMesSelecionado()), Integer.valueOf(getAnoSelecionado())).toUpperCase();
			
			Long longCidade = Long.valueOf(getCidadeInicial().getId());
			
			Integer inteiroCidade = Integer.valueOf((int) longCidade.longValue());    
			
			setCidadeSelecionada(inteiroCidade);
			
			rpCidade = service.getVisualizarCidade(getCidadeSelecionada(),getMesSelecionado(),getAnoSelecionado());
			
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('cidadeDialog').show();");
			context.update(":form:cidadeDetail");
			
			
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e);
		}
		
		logger.info("Fim do método visualizarCidadeDistribuicao()");
	}
	
	
	public void editarDistribuicao(){
		logger.info("Inicio do método editarDistribuicao()");
		
		try {
			
			
			Long longCidade = Long.valueOf(getCidadeInicial().getId());
			
			Integer inteiroCidade = Integer.valueOf((int) longCidade.longValue());    
			
			setCidadeSelecionada(inteiroCidade);
			
			interacaoDistribuicao = service.recuperarInteracaoDistirbuicao(getCidadeSelecionada(),Integer.valueOf(getMesSelecionado()), Integer.valueOf(getAnoSelecionado()));
			
			
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('editarDialog').show();");
			context.update(":form:editarDetail");
			
			
		} catch (Exception e) {
			FacesContext facesContext = FacesContext.getCurrentInstance();  
    		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Erro ao abrir tela de edição"));
			logger.error(e);
		}
		
		logger.info("Fim do método editarDistribuicao()");
	}
	
	
	public void editarDistribuicaoPA(){
		logger.info("Inicio do método editarDistribuicaoPA()");
		
		pontoAbastecimentosEdicao = new ArrayList();
		
		try {
		
		pontoAbastecimentosEdicao = service.recuperarPontoAbastecimentoEdicao(interacaoDistribuicaoSelecionado.getPipeiroDTO().getId(),interacaoDistribuicaoSelecionado.getCodHistoricoDistr());
		
		PontoAbastecimentoDTO pa;
		for (int i = 0; i < pontoAbastecimentosEdicao.size(); i++) {
			pa = pontoAbastecimentosEdicao.get(i);
			
			service.atribuirDiasDistribuicao(interacaoDistribuicaoSelecionado.getPipeiroDTO().getId(),interacaoDistribuicaoSelecionado.getCodHistoricoDistr(),pa);
		}
		


		distribuicaoDias.setDia1(pontoAbastecimentosEdicao.get(0).getDistribuicaoDiasPipeiroDTO().dia1);
		
		distribuicaoDias.setDia2(pontoAbastecimentosEdicao.get(0).getDistribuicaoDiasPipeiroDTO().dia2);

    	distribuicaoDias.setDia3(pontoAbastecimentosEdicao.get(0).getDistribuicaoDiasPipeiroDTO().dia3);
            
    	distribuicaoDias.setDia4(pontoAbastecimentosEdicao.get(0).getDistribuicaoDiasPipeiroDTO().dia4);
       
		distribuicaoDias.setDia5(pontoAbastecimentosEdicao.get(0).getDistribuicaoDiasPipeiroDTO().dia5);
        
    	distribuicaoDias.setDia6(pontoAbastecimentosEdicao.get(0).getDistribuicaoDiasPipeiroDTO().dia6);
        		 
    	distribuicaoDias.setDia7(pontoAbastecimentosEdicao.get(0).getDistribuicaoDiasPipeiroDTO().dia7);

    	distribuicaoDias.setDia8(pontoAbastecimentosEdicao.get(0).getDistribuicaoDiasPipeiroDTO().dia8);
        
    	distribuicaoDias.setDia9(pontoAbastecimentosEdicao.get(0).getDistribuicaoDiasPipeiroDTO().dia9);
        
    	distribuicaoDias.setDia10(pontoAbastecimentosEdicao.get(0).getDistribuicaoDiasPipeiroDTO().dia10);
        
    	distribuicaoDias.setDia11(pontoAbastecimentosEdicao.get(0).getDistribuicaoDiasPipeiroDTO().dia11);
        
    	distribuicaoDias.setDia12(pontoAbastecimentosEdicao.get(0).getDistribuicaoDiasPipeiroDTO().dia12);

    	distribuicaoDias.setDia13(pontoAbastecimentosEdicao.get(0).getDistribuicaoDiasPipeiroDTO().dia13);
        
    	distribuicaoDias.setDia14(pontoAbastecimentosEdicao.get(0).getDistribuicaoDiasPipeiroDTO().dia14);
        
    	distribuicaoDias.setDia15(pontoAbastecimentosEdicao.get(0).getDistribuicaoDiasPipeiroDTO().dia15);
        
    	distribuicaoDias.setDia16(pontoAbastecimentosEdicao.get(0).getDistribuicaoDiasPipeiroDTO().dia16);
        
    	distribuicaoDias.setDia17(pontoAbastecimentosEdicao.get(0).getDistribuicaoDiasPipeiroDTO().dia17);
        
    	distribuicaoDias.setDia18(pontoAbastecimentosEdicao.get(0).getDistribuicaoDiasPipeiroDTO().dia18);
        
    	distribuicaoDias.setDia19(pontoAbastecimentosEdicao.get(0).getDistribuicaoDiasPipeiroDTO().dia19);

    	distribuicaoDias.setDia20(pontoAbastecimentosEdicao.get(0).getDistribuicaoDiasPipeiroDTO().dia20);
        
    	distribuicaoDias.setDia21(pontoAbastecimentosEdicao.get(0).getDistribuicaoDiasPipeiroDTO().dia21);
        
    	distribuicaoDias.setDia22(pontoAbastecimentosEdicao.get(0).getDistribuicaoDiasPipeiroDTO().dia22);

    	distribuicaoDias.setDia23(pontoAbastecimentosEdicao.get(0).getDistribuicaoDiasPipeiroDTO().dia23);

    	distribuicaoDias.setDia24(pontoAbastecimentosEdicao.get(0).getDistribuicaoDiasPipeiroDTO().dia24);
     
    	distribuicaoDias.setDia25(pontoAbastecimentosEdicao.get(0).getDistribuicaoDiasPipeiroDTO().dia25);
        
    	distribuicaoDias.setDia26(pontoAbastecimentosEdicao.get(0).getDistribuicaoDiasPipeiroDTO().dia26);

    	distribuicaoDias.setDia27(pontoAbastecimentosEdicao.get(0).getDistribuicaoDiasPipeiroDTO().dia27);
        
    	distribuicaoDias.setDia28(pontoAbastecimentosEdicao.get(0).getDistribuicaoDiasPipeiroDTO().dia28);

    	distribuicaoDias.setDia29(pontoAbastecimentosEdicao.get(0).getDistribuicaoDiasPipeiroDTO().dia29);
        
    	distribuicaoDias.setDia30(pontoAbastecimentosEdicao.get(0).getDistribuicaoDiasPipeiroDTO().dia30);

    	distribuicaoDias.setDia31(pontoAbastecimentosEdicao.get(0).getDistribuicaoDiasPipeiroDTO().dia31);
		
    	rotaSelecionada = new RotaPagamentoPipeiroDTO();
    	
    	 for (int i = 0; i < pontoAbastecimentosEdicao.size(); i++) {
         	pa = (PontoAbastecimentoDTO) pontoAbastecimentosEdicao.get(i);
         		
         			rotaSelecionada.setTotalDia1(rotaSelecionada.getTotalDia1()+pa.getDistribuicaoDiasPipeiroDTO().getDia1().getDias());
 					rotaSelecionada.setTotalDia2(rotaSelecionada.getTotalDia2()+pa.getDistribuicaoDiasPipeiroDTO().getDia2().getDias());
 					rotaSelecionada.setTotalDia3(rotaSelecionada.getTotalDia3()+pa.getDistribuicaoDiasPipeiroDTO().getDia3().getDias());
 					rotaSelecionada.setTotalDia4(rotaSelecionada.getTotalDia4()+pa.getDistribuicaoDiasPipeiroDTO().getDia4().getDias());
 					rotaSelecionada.setTotalDia5(rotaSelecionada.getTotalDia5()+pa.getDistribuicaoDiasPipeiroDTO().getDia5().getDias());
 					rotaSelecionada.setTotalDia6(rotaSelecionada.getTotalDia6()+pa.getDistribuicaoDiasPipeiroDTO().getDia6().getDias());
 					rotaSelecionada.setTotalDia7(rotaSelecionada.getTotalDia7()+pa.getDistribuicaoDiasPipeiroDTO().getDia7().getDias());
 					rotaSelecionada.setTotalDia8(rotaSelecionada.getTotalDia8()+pa.getDistribuicaoDiasPipeiroDTO().getDia8().getDias());
 					rotaSelecionada.setTotalDia9(rotaSelecionada.getTotalDia9()+pa.getDistribuicaoDiasPipeiroDTO().getDia9().getDias());
 					rotaSelecionada.setTotalDia10(rotaSelecionada.getTotalDia10()+pa.getDistribuicaoDiasPipeiroDTO().getDia10().getDias());
 					rotaSelecionada.setTotalDia11(rotaSelecionada.getTotalDia11()+pa.getDistribuicaoDiasPipeiroDTO().getDia11().getDias());
 					rotaSelecionada.setTotalDia12(rotaSelecionada.getTotalDia12()+pa.getDistribuicaoDiasPipeiroDTO().getDia12().getDias());
 					rotaSelecionada.setTotalDia13(rotaSelecionada.getTotalDia13()+pa.getDistribuicaoDiasPipeiroDTO().getDia13().getDias());
 					rotaSelecionada.setTotalDia14(rotaSelecionada.getTotalDia14()+pa.getDistribuicaoDiasPipeiroDTO().getDia14().getDias());
 					rotaSelecionada.setTotalDia15(rotaSelecionada.getTotalDia15()+pa.getDistribuicaoDiasPipeiroDTO().getDia15().getDias());
 					rotaSelecionada.setTotalDia16(rotaSelecionada.getTotalDia16()+pa.getDistribuicaoDiasPipeiroDTO().getDia16().getDias());
 					rotaSelecionada.setTotalDia17(rotaSelecionada.getTotalDia17()+pa.getDistribuicaoDiasPipeiroDTO().getDia17().getDias());
 					rotaSelecionada.setTotalDia18(rotaSelecionada.getTotalDia18()+pa.getDistribuicaoDiasPipeiroDTO().getDia18().getDias());
 					rotaSelecionada.setTotalDia19(rotaSelecionada.getTotalDia19()+pa.getDistribuicaoDiasPipeiroDTO().getDia19().getDias());
 					rotaSelecionada.setTotalDia20(rotaSelecionada.getTotalDia20()+pa.getDistribuicaoDiasPipeiroDTO().getDia20().getDias());
 					rotaSelecionada.setTotalDia21(rotaSelecionada.getTotalDia21()+pa.getDistribuicaoDiasPipeiroDTO().getDia21().getDias());
 					rotaSelecionada.setTotalDia22(rotaSelecionada.getTotalDia22()+pa.getDistribuicaoDiasPipeiroDTO().getDia22().getDias());
 					rotaSelecionada.setTotalDia23(rotaSelecionada.getTotalDia23()+pa.getDistribuicaoDiasPipeiroDTO().getDia23().getDias());
 					rotaSelecionada.setTotalDia24(rotaSelecionada.getTotalDia24()+pa.getDistribuicaoDiasPipeiroDTO().getDia24().getDias());
 					rotaSelecionada.setTotalDia25(rotaSelecionada.getTotalDia25()+pa.getDistribuicaoDiasPipeiroDTO().getDia25().getDias());
 					rotaSelecionada.setTotalDia26(rotaSelecionada.getTotalDia26()+pa.getDistribuicaoDiasPipeiroDTO().getDia26().getDias());
 					rotaSelecionada.setTotalDia27(rotaSelecionada.getTotalDia27()+pa.getDistribuicaoDiasPipeiroDTO().getDia27().getDias());
 					rotaSelecionada.setTotalDia28(rotaSelecionada.getTotalDia28()+pa.getDistribuicaoDiasPipeiroDTO().getDia28().getDias());
 					rotaSelecionada.setTotalDia29(rotaSelecionada.getTotalDia29()+pa.getDistribuicaoDiasPipeiroDTO().getDia29().getDias());
 					rotaSelecionada.setTotalDia30(rotaSelecionada.getTotalDia30()+pa.getDistribuicaoDiasPipeiroDTO().getDia30().getDias());
 					rotaSelecionada.setTotalDia31(rotaSelecionada.getTotalDia31()+pa.getDistribuicaoDiasPipeiroDTO().getDia31().getDias());
         		
 		}
    	 
    	 
    	rotaSelecionada.setPontoAbastecimentoDTOList(pontoAbastecimentosEdicao);
    	
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('diasEditDialog').show();");
		context.update(":form:diasEditDetail");
		
		} catch (Exception e) {
			FacesContext facesContext = FacesContext.getCurrentInstance();  
    		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Erro abrir tela de edição"));
			logger.error(e);
		}
		logger.info("Fim do método editarDistribuicaoPA()");
	}
	
	public void salvardistribuicaoAutoDias(){
		
		try {
			
			logger.info("Fim do método salvardistribuicaoAutoDias()");
		
			PontoAbastecimentoDTO pa;
			for (int i = 0; i < pontoAbastecimentosEdicao.size(); i++) {
				pa = pontoAbastecimentosEdicao.get(i);
				
				service.atualizarDiasDistribuicao(interacaoDistribuicaoSelecionado.getPipeiroDTO().getId(),interacaoDistribuicaoSelecionado.getCodHistoricoDistr(),pa);
			}
		
		} catch (Exception e) {
			FacesContext facesContext = FacesContext.getCurrentInstance();  
    		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Erro ao salvar dias editados"));
			logger.error(e);
		}
		
		logger.info("Fim do método salvardistribuicaoAutoDias()");
		
	}
	
	
	public void recuperarParametros() throws Exception {
		logger.info("Inicio do método recuperarParametros()");
		
		Map<String, Object> params = new HashMap<String, Object>();
		
		try {
			
		params = service.recuperarParametrosHist(Integer.valueOf(getMesSelecionado()),Integer.valueOf(getAnoSelecionado()));
		
		if(params != null){
			
			setNumBAR((int) params.get("NUM_BAR"));
			setDataSorteio((Date) params.get("DATA_SORTEIO"));
			setDataBAR((Date) params.get("DATA_BAR"));
		}else{
			setNumBAR(0);
			setDataSorteio(null);
			setDataBAR(null);
		}
		
		Map<String, Object> params2 = new HashMap<String, Object>();
		
		params2 = service.recuperarParametrosInteraDistr(Integer.valueOf(getMesSelecionado()),Integer.valueOf(getAnoSelecionado()),getPipeiroSelecionadoImpressao());
		
		if(params2 != null){
			
			setNumLacre((int) params2.get("NUM_LACRE"));
			setNumOS((int) params2.get("NUM_OS"));
		}else{
			setNumOS(0);
			setNumLacre(0);
		}
		
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e);
		}
		
		logger.info("Fim do método recuperarParametros()");
	}


	public void excluirCidadeDistribuicao(){
		
		logger.info("Inicio do método excluirCidadeDistribuicao()");
		
		try {
			
		
		Long longCidade = Long.valueOf(getCidadeInicial().getId());
		
		Integer inteiroCidade = Integer.valueOf((int) longCidade.longValue());    
		
		setCidadeSelecionada(inteiroCidade);
		
		//Buscar código histórico distribuição
		int codigoHistorico = service.selectCodHistorico(getCidadeSelecionada(),getMesSelecionado(),getAnoSelecionado());
		
		//
		service.deleteInteracaoDistribuicao(codigoHistorico);
		
		service.deleteInteracaoPipeiroPa(codigoHistorico);
		
		service.deleteInteracaoDatas(codigoHistorico);
		
		//Salvar dados em hitorico_distribuicao
		//OBS.: Falta buscar dados do Usuário
		int codHistorico = service.inserirHistoricoDistribuicao(new Date(),1,"Exclusão de dados da cidade de código "+inteiroCidade);
		
		
		//Salvar dados em interacao_usuario
		UsuarioDTO usuarioNaSessao = (UsuarioDTO) Funcoes.getSession().getAttribute(Constantes.USUARIO_LOGADO);
		
		service.inserirInteracaoUsuario(Constantes.DISTRIBUICAO_PIPEIRO_PA,Constantes.EXCLUIRDADOS,"Exclusão de dados da cidade de código "+inteiroCidade,new Date(),usuarioNaSessao.getId());
		
		recuperarCidadeTelaInicial();
		
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e);
		}

		logger.info("Fim do método excluirCidadeDistribuicao()");
		
	}
	
	public String onFlowProcess(FlowEvent event) throws Exception {
		logger.info("Inicio do método onFlowProcess()");
		
		
		if(!Funcoes.isNuloOuVazio(tipoDistribuicao) && tipoDistribuicao.equalsIgnoreCase("manual")){
			
			
	    	pontoAbastecimentosClone.addAll(service.getPontoAbastecimentoPorCidade(getCidadeSelecionada()));
			
			setFlagExibirTabela(1);
			
			setTotalViagensReal(0);
			setTotalValor(0);
			
			RequestContext context = RequestContext.getCurrentInstance();
			context.update(":form:diferencaMaoirMenor");
			context.update(":form:valorTotalRota");
			
			rotaPagamentoPipeiroDTOList.clear();
			
			if(flagRecuperarUltimaDistribuicao){
				recuperarUltimaDistribuicao();
				setTotaisValorDiferenca(rotaPagamentoPipeiroDTOList);
				pontoAbastecimentosClone.clear();
			}
			
			
		}else if(!Funcoes.isNuloOuVazio(tipoDistribuicao) && tipoDistribuicao.equalsIgnoreCase("auto")){
			
			setFlagExibirTabela(0);
			
			if(flagRecuperarUltimaDistribuicao){
				
				recuperarUltimaDistribuicao();
			
			}else{
			
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new Date());
				calendar.set(Integer.valueOf(getAnoSelecionado()), Integer.valueOf(getMesSelecionado()), 1);
				
				try {
					
				rotaPagamentoPipeiroDTOList = service.calcularRotaPagamento(pontoAbastecimentos,pipeiros,getCidadeSelecionada(),
						calendar,datasDistribuicao,totalDias);
				} catch (Exception e) {
					// TODO: handle exception
					logger.error(e);
				}
			}

			setTotaisValorDiferenca(rotaPagamentoPipeiroDTOList);
			
		}
		
		logger.info("Fim do método onFlowProcess()");
		return event.getNewStep();
            
    }
	
	private void setTotaisValorDiferenca(List<RotaPagamentoPipeiroDTO> rotaPagamentoPipeiroDTOList2) {
		// TODO Auto-generated method stub
		
		RotaPagamentoPipeiroDTO rp;
		double maiorValor = 0;
		double menorValor = 0;
		double valorTotal = 0;
		
		
		for (int i = 0; i < rotaPagamentoPipeiroDTOList2.size(); i++) {
			rp = rotaPagamentoPipeiroDTOList2.get(i);
			
			if(i == 0){
				maiorValor = rp.getValorTotalPA();
			}else if(rp.getValorTotalPA() > maiorValor){
				maiorValor = rp.getValorTotalPA();
			}
			
			if(i == 0){
				menorValor = rp.getValorTotalPA();
			}else if(rp.getValorTotalPA() < menorValor){
				menorValor = rp.getValorTotalPA();
			}
			
			valorTotal += rp.getValorTotalPA();
		}
		
		
		setValorTotalRota(Funcoes.formatarDouble(valorTotal));
		setDiferencaMaoirMenor(Funcoes.formatarDouble(maiorValor-menorValor));
		
	}

	public void redistribuir(){
		logger.info("Inicio do método redistribuir()");
		setFlagExibirTabela(0);
		
		pontoAbastecimentoPendente.clear();
		
		setTotalValorPendente(0);
		setTotalViagensRealPendente(0);
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Integer.valueOf(getAnoSelecionado()), Integer.valueOf(getMesSelecionado()), 1);
		
		try{
		rotaPagamentoPipeiroDTOList = service.calcularRotaPagamento(pontoAbastecimentos,pipeiros,getCidadeSelecionada(),calendar,
				datasDistribuicao,totalDias);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e);
		}
		
		setTotaisValorDiferenca(rotaPagamentoPipeiroDTOList);
		
		logger.info("Fim do método redistribuir()");
		
	}
	
	public void realocar(){
		
		logger.info("Inicio do método realocar()");
		
		PontoAbastecimentoDTO pa;
		RotaPagamentoPipeiroDTO rp;
		PipeiroDTO pipeiroSelecionado = null;
		String nomePipeiro = getPipeiroSelecionado();
		
		List<RotaPagamentoPipeiroDTO> rpTotais = new ArrayList();
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Integer.valueOf(getAnoSelecionado()), Integer.valueOf(getMesSelecionado()), 1);

		if(nomePipeiro.equalsIgnoreCase("Nao Informado")){
			
			FacesContext context = FacesContext.getCurrentInstance();  
	        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Nenhum Pipeiro foi selecionado"));
			
		}else{
			
			if(!Funcoes.isListaNulaOuVazia(pontoAbastecimentoPendente) && pontoAbastecimentoPendenteSelecionado != 0){
				
				for (int i = 0; i < pipeiros.size(); i++) {
					PipeiroDTO pipeiro = pipeiros.get(i);
					
					if(pipeiro.getNome().equalsIgnoreCase(nomePipeiro)){
						pipeiroSelecionado = pipeiro;
						break;
					}
					
				}
	
				
				for (int i = 0; i < pontoAbastecimentoPendente.size(); i++) {
					pa = pontoAbastecimentoPendente.get(i);
					
					
					if(pa.getId() == pontoAbastecimentoPendenteSelecionado){
						
						
						try{
						service.calcularRotaPagamentoManualmente(rotaPagamentoPipeiroDTOList,pa,pipeiroSelecionado,
								getCidadeSelecionada(),calendar,datasDistribuicao,totalDias);
						} catch (Exception e) {
							// TODO: handle exception
							logger.error(e);
						}
						setTotaisValorDiferenca(rotaPagamentoPipeiroDTOList);
						
						pontoAbastecimentoPendente.remove(pa);
						
						break;
					}
				}
				
				totalValorPendente = 0;
				totalViagensRealPendente = 0;
				
				
			}else{
				
				 FacesContext context = FacesContext.getCurrentInstance();  
		         context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Nenhum Ponto de abastecimento foi selecionado"));
			}
		
		}
		
		logger.info("Fim do método realocar()");
		
	}
	
	public void recuperarUltimaDistribuicao(){
		logger.info("Fim do método recuperarUltimaDistribuicao()");
		try {
			rotaPagamentoPipeiroDTOList.clear();

			int mes = Integer.valueOf(getMesSelecionado());
			int ano = Integer.valueOf(getAnoSelecionado());
			
			if(mes == 0){
				mes = 11;
				ano = ano-1;
			}else{
				mes = mes-1;
			}
			
			List<InteracaoDistribuicaoDTO> interacaoDistribuicao;
			
			interacaoDistribuicao = service.recuperarInteracaoDistirbuicao(getCidadeSelecionada(),mes, ano);
			
			
			if(interacaoDistribuicao.isEmpty()){
				FacesContext facesContext = FacesContext.getCurrentInstance();  
	    		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Não existe distribuição no mês anterior"));
	    		return;
			}
			
			int codHistoricoDistr = interacaoDistribuicao.get(0).getCodHistoricoDistr();
			
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.set(Integer.valueOf(getAnoSelecionado()), Integer.valueOf(getMesSelecionado()), 1);
			
			String mesAno = Funcoes.obterAnoMesExtenso(calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));
			
			InteracaoDistribuicaoDTO intDist;
			RotaPagamentoPipeiroDTO rp;
			for (int i = 0; i < interacaoDistribuicao.size(); i++) {
				intDist = interacaoDistribuicao.get(i);
				
				rp = new RotaPagamentoPipeiroDTO();
				
				rp.getCidadeDTO().setId(intDist.getCidadeDTO().getId());
				rp.getCidadeDTO().setCidade(intDist.getCidadeDTO().getCidade());
				rp.getCidadeDTO().getPontoColetaDTO().setId(intDist.getPontoColetaDTO().getId());
				rp.getCidadeDTO().setCodPontoColeta(intDist.getPontoColetaDTO().getId());
				rp.getPipeiroDTO().setId(intDist.getPipeiroDTO().getId());
				rp.getPipeiroDTO().setNome(intDist.getPipeiroDTO().getNome());
				rp.getPipeiroDTO().setCpf(intDist.getPipeiroDTO().getCpf());
				rp.getPipeiroDTO().getVeiculoDTO().setId(intDist.getVeiculoDTO().getId());
				rp.getPipeiroDTO().getVeiculoDTO().setPlaca(intDist.getVeiculoDTO().getPlaca());
				rp.getPipeiroDTO().getVeiculoDTO().setCapacidadePipa(intDist.getVeiculoDTO().getCapacidadePipa());
				rp.setNumDiasMes(totalDias);
				rp.setLitrosPessoa(Integer.valueOf(rpcProperties.buscaPropriedade("litrosPessoa")));
//				rp.setLitrosPessoa(intDist.getQtdLitrosPessoa());
				rp.setValorTotalPA(intDist.getValorTotal());
				rp.setKmTotal(intDist.getKmTotal());
				rp.setTotalViagensFormula(intDist.getTotalViagensFormula());
				rp.setTotalViagensReal(intDist.getTotalViagensReal());
				rp.setTotalApontadores(intDist.getTotalApontadores());
				rp.setTotalPessoas(intDist.getTotalPessoas());
				rp.setTotalQtdAgua(intDist.getTotalQdtAgua());
				rp.setTotalDistacia(intDist.getTotalDistancia());
				rp.setMesAno(mesAno);
				
				rotaPagamentoPipeiroDTOList.add(rp);
				
			}
			
			List<InteracaoPipeiroPADTO> interacaoPipeiroPA;
			InteracaoPipeiroPADTO interacaoPipeiroPAOBJ;
			
			List paList;
			PontoAbastecimentoDTO pa;
			
			RotaPagamentoPipeiroDTO rp2;
			for (int i = 0; i < rotaPagamentoPipeiroDTOList.size(); i++) {
				rp2 = rotaPagamentoPipeiroDTOList.get(i);
				
				interacaoPipeiroPA = service.recuperarInteracaoPipeiroPA(codHistoricoDistr,rp2.getPipeiroDTO().getId());
				
				paList = new ArrayList();
				
				for (int j = 0; j < interacaoPipeiroPA.size(); j++) {
					interacaoPipeiroPAOBJ = interacaoPipeiroPA.get(j);
					
					pa = new PontoAbastecimentoDTO();
					
					pa.setId(interacaoPipeiroPAOBJ.getPontoAbastecimentoDTO().getId());
					pa.setComunidade(interacaoPipeiroPAOBJ.getPontoAbastecimentoDTO().getComunidade());
					pa.setApontador(interacaoPipeiroPAOBJ.getPontoAbastecimentoDTO().getApontador());
					pa.setPopulacao(interacaoPipeiroPAOBJ.getPontoAbastecimentoDTO().getPopulacao());
					pa.setMomento(interacaoPipeiroPAOBJ.getPontoAbastecimentoDTO().getMomento());
					pa.setCapacidadePipa(rp2.getPipeiroDTO().getVeiculoDTO().getCapacidadePipa());
					pa.setQtdAgua(interacaoPipeiroPAOBJ.getTotalQtdAgua());
					pa.setQtdViagensFormula(interacaoPipeiroPAOBJ.getTotalViagemFormula());
					pa.setQtdViagensReal(interacaoPipeiroPAOBJ.getTotalviagemReal());
					pa.setValorBruto(interacaoPipeiroPAOBJ.getValorBruto());
					pa.setKmPercorridoPA(interacaoPipeiroPAOBJ.getKmPecorrido());
					pa.setObs(interacaoPipeiroPAOBJ.getObs());
					pa.setIndice(interacaoPipeiroPAOBJ.getIndice());
					
					distribuirDiasCarrada(pa,datasDistribuicao);
					
					paList.add(pa);
				}
				
				rp2.setPontoAbastecimentoDTOList(paList);
			}
			
//			RotaPagamentoPipeiroDTO rp3;
//			PontoAbastecimentoDTO pa2;
//			DistribuicaoDiasPipeiroDTO dias;
//			for (int i = 0; i < rotaPagamentoPipeiroDTOList.size(); i++) {
//				rp3 = rotaPagamentoPipeiroDTOList.get(i);
//				
//				for (int j = 0; j < rp3.getPontoAbastecimentoDTOList().size(); j++) {
//					pa2 = (PontoAbastecimentoDTO) rp3.getPontoAbastecimentoDTOList().get(j);
//					
//					dias = service.recuperarInteracaoDatas(pa2.getId(),codHistoricoDistr);
//					
//					pa2.setDistribuicaoDiasPipeiroDTO(dias);
//				}
//			}
		
			
		} catch (Exception e) {
			FacesContext facesContext = FacesContext.getCurrentInstance();  
    		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Erro ao recuperar última distribuição"));
		}
		logger.info("Fim do método recuperarUltimaDistribuicao()");
	}
	
	private void distribuirDiasCarrada(PontoAbastecimentoDTO pontoAbastecimentoDTO, List<DataDTO> datasDistribuicao) {

		int contadorDiasUteis = 0;
		int contadorQuantCarradas = 0;
		DataDTO dataObj;
		
		for (int i = 0; i < datasDistribuicao.size(); i++) {

			dataObj = datasDistribuicao.get(i);
			
			DataDTO data = new DataDTO(dataObj.getData(), dataObj.getDatasExtenso(), dataObj.getIndice(), dataObj.isEhFimDeSemana(), dataObj.isExibirColuna());
			
				contadorDiasUteis++;
			
			switch( i )
			{
			    case 0:
			    	pontoAbastecimentoDTO.getDistribuicaoDiasPipeiroDTO().setDia1(data);
			            break;
			    
			    case 1:
			    	pontoAbastecimentoDTO.getDistribuicaoDiasPipeiroDTO().setDia2(data);
			            break;
			    
			    case 2:
			    	pontoAbastecimentoDTO.getDistribuicaoDiasPipeiroDTO().setDia3(data);
			            break;
			    case 3:
			    	pontoAbastecimentoDTO.getDistribuicaoDiasPipeiroDTO().setDia4(data);
		            break;
			    case 4:
			    	pontoAbastecimentoDTO.getDistribuicaoDiasPipeiroDTO().setDia5(data);
		            break;
			    case 5:
			    	pontoAbastecimentoDTO.getDistribuicaoDiasPipeiroDTO().setDia6(data);
		            break;
			    case 6:
			    	pontoAbastecimentoDTO.getDistribuicaoDiasPipeiroDTO().setDia7(data);
		            break;
			    case 7:
			    	pontoAbastecimentoDTO.getDistribuicaoDiasPipeiroDTO().setDia8(data);
		            break;
			    case 8:
			    	pontoAbastecimentoDTO.getDistribuicaoDiasPipeiroDTO().setDia9(data);
		            break;
			    case 9:
			    	pontoAbastecimentoDTO.getDistribuicaoDiasPipeiroDTO().setDia10(data);
		            break;
			    case 10:
			    	pontoAbastecimentoDTO.getDistribuicaoDiasPipeiroDTO().setDia11(data);
		            break;
			    case 11:
			    	pontoAbastecimentoDTO.getDistribuicaoDiasPipeiroDTO().setDia12(data);
		            break;
			    case 12:
			    	pontoAbastecimentoDTO.getDistribuicaoDiasPipeiroDTO().setDia13(data);
		            break;
			    case 13:
			    	pontoAbastecimentoDTO.getDistribuicaoDiasPipeiroDTO().setDia14(data);
		            break;
			    case 14:
			    	pontoAbastecimentoDTO.getDistribuicaoDiasPipeiroDTO().setDia15(data);
		            break;
			    case 15:
			    	pontoAbastecimentoDTO.getDistribuicaoDiasPipeiroDTO().setDia16(data);
		            break;
			    case 16:
			    	pontoAbastecimentoDTO.getDistribuicaoDiasPipeiroDTO().setDia17(data);
		            break;
			    case 17:
			    	pontoAbastecimentoDTO.getDistribuicaoDiasPipeiroDTO().setDia18(data);
		            break;
			    case 18:
			    	pontoAbastecimentoDTO.getDistribuicaoDiasPipeiroDTO().setDia19(data);
		            break;
			    case 19:
			    	pontoAbastecimentoDTO.getDistribuicaoDiasPipeiroDTO().setDia20(data);
		            break;
			    case 20:
			    	pontoAbastecimentoDTO.getDistribuicaoDiasPipeiroDTO().setDia21(data);
		            break;
			    case 21:
			    	pontoAbastecimentoDTO.getDistribuicaoDiasPipeiroDTO().setDia22(data);
		            break;
			    case 22:
			    	pontoAbastecimentoDTO.getDistribuicaoDiasPipeiroDTO().setDia23(data);
		            break;
			    case 23:
			    	pontoAbastecimentoDTO.getDistribuicaoDiasPipeiroDTO().setDia24(data);
		            break;
			    case 24:
			    	pontoAbastecimentoDTO.getDistribuicaoDiasPipeiroDTO().setDia25(data);
		            break;
			    case 25:
			    	pontoAbastecimentoDTO.getDistribuicaoDiasPipeiroDTO().setDia26(data);
		            break;
			    case 26:
			    	pontoAbastecimentoDTO.getDistribuicaoDiasPipeiroDTO().setDia27(data);
		            break;
			    case 27:
			    	pontoAbastecimentoDTO.getDistribuicaoDiasPipeiroDTO().setDia28(data);
		            break;
			    case 28:
			    	pontoAbastecimentoDTO.getDistribuicaoDiasPipeiroDTO().setDia29(data);
		            break;
			    case 29:
			    	pontoAbastecimentoDTO.getDistribuicaoDiasPipeiroDTO().setDia30(data);
		            break;
			    case 30:
			    	pontoAbastecimentoDTO.getDistribuicaoDiasPipeiroDTO().setDia31(data);
		            break;
			}
			
		}
			
			
		contadorQuantCarradas = contadorDiasUteis / pontoAbastecimentoDTO.getQtdViagensReal();
		
		
		if(contadorQuantCarradas == 0){
			contadorQuantCarradas = 1;
		}
		
		pontoAbastecimentoDTO.setQtdCarradas(contadorQuantCarradas);
		
		}


	public void valorPrevio(){
		
		logger.info("Inicio do método valorPrevio()");
		
		PontoAbastecimentoDTO pa;
		RotaPagamentoPipeiroDTO rp;
		PipeiroDTO pipeiroSelecionado = null;
		String nomePipeiro = getPipeiroSelecionado();
		
		List<RotaPagamentoPipeiroDTO> rpTotais = new ArrayList();
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Integer.valueOf(getAnoSelecionado()), Integer.valueOf(getMesSelecionado()), 1);

		
		if(!nomePipeiro.equalsIgnoreCase("Nao Informado") || (!Funcoes.isListaNulaOuVazia(pontoAbastecimentoPendente) && pontoAbastecimentoPendenteSelecionado != 0)){
		
			for (int i = 0; i < pipeiros.size(); i++) {
				PipeiroDTO pipeiro = pipeiros.get(i);
				
				if(pipeiro.getNome().equalsIgnoreCase(nomePipeiro)){
					pipeiroSelecionado = pipeiro;
					break;
				}
				
			}

			List<RotaPagamentoPipeiroDTO> rotaPagamentoPipeiroDTOListManualPrevio = new ArrayList();
			
			
			for (int i = 0; i < pontoAbastecimentoPendente.size(); i++) {
				pa = pontoAbastecimentoPendente.get(i);
				
				
				if(pa.getId() == pontoAbastecimentoPendenteSelecionado){
					
					
					try{
					service.calcularRotaPagamentoManualmente(rotaPagamentoPipeiroDTOListManualPrevio,pa,pipeiroSelecionado,
							getCidadeSelecionada(),calendar,datasDistribuicao,totalDias);
					} catch (Exception e) {
						// TODO: handle exception
						logger.error(e);
					}
					break;
				}
			}
			
			
			RotaPagamentoPipeiroDTO rpa;
			int totalViagensReal = 0;
			double totalvalor = 0;
			for (int i = 0; i < rotaPagamentoPipeiroDTOListManualPrevio.size(); i++) {
				rpa = rotaPagamentoPipeiroDTOListManualPrevio.get(i);
				totalViagensReal = totalViagensReal + rpa.getTotalViagensReal();
				totalvalor = totalvalor + rpa.getValorTotalPA();
			}
			
			setTotalViagensRealPendente(totalViagensReal);
			setTotalValorPendente(totalvalor);		
			
		}
		
		logger.info("Fim do método valorPrevio()");
	}
	
	public void onDateSelect(SelectEvent event) {

		logger.info("Inicio do método onDateSelect()");
		setDateFinal((Date) event.getObject());
		int intervalo = 0;  
		int i = 0; 
		  
		Calendar periodo1 = Calendar.getInstance();  
		periodo1.setTime(getDateInicial());   
		  
		Calendar periodo2 = Calendar.getInstance();  
		periodo2.setTime(getDateFinal());   
		  
		intervalo = (periodo2.get(periodo2.DAY_OF_YEAR) - periodo1.get(periodo1.DAY_OF_YEAR))+1; 
		
		datas = new ArrayList<DataDTO>();
		DataDTO dataDTO;
		String dia = "";
		   
		selectedDatas.clear();
		for (;i<intervalo;i++) {
		    	
		    	dataDTO = new DataDTO();
		    	
		    	if(i == 0){
		    		periodo1.add(periodo1.DATE,0);
		    	}else{
		    		
		    		periodo1.add(periodo1.DATE,1);  
		    	}
		    	
		    	if(periodo1.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
		    		dia = "Domingo";  
		    	}else if(periodo1.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY){
		    		dia = "Segunda";
		    	}else if(periodo1.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY){
		    		dia = "Terça";
		    	}else if(periodo1.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY){
		    		dia = "Quarta";
		    	}else if(periodo1.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY){
		    		dia = "Quinta";
		    	}else if(periodo1.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY){
		    		dia = "Sexta";
		    	}else if(periodo1.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
		    		dia = "Sábado";
		    	}
		    	
		    	dataDTO.setData(periodo1);
		    	dataDTO.setDatasExtenso(periodo1.get(periodo1.DATE)+ "/" + (periodo1.get(periodo1.MONTH) + 1)+" ("+dia+")");      
		    	
		    	datas.add(dataDTO);
		    	selectedDatas.add(dataDTO.getData());
		    	
		        System.out.println(i + " - " + periodo1.get(periodo1.DATE)   
		        + "/" + (periodo1.get(periodo1.MONTH) + 1) + "/" + periodo1.get(periodo1.YEAR));  
		    }
		
//		setTotalDias(selectedDatas.size());
		somaDias();   
		
		logger.info("Fim do método onDateSelect()");
    }
	
	public void somaDias(){
		
		int intervalo = 0;  
		int i = 0; 
		  
		Calendar periodo1 = Calendar.getInstance();  
		periodo1.setTime(getDateInicial());   
		  
		Calendar periodo2 = Calendar.getInstance();  
		periodo2.setTime(getDateFinal());   
		  
		intervalo = (periodo2.get(periodo2.DAY_OF_YEAR) - periodo1.get(periodo1.DAY_OF_YEAR))+1;
		
		setTotalDias(intervalo);
		
	}
	
	public void onSelectEdit(SelectEvent event) {
		//Popula tabela de Distribuição	
		logger.info("Inicio do método onSelectEdit()");

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Integer.valueOf(getAnoSelecionado()), Integer.valueOf(getMesSelecionado()), 1);
		
		PipeiroDTO pipeiroSelecionado = null;
		PontoAbastecimentoDTO pontoAbastecimentoSelecionado = null;
		
		String nomePipeiro = getPipeiroSelecionado();
		
		if(Funcoes.isNuloOuVazio(nomePipeiro)){
			FacesContext facesContext = FacesContext.getCurrentInstance();  
    		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Pipeiro não selecionado"));
		}else{
		
		for (int i = 0; i < pipeiros.size(); i++) {
			PipeiroDTO pipeiro = pipeiros.get(i);
			
			if(pipeiro.getNome().equalsIgnoreCase(nomePipeiro)){
				pipeiroSelecionado = pipeiro;
			}
			
		}

		rotaPagamentoPipeiroDTOListManual.clear();
		
		for (int i = 0; i < pontoAbastecimentosManual.size(); i++) {
			
			pontoAbastecimentoSelecionado = pontoAbastecimentosManual.get(i);
			
			try{
			service.calcularRotaPagamentoManualmente(rotaPagamentoPipeiroDTOListManual,pontoAbastecimentoSelecionado,pipeiroSelecionado,
					getCidadeSelecionada(),calendar,datasDistribuicao,totalDias);
			} catch (Exception e) {
				// TODO: handle exception
				logger.error(e);
			}
			
		}
		
		RotaPagamentoPipeiroDTO rpa;
		int totalViagensReal = 0;
		double totalvalor = 0;
		for (int i = 0; i < rotaPagamentoPipeiroDTOListManual.size(); i++) {
			rpa = rotaPagamentoPipeiroDTOListManual.get(i);
			totalViagensReal = totalViagensReal + rpa.getTotalViagensReal();
			totalvalor = totalvalor + rpa.getValorTotalPA();
		}
		
		setTotalViagensReal(totalViagensReal);
		setTotalValor(totalvalor);
		
		}
		
		logger.info("Fim do método onSelectEdit()");
	}
	
	public void unSelectEdit() {
		logger.info("Inicio do método unSelectEdit()");
		//Popula tabela de Distribuição	
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Integer.valueOf(getAnoSelecionado()), Integer.valueOf(getMesSelecionado()), 1);
		
		PipeiroDTO pipeiroSelecionado = null;
		PontoAbastecimentoDTO pontoAbastecimentoSelecionado = null;
		
		String nomePipeiro = getPipeiroSelecionado();
		
		for (int i = 0; i < pipeiros.size(); i++) {
			PipeiroDTO pipeiro = pipeiros.get(i);
			
			if(pipeiro.getNome().equalsIgnoreCase(nomePipeiro)){
				pipeiroSelecionado = pipeiro;
			}
			
		}

		rotaPagamentoPipeiroDTOListManual.clear();
		
		for (int i = 0; i < pontoAbastecimentosManual.size(); i++) {
			
			pontoAbastecimentoSelecionado = pontoAbastecimentosManual.get(i);
			
			try{
			service.calcularRotaPagamentoManualmente(rotaPagamentoPipeiroDTOListManual,pontoAbastecimentoSelecionado,pipeiroSelecionado,
					getCidadeSelecionada(),calendar,datasDistribuicao,totalDias);
			} catch (Exception e) {
				// TODO: handle exception
				logger.error(e);
			}
			
		}
		
		RotaPagamentoPipeiroDTO rpa;
		int totalViagensReal = 0;
		double totalvalor = 0;
		for (int i = 0; i < rotaPagamentoPipeiroDTOListManual.size(); i++) {
			rpa = rotaPagamentoPipeiroDTOListManual.get(i);
			totalViagensReal = totalViagensReal + rpa.getTotalViagensReal();
			totalvalor = totalvalor + rpa.getValorTotalPA();
		}
		
		setTotalViagensReal(totalViagensReal);
		setTotalValor(totalvalor);
		
		logger.info("Fim do método unSelectEdit()");
	}

	public void selectEditAll() {
		logger.info("Inicio do método selectEditAll()");
		//Popula tabela de Distribuição	
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Integer.valueOf(getAnoSelecionado()), Integer.valueOf(getMesSelecionado()), 1);
		PipeiroDTO pipeiroSelecionado = null;
		PontoAbastecimentoDTO pontoAbastecimentoSelecionado = null;
		
		String nomePipeiro = getPipeiroSelecionado();
		
		for (int i = 0; i < pipeiros.size(); i++) {
			PipeiroDTO pipeiro = pipeiros.get(i);
			
			if(pipeiro.getNome().equalsIgnoreCase(nomePipeiro)){
				pipeiroSelecionado = pipeiro;
			}
			
		}

		rotaPagamentoPipeiroDTOListManual.clear();
		
		for (int i = 0; i < pontoAbastecimentosManual.size(); i++) {
			
			pontoAbastecimentoSelecionado = pontoAbastecimentosManual.get(i);
			
			try{
			service.calcularRotaPagamentoManualmente(rotaPagamentoPipeiroDTOListManual,pontoAbastecimentoSelecionado,pipeiroSelecionado,
					getCidadeSelecionada(),calendar,datasDistribuicao,totalDias);
			} catch (Exception e) {
				// TODO: handle exception
				logger.error(e);
			}
			
		}
		
		RotaPagamentoPipeiroDTO rpa;
		int totalViagensReal = 0;
		double totalvalor = 0;
		for (int i = 0; i < rotaPagamentoPipeiroDTOListManual.size(); i++) {
			rpa = rotaPagamentoPipeiroDTOListManual.get(i);
			totalViagensReal = totalViagensReal + rpa.getTotalViagensReal();
			totalvalor = totalvalor + rpa.getValorTotalPA();
		}
		
		setTotalViagensReal(totalViagensReal);
		setTotalValor(totalvalor);
		logger.info("Fim do método selectEditAll()");
	}

	public void confirmarDistribuicao() {
		logger.info("Inicio do método confirmarDistribuicao()");
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Integer.valueOf(getAnoSelecionado()), Integer.valueOf(getMesSelecionado()), 1);
		
		//Removendo Pipeiro da Lista
		String nomePipeiro = getPipeiroSelecionado();
		PipeiroDTO pipeiroSelecionado = null;
		for (int i = 0; i < pipeiros.size(); i++) {
			PipeiroDTO pipeiro = pipeiros.get(i);
			
			if(pipeiro.getNome().equalsIgnoreCase(nomePipeiro)){
				pipeiroSelecionado = pipeiro;
			}
		}
		

		//Removendo Ponto de Abastecimento da Lista
		PontoAbastecimentoDTO pa;
		for (int i = 0; i < pontoAbastecimentosManual.size(); i++) {
			pa = pontoAbastecimentosManual.get(i);
			
			try{
			service.calcularRotaPagamentoManualmente(rotaPagamentoPipeiroDTOList,pa,pipeiroSelecionado,
					getCidadeSelecionada(),calendar,datasDistribuicao,totalDias);
			} catch (Exception e) {
				// TODO: handle exception
				logger.error(e);
			}
			pontoAbastecimentosClone.remove(pa);
			pipeirosRestante.remove(pipeiroSelecionado);
		}
		
		setTotaisValorDiferenca(rotaPagamentoPipeiroDTOList);
		setTotalViagensReal(0);
		setTotalValor(0);
		logger.info("Fim do método confirmarDistribuicao()");
	}
	
	
	public void excluirPontoAbastecimento(){
		logger.info("Inicio do método excluirPontoAbastecimento()");
		
		PontoAbastecimentoDTO pontoSelecionado = pontoAbastecimentoSelecionado;
		
//		RotaPagamentoPipeiroDTO rotaPSelecionada = rotaSelecionada;
		
		
		for (int i = 0; i < rotaPagamentoPipeiroDTOList.size(); i++) {
			RotaPagamentoPipeiroDTO rp = rotaPagamentoPipeiroDTOList.get(i);
			
			if(rp == rotaSelecionada){
				try{
				service.removerPontoAbastecimentoSelecionado(rp,pontoSelecionado);
				} catch (Exception e) {
					// TODO: handle exception
					logger.error(e);
				}
				
				if(rp.getPontoAbastecimentoDTOList().size() == 0){
					rotaPagamentoPipeiroDTOList.remove(rp);
					
					setTotaisValorDiferenca(rotaPagamentoPipeiroDTOList);
					break;
				}
			}
		}
		
		formatarDouble(rotaPagamentoPipeiroDTOList);
		
		//Se for Distribuição manual retorna a localidade para a lista
		if(!Funcoes.isNuloOuVazio(tipoDistribuicao) && tipoDistribuicao.equalsIgnoreCase("manual")){
			
			pontoAbastecimentosClone.add(pontoSelecionado);
			
		}
		
		//Se for Distribuição automática coloca a localidade na lista de PAs pendente
		if(!Funcoes.isNuloOuVazio(tipoDistribuicao) && tipoDistribuicao.equalsIgnoreCase("auto")){
			
			pontoAbastecimentoPendente.add(pontoSelecionado);
			
		}
		
		logger.info("Fim do método excluirPontoAbastecimento()");
	}
	
	public void distribuicaoAutoDias(){
	
		try{
			
//		pontoAbastecimentosEdicao.clear();	
			
		service.definirDiasCarradas(getRotaSelecionada());
		
		
		pontoAbastecimentosEdicao = rotaSelecionada.getPontoAbastecimentoDTOList();
		
	} catch (Exception e) {
		// TODO: handle exception
		logger.error(e);
	}
	
	}
	
	
	private void formatarDouble(List listaRotaPagamentoPipeiroDTO) {

		for (int i = 0; i < listaRotaPagamentoPipeiroDTO.size(); i++) {
			RotaPagamentoPipeiroDTO rp = (RotaPagamentoPipeiroDTO) listaRotaPagamentoPipeiroDTO
					.get(i);

			rp.setTotalQtdAgua(Funcoes.formatarDouble(rp.getTotalQtdAgua()));
			rp.setTotalViagensFormula(Funcoes.formatarDouble(rp
					.getTotalViagensFormula()));
			rp.setValorTotalPA(Funcoes.formatarDouble(rp.getValorTotalPA()));

			for (int j = 0; j < rp.getPontoAbastecimentoDTOList().size(); j++) {
				PontoAbastecimentoDTO pa = (PontoAbastecimentoDTO) rp
						.getPontoAbastecimentoDTOList().get(j);

				pa.setCapacidadePipa(Funcoes.formatarDouble(pa
						.getCapacidadePipa()));
				pa.setDistancia(Funcoes.formatarDouble(pa.getDistancia()));
				pa.setQtdAgua(Funcoes.formatarDouble(pa.getQtdAgua()));
				pa.setQtdViagensFormula(Funcoes.formatarDouble(pa
						.getQtdViagensFormula()));
				pa.setValorBruto(Funcoes.formatarDouble(pa.getValorBruto()));
			}
		}
	}
	
	public int getCidadeSelecionada() {
		return cidadeSelecionada;
	}

	public void setCidadeSelecionada(int cidadeSelecionada) {
		this.cidadeSelecionada = cidadeSelecionada;
	}

	public List<CidadeDTO> getCidades() {
		return cidades;
	}

	public void setCidades(List<CidadeDTO> cidades) {
		this.cidades = cidades;
	}

	public List<PontoAbastecimentoDTO> getPontoAbastecimentos() {
		return pontoAbastecimentos;
	}

	public void setPontoAbastecimentos(
			List<PontoAbastecimentoDTO> pontoAbastecimentos) {
		this.pontoAbastecimentos = pontoAbastecimentos;
	}

	public List<PontoAbastecimentoDTO> getPontoAbastecimentosClone() {
		return pontoAbastecimentosClone;
	}

	public void setPontoAbastecimentosClone(
			List<PontoAbastecimentoDTO> pontoAbastecimentosClone) {
		this.pontoAbastecimentosClone = pontoAbastecimentosClone;
	}

	public PagamentoPipeiroSevice getService() {
		return service;
	}

	public void setService(PagamentoPipeiroSevice service) {
		this.service = service;
	}

	public List<PipeiroDTO> getPipeiros() {
		return pipeiros;
	}

	public void setPipeiros(List<PipeiroDTO> pipeiros) {
		this.pipeiros = pipeiros;
	}

	public List<PipeiroDTO> getPipeirosClone() {
		return pipeirosClone;
	}

	public void setPipeirosClone(List<PipeiroDTO> pipeirosClone) {
		this.pipeirosClone = pipeirosClone;
	}

	public String getTipoDistribuicao() {
		return tipoDistribuicao;
	}

	public void setTipoDistribuicao(String tipoDistribuicao) {
		this.tipoDistribuicao = tipoDistribuicao;
	}

	public int getFlagExibirTabela() {
		return flagExibirTabela;
	}

	public void setFlagExibirTabela(int flagExibirTabela) {
		this.flagExibirTabela = flagExibirTabela;
	}

	public double getTotalValor() {
		return totalValor;
	}

	public void setTotalValor(double totalValor) {
		this.totalValor = totalValor;
	}

	public String getMesSelecionado() {
		return mesSelecionado;
	}

	public void setMesSelecionado(String mesSelecionado) {
		this.mesSelecionado = mesSelecionado;
	}

	public String getAnoSelecionado() {
		return anoSelecionado;
	}

	public void setAnoSelecionado(String anoSelecionado) {
		this.anoSelecionado = anoSelecionado;
	}

	public List<RotaPagamentoPipeiroDTO> getRotaPagamentoPipeiroDTOList() {
		return rotaPagamentoPipeiroDTOList;
	}

	public void setRotaPagamentoPipeiroDTOList(
			List<RotaPagamentoPipeiroDTO> rotaPagamentoPipeiroDTOList) {
		this.rotaPagamentoPipeiroDTOList = rotaPagamentoPipeiroDTOList;
	}

	public String getPipeiroSelecionado() {
		return pipeiroSelecionado;
	}

	public void setPipeiroSelecionado(String pipeiroSelecionado) {
		this.pipeiroSelecionado = pipeiroSelecionado;
	}
	
	public List<DataDTO> getDatas() {
		return datas;
	}
	

	public int getTotalDias() {
		return totalDias;
	}

	public void setTotalDias(int totalDias) {
		this.totalDias = totalDias;
	}

	public void setDatas(List<DataDTO> datas) {
		this.datas = datas;
	}

	public List<Date> getSelectedDatas() {
		return selectedDatas;
	}

	public void setSelectedDatas(List<Date> selectedDatas) {
		this.selectedDatas = selectedDatas;
	}

	public Date getDateInicial() {
		return dateInicial;
	}

	public void setDateInicial(Date dateInicial) {
		this.dateInicial = dateInicial;
	}

	public Date getDateFinal() {
		return dateFinal;
	}

	public void setDateFinal(Date dateFinal) {
		this.dateFinal = dateFinal;
	}

	public List<RotaPagamentoPipeiroDTO> getRotaPagamentoPipeiroDTOListManual() {
		return rotaPagamentoPipeiroDTOListManual;
	}

	public void setRotaPagamentoPipeiroDTOListManual(
			List<RotaPagamentoPipeiroDTO> rotaPagamentoPipeiroDTOListManual) {
		this.rotaPagamentoPipeiroDTOListManual = rotaPagamentoPipeiroDTOListManual;
	}

	public List<PontoAbastecimentoDTO> getPontoAbastecimentosManual() {
		return pontoAbastecimentosManual;
	}

	public void setPontoAbastecimentosManual(
			List<PontoAbastecimentoDTO> pontoAbastecimentosManual) {
		this.pontoAbastecimentosManual = pontoAbastecimentosManual;
	}

	public RotaPagamentoPipeiroDTO getRotaSelecionada() {
		return rotaSelecionada;
	}

	public void setRotaSelecionada(RotaPagamentoPipeiroDTO rotaSelecionada) {
		this.rotaSelecionada = rotaSelecionada;
	}

	public PontoAbastecimentoDTO getPontoAbastecimentoSelecionado() {
		return pontoAbastecimentoSelecionado;
	}

	public void setPontoAbastecimentoSelecionado(
			PontoAbastecimentoDTO pontoAbastecimentoSelecionado) {
		this.pontoAbastecimentoSelecionado = pontoAbastecimentoSelecionado;
	}

	public List<RotaPagamentoPipeiroDTO> getRotaPagamentoSelecionada() {
		return rotaPagamentoSelecionada;
	}

	public void setRotaPagamentoSelecionada(
			List<RotaPagamentoPipeiroDTO> rotaPagamentoSelecionada) {
		this.rotaPagamentoSelecionada = rotaPagamentoSelecionada;
	}

	public List<PontoAbastecimentoDTO> getPalist() {
		return palist;
	}

	public void setPalist(List<PontoAbastecimentoDTO> palist) {
		this.palist = palist;
	}

	public List<DataDTO> getDatasDistribuicao() {
		return datasDistribuicao;
	}

	public void setDatasDistribuicao(List<DataDTO> datasDistribuicao) {
		this.datasDistribuicao = datasDistribuicao;
	}
	
	 public void onRowEdit(RowEditEvent event) {
		 
		 try {
			
			 AssinaturasDTO AssinaturasDTO = (AssinaturasDTO) event.getObject();
			 
			 service.atualizarAssinaturaDistrPieiro(AssinaturasDTO);
			} catch (Exception e) {
				FacesContext facesContext = FacesContext.getCurrentInstance();  
	    		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Erro ao atualizar distribuição. ["
	    				+e.toString()+ " ]"));
			}
			 
	 }

	
	public void onCellEdit(CellEditEvent event) {
		logger.info("Inicio do método onCellEdit()");
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();
        
        int novoValor = Integer.valueOf(newValue.toString());
        String cabecalho = event.getColumn().getHeaderText();
        
        int indice = event.getRowIndex();
         
        PontoAbastecimentoDTO pa;
        for (int i = 0; i < rotaSelecionada.getPontoAbastecimentoDTOList().size(); i++) {
        	pa = (PontoAbastecimentoDTO) rotaSelecionada.getPontoAbastecimentoDTOList().get(i);
        	
        	if(i == indice){
        		
        		if(pa.getDistribuicaoDiasPipeiroDTO().getDia1().getDatasExtenso().equalsIgnoreCase(cabecalho)){
        			rotaSelecionada.setTotalDia1(rotaSelecionada.getTotalDia1()+novoValor);
				}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia2().getDatasExtenso().equalsIgnoreCase(cabecalho)){
					rotaSelecionada.setTotalDia2(rotaSelecionada.getTotalDia2()+novoValor);
				}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia3().getDatasExtenso().equalsIgnoreCase(cabecalho)){
					rotaSelecionada.setTotalDia3(rotaSelecionada.getTotalDia3()+novoValor);
				}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia4().getDatasExtenso().equalsIgnoreCase(cabecalho)){
					rotaSelecionada.setTotalDia4(rotaSelecionada.getTotalDia4()+novoValor);
				}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia5().getDatasExtenso().equalsIgnoreCase(cabecalho)){
					rotaSelecionada.setTotalDia5(rotaSelecionada.getTotalDia5()+novoValor);
				}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia6().getDatasExtenso().equalsIgnoreCase(cabecalho)){
					rotaSelecionada.setTotalDia6(rotaSelecionada.getTotalDia6()+novoValor);
				}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia7().getDatasExtenso().equalsIgnoreCase(cabecalho)){
					rotaSelecionada.setTotalDia7(rotaSelecionada.getTotalDia7()+novoValor);
				}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia8().getDatasExtenso().equalsIgnoreCase(cabecalho)){
					rotaSelecionada.setTotalDia8(rotaSelecionada.getTotalDia8()+novoValor);
				}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia9().getDatasExtenso().equalsIgnoreCase(cabecalho)){
					rotaSelecionada.setTotalDia9(rotaSelecionada.getTotalDia9()+novoValor);
				}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia10().getDatasExtenso().equalsIgnoreCase(cabecalho)){
					rotaSelecionada.setTotalDia10(rotaSelecionada.getTotalDia10()+novoValor);
				}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia11().getDatasExtenso().equalsIgnoreCase(cabecalho)){
					rotaSelecionada.setTotalDia11(rotaSelecionada.getTotalDia11()+novoValor);
				}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia12().getDatasExtenso().equalsIgnoreCase(cabecalho)){
					rotaSelecionada.setTotalDia12(rotaSelecionada.getTotalDia12()+novoValor);
				}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia13().getDatasExtenso().equalsIgnoreCase(cabecalho)){
					rotaSelecionada.setTotalDia13(rotaSelecionada.getTotalDia13()+novoValor);
				}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia14().getDatasExtenso().equalsIgnoreCase(cabecalho)){
					rotaSelecionada.setTotalDia14(rotaSelecionada.getTotalDia14()+novoValor);
				}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia15().getDatasExtenso().equalsIgnoreCase(cabecalho)){
					rotaSelecionada.setTotalDia15(rotaSelecionada.getTotalDia15()+novoValor);
				}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia16().getDatasExtenso().equalsIgnoreCase(cabecalho)){
					rotaSelecionada.setTotalDia16(rotaSelecionada.getTotalDia16()+novoValor);
				}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia17().getDatasExtenso().equalsIgnoreCase(cabecalho)){
					rotaSelecionada.setTotalDia17(rotaSelecionada.getTotalDia17()+novoValor);
				}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia18().getDatasExtenso().equalsIgnoreCase(cabecalho)){
					rotaSelecionada.setTotalDia18(rotaSelecionada.getTotalDia18()+novoValor);
				}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia19().getDatasExtenso().equalsIgnoreCase(cabecalho)){
					rotaSelecionada.setTotalDia19(rotaSelecionada.getTotalDia19()+novoValor);
				}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia20().getDatasExtenso().equalsIgnoreCase(cabecalho)){
					rotaSelecionada.setTotalDia20(rotaSelecionada.getTotalDia20()+novoValor);
				}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia21().getDatasExtenso().equalsIgnoreCase(cabecalho)){
					rotaSelecionada.setTotalDia21(rotaSelecionada.getTotalDia21()+novoValor);
				}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia22().getDatasExtenso().equalsIgnoreCase(cabecalho)){
					rotaSelecionada.setTotalDia22(rotaSelecionada.getTotalDia22()+novoValor);
				}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia23().getDatasExtenso().equalsIgnoreCase(cabecalho)){
					rotaSelecionada.setTotalDia23(rotaSelecionada.getTotalDia23()+novoValor);
				}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia24().getDatasExtenso().equalsIgnoreCase(cabecalho)){
					rotaSelecionada.setTotalDia24(rotaSelecionada.getTotalDia24()+novoValor);
				}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia25().getDatasExtenso().equalsIgnoreCase(cabecalho)){
					rotaSelecionada.setTotalDia25(rotaSelecionada.getTotalDia25()+novoValor);
				}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia26().getDatasExtenso().equalsIgnoreCase(cabecalho)){
					rotaSelecionada.setTotalDia26(rotaSelecionada.getTotalDia26()+novoValor);
				}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia27().getDatasExtenso().equalsIgnoreCase(cabecalho)){
					rotaSelecionada.setTotalDia27(rotaSelecionada.getTotalDia27()+novoValor);
				}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia28().getDatasExtenso().equalsIgnoreCase(cabecalho)){
					rotaSelecionada.setTotalDia28(rotaSelecionada.getTotalDia28()+novoValor);
				}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia29().getDatasExtenso().equalsIgnoreCase(cabecalho)){
					rotaSelecionada.setTotalDia29(rotaSelecionada.getTotalDia29()+novoValor);
				}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia30().getDatasExtenso().equalsIgnoreCase(cabecalho)){
					rotaSelecionada.setTotalDia30(rotaSelecionada.getTotalDia30()+novoValor);
				}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia31().getDatasExtenso().equalsIgnoreCase(cabecalho)){
					rotaSelecionada.setTotalDia31(rotaSelecionada.getTotalDia31()+novoValor);
				}
        		
        	}
		}
        
//        if(newValue != null && !newValue.equals(oldValue)) {
//            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
//            FacesContext.getCurrentInstance().addMessage(null, msg);
//        }
        
        logger.info("Fim do método onCellEdit()");
    }
	
	public void onCellEditEdit(CellEditEvent event) {
		logger.info("Inicio do método onCellEditEdit()");
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();
        
        int novoValor = Integer.valueOf(newValue.toString());
        String cabecalho = event.getColumn().getHeaderText();
        
        int indice = event.getRowIndex();
         
        PontoAbastecimentoDTO pa;
        for (int i = 0; i < pontoAbastecimentosEdicao.size(); i++) {
        	pa = (PontoAbastecimentoDTO) pontoAbastecimentosEdicao.get(i);
        	
        	if(i == indice){
        		
        		if(pa.getDistribuicaoDiasPipeiroDTO().getDia1().getDatasExtenso().equalsIgnoreCase(cabecalho)){
        			rotaSelecionada.setTotalDia1(rotaSelecionada.getTotalDia1()+novoValor);
				}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia2().getDatasExtenso().equalsIgnoreCase(cabecalho)){
					rotaSelecionada.setTotalDia2(rotaSelecionada.getTotalDia2()+novoValor);
				}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia3().getDatasExtenso().equalsIgnoreCase(cabecalho)){
					rotaSelecionada.setTotalDia3(rotaSelecionada.getTotalDia3()+novoValor);
				}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia4().getDatasExtenso().equalsIgnoreCase(cabecalho)){
					rotaSelecionada.setTotalDia4(rotaSelecionada.getTotalDia4()+novoValor);
				}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia5().getDatasExtenso().equalsIgnoreCase(cabecalho)){
					rotaSelecionada.setTotalDia5(rotaSelecionada.getTotalDia5()+novoValor);
				}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia6().getDatasExtenso().equalsIgnoreCase(cabecalho)){
					rotaSelecionada.setTotalDia6(rotaSelecionada.getTotalDia6()+novoValor);
				}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia7().getDatasExtenso().equalsIgnoreCase(cabecalho)){
					rotaSelecionada.setTotalDia7(rotaSelecionada.getTotalDia7()+novoValor);
				}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia8().getDatasExtenso().equalsIgnoreCase(cabecalho)){
					rotaSelecionada.setTotalDia8(rotaSelecionada.getTotalDia8()+novoValor);
				}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia9().getDatasExtenso().equalsIgnoreCase(cabecalho)){
					rotaSelecionada.setTotalDia9(rotaSelecionada.getTotalDia9()+novoValor);
				}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia10().getDatasExtenso().equalsIgnoreCase(cabecalho)){
					rotaSelecionada.setTotalDia10(rotaSelecionada.getTotalDia10()+novoValor);
				}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia11().getDatasExtenso().equalsIgnoreCase(cabecalho)){
					rotaSelecionada.setTotalDia11(rotaSelecionada.getTotalDia11()+novoValor);
				}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia12().getDatasExtenso().equalsIgnoreCase(cabecalho)){
					rotaSelecionada.setTotalDia12(rotaSelecionada.getTotalDia12()+novoValor);
				}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia13().getDatasExtenso().equalsIgnoreCase(cabecalho)){
					rotaSelecionada.setTotalDia13(rotaSelecionada.getTotalDia13()+novoValor);
				}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia14().getDatasExtenso().equalsIgnoreCase(cabecalho)){
					rotaSelecionada.setTotalDia14(rotaSelecionada.getTotalDia14()+novoValor);
				}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia15().getDatasExtenso().equalsIgnoreCase(cabecalho)){
					rotaSelecionada.setTotalDia15(rotaSelecionada.getTotalDia15()+novoValor);
				}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia16().getDatasExtenso().equalsIgnoreCase(cabecalho)){
					rotaSelecionada.setTotalDia16(rotaSelecionada.getTotalDia16()+novoValor);
				}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia17().getDatasExtenso().equalsIgnoreCase(cabecalho)){
					rotaSelecionada.setTotalDia17(rotaSelecionada.getTotalDia17()+novoValor);
				}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia18().getDatasExtenso().equalsIgnoreCase(cabecalho)){
					rotaSelecionada.setTotalDia18(rotaSelecionada.getTotalDia18()+novoValor);
				}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia19().getDatasExtenso().equalsIgnoreCase(cabecalho)){
					rotaSelecionada.setTotalDia19(rotaSelecionada.getTotalDia19()+novoValor);
				}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia20().getDatasExtenso().equalsIgnoreCase(cabecalho)){
					rotaSelecionada.setTotalDia20(rotaSelecionada.getTotalDia20()+novoValor);
				}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia21().getDatasExtenso().equalsIgnoreCase(cabecalho)){
					rotaSelecionada.setTotalDia21(rotaSelecionada.getTotalDia21()+novoValor);
				}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia22().getDatasExtenso().equalsIgnoreCase(cabecalho)){
					rotaSelecionada.setTotalDia22(rotaSelecionada.getTotalDia22()+novoValor);
				}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia23().getDatasExtenso().equalsIgnoreCase(cabecalho)){
					rotaSelecionada.setTotalDia23(rotaSelecionada.getTotalDia23()+novoValor);
				}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia24().getDatasExtenso().equalsIgnoreCase(cabecalho)){
					rotaSelecionada.setTotalDia24(rotaSelecionada.getTotalDia24()+novoValor);
				}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia25().getDatasExtenso().equalsIgnoreCase(cabecalho)){
					rotaSelecionada.setTotalDia25(rotaSelecionada.getTotalDia25()+novoValor);
				}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia26().getDatasExtenso().equalsIgnoreCase(cabecalho)){
					rotaSelecionada.setTotalDia26(rotaSelecionada.getTotalDia26()+novoValor);
				}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia27().getDatasExtenso().equalsIgnoreCase(cabecalho)){
					rotaSelecionada.setTotalDia27(rotaSelecionada.getTotalDia27()+novoValor);
				}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia28().getDatasExtenso().equalsIgnoreCase(cabecalho)){
					rotaSelecionada.setTotalDia28(rotaSelecionada.getTotalDia28()+novoValor);
				}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia29().getDatasExtenso().equalsIgnoreCase(cabecalho)){
					rotaSelecionada.setTotalDia29(rotaSelecionada.getTotalDia29()+novoValor);
				}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia30().getDatasExtenso().equalsIgnoreCase(cabecalho)){
					rotaSelecionada.setTotalDia30(rotaSelecionada.getTotalDia30()+novoValor);
				}else if(pa.getDistribuicaoDiasPipeiroDTO().getDia31().getDatasExtenso().equalsIgnoreCase(cabecalho)){
					rotaSelecionada.setTotalDia31(rotaSelecionada.getTotalDia31()+novoValor);
				}
        		
        	}
		}
        
        rotaSelecionada.setPontoAbastecimentoDTOList(pontoAbastecimentosEdicao);
//        if(newValue != null && !newValue.equals(oldValue)) {
//            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
//            FacesContext.getCurrentInstance().addMessage(null, msg);
//        }
        
        logger.info("Fim do método onCellEdit()");
    }
	
	
	public String colorColumn(DataDTO dia){
		
		String cor = "";
		
		
		if(dia.isEhFimDeSemana()){
			cor = "background-color: yellow";
		}
		
		return cor;
	}
	
	private void distribuirDiasCarrada(DistribuicaoDiasPipeiroDTO distribuicaoDias, List<DataDTO> datasDistribuicao) {

		for (int i = 0; i < datasDistribuicao.size(); i++) {
			DataDTO data = datasDistribuicao.get(i);
			
			switch( i )
			{
			    case 0:
			    	distribuicaoDias.setDia1(data);
			            break;
			    
			    case 1:
			    	distribuicaoDias.setDia2(data);
			            break;
			    
			    case 2:
			    	distribuicaoDias.setDia3(data);
			            break;
			    case 3:
			    	distribuicaoDias.setDia4(data);
		            break;
			    case 4:
			    	distribuicaoDias.setDia5(data);
		            break;
			    case 5:
			    	distribuicaoDias.setDia6(data);
		            break;
			    case 6:
			    	distribuicaoDias.setDia7(data);
		            break;
			    case 7:
			    	distribuicaoDias.setDia8(data);
		            break;
			    case 8:
			    	distribuicaoDias.setDia9(data);
		            break;
			    case 9:
			    	distribuicaoDias.setDia10(data);
		            break;
			    case 10:
			    	distribuicaoDias.setDia11(data);
		            break;
			    case 11:
			    	distribuicaoDias.setDia12(data);
		            break;
			    case 12:
			    	distribuicaoDias.setDia13(data);
		            break;
			    case 13:
			    	distribuicaoDias.setDia14(data);
		            break;
			    case 14:
			    	distribuicaoDias.setDia15(data);
		            break;
			    case 15:
			    	distribuicaoDias.setDia16(data);
		            break;
			    case 16:
			    	distribuicaoDias.setDia17(data);
		            break;
			    case 17:
			    	distribuicaoDias.setDia18(data);
		            break;
			    case 18:
			    	distribuicaoDias.setDia19(data);
		            break;
			    case 19:
			    	distribuicaoDias.setDia20(data);
		            break;
			    case 20:
			    	distribuicaoDias.setDia21(data);
		            break;
			    case 21:
			    	distribuicaoDias.setDia22(data);
		            break;
			    case 22:
			    	distribuicaoDias.setDia23(data);
		            break;
			    case 23:
			    	distribuicaoDias.setDia24(data);
		            break;
			    case 24:
			    	distribuicaoDias.setDia25(data);
		            break;
			    case 25:
			    	distribuicaoDias.setDia26(data);
		            break;
			    case 26:
			    	distribuicaoDias.setDia27(data);
		            break;
			    case 27:
			    	distribuicaoDias.setDia28(data);
		            break;
			    case 28:
			    	distribuicaoDias.setDia29(data);
		            break;
			    case 29:
			    	distribuicaoDias.setDia30(data);
		            break;
			    case 30:
			    	distribuicaoDias.setDia31(data);
		            break;
			}
			
		}
			
			
		}


	public DistribuicaoDiasPipeiroDTO getDistribuicaoDias() {
		return distribuicaoDias;
	}

	public void setDistribuicaoDias(DistribuicaoDiasPipeiroDTO distribuicaoDias) {
		this.distribuicaoDias = distribuicaoDias;
	}

	public int getTotalViagensReal() {
		return totalViagensReal;
	}

	public void setTotalViagensReal(int totalViagensReal) {
		this.totalViagensReal = totalViagensReal;
	}

	public List<PontoAbastecimentoDTO> getPontoAbastecimentoPendente() {
		return pontoAbastecimentoPendente;
	}

	public void setPontoAbastecimentoPendente(
			List<PontoAbastecimentoDTO> pontoAbastecimentoPendente) {
		this.pontoAbastecimentoPendente = pontoAbastecimentoPendente;
	}

	public int getPontoAbastecimentoPendenteSelecionado() {
		return pontoAbastecimentoPendenteSelecionado;
	}

	public void setPontoAbastecimentoPendenteSelecionado(
			int pontoAbastecimentoPendenteSelecionado) {
		this.pontoAbastecimentoPendenteSelecionado = pontoAbastecimentoPendenteSelecionado;
	}

	public int getTotalViagensRealPendente() {
		return totalViagensRealPendente;
	}

	public void setTotalViagensRealPendente(int totalViagensRealPendente) {
		this.totalViagensRealPendente = totalViagensRealPendente;
	}

	public double getTotalValorPendente() {
		return totalValorPendente;
	}

	public void setTotalValorPendente(double totalValorPendente) {
		this.totalValorPendente = totalValorPendente;
	}

	public double getValorTotalRota() {
		return valorTotalRota;
	}

	public void setValorTotalRota(double valorTotalRota) {
		this.valorTotalRota = valorTotalRota;
	}

	public double getDiferencaMaoirMenor() {
		return diferencaMaoirMenor;
	}

	public void setDiferencaMaoirMenor(double diferencaMaoirMenor) {
		this.diferencaMaoirMenor = diferencaMaoirMenor;
	}

	public CidadeDTO getCidadeInicial() {
		return cidadeInicial;
	}

	public void setCidadeInicial(CidadeDTO cidadeInicial) {
		this.cidadeInicial = cidadeInicial;
	}


	public int getFlagExibirTabelaCidadeInicial() {
		return flagExibirTabelaCidadeInicial;
	}


	public void setFlagExibirTabelaCidadeInicial(int flagExibirTabelaCidadeInicial) {
		this.flagExibirTabelaCidadeInicial = flagExibirTabelaCidadeInicial;
	}


	public int getFlagExibirDefinirDias() {
		return flagExibirDefinirDias;
	}


	public void setFlagExibirDefinirDias(int flagExibirDefinirDias) {
		this.flagExibirDefinirDias = flagExibirDefinirDias;
	}


	public boolean isHalibitarCampoAno() {
		return halibitarCampoAno;
	}


	public void setHalibitarCampoAno(boolean halibitarCampoAno) {
		this.halibitarCampoAno = halibitarCampoAno;
	}


	public List<RotaPagamentoPipeiroDTO> getRpCidade() {
		return rpCidade;
	}


	public void setRpCidade(List<RotaPagamentoPipeiroDTO> rpCidade) {
		this.rpCidade = rpCidade;
	}


	public List<RotaPagamentoPipeiroDTO> getRpCidadeSelectMode() {
		return rpCidadeSelectMode;
	}


	public void setRpCidadeSelectMode(
			List<RotaPagamentoPipeiroDTO> rpCidadeSelectMode) {
		this.rpCidadeSelectMode = rpCidadeSelectMode;
	}


	public int getPipeiroSelecionadoImpressao() {
		return pipeiroSelecionadoImpressao;
	}


	public void setPipeiroSelecionadoImpressao(int pipeiroSelecionadoImpressao) {
		this.pipeiroSelecionadoImpressao = pipeiroSelecionadoImpressao;
	}


	public int getReducaoViagens() {
		return reducaoViagens;
	}


	public void setReducaoViagens(int reducaoViagens) {
		this.reducaoViagens = reducaoViagens;
	}


	public String getObsReducao() {
		return obsReducao;
	}


	public void setObsReducao(String obsReducao) {
		this.obsReducao = obsReducao;
	}


	public List<AssinaturasDTO> getAssinaturas() {
		return assinaturas;
	}


	public void setAssinaturas(List<AssinaturasDTO> assinaturas) {
		this.assinaturas = assinaturas;
	}


	public PipeiroService getServicePipeiro() {
		return servicePipeiro;
	}


	public void setServicePipeiro(PipeiroService servicePipeiro) {
		this.servicePipeiro = servicePipeiro;
	}


	public CidadeService getServiceCidade() {
		return serviceCidade;
	}


	public void setServiceCidade(CidadeService serviceCidade) {
		this.serviceCidade = serviceCidade;
	}


	public int getNumOS() {
		return numOS;
	}


	public void setNumOS(int numOS) {
		this.numOS = numOS;
	}


	public int getNumBAR() {
		return numBAR;
	}


	public void setNumBAR(int numBAR) {
		this.numBAR = numBAR;
	}


	public int getNumLacre() {
		return numLacre;
	}


	public void setNumLacre(int numLacre) {
		this.numLacre = numLacre;
	}


	public Date getDataSorteio() {
		return dataSorteio;
	}


	public void setDataSorteio(Date dataSorteio) {
		this.dataSorteio = dataSorteio;
	}


	public Date getDataBAR() {
		return dataBAR;
	}


	public void setDataBAR(Date dataBAR) {
		this.dataBAR = dataBAR;
	}


	public List<PipeiroDTO> getPipeirosRestante() {
		return pipeirosRestante;
	}


	public void setPipeirosRestante(List<PipeiroDTO> pipeirosRestante) {
		this.pipeirosRestante = pipeirosRestante;
	}


	public String getMsgDefDias() {
		return msgDefDias;
	}


	public void setMsgDefDias(String msgDefDias) {
		this.msgDefDias = msgDefDias;
	}


	public String getMesAnoSelecionadoExtenso() {
		return mesAnoSelecionadoExtenso;
	}


	public void setMesAnoSelecionadoExtenso(String mesAnoSelecionadoExtenso) {
		this.mesAnoSelecionadoExtenso = mesAnoSelecionadoExtenso;
	}


	public List<InteracaoDistribuicaoDTO> getInteracaoDistribuicao() {
		return interacaoDistribuicao;
	}


	public void setInteracaoDistribuicao(
			List<InteracaoDistribuicaoDTO> interacaoDistribuicao) {
		this.interacaoDistribuicao = interacaoDistribuicao;
	}


	public InteracaoDistribuicaoDTO getInteracaoDistribuicaoSelecionado() {
		return interacaoDistribuicaoSelecionado;
	}


	public void setInteracaoDistribuicaoSelecionado(
			InteracaoDistribuicaoDTO interacaoDistribuicaoSelecionado) {
		this.interacaoDistribuicaoSelecionado = interacaoDistribuicaoSelecionado;
	}


	public List<PontoAbastecimentoDTO> getPontoAbastecimentosEdicao() {
		return pontoAbastecimentosEdicao;
	}


	public void setPontoAbastecimentosEdicao(
			List<PontoAbastecimentoDTO> pontoAbastecimentosEdicao) {
		this.pontoAbastecimentosEdicao = pontoAbastecimentosEdicao;
	}


	public boolean isFlagRecuperarUltimaDistribuicao() {
		return flagRecuperarUltimaDistribuicao;
	}


	public void setFlagRecuperarUltimaDistribuicao(
			boolean flagRecuperarUltimaDistribuicao) {
		this.flagRecuperarUltimaDistribuicao = flagRecuperarUltimaDistribuicao;
	}

}