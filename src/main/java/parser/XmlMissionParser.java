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

        linkTechniquesToSorcerers(mission);

        return mission;
    }

    private void linkTechniquesToSorcerers(Mission mission) {
        for (Technique technique : mission.getTechniques()) {

            String ownerName = technique.getOwnerName();

            if (ownerName != null && !ownerName.isEmpty()) {
                Sorcerer owner = findSorcererByName(mission, ownerName);
                if (owner != null) {
                    technique.setOwner(owner);
                } else {
                    System.out.println("Предупреждение: владелец техники '" + ownerName + "' не найден");
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