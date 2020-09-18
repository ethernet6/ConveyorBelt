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

            Conveyorbelt.tick = x; // Current simulation time step

            System.out.println("Step " + (x + 1));

            Conveyorbelt.AddComponentToBelt(Probabilities, parts);

            System.out.println("Before Acquisition of Parts");
            System.out.println("NEW COMPONENT ADDED");
                Conveyorbelt.ShowBeltState();
            System.out.println("");

            for (int b = 0; b < Workstations; b++) {

                System.out.println("Workstation " + b);
                Action a1 = new Action();

                // Get new actions proposed by the workers based on belt state and the contents of their hands
                // Execute the proposed actions and return the new belt state
                Conveyorbelt = a1.getActions(row1[b], row2[b], Conveyorbelt, x);
                
                // Update the workers at this slot on the conveyor belt of the actions taken and any new components taken or placed
                row1[b] = a1.UpdateWorker1();
                row2[b] = a1.UpdateWorker2();
                
                

            }

            Conveyorbelt.MoveBelt();

            System.out.println("After Acquisition -- belt moves +1 slot");
            Conveyorbelt.ShowBeltState();

        }

        System.out.println("");
        System.out.println("");
        System.out.println("END OF SIMULATION");
        System.out.println("");
        Conveyorbelt.ComputeStats();

    }

    public static void main(String[] args) {

        // Components to be made at random
        Component[] AssetsToGenerate = new Component[]{
            new Component("A"),
            new Component("B"),
            new Component("C"),
            new Component("N")
        };

        // The proabilities inidicate the chance the asset in input order will be generated
        double[] discreteProbabilities = new double[]{0.4, 0.2, 0.2, 0.2};

        int SimulationLength = 100;
        int ConveyorLength = 6;
        int WorkstationsUsed = 6; // Each slot of the belt will have a station but these may be less than the number of slot of the belt

        // Therefore workers will be placed on both rows 1 & 2 (2 per belt slot)
        
        Simulation sim = new Simulation(AssetsToGenerate, discreteProbabilities);

        // Setup the simulation - populate the workers, belt and other variables 
        sim.SetupSimulation(WorkstationsUsed, ConveyorLength, SimulationLength);
        
        // Run the simulation with the parameters declared above
        sim.RunSimulation();
    }
}
