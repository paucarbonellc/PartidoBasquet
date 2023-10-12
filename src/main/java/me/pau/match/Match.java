package me.pau.match;

import me.pau.Shell;
import me.pau.point.Point;
import me.pau.team.MatchTeam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Match {

    private final static int MAX_ROUNDS = 4;

    private final HashMap<String, MatchTeam> teams = new HashMap<>(2);

    private int round = 0;
    private int maxDiff = 0;
    private boolean active = false; // Este valor servirá para poder controlar el bucle
    private final HashMap<String, List<Point>> pointsPerRound = new HashMap<>();

    /**
     * Objeto principal para el funcionamiento del partido
     */
    public Match() {
        startMatch();
        startRound();
    }

    private void startMatch() {
        MatchTeam A = new MatchTeam(Shell.read("Escribe el nombre del equipo A"));
        MatchTeam B = new MatchTeam(Shell.read("Escribe el nombre del equipo B"));

        teams.put("A", A);
        teams.put("B", B);
    }

    private void startRound() {
        nextOrFinish();
    }

    private void nextOrFinish() {
        active = false;
        if (round >= MAX_ROUNDS) {
            finish();
            return;
        }

        showRoundInfo();
        round++;
        System.out.printf("*** EMPIEZA LA RONDA NÚMERO %s ***", round);
        active = true;
        initPointsPerRound();
        startListen();
    }

    private void initPointsPerRound() {
        pointsPerRound.put("A_" + round, new ArrayList<>());
        pointsPerRound.put("B_" + round, new ArrayList<>());
    }

    private void showRoundInfo() {
        if (round == 0) return;

        MatchTeam a = getTeam("A");
        MatchTeam b = getTeam("B");

        int aPoints = getRoundPoints("A");
        int bPoints = getRoundPoints("B");

        System.out.printf(
                """
                        ==================================
                        FINAL DE LA RONDA NÚMERO %s
                        En esta ronda han marcado estos puntos:
                            %s: %s
                            %s: %s
                        ==================================
                        %n""", round,
                a.getName(),
                aPoints,
                b.getName(),
                bPoints
        );
    }

    private int getRoundPoints(String id) {
        int counter = 0;

        for (Point point : getPointsRound(id)) {
            counter += point.getAmount();
        }

        return counter;
    }

    private void startListen() {
        while (active) {
            Shell.read("Escribe los puntos o 0 para acabar la ronda", this::processInput);
        }
    }

    /**
     * Procesar el input del usuario
     *
     * @param input
     */
    private void processInput(String input) {
        if (!ALLOWED_CHARS.contains(input)) {
            System.out.println("No has escrito un valor correcto.");
            return;
        }

        switch (input) {
            case "0" -> nextOrFinish();

            case "1", "2", "3" -> {
                int points = Integer.parseInt(input);
                Shell.read("Escribe A (%s) o B (%s)".formatted(getTeam("A").getName(), getTeam("B").getName()),
                        selectedTeam -> {
                            if (!ALLOWED_CHARS.contains(selectedTeam)) {
                                System.out.println("Nombre no válido");
                                return;
                            }

                            MatchTeam a = getTeam("A");
                            MatchTeam b = getTeam("B");

                            MatchTeam pointTeam = selectedTeam.equals("A") ? a : b;

                            Point point = new Point(selectedTeam, points);

                            addRoundPoint(selectedTeam, point);
                            pointTeam.addPoints(point);

                            System.out.printf("""
                                            ---------------------------------
                                            %s punto(s) para %s
                                                                                
                                            %s: %s
                                            %s: %s
                                                                                
                                            Diferencia de: %s punto(s)
                                            Máxima diferéncia: %s punto(s)
                                            ---------------------------------
                                            """,
                                    points,
                                    pointTeam.getName(),
                                    a.getName(),
                                    a.getPoints(),
                                    b.getName(),
                                    b.getPoints(),
                                    getDiffPoints(a, b),
                                    getMaxDiff(a, b)
                            );
                        });
            }
        }
    }

    private int getMaxDiff(MatchTeam a, MatchTeam b) {
        maxDiff = Math.max(maxDiff, getDiffPoints(a, b));
        return maxDiff;
    }

    private int getDiffPoints(MatchTeam a, MatchTeam b) {
        return Math.max(a.getPoints(), b.getPoints()) - Math.min(a.getPoints(), b.getPoints());
    }

    private void addRoundPoint(String selectedTeam, Point point) {
        getPointsRound(selectedTeam).add(point);
    }

    public MatchTeam getTeam(String id) {
        return teams.get(id);
    }

    private void finish() {
        showMatchInfo();
        System.exit(0);
    }

    private void showMatchInfo() {
        MatchTeam a = getTeam("A");
        MatchTeam b = getTeam("B");

        int count1point = getPointsMatch(1);
        int count2point = getPointsMatch(2);
        int count3point = getPointsMatch(3);

        System.out.printf(
                """
                        >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                        ¡FIN DEL PARTIDO!
                                
                        %s: %s
                        %s: %s
                                
                        %s
                                
                        Entre los dos equipos han hecho:
                            %s canasta(s) de 1 punto
                            %s canasta(s) de 2 puntos
                            %s canasta(s) de 3 puntos
                        >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                        %n""", a.getName(),
                a.getPoints(),
                b.getName(),
                b.getPoints(),
                getWinnerString(a, b),
                count1point,
                count2point,
                count3point
        );
    }

    private int getPointsMatch(int i) {
        int counter = 0;

        for (List<Point> value : pointsPerRound.values()) {
            for (Point point : value) {
                if (point.getAmount() == i) counter++;
            }
        }

        return counter;
    }

    private String getWinnerString(MatchTeam a, MatchTeam b) {
        if (a.getPoints() == b.getPoints()) return "Han quedado empate";

        return "El ganador es... " + (a.getPoints() > b.getPoints() ? a.getName() : b.getName());
    }

    private List<Point> getPointsRound(String id) {
        return pointsPerRound.get(id + "_" + round);
    }

    private static final List<String> ALLOWED_CHARS = List.of(
            "0",
            "1",
            "2",
            "3",
            "A",
            "B"
    );
}
