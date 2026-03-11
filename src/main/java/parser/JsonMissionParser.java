package parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import model.Mission;
import model.Sorcerer;
import model.Technique;

import java.io.File;
import java.io.IOException;

public class JsonMissionParser implements MissionParser {
    private final ObjectMapper objectMapper;

    public JsonMissionParser() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public Mission parse(File file) throws IOException {

        Mission mission = objectMapper.readValue(file, Mission.class);

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