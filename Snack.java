/**

   * Snack --- initialize a Snack object in Java
   * @author Chuckee Kondo (Chaz)
   */

import java.text.*; //to access DecimalFormat
   
public class Snack {

   private int barcode; // barcode identifier
   private int calories; // number of calories in the snack
   private double price; // price of the snack
   private String name; // name of the snack
   
   /**
      * Snack Constructor ~
      * Initializes a Snack object and validates parameters
      * @params int barcode, int calories, double price, String name
      * @exception throws SnackException
      * @return No return value
      */
   
   public Snack(int barcode, int calories, double price, String name) throws Exception {
      this.setBarcode(barcode);
      this.setCalories(calories);
      this.setPrice(price);
      this.setName(name); 
   }
   
   /**
      * setBarcode ~
      * Validates and Updates the barcode value
      * valid values range from 10001 to 99999
      * @param int newBarcode
      * number to represent the new barcode
      * @exception SnackException: when parameter is out of range
      * @return No return value
      */
   
   public void setBarcode(int newBarcode) throws Exception {
      if (newBarcode > 99999 || newBarcode < 10001) {
         SnackException barcodeError = new SnackException("Error. Valid barcode range is [10001 - 99999]");
         throw barcodeError; 
      }
      else {
         this.barcode = newBarcode;
      }
   }
   
   /**
      * setCalories ~
      * Validates and Updates calories value
      * valid values range from 0 to 2000
      * @param int newCalorieValue
      * number to represent the new amount of calories
      * @exception SnackException: when parameter is out of range
      * @return No return value
      */
   
   public void setCalories(int newCalorieValue) throws Exception {
      if (newCalorieValue < 0 || newCalorieValue > 2000) {
         SnackException calorieError = new SnackException("Error. Valid calorie range is [0, 2000]");
         throw calorieError; 
      }
      else {
         this.calories = newCalorieValue;
      }
   }
   
   /**
      * setPrice ~
      * Validates and Updates the price value
      * valid values range from 1.00 to 5.00
      * @param double newPrice
      * number to represent the new price
      * @exception SnackException: when parameter is out of range
      * @return No return value
      */
   
   public void setPrice(double newPrice) throws Exception {
      if (newPrice < 1.00 || newPrice > 5.00) {
         SnackException priceError = new SnackException("Error. Valid price range is [1.00, 5.00]");
         throw priceError; 
      }
      else {
         this.price = newPrice;
      }
   }
   
   /**
      * setName ~
      * Validates and Updates the name value
      * valid values range from 2 characters and up
      * @param String newName
      * number to represent the new name
      * @exception SnackException: when parameter is out of range
      * @return No return value
      */
   
   public void setName(String newName) throws Exception {
      // trim prior to eliminate leading and trailing spaces
      String trimmedName = newName.trim();
      if (trimmedName.length() <= 1) {
         SnackException nameError = new SnackException("Error. Name must be two or more characters.");
         throw nameError; 
      }
      else {
         this.name = trimmedName;
      }
   }
   
   /**
      * getBarcode ~
      * Gets barcode value
      * @param No parameters
      * @return int barcode
      */
   
   public int getBarcode() {
      return this.barcode;
   }
   
   /**
      * getCalories ~
      * Gets calories value
      * @param No parameters
      * @return int calories
      */
   
   public int getCalories() {
      return this.calories;
   }
   
   /**
      * getPrice ~
      * Gets price value
      * Utilizes private helper method: formatPrice
      * @param No parameters
      * @return double price
      */
   
   public double getPrice() {
      String formattedPrice = this.formatPrice();
      return this.price;
   }
   
   /**
      * getName ~
      * Gets name value
      * @param No parameters
      * @return String name
      */
   
   public String getName() {
      return this.name;
   }
   
   /**
      * toString ~
      * Formats object items into a concatenated string
      * Utilizes private helper method: formatPrice
      * @param No parameters
      * @return String of object values
      */
   
   public String toString() {
      String barcodeString = "Barcode: " + this.barcode;
      String calorieString = "Calories: " + this.calories;
      String priceString = "Price: " + this.formatPrice();
      String nameString = "Name: " + this.name;
      return barcodeString + "\n" + calorieString + "\n" + priceString + "\n" + nameString;
   }
   
   /**
      * formatPrice ~
      * Private method for formatting price using the DecimalFormat class
      * Utilizes private helper method: formatPrice
      * @param No parameters
      * @return String of formatted price
      */
   
   private String formatPrice() {
      DecimalFormat formatter = new DecimalFormat("$#.00");
      String output = formatter.format(this.price);
      return output;
   }

}
