/*=====================================================================================================================
 * @Author: Carlos Capili  @Date:2018-02-11
 * 
 * This program takes a credit card input and determines whether it is a Master Card or Visa and if it is a valid card
 * This class in particular, creates the object and calls the input method in the other class.
 * 
 * ===================================================================================================================*/
import java.util.Scanner;
class MainPrgAssign1CarlosCapili{
  public static void main(String[]arg) {
    
    CreditCardCarlos code = new CreditCardCarlos();
    code.inputNo();
    
  }// end of main 
}// end of class MainPrg

/*=====================================================================================================================
 * @Author: Carlos Capili  @Date:2018-02-11
 * 
 * This class deals with input, validating and displaying the results of the credit card number.
 *
 * ===================================================================================================================*/
class CreditCardCarlos {
  Scanner in = new Scanner(System.in);
  String cardNo;
  
  /*=====================================================================================================================
   * @Author: Carlos Capili  @Date: 2018-02-11
   * @param: none            @return: void 
   * 
   * This method deals with asking the user to input a credit card number and asking if they would like to input another.
   *
   * ===================================================================================================================*/ 
  public void inputNo()
  {
    char letter='a';
    String letter2;
    
    do{
      System.out.println("Please Enter A Credit Card Number:");
      cardNo=in.nextLine();
      validateNo();
      displayResult();
      
      do{
        System.out.println("Do you wish to enter another Credit Card number(Y/N)?");
        letter2=in.nextLine();
        letter=letter2.charAt(0);// so it only takes first letter
        if((letter=='Y'||letter=='N')&&letter2.length()==1) // so it only accepts one letter
          break;
      }while(letter!='N'||letter!='Y');
      
      switch(letter)
      {
        case 'Y':
          break;
          
        case 'N':
          System.out.println("Good-Bye!");
          break;
      }// end of switch
      
    }while(letter!='N');
    
  }// end of inputNo
  
  /*=====================================================================================================================
   * @Author: Carlos Capili  @Date: 2018-02-11
   * @param: none            @return: boolean 
   * 
   * This method is responsible for checking if the inputted number is a valid credit card.
   *
   * ===================================================================================================================*/ 
  public boolean validateNo() {
    boolean validated = false;
    int sum=0;
    int add=0; int add2=0; // two variables are for 
    int[]check= new int[16];
    
    //------------ CHECKS IF cardNo IS 16 CHARACTERS AND IF ITS ALL NUMBERS---------------- 
    for(int a=0;a<cardNo.length()-1;a++)
    {
      if(cardNo.charAt(a)<48||cardNo.charAt(a)>57)
        return false;
    }// end of for
    
    if (cardNo.length()<16||cardNo.length()>16)
    {
      return false;
    }
    //-------------------------------------------------------------------------------------
    
    for(int n=0;n<=cardNo.length()-1;n=n+2) // for even numbers to be multiplied by 2 and put into array
    {
      int changeNum=0;
      int myNum=cardNo.charAt(n)*2;
      
      changeNum=myNum-96; // convert ASCII value into real number value by subtracting by 96 since zero value is 48.
      check[n]=changeNum; // places in even element index in array starting from 0.
      
      if(n==14) // prevents repeat
        break;
      
    }// end of for
    
    for(int m=1;m<=cardNo.length()-1;m=m+2) // for odd numbers and put into array
    {
      int changeNum2=0;
      int myNum2=cardNo.charAt(m);
      changeNum2=myNum2-48; // converts odd numbers into real number value
      check[m]=changeNum2; // places in odd element index in array
    }// end of for
    
    for(int d=0;d<=check.length-1;d++)
    {  
      int sum2=0;
      int sum3=0;
      
      if(check[d]>=10) // this separates the digits 10 or greater and adds them up
      {
        sum2=((check[d]-10)+1);// this will allow to get the second digit of number then add it by the first which is always 1
        add=add+sum2;
      }
      else // adds up the digits less than 10
      {
        sum3=check[d];
        add2=add2+sum3;
      }
      
      sum=add+add2; 
      
    }// end of for
    
    if(sum%10==0)// if there is no remainder it is divisible by 10 and is true
      validated=true;
    else
      validated=false;
    
    return validated; // Test num:5314772685932112
    
  }// end of validateNo
  
  /*=====================================================================================================================
   * @Author: Carlos Capili  @Date: 2018-02-11
   * @param: none            @return: void 
   * 
   * This method outputs the results of the credit card, whether it is a Master card or Visa and whether it is valid. 
   *
   * ===================================================================================================================*/ 
  public void displayResult() {
    
    if(cardNo.charAt(0)==53)//Master card (ASCII for number 5 is 53)
    {  
      if(validateNo()==false)
        System.out.printf("Master Card %s is INVALID.\n",cardNo);
      
      if(validateNo()==true)
        System.out.printf("Master Card %s is VALID.\n",cardNo);
    }// end of master
    
    else if(cardNo.charAt(0)==52)// Visa Card
    {  
      if(validateNo()==false)
        System.out.printf("Visa Card %s is INVALID.\n",cardNo);
      
      if(validateNo()==true)
        System.out.printf("Visa Card %s is VALID.\n",cardNo);
    }// end of visa
    else
      System.out.println("This Card is INVALID");
    
  }// end of displayResult
  
}// end of class CreditCard