/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * Generated with the TypeScript template
 * https://github.com/react-native-community/react-native-template-typescript
 *
 * @format
 */

import React, {useState} from 'react';
import {
  NativeModules,
  Button,
  SafeAreaView,
  StatusBar,
  NativeEventEmitter,
  View,
  TextInput,
  ToastAndroid,
} from 'react-native';
// import CalendarModule from './CalendarModule';

const App = () => {
  const [amount, setAmount] = useState<string>();
  const {CliSiteFModule, eventoTeste} = NativeModules;

  // const {DEFAULT_EVENT_NAME} = CalendarModule.getConstants();
  // console.log(DEFAULT_EVENT_NAME);

  const handlePing = () => {
    // CalendarModule.createCalendarEvent('Party', 'My House', eventId => {
    //   console.log(`Created a new event with id ${eventId}`);
    //   CalendarModule.showToast(`Event Created ${eventId}`);
    // });

    CliSiteFModule.ping('React Button Click message');
  };

  const hanldeStartTransaction = () => {
    if (!isNaN(Number(amount))) {
      CliSiteFModule.startTransaction(Number(amount));
    } else {
      ToastAndroid.show('Insira o valor da compra', 2000);
    }
  };

  const eventEmitter = new NativeEventEmitter(eventoTeste);

  eventEmitter.addListener('eventoTeste', parametros => {
    console.log('parametros', parametros);
  });

  return (
    <SafeAreaView>
      <StatusBar barStyle={'dark-content'} backgroundColor="#000" />
      <View style={{marginBottom: 35}}>
        <Button
          title="Click to invoke your native module!"
          color="#841584"
          onPress={handlePing}
        />
      </View>
      <View style={{marginBottom: 35}}>
        <TextInput
          style={{backgroundColor: '#FFF', color: '#000'}}
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
    </SafeAreaView>
  );
};

export default App;
