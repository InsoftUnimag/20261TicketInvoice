export class ApiError extends Error {
  constructor(
    public readonly status: number,
    public readonly code: string,
    message: string,
    public readonly details?: Record<string, string[]>,
  ) {
    super(message);
    this.name = "ApiError";
  }

  get isClientError() {
    return this.status >= 400 && this.status < 500;
  }

  get isServerError() {
    return this.status >= 500;
  }

  get isBusinessError() {
    return this.status === 409 || this.status === 422;
  }
}

export class NetworkError extends Error {
  constructor(cause: unknown) {
    super("Error de conexión con el servidor");
    this.name = "NetworkError";
    this.cause = cause;
  }
}
