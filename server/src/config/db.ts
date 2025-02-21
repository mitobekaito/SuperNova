import mongoose from "mongoose";
import { MONGO_URI } from "./dotenv";

export const connectDB = async () => {
  try {
    await mongoose.connect(MONGO_URI);
    console.log("✅ MongoDB（supernova）接続成功");
  } catch (error) {
    console.error("❌ MongoDB 接続エラー:", error);
    process.exit(1);
  }
};
