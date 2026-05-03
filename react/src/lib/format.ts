export const formatEUR = (v: number) =>
  new Intl.NumberFormat("pt-PT", { style: "currency", currency: "EUR" }).format(v);

export const formatDate = (iso: string) =>
  new Date(iso).toLocaleDateString("pt-PT");

export const formatDateTime = (iso: string) =>
  new Date(iso).toLocaleString("pt-PT", { dateStyle: "short", timeStyle: "short" });
