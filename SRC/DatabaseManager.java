import java.util.Scanner;

/*
 * Klassen hanterar allt som har med att indexera databasen att göra.
 */
public class DatabaseManager {
	
	/*
	 * Skapar 1 scanner-, 2 int-, 1 string- och 3 booleanvariabler.
	 */
	private Scanner userInput = new Scanner(System.in);
	private int noSearchResultCheck = 0, gradeSearch = 0;
	private String titleSearch = "";
	private boolean numberCheck = false, gradeEntryCheck = false, emptyEntryCheck = false;
	
	/*
	 * Metoden tar filmlistan och betygslistan och söker efter film med
	 * hjälp av användarens inmatning.
	 */
	public void searchDatabaseByTitles(String[] movieList, int[] gradeList) {
		System.out.println("Skriv in titeln på filmen du söker efter.");
		
		while (!emptyEntryCheck) {
			titleSearch = userInput.nextLine().toLowerCase().trim();
			
			if (titleSearch.isEmpty()) {
				System.out.println("Fältet får inte vara tomt!");
			} else {
				emptyEntryCheck = true;
			}
		}
		emptyEntryCheck = false;
		
		/*
		 * Itererar och kollar om ordet som användaren matat in finns
		 * i index hos filmlistan. Därefter skrivs titel och tillhörande betyg upp.
		 * Eftersom element på film och betyg är samma på arrayen movieList
		 * och gradeList kan samma index "i" användas för att visa upp båda.
		 */
		
		for (int i = 0; i < movieList.length; i++) {
			if (movieList[i].toLowerCase().contains(titleSearch)) {
				noSearchResultCheck++;
				System.out.println("Titel: " + movieList[i] 
						+ " - Betyg: " + gradeList[i] + "/" + Database.MAXIMUM_GRADE);
				}
			}
		
		System.out.println("-----------------");
		
		/*
		 * Om inget resultat hittas i loop förblir noSearchResultCheck noll och
		 * således spelas denna if statement upp.
		 */
		if (noSearchResultCheck == 0) {
			System.out.println("Inga resultat kunde hittas!");
			System.out.println("-----------------");
		}
		/*
		 * Sätter noSearchResultCheck till noll så att man kan använda
		 * metoden igen på ett korrekt sätt.
		 */
		noSearchResultCheck = 0;
	}
	
	/*
	 * Metoden tar filmlistan och betygslistan och söker i databasen
	 * efter filmer beroende på minimibetyg som sätts av användaren.
	 */
	public void searchDatabaseByGrades(int[] gradeList, String[] movieList) {
		System.out.println("Skriv in ett minimibetyg mellan " + Database.MINIMUM_GRADE 
				+ " och " + Database.MAXIMUM_GRADE + ".");
		
		while (!gradeEntryCheck) {
			try {
				while (!numberCheck) {
					gradeSearch = userInput.nextInt();
					
					if (gradeSearch < Database.MINIMUM_GRADE || gradeSearch > Database.MAXIMUM_GRADE) {
						System.out.println("En heltalssiffra MELLAN " + Database.MINIMUM_GRADE 
								+ " och " + Database.MAXIMUM_GRADE + " måste matas in som betyg!");
						userInput.nextLine();
					} else {
						numberCheck = true;
						userInput.nextLine();
					}
				}
				gradeEntryCheck = true;
			} catch (Exception e) {
				System.out.println("Du måste mata in en HELTALSSIFFRA som betyg!");
				userInput.nextLine();
			}
		}
		
		/*
		 * Itererar och kollar ifall siffran som användaren matat in
		 * matchar siffran från gradelist. Ifall betyget från gradeList
		 * är högre än eller lika med användarens inmatning visas
		 * filmtiteln och betyget upp.
		 */
		for (int i = 0; i < gradeList.length; i++) {
			
			if (gradeList[i] >= gradeSearch) {
				noSearchResultCheck++;
				
				/*
				 * Ifall en annan programmerare justerar Database.MAXIMUM_GRADE
				 * eller Database.MINIMUM_GRADE kommer ju filens gamla betyg inte
				 * förändras. För att motverka att vi får fall såsom
				 * "Betyg: 10/5" eller att det står "Betyg: 1/5" när
				 * Database.MINIMUM_GRADE = 2 så har nedanstående lösning
				 * implementerats.
				 */
				if (gradeList[i] > Database.MAXIMUM_GRADE) {
					gradeList[i] = Database.MAXIMUM_GRADE;
				} else if (gradeList[i] < Database.MINIMUM_GRADE) {
					gradeList[i] = Database.MINIMUM_GRADE;
				}
				
				System.out.println("Titel: " + movieList[i] + " - Betyg: "
						+ gradeList[i] + "/" + Database.MAXIMUM_GRADE);
			}
		}
		
		System.out.println("-----------------");
		
		if (noSearchResultCheck == 0) {
			System.out.println("Inga resultat kunde hittas!");
			System.out.println("-----------------");
		}
		
		/*
		 * Sätter error handling variablerna till false så att man kan
		 * använda metoden igen på ett korrekt sätt. Sätter
		 * också noSearchResultCheck till noll.
		 */
		noSearchResultCheck = 0;
		gradeEntryCheck = false;
		numberCheck = false;
	}
}
