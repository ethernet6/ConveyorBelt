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
public class Worker {

    boolean LeftHandEmpty;
    boolean RightHandEmpty;
    boolean ComponentA_Collected; // A type component collection flag
    boolean ComponentT_Collected; // any B or C type component collection flag
    boolean ProductMade;

    int WorkerId; // index of the worker array used as an ID
    int SideOfBelt = 1;

    Component alpha; // type A (Left hand of the assembly worker)
    Component beta;  // type B or C (Right hand of the assembly worker)

    Component product;

    String alphaType = "A"; // String to check for A type components
    String betaTypeOne = "B"; // String to check for B type components
    String betaTypeTwo = "C";   // String to check for C type components

    String EmptyType = "N"; // String to create empty [component] slot on conveyor when A, B, C component is removed by worker.

    String madeProductOne = "P"; // String to create P component
    String madeProductTwo = "Q"; // String to create Q component

    int creationTick; // Simulation time when product assembly starts
    int ProductionTime = 4; // timeout to wait 4 conveyor movements to simulate assembly time
    int CurrentTick; // Current simulation time

    Worker(int n) {
        LeftHandEmpty = true;
        RightHandEmpty = true;

        ComponentA_Collected = false;
        ProductMade = false;
        WorkerId = n;

        alpha = new Component("N");
        beta = new Component("N");;

    }

    Worker(int n, int row) {
        LeftHandEmpty = true;
        RightHandEmpty = true;

        ComponentA_Collected = false;
        ProductMade = false;
        WorkerId = n;

        alpha = new Component("N");
        beta = new Component("N");;
        SideOfBelt = row;
        //ProductionTime = atime;

    }

    Conveyor AcquireMatchingComponent(Conveyor c, int simTick) {

        
        CurrentTick = simTick;
//is a component ? Check the item selected is not an empty [component] slot
        if (isComponent(c.BeltSlots[WorkerId]) && isAHandFree() && !c.BeltSlots[WorkerId].isLocked) {

            
            // Check the component and check if it is an A type and if it is required
            boolean isComponentA = isComponentTypeA(c.BeltSlots[WorkerId]);
            if (isComponentA && !ComponentA_Collected && !c.BeltSlots[WorkerId].isLocked) {

                
                alpha = c.BeltSlots[WorkerId];
                ComponentA_Collected = true;
                LeftHandEmpty = false;
                RemoveComponent(c);

                
            }
            if (!isComponentA && !c.BeltSlots[WorkerId].isLocked && !ComponentT_Collected) {
                    // Check the component and check if it is an B or C type and if it is required
                
                beta = c.BeltSlots[WorkerId];
                ComponentT_Collected = true;
                RightHandEmpty = false;
                RemoveComponent(c);

                
            }

           
        }

        if (!isAHandFree()) {

           
            if (!ProductMade) {
                
                creationTick = c.tick;
                CreateProduct(c);
            }

            if (ProductionTimeoutElapsed() && ProductMade) {
               
                DepositProduct(c);
            }

        }
        showBlocks(); // Show components acquired
        //return flag;
        return c;
    }

    // Show components acquired
    void showBlocks() {

        System.out.println("W" + SideOfBelt + ": " + "(" + alpha.type + "," + beta.type + ")");
    }

    boolean ProductionTimeoutElapsed() {

        boolean flag = false;

        if (CurrentTick >= (creationTick + ProductionTime)) {
            flag = true;
        }

        return flag;

    }

    boolean isAHandFree() {

        boolean flag = false;

        if (LeftHandEmpty && RightHandEmpty) {
            flag = true;
        }

        if (LeftHandEmpty && !RightHandEmpty) {
            flag = true;
        } else if (!LeftHandEmpty && RightHandEmpty) {
            flag = true;
        }

        return flag;

    }

    boolean isComponentTypeA(Component m) {

        boolean flag = false;

        if (m.type.equals(alphaType)) {
            flag = true;
        }

        return flag;
    }

    boolean isEmpty(Component m) {

        boolean flag = false;

        if (m.type.equals(EmptyType)) {
            flag = true;
        }

        return flag;

    }

    boolean isComponent(Component m) {

        boolean flag = false;

        if (!m.type.equals(EmptyType) && !m.type.equals("P") && !m.type.equals("Q")) {
            flag = true;
        }

        return flag;
    }

    boolean CorrectComponentChosen() {

        boolean CorrectBlock = false;

        if (alpha.type.equals(alphaType)) {

            if (beta.type.equals(betaTypeOne) || beta.type.equals(betaTypeTwo)) {
                CorrectBlock = true;
            }
        }

        return CorrectBlock;

    }

    // Create a product P or Q depending on components collected
    void CreateProduct(Conveyor c) {

        Component AssembledProduct;

        if (!LeftHandEmpty && !RightHandEmpty) {

            boolean block = CorrectComponentChosen();
            if (block) {

                if (alpha.type.equals(alphaType)) {

                    if (beta.type.equals(betaTypeOne)) {

                        AssembledProduct = new Component("P");
                        product = AssembledProduct;
                        ProductMade = true;

                        
                    }
                    else {

                        AssembledProduct = new Component("Q");
                        product = AssembledProduct;
                        ProductMade = true;
                        

                    }
                }

            }
        }
    }

    // Place the product down on the belt when complete. Function will called continuously each simulation
    // cycle until product is placed down. Until the no new components will be collected.
    void DepositProduct(Conveyor c) {

        
        if (isEmpty(c.BeltSlots[WorkerId])) {

            c.lockSlot(WorkerId);
            c.BeltSlots[WorkerId] = product;
            c.unlockSlot(WorkerId);

            
            alpha = new Component("N");
            beta = new Component("N");
            ComponentA_Collected = false;
            ComponentT_Collected = false;
            LeftHandEmpty = true;
            RightHandEmpty = true;
            ProductMade = false;
           
        }

    }
    // Remove a component from the belt and leave an empty slot in its place
    void RemoveComponent(Conveyor c) {

        c.BeltSlots[WorkerId] = new Component("N");

    }

    // Propose a new possible action to take depending on components in hand and on the belt
    int ProposeAction(Conveyor c, int simTick) {

        int[] vote = new int[1];

        CurrentTick = simTick;

        if (isComponent(c.BeltSlots[WorkerId]) && isAHandFree() && !c.BeltSlots[WorkerId].isLocked) {

            boolean isComponentA = isComponentTypeA(c.BeltSlots[WorkerId]);
            if (isComponentA && !ComponentA_Collected && !c.BeltSlots[WorkerId].isLocked) {

                // collect A type component
                vote[0] = 1;
            }
            if (!isComponentA && !c.BeltSlots[WorkerId].isLocked && !ComponentT_Collected) {

                // collect B or C type component
                vote[0] = 2;
            }

        }

        if (!isAHandFree()) {

            if (!ProductMade) {

                creationTick = c.tick;
                // create product

                //vote[0] = 3;
            }

            if (ProductionTimeoutElapsed() && ProductMade) {

                // deposit product
                vote[0] = 4;
            }

            if (!ProductionTimeoutElapsed() && ProductMade) {

                // still making product - NO ACTION
                vote[0] = 0;
            }

        }

        //return flag;
        return vote[0];
    }

}
