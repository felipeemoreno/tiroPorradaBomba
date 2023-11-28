import {NativeEventEmitter, NativeModules} from 'react-native';

const {CliSiTefSDK} = NativeModules;

class CliSiTefPDV {
  constructor({client, configuration, isSimulated = false}) {
    this.configuration = configuration;
    this.client = client;
    this.isSimulated = isSimulated;

    this._isReady = this._init();
    this.client.setEventHandler(null, this.onPinPadEvent.bind(this));
    this.client.setDataHandler(this.onData.bind(this));
  }

  async _init() {
    return this.client.configure(
      this.configuration.enderecoSitef,
      this.configuration.codigoLoja,
      this.configuration.numeroTerminal,
      this.configuration.cnpjEmpresa,
      this.configuration.cnpjLoja,
    );
  }

  get isReady() {
    return this._isReady;
  }

  async payment(modalidade, valor, {cupomFiscal, dataFiscal, operador = ''}) {
    if (this._transactionStream) {
      throw new Error('Another transaction is already in progress.');
    }

    try {
      const success = await this.client.startTransaction(
        modalidade,
        valor,
        cupomFiscal,
        dataFiscal,
        operador,
      );

      if (!success) {
        throw new Error('Unable to start payment process');
      }
    } catch (error) {
      throw error;
    }

    this._transactionStream = new TransactionStream({
      onCancel: () => this.client.abortTransaction(),
    });

    this.client.setEventHandler(
      this.onTransactionEvent.bind(this),
      this.onPinPadEvent.bind(this),
    );

    return this._transactionStream.stream;
  }

  async continueTransaction(data) {
    return this.client.continueTransaction(data);
  }

  async isPinPadPresent() {
    if (this.isSimulated) {
      const pinPadSimulatedInfo = {
        isPresent: true,
        isConnected: true,
        isReady: true,
      };
      this.pinPadStream.emit(pinPadSimulatedInfo);
      return true;
    }

    const pinPad = await this.client.getPinpadInformation();
    const pinPadStreamInfo = this.pinPadStream.pinPadInfo;
    pinPadStreamInfo.isPresent = pinPad.isPresent;
    this.pinPadStream.emit(pinPadStreamInfo);
    return this.pinPadStream.pinPadInfo.isPresent;
  }

  async cancelTransaction() {
    try {
      await this.client.finishLastTransaction(false);
    } catch (e) {
      if (e.code === '-12') {
        await this.client.abortTransaction();
      } else {
        throw e;
      }
    }

    if (this._transactionStream) {
      this._transactionStream.success(false);
      this._transactionStream.emit(this._transactionStream.transaction);
    }
  }

  onTransactionEvent(event, {exception}) {
    const t = this._transactionStream?.transaction;

    if (t) {
      switch (event) {
        case 'transactionConfirm':
        case 'transactionOk':
          this._transactionStream?.success(true);
          break;
        case 'transactionError':
        case 'transactionFailed':
          this._transactionStream?.success(false);
          break;
        default:
        // noop
      }

      this._transactionStream?.event(event);
      this._transactionStream?.done();
      this._transactionStream = null;
    }
  }

  onPinPadEvent(event, {exception}) {
    const pinPad = this.pinPadStream.pinPadInfo;
    pinPad.event = event;

    switch (event) {
      case 'startBluetooth':
        pinPad.waiting = true;
        pinPad.isBluetoothEnabled = false;
        break;
      case 'endBluetooth':
        pinPad.waiting = false;
        pinPad.isBluetoothEnabled = true;
        break;
      case 'waitingPinPadConnection':
        pinPad.waiting = true;
        pinPad.isConnected = false;
        break;
      case 'pinPadOk':
        pinPad.waiting = false;
        pinPad.isConnected = true;
        break;
      case 'waitingPinPadBluetooth':
        pinPad.waiting = true;
        pinPad.isReady = false;
        break;
      case 'pinPadBluetoothConnected':
        pinPad.waiting = false;
        pinPad.isReady = true;
        break;
      case 'pinPadBluetoothDisconnected':
        pinPad.waiting = false;
        pinPad.isReady = false;
        break;
      case 'unknown':
      case 'genericError':
        this.pinPadStream.error(exception || `Unhandled event ${event}`);
        return;
    }

    this.pinPadStream.emit(pinPad);
  }

  onData(data) {
    this.dataStream.sink.add(data);
  }
}

export default CliSiTefPDV;
