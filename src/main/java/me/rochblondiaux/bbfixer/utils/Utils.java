package me.rochblondiaux.bbfixer.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Utils {

    private static final String VALID_NAMESPACE_REGEX = "[^a-z0-9_.-]";

    public static String fix(String input) {
        // Fix
        input = input.toLowerCase();
        input = input.replaceAll(" ", "_");
        input = replaceAccent(input);

        // Remove illegal characters
        input = removeIllegalCharacters(input);

        // Return
        return input;
    }

    private static String removeIllegalCharacters(String input) {
        Pattern pattern = Pattern.compile(VALID_NAMESPACE_REGEX);
        Matcher matcher = pattern.matcher(input);
        return matcher.replaceAll("");
    }

    private static String replaceAccent(String input) {
        return input.replace("é", "e")
                .replace("è", "e")
                .replace("ê", "e")
                .replace("à", "a")
                .replace("â", "a")
                .replace("ç", "c")
                .replace("ù", "u")
                .replace("û", "u")
                .replace("î", "i")
                .replace("ô", "o")
                .replace("ö", "o")
                .replace("ï", "i")
                .replace("ü", "u")
                .replace("ë", "e")
                .replace("ÿ", "y")
                .replace("æ", "ae")
                .replace("œ", "oe");
    }
}
