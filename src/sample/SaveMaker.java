package sample;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

// 1 = -- (rotated)
// 0 = | (not rotated)

public class SaveMaker {
    private final static String SAVE_FOLDER = new File("saves").toString();
    private final static int MAX_SAVE_LEN = 4096;

    public static void InitSaveDirIfNeed() {
        File saveDir = new File(SAVE_FOLDER);
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }
    }

    public static List<Integer> ReadDataFromFile(String filename) {
        InitSaveDirIfNeed();

        File file = new File(SAVE_FOLDER, filename.concat(".sav"));
//        System.out.println("FilePath is: " + file);

        List<Integer> data = new ArrayList<>();
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(file));

            byte[] bytes = new byte[MAX_SAVE_LEN];
            int offset = 0;
            int add = 0;
            while ((add = in.read(bytes, offset, MAX_SAVE_LEN - offset)) != -1) {
                offset += add;
            }

//            System.out.println("Size of file is: " + offset);

            int idx = 0;
            int sideSize = bytes[idx++];
//            System.out.println("side size is: " + sideSize);

            int hashSumByCount = 0;
            int fieldSize = sideSize * sideSize;
            for (int i = 0; i < fieldSize; ++i) {
                int elem = bytes[idx++];
                hashSumByCount += elem;
                data.add(elem);
            }
//            System.out.println("hash sum by count is: " + hashSumByCount);

            int hashSumFromFile = bytes[idx++];
//            System.out.println("hash sum from fule is: " + hashSumFromFile);
            if (hashSumFromFile != hashSumByCount) {
                throw new RuntimeException();
            }

            int styleValue = bytes[idx++];
//            System.out.println("style is: " + styleValue);
            data.add(styleValue);
        }
        catch (FileNotFoundException e) {
            System.err.println("Такого файла не существует");
        }
        catch (IOException e) {
            System.out.println("Ошибка чтения файла сохранений");
        }
        catch (NullPointerException e) {
            System.err.println("Файл пустой");
        }
        catch (RuntimeException e) {
            System.out.println("Файл сохранений поврежден");
        }

        return data;
    }

    public static boolean WriteDataToFile(List<Integer> field, Integer style, String filename) {
        InitSaveDirIfNeed();

        File file = new File(SAVE_FOLDER, filename.concat(".sav"));
//        System.out.println("FilePath save is: " + file);

        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            Integer sideSize = 0;
            while (sideSize * sideSize < field.size()) {
                ++sideSize;
            }

            if (sideSize * sideSize != field.size()) {
                throw new RuntimeException();
            }

            byte[] bytes = new byte[MAX_SAVE_LEN];

            int idx = 0;
            bytes[idx++] = sideSize.byteValue();

            Integer hashSum = 0;
            for (int i = 0; i < field.size(); ++i) {
                Integer value = field.get(i);
                if (value < 0 || value > 1) {
                    throw new RuntimeException();
                }
                bytes[idx++] = value.byteValue();
                hashSum += value;
            }

            bytes[idx++] = hashSum.byteValue();
            bytes[idx++] = style.byteValue();

            OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
            out.write(bytes, 0, idx);
            out.flush();
            out.close();
        }
        catch (RuntimeException e) {
            System.out.println("Переданы неверные данные для сохранения");
            return false;
        }
        catch (FileNotFoundException e) {
            System.out.println("Файл сохранения не найден");
            return false;
        }
        catch (IOException e) {
            System.out.println("Не удалось сохранить файл");
            return false;
        }
        System.out.println("Успешно сохранено");
        return true;
    }


    public static boolean TestSaveMaker() {
        // gen data
        int sideSize = 4;
        List<Integer> field = new ArrayList<>();

        int fieldSize = sideSize * sideSize;
        for (int i = 0; i < fieldSize; ++i) {
            int value = i % 2;
            field.add(value);
        }

        int style = 1;
        String filename = "01";

        if (!WriteDataToFile(field, style, filename)) {
            return false;
        }

        List<Integer> dataFromFile = ReadDataFromFile(filename);
        for (int i = 0; i < field.size(); ++i) {
            if (field.get(i) != dataFromFile.get(i)) {
                return false;
            }
        }
        return dataFromFile.get(dataFromFile.size() - 1) == style;
    }
}
