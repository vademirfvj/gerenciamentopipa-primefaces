package opp.dto;

import java.io.Serializable;

public class IndiceDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7050389209330996788L;

	private int id;
	private String dscIndice;
	private double indice;
	
	public IndiceDTO() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDscIndice() {
		return dscIndice;
	}

	public void setDscIndice(String dscIndice) {
		this.dscIndice = dscIndice;
	}

	public double getIndice() {
		return indice;
	}

	public void setIndice(double indice) {
		this.indice = indice;
	}
	
	
}
