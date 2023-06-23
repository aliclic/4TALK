package modelo;

import java.util.ArrayList;

public class Participante {
	private String nome;
	private ArrayList<Mensagem> recebidas;
	private ArrayList<Mensagem> enviadas;
	
	
	public Participante(String nome) {
		super();
		this.nome = nome;
	}
	
	public void adicionarEnviada(Mensagem msg) {
		enviadas.add(msg);
	}
	
	public void removerEnviada(Mensagem msg) {
		enviadas.remove(msg);
	}
	
	public Mensagem localizarEnviada(int id) {
		for(Mensagem m : enviadas) {
			if(m.getId() == id)
				return m;
		}
		return null;
	}
	
	public void adicionarRecebida(Mensagem msg) {
		recebidas.add(msg);
	}
	
	public void removerRecebida(Mensagem msg) {
		recebidas.remove(msg);
	}
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public ArrayList<Mensagem> getRecebidas() {
		return recebidas;
	}

	public void setRecebidas(ArrayList<Mensagem> recebidas) {
		this.recebidas = recebidas;
	}

	public ArrayList<Mensagem> getEnviadas() {
		return enviadas;
	}

	public void setEnviadas(ArrayList<Mensagem> enviadas) {
		this.enviadas = enviadas;
	}

	@Override
	public String toString() {
		return "Participante [nome=" + nome + ", recebidas=" + recebidas + ", enviadas=" + enviadas + "]";
	}
	
	
}
