package parser;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import model.Mission;
import model.Sorcerer;
import model.Technique;

import java.io.File;
import java.io.IOException;

public class XmlMissionParser implements MissionParser {
    private final XmlMapper xmlMapper;

    public XmlMissionParser() {
        this.xmlMapper = new XmlMapper();
        this.xmlMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public Mission parse(File file) throws IOException {
        Mission mission = xmlMapper.readValue(file, Mission.class);

        validateAndFixMission(mission);
        linkTechniquesToSorcerers(mission);

        return mission;
    }

    private void validateAndFixMission(Mission mission) {
        if (mission.getDamageCost() < 0) {
            System.out.println("Предупреждение: ущерб миссии не может быть отрицательным (" +
                    mission.getDamageCost() + "). Установлено значение 0");
            mission.setDamageCost(0);
        }

        for (Technique technique : mission.getTechniques()) {
            if (technique.getDamage() < 0) {
                System.out.println("Предупреждение: урон техники '" +
                        technique.getName() + "' не может быть отрицательным (" +
                        technique.getDamage() + "). Установлено значение 0");
                technique.setDamage(0);
            }
        }
    }

    private void linkTechniquesToSorcerers(Mission mission) {
        for (Technique technique : mission.getTechniques()) {
            String ownerName = technique.getOwnerName();

            if (ownerName != null && !ownerName.isEmpty()) {
                Sorcerer owner = findSorcererByName(mission, ownerName);
                if (owner != null) {
                    technique.setOwner(owner);
                } else {
                    System.out.println("Предупреждение: владелец техники '" + ownerName + "' не найден в списке участников");
                }
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