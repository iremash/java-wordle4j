package ru.yandex.practicum.wordle;


import java.util.*;

/*
этот класс содержит в себе список слов List<String>
    его методы похожи на методы списка, но учитывают особенности игры
    также этот класс может содержать рутинные функции по сравнению слов, букв и т.д.
 */
public class WordleDictionary {
    private final List<String> words;
    private final LogFileWork logWriter;

    public WordleDictionary(List<String> words, LogFileWork logWriter) {
        this.words = words;
        this.logWriter = logWriter;
    }

    public boolean isInDictionary(String word) {
        return (words.contains(word));
    }

    public String getWord(int index) {
        return words.get(index);
    }

    public int getLength() {
        return words.size();
    }


    public List<String> findWordsForHint(Map<String, ArrayList<Integer>> rightLetters,
                                         List<String> wrongLetters, List<String> misplacedLetters,
                                         HashMap<String, String> usedWords, List<String> hints) {
        List<String> wordsForHint = new ArrayList<>();
        for (String word : words) {
            boolean isSuitable = true;
            if (usedWords.containsKey(word) || hints.contains(word)) {
                continue;
            }
            for (String wrongLetter : wrongLetters) {
                if (word.contains(wrongLetter)) {
                    isSuitable = false;
                    break;
                }
            }
            if (!isSuitable) continue;
            for (String misplacedLetter : misplacedLetters) {
                if (!word.contains(misplacedLetter)) {
                    isSuitable = false;
                    break;
                }
            }
            if (!isSuitable) continue;
            for (String rightLetter : rightLetters.keySet()) {
                ArrayList<Integer> indexes = rightLetters.get(rightLetter);
                for (int index : indexes) {
                    if (!rightLetter.equals(String.valueOf(word.charAt(index)))) {
                        isSuitable = false;
                        break;
                    }
                }
            }

            if (isSuitable) {
                wordsForHint.add(word);
            }
        }
        return wordsForHint;
    }


}
