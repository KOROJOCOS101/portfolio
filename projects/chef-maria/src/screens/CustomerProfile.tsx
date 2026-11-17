import React from 'react';
import { View, Text, TextInput, StyleSheet, ScrollView, Button, Alert } from 'react-native';

const CustomerProfile: React.FC = () => {
  const handleSubmit = () => {
    Alert.alert('Customer details have been submitted.');
  };

  return (
    <ScrollView contentContainerStyle={styles.container}>
      <Text style={styles.heading}>Customer Profile</Text>

      <TextInput placeholder="First Name" style={styles.input} />
      <TextInput placeholder="Surname" style={styles.input} />
      <TextInput placeholder="Middle Name" style={styles.input} />
      <TextInput placeholder="Date of Birth" style={styles.input} />
      <TextInput placeholder="Home Address" style={styles.input} />

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
  buttonContainer: {
    marginTop: 20,
  },
});

export default CustomerProfile;