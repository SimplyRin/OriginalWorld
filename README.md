# OriginalWorld
一つのサーバーで、一人一つワールドを自由に作成できるプラグイン

# Download
v1.0: [OriginalWorld-1.0.jar](https://github.com/SimplyRin/OriginalWorld/releases/download/1.0/OriginalWorld-1.0.jar)

# Requirements
- Java 8+
- [MultiWorld](https://www.spigotmc.org/resources/multiworld.4598/)

# Function
- ノーマル/スーパーフラットワールドの作成
- 別のプレイヤーのワールドは持ち主が編集を許可した場合のみブロックなどの設置/破壊が可能
- プレイヤーが切断時自動的にワールドをアンロード

# Command
```
/originalworld create <type> : ワールドを作成します。
/originalworld tp : 作成したワールドにテレポートします。
/originalworld add <player> : ターゲットプレイヤーに自分のワールドを編集する権利を付与します。
/originalworld remove <player> : ターゲットプレイヤーから自分のワールドを編集する権利を剥奪します。
/originalworld list : 編集を許可したプレイヤーを確認します。
エイリアス: /ow
```

# Open Source License
**・jOOR | Apache License 2.0**
```
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
