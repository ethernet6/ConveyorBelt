/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workerbeltfactory;

import java.util.Arrays;

/**
 *
 * @author James
 */
public class Test3 {
    
    public static void main (String args[] ){
    
        int[] arr = {17,18,5,4,6,1};
        
        int[] aa = replaceElements(arr);
        
        System.out.println(Arrays.toString(aa));
    
    }
    
    
     public static int[] replaceElements(int[] arr) {
        
        int temp = 0;
        
        int[] arrCopy = new int[arr.length];
        
        int b = -1;
        
        for(int x = arr.length-1; x > -1 ; x-- ){
            
            
            if(x == arr.length-1){
                
                 temp = arr[arr.length-1];
        
                 
                 System.out.println(Arrays.toString(arrCopy));
            }
             if(x == arr.length-2){
                 
                 int max = 0;
                 
                 for(int pos = x+1; pos < arr.length ; pos++){
                     
                     if (arr[pos] > max){
                         System.out.println(" NEG 2 " + arr[pos]);
                         max = arr[pos];
                     }
                     
                 }
                 
                 arrCopy[arr.length-2] = max;
                 
                 System.out.println(Arrays.toString(arrCopy));
                 
                 
             }
            
            
            else{
                
                int max = 0;
                 
                 for(int pos = x+1; pos < arr.length ; pos++){
                     
                     if (arr[pos] > max){
                         
                         max = arr[pos];
                     }
                     
                 }
                 
                 arrCopy[x] = max;
                
            }
        }
        
        arrCopy[arr.length-1] = b;
        
        
        return arrCopy;
        
    }
}
