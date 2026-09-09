const BASE_URL = "http://localhost:8080/api";

async function handle(response: Response) {
    if (!response.ok) {
        const message = await response.text();
        throw new Error(message || `Request failed: ${response.status}`);
    }
    return response.json();
}

export async function register(email: string, password: string, fullName: string) {
    const response = await fetch(`${BASE_URL}/auth/register`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, password, fullName }),
    });
    return handle(response);
}

export async function login(email: string, password: string) {
    const response = await fetch(`${BASE_URL}/auth/login`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, password }),
    });
    return handle(response);
}

export async function uploadImage(file: File, patientId: number) {
    const formData = new FormData();
    formData.append("file", file);
    formData.append("patientId", String(patientId));
    const response = await fetch(`${BASE_URL}/images/upload`, {
        method: "POST",
        body: formData,
    });
    return handle(response);
}

export async function patientImages(patientId: number) {
    return handle(await fetch(`${BASE_URL}/patients/${patientId}/images`));
}

export async function patientAssessments(patientId: number) {
    return handle(await fetch(`${BASE_URL}/patients/${patientId}/assessments`));
}

export async function pendingReviews() {
    return handle(await fetch(`${BASE_URL}/doctor/reviews/pending`));
}

export async function submitAssessment(imageId: number, data: {
    doctorId: number;
    riskLevel: string;
    finding: string;
    diagnosis: string;
    recommendation: string;
    note: string;
}) {
    const response = await fetch(`${BASE_URL}/doctor/reviews/${imageId}`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data),
    });
    return handle(response);
}

export async function clinicDashboard(clinicId: number) {
    return handle(await fetch(`${BASE_URL}/clinic/dashboard?clinicId=${clinicId}`));
}

export async function clinicMembers(clinicId: number) {
    return handle(await fetch(`${BASE_URL}/clinic/${clinicId}/members`));
}

export async function addClinicMember(clinicId: number, data: { fullName: string; email: string; role: string }) {
    const response = await fetch(`${BASE_URL}/clinic/${clinicId}/members`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ ...data, status: "ACTIVE" }),
    });
    return handle(response);
}

export async function updateMemberStatus(memberId: number, status: string) {
    const response = await fetch(`${BASE_URL}/clinic/members/${memberId}/status?status=${encodeURIComponent(status)}`, {
        method: "PATCH",
    });
    return handle(response);
}
