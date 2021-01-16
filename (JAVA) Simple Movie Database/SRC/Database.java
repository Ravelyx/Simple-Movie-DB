import java.util.Arrays;
import java.util.Scanner;

/*
 * Programmet �r en filmdatabas som l�ter anv�ndaren mata in en siffra mellan
 * OPTIONCHOICE_MIN och OPTIONCHOICE_MAX. Den l�ter anv�ndaren s�ka databasen
 * med hj�lp av antingen titel eller betyg. Det g�r �ven att l�gga in filmer.
 */
public class Database {
	
	/*
	 * Instansvariabler som g�r det enklare att modifiera programmet.
	 * Kolla PDF-dokumentet och l�s koden f�r djupare insikt.
	 */
	public static final int MINIMUM_GRADE = 1;
	public static final int MAXIMUM_GRADE = 5;
	public static final int OPTIONCHOICE_MIN = 1;
	public static final int OPTIONCHOICE_MAX = 5;
	public static final String CANCEL_NEW_MOVIE = "avbrytX";
	
	/*
	 * Importerar in n�dv�ndiga klasser f�r att programmet ska fungera.
	 * Dessa hanterar filoperationer och indexering av databas.
	 */
	FileManager fileManager;
	DatabaseManager databaseManager;
	
	/*
	 * Skapar 1 scanner, 1 integer, 2 string arrays, 1 int array och 3 boolean variabler.
	 */
	private Scanner userInput = new Scanner(System.in);
	private int optionChoice = 0;
	private String [] movieList = null, gradeListTemp = null;
	private int[] gradeList = null;
	private boolean correctInputCheck = false, errorHandler = false, numberCheck = false;	
	
	/*
	 * Constructor-metod f�r att programmet ska k�ras s� fort launcher kallar p� den.
	 * Den v�lkomnar anv�ndaren, skapar objekt till �vriga klasser, importerar in
	 * lista p� filmer och betyg p� ett korrekt s�tt och startar UI.
	 */
	public Database() {
		welcome();
		init();
		handleFiles();
		UI();
	}
	
	private void welcome() {
		System.out.println("V�lkommen till filmdatabasen!");
	}
	
	/*
	 * Skapar objekt till �vriga klasser.
	 */
	private void init() {
		fileManager = new FileManager();
		databaseManager = new DatabaseManager();
	}
	
	/*
	 * Importerar in fil p� korrekt s�tt och utf�r n�dv�ndig casting.
	 * Hanterar ocks� uppdateringar av fil n�r nya filmer l�ggs till.
	 */
	private void handleFiles() {
		movieList = fileManager.importMovies(0);
		gradeListTemp = fileManager.importMovies(1);
		
		/*
		 * gradeList g�rs om till en int array fr�n en String array eftersom
		 * det bara �r siffror i den och detta underl�ttar j�mf�relse senare i programmet.
		 */
		gradeList = new int[gradeListTemp.length];
		gradeList = Arrays.stream(gradeListTemp).mapToInt(Integer::parseInt).toArray();
	}
	
	/*
	 * Hanterar all text och fr�gar anv�ndaren om inmatning mellan OPTIONCHOICE_MAX
	 * och OPTIONCHOICE_MIN. Vid korrekt inmatning skickas anv�ndaren vidare till
	 * choiceOutcome-metoden.
	 */
	private void UI() {
		
		System.out.println("V�lj alternativ genom att skriva in en heltalssiffra mellan "
				+ OPTIONCHOICE_MIN + " och " + OPTIONCHOICE_MAX + "!\n");

		while (!correctInputCheck) {
			/*
			 * numberCheck och errorHandler s�tts till false h�r i b�rjan s�
			 * att loopen ska b�rja om p� nytt n�r koden n�tt sista raden.
			 */
			numberCheck = false;
			errorHandler = false;
			
			System.out.println("1. S�k med titel\n2. S�k med betyg\n3. L�gg till en film\n4. Se hela filmlistan\n"
					+ "5. Avsluta programmet");
			
			while (!errorHandler) {
				try {
					while (!numberCheck) {
						optionChoice = userInput.nextInt();
						if (optionChoice < OPTIONCHOICE_MIN || optionChoice > OPTIONCHOICE_MAX) {
							System.out.println("Du m�ste v�lja en heltalssiffra mellan " 
									+ OPTIONCHOICE_MIN + " och " + OPTIONCHOICE_MAX + "!");		
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
			 * S�tter in valet man gjort in i metoden nedan.
			 */
			choiceOutcome(optionChoice);
		}
	}
	
	/*
	 * Ger ett utfall beroende p� int optionChoice-valet man gjort.
	 */
	private void choiceOutcome(int optionChoice) {
		if (optionChoice == 1) {
			databaseManager.searchDatabaseByTitles(movieList, gradeList);
		} else if (optionChoice == 2) {
			databaseManager.searchDatabaseByGrades(gradeList, movieList);
		} else if (optionChoice == 3) {
			fileManager.addMovie();
			/*
			 * handleFiles() uppdaterar arraylistorna f�r film och betyg
			 * s� att man inte beh�ver starta om programmet.
			 */
			handleFiles();
		} else if (optionChoice == 4) {
			for (int i = 0; i < gradeList.length; i++) {
				
				if (gradeList[i] > Database.MAXIMUM_GRADE) {
					gradeList[i] = Database.MAXIMUM_GRADE;
				} else if (gradeList[i] < Database.MINIMUM_GRADE) {
					gradeList[i] = Database.MINIMUM_GRADE;
				}
				
				System.out.println("Titel: " + movieList[i] 
						+ " - Betyg: " + gradeList[i] + "/" + Database.MAXIMUM_GRADE);
			}
			System.out.println("-----------------");
		} else if (optionChoice == 5) {
			System.out.println("Programmet har avslutats!");
			userInput.close();
			System.exit(0);
		} else {
			/*
			 * F�r andra utvecklare.
			 */
			System.out.println("VARNING: Du har �kat OPTIONCHOICE_MAX till h�gre �n 5, "
					+ "men har inte lagt in ny funktionalitet.\nD�rf�r visas detta meddelande.");
			System.out.println("-----------------");
		}
	}
}