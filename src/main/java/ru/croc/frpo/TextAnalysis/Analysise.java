package ru.croc.frpo.TextAnalysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**Класс, который содержит методы
 * обработки текста и вспомогательные методы. */
public class Analysise {
	private static final char SPACEBAR = (char) 0x0020; // константа-пробел
	
	/**Производит подсчет слов в списке строк.
	 * @param arg - Список строк, слова в которых считаем. 
	 * @return Возвращает число слов в списке строк.
	 */
	int count(ArrayList<String> arg) {
		int result = 0;
		for (String line : arg){
			int countInLine = 0;
			line = SPACEBAR + line; // Прибавляем к началу строки пробел,
			Pattern pattern = Pattern.compile("\\s\\S"); // потому что счтиаем количество сочетанй "пробел+непробел"
			Matcher matcher = pattern.matcher(line);   // Итого, получаем количество любых групп символов, между которыми стоят пробелы.
			while (matcher.find()) {
				countInLine ++; //Считаем количество найденных сочетаний в строке
			}
			result +=countInLine; //Прибавляем это количество к общему количеству сочетаний
		}
		return result;
	}

	/**Производит фильтрацию текста. 
	 * @param arg - Список строк, среди которых ищем сочетание.
	 * @param pattern - Выражение для поиска. Поиск может осуществляться как 
	 * по полному совпаданию слова (тогда можно просто ввести это слово),
	 * так и используя операторы '*' (заменяет любое количество буков и цифр) 
	 * и '?'(заменяет ровно одну букву или цифру).Разрешенные символы - [A-Za-zА-Яа-яёЁ?*0-9].
	 * @return Возвращает список, содержащий объекты типа Matcher -
	 * по одному для каждой поданной на вход строки массива.
	 */
	ArrayList<Matcher> filter(ArrayList<String> arg , String pattern) {
		ArrayList<Matcher> matchers = new ArrayList<>();
		if (!Pattern.matches("[A-Za-zА-Яа-яёЁ?*0-9]*", pattern)){ //Проверяем, входит ли вводимое выражения для поиска в допустимый диапазон.
			System.out.println("Ваше выражение для поиска содержит недопустимые символы."
						+ " Допустимые символы - А-я, A-z, цифры, '*' и '?'.");
			return null;
		}
		for (int i = 0; i<pattern.length(); i++){ //Цикл по всем символам паттерна-строки
			if (pattern.charAt(i) == '*' ){						 //Если встречаем звездочку, заменяем ее на регулярное выражение, соответствующее
				pattern = pattern.substring(0, i) + "\\S*" + pattern.substring(i+1); // произвольному количеству символов "непустого места".
				i+=2; //При замене звездочки на регулярное выражение меняется длина строки.
			}
			if (pattern.charAt(i) == '?' ){		//Если встречаем вопрос, заменяем его на регулярное выражение, соответствующее
				pattern = pattern.substring(0, i) + "\\S{1}" + pattern.substring(i+1); //одному символу "непустого места".
				i+=4;  //При замене вопроса на регулярное выражение меняется длина строки.
			}
		}
		pattern = "\\s" + pattern ; //Сначала в паттерне должно идти пустое место
		Pattern pat = Pattern.compile(pattern);	 
		for (String line : arg){
			line =  SPACEBAR + line; //В начале каждой строки ставим пробел (для верного нахождения соответствия паттерну первого слова,
			Matcher matcher = pat.matcher(line); //так как в паттерне ищем слова, вначале которых идет пробел.
			matchers.add(matcher); //Добавляем матчер в список для возврата.
		}
	return matchers;
	}
	
	/** Превращает файл в список строк.
	 * @param file - Файл для приведения к списку строк.
	 * @return Возвращает список строк.
	 * @throws IOException Возможно получение ошибок доступа к файлу, или других ошибок, связанных с чтением файла.
	 */
	ArrayList<String> fileToArrayList(File file) throws IOException{
        BufferedReader inputStream = null; 
        ArrayList<String> text = new ArrayList<>();
        try { //Считываем файл построчно и добавляем строку к списку строк
            inputStream = new BufferedReader(new FileReader(file));
            String line;
            while ((line = inputStream.readLine()) != null) {
                text.add(line);
            }
        } finally { //Закрываем входящий поток.
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return text;
	}

	/**С помощью других методов производит полный анализ списка строк.
	 * @param text - Список строк для анализа.
	 */
	void analyse(ArrayList<String> text){
		System.out.println("Вы ввели слов: " + count(text));
		System.out.println("Для поиска выражения в тексте можно использовать символы русских и "
				+ "латинских буков, цифр. \n'?' заменяет один любой символ, '*' - произвольное количество символов. ");
		System.out.print("Введите выражение для поиска: ");
		Scanner scan = new Scanner(System.in);  //Если я закрою его в методе, то больше с клавиатуры не прочитаешь. Какое решение можно предпринять?
		String pattern = scan.nextLine();     
		ArrayList<Matcher> matchers = filter(text, pattern);
		printMatchers(matchers);
	}
	
	/**Печатает матчеры - выводит на экран информацию о том, в какой строке и где расположены совпадения,
	 * показывает сами совпадения. Сообщает, если не найдено ни одного совпадения.
	 * @param matchers - Список матчеров для печати.
	 */
	void printMatchers(ArrayList<Matcher> matchers){
		if (matchers == null) { //Практически можно получить на вход пустой объект.
			return;
		}
		boolean found = false;
		for(Matcher matcher : matchers){ //Выводим на экран всю информацию о совпадениях.
			while (matcher.find()){
				String word = matcher.group().substring(1);
				System.out.println("В строке " + (matchers.indexOf(matcher)+1) + " найдено слово '" + word + "', "
						+ "которое находится с " + matcher.start() +  " по " + matcher.end() + " позиции строки.");
				found = true;
			}
		}
		if (!found){ //Если совпадений не найдено
			System.out.println("В тексте не найдено ни одного совпадения.");
		}
	}

}