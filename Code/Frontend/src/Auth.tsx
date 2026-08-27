import { useState } from "react";
import { login,register } from "./api";

type AuthProps = {
    onLogin: () => void;
};

export default function Auth({ onLogin }: AuthProps) {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");
    const [isRegister, setIsRegister] = useState(false);
    const [success, setSuccess] = useState("");

    async function handleLogin(e: React.FormEvent) {
        e.preventDefault();
        setError("");

        try {
            await login(email, password);
            onLogin();
        } catch (err) {
            setError("Email hoặc mật khẩu không đúng");
        }
    }

    async function handleRegister(e: React.FormEvent) {
        e.preventDefault();
        setError("");
        setSuccess("");

        try {
            await register(email, password);

            setSuccess("Registration successful. You can now login.");

            setIsRegister(false);
            setPassword("");
        } catch (err) {
            setError("Email đã tồn tại hoặc đăng ký thất bại");
        }
    }

    return (
        <div style={{
            minHeight: "100vh",
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
            background: "#f4f9f8"
        }}>
            <form
                onSubmit={isRegister ? handleRegister : handleLogin}
                style={{
                    width: "380px",
                    padding: "40px",
                    background: "white",
                    borderRadius: "20px",
                    boxShadow: "0 10px 30px rgba(0,0,0,0.1)"
                }}
            >
                <h1 style={{ color: "#087f73" }}>AURA</h1>

                <h2>{isRegister ? "Create account" : "Welcome back"}</h2>

                <p>
                    {isRegister
                        ? "Create your patient account"
                        : "Login to your patient portal"}
                </p>

                <label>Email</label>
                <input
                    type="email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    placeholder="Email"
                    required
                    style={{
                        width: "100%",
                        padding: "12px",
                        margin: "8px 0 20px"
                    }}
                />

                <label>Password</label>
                <input
                    type="password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    placeholder="Password"
                    required
                    style={{
                        width: "100%",
                        padding: "12px",
                        margin: "8px 0 20px"
                    }}
                />

                {error && (
                    <p style={{ color: "red" }}>
                        {error}
                    </p>
                )}
                {success && (
                    <p style={{ color: "green" }}>
                        {success}
                    </p>
                )}

                <button
                    type="submit"
                    style={{
                        width: "100%",
                        padding: "14px",
                        background: "#087f73",
                        color: "white",
                        border: "none",
                        borderRadius: "10px",
                        fontSize: "16px",
                        cursor: "pointer"
                    }}
                >
                    {isRegister ? "Create account" : "Login"}
                </button>
                <p style={{ textAlign: "center", marginTop: "20px" }}>
                    {isRegister ? "Already have an account?" : "Don't have an account?"}

                    <button
                        type="button"
                        onClick={() => {
                            setIsRegister(!isRegister);
                            setError("");
                            setSuccess("");
                        }}
                        style={{
                            marginLeft: "8px",
                            background: "none",
                            border: "none",
                            color: "#087f73",
                            cursor: "pointer",
                            fontWeight: "bold"
                        }}
                    >
                        {isRegister ? "Login" : "Register"}
                    </button>
                </p>

            </form>
        </div>
    );
}