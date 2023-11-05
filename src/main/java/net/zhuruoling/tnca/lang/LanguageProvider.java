package net.zhuruoling.tnca.lang;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LanguageProvider {
    public static final LanguageProvider INSTANCE = new LanguageProvider();
    public static final String[] LANGUAGES = new String[]{"zh_cn","en_us"};
    private static final Gson GSON = new GsonBuilder().serializeNulls().setPrettyPrinting().create();

    private final HashMap<String, HashMap<String, String>> translationMap = new HashMap<>();

    @SuppressWarnings("ALL")
    public void init(){
        for (String l : LANGUAGES) {
            var reader = new InputStreamReader(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("asstes/tnca/lang/" + l + ".json")));
            HashMap<String, String> langMap = new HashMap<String, String>();
            langMap = GSON.fromJson(reader, langMap.getClass());
            translationMap.put(l, langMap);
        }
    }


    public Map<String,String> canHasTranslations(String lang){
        return translationMap.get(lang);
    }
}
