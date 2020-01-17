/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ecc;

import processing.app.ui.Editor;

/**
 *
 * @author dahjon
 */
public class ReplaceMacros extends Macros {

    String imp = "";

    public ReplaceMacros(String key, String code, int carBack) {
        super(key, code, carBack);
    }

    public ReplaceMacros(String key, String code, int carBack, String imp) {
        super(key, code, carBack);
        this.imp = imp;
    }

    @Override
    public void insert(Editor editor, int indent) {
        String indentStr = new String(new char[indent]).replace('\0', ' ');
        String str = code.replaceAll("\n", "\n" + indentStr);
        String etxt = editor.getText();
        int cur = editor.getCaretOffset();
        etxt = etxt.substring(0, cur - key.length()) + str + etxt.substring(cur);
        editor.setText(etxt);
        int implen=0;
        if (imp.length() > 0) {
            if (editor.getText().indexOf(imp) < 0) {
                editor.setText(imp + etxt);
                implen=imp.length();
            }
        }
        int carPos = cur + str.length() + implen - key.length() - carBack;
        editor.getTextArea().setCaretPosition(carPos);

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
