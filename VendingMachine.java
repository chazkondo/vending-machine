/**

   * Vending Machine --- a software representation
   * An application for the input of snacks
   * Includes add, remove, view, and more
   * @author Chuckee Kondo (Chaz)
   */

import java.util.Scanner; // used for scnr
import java.text.DecimalFormat; // used for price format
import java.math.RoundingMode; // used to ensure price format rounds down
   
public class VendingMachine {

   private static boolean isMenuVisible = false; // the state of menu visibility
   private static boolean secretMenuToggled = false; // the state of the secret menu visibility
   private static Snack[] snackList; // the snack list arr of the current vending machine state
   private static Scanner scnr; // scanner to view user input
   private static String userInput = "init"; // initial user input arbitrarily set to "init"
   
   /** Driver Class -
      * Initializes the Vending Machine program
      * @params String[] args
      * includes the command line arguments
      * @exception throws SnackException
      * @return No return value
      */
      
   public static void main(String[] args) throws Exception {
      // print welcome message
      System.out.println("Welcome to the Snack Vending Machine!\n");
      
      // initialize snack array to 0 length
      snackList = new Snack[0];
      
      // initialize menu loop
      while (!userInput.equals("0")) {
         // if statement to check secret menu state
         if (!secretMenuToggled) {
            // try/catch block to re-loop the program when errors are thrown
            try {
               // main vending machine method
               runVendingMachine();
            }
            catch (Exception e) {
                System.out.println("\n\nOops, an error occurred - \n" + e + "\n");
                System.out.println("Restarting...\n");
                reset(); // helper method to reset variables to initial states
            }
         }
         else {
            activateSecretMenu();
         }
      }
      
      endProgram();
   }
   
   /**
      * runVendingMachine ~
      * Runs the main Vending Machine logic
      * Prints menu and runs a switch case for new user input
      * @param No parameters
      * @exception Passes exceptions thrown from within to the driver class
      * @return No return value
      */
   
   private static void runVendingMachine() throws Exception {
      // start program
      if (!isMenuVisible) {
         printMenu();
      } else {
         // switch case that interprets user input when the menu has been displayed
         switch (userInput) {
            case "1":  
               addSnack(); // add snack
               break;
            case "2":  
               deleteSnackByBarcode(); // remove snack
               break;
            case "3":  
               printItemsOverInput(); // print snacks over user price input
               break;
            case "4":  
               printAllSnacks(); // print all snacks
               break;
            case "seed":
               addSeedSnacks(); // helper method
               break;
            case "up up down down left right left right start":
               triggerSecret(); // just for fun
               break;
            default: 
               catchDefaultCase(); // helper method
               break;
         }
      }
   }
   
   /**
      * printMenu ~
      * Prints the program options for the user
      * Resets scanner and reads the next userInput
      * Sets isMenuVisible boolean to true
      * @param No parameters
      * @return No return value
      */
   
   private static void printMenu() {
      scnr = new Scanner(System.in);
      System.out.println("Menu\n");
      System.out.println("1. Add a snack");
      System.out.println("2. Remove a snack");
      System.out.println("3. Print snacks that cost more than a given price");
      System.out.println("4. Print all the snacks");
      System.out.println("0. End this program");
      
      isMenuVisible = true;
      userInput = scnr.nextLine();
   }
   
   
   
               /*   Main Methods   */
   
   
   
   /**
      * addSnack ~
      * Allows for the creation of new snacks
      * Guides the user to add a new snack to the current instantiated snackList[]
      * @param No parameters
      * @exception Passes exceptions thrown from within to runVendingMachine()
      * @return No return value
      */
      
   private static void addSnack() throws Exception {
      // print basic instruction for user
      System.out.println("\n\n\n\n\nPlease enter a snack\n");
      
      // ask user for the barcode of the new snack
      System.out.println("Please enter the snack barcode. \nValid barcode range is [10001 - 99999].");
      
      // set and prevalidate barcode immediately for better user experience
      int barcode = prevalidateBarcode(); // validation method
      
      // check if the barcode already exist and throw an error if necessary
      if (isBarcodeDuplicate(barcode)) {
         SnackException barcodeDuplicateError = new SnackException("Error. Barcode already exists.");
         throw barcodeDuplicateError; 
      }
      
      // ask user for the calories of the new snack
      System.out.println("Please enter the snack calories. \nValid range is [0 - 2000].");
      
      // set and prevalidate calories immediately for better user experience
      int calories = prevalidateCalories(); // validation method
      
      // ask user for the price of the new snack
      System.out.println("Please enter the snack price. \nValid range is [1.00 - 5.00].");
      
      // set and prevalidate price immediately for better user experience
      double price = prevalidatePrice(); // validation method
      
      // ask user for the name of the new snack
      System.out.println("Please enter the snack name");
      
      // set and prevalidate price immediately for better user experience
      String name = prevalidateName(); // validation method
      
      // create the snack obj
      Snack newSnack = new Snack(barcode, calories, price, name);
      
      // add to the snackList[] arr
      addToSnackArr(newSnack);

      // print success message
      System.out.print("\nNew Item:\n\n" + newSnack.toString() + "\n\nSuccessfully added!\n-------------------\n\n");
      
      Thread.sleep(1000); // added for better user experience
      
      // reset variables to initial state before the BREAK in runVendingMachine() to keep the program running neatly
      reset();
   }
   
   /**
      * deleteSnackByBarcode ~
      * Allows for the deletion/removal of snacks currently within snackList[]
      * Guides the user to remove a snack by its barcode int
      * @param No parameters
      * @exception Passes exceptions thrown from within to runVendingMachine()
      * @return No return value
      */
   
   private static void deleteSnackByBarcode() throws Exception {
      boolean continueFunction = true; // dictates if this function should end early due to various circumstances
      
      // exit back to main menu IF snackList[] length is 0
      if (snackList.length == 0) {
         reset(); // reset variables
         
         // tell user no snacks are available and therefore nothing was removed
         System.out.println("\nNo snacks available."); 
         System.out.println("Nothing removed.\n"); 
         
         // prevent the function from continuing
         continueFunction = false; // prevents entering the following next while loop
      }
      
      // enter a while loop that allows for this function to be broken if continueFunction is updated to false
      while (continueFunction) {
         
         // print basic instruction for the user
         System.out.println("Please enter the barcode of the snack item you would like to remove");
         
         // display barcodes of current snacks to help the user pick a barcode
         displayCurrentBarcodes(); // helper method
         
         // prevalidate immediately for program efficiecny 
         int barcode = prevalidateBarcode(); // validation method
         
         // find the index of the snack object the user wants to remove
         int index = findSnackByBarcode(barcode); // helper method
         
         if (index != -1) { // indicates the index exists
         
            // keep of a reference to the item being removed to notify the user
            String removedSnackName = snackList[index].getName();
            
            // create snackListCopy[] arr to dynamically reflect the snackList length with the removed item
            Snack[] snackListCopy = new Snack[snackList.length-1];
            
            // sets snackList[] arr to snackListCopy[] with length 0 IF there is only one snack
            if (snackList.length == 1) {
               snackList = snackListCopy;
               reset();
               
               // print success message and end function
               System.out.print("\n" + removedSnackName + " successfully removed!\n\n");
               continueFunction = false; // exit while loop
            }
            else { // indicates that there are more than one snack in snackList[]
               // loop through snackList[] to copy snacks into snackListCopy[]
               for (int i = 0; i < snackList.length-1; i++) {
                  // copy snack over IF "i" index is less than the index of the removed snack
                  if (i < index) {
                     snackListCopy[i] = snackList[i];
                  }
                  // copy over the next snack index (i+1) IF "i" index is equal
                  // or greater than the index of the removed snack
                  else {
                     snackListCopy[i] = snackList[i+1];
                  }
               }
               
               // set snackList[] to snackListCopy[] to reflect the correct removal of the snack 
               snackList = snackListCopy;
               reset();
               
               // print out success message and end function
               System.out.print("\n" + removedSnackName + " successfully removed!\n\n");
               continueFunction = false; // exit while loop
            }
         }
         else { // indicates the index doesn't exist
         
            // print that no matching barcode was found, therefore nothing was removed
            System.out.println("Sorry, no snack with that matching barcode was found.");
            System.out.println("Nothing removed.\n");
            
            // end function
            reset();
            continueFunction = false; // exit while loop
         }
      }
   }
   
   /**
      * printItemsOverInput ~
      * Displays all snacks over the user inputted price
      * Guides the user to view snacks over a certain price
      * @param No parameters
      * @exception Passes exceptions thrown from within to runVendingMachine()
      * @exception Exception e necessary for ending while loop during validation
      * @return No return value
      */
   
   private static void printItemsOverInput() throws Exception {
      boolean continueFunction = true; // dictates if this function should end early due to various circumstances
      boolean noItemFound = true; // dictates if any snack over the user inputted price is found
      
      // exit back to main menu IF snackList[] length is 0
      if (snackList.length == 0) {
         reset();
         
         // tell user no snacks are available and therefore nothing is viewable
         System.out.println("\nNo snacks available to compare price.\n");
         continueFunction = false; // prevents entering the following next while loop
      }
      
      // enter a while loop that allows for this function to be broken if continueFunction is updated to false
      while (continueFunction) {
         double userPrice; // declare userPrice
         
         // print basic instruction for the user
         System.out.println("Enter a price. (This should be of type double. Ex: 4.45) \n");
         
         // set and validate userInput immediately with try/catch block to break the while loop IF there is an exception
         try {
            userPrice = validatePriceComparison(); // validation method
         }
         catch (Exception e) {
            continueFunction = false; // break loop in case of error
            throw e;
         }
         
         // print a user friendly title for the following snacks
         System.out.println("Showing all items above " + formatPrice(userPrice) + ":\n");
         
         // loop through snackList[] and print items that have a price greater than userInput
         for (int i = 0; i < snackList.length; i++) {
            if (snackList[i].getPrice() > userPrice) {
               System.out.println(snackList[i].toString() + "\n");
               // keep track of if no items match
               if (noItemFound) {
                  noItemFound = false;
               }
            }
         }
         
         // print "None" IF no items were printed in the previous for loop
         if (noItemFound) {
            System.out.println("None\n");
         }
         
         // end function
         reset();
         continueFunction = false;
      }
   }
   
   /**
      * printAllSnacks ~
      * Displays all snacks in the current instantiation of snackList[]
      * @param No parameters
      * @exception No exceptions
      * @return No return value
      */
   
   private static void printAllSnacks() {
      // print appropriate message if there are no snacks
      if (snackList.length == 0) {
         System.out.println("\nSorry, no snacks available. Please add a snack.\n");
      } else {
         
         // print a user friendly title for the following snacks
         System.out.println("\nCurrent snacks available: \n");
         for (int i = 0; i < snackList.length; i++) {
            System.out.println(snackList[i].toString() + "\n");
         }
         System.out.println(); // extra user friendly formatting
      }
      reset(); // reset prior to break in runVendingMachine()
   }

   
   
               /*   Helper Methods   */
   
   
   
   /**
      * addSeedSnacks ~
      * Adds three premade snacks to snackList[]
      * First performs barcode check
      * @param No parameters
      * @exception No exceptions
      * @return No return value
      */
   
   private static void addSeedSnacks() throws Exception {
      boolean seedInfoExists = false; // dictates if a matching barcode already exists
      
      // loop and search snackList[] for barcodes 10001, 10002, and 10003
      for (int i = 0; i < snackList.length; i++) {
         if (snackList[i].getBarcode() == 10001) {
            seedInfoExists = true;
         }
         if (snackList[i].getBarcode() == 10002) {
            seedInfoExists = true;
         }
         if (snackList[i].getBarcode() == 10003) {
            seedInfoExists = true;
         }
      }
      
      // print that the program is unable to seed IF seedInfoExists is true
      if (seedInfoExists) {
         System.out.println("Unable to seed snacks. One or more seeded barcodes exist.\n");
      } 
      else {
         // create and add three premade snacks to snackList[]
         Snack apple = new Snack(10001, 100, 1.10, "Apple");
         addToSnackArr(apple);
         
         Snack orange = new Snack(10002, 100, 2.00, "Orange");
         addToSnackArr(orange);
         
         Snack chocolateBar = new Snack(10003, 100, 3.55, "Chocolate Bar");
         addToSnackArr(chocolateBar);
         
         // print success message
         System.out.println("Successfully injected seed snacks.\n");
      }
      reset(); // reset prior to break
   }
   
   /**
      * catchDefaultCase ~
      * Catches invalid user inputs
      * Detects if user input is a integer
      * @param No parameters
      * @exception NumberFormatException typeMismatch
      * Indicate if value is not an integer (despite being a string)
      * @return No return value
      */
   
   private static void catchDefaultCase() {
      // implement try/catch block to catch invallid userInput types and throw to runVendingMachine()
      try {
         // print error message for user
         System.out.println("\n\n\n\n\nSorry, invalid input: " + userInput);
         
         // parse the userInput string to detect if it is an int
         Integer.parseInt(userInput);
         
         // indicate that the number is out of range IF userInput is an integer
         System.out.println("Number out of range.\n");
      } 
      catch (NumberFormatException typeMismatch) { // indicates that userInput is not an int
         // print error message for user
         System.out.println("Program is expecting an integer.");
         
         // throw error to runVendingMachine();
         throw typeMismatch;
      }
      reset(); // reset prior to break
   }
   
   /**
      * endProgram ~
      * Stops the VendingMachine program
      * @param No parameters
      * @exception e
      * Catches any issues with Thread.sleep() and continues the shut down process
      * @return No return value
      */
   
   private static void endProgram() throws Exception {
      // implement try/catch to catch any issues with Thread.sleep()
      try {
         // print shutting down message
         System.out.print("\n\nShutting down...");
         
         // create a dramatic pause for the user
         Thread.sleep(1500);
         
         // prints the final message
         System.out.print("\n\nThank you for choosing the Snack Vending Machine. Goodbye.");
      }
      catch (Exception e) { // indicates there was a problem with Thread.sleep()
         // prints final messages
         System.out.print("An error occurred while shutting down...");
         System.out.print("Goodbye.");
      }
   }
   
   /**
      * findSnackByBarcode ~
      * Finds a snack object in snackList[] with matching barcode param
      * @param int barcode
      * The int representing the barcode of the desired snack within snackList[]
      * @return int
      * An int representing the index of the desired snack object within snackList[]
      */
      
   private static int findSnackByBarcode(int barcode) {
      // loop through snackList[] and return the snack that matches the barcode
      for (int i = 0; i < snackList.length; i++) {
         if (barcode == snackList[i].getBarcode()) {
            return i;
         }
      }
      // return -1 as an indication that no item was found
      return -1; // -1 to represent the index does not exist
   }
   
   /**
      * displayCurrentBarcodes ~
      * Displays all the barcodes currently in snackList[]
      * @param No parameters
      * @return No return value
      */
   
   private static void displayCurrentBarcodes() {
      // loop through snackList[] and print all barcodes
      for (int i = 0; i < snackList.length; i++) {
         System.out.println(snackList[i].getBarcode());
      }
   }
   
   /**
      * addToSnackArr ~
      * Adds newSnack to snackList[]
      * @param Snack newSnack
      * The int representing the barcode of the desired snack within snackList[]
      * @return No return value
      */
   
   private static void addToSnackArr(Snack newSnack) {
      // initialize newList arr and
      // set it to be a greater length than the current snackList
      Snack[] newList = new Snack[snackList.length+1];
      
      // copy items from current snackList to the newList arr
      for (int j = 0; j < snackList.length; j++) {
         newList[j] = snackList[j];
      }
      
      // add the newly added item
      newList[snackList.length] = newSnack;
      
      // let the original list reflect the new list
      snackList = newList;
   }
   
   /**
      * reset ~
      * Resets the variables in the driver class to correctly loop the program again
      * @param No parameters
      * @return No return value
      */
   
   private static void reset() {
      isMenuVisible = false; // allows runVendingMachine() to choose the printMenu() path
      userInput = "init"; // resets any user input
   }
   
   /**
      * formatPrice ~
      * Private method for formatting price using the DecimalFormat class
      * Utilizes private helper method: formatPrice
      * @param double price
      * actual value of price to be rounded down and displayed nicely
      * @return String of formatted price
      */
   
   private static String formatPrice(double price) {
      // set the desired return format
      DecimalFormat formatter = new DecimalFormat("$#.00");
      
      // make sure digits past the hundreths spot doesn't round the price up by one penny
      formatter.setRoundingMode(RoundingMode.DOWN);
      
      // declare output and return the value
      String output = formatter.format(price);
      return output;
   }
   
   
   
               /*   Validation Methods   */
   
   
   
   /**
      * prevalidateBarcode ~
      * Prevalidates user inputted barcode
      * @param No parameters
      * @exception Throws Exception mismatch to parent method
      * @exception Throws SnackException barcodeError to parent method
      * @return int representing validated barcode
      */
      
   private static int prevalidateBarcode() throws Exception {
      int barcode; // declare new barcode
      
      // try to set barcode to next integer from user
      try { 
         barcode = scnr.nextInt();
      }
      
      // catch any type mismatch
      catch (Exception mismatch) {
         System.out.print("Program is expecting an integer between 10001 - 99999 inclusive.");
         throw mismatch;
      }
      
      // throw error IF out of range
      if (barcode > 99999 || barcode < 10001) {
         SnackException barcodeError = new SnackException("Error. Valid barcode range is [10001 - 99999]");
         throw barcodeError; 
      }
      
      // if no error thrown, return validated barcode
      return barcode;
   }
   
   /**
      * isBarcodeDuplicate ~
      * Indicates if a user inputted barcode already exists within snackList[]
      * @param int barcode
      * The barcode representing the user input of the new snack being added
      * @return boolean
      * Returns true if barcode is a duplicate and false if it's not
      */
   
   private static boolean isBarcodeDuplicate(int barcode) {
      // loop through snackList and check for barcode matches
      for (int i = 0; i < snackList.length; i++) {
         if (barcode == snackList[i].getBarcode()) {
            return true; // match found
         }
      }
      return false; // match not found
   }
   
   /**
      * prevalidateCalories ~
      * Prevalidates user inputted calories
      * @param No parameters
      * @exception Throws Exception mismatch to parent method
      * @exception Throws SnackException calorieError to parent method
      * @return int representing validated calories
      */
   
   private static int prevalidateCalories() throws Exception {
      int calories; // declare new calories
      
      // try to set calories to next integer from user
      try { 
         calories = scnr.nextInt();
      }
      // catch any type mismatch
      catch (Exception mismatch) {
         System.out.print("Program is expecting an integer between 0 - 2000 inclusive.");
         throw mismatch;
      }
   
      // throw exception IF out of range
      if (calories < 0 || calories > 2000) {
         SnackException calorieError = new SnackException("Error. Valid calorie range is [0, 2000]");
         throw calorieError; 
      }
      
      // if no error thrown, return validated calories
      return calories;
   }
   
   /**
      * prevalidatePrice ~
      * Prevalidates user inputted price
      * @param No parameters
      * @exception Throws Exception mismatch to parent method
      * @exception Throws SnackException priceError to parent method
      * @return double representing validated price
      */
   
   private static double prevalidatePrice() throws Exception {
      double price; // declare new price
      
      // try to set price to next double from user
      try { 
         price = scnr.nextDouble();
      }
      
      // catch any type mismatch
      catch (Exception mismatch) {
         System.out.print("Program is expecting a double between 1.00 - 5.00 inclusive.");
         throw mismatch;
      }
      
      // throw exception IF out of range
      if (price < 1.00 || price > 5.00) {
         SnackException priceError = new SnackException("Error. Valid price range is [1.00, 5.00]");
         throw priceError; 
      }
      
      // if no error thrown, return validated price
      return price;
   }
   
   /**
      * prevalidateName ~
      * Prevalidates user inputted name
      * @param No parameters
      * @exception Throws Exception mismatch to parent method
      * @exception Throws SnackException nameError to parent method
      * @return String representing validated name
      */
   
   private static String prevalidateName() throws Exception {
      String name; // declare new name
      scnr = new Scanner(System.in); // reset scnr
      
      // try to set name to next line from user
      try { 
         name = scnr.nextLine();
      }
      
      // catch any type mismatch
      catch (Exception mismatch) {
         System.out.print("Program is expecting a string of at least two non-empty characters.");
         throw mismatch;
      }
      
      String trimmedName = name.trim(); // remove leading and trailing spaces
      
      // throw exception IF length is below 2 characters
      if (trimmedName.length() <= 1) {
         SnackException nameError = new SnackException("Error. Name must be two or more characters.");
         throw nameError; 
      }
      
      // if no error thrown, return validated name
      return name;
   }
   
   /**
      * validatePriceComparison ~
      * Validates user inputted price for price check
      * @param No parameters
      * @exception Throws Exception mismatch to parent method
      * @exception Throws SnackException userPriceError to parent method
      * @return double representing validated price
      */
   
   private static double validatePriceComparison () throws Exception {
      double userPrice; // declare new userPrice
      
      // try to set userPrice to next double from user
      try { 
         userPrice = scnr.nextDouble();
      }
      
      // catch any type mismatch
      catch (Exception mismatch) {
         System.out.print("Program is expecting a double between 0 - 5.00 inclusive.");
         throw mismatch;
      }
      
      // throw exception IF out of range
      if (userPrice < 0 || userPrice > 5.00) {
         SnackException userPriceError = new SnackException("Error: Out of range. Snack prices only range from 1.00 to 5.00.");
         throw userPriceError; 
      }
      
      // if no error thrown, return validated price
      return userPrice;
   }
   
   
   
               /*   Just For Fun   */
   
   
   
   /**
      * triggerSecret ~
      * Triggers the secret menu
      * @param No parameters
      * @exception Passes any exceptions due to Thread.sleep to runVendingMachine()
      * @return No return value
      */
      
   private static void triggerSecret() throws Exception {
      System.out.println("\n\n\n\n\nSecret Found\n\n\n\n\n");
      Thread.sleep(1500);
      secretMenuToggled = true;
   }
   
   /**
      * activateSecretMenu ~
      * Displays the secret menu
      * Contains second switch statement
      * @param No parameters
      * @return No return value
      */
   
   private static void activateSecretMenu() {
      scnr = new Scanner(System.in);
      System.out.println("`~~Secret Menu~~`\n");
      System.out.println("1. Meme 1 url");
      System.out.println("2. Meme 2 url");
      System.out.println("3. Meme 3 url");
      System.out.println("4. Meme 4 url");
      System.out.println("5. ICS 111 Playlist url");
      System.out.println("6. ICS 211 Playlist url");
      System.out.println("7. Return to normal menu");
      System.out.println("0. Shut down");
      
      userInput = scnr.nextLine();
      
      switch (userInput) {
         case "1":  
            System.out.println("\nhttps://i.pinimg.com/originals/9c/03/d3/9c03d32309a7de8518b81dbbd386b9b7.jpg\n");
            break;
         case "2":  
            System.out.println("\nhttps://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSF_VCu1qlbi_P8vx323dNQXv-g-XvJSykgMA&usqp=CAU\n");
            break;
         case "3":  
            System.out.println("\nhttps://img.universitystudent.org/1/4/3280/i-love-deadlines-i-like-to-wave-at-them-as-they-pass-by-meme.jpg\n");
            break;
         case "4":  
            System.out.println("\nhttps://www.memecreator.org/static/images/memes/5288749.jpg\n");
            break;
         case "5":  
            System.out.println("\nhttps://www.youtube.com/playlist?list=PLiv4PqZ35v55aWLZty3sFtELk_Rpz14sv\n");
            break;
         case "6":  
            System.out.println("\nhttps://www.youtube.com/watch?v=-N6cqF3bajc&list=PLiv4PqZ35v55VR3V9NQNKO6rLxfYjJ7fB&index=6\n");
            break;
         case "7":  
            secretMenuToggled = false;
            reset();
            break;
         case "0":  
            break;
         default: 
            System.out.println("\n\n\n\n\nSorry, invalid secret menu input: " + userInput + "\n");
            break;
      }
   }
   
}
