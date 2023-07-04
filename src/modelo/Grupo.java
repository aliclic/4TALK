package modelo;

import java.util.ArrayList;

public class Grupo extends Participante {
	private ArrayList<Individual> individuos = new ArrayList<>();
	
	public Grupo(String nome) {
		super(nome);
	}
	
	public void adicionar(Individual i) {
		individuos.add(i);
	}
	
	public void remover(Individual i) {
		individuos.remove(i);
	}

	public ArrayList<Individual> getIndividuos() {
		return individuos;
	}

	public void setIndividuos(ArrayList<Individual> individuos) {
		this.individuos = individuos;
	}

	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		sb.append("\n individuos do grupo:");
		if (individuos.isEmpty()) {
			sb.append(" vazio");
		} else {
			for (Individual i : getIndividuos()) {
				sb.append("\n --> ").append(i.getNome());
			}
		}
		return sb.toString();
	}
	
	
}
