ランダム値の任意サイズのファイルを作成したり、ファイルを読み込んでCRC32を計算するテスト用アプリ.

[usage]
*ランダムな値のファイルの作成
  java -jar BigFileTester.jar -create filename -size nnn

-sizeはメガバイト単位

*ファイルを読み込みCRC32の算定
  java -jar BigFileTester.jar -read filename [-count nnn]

-countはデフォルトは1
指定した回数だけ繰り返し読み込む.

