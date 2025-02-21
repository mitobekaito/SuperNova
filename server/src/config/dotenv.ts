import dotenv from "dotenv";
import path from "path";

dotenv.config({ path: path.resolve(__dirname, "../../.env") });

// 環境変数の取得
export const MONGO_URI: string = process.env.MONGO_URI || "";
export const PORT: number = process.env.PORT ? parseInt(process.env.PORT, 10) : 5000;
export const SERIAL_PORT: string = process.env.SERIAL_PORT || "COM5";
export const BAUD_RATE: number = process.env.BAUD_RATE ? parseInt(process.env.BAUD_RATE, 10) : 9600;
export const MAX_RECORDS: number = process.env.MAX_RECORDS ? parseInt(process.env.MAX_RECORDS, 10) : 50;
