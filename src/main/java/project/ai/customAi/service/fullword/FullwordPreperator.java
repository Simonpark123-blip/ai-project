package project.ai.customAi.service.fullword;

import lombok.extern.slf4j.Slf4j;

import java.util.Locale;

@Slf4j
public class FullwordPreperator {

    private FullwordPreperator() { throw new IllegalStateException("Utility class"); }

    public static String cleanWord(String uncleanedWord) {
        String cleanedWord = uncleanedWord.toLowerCase(Locale.GERMAN);
        cleanedWord = cleanedWord.replace("ä", "ae")
                                 .replace("ö", "oe")
                                 .replace("ü", "ue")
                                 .replace("ß", "ss");
        cleanedWord = cleanedWord.replaceAll("[^a-z_]", "");
        log.info("Cleaned word '{}' for '{}'", cleanedWord, uncleanedWord);
        return cleanedWord;
    }

}
