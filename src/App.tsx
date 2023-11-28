import React, {useEffect, useState} from 'react';

import {
  Button,
  SafeAreaView,
  StatusBar,
  NativeEventEmitter,
  View,
  TextInput,
  ToastAndroid,
} from 'react-native';

import CliSiteFModule from './CliSiteFModule';
import CalendarModule from './CalendarModule';

const App = () => {
  const [amount, setAmount] = useState<string>();
  const [eventData, setEventData] = useState<{}>();

  const handlePing = () => {
    CliSiteFModule.ping('React Button Clicked!');
  };

  const handlePinpadDisplayMessage = (message: string) => {
    CliSiteFModule.setPinpadDisplayMessage(message);
  };

  const handlePinpadReadYesNo = () =>
    CliSiteFModule.pinpadReadYesNo('pinpadReadYesNo');

  const handlePinpadIsPresent = () => CliSiteFModule.pinpadIsPresent();

  const hanldeStartTransaction = () => {
    if (!isNaN(Number(amount))) {
      CliSiteFModule.startTransaction(Number(amount));
    } else {
      ToastAndroid.show('Insira o valor da compra', 2000);
    }
  };

  const handleContinueTransaction = () => {
    if (eventData) {
      CliSiteFModule.continueTransaction(JSON.stringify(eventData));
    }
  };

  const handleAbortTransaction = (value = -1) => {
    CliSiteFModule.abortTransaction(value);
  };

  const eventEmitter = new NativeEventEmitter(CliSiteFModule.eventsMessage);

  eventEmitter.addListener('eventsMessage', (messages: any) => {
    // const eventmesssage = JSON.parse(messages);
    console.log(typeof messages);
    // console.log('eventsMessage', eventmesssage);

    if (messages && messages?.event) {
      console.log(messages);
      // const data = {
      //   event: messages.getString('event'),
      //   currentStage: messages.getInt('currentStage'),
      //   buffer: messages.getString('buffer'),
      //   shouldContinue: messages.getBoolean('shouldContinue'),
      //   fieldId: messages.getInt('fieldId'),
      //   maxLength: messages.getInt('maxLength'),
      //   minLength: messages.getInt('minLength'),
      // };
      const data = {
        event: messages.event,
        currentStage: messages.currentStage,
        buffer: messages.buffer,
        shouldContinue: messages.shouldContinue,
        fieldId: messages.fieldId,
        maxLength: messages.maxLength,
        minLength: messages.minLength,
      };
      console.log(messages.buffer);

      if (data.buffer === 'Forneca o numero do cartao') {
        handlePinpadDisplayMessage('Entre com o cartão');
        setEventData(data);
      }
    } else {
      console.log('eventsMessage', messages);
    }
  });

  useEffect(() => {
    CliSiteFModule.configure(
      '192.168.0.17',
      '00000000',
      'SE000001',
      '00000000000000',
      '00000000000000',
    );
  }, []);

  return (
    <SafeAreaView>
      <StatusBar barStyle={'dark-content'} backgroundColor="#000" />
      <View style={{margin: 35}}>
        <Button
          title="PING Native Module!"
          color="#841584"
          onPress={handlePing}
        />
      </View>
      <View style={{margin: 35, marginBottom: 15}}>
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
      <View style={{margin: 35, marginBottom: 15}}>
        <Button
          title="Continuar transação!"
          color="#841584"
          onPress={() => handleContinueTransaction()}
        />
      </View>
      <View style={{margin: 35, marginBottom: 15}}>
        <Button
          title="Cancelar transação!"
          color="#841584"
          onPress={() => handleAbortTransaction(-1)}
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
  const onPress = () => {
    CalendarModule.createCalendarEvent(
      'Evento Teste',
      'Curitiba - PR',
      (error, eventId) => {
        console.log(`Created a new event with id ${eventId}`);
      },
    );
  };

  return <Button title="Create Event!" color="#841584" onPress={onPress} />;
};

export default App;
