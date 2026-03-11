package org.example;

import model.Mission;
import parser.MissionParser;
import parser.ParserFactory;
import ui.FileChooser;
import utils.MissionPrettyPrinter;
import java.util.Scanner;

public class MissionManager {
    private Scanner scanner;

    public MissionManager() {
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        printWelcome();

        while (true) {
            FileChooser chooser = new FileChooser();
            FileChooser.FileFormat format = chooser.start();

            if (format == FileChooser.FileFormat.UNKNOWN) {
                if (!handleFileNotFound()) break;
                continue;
            }

            try {
                MissionParser parser = ParserFactory.getParser(format);
                Mission mission = parser.parse(chooser.getSelectedFile());
                MissionPrettyPrinter.print(mission);

                if (!askToContinue()) break;
            } catch (Exception e) {
                handleError(e);
                if (!askToContinue()) break;
            }
        }

        scanner.close();
        printGoodbye();
    }

    private void printWelcome() {
        System.out.println("Для выхода введите 0 вместо пути к файлу");
    }

    private void printGoodbye() {
        System.out.println("ПРОГРАММА ЗАВЕРШЕНА");
    }

    private boolean handleFileNotFound() {
        System.out.print("Файл не найден. Хотите выйти? (0 - да, любой символ - попробовать снова): ");
        return !scanner.nextLine().trim().equals("0");
    }

    private boolean askToContinue() {
        System.out.print("\nНажмите Enter для продолжения или введите 0 для выхода: ");
        return !scanner.nextLine().trim().equals("0");
    }

    private void handleError(Exception e) {
        System.out.println("\n ОШИБКА ПРИ ПАРСИНГЕ ФАЙЛА");
        System.out.println("Причина: " + e.getMessage());
        System.out.println("\nДетали ошибки:");
        e.printStackTrace();
    }
}