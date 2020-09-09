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
    boolean ComponentA_Collected;
    boolean ComponentT_Collected;
    boolean ProductMade;

    int WorkerId;
    int SideOfBelt = 1;

    Component alpha; // type A
    Component beta;  // type B or C

    Component product;

    String alphaType = "A";
    String betaTypeOne = "B";
    String betaTypeTwo = "C";

    String EmptyType = "N";

    String madeProductOne = "P";
    String madeProductTwo = "Q";
    
    
    int creationTick;
    int ProductionTime = 4;
    int CurrentTick;

    Worker(int n) {
        LeftHandEmpty = true;
        RightHandEmpty = true;

        ComponentA_Collected = false;
        ProductMade = false;
        WorkerId = n;
        
        alpha =new Component("N");
        beta =new Component("N");;

    }
    
    Worker(int n, int atime) {
        LeftHandEmpty = true;
        RightHandEmpty = true;

        ComponentA_Collected = false;
        ProductMade = false;
        WorkerId = n;
        
        alpha =new Component("N");
        beta =new Component("N");;
        SideOfBelt = atime;
        //ProductionTime = atime;

    }

    Conveyor AcquireMatchingComponent(Conveyor c, int simTick) {

        //boolean flag = false;
        
        CurrentTick = simTick;

        if (isComponent(c.BeltSlots[WorkerId]) && isAHandFree() && !c.BeltSlots[WorkerId].isLocked) {

            //is a component ?
            
            System.out.println("IS COMP?" + SideOfBelt);
            
            
            boolean isComponentA = isComponentTypeA(c.BeltSlots[WorkerId]);
            if (isComponentA && !ComponentA_Collected && !c.BeltSlots[WorkerId].isLocked) {

                //c.lockSlot(WorkerId);

                alpha = c.BeltSlots[WorkerId];
                ComponentA_Collected = true;
                LeftHandEmpty = false;
                RemoveComponent(c);
                
                //c.unlockSlot(WorkerId);
                
                //flag = true;
            }
            if (!isComponentA &&!c.BeltSlots[WorkerId].isLocked && !ComponentT_Collected) {

                //c.lockSlot(WorkerId);
                beta = c.BeltSlots[WorkerId];
                ComponentT_Collected = true;
                RightHandEmpty = false;
                RemoveComponent(c);
                
                //c.unlockSlot(WorkerId);
                //flag = true;
            }
            
            
            if(c.BeltSlots[WorkerId].isLocked){
                 System.out.println("Locked by W:"+ WorkerId + "");
            }
        }
        
        if(!isAHandFree()){
            
            System.out.println("NHF");
            if(!ProductMade){
                System.out.println("PM WWW ON");
                creationTick = c.tick;
                CreateProduct(c);
            }
            
            if(ProductionTimeoutElapsed() && ProductMade){
                System.out.println("TIMEOUT ON WWW PM");
                DepositProduct(c);
            }
            
        }
        showBlocks();
        //return flag;
        return c;
    }
    
    void showBlocks(){
        
        System.out.println("W"+ SideOfBelt + ": "+ "("+ alpha.type + "," + beta.type +")");
    }
    
    boolean ProductionTimeoutElapsed(){
    
        boolean flag = false;
    
         if( CurrentTick >= (creationTick + ProductionTime) ){
             flag = true;
         }
         
         return flag;
    
    }
    
    boolean isAHandFree(){
        
        boolean flag = false;
        
       if(LeftHandEmpty && RightHandEmpty){
            flag = true;
        } 
        
        if(LeftHandEmpty && !RightHandEmpty){
            flag = true;
        }else if(!LeftHandEmpty && RightHandEmpty){
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
    
    
    boolean isEmpty(Component m){
    
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
                        
                        System.out.println("PM");
                        System.out.println("p_" +AssembledProduct.type);
                        
                        
                        /*if( creationTick == (creationTick + ProductionTime) ){
                            DepositProduct(c);
                        }*/
                        
                    } //if (beta.type.equals(betaTypeTwo)) {
                    else {

                         AssembledProduct = new Component("Q");
                        product = AssembledProduct;
                        ProductMade = true;
                        //c.unlockSlot(WorkerId);
                        System.out.println("PM");
                        System.out.println("p_" +AssembledProduct.type);
                        /*if( creationTick == (creationTick + ProductionTime) ){
                            DepositProduct(c);
                        }*/

                    }
                }

            }
        }
    }
    
    void DepositProduct(Conveyor c){
        
        System.out.println("attempt to deposit");
        if(!c.BeltSlots[WorkerId].isLocked && isEmpty( c.BeltSlots[WorkerId]) ){
        
            c.lockSlot(WorkerId);
            c.BeltSlots[WorkerId] = product;
             c.unlockSlot(WorkerId);
             
             System.out.println("AT DEPOT YES");
             alpha = new Component("N");
             beta  = new Component("N");
             ComponentA_Collected = false;
             ComponentT_Collected = false;
                LeftHandEmpty = true;
                RightHandEmpty = true;
                ProductMade = false;
           // c.BeltSlots[WorkerId].isLocked = false;
        }
        
        
        
    }
    
    void RemoveComponent(Conveyor c){
        
        c.BeltSlots[WorkerId] = new Component("N");
        
    }
    
    
        int ProposeAction(Conveyor c, int simTick) {

        //boolean flag = false;
        int[] vote = new int[1];
        
        CurrentTick = simTick;

        if (isComponent(c.BeltSlots[WorkerId]) && isAHandFree() && !c.BeltSlots[WorkerId].isLocked) {

            //is a component ?
            
            //System.out.println("IS COMP?" + SideOfBelt);
            
            
            boolean isComponentA = isComponentTypeA(c.BeltSlots[WorkerId]);
            if (isComponentA && !ComponentA_Collected && !c.BeltSlots[WorkerId].isLocked) {

                // collect A
                vote[0] = 1;
            }
            if (!isComponentA &&!c.BeltSlots[WorkerId].isLocked && !ComponentT_Collected) {

                // collect B or C
                vote[0] = 2;
            }
            
            
            
        }
        
        if(!isAHandFree()){
            
            
            if(!ProductMade){
                
                creationTick = c.tick;
                // create product
                
                vote[0] = 3;
            }
            
            if(ProductionTimeoutElapsed() && ProductMade){
                
                // deposit product
                
                vote[0] = 4;
            }
            
            if(!ProductionTimeoutElapsed() && ProductMade){
                
                // still making product - NO ACTION
                
                vote[0] = 0;
            }
            
            
            
        }
        
        //return flag;
        return vote[0];
    }

}
