package com.utils.vminformatica;

import java.util.HashMap;

public class HousieTicket {
    private String[][] ticket = new String[3][5];
    private boolean[][] ticketFlag = new boolean[3][5];
    private String ticketId;
    private boolean isFullHouse;
    private String userName;
    
    public static final String QUEENS_CORNER = "Queens Corner";
    public static final String KINGS_CORNER = "Kings Corner";
    public static final String TOP_LINE = "Top Line";
    public static final String MIDDLE_LINE = "Middle Line";
    public static final String BOTTOM_LINE = "Bottom Line";
    public static final String QUICK_7 = "Quick 7";
    public static final String PYRAMID = "Pyramid";
    public static final String STAR = "Star";
    public static final String FIRST_FULL_HOUSE = "First Full House";
    public static final String SECOND_FULL_HOUSE = "Second Full House";
    
    public static HashMap<String, Boolean> HousiePrizesAvailable = new HashMap<String, Boolean>();
    
    static {
        HousiePrizesAvailable.put(QUEENS_CORNER, false);
        HousiePrizesAvailable.put(KINGS_CORNER, false);
        HousiePrizesAvailable.put(TOP_LINE, false);
        HousiePrizesAvailable.put(MIDDLE_LINE, false);
        HousiePrizesAvailable.put(BOTTOM_LINE, false);
        HousiePrizesAvailable.put(QUICK_7, false);
        HousiePrizesAvailable.put(PYRAMID, false);
        HousiePrizesAvailable.put(STAR, false);
        HousiePrizesAvailable.put(FIRST_FULL_HOUSE, false);
        HousiePrizesAvailable.put(SECOND_FULL_HOUSE, false);
    }
    
    HousieTicket(String ticketIdVal, String[][] ticketData) {
        ticketId = ticketIdVal;
        int ticketIdNum = Integer.parseInt(ticketId);
        
        if (ticketIdNum >= 1 && ticketIdNum <= 10 || ticketIdNum >= 27 && ticketIdNum <= 30) {
            userName = "Dinesh";
        } else if (ticketIdNum >= 11 && ticketIdNum <= 26) {
            userName = "Ankit";
        } else {
            userName = "Subra";
        }
        
        isFullHouse = false;
        
        for (int i=0; i < 3; i++) {
            for (int j=0; j < 5; j++) {
                ticket[i][j] = ticketData[i][j];
                ticketFlag[i][j] = false;
            }
        }
    }
    
    public void setTicketFlag(String number) {
        for (int i=0; i < 3; i++) {
            for (int j=0; j < 5; j++) {
                if (ticket[i][j].equals(number)) {
                    ticketFlag[i][j] = true;
                }
            }
        }
    }
    
    public void unsetTicketFlag(String number) {
        for (int i=0; i < 3; i++) {
            for (int j=0; j < 5; j++) {
                if (ticket[i][j].equals(number)) {
                    ticketFlag[i][j] = false;
                }
            }
        }
    }
    
    // last number in each line
    public boolean isQueensCorner() {
        for (int i = 0; i < 3; i++) {
            if (!ticketFlag[i][0]) {
                return false;
            }
        }
        
        return true;
    }

    // first number in each line
    public boolean isKingsCorner() {
        for (int i = 0; i < 3; i++) {
            if (!ticketFlag[i][4]) {
                return false;
            }
        }

        return true;
    }
    
    public boolean isTopLine() {
        for (int j = 0; j < 5; j++) {
            if (!ticketFlag[0][j]) {
                return false;
            }
        }

        return true;
    }
    
    public boolean isMiddleLine() {
        for (int j = 0; j < 5; j++) {
            if (!ticketFlag[1][j]) {
                return false;
            }
        }

        return true;
    }
    
    public boolean isBottomLine() {
        for (int j = 0; j < 5; j++) {
            if (!ticketFlag[2][j]) {
                return false;
            }
        }

        return true;
    }
     
    public boolean isQuick7() {
        int count = 0;
        
        for (int i=0; i < 3; i++) {
            for (int j=0; j < 5; j++) {
               if (ticketFlag[i][j]) {
                   count++;
                   if (count == 7) {
                       return true;
                   }
               }
            }
        }
        
        return false;
    }
    
    // Top Row: 3rd Number; Middle Row: 2nd, 4th Number; Bottom Row: 1st, 3rd, 5th Number
    public boolean isPyramid() {
       int c = 0;
       
        for (int j = 0; j < 5; j++) {
           if (ticketFlag[0][j]) {
               c++;
           }
       }
        
        if (c < 3) {
            return false;
        }
        
        c = 0;
        
        for (int j = 0; j < 5; j++) {
            if (ticketFlag[1][j]) {
                c++;
            }
        }
        
        if (c < 2) {
            return false;
        }
        
        c = 0;
        
        for (int j = 0; j < 5; j++) {
            if (ticketFlag[2][j]) {
                c++;
            }
        }
         
        return c > 0;
    }
    
    // Top Row: 1st, 3rd, 5th Number; Middle Row: All Numbers; Bottom Row: 1st, 3rd, 5th Number
    public boolean isStar() {
        boolean flag = ticketFlag[0][0] & ticketFlag[0][4] & ticketFlag[1][2] &
                ticketFlag[2][0] & ticketFlag[2][4];
        
        return flag;
    }
    
    // all numbers
    public boolean isFullHouse() {
        for (int i=0; i < 3; i++) {
            for (int j=0; j < 5; j++) {
               if (!ticketFlag[i][j]) {
                   return false;
               }
            }
        }
        
        isFullHouse = true;
        return true;
    }
    
    public String getTicketId() {
        return ticketId;
    }
    
    public boolean isFullHouseDone() {
        return isFullHouse;
    }
    
    public String getTicketUser() {
        return userName;
    }
    
    public String getMarkedNumbers() {
        String log = "";
        
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 5; j++) {
                if (ticketFlag[i][j]) {
                    log += (ticket[i][j] + " ");
                }
            }
        }
        
        return log;
    }
}