#import "One.h"
#import <UIKit/UIKit.h>
#import <Thunderhead/One.h>

@implementation RCTOne

RCT_EXPORT_MODULE(One)

RCT_EXPORT_METHOD(init:(NSString *)siteKey uri:(NSString *)uri apiKey:(NSString *)apiKey sharedSecret:(NSString *)sharedSecret userId:(NSString *)userId adminMode:(BOOL)adminMode hostName:(NSString *)hostName)
{
    [One startSessionWithSK:siteKey
                        uri:uri
                     apiKey:apiKey
               sharedSecret:sharedSecret
                     userId:userId
                  adminMode:adminMode
                   hostName:hostName];
    [One disableAutomaticInteractionDetection:YES];
}

RCT_EXPORT_METHOD(setLogLevel:(OneLogLevel)level)
{
    [One setLogLevel:level];
}

RCT_EXPORT_METHOD(sendInteraction:(NSString *)interaction
                  withProperties:(NSDictionary *)properties
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)
{
    [One sendInteraction:interaction withProperties:properties andBlock:^(NSDictionary *response, NSError *error) {
        if (error && reject) {
            reject([NSString stringWithFormat:@"%@",@(error.code)],error.localizedDescription, error);
        } else if (resolve) {
            resolve(response);
        }
    }];
}

// The parameters of this method have been swapped in this method
// declaration in order to reconcile with the ONE SDK for Android
// and Javascript.
RCT_EXPORT_METHOD(sendProperties:(NSString *)interaction 
                  forInteraction:(NSDictionary *)properties)
{
    [One sendProperties:properties forInteractionPath:interaction];
}

RCT_EXPORT_METHOD(sendBaseTouchpointProperties:(NSDictionary *)properties)
{
    [One sendBaseTouchpointProperties:properties];
}

RCT_EXPORT_METHOD(sendResponseCode:(NSString *)responseCode forInteraction:(NSString *)interaction)
{
    [One sendResponseCode:responseCode forInteractionPath:interaction];
}

RCT_EXPORT_METHOD(getTid:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)
{
    if (!resolve) {
        return;
    }
    
    resolve([One getTid]);
}

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}

+ (BOOL)requiresMainQueueSetup
{
    return YES;
}

- (NSDictionary *)constantsToExport
{
  return @{ 
    @"LogLevelNone" : @(kOneLogLevelNone),
		@"LogLevelAll" : @(kOneLogLevelAll),
		@"LogLevelWebService" : @(kOneLogLevelWebService),
		@"LogLevelFramework" : @(kOneLogLevelFramework)
	};
}


@end
