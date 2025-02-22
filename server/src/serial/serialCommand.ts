import { SerialPort } from "serialport";
import { SERIAL_PORT, BAUD_RATE } from "../config/dotenv";

let port: SerialPort | null = null;

/**
 * ✅ シリアルポートを開く関数（Arduino との通信を一元管理）
 */
export const openSerialPort = () => {
  if (port) {
    console.log("⚠️ 既にシリアルポートが開いています。");
    return;
  }

  try {
    port = new SerialPort({ path: SERIAL_PORT, baudRate: BAUD_RATE });

    port.on("open", () => {
      console.log(`✅ シリアルポート ${SERIAL_PORT} が開かれました`);
    });

    port.on("error", (err) => {
      console.error("❌ シリアルポートエラー:", err.message);
      // console.log("🔄 5秒後に再試行します...");
      // setTimeout(openSerialPort, 5000);
    });
  } catch (error) {
    console.error("❌ シリアルポートの初期化エラー:", error);
    console.log("🔄 5秒後に再試行します...");
    setTimeout(openSerialPort, 5000);
  }
};

/**
 * ✅ シリアルポートのインスタンスを取得する関数
 */
export const getSerialPort = (): SerialPort | null => {
  return port;
};

/**
 * ✅ Arduino へコマンドを送信する関数
 * @param command 送信するコマンド（例: "LED_ON", "LED_OFF"）
 */
export const sendCommandToArduino = (command: string) => {
  if (!port || !port.isOpen) {
    console.error(
      "❌ シリアルポートが開かれていません。コマンドを送信できません"
    );
    return;
  }

  console.log(`🚀 Arduino へ送信: ${command}`);

  port.write(`${command}\n`, (err) => {
    if (err) {
      console.error("❌ シリアルポート書き込みエラー:", err.message);
      return;
    }
    console.log(`✅ Arduino に送信完了: ${command}`);
  });
};
