import { useEffect, useState } from "react";
import Auth from "./Auth";
import {
    Activity,
    BarChart3,
    Bell,
    FileText,
    History as HistoryIcon,
    Home,
    ImagePlus,
    Menu,
    MessageSquare,
    ShieldCheck,
    UploadCloud,
    Users,
    X,
} from "lucide-react";

import { analyze, history } from "./api";

type Page = "Dashboard" | "New Analysis" | "History";

type AnalysisResult = {
    riskLevel: string;
    riskScore: number;
    explanation: string;
    modelVersion: string;
    findings?: string[];
};

type HistoryItem = {
    id?: string | number;
    createdAt: string;
    filename: string;
    riskLevel: string;
    riskScore: number;
    modelVersion: string;
};

export default function App() {
    const [loggedIn, setLoggedIn] = useState(false);
    const [page, setPage] = useState<Page>("Dashboard");
    const [sidebarOpen, setSidebarOpen] = useState(false);

    if (!loggedIn) {
        return <Auth onLogin={() => setLoggedIn(true)} />;
    }
    return (
        <div className="app">
            <aside className={`side ${sidebarOpen ? "open" : ""}`}>
                <div className="brand">
                    <Activity />
                    <b>AURA</b>
                    <small>Retinal Health AI</small>
                </div>

                <Nav
                    icon={<Home />}
                    label="Dashboard"
                    active={page === "Dashboard"}
                    onClick={() => setPage("Dashboard")}
                />

                <Nav
                    icon={<ImagePlus />}
                    label="New Analysis"
                    active={page === "New Analysis"}
                    onClick={() => setPage("New Analysis")}
                />

                <Nav
                    icon={<HistoryIcon />}
                    label="Analysis History"
                    active={page === "History"}
                    onClick={() => setPage("History")}
                />

                <Nav
                    icon={<MessageSquare />}
                    label="Messages"
                    onClick={() => {}}
                />

                <div className="protect">
                    <ShieldCheck />

                    <span>
            <b>Protected health data</b>
            <small>Secure & private</small>
          </span>
                </div>
            </aside>

            <main>
                <header>
                    <button
                        className="hamb"
                        onClick={() => setSidebarOpen((value) => !value)}
                        aria-label="Toggle navigation"
                    >
                        {sidebarOpen ? <X /> : <Menu />}
                    </button>

                    <span>
            Patient Portal / <b>{page}</b>
          </span>

                    <div className="user">
                        <span>TN&nbsp; Tú Nguyễn</span>
                        <Bell size={18} />
                    </div>
                </header>

                <div className="content">
                    {page === "Dashboard" && <Dashboard onNavigate={setPage} />}
                    {page === "New Analysis" && <NewAnalysis />}
                    {page === "History" && <History />}
                </div>
            </main>
        </div>
    );
}

/* -------------------------------------------------------------------------- */
/* Navigation                                                                */
/* -------------------------------------------------------------------------- */

type NavProps = {
    icon: React.ReactNode;
    label: string;
    active?: boolean;
    onClick: () => void;
};

function Nav({ icon, label, active = false, onClick }: NavProps) {
    return (
        <button
            type="button"
            onClick={onClick}
            className={`nav ${active ? "active" : ""}`}
        >
            {icon}
            <span>{label}</span>
        </button>
    );
}

/* -------------------------------------------------------------------------- */
/* Dashboard                                                                  */
/* -------------------------------------------------------------------------- */

type DashboardProps = {
    onNavigate: (page: Page) => void;
};

function Dashboard({ onNavigate }: DashboardProps) {
    return (
        <>
            <section className="hero">
                <div>
                    <small>AI-ASSISTED SCREENING</small>

                    <h1>Good evening, Tú.</h1>

                    <p>
                        Review your retinal health and keep track of your screening
                        history.
                    </p>

                    <button
                        type="button"
                        onClick={() => onNavigate("New Analysis")}
                    >
                        + Start new analysis
                    </button>
                </div>

                <Activity size={100} />
            </section>

            <div className="stats">
                <Stat
                    icon={<Activity />}
                    title="Total screenings"
                    value="12"
                />

                <Stat
                    icon={<BarChart3 />}
                    title="Latest risk score"
                    value="18%"
                />

                <Stat
                    icon={<FileText />}
                    title="Reports available"
                    value="9"
                />

                <Stat
                    icon={<Users />}
                    title="Doctor consultations"
                    value="3"
                />
            </div>

            <section className="panel">
                <h2>Latest analysis</h2>

                <div className="result">
                    <div className="eye">RETINA</div>

                    <div>
                        <b>Retinal vascular screening</b>

                        <p>18 Aug 2026 · retina_018.jpg</p>

                        <span className="low">LOW RISK · 18%</span>

                        <p>
                            No major vascular abnormality detected by the screening model.
                        </p>
                    </div>
                </div>
            </section>
        </>
    );
}

type StatProps = {
    icon: React.ReactNode;
    title: string;
    value: string;
};

function Stat({ icon, title, value }: StatProps) {
    return (
        <div className="stat">
            {icon}

            <span>
        {title}
                <b>{value}</b>
      </span>
        </div>
    );
}

/* -------------------------------------------------------------------------- */
/* New Analysis                                                               */
/* -------------------------------------------------------------------------- */

function NewAnalysis() {
    const [file, setFile] = useState<File | null>(null);
    const [result, setResult] = useState<AnalysisResult | null>(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");

    const handleFileChange = (
        event: React.ChangeEvent<HTMLInputElement>,
    ) => {
        const selectedFile = event.target.files?.[0] ?? null;

        setFile(selectedFile);
        setResult(null);
        setError("");
    };

    const handleAnalyze = async () => {
        if (!file) return;

        setLoading(true);
        setError("");
        setResult(null);

        try {
            const data = await analyze(file);
            setResult(data);
        } catch (error) {
            setError(
                error instanceof Error
                    ? error.message
                    : "Analysis failed. Please try again.",
            );
        } finally {
            setLoading(false);
        }
    };

    return (
        <>
            <div className="title">
                <h1>New retinal analysis</h1>

                <p>
                    Upload a clear fundus image for AI-assisted screening.
                </p>
            </div>

            <div className="grid">
                <section className="upload">
                    <UploadCloud size={42} />

                    <h2>{file ? file.name : "Upload retinal image"}</h2>

                    <p>JPG or PNG · maximum 20 MB</p>

                    <input
                        id="retinal-file"
                        hidden
                        type="file"
                        accept="image/jpeg,image/png"
                        onChange={handleFileChange}
                    />

                    <label htmlFor="retinal-file">Choose image</label>

                    {file && (
                        <button
                            type="button"
                            onClick={handleAnalyze}
                            disabled={loading}
                        >
                            {loading ? "Analyzing..." : "Analyze image"}
                        </button>
                    )}

                    {error && <div className="error">{error}</div>}
                </section>

                {result ? (
                    <AnalysisResult result={result} />
                ) : (
                    <UploadGuidelines />
                )}
            </div>
        </>
    );
}

type AnalysisResultProps = {
    result: AnalysisResult;
};

function AnalysisResult({ result }: AnalysisResultProps) {
    const score = Math.round(result.riskScore * 100);

    return (
        <section className="panel">
            <h2>AI analysis result</h2>

            <div className="risk">
                {result.riskLevel}

                <b>{score}%</b>
            </div>

            <p>{result.explanation}</p>

            <p>
                <b>Model:</b> {result.modelVersion}
            </p>

            <hr />

            <b>Findings</b>

            <p>{result.findings?.join(" ") || "No findings reported."}</p>

            <small>
                Screening support only — not a medical diagnosis.
            </small>
        </section>
    );
}

function UploadGuidelines() {
    return (
        <section className="panel">
            <h2>Before you upload</h2>

            <p>✓ Use a clear, focused retinal image</p>
            <p>✓ Ensure the optic disc is visible</p>
            <p>✓ Remove personal information from the image</p>
            <p>✓ Clinical review is recommended</p>
        </section>
    );
}

/* -------------------------------------------------------------------------- */
/* History                                                                    */
/* -------------------------------------------------------------------------- */

function History() {
    const [data, setData] = useState<HistoryItem[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        const loadHistory = async () => {
            try {
                const result = await history();
                setData(result);
            } catch {
                setError("Cannot load analysis history.");
            } finally {
                setLoading(false);
            }
        };

        loadHistory();
    }, []);

    return (
        <>
            <div className="title">
                <h1>Analysis history</h1>

                <p>Previous retinal screening results.</p>
            </div>

            <section className="panel">
                {loading && <p>Loading history...</p>}

                {error && <div className="error">{error}</div>}

                {!loading && !error && data.length > 0 && (
                    <table>
                        <thead>
                        <tr>
                            <th>Date</th>
                            <th>Image</th>
                            <th>Risk</th>
                            <th>Score</th>
                            <th>Model</th>
                        </tr>
                        </thead>

                        <tbody>
                        {data.map((item) => (
                            <tr key={item.id ?? `${item.createdAt}-${item.filename}`}>
                                <td>
                                    {new Date(item.createdAt).toLocaleString()}
                                </td>

                                <td>{item.filename}</td>

                                <td>
                    <span className={item.riskLevel.toLowerCase()}>
                      {item.riskLevel}
                    </span>
                                </td>

                                <td>{Math.round(item.riskScore * 100)}%</td>

                                <td>{item.modelVersion}</td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                )}

                {!loading && !error && data.length === 0 && (
                    <p>No analyses yet. Upload your first image.</p>
                )}
            </section>
        </>
    );
}