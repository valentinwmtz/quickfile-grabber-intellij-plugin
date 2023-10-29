package easybraindev.quickfilegrabber.domain;

import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.vfs.VirtualFile;

import javax.swing.*;

public class FileSelectionHandler implements FileEditorManagerListener {
    private final JList<String> fileList;

    public FileSelectionHandler(JList<String> fileList) {
        this.fileList = fileList;
    }

    @Override
    public void selectionChanged(FileEditorManagerEvent event) {
        VirtualFile newFile = event.getNewFile();
        if (newFile != null) {
            fileList.setSelectedValue(newFile.getName(), true);
        }
    }
}
