package ru.yandex.practicum.wordle;


import ru.yandex.practicum.exception.HintDictionaryIsEmptyException;
import ru.yandex.practicum.exception.WordIsAlreadyUsedException;
import ru.yandex.practicum.exception.WordNotFoundInDictionaryException;

import java.io.IOException;
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
    private static LogFileWork fileWork;


    public static void main(String[] args) {
        try {
            WordleGame game = createGame();
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
                    fileWork.writeMistake(e);
                    System.out.println("Попробуйте еще раз");
                } catch (WordIsAlreadyUsedException e) {
                    fileWork.writeMistake(e);
                    System.out.println(e.getMessage() + "Попробуйте еще раз!");
                } catch (HintDictionaryIsEmptyException e) {
                    fileWork.writeMistake(e);
                    System.out.println("Словарь подсказок пуст(");
                }
            }
        } catch (IOException e) {
            fileWork.writeMistake(e);
        } catch (RuntimeException e) {
            fileWork.writeMistake(e);
        } finally {
            fileWork.endWork();
        }
    }

    public static WordleGame createGame() throws IOException {
        fileWork = new LogFileWork();
        WordleDictionaryLoader wdl = new WordleDictionaryLoader(fileWork);
        WordleDictionary dictionary = wdl.loadDictionary("words_ru.txt");
        return new WordleGame(dictionary, fileWork);
    }

}
