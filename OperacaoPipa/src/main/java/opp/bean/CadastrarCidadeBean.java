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
import opp.dto.PontoColetaDTO;
import opp.dto.UsuarioDTO;
import opp.entity.Estados;
import opp.entity.Status;
import opp.sevice.CidadeService;
import opp.sevice.PagamentoPipeiroSevice;
import opp.sevice.PontoColetaService;
import opp.util.Constantes;
import opp.util.Funcoes;
import opp.util.Propriedades;

import org.apache.log4j.Logger;

@ViewScoped
@ManagedBean(name = "cadastrarCidadeBean")
@SessionScoped
public class CadastrarCidadeBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3534018826630634705L;
	private static final Logger logger = Logger.getLogger(CadastrarCidadeBean.class);
	
	private static Propriedades rpcProperties = Propriedades.getInstance();
	
	
	/* Atributos de Cadastro */
	private CidadeDTO cidade;
	private int codPontoColeta;
	private String cidadeNome;   
	private String uf;       
	private String geolocalizacao;
	private int numHabitantes;
	private String email;    
	private int carr_max;     
	private String coordSopMunicipal;
	private String foneSopMunicipal; 
	private int numPipeiros; 
	private String status;

	private List<PontoColetaDTO> listaPC;
	
	/* Atributos de Pesquisa */
	private String pesquisaCidade;
	private String pesquisaStatusCidade;
	private List<CidadeDTO> listaPesquisa;
	
	/* Atributos de Edição */
	private CidadeDTO cidadeEditavel;
	
	@ManagedProperty(value = "#{cidadeService}")
	private CidadeService cidadeService;
	@ManagedProperty(value = "#{pontoColetaService}")
	private PontoColetaService pontoColetaService;
	@ManagedProperty("#{pagamentoPipeiroSevice}")
	private PagamentoPipeiroSevice service;
	
	
	@PostConstruct
	public void loadPage() {
		
		logger.info("Inicio da tela de Cadastro de Cidade.");
		
		cidadeService = new CidadeService();
		pontoColetaService = new PontoColetaService();
		service = new PagamentoPipeiroSevice();
		
		try {
			listaPC = pontoColetaService.recuperaTodosPontosColeta();
		} catch (Exception ex) {
			logger.error(ex);
			FacesContext.getCurrentInstance().addMessage(    
	                "msgCadastrarCidade",    
	                new FacesMessage(FacesMessage.SEVERITY_ERROR,    
	                        "Erro ao recuperar Pontos de Coleta.", null));
		}
		cidadeEditavel = new CidadeDTO();
		
	}
	
	/**
	 * Ações
	 */
	public void salvar(){
		
		logger.info("Inicio do metodo salvar().");
		
		
		if(campoEhValido(codPontoColeta,cidadeNome,uf,geolocalizacao,numHabitantes,
				email,carr_max,coordSopMunicipal,foneSopMunicipal,numPipeiros,status)){
			
			
			setStatus("ATIVO");
			
			//Monta objeto
			cidade = new CidadeDTO(
					codPontoColeta, cidadeNome, uf,
					geolocalizacao, numHabitantes, email,
					carr_max, coordSopMunicipal, foneSopMunicipal,
					numPipeiros, status
					);
			
			//Verifica se Existe
			boolean existe = false;
			
			try {
				existe = verificaSeExiste(cidade.getCidade().trim());
			} catch (Exception e) {
				logger.error(e);
				FacesContext.getCurrentInstance().addMessage(    
		                "msgCadastrarCidade",    
		                new FacesMessage(FacesMessage.SEVERITY_ERROR,    
		                        "Não foi possível salvar o registro.", null));
			}
			
			if(!existe){
				//Insere Cidade
				boolean erro = false;
				
				try{
					cidadeService.insereCidade(cidade);
					
					//Salvar dados em interacao_usuario
					UsuarioDTO usuarioNaSessao = (UsuarioDTO) Funcoes.getSession().getAttribute(Constantes.USUARIO_LOGADO);
					
					service.inserirInteracaoUsuario(Constantes.CADASTRO_CIDADE,Constantes.INSERIRDADOS,null,new Date(),usuarioNaSessao.getId());
					
				}catch(Exception ex){
					logger.error(ex);
					erro = true;
				}
				
				if(erro){
					logger.error("Não foi possível salvar o registro.");
					FacesContext.getCurrentInstance().addMessage(    
		                "msgCadastrarCidade",    
		                new FacesMessage(FacesMessage.SEVERITY_ERROR,    
		                        "Não foi possível salvar o registro.", null));
				}else{
					logger.info("Registro salvo com sucesso.");
					FacesContext.getCurrentInstance().addMessage(    
		                "msgCadastrarCidade",    
		                new FacesMessage(FacesMessage.SEVERITY_INFO,    
		                        "Registro salvo com sucesso.", null));
					limpaForm();
				}
			}else{
				
				logger.info("Registro já existe.");
				
				FacesContext.getCurrentInstance().addMessage(    
		                "msgCadastrarCidade",    
		                new FacesMessage(FacesMessage.SEVERITY_ERROR,    
		                        "Registro já existe.", null));
			}
		}else{
			
			StringBuffer msgErro = new StringBuffer();
			
			msgErro.append("Campo(s) obrigatório(s): ");
			
			if(Funcoes.isNuloOuVazio(codPontoColeta)){
				msgErro.append("- Ponto de Coleta ");
			}
			if(Funcoes.isNuloOuVazio(cidadeNome)){
				msgErro.append("- Nome da cidade ");
			}
			if(Funcoes.isNuloOuVazio(uf)){
				msgErro.append("- UF ");
			}
			if(carr_max == 0){
				msgErro.append("- Carradas diárias ");
			}

			FacesContext.getCurrentInstance().addMessage(    
					"msgCadastrarCidade",    
					new FacesMessage(FacesMessage.SEVERITY_ERROR,    
							msgErro.toString(), null));
		}
		
		logger.info("Fim do metodo salvar().");
	}

	public boolean verificaSeExiste(String cidade) throws Exception{
		boolean existe = false;
		
		CidadeDTO cidadeProcurada = cidadeService.recuperaCidadePorNome(cidade);
		if(null != cidadeProcurada && cidadeProcurada.getCidade().equalsIgnoreCase(cidade)){
			existe = true;
		}
		
		return existe;
	}
	
	public void limpaEditavel(){
		cidadeEditavel = new CidadeDTO();
	}
	
	public void editar(){
		
		logger.info("Inicio do metodo editar().");
		
		boolean erro = false;
		String msg = "";
		
		if(campoEhValido(
				cidadeEditavel.getCodPontoColeta(), cidadeEditavel.getCidade(), cidadeEditavel.getUf(),
				cidadeEditavel.getGeolocalizacao(), cidadeEditavel.getNumHabitantes(), cidadeEditavel.getEmail(),
				cidadeEditavel.getCarr_max(), cidadeEditavel.getCoordSopMunicipal(), cidadeEditavel.getFoneSopMunicipal(),
				cidadeEditavel.getNumPipeiros(), cidadeEditavel.getStatus())){
			try{
				cidadeService.atualizaCidade(cidadeEditavel);
				
				UsuarioDTO usuarioNaSessao = (UsuarioDTO) Funcoes.getSession().getAttribute(Constantes.USUARIO_LOGADO);
				
				service.inserirInteracaoUsuario(Constantes.CADASTRO_CIDADE,Constantes.ATUALIZARDADOS,null,new Date(),usuarioNaSessao.getId());
				
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
			FacesContext.getCurrentInstance().addMessage("msgCadastrarCidade",
					new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null));
		}
		
		logger.info("Fim do metodo editar().");
	}
	
	public void enviaEditavel(CidadeDTO cidade){
		cidadeEditavel = new CidadeDTO(
				cidade.getId(), cidade.getCodPontoColeta(), cidade.getCidade(), cidade.getUf(),
				cidade.getGeolocalizacao(), cidade.getNumHabitantes(), cidade.getEmail(),
				cidade.getCarr_max(), cidade.getCoordSopMunicipal(), cidade.getFoneSopMunicipal(),
				cidade.getNumPipeiros(), cidade.getStatus()
				);
	}
	
	public void pesquisar(){
		
		logger.info("Inicio do metodo pesquisar().");
		
		String msg = null;
		//Pesquisa Cidade
		try{
			listaPesquisa = cidadeService.recuperaCidadesPorNome(pesquisaCidade,pesquisaStatusCidade);
		
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
			FacesContext.getCurrentInstance().addMessage("msgCadastrarCidade",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null));
		}	
		
		logger.info("Fim do metodo pesquisar().");
	}
	
	private boolean campoEhValido(int codPontoColeta, String cidadeNome,String uf,String geolocalizacao,
			int numHabitantes,String email,int carr_max,String coordSopMunicipal,
			String foneSopMunicipal,int numPipeiros,String status){
		
		
		if(Funcoes.isNuloOuVazio(codPontoColeta) || Funcoes.isNuloOuVazio(cidadeNome) ||
				Funcoes.isNuloOuVazio(uf) || Funcoes.isNuloOuVazioZero(carr_max)){
			return false;
		}
		
		return true;
	}
	
	public void limpaForm(){
		codPontoColeta = 0;
		cidadeNome = "";
		uf = "";
		geolocalizacao = "";
		numHabitantes = 0;
		email = "";
		carr_max = 0;
		coordSopMunicipal = "";
		foneSopMunicipal = "";
		numPipeiros = 0;
		status = "";
     
	}
	
	public void imprimirRelatorio() throws JRException,Exception{
		
		logger.info("Inicio do metodo imprimirRelatorio().");
		
		boolean erro = false;
		String msg = "";
		
		try {
			
			
			List<CidadeDTO> lista = new ArrayList<CidadeDTO>();
			lista = cidadeService.recuperaTodasCidades();
			
			String caminhoRelatorio = rpcProperties.buscaPropriedade("caminho.relatorios");
			String caminhoImpressao = rpcProperties.buscaPropriedade("caminho.impressao");
			
			// compilacao do JRXML
			JasperReport report = JasperCompileManager
					.compileReport(caminhoRelatorio+"relatorio_cidade.jrxml");
	
			JasperPrint print = JasperFillManager.fillReport(report, null,
					new JRBeanCollectionDataSource(lista));
	
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat formatadorData = new SimpleDateFormat("yyyyMMddHHmmss");
			String timeStamp =	formatadorData.format(calendar.getTime()); 
			
			String arquivo = "RelatorioCidades_"+timeStamp+".pdf";
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
		
		logger.info("Fim do metodo imprimirRelatorio().");
	}
	
	
	
	
	
	
	
	
	/**
	 * Getters and Setters
	 */
	public Estados[] getEstadosEnum() {
		return Estados.values();
	}

	public Status[] getStatusEnum() {
		return Status.values();
	}
	
	public CidadeDTO getCidade() {
		return cidade;
	}

	public void setCidade(CidadeDTO cidade) {
		this.cidade = cidade;
	}

	public int getCodPontoColeta() {
		return codPontoColeta;
	}

	public void setCodPontoColeta(int codPontoColeta) {
		this.codPontoColeta = codPontoColeta;
	}

	public String getCidadeNome() {
		return cidadeNome;
	}

	public void setCidadeNome(String cidadeNome) {
		this.cidadeNome = cidadeNome;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getGeolocalizacao() {
		return geolocalizacao;
	}

	public void setGeolocalizacao(String geolocalizacao) {
		this.geolocalizacao = geolocalizacao;
	}

	public int getNumHabitantes() {
		return numHabitantes;
	}

	public void setNumHabitantes(int numHabitantes) {
		this.numHabitantes = numHabitantes;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getCarr_max() {
		return carr_max;
	}

	public void setCarr_max(int carr_max) {
		this.carr_max = carr_max;
	}

	public String getCoordSopMunicipal() {
		return coordSopMunicipal;
	}

	public void setCoordSopMunicipal(String coordSopMunicipal) {
		this.coordSopMunicipal = coordSopMunicipal;
	}

	public String getFoneSopMunicipal() {
		return foneSopMunicipal;
	}

	public void setFoneSopMunicipal(String foneSopMunicipal) {
		this.foneSopMunicipal = foneSopMunicipal;
	}

	public int getNumPipeiros() {
		return numPipeiros;
	}

	public void setNumPipeiros(int numPipeiros) {
		this.numPipeiros = numPipeiros;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPesquisaCidade() {
		return pesquisaCidade;
	}

	public void setPesquisaCidade(String pesquisaCidade) {
		this.pesquisaCidade = pesquisaCidade;
	}

	public String getPesquisaStatusCidade() {
		return pesquisaStatusCidade;
	}

	public void setPesquisaStatusCidade(String pesquisaStatusCidade) {
		this.pesquisaStatusCidade = pesquisaStatusCidade;
	}

	public List<CidadeDTO> getListaPesquisa() {
		return listaPesquisa;
	}

	public void setListaPesquisa(List<CidadeDTO> listaPesquisa) {
		this.listaPesquisa = listaPesquisa;
	}

	public CidadeDTO getCidadeEditavel() {
		return cidadeEditavel;
	}

	public void setCidadeEditavel(CidadeDTO cidadeEditavel) {
		this.cidadeEditavel = cidadeEditavel;
	}

	public CidadeService getCidadeService() {
		return cidadeService;
	}

	public void setCidadeService(CidadeService cidadeService) {
		this.cidadeService = cidadeService;
	}

	public List<PontoColetaDTO> getListaPC() {
		if(null == listaPC){
			listaPC = new ArrayList<PontoColetaDTO>();
		}
		
		return listaPC;
	}

	public void setListaPC(List<PontoColetaDTO> listaPC) {
		this.listaPC = listaPC;
	}

	public PontoColetaService getPontoColetaService() {
		return pontoColetaService;
	}

	public void setPontoColetaService(PontoColetaService pontoColetaService) {
		this.pontoColetaService = pontoColetaService;
	}

	public PagamentoPipeiroSevice getService() {
		return service;
	}

	public void setService(PagamentoPipeiroSevice service) {
		this.service = service;
	}
	
	
}
