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
import opp.dto.PontoColetaDTO;
import opp.dto.UsuarioDTO;
import opp.entity.Status;
import opp.sevice.PagamentoPipeiroSevice;
import opp.sevice.PontoColetaService;
import opp.util.Constantes;
import opp.util.Funcoes;
import opp.util.Propriedades;

import org.apache.log4j.Logger;

@ViewScoped
@ManagedBean(name = "cadastrarPCBean")
@SessionScoped
public class CadastrarPCBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3534018826630634705L;
	private static final Logger logger = Logger.getLogger(CadastrarPCBean.class);
	
	private static Propriedades rpcProperties = Propriedades.getInstance();
	
	/* Atributos de Cadastro */
	private PontoColetaDTO PC;
	private String pontoColeta;
	private String geoLocalizacao;
	private String status;
	private int codGcda;
	
	/* Atributos de Pesquisa */
	private String pesquisaPC;
	private String pesquisaStatusPC;
	private List<PontoColetaDTO> listaPesquisa;
	
	/* Atributos de Edição */
	private PontoColetaDTO pcEditavel;
	
	/* Service */
	@ManagedProperty(value = "#{pontoColetaService}")
	private PontoColetaService pontoColetaService;
	@ManagedProperty("#{pagamentoPipeiroSevice}")
	private PagamentoPipeiroSevice service;
	
	@PostConstruct
	public void loadPage() {
		
		logger.info("Inicio da tela de Cadastro de Ponto de coleta.");
		
		pontoColetaService = new PontoColetaService();
		service = new PagamentoPipeiroSevice();

	}
	
	/**
	 * Ações
	 */
	public void salvarPC(){
		
		logger.info("Inicio do método salvar()");
		
		status = Status.ATIVO.toString();
		
		if(campoEhValido(pontoColeta,geoLocalizacao,status,codGcda)){
			
			setStatus("ATIVO");
			
			//Monta ponto
			PC = new PontoColetaDTO(pontoColeta,geoLocalizacao,status,codGcda);
			
			//Verifica se Existe
			boolean existe = false;
			
			try {
				existe = verificaSeExiste(PC.getPontoColeta().trim());
			} catch (Exception e) {
				logger.error(e);
				FacesContext.getCurrentInstance().addMessage(    
		                "msgCadastrarPC",    
		                new FacesMessage(FacesMessage.SEVERITY_ERROR,    
		                        "Não foi possível salvar o registro.", null));
			}
			
			if(!existe){
				//Insere Cidade
				boolean erro = false;
				
				try{
					pontoColetaService.inserePontoColeta(PC);
					
					//Salvar dados em interacao_usuario
					UsuarioDTO usuarioNaSessao = (UsuarioDTO) Funcoes.getSession().getAttribute(Constantes.USUARIO_LOGADO);
					
					service.inserirInteracaoUsuario(Constantes.CADASTRO_PC,Constantes.INSERIRDADOS,null,new Date(),usuarioNaSessao.getId());
					
				}catch(Exception ex){
					logger.error(ex);
					erro = true;
				}
				
				if(erro){
					logger.error("Não foi possível salvar o registro.");
					FacesContext.getCurrentInstance().addMessage(    
		                "msgCadastrarPC",    
		                new FacesMessage(FacesMessage.SEVERITY_ERROR,    
		                        "Não foi possível salvar o registro.", null));
				}else{
					logger.info("Registro salvo com sucesso.");
					FacesContext.getCurrentInstance().addMessage(    
		                "msgCadastrarPC",    
		                new FacesMessage(FacesMessage.SEVERITY_INFO,    
		                        "Registro salvo com sucesso.", null));
					limpaForm();
				}
			}else{
				
				FacesContext.getCurrentInstance().addMessage(    
		                "msgCadastrarPC",    
		                new FacesMessage(FacesMessage.SEVERITY_ERROR,    
		                        "Registro já existe.", null));
			}
		}else{
			
			StringBuffer msgErro = new StringBuffer();
			
			msgErro.append("Campo(s) obrigatório(s): ");
			
			if(Funcoes.isNuloOuVazio(pontoColeta)){
				msgErro.append("- Nome do Ponto de Coleta ");
			}

			FacesContext.getCurrentInstance().addMessage(    
					"msgCadastrarCidade",    
					new FacesMessage(FacesMessage.SEVERITY_ERROR,    
							msgErro.toString(), null));

		}
		
		logger.info("Fim do método salvar()");

	}

	public boolean verificaSeExiste(String pontoColeta) throws Exception{
		boolean existe = false;
		
		PontoColetaDTO pcProcurado = pontoColetaService.recuperaPontoColetaPorNome(pontoColeta);
		if(null != pcProcurado && pcProcurado.getPontoColeta().equalsIgnoreCase(pontoColeta)){
			existe = true;
		}
		
		return existe;
	}
	
	public void limpaEditavel(){
		pcEditavel = null;
	}
	
	public void editarPC(){
		
		logger.info("Inicio do método editarPC()");
		
		boolean erro = false;
		String msg = "";
		
		if(campoEhValido(pcEditavel.getPontoColeta(),pcEditavel.getGeoLocalizacao(),pcEditavel.getStatus(),pcEditavel.getCodGcda())){
			try{
				pontoColetaService.atualizaPontoColeta(pcEditavel);
				
				UsuarioDTO usuarioNaSessao = (UsuarioDTO) Funcoes.getSession().getAttribute(Constantes.USUARIO_LOGADO);
				
				service.inserirInteracaoUsuario(Constantes.CADASTRO_PC,Constantes.ATUALIZARDADOS,null,new Date(),usuarioNaSessao.getId());
			}catch(Exception ex){
				erro = true;
				msg = "Não foi possível editar o registro.";
				logger.error(ex);
			}
		}
	
		if(!erro){
			limpaEditavel();
			pesquisarPC();
		}else{
			logger.error("Não foi possível editar o registro.");
			FacesContext.getCurrentInstance().addMessage("msgCadastrarPC",
					new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null));
		}
		
		logger.info("Fim do método editarPC()");
	}
	
	public void enviaPCEditavel(PontoColetaDTO pc){
		pcEditavel = new PontoColetaDTO(pc.getId(),pc.getPontoColeta(),pc.getGeoLocalizacao(),pc.getStatus(),pc.getCodGcda());
	}
	
	public void pesquisarPC(){
		
		logger.info("Inicio do método pesquisarPC()");
		
		String msg = null;
		//Pesquisa Cidade
		try{
			listaPesquisa = pontoColetaService.recuperaPontosColetaPorNome(pesquisaPC,pesquisaStatusPC);
		
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
			FacesContext.getCurrentInstance().addMessage("msgCadastrarPC",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null));
		}	
		
		logger.info("Fim do método pesquisarPC()");	
	}
	
	private boolean campoEhValido(String nomePC,String geoLocalizacao,String status, int codGcda){
		
		if(Funcoes.isNuloOuVazio(nomePC)){
			return false;
		}
		
		return true;
	}
	
	public void limpaForm(){
		pontoColeta = "";       
		geoLocalizacao = "";
		status = "";       
		codGcda = 0;
	}
	
	public void imprimirRelatorio() throws JRException,Exception{
		
		logger.info("Inicio do método imprimirRelatorio()");
		
		boolean erro = false;
		String msg = "";
		
		try {
			
			List<PontoColetaDTO> lista = new ArrayList<PontoColetaDTO>();
			lista = pontoColetaService.recuperaTodosPontosColeta();
			
			String caminhoRelatorio = rpcProperties.buscaPropriedade("caminho.relatorios");
			String caminhoImpressao = rpcProperties.buscaPropriedade("caminho.impressao");
			
			// compilacao do JRXML
			JasperReport report = JasperCompileManager
					.compileReport(caminhoRelatorio+"relatorio_pc.jrxml");
									
			JasperPrint print = JasperFillManager.fillReport(report, null,
					new JRBeanCollectionDataSource(lista));
	
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat formatadorData = new SimpleDateFormat("yyyyMMddHHmmss");
			String timeStamp =	formatadorData.format(calendar.getTime()); 
			
			String arquivo = "RelatorioPC_"+timeStamp+".pdf";
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

	
	/**
	 * Getters and Setters
	 */
	public PontoColetaDTO getPC() {
		return PC;
	}

	public void setPC(PontoColetaDTO pontoColeta) {
		this.PC = pontoColeta;
	}

	public PontoColetaService getPontoColetaService() {
		return pontoColetaService;
	}

	public void setPontoColetaService(PontoColetaService pontoColetaService) {
		this.pontoColetaService = pontoColetaService;
	}

	public String getPontoColeta() {
		return pontoColeta;
	}

	public void setPontoColeta(String pontoColeta) {
		this.pontoColeta = pontoColeta;
	}

	public String getGeoLocalizacao() {
		return geoLocalizacao;
	}

	public void setGeoLocalizacao(String geoLocalizacao) {
		this.geoLocalizacao = geoLocalizacao;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPesquisaPC() {
		return pesquisaPC;
	}

	public void setPesquisaPC(String pesquisaPC) {
		this.pesquisaPC = pesquisaPC;
	}

	public List<PontoColetaDTO> getListaPesquisa() {
		if(null == listaPesquisa){
			listaPesquisa = new ArrayList<PontoColetaDTO>();
		}
		return listaPesquisa;
	}

	public void setListaPesquisa(List<PontoColetaDTO> listaPesquisa) {
		this.listaPesquisa = listaPesquisa;
	}

	public PontoColetaDTO getPcEditavel() {
		return pcEditavel;
	}

	public void setPcEditavel(PontoColetaDTO pcEditavel) {
		this.pcEditavel = pcEditavel;
	}

	public Status[] getStatusEnum() {
		return Status.values();
	}

	public String getPesquisaStatusPC() {
		return pesquisaStatusPC;
	}

	public void setPesquisaStatusPC(String pesquisaStatusPC) {
		this.pesquisaStatusPC = pesquisaStatusPC;
	}

	public int getCodGcda() {
		return codGcda;
	}

	public void setCodGcda(int codGcda) {
		this.codGcda = codGcda;
	}

	public PagamentoPipeiroSevice getService() {
		return service;
	}

	public void setService(PagamentoPipeiroSevice service) {
		this.service = service;
	}
}
