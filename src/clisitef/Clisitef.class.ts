import CliSiteFModule from '../CliSiteFModule';

let instance;

class ClisiTef {
  constructor() {
    if (instance) {
      throw new Error('You can only create one instace!');
    }
    instance = this;
  }

  configure() {
    CliSiteFModule.configure(
      '192.168.0.17',
      '00000000',
      'SE000001',
      '00000000000000',
      '00000000000000',
    );
  }

  ping(pingMsg?: string) {
    CliSiteFModule.ping(`React Ping! ${pingMsg ?? ''}`);
  }

  async setPinpadDisplayMessage(message: string) {
    return await CliSiteFModule.setPinpadDisplayMessage(message);
  }

  async pinpadReadYesNo() {
    return await CliSiteFModule.pinpadReadYesNo('pinpadReadYesNo');
  }

  async pinpadIsPresent() {
    return await CliSiteFModule.pinpadIsPresent();
  }

  async startTransaction(amount: number) {
    try {
      const pinPadIsPresent = await CliSiteFModule.pinpadIsPresent();
      if (pinPadIsPresent) {
        return await CliSiteFModule.startTransaction(amount);
      } else {
        throw new Error('PinPad não encontrado');
      }
    } catch (e) {
      throw e;
    }
  }
}

export default ClisiTef;
