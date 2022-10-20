# DC-assignment
Distributed Computing Sem-01 july-Dec 2022 BITS PILANI Assignment

## System Requirement
- java 11.0.13 2021-10-19 LTS
- Java(TM) SE Runtime Environment 18.9 (build 11.0.13+10-LTS-370)
- Java HotSpot(TM) 64-Bit Server VM 18.9 (build 11.0.13+10-LTS-370, mixed mode)
- Designed and developed on MacOS (can also run on linux/ubuntu env. Can't Compartible with Windows)
- Implemented with the help of Java Socket Programming, and Threading. Portes mentioned in `sites.config` must be available and accessable in your system.

## Assumption
- This Application Suzuki–Kasami’s Broadcast Algorithm for implementing distributed mutual exclusion assumes there will be only 4 Sites (site deatils mentioned in sites.config) in the system who tries to access Critial Section `CS`.
- `Sites` here means 4 running instance of this application, in 4 different Terminal, in the same system.
- This Programs Assumes, initially `Site 1` (which is running in first Terminal) Holds the Token, all other Sites are in ideal state or tries to acquire and access `CS`.
- Executing a `CS` by a Site is simply a `Sleep()` statement for 5-15 seconds..

## Compile & Run
- chdir to `src` directory
- Run below command to Compile the code.
    - `javac -d ../build *.java`
- chdir to `build` and run below command to create executable jar file.
    - `jar cvfe Suzuki-Kasami.jar SuzukiKasami *.class`
- Run the `.jar` file in 4 different Terminal with same command below.
    - `java -jar Suzuki-Kasami.jar`

## Steps
- Each Terminal runs a single Site in your local system.
- Run the `.jar` file in 4 different Terminal with same command below.
    - `java -jar Suzuki-Kasami.jar`
- Once the program starts, it will ask the site number : 
    - `Enter site number (1-4):`
    - here in first Terminal, enter 1, in second, third and forth Terminal, enter 2, 3, 4 respectively
    - wait for a moment until all sites are initialized and running
- After entering the corresponding site number in corresponding Terminal, it will ask:
    - `Press ENTER to enter CS:`    (this will display in all 4 Terminals)
- This Programs Assumes, initially `Site 1` (which is running in first Terminal) Holds the Token
- If you decide, lets `Site 2` next acquire and execute the `CS` then goto second Terminal and hit `Enter`
- Above step you can also perform on other Terminal at a time and watch the Output Printing on the Terminal.
- type `quit` or `Ctrl+c` in Terminal to Exit.