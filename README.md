# react-native-one

The Thunderhead ONE SDK React Native Module for iOS and Android.

## Installation

To install the ONE React Module, navigate to your app’s folder and run the following command:

```sh
yarn add react-native-one
```
or

```sh
npm install react-native-one
```

### iOS

For iOS, you'll also need to navigate to the iOS project folder and initialize the pod to install our native SDK dependency.
```sh
pod install
```

### Android

For Android, you'll need to add the Thunderhead SDK repository url to your app gradle file.

```gradle
repositories {
  maven {
   url 'https://thunderhead.mycloudrepo.io/public/repositories/one-sdk-android'
  }
}
```
*Note:* 
* Requires minimum API level 21+.

## Usage

### Initialization
To initialize the ONE React Native Module, call the following method:

```javascript
import { NativeModules } from 'react-native';
const One = NativeModules.One;

export const ONE_PARAMETERS = {
    "siteKey": "<YOUR-SITE-KEY>",
    "touchpointUri" : "<YOUR-TOUCHPOINT-URI>",
    "apiKey" : "<YOUR-API-KEY>",
    "sharedSecret" : "<YOUR-SHARED-SECRET>",
    "userId" : "<YOUR-USER-ID>",
    "adminMode" : false,
    "hostname" : "<YOUR-HOSTNAME>"
}

One.init(
  ONE_PARAMETERS.siteKey,
  ONE_PARAMETERS.touchpointUri,
  ONE_PARAMETERS.apiKey,
  ONE_PARAMETERS.sharedSecret,
  ONE_PARAMETERS.userId,
  ONE_PARAMETERS.adminMode,
  ONE_PARAMETERS.hostname
);
```
* See example of usage [here](https://github.com/thunderheadone/one-sdk-react-native/tree/master/example/src/App.tsx#L34)

### Send an Interaction 
To send an Interaction request without properties, call the following method:
```javascript
One.sendInteraction("/interactionPath", null);
```
* See example of usage [here](https://github.com/thunderheadone/one-sdk-react-native/tree/master/example/src/App.tsx#L56)

To send an Interaction request with properties, call the following method:
```javascript
One.sendInteraction("/interactionPath", {key: 'value'});
```
* See example of usage [here](https://github.com/thunderheadone/one-sdk-react-native/tree/master/example/src/App.tsx#L145)

### Get tid
To get the tid for the current app, call the following public method:
```javascript
One.getTid();
```
* See example of usage [here](https://github.com/thunderheadone/one-sdk-react-native/tree/master/example/src/App.tsx#L78)

### Access debug information
To configure logging, call the following method:
```javascript
One.setLogLevel(One.LogLevelAll)
```
* See example of usage [here](https://github.com/thunderheadone/one-sdk-react-native/tree/master/example/src/App.tsx#L31)

## Questions or need help

### Thunderhead ONE Support
_The Thunderhead team is available 24/7 to answer any questions you have. Just email onesupport@thunderhead.com or visit our docs page for more detailed installation and usage information._

### Salesforce Interaction Studio Support
_For Salesforce Marketing Cloud Interaction Studio questions, please submit a support ticket via https://help.salesforce.com/home_
