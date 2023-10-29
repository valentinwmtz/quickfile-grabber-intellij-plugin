package easybraindev.quickfilegrabber.domain;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.vfs.VirtualFile;

public class FileManager {

    public Editor[] getAllEditors() {
        return EditorFactory.getInstance().getAllEditors();
    }

    public VirtualFile getFile(Editor editor) {
        return FileDocumentManager.getInstance().getFile(editor.getDocument());
    }

    public VirtualFile findFileByName(String fileName) {
        for (Editor editor : getAllEditors()) {
            VirtualFile file = getFile(editor);
            if (file != null && fileName.equals(file.getName())) {
                return file;
            }
        }
        return null;
    }
}
