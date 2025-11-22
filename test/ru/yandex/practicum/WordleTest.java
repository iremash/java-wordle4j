package ru.yandex.practicum;

import org.junit.jupiter.api.*;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

import ru.yandex.practicum.exception.WordIsAlreadyUsedException;
import ru.yandex.practicum.exception.WordNotFoundInDictionaryException;
import ru.yandex.practicum.wordle.*;

class WordleTest {
    PrintWriter log;
    WordleDictionary dictionary;
    WordleGame game;

    void loadTheGame() throws Exception {
        WordleDictionaryLoader wdl = new WordleDictionaryLoader(log);
        dictionary = wdl.loadDictionary(
                "words_ru.txt");
        game = new WordleGame(dictionary, log);
    }

    @Test
    void createsDictionary() throws Exception {
        loadTheGame();
        assertNotNull(dictionary);
    }

    @Test
    void createsGame() throws Exception {
        loadTheGame();
        assertNotNull(game);
    }

    @Test
    void gameThrowsFileNotInDictionaryException() throws Exception {
        loadTheGame();
        assertThrows(WordNotFoundInDictionaryException.class, () -> {
            game.checkWord("xx");
        });
    }

    @Test
    void gameThrowsFileIsAlreadyUsedException() throws Exception {
        loadTheGame();
        game.takeAGuess("кухня");
        assertThrows(WordIsAlreadyUsedException.class, () -> {
            game.takeAGuess("кухня");
        });
    }

    @Test
    void givesAHint() throws Exception {
        loadTheGame();
        game.takeAGuess("башня");
        String hint = game.giveAHint();
        assertNotNull(hint);
    }

    @Test
    void writesUsedWordsToMap() throws Exception {
        loadTheGame();
        game.takeAGuess("кузня");
        assertNotNull(game.getUsedWords().get("кузня"));
    }

}


