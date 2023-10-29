package easybraindev.quickfilegrabber.domain;

public class FileFormatter {

    public String formatFileContent(String fileName, String content, FormattingConfiguration config) {
        if (config.shouldRemoveTypeScriptImports() && fileName.endsWith(".ts")) {
            content = removeTypeScriptImports(content);
        }
        return "\"" + fileName + ":\n" + content + "\"";
    }

    private String removeTypeScriptImports(String content) {
        String[] lines = content.split("\n");
        StringBuilder result = new StringBuilder();
        for (String line : lines) {
            if (!line.trim().startsWith("import ")) {
                result.append(line).append("\n");
            }
        }
        return result.toString();
    }
}
