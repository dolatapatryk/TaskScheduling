package okProjekt;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Random;

public class Main {

	
	public final static double kara = 0.25;
	public static int liczbaZadan = 50;
	public static int liczbaPrzerw = 5;
	
	
	public static void main(String[] args) throws Exception {
		Random r = new Random();
		
		
		
				
		
		int liczbaTestow = 1;
		int liczbaInstancji = 1;
		

		for(int z=0;z<liczbaTestow;z++) {
			
		
		int liczbaGeneracji = 100;
		
		String plik = "TESTY/testDluzszePrzerwyStaleOperacje/przerwy1-"+GeneratorInstancji.maxCzasTrwania+"/"+"test.test";
		BufferedOutputStream testOut = new BufferedOutputStream(new FileOutputStream(plik));
		PrintWriter x = new PrintWriter(new OutputStreamWriter(testOut));

		
		x.write("Liczba zadan: "+liczbaZadan+"\n");
		x.write("Liczba przerw na kazdej z maszyn: "+liczbaPrzerw+"\n");
		x.write("Max czas trwania operacji: "+GeneratorInstancji.maxCzasTrwaniaZadania+"\n");
		x.write("Max czas trwania przerw: "+GeneratorInstancji.maxCzasTrwania+"\n");
		x.write("Liczba instancji: "+liczbaInstancji+"\n");
		x.write("Liczba generacji: "+liczbaGeneracji+"\n");
		x.write("\n");
		x.write("Wielkosc populacji: "+Algorytm.getWielkoscPopulacji()+"\n");
		x.write("Wspolczynnik mutacji: "+Algorytm.getWspolczynnikMutacji()+"\n");
		x.write("Liczba kandydatow do turnieju: "+Algorytm.getLiczbaKandydatowDoTurnieju()+"\n");
		x.write("Liczba zwyciezcow: "+Algorytm.getLiczbaZwyciezcowTurniejow()+"\n");
		x.write("Wspolczynnik podzialu: "+Algorytm.getWspolczynnikPodzialu()+"\n");
		
		
		
		for(int i=0;i<liczbaInstancji;i++) {
			
			
			Algorytm.ladujInstancje(i,"INSTANCJE/instancjeZmiennaLiczbaGeneracji/instancja");
			System.out.println("***INSTANCJA "+i+"***");
			OsobnikPopulacji y = new OsobnikPopulacji(Algorytm.getListaZadan(),Algorytm.maszyna1,Algorytm.maszyna2,Algorytm.generujRozwiazanie2(Algorytm.getListaZadan(), Algorytm.maszyna1,Algorytm.maszyna2));
			Populacja pop = Algorytm.tworzPierwszaPopulacje();
			Algorytm.listaPopulacji.add(pop);
			System.out.println("Njagorszy  z populacji zerowej: ");
			System.out.println(Algorytm.najgorszyZPopulacji(pop).getWynik());
			System.out.println("Srednia z populacji zerowej:");
			int srednia = 0;
			for(int j=0;j<pop.getListaOsobnikow().size();j++) {
				srednia+=pop.getListaOsobnikow().get(j).getWynik();
			}
			srednia=srednia/pop.getListaOsobnikow().size();
			System.out.println(srednia);
			for(int j=0;j<liczbaGeneracji;j++) {
				Populacja dziecko = Algorytm.algorytmGenetyczny(Algorytm.listaPopulacji.get(j));
				Algorytm.listaPopulacji.add(dziecko);
			}
				int najlepszy;
				System.out.println("Najlepszy z populacji: "+liczbaGeneracji);
				OsobnikPopulacji najlepszyOsobnik = Algorytm.najlepszyZPopulacji(Algorytm.listaPopulacji.get(liczbaGeneracji));
				najlepszy = Algorytm.najlepszyZPopulacji(Algorytm.listaPopulacji.get(liczbaGeneracji)).getWynik();
				System.out.println(najlepszy);
				System.out.println();
				double procent = 100 - ((double)najlepszy/srednia)*100;
				String procentZPrzecinkiem = String.valueOf(round(procent,2));
				procentZPrzecinkiem = procentZPrzecinkiem.replace(".", ",");
				zapiszWynik(i,najlepszyOsobnik,srednia,"WYNIKI/wynikiZmiennaLiczbaGeneracji/wynik");
				x.write(srednia+";"+najlepszy+";"+procentZPrzecinkiem+" %\n");
				System.out.println(round(procent,2)+" % ");
				Algorytm.listaPopulacji.clear();
				Algorytm.maszyna1.clear();
				Algorytm.maszyna2.clear();
				
				
		}
		x.close();
		
		
		}
		

	}
	
	 public static float round(double liczba, int miejscaPoPrzecinku) //metoda zaokraglajaca liczbe double do dwoch miejsc po przecinku

	   {  float temp = (float)(liczba*(Math.pow(10, miejscaPoPrzecinku)));

	          temp = (Math.round(temp));

	          temp = temp/(int)(Math.pow(10, miejscaPoPrzecinku));

	          return temp;

	   }
	 
	 public static void zapiszWynik(int numerInstancji,OsobnikPopulacji osobnik, int pierwotnyWynik, String sciezka) throws FileNotFoundException {
		String plik = sciezka+numerInstancji+".wynik";
		BufferedOutputStream wynikOut = new BufferedOutputStream(new FileOutputStream(plik));
		PrintWriter x = new PrintWriter(new OutputStreamWriter(wynikOut));
		
		int licznikMaint1 = 0;
		int licznikMaint2 = 0;
		int licznikIdle1 = 0;
		int licznikIdle2 = 0;
		
		int dlugoscMaint1 = 0;
		int dlugoscMaint2 = 0;
		int dlugoscIdle1 = 0;
		int dlugoscIdle2 = 0;
		
		x.write("**** "+numerInstancji+" ****\n");
		x.write(osobnik.getWynik()+";"+pierwotnyWynik+"\n");
		x.write("M1: ");
		for(Blok blok:osobnik.getMaszyna1()) {
			if(blok instanceof Operacja) {
				Operacja op = (Operacja) blok;
				x.write("op"+op.getNumerOperacji()+"_"+op.getNumerZadania()+","+op.getCzasStartu()+","+op.getCzasTrwania()+","+op.getCzasTrwaniaPraktyczny()+";");
			}else if(blok instanceof Maintenance) {
				Maintenance m = (Maintenance) blok;
				x.write("maint"+(licznikMaint1+1)+"_"+"M1"+","+m.getCzasStartu()+","+m.getCzasTrwania()+";");
				licznikMaint1++;
				dlugoscMaint1+=m.getCzasTrwania();
			}else if(blok instanceof Idle) {
				Idle idle = (Idle) blok;
				x.write("idle"+(licznikIdle1+1)+"_M1"+","+idle.getCzasStartu()+","+idle.getCzasTrwania()+";");
				licznikIdle1++;
				dlugoscIdle1+=idle.getCzasTrwania();
			}
		}
		x.write("\nM2: ");
		for(Blok blok:osobnik.getMaszyna2()) {
			if(blok instanceof Operacja) {
				Operacja op = (Operacja) blok;
				x.write("op"+op.getNumerOperacji()+"_"+op.getNumerZadania()+","+op.getCzasStartu()+","+op.getCzasTrwania()+","+op.getCzasTrwaniaPraktyczny()+";");
			}else if(blok instanceof Maintenance) {
				Maintenance m = (Maintenance) blok;
				x.write("maint"+(licznikMaint2+1)+"_"+"M2"+","+m.getCzasStartu()+","+m.getCzasTrwania()+";");
				licznikMaint2++;
				dlugoscMaint2+=m.getCzasTrwania();
			}else if(blok instanceof Idle) {
				Idle idle = (Idle) blok;
				x.write("idle"+(licznikIdle2+1)+"_M2"+","+idle.getCzasStartu()+","+idle.getCzasTrwania()+";");
				licznikIdle2++;
				dlugoscIdle2+=idle.getCzasTrwania();
			}
		}
		
		x.write("\n"+licznikMaint1+","+dlugoscMaint1);
		x.write("\n"+licznikMaint2+","+dlugoscMaint2);
		x.write("\n"+licznikIdle1+";"+dlugoscIdle1);
		x.write("\n"+licznikIdle2+";"+dlugoscIdle2+"\n");
		x.write("*** EOF ***");
		x.close();
		
		
		
	 }

}
