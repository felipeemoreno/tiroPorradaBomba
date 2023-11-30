export type OnData = {
  buffer: string;
  currentStage?: number;
  event?: EventData;
  fieldId?: number;
  maxLength?: number;
  minLength?: number;
  shouldContinue?: boolean;
};

export enum EventData {
  'MESSAGE',
  'GET_FIELD',
}
