package ecc;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import processing.app.Base;
import processing.app.tools.Tool;
import processing.app.ui.Editor;

public class ExtendedCodeCompletion implements Tool, KeyListener {

    Base base;
    Editor editor;
    // In Processing 3, the "Base" object is passed instead of an "Editor"

    public void init(Base base) {
        // Store a reference to the Processing application itself
        this.base = base;
    }

    public void run() {
        // Run this Tool on the currently active Editor window
        System.out.println("Extended Code Completion is running.");
        editor = base.getActiveEditor();
        editor.getTextArea().addKeyListener(this);

    }

    public String getMenuTitle() {
        return "Extended Code Completion";
    }

    @Override
    public void keyPressed(KeyEvent ke) {

        if (ke.getKeyCode() == KeyEvent.VK_SPACE && ke.getModifiers() == InputEvent.CTRL_MASK) {
            ke.consume();
            //System.out.println("keyPressed ctrl+space");
            String txt = getTextBeforeCaret();
            //System.out.println("txt = '" + txt + "'");
            int indent = getSpacesBeforeText(txt.length());
            //System.out.println("indent = " + indent);
            Macros m = Macros.find(editor, txt);
            if (m != null) {
                m.insert(editor, indent);
            }
        }
    }

    private int getSpacesBeforeText(int textLen) {
        int start = editor.getCaretOffset() - 1 - textLen;
        int i = start;
        String edtext = editor.getText();
        if (start >= 0) {
            char c = edtext.charAt(start);
            while (c == ' ' && i >= 0) {
                //System.out.println("i = " + i+" c = " + c);
                i--;
                if (i >= 0) {
                    c = editor.getText().charAt(i);
                }
            }
        }

        return start - i;
    }

    private String getTextBeforeCaret() {
        int start = editor.getCaretOffset() - 1;
        if (start >= 0) {
            //System.out.println("start = " + start);
            int i = start;
            String edtext = editor.getText();
            //System.out.println("edtext = " + edtext);
            //System.out.println("edtext.length() = " + edtext.length());
            char c = edtext.charAt(start);
            while (Character.isLetter(c) && i >= 0) {
                i--;
                if (i >= 0) {
                    c = editor.getText().charAt(i);
                }
            }
            i++;
            return editor.getText().substring(i, start + 1);
        } else {
            return "";
        }
    }

    @Override
    public void keyTyped(KeyEvent ke
    ) {
//        if (ke.getKeyCode() == KeyEvent.VK_TAB) {
//            System.out.println("keyTyped ...Tab tryckt!!! consume");
//            ke.consume();
//        }

    }

    @Override
    public void keyReleased(KeyEvent ke
    ) {
//        if (ke.getKeyCode() == KeyEvent.VK_TAB) {
//            System.out.println("keyReleased ...Tab tryckt!!! consume");
//            ke.consume();
//        }        
    }
}
