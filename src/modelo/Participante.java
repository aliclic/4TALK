package modelo;

import java.util.ArrayList;

public class Participante {
	private String nome;
	private ArrayList<Mensagem> recebidas = new ArrayList<>();
	private ArrayList<Mensagem> enviadas = new ArrayList<>();
	
	
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
	
	public Mensagem localizarRecebida(int id) {
		for(Mensagem m : recebidas) {
			if(m.getId() == id)
				return m;
		}
		return null;
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
		StringBuilder sb = new StringBuilder();
		sb.append("Nome = ").append(this.getNome()).append("\n");
		sb.append(" Mensagens enviadas:");
		if (this.getEnviadas().isEmpty())
			sb.append(" sem mensagens enviadas");
		else {
			for (Mensagem m : this.getEnviadas()) {
				sb.append("\n --> ").append(m);
			}
		}
		sb.append("\n Mensagens recebidas:");
		if (this.getRecebidas().isEmpty())
			sb.append(" sem mensagens recebidas");
		else {
			for (Mensagem m : this.getRecebidas()) {
				sb.append("\n --> ").append(m);
			}
		}
		return sb.toString();
		 
	}
	
	
}
