package okProjekt;

import java.util.List;

public class OsobnikPopulacji {
	private List<Zadanie> zadania;
	private List<Blok> maszyna1;
	private List<Blok> maszyna2;
	private int wynik;
	
	public OsobnikPopulacji(List<Zadanie> zadania, List<Blok> maszyna1, List<Blok> maszyna2, int wynik) {
        this.setZadania(zadania);
        this.setMaszyna1(maszyna1);
        this.setMaszyna2(maszyna2);
        this.setWynik(wynik);
    }

	public List<Zadanie> getZadania() {
		return zadania;
	}

	public void setZadania(List<Zadanie> zadania) {
		this.zadania = zadania;
	}

	public List<Blok> getMaszyna1() {
		return maszyna1;
	}

	public void setMaszyna1(List<Blok> maszyna1) {
		this.maszyna1 = maszyna1;
	}

	public List<Blok> getMaszyna2() {
		return maszyna2;
	}

	public void setMaszyna2(List<Blok> maszyna2) {
		this.maszyna2 = maszyna2;
	}

	public int getWynik() {
		return wynik;
	}

	public void setWynik(int wynik) {
		this.wynik = wynik;
	}
}
