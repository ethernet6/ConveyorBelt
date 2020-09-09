/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workerbeltfactory;

import java.security.SecureRandom;
import java.util.*;
/**
 *
 * @author James
 */
public class Conveyor {
    
    int tick;
    int BeltLength;
    Component BeltSlots [];
    ArrayList<Component> BeltsEnd = new ArrayList<Component>();
    
    public Conveyor (){
        BeltLength = 5;
        BeltSlots = new Component [BeltLength];
        SetupBelt(BeltSlots);
    }
    
    public Conveyor (int n){
        
        BeltLength = n;
        BeltSlots = new Component [BeltLength];
        SetupBelt(BeltSlots);
    
    }
    
    void SetupBelt(Component[] belt){
        
        for(int q = 0; q < belt.length; q++){
            
            belt[q] = new Component("N"); // fill belt with empty slots
        }
        
        
    }
    
    void MoveBelt(){
    
    
        int LastItemOnBelt = BeltSlots.length;
        int LastItemIndex = LastItemOnBelt - 1;
        
        BeltsEnd.add(BeltSlots[LastItemIndex]);
        
        //Now remove item from belt
        
        for(int a = (BeltSlots.length - 2); a >=0 ; a--){
            
            BeltSlots[a+1] = BeltSlots[a];
        }
        
        // insert empty component at index 0 until it is filled randomly by the 
        // ADD function.
        
        BeltSlots[0] = new Component("N");
    }
    
    void lockSlot(int i){
    
        BeltSlots[i].isLocked = true;
        
    }
    
    void unlockSlot(int i){
    
        BeltSlots[i].isLocked = false;
    
    }
    
    void ShowBeltState(){
        
        System.out.print("-->| ");
        
        for(int a = 0; a < BeltSlots.length; a++){
        
            
            System.out.print(BeltSlots[a].type + "  | ");
            
            
        }
        System.out.print("-->");
        System.out.println("");
        System.out.println("");
    
        
    
    
    }
    
    void AddComponentToBelt(double p [], Component a[]) {

        double probs[] = p;
        Component assets[] = a;
        
        //double probs[] = new double[6];
        //String assets[] = new String[6];
        
        /*
        probs[0] = 0.1;
        probs[1] = 0.2;
        probs[2] = 0.3;

        probs[3] = 0.1;
        probs[4] = 0.1;
        probs[5] = 0.2;
        
        
        assets[0] = "A";
        assets[1] = "B";
        assets[2] = "C";

        assets[3] = "NL";
        assets[4] = "Q";
        assets[5] = "Z";
        */

        double left[] = new double[probs.length];
        double right[] = new double[probs.length];

        for (int i = 0; i < probs.length; i++) {

            // left side 
            if (i == 0) {

                left[0] = 0.0;
                right[0] = probs[0];

            } else {

                left[i] = left[i - 1] + probs[i - 1];

                right[i] = probs[i] + right[i - 1];
            }

        }
        
        
        SecureRandom rand = new SecureRandom();

        for (int i = 0; i < 1; i++) {

           
            //System.out.println("ITR " + i);
            

            Double n = rand.nextDouble();

            for (int j = 0; j < probs.length; j++) {
                 

                if (left[j] <= n && n < right[j]) {
                    //System.out.println("CHK " + left[j] + " <= x <" + right[j]);
                   //assets[j]
                    System.out.println("@Conveyor Asset Type: " + assets[j].type);
                    BeltSlots[0] = assets[j];
                    
                    
                }

            }

        }

    }
    
}
