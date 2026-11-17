import React from 'react';
import { NavigationContainer } from '@react-navigation/native';
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import { StatusBar } from 'expo-status-bar';
import CustomerProfile from './src/screens/CustomerProfile';
import OrderTransaction from './src/screens/OrderTransaction';

const Tab = createBottomTabNavigator();

export default function App() {
  return (
    <NavigationContainer>
      <StatusBar style="dark" />
      <Tab.Navigator screenOptions={{ tabBarActiveTintColor: '#C62828', headerTitle: 'Chef Maria' }}>
        <Tab.Screen name="Profile" component={CustomerProfile} />
        <Tab.Screen name="Order" component={OrderTransaction} />
      </Tab.Navigator>
    </NavigationContainer>
  );
}
