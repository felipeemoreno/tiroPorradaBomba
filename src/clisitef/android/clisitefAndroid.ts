import CliSiteFModule from '../../CliSiteFModule';

const CliSiTefAndroid: CliSiteFModule = () => {
  const abortTransaction = async () => {
    const success: boolean = await CliSiteFModule.abortTransaction({
      continua: 0,
    });

    return success ?? false;
  };
};

export default CliSiTefAndroid;
