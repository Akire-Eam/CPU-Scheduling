package com.example.cmsc125_lab3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;


public class Main {
    static ArrayList<ArrayList<String>> m1 = new ArrayList<>();
    static ArrayList<Process> processList = new ArrayList<>();

    static void readCSV(ArrayList<ArrayList<String>> m1) {
        Scanner stringInput;
        String fileName;
        File file;

        do {
            try {
                System.out.println("Enter csv filename (include.csv):");
                stringInput = new Scanner(System.in);
                fileName = stringInput.nextLine();
                file = new File(fileName);
                Scanner sc = new Scanner(file);
                break;

            } catch (NullPointerException n) {
                System.out.println("No input detected. Enter a valid filename.");
            } catch (FileNotFoundException err) {
                System.out.println("File not found. Enter a valid filename");
            }
        } while (true);

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            int counter = 0;
            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] values = line.split("\\s+");
                ArrayList<String> row = new ArrayList<>();

                if (values.length >= 3) {
                    for (String value : values) {
                        String cleanedValue = value.replaceAll("\\P{Print}", "");
                        row.add(cleanedValue.trim());
                    }

                    row.add(0, String.valueOf(counter + 1));
                    m1.add(row);
                    counter++;
                } else {
                    System.out.println("Skipping line with insufficient values: " + line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void assignToArray(ArrayList<ArrayList<String>> m1, ArrayList<Process> processList) {
        for (ArrayList<String> row : m1) {
            Process p = new Process(Integer.parseInt(row.get(0)), Integer.parseInt(row.get(1)), Integer.parseInt(row.get(2)), Integer.parseInt(row.get(3)), false);
            processList.add(p);
        }
    }

    static boolean isATsame(ArrayList<Process> processList){

        boolean areAllSame = true;
        int firstValue = processList.get(0).getAt();
        for (int i = 1; i < processList.size(); i++) {
            int currentValue = processList.get(i).getAt();
            if (currentValue != firstValue) {
                areAllSame = false;
                break;
            }
        }
        return areAllSame;
    }
    static void fcfsAlgo(ArrayList<Process> processList) {
        System.out.println("FIRST COME FIRST SERVE ALGORITHM: ");
        printHeader();

        processList.sort(new Comparator<Process>() {
            @Override
            public int compare(Process p1, Process p2) {
                if (p1.getAt() != p2.getAt()) {
                    return Integer.compare(p1.getAt(), p2.getAt());
                } else if (p1.getBt() != p2.getBt()) {
                    return Integer.compare(p1.getBt(), p2.getBt());
                } else {
                    return Integer.compare(p1.getPn(), p2.getPn());
                }
            }
        });

        if (isATsame(processList)) {
            int currentTime = 0;
            for (Process process : processList) {
                int completionTime = process.getBt() + currentTime;
                process.setCt(completionTime);
                process.setTat(completionTime - process.getAt());
                process.setRt(currentTime - process.getAt());
                process.setWt(currentTime - process.getAt());


                currentTime += process.getBt();
            }

        } else {
            int currentTime = 0;
            for (Process process : processList) {
                if (process.getAt() <= currentTime) {
                    int completionTime = process.getBt() + currentTime;
                    process.setCt(completionTime);
                    process.setTat(completionTime - process.getAt());
                    process.setWt(process.getTat() - process.getBt());


                    currentTime += process.getBt();
                } else {
                    currentTime = process.getAt() + process.getBt();
                    int completionTime = currentTime;
                    process.setCt(completionTime);
                    process.setTat(completionTime - process.getAt());
                    process.setWt(process.getTat() - process.getBt());

                }
            }

        }
        printProcesses(processList);

        double totalWaitingTime = processList.stream().mapToDouble(Process::getWt).sum();
        double totalTurnaroundTime = processList.stream().mapToDouble(Process::getTat).sum();

        double averageWaitingTime = totalWaitingTime / processList.size();
        double averageTurnaroundTime = totalTurnaroundTime / processList.size();

        System.out.println("\nAWT: " + averageWaitingTime);
        System.out.println("ATT: " + averageTurnaroundTime);
    }


    static void sjfAlgo(ArrayList<Process> processList) {

        processList.sort(new Comparator<Process>() {
            @Override
            public int compare(Process p1, Process p2) {
                return Integer.compare(p1.getBt(), p2.getBt());
            }
        });

        System.out.println("SHORTEST JOB FIRST ALGORITHM: ");
        printHeader();


        if (isATsame(processList)) {
            int currentTime = 0;



            for (Process process : processList) {
                int completionTime = process.getBt() + currentTime;
                process.setCt(process.getBt() + currentTime);
                process.setTat(completionTime - process.getAt());
                process.setRt(currentTime - process.getAt());
                process.setWt(currentTime - process.getAt());

                currentTime += process.getBt();
            }

        } else {
            ArrayList<Process> processListCopy = new ArrayList<>();
            for (Process process : processList) {
                Process clonedProcess = new Process(process);
                processListCopy.add(clonedProcess);
            }

            int currentTime = 0;
            int endTime = 0;

            while (!processListCopy.isEmpty()) {
                Process shortestProcess = null;

                for (Process process : processListCopy) {
                    if (process.getAt() <= currentTime && !process.isUsed()) {
                        if (shortestProcess == null || process.getBt() < shortestProcess.getBt()) {
                            shortestProcess = process;
                        }
                    }
                }

                if (shortestProcess != null) {
                    shortestProcess.setUsedState(true);
                    int completionTime = shortestProcess.getBt() + currentTime;
                    shortestProcess.setCt(completionTime);
                    shortestProcess.setTat(completionTime - shortestProcess.getAt());
                    shortestProcess.setRt(currentTime - shortestProcess.getAt());
                    shortestProcess.setWt(shortestProcess.getTat() - shortestProcess.getBt());


                    for (Process process : processList) {
                        if (process.getPid() == shortestProcess.getPid()) {
                            process.setCt(completionTime);
                            process.setTat(shortestProcess.getTat());
                            process.setRt(shortestProcess.getRt());
                            process.setWt(shortestProcess.getWt());
                            break;
                        }
                    }

                    currentTime += shortestProcess.getBt();

                    processListCopy.remove(shortestProcess);

                    if (completionTime > endTime) {
                        endTime = completionTime;
                    }
                } else {
                    currentTime++;
                }
            }

        }

        printProcesses(processList);

        double totalWaitingTime = processList.stream().mapToDouble(Process::getWt).sum();
        double totalTurnaroundTime = processList.stream().mapToDouble(Process::getTat).sum();

        double averageWaitingTime = totalWaitingTime / processList.size();
        double averageTurnaroundTime = totalTurnaroundTime / processList.size();

        System.out.println("\nAWT: " + averageWaitingTime);
        System.out.println("ATT: " + averageTurnaroundTime);
    }
    static void srtfAlgo(ArrayList<Process> processList){

        processList.sort(new Comparator<Process>() {
            @Override
            public int compare(Process p1, Process p2) {
                return Integer.compare(p1.getBt(), p2.getBt());
            }
        });

        System.out.println("SHORTEST REMAINING TIME FIRST ALGORITHM: ");
        printHeader();

        if(isATsame(processList)){
            int currentTime = 0;

            for (Process process : processList) {
                int completionTime = process.getBt() + currentTime;
                process.setCt(process.getBt() + currentTime);
                process.setTat(completionTime - process.getAt());
                process.setRt(currentTime - process.getAt());
                process.setWt(currentTime - process.getAt());

                currentTime += process.getBt();
            }

        }
        else{

            ArrayList<Process> remainingTimeList = new ArrayList<>();
            for (Process process : processList) {
                Process clonedProcess = new Process(process);
                remainingTimeList.add(clonedProcess);
            }

            int currentTime = 0;
            int completedProcesses = 0;

            while (completedProcesses < processList.size()) {
                Process shortestProcess = null;
                int shortestBt = Integer.MAX_VALUE;
                int shortestAt = Integer.MAX_VALUE;

                for (Process process : remainingTimeList) {
                    if (process.getAt() <= currentTime && process.getBt() < shortestBt && process.getBt() > 0) {
                        shortestProcess = process;
                        shortestBt = process.getBt();
                        shortestAt = process.getAt();
                    } else if (process.getAt() <= currentTime && process.getBt() == shortestBt && process.getBt() > 0) {
                        if (process.getAt() < shortestAt) {
                            shortestProcess = process;
                            shortestAt = process.getAt();
                        }
                    }
                }

                if (shortestProcess != null) {
                    int currentIndexFirstExec = shortestProcess.getPid();
                    Process process_firstexec = null;
                    for (Process p : processList) {
                        if (currentIndexFirstExec == p.getPid()) {
                            process_firstexec = p;
                            break;
                        }
                    }
                    assert process_firstexec != null;
                    if (!process_firstexec.isUsed()) {
                        process_firstexec.setRt(currentTime - process_firstexec.getAt());
                        process_firstexec.setUsedState(true);
                    }

                    shortestProcess.setBt(shortestProcess.getBt() - 1);

                    if (shortestProcess.getBt() == 0) {
                        completedProcesses++;

                        int currentIndex = shortestProcess.getPid();
                        Process process = null;
                        for (Process p : processList) {
                            if (currentIndex == p.getPid()) {
                                process = p;
                                break;
                            }
                        }

                        assert process != null;
                        process.setCt(currentTime + 1);
                        process.setTat(process.getCt() - process.getAt());
                        process.setWt(process.getTat() - process.getBt());

                        remainingTimeList.remove(shortestProcess);
                    }
                }

                currentTime++;
            }


        }

        printProcesses(processList);

        double totalWaitingTime = processList.stream().mapToDouble(Process::getWt).sum();
        double totalTurnaroundTime = processList.stream().mapToDouble(Process::getTat).sum();

        double averageWaitingTime = totalWaitingTime / processList.size();
        double averageTurnaroundTime = totalTurnaroundTime / processList.size();

        System.out.println("\nAWT: " + averageWaitingTime);
        System.out.println("ATT: " + averageTurnaroundTime);
    }
    static void prioNPAlgo(ArrayList<Process> inputProcessList) {
        ArrayList<Process> processList = new ArrayList<>(inputProcessList);

        processList.sort(new Comparator<Process>() {
            @Override
            public int compare(Process p1, Process p2) {
                if (p1.getAt() != p2.getAt()) {
                    return Integer.compare(p1.getAt(), p2.getAt());
                } else if (p1.getPn() != p2.getPn()) {
                    return Integer.compare(p1.getPn(), p2.getPn());
                } else {
                    return Integer.compare(p1.getPid(), p2.getPid());
                }
            }
        });

        System.out.println("PRIO NP ALGORITHM: ");
        printHeader();

        List<Process> completedProcesses = new ArrayList<>();

        if (isATsame(processList)) {
            int currentTime = 0;

            for (Process process : processList) {
                int completionTime = process.getBt() + currentTime;
                process.setCt(completionTime);
                process.setTat(completionTime - process.getAt());
                process.setRt(currentTime - process.getAt());
                process.setWt(currentTime - process.getAt());

                currentTime += process.getBt();

                completedProcesses.add(process);
            }
        } else {
            int currentTime = 0;

            while (!processList.isEmpty()) {
                Process selectedProcess = null;

                for (Process process : processList) {
                    if (process.getAt() <= currentTime) {
                        if (selectedProcess == null || process.getPn() < selectedProcess.getPn()) {
                            selectedProcess = process;
                        }
                    }
                }

                if (selectedProcess != null) {
                    int completionTime = selectedProcess.getBt() + currentTime;
                    selectedProcess.setCt(completionTime);
                    selectedProcess.setTat(completionTime - selectedProcess.getAt());
                    selectedProcess.setWt(selectedProcess.getTat() - selectedProcess.getBt());
                    selectedProcess.setRt(currentTime - selectedProcess.getAt());

                    currentTime += selectedProcess.getBt();

                    completedProcesses.add(selectedProcess);

                    processList.remove(selectedProcess);
                } else {
                    currentTime++;
                }
            }
        }

        completedProcesses.sort(Comparator.comparingInt(Process::getPid));

        for (Process process : completedProcesses) {
            String row = String.format("|%8s\t\t|%10s\t\t\t|%10s\t\t\t|%8s\t\t|%10s\t\t\t|%10s\t\t\t|%10s\t\t\t|",
                    process.getPid(), process.getPn(), process.getAt(), process.getBt(), process.getCt(), process.getWt(), process.getTat());
            System.out.println(row);
        }

        double totalWaitingTime = completedProcesses.stream().mapToDouble(Process::getWt).sum();
        double totalTurnaroundTime = completedProcesses.stream().mapToDouble(Process::getTat).sum();
        double averageWaitingTime = totalWaitingTime / completedProcesses.size();
        double averageTurnaroundTime = totalTurnaroundTime / completedProcesses.size();

        System.out.println("\nAWT: " + averageWaitingTime);
        System.out.println("ATT: " + averageTurnaroundTime);
    }


    static void prioPAlgo(ArrayList<Process> processList) {
        processList.sort(new Comparator<Process>() {
            @Override
            public int compare(Process p1, Process p2) {
                if (p1.getAt() != p2.getAt()) {
                    return Integer.compare(p1.getAt(), p2.getAt());
                } else if (p1.getPn() != p2.getPn()) {
                    return Integer.compare(p1.getPn(), p2.getPn());
                } else {
                    return Integer.compare(p1.getPid(), p2.getPid());
                }
            }
        });

        System.out.println("PRIO P ALGORITHM: ");
        printHeader();

        if (isATsame(processList)) {
            int currentTime = 0;

            for (Process process : processList) {

                int completionTime = process.getBt() + currentTime;
                process.setCt(process.getBt() + currentTime);
                process.setTat(completionTime - process.getAt());
                process.setRt(currentTime - process.getAt());
                process.setWt(currentTime - process.getAt());

                currentTime += process.getBt();
            }
        }

        else{

            ArrayList<Process> remainingTimeList = new ArrayList<>();
            for (Process process : processList) {
                Process clonedProcess = new Process(process);
                remainingTimeList.add(clonedProcess);
            }

            int currentTime = 0;
            int completedProcesses = 0;

            while (completedProcesses < processList.size()) {
                Process highestPrioProcess = null;
                int shortestPn = Integer.MAX_VALUE;

                for (Process process : remainingTimeList) {
                    if (process.getAt() <= currentTime && process.getPn() < shortestPn && process.getBt() > 0) {
                        highestPrioProcess = process;
                        shortestPn = process.getPn();

                        int currentIndexFirstExec = highestPrioProcess.getPid();
                        Process process_firstexec = null;
                        for (Process p : processList) {
                            if (currentIndexFirstExec == p.getPid()) {
                                process_firstexec = p;
                                break;
                            }
                        }
                        assert process_firstexec != null;
                        if(!process_firstexec.isUsed()){
                            process_firstexec.setRt(currentTime - process_firstexec.getAt());
                            process_firstexec.setUsedState(true);
                        }
                    }
                }

                if (highestPrioProcess != null) {
                    highestPrioProcess.setBt(highestPrioProcess.getBt() - 1);

                    if (highestPrioProcess.getBt() == 0) {
                        completedProcesses++;

                        int currentIndex = highestPrioProcess.getPid();
                        Process process = null;
                        for (Process p : processList) {
                            if (currentIndex == p.getPid()) {
                                process = p;
                                break;
                            }
                        }

                        assert process != null;
                        process.setCt(currentTime + 1);
                        process.setTat(process.getCt() - process.getAt());
                        process.setWt(process.getTat() - process.getBt());

                        remainingTimeList.remove(highestPrioProcess);
                    }
                }
                currentTime++;
            }

        }

        printProcesses(processList);

        double totalWaitingTime = processList.stream().mapToDouble(Process::getWt).sum();
        double totalTurnaroundTime = processList.stream().mapToDouble(Process::getTat).sum();

        double averageWaitingTime = totalWaitingTime / processList.size();
        double averageTurnaroundTime = totalTurnaroundTime / processList.size();

        System.out.println("\nAWT: " + averageWaitingTime);
        System.out.println("ATT: " + averageTurnaroundTime);
    }
    static void roundRobinAlgo(ArrayList<Process> processList, int quantumTime) {
        System.out.println("ROUND ROBIN ALGORITHM: ");
        printHeader();

        processList.sort(new Comparator<Process>() {
            @Override
            public int compare(Process p1, Process p2) {
                if (p1.getAt() != p2.getAt()) {
                    return Integer.compare(p1.getAt(), p2.getAt());
                } else {
                    return Integer.compare(p1.getPid(), p2.getPid());
                }
            }
        });

        int n = processList.size();
        int[] remainingBurstTime = new int[n];

        for (int i = 0; i < n; i++) {
            remainingBurstTime[i] = processList.get(i).getBt();
        }

        int time = 0;
        while (true) {
            boolean done = true;
            for (int i = 0; i < n; i++) {
                if (remainingBurstTime[i] > 0) {
                    done = false;
                    if (remainingBurstTime[i] > quantumTime) {
                        time += quantumTime;
                        remainingBurstTime[i] -= quantumTime;
                    } else {
                        time += remainingBurstTime[i];
                        processList.get(i).setWt(time - processList.get(i).getBt() - processList.get(i).getAt());
                        remainingBurstTime[i] = 0;
                    }
                }
            }
            if (done)
                break;
        }

        for (int i = 0; i < n; i++) {
            processList.get(i).setTat(processList.get(i).getBt() + processList.get(i).getWt());
            processList.get(i).setCt(processList.get(i).getAt() + processList.get(i).getTat());
        }

        printProcesses(processList);

        double totalWaitingTime = processList.stream().mapToDouble(Process::getWt).sum();
        double totalTurnaroundTime = processList.stream().mapToDouble(Process::getTat).sum();

        double averageWaitingTime = totalWaitingTime / processList.size();
        double averageTurnaroundTime = totalTurnaroundTime / processList.size();

        System.out.println("\nAWT: " + averageWaitingTime);
        System.out.println("ATT: " + averageTurnaroundTime);
    }

    private static void printHeader() {
        System.out.println("|\t Process \t|\tPriority Number\t|\tArrival Time\t|\tBurst Time\t|\tCompletion Time\t|\tWaiting Time\t|\tTurnaround Time\t|");
    }

    private static void printProcesses(ArrayList<Process> processList) {

        processList.sort(Comparator.comparingInt(Process::getPid));

        for (Process process : processList) {
            String row = String.format("|%8s\t\t|%10s\t\t\t|%10s\t\t\t|%8s\t\t|%10s\t\t\t|%10s\t\t\t|%10s\t\t\t|",
                    process.getPid(), process.getPn(), process.getAt(), process.getBt(), process.getCt(), process.getWt(), process.getTat());
            System.out.println(row);
        }
    }


    public static void main(String[] args) {
        readCSV(m1);
        assignToArray(m1,processList);
        Scanner scanner = new Scanner(System.in);

        int decision;
        do {
            System.out.println("\nPlease select your scheduling algorithm:");
            System.out.println("1 - FCFS");
            System.out.println("2 - SJF");
            System.out.println("3 - SRTF");
            System.out.println("4 - Round Robin");
            System.out.println("5 - Priority (preemptive)");
            System.out.println("6 - Priority (nonpreemptive)");
            System.out.println("7 - Quit");
            System.out.print("Select Algorithm: ");
            decision = scanner.nextInt();

            switch (decision) {
                case 1:
                    fcfsAlgo(processList);
                    break;
                case 2:
                    sjfAlgo(processList);
                    break;
                case 3:
                    srtfAlgo(processList);
                    break;
                case 4:
                    System.out.print("Input Quantum Time: ");
                    int quantumTime = scanner.nextInt();
                    roundRobinAlgo(processList, quantumTime);
                    break;
                case 5:
                    prioPAlgo(processList);
                    break;
                case 6:
                    prioNPAlgo(processList);
                    break;
                case 7:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid decision. Select a valid option");
            }
        } while (decision != 7);

    }
}