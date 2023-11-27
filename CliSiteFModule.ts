/**
 * This exposes the native CalendarModule module as a JS module. This has a
 * function 'createCalendarEvent' which takes the following parameters:
 *
 * 1. String name: A string representing the name of the event
 * 2. String location: A string representing the location of the event
 */
import {NativeModule, NativeModules} from 'react-native';

const {CliSiteFModule} = NativeModules;

interface ConstantesInterface {
  DEFAULT_EVENT_NAME: string;
}

interface CliSiteFInterface {
  eventsMessage: NativeModule;
  ping(pingMsg: string): void;
  startTransaction(amount: number): void;
  pinPadMessage(message: string): void;
  pinPadIsPresent(callback?: (error: any, isPresent: boolean) => void): void;
  continueTransaction(data: string): void;
  abortTransaction(value: number): void;
  // startTransaction(value: number, callback?: (eventId: string) => void): void;
  getConstants(): ConstantesInterface;
}
export default CliSiteFModule as CliSiteFInterface;
