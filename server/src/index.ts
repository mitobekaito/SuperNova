import path from "path";
import express, { Request, Response } from "express";
import mongoose from "mongoose";
import cors from "cors";
import dotenv from "dotenv";

// ✅  .env を読み込む
dotenv.config({ path: path.resolve(__dirname, "../.env") });

console.log("MongoDB URI:", process.env.MONGO_URI); // ✅ デバッグ用に出力

const app = express();
app.use(express.json());
app.use(cors({ origin: "*" }));

const PORT = parseInt(process.env.PORT || "5000", 10);
const MONGO_URI = process.env.MONGO_URI || "";

// ✅ MongoDB に接続
mongoose.connect(MONGO_URI)
    .then(() => console.log("✅ MongoDB（supernova）接続成功"))
    .catch((err) => {
        console.error("❌ MongoDB 接続エラー:", err);
        process.exit(1);
    });

// 🌟 Sensor Data Schema
const sensorSchema = new mongoose.Schema({
    timestamp: { type: String, required: true },
    temperature: { type: Number, required: true },
    humidity: { type: Number, required: true },
    motion_detected: { type: Boolean, required: true }
}, { collection: 'arduino_data' });
const SensorData = mongoose.model("SensorData", sensorSchema);

// 📌 `GET /sensor-data`
app.get("/sensor-data", async (req: Request, res: Response): Promise<void> => {
    try {
        const data = await SensorData.find().sort({ timestamp: -1 }).limit(10);
        res.json(data);
    } catch (error) {
        console.error("❌ データ取得エラー:", error);
        res.status(500).json({ message: "データ取得エラー", error });
    }
});

// 📌 `POST /sensor-data`
app.post("/sensor-data", async (req: Request, res: Response): Promise<void> => {
    try {
        const { timestamp, temperature, humidity, motion_detected } = req.body;

        if (!timestamp || temperature === undefined || humidity === undefined || motion_detected === undefined) {
            res.status(400).json({ message: "❌ 必要なデータが不足しています" });
            return;
        }

        const newData = new SensorData({ timestamp, temperature, humidity, motion_detected });
        await newData.save();
        res.status(201).json({ message: "✅ データが正常に保存されました" });
    } catch (error) {
        console.error("❌ データ保存エラー:", error);
        res.status(500).json({ message: "データ保存エラー", error });
    }
});

// ✅ ルートエンドポイント
app.get("/", (req: Request, res: Response): void => {
    res.send("✅ TypeScript ベースの Node.js サーバーが動作中！");
});

// 🚀 サーバーを起動
app.listen(PORT, "0.0.0.0", () => {
    console.log(`✅ サーバーがポート ${PORT} で起動しました`);
});
