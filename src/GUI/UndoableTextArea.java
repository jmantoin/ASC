
package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

class UndoableTextArea extends JTextArea implements UndoableEditListener, FocusListener,
  KeyListener {

	private UndoManager m_undoManager;

	public UndoableTextArea() {
		this(new String());
	}

	public UndoableTextArea(String text) {
		super(text);
		getDocument().addUndoableEditListener(this);
		this.addKeyListener(this);
		this.addFocusListener(this);
		createUndoMananger();

	}

	public void createUndoMananger() {
		m_undoManager = new UndoManager();
		m_undoManager.setLimit(100);
	}

	public void removeUndoMananger() {
	m_undoManager.end();
	}

	public void focusGained(FocusEvent fe) {
	}

	public void focusLost(FocusEvent fe) {
	}

	public void undoableEditHappened(UndoableEditEvent e) {
		m_undoManager.addEdit(e.getEdit());
	}

	public void keyPressed(KeyEvent e) {
	}

	public void undoLast() {
		try {
			m_undoManager.undo();
		} catch (CannotUndoException cue) {
			Toolkit.getDefaultToolkit().beep();
		}
	}

	public void redoLast() {
		try {
			m_undoManager.redo();
		} catch (CannotRedoException cue) {
			Toolkit.getDefaultToolkit().beep();
		}
	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}
}
