import mongoose from "mongoose";

// ✅ センサーデータのスキーマ
const sensorSchema = new mongoose.Schema(
  {
    temperature: { type: Number, required: true },
    humidity: { type: Number, required: true },
    motion: { type: Boolean, required: true },
    flame: { type: Boolean, required: true },
    timestamp: { type: Date, default: Date.now },
  },
  { collection: "sensordatas" }
);

export const SensorData = mongoose.model("SensorData", sensorSchema);
