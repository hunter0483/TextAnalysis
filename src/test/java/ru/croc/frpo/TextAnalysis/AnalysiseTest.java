package ru.croc.frpo.TextAnalysis;



import java.util.ArrayList;
import java.util.regex.Matcher;

import org.junit.Test;

import junit.framework.TestCase;

public class AnalysiseTest extends TestCase {
	
	
	Analysise analyse = new Analysise();

	
	@Test(timeout=1000)
	public void testCount(){
		ArrayList<String> arg = new ArrayList<String>();
		arg.add("Сегодня прекрасный день для прогулки!");
		int count = analyse.count(arg);
		assertEquals(5, count);
	}
	

	
	@Test
	public void testCountOneSpaced(){
		ArrayList<String> arg = new ArrayList<String>();
		arg.add("Сегодня прекрасный день	  для прогулки в парке !!!");
		int count = analyse.count(arg);
		assertEquals(8, count);
	}
	
	@Test
	public void testCountChristmas(){
		ArrayList<String> arg = new ArrayList<String>();
		arg.add("В лесу родилась ёлочка, В лесу она росла!, Зимой и летом стройная, Зеленая была.");
		int count = analyse.count(arg);
		assertEquals(14, count);
	}
	

	
	@Test
	public void testNumbers(){
		ArrayList<String> arg = new ArrayList<String>();
		arg.add(
				" Сегодня прекрасный день 77   для прогулки в парке 42 23 43 !!!");
		int count = analyse.count(arg);
		assertEquals(12, count);
	}
	

	@Test
	public void testFilter(){
		String arg = "ааа аас ааб";
		ArrayList<String> list = new ArrayList<String>();
		list.add(arg);
		String pattern = "а*";
		ArrayList<Integer> ans = new ArrayList<>();
		ans.add(0);
		ans.add(4);
		ans.add(8);
		ArrayList<Matcher> matchers =  analyse.filter(list, pattern);
		for (Matcher matcher : matchers){
			while (matcher.find()){
				int compare = ans.get(0);
				assertEquals(compare, matcher.start());
				ans.remove(0);
			}
		}
	}
	
	
	

}
