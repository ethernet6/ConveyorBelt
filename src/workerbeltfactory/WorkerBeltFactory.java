/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workerbeltfactory;

import java.util.*;
import java.security.SecureRandom;

/**
 *
 * @author James Purcell Date: 6 Sept 2020 Sunday
 */
public class WorkerBeltFactory {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        //Random rand = new Random();
        SecureRandom rand = new SecureRandom();
        int fd = 10;
        /*
        for (int t = 0; t < 15; t++) {
            Double n = rand.nextDouble();
            //int n = rand.nextInt(5);
            //n += 1;

            if (n < .4) {
                // fd = 1; System.out.println("A " + n);
            }
            if (n >= .4 && n < .6) {
                //fd = 2; System.out.println("B " + n);
            }
            if (n >= .6 && n < .8) {
                //fd = 3; System.out.println("C " + n);
            }
            if (n >= .8) {
                //fd = 4; System.out.println("N " + n);
            }

            //System.out.println("RN " + n);
        }
         */

        //String[] AssetsToGenerate = new String[] { "A",   "B",    "C",   "N"};
        Component[] AssetsToGenerate = new Component[]{
            new Component("A"),
            new Component("B"),
            new Component("C"),
            new Component("N")
        };
        double[] discreteProbabilities = new double[]{0.4, 0.2, 0.2, 0.2};

        int SimulationLength = 100;
        int ConveyorLength = 5;

        //CreateComponent(discreteProbabilities, AssetsToGenerate);
        // Deploy workers
        Worker[] workers1 = new Worker[5];
        Worker[] workers2 = new Worker[5];

        for (int a = 0; a < 5; a++) {

            workers1[a] = new Worker(a);
            workers2[a] = new Worker(a, 2);
        }

        SecureRandom WorkerSearch = new SecureRandom();
        //Double n = rand.nextDouble();

        //n += 1;
        System.out.println("");

        Conveyor belt = new Conveyor();

        //System.out.println("Belt LL " + belt.BeltSlots.length);
        //belt.AddComponentToBelt(discreteProbabilities, AssetsToGenerate);
        System.out.println("Belt at 0 " + belt.BeltSlots[0]);
        System.out.println("Belt at 3 " + belt.BeltSlots[3]);

        //start simulation
        for (int x = 0; x < SimulationLength; x++) {
            belt.tick = x;
            //CreateComponent(discreteProbabilities,AssetsToGenerate);
            System.out.println("Step " + (x + 1));

            if (x > 0) {
                belt.MoveBelt();
            }

            belt.AddComponentToBelt(discreteProbabilities, AssetsToGenerate);
            System.out.println("Before ACQ");
            belt.ShowBeltState();
            System.out.println("");

            for (int b = 0; b < workers1.length; b++) {

                int WorkerSelection = WorkerSearch.nextInt(2);
                //System.out.println("ws " + WorkerSelection);
                System.out.println("Workstation " + b);

                // check action
                int action01 = workers1[b].ProposeAction(belt, x);
                int action02 = workers2[b].ProposeAction(belt, x);

                System.out.println("ac " + action01);
                System.out.println("ac " + action02);

                if (DoActionsConflict(action01, action02)) {
                    belt = ResolveAction(belt, x, workers1, workers2, b);
                } 
                
                if (action02 == 4 && action01 == 4) {
                        System.out.println("ATX 404");
                        //belt = workers1[b].AcquireMatchingComponent(belt, x);
                        
                    }
                
                
                else {

                    Random randomGenerator = new Random();
                    if (action02 == 0 && action01 == 0) {
                        workers1[b].showBlocks();
                        workers2[b].showBlocks();
                    } 
                    
                     
                    
                    if(action01 == 0 && action02 > 0){
                        workers1[b].showBlocks();
                        belt = workers2[b].AcquireMatchingComponent(belt, x);
                    }
                    
                    if(action01 > 0 && action02 == 0){
                        
                        belt = workers1[b].AcquireMatchingComponent(belt, x);
                        workers2[b].showBlocks();
                    }

                }

                //belt = workers1[b].AcquireMatchingComponent(belt, x);
                //belt = workers2[b].AcquireMatchingComponent(belt, x);
                //belt = workers1[b].AcquireMatchingComponent(belt, x);
                if (WorkerSelection == 0) {

                } else {

                    //belt = workers2[b].AcquireMatchingComponent(belt, x);
                    //belt = workers1[b].AcquireMatchingComponent(belt, x);
                }

            }

            //if()
            System.out.println("After ACQ");
            belt.ShowBeltState();

        }

    }

    static boolean DoActionsConflict(int a, int b) {
        boolean flag = false;

        boolean checkA = isZeroValue(a);

        if (!checkA && !isZeroValue(b)) {
            flag = true;
        }

        return flag;
    }

    static Conveyor ResolveAction(Conveyor c, int SimulationTime, Worker[] w1, Worker[] w2, int index) {

        Random randomGenerator = new Random();
        if (randomGenerator.nextInt(2) > 0) {
            c = w1[index].AcquireMatchingComponent(c, SimulationTime);
            w2[index].showBlocks();
        } else {
            w1[index].showBlocks();
            c = w2[index].AcquireMatchingComponent(c, SimulationTime);
        }

        return c;
    }

    static boolean isZeroValue(int d) {

        boolean flag = false;

        if (d == 0) {
            flag = true;
        }

        return flag;

    }

    static void CreateComponent1(double p[], String a[]) {

        double probs[] = p;
        String assets[] = a;

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
                    System.out.println("ITR " + i + "> " + assets[j]);
                    System.out.println("");

                }

            }

        }

    }

}
