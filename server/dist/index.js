"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
const express_1 = __importDefault(require("express"));
const mongoose_1 = __importDefault(require("mongoose"));
const cors_1 = __importDefault(require("cors"));
const dotenv_1 = __importDefault(require("dotenv"));

// Load environment variables from .env file
dotenv_1.default.config();

const app = (0, express_1.default)();
app.use(express_1.default.json());
app.use((0, cors_1.default)());

const PORT = process.env.PORT || 5000;
const MONGO_URI = process.env.MONGO_URI || "mongodb://localhost:27017/supernova";

// Connect to MongoDB
mongoose_1.default
    .connect(MONGO_URI, {
    useNewUrlParser: true,
    useUnifiedTopology: true,
})
    .then(() => console.log("MongoDB Connection Successful"))
    .catch((err) => console.error("MongoDB Connection Error:", err));

// Define a simple API endpoint
app.get("/", (req, res) => {
    res.send("TypeScript-based Node.js server is running!");
});

// Start the server
app.listen(PORT, () => {
    console.log(`Server is running on port ${PORT}`);
});

//# sourceMappingURL=index.js.map
