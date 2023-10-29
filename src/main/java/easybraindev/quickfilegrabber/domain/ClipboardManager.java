package easybraindev.quickfilegrabber.domain;

import com.intellij.openapi.ide.CopyPasteManager;

import java.awt.datatransfer.StringSelection;

public class ClipboardManager {

    public void copyToClipboard(String content) {
        StringSelection stringSelection = new StringSelection(content);
        CopyPasteManager.getInstance().setContents(stringSelection);
    }
}
