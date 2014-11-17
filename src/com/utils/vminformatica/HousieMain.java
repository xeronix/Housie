package com.utils.vminformatica;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

public class HousieMain {

    protected Shell shlHousie;
    private Text addedNumber;
    private Text deletedNumber;
    private static Table numberTable;
    
    private static LinkedHashSet <String> numberSet = new LinkedHashSet<String>();
    private Text findNumber;
    private Button btnFind;
    private static Label numberFindLabel;
    
    private static Button btnQueensCorner;
    private static Button btnKingsCornerDone;
    private static Button btnTopLineDone;
    private static Button btnMiddleLine;
    private static Button btnBottomLine;
    private static Button btnQuick7;
    private static Button btnPyramid;
    private static Button btnStar;
    private static Button btnFirstFullHouse;
    private static Button btnSecondFullHouse;
    
    private static ArrayList<HousieTicket> housieTickets = new ArrayList<HousieTicket>();
    
    private static final String housieTicketDataFile = "C:\\tickets.txt";
    private static final String numberSetFile = "C:\\numbers.txt";

    private static StyledText logs;
    
    static {
        readHousieTicketData();
        readHousieNumberSetFile();
    }
    
    public static void readHousieNumberSetFile() {
        BufferedReader br = null;
        
        try {
            File file = new File(numberSetFile);
            
            if (!file.exists()) {
                return;
            }
            
            String line;
            
            br = new BufferedReader(new FileReader(numberSetFile));
 
            while ((line = br.readLine()) != null) {
                if (line.length() > 0) {
                    numberSet.add(line);
                }
            }  
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public static void readHousieTicketData() {
        BufferedReader br = null;
        
        try {
            String line;
            
            br = new BufferedReader(new FileReader(housieTicketDataFile));
 
            while ((line = br.readLine()) != null) {
                String ticketId = line;
                String ticketData[][] = new String[3][5];
                
                for (int i = 0; i < 3; i++) {
                    line = br.readLine().trim();
                    ticketData[i] = line.split(" ");
                    for (int j = 0; j < 5; j++) {
                        ticketData[i][j] = ticketData[i][j].trim();
                    }
                }
                
                HousieTicket housieTicket = new HousieTicket(ticketId, ticketData);
                housieTickets.add(housieTicket);
                
                br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    /**
     * Launch the application.
     * @param args
     */
    public static void main(String[] args) {
        try {
            HousieMain window = new HousieMain();
            window.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Open the window.
     */
    public void open() {
        Display display = Display.getDefault();
        createContents();
        
        Monitor primary = display.getPrimaryMonitor();
        Rectangle bounds = primary.getBounds();
        Rectangle rect = shlHousie.getBounds();
        
        int x = bounds.x + (bounds.width - rect.width) / 2;
        int y = bounds.y + (bounds.height - rect.height) / 2;
        
        shlHousie.setLocation(x, y);
        
        shlHousie.open();
        shlHousie.layout();
        while (!shlHousie.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    public boolean isInteger(String s) {
        try {
           Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Create contents of the window.
     */
    protected void createContents() {
        shlHousie = new Shell(SWT.MIN | SWT.CLOSE | SWT.TITLE);
        shlHousie.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        shlHousie.setSize(688, 604);
        shlHousie.setText("Housie");
        
        addedNumber = new Text(shlHousie, SWT.BORDER);
        addedNumber.setBounds(10, 370, 64, 21);
        
        Button btnNewButton = new Button(shlHousie, SWT.NONE);
        btnNewButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
               numberFindLabel.setText("");
               
               String number = addedNumber.getText().trim();
     
               if (isInteger(number)) {
                   numberSet.add(number);
                   saveNumberSetToFile();
                   updateNumberTable();
                   updateHousieTickets(number, 1);
                   checkHousiePrizes();
                   addedNumber.setText("");
                   addedNumber.setFocus();
               }
            }
        });
        btnNewButton.setBounds(94, 368, 75, 25);
        btnNewButton.setText("Add");
        
        deletedNumber = new Text(shlHousie, SWT.BORDER);
        deletedNumber.setBounds(10, 437, 64, 21);
        
        Button btnNewButton_1 = new Button(shlHousie, SWT.NONE);
        btnNewButton_1.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                numberFindLabel.setText("");
                
                String number = deletedNumber.getText().trim();
                
                if (isInteger(number)) {
                    numberSet.remove(number);
                    saveNumberSetToFile();
                    updateNumberTable();
                    updateHousieTickets(number, 0);
                    reloadHousieTicketsAndPrizes();
                    deletedNumber.setText("");
                    addedNumber.setText("");
                    addedNumber.setFocus();
                }
            }
        });
        btnNewButton_1.setBounds(94, 435, 75, 25);
        btnNewButton_1.setText("Delete");
        
        numberTable = new Table(shlHousie, SWT.BORDER | SWT.FULL_SELECTION);
        numberTable.setBounds(10, 16, 159, 311);
        numberTable.setHeaderVisible(true);
        numberTable.setLinesVisible(true);
        
        Label label = new Label(shlHousie, SWT.SEPARATOR | SWT.VERTICAL);
        label.setBounds(188, 16, 2, 311);
        
        Menu menu = new Menu(shlHousie);
        shlHousie.setMenu(menu);
        
        findNumber = new Text(shlHousie, SWT.BORDER);
        findNumber.setBounds(10, 506, 64, 21);
        
        btnFind = new Button(shlHousie, SWT.NONE);
        btnFind.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                String number = findNumber.getText().trim();
                
                if (isInteger(number)) {
                    if (numberSet.contains(number)) {
                        numberFindLabel.setText("Found");
                    } else {
                        numberFindLabel.setText("Not Found");
                    }
                    
                    findNumber.setText("");
                    addedNumber.setText("");
                    addedNumber.setFocus();
                }
            }
        });
        btnFind.setBounds(94, 504, 75, 25);
        btnFind.setText("Find");
        
        numberFindLabel = new Label(shlHousie, SWT.NONE);
        numberFindLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        numberFindLabel.setBounds(10, 533, 89, 21);
        numberFindLabel.setText("");
        
        btnMiddleLine = new Button(shlHousie, SWT.NONE);
        btnMiddleLine.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                btnMiddleLine.setEnabled(false);
                HousieTicket.HousiePrizesAvailable.put(HousieTicket.MIDDLE_LINE, false);
            }
        });
        btnMiddleLine.setBounds(233, 463, 113, 25);
        btnMiddleLine.setText("Middle Line");
        
        btnQueensCorner = new Button(shlHousie, SWT.NONE);
        btnQueensCorner.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                btnQueensCorner.setEnabled(false);
                HousieTicket.HousiePrizesAvailable.put(HousieTicket.QUEENS_CORNER, false);
            }
        });
        btnQueensCorner.setText("Queens Corner");
        btnQueensCorner.setBounds(233, 370, 113, 25);
        
        btnKingsCornerDone = new Button(shlHousie, SWT.NONE);
        btnKingsCornerDone.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                btnKingsCornerDone.setEnabled(false);
                HousieTicket.HousiePrizesAvailable.put(HousieTicket.KINGS_CORNER, false);
            }
        });
        btnKingsCornerDone.setText("Kings Corner");
        btnKingsCornerDone.setBounds(233, 401, 113, 25);
        
        btnTopLineDone = new Button(shlHousie, SWT.NONE);
        btnTopLineDone.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                btnTopLineDone.setEnabled(false);
                HousieTicket.HousiePrizesAvailable.put(HousieTicket.TOP_LINE, false);
            }
        });
        btnTopLineDone.setText("Top Line");
        btnTopLineDone.setBounds(233, 432, 113, 25);
        
        btnStar = new Button(shlHousie, SWT.NONE);
        btnStar.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                btnStar.setEnabled(false);
                HousieTicket.HousiePrizesAvailable.put(HousieTicket.STAR, false);
            }
        });
        btnStar.setText("Star");
        btnStar.setBounds(444, 432, 113, 25);
        
        btnBottomLine = new Button(shlHousie, SWT.NONE);
        btnBottomLine.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                btnBottomLine.setEnabled(false);
                HousieTicket.HousiePrizesAvailable.put(HousieTicket.BOTTOM_LINE, false);
            }
        });
        btnBottomLine.setText("Bottom Line");
        btnBottomLine.setBounds(233, 494, 113, 25);
        
        btnQuick7 = new Button(shlHousie, SWT.NONE);
        btnQuick7.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                btnQuick7.setEnabled(false);
                HousieTicket.HousiePrizesAvailable.put(HousieTicket.QUICK_7, false);
            }
        });
        btnQuick7.setText("Quick 7");
        btnQuick7.setBounds(444, 370, 113, 25);
        
        btnPyramid = new Button(shlHousie, SWT.NONE);
        btnPyramid.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                btnPyramid.setEnabled(false);
                HousieTicket.HousiePrizesAvailable.put(HousieTicket.PYRAMID, false);
            }
        });
        btnPyramid.setText("Pyramid");
        btnPyramid.setBounds(444, 401, 113, 25);
        
        btnFirstFullHouse = new Button(shlHousie, SWT.NONE);
        btnFirstFullHouse.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                btnFirstFullHouse.setEnabled(false);
                HousieTicket.HousiePrizesAvailable.put(HousieTicket.FIRST_FULL_HOUSE, false);
            }
        });
        btnFirstFullHouse.setText("First Full House");
        btnFirstFullHouse.setBounds(444, 463, 113, 25);
        
        btnSecondFullHouse = new Button(shlHousie, SWT.NONE);
        btnSecondFullHouse.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                btnSecondFullHouse.setEnabled(false);
                HousieTicket.HousiePrizesAvailable.put(HousieTicket.SECOND_FULL_HOUSE, false);
            }
        });
        btnSecondFullHouse.setText("Second Full House");
        btnSecondFullHouse.setBounds(444, 494, 113, 25);
        
        Button btnQueensCornerReset = new Button(shlHousie, SWT.NONE);
        btnQueensCornerReset.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                btnQueensCorner.setEnabled(true);
                HousieTicket.HousiePrizesAvailable.put(HousieTicket.QUEENS_CORNER, true);
            }
        });
        btnQueensCornerReset.setBounds(352, 370, 57, 25);
        btnQueensCornerReset.setText("Reset");
        
        Button btnReset = new Button(shlHousie, SWT.NONE);
        btnReset.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                btnKingsCornerDone.setEnabled(true);
                HousieTicket.HousiePrizesAvailable.put(HousieTicket.KINGS_CORNER, true);
            }
        });
        btnReset.setText("Reset");
        btnReset.setBounds(352, 399, 57, 25);
        
        Button btnReset_1 = new Button(shlHousie, SWT.NONE);
        btnReset_1.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                btnTopLineDone.setEnabled(true);
                HousieTicket.HousiePrizesAvailable.put(HousieTicket.TOP_LINE, true);
            }
        });
        btnReset_1.setText("Reset");
        btnReset_1.setBounds(352, 433, 57, 25);
        
        Button btnReset_2 = new Button(shlHousie, SWT.NONE);
        btnReset_2.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                btnMiddleLine.setEnabled(true);
                HousieTicket.HousiePrizesAvailable.put(HousieTicket.MIDDLE_LINE, true);
            }
        });
        btnReset_2.setText("Reset");
        btnReset_2.setBounds(352, 463, 57, 25);
        
        Button btnReset_3 = new Button(shlHousie, SWT.NONE);
        btnReset_3.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                btnBottomLine.setEnabled(true);
                HousieTicket.HousiePrizesAvailable.put(HousieTicket.BOTTOM_LINE, true);
            }
        });
        btnReset_3.setText("Reset");
        btnReset_3.setBounds(352, 494, 57, 25);
        
        Button btnResetn = new Button(shlHousie, SWT.NONE);
        btnResetn.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                btnQuick7.setEnabled(true);
                HousieTicket.HousiePrizesAvailable.put(HousieTicket.QUICK_7, true);
            }
        });
        btnResetn.setText("Reset");
        btnResetn.setBounds(563, 370, 59, 25);
        
        Button btnReset_4 = new Button(shlHousie, SWT.NONE);
        btnReset_4.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                btnPyramid.setEnabled(true);
                HousieTicket.HousiePrizesAvailable.put(HousieTicket.PYRAMID, true);
            }
        });
        btnReset_4.setText("Reset");
        btnReset_4.setBounds(563, 399, 59, 25);
        
        Button btnReset_5 = new Button(shlHousie, SWT.NONE);
        btnReset_5.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                btnStar.setEnabled(true);
                HousieTicket.HousiePrizesAvailable.put(HousieTicket.STAR, true);
            }
        });
        btnReset_5.setText("Reset");
        btnReset_5.setBounds(563, 430, 59, 25);
        
        Button btnReset_6 = new Button(shlHousie, SWT.NONE);
        btnReset_6.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                btnFirstFullHouse.setEnabled(true);
                HousieTicket.HousiePrizesAvailable.put(HousieTicket.FIRST_FULL_HOUSE, true);
            }
        });
        btnReset_6.setText("Reset");
        btnReset_6.setBounds(563, 461, 59, 25);
        
        Button btnReset_7 = new Button(shlHousie, SWT.NONE);
        btnReset_7.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                btnSecondFullHouse.setEnabled(true);
                HousieTicket.HousiePrizesAvailable.put(HousieTicket.SECOND_FULL_HOUSE, true);
            }
        });
        btnReset_7.setText("Reset");
        btnReset_7.setBounds(563, 494, 59, 25);
        
        Label lblNewLabel = new Label(shlHousie, SWT.NONE);
        lblNewLabel.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.BOLD));
        lblNewLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        lblNewLabel.setBounds(385, 343, 119, 21);
        lblNewLabel.setText("Prize Status");
        
        logs = new StyledText(shlHousie, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
        logs.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.BOLD));
        logs.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        logs.setEditable(false);
        logs.setBounds(233, 26, 389, 301);
        
        updateNumberTable();
        reloadHousieTicketsAndPrizes();
    }
    
    public synchronized static void saveNumberSetToFile() {
        Display.getDefault().asyncExec(new Runnable() {
            public void run() {
                try {
                    File file = new File(numberSetFile);
         
                    // if file doesn't exists, then create it
                    if (!file.exists()) {
                        file.createNewFile();
                    }
         
                    FileWriter fw = new FileWriter(file.getAbsoluteFile());
                    BufferedWriter bw = new BufferedWriter(fw);
                    
                    for(String number: numberSet) {
                        bw.write(number + "\n");
                    }
                    
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    public synchronized static void updateNumberTable() {
        Display.getDefault().asyncExec(new Runnable() {
            public void run() {
                numberTable.removeAll();
                
                if (!numberSet.isEmpty()) {
                    for (String number : numberSet) {
                        TableItem item = new TableItem(numberTable, SWT.NONE);
                        item.setText(number);
                    }
                }
                
                numberTable.setTopIndex(numberSet.size()-1);
            }
        });
    }
    
    public synchronized static void updateHousieTickets(String number, int add_delete_flag) {
        final String housieNumber = number;
        final int flag = add_delete_flag;

        for (HousieTicket housieTicket : housieTickets) {
            if (flag == 1) {
                housieTicket.setTicketFlag(housieNumber);
            } else {
                housieTicket.unsetTicketFlag(housieNumber);
            }
        }
    }
    
    public synchronized static void updateLogs(String message) {
        final String logMessage = message;
        
        Display.getDefault().asyncExec(new Runnable() {
            public void run() {                  
                int cursorPos = logs.getCharCount();
                Color color = SWTResourceManager.getColor(SWT.COLOR_GRAY);    
                StyleRange range = new StyleRange(0, cursorPos, color, null); 
                logs.setStyleRange(range);
                
                logs.append(logMessage);
                logs.setSelection(logs.getText().length());

               /* cursorPos = logs.getCharCount();
                color = SWTResourceManager.getColor(SWT.COLOR_BLACK);    
                range = new StyleRange(cursorPos-logMessage.length(), cursorPos, color, null);   
                logs.setStyleRange(range);*/
              }
        });
    }
    
    public synchronized static void checkHousiePrizes() {

        String logMessage = "";

        for (HousieTicket housieTicket : housieTickets) {
            if (HousieTicket.HousiePrizesAvailable.get(HousieTicket.QUEENS_CORNER)) {
                if (housieTicket.isQueensCorner()) {
                    HousieTicket.HousiePrizesAvailable.put(HousieTicket.QUEENS_CORNER, false);
                    btnQueensCorner.setEnabled(false);
                    logMessage += housieTicket.getTicketUser() + " : " + "Ticket " + housieTicket.getTicketId() + " : " + HousieTicket.QUEENS_CORNER
                            + "\n";
                }
            }

            if (HousieTicket.HousiePrizesAvailable.get(HousieTicket.KINGS_CORNER)) {
                if (housieTicket.isKingsCorner()) {
                    HousieTicket.HousiePrizesAvailable.put(HousieTicket.KINGS_CORNER, false);
                    btnKingsCornerDone.setEnabled(false);
                    logMessage += housieTicket.getTicketUser() + " : " + "Ticket " + housieTicket.getTicketId() + " : " + HousieTicket.KINGS_CORNER
                            + "\n";
                }
            }

            if (HousieTicket.HousiePrizesAvailable.get(HousieTicket.TOP_LINE)) {
                if (housieTicket.isTopLine()) {
                    HousieTicket.HousiePrizesAvailable.put(HousieTicket.TOP_LINE, false);
                    btnTopLineDone.setEnabled(false);
                    logMessage += housieTicket.getTicketUser() + " : " + "Ticket " + housieTicket.getTicketId() + " : " + HousieTicket.TOP_LINE + "\n";
                }
            }

            if (HousieTicket.HousiePrizesAvailable.get(HousieTicket.MIDDLE_LINE)) {
                if (housieTicket.isMiddleLine()) {
                    HousieTicket.HousiePrizesAvailable.put(HousieTicket.MIDDLE_LINE, false);
                    btnMiddleLine.setEnabled(false);
                    logMessage += housieTicket.getTicketUser() + " : " + "Ticket " + housieTicket.getTicketId() + " : " + HousieTicket.MIDDLE_LINE
                            + "\n";
                }
            }

            if (HousieTicket.HousiePrizesAvailable.get(HousieTicket.BOTTOM_LINE)) {
                if (housieTicket.isBottomLine()) {
                    HousieTicket.HousiePrizesAvailable.put(HousieTicket.BOTTOM_LINE, false);
                    btnBottomLine.setEnabled(false);
                    logMessage += housieTicket.getTicketUser() + " : " + "Ticket " + housieTicket.getTicketId() + " : " + HousieTicket.BOTTOM_LINE
                            + "\n";
                }
            }

            if (HousieTicket.HousiePrizesAvailable.get(HousieTicket.QUICK_7)) {
                if (housieTicket.isQuick7()) {
                    HousieTicket.HousiePrizesAvailable.put(HousieTicket.QUICK_7, false);
                    btnQuick7.setEnabled(false);
                    logMessage += housieTicket.getTicketUser() + " : " + "Ticket " + housieTicket.getTicketId() + " : " + HousieTicket.QUICK_7 + "\n";
                    logMessage += housieTicket.getMarkedNumbers() + "\n";
                }
            }

            if (HousieTicket.HousiePrizesAvailable.get(HousieTicket.PYRAMID)) {
                if (housieTicket.isPyramid()) {
                    HousieTicket.HousiePrizesAvailable.put(HousieTicket.PYRAMID, false);
                    btnPyramid.setEnabled(false);
                    logMessage += housieTicket.getTicketUser() + " : " + "Ticket " + housieTicket.getTicketId() + " : " + HousieTicket.PYRAMID + "\n";
                }
            }

            if (HousieTicket.HousiePrizesAvailable.get(HousieTicket.STAR)) {
                if (housieTicket.isStar()) {
                    HousieTicket.HousiePrizesAvailable.put(HousieTicket.STAR, false);
                    btnStar.setEnabled(false);
                    logMessage += housieTicket.getTicketUser() + " : " + "Ticket " + housieTicket.getTicketId() + " : " + HousieTicket.STAR + "\n";
                }
            }

            if (HousieTicket.HousiePrizesAvailable.get(HousieTicket.FIRST_FULL_HOUSE)) {
                if (housieTicket.isFullHouse()) {
                    HousieTicket.HousiePrizesAvailable.put(HousieTicket.FIRST_FULL_HOUSE, false);
                    btnFirstFullHouse.setEnabled(false);
                    logMessage += housieTicket.getTicketUser() + " : " + "Ticket " + housieTicket.getTicketId() + " : " + HousieTicket.FIRST_FULL_HOUSE
                            + "\n";
                }
            } else if (HousieTicket.HousiePrizesAvailable.get(HousieTicket.SECOND_FULL_HOUSE)) {
                // full house can be achieved only once per ticket
                if (!housieTicket.isFullHouseDone() &&  housieTicket.isFullHouse()) {
                    HousieTicket.HousiePrizesAvailable.put(HousieTicket.SECOND_FULL_HOUSE, false);
                    btnSecondFullHouse.setEnabled(false);
                    logMessage += housieTicket.getTicketUser() + " : " + "Ticket " + housieTicket.getTicketId() + " : "
                            + HousieTicket.SECOND_FULL_HOUSE + "\n";
                }
            }
        }

        updateLogs(logMessage);
    }
    
    public synchronized static void reloadHousieTicketsAndPrizes() {
        Display.getDefault().asyncExec(new Runnable() {
            public void run() {
                boolean isQueensCornerAvailable = true;
                boolean isKingsCornerAvailable = true;
                boolean isTopLineAvailable = true;
                boolean isMiddleLineAvailable = true;
                boolean isBottomLineAvailable = true;
                boolean isQuick7Available = true;
                boolean isPyramidAvailable = true;
                boolean isStarAvailable = true;
                boolean isFirstFullHouseAvailable = true;
                boolean isSecondFullHouseAvailable = true;

                for (HousieTicket housieTicket : housieTickets) {
                    if (!numberSet.isEmpty()) {
                        for (String number : numberSet) {
                            housieTicket.setTicketFlag(number);
                        }
                    }

                    isQueensCornerAvailable &= (!housieTicket.isQueensCorner());
                    isKingsCornerAvailable &= (!housieTicket.isKingsCorner());
                    isTopLineAvailable &= (!housieTicket.isTopLine());
                    isMiddleLineAvailable &= (!housieTicket.isMiddleLine());
                    isBottomLineAvailable &= (!housieTicket.isBottomLine());
                    isQuick7Available &= (!housieTicket.isQuick7());
                    isPyramidAvailable &= (!housieTicket.isPyramid());
                    isStarAvailable &= (!housieTicket.isStar());
                    
                    if (housieTicket.isFullHouse()) {
                        if (isFirstFullHouseAvailable) {
                            isFirstFullHouseAvailable = false;
                        } else {
                            isSecondFullHouseAvailable &= false;
                        }
                    }
                }

                HousieTicket.HousiePrizesAvailable.put(HousieTicket.QUEENS_CORNER, isQueensCornerAvailable);
                btnQueensCorner.setEnabled(isQueensCornerAvailable);

                HousieTicket.HousiePrizesAvailable.put(HousieTicket.KINGS_CORNER, isKingsCornerAvailable);
                btnKingsCornerDone.setEnabled(isKingsCornerAvailable);

                HousieTicket.HousiePrizesAvailable.put(HousieTicket.TOP_LINE, isTopLineAvailable);
                btnTopLineDone.setEnabled(isTopLineAvailable);

                HousieTicket.HousiePrizesAvailable.put(HousieTicket.MIDDLE_LINE, isMiddleLineAvailable);
                btnMiddleLine.setEnabled(isMiddleLineAvailable);

                HousieTicket.HousiePrizesAvailable.put(HousieTicket.BOTTOM_LINE, isBottomLineAvailable);
                btnBottomLine.setEnabled(isBottomLineAvailable);

                HousieTicket.HousiePrizesAvailable.put(HousieTicket.QUICK_7, isQuick7Available);
                btnQuick7.setEnabled(isQuick7Available);

                HousieTicket.HousiePrizesAvailable.put(HousieTicket.PYRAMID, isPyramidAvailable);
                btnPyramid.setEnabled(isPyramidAvailable);

                HousieTicket.HousiePrizesAvailable.put(HousieTicket.STAR, isStarAvailable);
                btnStar.setEnabled(isStarAvailable);

                HousieTicket.HousiePrizesAvailable.put(HousieTicket.FIRST_FULL_HOUSE, isFirstFullHouseAvailable);
                btnFirstFullHouse.setEnabled(isFirstFullHouseAvailable);

                HousieTicket.HousiePrizesAvailable.put(HousieTicket.SECOND_FULL_HOUSE, isSecondFullHouseAvailable);
                btnSecondFullHouse.setEnabled(isSecondFullHouseAvailable);
            }
        });
    }
}
