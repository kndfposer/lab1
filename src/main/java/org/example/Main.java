package org.example;

import model.Mission;
import parser.MissionParser;
import parser.ParserFactory;
import ui.FileChooser;

public class Main {
    public static void main(String[] args) {
        FileChooser chooser = new FileChooser();
        FileChooser.FileFormat format = chooser.start();
        if (format == FileChooser.FileFormat.UNKNOWN) {
            System.out.println("Не удалось определить формат файла или файл не выбран.");
            return;
        }

        try {
            MissionParser parser = ParserFactory.getParser(format);
            Mission mission = parser.parse(chooser.getSelectedFile());
            System.out.println("\nМиссия успешно загружена:");
            System.out.println(mission);
        } catch (Exception e) {
            System.out.println("Ошибка при парсинге файла: " + e.getMessage());
            e.printStackTrace();
        }
    }
}