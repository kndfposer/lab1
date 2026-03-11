package ui;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class FileChooser {
    private File selectedFile;
    private FileFormat format;

    public enum FileFormat {
        JSON, XML, TEXT, UNKNOWN
    }

    public FileFormat start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите путь к файлу миссии:");
        String filePath = scanner.nextLine().trim();

        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("Ошибка: файл не найден по пути: " + filePath);
            return FileFormat.UNKNOWN;
        }
        if (!file.isFile()) {
            System.out.println("Ошибка: указанный путь не является файлом.");
            return FileFormat.UNKNOWN;
        }

        this.selectedFile = file;
        this.format = detectFormat(file);
        System.out.println("Файл: " + file.getAbsolutePath());
        System.out.println("Определённый формат: " + format);
        return format;
    }

    public File getSelectedFile() {
        return selectedFile;
    }

    public FileFormat getFormat() {
        return format;
    }

    public static FileFormat detectFormat(File file) {
        String fileName = file.getName().toLowerCase();

        if (fileName.endsWith(".json")) {
            return FileFormat.JSON;
        } else if (fileName.endsWith(".xml")) {
            return FileFormat.XML;
        } else if (fileName.endsWith(".txt")) {
            return FileFormat.TEXT;
        }
        try {
            String contentStart = Files.readString(Path.of(file.getPath())).trim();
            if (contentStart.isEmpty()) {
                return FileFormat.TEXT;
            }
            char firstChar = contentStart.charAt(0);
            if (firstChar == '{') {
                return FileFormat.JSON;
            } else if (firstChar == '<') {
                return FileFormat.XML;
            } else {
                return FileFormat.TEXT;
            }
        } catch (IOException e) {
            return FileFormat.UNKNOWN;
        }
    }
}