package easybraindev.quickfilegrabber.usecases;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.vfs.VirtualFile;
import easybraindev.quickfilegrabber.domain.ClipboardManager;
import easybraindev.quickfilegrabber.domain.FileFormatter;
import easybraindev.quickfilegrabber.domain.FileManager;

import java.util.List;

public class CopyFilesContentToClipboardUseCase {

    private final FileManager fileManager;
    private final FileFormatter fileFormatter;
    private final ClipboardManager clipboardManager;

    public CopyFilesContentToClipboardUseCase(FileManager fileManager, FileFormatter fileFormatter, ClipboardManager clipboardManager) {
        this.fileManager = fileManager;
        this.fileFormatter = fileFormatter;
        this.clipboardManager = clipboardManager;
    }

    public void execute(List<String> fileNames) {
        StringBuilder filesContent = new StringBuilder();

        for (String fileName : fileNames) {
            VirtualFile file = fileManager.findFileByName(fileName);
            if (file != null) {
                Document document = FileDocumentManager.getInstance().getDocument(file);
                if (document != null) {
                    String fileContent = document.getText();
                    filesContent.append(fileFormatter.formatFileContent(fileName, fileContent)).append("\n\n");
                }
            }
        }

        clipboardManager.copyToClipboard(filesContent.toString());
    }
}
