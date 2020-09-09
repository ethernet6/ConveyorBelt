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
    
    String type;
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
