package com.ryanlaverick;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class TradeSession {
    private final Map<Teams, DraftClass> draftClasses;

    public TradeSession() {
        this.draftClasses = new EnumMap<>(Teams.class);

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
                String[] components = line.split(",");

                int round = Integer.parseInt(components[0]);
                int pick = Integer.parseInt(components[1]);
                int weight = Integer.parseInt(components[2]);
                String team = String.valueOf(components[3]);

                Teams teams = Teams.tryFrom(team);

                if (teams == null) {
                    continue;
                }

                if (this.draftClasses.containsKey(teams)) {
                    DraftClass draftClass = this.draftClasses.get(teams);
                    draftClass.picks().add(new Selection(round, pick, weight));
                } else {
                    DraftClass draftClass = new DraftClass(List.of(new Selection(round, pick, weight)));
                    this.draftClasses.put(teams, draftClass);
                }
            }
        } catch (FileNotFoundException ignored) {
            System.out.println("Unable to read picks file! Please try again shortly...");
        }

        for (Map.Entry<Teams, DraftClass> entry : draftClasses.entrySet()) {
            System.out.println(entry.getKey().getShortName() + " - " + entry.getValue().picks().toString());
        }
    }
}
