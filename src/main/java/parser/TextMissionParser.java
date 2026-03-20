package parser;

import model.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;

public class TextMissionParser implements MissionParser {

    @Override
    public Mission parse(File file) throws IOException {
        Mission mission = new Mission();
        List<String> lines = Files.readAllLines(file.toPath());

        for (String line : lines) {
            line = line.trim();

            // Пропускаем пустые строки и комментарии
            if (line.isEmpty() || line.startsWith("#") || line.startsWith("<!--")) {
                continue;
            }

            // Разделяем строку на ключ и значение по первому двоеточию
            String[] parts = line.split(":", 2);
            if (parts.length < 2) {
                continue;
            }

            String key = parts[0].trim();
            String value = parts[1].trim();

            // Обрабатываем простые поля миссии
            if (key.equals("missionId")) {
                mission.setMissionId(value);
            }
            else if (key.equals("date")) {
                mission.setDate(LocalDate.parse(value));
            }
            else if (key.equals("location")) {
                mission.setLocation(value);
            }
            else if (key.equals("outcome")) {
                mission.setOutcome(value);
            }
            else if (key.equals("damageCost")) {
                mission.setDamageCost(Long.parseLong(value));
            }
            else if (key.equals("note")) {
                mission.setNote(value);
            }
            // Обрабатываем проклятие (curse.name, curse.threatLevel)
            else if (key.startsWith("curse.")) {
                String field = key.substring(6); // отрезаем "curse."
                handleCurse(mission, field, value);
            }
            // Обрабатываем магов (sorcerer[0].name, sorcerer[0].rank)
            else if (key.startsWith("sorcerer[")) {
                handleSorcerer(mission, key, value);
            }
            // Обрабатываем техники (technique[0].name, technique[0].owner и т.д.)
            else if (key.startsWith("technique[")) {
                handleTechnique(mission, key, value);
            }
            // Все остальные ключи игнорируем
        }

        return mission;
    }

    /**
     * Обработка полей проклятия
     */
    private void handleCurse(Mission mission, String field, String value) {
        Curse curse = mission.getCurse();

        // Если проклятия ещё нет, создаём новое
        if (curse == null) {
            curse = new Curse();
            mission.setCurse(curse);
        }

        // Заполняем соответствующее поле
        if (field.equals("name")) {
            curse.setName(value);
        } else if (field.equals("threatLevel")) {
            curse.setThreatLevel(value);
        }
    }

    /**
     * Обработка полей магов
     * Ключ имеет формат: sorcerer[индекс].поле
     */
    private void handleSorcerer(Mission mission, String key, String value) {
        // Находим индекс в квадратных скобках
        int startIdx = key.indexOf('[') + 1;
        int endIdx = key.indexOf(']');

        // Проверяем, что скобки существуют и корректны
        if (startIdx == 0 || endIdx == -1) {
            return; // неправильный формат
        }

        // Извлекаем индекс и преобразуем в число
        String indexStr = key.substring(startIdx, endIdx);
        int index;
        try {
            index = Integer.parseInt(indexStr);
        } catch (NumberFormatException e) {
            return; // индекс не число
        }

        // Находим точку после закрывающей скобки и извлекаем имя поля
        int dotIdx = key.indexOf('.', endIdx);
        if (dotIdx == -1) {
            return; // нет точки
        }
        String field = key.substring(dotIdx + 1);

        // Получаем список магов и расширяем его до нужного индекса
        List<Sorcerer> sorcerers = mission.getSorcerers();
        while (sorcerers.size() <= index) {
            sorcerers.add(new Sorcerer());
        }

        // Получаем мага по индексу
        Sorcerer sorcerer = sorcerers.get(index);

        // Заполняем соответствующее поле
        if (field.equals("name")) {
            sorcerer.setName(value);
        } else if (field.equals("rank")) {
            sorcerer.setRank(value);
        }
    }
    private void handleTechnique(Mission mission, String key, String value) {
        // Находим индекс в квадратных скобках
        int startIdx = key.indexOf('[') + 1;
        int endIdx = key.indexOf(']');

        if (startIdx == 0 || endIdx == -1) {
            return;
        }

        // Извлекаем индекс
        String indexStr = key.substring(startIdx, endIdx);
        int index;
        try {
            index = Integer.parseInt(indexStr);
        } catch (NumberFormatException e) {
            return;
        }

        // Находим точку и имя поля
        int dotIdx = key.indexOf('.', endIdx);
        if (dotIdx == -1) {
            return;
        }
        String field = key.substring(dotIdx + 1);

        // Получаем список техник и расширяем его
        List<Technique> techniques = mission.getTechniques();
        while (techniques.size() <= index) {
            techniques.add(new Technique());
        }

        // Получаем технику по индексу
        Technique technique = techniques.get(index);

        // Заполняем поля в зависимости от имени поля
        if (field.equals("name")) {
            technique.setName(value);
        }
        else if (field.equals("type")) {
            technique.setType(value);
        }
        else if (field.equals("owner")) {
            // СОХРАНЯЕМ ИМЯ ВЛАДЕЛЬЦА КАК СТРОКУ
            technique.setOwnerName(value);

            // ПЫТАЕМСЯ НАЙТИ МАГА С ТАКИМ ИМЕНЕМ
            Sorcerer owner = findSorcererByName(mission, value);
            if (owner != null) {
                technique.setOwner(owner); // Устанавливаем ссылку на объект
            }
            // Если маг не найден - owner остаётся null, но ownerName сохранён
        }
        else if (field.equals("damage")) {
            try {
                technique.setDamage(Long.parseLong(value));
            } catch (NumberFormatException e) {
                // игнорируем некорректное значение
            }
        }
    }

    private Sorcerer findSorcererByName(Mission mission, String name) {
        for (Sorcerer sorcerer : mission.getSorcerers()) {
            if (name.equals(sorcerer.getName())) {
                return sorcerer;
            }
        }
        return null;
    }
}