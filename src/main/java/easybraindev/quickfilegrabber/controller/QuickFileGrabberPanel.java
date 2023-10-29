package easybraindev.quickfilegrabber.controller;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.messages.MessageBusConnection;
import easybraindev.quickfilegrabber.domain.ClipboardManager;
import easybraindev.quickfilegrabber.domain.FileFormatter;
import easybraindev.quickfilegrabber.domain.FileManager;
import easybraindev.quickfilegrabber.domain.FileSelectionHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;

import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBScrollPane;
import easybraindev.quickfilegrabber.usecases.CopyFilesContentToClipboardUseCase;

public class QuickFileGrabberPanel extends JPanel {

    private final Project project;
    private final JBList<String> fileList;
    private final DefaultListModel<String> fileListModel;
    private final JButton copySelectedButton;
    private final JButton copyAllButton;

    private final FileManager fileManager = new FileManager();
    private final ClipboardManager clipboardManager = new ClipboardManager();
    private final FileFormatter fileFormatter = new FileFormatter();
    private final CopyFilesContentToClipboardUseCase copyFilesContentToClipboardUseCase = new CopyFilesContentToClipboardUseCase(fileManager, fileFormatter, clipboardManager);

    public QuickFileGrabberPanel(Project project) {
        this.project = project;
        this.setLayout(new BorderLayout());

        fileListModel = new DefaultListModel<>();
        fileList = new JBList<>(fileListModel);
        copySelectedButton = new JButton("Copy Selected");
        copyAllButton = new JButton("Copy All");

        copySelectedButton.addActionListener(new CopySelectedListener());
        copyAllButton.addActionListener(new CopyAllListener());

        populateFileList();
        createUiPanel();
        registerFileEditorManagerListener(project);
    }

    private void createUiPanel() {
        JScrollPane listScroller = new JBScrollPane(fileList);
        this.add(listScroller, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(copySelectedButton);
        buttonPanel.add(copyAllButton);
        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void registerFileEditorManagerListener(Project project) {
        MessageBusConnection connection = project.getMessageBus().connect();
        connection.subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, new FileSelectionHandler(fileList));
    }

    private void populateFileList() {
        Editor[] editors = fileManager.getAllEditors();
        VirtualFile currentFile = FileEditorManager.getInstance(project).getSelectedFiles()[0];

        for (Editor editor : editors) {
            VirtualFile file = fileManager.getFile(editor);
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
            List<String> selectedFilesNames = fileList.getSelectedValuesList();
            copyFilesContentToClipboardUseCase.execute(selectedFilesNames);
        }
    }

    private class CopyAllListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            List<String> allFilesNames = Collections.list(fileListModel.elements());
            copyFilesContentToClipboardUseCase.execute(allFilesNames);
        }
    }
}
