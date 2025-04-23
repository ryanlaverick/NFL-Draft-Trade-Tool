package com.ryanlaverick;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Teams {
    // NFC North
    CHICAGO_BEARS("CHI", List.of("Chicago Bears", "Bears")),
    DETROIT_LIONS("DET", List.of("Detroit Lions", "Lions")),
    GREEN_BAY_PACKERS("GB", List.of("Green Bay Packers", "Packers")),
    MINNESOTA_VIKINGS("MIN", List.of("Minnesota Vikings", "Vikings")),

    // NFC East
    NEW_YORK_GIANTS("NYG", List.of("New York Giants", "Giants")),
    PHILADELPHIA_EAGLES("PHI", List.of("Philadelphia Eagles", "Eagles")),
    WASHINGTON_COMMANDERS("WAS", List.of("Washington Commanders", "Commanders")),
    DALLAS_COWBOYS("DAL", List.of("Dallas Cowboys", "Cowboys")),

    // NFC South
    CAROLINA_PANTHERS("CAR", List.of("Carolina Panthers", "Panthers")),
    TAMPA_BAY_BUCCANEERS("TB", List.of("Tampa Bay Buccaneers", "Buccaneers")),
    NEW_ORLEANS_SAINTS("NO", List.of("New Orleans Saints", "Saints")),
    ATLANTA_FALCONS("ATL", List.of("Atlanta Falcons", "Falcons")),

    // NFC West
    ARIZONA_CARDINALS("ARI", List.of("Arizona Cardinals", "Cardinals")),
    SEATTLE_SEAHAWKS("SEA", List.of("Seattle Seahawks", "Seahawks")),
    SAN_FRANCISCO_49ERS("SF", List.of("San Francisco 49ers", "49ers")),
    LOS_ANGELES_RAMS("LAR", List.of("Los Angeles Rams", "Rams")),

    // AFC North
    BALTIMORE_RAVENS("BAL", List.of("Baltimore Ravens", "Ravens")),
    CINCINNATI_BENGALS("CIN", List.of("Cincinnati Bengals", "Bengals")),
    CLEVELAND_BROWNS("CLE", List.of("Cleveland Browns", "Browns")),
    PITTSBURGH_STEELERS("PIT", List.of("Pittsburgh Steelers", "Steelers")),

    // AFC East
    BUFFALO_BILLS("BUF", List.of("Buffalo Bills", "Bills")),
    NEW_ENGLAND_PATRIOTS("NE", List.of("New England Patriots", "Patriots")),
    NEW_YORK_JETS("NYJ", List.of("New York Jets", "Jets")),
    MIAMI_DOLPHINS("MIA", List.of("Miami Dolphins", "Dolphins")),

    // AFC South
    HOUSTON_TEXANS("HOU", List.of("Houston Texans", "Texans")),
    JACKSONVILLE_JAGUARS("JAX", List.of("Jacksonville Jaguars", "Jaguars")),
    TENNESSEE_TITANS("TEN", List.of("Tennessee Titans", "Titans")),
    INDIANAPOLIS_COLTS("IND", List.of("Indianapolis Colts", "Colts")),

    // AFC West
    DENVER_BRONCOS("DEN", List.of("Denver Broncos", "Broncos")),
    KANSAS_CITY_CHIEFS("KC", List.of("Kansas City Chiefs", "Chiefs")),
    LOS_ANGELES_CHARGERS("LAC", List.of("Los Angeles Chargers", "Chargers")),
    LAS_VEGAS_RAIDERS("LV", List.of("Las Vegas Raiders", "Raiders"));

    final List<String> aliases;
    final String shortName;

    Teams(String shortName, List<String> aliases) {
        this.shortName = shortName;
        this.aliases = aliases;
    }

    public String getShortName() {
        return shortName;
    }

    public List<String> getAliases() {
        return aliases;
    }

    private static final Map<String, Teams> aliasMap = new HashMap<>();

    static {
        for (Teams team : Teams.values()) {
            for (String alias : team.getAliases()) {
                aliasMap.put(alias, team);
            }
        }
    }

    public static Map<String, Teams> getAliasMap() {
        return aliasMap;
    }

    public static Teams tryFrom(String string) {
        for (Teams team : Teams.values()) {
            if (string.equalsIgnoreCase(team.getShortName())) {
                return team;
            }

            for (String alias : team.getAliases()) {
                if (string.equalsIgnoreCase(alias)) {
                    return team;
                }
            }
        }

        return null;
    }
}
