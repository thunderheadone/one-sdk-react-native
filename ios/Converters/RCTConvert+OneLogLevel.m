//
//  RCTConvert+OneLogLevel.m
//  RCTOne
//
//  Copyright © 2017 Thunderhead. All rights reserved.
//

#import "RCTConvert+OneLogLevel.h"
#import <Thunderhead/One.h>

@implementation RCTConvert (OneLogLevel)

RCT_ENUM_CONVERTER(OneLogLevel, (@{@"LogLevelNone" : @(kOneLogLevelNone), @"LogLevelAll" : @(kOneLogLevelAll), @"LogLevelWebService" : @(kOneLogLevelWebService), @"LogLevelFramework" : @(kOneLogLevelFramework)}), kOneLogLevelNone, integerValue);

@end
