package com.ryanlaverick;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

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
            System.out.println(" ");

            choice = input.nextInt();
        }

        File draftChartFile;
        if (choice == 1) {
            draftChartFile = new File("src/jimmy-johnson-draft-chart.csv");
            System.out.println("You picked: JIMMY JOHNSON DRAFT CHART");
            System.out.println(" ");
        } else {
            draftChartFile = new File("src/rich-hill-draft-chart.csv");
            System.out.println("You picked: RICH HILL DRAFT CHART (REVISED - 2025)");
            System.out.println(" ");
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
                int weight = Integer.parseInt(components[2]);

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
                int weight = weightSelection.get().weight();

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
}
