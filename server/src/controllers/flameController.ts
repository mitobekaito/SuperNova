import { Request, Response } from "express";
import { sendCommandToArduino } from "../serial/serialCommand";

//flameのコマンドを受け取る処理
export const handleFlameCommand = (req: Request, res: Response): void => {
  try {
    const { flame_command } = req.body; //flame_commandを受け取る

    if (!flame_command) {
      res.status(400).json({ message: "❌ Flame コマンドが不足しています" });
      return;
    }

    //受信したコマンドをコンソールに出力
    console.log(`🔥 受信した Flame コマンド: ${flame_command}`);

    sendCommandToArduino(`FIRE ${flame_command}`); //flame_commandをArduinoに送信
    console.log(`FIRE ${flame_command} を送信しました。`);
    

    res.json({
      message: `✅ Flame コマンド '${flame_command}' を送信しました`,
    });
  } catch (error) {
    console.error("❌ Flame コマンド処理エラー:", error);
    res.status(500).json({ message: "Flame コマンド処理エラー", error });
  }
};
