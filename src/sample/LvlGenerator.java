package sample;

import java.util.List;

public class LvlGenerator {
    private static final int GRADES = 4;
    private static final int LVLS = 7;
    private static final int SIDE_SIZE = 4;
    private static int[][] DIFF = {
        {1, 2, 2, 2, 3, 3, 3},
        {1, 2, 2, 3, 3, 3, 3},
        {1, 2, 2, 3, 3, 3, 4},
        {1, 2, 2, 3, 3, 4, 4}
    };
    private static int[][] SEEDS = {
        {42, 334, 31, 131311, 34445, 452312, 1111},
        {1233567, 6767454, 4234234, 456656, 8787, 787456, 5324},
        {1246567, 77878, 7854645, 53453, 345345, 676, 5634},
        {342356, 5656, 76788, 456456, 564563, 45233452, 35234345}
    };

    public static int GetLvlDiff(int grade, int lvl) {
        return DIFF[grade][lvl];
    }

    public static int GetLvlCountByGrade(int grade) {
        return LVLS;
    }

    public static int GetGradeCount() {
        return GRADES;
    }

    public static void GenAll() {
        for (int i = 0; i < GRADES; ++i) {
            for (int j = 0; j < LVLS; ++j) {
                List<Integer> field = Helper.GenField(SIDE_SIZE, DIFF[i][j], SEEDS[i][j]);
                String str = new Integer(1 + j).toString();
                SaveMaker.WriteDataToFile(field, (i + j) % 2, 1 + i, str);
            }
        }
    }
}
