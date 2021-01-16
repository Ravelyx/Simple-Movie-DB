import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
 * Klassen hanterar alla filrelaterade operationer. I v�rt fall handlar
 * det om att importera in filmlista och tillh�rande betyg samt l�gga
 * till nya filmer.
 */
public class FileManager {
	
	/*
	 * 2 scannervariabler och 3 booleanvariabler skapas.
	 */
	private Scanner userInput = new Scanner(System.in), readFile = null;
	private boolean errorHandler = false, numberCheck = false, titleCheck = false;
	
	/*
	 * Metoden tar in en siffra beroende p� om man vill importera
	 * film eller betyg. Siffer-s�ttning sker automatiskt av programmet
	 * i Database-klassen i metoden handleFiles().
	 */
	public String[] importMovies(int importChoice) {
		try {
			readFile = new Scanner(new FileReader("fil\\DatabaseInfo.txt"));
		} catch (FileNotFoundException e) {
			System.out.println("Filen kunde inte hittas! �r filen p� r�tt st�lle?");
		}
		
		List<String> outputList = new ArrayList<String>();
		
		/*
		 * Anv�nder en delimiter f�r att split vid kommatecknet i DatabaseInfo.txt-filen.
		 * Efter split l�ggs det som �r till h�ger om kommatecknet in som sitt eget element.
		 * Om vi d�rf�r har 7 rader p� filen DatabaseInfo.txt f�r vi totalt 14 element.
		 * Detta betyder att vart andra element skiftar mellan titel och betyg.
		 */
		while (readFile.hasNext()) {
			outputList.add(readFile.useDelimiter("[,\n]").next().trim());
		}
					
		readFile.close();
		
		/*
		 * importChoice == 0 st�r f�r filmimportering. Betygen tas bort
		 * h�r. Funkar p� s�dant s�tt att den f�rst tar bort element 0 och
		 * sedan skiftar n�sta element bak�t (d.v.s element 1 blir element 0
		 * under andra iterationen, element 2 blir element 1,
		 * element 3 blir element 2 o.s.v.). Detta betyder i praktiken att varje
		 * andra element raderas med start p� element "i" eftersom elementen
		 * hela tiden f�rflyttas bak med 1 vid varje iteration. Fungerar likadant p�
		 * importChoice == 1 l�ngre ned.
		 */
		if (importChoice == 0) {
			for (int i = 1; i < outputList.size(); i++) {
				outputList.remove(i);
			}
			
			/*
			 * importChoice == 1 st�r f�r betygsimportering. Filmtitlarna
			 * tas bort h�r.
			 */
		} else if (importChoice == 1) {
			for (int i = 0; i < outputList.size(); i++) {
				outputList.remove(i);				
			}
		}
		
		String[] outputArray = outputList.toArray(new String[0]);
		
		return outputArray;
	}
	
	/*
	 * Metoden l�gger till nya filmer med hj�lp av anv�ndarens inmatning.
	 */
	public void addMovie() {
		String movie = null;
		int grade = 0;

		System.out.println("Skriv namnet p� filmen som du vill l�gga till. "
				+ "Skriv " + Database.CANCEL_NEW_MOVIE + " f�r att avbryta.");
		
		/*
		 * Fr�gar anv�ndaren om inmatning av filmtitel.
		 */
		while (!titleCheck) {
			movie = userInput.nextLine().trim();
			
			if (movie.isEmpty()) {
				System.out.println("Filmens titel kan inte vara tom!");
			} else {
				titleCheck = true;
			}
		}
		
		titleCheck = false;
		
		/*
		 * If statement som avbryter "l�gg till film" ifall CANCEL_NEW_MOVIE skrivs in.
		 */
		if (movie.equalsIgnoreCase(Database.CANCEL_NEW_MOVIE)) {
			System.out.println("-----------------");
			return;
		}
		
		/*
		 * Fr�gar anv�ndaren om inmatning av filmens betyg.
		 */
		System.out.println("Vad �r filmens betyg (" + Database.MINIMUM_GRADE 
				+ "-" + Database.MAXIMUM_GRADE + ")?");
		
		while (!errorHandler) {
			try {
				while (!numberCheck) {
					grade = userInput.nextInt();
					
					if (grade < Database.MINIMUM_GRADE || grade > Database.MAXIMUM_GRADE) {
						System.out.println("Betyget kan enbart vara en heltalssiffra mellan " 
								+ Database.MINIMUM_GRADE + " och " + Database.MAXIMUM_GRADE + "!");
						userInput.nextLine();
					} else {
						numberCheck = true;
					}
				}
				errorHandler = true;
			} catch (Exception e) {
				System.out.println("Du m�ste v�lja en HELTALSSIFFRA! Testa igen!");
				userInput.nextLine();
			}
		}
		
		/*
		 * L�gger till anv�ndarens inmatning till fil.
		 */
		 try {
			FileWriter fileWriter = new FileWriter("fil\\DatabaseInfo.txt", true);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			PrintWriter writeToFile = new PrintWriter(bufferedWriter);
			writeToFile.println(movie + ", " + grade);
			writeToFile.close();
			System.out.println("Lade till filmen " + movie + " till fil p� ett korrekt s�tt.");
			System.out.println("-----------------");
		} catch (IOException e) {
			System.out.println("Filen kunde inte skrivas till! �r filen p� r�tt plats?");
		}
		
		/*
		 * userInput.nextLine() tar bort information fr�n scannern
		 * (hoppar till nextLine) s� att man kan anv�nda metoden igen.
		 */
		numberCheck = false;
		errorHandler = false;
		userInput.nextLine();
		 
	}
}
