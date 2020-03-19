package com.sid.ovli.handlers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.BadLocationException;

import com.sid.ovli.Ttaq;

public class BtnSearchHandler implements IHandler {

	Ttaq ttaq;
	
	public void perform(Ttaq ttaq) {

		this.ttaq = ttaq;
		
		String search = ttaq.textMsgException.getText().toLowerCase();
		boolean trouve = false;

		Pattern pattern = Pattern.compile(search);
		Matcher matcher = pattern.matcher(ttaq.textPaneResult.getText()
				.toLowerCase().replaceAll("\n", ""));

		trouve = matcher.find(ttaq.textPaneResult.getCaretPosition() + 1);
		if (trouve) {
			int start = matcher.start();
			int end = matcher.end();
			try {
				ttaq.textPaneResult.getHighlighter().addHighlight(start,
						end, ttaq.textPaneResult.getHighlightPainter());
				ttaq.textPaneResult.setCaretPosition(start);
			} catch (BadLocationException ex) {
				System.err
						.println("An error occured, please try again");
			}
		}

		/*
		 * Pour colorier tout d'un seul coup while(matcher.find()){ int
		 * start = matcher.start(); int end = matcher.end(); try {
		 * textPaneResult.getHighlighter().addHighlight(start, end,
		 * textPaneResult.getHighlightPainter());
		 * 
		 * } catch (BadLocationException ex) {
		 * System.err.println("An error occured, please try again"); }
		 * 
		 * }
		 */

		/*
		 * int offset = textPaneResult.getText().indexOf(search); int
		 * length = search.length();
		 * 
		 * while (offset != -1) { try {
		 * textPaneResult.getHighlighter().addHighlight(offset, offset +
		 * length, textPaneResult.getHighlightPainter()); offset =
		 * textPaneResult.getText().indexOf(search, offset + 1); } catch
		 * (BadLocationException ex) {
		 * System.err.println("An error occured, please try again"); } }
		 */
	}

}
