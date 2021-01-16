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
 * Klassen hanterar alla filrelaterade operationer. I vårt fall handlar
 * det om att importera in filmlista och tillhörande betyg samt lägga
 * till nya filmer.
 */
public class FileManager {
	
	/*
	 * 2 scannervariabler och 3 booleanvariabler skapas.
	 */
	private Scanner userInput = new Scanner(System.in), readFile = null;
	private boolean errorHandler = false, numberCheck = false, titleCheck = false;
	
	/*
	 * Metoden tar in en siffra beroende på om man vill importera
	 * film eller betyg. Siffer-sättning sker automatiskt av programmet
	 * i Database-klassen i metoden handleFiles().
	 */
	public String[] importMovies(int importChoice) {
		try {
			readFile = new Scanner(new FileReader("fil\\DatabaseInfo.txt"));
		} catch (FileNotFoundException e) {
			System.out.println("Filen kunde inte hittas! Är filen på rätt ställe?");
		}
		
		List<String> outputList = new ArrayList<String>();
		
		/*
		 * Använder en delimiter för att split vid kommatecknet i DatabaseInfo.txt-filen.
		 * Efter split läggs det som är till höger om kommatecknet in som sitt eget element.
		 * Om vi därför har 7 rader på filen DatabaseInfo.txt får vi totalt 14 element.
		 * Detta betyder att vart andra element skiftar mellan titel och betyg.
		 */
		while (readFile.hasNext()) {
			outputList.add(readFile.useDelimiter("[,\n]").next().trim());
		}
					
		readFile.close();
		
		/*
		 * importChoice == 0 står för filmimportering. Betygen tas bort
		 * här. Funkar på sådant sätt att den först tar bort element 0 och
		 * sedan skiftar nästa element bakåt (d.v.s element 1 blir element 0
		 * under andra iterationen, element 2 blir element 1,
		 * element 3 blir element 2 o.s.v.). Detta betyder i praktiken att varje
		 * andra element raderas med start på element "i" eftersom elementen
		 * hela tiden förflyttas bak med 1 vid varje iteration. Fungerar likadant på
		 * importChoice == 1 längre ned.
		 */
		if (importChoice == 0) {
			for (int i = 1; i < outputList.size(); i++) {
				outputList.remove(i);
			}
			
			/*
			 * importChoice == 1 står för betygsimportering. Filmtitlarna
			 * tas bort här.
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
	 * Metoden lägger till nya filmer med hjälp av användarens inmatning.
	 */
	public void addMovie() {
		String movie = null;
		int grade = 0;

		System.out.println("Skriv namnet på filmen som du vill lägga till. "
				+ "Skriv " + Database.CANCEL_NEW_MOVIE + " för att avbryta.");
		
		/*
		 * Frågar användaren om inmatning av filmtitel.
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
		 * If statement som avbryter "lägg till film" ifall CANCEL_NEW_MOVIE skrivs in.
		 */
		if (movie.equalsIgnoreCase(Database.CANCEL_NEW_MOVIE)) {
			System.out.println("-----------------");
			return;
		}
		
		/*
		 * Frågar användaren om inmatning av filmens betyg.
		 */
		System.out.println("Vad är filmens betyg (" + Database.MINIMUM_GRADE 
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
				System.out.println("Du måste välja en HELTALSSIFFRA! Testa igen!");
				userInput.nextLine();
			}
		}
		
		/*
		 * Lägger till användarens inmatning till fil.
		 */
		 try {
			FileWriter fileWriter = new FileWriter("fil\\DatabaseInfo.txt", true);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			PrintWriter writeToFile = new PrintWriter(bufferedWriter);
			writeToFile.println(movie + ", " + grade);
			writeToFile.close();
			System.out.println("Lade till filmen " + movie + " till fil på ett korrekt sätt.");
			System.out.println("-----------------");
		} catch (IOException e) {
			System.out.println("Filen kunde inte skrivas till! Är filen på rätt plats?");
		}
		
		/*
		 * userInput.nextLine() tar bort information från scannern
		 * (hoppar till nextLine) så att man kan använda metoden igen.
		 */
		numberCheck = false;
		errorHandler = false;
		userInput.nextLine();
		 
	}
}
