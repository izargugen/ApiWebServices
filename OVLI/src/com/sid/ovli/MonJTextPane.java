package com.sid.ovli;
import java.awt.Color;

import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;


public class MonJTextPane extends JTextPane{

	public void append(String msg, Color c)
    {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        int len = this.getDocument().getLength();
        this.setCaretPosition(len);
        this.setCharacterAttributes(aset, false);
        this.replaceSelection(msg);
    }
	
	public HighlightPainter getHighlightPainter(){
		Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.orange);
		return painter;
	}
}
