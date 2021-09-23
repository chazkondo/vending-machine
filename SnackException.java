/**

   * SnackException --- initialize an Exception object to be thrown
   * @author Chuckee Kondo (Chaz)
   */

public class SnackException extends Exception {
   private String errorMessage; // error message to display
   
   /**
      * SnackException Constructor ~
      * Initializes a SnackException
      * @params String initializedMessage
      * initializes the error message by calling the setMessage method
      * @return No return value
      */
   
   public SnackException(String initializedMessage) {
      this.setMessage(initializedMessage);
   }
   
   /**
      * setMessage ~
      * Private method
      * Sets the Exception error message
      * @param String message
      * String that addresses the error
      * @return No return value
      */
   
   private void setMessage(String message) {
      this.errorMessage = message;
   }
   
   /**
      * getMessage ~
      * Print the error message when Exception is thrown
      * @param No parameters
      * @return String errorMessage
      */
   
   public String getMessage() {
      return this.errorMessage;
   }
}