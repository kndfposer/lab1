package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@JacksonXmlRootElement(localName = "mission")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Mission {
    private String missionId;
    private LocalDate date;
    private String location;
    private String outcome;
    private long damageCost;
    private Curse curse;

    @JacksonXmlElementWrapper(localName = "sorcerers")
    @JacksonXmlProperty(localName = "sorcerer")
    private List<Sorcerer> sorcerers = new ArrayList<>();

    @JacksonXmlElementWrapper(localName = "techniques")
    @JacksonXmlProperty(localName = "technique")
    private List<Technique> techniques = new ArrayList<>();

    private String note;

    // Конструкторы
    public Mission() {}

    // Геттеры и сеттеры
    public String getMissionId() { return missionId; }
    public void setMissionId(String missionId) { this.missionId = missionId; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getOutcome() { return outcome; }
    public void setOutcome(String outcome) { this.outcome = outcome; }

    public long getDamageCost() { return damageCost; }
    public void setDamageCost(long damageCost) { this.damageCost = damageCost; }

    public Curse getCurse() { return curse; }
    public void setCurse(Curse curse) { this.curse = curse; }

    public List<Sorcerer> getSorcerers() { return sorcerers; }
    public void setSorcerers(List<Sorcerer> sorcerers) { this.sorcerers = sorcerers; }

    public List<Technique> getTechniques() { return techniques; }
    public void setTechniques(List<Technique> techniques) { this.techniques = techniques; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    @Override
    public String toString() {
        return "Mission{" +
                "missionId='" + missionId + '\'' +
                ", date=" + date +
                ", location='" + location + '\'' +
                ", outcome='" + outcome + '\'' +
                ", damageCost=" + damageCost +
                ", curse=" + curse +
                ", sorcerers=" + sorcerers +
                ", techniques=" + techniques +
                ", note='" + note + '\'' +
                '}';
    }
}