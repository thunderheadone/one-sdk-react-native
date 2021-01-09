/*
 *   OneModule.java
 *   Thunderhead
 *
 *   Copyright © 2017 Thunderhead. All rights reserved.
 */

package com.reactnativeone;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.ReadableNativeMap;
import com.facebook.react.bridge.WritableMap;
import com.thunderhead.One;
import com.thunderhead.OneLogLevel;
import com.thunderhead.OneModes;
import com.thunderhead.android.api.codeless.OneCodelessInteractionTrackingConfiguration;
import com.thunderhead.android.api.configuration.OneConfiguration;
import com.thunderhead.android.api.interactions.OneCall;
import com.thunderhead.android.api.interactions.OneCallback;
import com.thunderhead.android.api.interactions.OneInteractionPath;
import com.thunderhead.android.api.interactions.OneRequest;
import com.thunderhead.android.api.interactions.OneResponseCode;
import com.thunderhead.android.api.interactions.OneResponseCodeRequest;
import com.thunderhead.android.api.responsetypes.OneAPIError;
import com.thunderhead.android.api.responsetypes.OneResponse;
import com.thunderhead.android.api.responsetypes.OneSDKError;

import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OneModule extends ReactContextBaseJavaModule {

  public OneModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  public String getName() {
    return "One";
  }

  @ReactMethod
  public void init(String siteKey, String touchpointUri, String apiKey,
                   String sharedSecret, String userId, boolean isAdminMode,
                   String hostName) {
    final OneConfiguration oneConfiguration = new OneConfiguration.Builder()
      .siteKey(siteKey)
      .apiKey(apiKey)
      .sharedSecret(sharedSecret)
      .userId(userId)
      .host(URI.create(hostName))
      .touchpoint(URI.create(touchpointUri))
      .mode(isAdminMode ? OneModes.ADMIN_MODE : OneModes.USER_MODE)
      .build();

    One.setConfiguration(oneConfiguration);
    final OneCodelessInteractionTrackingConfiguration codelessInteractionTrackingConfiguration =
      new OneCodelessInteractionTrackingConfiguration.Builder()
        .disableCodelessInteractionTracking(true)
        .build();
    One.setCodelessInteractionTrackingConfiguration(codelessInteractionTrackingConfiguration);
  }

  @ReactMethod
  public void sendInteraction(String interaction, ReadableMap propertiesMap, final Promise promise) {
    HashMap<String, String> properties = getPropertiesFromReadableMap(propertiesMap);
    final OneRequest sendInteractionRequest = new OneRequest.Builder()
      .interactionPath(new OneInteractionPath(URI.create(interaction)))
      .properties(properties)
      .build();
    final OneCall sendInteractionCall = One.sendInteraction(sendInteractionRequest);

    if (promise == null) {
      sendInteractionCall.enqueue(null);
    } else {
      sendInteractionCall.enqueue(new OneCallback() {
        @Override
        public void onSuccess(@NotNull OneResponse oneResponse) {
          notifyResult(promise,
            "{\"captures: " + oneResponse.getCaptures().toString() + ", " +
                  "\"interactionPath\": \"" + oneResponse.getInteractionPath().getValue().toString() + "\", " +
                  "\"optimizations: " + oneResponse.getOptimizations().toString() + ", " +
                  "\"statusCode\": " + oneResponse.getHttpStatusCode() + ", " +
                  "\"tid\": \"" + oneResponse.getTid() + "\", " +
                  "\"trackers\": " + oneResponse.getTrackers().toString() + "}"
          );
        }

        @Override
        public void onError(@NotNull OneSDKError oneSDKError) {
          notifyProblem(promise, "" + oneSDKError.getSystemCode(), oneSDKError.getErrorMessage());
        }

        @Override
        public void onFailure(@NotNull OneAPIError oneAPIError) {
          notifyProblem(promise, "" + oneAPIError.getHttpStatusCode(), oneAPIError.getErrorMessage());
        }
      });
    }
  }

  @ReactMethod
  public void sendProperties(String interaction, ReadableMap propertiesMap) {
    HashMap<String, String> properties = getPropertiesFromReadableMap(propertiesMap);
    final OneRequest sendPropertiesRequest = new OneRequest.Builder()
      .interactionPath(new OneInteractionPath(URI.create(interaction)))
      .properties(properties)
      .build();

    final OneCall sendPropertiesCall = One.sendProperties(sendPropertiesRequest);
    sendPropertiesCall.enqueue(null);
  }

  @ReactMethod
  public void sendResponseCode(String responseCode, String interaction) {
    final OneResponseCodeRequest responseCodeRequest = new OneResponseCodeRequest.Builder()
      .responseCode(new OneResponseCode(responseCode))
      .interactionPath(new OneInteractionPath(URI.create(interaction)))
      .build();
    One.sendResponseCode(responseCodeRequest).enqueue(null);
  }

  @ReactMethod
  public void getTid(Promise promise) {
    notifyResult(promise, One.getTid());
  }

  @ReactMethod
  public void setLogLevel(Integer logLevel) {
    One.setLogLevel(logLevelFromInteger(logLevel));
  }

  @Override
  public Map<String, Object> getConstants() {
    /*
     * Export the SDK's log levels to js. They will be accessible
     * like this: One.LogLevelNone, One.LogLevelWebService,
     * One.LogLevelFramework and One.LogLevelAll
     */
    final Map<String, Object> constants = new HashMap<>();
    constants.put("LogLevelNone", 0);
    constants.put("LogLevelWebService", 1);
    constants.put("LogLevelFramework", 2);
    constants.put("LogLevelAll", 3);
    return constants;
  }

  private OneLogLevel logLevelFromInteger(Integer logLevel) {
    OneLogLevel level;
    switch (logLevel) {
      case 1:
        level = OneLogLevel.WEB_SERVICE;
        break;
      case 2:
        level = OneLogLevel.FRAMEWORK;
        break;
      case 3:
        level = OneLogLevel.ALL;
        break;
      default:
        level = OneLogLevel.NONE;
    }
    return level;
  }

  private HashMap<String, String> getPropertiesFromReadableMap(ReadableMap readableMap) {
    if (readableMap != null && readableMap instanceof ReadableNativeMap) {
      ReadableMapKeySetIterator iterator = ((ReadableNativeMap) readableMap).keySetIterator();
      HashMap<String, String> hashMap = new HashMap<>();
      while (iterator.hasNextKey()) {
        String key = iterator.nextKey();
        switch (readableMap.getType(key)) {
          case String:
            hashMap.put(key, readableMap.getString(key));
            break;
        }
      }
      return hashMap;
    }
    return null;
  }

  private ArrayList<String> getArrayListFromReadableArray(ReadableArray readableArray) {
    ArrayList<String> result = new ArrayList<>(readableArray.size());
    for (int index = 0; index < readableArray.size(); index++) {
      switch (readableArray.getType(index)) {
        case String: {
          String entry = (String) readableArray.getString(index);
          if (!entry.isEmpty()) {
            result.add(entry);
          }
          break;
        }
      }
    }
    return result;
  }

  private void notifyProblem(Promise promise, Throwable throwable) {
    /*
     * We have to catch the rejection to prevent the following exception:
     *
     * java.lang.RuntimeException: Illegal callback invocation from native module.
     * This callback type only permits a single invocation from native code.
     *
     * It's not clear at the moment why React Native throws the exception relating
     * to this module. There is an issue raised about it:
     * https://github.com/facebook/react-native/issues/13595
     */
    try {
      promise.reject(throwable);
    } catch (RuntimeException e) {
      e.printStackTrace();
    }
  }

  private void notifyProblem(Promise promise, String code, String message) {
    try {
      promise.reject("" + code, message, (WritableMap) null);
    } catch (RuntimeException e) {
      e.printStackTrace();
    }
  }

  private void notifyResult(Promise promise, String result) {
    try {
      promise.resolve(result);
    } catch (RuntimeException e) {
      e.printStackTrace();
    }
  }
}
