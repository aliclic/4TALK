package modelo;

import java.util.ArrayList;

public class Grupo extends Participante {
	private ArrayList<Individual> individuos;
	
	public Grupo(String nome) {
		super(nome);
	}
	
	public void adicionar(Individual i) {
		individuos.add(i);
		
	}

	public ArrayList<Individual> getIndividuos() {
		return individuos;
	}

	public void setIndividuos(ArrayList<Individual> individuos) {
		this.individuos = individuos;
	}

	@Override
	public String toString() {
		return "Grupo [individuos=" + individuos + "]";
	}
	
}