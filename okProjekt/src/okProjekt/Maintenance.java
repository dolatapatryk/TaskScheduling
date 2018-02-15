package okProjekt;

public class Maintenance extends Blok {

	public Maintenance(int czasStartu, int czasTrwania, int maszyna) {
		super(czasTrwania, maszyna);
		this.setCzasStartu(czasStartu);
	}

	
	
	
	public Maintenance() {
		super();
	}




	public String toString() {
		return "Maintenance, "+"czas startu: "+getCzasStartu()+", czas trwania: "+getCzasTrwania()+", czas zakonczenia: "+(getCzasStartu()+getCzasTrwania())+", maszyna: "+getMaszyna();
	}
}
