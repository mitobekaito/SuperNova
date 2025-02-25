import { Request, Response } from "express";
import { sendCommandToArduino } from "../serial/serialCommand";

// LED のコマンドを受け取る処理
export const handleLedCommand = (req: Request, res: Response): void => {
  try {
    const { led_command } = req.body;

    if (!led_command) {
      res.status(400).json({ message: "❌ LED コマンドが不足しています" });
      return;
    }

    console.log(`💡 受信した LED コマンド: ${led_command}`);

    sendCommandToArduino(`LED ${led_command}`); // LED コマンドを Arduino に送信
    console.log(`LED ${led_command} を送信しました。`);

    res.json({ message: `✅ LED コマンド '${led_command}' を送信しました` });
  } catch (error) {
    console.error("❌ LED コマンド処理エラー:", error);
    res.status(500).json({ message: "LED コマンド処理エラー", error });
  }
};
