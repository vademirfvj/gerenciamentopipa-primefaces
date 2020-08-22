package opp.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
import opp.dto.CidadeDTO;
import opp.dto.IndiceDTO;
import opp.dto.PontoAbastecimentoDTO;
import opp.dto.UsuarioDTO;
import opp.entity.Status;
import opp.sevice.CidadeService;
import opp.sevice.PagamentoPipeiroSevice;
import opp.sevice.PontoAbastecimentoService;
import opp.util.Constantes;
import opp.util.Funcoes;
import opp.util.Propriedades;

import org.apache.log4j.Logger;
import org.primefaces.event.RowEditEvent;

@ViewScoped
@ManagedBean(name = "cadastrarPontoAbastecimentoBean")
@SessionScoped
public class CadastrarPontoAbastecimentoBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7978755276492782641L;

	private static final Logger logger = Logger.getLogger(CadastrarPontoAbastecimentoBean.class);
	
	private static Propriedades rpcProperties = Propriedades.getInstance();
	
	/* Atributos de Cadastro */
	private PontoAbastecimentoDTO pontoAbastecimento;
	private int id;
	private String comunidade;
	private int populacao;
	private double distancia;
	private String momento;
	private int codcidade;
	private String georeferenciamento;
	private double volume;
	private double asfalto;
	private double terra;
	private double indice;
	private String apontador;
	private String substituto;
	private String status;
	private String msg5l;
	private String msg20l;

	private int codGcda;
	private int litrosDiario;
	private int populacaoDefault;
	private int populacao5L;
	private int codIndice;
	
	private boolean indiceEspecial;
	private boolean indiceEspecialEdit;
	
	private List<CidadeDTO> listaCidade;
	
	/* Atributos de Pesquisa */
	private String pesquisaPontoAbastecimento;
	private String pesquisaStatusPontoAbastecimento;
	private List<PontoAbastecimentoDTO> listaPesquisa;
	private int codcidadePesquisa;
	
	/* Atributos de Edição */
	private PontoAbastecimentoDTO pontoAbastecimentoEditavel;
	
	//Atributos da Aba Indice
	private List<IndiceDTO> listaIndice;
	
	@ManagedProperty(value = "#{pontoAbastecimentoService}")
	private PontoAbastecimentoService pontoAbastecimentoService;
	@ManagedProperty(value = "#{cidadeService}")
	private CidadeService cidadeService;
	@ManagedProperty("#{pagamentoPipeiroSevice}")
	private PagamentoPipeiroSevice service;
	
	@PostConstruct
	public void loadPage() {
		
		logger.info("Inicio da tela de Cadastro de Ponto de Abastecimento.");
		
		
		cidadeService = new CidadeService();
		pontoAbastecimentoService = new PontoAbastecimentoService();
		service = new PagamentoPipeiroSevice();
		
		try {
			listaCidade = cidadeService.recuperaTodasCidades();
		} catch (Exception ex) {
			logger.error(ex);
			FacesContext.getCurrentInstance().addMessage(    
	                "msgCadastrarPontoAbastecimento",    
	                new FacesMessage(FacesMessage.SEVERITY_ERROR,    
	                        "Erro ao recuperar Cidadades.", null));
		}
		pontoAbastecimentoEditavel = new PontoAbastecimentoDTO();
		
		try {
			
		listaIndice = pontoAbastecimentoService.recuperaIndiceTodos();
		
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		msg5l = rpcProperties.buscaPropriedade("mensagem.5l");
		msg20l = rpcProperties.buscaPropriedade("mensagem.20l"); 

	}
	
	/**
	 * Ações
	 */
	public void salvar(){
		
		logger.info("Inicio do método salvar()");
		
		status = Status.ATIVO.toString();
		
		if(campoEhValido(id, comunidade, populacao, distancia,
				momento, codcidade, georeferenciamento, volume,
				asfalto, terra, indice, apontador, substituto, status, populacao5L, populacaoDefault, codGcda)){
			
			//Monta objeto
			pontoAbastecimento = new PontoAbastecimentoDTO(
					comunidade, populacao, distancia, momento, codcidade, georeferenciamento, 
					volume, asfalto, terra, indice, apontador,substituto,status, populacao5L, populacaoDefault, codGcda, codIndice, litrosDiario
					);
			
			//Verifica se Existe
			boolean existe = false;
			
			try {
				existe = verificaSeExiste(pontoAbastecimento.getComunidade().trim(),codcidade);
			} catch (Exception e) {
				logger.error(e);
				FacesContext.getCurrentInstance().addMessage(    
		                "msgCadastrarPontoAbastecimento",    
		                new FacesMessage(FacesMessage.SEVERITY_ERROR,    
		                        "Não foi possível salvar o registro.", null));
			}
			
			if(!existe){
				//Insere PontoAbastecimento
				boolean erro = false;
				
				try{
					pontoAbastecimentoService.inserePontoAbastecimento(pontoAbastecimento);
					
					UsuarioDTO usuarioNaSessao = (UsuarioDTO) Funcoes.getSession().getAttribute(Constantes.USUARIO_LOGADO);
					
					service.inserirInteracaoUsuario(Constantes.CADASTRO_PA,Constantes.INSERIRDADOS,null,new Date(),usuarioNaSessao.getId());
					
				}catch(Exception ex){
					logger.error(ex);
					erro = true;
				}
				
				if(erro){
					logger.error("Não foi possível salvar o registro.");
					FacesContext.getCurrentInstance().addMessage(    
		                "msgCadastrarPontoAbastecimento",    
		                new FacesMessage(FacesMessage.SEVERITY_ERROR,    
		                        "Não foi possível salvar o registro.", null));
				}else{
					logger.info("Registro salvo com sucesso.");
					FacesContext.getCurrentInstance().addMessage(    
		                "msgCadastrarPontoAbastecimento",    
		                new FacesMessage(FacesMessage.SEVERITY_INFO,    
		                        "Registro salvo com sucesso.", null));
					limpaForm();
				}
			}else{
				
				FacesContext.getCurrentInstance().addMessage(    
		                "msgCadastrarPontoAbastecimento",    
		                new FacesMessage(FacesMessage.SEVERITY_ERROR,    
		                        "Registro já existe.", null));
			}
		}else{
			
			StringBuffer msgErro = new StringBuffer();
			
			msgErro.append("Campo(s) obrigatório(s): ");
			
			if(Funcoes.isNuloOuVazio(comunidade)){
				msgErro.append("- Nome do Ponto de Abastecimento ");
			}
			if(Funcoes.isNuloOuVazioZero(populacao5L) && Funcoes.isNuloOuVazioZero(populacaoDefault)){
				msgErro.append("- População 5 L e/ou População 20 L ");
			}
			if(Funcoes.isNuloOuVazio(codcidade)){
				msgErro.append("- Cidade ");
			}
			if(Funcoes.isNuloOuVazioOuDoubleZero(asfalto) && Funcoes.isNuloOuVazioOuDoubleZero(terra)){
				msgErro.append("- Asfalto e/ou Terra ");
			}
			if(Funcoes.isNuloOuVazio(apontador)){
				msgErro.append("- Apontador ");
			}

			FacesContext.getCurrentInstance().addMessage(    
					"msgCadastrarCidade",    
					new FacesMessage(FacesMessage.SEVERITY_ERROR,    
							msgErro.toString(), null));

		}

		logger.info("Fim do método salvar()");
	}
	
	public void somaPopulacao(){
		
		logger.info("Inicio do método somaPopulacao()");
		
		int somaPopulacao = populacaoDefault + populacao5L;
		
		setPopulacao(somaPopulacao);
		
		int litrosDefaut = Integer.valueOf(rpcProperties.buscaPropriedade("litrosPessoa"));
		
		int litrosDiarios = populacao5L*5 + populacaoDefault*litrosDefaut;
		
		setLitrosDiario(litrosDiarios);
		
		logger.info("Fim do método somaPopulacao()");
	}
	
	public void somaPopulacaoEditavel(){
		
		logger.info("Inicio do método somaPopulacaoEditavel()");
		
		int somaPopulacao = pontoAbastecimentoEditavel.getPopulacaoDefault() + pontoAbastecimentoEditavel.getPopulacao5L();
		
		pontoAbastecimentoEditavel.setPopulacao(somaPopulacao);
		
		int litrosDefaut = Integer.valueOf(rpcProperties.buscaPropriedade("litrosPessoa"));
		
		int litrosDiarios = populacao5L*5 + populacaoDefault*litrosDefaut;
		
		pontoAbastecimentoEditavel.setLitrosDiario(litrosDiarios);
		
		logger.info("Fim do método somaPopulacaoEditavel()");
		
	}
	
	
	public void calculaIndice(){
		
		logger.info("Inicio do método calculaIndice()");
		
		if(!indiceEspecial){	

			try {
				
			IndiceDTO indice = new IndiceDTO();
			
			if(asfalto >= terra && !Funcoes.isNuloOuVazioOuDoubleZero(terra)){
				
				indice = pontoAbastecimentoService.recuperaIndicePorDescricao(Constantes.MAIS_ASFALTO);
				
			}else if (terra > asfalto && !Funcoes.isNuloOuVazioOuDoubleZero(asfalto)){
				
				indice = pontoAbastecimentoService.recuperaIndicePorDescricao(Constantes.MAIS_TERRA);
			
			}else if(!Funcoes.isNuloOuVazioOuDoubleZero(terra) && Funcoes.isNuloOuVazioOuDoubleZero(asfalto)){
				
				indice = pontoAbastecimentoService.recuperaIndicePorDescricao(Constantes.SO_TERRA);
			
			}else if(!Funcoes.isNuloOuVazioOuDoubleZero(asfalto) && Funcoes.isNuloOuVazioOuDoubleZero(terra)){
				
				indice = pontoAbastecimentoService.recuperaIndicePorDescricao(Constantes.SO_ASFALTO);
			}
			
			setIndice(indice.getIndice());
			setMomento(indice.getDscIndice());
			setCodIndice(indice.getId());
			setDistancia(asfalto+terra);
			
			
			} catch (Exception e) {
				FacesContext facesContext = FacesContext.getCurrentInstance();  
	    		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Erro ao calcular o índice. ["
	    				+e.toString()+ " ]"));
	    		logger.error("Erro ao calcular o índice. ["
	    				+e.toString()+ " ]");
			}
		
		}else{
			
			setDistancia(asfalto+terra);
		}
		
		logger.info("Fim do método calculaIndice()");
	}
	
	public void calculaIndiceEditavel(){
		
		logger.info("Inicio do método calculaIndiceEditavel()");
		
		
		if(!indiceEspecialEdit){
		
			try {
				
			
			IndiceDTO indice = new IndiceDTO();
			
			if(pontoAbastecimentoEditavel.getAsfalto() >= pontoAbastecimentoEditavel.getTerra() && !Funcoes.isNuloOuVazioOuDoubleZero(pontoAbastecimentoEditavel.getTerra())){
				
				indice = pontoAbastecimentoService.recuperaIndicePorDescricao(Constantes.MAIS_ASFALTO);
				
			}else if (pontoAbastecimentoEditavel.getTerra() > pontoAbastecimentoEditavel.getAsfalto() && !Funcoes.isNuloOuVazioOuDoubleZero(pontoAbastecimentoEditavel.getAsfalto())){
				
				indice = pontoAbastecimentoService.recuperaIndicePorDescricao(Constantes.MAIS_TERRA);
			
			}else if(!Funcoes.isNuloOuVazioOuDoubleZero(pontoAbastecimentoEditavel.getTerra()) && Funcoes.isNuloOuVazioOuDoubleZero(pontoAbastecimentoEditavel.getAsfalto())){
				
				indice = pontoAbastecimentoService.recuperaIndicePorDescricao(Constantes.SO_TERRA);
			
			}else if(!Funcoes.isNuloOuVazioOuDoubleZero(pontoAbastecimentoEditavel.getAsfalto()) && Funcoes.isNuloOuVazioOuDoubleZero(pontoAbastecimentoEditavel.getTerra())){
				
				indice = pontoAbastecimentoService.recuperaIndicePorDescricao(Constantes.SO_ASFALTO);
			}
			
			pontoAbastecimentoEditavel.setIndice(indice.getIndice());
			pontoAbastecimentoEditavel.setCodIndice(indice.getId());
			pontoAbastecimentoEditavel.setMomento(indice.getDscIndice());
			pontoAbastecimentoEditavel.setDistancia(pontoAbastecimentoEditavel.getAsfalto()+pontoAbastecimentoEditavel.getTerra());
	
			} catch (Exception e) {
				FacesContext facesContext = FacesContext.getCurrentInstance();  
	    		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Erro ao calcular o índice editavel. ["
	    				+e.toString()+ " ]"));
	    		logger.error("Erro ao calcular o índice. ["
	    				+e.toString()+ " ]");
			}
		
		}
		
		logger.info("Fim do método calculaIndiceEditavel()");
	}
	
	 public void onRowEdit(RowEditEvent event) {
		 logger.info("Inicio do método onRowEdit()");
		 
		 try {
			
			 IndiceDTO indice = (IndiceDTO) event.getObject();
			 
			 pontoAbastecimentoService.atualizarIndices(indice);
			 
			} catch (Exception e) {
				FacesContext facesContext = FacesContext.getCurrentInstance();  
	    		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Erro ao atualizar o índice. ["
	    				+e.toString()+ " ]"));
	    		logger.error("Erro ao atualizar o índice. ["
	    				+e.toString()+ " ]");
			}
		 logger.info("Fim do método onRowEdit()");	 
	 }

	public boolean verificaSeExiste(String pontoAbastecimento, int codcidade) throws Exception{
		boolean existe = false;
		
		PontoAbastecimentoDTO pontoAbastecimentoProcurada = pontoAbastecimentoService.recuperaPontoAbastecimentoPorComunidade(pontoAbastecimento,codcidade);
		if(null != pontoAbastecimentoProcurada && pontoAbastecimentoProcurada.getComunidade().equalsIgnoreCase(pontoAbastecimento)){
			existe = true;
		}
		
		return existe;
	}
	
	public void limpaEditavel(){
		pontoAbastecimentoEditavel = new PontoAbastecimentoDTO();
	}
	
	public void editar(){
		
		 logger.info("Inicio do método editar()");	 
		
		boolean erro = false;
		String msg = "";
		
		if(campoEhValido( pontoAbastecimentoEditavel.getId(),pontoAbastecimentoEditavel.getComunidade(),pontoAbastecimentoEditavel.getPopulacao(),pontoAbastecimentoEditavel.getDistancia(),
				pontoAbastecimentoEditavel.getMomento(),pontoAbastecimentoEditavel.getCodcidade(),pontoAbastecimentoEditavel.getGeoreferenciamento(),pontoAbastecimentoEditavel.getVolume(),
				pontoAbastecimentoEditavel.getAsfalto(),pontoAbastecimentoEditavel.getTerra(),pontoAbastecimentoEditavel.getIndice(),pontoAbastecimentoEditavel.getApontador(),
				pontoAbastecimentoEditavel.getSubstituto(),pontoAbastecimentoEditavel.getStatus(),
				pontoAbastecimentoEditavel.getPopulacao5L(), pontoAbastecimentoEditavel.getPopulacaoDefault(), pontoAbastecimentoEditavel.getCodGcda()
				)){
			try{
				pontoAbastecimentoService.atualizaPontoAbastecimento(pontoAbastecimentoEditavel);
				
				UsuarioDTO usuarioNaSessao = (UsuarioDTO) Funcoes.getSession().getAttribute(Constantes.USUARIO_LOGADO);
				
				service.inserirInteracaoUsuario(Constantes.CADASTRO_PA,Constantes.ATUALIZARDADOS,null,new Date(),usuarioNaSessao.getId());
				
			}catch(Exception ex){
				erro = true;
				msg = "Não foi possível editar o registro.";
				logger.error(ex);
			}
		}
	
		if(!erro){
			limpaEditavel();
			pesquisar();
		}else{
			logger.error("Não foi possível editar o registro.");
			FacesContext.getCurrentInstance().addMessage("msgCadastrarPontoAbastecimento",
					new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null));
		}
		
		 logger.info("Fim do método editar()");	
	}
	
	public void enviaEditavel(PontoAbastecimentoDTO pontoAbastecimento){
		pontoAbastecimentoEditavel = new PontoAbastecimentoDTO(
				pontoAbastecimento.getId(),pontoAbastecimento.getComunidade(),pontoAbastecimento.getPopulacao(),pontoAbastecimento.getDistancia(),pontoAbastecimento.getMomento(),
				pontoAbastecimento.getCodcidade(),pontoAbastecimento.getGeoreferenciamento(),pontoAbastecimento.getVolume(),pontoAbastecimento.getAsfalto(),pontoAbastecimento.getTerra(),
				pontoAbastecimento.getIndice(),pontoAbastecimento.getApontador(),pontoAbastecimento.getSubstituto(),pontoAbastecimento.getStatus(),
				pontoAbastecimento.getPopulacao5L(), pontoAbastecimento.getPopulacaoDefault(), pontoAbastecimento.getCodGcda(), pontoAbastecimento.getLitrosDiario(),pontoAbastecimento.getCodIndice()
				);
	}
	
	public void pesquisar(){
		
		 logger.info("Inicio do método pesquisar()");	
		
		String msg = null;
		//Pesquisa PontoAbastecimento
		try{
			listaPesquisa = pontoAbastecimentoService.recuperaPontoAbastecimentosPorComunidade(pesquisaPontoAbastecimento,pesquisaStatusPontoAbastecimento,codcidadePesquisa);
			if(null == listaPesquisa){
				msg = "Não foi possível pesquisar o registro.";
	
			}else if(listaPesquisa.isEmpty()){
				msg = "Nenhum registro foi encontrado.";
			}
		
		}catch(Exception ex){
			logger.error(ex);
			msg = "Não foi possível pesquisar o registro.";
		}
		
		if(null != msg){
			logger.error(msg);
			FacesContext.getCurrentInstance().addMessage("msgCadastrarPontoAbastecimento",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null));
		}	
		
		 logger.info("Fim do método pesquisar()");
	}
	
	private boolean campoEhValido(
			int id, String comunidade, int populacao, double distancia, String momento,
			int codcidade, String georeferenciamento, double volume, double asfalto,
			double terra, double indice, String apontador, String substituto, String status, int populacao5L, int populacaoDefault, int codGcda){
		
		if(Funcoes.isNuloOuVazio(comunidade) || (Funcoes.isNuloOuVazio(populacao5L) &&
				Funcoes.isNuloOuVazio(populacaoDefault)) || Funcoes.isNuloOuVazio(codcidade)
				|| (Funcoes.isNuloOuVazio(asfalto) && Funcoes.isNuloOuVazio(terra)) 
				|| Funcoes.isNuloOuVazio(apontador)){
			return false;
		}
		
		return true;
	}
	
	public void limpaForm(){

		id = 0;
		comunidade = "";
		populacao = 0;
		distancia = 0;
		momento = "";
		codcidade = 0;
		georeferenciamento = "";
		volume = 0;
		asfalto = 0;
		terra = 0;
		indice = 0;
		apontador = "";
		substituto = "";
		status = "";
		populacao5L = 0;
		populacaoDefault = 0;
		codGcda = 0;
	}
	
	public void imprimirRelatorio() throws JRException,Exception{
		
		 logger.info("Inicio do método imprimirRelatorio()");
		
		boolean erro = false;
		String msg = "";
		
		try {
			
			List<PontoAbastecimentoDTO> lista = new ArrayList<PontoAbastecimentoDTO>();
			lista = pontoAbastecimentoService.recuperaTodosPontoAbastecimentos();
			
			String caminhoRelatorio = rpcProperties.buscaPropriedade("caminho.relatorios");
			String caminhoImpressao = rpcProperties.buscaPropriedade("caminho.impressao");
			
			// compilacao do JRXML
			JasperReport report = JasperCompileManager
					.compileReport(caminhoRelatorio+"relatorio_pa.jrxml");
									
			JasperPrint print = JasperFillManager.fillReport(report, null,
					new JRBeanCollectionDataSource(lista));
	
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat formatadorData = new SimpleDateFormat("yyyyMMddHHmmss");
			String timeStamp =	formatadorData.format(calendar.getTime()); 
			
			String arquivo = "RelatorioPA_"+timeStamp+".pdf";
			// exportacao do relatorio para outro formato, no caso PDF
			JasperExportManager.exportReportToPdfFile(print,
					caminhoImpressao+arquivo);

		} catch (Exception e) {
			erro = true;
			logger.error(e);
		}
		
		if(erro){
			msg = "Erro ao gerar relatório.";
			FacesContext.getCurrentInstance().addMessage("msgCadastrarMilitar",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null));
		}else{
			msg = "Relatório gerado com sucesso.";
			FacesContext.getCurrentInstance().addMessage("msgCadastrarMilitar",
					new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null));
		}
		
		 logger.info("Fim do método imprimirRelatorio()");
	}

	public void addIndiceEspecial(){
		
		logger.info("Inicio do método addIndiceEspecial()");

		if(indiceEspecial){
			
			
			try {
				
			IndiceDTO indice = new IndiceDTO();
			
			indice = pontoAbastecimentoService.recuperaIndicePorDescricao(Constantes.INDICE_ESPECIAL);
				
			setIndice(indice.getIndice());
			setMomento(indice.getDscIndice());
			setCodIndice(indice.getId());
			
			
			} catch (Exception e) {
				FacesContext facesContext = FacesContext.getCurrentInstance();  
	    		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Erro ao calcular o índice. ["
	    				+e.toString()+ " ]"));
	    		logger.error("Erro ao calcular o índice. ["
	    				+e.toString()+ " ]");
			}
			
			
		}else{
			
			calculaIndice();
		}
		
		logger.info("Fim do método addIndiceEspecial()");
	}
	
	
	public void addIndiceEspecialEditavel(){
		
		logger.info("Inicio do método addIndiceEspecialEditavel()");

		if(indiceEspecialEdit){
			
			
			try {
				
			IndiceDTO indice = new IndiceDTO();
			
			indice = pontoAbastecimentoService.recuperaIndicePorDescricao(Constantes.INDICE_ESPECIAL);
				
			pontoAbastecimentoEditavel.setIndice(indice.getIndice());
			pontoAbastecimentoEditavel.setCodIndice(indice.getId());
			pontoAbastecimentoEditavel.setMomento(indice.getDscIndice());
			
			
			} catch (Exception e) {
				FacesContext facesContext = FacesContext.getCurrentInstance();  
	    		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Erro ao calcular o índice. ["
	    				+e.toString()+ " ]"));
	    		logger.error("Erro ao calcular o índice. ["
	    				+e.toString()+ " ]");
			}
			
			
		}else{
			
			calculaIndiceEditavel();
		}
		
		logger.info("Fim do método addIndiceEspecialEditavel()");
	}
	
	
	/**
	 * Getters and Setters
	 */
	public Status[] getStatusEnum() {
		return Status.values();
	}

	public PontoAbastecimentoDTO getPontoAbastecimento() {
		return pontoAbastecimento;
	}

	public void setPontoAbastecimento(PontoAbastecimentoDTO pontoAbastecimento) {
		this.pontoAbastecimento = pontoAbastecimento;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<CidadeDTO> getListaCidade() {
		if(null == listaCidade){
			listaCidade = new ArrayList<CidadeDTO>();
		}
		
		return listaCidade;
	}

	public void setListaPontoAbastecimento(List<CidadeDTO> listaCidade) {
		this.listaCidade = listaCidade;
	}

	public String getPesquisaPontoAbastecimento() {
		return pesquisaPontoAbastecimento;
	}

	public void setPesquisaPontoAbastecimento(String pesquisaPontoAbastecimento) {
		this.pesquisaPontoAbastecimento = pesquisaPontoAbastecimento;
	}

	public String getPesquisaStatusPontoAbastecimento() {
		return pesquisaStatusPontoAbastecimento;
	}

	public void setPesquisaStatusPontoAbastecimento(String pesquisaStatusPontoAbastecimento) {
		this.pesquisaStatusPontoAbastecimento = pesquisaStatusPontoAbastecimento;
	}

	public List<PontoAbastecimentoDTO> getListaPesquisa() {
		return listaPesquisa;
	}

	public void setListaPesquisa(List<PontoAbastecimentoDTO> listaPesquisa) {
		this.listaPesquisa = listaPesquisa;
	}

	public PontoAbastecimentoDTO getPontoAbastecimentoEditavel() {
		return pontoAbastecimentoEditavel;
	}

	public void setPontoAbastecimentoEditavel(PontoAbastecimentoDTO pontoAbastecimentoEditavel) {
		this.pontoAbastecimentoEditavel = pontoAbastecimentoEditavel;
	}

	public PontoAbastecimentoService getPontoAbastecimentoService() {
		return pontoAbastecimentoService;
	}

	public void setPontoAbastecimentoService(PontoAbastecimentoService pontoAbastecimentoService) {
		this.pontoAbastecimentoService = pontoAbastecimentoService;
	}

	public String getComunidade() {
		return comunidade;
	}

	public void setComunidade(String comunidade) {
		this.comunidade = comunidade;
	}

	public double getDistancia() {
		return distancia;
	}

	public void setDistancia(double distancia) {
		this.distancia = distancia;
	}

	public String getMomento() {
		return momento;
	}

	public void setMomento(String momento) {
		this.momento = momento;
	}

	public int getCodcidade() {
		return codcidade;
	}

	public void setCodcidade(int codcidade) {
		this.codcidade = codcidade;
	}

	public String getGeoreferenciamento() {
		return georeferenciamento;
	}

	public void setGeoreferenciamento(String georeferenciamento) {
		this.georeferenciamento = georeferenciamento;
	}

	public double getVolume() {
		return volume;
	}

	public void setVolume(double volume) {
		this.volume = volume;
	}

	public double getAsfalto() {
		return asfalto;
	}

	public void setAsfalto(double asfalto) {
		this.asfalto = asfalto;
	}

	public double getTerra() {
		return terra;
	}

	public void setTerra(double terra) {
		this.terra = terra;
	}

	public double getIndice() {
		return indice;
	}

	public void setIndice(double indice) {
		this.indice = indice;
	}

	public String getApontador() {
		return apontador;
	}

	public void setApontador(String apontador) {
		this.apontador = apontador;
	}

	public String getSubstituto() {
		return substituto;
	}

	public void setSubstituto(String substituto) {
		this.substituto = substituto;
	}

	public int getPopulacao() {
		return populacao;
	}

	public void setPopulacao(int populacao) {
		this.populacao = populacao;
	}

	public CidadeService getCidadeService() {
		return cidadeService;
	}

	public void setCidadeService(CidadeService cidadeService) {
		this.cidadeService = cidadeService;
	}

	public void setListaCidade(List<CidadeDTO> listaCidade) {
		this.listaCidade = listaCidade;
	}

	public int getCodGcda() {
		return codGcda;
	}

	public void setCodGcda(int codGcda) {
		this.codGcda = codGcda;
	}

	public int getLitrosDiario() {
		return litrosDiario;
	}

	public void setLitrosDiario(int litrosDiario) {
		this.litrosDiario = litrosDiario;
	}

	public int getPopulacaoDefault() {
		return populacaoDefault;
	}

	public void setPopulacaoDefault(int populacaoDefault) {
		this.populacaoDefault = populacaoDefault;
	}

	public int getPopulacao5L() {
		return populacao5L;
	}

	public void setPopulacao5L(int populacao5l) {
		populacao5L = populacao5l;
	}

	public int getCodIndice() {
		return codIndice;
	}

	public void setCodIndice(int codIndice) {
		this.codIndice = codIndice;
	}

	public List<IndiceDTO> getListaIndice() {
		return listaIndice;
	}

	public void setListaIndice(List<IndiceDTO> listaIndice) {
		this.listaIndice = listaIndice;
	}

	public String getMsg5l() {
		return msg5l;
	}

	public void setMsg5l(String msg5l) {
		this.msg5l = msg5l;
	}

	public String getMsg20l() {
		return msg20l;
	}

	public void setMsg20l(String msg20l) {
		this.msg20l = msg20l;
	}

	public int getCodcidadePesquisa() {
		return codcidadePesquisa;
	}

	public void setCodcidadePesquisa(int codcidadePesquisa) {
		this.codcidadePesquisa = codcidadePesquisa;
	}

	public PagamentoPipeiroSevice getService() {
		return service;
	}

	public void setService(PagamentoPipeiroSevice service) {
		this.service = service;
	}

	public boolean isIndiceEspecial() {
		return indiceEspecial;
	}

	public void setIndiceEspecial(boolean indiceEspecial) {
		this.indiceEspecial = indiceEspecial;
	}

	public boolean isIndiceEspecialEdit() {
		return indiceEspecialEdit;
	}

	public void setIndiceEspecialEdit(boolean indiceEspecialEdit) {
		this.indiceEspecialEdit = indiceEspecialEdit;
	}
	
}
