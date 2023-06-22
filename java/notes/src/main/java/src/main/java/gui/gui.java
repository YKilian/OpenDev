package src.main.java.gui;

import javax.swing.*;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.*;
import java.io.File;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.ArrayDeque;
import java.util.Deque;

import src.main.java.jsonManipulator.Reader;
import src.main.java.jsonManipulator.Writer;
import src.main.java.utilz.BorderedListCellRenderer;
import src.main.java.utilz.Export;


public class gui{
    private static String filename = "/res/notes.json";
    private static Reader reader;
    private static Writer writer;

    private static JPanel headPanel;
    private static JLabel label;
    private static JMenuBar menuBar;
    private static JMenu fileMenu;
    private static JMenuItem saveItem;
    private static JMenuItem deleteItem;
    private static JMenuItem exportTXTItem;
    private static JMenuItem exitItem;
    private static JScrollPane scrollbar;
    private static JScrollPane scrolltext;
    private static JTextField titleField;
    private static JTextArea noteArea;
    private static JPanel editorPanel;
    private static JFrame mainFrame;
    private static JList<String> list;

    private static String[] oldVal = {"","",""};

    private static Deque<String> undoStack;
    private static int maxSize = 20;

    public static Color accentColor = new Color(250, 99, 61, 255);
    private static Color header = new Color(228, 232, 237, 255);
    private static Color background = new Color(245, 246, 247, 255);

    public static void main(String[] args) {
        File currentFile = new File(gui.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        File parentDir = currentFile.getParentFile().getParentFile();
        String parentPath = parentDir.getAbsolutePath().replace(File.separator, "/");
        filename = parentPath + filename;
        
        new gui().run();
    }

    public void run(){
        reader = new Reader(filename);
        writer = new Writer(filename);
        
        writer.updateNotes();

        String[] elements = reader.getAllElements();

        list = new JList<>(elements);
        list.setCellRenderer(new BorderedListCellRenderer());
        list.addListSelectionListener(new MyListSelectionListener());

        buildUI();
        System.out.println("JNotes V.0.1");

        undoStack = new ArrayDeque<>();

        addKeyListenerToComponents(mainFrame.getContentPane());
        addMenuListenerToComponents();
    }

    private void addKeyListenerToComponents(Container container) {
        Component[] components = container.getComponents();
        for (Component component : components) {
            // Add KeyListener to component
            component.addKeyListener(new KeyCheck(this));

            // If the component is a container, recursively traverse its components
            if (component instanceof Container) {
                addKeyListenerToComponents((Container) component);
            }
        }
        
    }

    private void addMenuListenerToComponents(){
        saveItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                save();
            }
        });

        deleteItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                delete();
            }
        });

        exportTXTItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                export();
            }
        });

        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    public static void actionHandler(String action){
        switch(action){
            case "Save":
                save();
                break;
            case "Delete":
                delete();
                break;
            case "Undo":
                undo();
                break;
            case "Export":
                export();
                break;
            case "Exit":
                System.exit(0);
                break;
            case "SaveStatus":
                saveStatus();
                break;
            default:
                System.out.println("No action found");
        }
    }


    private static void save(){
        String title = titleField.getText();
        String note = noteArea.getText();
        writer.writeToJson(title, note, oldVal);
        updateList();
        clear();
        saveStatus();
    }

    private static void saveStatus(){
       if (undoStack.size() >= maxSize) {
            undoStack.removeFirst();
        }
        undoStack.addLast(noteArea.getText());
    }

    private static void delete(){
        writer.deleteFromJson(oldVal);
        updateList();
        clear();
    }

    private static void undo(){
        if(undoStack.size() > 1){
            undoStack.removeLast();
        }
        noteArea.setText(undoStack.getLast());
    }

    private static  void export(){
        Export.exportAsTXT(oldVal[0], oldVal[1]);
    }


    private static  void updateList() {
        String[] elements = reader.getAllElements();

        DefaultListModel<String> model = new DefaultListModel<>();
        for (String element : elements) {
            model.addElement(element);
        }

        list.setModel(model); // Update the model of the existing JList

        list.setCellRenderer(new BorderedListCellRenderer());
        list.addListSelectionListener(new MyListSelectionListener());

        scrollbar.setViewportView(list);
    }

    public static void clear(){
        gui.setValues("", "", "");
    }


    public static void setValues(String title, String note, String timestamp){
        titleField.setText(title);
        noteArea.setText(note);
        gui.oldVal[0] = title;
        gui.oldVal[1] = note;
        gui.oldVal[2] = timestamp;

        if(title != ""){
            mainFrame.setTitle("JNotes - " + title);
        }
        else{
            mainFrame.setTitle("JNotes");
        }

        noteArea.setCaretPosition(0); // set cursor to the beginning of the text
        undoStack.clear();
        actionHandler("SaveStatus");
    }

    private static void buildUI(){
        menuBar = new JMenuBar();
        menuBar.setBounds(150, 25, 1100, 25);

        fileMenu = new JMenu("File");
        fileMenu.setBounds(200, 25, 100, 25);

        saveItem = new JMenuItem("Save");
        deleteItem = new JMenuItem("Delete");
        exportTXTItem = new JMenuItem("Export as .txt");
        exitItem = new JMenuItem("Exit");

        fileMenu.add(saveItem);
        fileMenu.add(deleteItem);
        fileMenu.addSeparator();
        fileMenu.add(exportTXTItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        menuBar.add(fileMenu);

        label = new JLabel("JNotes");
        label.setBounds(10, 0, 160, 50);
        label.setForeground(accentColor);
        label.setFont(new Font("Courier", Font.BOLD, 30));

        headPanel = new JPanel();
        headPanel.setLayout(null);
        headPanel.setBounds(0, 0, 1250, 50);
        headPanel.setBackground(header);
        headPanel.add(label);
        headPanel.add(menuBar);

        scrollbar = new JScrollPane(list);
        scrollbar.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollbar.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollbar.setBounds(10, 60, 270, 540);
        scrollbar.setViewportView(list);

        titleField = new JTextField(30);
        titleField.setBounds(300, 60, 400, 25);

        noteArea = new JTextArea();
        noteArea.setBounds(300, 100, 880, 500);

        scrolltext = new JScrollPane(noteArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrolltext.setBounds(300, 100, 900, 500);

        editorPanel = new JPanel();
        editorPanel.setLayout(null);
        editorPanel.add(headPanel);
        editorPanel.add(scrollbar);
        editorPanel.add(scrolltext);
        editorPanel.add(titleField);
        editorPanel.setBackground(background);
        editorPanel.setPreferredSize(new Dimension(1230, 630));

        mainFrame = new JFrame("JNotes");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.add(editorPanel);
        mainFrame.setVisible(true);
        mainFrame.setSize(new Dimension(1230, 630));
        mainFrame.setResizable(false);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.pack();
    }

}

class MyListSelectionListener implements ListSelectionListener {
    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            JList<String> source = (JList<String>) e.getSource();

            // Überprüfe, ob eine Auswahl getroffen wurde
            if (!source.isSelectionEmpty()) {
                // Rufe die gewünschte Funktion auf
                String selectedValue = source.getSelectedValue();
                if(selectedValue != ""){
                    if(selectedValue.equals("<html> <br> + Add new note <br> <br>")){
                        gui.clear();
                    }else{
                        int meta_start = selectedValue.indexOf("<--Meta-->")+11;
                        String meta = selectedValue.substring(meta_start);
                        int title_start = meta.indexOf("<--Title-->")+12;
                        int title_end = meta.indexOf("<--Timestamp-->")-1;
                        int timestamp_start = meta.indexOf("<--Timestamp-->")+16;
                        int timestamp_end = meta.indexOf("<--Note-->")-1;
                        int note_start = meta.indexOf("<--Note-->")+11;
                        String title = meta.substring(title_start, title_end);
                        String note = meta.substring(note_start);
                        String timestamp = meta.substring(timestamp_start, timestamp_end);
                        gui.setValues(title, note, timestamp);
                    }
                }
            }
        }
    }
}
