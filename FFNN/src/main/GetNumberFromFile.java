package main;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class GetNumberFromFile {
	
	public static Double Num = 0d;
	
	public static Double getLargestNumber(String FilePath) {
        try {
            Scanner file = new Scanner(new File(FilePath));
            Double largest = file.nextDouble();

            while(file.hasNextDouble()) {
                Double number = file.nextDouble();

                if(number > largest) {
                    largest = number;
                }
            }

            file.close();
            
            Num = largest;
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
        return(Num);
    }
	
	public static Double getSmallestNumber(String FilePath) {
        try {
            Scanner file = new Scanner(new File(FilePath));
            Double smallest = file.nextDouble();

            while(file.hasNextDouble()) {
                Double number = file.nextDouble();

                if(number < smallest) {
                    smallest = number;
                }
            }

            file.close();
            
            Num = smallest;
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
        return(Num);
    }
}
