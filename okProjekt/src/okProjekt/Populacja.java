package okProjekt;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Populacja {
	private List<OsobnikPopulacji> listaOsobnikow;
	
	public Populacja() {
		listaOsobnikow = new LinkedList<>();
	}

	public List<OsobnikPopulacji> getListaOsobnikow() {
		return listaOsobnikow;
	}

	public void setListaOsobnikow(List<OsobnikPopulacji> populacja) {
		this.listaOsobnikow = populacja;
	}
}
