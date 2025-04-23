package com.ryanlaverick;

import java.util.Scanner;

public class NFLTradeTool {
    public static void main(String[] args) {
        TradeSession tradeSession = new TradeSession();

        Scanner input = new Scanner(System.in);
        System.out.println("Please enter which Team you wish to act as: ");

        String inputTeamActingAs = input.nextLine();
        Teams teamActingAs = Teams.tryFrom(inputTeamActingAs);

        if (teamActingAs == null) {
            System.out.println("Unable to determine Team from input: " + inputTeamActingAs);
            System.out.println(" ");
            return;
        }

        System.out.println("You chose to trade as: " + teamActingAs.getShortName());
        System.out.println(" ");

        System.out.println("Please enter which Team you wish to trade with: ");

        String inputTeamTradingWith = input.nextLine();
        Teams teamTradingWith = Teams.tryFrom(inputTeamTradingWith);

        if (teamTradingWith == null) {
            System.out.println("Unable to determine Team from input: " + inputTeamTradingWith);
            System.out.println(" ");
            return;
        }

        System.out.println("You chose to trade as: " + teamTradingWith.getShortName());
        System.out.println(" ");

        for (Selection selection : tradeSession.getDraftClasses().get(teamActingAs).picks()) {
            System.out.println(teamActingAs.getShortName() + " - Round " + selection.round() + ", pick " + selection.pick() + " is worth " + selection.weight() + " points");
        }

        System.out.println(" ");

        for (Selection selection : tradeSession.getDraftClasses().get(teamTradingWith).picks()) {
            System.out.println(teamTradingWith.getShortName() + " - Round " + selection.round() + ", pick " + selection.pick() + " is worth " + selection.weight() + " points");
        }

        System.out.println("What would you like to send from " + teamActingAs.getShortName() + "?");
        String sending = input.nextLine().trim();

        System.out.println("What would you like in return from " + teamTradingWith.getShortName() + "?");
        String receiving = input.nextLine().trim();

        tradeSession.proposeTrade(teamActingAs, sending, teamTradingWith, receiving);
    }
}
