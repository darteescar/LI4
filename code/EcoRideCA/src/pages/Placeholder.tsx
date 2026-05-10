import { Construction } from "lucide-react";
import { Card, CardContent } from "@/components/ui/card";
import { PageHeader } from "@/components/layout/PageHeader";

interface Props {
  title: string;
  description?: string;
  note?: string;
}

export function Placeholder({ title, description, note }: Props) {
  return (
    <div>
      <PageHeader title={title} description={description} />
      <Card className="shadow-sm">
        <CardContent className="flex flex-col items-center justify-center gap-3 py-16 text-center">
          <div className="flex h-12 w-12 items-center justify-center rounded-full bg-warning-soft text-warning">
            <Construction className="h-6 w-6" />
          </div>
          <h3 className="text-lg font-semibold">Em breve</h3>
          <p className="max-w-md text-sm text-muted-foreground">
            {note ?? "Esta área será implementada nas próximas fases (Stock, Ordens de Serviço, Financeiro e Alertas)."}
          </p>
        </CardContent>
      </Card>
    </div>
  );
}
