package ru.croc.frpo.TextAnalysis;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
/**
 * Это консольное приложение придназначено для анализа консольного ввода и файловв.
 * Оно подсчитывает количество введённых слов и может фильтровать ввод.
 *
 */
public class App {
	
	public static void main(String[] args) {
		
		Analysise analysis = new Analysise();
		System.out.println("Доброго времени суток!\n");
		boolean flag = true;                          // true до тех пор, пока пользователь не захочет завершить работу
		String answer = null;
		Scanner scan = new Scanner(System.in);			
		while (flag == true) {
			// Начинаем основную часть - обработку считывания из файла или с консоли
			body: { 
			System.out.println("Вы хотите ввести текст здесь или загрузить его из файла? (console/file)");
			answer = scan.nextLine();		//считываем с клавиатуры ответ
			if(answer.equals("console")) {			//Пользователь выбрал консольный ввод
				System.out.println("Введите текст:");
				String line = scan.nextLine();
				ArrayList<String> text = new ArrayList<>();		//С клавиатуры помещаем данные в ArrayList
				text.add(line);
				analysis.analyse(text);				//И запускаем метод-анализатор
			} else if (answer.equals("file")){			//Пользователь выбрал ввод с помощью файла
				System.out.println("Введите полный путь до файла.");
				String name = scan.nextLine();
				Path path = Paths.get(name);
				File file = path.toFile();
				if (!file.exists()){
					System.out.println("К сожалению, такой файл в папке не найден.");
					break body;
				}
				if(!file.canRead()){
					System.out.println("Нет прав для чтения файла.");
					break body;
				}
				
				try {
					ArrayList<String> text = analysis.fileToArrayList(file);
					analysis.analyse(text);
				} catch (IOException e) {
					System.out.println("К сожалению, произошла ошибка, связанная с файлом. Попробуйте ещё раз.");
					e.printStackTrace();
					break body;
				}
				
			} else{
				System.out.println("Вы ввели неверный запрос!");
			}
			} 
		//  Конец основной части программы. Далее пользователя спрашивают, хочет ли он завершить работу.
			System.out.print("Хотите завершить работу? (Y/N)");
			answer = scan.nextLine();
			if (answer.equals("Y")){
				System.out.println("До свидания, всего вам хорошего! =)");
				flag = false;
			} else if (answer.equals("N")){	
				//Ничего не меняем, продолжаем работу
			} else{
				System.out.println("Вы ввели недопустимый ответ. Я завершаю работу.");
				flag = false;	
			
			}
		}
		scan.close();
		}
	}



