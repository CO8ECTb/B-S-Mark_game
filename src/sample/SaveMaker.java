package sample;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

// 1 = -- (rotated)
// 0 = | (not rotated)

public class SaveMaker {
    private final static String COMMON_SAVE_FOLDER = new File("saves").toString();
    private final static int MAX_SAVE_LEN = 4096;

    private static byte GetByte(Integer x, int pos) {
        x >>= 8 * pos;
        x &= 255;
        return x.byteValue();
    }

    private static int SetByte(Integer msk, byte x, int pos) {
        int y = x;
        y <<= 8 * pos;
        msk |= y;
        return msk;
    }

    public static void InitSaveDirIfNeed(Integer grade) {
        File saveDir = new File(COMMON_SAVE_FOLDER, grade.toString());
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }
    }


    public static Boolean isFile(String filename, Integer grade){
        File saveFolder = new File(COMMON_SAVE_FOLDER, grade.toString());
        File file = new File(saveFolder, filename.concat(".sav.cur"));
        if(file.isFile()) return true;
        else return false;
    }

    public static List<Integer> ReadDataFromFile(String filename, Integer grade) {
        InitSaveDirIfNeed(grade);

        File saveFolder = new File(COMMON_SAVE_FOLDER, grade.toString());
        File file = new File(saveFolder, filename.concat(".sav"));
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

    public static boolean WriteDataToFile(List<Integer> field, Integer style, Integer grade, String filename) {
        InitSaveDirIfNeed(grade);

        File saveForlder = new File(COMMON_SAVE_FOLDER, grade.toString());
        File file = new File(saveForlder, filename.concat(".sav"));
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

    public static List<Integer> ReadLvlInfoFromFile(String filename, Integer grade) {
        InitSaveDirIfNeed(grade);

        File saveFolder = new File(COMMON_SAVE_FOLDER, grade.toString());
        File file = new File(saveFolder, filename.concat(".sav.cur"));

        List<Integer> data = new ArrayList<>();
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(file));

            byte[] bytes = new byte[MAX_SAVE_LEN];
            int offset = 0;
            int add = 0;
            while ((add = in.read(bytes, offset, MAX_SAVE_LEN - offset)) != -1) {
                offset += add;
            }

            int idx = 0;
            if (offset % 4 != 0) {
                throw new RuntimeException();
            }

            int mask = 0;
            int hashSum = 0;
            for (int i = 0; i < offset; ++i) {
                mask = SetByte(mask, bytes[i], i % 4);
                if (i % 4 == 3) {
                    data.add(mask);
                    hashSum ^= mask;
                    mask = 0;
                }
            }

            if (hashSum != 0) {
                throw new RuntimeException();
            }

            data.remove(data.size() - 1);
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

    public static boolean WriteLvlInfoToFile(List<Integer> info, Integer grade, String filename) {
        InitSaveDirIfNeed(grade);

        File saveForlder = new File(COMMON_SAVE_FOLDER, grade.toString());
        File file = new File(saveForlder, filename.concat(".sav.cur"));
//        System.out.println("FilePath save is: " + file);

        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            byte[] bytes = new byte[MAX_SAVE_LEN];
            Integer hashSum = 0;
            int idx = 0;
            for (Integer unit : info) {
                hashSum ^= unit;
                for (int i = 0; i < 4; ++i) {
                    bytes[idx++] = GetByte(unit, i);
                }
            }

            for (int i = 0; i < 4; ++i) {
                bytes[idx++] = GetByte(hashSum, i);
            }

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
        String filename = "1";

        int grade = 0;
        if (!WriteDataToFile(field, style, grade, filename)) {
            return false;
        }

        List<Integer> dataFromFile = ReadDataFromFile(filename, grade);
        for (int i = 0; i < field.size(); ++i) {
            if (field.get(i) != dataFromFile.get(i)) {
                return false;
            }
        }
        if (dataFromFile.get(dataFromFile.size() - 1) != style) {
            return false;
        }


        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 11; ++i) {
            list.add(i * 3);
        }
        if (!WriteLvlInfoToFile(list, grade, filename)) {
            return false;
        }

        List<Integer> listFromFile = ReadLvlInfoFromFile(filename, grade);
        if (listFromFile.size() != list.size()) {
            return false;
        }

        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i) != listFromFile.get(i)) {
                return false;
            }
        }

        return true;
    }




    public static List<Element> parseCollection(List<Integer> list, int dimension){
        List<Element> elements = new ArrayList<>();
        int lvlStyle = list.get(list.size()-1);
        for(int i = 0; i < list.size()-1; ++i){
            boolean isRotated = list.get(i) != 0;
            int col = i/dimension;
            int row = i-col*dimension;
            Element element = new Element(col,row,false,isRotated,lvlStyle);
            elements.add(element);
        }
        return elements;
    }

    public static List<Integer> parseElementCollection(List<Element> items, int counter){
        List<Integer> list = new ArrayList<>();
        for(Element el: items){
            list.add(el.isRotated() ? 1 : 0);
        }
        list.add(counter);
        return list;
    }


    public static Integer getLevelStyle(List<Integer> list){
        return list.get(list.size()-1);
    }

    public static Integer getCounter(List<Integer> list){
        return list.get(list.size()-1);
    }
}
