import CliSiteFModule from '../CliSiteFModule';

const clisiTef = {
  configure: () => {
    CliSiteFModule.configure(
      '192.168.0.17',
      '00000000',
      'SE000001',
      '00000000000000',
      '00000000000000',
    );
  },

  ping: (pingMsg?: string) => {
    CliSiteFModule.ping(`React Ping! ${pingMsg ?? ''}`);
  },

  setPinpadDisplayMessage: async (message: string) => {
    return await CliSiteFModule.setPinpadDisplayMessage(message);
  },

  pinpadReadYesNo: async () => {
    return await CliSiteFModule.pinpadReadYesNo('pinpadReadYesNo');
  },

  pinpadIsPresent: async () => {
    return await CliSiteFModule.pinpadIsPresent();
  },
  startTransaction: async (amount: number) => {
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
  },
};

export {clisiTef};
