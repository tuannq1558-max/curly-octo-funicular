import { useState } from "react";
import { login, register } from "./api";

export type SessionUser = {
    id: number;
    email: string;
    fullName: string;
    role: "PATIENT" | "DOCTOR" | "CLINIC" | "ADMIN";
};

type AuthProps = { onLogin: (user: SessionUser) => void };

export default function Auth({ onLogin }: AuthProps) {
    const [isRegister, setIsRegister] = useState(false);
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [fullName, setFullName] = useState("");
    const [error, setError] = useState("");
    const [success, setSuccess] = useState("");

    async function submit(e: React.FormEvent) {
        e.preventDefault();
        setError(""); setSuccess("");
        try {
            if (isRegister) {
                await register(email, password, fullName);
                setSuccess("Account created. You can now log in.");
                setIsRegister(false);
                setPassword("");
            } else {
                const user = await login(email, password);
                localStorage.setItem("auraUser", JSON.stringify(user));
                onLogin(user);
            }
        } catch (err) {
            setError(err instanceof Error ? err.message : "Request failed");
        }
    }

    return (
        <div className="auth-page">
            <form className="auth-card" onSubmit={submit}>
                <div className="auth-brand">AURA</div>
                <h1>{isRegister ? "Create account" : "Welcome back"}</h1>
                <p>{isRegister ? "Create your patient account" : "Login to your AURA portal"}</p>
                {isRegister && <input value={fullName} onChange={e => setFullName(e.target.value)} placeholder="Full name" required />}
                <input type="email" value={email} onChange={e => setEmail(e.target.value)} placeholder="Email" required />
                <input type="password" value={password} onChange={e => setPassword(e.target.value)} placeholder="Password" required minLength={6} />
                {error && <div className="error">{error}</div>}
                {success && <div className="success">{success}</div>}
                <button type="submit">{isRegister ? "Create account" : "Login"}</button>
                <div className="demo-note">
                    <b>Demo accounts</b><br />
                    Doctor: <b>doctor@aura.vn</b> / <b>doctor123</b><br />
                    Clinic: <b>clinic@aura.vn</b> / <b>clinic123</b>
                </div>
                <button type="button" className="auth-switch" onClick={() => {setIsRegister(!isRegister);setError("");setSuccess("");}}>
                    {isRegister ? "Already have an account? Login" : "Don't have an account? Register"}
                </button>
                {!isRegister && (
                    <div className="demo-actions">
                        <button type="button" className="demo-button" onClick={() => { setEmail("clinic@aura.vn"); setPassword("clinic123"); }}>Use clinic</button>
                        <button type="button" className="demo-button" onClick={() => { setEmail("doctor@aura.vn"); setPassword("doctor123"); }}>Use doctor</button>
                    </div>
                )}
            </form>
        </div>
    );
}
