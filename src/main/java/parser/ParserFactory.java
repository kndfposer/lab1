package parser;
import ui.FileChooser;
public class ParserFactory {
    public static MissionParser getParser(FileChooser.FileFormat format) {
        switch (format) {
            case TEXT:
                return new TextMissionParser();
            case JSON:
                return new JsonMissionParser();
            case XML:
                return new XmlMissionParser();
            default:
                throw new IllegalArgumentException("Неподдерживаемый формат: " + format);
        }
    }
}