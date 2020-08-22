package opp.bean;

import java.io.Serializable;
import java.nio.charset.CodingErrorAction;
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
import opp.dto.PipeiroDTO;
import opp.dto.UsuarioDTO;
import opp.dto.VeiculoDTO;
import opp.entity.Status;
import opp.sevice.CidadeService;
import opp.sevice.PagamentoPipeiroSevice;
import opp.sevice.PipeiroService;
import opp.sevice.VeiculoService;
import opp.util.Constantes;
import opp.util.Funcoes;
import opp.util.Propriedades;

import org.apache.log4j.Logger;

@ViewScoped
@ManagedBean(name = "cadastrarPipeiroBean")
@SessionScoped
public class CadastrarPipeiroBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8882956431657279608L;

	private static final Logger logger = Logger.getLogger(CadastrarPipeiroBean.class);
	
	private static Propriedades rpcProperties = Propriedades.getInstance();
	
	/* Atributos de Cadastro */
	private PipeiroDTO pipeiro;
	private int id;
	private String nome;
	private int codCidade;
	private int cnh;
	private Date validadeCnh;
	private int identidade;
	private String orgaoEmissor;
	private String nit;
	private String cpf;
	private String codBanco;
	private String banco;
	private String agencia;
	private String conta;
	private int codVeiculo;
	private double saldoSiaf;
	private double recursoAno;
	private double restoPagar;
	private String status;

	private List<CidadeDTO> listaCidade;
	private List<VeiculoDTO> listaVeiculo;
	
	/* Atributos de Pesquisa */
	private String pesquisaPipeiro;
	private String pesquisaStatusPipeiro;
	private List<PipeiroDTO> listaPesquisa;
	private int codcidadePesquisa;
	
	/* Atributos de Edição */
	private PipeiroDTO pipeiroEditavel;
	
	@ManagedProperty(value = "#{pipeiroService}")
	private PipeiroService pipeiroService;
	@ManagedProperty(value = "#{cidadeService}")
	private CidadeService cidadeService;
	@ManagedProperty(value = "#{veiculoService}")
	private VeiculoService veiculoService;
	@ManagedProperty("#{pagamentoPipeiroSevice}")
	private PagamentoPipeiroSevice service;
	
	@PostConstruct
	public void loadPage() {
		
		logger.info("Inicio da tela de Cadastro de Pipeiro.");
		
		boolean erro = false;
		veiculoService = new VeiculoService();
		cidadeService = new CidadeService();
		pipeiroService = new PipeiroService();
		service = new PagamentoPipeiroSevice();
		
		
		try {
			listaCidade = cidadeService.recuperaTodasCidadesAtivas();
		} catch (Exception ex) {
			erro = true;
			logger.error(ex);
			FacesContext.getCurrentInstance().addMessage(    
	                "msgCadastrarPipeiro",    
	                new FacesMessage(FacesMessage.SEVERITY_ERROR,    
	                        "Erro ao recuperar Cidadades.", null));
		}
		
		if(!erro){
			try {
				listaVeiculo = veiculoService.recuperaTodosVeiculosAtivos();
			} catch (Exception ex) {
				logger.error(ex);
				FacesContext.getCurrentInstance().addMessage(    
		                "msgCadastrarPipeiro",    
		                new FacesMessage(FacesMessage.SEVERITY_ERROR,    
		                        "Erro ao recuperar Veículos.", null));
			}
		}
		
		pipeiroEditavel = new PipeiroDTO();
	}
	
	/**
	 * Ações
	 */
	public void salvar(){
		
		logger.info("Inicio do método salvar()");
		
		
//		if(!Funcoes.validarCpf(cpf)){
//			
//			FacesContext.getCurrentInstance().addMessage(    
//					"msgCadastrarCidade",    
//					new FacesMessage(FacesMessage.SEVERITY_ERROR,    
//							"CPF inválido", null));
//			return;
//			
//		}
		
		if(campoEhValido(
				id,
				nome,
				codCidade,
				cnh,
				validadeCnh,
				identidade,
				orgaoEmissor,
				nit,
				cpf,
				codBanco,
				banco,
				agencia,
				conta,
				codVeiculo,
				saldoSiaf,
				recursoAno,
				restoPagar,
				status)){
			
			setStatus("ATIVO");
			
			//Monta objeto
			pipeiro = new PipeiroDTO(
					nome,
					codCidade,
					cnh,
					validadeCnh,
					identidade,
					orgaoEmissor,
					nit,
					cpf,
					codBanco,
					banco,
					agencia,
					conta,
					codVeiculo,
					saldoSiaf,
					recursoAno,
					restoPagar,
					status
					);
			
			//Verifica se Existe
			boolean existe = false;
			
			try {
				existe = verificaSeExiste(pipeiro.getNome().trim());
			} catch (Exception e) {
				logger.error(e);
				FacesContext.getCurrentInstance().addMessage(    
		                "msgCadastrarPipeiro",    
		                new FacesMessage(FacesMessage.SEVERITY_ERROR,    
		                        "Não foi possível salvar o registro.", null));
			}
			
			if(!existe){
				//Insere Pipeiro
				boolean erro = false;
				
				try{
					pipeiroService.inserePipeiro(pipeiro);
					
					UsuarioDTO usuarioNaSessao = (UsuarioDTO) Funcoes.getSession().getAttribute(Constantes.USUARIO_LOGADO);
					
					service.inserirInteracaoUsuario(Constantes.CADASTRO_PIPEIRO,Constantes.INSERIRDADOS,null,new Date(),usuarioNaSessao.getId());
				
				}catch(Exception ex){
					logger.error(ex);
					erro = true;
				}
				
				if(erro){
					logger.error("Não foi possível salvar o registro.");
					FacesContext.getCurrentInstance().addMessage(    
		                "msgCadastrarPipeiro",    
		                new FacesMessage(FacesMessage.SEVERITY_ERROR,    
		                        "Não foi possível salvar o registro.", null));
				}else{
					logger.info("Registro salvo com sucesso.");
					FacesContext.getCurrentInstance().addMessage(    
		                "msgCadastrarPipeiro",    
		                new FacesMessage(FacesMessage.SEVERITY_INFO,    
		                        "Registro salvo com sucesso.", null));
					limpaForm();
				}
			}else{
				
				FacesContext.getCurrentInstance().addMessage(    
		                "msgCadastrarPipeiro",    
		                new FacesMessage(FacesMessage.SEVERITY_ERROR,    
		                        "Registro já existe.", null));
			}
		}else{
			
			StringBuffer msgErro = new StringBuffer();
			
			msgErro.append("Campo(s) obrigatório(s): ");
			
			if(Funcoes.isNuloOuVazio(nome)){
				msgErro.append("- Nome do Pipeiro ");
			}
			if(Funcoes.isNuloOuVazioZero(codCidade)){
				msgErro.append("- Cidade ");
			}
			if(Funcoes.isNuloOuVazioZero(identidade)){
				msgErro.append("- RG ");
			}
			if(Funcoes.isNuloOuVazio(orgaoEmissor)){
				msgErro.append("- Orgão Emissor ");
			}
			if(Funcoes.isNuloOuVazioZero(cpf)){
				msgErro.append("- CPF ");
			}
			if(Funcoes.isNuloOuVazioZero(nit)){
				msgErro.append("- NIT ");
			}
			if(Funcoes.isNuloOuVazio(codBanco)){
				msgErro.append("- Código do Banco ");
			}
			if(Funcoes.isNuloOuVazio(banco)){
				msgErro.append("- Banco ");
			}
			if(Funcoes.isNuloOuVazio(conta)){
				msgErro.append("- Conta ");
			}
			if(Funcoes.isNuloOuVazio(agencia)){
				msgErro.append("- Agência ");
			}
			if(Funcoes.isNuloOuVazio(codVeiculo)){
				msgErro.append("- Veículo ");
			}

			FacesContext.getCurrentInstance().addMessage(    
					"msgCadastrarCidade",    
					new FacesMessage(FacesMessage.SEVERITY_ERROR,    
							msgErro.toString(), null));

		}
		
		logger.info("Fim do método salvar()");

	}

	public boolean verificaSeExiste(String nome) throws Exception{
		boolean existe = false;
		
		PipeiroDTO pipeiroProcurada = pipeiroService.recuperaPipeiroPorNome(nome);
		if(null != pipeiroProcurada && pipeiroProcurada.getNome().equalsIgnoreCase(nome)){
			existe = true;
		}
		
		return existe;
	}
	
	public void limpaEditavel(){
		pipeiroEditavel = new PipeiroDTO();
	}
	
	public void editar(){
		
		logger.info("Inicio do método editar()");
		
		boolean erro = false;
		String msg = "";
		
		if(campoEhValido(
				pipeiroEditavel.getId(),
				pipeiroEditavel.getNome(),
				pipeiroEditavel.getCodCidade(),
				pipeiroEditavel.getCnh(),
				pipeiroEditavel.getValidadeCnh(),
				pipeiroEditavel.getIdentidade(),
				pipeiroEditavel.getOrgaoEmissor(),
				pipeiroEditavel.getNit(),
				pipeiroEditavel.getCpf(),
				pipeiroEditavel.getCodBanco(),
				pipeiroEditavel.getBanco(),
				pipeiroEditavel.getAgencia(),
				pipeiroEditavel.getConta(),
				pipeiroEditavel.getCodVeiculo(),
				pipeiroEditavel.getSaldoSiaf(),
				pipeiroEditavel.getRecursoAno(),
				pipeiroEditavel.getRestoPagar(),
				pipeiroEditavel.getStatus()
				)){
			try{
				pipeiroService.atualizaPipeiro(pipeiroEditavel);
				
				UsuarioDTO usuarioNaSessao = (UsuarioDTO) Funcoes.getSession().getAttribute(Constantes.USUARIO_LOGADO);
				
				service.inserirInteracaoUsuario(Constantes.CADASTRO_PIPEIRO,Constantes.ATUALIZARDADOS,null,new Date(),usuarioNaSessao.getId());
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
			FacesContext.getCurrentInstance().addMessage("msgCadastrarPipeiro",
					new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null));
		}
		
		logger.info("Fim do método editar()");
	}
	
	public void enviaEditavel(PipeiroDTO pipeiro){
		pipeiroEditavel = new PipeiroDTO(
				pipeiro.getId(),
				pipeiro.getNome(),
				pipeiro.getCodCidade(),
				pipeiro.getCnh(),
				pipeiro.getValidadeCnh(),
				pipeiro.getIdentidade(),
				pipeiro.getOrgaoEmissor(),
				pipeiro.getNit(),
				pipeiro.getCpf(),
				pipeiro.getCodBanco(),
				pipeiro.getBanco(),
				pipeiro.getAgencia(),
				pipeiro.getConta(),
				pipeiro.getCodVeiculo(),
				pipeiro.getSaldoSiaf(),
				pipeiro.getRecursoAno(),
				pipeiro.getRestoPagar(),
				pipeiro.getStatus()
				);
	}
	
	public void pesquisar(){
		
		logger.info("Inicio do método pesquisar()");
		
		String msg = null;
		//Pesquisa Pipeiro
		try{
			listaPesquisa = pipeiroService.recuperaPipeirosPorNome(pesquisaPipeiro,pesquisaStatusPipeiro, codcidadePesquisa);
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
			FacesContext.getCurrentInstance().addMessage("msgCadastrarPipeiro",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null));
		}	
		
		logger.info("Fim do método pesquisar()");
	}
	
	private boolean campoEhValido(int id, String nome, int codCidade, int cnh,
			Date validadeCnh, int identidade, String orgaoEmissor, String nit,
			String cpf, String codBanco, String banco, String agencia,
			String conta, int codVeiculo, double saldoSiaf, double recursoAno,
			double restoPagar, String status){
		
		if(Funcoes.isNuloOuVazio(nome) || (Funcoes.isNuloOuVazio(codCidade) &&
				Funcoes.isNuloOuVazio(identidade)) || Funcoes.isNuloOuVazio(orgaoEmissor)
				|| (Funcoes.isNuloOuVazio(nit) && Funcoes.isNuloOuVazio(cpf)) 
				|| Funcoes.isNuloOuVazio(codBanco) || Funcoes.isNuloOuVazio(banco) || Funcoes.isNuloOuVazio(agencia)
				|| Funcoes.isNuloOuVazio(conta) || Funcoes.isNuloOuVazio(codVeiculo)){
			return false;
		}
		
		return true;
	}
	
	public void limpaForm(){

		id = 0;
		nome = "";
		codCidade = 0;
		cnh = 0;
		validadeCnh = null;
		identidade = 0;
		orgaoEmissor = "";
		nit = "";
		cpf = "";
		codBanco = "";
		banco = "";
		agencia = "";
		conta = "";
		codVeiculo = 0;
		saldoSiaf = 0;
		recursoAno = 0;
		restoPagar = 0;
		status = "";
		
	}
	
	public void imprimirRelatorio() throws JRException,Exception{
		
		logger.info("Inicio do método imprimirRelatorio()");
		
		boolean erro = false;
		String msg = "";
		
		try {
			
			List<PipeiroDTO> lista = new ArrayList<PipeiroDTO>();
			lista = pipeiroService.recuperaTodosPipeiros();
			
			String caminhoRelatorio = rpcProperties.buscaPropriedade("caminho.relatorios");
			String caminhoImpressao = rpcProperties.buscaPropriedade("caminho.impressao");
			
			// compilacao do JRXML
			JasperReport report = JasperCompileManager
					.compileReport(caminhoRelatorio+"relatorio_pipeiro.jrxml");
									
			JasperPrint print = JasperFillManager.fillReport(report, null,
					new JRBeanCollectionDataSource(lista));
	
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat formatadorData = new SimpleDateFormat("yyyyMMddHHmmss");
			String timeStamp =	formatadorData.format(calendar.getTime()); 
			
			String arquivo = "RelatorioPipeiro_"+timeStamp+".pdf";
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

	public PipeiroDTO getPipeiro() {
		return pipeiro;
	}

	public void setPipeiro(PipeiroDTO pipeiro) {
		this.pipeiro = pipeiro;
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

	public List<VeiculoDTO> getListaVeiculo() {
		if(null == listaVeiculo){
			listaVeiculo = new ArrayList<VeiculoDTO>();
		}
		
		return listaVeiculo;
	}
	

	public String getPesquisaPipeiro() {
		return pesquisaPipeiro;
	}

	public void setPesquisaPipeiro(String pesquisaPipeiro) {
		this.pesquisaPipeiro = pesquisaPipeiro;
	}

	public String getPesquisaStatusPipeiro() {
		return pesquisaStatusPipeiro;
	}

	public void setPesquisaStatusPipeiro(String pesquisaStatusPipeiro) {
		this.pesquisaStatusPipeiro = pesquisaStatusPipeiro;
	}

	public List<PipeiroDTO> getListaPesquisa() {
		return listaPesquisa;
	}

	public void setListaPesquisa(List<PipeiroDTO> listaPesquisa) {
		this.listaPesquisa = listaPesquisa;
	}

	public PipeiroDTO getPipeiroEditavel() {
		return pipeiroEditavel;
	}

	public void setPipeiroEditavel(PipeiroDTO pipeiroEditavel) {
		this.pipeiroEditavel = pipeiroEditavel;
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

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getCodCidade() {
		return codCidade;
	}

	public void setCodCidade(int codCidade) {
		this.codCidade = codCidade;
	}

	public int getCnh() {
		return cnh;
	}

	public void setCnh(int cnh) {
		this.cnh = cnh;
	}

	public Date getValidadeCnh() {
		return validadeCnh;
	}

	public void setValidadeCnh(Date validadeCnh) {
		this.validadeCnh = validadeCnh;
	}

	public int getIdentidade() {
		return identidade;
	}

	public void setIdentidade(int identidade) {
		this.identidade = identidade;
	}

	public String getOrgaoEmissor() {
		return orgaoEmissor;
	}

	public void setOrgaoEmissor(String orgaoEmissor) {
		this.orgaoEmissor = orgaoEmissor;
	}

	public String getNit() {
		return nit;
	}

	public void setNit(String nit) {
		this.nit = nit;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getCodBanco() {
		return codBanco;
	}

	public void setCodBanco(String codBanco) {
		this.codBanco = codBanco;
	}

	public String getBanco() {
		return banco;
	}

	public void setBanco(String banco) {
		this.banco = banco;
	}

	public String getAgencia() {
		return agencia;
	}

	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}

	public String getConta() {
		return conta;
	}

	public void setConta(String conta) {
		this.conta = conta;
	}

	public int getCodVeiculo() {
		return codVeiculo;
	}

	public void setCodVeiculo(int codVeiculo) {
		this.codVeiculo = codVeiculo;
	}

	public double getSaldoSiaf() {
		return saldoSiaf;
	}

	public void setSaldoSiaf(double saldoSiaf) {
		this.saldoSiaf = saldoSiaf;
	}

	public double getRecursoAno() {
		return recursoAno;
	}

	public void setRecursoAno(double recursoAno) {
		this.recursoAno = recursoAno;
	}

	public double getRestoPagar() {
		return restoPagar;
	}

	public void setRestoPagar(double restoPagar) {
		this.restoPagar = restoPagar;
	}

	public PipeiroService getPipeiroService() {
		return pipeiroService;
	}

	public void setPipeiroService(PipeiroService pipeiroService) {
		this.pipeiroService = pipeiroService;
	}

	public VeiculoService getVeiculoService() {
		return veiculoService;
	}

	public void setVeiculoService(VeiculoService veiculoService) {
		this.veiculoService = veiculoService;
	}

	public void setListaVeiculo(List<VeiculoDTO> listaVeiculo) {
		this.listaVeiculo = listaVeiculo;
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
}
