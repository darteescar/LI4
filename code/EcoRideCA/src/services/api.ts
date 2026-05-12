const TOKEN_KEY = "ecoride-token";

async function request<T>(path: string, init?: RequestInit): Promise<T> {
  const token = localStorage.getItem(TOKEN_KEY);
  const res = await fetch(`/api${path}`, {
    headers: {
      "Content-Type": "application/json",
      ...(token ? { "Authorization": token } : {}),
      ...init?.headers,
    },
    credentials: "include",
    ...init,
  });

  if (res.status === 401) {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem("ecoride-user");
    window.location.replace("/login");
    throw new Error("Sessão expirada. Faz login novamente.");
  }

  if (!res.ok) {
    const text = await res.text();
    let msg = text || `Erro ${res.status}`;
    try {
      const json = JSON.parse(text) as { mensagem?: string; message?: string };
      msg = json.mensagem ?? json.message ?? msg;
    } catch { /* resposta não é JSON — usa texto cru */ }
    throw new Error(msg);
  }

  if (res.status === 204) return undefined as T;
  return res.json() as Promise<T>;
}

export const api = {
  get:    <T>(path: string)                    => request<T>(path),
  post:   <T>(path: string, body: unknown)     => request<T>(path, { method: "POST",   body: JSON.stringify(body) }),
  patch:  <T>(path: string, body: unknown)     => request<T>(path, { method: "PATCH",  body: JSON.stringify(body) }),
  delete: (path: string)                       => request<void>(path, { method: "DELETE" }),
};
