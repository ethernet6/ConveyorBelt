/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workerbeltfactory;


import java.security.SecureRandom;


/**
 *
 * @author James
 */
public class Action {
    
    // Possible actions: Action (value)
    
    // Action 0 - Assembling or component not needed
    // Action 1 - Component is an A Type component - take block
    // Action 2 - Component is a B or C Type component - take block
    // Action 4 - Return finished porduct to belt
            

    Worker ww1; // Row 1 Worker
    Worker ww2; // Row 2 Worker

    public Action() {

    }

    Conveyor getActions(Worker w1, Worker w2, Conveyor belt, int x) {

        // Get workers to propose a new action based on whether they are collecting components
        // either placing products or still working on their assembly.
        int action01 = w1.ProposeAction(belt, x);
        int action02 = w2.ProposeAction(belt, x);
        
        /*System.out.println("A1 : "+ action01);
        System.out.println("PM1 " + w1.ProductMade);
        System.out.println("CR tick " + w1.creationTick);
        System.out.println("TV: " + x);
        System.out.println("");
        System.out.println("A2 : "+ action02);
         System.out.println("PM2 " + w2.ProductMade);
         System.out.println("CR tick " + w1.creationTick);*/

        //Execute proposed actions - resolve any conflicts e.g. taking or placing components/products on the belt at the same time.
        
        // Resolve actions based on knowledge of a worker's hands and desired action if both are not still assembling
        if (AreActionsConflicted(action01, action02)) {
            belt = ResolveActions(belt, x, w1, w2);
        } 
        
        if (action02 == 4 && action01 == 4) {
                        //System.out.println("ATX 404");
        }
        // If one worker is still assembling then direct the other worker as to what to do
        else {

            if (action02 == 0 && action01 == 0) {
                w1.showBlocks();
                w2.showBlocks();
                SetWorker(w1, w2);
            }

            if (action01 == 0 && action02 > 0) {
                w1.showBlocks();
                belt = w2.AcquireMatchingComponent(belt, x);
                SetWorker(w1, w2);
            }

            if (action01 > 0 && action02 == 0) {

                belt = w1.AcquireMatchingComponent(belt, x);
                w2.showBlocks();
                SetWorker(w1, w2);
            }

        }

        return belt;
    }

    Worker UpdateWorker1() {
        return ww1;
    }
    
    Worker UpdateWorker2() {
        return ww2;
    }

    void SetWorker(Worker w1, Worker w2) {

        ww1 = w1;
        ww2 = w2;
    }

    boolean AreActionsConflicted(int a, int b) {
        boolean flag = false;

        boolean checkA = isZeroValue(a);

        if (!checkA && !isZeroValue(b)) {
            flag = true;
            // Actions are conflicted if both actions are non zero
        }

        return flag;
    }

    Conveyor ResolveActions(Conveyor c, int SimulationTime, Worker w1, Worker w2) {

        SecureRandom randomGenerator = new SecureRandom();
        if (randomGenerator.nextInt(2) > 0) {
            c = w1.AcquireMatchingComponent(c, SimulationTime);
            w2.showBlocks();

            SetWorker(w1, w2);

        } else {
            w1.showBlocks();
            c = w2.AcquireMatchingComponent(c, SimulationTime);

            SetWorker(w1, w2);
        }

        return c;
    }

    boolean isZeroValue(int d) {
        
        // If a worker action is 0 (zero) then they are assembling if not they are collecting blocks or handing in finished products

        boolean flag = false;

        if (d == 0) {
            flag = true;
        }

        return flag;

    }
}
