package okProjekt;

public class Operacja extends Blok implements Cloneable {

	private int czasTrwaniaPraktyczny;
	private int numerZadania;
	private int numerOperacji;
	
	public Operacja() {
		super();
		
	}
	
	public Operacja(int czasTrwania, int maszyna) {
		super(czasTrwania, maszyna);
		
	}

	public int getCzasTrwaniaPraktyczny() {
		return czasTrwaniaPraktyczny;
	}

	public void setCzasTrwaniaPraktyczny(int czasTrwaniaPraktyczny) {
		this.czasTrwaniaPraktyczny = czasTrwaniaPraktyczny;
	}
	
	public void dodajKare() {
		this.czasTrwaniaPraktyczny = GeneratorInstancji.sufit((double)this.czasTrwaniaPraktyczny + (Main.kara)*this.getCzasTrwania());
	}

	public int getNumerZadania() {
		return numerZadania;
	}

	public void setNumerZadania(int numerZadania) {
		this.numerZadania = numerZadania;
	}

	public int getNumerOperacji() {
		return numerOperacji;
	}

	public void setNumerOperacji(int numerOperacji) {
		this.numerOperacji = numerOperacji;
	}
	
	public String toString() {
		return "Op. "+this.getNumerOperacji()+" zad."+this.getNumerZadania() +" czas st: "+this.getCzasStartu()+" czas trw wg inst: "+
	this.getCzasTrwania()+" czas trw prakt: "+this.getCzasTrwaniaPraktyczny();
	}
	
	 @Override
	    public Object clone() throws CloneNotSupportedException {
	        return super.clone();
	    }

}
