
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import inventory.*;
import list.ListException;

public class InventoryApp {
    private Inventory inventory = new Inventory();
    private String nextLine;
    private BufferedReader stdin = new BufferedReader(
                           new InputStreamReader(System.in));

    // Read user input
    private void readInput(String prompt) {
        System.out.print(prompt);
        try {
            nextLine = stdin.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Display the inventory information for a specified title.
    // Precondtions: None.
    // Postcondtion: If the title is not currently in the inventory, print error
    //               message; otherwise, print the inventory information for the
    //               specified title.
    //
    private void displayVideoInfo(String title){
        StockItem stockItem = inventory.findStockItem(title);
        if (stockItem != null)
            inventory.displayStockItemInfo(stockItem);
        else
            System.out.println("Stock item not found! (title: " + title + ")");
    }

    // Add a new title to the inventory.
    // Precondtions: None.
    // Postcondtion: If the title is not currently in the inventory, add it
    //               into the inventory, and set up the initial want value
    //               for that title.
    //
    private void addNewTitle(String title){
        StockItem stockItem = inventory.findStockItem(title);

        if (stockItem != null) {
            System.out.println("-- Title already exists! Use M <title> " +
                               "to modify want value.");
            return;
        } else stockItem = new StockItem(title);

        readInput("Input the initial want value for stock item \"" + title + "\": ");
        int want;

        try {
            want = Integer.parseInt(nextLine);
        } catch (Exception e) {
            System.out.println("** Invalid input: the initial want " +
                    "value is set to zero!");
            want = 0;
        }
        stockItem.setWant(want);
        inventory.insertStockItem(stockItem);
        System.out.println("-- A new title (" + title +
             ") with the initial want value of " + want + " is added!");
    }

    // Modify the want value for a specified title.
    // Preconditions: None.
    // Postcondition: Prompt the user to input the want value for the specified
    //                title. If the input is valid, the want value for the title
    //                is modified. Otherwise, the want value is set to zero.
    //
    private void modifyWantValue(String title) {

        // ==> 1. Add your code here!
        // System.out.println("-- To be implemented!"); // comment this statement!

        // Find the StockItem that the user is attempting to modify.
        StockItem item = inventory.findStockItem(title);

        // Ensure the title exists.
        if(item == null) {
            // Give the user an error message.
            System.out.printf("Title \"%s\" cannot be found!\n", title);

            // Exit the method without continuing.
            return;
        }

        // Prompt the user to enter the want value for the specific title.
        readInput(String.format("Input the want value for stock item \"%s\": ", title));

        // Try to parse the input as an integer and store the result.
        int wantValue = 0;
        try {
            wantValue = Integer.parseInt(nextLine);

            // Ensure the value is greater than or equal to 0.
            if(wantValue < 0) throw new NumberFormatException();
        } catch(NumberFormatException e) {
            // Give the user an error message since the input was not an valid want value.
            System.out.printf("The inputted value of \"%s\" is not a valid want value!\n", nextLine);

            // Exit the method without continuing.
            return;
        }

        // Inform the user about the change to the want value.
        System.out.printf("\n-- The want value for stock item \"%s\" is changed from %d to %d.\n", title, item.getWant(), wantValue);

        // Set the want value of the item.
        item.setWant(wantValue);
    }

    // Print out the purchase order for additional videos based on a comparison
    // of the have and want values in the inventory.
    // Preconditions: None.
    // Postconditoon: For each title in the inventory list, if the want value is
    //                bigger than the have value, the have value is brought up to
    //                the want value, and the purchase order for that title is
    //                printed out.
    //
    private void purchaseOrder() {

        // ==> 2. Add your code here!
        // System.out.println("-- To be implemented!"); // comment this statement!

        // Create a flag to store whether any orders have been placed.
        boolean orderPlaced = false;

        // Create a counter to store the line number of the purchase order.
        int lineNum = 1;

        // Loop through every title in the inventory list.
        for(int i = 1; i <= inventory.size(); i++) {
            // Store a reference to the item locally.
            StockItem item = (StockItem) inventory.get(i);

            // Store the want and have values of this item.
            int wantValue = item.getWant();
            int haveValue = item.getHave();

            // Check if the want value is greater than the have value.
            if(wantValue > haveValue) {
                // Check if the orderPlaced flag is false, meaning that this is the first order to be placed.
                if(!orderPlaced) {
                    // Print out the purchase order title.
                    System.out.println("");
                    System.out.println("Purchase Order");
                    System.out.println("==============");
                }

                // Set the orderPlaced flag to true.
                orderPlaced = true;

                // Print the order message.
                System.out.printf("[%d] %d videos for stock item \"%s\" has been ordered.\n", lineNum, wantValue - haveValue, item.getTitle());

                // Set the have value of this item to the want value.
                item.setHave(wantValue);

                // Increment the line counter by 1.
                lineNum++;
            }
        }

        // Check if there were no orders placed.
        if(!orderPlaced) {
            // Print a message explaining that the purchase order is empty.
            System.out.println("\nNo videos have been ordered.");
        }

    }

    // Print out the return order for unwanted videos based on a comparison
    // of the have and want values in the inventory.
    // Preconditions: None.
    // Postconditoon: For each title, if the have value is bigger than the want
    //                value, the have vaule is reduced to the want value, and
    //                the return order for that title is printed out.
    //
    private void returnOrder() {

        // ==> 3. Add your code here!
        // System.out.println("-- To be implemented!"); // comment this statement!

        // Create a flag to store whether any returns have been placed.
        boolean returnsPlaced = false;

        // Create a counter to store the line number of the return order.
        int lineNum = 1;

        // Loop through every title in the inventory list.
        for(int i = 1; i <= inventory.size(); i++) {
            // Store a reference to the item locally.
            StockItem item = (StockItem) inventory.get(i);

            // Store the want and have values of this item.
            int wantValue = item.getWant();
            int haveValue = item.getHave();

            // Check if the have value is greater than the want value.
            if(haveValue > wantValue) {
                // Check if the returnsPlaced flag is false, meaning that this is the first return to be placed.
                if(!returnsPlaced) {
                    // Print out the return order title.
                    System.out.println("");
                    System.out.println("Return Order");
                    System.out.println("============");
                }

                // Set the returnsPlaced flag to true.
                returnsPlaced = true;

                // Print the return order message.
                System.out.printf("[%d] %d videos for stock item \"%s\" has been returned.\n", lineNum, haveValue - wantValue, item.getTitle());

                // Set the have value of this item to the want value.
                item.setHave(wantValue);

                // Increment the line counter by 1.
                lineNum++;
            }
        }

        // Check if there were no returns placed.
        if(!returnsPlaced) {
            // Print a message explaining that the return order is empty.
            System.out.println("\nNo videos have been returned.");
        }
    }

    // Sell a video for the specified title. If the title is sold out, put
    // a name on the waiting list for the title.
    // Preconditions: None.
    // Postcondition: The have value for the specified title is decreased
    //                by 1 if the have value is bigger than zero. If the title
    //                is sold out, put a name on the wait list for the title.
    //
    private void sellVideo(String title) {

        // ==> 4. Add your code here!
        // System.out.println("-- To be implemented!"); // comment this statement!

        // Find the StockItem that the user is attempting to sell.
        StockItem item = inventory.findStockItem(title);

        // Ensure the title exists.
        if(item == null) {
            // Give the user an error message.
            System.out.printf("Title \"%s\" cannot be found!\n", title);

            // Exit the method without continuing.
            return;
        }

        // Get the have value of the item.
        int haveValue = item.getHave();

        // Check if the title is sold out.
        if(haveValue <= 0) {
            // Inform the user that the title is sold out and that they should put a name on the waiting list.
            System.out.printf("The title is sold out! Put a name on the waiting list for stock item \"%s\"\n", title);
        }

        // Prompt the user to enter a first name.
        readInput("Input first name: ");

        // Store the first name entered.
        String firstName = nextLine;

        // Prompt the user to enter a last name.
        readInput("Input last name: ");

        // Store the last name entered.
        String lastName = nextLine;

        // Create a Person object with the entered name.
        Person person = new Person(firstName, lastName);

        // Attempt to add this person to the customer list.
        inventory.addToCustomerList(person);

        // Check if the title is not sold out.
        if(haveValue > 0) {
            // Inform the user that the title has been sold.
            System.out.printf("-- A video of title \"%s\" is sold to %s.\n", title, person);

            // Decrease the have value by one.
            item.setHave(haveValue - 1);
        } else {
            // Get the waiting list of the title.
            WaitingList waitingList = item.getWaitingList();

            // Add the person to the waiting list at the end.
            waitingList.addLast(person);

            // Inform the user that the name has been added to the waiting list.
            System.out.printf("-- \"%s\" has been put on the waiting list for stock item \"%s\".\n", person, title);
        }

    }

    // Deliver videos to people on the waiting list for a specified title
    // Preconditions: None.
    // Postcondition: If the have value for the specified title is bigger than
    //                zero, deliver videos to the people on the waiting list
    //                for that title. If there are not enough videos currently
    //                in stock, those people in the front of the waiting
    //                list will receive videos.
    //
    private void deliverVideo(String title) {

        // ==> 5. Add your code here!
        // System.out.println("-- To be implemented!"); // comment this statement!

        // Find the StockItem that the user is attempting to deliver.
        StockItem item = inventory.findStockItem(title);

        // Ensure the title exists.
        if(item == null) {
            // Give the user an error message.
            System.out.printf("Title \"%s\" cannot be found!\n", title);

            // Exit the method without continuing.
            return;
        }

        // Get the have value of the item.
        int haveValue = item.getHave();

        // Get the waiting list for the title.
        WaitingList waitingList = item.getWaitingList();

        // Check if the have value is greater than zero.
        if(haveValue > 0) {
            // Deliver videos to each person on the waiting list as long as the have value is greater than 0.
            while(!waitingList.isEmpty() && item.getHave() > 0) {
                // While there are still people on the waiting list and titles to be delivered.
                // Get the first person on the waiting list.
                Person person = (Person) waitingList.get(1);

                // Inform the user that the video has been delivered.
                System.out.printf("-- The video \"%s\" is delivered to \"%s\"\n", title, person);

                // Remove the person from the waiting list.
                waitingList.removeFirst();

                // Decrease the have value by one.
                haveValue--;
                item.setHave(haveValue);
            }
        } else {
            // Inform the user that no stock is available.
            System.out.println("-- No video for this title is currently in stock!");
            System.out.println("   Please order first!");
        }

    }

    /**
     * Displays the customer list.
     */
    private void displayCustomerList() {
        // Display the customer list.
        inventory.displayCustomerList();
    }

    /**
     * Deletes a specified title from the inventory.
     * @param title the title to be removed
     */
    private void deleteTitle(String title) {
        // Get the StockItem referenced by the title.
        StockItem item = inventory.findStockItem(title);

        // Ensure the title exists.
        if(item == null) {
            // Give the user an error message.
            System.out.printf("** Title (%s) not found!\n", title);

            // Exit the method without continuing.
            return;
        }

        // Delete the item from the inventory.
        inventory.deleteStockItem(item);

        // Inform the user that the title has been removed.
        System.out.printf("-- Title (%s) has been removed!\n", title);
    }

    /**
     * Deletes a customer from the customer list.
     */
    private void deleteCustomer() {
        // Prompt the user to enter a first name.
        readInput("Input first name: ");

        // Store the first name entered.
        String firstName = nextLine;

        // Prompt the user to enter a last name.
        readInput("Input last name: ");

        // Store the last name entered.
        String lastName = nextLine;

        // Create a Person object with the entered name.
        Person person = new Person(firstName, lastName);

        // Delete the person from the customer list.
        inventory.deleteFromCustomerList(person);
    }

    /**
     * Deletes all elements in the inventory list and the customer list.
     */
    private void deleteAll() {
        // Warn the user that this will delete all elements.
        System.out.println("Warning: All elements in the inventory list and the customer list will be removed!");

        // Prompt the user asking if they would like to proceed.
        nextLine = "";
        while(!nextLine.toUpperCase().equals("Y") && !nextLine.toUpperCase().equals("N")) {
            readInput("Do you want to proceed (Y/N)? ");
        }

        // Check if the user replied yes.
        if(nextLine.toUpperCase().equals("Y")) {
            // Remove every element in the inventory list.
            inventory.removeAll();

            // Remove all elements in the customer list.
            inventory.deleteAllFromCustomerList();

            // Inform the user that all elements have been removed.
            System.out.println("All elements in the inventory list and the customer list have been removed!");
        } else {
            // Inform the user that the elements have remained as they were.
            System.out.println("No elements have been removed.");
        }
    }

    // Display the help menu for user interface
    //
    private void helpMenu() {
        System.out.println();
        System.out.println("                             =========");
        System.out.println("                             Help Menu");
        System.out.println("                             =========");
        System.out.println("---------------------------------------------------------------------");
        System.out.println("H         (Help)      Help menu");
        System.out.println("I <title> (inquire)   Display the inventory info");
        System.out.println("L         (list)      List the entire inventory");
        System.out.println("A <title> (add)       Add a new title to the inventory");
        System.out.println("M <title> (modify)    Modify the want value for a specific title");
        System.out.println("D <title> (delivery)  Deliver videos to people on the waiting list");
        System.out.println("O         (order)     Display the purchase order");
        System.out.println("R         (return)    Display the return order");
        System.out.println("S <title> (sell)      Sell a specified title");
        System.out.println("Q         (quit)      Save the inventory info and terminate execution");
        System.out.println("---------------------------------------------------------------------");

    }

    // process user input and print out the results
    public void processInput() {
        Inventory inv = null;
        StockItem stockItem;
        String command, title =  null;

        System.out.println("Restoring inventory from file \"inventory.dat\" ... ");
        if ((inv = (Inventory) inventory.restoreInventory()) != null)
            inventory = inv;
        else {
            System.out.println("Restore inventory error!");
        }

        helpMenu(); // display help menu

        while(true) {
            readInput("\nInput the command (enter \"Q\" or \"q\" to quit): ");

            StringTokenizer input = new StringTokenizer(nextLine);
            try {
                command = input.nextToken();
                if (command.charAt(0) == 'I' || command.charAt(0) == 'i' ||
                    command.charAt(0) == 'A' || command.charAt(0) == 'a' ||
                    command.charAt(0) == 'M' || command.charAt(0) == 'm' ||
                    command.charAt(0) == 'D' || command.charAt(0) == 'd' ||
                    command.charAt(0) == 'S' || command.charAt(0) == 's' ||
                    command.charAt(0) == 'X' || command.charAt(0) == 'x') {
                    title = nextLine.substring(2);
                }
            } catch (Exception e) {
                System.out.println("Invalid input! Type 'H' for help.");
                continue;
            }

            switch (command.charAt(0)) {
               case 'H': case 'h': helpMenu(); break;
               case 'L': case 'l': inventory.listInventory(); break;
               case 'I': case 'i': displayVideoInfo(title); break;
               case 'A': case 'a': addNewTitle(title); break;
               case 'M': case 'm': modifyWantValue(title); break;
               case 'D': case 'd': deliverVideo(title); break;
               case 'O': case 'o': purchaseOrder(); break;
               case 'R': case 'r': returnOrder(); break;
               case 'S': case 's': sellVideo(title); break;
               case 'C': case 'c': displayCustomerList(); break;
               case 'X': case 'x': deleteTitle(title); break;
               case 'Y': case 'y': deleteCustomer(); break;
               case 'Z': case 'z': deleteAll(); break;
               case 'Q': case 'q':
                  System.out.print("\nSaving inventory to file \"inventory.dat\" ... ");
                  inventory.saveInventory();
                  System.out.println("Done!");
                  System.exit(0);
               default: System.out.println("Invalid input! Type 'H' for help.");
            }
        }
    }

    public static void main(String[] args) {
        InventoryApp inventoryApp = new InventoryApp();
        inventoryApp.processInput();
    }
}
