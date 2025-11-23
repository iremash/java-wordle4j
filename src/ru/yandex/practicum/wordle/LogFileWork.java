package ru.yandex.practicum.wordle;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LogFileWork {
    private static final String HOME = System.getProperty("user.home");
    private static final String logFileName = "log.txt";
    private final Path log;
    private final PrintWriter logWriter;

    public LogFileWork() {
        log = createLogFile();
        logWriter = createPrintWriter();
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
            System.exit(-1);
        }
        return file;
    }

    public static PrintWriter createPrintWriter() {
        PrintWriter logWriter;
        try {
            logWriter = new PrintWriter(logFileName);
        } catch (FileNotFoundException e) {
            System.err.format("log файл не найден.");
            logWriter = null;
            System.exit(-1);
        }
        return logWriter;

    }

    public void writeMistake(Exception e) {
        logWriter.print(e.getMessage());
    }

    public void endWork() {
        try {
            Files.delete(log.getFileName());
        } catch (IOException e) {
            System.err.format("log файл не найден.");
        }
        logWriter.close();
    }
}
