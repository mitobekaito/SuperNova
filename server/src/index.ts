import express from "express";
import mongoose from "mongoose";
import cors from "cors";
import dotenv from "dotenv";

// 環境変数のロード
dotenv.config();

const app = express();
app.use(express.json());
app.use(cors());

const PORT = process.env.PORT || 5000;
const MONGO_URI = process.env.MONGO_URI || "mongodb://localhost:27017/supernova";

// MongoDB に接続
mongoose
  .connect(MONGO_URI, {
    useNewUrlParser: true,
    useUnifiedTopology: true,
  } as mongoose.ConnectOptions)
  .then(() => console.log("MongoDB 接続成功"))
  .catch((err) => console.error("MongoDB 接続エラー:", err));

// API のエンドポイント
app.get("/", (req, res) => {
  res.send("TypeScript 版 Node.js サーバーが動作しています！");
});

// サーバー起動
app.listen(PORT, () => {
  console.log(`サーバーがポート ${PORT} で起動`);
});
