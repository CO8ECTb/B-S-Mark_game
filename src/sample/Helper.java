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

        int mask = 0;
        for (int i = 0; i < field.size(); ++i) {
            if (field.get(i) == 1) {
                mask |= 1 << i;
            }
        }

        int size = 1 << field.size();
        int finalState = size - 1;
        int used[] = new int[size];
        used[finalState] = 1;

        LinkedList<Integer> que = new LinkedList<>();
        que.addLast(finalState);

        while (!que.isEmpty()) {
            int curMask = que.getFirst();
            que.removeFirst();

            for (int i = 0; i < field.size(); ++i) {
                int newMask = Mutate(curMask, sideSize, i);
                if (used[newMask] == 0) {
                    que.addLast(newMask);
                    used[newMask] = used[curMask] + 1;
                }
            }
        }

        for (int i = 0; i < field.size(); ++i) {
            int resMask = Mutate(mask, sideSize, i);
            if (used[resMask] + 1 == used[mask]) {
                return i;
            }
        }

        return -1;
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
