package net.pimathclanguage.rotationpuzzle;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.Semaphore;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import lombok.Getter;
import lombok.Setter;
import net.pimathclanguage.rotationpuzzle.Tuple.Tuple2;
import net.pimathclanguage.rotationpuzzle.Tuple.Tuple3;
import net.pimathclanguage.rotationpuzzle.Tuple.Tuple4;

import net.pimathclanguage.rotationpuzzle.DataClasses.HashTup2ListTup4;
import net.pimathclanguage.rotationpuzzle.DataClasses.HashIntHashTup2ListTup4;
import net.pimathclanguage.rotationpuzzle.DataClasses.HashTup2HashIntHashTup2ListTup4;

import net.pimathclanguage.rotationpuzzle.FunctionInterfaces.Function2;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class RotationPuzzle extends JFrame implements KeyListener {
  private SolvingInputs solvingInputs = new SolvingInputs();
  private HashTup2HashIntHashTup2ListTup4 movingTableFields;
  private HashIntHashTup2ListTup4 currentMovingTable;
  private Semaphore semaphoreMovingTableFields = new Semaphore(1);

  private ArrayList<Integer> numsOrder;
  private HashMap<String, ArrayList<Tuple4<Integer, Integer, Integer, Boolean>>> movingTable9x9;
  private Semaphore semaphoreMovingTable9x9 = new Semaphore(1);

  private Semaphore doingSlowlyMoves = new Semaphore(1);
  private int movingSpeedTime; // ms

  private MenuItemSizeFieldActionListener currentMenuItemSizeFieldActionListener = null;
  private MenuItemSizeButtonActionListener currentMenuItemSizeButtonActionListener = null;
  private MenuItemSolvingSpeedActionListener currentMenuItemSolvingSpeedActionListener = null;
  private Semaphore semaphoreActionListener = new Semaphore(1);

  private Semaphore semaphoreJComponents = new Semaphore(1);

  // Labels
  private JLabel labelFieldSizeText;
  private JLabel labelFieldSize;
  private JLabel labelButtonSizeText;
  private JLabel labelButtonSize;
  private JLabel labelSolveSpeedText;
  private JLabel labelSolveSpeed;

  private JLabel labelFrame;
  private int labelThickness = 6;
  private int labelSpacing = 3;

  // Menubar with Menus and MenuItems
  private JMenuBar menuBar;
  //// Main
  private JMenu menuMain;
  private JMenuItem menuitemNew;
  private JMenuItem menuitemMix;
  private JMenuItem menuitemSolve;
  private JMenuItem menuitemExit;
  //// Settings
  private JMenu menuSettings;
  ////// Size of Field
  private JMenu menuSizeField;
  private int[] fieldSizes;
  private JMenuItem[] menuitemSizeField;
  private int sizeFieldChosen;
  ////// Size of Buttons
  private JMenu menuSizeButton;
  private int[] buttonSizes;
  private JMenuItem[] menuitemSizeButton;
  private int sizeButtonChosen;
  ////// Solving speed
  private JMenu menuSolvingSpeed;
  private int[] timesInMilliSeconds;
  private JMenuItem[] menuitemSolvingSpeed;
  private int lastUsedIdxMenuItemSolvingSpeed;
  ////// Other
  private JCheckBoxMenuItem checkboxmenuitemAutoCenter;
  private JCheckBoxMenuItem checkboxmenuitemShowColor;
  private JMenuItem menuitemChangeKeys;
  //// Help
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
  // Selected by solve
  private Color colorb5 = new Color(0xA9A929);
  private Color colorf5 = new Color(0x0F142A);
  // Label colors
  private Color colorlb0 = new Color(0x122438);
  private Color colorlf0 = new Color(0xD0D0A0);
  private Color colorlb1 = new Color(0x4D7A5B);
  private Color colorlf1 = new Color(0xF6FFED);

  // Add the tracker of the rotations per position
  private List<Tuple3<Integer, Integer, Integer>> rotTracker = new ArrayList<>();

  // Constructor of this Class
  public RotationPuzzle() {
    movingTableFields = new HashTup2HashIntHashTup2ListTup4();
    // Default
    setVariablesDefault();
    setObjectDefault();
    // This Frame properties
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLocationRelativeTo(null);
    this.setLayout(null);
    this.addKeyListener(this);
    this.setTitle("Rotation Puzzle - by Ziko Haris");
    this.setJMenuBar(menuBar);
    this.setResizable(false);
    setWindowSize();
  }

  private void setWindowSize() {
    this.setSize(new Dimension(
        bfL * 2 + 2 + bfW * fieldSizeX + bfSpace * (fieldSizeX - 1),
        bfT + 60 + 6 + bfH * fieldSizeY + bfSpace * (fieldSizeY - 1)
    ));

    try {
      Thread.sleep(20);
    } catch (Exception e) {

    }
    if (checkboxmenuitemAutoCenter.getState()) {
      this.setLocationRelativeTo(null);
    }
  }

  private void setVariablesDefault() {
    selectx = 1;
    selecty = 1;
    isRotationAntiClockwise = false;
    isRotation1 = false;
    isRotation2 = false;
    isSelectActive = false;

    fieldSizes = new int[10];
    for (int i = 0; i < fieldSizes.length; ++i) {
      fieldSizes[i] = 4 + i;
    }

    buttonSizes = new int[15];
    for (int i = 0; i < buttonSizes.length; ++i) {
      buttonSizes[i] = 30 + i * 5;
    }

    timesInMilliSeconds = new int[]{1, 2, 5, 10, 20, 50, 100, 200, 500};
  }

  private void setObjectDefault() {
    fieldSizeX = 5;
    fieldSizeY = 5;
    bfW = 60;
    bfH = 60;
    bfL = 20;
    bfT = 88;
    bfSpace = 6;

    createButtonField();
    setMenuBar();
    setLabels();
    setButtonFieldBound();
    new Thread() {
      @Override
      public void run() {
        loadNewMovingTable();
        loadMovingTable9x9();
      }
    }.start();

    currentMenuItemSizeFieldActionListener.doCurrent();
    currentMenuItemSizeButtonActionListener.doCurrent();
    currentMenuItemSolvingSpeedActionListener.doCurrent();
  }

  private void createButtonField() {
    Font newFont = new Font((new JButton()).getFont().getFontName(), Font.BOLD, 18);
    this.fieldInt = new int[20][20];
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
        this.fieldInt[j][i] = i + fieldSizeX * j + 1;
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
            Border outerBorder = new LineBorder(Color.RED, 2);
            btn.setBorder(outerBorder);

            btn.setBounds(btn.getX()-2, btn.getY()-2, btn.getWidth()+4, btn.getHeight()+4);
          } else {
//            btn.setBorder(null);
            Border outerBorder = new LineBorder(Color.BLUE, 2);
            btn.setBorder(outerBorder);

            btn.setBounds(btn.getX()-2, btn.getY()-2, btn.getWidth()+4, btn.getHeight()+4);
          }
          this.fieldInt[j][i] = i + j * fieldSizeX + 1;
        }
      }
    }

    labelFrame.setBounds(
        bfL - labelThickness - labelSpacing,
        bfT - labelThickness - labelSpacing,
        bfW * fieldSizeX + bfSpace * (fieldSizeX - 1) + (labelThickness + labelSpacing) * 2,
        bfH * fieldSizeY + bfSpace * (fieldSizeY - 1) + (labelThickness + labelSpacing) * 2);
  }

  // Define the Bar with there Elements
  private void setMenuBar() {
    Font font14 = new Font(new JMenu().getFont().getName(), new JMenu().getFont().getStyle(), 14);
    menuBar = new JMenuBar();
    // Main
    menuMain = new JMenu();
    menuMain.setText("Main");
    menuBar.add(menuMain);
    // New
    menuitemNew = new JMenuItem();
    menuitemNew.setText("New Game");
    menuitemNew.addActionListener(e -> fieldNew());
    menuMain.add(menuitemNew);
    // Mix
    menuitemMix = new JMenuItem();
    menuitemMix.setText("Mix Field");
    menuitemMix.addActionListener(e -> fieldMix());
    menuMain.add(menuitemMix);
    // Solve
    menuitemSolve = new JMenuItem();
    menuitemSolve.setText("Solve Field");
    menuitemSolve.addActionListener(e -> fieldSolve());
    menuMain.add(menuitemSolve);
    // Seperator
    menuMain.add(new JSeparator());
    // Exit
    menuitemExit = new JMenuItem();
    menuitemExit.setText("Exit");
    menuitemExit.addActionListener(e -> System.exit(0));
    menuMain.add(menuitemExit);
    // Size
    menuSettings = new JMenu();
    menuSettings.setText("Settings");
    menuBar.add(menuSettings);
    // Size of Field
    menuSizeField = new JMenu();
    menuSizeField.setText("Size of Field");
    menuSizeField.setFont(font14);
    menuSettings.add(menuSizeField);
    // Size Field List
    menuitemSizeField = new JMenuItem[fieldSizes.length];
    for (int i = 0; i < fieldSizes.length; i++) {
      int fieldSize = fieldSizes[i];
      menuitemSizeField[i] = new JMenuItem();
      menuitemSizeField[i].setText(fieldSize + "x" + fieldSize);
      ActionListener menuFieldListener = new MenuItemSizeFieldActionListener(i, fieldSize, false);
      menuitemSizeField[i].addActionListener(menuFieldListener);
      menuSizeField.add(menuitemSizeField[i]);
    }
    for (int i = 0; i < menuitemSizeField.length; ++i) {
      MenuItemSizeFieldActionListener menuFieldListener = (MenuItemSizeFieldActionListener)menuitemSizeField[i].getActionListeners()[0];
      if (i > 0) {
        menuFieldListener.setPrevActionListener((MenuItemSizeFieldActionListener)menuitemSizeField[i - 1].getActionListeners()[0]);
      }
      if (i < menuitemSizeField.length - 1) {
        menuFieldListener.setNextActionListener((MenuItemSizeFieldActionListener)menuitemSizeField[i + 1].getActionListeners()[0]);
      }
    }
    currentMenuItemSizeFieldActionListener = (MenuItemSizeFieldActionListener)menuitemSizeField[1].getActionListeners()[0];
    // The Standard Chosen one is with 5x5 Field
    sizeFieldChosen = 1;
    menuitemSizeField[sizeFieldChosen].setBackground(colorb4);
    menuitemSizeField[sizeFieldChosen].setForeground(colorf4);
    // Size of Button
    menuSizeButton = new JMenu();
    menuSizeButton.setText("Size of Button");
    menuSizeButton.setFont(font14);
    menuSettings.add(menuSizeButton);

    menuitemSizeButton = new JMenuItem[buttonSizes.length];
    for (int i = 0; i < buttonSizes.length; i++) {
      int buttonSize = buttonSizes[i];
      menuitemSizeButton[i] = new JMenuItem();
      menuitemSizeButton[i].setText(buttonSize + "px");
      menuitemSizeButton[i].addActionListener(new MenuItemSizeButtonActionListener(i, buttonSize, false));
      menuSizeButton.add(menuitemSizeButton[i]);
    }

    // TODO: change to doCurrent function!
    sizeButtonChosen = 6;
    menuitemSizeButton[sizeButtonChosen].setBackground(colorb4);
    menuitemSizeButton[sizeButtonChosen].setForeground(colorf4);

    for (int i = 0; i < menuitemSizeButton.length; ++i) {
      MenuItemSizeButtonActionListener menuFieldListener = (MenuItemSizeButtonActionListener)menuitemSizeButton[i].getActionListeners()[0];
      if (i > 0) {
        menuFieldListener.setPrevActionListener((MenuItemSizeButtonActionListener)menuitemSizeButton[i - 1].getActionListeners()[0]);
      }
      if (i < menuitemSizeButton.length - 1) {
        menuFieldListener.setNextActionListener((MenuItemSizeButtonActionListener)menuitemSizeButton[i + 1].getActionListeners()[0]);
      }
    }

    currentMenuItemSizeButtonActionListener = (MenuItemSizeButtonActionListener)menuitemSizeButton[5].getActionListeners()[0];
    // Solving Time
    menuSolvingSpeed = new JMenu();
    menuSolvingSpeed.setText("Solving Speed");
    menuSolvingSpeed.setFont(font14);
    menuSettings.add(menuSolvingSpeed);

    menuitemSolvingSpeed = new JMenuItem[timesInMilliSeconds.length];

    for (int i = 0; i < timesInMilliSeconds.length; ++i) {
      JMenuItem jmi = new JMenuItem();
      jmi.setText(timesInMilliSeconds[i]+" ms");
      jmi.addActionListener(new MenuItemSolvingSpeedActionListener(i, jmi, timesInMilliSeconds[i]));
      menuitemSolvingSpeed[i] = jmi;
      menuSolvingSpeed.add(jmi);
    }

    for (int i = 0; i < timesInMilliSeconds.length; ++i) {
      MenuItemSolvingSpeedActionListener now = (MenuItemSolvingSpeedActionListener)menuitemSolvingSpeed[i].getActionListeners()[0];
      if (i > 0) {
        now.prevActionListener = (MenuItemSolvingSpeedActionListener)menuitemSolvingSpeed[i - 1].getActionListeners()[0];
      }
      if (i < timesInMilliSeconds.length - 1) {
        now.nextActionListener = (MenuItemSolvingSpeedActionListener)menuitemSolvingSpeed[i + 1].getActionListeners()[0];
      }
    }

    lastUsedIdxMenuItemSolvingSpeed = 3;
    currentMenuItemSolvingSpeedActionListener = (MenuItemSolvingSpeedActionListener)menuitemSolvingSpeed[lastUsedIdxMenuItemSolvingSpeed].getActionListeners()[0];
    // Settings
//    menuSettings2 = new JMenu();
//    menuSettings2.setText("Settings");
//    menuBar.add(menuSettings2);
    // AutoCenter
    checkboxmenuitemAutoCenter = new JCheckBoxMenuItem();
    checkboxmenuitemAutoCenter.setText("Auto Center");
    checkboxmenuitemAutoCenter.setState(true);
    checkboxmenuitemAutoCenter.setFont(font14);
    menuSettings.add(checkboxmenuitemAutoCenter);
    // Show Color
    checkboxmenuitemShowColor = new JCheckBoxMenuItem();
    checkboxmenuitemShowColor.setText("Show Color");
    checkboxmenuitemShowColor.setState(true);
    checkboxmenuitemShowColor.addActionListener(e -> refreshFieldButton(isSelectActive));
    checkboxmenuitemShowColor.setFont(font14);
    menuSettings.add(checkboxmenuitemShowColor);
    // Change Keys
    menuitemChangeKeys = createNewJMenuItem("Change Keys",
        null,
        menuSettings);
    menuitemChangeKeys.setFont(font14);
    // Help
    menuHelp = new JMenu();
    menuHelp.setText("Help");
    menuBar.add(menuHelp);
    // Intro
    menuitemIntro = new JMenuItem();
    menuitemIntro.setText("Intro");
    menuitemIntro.addActionListener(e -> {
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
    });
    menuHelp.add(menuitemIntro);
    // Info
    menuitemInfo = new JMenuItem();
    menuitemInfo.setText("Info");
    menuitemInfo.addActionListener(e -> {
      JOptionPane.showConfirmDialog(null,
          "Rotation Puzzle programmed by Haris Ziko\n" +
              "Last Date of changes: 2019.05.24; 11:19\n" +
              "Added Features:\n" +
              "+ Auto Size\n+ Show Color\n+ Change Button Size\n+ Change Field Size\n+ Mix and solve the puzzle!\n" +
              "+ Add multithreading plus locking/semaphores too\n" +
              "+ Can use keyboard to play too\n" +
              "v0.2.6", "Rotations Puzzle - Info", JOptionPane.DEFAULT_OPTION);
    });
    menuHelp.add(menuitemInfo);
  }

  private void setLabels() {
    Font font = new Font((new JButton()).getFont().getFontName(), Font.BOLD, 10);
    int border = 2;

    int w1 = 80;
    int w2 = 50;

    Rectangle r11 = new Rectangle(bfL - labelThickness - labelSpacing, 10, w1 + w2, 20);
    Rectangle r12 = new Rectangle(r11.x + w1, r11.y + border, r11.width - w1 - border, r11.height - border * 2);

    Rectangle r21 = new Rectangle(r11.x, r11.y + r11.height + border, w1 + w2, r11.height);
    Rectangle r22 = new Rectangle(r21.x + w1, r21.y + border, r21.width - w1 - border, r21.height - border * 2);

    Rectangle r31 = new Rectangle(r11.x, r21.y +r21.height + border, w1 + w2, r11.height);
    Rectangle r32 = new Rectangle(r31.x + w1, r31.y + border, r31.width - w1 - border, r31.height - border * 2);

    labelFieldSizeText = new JLabel();
    labelFieldSizeText.setText(" Field Size:");
    labelFieldSizeText.setBounds(r11);
    labelFieldSizeText.setFont(font);
    labelFieldSizeText.setBackground(colorlb0);
    labelFieldSizeText.setForeground(colorlf0);
    labelFieldSizeText.setBorder(null);
    labelFieldSizeText.setOpaque(true);

    labelFieldSize = new JLabel();
    labelFieldSize.setText(fieldSizeX + "x" + fieldSizeY);
    labelFieldSize.setBounds(r12);
    labelFieldSize.setFont(font);
    labelFieldSize.setBackground(colorlb1);
    labelFieldSize.setForeground(colorlf1);
    labelFieldSize.setBorder(null);
    labelFieldSize.setOpaque(true);
    labelFieldSize.setHorizontalAlignment(JLabel.CENTER);

    labelButtonSizeText = new JLabel();
    labelButtonSizeText.setText(" Button Size:");
    labelButtonSizeText.setBounds(r21);
    labelButtonSizeText.setFont(font);
    labelButtonSizeText.setBackground(colorlb0);
    labelButtonSizeText.setForeground(colorlf0);
    labelButtonSizeText.setBorder(null);
    labelButtonSizeText.setOpaque(true);

    labelButtonSize = new JLabel();
    labelButtonSize.setText("" + bfW);
    labelButtonSize.setBounds(r22);
    labelButtonSize.setFont(font);
    labelButtonSize.setBackground(colorlb1);
    labelButtonSize.setForeground(colorlf1);
    labelButtonSize.setBorder(null);
    labelButtonSize.setOpaque(true);
    labelButtonSize.setHorizontalAlignment(JLabel.CENTER);

    labelSolveSpeedText = new JLabel();
    labelSolveSpeedText.setText(" Solve Speed:");
    labelSolveSpeedText.setBounds(r31);
    labelSolveSpeedText.setFont(font);
    labelSolveSpeedText.setBackground(colorlb0);
    labelSolveSpeedText.setForeground(colorlf0);
    labelSolveSpeedText.setBorder(null);
    labelSolveSpeedText.setOpaque(true);

    labelSolveSpeed = new JLabel();
    labelSolveSpeed.setText(movingSpeedTime + " ms");
    labelSolveSpeed.setBounds(r32);
    labelSolveSpeed.setFont(font);
    labelSolveSpeed.setBackground(colorlb1);
    labelSolveSpeed.setForeground(colorlf1);
    labelSolveSpeed.setBorder(null);
    labelSolveSpeed.setOpaque(true);
    labelSolveSpeed.setHorizontalAlignment(JLabel.CENTER);

    this.add(labelFieldSize);
    this.add(labelFieldSizeText);

    this.add(labelButtonSize);
    this.add(labelButtonSizeText);

    this.add(labelSolveSpeed);
    this.add(labelSolveSpeedText);

    labelFrame = new JLabel();
    labelFrame.setBorder(BorderFactory.createLineBorder(new Color(0x000000), labelThickness));
    this.add(labelFrame);
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
    @Getter @Setter public int x;
    @Getter @Setter public int y;

    public ButtonActionListener(int x, int y) {
      this.setX(x);
      this.setY(y);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      if (x > 0 && x < fieldSizeX - 1 && y > 0 && y < fieldSizeY - 1) {
        if (!doingSlowlyMoves.tryAcquire()) {
          return;
        }
        rotateFieldNumbersAction(x, y);
        refreshFieldButton(false);
        isSelectActive = false;
        doingSlowlyMoves.release();
      }
    }
  }

  public void createNewNumsOrder() {
    int sizeY = this.fieldSizeY;
    int sizeX = this.fieldSizeX;

    numsOrder = new ArrayList<>();

    // Do first all the lower part
    for (int y = sizeY-1; y > 2; --y) {
      for (int x = sizeX-1; x > -1; --x) {
        numsOrder.add(y*sizeX+x);
      }
    }

    // Then next the upper right part
    for (int x = sizeX-1; x > 2; --x) {
      for (int y = 2; y > -1; --y) {
        numsOrder.add(y*sizeX+x);
      }
    }
  }

  private void loadNewMovingTable() {
    try { semaphoreMovingTableFields.acquire(); } catch (Exception e) { e.printStackTrace(); }
    int sizeX = this.fieldSizeX;
    int sizeY = this.fieldSizeY;

    createNewNumsOrder();

    // TODO: move this part to a own written class!
    Tuple2<Integer, Integer> fieldSize = new Tuple2<>(sizeX, sizeY);
    HashIntHashTup2ListTup4 movingTable = new HashIntHashTup2ListTup4();
    if (movingTableFields.v.containsKey(fieldSize)) {
      currentMovingTable = movingTableFields.v.get(fieldSize);
      semaphoreMovingTableFields.release();
      return;
    }
    movingTableFields.v.put(fieldSize, movingTable);

    Function2<Integer, HashTup2ListTup4> fillMovingTable = (num_, movingFields_) -> {
      int row = num_ / sizeX;
      int col = num_ % sizeX;
      if ((row >= 0 && row <= 2) && (col >= 0 && col <= 2)) {
        return;
      }

      if (row > 2) {
        boolean lastRowFinished = false;
        for (int y = 0; y < sizeY; ++y) {
          for (int x = 0; x < sizeX; ++x) {
            if (y * sizeX + x >= num_) {
              lastRowFinished = true;
              break;
            }
            movingFields_.v.put(new Tuple2<>(x, y), new ArrayList<>());
          }
          if (lastRowFinished) {
            break;
          }
        }
      } else {
        for (int x = 0; x < col; ++x) {
          for (int y = 0; y < 3; ++y) {
            movingFields_.v.put(new Tuple2<>(x, y), new ArrayList<>());
          }
        }

        for (int y = 0; y < row; ++y) {
          movingFields_.v.put(new Tuple2<>(col, y), new ArrayList<>());
        }
      }
    };

    for (int num = 0; num < sizeX * sizeY; ++num) {
      HashTup2ListTup4 movingFields = new HashTup2ListTup4();
      movingTable.v.put(num, movingFields);
      fillMovingTable.func(num, movingFields);
    }

    try {
      String jsonInputString = solvingInputs.jsonInputStrings.get(sizeX);
      byte[] val = new byte[jsonInputString.length() / 2];
      for (int i = 0; i < val.length; i++) {
        int index = i * 2;
        int j = Integer.parseInt(jsonInputString.substring(index, index + 2), 16);
        val[i] = (byte) j;
      }
      InputStream myInputStream = new ByteArrayInputStream(val);
      Reader targetReader = new InputStreamReader(myInputStream);
      System.out.println("Should try to read the string as FileReader object!");
      Object obj = new JSONParser().parse(targetReader);
      JSONObject jo = (JSONObject)obj;
      for (Iterator iterator = jo.keySet().iterator(); iterator.hasNext(); ) {
        String key = (String) iterator.next();

        Object obj2 = jo.get(key);
        JSONObject jo2 = (JSONObject)obj2;
        for (Iterator iterator2 = jo2.keySet().iterator(); iterator2.hasNext(); ) {
          String key2 = (String) iterator2.next();

          int cellNumber = Integer.valueOf(key2);

          Object obj3 = jo2.get(key2);
          JSONObject jo3 = (JSONObject)obj3;
          for (Iterator iterator3 = jo3.keySet().iterator(); iterator3.hasNext(); ) {
            String key3 = (String) iterator3.next();

            String centerCellArr[] = key3.replaceAll(", ", ",").replaceAll("\\(", "").replaceAll("\\)", "").split(",");
            Tuple2<Integer, Integer> centerCell = new Tuple2<>(Integer.valueOf(centerCellArr[0]), Integer.valueOf(centerCellArr[1]));

            Object obj4 = jo3.get(key3);
            JSONArray jo4 = (JSONArray)obj4;
            ArrayList<Tuple4<Integer, Integer, Integer, Boolean>> moves = new ArrayList<>();
            for (int i = 0; i < jo4.size(); ++i) {
              String move = (String) jo4.get(i);
              String movePrepare = move.replaceAll(", ", ",").replace("False", "false").replace("True", "true").replaceAll("\\(", "").replaceAll("\\)", "");
              String moveSplit[] = movePrepare.split(",");
              int x = Integer.valueOf(moveSplit[0]);
              int y = Integer.valueOf(moveSplit[1]);
              int r = Integer.valueOf(moveSplit[2]);
              boolean c = Boolean.valueOf(moveSplit[3]);
              Tuple4<Integer, Integer, Integer, Boolean> t4 = new Tuple4<>(x, y, r, c);
              moves.add(t4);
            }

            if (movingTable.v.containsKey(cellNumber)) {
              HashTup2ListTup4 movingTabl = movingTable.v.get(cellNumber);
              if (movingTabl.v.containsKey(centerCell)) {
                ArrayList<Tuple4<Integer, Integer, Integer, Boolean>> moves_ = movingTabl.v.get(centerCell);
                moves_.addAll(moves);
              }
            }
          }
        }
      }

      currentMovingTable = movingTable;
    } catch (Exception e) {
      System.out.println("An Exception occured!");
      e.printStackTrace();
      currentMovingTable = null;
    }
    semaphoreMovingTableFields.release();
  }

  private void loadMovingTable9x9() {
    try { semaphoreMovingTable9x9.acquire(); } catch (Exception e) { e.printStackTrace(); }

    movingTable9x9 = new HashMap<>();
    int i = 0;
    BufferedReader reader;
    try {
      InputStream is = net.pimathclanguage.rotationpuzzle.RotationPuzzle.class.getResourceAsStream("/all_moves_3x3_field_text.txt");
//      InputStream is = getClass().getResourceAsStream("all_moves_3x3_field_text.txt");
      InputStreamReader isr = new InputStreamReader(is);
      reader = new BufferedReader(isr);
//      reader = new BufferedReader(new FileReader(filePath));
      String line = reader.readLine();
      while (line != null) {
        if (++i % 10000 == 0) {
          System.out.println("i: "+i);
//          try {
//            Thread.sleep(100);
//          } catch (Exception e) {
//            e.printStackTrace();
//          }
        }
        String field3x3 = line.substring(0, 9);

        String movesStr = line.substring(9);

        ArrayList<Tuple4<Integer, Integer, Integer, Boolean>> movesList = new ArrayList<>();
        int parts = movesStr.length() / 4;
        for (int j = 0; j < parts; ++j) {
          String movePart = movesStr.substring(4*j, 4*(j+1));
          int x = Integer.valueOf(movePart.substring(0, 1));
          int y = Integer.valueOf(movePart.substring(1, 2));
          int r = Integer.valueOf(movePart.substring(2, 3));
          boolean c = Integer.valueOf(movePart.substring(3, 4)) == 1;
          movesList.add(new Tuple4<>(x, y, r, c));
        }

        movingTable9x9.put(field3x3, movesList);

        line = reader.readLine();
      }
      reader.close();
    } catch (IOException e) {
      e.printStackTrace();
      movingTable9x9 = null;
    }

    semaphoreMovingTable9x9.release();
  }

  private class MenuItemSolvingSpeedActionListener implements ActionListener {
    private RotationPuzzle rp = RotationPuzzle.this;

    @Getter @Setter private int index;
    @Getter @Setter private JMenuItem jmi;
    @Getter @Setter private int movingSpeedTime;

    @Getter @Setter private MenuItemSolvingSpeedActionListener prevActionListener = null;
    @Getter @Setter private MenuItemSolvingSpeedActionListener nextActionListener = null;

    public void doNext() {
      if (nextActionListener != null) {
        nextActionListener.actionPerformed(new ActionEvent(this, index, "from SolvingSpeedAL"));
      }
    }

    public void doCurrent() {
      this.actionPerformed(new ActionEvent(this, index, "from SolvingSpeeddAL"));
    }

    public void doPrev() {
      if (prevActionListener != null) {
        prevActionListener.actionPerformed(new ActionEvent(this, index, "from SolvingSpeedAL"));
      }
    }

    MenuItemSolvingSpeedActionListener(int idx, JMenuItem jmi, int movingSpeedTime) {
      this.index = idx;
      this.jmi = jmi;
      this.movingSpeedTime = movingSpeedTime;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      try { rp.semaphoreActionListener.acquire(); } catch (Exception ex) { ex.printStackTrace(); }
      try { rp.semaphoreJComponents.acquire(); } catch (Exception ex) { ex.printStackTrace(); }

      JMenuItem prevJmi = menuitemSolvingSpeed[lastUsedIdxMenuItemSolvingSpeed];
      prevJmi.setBackground(colorb0);
      prevJmi.setForeground(colorf0);

      jmi.setBackground(colorb4);
      jmi.setForeground(colorf4);

      rp.lastUsedIdxMenuItemSolvingSpeed = index;
      rp.movingSpeedTime = movingSpeedTime;

      rp.currentMenuItemSolvingSpeedActionListener = this;
      rp.labelSolveSpeed.setText(rp.movingSpeedTime + " ms");

      rp.semaphoreJComponents.release();
      rp.semaphoreActionListener.release();
    }
  }

  private class MenuItemSizeFieldActionListener implements ActionListener {
    private RotationPuzzle rp = RotationPuzzle.this;

    @Getter @Setter public int index;
    @Getter @Setter public int size;
    @Getter @Setter public boolean isCustom;

    @Getter @Setter public MenuItemSizeFieldActionListener nextActionListener = null;
    @Getter @Setter public MenuItemSizeFieldActionListener prevActionListener = null;

    public void doNext() {
      if (nextActionListener != null) {
        nextActionListener.actionPerformed(new ActionEvent(this, index, "from SizeFieldAL"));
      }
    }

    public void doCurrent() {
      this.actionPerformed(new ActionEvent(this, index, "from SizeFieldAL"));
    }

    public void doPrev() {
      if (prevActionListener != null) {
        prevActionListener.actionPerformed(new ActionEvent(this, index, "from SizeFieldAL"));
      }
    }


    public MenuItemSizeFieldActionListener(int index, int size, boolean isCustom) {
      this.index = index;
      this.size = size;
      this.isCustom = isCustom;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      try { rp.semaphoreActionListener.acquire(); } catch (Exception ex) { ex.printStackTrace(); }
      try { rp.semaphoreJComponents.acquire(); } catch (Exception ex) { ex.printStackTrace(); }

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
      new Thread() {
        @Override
        public void run() {
          if (semaphoreMovingTableFields.availablePermits() > 0) {
            loadNewMovingTable();
          }
        }
      }.start();
//      loadNewMovingTable();
      System.out.println("Field Menu Item #" + index);

      rp.currentMenuItemSizeFieldActionListener = this;
      rp.labelFieldSize.setText(rp.fieldSizeX + "x" + rp.fieldSizeY);

      rp.semaphoreJComponents.release();
      rp.semaphoreActionListener.release();
    }
  }

  private class MenuItemSizeButtonActionListener implements ActionListener {
    private RotationPuzzle rp = RotationPuzzle.this;

    @Getter @Setter public int index;
    @Getter @Setter public int size;
    @Getter @Setter public boolean isCustom;

    @Getter @Setter public MenuItemSizeButtonActionListener nextActionListener = null;
    @Getter @Setter public MenuItemSizeButtonActionListener prevActionListener = null;

    public void doNext() {
      if (nextActionListener != null) {
        nextActionListener.actionPerformed(new ActionEvent(this, index, "from SizeButtonAL"));
      }
    }

    public void doCurrent() {
      this.actionPerformed(new ActionEvent(this, index, "from SizeButtonAL"));
    }

    public void doPrev() {
      if (prevActionListener != null) {
        prevActionListener.actionPerformed(new ActionEvent(this, index, "from SizeButtonAL"));
      }
    }

    public MenuItemSizeButtonActionListener(int index, int size, boolean isCustom) {
      this.index = index;
      this.size = size;
      this.isCustom = isCustom;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      try { rp.semaphoreActionListener.acquire(); } catch (Exception ex) { ex.printStackTrace(); }
      try { rp.semaphoreJComponents.acquire(); } catch (Exception ex) { ex.printStackTrace(); }

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
      System.out.println("Button Menu Item #" + index);

      rp.currentMenuItemSizeButtonActionListener = this;

      rp.semaphoreJComponents.release();
      rp.semaphoreActionListener.release();
    }
  }

  public void rotateFieldNumbersClockwise(int x, int y) {
    if (y > 0 && y < this.fieldInt.length - 1) {
      if (x > 0 && x < this.fieldInt[y].length - 1) {
        int number = this.fieldInt[y - 1][x - 1];
        this.fieldInt[y - 1][x - 1] = this.fieldInt[y - 0][x - 1];
        this.fieldInt[y - 0][x - 1] = this.fieldInt[y + 1][x - 1];
        this.fieldInt[y + 1][x - 1] = this.fieldInt[y + 1][x - 0];
        this.fieldInt[y + 1][x - 0] = this.fieldInt[y + 1][x + 1];
        this.fieldInt[y + 1][x + 1] = this.fieldInt[y + 0][x + 1];
        this.fieldInt[y + 0][x + 1] = this.fieldInt[y - 1][x + 1];
        this.fieldInt[y - 1][x + 1] = this.fieldInt[y - 1][x + 0];
        this.fieldInt[y - 1][x + 0] = number;
      }
    }
  }

  public void rotateFieldNumbersAntiClockwise(int x, int y) {
    if (y > 0 && y < this.fieldInt.length - 1) {
      if (x > 0 && x < this.fieldInt[y].length - 1) {
        int number = this.fieldInt[y - 1][x + 0];
        this.fieldInt[y - 1][x + 0] = this.fieldInt[y - 1][x + 1];
        this.fieldInt[y - 1][x + 1] = this.fieldInt[y + 0][x + 1];
        this.fieldInt[y + 0][x + 1] = this.fieldInt[y + 1][x + 1];
        this.fieldInt[y + 1][x + 1] = this.fieldInt[y + 1][x - 0];
        this.fieldInt[y + 1][x - 0] = this.fieldInt[y + 1][x - 1];
        this.fieldInt[y + 1][x - 1] = this.fieldInt[y - 0][x - 1];
        this.fieldInt[y - 0][x - 1] = this.fieldInt[y - 1][x - 1];
        this.fieldInt[y - 1][x - 1] = number;
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
        fieldButton[j][i].setText(String.valueOf(this.fieldInt[j][i]));
        if (!(i == selectx && j == selecty)) {
          if (!checkboxmenuitemShowColor.getState()) {
            fieldButton[j][i].setBackground(colorb0);
            fieldButton[j][i].setForeground(colorf0);
          } else if (this.fieldInt[j][i] == i + fieldSizeX * j + 1) {
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
          } else if (this.fieldInt[j][i] == i + fieldSizeX * j + 1) {
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
        this.fieldInt[j][i] = i + fieldSizeX * j + 1;
      }
    }
    refreshFieldButton(isSelectActive);
  }

  public void fieldMix() {
//    if (!doingSlowlyMoves.tryAcquire()) { // TODO: is not working correctly!
//      System.out.println("Could not aquire lock 'doingSlowlyMoves' from function 'fieldMix'!");
//      return;
//    }
    if (!doingSlowlyMoves.tryAcquire()) {
      return;
    }
    System.out.println("Locking semaphore 'doingSlowlyMoves' in fieldMix!");
//    try {
//      doingSlowlyMoves.acquire();
//      System.out.println("fieldMix: aquired lock 'doingSlowlyMoves'!");
//    } catch (Exception e) { e.printStackTrace(); }

//    finishedDoingSlowlyMoves = false;

    ArrayList<Tuple4<Integer, Integer, Integer, Boolean>> movesList = new ArrayList<>();

    Random random = new Random();
    int maxX = fieldSizeX - 2;
    int maxY = fieldSizeY - 2;
    for (int i = 0; i < fieldSizeX * fieldSizeY; i++) {
      int x = 1 + Math.abs(random.nextInt()) % maxX;
      int y = 1 + Math.abs(random.nextInt()) % maxY;
      int r = Math.abs(random.nextInt()) % 4+1;
      boolean c = (Math.abs(random.nextInt() % 2) == 0);

      movesList.add(new Tuple4<>(x, y, r, c));
    }

    doTheMovesSlowly(movesList);

//    try { doingSlowlyMoves.acquire(); } catch (Exception e) { e.printStackTrace(); }
//    System.out.println("fieldMix: release lock 'doingSlowlyMoves'!");
//    doingSlowlyMoves.release();
  }

  private void fieldSolveFirstPart(ArrayList<Tuple4<Integer, Integer, Integer, Boolean>> movesList) {
    if (!semaphoreMovingTableFields.tryAcquire()) {
      return;
    }

    int sizeY = this.fieldSizeY;
    int sizeX = this.fieldSizeX;

    for (int idx = 0; idx < numsOrder.size(); ++idx) {
      int num = numsOrder.get(idx);
      HashTup2ListTup4 mt = currentMovingTable.v.get(num);

      boolean isFoundNum = false;
      for (int y = 0; y < sizeY; ++y) {
        for (int x = 0; x < sizeX; ++x) {
          if (this.fieldInt[y][x] == (num+1)) {
            isFoundNum = true;

            if (!mt.v.containsKey(new Tuple2<>(x, y))) {
              break;
            }

            ArrayList<Tuple4<Integer, Integer, Integer, Boolean>> moves = mt.v.get(new Tuple2<>(x, y));
            for (int i = 0; i < moves.size(); ++i) {
              Tuple4<Integer, Integer, Integer, Boolean> m = moves.get(i);
              rotateFieldNumbers(m);
              movesList.add(m);
            }
            break;
          }
        }
        if (isFoundNum) {
          break;
        }
      }
    }
    semaphoreMovingTableFields.release();
  }

  private void fieldSolveSecondPart(ArrayList<Tuple4<Integer, Integer, Integer, Boolean>> movesList) {
    int sizeX = this.fieldSizeX;

    HashMap<Integer, String> changingNumbers = new HashMap<>();

    for (int y = 0; y < 3; ++y) {
      for (int x = 0; x < 3; ++x) {
        changingNumbers.put(y*sizeX+x, ""+(y*3+x));
      }
    }

    String field9x9 = "";

    for (int y = 0; y < 3; ++y) {
      for (int x = 0; x < 3; ++x) {
        field9x9 += changingNumbers.get(fieldInt[y][x]-1);
      }
    }

    System.out.println("field9x9: "+field9x9);

    if (!semaphoreMovingTable9x9.tryAcquire()) {
      return;
    }

    if (movingTable9x9 == null) {
      semaphoreMovingTable9x9.release();
      return;
    }

    ArrayList<Tuple4<Integer, Integer, Integer, Boolean>> moves = movingTable9x9.get(field9x9);
    moves.forEach(t -> {
      rotateFieldNumbers(t);
      movesList.add(t);
    });

    semaphoreMovingTable9x9.release();
  }

  public void fieldSolve() {
//    if (!doingSlowlyMoves.tryAcquire()) {
//      System.out.println("Could not aquire lock 'doingSlowlyMoves' from function 'fieldSolve'!");
//      return;
//    }
    System.out.println("Locking semaphore 'doingSlowlyMoves' in fieldSolve!");
    if (!doingSlowlyMoves.tryAcquire()) {
      return;
    }
//    try {
//      doingSlowlyMoves.acquire();
//      System.out.println("fieldSolve: aquired lock 'doingSlowlyMoves'!");
//    } catch (Exception e) { e.printStackTrace(); }

    if (currentMovingTable == null) {
      return;
    }

    ArrayList<Tuple4<Integer, Integer, Integer, Boolean>> movesList = new ArrayList<>();

    int[][] fieldIntCopy = new int[fieldSizeY][];
    for (int y = 0; y < fieldSizeY; ++y) {
      fieldIntCopy[y] = Arrays.copyOf(fieldInt[y], fieldSizeX);
    }

    fieldSolveFirstPart(movesList);
    fieldSolveSecondPart(movesList);

    for (int y = 0; y < fieldSizeY; ++y) {
      System.arraycopy(fieldIntCopy[y], 0, fieldInt[y], 0, fieldSizeX);
    }

    doTheMovesSlowly(movesList);

//    try { doingSlowlyMoves.acquire(); } catch (Exception e) { e.printStackTrace(); }
//    System.out.println("fieldSolve: release lock 'doingSlowlyMoves'!");
//    doingSlowlyMoves.release();
  }

  private void doTheMovesSlowly(ArrayList<Tuple4<Integer, Integer, Integer, Boolean>> movesList) {
    (new Thread() {
      RotationPuzzle rp = RotationPuzzle.this;

      private void sleep() {
        try { Thread.sleep(rp.movingSpeedTime); } catch (Exception e) { e.printStackTrace(); }
      }

      private void sleepShort() {
        try { Thread.sleep(1); } catch (Exception e) { e.printStackTrace(); }
      }

      @Override
      public void run() {
        class A {
          public Semaphore sem = new Semaphore(1);
          public boolean finished = false;
          public Tuple4<Integer, Integer, Integer, Boolean> move = null;
          public Color cbBefore = null;
          public Color cfBefore = null;
          public int x = 0;
          public int y = 0;
          public int r = 0;
          public boolean c = false;
        }

        A a = new A();

        Runnable th1 = new Runnable() {
          RotationPuzzle rp = RotationPuzzle.this;

          @Override
          public void run() {
            try { a.sem.acquire(); } catch (Exception e) { }

            rp.fieldButton[a.y][a.x].setBackground(colorb5);
            rp.fieldButton[a.y][a.x].setForeground(colorf5);

            a.finished = true;

            a.sem.release();
          }
        };

        Runnable th2 = new Runnable() {
          RotationPuzzle rp = RotationPuzzle.this;

          @Override
          public void run() {
            int x = a.x;
            int y = a.y;
            boolean c = a.c;

            try { a.sem.acquire(); } catch (Exception e) { }

            rp.rotateFieldNumbers(x, y, 1, c);
            rp.refreshFieldButton(false);
            rp.fieldButton[y][x].setBackground(colorb5);
            rp.fieldButton[y][x].setForeground(colorf5);

            a.finished = true;

            a.sem.release();
          }
        };

        Runnable th3 = new Runnable() {
          RotationPuzzle rp = RotationPuzzle.this;

          @Override
          public void run() {
            try { a.sem.acquire(); } catch (Exception e) { }

            rp.fieldButton[a.y][a.x].setBackground(a.cbBefore);
            rp.fieldButton[a.y][a.x].setForeground(a.cfBefore);

            a.finished = true;

            a.sem.release();
          }
        };

        try { a.sem.acquire(); } catch (Exception e) { }
        movesList.forEach(t -> {
          a.move = t;

          a.x = t.v1;
          a.y = t.v2;
          a.r = t.v3;
          a.c = t.v4;

          a.cbBefore = rp.fieldButton[a.y][a.x].getBackground();
          a.cfBefore = rp.fieldButton[a.y][a.x].getForeground();

          SwingUtilities.invokeLater(th1);
          a.finished = false;
          a.sem.release();
          sleep();

          try { a.sem.acquire(); } catch (Exception e) { }
          while (!a.finished) {
            a.sem.release();
            sleepShort();
            try { a.sem.acquire(); } catch (Exception e) { }
          }

          for (int i = 0; i < a.r; ++i) {
            SwingUtilities.invokeLater(th2);
            a.finished = false;
            a.sem.release();
            sleep();

            try { a.sem.acquire(); } catch (Exception e) { }
            while (!a.finished) {
              a.sem.release();
              sleepShort();
              try { a.sem.acquire(); } catch (Exception e) { }
            }
          }

          SwingUtilities.invokeLater(th3);
          a.finished = false;
          a.sem.release();
          sleep();

          try { a.sem.acquire(); } catch (Exception e) { }
          while (!a.finished) {
            a.sem.release();
            sleepShort();
            try { a.sem.acquire(); } catch (Exception e) { }
          }
        });

        a.sem.release();
        rp.doingSlowlyMoves.release();
      }
    }).start();
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
        new Thread() {
          @Override
          public void run() {
            fieldMix();
          }
        }.start();
        break;
      case 'b':
        new Thread() {
          @Override
          public void run() {
            fieldSolve();
          }
        }.start();
//        fieldSolve();
        break;
      case ',':
        System.out.println("Pressed the ',' key!");
        break;
      case 'q':
        currentMenuItemSizeFieldActionListener.doPrev();
        break;
      case 'w':
        currentMenuItemSizeFieldActionListener.doNext();
        break;
      case 'e':
        currentMenuItemSizeButtonActionListener.doPrev();
        break;
      case 'r':
        currentMenuItemSizeButtonActionListener.doNext();
        break;
      case 't':
        currentMenuItemSolvingSpeedActionListener.doPrev();
        break;
      case 'z':
        currentMenuItemSolvingSpeedActionListener.doNext();
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
