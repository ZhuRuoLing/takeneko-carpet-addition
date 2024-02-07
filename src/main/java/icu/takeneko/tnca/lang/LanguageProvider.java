package icu.takeneko.tnca.lang;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LanguageProvider {
    public static final LanguageProvider INSTANCE = new LanguageProvider();
    public static final String[] LANGUAGES = new String[]{"zh_cn", "en_us"};
    private static final Gson GSON = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
    private String language = "en_us";
    private final HashMap<String, HashMap<String, String>> translationMap = new HashMap<>();

    @SuppressWarnings("ALL")
    public void init() {
        for (String l : LANGUAGES) {
            var reader = new InputStreamReader(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("assets/tnca/lang/" + l + ".json")));
            HashMap<String, String> langMap = new HashMap<String, String>();
            langMap = GSON.fromJson(reader, langMap.getClass());
            translationMap.put(l, langMap);
        }
    }

    public Map<String, String> getTranslationsForLang(String lang) {
        return translationMap.get(lang);
    }

    public boolean hasTranslations(String lang, String key) {
        return getTranslationValue(lang, key) != null;
    }

    public String getTranslationValue(String lang, String key) {
        synchronized (translationMap) {
            if (translationMap.containsKey(lang)) {
                return translationMap.get(lang).get(key);
            }
            return null;
        }
    }

    public static String translate(String key, Object... args) {
        String lang = INSTANCE.getLanguage();
        if (INSTANCE.hasTranslations(lang, key)) {
            return MessageFormat.format(INSTANCE.getTranslationValue(lang, key), args);
        }
        return key + Arrays.deepToString(args);
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
