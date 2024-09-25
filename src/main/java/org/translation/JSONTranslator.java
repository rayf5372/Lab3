package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * An implementation of the Translator interface which reads in the translation
 * data from a JSON file. The data is read in once each time an instance of this class is constructed.
 */
public class JSONTranslator implements Translator {

    // Instance variable to store translations: country code to language code to translation
    private final Map<String, Map<String, String>> translations;

    /**
     * Constructs a JSONTranslator using data from the sample.json resources file.
     */
    public JSONTranslator() {
        this("sample.json");
    }

    /**
     * Constructs a JSONTranslator populated using data from the specified resources file.
     * @param filename the name of the file in resources to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public JSONTranslator(String filename) {
        // Initialize the translations map
        translations = new HashMap<>();

        // Read the file to get the data to populate the translations map
        try {
            // Read the JSON file content as a String
            String jsonString = Files.readString(
                    Paths.get(getClass().getClassLoader().getResource(filename).toURI())
            );

            // Parse the JSON string into a JSONArray
            JSONArray jsonArray = new JSONArray(jsonString);

            // Use the data in the jsonArray to populate the instance variable
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject countryObj = jsonArray.getJSONObject(i);

                // Assume the country code is stored under "cca3" or "countryCode"
                String countryCode = null;
                if (countryObj.has("cca3")) {
                    countryCode = countryObj.getString("cca3");
                }
                else if (countryObj.has("countryCode")) {
                    countryCode = countryObj.getString("countryCode");
                }

                if (countryCode != null) {
                    // Create a map to store language code to translation
                    Map<String, String> languageMap = new HashMap<>();

                    // Iterate over the keys in the country object
                    Iterator<String> keys = countryObj.keys();
                    while (keys.hasNext()) {
                        String key = keys.next();
                        // Skip the country code keys
                        if (!"cca3".equals(key) && !"countryCode".equals(key)) {
                            // Assume other keys are language codes
                            String translation = countryObj.getString(key);
                            languageMap.put(key, translation);
                        }
                    }

                    // Put the language map into the translations map
                    translations.put(countryCode, languageMap);
                }
            }

        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Returns the language abbreviations for all languages whose translations are
     * available for the given country.
     *
     * @param country the country code
     * @return list of language abbreviations which are available for this country
     */
    @Override
    public List<String> getCountryLanguages(String country) {
        // Return an appropriate list of language codes without aliasing mutable objects
        Map<String, String> languageMap = translations.get(country);
        if (languageMap != null) {
            return new ArrayList<>(languageMap.keySet());
        }
        else {
            return new ArrayList<>();
        }
    }

    /**
     * Returns the country abbreviations for all countries whose translations are
     * available from this Translator.
     *
     * @return list of country abbreviations for which we have translations available
     */
    @Override
    public List<String> getCountries() {
        // Return an appropriate list of country codes without aliasing mutable objects
        return new ArrayList<>(translations.keySet());
    }

    /**
     * Returns the name of the country based on the specified country abbreviation and language abbreviation.
     *
     * @param country  the country code
     * @param language the language code
     * @return the name of the country in the given language or null if no translation is available
     */
    @Override
    public String translate(String country, String language) {
        // Retrieve the language map for the given country code
        Map<String, String> languageMap = translations.get(country);
        if (languageMap != null) {
            // Return the translation for the given language code
            return languageMap.get(language);
        }
        else {
            return null;
        }
    }
}
