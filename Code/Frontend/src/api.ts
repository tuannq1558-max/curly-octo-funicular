const BASE_URL = "http://localhost:8080/api";

async function handleResponse(response: Response) {
    if (!response.ok) {
        const message = await response.text();
        throw new Error(message || `Request failed: ${response.status}`);
    }

    return response.json();
}

export async function analyze(file: File) {
    const formData = new FormData();
    formData.append("file", file);

    const response = await fetch(`${BASE_URL}/analysis/upload`, {
        method: "POST",
        body: formData,
    });

    return handleResponse(response);
}

export async function history() {
    const response = await fetch(`${BASE_URL}/analysis`);

    if (!response.ok) {
        throw new Error("Cannot load history");
    }

    return response.json();
}

export async function register(email: string, password: string) {
    const r = await fetch(`${BASE_URL}/auth/register`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json", //bao cho du lieu gui len la json
        },
        body: JSON.stringify({
            email: email,           // bien object javascript thanh JSON de gui qua HTTP
            password: password,
        }),
    });
    if (!r.ok){
        throw new Error(await r.text());
    }
    return r.json() ;
}

export async function login(email: string, password: string) {
    const l = await fetch(`${BASE_URL
    }/auth/login`,{
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            email: email,
            password: password,
        }),
    });
    if (!l.ok){
        throw new Error(await  l.text());
    }
    return l.json();
}