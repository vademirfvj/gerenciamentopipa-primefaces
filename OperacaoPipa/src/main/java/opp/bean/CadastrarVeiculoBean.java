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
import opp.dto.UsuarioDTO;
import opp.dto.VeiculoDTO;
import opp.entity.Status;
import opp.sevice.PagamentoPipeiroSevice;
import opp.sevice.VeiculoService;
import opp.util.Constantes;
import opp.util.Funcoes;
import opp.util.Propriedades;

import org.apache.log4j.Logger;

@ViewScoped
@ManagedBean(name = "cadastrarVeiculoBean")
@SessionScoped
public class CadastrarVeiculoBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4148468644693922306L;

	private static final Logger logger = Logger.getLogger(CadastrarVeiculoBean.class);
	
	private static Propriedades rpcProperties = Propriedades.getInstance();

	
	/* Atributos de Cadastro */
	private VeiculoDTO veiculo;
	private int id;
	private String placa;
	private String renavam;
	private double capacidadePipa;
	private String marcaModelo;
	private String status;

	private List<VeiculoDTO> listaVeiculo;
	
	/* Atributos de Pesquisa */
	private String pesquisaVeiculo;
	private String pesquisaStatusVeiculo;
	private List<VeiculoDTO> listaPesquisa;
	
	/* Atributos de Edição */
	private VeiculoDTO veiculoEditavel;
	
	@ManagedProperty(value = "#{veiculoService}")
	private VeiculoService veiculoService;
	@ManagedProperty("#{pagamentoPipeiroSevice}")
	private PagamentoPipeiroSevice service;
	
	@PostConstruct
	public void loadPage() {
		
		logger.info("Inicio da tela de Cadastro de Veiculo.");
		
		veiculoService = new VeiculoService();
		veiculoEditavel = new VeiculoDTO();
		service = new PagamentoPipeiroSevice();

	}
	
	/**
	 * Ações
	 */
	public void salvar(){
		
		logger.info("Inicio do método salvar()");

		if(campoEhValido(id,placa,renavam,capacidadePipa,marcaModelo,status)){
			
			setStatus("ATIVO");
			
			//Monta objeto
			veiculo = new VeiculoDTO(
					placa,renavam,capacidadePipa,marcaModelo,status
					);
			
			//Verifica se Existe
			boolean existe = false;
			
			try {
				existe = verificaSeExiste(veiculo.getPlaca().trim());
			} catch (Exception e) {
				logger.error(e);
				FacesContext.getCurrentInstance().addMessage(    
		                "msgCadastrarVeiculo",    
		                new FacesMessage(FacesMessage.SEVERITY_ERROR,    
		                        "Não foi possível salvar o registro.", null));
			}
			
			if(!existe){
				//Insere Veiculo
				boolean erro = false;
				
				try{
					veiculoService.insereVeiculo(veiculo);
					
					UsuarioDTO usuarioNaSessao = (UsuarioDTO) Funcoes.getSession().getAttribute(Constantes.USUARIO_LOGADO);
					
					service.inserirInteracaoUsuario(Constantes.CADASTRO_VEICULO,Constantes.INSERIRDADOS,null,new Date(),usuarioNaSessao.getId());
					
				}catch(Exception ex){
					logger.error(ex);
					erro = true;
				}
				
				if(erro){
					logger.error("Não foi possível salvar o registro.");
					FacesContext.getCurrentInstance().addMessage(    
		                "msgCadastrarVeiculo",    
		                new FacesMessage(FacesMessage.SEVERITY_ERROR,    
		                        "Não foi possível salvar o registro.", null));
				}else{
					logger.info("Registro salvo com sucesso.");
					FacesContext.getCurrentInstance().addMessage(    
		                "msgCadastrarVeiculo",    
		                new FacesMessage(FacesMessage.SEVERITY_INFO,    
		                        "Registro salvo com sucesso.", null));
					limpaForm();
				}
			}else{
				
				FacesContext.getCurrentInstance().addMessage(    
		                "msgCadastrarVeiculo",    
		                new FacesMessage(FacesMessage.SEVERITY_ERROR,    
		                        "Registro já existe.", null));
			}
		}else{
			
			StringBuffer msgErro = new StringBuffer();
			
			msgErro.append("Campo(s) obrigatório(s): ");
			
			if(Funcoes.isNuloOuVazio(placa)){
				msgErro.append("- Placa ");
			}
			if(Funcoes.isNuloOuVazio(capacidadePipa)){
				msgErro.append("- Capacidade da Pipa ");
			}

			FacesContext.getCurrentInstance().addMessage(    
					"msgCadastrarCidade",    
					new FacesMessage(FacesMessage.SEVERITY_ERROR,    
							msgErro.toString(), null));

		}
		
		logger.info("Fim do método salvar()");

	}

	public boolean verificaSeExiste(String veiculo) throws Exception{
		boolean existe = false;
		
		VeiculoDTO veiculoProcurada = veiculoService.recuperaVeiculoPorPlaca(veiculo);
		if(null != veiculoProcurada && veiculoProcurada.getPlaca().equalsIgnoreCase(veiculo)){
			existe = true;
		}
		
		return existe;
	}
	
	public void limpaEditavel(){
		veiculoEditavel = new VeiculoDTO();
	}
	
	public void editar(){
		
		logger.info("Inicio do método editar()");
		
		
		boolean erro = false;
		String msg = "";
		
		if(campoEhValido( veiculoEditavel.getId(),veiculoEditavel.getPlaca(),
				veiculoEditavel.getRenavam(),veiculoEditavel.getCapacidadePipa(),
				veiculoEditavel.getMarcaModelo(),veiculoEditavel.getStatus())){
			try{
				veiculoService.atualizaVeiculo(veiculoEditavel);
				
				UsuarioDTO usuarioNaSessao = (UsuarioDTO) Funcoes.getSession().getAttribute(Constantes.USUARIO_LOGADO);
				
				service.inserirInteracaoUsuario(Constantes.CADASTRO_VEICULO,Constantes.ATUALIZARDADOS,null,new Date(),usuarioNaSessao.getId());
				
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
			FacesContext.getCurrentInstance().addMessage("msgCadastrarVeiculo",
					new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null));
		}
		
		logger.info("Fim do método editar()");	
	}
	
	public void enviaEditavel(VeiculoDTO veiculo){
		veiculoEditavel = new VeiculoDTO(
				veiculo.getId(), veiculo.getPlaca(),
				veiculo.getRenavam(),veiculo.getCapacidadePipa(),
				veiculo.getMarcaModelo(),veiculo.getStatus()
				);
	}
	
	public void pesquisar(){
		logger.info("Inicio do método pesquisar()");
		String msg = null;
		//Pesquisa Veiculo
		try{
			listaPesquisa = veiculoService.recuperaVeiculosPorPlaca(pesquisaVeiculo,pesquisaStatusVeiculo);
		
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
			FacesContext.getCurrentInstance().addMessage("msgCadastrarVeiculo",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null));
		}
		
		logger.info("Fim do método pesquisar()");	
	}
	
	private boolean campoEhValido(int id, String placa,String renavam,
			double capacidadePipa,String marcaModelo,String status){
		
		if(Funcoes.isNuloOuVazio(placa) || Funcoes.isNuloOuVazio(capacidadePipa)){
			return false;
		}
		
		return true;
	}
	
	public void limpaForm(){

		id = 0;
		placa="";
		renavam="";
		capacidadePipa=0;
		marcaModelo="";
		status = "";
		
	}
	
	public void imprimirRelatorio() throws JRException,Exception{
		logger.info("Inicio do método imprimirRelatorio()");
		boolean erro = false;
		String msg = "";
		
		try {
			
			
			List<VeiculoDTO> lista = new ArrayList<VeiculoDTO>();
			lista = veiculoService.recuperaTodosVeiculos();
			
			
			String caminhoRelatorio = rpcProperties.buscaPropriedade("caminho.relatorios");
			String caminhoImpressao = rpcProperties.buscaPropriedade("caminho.impressao");
			
			// compilacao do JRXML
			JasperReport report = JasperCompileManager
					.compileReport(caminhoRelatorio+"relatorio_veiculo.jrxml");
									
			JasperPrint print = JasperFillManager.fillReport(report, null,
					new JRBeanCollectionDataSource(lista));
	
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat formatadorData = new SimpleDateFormat("yyyyMMddHHmmss");
			String timeStamp =	formatadorData.format(calendar.getTime()); 
			
			String arquivo = "RelatorioVeiculos_"+timeStamp+".pdf";
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
	public Status[] getStatusEnum() {
		return Status.values();
	}

	public VeiculoDTO getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(VeiculoDTO veiculo) {
		this.veiculo = veiculo;
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

	public List<VeiculoDTO> getListaVeiculo() {
		if(null == listaVeiculo){
			listaVeiculo = new ArrayList<VeiculoDTO>();
		}
		
		return listaVeiculo;
	}

	public void setListaVeiculo(List<VeiculoDTO> listaVeiculo) {
		this.listaVeiculo = listaVeiculo;
	}

	public String getPesquisaVeiculo() {
		return pesquisaVeiculo;
	}

	public void setPesquisaVeiculo(String pesquisaVeiculo) {
		this.pesquisaVeiculo = pesquisaVeiculo;
	}

	public String getPesquisaStatusVeiculo() {
		return pesquisaStatusVeiculo;
	}

	public void setPesquisaStatusVeiculo(String pesquisaStatusVeiculo) {
		this.pesquisaStatusVeiculo = pesquisaStatusVeiculo;
	}

	public List<VeiculoDTO> getListaPesquisa() {
		return listaPesquisa;
	}

	public void setListaPesquisa(List<VeiculoDTO> listaPesquisa) {
		this.listaPesquisa = listaPesquisa;
	}

	public VeiculoDTO getVeiculoEditavel() {
		return veiculoEditavel;
	}

	public void setVeiculoEditavel(VeiculoDTO veiculoEditavel) {
		this.veiculoEditavel = veiculoEditavel;
	}

	public VeiculoService getVeiculoService() {
		return veiculoService;
	}

	public void setVeiculoService(VeiculoService veiculoService) {
		this.veiculoService = veiculoService;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public String getRenavam() {
		return renavam;
	}

	public void setRenavam(String renavam) {
		this.renavam = renavam;
	}

	public double getCapacidadePipa() {
		return capacidadePipa;
	}

	public void setCapacidadePipa(double capacidadePipa) {
		this.capacidadePipa = capacidadePipa;
	}

	public String getMarcaModelo() {
		return marcaModelo;
	}

	public void setMarcaModelo(String marcaModelo) {
		this.marcaModelo = marcaModelo;
	}
	
	public PagamentoPipeiroSevice getService() {
		return service;
	}

	public void setService(PagamentoPipeiroSevice service) {
		this.service = service;
	}
	
}
