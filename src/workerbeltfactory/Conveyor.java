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
    ArrayList<Component> EndOfAssemblyParts = new ArrayList<Component>();

    int WorkerSlots;

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

        //Now remove item from belt
        EndOfAssemblyParts.add(BeltSlots[LastItemIndex]);

        for (int a = (BeltSlots.length - 2); a >= 0; a--) {

            BeltSlots[a + 1] = BeltSlots[a];
        }

        // insert an empty component at index 0 until it is filled randomly by the 
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

        System.out.println("v");
        System.out.println("^");

        for (int a = 0; a < WorkerSlots; a++) {
            if (a == 0) {

            }
            System.out.print("V    ");
        }
        System.out.println("");
        for (int a = 0; a < BeltSlots.length; a++) {

            if (a < WorkerSlots) {

            }

            if (a == 0) {
                //System.out.print("-->| ");
            }

            System.out.print(BeltSlots[a].type + "  | ");

        }
        //System.out.print("-->");
        System.out.println("");
        for (int a = 0; a < WorkerSlots; a++) {
            System.out.print("^    ");
        }

        System.out.println("");
        System.out.println("");

    }

    void ComputeStats() {

        System.out.println("Number of Objects (including empty slots)");
        System.out.println(EndOfAssemblyParts.size());
        System.out.println("");
        double acomp = 0.0; // A type components total
        double bcomp = 0.0; // B type components total
        double ccomp = 0.0; // C type components total

        double ncomp = 0.0; // N type components [empty slots - that passed by] total

        double pcomp = 0.0; // P type products total
        double qcomp = 0.0; // Q type products total

        //HashMap<Component, String> hmap = new HashMap<Component, String>();
        for (int a = 0; a < EndOfAssemblyParts.size(); a++) {

            //hmap.put(EndOfAssemblyParts.get(a) , EndOfAssemblyParts.get(a).type );
            if (EndOfAssemblyParts.get(a).type == "P") {

                pcomp++;
            }

            if (EndOfAssemblyParts.get(a).type == "Q") {

                qcomp++;
            }

            if (EndOfAssemblyParts.get(a).type == "A") {

                acomp++;
            }

            if (EndOfAssemblyParts.get(a).type == "B") {

                bcomp++;
            }

            if (EndOfAssemblyParts.get(a).type == "C") {

                ccomp++;
            }

            if (EndOfAssemblyParts.get(a).type == "N") {

                ncomp++;
            }

        }

        double products = pcomp + qcomp;

        double pr = 0.0;

        double qr = 0.0;

        if (products != 0.0) {

            pr = (pcomp / products * 100);

            qr = (qcomp / products * 100);
        }

        System.out.println("Products Made " + products);
        System.out.printf("P Type: " + pcomp + "   Ratio: %.1f", pr);
        System.out.print("%");
        System.out.println("");
        System.out.printf("Q Type: " + qcomp + "   Ratio: %.1f", qr);
        System.out.print("%");
        System.out.println("");
        System.out.println("-------------------------------------");
        System.out.println("Components not used : " + ((EndOfAssemblyParts.size() - products) - ncomp));
        System.out.println("");

        double cmp = ((EndOfAssemblyParts.size() - products) - ncomp);

        double ar = 0.0;
        double br = 0.0;
        double cr = 0.0;

        if (cmp != 0.0) {
            ar = (acomp / cmp * 100);

            br = (bcomp / cmp * 100);

            cr = (ccomp / cmp * 100);

        }

        System.out.printf("A Type: " + acomp + "   Ratio: %.1f", ar);
        System.out.print("%");
        System.out.println("");
        System.out.printf("B Type: " + bcomp + "    Ratio: %.1f", br);
        System.out.print("%");
        System.out.println("");
        System.out.printf("C Type: " + ccomp + "    Ratio: %.1f", cr);
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
         * Where 0.0 <= x < 0.2 for P(x) = 0.2 Where 0.2 <= x < 0.4 for P(y) =
         * 0.3
         *
         * etc.
         *
         * Where P(x) + P(y) ... P(n) = 1
         *
         */
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
                    
                    if (!BeltSlots[0].type.equals("N")) {
                        MoveBelt(); // If there is a component at the start of the belt then move the belt to make room for a new component
                    }
                    BeltSlots[0] = assets[j];

                }

            }

        }

    }

}
