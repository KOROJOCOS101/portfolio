import React from 'react';
import { View, Text, TextInput, StyleSheet, ScrollView, Button, Alert } from 'react-native';

const OrderTransaction: React.FC = () => {
  const handleSubmit = () => {
    Alert.alert('Order transaction data has been submitted.');
  };

  return (
    <ScrollView contentContainerStyle={styles.container}>
      <Text style={styles.heading}>Order Transaction</Text>

      <TextInput placeholder="Order Date" style={styles.input} />
      <TextInput placeholder="Menu Item Ordered" style={styles.input} />
      <TextInput placeholder="Special Instructions" multiline numberOfLines={3} style={[styles.input, styles.multiline]} />
      <TextInput placeholder="Payment Method" style={styles.input} />
      <TextInput placeholder="Next Reservation Date" style={styles.input} />

      <View style={styles.buttonContainer}>
        <Button title="Submit" onPress={handleSubmit} color="#C62828" />
      </View>
    </ScrollView>
  );
};

const styles = StyleSheet.create({
  container: {
    padding: 20,
    backgroundColor: '#fff',
  },
  heading: {
    fontSize: 24,
    color: '#C62828',
    fontWeight: 'bold',
    marginBottom: 20,
  },
  input: {
    borderColor: '#C62828',
    borderWidth: 1,
    padding: 10,
    marginBottom: 12,
    borderRadius: 8,
  },
  multiline: {
    height: 80,
    textAlignVertical: 'top',
  },
  buttonContainer: {
    marginTop: 20,
  },
});

export default OrderTransaction;