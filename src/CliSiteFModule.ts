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
  configure(
    enderecoSitef: string,
    codigoLoja: string,
    numeroTerminal: string,
    cnpjEmpresa: string,
    cnpjLoja: string,
  ): void;
  ping(pingMsg: string): void;
  setPinpadDisplayMessage(
    message: string,
    callback?: (response: any) => void,
  ): Promise<{response: any}>;
  pinpadReadYesNo(message: string): Promise<{response: any}>;
  pinpadIsPresent(): Promise<{response: any}>;
  startTransaction(amount: number): Promise<{response: any}>;
  continueTransaction(data: string): Promise<{response: any}>;
  abortTransaction(value: number): void;
  // startTransaction(value: number, callback?: (eventId: string) => void): void;
  getConstants(): ConstantesInterface;
  eventsMessage: NativeModule;
}
export default CliSiteFModule as CliSiteFInterface;
