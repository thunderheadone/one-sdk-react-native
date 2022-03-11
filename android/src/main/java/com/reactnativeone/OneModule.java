// OneModule.java

package com.reactnativeone;

import android.os.Build;
import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.ReadableNativeMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableNativeMap;
import com.thunderhead.mobile.One;
import com.thunderhead.mobile.codeless.OneCodelessInteractionTrackingConfiguration;
import com.thunderhead.mobile.configuration.OneConfiguration;
import com.thunderhead.mobile.configuration.OneMode;
import com.thunderhead.mobile.interactions.OneInteractionPath;
import com.thunderhead.mobile.interactions.OneRequest;
import com.thunderhead.mobile.interactions.OneResponseCode;
import com.thunderhead.mobile.interactions.OneResponseCodeRequest;
import com.thunderhead.mobile.logging.OneLogComponent;
import com.thunderhead.mobile.logging.OneLogLevel;
import com.thunderhead.mobile.logging.OneLoggingConfiguration;
import com.thunderhead.mobile.optout.OneOptInOptions;
import com.thunderhead.mobile.optout.OneOptOutConfiguration;

import com.thunderhead.mobile.responsetypes.OneAPIError;
import com.thunderhead.mobile.responsetypes.OneResponse;
import com.thunderhead.mobile.responsetypes.OneSDKError;
import com.thunderhead.mobile.responsetypes.OptimizationPoint;

import java.net.URI;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class OneModule extends ReactContextBaseJavaModule {
  protected static final String NAME = "OneModule";
  private final ExecutorService executor = Executors.newSingleThreadExecutor();

  public OneModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  public String getName() {
    return NAME;
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
      .mode(isAdminMode ? OneMode.ADMIN : OneMode.USER)
      .build();

    One.setConfiguration(oneConfiguration);
    final OneCodelessInteractionTrackingConfiguration codelessInteractionTrackingConfiguration =
      new OneCodelessInteractionTrackingConfiguration.Builder()
        .disableCodelessInteractionTracking(true)
        .build();
    One.setCodelessInteractionTrackingConfiguration(codelessInteractionTrackingConfiguration);
  }

  @ReactMethod
  public void sendInteraction(String interactionPath, ReadableMap propertiesMap, final Promise promise) {
    HashMap<String, String> properties = getPropertiesFromReadableMap(propertiesMap);
    final OneRequest sendInteractionRequest = new OneRequest.Builder()
      .interactionPath(new OneInteractionPath(URI.create(interactionPath)))
      .properties(properties)
      .build();
    executor.submit(() -> {
      try {
        OneResponse response;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
          response = One.sendInteraction(true, sendInteractionRequest).join();
        } else {
          response = One.sendInteractionLegacySupport(true, sendInteractionRequest).join();
        }
        One.processResponse(response);
        WritableNativeMap responseMap = responseObjectToReadableMap(response);
        notifyResult(promise, responseMap);
      } catch (ExecutionException error) {
        notifyProblem(promise, Integer.toString(error.hashCode()), error.getLocalizedMessage());
        Log.e(NAME, "[Thunderhead] Send Interaction Completion Error: " + error.getCause());
      } catch (OneSDKError error) {
        notifyProblem(promise, Integer.toString(error.getSystemCode()), error.getLocalizedMessage());
        Log.e(NAME, "[Thunderhead] Send Interaction SDK Error: " + error.getErrorMessage());
      } catch (OneAPIError error) {
        notifyProblem(promise, Integer.toString(error.getHttpStatusCode()), error.getLocalizedMessage());
        Log.e(NAME, "[Thunderhead] Send Interaction Api Error: " + error.getErrorMessage());
      }
    });
  }

  @ReactMethod
  public void sendProperties(String interactionPath, ReadableMap propertiesMap, final Promise promise) {
    HashMap<String, String> properties = getPropertiesFromReadableMap(propertiesMap);
    final OneRequest sendPropertiesRequest = new OneRequest.Builder()
      .interactionPath(new OneInteractionPath(URI.create(interactionPath)))
      .properties(properties)
      .build();
    executor.submit(() -> {
      try {
        OneResponse response;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
          response = One.sendProperties(true, sendPropertiesRequest).join();
        } else {
          response = One.sendPropertiesLegacySupport(true, sendPropertiesRequest).join();
        }
        One.processResponse(response);
        WritableNativeMap responseMap = responseObjectToReadableMap(response);
        notifyResult(promise, responseMap);
      } catch (ExecutionException error) {
        notifyProblem(promise, Integer.toString(error.hashCode()), error.getLocalizedMessage());
        Log.e(NAME, "[Thunderhead] Send Properties Completion Error: " + error.getCause());
      } catch (OneSDKError error) {
        notifyProblem(promise, Integer.toString(error.getSystemCode()), error.getLocalizedMessage());
        Log.e(NAME, "[Thunderhead] Send Properties SDK Error: " + error.getErrorMessage());
      } catch (OneAPIError error) {
        notifyProblem(promise, Integer.toString(error.getHttpStatusCode()), error.getLocalizedMessage());
        Log.e(NAME, "[Thunderhead] Send Properties Api Error: " + error.getErrorMessage());
      }
    });
  }

  @ReactMethod
  public void sendResponseCode(String interactionPath, String responseCode, final Promise promise) {
    executor.submit(() -> {
      final OneResponseCodeRequest responseCodeRequest = new OneResponseCodeRequest.Builder()
        .responseCode(new OneResponseCode(responseCode))
        .interactionPath(new OneInteractionPath(URI.create(interactionPath)))
        .build();

      try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
          One.sendResponseCode(true, responseCodeRequest);
        } else {
          One.sendResponseCodeLegacySupport(true, responseCodeRequest);
        }
        notifyResult(promise, null);
      } catch (ExecutionException error) {
        notifyProblem(promise, Integer.toString(error.hashCode()), error.getLocalizedMessage());
        Log.e(NAME, "[Thunderhead] Send Response Code Completion Error: " + error.getCause());
      } catch (OneSDKError error) {
        notifyProblem(promise, Integer.toString(error.getSystemCode()), error.getLocalizedMessage());
        Log.e(NAME, "[Thunderhead] Send Response Code SDK Error: " + error.getErrorMessage());
      } catch (OneAPIError error) {
        notifyProblem(promise, Integer.toString(error.getHttpStatusCode()), error.getLocalizedMessage());
        Log.e(NAME, "[Thunderhead] Send Response Code Api Error: " + error.getErrorMessage());
      }
    });
  }

  @ReactMethod
  public void getTid(Promise promise) {
    notifyResult(promise, One.getTid());
  }

  @ReactMethod
  public void enableLogging(Boolean enabled) {
    if (enabled == null) {
      enabled = true;
    }

    OneLoggingConfiguration.Builder builder = OneLoggingConfiguration.builder().log(OneLogComponent.ANY);
    if (enabled) {
      builder.log(OneLogLevel.VERBOSE).log(OneLogLevel.DEBUG);
    } else {
      builder.log(OneLogLevel.WARN).log(OneLogLevel.ERROR);
    }
    One.setLoggingConfiguration(builder.build());
  }

  @ReactMethod
  public void optOut(Boolean optOut) {
    OneOptOutConfiguration.Builder builder = new OneOptOutConfiguration.Builder();

    if (optOut == null) {
      optOut = false;
    }

    builder.optOut(optOut);
    One.setOptOutConfiguration(builder.build());
  }

  @ReactMethod
  public void optOutCityCountryDetection(Boolean optOut) {
    OneOptOutConfiguration.Builder builder = new OneOptOutConfiguration.Builder();

    if (optOut == null) {
      optOut = false;
    }

    EnumSet<OneOptInOptions> optInOptions = EnumSet.noneOf(OneOptInOptions.class);
    if (!optOut) {
      optInOptions.add(OneOptInOptions.CITY_COUNTRY_DETECTION);
    }
    builder.optOut(false);
    builder.optInOptions(optInOptions);
    One.setOptOutConfiguration(builder.build());
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

  // Convert OneResponse Class to HashMap so React can read it.
  private WritableNativeMap responseObjectToReadableMap(OneResponse response) {
    if (response != null) {
      HashMap<String, Object> responseMap = new HashMap<>();
      WritableNativeMap writableMap = new WritableNativeMap();

      writableMap.putString("tid", response.getTid());
      writableMap.putString("interactionPath", response.getInteractionPath().getValue().getPath());

      if (!response.getOptimizationPoints().isEmpty()) {
        WritableNativeArray writableOptimizationsArray = new WritableNativeArray();

        for (OptimizationPoint point : response.getOptimizationPoints()) {
          WritableNativeMap writableOptimizationPointMap = new WritableNativeMap();

          writableOptimizationPointMap.putString("data", point.getData());
          writableOptimizationPointMap.putString("path", point.getPath());
          writableOptimizationPointMap.putString("responseId", point.getResponseId());
          writableOptimizationPointMap.putString("dataMimeType", point.getDataMimeType());
          writableOptimizationPointMap.putString("directives", point.getDirectives());
          writableOptimizationPointMap.putString("name", point.getName());
          writableOptimizationPointMap.putString("viewPointName", point.getViewPointName());
          writableOptimizationPointMap.putString("viewPointId", point.getViewPointId());
          writableOptimizationsArray.pushMap(writableOptimizationPointMap);
        }
        writableMap.putArray("optimizations", writableOptimizationsArray);
      }
      return writableMap;
    }
    return null;
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
      promise.reject(code, message, null);
    } catch (RuntimeException e) {
      e.printStackTrace();
    }
  }

  private void notifyResult(Promise promise, Object result) {
    try {
      promise.resolve(result);
    } catch (RuntimeException e) {
      e.printStackTrace();
    }
  }
}
