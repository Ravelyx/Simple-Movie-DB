import java.util.Arrays;
import java.util.Scanner;

/*
 * Programmet är en filmdatabas som låter användaren mata in en siffra mellan
 * OPTIONCHOICE_MIN och OPTIONCHOICE_MAX. Den låter användaren söka databasen
 * med hjälp av antingen titel eller betyg. Det går även att lägga in filmer.
 */
public class Database {
	
	/*
	 * Instansvariabler som gör det enklare att modifiera programmet.
	 * Kolla PDF-dokumentet och läs koden för djupare insikt.
	 */
	public static final int MINIMUM_GRADE = 1;
	public static final int MAXIMUM_GRADE = 5;
	public static final int OPTIONCHOICE_MIN = 1;
	public static final int OPTIONCHOICE_MAX = 5;
	public static final String CANCEL_NEW_MOVIE = "avbrytX";
	
	/*
	 * Importerar in nödvändiga klasser för att programmet ska fungera.
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
	 * Constructor-metod för att programmet ska köras så fort launcher kallar på den.
	 * Den välkomnar användaren, skapar objekt till övriga klasser, importerar in
	 * lista på filmer och betyg på ett korrekt sätt och startar UI.
	 */
	public Database() {
		welcome();
		init();
		handleFiles();
		UI();
	}
	
	private void welcome() {
		System.out.println("Välkommen till filmdatabasen!");
	}
	
	/*
	 * Skapar objekt till övriga klasser.
	 */
	private void init() {
		fileManager = new FileManager();
		databaseManager = new DatabaseManager();
	}
	
	/*
	 * Importerar in fil på korrekt sätt och utför nödvändig casting.
	 * Hanterar också uppdateringar av fil när nya filmer läggs till.
	 */
	private void handleFiles() {
		movieList = fileManager.importMovies(0);
		gradeListTemp = fileManager.importMovies(1);
		
		/*
		 * gradeList görs om till en int array från en String array eftersom
		 * det bara är siffror i den och detta underlättar jämförelse senare i programmet.
		 */
		gradeList = new int[gradeListTemp.length];
		gradeList = Arrays.stream(gradeListTemp).mapToInt(Integer::parseInt).toArray();
	}
	
	/*
	 * Hanterar all text och frågar användaren om inmatning mellan OPTIONCHOICE_MAX
	 * och OPTIONCHOICE_MIN. Vid korrekt inmatning skickas användaren vidare till
	 * choiceOutcome-metoden.
	 */
	private void UI() {
		
		System.out.println("Välj alternativ genom att skriva in en heltalssiffra mellan "
				+ OPTIONCHOICE_MIN + " och " + OPTIONCHOICE_MAX + "!\n");

		while (!correctInputCheck) {
			/*
			 * numberCheck och errorHandler sätts till false här i början så
			 * att loopen ska börja om på nytt när koden nått sista raden.
			 */
			numberCheck = false;
			errorHandler = false;
			
			System.out.println("1. Sök med titel\n2. Sök med betyg\n3. Lägg till en film\n4. Se hela filmlistan\n"
					+ "5. Avsluta programmet");
			
			while (!errorHandler) {
				try {
					while (!numberCheck) {
						optionChoice = userInput.nextInt();
						if (optionChoice < OPTIONCHOICE_MIN || optionChoice > OPTIONCHOICE_MAX) {
							System.out.println("Du måste välja en heltalssiffra mellan " 
									+ OPTIONCHOICE_MIN + " och " + OPTIONCHOICE_MAX + "!");		
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
			 * Sätter in valet man gjort in i metoden nedan.
			 */
			choiceOutcome(optionChoice);
		}
	}
	
	/*
	 * Ger ett utfall beroende på int optionChoice-valet man gjort.
	 */
	private void choiceOutcome(int optionChoice) {
		if (optionChoice == 1) {
			databaseManager.searchDatabaseByTitles(movieList, gradeList);
		} else if (optionChoice == 2) {
			databaseManager.searchDatabaseByGrades(gradeList, movieList);
		} else if (optionChoice == 3) {
			fileManager.addMovie();
			/*
			 * handleFiles() uppdaterar arraylistorna för film och betyg
			 * så att man inte behöver starta om programmet.
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
			 * För andra utvecklare.
			 */
			System.out.println("VARNING: Du har ökat OPTIONCHOICE_MAX till högre än 5, "
					+ "men har inte lagt in ny funktionalitet.\nDärför visas detta meddelande.");
			System.out.println("-----------------");
		}
	}
}