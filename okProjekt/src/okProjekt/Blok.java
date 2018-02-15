package okProjekt;



public class Blok implements Cloneable {
	private int czasStartu;
	private int czasTrwania;
	private int maszyna;
	
	public Blok() {
		
	}
	
	public Blok(int czasTrwania, int maszyna) {
		this.czasTrwania = czasTrwania;
		this.maszyna = maszyna;
		
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() +" czas trwania: " + getCzasTrwania();
		
	}
	
	
	public int getCzasStartu() {
		return czasStartu;
	}
	public void setCzasStartu(int czasStartu) {
		this.czasStartu = czasStartu;
	}
	public int getCzasTrwania() {
		return czasTrwania;
	}
	public void setCzasTrwania(int czasTrwania) {
		this.czasTrwania = czasTrwania;
	}


	public int getMaszyna() {
		return maszyna;
	}



	public void setMaszyna(int maszyna) {
		this.maszyna = maszyna;
	}
	
	 @Override
	    public Object clone() throws CloneNotSupportedException {
	        return super.clone();
	    }
}
