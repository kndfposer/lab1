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
            if (line.isEmpty() || line.startsWith("#") || line.startsWith("<!--")) {
                continue;
            }

            String[] parts = line.split(":", 2);
            if (parts.length < 2) {
                continue;
            }

            String key = parts[0].trim();
            String value = parts[1].trim();
            if (key.equals("missionId")) {
                mission.setMissionId(value);
            } else if (key.equals("date")) {
                mission.setDate(LocalDate.parse(value));
            } else if (key.equals("location")) {
                mission.setLocation(value);
            } else if (key.equals("outcome")) {
                mission.setOutcome(value);
            } else if (key.equals("damageCost")) {
                mission.setDamageCost(Long.parseLong(value));
            } else if (key.equals("note")) {
                mission.setNote(value);
            }
            else if (key.startsWith("curse.")) {
                String field = key.substring(6);
                handleCurse(mission, field, value);
            }
            else if (key.startsWith("sorcerer[")) {
                handleSorcererSimple(mission, key, value);
            }
            else if (key.startsWith("technique[")) {
                handleTechniqueSimple(mission, key, value);
            }
        }
        return mission;
    }

    private void handleCurse(Mission mission, String field, String value) {
        Curse curse = mission.getCurse();
        if (curse == null) {
            curse = new Curse();
            mission.setCurse(curse);
        }

        if (field.equals("name")) {
            curse.setName(value);
        } else if (field.equals("threatLevel")) {
            curse.setThreatLevel(value);
        }
    }

    private void handleSorcererSimple(Mission mission, String key, String value) {
        int startIdx = key.indexOf('[') + 1;
        int endIdx = key.indexOf(']');
        if (startIdx == 0 || endIdx == -1) return; // нет скобок

        String indexStr = key.substring(startIdx, endIdx);
        int index;
        try {
            index = Integer.parseInt(indexStr);
        } catch (NumberFormatException e) {
            return;
        }

        int dotIdx = key.indexOf('.', endIdx);
        if (dotIdx == -1) return;

        String field = key.substring(dotIdx + 1);
        List<Sorcerer> sorcerers = mission.getSorcerers();
        while (sorcerers.size() <= index) {
            sorcerers.add(new Sorcerer());
        }
        Sorcerer sorcerer = sorcerers.get(index);
        if (field.equals("name")) {
            sorcerer.setName(value);
        } else if (field.equals("rank")) {
            sorcerer.setRank(value);
        }
    }

    private void handleTechniqueSimple(Mission mission, String key, String value) {

        int startIdx = key.indexOf('[') + 1;
        int endIdx = key.indexOf(']');
        if (startIdx == 0 || endIdx == -1) return;

        String indexStr = key.substring(startIdx, endIdx);
        int index;
        try {
            index = Integer.parseInt(indexStr);
        } catch (NumberFormatException e) {
            return;
        }

        int dotIdx = key.indexOf('.', endIdx);
        if (dotIdx == -1) return;
        String field = key.substring(dotIdx + 1);
        List<Technique> techniques = mission.getTechniques();
        while (techniques.size() <= index) {
            techniques.add(new Technique());
        }
        Technique technique = techniques.get(index);
        if (field.equals("name")) {
            technique.setName(value);
        } else if (field.equals("type")) {
            technique.setType(value);
        } else if (field.equals("owner")) {
            technique.setOwner(value);
        } else if (field.equals("damage")) {
            try {
                technique.setDamage(Long.parseLong(value));
            } catch (NumberFormatException e) {
            }
        }
    }
}