package CrimeAppImproved;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class CrimeSort {
	static List<CrimeData> crimes = new ArrayList<CrimeData>();

	public enum HighestFrequency {
		LEGISLATION, SUCCESS_LEGISLATION, ETHNICITY
	}

	static Scanner input = new Scanner(System.in);

	public static void main(String[] args) throws FileNotFoundException, ParseException {
		readFile();
		menu();
	}

	public static void menu() {
		System.out.println("Please choose an option:\n" + "1 - Sort Crimes By Type (Task A & B)\n"
				+ "2 - Stop & Search Statistics (Tasks C)\n"
				+ "3 - Legislation with Highest stop and search frequency (Task D)\n"
				+ "4 - Legislation again but successful stop searches (Task E)\n"
				+ "5 - Ethnic Group Stop and Search (Task F & G)\n" + "6 - Gender And Strip Search (Task H)\n"
				+ "Q - Quit\nChoice : ");
		Scanner input = new Scanner(System.in);
		String scanChoice;
		do {
			scanChoice = input.nextLine().toUpperCase();
			switch (scanChoice) {
			case "1":
				sortDate();
				outputCrimeTypes(crimes);
				break;
			case "2":
				stopSearches(crimes);
				break;
			case "3":
				getInfo(HighestFrequency.LEGISLATION);
				break;
			case "4":
				getInfo(HighestFrequency.SUCCESS_LEGISLATION);
				break;
			case "5":
				getInfo(HighestFrequency.ETHNICITY);
				break;
			case "6":
				searchByGender(crimes);
				break;
			}
			input.close();
		} while (!scanChoice.equals("Q"));
		System.out.println("--- TERMINATED ---");
		System.exit(0);

	}

	public static List<CrimeData> readFile() throws FileNotFoundException, ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String[] files = { "data/2018-02/2018-02-cheshire-stop-and-search.csv",
				"data/2018-02/2018-02-merseyside-stop-and-search.csv",
				"data/2018-03/2018-03-cheshire-stop-and-search.csv",
				"data/2018-03/2018-03-merseyside-stop-and-search.csv",
				"data/2018-04/2018-04-cheshire-stop-and-search.csv",
				"data/2018-04/2018-04-merseyside-stop-and-search.csv" };

		for (int i = 0; i < files.length; i++) {

			final String SEP = ",";
			try {
				String currentPoliceForce = "";

				if (files[i].contains("merseyside"))
					;
				{
					currentPoliceForce = "Merseyside";
				}
				if (files[i].contains("cheshire")) {

					currentPoliceForce = "Cheshire";
				}

				File csvFile = new File(files[i]);

				Scanner csvScan = new Scanner(csvFile);
				csvScan.nextLine();

				while (csvScan.hasNextLine()) {
					String line = csvScan.nextLine();

					String[] parts = line.split(SEP, -1);

					CrimeData crime = new CrimeData();

					int pos = 0;
					crime.crimeType = parts[pos++];
					String dateOf = parts[pos++];
					String newString = dateOf.replaceAll("T", " ");
					Date date = sdf.parse(newString);
					crime.date = date;
					crime.partPolicingOp = Boolean.valueOf(parts[pos++]);
					crime.policingOp = parts[pos++];
					crime.lattitude = parts[pos++];
					crime.longitude = parts[pos++];
					crime.gender = parts[pos++];
					crime.ageRange = parts[pos++];
					crime.selfEthnicity = parts[pos++];
					crime.officerEthnicity = parts[pos++];
					crime.legislation = parts[pos++];
					crime.objectOfSearch = parts[pos++];
					crime.outcome = parts[pos++];
					crime.outcomeLinkedToSearch = Boolean.valueOf(parts[pos++]);
					crime.stripSearch = Boolean.valueOf(parts[pos++]);
					crime.policeForce = currentPoliceForce;

					crimes.add(crime);

				}
				csvScan.close();

			} catch (FileNotFoundException obj) {
				System.out.println(obj);
				System.out.println("Put the proxy file into the correct location...");
				System.out.println("Press enter once it has been moved to continue");
				input.nextLine();
			}
		}
		return crimes;
	}

	public static List<CrimeData> mergeSort(List<CrimeData> crime) {
		List<CrimeData> left = new ArrayList<>();
		List<CrimeData> right = new ArrayList<>();
		int midPoint;

		if (crime.size() == 1) {
			return crime;
		} else {
			midPoint = crime.size() / 2;
			for (int i = 0; i < midPoint; i++) {
				CrimeData current = crime.get(i);
				left.add(current);
			}
			for (int i = midPoint; i < crime.size(); i++) {
				CrimeData current = crime.get(i);
				right.add(current);
			}
			left = mergeSort(left);
			right = mergeSort(right);
			merge(left, right, crime);
		}
		return crime;
	}

	private static void merge(List<CrimeData> left, List<CrimeData> right, List<CrimeData> crime) {
		int leftIdx = 0;
		int rightIdx = 0;
		int totalIdx = 0;

		while (leftIdx < left.size() && rightIdx < right.size()) {
			CrimeData leftSide = left.get(leftIdx);
			CrimeData rightSide = right.get(rightIdx);
			if ((leftSide.date.compareTo(rightSide.date)) < 0) {
				crime.set(totalIdx, left.get(leftIdx));
				leftIdx++;
			} else {
				crime.set(totalIdx, right.get(rightIdx));
				rightIdx++;
			}
			totalIdx++;
		}
		List<CrimeData> rest;
		int restIdx;
		if (leftIdx >= left.size()) {
			rest = right;
			restIdx = rightIdx;
		} else {
			rest = left;
			restIdx = leftIdx;
		}
		for (int i = restIdx; i < rest.size(); i++) {
			crime.set(totalIdx, rest.get(i));
			totalIdx++;
		}
	}

	private static void outputCrimeTypes(List<CrimeData> crime) {
		Set<String> hs = new HashSet<String>();

		for (int i = 0; i < crime.size(); i++) {
			CrimeData currentCrime = crime.get(i);
			if (currentCrime != null) {
				hs.add(currentCrime.objectOfSearch);
			}
		}
		for (String k : hs) {
			System.out.println(k);
		}
		searchBy(crime);

	}

	private static void sortDate() {
		mergeSort(crimes);
	}

	private static void searchBy(List<CrimeData> crime) {
		System.out.println("What Object Of Search Would You Like To Filter By? : ");
		String choice = input.nextLine();
		for (int i = 0; i < crime.size(); i++) {
			CrimeData currentCrime = crime.get(i);
			if (currentCrime.objectOfSearch.equalsIgnoreCase(choice)) {
				System.out.println(" | Object Of Search : " + currentCrime.objectOfSearch + " | Crime Type : "
						+ currentCrime.crimeType + " | Date : " + currentCrime.date + " | The police force is  : "
						+ currentCrime.policeForce);
			}
		}
		menu();
	}

	// Method for Feature C
	private static void stopSearches(List<CrimeData> crime) {
		int bothSuccessful = 0;
		int oneSuccessful = 0;
		int bothFailed = 0;

		for (int i = 0; i < crime.size(); i++) {
			CrimeData searchLink = crime.get(i);
			if (searchLink.outcome.startsWith("Nothing") || searchLink.outcome.equals("")
					&& (searchLink.outcomeLinkedToSearch == false || searchLink.outcomeLinkedToSearch == null)) {
				bothFailed++;
			} else if (searchLink.outcome.startsWith("Suspect") || searchLink.outcome.startsWith("Local")
					|| searchLink.outcome.startsWith("Offender")
					|| searchLink.outcome.startsWith("Article") && (searchLink.outcomeLinkedToSearch == true
							|| searchLink.outcomeLinkedToSearch == false)) {
				if (searchLink.outcomeLinkedToSearch == true) {
					bothSuccessful++;
				} else if (searchLink.outcomeLinkedToSearch == false) {
					oneSuccessful++;
				}
			}
		}
		System.out.println("\nStop searches where:\n");
		System.out.println("- The suspect was arrested and the outcome was related to the stop: " + bothSuccessful);
		System.out.println("- The suspect was arrested but the stop wasn't related to the outcome: " + oneSuccessful);
		System.out.println("- The stop occured but no futher action happened: " + bothFailed);
		System.out.println("\n");
		menu();
	}

	private static void getInfo(HighestFrequency freq) {
		String monthIn = "";
		String policeForceIn = "";
		String legiIn = "";
		String subChoice = "";
		if (freq == HighestFrequency.LEGISLATION || freq == HighestFrequency.SUCCESS_LEGISLATION) {

			System.out.println("Enter your chosen month in numeric form (Feburary is 02, December is 12): ");
			monthIn = input.next();
			if(freq == HighestFrequency.LEGISLATION) {
				legislationSort(crimes, monthIn);
				menu();
			}else if(freq == HighestFrequency.SUCCESS_LEGISLATION) {
				legislationSort(crimes, monthIn);
				legislationSuccessSort(crimes, monthIn);
				menu();
			}
		} else if (freq == HighestFrequency.ETHNICITY) {

			System.out.println("Do you want to search based off a month/police force (A) or legislation (B)?\n (Reaching Task G requires option A): ");
			subChoice = input.nextLine().toUpperCase();
			if (subChoice.equals("A")) {
				System.out.println("Enter your chosen month in numeric form (Feburary is 02, December is 12): ");
				monthIn = input.next();
				System.out.println("Enter your chosen police force: ");
				policeForceIn = input.next();
				ethnicitySort(crimes, monthIn, policeForceIn, "", subChoice);
				menu();
			} else if (subChoice.equals("B")) {
				System.out.println("Enter your chosen legislation: ");
				legiIn = input.nextLine();
				ethnicitySort(crimes, "", "", legiIn, subChoice);
				menu();
			}
			
		}
	}

	private static void legislationSort(List<CrimeData> crime, String monthIn) {
		HashMap<String, Integer> matches = new HashMap<String, Integer>();
		SimpleDateFormat sdf;
		String toMatch;
		String month;
		int highestFreq = 0;
		String legislation = null;
		for (int i = 0; i < crime.size(); i++) {
			CrimeData currentCrime = crime.get(i);
			if (currentCrime != null) {
				sdf = new SimpleDateFormat("MM");
				month = sdf.format(currentCrime.date);
				if (month.equalsIgnoreCase(monthIn)) {
					toMatch = currentCrime.legislation.toLowerCase();
					if (matches.containsKey(toMatch))
						matches.put(toMatch, matches.get(toMatch) + 1);
					else
						matches.put(toMatch, 1);
				}
			}
		}
		for (String currentValue : matches.keySet()) {
			int numOfValues = matches.get(currentValue);
			if (numOfValues > highestFreq) {
				highestFreq = numOfValues;
				legislation = currentValue;

			}
		}
		System.out.printf("The Highest stop and search frequency is %s which has been conducted %d times", legislation,
				highestFreq);
		System.out.println("\n");
	}

	private static void legislationSuccessSort(List<CrimeData> crime, String monthIn) {
		SimpleDateFormat sdf;
		String month;
		int successCount = 0;
		for (int i = 0; i < crime.size(); i++) {
			CrimeData currentCrime = crime.get(i);
			if (currentCrime != null) {
				sdf = new SimpleDateFormat("MM");
				month = sdf.format(currentCrime.date);
				if (month.equalsIgnoreCase(monthIn)) {
					if (currentCrime.outcome.startsWith("Suspect") || currentCrime.outcome.startsWith("Local")
							|| currentCrime.outcome.startsWith("Offender")
							|| currentCrime.outcome.startsWith("Article") && (currentCrime.outcomeLinkedToSearch == true
									|| currentCrime.outcomeLinkedToSearch == false)) {
						if (currentCrime.outcomeLinkedToSearch == true) {
							successCount++;
						}
					}
				}
			}
		}
		System.out.println("This month also had " + successCount + " successful stops.\n");

	}

	private static void ethnicitySort(List<CrimeData> crime, String monthIn, String policeForceIn, String legiIn, String subChoice) {
		HashMap<String, Integer> matches = new HashMap<String, Integer>();
		SimpleDateFormat sdf;
		String toMatch = null;
		String month;
		int highestFreq = 0;
		String legislation = null;
		for (int i = 0; i < crime.size(); i++) {
			CrimeData currentCrime = crime.get(i);
			if (currentCrime != null) {
				sdf = new SimpleDateFormat("MM");
				month = sdf.format(currentCrime.date);
				if (subChoice.equals("A")) {
					if (month.equalsIgnoreCase(monthIn) && currentCrime.policeForce.equalsIgnoreCase(policeForceIn)) {
						toMatch = currentCrime.selfEthnicity;
						if (matches.containsKey(toMatch)) {
							matches.put(toMatch, matches.get(toMatch) + 1);
						} else {
							matches.put(toMatch, 1);
						}
					}
				}
				if (subChoice.equals("B")) {
					if (currentCrime.legislation.equalsIgnoreCase(legiIn)) {
						toMatch = currentCrime.selfEthnicity;
						if (matches.containsKey(toMatch)) {
							matches.put(toMatch, matches.get(toMatch) + 1);
						} else {
							matches.put(toMatch, 1);
						}
					}
				}
				
			}
		}
		for (String currentValue : matches.keySet()) {
			int numOfValues = matches.get(currentValue);
			if (numOfValues > highestFreq) {
				highestFreq = numOfValues;
				legislation = currentValue;

			}
		}
		System.out.printf("The Highest stop and search frequency is %s which has been conducted %d times", legislation,
				highestFreq);
		System.out.println("\n");
		if (subChoice.equals("A")) {

			System.out.println("(Task G) Do you want to see the reverse chronological order of these results? (Y/N): ");
			String sortChoice = input.next().toUpperCase();
			if (sortChoice.equals("Y")) {
				sortDate();
				for (int i = crime.size() - 1; i > 0; i--) {
					CrimeData currentCrime = crime.get(i);
					sdf = new SimpleDateFormat("MM");
					month = sdf.format(currentCrime.date);
					if (month.equalsIgnoreCase(monthIn) && currentCrime.policeForce.equalsIgnoreCase(policeForceIn) && currentCrime.selfEthnicity.equals(legislation)) {
						System.out.println(" | Date : " + currentCrime.date + " | Ethnic Group : "
								+ currentCrime.selfEthnicity + " | Object Of Search : " + currentCrime.objectOfSearch
								+ " | Crime Type : " + currentCrime.crimeType);
					}
				}
				System.out.println("\n");
			}
		}
	}
	private static void searchByGender(List<CrimeData> crime) {
		System.out.println("What Gender Would You Like To Search By? : ");
		String choice = input.nextLine();
		System.out.println("Removal Of Just Outer Clothing? (Y/N) : ");
		String choice1 = input.nextLine();
		Boolean stripSearch = false;
		if (choice1.equalsIgnoreCase("Y"))
			stripSearch = true;
		else
			stripSearch = false;

		for (int i = 0; i < crime.size(); i++) {
			CrimeData currentCrime = crime.get(i);
			if (currentCrime.gender.equalsIgnoreCase(choice) && stripSearch == true) {
				if (currentCrime.stripSearch == true) {
					System.out.println(" | Object Of Search : " + " | Crime Type : " + currentCrime.crimeType
							+ " | Gender : " + currentCrime.gender + " | Removal Of Outer Clothing : "
							+ currentCrime.stripSearch);
				}
			} else if (currentCrime.gender.equalsIgnoreCase(choice) && stripSearch == false) {
				System.out
						.println(" | Object Of Search : " + " | Crime Type : " + currentCrime.crimeType + " | Gender : "
								+ currentCrime.gender + " | Removal Of Outer Clothing : " + currentCrime.stripSearch);
			}
		}
		menu();
	}
}
