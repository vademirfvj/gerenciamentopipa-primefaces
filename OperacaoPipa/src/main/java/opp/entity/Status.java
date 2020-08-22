package opp.entity;

public enum Status {
	

	ATIVO("ATIVO"),
	INATIVO("INATIVO");
	
	private String label;

    private Status(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
	
}
