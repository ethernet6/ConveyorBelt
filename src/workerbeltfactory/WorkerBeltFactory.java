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
    
    Component[] parts;
    double[] Probabilities;
    Worker[] row1;
    Worker[] row2;
    
    int Workstations;
    
    Conveyor Conveyorbelt;
    
    int BeltLength;
    
    int SimulationLength;
    
    
    WorkerBeltFactory(Component[] cmp, double[] pb){
        
      parts = cmp;
      Probabilities = pb;
      
    
    }
    
    void PopulateWorkers( int WorkstationsUsed){
    
        
        row1 = new Worker[WorkstationsUsed];
      row2 = new Worker[WorkstationsUsed];
      Workstations = WorkstationsUsed;
      
        for (int a = 0; a < Workstations; a++) {

            row1[a] = new Worker(a);
            row2[a] = new Worker(a, 2);
        }
    }
    
    void CreateBelt(int m_beltLength){
    
        Conveyorbelt = new Conveyor(m_beltLength);
        BeltLength = m_beltLength;
    }
    
    void SetSimulationLength(int SimTime){
    
        SimulationLength = SimTime;
    }
    
    void SetupSimulation(int w, int b , int s){
    
        PopulateWorkers(w);
        CreateBelt(b);
        SetSimulationLength(s);
        
    
    }
    
    void RunSimulation(){
    
        for(int x=0; x< SimulationLength; x++){
            
            Conveyorbelt.tick = x;
            
            System.out.println("Step " + (x + 1));
            
            Conveyorbelt.AddComponentToBelt(Probabilities, parts);
            
            System.out.println("Before ACQ");
            Conveyorbelt.ShowBeltState();
            System.out.println("");
            
            for (int b = 0; b < Workstations; b++) {
                
                Action a1 = new Action();
            
            Conveyorbelt = a1.getActions(row1[b], row2[b], Conveyorbelt, b);
            
            
            }
            
            Conveyorbelt.MoveBelt();
            
            System.out.println("After ACQ");
            Conveyorbelt.ShowBeltState();
        
        }
        
        System.out.println("");
        System.out.println("");
        System.out.println("END OF SIMULATION");
        System.out.println("");
        Conveyorbelt.ComputeStats();
    
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        
       
        
        
        Component[] AssetsToGenerate = new Component[]{
            new Component("A"),
            new Component("B"),
            new Component("C"),
            new Component("N")
        };
        
        double[] discreteProbabilities = new double[]{0.4, 0.2, 0.2, 0.2};

        int SimulationLength = 100;
        int ConveyorLength = 14;
        int WorkstationsUsed = 2;

        //CreateComponent(discreteProbabilities, AssetsToGenerate);
        // Deploy workers
        Worker[] workers1 = new Worker[WorkstationsUsed];
        Worker[] workers2 = new Worker[WorkstationsUsed];

        for (int a = 0; a < WorkstationsUsed; a++) {

            workers1[a] = new Worker(a);
            workers2[a] = new Worker(a, 2);
        }

        
        System.out.println("");

        Conveyor belt = new Conveyor(ConveyorLength);

        

        //start simulation
        for (int x = 0; x < SimulationLength; x++) {
            belt.tick = x;
            //CreateComponent(discreteProbabilities,AssetsToGenerate);
            System.out.println("Step " + (x + 1));

            

            belt.AddComponentToBelt(discreteProbabilities, AssetsToGenerate);
            System.out.println("Before ACQ");
            belt.ShowBeltState();
            System.out.println("");
            
            System.out.println("wwqa "+ workers1.length);

            for (int b = 0; b < workers1.length; b++) {

                
                System.out.println("Workstation " + b);

                // check action
                int action01 = workers1[b].ProposeAction(belt, x);
                int action02 = workers2[b].ProposeAction(belt, x);

                
                //int action01 = 2;
                //int action02 = 3;        
                
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

                

            }

            if (x >= 0) {
                belt.MoveBelt();
            }

            //if()
            System.out.println("After ACQ");
            belt.ShowBeltState();

        } // END OF SIMULATION
        
        System.out.println("");
        System.out.println("");
        System.out.println("END OF SIMULATION");
        System.out.println("");
        belt.ComputeStats();

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

    
}
