const BASE="http://localhost:8080/api";
export async function analyze(file:File){const f=new FormData();f.append("file",file);const r=await fetch(`${BASE}/analysis/upload`,{method:"POST",body:f});if(!r.ok)throw new Error(await r.text());return r.json();}
export async function history(){const r=await fetch(`${BASE}/analysis`);if(!r.ok)throw new Error("Cannot load history");return r.json();}