package com.ryanlaverick;

import java.util.Scanner;

public class NFLTradeTool {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter which Team you wish to act as: ");

        String inputTeam = input.nextLine();
        System.out.println("You picked: " + inputTeam.toUpperCase());

        Teams team = Teams.tryFrom(inputTeam);

        if (team == null) {
            System.out.println("Unable to determine Team from input: " + inputTeam);
            return;
        }

        System.out.println("Found team " + team.getAliases().toString() + " from input: " + inputTeam);
    }
}
