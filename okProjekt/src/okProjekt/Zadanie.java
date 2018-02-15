package okProjekt;

import java.util.Random;

public class Zadanie implements Cloneable  {

	
	private int numerZadania;
	
	private Operacja[] operacje = new Operacja[2];
	
	Random r = new Random();
	
	public Zadanie(int numerZadania) {
		operacje[0]=new Operacja();
		operacje[1]=new Operacja();
		
		operacje[0].setNumerZadania(numerZadania);
		operacje[0].setNumerOperacji(1);
		operacje[1].setNumerZadania(numerZadania);
		operacje[1].setNumerOperacji(2);
		
//		operacje[0].setMaszyna(r.nextInt(2)+1);
//		if(operacje[0].getMaszyna()==1) {
//			operacje[1].setMaszyna(2);
//		}else {
//			operacje[1].setMaszyna(1);
//		}
		this.numerZadania = numerZadania;
		
	}

	public String toString() {
		return "Zadanie " + numerZadania + " Operacja 1, czas trwania: "+operacje[0].getCzasTrwania()+", maszyna: " + operacje[0].getMaszyna()+
				" |  Operacja 2, czas trwania: "+operacje[1].getCzasTrwania()+", maszyna: " + operacje[1].getMaszyna();
	}

	public Operacja[] getOperacje() {
		return operacje;
	}

	public void setOperacje(Operacja[] operacje) {
		this.operacje = operacje;
	}

	public int getNumerZadania() {
		return numerZadania;
	}

	public void setNumerZadania(int numerZadania) {
		this.numerZadania = numerZadania;
	}

	 @Override
	    public Object clone() throws CloneNotSupportedException {
		 	Zadanie zadanie = new Zadanie(0);
		 	zadanie.setNumerZadania(this.getNumerZadania());
		 	Operacja[] pOperacje = new Operacja[2];
		 	pOperacje[0] = (Operacja) this.getOperacje()[0].clone();
		 	pOperacje[1] = (Operacja) this.getOperacje()[1].clone();
		 	zadanie.setOperacje(pOperacje);
		 	
	        return zadanie;
	    }


	

}
