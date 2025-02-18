import express from "express";
import mongoose from "mongoose";
import cors from "cors";
import dotenv from "dotenv";

dotenv.config();

const app = express();
app.use(express.json());
app.use(cors());

const PORT = process.env.PORT || 5000;
const MONGO_URI = process.env.MONGO_URI || "mongodb://localhost:27017/supernova";

// Connect to MongoDB
mongoose.connect(MONGO_URI, {
    useNewUrlParser: true,
    useUnifiedTopology: true,
} as mongoose.ConnectOptions)
    .then(() => console.log("MongoDB Connection Successful"))
    .catch((err) => console.error("MongoDB Connection Error:", err));

// 🌟 Sensor Data Schema
const sensorSchema = new mongoose.Schema({
    timestamp: String,
    temperature: Number,
    motion_detected: Boolean
});
const SensorData = mongoose.model("SensorData", sensorSchema);

// 📌 GET: Retrieve sensor data
app.get("/sensor-data", async (req, res) => {
    try {
        const data = await SensorData.find().sort({ timestamp: -1 }).limit(10); // Get latest 10 entries
        res.json(data);
    } catch (error) {
        res.status(500).json({ message: "Error fetching data", error });
    }
});

// 📌 POST: Save new sensor data
app.post("/sensor-data", async (req, res) => {
    try {
        const { timestamp, temperature, motion_detected } = req.body;
        const newData = new SensorData({ timestamp, temperature, motion_detected });
        await newData.save();
        res.status(201).json({ message: "Data saved successfully" });
    } catch (error) {
        res.status(500).json({ message: "Error saving data", error });
    }
});

// ✅ Root endpoint
app.get("/", (req, res) => {
    res.send("TypeScript-based Node.js server is running!");
});

// Start the server
app.listen(PORT, () => {
    console.log(`Server is running on port ${PORT}`);
});
