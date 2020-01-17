/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ecc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import processing.app.ui.Editor;

/**
 *
 * @author dahjon
 */
public class FunctionMacro extends Macros {

    public FunctionMacro() {

        super("(void|int|float|double|long|String|char|byte|PVector)\\s+[0-9a-zA-ZåäöÅÄÖ]+\\(", "){\n   \n}\n", 3);
//        super("void [0-9a-zA-ZåäöÅÄÖ]+\\(", "){\n   \n}\n", 3);
    }

    public boolean stringIsThisMacro(Editor editor, String sstr) {
        String etxt = editor.getText();
        etxt = etxt.substring(0, editor.getCaretOffset());
        int previousLineBreak = etxt.lastIndexOf('\n');
        int nextLineBreak = editor.getText().indexOf('\n', editor.getCaretOffset());
        if (nextLineBreak == -1) {
            nextLineBreak = editor.getText().length();
        }
        if (caretIsOnLastNonspace(nextLineBreak, editor, etxt)
                //&& etxt.trim().charAt(etxt.length() - 1) != '{'
                ) {
            String row = etxt.substring(previousLineBreak + 1);
            Pattern p = Pattern.compile(key);
            Matcher m = p.matcher(new StringBuilder(row));
            return m.find();

        } else {
            return false;
        }
    }

    @Override
    public void insert(Editor editor, int indent) {
        char caretChar = editor.getText().charAt(editor.getCaretOffset()-1);
        //System.out.println("caretChar = " + caretChar);
        if (caretChar == ')') {
            code = code.substring(1);
//            super.insert(editor, indent);
            super.insert(editor, 0);
            code = ")" + code;
        } else {
            super.insert(editor, 0);
//            super.insert(editor, indent);
        }
    }

    public static boolean caretIsOnLastNonspace(int nextLineBreak, Editor editor, String etxt) {
        int caretOffset = editor.getCaretOffset() - 1;
        nextLineBreak--;
        //System.out.println("caretOffset = " + caretOffset);
        while (nextLineBreak >= 0 && editor.getText().charAt(nextLineBreak) == ' ') {
            //System.out.println("nextLineBreak = " + nextLineBreak);
            nextLineBreak--;
        }
        return nextLineBreak == caretOffset;
    }
}
