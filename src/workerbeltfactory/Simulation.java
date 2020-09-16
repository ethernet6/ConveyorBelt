/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workerbeltfactory;

/**
 *
 * @author James
 */
public class Simulation {

    Component[] parts;
    double[] Probabilities;
    Worker[] row1;
    Worker[] row2;

    int Workstations;

    Conveyor Conveyorbelt;

    int BeltLength;

    int SimulationLength;

    Simulation(Component[] cmp, double[] pb) {

        parts = cmp;
        Probabilities = pb;

    }

    void PopulateWorkers(int WorkstationsUsed) {

        row1 = new Worker[WorkstationsUsed];
        row2 = new Worker[WorkstationsUsed];
        Workstations = WorkstationsUsed;
        
        Conveyorbelt.WorkerSlots = WorkstationsUsed;

        for (int a = 0; a < Workstations; a++) {

            row1[a] = new Worker(a); // populate row 1 workers
            row2[a] = new Worker(a, 2); // populate row 2 workers
        }
    }

    void CreateBelt(int m_beltLength) {

        Conveyorbelt = new Conveyor(m_beltLength);
        BeltLength = m_beltLength;
    }

    void SetSimulationLength(int SimTime) {

        SimulationLength = SimTime;
    }
    
    int BeltLengthCheck(int b){
    
        int default_length = 5;
        if(b < 1){
         
            System.out.println("ERROR @BeltLengthCheck");
            System.out.println("Belt Length cannot be zero");
            System.out.println("Default length used instead. Length = 5 (units)");
            b = default_length;
            System.out.println("");
            System.out.println("");
        }
        
        return b;
    }
    
    int EnoughWorkersCheck(int w,int b){
    
        if(w > b || w < 0){
            w = b;
            System.out.println("ERROR @EnoughWorkersCheck");
            System.out.println("Not enough workers to service belt");
            System.out.println("Workers assigned will be set to twice the"
                    + " belt length. One worker each side to a slot");
            System.out.println("");
            System.out.println("New Settings:");
            System.out.println("-------------------------");
            System.out.println("Belt length (same) " + b);
            System.out.println("No. of workers per side " + w);
            System.out.println("");
            System.out.println("");
            
        }
        
        return w;
    }

    void SetupSimulation(int w, int b, int s) {
        b = BeltLengthCheck(b);
        CreateBelt(b);
        
        w = EnoughWorkersCheck(w,b);
        PopulateWorkers(w);
        
        SetSimulationLength(s);

    }

    void RunSimulation() {

        for (int x = 0; x < SimulationLength; x++) {

            Conveyorbelt.tick = x;

            System.out.println("Step " + (x + 1));

            Conveyorbelt.AddComponentToBelt(Probabilities, parts);

            System.out.println("Before Acquisition of Parts");
            System.out.println("NEW COMPONENT ADDED");
            Conveyorbelt.ShowBeltState();
            System.out.println("");

            for (int b = 0; b < Workstations; b++) {

                System.out.println("Workstation " + b);
                Action a1 = new Action();

                Conveyorbelt = a1.getActions(row1[b], row2[b], Conveyorbelt, x);
                row1[b] = a1.UpdateWorker1();
                row2[b] = a1.UpdateWorker2();
                
                

            }

            Conveyorbelt.MoveBelt();

            System.out.println("After ACQ -- belt moves +1");
            Conveyorbelt.ShowBeltState();

        }

        System.out.println("");
        System.out.println("");
        System.out.println("END OF SIMULATION");
        System.out.println("");
        Conveyorbelt.ComputeStats();

    }

    public static void main(String[] args) {

        
        Component[] AssetsToGenerate = new Component[]{
            new Component("A"),
            new Component("B"),
            new Component("C"),
            new Component("N")
        };

        double[] discreteProbabilities = new double[]{0.4, 0.2, 0.2, 0.2};

        int SimulationLength = 1;
        int ConveyorLength = 6;
        int WorkstationsUsed = 6;

        Simulation sim = new Simulation(AssetsToGenerate, discreteProbabilities);

        sim.SetupSimulation(WorkstationsUsed, ConveyorLength, SimulationLength);
        sim.RunSimulation();
    }
}
