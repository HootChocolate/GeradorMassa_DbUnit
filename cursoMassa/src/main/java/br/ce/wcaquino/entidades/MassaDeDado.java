package br.ce.wcaquino.entidades;

public class MassaDeDado {

	private long id;
	private String valor;
	
	public MassaDeDado() {}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public long getId() {
		return id;
	}
	
	public void setValor(String contaSeuBarriga) {
		this.valor = contaSeuBarriga;
	}
	
	public String getValor() {
		return valor;
	}
	
	@Override
	public String toString() {
		String print = 
				"\n=========================="
		+ "\n      Dados da Conta       "
		+ "\nid: " + getId()
		+ "\nNome: " + getValor()
		+ "\n==========================";
		return print;
	}
}
