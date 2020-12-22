import java.io.*;
import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import javax.imageio.*;
import java.util.*;


public class EcoSchool2018 {

    public static void main(String[] args) {
        EcoDatabase saintJosephSS = new EcoDatabase();
        Scanner keyIn = new Scanner(System.in);
        int choice;
        while (true) {
            //Menu 1: Asks to load file or exit
            do {
                System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------");
                System.out.println("DMZ EcoSchool Data Report (c) 2018");
                System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------");
                System.out.println("1. Load File");
                System.out.println("2. Exit Program");
                System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------");
                try {
                    choice = Integer.parseInt(keyIn.nextLine());
                } catch (NumberFormatException e) {
                    choice = 0;
                }
                if (choice != 1 && choice != 2)
                    System.err.printf("INVALID INPUT!%n");
            } while (choice != 1 && choice != 2);

            //Menu 2: Asks what to do with data
            switch (choice) {
                case 1:
                    boolean success = saintJosephSS.askFile();
                    if (!success)
                        break;
                    else
                        System.out.printf("The File '%s' Has Loaded Successfully...%n", EcoDatabase.getFileName());
                    do {
                        do {
                            // Menu 2
                            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------");
                            System.out.printf("DMZ EcoSchool Data Report (c) 2018 - %s%n", EcoDatabase.getFileDir());
                            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------");
                            System.out.printf("1. Show Floor Map%n2. Search Room by # or ID%n3. Display All Rooms%n");
                            System.out.printf("4. Edit Room Data%n5. Save File As...%n6. Exit File%n");
                            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------");
                            try {
                                choice = Integer.parseInt(keyIn.nextLine());
                            } catch (NumberFormatException e) {
                                choice = 0;
                            }
                            if (!(choice > 0 && choice < 7))
                                System.err.printf("INVALID INPUT!%n");
                        } while (!(choice > 0 && choice < 7));

                        switch (choice) {
                            case 1:
                                try {
                                    EcoDatabase.showMap();
                                    break;
                                } catch (IOException e) {
                                    System.out.println("Sorry, it appears the map file has been moved or deleted.");
                                }
                            case 2:
                                //Submenu: Find room
                                System.out.println("What room would you like to find?");
                                Room room = saintJosephSS.searchRoom(keyIn.nextLine());
                                if (room == null)
                                    System.out.println("The room you have searched for is not available");
                                else
                                    saintJosephSS.displayInfo(room);
                                break;
                            case 3:
                                saintJosephSS.displayAllInfo();
                                break;
                            case 4:
                                saintJosephSS.editRoom();
                                break;
                            case 5:
                                saintJosephSS.saveFile();
                                break;
                            default:
                                System.out.printf("Returning to Main Menu...%n"); // clear array list
                        }
                        System.out.printf("%nPRESS ENTER TO CONTINUE...");
                        keyIn.nextLine();
                    } while (choice != 6);
                    break;
                default:
                    System.err.printf("Exitting Program... Thank You For Using DMZ's EcoSchool Data Report%n");
                    return;
            }
        }
    }
}

class EcoDatabase {
    private ArrayList<Room> rooms = new ArrayList<Room>();
    private static String fileName = null;
    private static String fileDir = null;
    
    public static String getFileDir(){ return fileDir; }
    public static String getFileName(){ return fileName; }
    public void setFileName(String fileName){ this.fileName = fileName; }

    boolean askFile() {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter the name of the file to open:(enter blank to open default of input.csv)");
        try {
            String fileName = in.nextLine().trim();
            if (fileName.equals("")) {
                System.out.println("input.csv");
                fileName = "input.csv";
            }
            loadFile(fileName);
        } catch (InputMismatchException e) {
            System.err.println("Sorry, the file provided was corrupt.");
            return false;
        } catch (IOException e) {
            System.err.println("Sorry, the file was not available.");
            return false;
        }
        return true;
    }


    private void loadFile(String fileName) throws IOException, InputMismatchException {
        boolean add = false;
        ArrayList<Room> newRooms = new ArrayList<Room>();
        File file = new File(fileName);
        Scanner in = new Scanner(file);
        in.useDelimiter(",");
        Room room = new Room("");
        boolean first = true;
        while (in.hasNextLine()) {
            String string = nextGood(in);
            if (string.equals("*") || first) {
                first = false;
                newRooms.add(room);
                room = new Room(nextGood(in));
            } else {
                String name = string;
                int number = Integer.parseInt(nextGood(in));
                double kilowatts = Double.parseDouble(nextGood(in));
                int hours = Integer.parseInt(nextGood(in));
                if (number > 0)
                    room.addItem(name, number, kilowatts, hours);
            }


        }
        newRooms.add(room);
        rooms.addAll(newRooms);
        this.fileName = fileName;
        fileDir = String.format("%s\\%s", file.getAbsoluteFile().getParent(), file);
    }


    String nextGood(Scanner in) {
        boolean good = false;
        String ret = "";
        while (!good) {
            if (in.hasNext()) {
                String input = in.next();
                input = input.replaceAll(System.getProperty("line.separator"), "");
                if (!input.equals(""))
                    good = true;
                ret = input;
            } else {
                return null;
            }
        }
        return ret;
    }

    void addRoom(Room room) {
        rooms.add(room);
    }

    public static void showMap() throws IOException {
        System.out.printf("Which Floor? (Alt + F4 Exits Floor Map Window)%n%n1. Floor 1%n2. Floor 2%n3. Floor 3%n");
        Scanner keyIn = new Scanner(System.in);
        int choice = 0;
        do {
            String response = keyIn.nextLine();
            try {
                choice = Integer.parseInt(response);
            } catch (NumberFormatException e) {
                choice = 0;
            }
            if (!(choice >= 1 && choice <= 3))
                System.err.println("INVALID INPUT");
        } while (!(choice >= 1 && choice <= 3));

        JLabel label = new JLabel();
        JFrame frame = new JFrame();
        BufferedImage loadedImage;
        frame.setLayout(new FlowLayout());
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);
        frame.setSize(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
        double screenRatio = (double) Toolkit.getDefaultToolkit().getScreenSize().width / Toolkit.getDefaultToolkit().getScreenSize().height;
        switch (choice) {
            default:
                loadedImage = ImageIO.read(new File("Maps\\MAP1.floor"));
                break;
            case 2:
                loadedImage = ImageIO.read(new File("Maps\\MAP2.floor"));
                break;
            case 3:
                loadedImage = ImageIO.read(new File("Maps\\MAP3.floor"));
                break;
        }
        if (screenRatio > 16 / 9d) // (ie. 21:9)
            label.setIcon(new ImageIcon(loadedImage.getScaledInstance((int) (16 * frame.getHeight() / 9d), frame.getHeight(), Image.SCALE_DEFAULT)));
        else if (screenRatio < 16 / 9d) // (ie 4:3)
            label.setIcon(new ImageIcon(loadedImage.getScaledInstance(frame.getWidth(), (int) (9 * frame.getWidth() / 16d), Image.SCALE_DEFAULT)));
        else
            label.setIcon(new ImageIcon(loadedImage.getScaledInstance(frame.getWidth(), frame.getHeight(), Image.SCALE_DEFAULT)));
        frame.add(label);
        frame.setVisible(true);
    } // end of method showMap

    Room searchRoom(String roomID) {
        //find room
        //find miscellaneous place (e.g. hallways)
        for (Room room : rooms) {
            if (room.getRoomID().equals(roomID)) {
                return room;
            }
        }
        return null;
    }

    /**********************************************
     * @Author: Carlos Capili Date:6/7/2018
     * @return: void     @param:int
     *
     * Responsible for displaying the appropriate
     * room based on user input.
     *
     * **********************************************/

    void displayInfo(Room room) {
        System.out.println("=========================================================================================================");
        System.out.println("----------------------------------------------------------------------------------------------------");
        System.out.printf("Room: %s\n", room.getRoomID());
        System.out.println("----------------------------------------------------------------------------------------------------");
        System.out.println("Items \t|\tWattage\n");
        room.displayItems();
        System.out.printf("Daily Kilowatt Consumption: %.2f%n",room.calcDailyKiloWatts());
        System.out.printf("Annual Kilowatt Consumption: %.2f%n",8*30*room.calcDailyKiloWatts());
        System.out.printf("Kilograms of CO2e: %.2f%n",8*30*180*room.calcDailyKiloWatts());
        System.out.printf("Trees consumed: %.2f%n",8*30*180*room.calcDailyKiloWatts()/12);
        System.out.println();
        System.out.println("Compared to the rest of the school...");
        calcStats();
        System.out.println("=========================================================================================================");
    } // end of displayInfo

    void calcStats() {
        double total = 0;
        for (Room room : rooms) {
            total += room.calcDailyKiloWatts();
        }
        System.out.printf("Every day, the kilowatt consumption is: %.2f, for a total annual consumption of %.2f.kWh%n"
                , total, 8 * 30 * total);
        System.out.printf("According to Environment Canada, 1kWh of electricity is equal to emitting " +
                "0.180 kg CO2e. So St. Joseph produces %fkg CO2e.%n", total * 8 * 30 * 0.180);
        System.out.println("According to the UNEP, the average tree absorbs 12kg of CO2e per year.");
        System.out.printf("Therefore, about %.0f of trees are needed to absorbs St. Joseph's energy consumption.%n",
                total * 8 * 30 * 0.180 / 12);
    }

    void saveFile() {
        boolean first = true;
        try {
          PrintWriter write = new PrintWriter("newData.txt");
            for (int i = 0; i < rooms.size(); i++) {
                Room room = rooms.get(i);
                write.print("*");
                write.print(room.getRoomID());
                write.print(",");
                for (String[] item : room.getItems()) {
                    write.print(item[0]);
                    write.print(",");
                    write.print(item[1]);
                    write.print(",");
                    write.print(item[2]);
                    write.print(",");
                    write.print(item[3]);
                    write.print(",");
                }
                if(i<rooms.size()-1)
                    write.print(",");
            }
        } catch (IOException e) {
            System.out.println("Oops! An error occurred during the save. Please try again.");

        }
        //Add these lines of code once the user can select a file name to save as
        //fileName = "the new fileName";
        //System.out.printf("File Saved to ''", fileName);
        //fileDir = String.format("%s\\%s", file.getAbsoluteFile().getParent(), file);
    }

    void editRoom() {
        rooms.add(new Room(""));
        Scanner keyIn = new Scanner(System.in);
        System.out.println("Which Room Would You Like to Edit?");
        int indexSelect = 0;
        boolean roomExists = false;
        do {
            String response = keyIn.nextLine().trim();
            for (int index = 0; index < rooms.size(); index++) {
                if (response.equals(rooms.get(index).getRoomID())) {
                    roomExists = true;
                    indexSelect = index;
                }
            }
            if (!roomExists)
                System.err.println("ERROR: THE REQUESTED ROOM DOES NOT EXIST!");
        } while (!roomExists);

        int choice;
        do {
            System.out.printf("Editing Room: %s%n%n", rooms.get(indexSelect).getRoomID());
            System.out.printf("1. Change Room ID%n2. List All Items%n3. Add Item%n4. Delete Item%n5. Clear Items%n6. Save Changes & Exit Room%n");
            do {
                try {
                    choice = Integer.parseInt(keyIn.nextLine());
                } catch (NumberFormatException e) {
                    choice = 0;
                }
                if (!(choice <= 6 && choice >= 0))
                    System.err.println("INVALID INPUT!");
            } while (!(choice <= 6 && choice >= 0));

            switch (choice) {
                case 1:
                    rooms.get(indexSelect).setRoomID("TEMP");
                    System.out.println("Please enter a new Room ID:");
                    boolean duplicateRoomID;
                    String roomID;
                    do{
                      duplicateRoomID = false;
                      roomID = keyIn.nextLine().trim();
                      for (int count = 0; count < rooms.size(); count++){
                        if (roomID.equals(rooms.get(count).getRoomID()))
                          duplicateRoomID = true;
                      }
                      if (duplicateRoomID)
                        System.err.printf("ERROR: THE ROOM NAME '%s' IS ALREADY TAKEN!%n", roomID);
                    } while (duplicateRoomID);
                    rooms.get(indexSelect).setRoomID(roomID);
                    System.out.printf("Room ID Changed to '%s' Successfully...%n", roomID);
                    break;
                case 2:
                    System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------");
                    System.out.printf("%-50s%-12s%-12s%-12s%n", "Item", "Quantity", "kW", "Hrs Active");
                    System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------");
                    for (int index = 0; index < rooms.get(indexSelect).getItems().size(); index++){
                      System.out.printf("%-50s%-12s%-12s%-12s%n", rooms.get(indexSelect).getItems().get(index)[0],
                                        rooms.get(indexSelect).getItems().get(index)[1],
                                        rooms.get(indexSelect).getItems().get(index)[2],
                                        rooms.get(indexSelect).getItems().get(index)[3]);
                    }
                    System.out.printf("-------------------------------------------------------------------------------------------------------------------------------------------------------%n");
                    break;
                case 3:
                    System.out.println("Item Name:");
                    String itemName = keyIn.nextLine();
                    System.out.println("Quantity:");
                    int quantity = -1;
                    double kWatts = -1.0, hours = -1.0;
                    do{
                      try{
                        quantity = Integer.parseInt(keyIn.nextLine());
                      } catch (NumberFormatException e){ quantity = -1; }
                      if (quantity < 0)
                        System.err.println("ERROR: INVALID INPUT");
                    } while (quantity < 0);
                    System.out.println("KiloWatts:");
                    do{
                      try{
                        kWatts = Double.valueOf(keyIn.nextLine());
                      } catch (NumberFormatException e){ kWatts = -1.0; }
                      if (kWatts < 0)
                        System.err.println("ERROR: INVALID INPUT");
                    } while (kWatts < 0);
                    System.out.println("Hours:");
                    do{
                      try{
                        hours = Double.valueOf(keyIn.nextLine());
                      } catch (NumberFormatException e){ hours = -1.0; }
                      if (hours < 0)
                        System.err.println("ERROR: INVALID INPUT");
                    } while (hours < 0);
                    rooms.get(indexSelect).addItem(itemName, quantity, kWatts, hours);
                    System.out.printf("%s Added Successfully...%n", itemName);
                    break;
                case 4:
                    System.out.printf("Delete Which Item?%n%n");
                    System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------");
                    System.out.printf("%-50s%-12s%-12s%-12s%n", "Item", "Quantity", "kW", "Hrs Active");
                    System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------");
                    for (int index = 0; index < rooms.get(indexSelect).getItems().size(); index++){
                      System.out.printf("%d. %-50s%-12s%-12s%-12s%n", index+1,
                                        rooms.get(indexSelect).getItems().get(index)[0],
                                        rooms.get(indexSelect).getItems().get(index)[1],
                                        rooms.get(indexSelect).getItems().get(index)[2],
                                        rooms.get(indexSelect).getItems().get(index)[3]);
                    }
                    System.out.printf("-------------------------------------------------------------------------------------------------------------------------------------------------------%n");
                    do{
                      try{
                        choice = Integer.parseInt(keyIn.nextLine());
                      } catch (NumberFormatException e){ choice = 0; }
                      if (!(choice <= rooms.get(indexSelect).getItems().size() && choice >= 1))
                        System.err.println("ERROR: INVALID INPUT");
                    } while (!(choice <= rooms.get(indexSelect).getItems().size() && choice >= 1));
                    System.out.printf("%s Deleted Successfully...%n", rooms.get(indexSelect).getItems().get(choice-1)[0]);
                    rooms.get(indexSelect).getItems().remove(choice-1);
                    break;
                case 5:
                    rooms.get(indexSelect).getItems().clear();
                    System.out.println("All Items Removed Successfully...");
                    break;
            }
            if (choice != 6){
              System.out.printf("%nPRESS ENTER TO CONTINUE...");
              keyIn.nextLine();
            }
        } while (choice != 6);
        System.out.printf("Changes to %s Saved Successfully...%n", rooms.get(indexSelect).getRoomID());
    } // end of method editRoom

    public void displayAllInfo() {
        for (Room room : rooms) {
            displayInfo(room);
        }
    }
}

class Room {
    private ArrayList<String[]> items = new ArrayList<String[]>();


    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public Room(String roomID) {
        this.roomID = roomID;
    }

    private String roomID;

    public ArrayList<String[]> getItems() {
        return items;
    }


    public void setItems(ArrayList<String[]> items) {
        this.items = items;
    }


    private void addItem(String[] info) {
        items.add(info);
    }

    void addItem(String name, int amount, double kilowatts, double hours) {
        String[] add = {name, String.valueOf(amount), String.valueOf(kilowatts), String.valueOf(hours)};
        addItem(add);
    }

    double calcKilowatts() {
        double total = 0.0;
        for (String[] item : items) {
            total += Integer.parseInt(item[1]) * Integer.parseInt(item[2]);
        }
        return total;
    }

    double calcDailyKiloWatts() {
        double total = 0.0;
        for (String[] item : items) {
            total += Integer.parseInt(item[1]) * Double.parseDouble(item[2]) * Double.parseDouble(item[3]);
        }
        return total;
    }

    void displayItems() {
        for (int increase = 0; increase <= items.size() - 1; increase++) {
            System.out.printf("%d. %s \t\t%s\n", increase + 1, items.get(increase)[0], items.get(increase)[1]);
        }
    }// end of displayItems
}
