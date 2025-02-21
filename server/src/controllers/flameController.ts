import { Request, Response } from "express";

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

    //受け取ったデータをそのままレスポンスとして返す
    res.json({
      message: `✅ Flame コマンド '${flame_command}' を受信しました`,
    });
  } catch (error) {
    console.error("❌ Flame コマンド処理エラー:", error);
    res.status(500).json({ message: "Flame コマンド処理エラー", error });
  }
};
