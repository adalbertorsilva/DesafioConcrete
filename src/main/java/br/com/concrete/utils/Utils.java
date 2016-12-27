package br.com.concrete.utils;

import java.util.stream.Stream;

public class Utils {

	public static boolean isNull(Object object){
		return object == null;
	}
	
	public static boolean anyNull(Object... fields){
		return Stream.of(fields).filter(Utils::isNull).count() > 0;
	}
	
	public static boolean anyEmpty(Object... fields){
		return Stream.of(fields).map(f -> (String) f)
								.filter(f -> f.isEmpty()).findAny().isPresent();
	}
	
	
}
