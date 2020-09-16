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
    Component BeltSlots[];
    ArrayList<Component> BeltsEnd = new ArrayList<Component>();

    public Conveyor() {
        BeltLength = 5;
        BeltSlots = new Component[BeltLength];
        SetupBelt(BeltSlots);
    }

    public Conveyor(int n) {

        BeltLength = n;
        BeltSlots = new Component[BeltLength];
        SetupBelt(BeltSlots);

    }

    private void SetupBelt(Component[] belt) {

        for (int q = 0; q < belt.length; q++) {

            belt[q] = new Component("N"); // fill belt with empty slots
        }

    }

    void MoveBelt() {

        int LastItemOnBelt = BeltSlots.length;
        int LastItemIndex = LastItemOnBelt - 1;

        BeltsEnd.add(BeltSlots[LastItemIndex]);

        //Now remove item from belt
        for (int a = (BeltSlots.length - 2); a >= 0; a--) {

            BeltSlots[a + 1] = BeltSlots[a];
        }

        // insert empty component at index 0 until it is filled randomly by the 
        // ADD function.
        BeltSlots[0] = new Component("N");
    }

    void lockSlot(int i) {

        BeltSlots[i].isLocked = true;

    }

    void unlockSlot(int i) {

        BeltSlots[i].isLocked = false;

    }

    void ShowBeltState() {

        System.out.print("-->| ");

        for (int a = 0; a < BeltSlots.length; a++) {

            System.out.print(BeltSlots[a].type + "  | ");

        }
        System.out.print("-->");
        System.out.println("");
        System.out.println("");

    }

    void ComputeStats() {

        System.out.println("Number of Objects (including empty slots)");
        System.out.println(BeltsEnd.size());
        System.out.println("");
        int acomp = 0;
        int bcomp = 0;
        int ccomp = 0;

        int ncomp = 0;

        int pcomp = 0;
        int qcomp = 0;

        //HashMap<Component, String> hmap = new HashMap<Component, String>();
        for (int a = 0; a < BeltsEnd.size(); a++) {

            //hmap.put(BeltsEnd.get(a) , BeltsEnd.get(a).type );
            if (BeltsEnd.get(a).type == "P") {

                pcomp++;
            }

            if (BeltsEnd.get(a).type == "Q") {

                qcomp++;
            }

            if (BeltsEnd.get(a).type == "A") {

                acomp++;
            }

            if (BeltsEnd.get(a).type == "B") {

                bcomp++;
            }

            if (BeltsEnd.get(a).type == "C") {

                ccomp++;
            }

            if (BeltsEnd.get(a).type == "N") {

                ncomp++;
            }

        }

        float products = pcomp + qcomp;

        System.out.println("Products Made " + products);
        System.out.printf("P Type: " + pcomp  + "   Ratio: %.1f", (pcomp/products * 100));
        System.out.print("%");
        System.out.println("");
        System.out.printf("Q Type: " + qcomp  + "   Ratio: %.1f", (qcomp/products * 100));
        System.out.print("%");
        System.out.println("");
        System.out.println("-------------------------------------");
        System.out.println("Components not used : " + ((BeltsEnd.size() - products) - ncomp) );
        System.out.println("");
        
        float cmp = ((BeltsEnd.size() - products) - ncomp);

        System.out.printf("A Type: " + acomp + "   Ratio: %.1f", (acomp/cmp * 100));
        System.out.print("%");
        System.out.println("");
        System.out.printf("B Type: " + bcomp + "    Ratio: %.1f", (bcomp/cmp * 100));
        System.out.print("%");
        System.out.println("");
        System.out.printf("C Type: " + ccomp + "    Ratio: %.1f", (ccomp/cmp * 100));
        System.out.print("%");
        System.out.println("");
        System.out.println("[N] Type: " + ncomp);

    }

    void AddComponentToBelt(double p[], Component a[]) {

        double probs[] = p;
        Component assets[] = a;

        double left[] = new double[probs.length];
        double right[] = new double[probs.length];

        //Formulating proability ranges for the cummulative probabilities of each event/block 
        /**
         * 
         * Where 0.0 <= x < 0.2 for P(x) = 0.2 
         * Where 0.2 <= x < 0.4 for P(y) = 0.3 
         * 
         * etc.
         * 
         * Where P(x) + P(y) ... P(n) = 1
         **/
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

            
            Double n = rand.nextDouble();

            for (int j = 0; j < probs.length; j++) {

                if (left[j] <= n && n < right[j]) {
                    // using the probabilities provided the item must fall between 
                    // a set range of probability that is equal to its likelihood of 
                    // occurring.
                    
                    // eg. left[j] <= n && n < right[j]
                    // is the same as 0.8 <= to 1 for P(x) = 0.2
                    
                    // A random number between 0 and  1 is chosen and if it falls between this range
                    // then the block accoreded this range is the block set as output.
                    
                    //System.out.println("@Conveyor Asset Type: " + assets[j].type);
                    
                    if(!BeltSlots[0].type.equals("N")){
                        MoveBelt(); // If there is a component at the start of the belt then move the belt to make room for a new component
                    }
                    BeltSlots[0] = assets[j];

                }

            }

        }

    }

}
