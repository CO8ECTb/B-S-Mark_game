package sample;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

import static sample.LvlGenerator.GetLvlDiff;

public class Stats {
    private static boolean CheckWin(List<Integer> field, int dim) {
        for (int i = 0; i < dim * dim; ++i) {
            if (i >= field.size()) {
                return false;
            }
            if (field.get(i) != 1) {
                return false;
            }
        }
        return true;
    }

    public static int GetLvlScore(Integer grade, Integer lvl) {
        List<Integer> rawData = SaveMaker.ReadLvlInfoFromFile((++lvl).toString(), grade);
        return CheckWin(rawData, 4) ? GetLvlDiff(grade, --lvl) : 0;
    }

    public static int GetLvlCounter(Integer grade, Integer lvl) {
        List<Integer> rawData = SaveMaker.ReadLvlInfoFromFile((++lvl).toString(), grade);
        return SaveMaker.getCounter(rawData);
    }

    public static List<Pair<Integer, Integer>> GetScoreForGrade(int grade) {
        List<Pair<Integer, Integer>> result = new ArrayList<>();
        int lvlsForThisGrade = LvlGenerator.GetLvlCountByGrade(grade);
        for (int j = 0; j < lvlsForThisGrade; ++j) {
            int factor = GetLvlScore(grade, j);
            int counter = GetLvlCounter(grade, j);
            result.add(new Pair<>(factor, counter));

            String token;
            if (factor == 0) {
                token = "Уровень " + (j + 1) + " не пройден.";
            } else {
                token = "Уровень " + (j + 1) + " пройден. + " + result.get(j).getKey() + " -- " + result.get(j).getValue();
            }
            System.out.println(token);
        }
        return result;
    }

    public static List<Pair<Integer, Integer>> GetColorsAndScores(List<Pair<Integer, Integer>> scores, int grade) {
        List<Pair<Integer, Integer>> colorsAndScores = new ArrayList<>();

        for (int i = 0; i < scores.size(); ++i) {
            Pair<Integer, Integer> curScore = scores.get(i);
            if (curScore.getKey() == 0) {
                colorsAndScores.add(new Pair<>(0, 0));
            } else {
                colorsAndScores.add(GetColorAndScore(scores.get(i).getValue(), grade, i));
            }
        }

        return colorsAndScores;
    }

    private static int GetColorByScore(int counter, int grade, int lvl) {
        int optimal = LvlGenerator.GetLvlDiff(grade - 1, lvl);
        if (counter <= optimal * 5) {
            return 3;
        }
        if (counter <= optimal * 15) {
            return 2;
        }
        return 1;
    }

    public static Pair<Integer, Integer> GetColorAndScore(int counter, int grade, int lvl) {
        int factor = GetLvlDiff(grade - 1, lvl);
        int color = GetColorByScore(counter, grade, lvl);
        int score = factor * color;
        return new Pair<>(color, score);
    }
}
