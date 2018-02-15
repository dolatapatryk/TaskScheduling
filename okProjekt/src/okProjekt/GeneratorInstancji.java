package okProjekt;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class GeneratorInstancji {

	public static Random r = new Random();
	public static int n = 50; //liczba zadan
	public static int k = 5; //liczba przerw na jednej maszynie
	
	public static int maxCzasTrwania = 300;
	public static int maxCzasTrwaniaZadania = 20;
	
	
	public static Zadanie[] tablicaZadan;
	
	public static Maintenance[][] tablicaPrzerw;
	
	private static int czasMaszyna1 = 0;
	private static int czasMaszyna2 = 0;
	
	public static void generujInstancje(int numerInstancji) throws FileNotFoundException {
		tablicaZadan = new Zadanie[Main.liczbaZadan];
		tablicaPrzerw = new Maintenance[2][Main.liczbaPrzerw];
		String plik = "INSTANCJE/instancjeZmiennaLiczbaGeneracji/instancja"+numerInstancji+".instancja";
		BufferedOutputStream instancjaOut = new BufferedOutputStream(new FileOutputStream(plik));
		PrintWriter x = new PrintWriter(new OutputStreamWriter(instancjaOut));
		x.write("**** "+numerInstancji+" ****\n");
		x.write(Main.liczbaZadan+"\n");
		
		generujZadania();
		generujMaintenance1();
		generujMaintenance2();
		
		for(int i=0;i<Main.liczbaZadan;i++) {
			Operacja[] tmp = new Operacja[2];
			tmp = tablicaZadan[i].getOperacje();
			x.write(tmp[0].getCzasTrwania()+";"+tmp[1].getCzasTrwania()+";"+tmp[0].getMaszyna()+";"+tmp[1].getMaszyna()+"\n");
			tmp = null;
		}
		for(int i=0;i<Main.liczbaPrzerw;i++) {
			x.write((i+1)+";"+tablicaPrzerw[0][i].getMaszyna()+";"+tablicaPrzerw[0][i].getCzasTrwania()+";"+tablicaPrzerw[0][i].getCzasStartu()+"\n");
		}
		for(int i=0;i<Main.liczbaPrzerw;i++) {
			x.write((i+1+Main.liczbaPrzerw)+";"+tablicaPrzerw[1][i].getMaszyna()+";"+tablicaPrzerw[1][i].getCzasTrwania()+";"+tablicaPrzerw[1][i].getCzasStartu()+"\n");
		}
		
		x.write("*** EOF ***");
		x.close();
		czasMaszyna1=0;
		czasMaszyna2=0;
		
	}
	
	
	
	public static void generujZadania() {
		for(int i=0;i<Main.liczbaZadan;i++) {
			Zadanie z = new Zadanie(i);
			z.getOperacje()[0].setCzasTrwania(r.nextInt(maxCzasTrwaniaZadania)+1);
			z.getOperacje()[0].setCzasTrwaniaPraktyczny(z.getOperacje()[0].getCzasTrwania());
			z.getOperacje()[1].setCzasTrwania(r.nextInt(maxCzasTrwaniaZadania)+1);
			z.getOperacje()[1].setCzasTrwaniaPraktyczny(z.getOperacje()[1].getCzasTrwania());
			
			
			tablicaZadan[i]=z;
			
			z.getOperacje()[0].setMaszyna(r.nextInt(2)+1);
			if(z.getOperacje()[0].getMaszyna()==1) {
				z.getOperacje()[1].setMaszyna(2);
			}else {
				z.getOperacje()[1].setMaszyna(1);
			}
			

		}
		for(int i=0;i<Main.liczbaZadan;i++) {
			if(tablicaZadan[i].getOperacje()[0].getMaszyna()==1) {
				czasMaszyna1+=tablicaZadan[i].getOperacje()[0].getCzasTrwania();
				czasMaszyna2+=tablicaZadan[i].getOperacje()[1].getCzasTrwania();
			}else {
				czasMaszyna2+=tablicaZadan[i].getOperacje()[0].getCzasTrwania();
				czasMaszyna1+=tablicaZadan[i].getOperacje()[1].getCzasTrwania();
			}
		}
	}
	
	
	
	public static void generujMaintenance1() {
		int[][] tabPrzedzialTrwaniaMaintenencow1 = new int[Main.liczbaPrzerw][2]; //k nr maintenance, k[0] czas startu, k[1], czas zakonczenia
		
		int czasStartu = r.nextInt(czasMaszyna1);
		int czasTrwania = r.nextInt(maxCzasTrwania)+1;
		Maintenance m0 = new Maintenance(czasStartu,czasTrwania,1);
		
		tablicaPrzerw[0][0]=m0;//losowy pierwszy maintenance do maszyny 0
		tabPrzedzialTrwaniaMaintenencow1[0][0] = m0.getCzasStartu(); //czas startu
		tabPrzedzialTrwaniaMaintenencow1[0][1] = m0.getCzasStartu() + m0.getCzasTrwania(); //czas zakonczenia
		
	
		
		for(int j=1;j<Main.liczbaPrzerw;j++) {
			boolean poprawnyCzas = true;
			boolean czyWhileMaDzialac = true;
			while(czyWhileMaDzialac) {
				czasStartu = r.nextInt(czasMaszyna1);
				czasTrwania = r.nextInt(maxCzasTrwania)+1;
				for(int i=0;i<j;i++) {
					
					if(czasStartu+czasTrwania <= tabPrzedzialTrwaniaMaintenencow1[i][0] || czasStartu >= tabPrzedzialTrwaniaMaintenencow1[i][1]) {
						poprawnyCzas = true;
					}else {
						poprawnyCzas = false;
						break;
					}
				}
				if(poprawnyCzas) {
				Maintenance m = new Maintenance(czasStartu, czasTrwania,1);
				
				tablicaPrzerw[0][j]=m;
				
				tabPrzedzialTrwaniaMaintenencow1[j][0] = czasStartu;
				tabPrzedzialTrwaniaMaintenencow1[j][1] = czasStartu+czasTrwania;
				czyWhileMaDzialac = false;
				}
			}
			
			
		}
		Porownywacz porownywacz = new Porownywacz();
		Arrays.sort(tablicaPrzerw[0],porownywacz);
		
		
	}
	
	
	public static void generujMaintenance2() {
		int[][] tabPrzedzialTrwaniaMaintenencow2 = new int[Main.liczbaPrzerw][2];
		int czasStartu = r.nextInt(czasMaszyna1);
		int czasTrwania = r.nextInt(maxCzasTrwania)+1;		
		Maintenance m0 = new Maintenance(czasStartu,czasTrwania,2);
		
		tablicaPrzerw[1][0]=m0;//losowy pierwszy maintenance do maszyny 1
		tabPrzedzialTrwaniaMaintenencow2[0][0] = m0.getCzasStartu(); //czas startu
		tabPrzedzialTrwaniaMaintenencow2[0][1] = m0.getCzasStartu() + m0.getCzasTrwania(); //czas zakonczenia
		
		
		
		for(int j=1;j<Main.liczbaPrzerw;j++) {
			boolean poprawnyCzas = true;
			boolean czyWhileMaDzialac = true;
			while(czyWhileMaDzialac) {
				czasStartu = r.nextInt(czasMaszyna1);
				czasTrwania = r.nextInt(maxCzasTrwania)+1;
				for(int i=0;i<j;i++) {
					
					if(czasStartu+czasTrwania <= tabPrzedzialTrwaniaMaintenencow2[i][0] || czasStartu >= tabPrzedzialTrwaniaMaintenencow2[i][1]) {
						poprawnyCzas = true;
					}else {
						poprawnyCzas = false;
						break;
					}
				}
				if(poprawnyCzas) {
				Maintenance m = new Maintenance(czasStartu, czasTrwania,2);
				tablicaPrzerw[1][j]=m;
				
				tabPrzedzialTrwaniaMaintenencow2[j][0] = czasStartu;
				tabPrzedzialTrwaniaMaintenencow2[j][1] = czasStartu+czasTrwania;
				czyWhileMaDzialac = false;
				}
			}
			
			
		}
		Porownywacz porownywacz = new Porownywacz();
		Arrays.sort(tablicaPrzerw[1],porownywacz);
		
		
	}
	
	
	
	
	
	public static int sufit(double liczba) {
		int x = (int) liczba;
		if(liczba>(int)liczba) {
		return (int)liczba+1;
		}else {
			return (int)liczba;
		}
	}
	

	
	public Maintenance[][] getTablicaPrzerw() {
		return tablicaPrzerw;
	}
	public void setTablicaPrzerw(Maintenance[][] tablicaPrzerw) {
		this.tablicaPrzerw = tablicaPrzerw;
	}


	public int getCzasMaszyna1() {
		return czasMaszyna1;
	}


	public void setCzasMaszyna1(int czasMaszyna1) {
		this.czasMaszyna1 = czasMaszyna1;
	}


	public int getCzasMaszyna2() {
		return czasMaszyna2;
	}


	public void setCzasMaszyna2(int czasMaszyna2) {
		this.czasMaszyna2 = czasMaszyna2;
	}
	
	
}

class Porownywacz implements Comparator<Maintenance>{

	@Override
	public int compare(Maintenance arg0, Maintenance arg1) {
		
		if(arg0.getCzasStartu()+arg0.getCzasTrwania() > arg1.getCzasStartu()+arg1.getCzasTrwania()) {
			return 1;
		}else if(arg0.getCzasStartu()+arg0.getCzasTrwania() < arg1.getCzasStartu()+arg1.getCzasTrwania()) {
			return -1;
		}else {
		return 0;
		}
		
	}

		
	
}
