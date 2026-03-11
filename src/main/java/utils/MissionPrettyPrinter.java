package utils;

import model.*;

public class MissionPrettyPrinter {
    public static void print(Mission mission) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("                 ИНФОРМАЦИЯ О МИССИИ");
        System.out.println("=".repeat(60));

        printSection("ОСНОВНЫЕ ДАННЫЕ");
        printField("ID миссии", mission.getMissionId());
        printField("Дата", mission.getDate() != null ? mission.getDate().toString() : "не указана");
        printField("Место", mission.getLocation());
        printField("Результат", mission.getOutcome());
        printField("Ущерб", mission.getDamageCost() + " ед.");

        printSection("ПРОКЛЯТИЕ");
        if (mission.getCurse() != null) {
            printField("Название", mission.getCurse().getName());
            printField("Уровень угрозы", mission.getCurse().getThreatLevel());
        } else {
            System.out.println("   Нет информации о проклятии");
        }

        printSection("УЧАСТНИКИ");
        if (mission.getSorcerers() != null && !mission.getSorcerers().isEmpty()) {
            for (int i = 0; i < mission.getSorcerers().size(); i++) {
                Sorcerer s = mission.getSorcerers().get(i);
                System.out.println("   Маг #" + (i + 1));
                printField("   Имя", s.getName(), 3);
                printField("   Ранг", s.getRank(), 3);
                System.out.println();
            }
        } else {
            System.out.println("    Нет информации об участниках");
        }

        printSection("ПРИМЕНЁННЫЕ ТЕХНИКИ");
        if (mission.getTechniques() != null && !mission.getTechniques().isEmpty()) {
            for (int i = 0; i < mission.getTechniques().size(); i++) {
                Technique t = mission.getTechniques().get(i);
                System.out.println(" Техника #" + (i + 1));
                printField("   Название", t.getName(), 3);
                printField("   Тип", t.getType(), 3);

                String ownerName = (t.getOwner() != null) ? t.getOwner().getName() : "неизвестен";
                printField("   Владелец", ownerName, 3);

                printField("   Урон", t.getDamage() + " ед.", 3);
                System.out.println();
            }
        } else {
            System.out.println("   Нет информации о техниках");
        }


        String anyNote = mission.getAnyNote();
        if (anyNote != null && !anyNote.isEmpty()) {
            printSection("ПРИМЕЧАНИЯ");
            System.out.println("   " + anyNote);
        }

        System.out.println("=".repeat(60));
    }

    private static void printSection(String title) {
        System.out.println("\n--- " + title + " " + "-".repeat(50 - title.length() - 4));
    }

    private static void printField(String label, String value) {
        printField(label, value, 0);
    }

    private static void printField(String label, String value, int indent) {
        String indentStr = " ".repeat(indent);
        System.out.printf("%s%-15s: %s%n", indentStr, label, value != null ? value : "не указано");
    }
}