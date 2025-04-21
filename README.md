# tdd-workshop
単体テストワークショップの教材

## Quick Start

### 起動方法（IntelliJ）

1. Java 17を準備する
2. プロジェクト構造（⌘+; | Ctrl+;）でSDKを設定する
3. 起動設定の「**bootRun+watch**」を実行する
4. ブラウザで http://localhost:8080/swagger-ui を開く
   - Swagger
5. ブラウザで http://localhost:8080/h2-console/ を開く
   - そのまま「Continue」でDBコンソールに入る

## このプロジェクトの構成

### フレームワーク
- SpringBoot
- Spring Data JDBC
- Lombok

### DB
- H2 DB
- アプリケーション起動時にDBが起動し以下を実行する
  - [schema.sql](./src/main/resources/schema.sql)
  - [data.sql](./src/main/resources/data.sql)
- アプリケーションを再起動とDBが初期状態に戻る
  - [ScheduleRepository](./src/test/java/com/example/tdd_workshop/schedule/db/ScheduleRepositoryTest.java)テスト実行時もH2 DBが起動している

## ソースコードの解説

### パッケージ構成
- [パッケージ com.example.tdd_workshop.schedule](./src/main/java/com/example/tdd_workshop/schedule) "schedule"ドメイン
    - [パッケージ db](./src/main/java/com/example/tdd_workshop/schedule/db) ドメインのDBに関するクラス
    - [パッケージ schema](./src/main/java/com/example/tdd_workshop/schedule/schema) ドメインのAPIリクエスト、レスポンスに関するクラス 

### 主なクラス
- [ScheduleController](./src/main/java/com/example/tdd_workshop/schedule/ScheduleController.java) HTTPリクエストとレスポンス、SwaggerのAPIドキュメントについて詳しい
- [ScheduleRepository](./src/main/java/com/example/tdd_workshop/schedule/db/ScheduleRepository.java) DBのSCHEDULEテーブルの仕様とDB操作に詳しい
- [ScheduleService](./src/main/java/com/example/tdd_workshop/schedule/ScheduleService.java) ControllerとRepositoryの間にあるクラス。

### recordを積極的に使用
- DAO、DTOなどのクラスはすべてrecordを使用しているためイミュータブル