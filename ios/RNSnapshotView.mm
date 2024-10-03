#import "RNSnapshotView.h"
#import <React/RCTUIManager.h>
#if __has_include(<React/RCTUIManagerUtils.h>)
#import <React/RCTUIManagerUtils.h>
#endif
#import <React/RCTBridge.h>

@implementation RNSnapshotView
RCT_EXPORT_MODULE()

@synthesize bridge = _bridge;

- (dispatch_queue_t)methodQueue
{
  return RCTGetUIManagerQueue();
}

static NSMutableDictionary<NSString*, UIView *> *snapshotMap;

+(NSDictionary<NSString *,UIView *> *)getSnapShotMap {
  if (snapshotMap == nil) {
    snapshotMap = [[NSMutableDictionary alloc] init];
  }
  return snapshotMap;
}

RCT_EXPORT_METHOD(
  captureSnapshot: (double)viewTag
  resolve:(RCTPromiseResolveBlock)resolve
  reject:(RCTPromiseRejectBlock)reject) {
  [self.bridge.uiManager addUIBlock:^(__unused RCTUIManager *uiManager, NSDictionary<NSNumber *, UIView *> *viewRegistry) {
    UIView *view = viewRegistry[[NSNumber numberWithDouble:viewTag]];
    if (view != nil) {
      UIView *snapshot = [view snapshotViewAfterScreenUpdates:NO];
      if (snapshotMap == nil) {
        snapshotMap = [[NSMutableDictionary alloc] init];
      }
      NSUUID *uuid = [NSUUID UUID];
      NSString *str = [uuid UUIDString];
      [snapshotMap setValue:snapshot forKey:str];
      resolve(str);
    } else {
      reject(@"not_found", @"View not found", nil);
    }
  }];
}

RCT_EXPORT_METHOD(
  releaseSnapshot: (NSString *)uuid
  resolve:(RCTPromiseResolveBlock)resolve
  reject:(RCTPromiseRejectBlock)reject) {
  if (uuid != nil) {
   if (snapshotMap == nil) {
     snapshotMap = [[NSMutableDictionary alloc] init];
   }
   [snapshotMap removeObjectForKey:uuid];
  }
  resolve(nil);
}

// Don't compile this code when we build for the old architecture.
#ifdef RCT_NEW_ARCH_ENABLED
- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:
    (const facebook::react::ObjCTurboModule::InitParams &)params
{
    return std::make_shared<facebook::react::NativeRNSnapshotViewSpecJSI>(params);
}
#endif


@end
