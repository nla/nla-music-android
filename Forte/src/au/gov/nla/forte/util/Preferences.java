package au.gov.nla.forte.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import au.gov.nla.forte.model.PreferencesEnum;

public class Preferences {
	private final static String FILE = "settings";
	private final static String DELIMITER = "-333-";
	
	private static SharedPreferences getPreferences(Context ctx){
		return ctx.getSharedPreferences(FILE, Context.MODE_PRIVATE);
	}
	
	private static Editor getEditor(Context ctx) {
		return getPreferences(ctx).edit();
	}
	
	public static void updatePreferences(Context context, HashMap<PreferencesEnum, String> preferences) {
		Editor editor = getEditor(context);
		for(PreferencesEnum key : preferences.keySet()){
			editor.putString(key.getValue(), preferences.get(key));
		}
		editor.commit();
	}

	public static void putPreference(Context context, PreferencesEnum preference, String value) {
		Editor editor = getEditor(context);
		editor.putString(preference.getValue(), value);
		editor.commit();
	}

	public static void putPreferenceAsList(Context context, PreferencesEnum preference, HashSet<String> value) {
		Editor editor = getEditor(context);
		String result = "";
		for(String string: value) {
			result += string + DELIMITER;
		}
		result = result.substring(0, result.length() - DELIMITER.length());
		editor.putString(preference.getValue(), result);
		editor.commit();
	}

	public static String getPreference(Context context, PreferencesEnum preference, String defaultValue) {
		return getPreferences(context).getString(preference.getValue(), defaultValue);
	}

	public static ArrayList<String> getPreferenceAsList(Context context, PreferencesEnum preference, ArrayList<String> defaultValue) {
		String value = getPreferences(context).getString(preference.getValue(), null);
		if(value == null) {
			return defaultValue;
		} else {
			return new ArrayList<String>(Arrays.asList(value.split(DELIMITER)));
		}
	}

	public static void removePreferences(Context context, HashSet<PreferencesEnum> preferences) {
		Editor editor = getEditor(context);
		for(PreferencesEnum key : preferences){
			editor.remove(key.getValue());
		}
		editor.commit();
	}
}

