package com.ryanlaverick;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class TradeSession {
    private final DraftChart draftChart;
    private final Map<Teams, DraftClass> draftClasses;

    public TradeSession() {
        this.draftClasses = new EnumMap<>(Teams.class);
        this.draftChart = new DraftChart(new ArrayList<>());

        this.loadChart();
        this.loadPicks();
    }

    private void loadChart() {
        Scanner input = new Scanner(System.in);
        List<Integer> options = List.of(1, 2);

        int choice = 0;

        while (! options.contains(choice)) {
            System.out.println("Please select the Draft Chart you would like to use:");
            System.out.println("    1. Jimmy Johnson Draft Chart");
            System.out.println("    2. Rich Hill Draft Chart (Revised - 2025)");

            choice = input.nextInt();
        }

        File draftChartFile;
        if (choice == 1) {
            draftChartFile = new File("src/jimmy-johnson-draft-chart.csv");
            System.out.println("You picked: JIMMY JOHNSON DRAFT CHART");
        } else {
            draftChartFile = new File("src/rich-hill-draft-chart.csv");
            System.out.println("You picked: RICH HILL DRAFT CHART (REVISED - 2025)");
        }

        if (! draftChartFile.exists()) {
            try {
                draftChartFile.createNewFile();
            } catch (IOException ignored) {
                System.out.println("Unable to create chart file! Please try again shortly...");
            }
        }

        try (Scanner scanner = new Scanner(draftChartFile)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                String[] components = line.split(",");

                int round = Integer.parseInt(components[0]);
                int pick = Integer.parseInt(components[1]);
                float weight = Float.parseFloat(components[2]);

                this.draftChart.selections().add(new Selection(round, pick, weight));
            }
        } catch (FileNotFoundException ignored) {
            System.out.println("Unable to read chart file! Please try again shortly...");
        }
    }

    private void loadPicks() {
        File picksFile = new File("src/picks.csv");

        if (! picksFile.exists()) {
            try {
                picksFile.createNewFile();
            } catch (IOException ignored) {
                System.out.println("Unable to create picks file! Please try again shortly...");
            }
        }

        try (Scanner scanner = new Scanner(picksFile)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                String[] components = line.split(",");

                int round = Integer.parseInt(components[0]);
                int pick = Integer.parseInt(components[1]);
                String team = String.valueOf(components[2]);

                Optional<Selection> weightSelection = this.draftChart.selections().stream().filter((selection -> selection.round() == round && selection.pick() == pick)).findFirst();
                if (weightSelection.isEmpty()) {
                    System.out.println("Unable to determine weight for Round " + round + ", Pick " + pick);
                    continue;
                }
                float weight = weightSelection.get().weight();

                Teams teams = Teams.tryFrom(team);

                if (teams == null) {
                    continue;
                }

                if (this.draftClasses.containsKey(teams)) {
                    DraftClass draftClass = this.draftClasses.get(teams);
                    draftClass.picks().add(new Selection(round, pick, weight));
                } else {
                    DraftClass draftClass = new DraftClass(new ArrayList<>());
                    draftClass.picks().add(new Selection(round, pick, weight));
                    this.draftClasses.put(teams, draftClass);
                }
            }
        } catch (FileNotFoundException ignored) {
            System.out.println("Unable to read picks file! Please try again shortly...");
        }
    }

    /**
     *
     * @param sending -- This is a list of the picks being sent from your team. Example: 1.31, 2.40
     * @param receiving -- This is a list of the picks being received back from the team you are trading with. Example: 2.59, 3.71
     * @return
     */
    public boolean proposeTrade(Teams sendingFrom, String sending, Teams sendingTo, String receiving) {
        int sendingWeight = 0;
        int receivingWeight = 0;
        boolean tradeSuccess = false;

        DraftClass sendingClass = this.draftClasses.get(sendingFrom);
        List<Selection> sendingSelections = new ArrayList<>();
        for (String sendingPick : sending.split(",")) {
            sendingPick = sendingPick.strip();

            int round = Integer.parseInt(sendingPick.split("\\.")[0]);
            int pick = Integer.parseInt(sendingPick.split("\\.")[1]);

            Optional<Selection> selection = sendingClass.picks().stream().filter(predicate -> predicate.round() == round && predicate.pick() == pick).findFirst();

            if (selection.isEmpty()) {
                System.out.println("Team " + sendingFrom.getShortName() + " does not have pick " + sendingPick + "!");
                return false;
            }

            sendingWeight += selection.get().weight();
            sendingSelections.add(selection.get());
        }

        DraftClass receivingClass = this.draftClasses.get(sendingTo);
        List<Selection> receivingSelections = new ArrayList<>();
        for (String receivingPick : receiving.split(",")) {
            receivingPick = receivingPick.strip();

            int round = Integer.parseInt(receivingPick.split("\\.")[0]);
            int pick = Integer.parseInt(receivingPick.split("\\.")[1]);

            Optional<Selection> selection = receivingClass.picks().stream().filter(predicate -> predicate.round() == round && predicate.pick() == pick).findFirst();

            if (selection.isEmpty()) {
                System.out.println("Team " + sendingTo.getShortName() + " does not have pick " + receivingPick + "!");
                return false;
            }

            receivingWeight += selection.get().weight();
            receivingSelections.add(selection.get());
        }

        System.out.println(sendingFrom.getShortName() + " is sending " + sendingWeight + " points worth of picks, in exchange for " + receivingWeight + " points worth of picks from " + sendingTo.getShortName());

        tradeSuccess = this.isSuccessful(sendingWeight, receivingWeight);

        if (tradeSuccess) {
            System.out.println("Successful trade! You sent: " + sending + " to " + sendingTo.getShortName() + " for " + receiving);
            System.out.println(" ");

            for (Selection selection : sendingSelections) {
                sendingClass.picks().remove(selection);
                receivingClass.picks().add(selection);
            }

            for (Selection selection : receivingSelections) {
                receivingClass.picks().remove(selection);
                sendingClass.picks().add(selection);
            }

            sendingClass.picks().sort(Comparator.comparingInt(Selection::round).thenComparingInt(Selection::pick));
            receivingClass.picks().sort(Comparator.comparingInt(Selection::round).thenComparingInt(Selection::pick));

            System.out.println(sendingFrom.getShortName() + " now has the following picks:");
            for (Selection selection : sendingClass.picks()) {
                System.out.println(sendingFrom.getShortName() + " - Round " + selection.round() + ", pick " + selection.pick() + " is worth " + selection.weight() + " points");
            }

            System.out.println(" ");

            System.out.println(sendingTo.getShortName() + " now has the following picks:");
            for (Selection selection : receivingClass.picks()) {
                System.out.println(sendingTo.getShortName() + " - Round " + selection.round() + ", pick " + selection.pick() + " is worth " + selection.weight() + " points");
            }
        } else {
            System.out.println("No trade! Your package of " + sending + " to " + sendingTo.getShortName() + " for " + receiving + " was wide of the mark! Adjust your offer, and try again!");
        }

        return tradeSuccess;
    }

    public Map<Teams, DraftClass> getDraftClasses() {
        return draftClasses;
    }

    private boolean isSuccessful(int offeredWeight, int targetWeight)  {
        if (offeredWeight >= targetWeight) {
            return true;
        }

        double weightBoundary = targetWeight * 0.9;
        if (offeredWeight < weightBoundary) {
            return false;
        }

        double weightProximity = (offeredWeight - weightBoundary) / (targetWeight - weightBoundary);

        return ThreadLocalRandom.current().nextDouble() < weightProximity;
    }
}
