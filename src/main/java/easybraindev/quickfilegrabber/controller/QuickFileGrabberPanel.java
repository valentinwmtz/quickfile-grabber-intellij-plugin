package easybraindev.quickfilegrabber.controller;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.messages.MessageBusConnection;

import java.util.List;
import java.util.Collections;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.intellij.ui.components.JBList;

public class QuickFileGrabberPanel extends JPanel {

    private final Project project;
    private final JList<String> fileList;
    private final DefaultListModel<String> fileListModel;
    private final JButton copySelectedButton;
    private final JButton copyAllButton;

    public QuickFileGrabberPanel(Project project) {
        this.project = project;

        this.setLayout(new BorderLayout());

        // Initialize components
        fileListModel = new DefaultListModel<>();
        fileList = new JBList<>(fileListModel);
        copySelectedButton = new JButton("Copy Selected");
        copyAllButton = new JButton("Copy All");

        // Add action listeners
        copySelectedButton.addActionListener(new CopySelectedListener());
        copyAllButton.addActionListener(new CopyAllListener());

        // Populate the file list
        populateFileList();

        // Add components to the panel
        JScrollPane listScroller = new JScrollPane(fileList);
        this.add(listScroller, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(copySelectedButton);
        buttonPanel.add(copyAllButton);
        this.add(buttonPanel, BorderLayout.SOUTH);

        MessageBusConnection connection = project.getMessageBus().connect();
        connection.subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, new FileEditorFocusChangeListener());
    }

    private class FileEditorFocusChangeListener implements FileEditorManagerListener {
        @Override
        public void selectionChanged(FileEditorManagerEvent event) {
            VirtualFile newFile = event.getNewFile();
            if (newFile != null) {
                fileList.setSelectedValue(newFile.getName(), true);
            }
        }
    }

    private void populateFileList() {
        Editor[] editors = EditorFactory.getInstance().getAllEditors();
        FileDocumentManager fileDocumentManager = FileDocumentManager.getInstance();
        VirtualFile currentFile = FileEditorManager.getInstance(project).getSelectedFiles()[0];

        for (Editor editor : editors) {
            VirtualFile file = fileDocumentManager.getFile(editor.getDocument());
            if (file != null) {
                fileListModel.addElement(file.getName());
                if (file.equals(currentFile)) {
                    fileList.setSelectedValue(file.getName(), true);
                }
            }
        }
    }

    private class CopySelectedListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            java.util.List<String> selectedFilesNames = fileList.getSelectedValuesList();
            copyFilesContentToClipboard(selectedFilesNames);
        }
    }

    private class CopyAllListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            java.util.List<String> allFilesNames = Collections.list(fileListModel.elements());
            copyFilesContentToClipboard(allFilesNames);
        }
    }

    private void copyFilesContentToClipboard(List<String> fileNames) {
        StringBuilder filesContent = new StringBuilder();

        for (String fileName : fileNames) {
            VirtualFile file = findFileByName(fileName);
            if (file != null) {
                Document document = FileDocumentManager.getInstance().getDocument(file);
                if (document != null) {
                    String fileContent = document.getText();
                    filesContent.append(formatFileContent(fileName, fileContent)).append("\n\n");
                }
            }
        }

        copyToClipboard(filesContent.toString());
    }

    private VirtualFile findFileByName(String fileName) {
        Editor[] editors = EditorFactory.getInstance().getAllEditors();
        for (Editor editor : editors) {
            VirtualFile file = FileDocumentManager.getInstance().getFile(editor.getDocument());
            if (file != null && fileName.equals(file.getName())) {
                return file;
            }
        }
        return null;
    }

    private String formatFileContent(String fileName, String content) {
        return "\"" + fileName + ":\n" + content + "\"";
    }

    private void copyToClipboard(String content) {
        StringSelection stringSelection = new StringSelection(content);
        CopyPasteManager.getInstance().setContents(stringSelection);
    }
}
