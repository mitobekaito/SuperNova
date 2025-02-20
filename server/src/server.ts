import app from "./app";
import { connectDB } from "./config/db";
import { PORT } from "./config/dotenv";

// ✅ MongoDB に接続
connectDB();

// ✅ サーバーを起動
app.listen(PORT, () => {
    console.log(`✅ サーバーがポート ${PORT} で起動しました`);
});
