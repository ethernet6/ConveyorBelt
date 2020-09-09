/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workerbeltfactory;

import java.security.SecureRandom;
import java.util.*;
/**
 *
 * @author James
 */
public class testGround {

    public static void main(String[] args) {

        //SecureRandom WorkerSearch = new SecureRandom();
        Random rand = new Random();
        //int n = rand.nextInt();
        int WorkerSelection = rand.nextInt(5);
        WorkerSelection += 1;
        for (int a = 0; a < 50; a++) {
            int n = rand.nextInt(2);
            System.out.println(n);
            
        }

    }

}
