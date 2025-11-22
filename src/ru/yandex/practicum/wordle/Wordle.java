package ru.yandex.practicum.wordle;


import ru.yandex.practicum.exception.HintDictionaryIsEmptyException;
import ru.yandex.practicum.exception.WordIsAlreadyUsedException;
import ru.yandex.practicum.exception.WordNotFoundInDictionaryException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/*
в главном классе нам нужно:
    создать лог-файл (он должен передаваться во все классы)
    создать загрузчик словарей WordleDictionaryLoader
    загрузить словарь WordleDictionary с помощью класса WordleDictionaryLoader
    затем создать игру WordleGame и передать ей словарь
    вызвать игровой метод в котором в цикле опрашивать пользователя и передавать информацию в игру
    вывести состояние игры и конечный результат
 */
public class Wordle {
    private static final String HOME = System.getProperty("user.home");
    private static final String logFileName = "log.txt";


    public static void main(String[] args) {
        Path log = createLogFile();
        PrintWriter fileOutput = createPrintWriter();
        try {
            ru.yandex.practicum.wordle.WordleDictionaryLoader wdl = new ru.yandex.practicum.wordle.WordleDictionaryLoader(fileOutput);
            ru.yandex.practicum.wordle.WordleDictionary dictionary = wdl.loadDictionary(
                    "java-wordle4j\\words_ru.txt");
            ru.yandex.practicum.wordle.WordleGame game = new ru.yandex.practicum.wordle.WordleGame(dictionary, fileOutput);
            Scanner sc = new Scanner(System.in);

            while (game.stepsLeft()) {
                try {
                    System.out.println("Введите слово из 5 букв или слово *подсказка* для получения помощи " +
                            "\n напишите *выход*, чтобы сдаться");
                    String word = sc.nextLine();
                    if (word.equals("подсказка")) {
                        System.out.println(game.giveAHint());
                    } else if (word.equals("выход")) {
                        System.out.println(game.endGame());
                    } else {
                        System.out.println(game.takeAGuess(word));
                    }
                } catch (WordNotFoundInDictionaryException e) {
                    fileOutput.print(e.getMessage());
                    System.out.println("Попробуйте еще раз");
                } catch (WordIsAlreadyUsedException e) {
                    fileOutput.print(e.getMessage());
                    System.out.println(e.getMessage() + "Попробуйте еще раз!");
                } catch (HintDictionaryIsEmptyException e) {
                    fileOutput.print(e.getMessage() + "Ошибка со словарем.");
                }
            }
        } catch (NullPointerException e) {
            fileOutput.print(e.getMessage());
        } catch (FileNotFoundException e) {
            fileOutput.print(e.getMessage());
        } catch (IOException e) {
            fileOutput.print(e.getMessage());
        } catch (RuntimeException e) {
            fileOutput.print(e.getMessage());
        } finally {
            try {
                Files.delete(log.getFileName());
            } catch (IOException e) {
                System.err.format("log файл не найден.");
            }
            fileOutput.close();
        }
    }

    public static Path createLogFile() {
        Path file;
        Path path = Paths.get(HOME, logFileName);
        try {
            if (!Files.exists(path)) {
                file = Files.createFile(path);
            } else {
                file = Paths.get(logFileName);
            }
        } catch (IOException e) {
            file = path;
            System.err.format("Произошла ошибка при создании log файла.");
        }
        return file;
    }

    public static PrintWriter createPrintWriter() {
        PrintWriter fileOutput;
        try {
            fileOutput = new PrintWriter(logFileName);
        } catch (FileNotFoundException e) {
            System.err.format("log файл не найден.");
            fileOutput = null;
        }
        return fileOutput;

    }

}
