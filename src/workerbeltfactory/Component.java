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
public class Component {
    
    // A B C types are components
    // P Q   types are products made from  A B C, (A,B) -> P AND (A, C) -> Q
    // N     types represent empty slots on the conveyor belt 
           //for components to be taken and products deposited
    
    String type; // Component type , A, B, C, P, Q, N
    boolean isLocked;
    
    int lockAuthor;
    
    public Component (){
        type = "N"; // N - Null, empty belt slot
        isLocked = false;
    }
    
    public Component (String s){
        
        type = s;
        isLocked = false;
    }
    
    
    
}
