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
  Modal,
  Alert,
} from 'react-native';

import CliSiteFModule from './CliSiteFModule';
import CalendarModule from './CalendarModule';

import {OnData} from './model/onData';
import {clisiTef} from './clisitef/clisitef';

const App = () => {
  const [notifications, setNotifications] = useState<OnData[]>([]);
  const [amount, setAmount] = useState<string>();
  const [modalTitle, setModalTitle] = useState<string>();
  const [inputPlaceholder, setInputPlaceholder] = useState<string>();
  const [modalOpened, setModalOpened] = useState<boolean>(false);

  const handlePing = () => {
    clisiTef.ping();
  };

  const handlePinpadDisplayMessage = async (message: string) => {
    try {
      const response = await clisiTef.setPinpadDisplayMessage(message);
      console.log('response pinpad display', response);
    } catch (e) {
      console.log(e);
    }
  };

  // const handlePinpadReadYesNo = async () => {
  //   const response = clisiTef.pinpadReadYesNo();
  //   console.log('response Read', response);
  // };

  // const handlePinpadIsPresent = async () => {
  //   const response = clisiTef.pinpadIsPresent();
  //   console.log('response is present', response);
  // };

  const hanldeStartTransaction = async () => {
    if (!isNaN(Number(amount))) {
      try {
        const response = await clisiTef.startTransaction(Number(amount));
        console.log('startTransaction', response);
      } catch (e) {
        ToastAndroid.show(`Erro ${e}`, 5000);

        console.log('response error start');
      }
    } else {
      ToastAndroid.show('Insira o valor da compra', 2000);
    }
  };

  const handleContinueTransaction = async (data: string) => {
    try {
      handleModalOpened(false);
      const response = await CliSiteFModule.continueTransaction(data ?? '');
      console.log('response continue', response);
    } catch (error) {}
  };

  const handleAbortTransaction = (value = -1) => {
    CliSiteFModule.abortTransaction(value);
  };

  const handleModalOpened = (value: boolean) => {
    setModalOpened(value);
  };

  useEffect(() => {
    clisiTef.configure();

    const eventEmitter = new NativeEventEmitter(CliSiteFModule.eventsMessage);

    const eventListenerOnData = eventEmitter.addListener(
      'onData',
      (event: any) => {
        console.log('message', event);
        if (event?.event) {
          console.log('event buffer', event.buffer);
          setNotifications(prevState => [event, ...prevState]);
          if (!event.shouldContinue) {
            if (event.event === 'GET_FIELD') {
              setModalTitle(event.buffer);
              setInputPlaceholder('');
              handleModalOpened(true);
            } else if (event.event === 'MENU_TITLE') {
              setModalTitle(event.buffer);
              handleModalOpened(true);
              setInputPlaceholder('');
            } else if (event.event === 'MENU_OPTIONS') {
              setInputPlaceholder(event.buffer);
              !modalOpened && handleModalOpened(true);
            }
          } else {
            console.log('onData', event);
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
      <ModalInput
        title={modalTitle}
        placeholder={inputPlaceholder}
        modalOpened={modalOpened}
        handleModalOpened={handleModalOpened}
        handleContinueTransaction={handleContinueTransaction}
      />
      <View style={{height: 300}}>
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
      {/* <View style={{margin: 15}}>
        <Button
          title="Continuar transação!"
          color="#841584"
          onPress={() => handleContinueTransaction()}
        />
      </View> */}
      <View style={{margin: 15}}>
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
      {/* <View style={{margin: 15}}>
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
      </View> */}

      <View style={{margin: 35, marginTop: 100}}>
        <NewModuleButton />
      </View>
    </SafeAreaView>
  );
};

type ModalInputProps = {
  title?: string;
  placeholder?: string;
  modalOpened: boolean;
  handleModalOpened: (value: boolean) => void;
  handleContinueTransaction: (data: string) => void;
};
const ModalInput: React.FC<ModalInputProps> = ({
  title,
  placeholder,
  modalOpened,
  handleModalOpened,
  handleContinueTransaction,
}) => {
  const [data, setData] = useState<string>();

  const handleOk = () => {
    handleContinueTransaction(data ?? '');
    setData('');
  };

  return (
    <Modal
      animationType="slide"
      transparent={true}
      visible={modalOpened}
      onRequestClose={() => {
        Alert.alert('Modal has been closed.');
        handleModalOpened(!modalOpened);
      }}>
      <View
        style={{
          backgroundColor: '#000000DD',
          flex: 1,
        }}>
        <View style={{justifyContent: 'center', flex: 1, margin: 50}}>
          <Text>{title}</Text>
          <TextInput
            style={{backgroundColor: '#FFF', color: '#000', marginBottom: 10}}
            placeholder={placeholder}
            placeholderTextColor="#666"
            onChangeText={setData}
            value={data}
          />
          <View style={{flexDirection: 'row', justifyContent: 'space-between'}}>
            <Button
              title="Cancelar"
              color="#841584"
              onPress={() => {
                console.log('cancelar button'), handleModalOpened(false);
              }}
            />
            <Button title="OK" color="#841584" onPress={handleOk} />
          </View>
        </View>
      </View>
    </Modal>
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
