package org.example;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class FileChooser {
    public enum FileFormat {
        JSON, HTML, TEXT, UNKNOWN
    }
    public static FileFormat detectFormat(File file) {
        String fileName = file.getName().toLowerCase();
        if (fileName.endsWith(".json")) {
            return FileFormat.JSON;
        } else if (fileName.endsWith(".html") || fileName.endsWith(".htm")) {
            return FileFormat.HTML;
        } else if (fileName.endsWith(".txt")) {
            return FileFormat.TEXT;
        }

        // 2. Анализ первых символов содержимого
        try {
            String contentStart = Files.readString(Path.of(file.getPath())).trim();
            if (contentStart.isEmpty()) {
                return FileFormat.TEXT;
            }
            char firstChar = contentStart.charAt(0);
            if (firstChar == '{') {
                return FileFormat.JSON;
            } else if (firstChar == '<') {
                return FileFormat.HTML;
            } else {
                return FileFormat.TEXT;
            }
        } catch (IOException e) {
            return FileFormat.UNKNOWN;
        }
    }
    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите путь к файлу миссии:");
        String filePath = scanner.nextLine().trim();

        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("Ошибка: файл не найден по пути: " + filePath);
            return;
        }
        if (!file.isFile()) {
            System.out.println("Ошибка: указанный путь не является файлом.");
            return;
        }

        FileFormat format = FileChooser.detectFormat(file);
        System.out.println("Файл: " + file.getAbsolutePath());
        System.out.println("Определённый формат: " + format);
    }
}