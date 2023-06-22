package src.main.java.utilz;

import javax.swing.*;
import javax.swing.border.Border;

import src.main.java.gui.gui;

import java.awt.*;

public class BorderedListCellRenderer extends DefaultListCellRenderer {
    private static final int BORDER_THICKNESS = 1;
    private static final Color BORDER_COLOR = Color.GRAY;

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

         // Erstelle eine sichtbare Border mit definierter Dicke und Farbe
         Border border = BorderFactory.createLineBorder(BORDER_COLOR, BORDER_THICKNESS);

        // Setze den Border für den Renderer
        if (renderer instanceof JComponent) {
            ((JComponent) renderer).setBorder(border);
        }

        // Setze die Border für den Renderer
        if (renderer instanceof JComponent) {
            ((JComponent) renderer).setBorder(border);
        }

        if (index == 0) {
            renderer.setBackground(gui.accentColor);
        }

        return renderer;
    }
}
