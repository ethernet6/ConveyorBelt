# ConveyorBelt

This package contains various classes that when complied and executed from the main method in the Simulation class meet the requirements of the ARM Conveyor Belt puzzle.

## Install

Use Eclipse or Netbeans to manage & run the Java project or run the package from the command line

## Run Program

Run the program from the Simulation class, Simulation.java, here adjust simulation parameters e.g. conveyor belt length, simulation run-time and 
number of belt slots that are serviced by workers (WorkstationsUsed).

Run the program from here by executing the main method in this class once parameters have been selected.

## Class files

Action.java - Worker actions co-ordinated here and then executed by the workers (Worker.java)

Component.java - All parts (A, B, C), products (P, Q) and empty slots on the belt (N) are component types and are objects based on this class.

Conveyor.java - All properties concerning the belt and the maintenance of its state are handled in this class.

Simulation.java - Handles the setup and running of specified simulations with user-set parameters

Worker.java - All actions concerning the acquisition of components, manufacture/depositing of products, hand tracking, component-tracking and next-action proposals
