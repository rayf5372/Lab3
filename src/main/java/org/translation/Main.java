package org.translation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Main class for this program.
 * Complete the code according to the "to do" notes.<br/>
 * The system will:<br/>
 * - prompt the user to pick a country name from a list<br/>
 * - prompt the user to pick the language they want it translated to from a list<br/>
 * - output the translation<br/>
 * - at any time, the user can type quit to quit the program<br/>
 */
public class Main {

    private static final String QUIT = "quit";

    public static void main(String[] args) {

        // Translator translator = new JSONTranslator(null);
        // Switched to JSONTranslator
        Translator translator = new JSONTranslator();

        runProgram(translator);
    }

    /**
     * This is the method which we will use to test your overall program, since
     * it allows us to pass in whatever translator object that we want!
     * See the class Javadoc for a summary of what the program will do.
     *
     * @param translator the Translator implementation to use in the program
     */
    public static void runProgram(Translator translator) {
        // Create instances of code converters
        CountryCodeConverter countryCodeConverter = new CountryCodeConverter();
        LanguageCodeConverter languageCodeConverter = new LanguageCodeConverter();

        while (true) {
            String countryName = promptForCountry(translator, countryCodeConverter);
            if (QUIT.equalsIgnoreCase(countryName)) {
                break;
            }

            // Convert country name back to its 3-letter country code
            String countryCode = countryCodeConverter.fromCountry(countryName);
            if (countryCode == null) {
                System.out.println("Invalid country selected.");
                continue;
            }

            // TODO Task: Once you switch promptForCountry so that it returns the country
            //            name rather than the 3-letter country code, you will need to
            //            convert it back to its 3-letter country code when calling promptForLanguage
            String languageName = promptForLanguage(translator, countryCode, languageCodeConverter);
            if (QUIT.equalsIgnoreCase(languageName)) {
                break;
            }

            // Convert language name back to its 2-letter language code
            String languageCode = languageCodeConverter.fromLanguage(languageName);
            if (languageCode == null) {
                System.out.println("Invalid language selected.");
                continue;
            }

            // TODO Task: Once you switch promptForLanguage so that it returns the language
            //            name rather than the 2-letter language code, you will need to
            //            convert it back to its 2-letter language code when calling translate.
            //            Note: you should use the actual names in the message printed below though,
            //            since the user will see the displayed message.
            String translation = translator.translate(countryCode, languageCode);
            if (translation != null) {
                System.out.println(countryName + " in " + languageName + " is " + translation);
            }
            else {
                System.out.println("Translation not found.");
            }

            System.out.println("Press enter to continue or quit to exit.");
            Scanner s = new Scanner(System.in);
            String textTyped = s.nextLine();

            if ("quit".equalsIgnoreCase(textTyped)) {
                break;
            }
        }
    }

    // Note: CheckStyle is configured so that we don't need javadoc for private methods
    private static String promptForCountry(Translator translator, CountryCodeConverter countryCodeConverter) {
        List<String> countryCodes = translator.getCountries();

        // Convert the country codes to the actual country names before sorting
        List<String> countryNames = new ArrayList<>();
        for (String code : countryCodes) {
            String name = countryCodeConverter.fromCountryCode(code);
            if (name != null) {
                countryNames.add(name);
            }
        }

        // Sort the countries alphabetically
        Collections.sort(countryNames);

        // Print them out; one per line
        System.out.println("Available countries:");
        for (String name : countryNames) {
            System.out.println(name);
        }

        System.out.println("Select a country from above:");

        Scanner s = new Scanner(System.in);
        return s.nextLine();
    }

    // Note: CheckStyle is configured so that we don't need javadoc for private methods
    private static String promptForLanguage(Translator translator, String countryCode, LanguageCodeConverter languageCodeConverter) {

        List<String> languageCodes = translator.getCountryLanguages(countryCode);

        // Convert the language codes to the actual language names before sorting
        List<String> languageNames = new ArrayList<>();
        for (String code : languageCodes) {
            String name = languageCodeConverter.fromLanguageCode(code);
            if (name != null) {
                languageNames.add(name);
            }
        }

        // Sort the languages alphabetically
        Collections.sort(languageNames);

        // Print them out; one per line
        System.out.println("Available languages:");
        for (String name : languageNames) {
            System.out.println(name);
        }

        System.out.println("Select a language from above:");

        Scanner s = new Scanner(System.in);
        return s.nextLine();
    }
}
