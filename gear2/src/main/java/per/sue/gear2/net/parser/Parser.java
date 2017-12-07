package per.sue.gear2.net.parser;



import org.json.JSONException;

import per.sue.gear2.net.exception.ParseException;


public interface Parser<T> {
	public abstract T parse(String jsonString) throws ParseException, JSONException;
}
