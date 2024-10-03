
#ifdef RCT_NEW_ARCH_ENABLED
#import "RNSnapshotViewSpec.h"

@interface RNSnapshotView : NSObject <NativeRNSnapshotViewSpec>
#else
#import <React/RCTBridgeModule.h>

@interface RNSnapshotView : NSObject <RCTBridgeModule>
#endif


+(NSDictionary<NSString*, UIView *> *)getSnapShotMap;

@end
