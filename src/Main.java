import java.util.Scanner;

// INFOMPR 2014-2015, Utrecht University
// Assignment 2: Implementation of RANSAC
// Niels Steenbergen, Tom Sýkora

public class Main {
  public static void main(String[] args){
	System.out.println("LETS SHOUT THAT FEELING FROM OUR CHEST");
    System.out.println("WE WANNA BE THE VERY BEST");
    System.out.println("________________________");
    
    System.out.println("Enter img path:");
	Scanner scan = new Scanner(System.in);
	String imgpath = scan.next();
	System.out.println(imgpath);
	scan.close();
	
	System.out.println("END OF PROGRAM");
  }
}
