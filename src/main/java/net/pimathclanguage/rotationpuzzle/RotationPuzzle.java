package net.pimathclanguage.rotationpuzzle;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import com.google.common.collect.Lists;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Delegate;
import net.pimathclanguage.rotationpuzzle.Tuple.Tuple2;
import net.pimathclanguage.rotationpuzzle.Tuple.Tuple3;
import net.pimathclanguage.rotationpuzzle.Tuple.Tuple4;
import net.pimathclanguage.rotationpuzzle.FunctionInterfaces.Function2;

public class RotationPuzzle extends JFrame implements KeyListener {
  // Menubar with Menus and MenuItems
  private JMenuBar menuBar;
  // Main
  private JMenu menuMain;
  private JMenuItem menuitemNew;
  private JMenuItem menuitemMix;
  private JMenuItem menuitemSolve;
  private JMenuItem menuitemExit;
  // Size
  private JMenu menuSize;
  // Size of Field
  private JMenu menuSizeField;
  private JMenuItem[] menuitemSizeField;
  private int sizeFieldChosen;
  // Size of Buttons
  private JMenu menuSizeButton;
  private JMenuItem[] menuitemSizeButton;
  private int sizeButtonChosen;
  // Settings
  private JMenu menuSettings;
  private JCheckBoxMenuItem checkboxmenuitemAutoCenter;
  private JCheckBoxMenuItem checkboxmenuitemShowColor;
  private JMenuItem menuitemChangeKeys;
  // Help
  private JMenu menuHelp;
  private JMenuItem menuitemIntro;
  private JMenuItem menuitemInfo;

  // Field Variables
  private JButton[][] fieldButton;
  private int[][] fieldInt;
  // Coordiantes of the select Field
  private int selectx;
  private int selecty;
  private boolean isSelectActive;
  // Values for additional features of rotation
  private boolean isRotationAntiClockwise;
  private boolean isRotation1;
  private boolean isRotation2;
  // Variables for the Button Field
  private int bfW;
  private int bfH;
  private int bfL;
  private int bfT;
  private int bfSpace;
  // Size of the Field
  private int fieldSizeX;
  private int fieldSizeY;
  // Colors
  // Standard Color of JButton
  private Color colorb0 = new JButton().getBackground();
  private Color colorf0 = new JButton().getForeground();
  // Correct position
  private Color colorb1 = new Color(0x0002554f);
  private Color colorf1 = new Color(0x003399AA);
  // False position
  private Color colorb2 = new Color(0x0002852f);
  private Color colorf2 = new Color(0x00000000);
  // Selected position
  private Color colorb3 = new Color(0x0010252f);
  private Color colorf3 = new Color(0x00FFFFFF);
  // Chosen Size
  private Color colorb4 = new Color(0x00502550);
  private Color colorf4 = new Color(0x00FF00FF);

  // Add the tracker of the rotations per position
  private List<Tuple3<Integer, Integer, Integer>> rotTracker = new ArrayList<>();

  // Constructor of this Class
  public RotationPuzzle() {
    // Default
    setVariablesDefault();
    setObjectDefault();
    // This Frame properties
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //this.setSize(364, 414);
    this.setLocationRelativeTo(null);
    this.setLayout(null);
    this.addKeyListener(this);
    this.setTitle("Rotation Puzzle - Ziko Haris");
    this.setJMenuBar(menuBar);
    this.setResizable(false);
    setWindowSize();
  }

  private void setWindowSize() {
    this.setPreferredSize(new Dimension(bfL * 2 + bfW * fieldSizeX + bfSpace * (fieldSizeX - 1),
        bfL + 60 + bfH * fieldSizeY + bfSpace * (fieldSizeY - 1)));

    this.pack();
    try {
      Thread.sleep(20);
    } catch (Exception e) {

    }
    if (checkboxmenuitemAutoCenter.getState()) {
      this.setLocationRelativeTo(null);
    }
    this.pack();
  }

  private void setVariablesDefault() {
    selectx = 1;
    selecty = 1;
    isRotationAntiClockwise = false;
    isRotation1 = false;
    isRotation2 = false;
    isSelectActive = false;
  }

  private void setObjectDefault() {
    fieldSizeX = 4;
    fieldSizeY = 4;
    bfW = 60;
    bfH = 60;
    bfL = 20;
    bfT = 20;
    bfSpace = 6;
    createButtonField();
    setButtonFieldBound();
    setMenuBar();
  }

  private void createButtonField() {
    Font newFont = new Font((new JButton()).getFont().getFontName(), Font.BOLD, 20);
    fieldInt = new int[20][20];
    fieldButton = new JButton[20][20];
    for (int j = 0; j < fieldButton.length; j++) {
      for (int i = 0; i < fieldButton[j].length; i++) {
        fieldButton[j][i] = new JButton();
        this.add(fieldButton[j][i]);
        JButton btn = fieldButton[j][i];
        btn.setSize(bfW, bfH);
        btn.setLocation(bfL + (bfW + bfSpace) * i, bfT + (bfH + bfSpace) * j);
        btn.setBackground(colorb1);
        btn.setForeground(colorf1);
        btn.setBorder(null);
        btn.setText(String.valueOf(i + fieldSizeX * j + 1));
        btn.addActionListener(new ButtonActionListener(i, j));
        btn.setFont(newFont);
        btn.setFocusable(false);
        fieldInt[j][i] = i + fieldSizeX * j + 1;
      }
    }
    for (int j = 1; j < fieldButton.length - 1; j++) {
      for (int i = 1; i < fieldButton[j].length - 1; i++) {
        JButton btn = fieldButton[j][i];
        Border outerBorder = new LineBorder(Color.RED, 3);
        btn.setBorder(outerBorder);
        btn.setBorderPainted(true);
        btn.setFocusPainted(false);
      }
    }
  }

  private void setButtonFieldBound() {
    for (int j = 0; j < fieldButton.length; j++) {
      for (int i = 0; i < fieldButton[j].length; i++) {
        JButton btn = fieldButton[j][i];
        if (i >= fieldSizeX || j >= fieldSizeY) {
          btn.setLocation(-bfW - 30, -bfH - 30);
        } else {
          btn.setLocation(bfL + (bfW + bfSpace) * i, bfT + (bfH + bfSpace) * j);
          btn.setSize(bfW, bfH);
          if (i > 0 && i < fieldSizeX - 1 && j > 0 && j < fieldSizeY - 1) {
            Border outerBorder = new LineBorder(Color.RED, 3);
            btn.setBorder(outerBorder);
          } else {
            btn.setBorder(null);
          }
          fieldInt[j][i] = i + j * fieldSizeX + 1;
        }
      }
    }
  }

  // Define the Bar with there Elements
  private void setMenuBar() {
    menuBar = new JMenuBar();
    // Main
    menuMain = new JMenu();
    menuMain.setText("Main");
    menuBar.add(menuMain);
    // New
    menuitemNew = new JMenuItem();
    menuitemNew.setText("New Game");
    menuitemNew.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        fieldNew();
      }
    });
    menuMain.add(menuitemNew);
    // Mix
    menuitemMix = new JMenuItem();
    menuitemMix.setText("Mix Field");
    menuitemMix.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        fieldMix();
      }
    });
    menuMain.add(menuitemMix);
    // Solve
    menuitemSolve = new JMenuItem();
    menuitemSolve.setText("Solve Field");
    menuitemSolve.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        fieldSolve();
      }
    });
    menuMain.add(menuitemSolve);
    // Seperator
    menuMain.add(new JSeparator());
    // Exit
    menuitemExit = new JMenuItem();
    menuitemExit.setText("Exit");
    menuitemExit.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        System.exit(0);
      }
    });
    menuMain.add(menuitemExit);
    // Size
    menuSize = new JMenu();
    menuSize.setText("Size");
    menuBar.add(menuSize);
    // Size of Field
    menuSizeField = new JMenu();
    menuSizeField.setText("Size of Field");
    menuSizeField.setFont(new Font(new JMenu().getFont().getName(), new JMenu().getFont().getStyle(), 14));
    menuSize.add(menuSizeField);
    // Size Field List
    menuitemSizeField = new JMenuItem[10];
    for (int i = 0; i < menuitemSizeField.length - 1; i++) {
      menuitemSizeField[i] = new JMenuItem();
      menuitemSizeField[i].setText(String.valueOf(i + 4) + "x" + String.valueOf(i + 4));
      menuitemSizeField[i].addActionListener(new MenuItemSizeFieldActionListener(i, i + 4, false));
      menuSizeField.add(menuitemSizeField[i]);
    }
    // Make the last one for Custom Size Field
    int iter = menuitemSizeField.length - 1;
    menuitemSizeField[iter] = new JMenuItem();
    menuitemSizeField[iter].setText("Custom");
    menuitemSizeField[iter].addActionListener(new MenuItemSizeFieldActionListener(iter, iter + 4, true));
    menuSizeField.add(menuitemSizeField[iter]);
    // The Standard Chosen one is with 5x5 Field
    sizeFieldChosen = 1;
    menuitemSizeField[sizeFieldChosen].setBackground(colorb4);
    menuitemSizeField[sizeFieldChosen].setForeground(colorf4);
    // Size of Button
    menuSizeButton = new JMenu();
    menuSizeButton.setText("Size of Button");
    menuSizeButton.setFont(new Font(new JMenu().getFont().getName(), new JMenu().getFont().getStyle(), 14));
    menuSize.add(menuSizeButton);
    // Size Button List
    menuitemSizeButton = new JMenuItem[16];
    for (int i = 0; i < menuitemSizeButton.length - 1; i++) {
      menuitemSizeButton[i] = new JMenuItem();
      menuitemSizeButton[i].setText(String.valueOf(30 + i * 5) + "px");
      menuitemSizeButton[i].addActionListener(new MenuItemSizeButtonActionListener(i, 30 + i * 5, false));
      menuSizeButton.add(menuitemSizeButton[i]);
    }
    // Make the last one for Custom Size Field
    iter = menuitemSizeButton.length - 1;
    menuitemSizeButton[iter] = new JMenuItem();
    menuitemSizeButton[iter].setText("Custom");
    menuitemSizeButton[iter].addActionListener(new MenuItemSizeButtonActionListener(iter, 30 + iter * 5, true));
    menuSizeButton.add(menuitemSizeButton[iter]);
    // The Standard Chosen one 60px height and width
    sizeButtonChosen = 6;
    menuitemSizeButton[sizeButtonChosen].setBackground(colorb4);
    menuitemSizeButton[sizeButtonChosen].setForeground(colorf4);
    // Settings
    menuSettings = new JMenu();
    menuSettings.setText("Settings");
    menuBar.add(menuSettings);
    // AutoCenter
    checkboxmenuitemAutoCenter = new JCheckBoxMenuItem();
    checkboxmenuitemAutoCenter.setText("Auto Center");
    checkboxmenuitemAutoCenter.setState(true);
    menuSettings.add(checkboxmenuitemAutoCenter);
    // Show Color
    checkboxmenuitemShowColor = new JCheckBoxMenuItem();
    checkboxmenuitemShowColor.setText("Show Color");
    checkboxmenuitemShowColor.setState(true);
    checkboxmenuitemShowColor.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        refreshFieldButton(isSelectActive);

      }
    });
    menuSettings.add(checkboxmenuitemShowColor);
    // Change Keys
    menuitemChangeKeys = createNewJMenuItem("Change Keys",
        null,
        menuSettings);
    // Help
    menuHelp = new JMenu();
    menuHelp.setText("Help");
    menuBar.add(menuHelp);
    // Intro
    menuitemIntro = new JMenuItem();
    menuitemIntro.setText("Intro");
    menuitemIntro.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        JOptionPane.showConfirmDialog(null, "How is this Game played?\n" +
            "You can play with mouse or with keyboard.\n" +
            "The middle Field with a border are clickable, the other outside are not.\n" +
            "If you want to play with keyboard, use the arrows.\n\n" +
            "Also notice that you only rotate 8 field clockwise.\n" +
            "Some more controls:\n" +
            "\"space\" ... Will rotate 1 cycle\n" +
            "\"a\" ....... Let you to rotate counter-clockwise\n" +
            "\"s\" ....... Rotate +1 more\n" +
            "\"d\" ....... Rotate +2 more\n\n" +
            "You can also combine these keys:\n" +
            "e.g. \"a\"+\"d\"+\"space\" will do 3 rotations counter-clockwise", "Rotations Puzzle - Intro", JOptionPane.DEFAULT_OPTION);
      }
    });
    menuHelp.add(menuitemIntro);
    // Info
    menuitemInfo = new JMenuItem();
    menuitemInfo.setText("Info");
    menuitemInfo.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        JOptionPane.showConfirmDialog(null,
            "Rotation Puzzle programmed by Haris Ziko\n" +
                "Last Date of changes: 2015.03.04;11:08\n" +
                "Added Features:\n" +
                "+Auto Size\n+Show Color\n" +
                "v0.2.2", "Rotations Puzzle - Info", JOptionPane.DEFAULT_OPTION);
      }
    });
    menuHelp.add(menuitemInfo);
  }

  private JMenu createNewJMenu(String text, ActionListener newActionListener, JMenuBar jMenuBar) {
    JMenu newObject = new JMenu();
    newObject.setText(text);
    newObject.addActionListener(newActionListener);
    jMenuBar.add(newObject);
    return newObject;
  }

  private JMenuItem createNewJMenuItem(String text, ActionListener newActionListener, JMenu jMenuBar) {
    JMenu newObject = new JMenu();
    newObject.setText(text);
    newObject.addActionListener(newActionListener);
    jMenuBar.add(newObject);
    return newObject;
  }

  private class ButtonActionListener implements ActionListener {
    @Getter
    @Setter
    public int x;
    @Getter
    @Setter
    public int y;

    public ButtonActionListener(int x, int y) {
      this.setX(x);
      this.setY(y);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      if (x > 0 && x < fieldSizeX - 1 && y > 0 && y < fieldSizeY - 1) {
        rotateFieldNumbersAction(x, y);
        refreshFieldButton(false);
        isSelectActive = false;
      }
    }
  }

  private class MenuItemSizeFieldActionListener implements ActionListener {
    @Getter
    @Setter
    public int index;
    @Getter
    @Setter
    public int size;
    @Getter
    @Setter
    public boolean isCustom;

    public MenuItemSizeFieldActionListener(int index, int size, boolean isCustom) {
      this.index = index;
      this.size = size;
      this.isCustom = isCustom;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      if (!isCustom) {
        fieldSizeX = size;
        fieldSizeY = size;
      } else {
        // Open a new JFrame with question:" How Many X and Y Tiles?"
      }
      menuitemSizeField[sizeFieldChosen].setBackground(new JMenuItem().getBackground());
      menuitemSizeField[sizeFieldChosen].setForeground(new JMenuItem().getForeground());
      sizeFieldChosen = index;
      menuitemSizeField[sizeFieldChosen].setBackground(colorb4);
      menuitemSizeField[sizeFieldChosen].setForeground(colorf4);
      setButtonFieldBound();
      setWindowSize();
      selectx = 1;
      selecty = 1;
      isSelectActive = false;
      refreshFieldButton(isSelectActive);
      System.out.println("Field Menu Item #" + String.valueOf(index));
    }
  }

  private class MenuItemSizeButtonActionListener implements ActionListener {
    @Getter
    @Setter
    public int index;
    @Getter
    @Setter
    public int size;
    @Getter
    @Setter
    public boolean isCustom;


    public MenuItemSizeButtonActionListener(int index, int size, boolean isCustom) {
      this.index = index;
      this.size = size;
      this.isCustom = isCustom;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      if (!isCustom) {
        bfW = size;
        bfH = size;
      } else {
        // Open a new JFrame with question:" How Many X and Y Tiles?"
      }
      menuitemSizeButton[sizeButtonChosen].setBackground(new JMenuItem().getBackground());
      menuitemSizeButton[sizeButtonChosen].setForeground(new JMenuItem().getForeground());
      sizeButtonChosen = index;
      menuitemSizeButton[sizeButtonChosen].setBackground(colorb4);
      menuitemSizeButton[sizeButtonChosen].setForeground(colorf4);
      setButtonFieldBound();
      setWindowSize();
      selectx = 1;
      selecty = 1;
      isSelectActive = false;
      refreshFieldButton(isSelectActive);
      System.out.println("Button Menu Item #" + String.valueOf(index));
    }
  }

  public void rotateFieldNumbersClockwise(int x, int y) {
    if (y > 0 && y < fieldInt.length - 1) {
      if (x > 0 && x < fieldInt[y].length - 1) {
        int number = fieldInt[y - 1][x - 1];
        fieldInt[y - 1][x - 1] = fieldInt[y - 0][x - 1];
        fieldInt[y - 0][x - 1] = fieldInt[y + 1][x - 1];
        fieldInt[y + 1][x - 1] = fieldInt[y + 1][x - 0];
        fieldInt[y + 1][x - 0] = fieldInt[y + 1][x + 1];
        fieldInt[y + 1][x + 1] = fieldInt[y + 0][x + 1];
        fieldInt[y + 0][x + 1] = fieldInt[y - 1][x + 1];
        fieldInt[y - 1][x + 1] = fieldInt[y - 1][x + 0];
        fieldInt[y - 1][x + 0] = number;
      }
    }
  }

  public void rotateFieldNumbersAntiClockwise(int x, int y) {
    if (y > 0 && y < fieldInt.length - 1) {
      if (x > 0 && x < fieldInt[y].length - 1) {
        int number = fieldInt[y - 1][x + 0];
        fieldInt[y - 1][x + 0] = fieldInt[y - 1][x + 1];
        fieldInt[y - 1][x + 1] = fieldInt[y + 0][x + 1];
        fieldInt[y + 0][x + 1] = fieldInt[y + 1][x + 1];
        fieldInt[y + 1][x + 1] = fieldInt[y + 1][x - 0];
        fieldInt[y + 1][x - 0] = fieldInt[y + 1][x - 1];
        fieldInt[y + 1][x - 1] = fieldInt[y - 0][x - 1];
        fieldInt[y - 0][x - 1] = fieldInt[y - 1][x - 1];
        fieldInt[y - 1][x - 1] = number;
      }
    }
  }

  public void rotateFieldNumbers(Tuple4<Integer, Integer, Integer, Boolean> t) {
    rotateFieldNumbers(t.v1, t.v2, t.v3, t.v4);
  }
  public void rotateFieldNumbers(int x, int y, int rotations, boolean clockwise) {
    if (clockwise) {
      for (int i = 0; i < rotations; ++i) {
        rotateFieldNumbersClockwise(x, y);
      }
    } else {
      for (int i = 0; i < rotations; ++i) {
        rotateFieldNumbersAntiClockwise(x, y);
      }
    }
  }

  public void rotateFieldNumbersAction(int x, int y) {
    int rotations = 1;
    if (isRotation1) {
      rotations += 1;
    }
    if (isRotation2) {
      rotations += 2;
    }
    if (isRotationAntiClockwise == false) {
      for (int i = 0; i < rotations; i++) {
        rotateFieldNumbersClockwise(x, y);
      }
    } else {
      for (int i = 0; i < rotations; i++) {
        rotateFieldNumbersAntiClockwise(x, y);
      }
    }
  }

  public void refreshFieldButton() {
    refreshFieldButton(true);
  }

  public void refreshFieldButton(boolean isRefreshSelect) {
    for (int j = 0; j < fieldSizeY; j++) {
      for (int i = 0; i < fieldSizeX; i++) {
        fieldButton[j][i].setText(String.valueOf(fieldInt[j][i]));
        if (!(i == selectx && j == selecty)) {
          if (!checkboxmenuitemShowColor.getState()) {
            fieldButton[j][i].setBackground(colorb0);
            fieldButton[j][i].setForeground(colorf0);
          } else if (fieldInt[j][i] == i + fieldSizeX * j + 1) {
            fieldButton[j][i].setBackground(colorb1);
            fieldButton[j][i].setForeground(colorf1);
          } else {
            fieldButton[j][i].setBackground(colorb2);
            fieldButton[j][i].setForeground(colorf2);
          }
        } else if (isRefreshSelect) {
          fieldButton[j][i].setBackground(colorb3);
          fieldButton[j][i].setForeground(colorf3);
        } else {
          if (!checkboxmenuitemShowColor.getState()) {
            fieldButton[j][i].setBackground(colorb0);
            fieldButton[j][i].setForeground(colorf0);
          } else if (fieldInt[j][i] == i + fieldSizeX * j + 1) {
            fieldButton[j][i].setBackground(colorb1);
            fieldButton[j][i].setForeground(colorf1);
          } else {
            fieldButton[j][i].setBackground(colorb2);
            fieldButton[j][i].setForeground(colorf2);
          }
        }
      }
    }
  }

  public void fieldNew() {
    for (int j = 0; j < fieldButton.length; j++) {
      for (int i = 0; i < fieldButton[j].length; i++) {
        fieldInt[j][i] = i + fieldSizeX * j + 1;
      }
    }
    refreshFieldButton(isSelectActive);
  }

  public void fieldMix() {
    Random r = new Random();
    int maxX = fieldSizeX - 2;
    int maxY = fieldSizeY - 2;
    int maxRotation = 6;
    int rotations = 0;
    for (int i = 0; i < fieldSizeX * fieldSizeY; i++) {
      rotateFieldNumbers(
          1 + Math.abs(r.nextInt()) % maxX,
          1 + Math.abs(r.nextInt()) % maxY,
          Math.abs(r.nextInt()) % 4+1,
          (Math.abs(r.nextInt() % 2) == 0));
    }
    refreshFieldButton(isSelectActive);
  }

  public void fieldSolve() {
    int sizeX = this.fieldSizeX;
    int sizeY = this.fieldSizeY;

    // TODO: move this part to a own written class!
    HashMap<Integer, Tup2ListTup4> movingTable = new HashMap<>();
    Function2<Integer, Tup2ListTup4> fillMovingTable =
        (num_, movingFields_) -> {
      int row = (num_ - 1) / sizeX;
      int col = (num_ - 1) % sizeX;
      if ((row >= 0 && row <= 2) && (col >= 0 && col <= 2)) {
        return;
      }

      boolean lastRowFinished = false;
      for (int y = 3; y < sizeY; ++y) {
        for (int x = 0; x < sizeX; ++x) {
          if (y * sizeX + x >= num_ - 1) {
            lastRowFinished = true;
            break;
          }
          movingFields_.v.put(new Tuple2<>(x, y), new ArrayList<>());
        }
        if (lastRowFinished) {
          break;
        }
      }

      for (int y = 0; y < 3; ++y) {
        for (int x = 0; x < 3; ++x) {
          movingFields_.v.put(new Tuple2<>(x, y), new ArrayList<>());
        }
      }

      if (row > 2) {
        for (int x = 0; x < sizeX; ++x) {
          for (int y = 0; y < 3; ++y) {
            movingFields_.v.put(new Tuple2<>(x, y), new ArrayList<>());
          }
        }
      } else {
        for (int x = 3; x < col - 1; ++x) {
          for (int y = 0; y < 3; ++y) {
            movingFields_.v.put(new Tuple2<>(x, y), new ArrayList<>());
          }
        }

        for (int x = col; x < col + 1; ++x) {
          for (int y = 0; y < 3; ++y) {
            if (y * sizeX + x >= num_ - 1) {
              return;
            }
            movingFields_.v.put(new Tuple2<>(x, y), new ArrayList<>());
          }
        }
      }
    };
    for (int num = 1; num <= sizeX * sizeY; ++num) {
      Tup2ListTup4 movingFields = new Tup2ListTup4();
      movingTable.put(num, movingFields);
      fillMovingTable.func(num, movingFields);
    }

    // TODO: move this part to a own written class too!
    if (sizeX == 4 && sizeY == 4) {
      Tup2ListTup4 mt16 = movingTable.get(16);
      mt16.v.get(new Tuple2<>(0, 0)).addAll(Lists.newArrayList(new Tuple4<>(1, 1, 3, true), new Tuple4<>(2, 2, 3, true)));
      mt16.v.get(new Tuple2<>(1, 0)).addAll(Lists.newArrayList(new Tuple4<>(1, 1, 2, true), new Tuple4<>(2, 2, 3, true)));
      mt16.v.get(new Tuple2<>(2, 0)).addAll(Lists.newArrayList(new Tuple4<>(1, 1, 1, true), new Tuple4<>(2, 2, 3, true)));
      mt16.v.get(new Tuple2<>(3, 0)).addAll(Lists.newArrayList(new Tuple4<>(2, 1, 1, true), new Tuple4<>(2, 2, 2, true)));

      mt16.v.get(new Tuple2<>(0, 1)).addAll(Lists.newArrayList(new Tuple4<>(1, 2, 1, true), new Tuple4<>(2, 2, 4, true)));
      mt16.v.get(new Tuple2<>(1, 1)).addAll(Lists.newArrayList(new Tuple4<>(2, 2, 4, true)));
      mt16.v.get(new Tuple2<>(2, 1)).addAll(Lists.newArrayList(new Tuple4<>(2, 2, 3, true)));
      mt16.v.get(new Tuple2<>(3, 1)).addAll(Lists.newArrayList(new Tuple4<>(2, 2, 2, true)));

      mt16.v.get(new Tuple2<>(0, 2)).addAll(Lists.newArrayList(new Tuple4<>(1, 2, 3, false), new Tuple4<>(2, 2, 1, false)));
      mt16.v.get(new Tuple2<>(1, 2)).addAll(Lists.newArrayList(new Tuple4<>(2, 2, 3, false)));
      mt16.v.get(new Tuple2<>(2, 2)).addAll(Lists.newArrayList(new Tuple4<>(1, 2, 1, true), new Tuple4<>(2, 2, 1, false)));
      mt16.v.get(new Tuple2<>(3, 2)).addAll(Lists.newArrayList(new Tuple4<>(2, 2, 1, true)));

      mt16.v.get(new Tuple2<>(0, 3)).addAll(Lists.newArrayList(new Tuple4<>(1, 2, 1, false), new Tuple4<>(2, 2, 2, false)));
      mt16.v.get(new Tuple2<>(1, 3)).addAll(Lists.newArrayList(new Tuple4<>(2, 2, 2, false)));
      mt16.v.get(new Tuple2<>(2, 3)).addAll(Lists.newArrayList(new Tuple4<>(2, 2, 1, false)));

      // Only move nr 16 for the field size 4x4!
      boolean isFoundNum = false;
      for (int y = 0; y < sizeY; ++y) {
        for (int x = 0; x < sizeX; ++x) {
          if (this.fieldInt[y][x] == 16) {
            isFoundNum = true;
            ArrayList<Tuple4<Integer, Integer, Integer, Boolean>> moves = mt16.v.get(new Tuple2<>(x, y));
            for (int i = 0; i < moves.size(); ++i) {
              rotateFieldNumbers(moves.get(i));
            }
            break;
          }
        }
        if (isFoundNum) {
          break;
        }
      }
    }

    refreshFieldButton(isSelectActive);
  }

  @Override
  public void keyTyped(KeyEvent e) {
    switch (e.getKeyChar()) {
      case 'a':
        isRotationAntiClockwise = true;
        break;
      case 's':
        isRotation1 = true;
        break;
      case 'd':
        isRotation2 = true;
        break;
      case ' ':
        // do the rotation
        System.out.println("x = " + selectx
            + "   y = " + selecty
            + "   clockwise = " + !isRotationAntiClockwise
            + "   rot+1 = " + isRotation1
            + "   rot2 = " + isRotation2);
        rotateFieldNumbersAction(selectx, selecty);
        refreshFieldButton();
        break;
      case 'm':
        fieldMix();
        break;
      case 'b':
        fieldSolve();
        break;
      case 'n':
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        fieldNew();
        break;
    }
  }

  @Override
  public void keyPressed(KeyEvent e) {
    //System.out.println("Key pressed:\n" + String.valueOf(e));
    int keyCode = e.getKeyCode();
    JButton btnPrev = fieldButton[selecty][selectx];
    JButton btnNext = null;
    boolean isChanging = false;
    // When not selected, then it should do nothing
    if (isSelectActive) {
      switch (keyCode) {
        case KeyEvent.VK_UP:
        case KeyEvent.VK_NUMPAD8:
          // handle up
          if (selecty > 1) {
            btnNext = fieldButton[selecty - 1][selectx];
            selecty--;
            isChanging = true;
          }
          break;
        case KeyEvent.VK_DOWN:
        case KeyEvent.VK_NUMPAD5:
          // handle down
          if (selecty < fieldSizeY - 2) {
            btnNext = fieldButton[selecty + 1][selectx];
            selecty++;
            isChanging = true;
          }
          break;
        case KeyEvent.VK_LEFT:
        case KeyEvent.VK_NUMPAD4:
          // handle left
          if (selectx > 1) {
            btnNext = fieldButton[selecty][selectx - 1];
            selectx--;
            isChanging = true;
          }
          break;
        case KeyEvent.VK_RIGHT:
        case KeyEvent.VK_NUMPAD6:
          // handle right
          if (selectx < fieldSizeX - 2) {
            btnNext = fieldButton[selecty][selectx + 1];
            selectx++;
            isChanging = true;
          }
          break;
      }
    }
    // When Changing is possible, then do it
    if (isChanging) {
      Color c = btnPrev.getBackground();
      btnPrev.setBackground(btnNext.getBackground());
      btnNext.setBackground(c);
      c = btnPrev.getForeground();
      btnPrev.setForeground(btnNext.getForeground());
      btnNext.setForeground(c);
    }
    // Turn SelectActive on, if it is false
    if (isSelectActive == false &&
        !(keyCode == KeyEvent.VK_N ||
            keyCode == KeyEvent.VK_M)) {
      isSelectActive = true;
    }
    // Also, refresh only when space, up, down, left or right is pressed
    if (keyCode == KeyEvent.VK_SPACE ||
        keyCode == KeyEvent.VK_UP ||
        keyCode == KeyEvent.VK_DOWN ||
        keyCode == KeyEvent.VK_LEFT ||
        keyCode == KeyEvent.VK_RIGHT ||
        keyCode == KeyEvent.VK_NUMPAD8 ||
        keyCode == KeyEvent.VK_NUMPAD5 ||
        keyCode == KeyEvent.VK_NUMPAD4 ||
        keyCode == KeyEvent.VK_NUMPAD6) {
      refreshFieldButton(true);
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
    int keyCode = e.getKeyCode();
    switch (keyCode) {
      case KeyEvent.VK_A:
        isRotationAntiClockwise = false;
        break;
      case KeyEvent.VK_S:
        isRotation1 = false;
        break;
      case KeyEvent.VK_D:
        isRotation2 = false;
        break;
    }
  }
}
