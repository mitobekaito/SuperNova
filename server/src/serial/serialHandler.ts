import { ReadlineParser } from "@serialport/parser-readline";
import { SensorData } from "../models/sensorData";
import { MAX_RECORDS } from "../config/dotenv";
import { getSerialPort } from "./serialCommand";

// ✅ シリアルポートを開く
export const setupSerialPort = () => {
  const port = getSerialPort(); // ✅ シリアルポートのインスタンスを取得
  if (!port) return;

  const parser = port.pipe(new ReadlineParser({ delimiter: "\n" }));

  // ✅ 受信データの処理
  parser.on("data", async (data: string) => {
    try {
      console.log("📡 受信データ:", data);

      const cleanData = data.trim();
      if (!cleanData) return;

      // 📌 文字列をパースして JSON に変換
      const jsonData = parseSensorData(cleanData);
      if (!jsonData) {
        console.warn("⚠️ データのパースに失敗しました:", cleanData);
        return;
      }

      // 📌 MongoDB のデータ件数を確認
      const count = await SensorData.countDocuments();
      if (count >= MAX_RECORDS) {
        console.log("⚠️ データが 50 件を超えました。古いデータを削除します...");
        await SensorData.deleteMany({});
      }

      // 📌 新しいデータを MongoDB に保存
      const newData = new SensorData(jsonData);
      await newData.save();
    } catch (error) {
      console.error("❌ データ処理エラー:", error);
    }
  });
};

/**
 * 🌡 Arduino からの文字列データを解析し、JSON に変換する関数
 * 🌡 Temp: 25.20°C | 💧 Hum: 49.50% | 🔥 Fire: NO | 🚶 PIR: NO
 */
const parseSensorData = (
  rawData: string
): {
  temperature: number;
  humidity: number;
  motion: boolean;
  flame: boolean;
} | null => {
  try {
    // 📌 正規表現で数値を抽出
    const tempMatch = rawData.match(/Temp:\s*([\d.]+)°C/);
    const humMatch = rawData.match(/Hum:\s*([\d.]+)%/);
    const fireMatch = rawData.match(/Fire:\s*(YES|NO)/);
    const motionMatch = rawData.match(/PIR:\s*(YES|NO)/);

    if (!tempMatch || !humMatch || !fireMatch || !motionMatch) {
      console.warn("⚠️ データの一部が欠落しています:", rawData);
      return null;
    }

    return {
      temperature: parseFloat(tempMatch[1]),
      humidity: parseFloat(humMatch[1]),
      flame: fireMatch[1] === "YES",
      motion: motionMatch[1] === "YES",
    };
  } catch (error) {
    console.error("❌ センサーデータの解析エラー:", error);
    return null;
  }
};
