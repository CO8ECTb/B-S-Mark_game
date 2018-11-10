package sample;

// 1 = -- (rotated)
// 0 = | (not rotated)

// field:
// 0  4  8  12
// 1  5  9  13
// 2  6  10 14
// 3  7  11 15

// confirmed by CO8ECTb

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Helper {
    // only for fixed dimensions
    private static boolean isInited = false;
    private static int dstSize;
    private static int[] distribution = null;

    public static void InitDistribution(int sideSize) {
        isInited = true;

        final int fieldSize = sideSize * sideSize;
        dstSize = 1 << fieldSize;
        int used[] = new int[dstSize];
        final int startState = dstSize - 1;
        used[startState] = 1;

        LinkedList<Integer> que = new LinkedList<>();
        que.addLast(startState);

        while (!que.isEmpty()) {
            int curMask = que.getFirst();
            que.removeFirst();

            for (int i = 0; i < fieldSize; ++i) {
                int newMask = Mutate(curMask, sideSize, i);
                if (used[newMask] == 0) {
                    que.addLast(newMask);
                    used[newMask] = used[curMask] + 1;
                }
            }
        }

        for (int i = 0; i < dstSize; ++i) {
            --used[i];
        }

        distribution = used;
    }

    private static int GetBit(int mask, int pos) {
        return (mask >> pos) & 1;
    }

    private static int GetIdx(int sideSize, int x, int y) {
        return x * sideSize + y;
    }

    private static int Mutate(int mask, int sideSize, int pos) {
        int pi = pos / sideSize;
        int pj = pos % sideSize;

        mask ^= 1 << GetIdx(sideSize, pi, pj);
        for (int it = 0; it < sideSize; ++it) {
            mask ^= 1 << GetIdx(sideSize, pi, it);
            mask ^= 1 << GetIdx(sideSize, it, pj);
        }
        return mask;
    }

    public static int GetTip(List<Integer> field) {
        final int fieldSize = field.size();

        int sideSize = 0;
        while (sideSize * sideSize < fieldSize) {
            ++sideSize;
        }

        if (!isInited) {
            InitDistribution(sideSize);
        }

        int mask = 0;
        for (int i = 0; i < fieldSize; ++i) {
            if (field.get(i) == 1) {
                mask |= 1 << i;
            }
        }

        for (int i = 0; i < fieldSize; ++i) {
            int resMask = Mutate(mask, sideSize, i);
            if (distribution[resMask] + 1 == distribution[mask]) {
                return i;
            }
        }

        return -1;
    }

    public static int GetTip(List<Element> list, int unused) {
        List<Integer> field = new ArrayList<>();
        for (Element el: list){
            field.add(el.isRotated() ? 1 : 0);
        }
        return GetTip(field);
    }

    public static List<Integer> GenField(int sideSize, int difficulty, int seed) {
        final int fieldSize = sideSize * sideSize;
        final int maxDifficulty = fieldSize + 1;
        if (difficulty > maxDifficulty) {
            System.out.println("Too difficult!");
            List<Integer> zeroField = new ArrayList<>();
            for (int i = 0; i < fieldSize; ++i) {
                zeroField.add(0);
            }
            return zeroField;
        }

        if (!isInited) {
            InitDistribution(sideSize);
        }

        List<Integer> resultedMasks = new ArrayList<>();
        for (int i = 0; i < dstSize; ++i) {
            if (distribution[i] == difficulty) {
                resultedMasks.add(i);
            }
        }
        if (resultedMasks.isEmpty()) {
            resultedMasks.add(0);
        }

        Random gen = new Random(seed);
        int resultedMaskIdx = gen.nextInt(resultedMasks.size());
        int resultedMask = resultedMasks.get(resultedMaskIdx);

        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < fieldSize; ++i) {
            result.add(GetBit(resultedMask, i));
        }

        return result;
    }

    public static void TestGetTip() {
        int sideSize = 4;
        int fieldSize = sideSize * sideSize;
        int a[] = {
          1, 1, 1, 0,
          0, 0, 0, 0,
          1, 1, 1, 0,
          1, 1, 1, 0
        };

        List<Integer> b = new ArrayList<>();
        for (int i = 0; i < fieldSize; ++i) {
            b.add(a[i]);
        }

        System.out.println("Test GetTip(): " + GetTip(b)); // 7


        int c[] = {
          0, 0, 0, 0,
          1, 1, 0, 1,
          1, 1, 0, 1,
          1, 1, 0, 1
        };

        List<Integer> d = new ArrayList<>();
        for (int i = 0; i < fieldSize; ++i) {
            d.add(c[i]);
        }

        System.out.println("Test GetTip(): " + GetTip(d)); // 2
    }

    public static int CalcDist(String a, String b) {
        if (a.length() > b.length()) {
            a = a + b;
            b = a.substring(0, (a.length() - b.length()));
            a = a.substring(b.length());
        }
        // now a.length() <= b.length()

        int dist[][] = new int[2][1 + a.length()];
        int cur = 1;
        for (int i = 0; i <= b.length(); ++i) {
            cur ^= 1;
            for (int j = 0; j < a.length(); ++j) {
                if (i == 0 || j == 0) {
                    dist[cur][j] = i > j ? i : j;
                } else {
                    dist[cur][j] = Math.min(dist[cur ^ 1][j - 1] + (b.charAt(i - 1) == a.charAt(j - 1) ? 1 : 0), 1 + Math.min(dist[cur][j - 1], dist[cur ^ 1][j]));
                }
            }
        }

        return dist[cur][a.length()];
    }

    public static boolean AreSameStrings(String a, String b) {
        return CalcDist(a, b) <= LIMIT;
    }
}
