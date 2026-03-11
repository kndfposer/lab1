package org.example;

import model.Mission;
import parser.MissionParser;
import parser.ParserFactory;
import ui.FileChooser;
import java.util.Scanner;

public class MissionManager {
    private Scanner scanner;
    public MissionManager() {
        this.scanner = new Scanner(System.in);
    }
    public void start() {
        System.out.println("Для выхода введите 0 вместо пути к файлу");

        while (true) {
            FileChooser chooser = new FileChooser();
            FileChooser.FileFormat format = chooser.start();

            if (format == FileChooser.FileFormat.UNKNOWN) {
                if (!handleFileNotFound()) {
                    break;
                }
                continue;
            }

            try {
                MissionParser parser = ParserFactory.getParser(format);
                Mission mission = parser.parse(chooser.getSelectedFile());
                displayMission(mission);

                if (!askToContinue()) {
                    break;
                }

            } catch (Exception e) {
                handleError(e);
                if (!askToContinue()) {
                    break;
                }
            }
        }

        scanner.close();
        System.out.println("Программа завершена.");
    }

    private boolean handleFileNotFound() {
        System.out.print("Файл не найден. Хотите выйти? (0 - да, любой символ - попробовать снова): ");
        String exitChoice = scanner.nextLine().trim();
        return !exitChoice.equals("0");
    }

    private void displayMission(Mission mission) {
        System.out.println("\nМиссия успешно загружена:");
        System.out.println(mission);
    }

    private boolean askToContinue() {
        System.out.print("\nНажмите Enter для продолжения или введите 0 для выхода: ");
        String continueChoice = scanner.nextLine().trim();
        return !continueChoice.equals("0");
    }

    private void handleError(Exception e) {
        System.out.println("\nОшибка при парсинге файла: " + e.getMessage());
        e.printStackTrace();
    }
}