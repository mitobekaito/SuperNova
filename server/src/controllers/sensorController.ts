import { Request, Response, NextFunction } from "express";
import { SensorData } from "../models/sensorData";

// ✅ 最新のセンサーデータを取得
export const getLatestSensorData = async (req: Request, res: Response) => {
  try {
    // console.log("📡 データ取得リクエスト受信");

    // ✅ 最新1件のみ取得
    const latestData = await SensorData.find().sort({ _id: -1 }).limit(1);

    if (!latestData.length) {
      console.log("⚠️ データがありません");
      res.json([]);
      return;
    }

    // console.log("📩 取得した最新データ:", latestData[0]);

    res.setHeader(
      "Cache-Control",
      "no-store, no-cache, must-revalidate, proxy-revalidate"
    );
    res.setHeader("Pragma", "no-cache");
    res.setHeader("Expires", "0");

    res.json(latestData);
  } catch (error) {
    console.error("❌ データ取得エラー:", error);
    res.status(500).json({ message: "データ取得エラー", error });
  }
};

// ✅ センサーデータを保存する
export const saveSensorData = async (
  req: Request,
  res: Response,
  next: NextFunction
): Promise<void> => {
  try {
    const { timestamp, temperature, humidity, motion, flame } = req.body;

    if (
      !timestamp ||
      temperature === undefined ||
      humidity === undefined ||
      motion === undefined ||
      flame === undefined
    ) {
      res.status(400).json({ message: "❌ 必要なデータが不足しています" });
    }

    const formattedTimestamp = new Date(timestamp).toISOString();
    const newData = new SensorData({
      timestamp: formattedTimestamp,
      temperature,
      humidity,
      motion,
      flame,
    });

    await newData.save();
    console.log("✅ 新しいデータが保存されました:", newData);
    res
      .status(201)
      .json({ message: "✅ データが正常に保存されました", data: newData });
  } catch (error) {
    console.error("❌ データ保存エラー:", error);
    res.status(500).json({ message: "データ保存エラー", error });
  }
};
