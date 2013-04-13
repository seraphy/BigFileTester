package jp.seraphyware.bigtesttester;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.util.zip.CRC32;

/**
 * ランダム値の任意サイズのファイルを作成したり、ファイルを読み込んでCRC32を計算するテスト用アプリ.<br>
 * 
 * @author seraphy
 */
public class BigFileTester {

    /**
     * メインエントリ.<br>
     * 
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            showHelp();
        }

        String cmd = args[0];
        String file = args[1];
        int count = 1;

        if (args.length >= 3) {
            String opt = args[2];
            if ((!"-count".equals(opt) && !"-size".equals(opt)) || args.length != 4) {
                showHelp();
            }
            String strCount = args[3];
            count = Integer.parseInt(strCount);
        }

        switch (cmd) {
        case "-create":
        case "-write":
            writeFile(file, count);
            break;

        case "-read":
            readFile(file, count);
            break;

        default:
            showHelp();
        }
    }

    /**
     * ファイルの作成
     * 
     * @param file
     *            作成ファイル名
     * @param count
     *            サイズ(MB単位)
     * @throws Exception
     *             失敗
     */
    public static void writeFile(String file, int count) throws Exception {
        SecureRandom rng = new SecureRandom();

        byte[] buf = new byte[1024 * 1024]; // 1mb

        long st = System.nanoTime();
        long span = 0;

        try (OutputStream bos = new BufferedOutputStream(new FileOutputStream(file))) {
            for (int idx = 0; idx < count; idx++) {
                rng.nextBytes(buf);
                bos.write(buf);

                span = (System.nanoTime() - st) / 1000 / 1000;
                System.console().printf("write %d/%d ... %dmSec\r", idx + 1, count, span);
            }
        }

        span = (System.nanoTime() - st) / 1000 / 1000;
        System.console().printf("\r\ndone ... %dmSec\r\n", span);
    }

    /**
     * ファイルを読み込んでCRC32を出力する.
     * 
     * @param file
     *            入力するファイル
     * @param count
     *            くりかえ回数
     * @throws Exception
     *             失敗
     */
    public static void readFile(String file, int count) throws Exception {
        CRC32 crc = new CRC32();

        byte[] buf = new byte[1024 * 1024]; // 1mb

        long st = System.nanoTime();
        long span = 0;

        for (int idx = 0; idx < count; idx++) {
            int cur = 0;
            try (InputStream bis = new BufferedInputStream(new FileInputStream(file))) {
                for (;;) {
                    int rd = bis.read(buf);
                    if (rd < 0) {
                        break;
                    }
                    crc.update(buf, 0, rd);
                    span = (System.nanoTime() - st) / 1000 / 1000;
                    System.console().printf("read %d/%d # %d ... %dmSec\r", idx + 1, count, cur, span);
                }
            }
        }

        span = (System.nanoTime() - st) / 1000 / 1000;
        System.console().printf("\r\ncrc32=%x\r\ndone ... %dmSec\r\n", crc.getValue(), span);
    }

    /**
     * ヘルプを表示して終了する.<br>
     */
    private static void showHelp() {
        System.out
                .println("[usage] java -jar BigFileTester.jar [-create filename [-size nnn] | -read filename [-count nnn]]");
        System.exit(1);
    }
}
