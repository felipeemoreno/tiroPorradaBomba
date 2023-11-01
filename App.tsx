/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * Generated with the TypeScript template
 * https://github.com/react-native-community/react-native-template-typescript
 *
 * @format
 */

import React from 'react';
import {NativeModules, Button, SafeAreaView, StatusBar} from 'react-native';
import CalendarModule from './CalendarModule';

const App = () => {
  const {CliSiteFModule} = NativeModules;

  const {DEFAULT_EVENT_NAME} = CalendarModule.getConstants();
  console.log(DEFAULT_EVENT_NAME);

  const onPress = () => {
    CalendarModule.createCalendarEvent('Party', 'My House', eventId => {
      console.log(`Created a new event with id ${eventId}`);
      CalendarModule.showToast(`Event Created ${eventId}`);
    });

    CliSiteFModule.ping('Ola mundo 2');
  };

  return (
    <SafeAreaView>
      <StatusBar barStyle={'dark-content'} backgroundColor="#000" />
      <Button
        title="Click to invoke your native module!"
        color="#841584"
        onPress={onPress}
      />
    </SafeAreaView>
  );
};

export default App;
