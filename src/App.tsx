import React, {useEffect, useState} from 'react';

import {
  Button,
  SafeAreaView,
  StatusBar,
  NativeEventEmitter,
  View,
  TextInput,
  ToastAndroid,
  FlatList,
  Text,
} from 'react-native';

import CliSiteFModule from './CliSiteFModule';
import CalendarModule from './CalendarModule';

import {OnData} from './model/onData';

const App = () => {
  const [notifications, setNotifications] = useState<OnData[]>([]);
  const [amount, setAmount] = useState<string>();
  const [eventData, setEventData] = useState<{}>();

  const handlePing = () => {
    CliSiteFModule.ping('React Button Clicked!');
  };

  const handlePinpadDisplayMessage = async (message: string) => {
    try {
      const response = await CliSiteFModule.setPinpadDisplayMessage(message);
      console.log('response pinpad display', response);
    } catch (e) {
      console.log(e);
    }
  };

  const handlePinpadReadYesNo = async () => {
    const response = await CliSiteFModule.pinpadReadYesNo('pinpadReadYesNo');
    console.log('response', response);
  };

  const handlePinpadIsPresent = async () => {
    const response = await CliSiteFModule.pinpadIsPresent();
    console.log('response is present', response);
  };

  const hanldeStartTransaction = async () => {
    if (!isNaN(Number(amount))) {
      try {
        const response = await CliSiteFModule.startTransaction(Number(amount));
        console.log('startTransaction', response);
      } catch (error) {
        console.log('response error start', error);
      }
    } else {
      ToastAndroid.show('Insira o valor da compra', 2000);
    }
  };

  const handleContinueTransaction = async () => {
    try {
      let data = '';
      if (eventData === 'card') {
        data = '5555666677778884';
      } else if (eventData === 'vencimento') {
        data = '1122';
      } else if (eventData === 'forma de pagamento') {
        data = '1';
      }
      const response = await CliSiteFModule.continueTransaction(data);
      console.log('response continue', response);
    } catch (error) {}
  };

  const handleAbortTransaction = (value = -1) => {
    CliSiteFModule.abortTransaction(value);
  };

  useEffect(() => {
    CliSiteFModule.configure(
      '192.168.0.17',
      '00000000',
      'SE000001',
      '00000000000000',
      '00000000000000',
    );

    const eventEmitter = new NativeEventEmitter(CliSiteFModule.eventsMessage);

    const eventListenerOnData = eventEmitter.addListener(
      'onData',
      (message: any) => {
        console.log('message', message);
        if (message) {
          if (message?.event) {
            console.log('event buffer', message.buffer);
            setNotifications(prevState => [message, ...prevState]);
            // const data: OnData = {
            //   event: message.event,
            //   currentStage: message.currentStage,
            //   buffer: message.buffer,
            //   shouldContinue: message.shouldContinue,
            //   fieldId: message.fieldId,
            //   maxLength: message.maxLength,
            //   minLength: message.minLength,
            // };

            if (message.buffer === 'Forneca o numero do cartao') {
              // handlePinpadDisplayMessage('Entre com o cartão');
              setEventData('card');
            } else if (
              message.buffer === 'Forneca a data de vencimento do cartao (MMAA)'
            ) {
              // handlePinpadDisplayMessage(
              //   'Forneca a data de vencimento do cartao (MMAA)',
              // );
              setEventData('vencimento');
            } else if (message.buffer === 'Selecione a forma de pagamento') {
              // handlePinpadDisplayMessage('Selecione a forma de pagamento');

              setEventData('forma de pagamento');
            }
          } else {
            console.log('onData', message);
          }
        }
      },
    );

    const eventListenerOnTransactionResult = eventEmitter.addListener(
      'onTransactionResult',
      (messages: any) => {
        const data: OnData = {buffer: messages};
        setNotifications(prevState => [data, ...prevState]);
        console.log('onTransactionResult', messages);
      },
    );

    const eventListenerBluetoothEvents = eventEmitter.addListener(
      'bluetoothEvents',
      (messages: any) => {
        const data: OnData = {buffer: messages};
        setNotifications(prevState => [data, ...prevState]);
        console.log('bluetoothEvents', messages);
      },
    );

    return () => {
      eventListenerOnData.remove();
      eventListenerOnTransactionResult.remove();
      eventListenerBluetoothEvents.remove();
    };
  }, []);

  console.log('COMPONENTE RENDERIZADO');
  return (
    <SafeAreaView>
      <StatusBar barStyle={'dark-content'} backgroundColor="#000" />
      <View style={{height: 200}}>
        <FlatList
          data={notifications}
          renderItem={({item}) => <Text>{item.buffer}</Text>}
          ListEmptyComponent={<Text>Sem dados</Text>}
        />
      </View>
      <View style={{margin: 15}}>
        <Button
          title="PING Native Module!"
          color="#841584"
          onPress={handlePing}
        />
      </View>
      <View style={{margin: 15}}>
        <TextInput
          style={{backgroundColor: '#FFF', color: '#000', marginBottom: 10}}
          placeholder="Valor"
          placeholderTextColor="#666"
          onChangeText={setAmount}
          value={amount}
          keyboardType="number-pad"
        />
        <Button
          title="Start Transaction!"
          color="#841584"
          onPress={hanldeStartTransaction}
        />
      </View>
      <View style={{margin: 15}}>
        <Button
          title="Continuar transação!"
          color="#841584"
          onPress={() => handleContinueTransaction()}
        />
      </View>
      <View style={{margin: 15}}>
        <Button
          title="Cancelar transação!"
          color="#841584"
          onPress={() => {
            handleAbortTransaction(-1), setEventData([]);
          }}
        />
      </View>
      <View style={{margin: 15}}>
        <Button
          title="PinPad Message Botão 1!"
          color="#841584"
          onPress={() => handlePinpadDisplayMessage('Botão 1')}
        />
      </View>
      <View style={{margin: 15}}>
        <Button
          title="PinPad Read Yes No!"
          color="#841584"
          onPress={() => handlePinpadReadYesNo()}
        />
      </View>
      <View style={{margin: 15}}>
        <Button
          title="PinPad is Present!"
          color="#841584"
          onPress={() => handlePinpadIsPresent()}
        />
      </View>

      <View style={{margin: 35, marginTop: 100}}>
        <NewModuleButton />
      </View>
    </SafeAreaView>
  );
};

const NewModuleButton = () => {
  const onPress = async () => {
    try {
      const eventId = await CalendarModule.createCalendarEvent(
        'Evento Teste',
        'Curitiba - PR',
      );

      console.log(`Created a new event with id ${eventId}`);
    } catch (e) {
      console.error(e);
    }
  };

  return <Button title="Create Event!" color="#841584" onPress={onPress} />;
};

export default App;
