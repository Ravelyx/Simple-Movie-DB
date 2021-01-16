import java.util.Scanner;

/*
 * Klassen hanterar allt som har med att indexera databasen att g�ra.
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
	 * Metoden tar filmlistan och betygslistan och s�ker efter film med
	 * hj�lp av anv�ndarens inmatning.
	 */
	public void searchDatabaseByTitles(String[] movieList, int[] gradeList) {
		System.out.println("Skriv in titeln p� filmen du s�ker efter.");
		
		while (!emptyEntryCheck) {
			titleSearch = userInput.nextLine().toLowerCase().trim();
			
			if (titleSearch.isEmpty()) {
				System.out.println("F�ltet f�r inte vara tomt!");
			} else {
				emptyEntryCheck = true;
			}
		}
		emptyEntryCheck = false;
		
		/*
		 * Itererar och kollar om ordet som anv�ndaren matat in finns
		 * i index hos filmlistan. D�refter skrivs titel och tillh�rande betyg upp.
		 * Eftersom element p� film och betyg �r samma p� arrayen movieList
		 * och gradeList kan samma index "i" anv�ndas f�r att visa upp b�da.
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
		 * Om inget resultat hittas i loop f�rblir noSearchResultCheck noll och
		 * s�ledes spelas denna if statement upp.
		 */
		if (noSearchResultCheck == 0) {
			System.out.println("Inga resultat kunde hittas!");
			System.out.println("-----------------");
		}
		/*
		 * S�tter noSearchResultCheck till noll s� att man kan anv�nda
		 * metoden igen p� ett korrekt s�tt.
		 */
		noSearchResultCheck = 0;
	}
	
	/*
	 * Metoden tar filmlistan och betygslistan och s�ker i databasen
	 * efter filmer beroende p� minimibetyg som s�tts av anv�ndaren.
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
								+ " och " + Database.MAXIMUM_GRADE + " m�ste matas in som betyg!");
						userInput.nextLine();
					} else {
						numberCheck = true;
						userInput.nextLine();
					}
				}
				gradeEntryCheck = true;
			} catch (Exception e) {
				System.out.println("Du m�ste mata in en HELTALSSIFFRA som betyg!");
				userInput.nextLine();
			}
		}
		
		/*
		 * Itererar och kollar ifall siffran som anv�ndaren matat in
		 * matchar siffran fr�n gradelist. Ifall betyget fr�n gradeList
		 * �r h�gre �n eller lika med anv�ndarens inmatning visas
		 * filmtiteln och betyget upp.
		 */
		for (int i = 0; i < gradeList.length; i++) {
			
			if (gradeList[i] >= gradeSearch) {
				noSearchResultCheck++;
				
				/*
				 * Ifall en annan programmerare justerar Database.MAXIMUM_GRADE
				 * eller Database.MINIMUM_GRADE kommer ju filens gamla betyg inte
				 * f�r�ndras. F�r att motverka att vi f�r fall s�som
				 * "Betyg: 10/5" eller att det st�r "Betyg: 1/5" n�r
				 * Database.MINIMUM_GRADE = 2 s� har nedanst�ende l�sning
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
		 * S�tter error handling variablerna till false s� att man kan
		 * anv�nda metoden igen p� ett korrekt s�tt. S�tter
		 * ocks� noSearchResultCheck till noll.
		 */
		noSearchResultCheck = 0;
		gradeEntryCheck = false;
		numberCheck = false;
	}
}
