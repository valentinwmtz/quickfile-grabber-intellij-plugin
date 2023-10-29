package easybraindev.quickfilegrabber.domain;

public class FileFormatter {

    public String formatFileContent(String fileName, String content) {
        return "\"" + fileName + ":\n" + content + "\"";
    }
}
