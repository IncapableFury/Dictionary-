/**
 * Author : Dongming Ma
 * TextEditor made by bugs and rage from a useless programmer.
 */
package hw3;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import javax.tools.*;

public class TextEditor extends JFrame {

    static public File currentFile = null;
    public boolean fileEdited = false;

    public JTextArea t, console;
    public JMenu fileMenu, buildMenu, toolsMenu;
    public JDialog closingwindow;

    public Font f = new Font("Times", Font.PLAIN, 24);
    public Font fs = new Font("Times", Font.PLAIN, 20);

    public ArrayList<Integer> errorLineNumbers = new ArrayList<>(5);
    public int CurrentError;

    public void createFileMenu() {
        fileMenu = new JMenu("File(F)");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        fileMenu.setFont(f);

        JMenuItem newfile = new JMenuItem("New");
        newfile.setFont(fs);
        newfile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        newfile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CurrentError = 0;
                new TextEditor();
            }
        });

        JMenuItem open = new JMenuItem("Open");
        open.setFont(fs);
        open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CurrentError = 0;
                if (!TextEditor.this.fileEdited) {
                    TextEditor.this.openFile();
                } else {
                    // TODO choose save file or not first if file edited
                    TextEditor.this.openFile();
                }
            }
        });

        JMenuItem save = new JMenuItem("Save");
        save.setFont(fs);
        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));

        save.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                CurrentError = 0;
                try {
                    TextEditor.this.saveFile();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        JMenuItem quit = new JMenuItem("Quit");
        quit.setFont(fs);
        quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (TextEditor.this.fileEdited) {
                    TextEditor.this.closingWindow();//需要加会话框
                } else {
                    // TODO choose save file or not first if file edited
                    dispose();
                }
            }
        });

        fileMenu.add(newfile);
        fileMenu.add(open);
        fileMenu.add(save);
        fileMenu.add(quit);
    }

    public void createToolsMenu() {
        toolsMenu = new JMenu("Tools(T)");
        toolsMenu.setMnemonic(KeyEvent.VK_T);
        toolsMenu.setFont(f);

        JMenuItem gotoNextError = new JMenuItem("Goto next error");
        gotoNextError.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0));
        gotoNextError.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    t.setCaretPosition(t.getLineStartOffset(errorLineNumbers.get(CurrentError) - 1));
                } catch (BadLocationException ex) {
                    Logger.getLogger(TextEditor.class.getName()).log(Level.SEVERE, null, ex);
                }
//                System.out.println(errorLineNumbers.get(0));
                CurrentError += 1;
                if (CurrentError == errorLineNumbers.size()) {
                    CurrentError = 0;
                }
            }
        });
        gotoNextError.setFont(fs);
        toolsMenu.add(gotoNextError);
    }

    public void createBuildMenu() {
        buildMenu = new JMenu("Build(B)");
        buildMenu.setMnemonic(KeyEvent.VK_B);
        buildMenu.setFont(f);

        JMenuItem run = new JMenuItem("Run");
        run.setFont(fs);
        run.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CurrentError = 0;
                try {
                    runFile();
                } catch (IOException ex) {
                    Logger.getLogger(TextEditor.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(TextEditor.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });
        JMenuItem compile = new JMenuItem("Compile");
        compile.setFont(fs);
        compile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CurrentError = 0;
                try {
                    compileFile();
                } catch (IOException ex) {
                    Logger.getLogger(TextEditor.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(TextEditor.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });
        buildMenu.add(compile);
        buildMenu.add(run);
    }

    public void createWindow() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                System.out.println("Stop debuging me.");
            }

            public void windowClosing(WindowEvent e) {
                if (fileEdited != false) {
                    closingWindow();
                } else {
                    dispose();
                }
            }
        });
        setSize(1080, 720);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();
        setLocation(((int) width - 1000) / 2, ((int) height - 1100) / 2);

        Container c = getContentPane();
        JMenuBar menubar = new JMenuBar();

        this.createFileMenu();
        this.createBuildMenu();
        this.createToolsMenu();

        t = new JTextArea();
        t.getDocument().addDocumentListener(new MyDocumentListener());
        t.setFont(f);
        t.addKeyListener(new MyKeyListener());

//        console = new JTextArea(5,20);
//        console.setFont(f);
        c.add(t, BorderLayout.CENTER);
//        c.add(console, BorderLayout.SOUTH);
        c.add(menubar, BorderLayout.NORTH);

        menubar.add(fileMenu);
        menubar.add(buildMenu);
        menubar.add(toolsMenu);
    }

    public TextEditor() {
        super("ultimateSimpleTextEditor(Build 0.1.0)");
        createWindow();
        setVisible(true);
    }

    public void gotoNextError(long linenumber) {

        t.setCaretPosition((int) linenumber);
    }

    public void openFile() {
        JFileChooser openFile = new JFileChooser();
        int returnVal = openFile.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = openFile.getSelectedFile();
            TextEditor.this.currentFile = file;
            try {
                Scanner sc = new Scanner(file);
                String str = "";
                while (sc.hasNextLine()) {
                    str += (sc.nextLine() + '\n');
                }
                TextEditor.this.t.setText(str);
                sc.close();
            } catch (FileNotFoundException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        } else if (returnVal == JFileChooser.CANCEL_OPTION) {
            // TODO cancel clicked
            ;
        } else {
            // TODO error occurred
            ;
        }
        System.out.println("You opened a file at " + currentFile.getAbsolutePath() + " called " + currentFile.getName());
    }

    public void saveFile() throws IOException {
        if (this.fileEdited && this.currentFile != null) {
            // just save
            this.writeFile(this.currentFile);
            fileEdited = false;
        } else if (this.fileEdited && this.currentFile == null) {
            // open save dialog, get path and save
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            chooser.showSaveDialog(null);
            File fileToSave = chooser.getSelectedFile();
            this.writeFile(fileToSave);
            fileEdited = false;
            this.currentFile = fileToSave;
        }

    }

//    public void compileFile() throws IOException, InterruptedException {
//        String path = currentFile.getAbsolutePath();
//        Process pro = Runtime.getRuntime().exec("javac " + path);
//        printLines("out:", pro.getInputStream());
//        printLines("err:", pro.getErrorStream());
//        pro.waitFor();
//    }
    public void compileFile() throws IOException, InterruptedException {
        errorLineNumbers.clear();
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics
                = new DiagnosticCollector<JavaFileObject>();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);
        String path = currentFile.getAbsolutePath();

//        int compilationResult = compiler.run(null, null, null, path);
        compiler.getTask(null, fileManager, diagnostics, null, null, fileManager.getJavaFileObjects(path)).call();
        for (Diagnostic diagnostic : diagnostics.getDiagnostics()) {
            System.out.format("Error on line %d in %s%n",
                    diagnostic.getLineNumber(),
                    diagnostic.getSource());
            errorLineNumbers.add((int) diagnostic.getLineNumber());
//            errorLineNumbers.add(diagnostic.getStartPosition());
        }

        fileManager.close();

    }

    private static void printLines(String str, InputStream input) throws IOException {
        String line = null;
        BufferedReader in = new BufferedReader(new InputStreamReader(input));
        while ((line = in.readLine()) != null) {
            System.out.println(str + " " + line);
        }

    }

    public void runFile() throws IOException, InterruptedException {
        String pathSplit[] = currentFile.getAbsolutePath().split("\\\\");
        String path = "";
        for (int i = 0; i < pathSplit.length - 1; i++) {
            path += pathSplit[i] + "\\";

        }
        String[] fileName = currentFile.getName().split("\\.");

        Process pro = Runtime.getRuntime().exec("java -cp " + path + " " + fileName[0]);
        printLines("out:", pro.getInputStream());
        printLines("err:", pro.getErrorStream());
        pro.waitFor();
    }

    public void writeFile(File file) throws IOException {
        String text = this.t.getText();
        // make sure \n in text replaced by the 'true line separator'
        BufferedReader reader = new BufferedReader(new StringReader(text));
        PrintWriter writer = new PrintWriter(new FileWriter(file));
        String line;
        while ((line = reader.readLine()) != null) {
            writer.println(line);
        }
        writer.close();
    }

    public void closingWindow() {
        JDialog askForSave = new JDialog(getOwner(), "Remember to SAVE!");
//        Point p = new Point(333, 333);
//        askForSave.setLocation(p);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();
        askForSave.setLocation(((int) width - 200) / 2, ((int) height - 300) / 2);

        askForSave.setFont(f);
        askForSave.setSize(400, 300);
        askForSave.setVisible(true);
//        Container c = getContentPane();

//        c.setLayout(new BorderLayout());
//        JPanel p = new JPanel();
//        p.setVisible(true);
//        p.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        askForSave.setLayout(new GridLayout(2, 1, 20, 20));
//        c.add(p, BorderLayout.CENTER);
//        p.setBackground(Color.WHITE);
        JButton yes = new JButton("Oh thank you");
        yes.setFont(f);
        yes.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    TextEditor.this.saveFile();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });
        JButton no = new JButton("Nope");
        no.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                askForSave.dispose();
            }
        });
        no.setFont(f);
        askForSave.add(yes);
        askForSave.add(no);

    }

    public void cut() {

    }

    public void paste() {

    }

    public static void main(String[] args) {
        new TextEditor();
    }

    // HashMap<String, Command>
    abstract class Command {

    }

    class CompileCommand extends Command {

        public void doit() {

        }
    }

    class MyKeyListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
//            System.out.print(e.getKeyChar());
        }

        @Override
        public void keyPressed(KeyEvent e) {
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }

    }

    class MyDocumentListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            // TODO Auto-generated method stub
            if (!TextEditor.this.fileEdited) {
//                System.out.println("file edited");
                TextEditor.this.fileEdited = true;
            }
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            // TODO Auto-generated method stub
            if (!TextEditor.this.fileEdited) {
//                System.out.println("file edited");
                TextEditor.this.fileEdited = true;
            }
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            // TODO Plain text components do not fire these events
        }

    }
}
