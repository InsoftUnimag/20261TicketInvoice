import { Component, type ErrorInfo, type ReactNode } from "react";
import { Button } from "@/shared/ui";

interface Props {
  children: ReactNode;
  fallback?: ReactNode;
}

interface State {
  hasError: boolean;
  error?: Error;
}

export class ErrorBoundary extends Component<Props, State> {
  state: State = { hasError: false };

  static getDerivedStateFromError(error: Error): State {
    return { hasError: true, error };
  }

  componentDidCatch(error: Error, info: ErrorInfo) {
    console.error("[ErrorBoundary]", error, info.componentStack);
  }

  render() {
    if (this.state.hasError) {
      return (
        this.props.fallback ?? (
          <div className="flex flex-col items-center justify-center gap-4 p-12">
            <p className="text-destructive font-medium">Ocurrió un error inesperado</p>
            <p className="text-muted-foreground text-sm">{this.state.error?.message}</p>
            <Button variant="outline" onClick={() => this.setState({ hasError: false })}>
              Reintentar
            </Button>
          </div>
        )
      );
    }
    return this.props.children;
  }
}
