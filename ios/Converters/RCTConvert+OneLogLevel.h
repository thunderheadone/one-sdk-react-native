//
//  RCTConvert+OneLogLevel.h
//  RCTOne
//
//  Copyright © 2017 Thunderhead. All rights reserved.
//

// As https://github.com/facebook/react-native/releases/tag/v0.40.0
// introduced breaking changes in terms of how React Native
// headers should be imported, we need to use conditional statement
// to choose the right way of adding headers
#if __has_include(<React/RCTConvert.h>)
#import <React/RCTConvert.h>
#else
#import "RCTConvert.h"
#endif

@interface RCTConvert (OneLogLevel)

@end
