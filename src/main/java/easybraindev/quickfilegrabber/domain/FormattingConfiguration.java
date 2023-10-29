package easybraindev.quickfilegrabber.domain;

public class FormattingConfiguration {
    private boolean removeTypeScriptImports;

    public boolean shouldRemoveTypeScriptImports() {
        return removeTypeScriptImports;
    }

    public void setRemoveTypeScriptImports(boolean removeTypeScriptImports) {
        this.removeTypeScriptImports = removeTypeScriptImports;
    }
}
