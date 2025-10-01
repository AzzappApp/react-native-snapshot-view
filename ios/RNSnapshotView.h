
#import "RNSnapshotViewSpec.h"
#import <UIKit/UIKit.h>

@interface RNSnapshotView : NSObject <NativeRNSnapshotViewSpec>

+ (NSMutableDictionary<NSString *, UIView *> *)getSnapShotMap;

@end
