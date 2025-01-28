## 概要
ユーザー情報を登録することで、気軽にメモを作成・保存できるAndroidアプリです。
サーバーサイドは [syncnote-server](https://github.com/Nkot117/syncnote-server/tree/main) とAPI通信でやりとりします。

## 機能
- ユーザー情報
  - 作成
  - 削除
- メモ情報
  - メモの一覧表示、詳細表示
  - メモの新規作成
  - メモ内容の編集
  - メモの削除
- 認証管理
  - サーバーサイドアプリから払い出されるJWTトークンをアプリ内で保持し、APIリクエスト時にヘッダーに付与して認証チェック

## スクリーンショット
| ログイン画面 | サインアップ画面 | メモ一覧画面 | メモ詳細 | アカウント画面 |
| :--- | :---: | ---: | ---: |  ---: |
| ![ログイン](https://github.com/user-attachments/assets/297bdd14-12af-4203-890b-83eefc4ca241) |![サインアップ](https://github.com/user-attachments/assets/df4added-c818-47ff-9684-3e50b21b793e)| ![メモ一覧](https://github.com/user-attachments/assets/72b564bb-2493-4b12-a9bb-6c4debcef12a)| ![メモ詳細](https://github.com/user-attachments/assets/1f74858e-06d2-4720-9b29-62c576e654b3)| ![アカウント](https://github.com/user-attachments/assets/6a88dfde-4536-4b52-9d67-036bd513c48f)| 
 
## 使用技術
- 言語・アーキテクチャ
  - Kotlin
  - Jetpack Compose
  - MVVM
  - Repositoryパターン
  - マルチモジュール
    - app
      - UIを配置
    - core:data
      - app ↔︎ networkを繋ぐリポジトリーを配置 
    - core:network
      - API通信を行うクラスを配置
- 依存関係注入
  - Hilt
- ネットワーク通信
  - Retrofit
  - moshi
- テスト
  - Kotest
  - mockk
- デバッグ
  - Hyperion
