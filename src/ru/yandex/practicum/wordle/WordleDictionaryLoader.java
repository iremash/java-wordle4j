package ru.yandex.practicum.wordle;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/*
этот класс содержит в себе всю рутину по работе с файлами словарей и с кодировками
    ему нужны методы по загрузке списка слов из файла по имени файла
    на выходе должен быть класс WordleDictionary
 */
public class WordleDictionaryLoader {
    final int wordsLength = 5;
    PrintWriter fileOutput;

    public WordleDictionaryLoader(PrintWriter fileOutput) {
        this.fileOutput = fileOutput;
    }

    public WordleDictionary loadDictionary(String filename) throws IOException {
        WordleDictionary wd;
        try (BufferedReader bf = new BufferedReader((new FileReader(filename, StandardCharsets.UTF_8)))) {
            List<String> words = new ArrayList<>();
            while (bf.ready()) {
                String word = bf.readLine();
                if (hasFiveLetters(word)) {
                    words.add(getStandard(word));
                }
            }
            wd = new WordleDictionary(words, fileOutput);
        }
        return wd;
    }

    public String getStandard(String word) {
        return word.replace("ё", "е").toLowerCase();
    }

    public Boolean hasFiveLetters(String word) {
        return (word.length() == wordsLength);
    }


}
