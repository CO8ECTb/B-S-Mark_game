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

public class Helper {
    // only for fixed dimensions
    private static boolean isInited = false;
    private static int[] distribution = null;

    public static void InitDistribution(int sideSize) {
        isInited = true;

        final int fieldSize = sideSize * sideSize;
        final int size = 1 << fieldSize;;
        int used[] = new int[size];
        final int startState = size - 1;
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
        int sideSize = 0;
        while (sideSize * sideSize < field.size()) {
            ++sideSize;
        }

        if (!isInited) {
            InitDistribution(sideSize);
        }

        int mask = 0;
        for (int i = 0; i < field.size(); ++i) {
            if (field.get(i) == 1) {
                mask |= 1 << i;
            }
        }

        for (int i = 0; i < field.size(); ++i) {
            int resMask = Mutate(mask, sideSize, i);
            if (distribution[resMask] + 1 == distribution[mask]) {
                return i;
            }
        }

        return -1;
    }

    public List<Integer> GenField(int sideSize, int difficulty) {
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

        int resultedMask = 0;
        for (int i = 0; i < fieldSize; ++i) {
            if (distribution[i] == difficulty) {
                resultedMask = i;
                break;
            }
        }

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
}
