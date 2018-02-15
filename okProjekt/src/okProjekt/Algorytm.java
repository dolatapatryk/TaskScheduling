package okProjekt;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Algorytm {
	
	public static List<Blok> maszyna1 = new LinkedList<>();
	public static List<Blok> maszyna2 = new LinkedList<>();
	public static List<Populacja> listaPopulacji = new LinkedList<>();
	
	private static int wielkoscPopulacji = 200;
	private static double wspolczynnikMutacji = 1.0;
	private static int liczbaKandydatowDoTurnieju = 30 ;
	private static int liczbaZwyciezcowTurniejow = 75;
	private static double wspolczynnikPodzialu = 0.5;
	
	private static int liczbaZadan;
	
	private static List<Zadanie> listaZadan = new LinkedList<>();
	private static List<Maintenance> listaPrzerw = new LinkedList<>();
	
	
	public static void ladujInstancje(int numerInstancji, String sciezka) throws IOException {
		listaZadan.clear();
		listaPrzerw.clear();
		String plik = sciezka+numerInstancji+".instancja";
		BufferedInputStream instancjaIn = new BufferedInputStream(new FileInputStream(plik));
		BufferedReader x = new BufferedReader(new InputStreamReader(instancjaIn));
		x.readLine();
		liczbaZadan = Integer.parseInt(x.readLine());
		String[] tab;
		String linia;
		for(int i=0;i<liczbaZadan;i++) {
			linia= x.readLine();
			tab = linia.split(";");
			Zadanie zadanie = new Zadanie(i);
			zadanie.getOperacje()[0].setCzasTrwania(Integer.parseInt(tab[0]));
			zadanie.getOperacje()[1].setCzasTrwania(Integer.parseInt(tab[1]));
			zadanie.getOperacje()[0].setMaszyna(Integer.parseInt(tab[2]));
			zadanie.getOperacje()[1].setMaszyna(Integer.parseInt(tab[3]));
			listaZadan.add(zadanie);
		}
		
		while(!(linia=x.readLine()).startsWith("*")){
			tab = linia.split(";");
			Maintenance przerwa = new Maintenance();
			przerwa.setMaszyna(Integer.parseInt(tab[1]));
			przerwa.setCzasTrwania(Integer.parseInt(tab[2]));
			przerwa.setCzasStartu(Integer.parseInt(tab[3]));
			listaPrzerw.add(przerwa);
			
		}

		
		x.close();
	}
	
	
	public static Populacja algorytmGenetyczny(Populacja populacjaRodzicow) throws Exception {
		Random r = new Random();
		Populacja populacjaDzieci = new Populacja();
		OsobnikPopulacji[] zwyciezcyTurniejow = new OsobnikPopulacji[liczbaZwyciezcowTurniejow];
		while(populacjaDzieci.getListaOsobnikow().size() != wielkoscPopulacji) {
			
			for(int i=0;i<liczbaZwyciezcowTurniejow;i++) {
				OsobnikPopulacji x = turniej(populacjaRodzicow);
				zwyciezcyTurniejow[i]=x;
				}
			
			OsobnikPopulacji[] poZmianach =  krzyzowanie(zwyciezcyTurniejow[r.nextInt(zwyciezcyTurniejow.length)],zwyciezcyTurniejow[r.nextInt(zwyciezcyTurniejow.length)]);
			double prawdopodobienstwo = r.nextDouble();
			if(prawdopodobienstwo <= wspolczynnikMutacji) {
			mutacja(poZmianach[0]);
			}
			prawdopodobienstwo = r.nextDouble();
			if(prawdopodobienstwo <= wspolczynnikMutacji) {
				mutacja(poZmianach[1]);
			}
			
			populacjaDzieci.getListaOsobnikow().add(poZmianach[0]);
			populacjaDzieci.getListaOsobnikow().add(poZmianach[1]);
		}
		
		int indeksNajlepszego = 0;
		for(int i=1;i<populacjaDzieci.getListaOsobnikow().size();i++) {
			if(populacjaDzieci.getListaOsobnikow().get(i).getWynik() < populacjaDzieci.getListaOsobnikow().get(indeksNajlepszego).getWynik()) {
				indeksNajlepszego = i;
			}
		}
		
		return populacjaDzieci;
		
		
	}
	
	private static OsobnikPopulacji turniej(Populacja populacja) {
		OsobnikPopulacji[] uczestnicyTurnieju = new OsobnikPopulacji[liczbaKandydatowDoTurnieju];
		Random r  = new Random();
		List<Integer> listaUzytychIndeksow = new LinkedList<>();
		int losowyIndeks;
		for(int i=0;i<liczbaKandydatowDoTurnieju;i++) {
			do {
				losowyIndeks = r.nextInt(populacja.getListaOsobnikow().size());
				if(!(listaUzytychIndeksow.contains(losowyIndeks))) {
					listaUzytychIndeksow.add(losowyIndeks);
					break;
				}
			}while(true);
			uczestnicyTurnieju[i] = populacja.getListaOsobnikow().get(losowyIndeks);
		}
		
		int indeksNajlepszego = 0;
		for(int i=1;i<uczestnicyTurnieju.length;i++) {
			if(uczestnicyTurnieju[i].getWynik() < uczestnicyTurnieju[indeksNajlepszego].getWynik()) {
				indeksNajlepszego = i;
			}
		}
		
		return uczestnicyTurnieju[indeksNajlepszego];
	}
	

	
	public static OsobnikPopulacji[] krzyzowanie(OsobnikPopulacji r1, OsobnikPopulacji r2) throws Exception {
		int indeksPodzialu = (int) (r1.getZadania().size()*wspolczynnikPodzialu);
		List<Zadanie> zadaniaDziecka1tmp = new LinkedList<>();//listy pomocnicze z zadaniami - tymi samymi obiektami co maja rodzice
		List<Zadanie> zadaniaDziecka2tmp = new LinkedList<>();
		for(int i=0;i<indeksPodzialu;i++) {
			zadaniaDziecka1tmp.add( r1.getZadania().get(i));
			zadaniaDziecka2tmp.add( r2.getZadania().get(i));
		}
		List<Zadanie> brakujaceZadaniaDziecka1 = new LinkedList<>();
		List<Zadanie> brakujaceZadaniaDziecka2 = new LinkedList<>();
		
		for(Zadanie zadanie: r1.getZadania()) {//bierze od rodzica1 zadania, których jeszcze mu brakuje
			if(!zadaniaDziecka1tmp.contains(zadanie)) {
				brakujaceZadaniaDziecka1.add(zadanie);
			}
		}
		
		for(Zadanie zadanie: r2.getZadania()) {//i dodaje te brakujace zadania w kolejnoœci takiej jak wystêpuj¹ w rodzicu 2
			for(int i=0;i<brakujaceZadaniaDziecka1.size();i++) {
				if(brakujaceZadaniaDziecka1.get(i).getNumerZadania()==zadanie.getNumerZadania()) {
					zadaniaDziecka1tmp.add(zadanie);
				}
			}
		}
		
		for(Zadanie zadanie: r2.getZadania()) {
			if(!zadaniaDziecka2tmp.contains(zadanie)) {
				brakujaceZadaniaDziecka2.add(zadanie);
			}
		}

		for(Zadanie zadanie: r1.getZadania()) {
			for(int i=0;i<brakujaceZadaniaDziecka2.size();i++) {
				if(brakujaceZadaniaDziecka2.get(i).getNumerZadania()==zadanie.getNumerZadania()) {
					zadaniaDziecka2tmp.add(zadanie);
				}
			}
		}
		
		List<Zadanie> zadaniaDziecka1 = new LinkedList<>();//listy ostateczne z nowymi, sklonowanymi zadaniami
		List<Zadanie> zadaniaDziecka2 = new LinkedList<>();
		
		for (Zadanie zadanie : zadaniaDziecka1tmp) {//dodaje na listy zadan dzieci sklonowane obiekty
			zadaniaDziecka1.add((Zadanie)zadanie.clone());
		}
		for (Zadanie zadanie : zadaniaDziecka2tmp) {
			zadaniaDziecka2.add((Zadanie)zadanie.clone());
		}
		
		List<Blok> maszyna1Dziecka1 = new LinkedList<>();
		List<Blok> maszyna2Dziecka1 = new LinkedList<>();
		
		List<Blok> maszyna1Dziecka2 = new LinkedList<>();
		List<Blok> maszyna2Dziecka2 = new LinkedList<>();
		
		int wynikDziecka1 = 0;
		int wynikDziecka2 = 0;
		
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!				
//					for (Zadanie zad : zadanieDziecka1) {
//						zad.wyczyszcOperacje();//ustawia czas startu = 0 i cz. praktyczny = czas trwania  dla obu operacji
//					}
//					for (Zadanie zad : zadanieDziecka2) {
//						zad.wyczyszcOperacje();//ustawia czas startu = 0 i cz. praktyczny = czas trwania  dla obu operacji
//					}
		
		
		try {
			wynikDziecka1 = generujRozwiazanie2(zadaniaDziecka1, maszyna1Dziecka1, maszyna2Dziecka1);
		
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		
		try {
			wynikDziecka2 = generujRozwiazanie2(zadaniaDziecka2, maszyna1Dziecka2, maszyna2Dziecka2);
			
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		} 
		
		OsobnikPopulacji dziecko1 = new OsobnikPopulacji(zadaniaDziecka1,maszyna1Dziecka1,maszyna2Dziecka1,wynikDziecka1);
		OsobnikPopulacji dziecko2 = new OsobnikPopulacji(zadaniaDziecka2,maszyna1Dziecka2,maszyna2Dziecka2,wynikDziecka2);
		OsobnikPopulacji[] wynik = new OsobnikPopulacji[2];
		wynik[0] = dziecko1;
		wynik[1] = dziecko2;
		return wynik;
		
	}
	
	
	public static void mutacja(OsobnikPopulacji osobnik) {
		Random r = new Random();
		double prawdopodobienstwo = r.nextDouble();
		if(prawdopodobienstwo <= wspolczynnikMutacji) {
			int indeks1 = r.nextInt(osobnik.getZadania().size());
			int indeks2 = -1;
			do {
				indeks2 = r.nextInt(osobnik.getZadania().size());
			}while(indeks1 == indeks2);
			
			Collections.swap(osobnik.getZadania(), indeks1, indeks2);
			
			int wynik = 0;
			
			try {
				wynik = generujRozwiazanie2(osobnik.getZadania(), osobnik.getMaszyna1(), osobnik.getMaszyna2());
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			} 
			osobnik.setWynik(wynik);
		}
	}
	
	public static OsobnikPopulacji najlepszyZPopulacji(Populacja populacja) {
		int indeks = 0;
		for(int i=0;i<populacja.getListaOsobnikow().size();i++) {
			if(populacja.getListaOsobnikow().get(i).getWynik() < populacja.getListaOsobnikow().get(indeks).getWynik()) {
				indeks = i;
			}
		}
		return populacja.getListaOsobnikow().get(indeks);
	}
	
	public static OsobnikPopulacji najgorszyZPopulacji(Populacja populacja) {
		int indeks = 0;
		for(int i=0;i<populacja.getListaOsobnikow().size();i++) {
			if(populacja.getListaOsobnikow().get(i).getWynik() > populacja.getListaOsobnikow().get(indeks).getWynik()) {
				indeks = i;
			}
		}
		return populacja.getListaOsobnikow().get(indeks);
	}
	
	
	
	
	public static int generujRozwiazanie2(List<Zadanie> listaZadan, List<Blok> maszyna1, List<Blok> maszyna2) throws CloneNotSupportedException {
		maszyna1.clear();
		maszyna2.clear();
		
		int czasM1 = 0;
		int czasM2 = 0;
		Maintenance tmpPrzerwa;
		
		
		for(int i=0;i<listaZadan.size();i++) {
			
			int licznikPrzerwPrzerywajacychOperacje = 0;
			Zadanie tmp = listaZadan.get(i);
			
			//najpierw pierwsza operacja zadania
			int czasTrwania1 = tmp.getOperacje()[0].getCzasTrwania();
			int maszynaOp1 = tmp.getOperacje()[0].getMaszyna();	
			int czasTrwania2 = tmp.getOperacje()[1].getCzasTrwania();
			int maszynaOp2 = tmp.getOperacje()[1].getMaszyna();
			if(maszynaOp1==1) {
				
				while ((tmpPrzerwa = sprawdzCzyNieMaPrzerwy(czasM1, 1, 1)) != null) {
	                maszyna1.add(tmpPrzerwa);
	                czasM1 += tmpPrzerwa.getCzasTrwania();
	            }
				
				do {
				if((tmpPrzerwa=sprawdzCzyNieMaPrzerwy(czasM1,czasTrwania1,maszynaOp1))==null) {//jesli nie ma na drodze przerwy
					tmp.getOperacje()[0].setCzasStartu(czasM1);
					tmp.getOperacje()[0].setCzasTrwaniaPraktyczny(czasTrwania1);
					maszyna1.add(tmp.getOperacje()[0]);
					czasM1+=czasTrwania1;
					czasTrwania1=0;		
				}else{//jesli jest na drodze przerwa
				if(licznikPrzerwPrzerywajacychOperacje==0){
					czasTrwania1 = GeneratorInstancji.sufit((double)czasTrwania1 + czasTrwania1*Main.kara);
					int czasDoPrzerwy = tmpPrzerwa.getCzasStartu()-czasM1;
					Zadanie tmp2 = (Zadanie) tmp.clone();
					tmp2.getOperacje()[0].setCzasTrwaniaPraktyczny(czasDoPrzerwy);
					tmp2.getOperacje()[0].setCzasStartu(czasM1);
					maszyna1.add(tmp2.getOperacje()[0]);
					tmp.getOperacje()[0].setCzasTrwaniaPraktyczny(czasTrwania1 - czasDoPrzerwy);
					czasM1+=czasDoPrzerwy;
					czasTrwania1-=czasDoPrzerwy;
					licznikPrzerwPrzerywajacychOperacje++;
					
					maszyna1.add(tmpPrzerwa);
					czasM1+=tmpPrzerwa.getCzasTrwania();
					
				}else{
					int czasDoPrzerwy = tmpPrzerwa.getCzasStartu()-czasM1;
					Zadanie tmp2 = (Zadanie) tmp.clone();
					tmp2.getOperacje()[0].setCzasTrwaniaPraktyczny(czasDoPrzerwy);
					tmp2.getOperacje()[0].setCzasStartu(czasM1);
					maszyna1.add(tmp2.getOperacje()[0]);
					tmp.getOperacje()[0].setCzasTrwaniaPraktyczny(czasTrwania1 - czasDoPrzerwy);
					czasM1+=czasDoPrzerwy;
					czasTrwania1-=czasDoPrzerwy;
					licznikPrzerwPrzerywajacychOperacje++;
					maszyna1.add(tmpPrzerwa);
					czasM1+=tmpPrzerwa.getCzasTrwania();
				}

			}
		}while(czasTrwania1 > 0);
				
				if (i == 0) {
	                do {
	   				 if((tmpPrzerwa=sprawdzCzyNieMaPrzerwy(czasM2,czasM1-czasM2,2))==null){
	   				 		Idle idle = new Idle(czasM1-czasM2, 2);
	   				 		idle.setCzasStartu(czasM2);
	   		                maszyna2.add(idle);
	   		                czasM2 += idle.getCzasTrwania();
	   				 	}else {
	   				 		Idle idle = new Idle(tmpPrzerwa.getCzasStartu() - czasM2,2);
	   				 		idle.setCzasStartu(czasM2);
	   				 		maszyna2.add(idle);
	   				 		maszyna2.add(tmpPrzerwa);
	   				 		czasM2+=idle.getCzasTrwania()+tmpPrzerwa.getCzasTrwania();			 		
	   				 	
	   				 	}
	   				 }while(czasM2<czasM1);
	                
	            }	
			 
			 if (czasM1 - czasM2 > 0) {
				 do {
				 if((tmpPrzerwa=sprawdzCzyNieMaPrzerwy(czasM2,czasM1-czasM2,2))==null){
				 		Idle idle = new Idle(czasM1-czasM2, 2);
				 		idle.setCzasStartu(czasM2);
		                maszyna2.add(idle);
		                czasM2 += idle.getCzasTrwania();
				 	}else {
				 		Idle idle = new Idle(tmpPrzerwa.getCzasStartu() - czasM2,2);
				 		idle.setCzasStartu(czasM2);
				 		maszyna2.add(idle);
				 		maszyna2.add(tmpPrzerwa);
				 		czasM2+=idle.getCzasTrwania()+tmpPrzerwa.getCzasTrwania();			 		
				 	
				 	}
				 }while(czasM2<czasM1);
	            }
				 
				 
				 while ((tmpPrzerwa = sprawdzCzyNieMaPrzerwy(czasM2, 1, 2)) != null) {
		                maszyna2.add(tmpPrzerwa);
		                czasM2 += tmpPrzerwa.getCzasTrwania();
		            }
				 
				 do {
						if((tmpPrzerwa=sprawdzCzyNieMaPrzerwy(czasM2,czasTrwania2,maszynaOp2))==null) {//jesli nie ma na drodze przerwy
							tmp.getOperacje()[1].setCzasStartu(czasM2);
							tmp.getOperacje()[1].setCzasTrwaniaPraktyczny(czasTrwania2);
							maszyna2.add(tmp.getOperacje()[1]);
							czasM2+=czasTrwania2;
							czasTrwania2=0;		
						}else{//jesli jest na drodze przerwa
						if(licznikPrzerwPrzerywajacychOperacje==0){
							czasTrwania2 = GeneratorInstancji.sufit((double)czasTrwania2 + czasTrwania2*Main.kara);
							int czasDoPrzerwy = tmpPrzerwa.getCzasStartu()-czasM2;
							Zadanie tmp2 = (Zadanie) tmp.clone();
							tmp2.getOperacje()[1].setCzasTrwaniaPraktyczny(czasDoPrzerwy);
							tmp2.getOperacje()[1].setCzasStartu(czasM2);
							maszyna2.add(tmp2.getOperacje()[1]);
							tmp.getOperacje()[1].setCzasTrwaniaPraktyczny(czasTrwania2 - czasDoPrzerwy);
							czasM2+=czasDoPrzerwy;
							czasTrwania2-=czasDoPrzerwy;
							licznikPrzerwPrzerywajacychOperacje++;
							
							maszyna2.add(tmpPrzerwa);
							czasM2+=tmpPrzerwa.getCzasTrwania();
							
						}else{
							int czasDoPrzerwy = tmpPrzerwa.getCzasStartu()-czasM2;
							Zadanie tmp2 = (Zadanie) tmp.clone();
							tmp2.getOperacje()[1].setCzasTrwaniaPraktyczny(czasDoPrzerwy);
							tmp2.getOperacje()[1].setCzasStartu(czasM2);
							maszyna2.add(tmp2.getOperacje()[1]);
							tmp.getOperacje()[1].setCzasTrwaniaPraktyczny(czasTrwania2 - czasDoPrzerwy);
							czasM2+=czasDoPrzerwy;
							czasTrwania2-=czasDoPrzerwy;
							licznikPrzerwPrzerywajacychOperacje++;
							maszyna2.add(tmpPrzerwa);
							czasM2+=tmpPrzerwa.getCzasTrwania();
						}
					}
				}while(czasTrwania2 > 0);	 

		}else {//jesli maszyna operacji1 jest rowna 2
			
			while ((tmpPrzerwa = sprawdzCzyNieMaPrzerwy(czasM2, 1, 2)) != null) {
                maszyna2.add(tmpPrzerwa);
                czasM2 += tmpPrzerwa.getCzasTrwania();
            }
			
			do {//operacja 1 na maszyne2
				if((tmpPrzerwa=sprawdzCzyNieMaPrzerwy(czasM2,czasTrwania1,maszynaOp1))==null) {//jesli nie ma na drodze przerwy
					tmp.getOperacje()[0].setCzasStartu(czasM2);
					tmp.getOperacje()[0].setCzasTrwaniaPraktyczny(czasTrwania1);
					maszyna2.add(tmp.getOperacje()[0]);
					czasM2+=czasTrwania1;
					czasTrwania1=0;		
				}else{//jesli jest na drodze przerwa
				if(licznikPrzerwPrzerywajacychOperacje==0){
					czasTrwania1 = GeneratorInstancji.sufit((double)czasTrwania1 + czasTrwania1*Main.kara);
					int czasDoPrzerwy = tmpPrzerwa.getCzasStartu()-czasM2;
					Zadanie tmp2 = (Zadanie) tmp.clone();
					tmp2.getOperacje()[0].setCzasTrwaniaPraktyczny(czasDoPrzerwy);
					tmp2.getOperacje()[0].setCzasStartu(czasM2);
					maszyna2.add(tmp2.getOperacje()[0]);
					tmp.getOperacje()[0].setCzasTrwaniaPraktyczny(czasTrwania1 - czasDoPrzerwy);
					czasM2+=czasDoPrzerwy;
					czasTrwania1-=czasDoPrzerwy;
					licznikPrzerwPrzerywajacychOperacje++;
					
					maszyna2.add(tmpPrzerwa);
					czasM2+=tmpPrzerwa.getCzasTrwania();
					
				}else{
					int czasDoPrzerwy = tmpPrzerwa.getCzasStartu()-czasM2;
					Zadanie tmp2 = (Zadanie) tmp.clone();
					tmp2.getOperacje()[0].setCzasTrwaniaPraktyczny(czasDoPrzerwy);
					tmp2.getOperacje()[0].setCzasStartu(czasM2);
					maszyna2.add(tmp2.getOperacje()[0]);
					tmp.getOperacje()[0].setCzasTrwaniaPraktyczny(czasTrwania1 - czasDoPrzerwy);
					czasM2+=czasDoPrzerwy;
					czasTrwania1-=czasDoPrzerwy;
					licznikPrzerwPrzerywajacychOperacje++;
					maszyna2.add(tmpPrzerwa);
					czasM2+=tmpPrzerwa.getCzasTrwania();
				}
			}
		}while(czasTrwania1 > 0);
		
			 if (i == 0) {
	                do {
	   				 if((tmpPrzerwa=sprawdzCzyNieMaPrzerwy(czasM1,czasM2-czasM1,1))==null){
	   				 		Idle idle = new Idle(czasM2-czasM1, 1);
	   				 		idle.setCzasStartu(czasM1);
	   		                maszyna1.add(idle);
	   		                czasM1 += idle.getCzasTrwania();
	   				 	}else {
	   				 		Idle idle = new Idle(tmpPrzerwa.getCzasStartu() - czasM1,1);
	   				 		idle.setCzasStartu(czasM1);
	   				 		maszyna1.add(idle);
	   				 		maszyna1.add(tmpPrzerwa);
	   				 		czasM1+=idle.getCzasTrwania()+tmpPrzerwa.getCzasTrwania();			 		
	   				 	
	   				 	}
	   				 }while(czasM1<czasM2);
	                
	            }	
			 
			 if (czasM2 - czasM1 > 0) {
				 do {
				 if((tmpPrzerwa=sprawdzCzyNieMaPrzerwy(czasM1,czasM2-czasM1,1))==null){
				 		Idle idle = new Idle(czasM2-czasM1, 1);
				 		idle.setCzasStartu(czasM1);
		                maszyna1.add(idle);
		                czasM1 += czasM2 - czasM1;
				 	}else {
				 		Idle idle = new Idle(tmpPrzerwa.getCzasStartu() - czasM1,1);
				 		idle.setCzasStartu(czasM1);
				 		maszyna1.add(idle);
				 		maszyna1.add(tmpPrzerwa);
				 		czasM1+=idle.getCzasTrwania()+tmpPrzerwa.getCzasTrwania();			 		
				 	
				 	}
				 }while(czasM1<czasM2);
	            }
			 
			 while ((tmpPrzerwa = sprawdzCzyNieMaPrzerwy(czasM1, 1, 1)) != null) {
	                maszyna1.add(tmpPrzerwa);
	                czasM1 += tmpPrzerwa.getCzasTrwania();
	            }
			 
			 do {//operacja 2 na maszyne 1
					if((tmpPrzerwa=sprawdzCzyNieMaPrzerwy(czasM1,czasTrwania2,maszynaOp2))==null) {//jesli nie ma na drodze przerwy
						tmp.getOperacje()[1].setCzasStartu(czasM1);
						tmp.getOperacje()[1].setCzasTrwaniaPraktyczny(czasTrwania2);
						maszyna1.add(tmp.getOperacje()[1]);
						czasM1+=czasTrwania2;
						czasTrwania2=0;		
					}else{//jesli jest na drodze przerwa
					if(licznikPrzerwPrzerywajacychOperacje==0){
						czasTrwania2 = GeneratorInstancji.sufit((double)czasTrwania2 + czasTrwania2*Main.kara);
						int czasDoPrzerwy = tmpPrzerwa.getCzasStartu()-czasM1;
						Zadanie tmp2 = (Zadanie) tmp.clone();
						tmp2.getOperacje()[1].setCzasTrwaniaPraktyczny(czasDoPrzerwy);
						tmp2.getOperacje()[1].setCzasStartu(czasM1);
						maszyna1.add(tmp2.getOperacje()[1]);
						tmp.getOperacje()[1].setCzasTrwaniaPraktyczny(czasTrwania2 - czasDoPrzerwy);
						czasM1+=czasDoPrzerwy;
						czasTrwania2-=czasDoPrzerwy;
						licznikPrzerwPrzerywajacychOperacje++;
						
						maszyna1.add(tmpPrzerwa);
						czasM1+=tmpPrzerwa.getCzasTrwania();
						
					}else{
						int czasDoPrzerwy = tmpPrzerwa.getCzasStartu()-czasM1;
						Zadanie tmp2 = (Zadanie) tmp.clone();
						tmp2.getOperacje()[1].setCzasTrwaniaPraktyczny(czasDoPrzerwy);
						tmp2.getOperacje()[1].setCzasStartu(czasM1);
						maszyna1.add(tmp2.getOperacje()[1]);
						tmp.getOperacje()[1].setCzasTrwaniaPraktyczny(czasTrwania2 - czasDoPrzerwy);
						czasM1+=czasDoPrzerwy;
						czasTrwania2-=czasDoPrzerwy;
						licznikPrzerwPrzerywajacychOperacje++;
						maszyna1.add(tmpPrzerwa);
						czasM1+=tmpPrzerwa.getCzasTrwania();
					}

				}
			}while(czasTrwania2 > 0);
		
			
			
		}
			
		}
		int miejsceZakonczenia = czasM1 > czasM2 ? czasM1 : czasM2;
		
		return miejsceZakonczenia;
	}	
	
	
	public static Maintenance sprawdzCzyNieMaPrzerwy(int start, int czasTrwania, int maszyna) {
	        for (int i = 0; i < listaPrzerw.size(); i++) {
	            Maintenance przerwa = listaPrzerw.get(i);
	            if (przerwa.getMaszyna() != maszyna) continue;

	            
	            if ((start >= przerwa.getCzasStartu() && start < przerwa.getCzasStartu()+przerwa.getCzasTrwania()) ||
	                    (przerwa.getCzasStartu() >= start && przerwa.getCzasStartu() < start + czasTrwania)) {
	                return przerwa;
	            }
	        }
	        return null;
	    }


	public static Populacja tworzPierwszaPopulacje() {
		Populacja populacja = new Populacja();
		
		for(int i=0;i<wielkoscPopulacji;i++) {
			List<Zadanie> tmpListaZadan = new LinkedList<>();
			List<Blok> tmpMaszyna1 = new LinkedList<>();
			List<Blok> tmpMaszyna2 = new LinkedList<>();
			for(int j=0;j<listaZadan.size();j++) {
				try {
					tmpListaZadan.add((Zadanie) listaZadan.get(j).clone());
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
			}
			Collections.shuffle(tmpListaZadan);
			
			int wynik = 0;
			
			try {
				wynik = generujRozwiazanie2(tmpListaZadan, tmpMaszyna1, tmpMaszyna2);
				
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			} 

			OsobnikPopulacji osobnik = new OsobnikPopulacji(tmpListaZadan,tmpMaszyna1,tmpMaszyna2,wynik);
			populacja.getListaOsobnikow().add(osobnik);
		}
		return populacja;
	}
	
	
	public static List<Zadanie> getListaZadan() {
		return listaZadan;
	}


	public static void setListaZadan(List<Zadanie> listaZadan) {
		Algorytm.listaZadan = listaZadan;
	}


	public static List<Maintenance> getListaPrzerw() {
		return listaPrzerw;
	}


	public static void setListaPrzerw(List<Maintenance> listaPrzerw) {
		Algorytm.listaPrzerw = listaPrzerw;
	}

	public static int getWielkoscPopulacji() {
		return wielkoscPopulacji;
	}

	public static void setWielkoscPopulacji(int wielkoscPopulacji) {
		Algorytm.wielkoscPopulacji = wielkoscPopulacji;
	}

	public static double getWspolczynnikMutacji() {
		return wspolczynnikMutacji;
	}

	public static void setWspolczynnikMutacji(double wspolczynnikMutacji) {
		Algorytm.wspolczynnikMutacji = wspolczynnikMutacji;
	}



	public static int getLiczbaKandydatowDoTurnieju() {
		return liczbaKandydatowDoTurnieju;
	}


	public static void setLiczbaKandydatowDoTurnieju(int liczbaKandydatowDoTurnieju) {
		Algorytm.liczbaKandydatowDoTurnieju = liczbaKandydatowDoTurnieju;
	}


	public static int getLiczbaZwyciezcowTurniejow() {
		return liczbaZwyciezcowTurniejow;
	}


	public static void setLiczbaZwyciezcowTurniejow(int liczbaZwyciezcowTurniejow) {
		Algorytm.liczbaZwyciezcowTurniejow = liczbaZwyciezcowTurniejow;
	}


	public static double getWspolczynnikPodzialu() {
		return wspolczynnikPodzialu;
	}


	public static void setWspolczynnikPodzialu(double wspolczynnikPodzialu) {
		Algorytm.wspolczynnikPodzialu = wspolczynnikPodzialu;
	}
}
