import app from "./app";
import { connectDB } from "./config/db";
import { PORT } from "./config/dotenv";
import { setupSerialPort } from "./serial/serialHandler";
import { openSerialPort } from "./serial/serialCommand";

// ✅ MongoDB に接続
connectDB();

// ✅ Arduino とのシリアル通信を開始
openSerialPort();
setupSerialPort();

// ✅ サーバーを起動
app.listen(PORT, () => {
    console.log(`✅ サーバーがポート ${PORT} で起動しました`);
});
