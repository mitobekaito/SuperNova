import dotenv from "dotenv";
import path from "path";

dotenv.config({ path: path.resolve(__dirname, "../../.env") });

export const MONGO_URI = process.env.MONGO_URI || "";
export const PORT = parseInt(process.env.PORT || "5000", 10);
