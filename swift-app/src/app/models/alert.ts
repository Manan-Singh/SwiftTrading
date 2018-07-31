export class Alert {
  message: string;
  type: AlertType;
  alertId: string;
  keepAfterRouteChange: boolean;

  constructor(init?:Partial<Alert>) {
    Object.assign(this, init);
  }
}

export enum AlertType {
  Success,
  Error,
  Info,
  Warning
}
