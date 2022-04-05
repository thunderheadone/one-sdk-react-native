# react-native-one

The Thunderhead ONE SDK React Native Module for iOS and Android.

## Installation

To install the ONE React Module, navigate to your app’s folder and run the following command:

```sh
// yarn
yarn add react-native-one

// npm
npm install react-native-one
```

### iOS

For iOS, run `pod install` in Terminal from the iOS project to install the Thunderhead native SDK dependency.

```sh
// navigate to iOS project folder
pod install
```
*Note:*
* Requires iOS 9+.

### Android

For Android, add the Thunderhead SDK `repository` url and `packagingOptions` to your app gradle file.

```gradle
android {
  // add packagingOptions under the `android` section.
  packagingOptions {
    pickFirst '**/*.so'
  }
}

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
* See example of usage [here](https://github.com/thunderheadone/one-sdk-react-native/tree/master/example/src/App.tsx#L28)

### Send an Interaction
To send an Interaction request without properties, call the following method:
```javascript
One.sendInteraction("/interactionPath", null);
```
* See example of usage [here](https://github.com/thunderheadone/one-sdk-react-native/tree/master/example/src/App.tsx#L55)

To send an Interaction request with properties, call the following method:
```javascript
One.sendInteraction("/interactionPath", {key: 'value'});
```
* See example of usage [here](https://github.com/thunderheadone/one-sdk-react-native/tree/master/example/src/App.tsx#L141)

### Send a response code
To send a response code, call the following method:
```javascript
One.sendResponseCode("/interactionPath", "yourResponseCode");
```
* See example of usage [here](https://github.com/thunderheadone/one-sdk-react-native/tree/master/example/src/App.tsx#L87)

### Get tid
To get the tid for the current app, call the following public method:
```javascript
One.getTid();
```
* See example of usage [here](https://github.com/thunderheadone/one-sdk-react-native/tree/master/example/src/App.tsx#L101)

### Opt an end-user out of tracking
To opt an end-user out of all tracking options, when the end-user does not give permission to be tracked in the client app, call the following method:
```javascript
// Opts out of all tracking options.
One.optOut(true);
```

To opt back in, call the following method:
```javascript
// Opt in for all tracking options.
One.optOut(false);
```

#### Opt an end user out of city country level tracking
To opt an end-user out of city/country level tracking, call the following method:
```javascript
// Calling this will opt the end-user back in for all tracking.
One.optOutCityCountryDetection(true);
```

#### Opt an end user out of keychain Tid storage (iOS only)
To opt an end-user out of all keychain Tid storage, call the following method:
```javascript
One.optOutKeychainTidStorage(true);
```

### Access debug information
To configure logging, call the following method:
```javascript
One.enableLogging(true)
```
* See example of usage [here](https://github.com/thunderheadone/one-sdk-react-native/tree/master/example/src/App.tsx#L25)

## Questions or need help

### Thunderhead ONE Support
_The Thunderhead team is available 24/7 to answer any questions you have. Just submit a ticket [here](https://support.thunderhead.com/hc/en-us/requests/new) or visit our docs page for more detailed installation and usage information._

### Salesforce Interaction Studio Support
_For Salesforce Marketing Cloud Interaction Studio questions, please submit a support ticket via https://help.salesforce.com/home_
