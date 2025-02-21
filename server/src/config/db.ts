import mongoose from "mongoose";
import { MONGO_URI } from "./dotenv";

// ✅ MongoDB に接続
export const connectDB = async (): Promise<void> => {
  try {
    if (!MONGO_URI) {
      throw new Error("❌ 環境変数 MONGO_URI が設定されていません");
    }

    await mongoose.connect(MONGO_URI, {
      connectTimeoutMS: 30000, // タイムアウト時間を設定
    });

    console.log("✅ MongoDB（supernova）接続成功");
  } catch (error: unknown) {
    const errorMessage = error instanceof Error ? error.message : "不明なエラー";
    console.error(`❌ MongoDB 接続エラー: ${errorMessage}`);
    process.exit(1);
  }
};
