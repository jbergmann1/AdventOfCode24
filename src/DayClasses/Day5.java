package DayClasses;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day5 implements Day {
    @Override
    public void execute() {
        String filePath = Day.filePath + "input5.txt";
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath), StandardCharsets.UTF_8)) {
            String line;
            int index = 0;
            int sum = 0;
            int fixSum = 0;
            String[][] rules = new String[1176][2];
            ArrayList<String[]> updates = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                if (line.contains("|")) {
                    rules[index][0] = line.split("\\|")[0];
                    rules[index][1] = line.split("\\|")[1];
                    index++;
                    continue;
                }
                if (line.isEmpty()) continue;
                String[] pages = line.split(",");
                updates.add(pages);
            }
            for (String[] update : updates) {
                if (checkUpdate(rules, update)) sum += Integer.parseInt(getMiddleNumber(update));
                else fixSum += Integer.parseInt(getMiddleNumber(fixUpdate(rules, update)));
            }
            System.out.println("sum: "+sum+" fixed Sum: "+fixSum);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkUpdate(String[][] rules, String[] update) {
        for (String page : update) {
            LinkedList<String> before = getPredecessors(rules, page);
            LinkedList<String> after = getSuccessors(rules, page, update);
            List<String> updateList = new LinkedList<>(Arrays.asList(update));
            for (String rulePage : before) {
                if (updateList.contains(rulePage) && updateList.indexOf(rulePage) > updateList.indexOf(page)) return false;
            }
            for (String rulePage : after) {
                if (updateList.contains(rulePage) && updateList.indexOf(rulePage) < updateList.indexOf(page)) return false;
            }
        }
        return true;
    }

    private String[] fixUpdate(String[][] rules, String[] update) {
        Set<String> visitedPages = new HashSet<>();
        List<String> fixedUpdate = new LinkedList<>();
        for (String page : update) {
            if (!visitedPages.contains(page)) {
                visit(rules, update, page, getSuccessors(rules, page, update), visitedPages, fixedUpdate);
            }
        }


        return fixedUpdate.toArray(new String[0]);
    }

    private void visit(String[][] rules, String[] update, String page, LinkedList<String> successors, Set<String> visitedPages, List<String> fixedUpdate) {
        if (!visitedPages.contains(page)) {
            visitedPages.add(page);
            successors.forEach(successor -> visit(rules, update, successor, getSuccessors(rules, successor, update), visitedPages, fixedUpdate));
            fixedUpdate.add(page);
        }
    }

    private LinkedList<String> getPredecessors(String[][] rules, String page) {
        LinkedList<String> predecessors = new LinkedList<>();
        for (String[] rule : rules) {
            if (rule[1].equals(page)) predecessors.add(rule[0]);
        }
        return predecessors;
    }

    private LinkedList<String> getSuccessors(String[][] rules, String page, String[] update) {
        LinkedList<String> successors = new LinkedList<>();
        for (String[] rule : rules) {
            if (rule[0].equals(page) && Arrays.asList(update).contains(rule[1])) successors.add(rule[1]);
        }
        return successors;
    }

    private String getMiddleNumber(String[] update) {
        return update[update.length / 2];
    }
}