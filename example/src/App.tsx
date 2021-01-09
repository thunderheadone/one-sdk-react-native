/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import {
  AppRegistry,
  StyleSheet,
  Text,
  View,
  ScrollView,
  Switch,
  NativeModules,
  Linking,
  TouchableOpacity,
  Platform
} from 'react-native';

import {ONE_PARAMETERS} from '../config/one'

const One = NativeModules.One;
const interaction = "/Interaction";
const properties = {key:"value"};

class AwesomeProject extends Component {
  
  constructor() {
    super();
    One.setLogLevel(One.LogLevelAll)

    // Init ONE 
    One.init(
      ONE_PARAMETERS.siteKey,
      ONE_PARAMETERS.touchpointUri,
      ONE_PARAMETERS.apiKey,
      ONE_PARAMETERS.sharedSecret,
      ONE_PARAMETERS.userId,
      ONE_PARAMETERS.adminMode,
      ONE_PARAMETERS.hostname
    );
  }

  _onSendInteractionWithPropertiesButtonPress = () => {
    console.log("Sending interaction with properties...");
    One.sendInteraction(interaction, properties).then((response) => {
      console.log(response);
    }, (error) => {
      console.log(error);
    }) 
  };

  _onSendInteractionWithoutPropertiesButtonPress = () => {
    console.log("Sending interaction without properties...");
    One.sendInteraction(interaction, null).then((response) => {
      console.log(response);
    },(error) => {
      console.log(error);
    }) 
  };

  _onSendPropertiesButtonPress = () => {
    console.log("Sending properties...");
    One.sendProperties(interaction, properties);
  };

  _onSendResponseCodeButtonPress = () => {
    console.log("Sending response code...");
    // This is just an example response code. A response code would 
    // typically be returned in an optimization.
    var responseCode = "dGlkPThmZDhkZmIwLTIwNzAtNDk5ZC04NjczLWEyM2YxNDNiYjhlNSxhYz0yOTAzMjM5OTcsY250PTI5NzIyNDA0MCxvcD0xNjkxNTc5MzAscnQ9UE9TSVRJVkVfQ0xJQ0ssc2s9T05FLUFUN0JUU0ExSEotNzQyMg";
    One.sendResponseCode(responseCode, interaction);
  };

  _onGetTidButtonPress = () => {
    console.log("Getting tid...");
    One.getTid().then((tid) => {
      alert(tid);
      console.log(tid);
    });
  };

  renderHeader = (title) => {
    return (
      <View style={styles.header}>
        <Text style={styles.headerText}>{title}</Text>
      </View>
    );
  }

  renderButton = (key, title, onPress) => {
    return (
      <TouchableOpacity key={key} onPress={onPress}>
        <Text style={styles.buttonText}>{title}</Text>
      </TouchableOpacity>
    );
  }

  renderSection = (name, sectionStyle) => {
    return (
      <View>
        <Text style={sectionStyle}>{name}</Text>
        <View style={styles.line} />
      </View>
    )
  }

  render() {
    return (
      <View style={styles.rootView}>
        {this.renderHeader("React Example")}
        <ScrollView contentContainerStyle={styles.scrollView}>
          <View style={styles.scrollViewContents}>
            {this.renderSection("Send Interaction", styles.section)}
            {this.renderButton("send_interaction_with_properties", "Send Interaction with properties", this._onSendInteractionWithPropertiesButtonPress)}
            {this.renderButton("send_interaction_without_properties", "Send Interaction without properties", this._onSendInteractionWithoutPropertiesButtonPress)}

            {this.renderSection("Send Properties", styles.section)}
            {this.renderButton("send_properties", "Send Properties", this._onSendPropertiesButtonPress)}
            {this.renderButton("send_response_code", "Send response code", this._onSendResponseCodeButtonPress)}

            {this.renderSection("Tid", styles.section)}
            {this.renderButton("get_tid", "Get tid", this._onGetTidButtonPress)}
          </View>
         </ScrollView>
       </View>
    );
  }
}

const styles = StyleSheet.create({
  rootView: {
    flex: 1,
    backgroundColor: '#1F1D28'
  },
  header: {
    backgroundColor: '#EE5158',
    justifyContent: 'center',
    alignItems: 'center',
    height: 80,
    paddingTop: 40,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.2,
    elevation: 2,
    position: 'relative'
  },
  headerText: {
    fontSize: 20,
    color: 'white'
  },
  scrollView: {
    padding:15
  },
  section: {
    paddingTop:30,
    paddingBottom:5,
    fontSize: 18,
    color: 'white',
    fontWeight: 'bold'
  },
  subSection: {
    paddingTop:5,
    fontSize: 15,
    fontWeight: 'bold'
  },
  instructions: {
    paddingLeft: 5,
    backgroundColor: 'transparent',
    color: '#333333',
    marginBottom: 5
  },
  line: {
    width: "100%",
    backgroundColor: 'grey',
    height: 1,
    marginTop: 5,
    marginBottom: 5
  },
  buttonText: {
    fontSize: 16,
    color: '#46BCDE',
    marginTop: 5,
    marginBottom: 5,
  }
});

export default AwesomeProject;