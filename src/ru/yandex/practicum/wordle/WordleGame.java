package ru.yandex.practicum.wordle;

import ru.yandex.practicum.exception.HintDictionaryIsEmptyException;
import ru.yandex.practicum.exception.WordIsAlreadyUsedException;
import ru.yandex.practicum.exception.WordNotFoundInDictionaryException;

import java.io.PrintWriter;
import java.util.*;

/*
в этом классе хранится словарь и состояние игры
    текущий шаг
    всё что пользователь вводил
    правильный ответ

в этом классе нужны методы, которые
    проанализируют совпадение слова с ответом
    предложат слово-подсказку с учётом всего, что вводил пользователь ранее

не забудьте про специальные типы исключений для игровых и неигровых ошибок
 */
public class WordleGame {

    private final String answer;

    private int steps;

    private final WordleDictionary dictionary;

    private final LinkedHashMap<String, String> usedWords = new LinkedHashMap<>();

    final int WORD_LENGTH = 5;
    private final Map<String, ArrayList<Integer>> rightLetters = new LinkedHashMap<>();
    private final List<String> misplacedLetters = new LinkedList<>();
    private final List<String> wrongLetters = new LinkedList<>();
    private final List<String> hints = new LinkedList<>();
    PrintWriter fileOutput;
    Random random = new Random();


    public LinkedHashMap<String, String> getUsedWords() {
        return usedWords;
    }

    public WordleGame(WordleDictionary dictionary, PrintWriter fileOutput) {
        this.fileOutput = fileOutput;
        this.dictionary = dictionary;
        answer = dictionary.getWord(random.nextInt(0, dictionary.getLength()));
        steps = 6;
    }

    public String takeAGuess(String word) throws WordNotFoundInDictionaryException, WordIsAlreadyUsedException {
        checkWord(word);
        StringBuilder sb = new StringBuilder();
        if (word.equals(answer)) {
            steps = 0;
            return "+++++ \n Вы победили!";
        }
        List<Character> available = checkForRightLetters(word, sb);
        checkOtherLetters(word, sb, available);
        usedWords.put(word, sb.toString());
        steps--;
        if (!stepsLeft()) {
            sb.append(" Вы проиграли( Правильный ответ был: " + answer);
        }
        return sb.toString();
    }

    public boolean stepsLeft() {
        return !(steps == 0);
    }

    public boolean isUsed(String word) {
        return (usedWords.containsKey(word));
    }

    public String giveAHint() throws HintDictionaryIsEmptyException, RuntimeException {
        List<String> words = dictionary.findWordsForHint(rightLetters, wrongLetters, misplacedLetters, usedWords, hints);
        if (words.isEmpty()) {
            throw new HintDictionaryIsEmptyException();
        }
        String hint = answer;
        int countControl = 0;
        while (hint.equals(answer)) {
            hint = words.get(random.nextInt(0, words.size()));
            countControl++;
            if (countControl == 10) {
                throw new RuntimeException();
            }
        }
        hints.add(hint);
        return hint;
    }

    public void checkWord(String word) throws WordNotFoundInDictionaryException, WordIsAlreadyUsedException {
        if (!dictionary.isInDictionary(word)) {
            throw new WordNotFoundInDictionaryException();
        } else if (isUsed(word)) {
            throw new WordIsAlreadyUsedException("Слово " + word + " уже вводилось: " + usedWords.get(word));
        }
    }

    public List<Character> checkForRightLetters(String word, StringBuilder sb) {
        List<Character> available = new ArrayList<>();
        for (int i = 0; i < WORD_LENGTH; i++) {
            String letter = String.valueOf(word.charAt(i));
            String answerLetter = String.valueOf(answer.charAt(i));
            available.add(answer.charAt(i));
            if (letter.equals(answerLetter)) {
                sb.append('+');
                rightLetters.computeIfAbsent(letter, initializeWith -> new ArrayList<>()).add(i);
                available.set(available.indexOf(word.charAt(i)), null);
            } else {
                sb.append("-");
            }
        }
        return available;
    }

    public void checkOtherLetters(String word, StringBuilder sb, List<Character> available) {
        for (int i = 0; i < WORD_LENGTH; i++) {
            char chLetter = word.charAt(i);
            String sLetter = String.valueOf(chLetter);
            if (answer.contains(sLetter) && available.contains(chLetter)) {
                sb.setCharAt(i, '^');
                available.set(available.indexOf(chLetter), null);
                misplacedLetters.add(sLetter);
            } else if (!rightLetters.containsKey(sLetter) && !misplacedLetters.contains(sLetter)) {
                wrongLetters.add(sLetter);
            }
        }
    }

    public String endGame() {
        steps = 0;
        return "Вы проиграли( Правильный ответ был: " + answer;
    }


}
