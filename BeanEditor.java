//Author: Dongming Ma
package reflection_api_automated_form;

import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import static reflection_api_automated_form.Reflection_API_Automated_Form.getAttributes;

public class BeanEditor extends JFrame {

    protected Integer numOfContributes;
    static HashMap<String, Method> Attributes;
    Font f = new Font("Times", Font.PLAIN, 40);
    static Object obj;

    public JPanel createAttribute(String AttributeName) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String valueString = "";
        Integer valueInteger = 0;
        Boolean valueBoolean = false;
        JTextField t = new JTextField("");
        Method m = Attributes.get("get" + AttributeName);
        if (m.getGenericReturnType() == String.class) {
            valueString = (String) m.invoke(obj, null);
            t.setText(valueString);
        } else if (m.getGenericReturnType() == Boolean.class) {

            valueBoolean = (Boolean) m.invoke(obj, null);
            t.setText(valueBoolean.toString());
        } else {
            valueInteger = (Integer) m.invoke(obj, null);
            t.setText(valueInteger.toString());

        }

//待解决：卡片尺寸自适应，自动加入卡片
        JPanel p = new JPanel();
        JButton b = new JButton("Enter");
        JLabel l = new JLabel(AttributeName);

        b.setFont(f);
        l.setFont(f);
        t.setFont(f);
        this.setPreferredSize(new Dimension(720, 200));
        p.setLayout(new GridLayout(1, 3, 20, 20));
        p.add(l);
        p.add(t);
        p.add(b);
        ActionListener submit = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newValueString = "";
                Integer newValueInteger = 0;
                Boolean newValueBoolean = false;
                Method m = Attributes.get("set" + AttributeName);
                if (Boolean.valueOf(t.getText())) {
                    try {
                        newValueBoolean = Boolean.valueOf(t.getText());
                        m.invoke(obj, newValueBoolean);
                        System.out.println("Object's " + AttributeName + " has been modified to " + newValueBoolean);
                    } catch (IllegalAccessException ex) {
                        Logger.getLogger(BeanEditor.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IllegalArgumentException ex) {
                        Logger.getLogger(BeanEditor.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InvocationTargetException ex) {
                        Logger.getLogger(BeanEditor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if (t.getText().matches(".*\\d+.*")) {
                    try {
                        newValueInteger = Integer.valueOf(t.getText());
                        m.invoke(obj, newValueInteger);
                        System.out.println("Object's " + AttributeName + " has been modified to " + newValueInteger);
                    } catch (IllegalAccessException ex) {
                        Logger.getLogger(BeanEditor.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IllegalArgumentException ex) {
                        Logger.getLogger(BeanEditor.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InvocationTargetException ex) {
                        Logger.getLogger(BeanEditor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    try {
                        newValueString = t.getText();
                        m.invoke(obj, newValueString);
                        System.out.println("Object's " + AttributeName + " has been modified to " + newValueString);
                    } catch (IllegalAccessException ex) {
                        Logger.getLogger(BeanEditor.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IllegalArgumentException ex) {
                        Logger.getLogger(BeanEditor.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InvocationTargetException ex) {
                        Logger.getLogger(BeanEditor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        };
        b.addActionListener(submit);
        t.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();

                if (key == KeyEvent.VK_ENTER) {
                    String newValueString = "";
                    Integer newValueInteger = 0;
                    Boolean newValueBoolean = false;
                    Method m = Attributes.get("set" + AttributeName);
                    if (Boolean.valueOf(t.getText())) {
                        try {
                            newValueBoolean = Boolean.valueOf(t.getText());
                            m.invoke(obj, newValueBoolean);
                            System.out.println("Object's " + AttributeName + " has been modified to " + newValueBoolean);
                        } catch (IllegalAccessException ex) {
                            Logger.getLogger(BeanEditor.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IllegalArgumentException ex) {
                            Logger.getLogger(BeanEditor.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (InvocationTargetException ex) {
                            Logger.getLogger(BeanEditor.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else if (t.getText().matches(".*\\d+.*")) {
                        try {
                            newValueInteger = Integer.valueOf(t.getText());
                            m.invoke(obj, newValueInteger);
                            System.out.println("Object's " + AttributeName + " has been modified to " + newValueInteger);
                        } catch (IllegalAccessException ex) {
                            Logger.getLogger(BeanEditor.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IllegalArgumentException ex) {
                            Logger.getLogger(BeanEditor.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (InvocationTargetException ex) {
                            Logger.getLogger(BeanEditor.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        try {
                            newValueString = t.getText();
                            m.invoke(obj, newValueString);
                            System.out.println("Object's " + AttributeName + " has been modified to " + newValueString);
                        } catch (IllegalAccessException ex) {
                            Logger.getLogger(BeanEditor.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IllegalArgumentException ex) {
                            Logger.getLogger(BeanEditor.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (InvocationTargetException ex) {
                            Logger.getLogger(BeanEditor.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        });
        return p;
    }

    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            System.out.println("enter pressed");
        }

    }

    public JPanel createBottomPanel() {
        JPanel p = new JPanel();
        JButton SubmitAllChanges = new JButton("SubmitAllChanges");
        SubmitAllChanges.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("too late to implement this with my trush code");
            }
        });
        JButton PrintOutObject = new JButton("PrintOutObject");
        PrintOutObject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(obj);
            }
        });
        p.setLayout(new GridLayout(1, 2, 20, 20));
        p.add(SubmitAllChanges);
        p.add(PrintOutObject);
        SubmitAllChanges.setFont(new Font("Times", Font.PLAIN, 30));
        PrintOutObject.setFont(new Font("Times", Font.PLAIN, 30));
        return p;
    }

    public JPanel createInfoDisplayPanel() {
        JPanel p = new JPanel();
        String[] name = obj.getClass().getName().split("\\.");
        Integer num = Attributes.size() / 2;
        p.setLayout(new GridLayout(1, 2, 20, 20));
        JLabel nameOfObject = new JLabel("Name: " + name[1]);
        JLabel numOfAttributes = new JLabel("NumOfAttributes: " + num.toString());
        nameOfObject.setFont(new Font("Times", Font.PLAIN, 30));
        numOfAttributes.setFont(new Font("Times", Font.PLAIN, 30));

        p.add(nameOfObject);
        p.add(numOfAttributes);

        return p;
    }

    public BeanEditor(Object obj) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        super("Bean Editor Mark.I");
        numOfContributes = Attributes.size() / 2;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(720, 1080);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();
        setLocation(((int) width - 720) / 2, ((int) height - 1080) / 2);
        //-----------Contents---------------
        Container c = getContentPane();
        JPanel AttributeListPanel = new JPanel();
        AttributeListPanel.setLayout(new GridLayout(numOfContributes, 1, 20, 20));
        c.add(AttributeListPanel, BorderLayout.CENTER);
        JPanel InfoDispalyPanel = createInfoDisplayPanel();
        c.add(InfoDispalyPanel, BorderLayout.NORTH);

        JPanel BottomPanel = createBottomPanel();
        c.add(BottomPanel, BorderLayout.SOUTH);
        //--------------insert Attributes list---------------
        Set<String> namesOfAttributes = new HashSet<String>();
        for (Map.Entry<String, Method> entry : Attributes.entrySet()) {
            namesOfAttributes.add(entry.getKey().substring(3));
        }
        for (String names : namesOfAttributes) {
            JPanel p = createAttribute(names);
            AttributeListPanel.add(p);
        }
        System.out.println(namesOfAttributes);

        setResizable(false);
        setVisible(true);
    }

    public static HashMap getAttributes(Class c) throws NoSuchMethodException {
        Method[] methods = c.getMethods();
        HashMap<String, Method> Attributes = new HashMap<>();
        for (int i = 0; i < methods.length; i++) {
            String name = methods[i].getName();
            Class type = methods[i].getReturnType();
            if ((name.startsWith("get") && !name.contains("getClass")) || name.startsWith("set")) {
                Attributes.put(name, methods[i]);
            }
        }
        return Attributes;
    }

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {

        Class c = Class.forName("reflection_api_automated_form.JavaBean1");
        JavaBean1 jb1 = new JavaBean1("Jack", 24, "Driver", false);
        obj = jb1;
        System.out.println(obj);

        Attributes = getAttributes(c);
        new BeanEditor(new JavaBean1());

    }
}
