package com.ryanlaverick;

import java.io.Console;
import java.util.Scanner;

public class NFLTradeTool {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter which Team you wish to act as: ");

        String team = input.nextLine();
        System.out.println("You picked: " + team.toUpperCase());
    }
}
