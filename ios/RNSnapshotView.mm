#import "RNSnapshotView.h"

#import <React/RCTBridge.h>
#import <React/RCTBridge+Private.h>
#import <React/RCTUIManager.h>

@implementation RNSnapshotView
RCT_EXPORT_MODULE()

@synthesize bridge = _bridge;

static NSMutableDictionary<NSString *, UIView *> *snapshotMap;

+ (NSMutableDictionary<NSString *, UIView *> *)getSnapShotMap
{
  if (snapshotMap == nil) {
    snapshotMap = [[NSMutableDictionary alloc] init];
  }
  return snapshotMap;
}

- (void)captureSnapshot:(double)viewTag
                resolve:(RCTPromiseResolveBlock)resolve
                 reject:(RCTPromiseRejectBlock)reject
{
  dispatch_async(dispatch_get_main_queue(), ^{
    UIView *view = [self viewForReactTag:viewTag];
    if (view == nil) {
      reject(@"not_found", @"View not found", nil);
      return;
    }

    UIView *snapshot = [view snapshotViewAfterScreenUpdates:NO];
    if (snapshot == nil) {
      reject(@"snapshot_failed", @"Failed to capture snapshot", nil);
      return;
    }

    NSString *identifier = [[NSUUID UUID] UUIDString];
    [[RNSnapshotView getSnapShotMap] setObject:snapshot forKey:identifier];
    resolve(identifier);
  });
}

- (void)releaseSnapshot:(NSString *)uuid
                resolve:(RCTPromiseResolveBlock)resolve
                 reject:(RCTPromiseRejectBlock)reject
{
  if (uuid != nil) {
    [[RNSnapshotView getSnapShotMap] removeObjectForKey:uuid];
  }
  resolve(nil);
}

- (UIView *)viewForReactTag:(double)viewTag
{
  RCTUIManager* uiManager = self.bridge.uiManager;
  if (uiManager == nil) {
    return nil;
  }
  return [uiManager viewForReactTag:[NSNumber numberWithDouble:viewTag]];
}

- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:
    (const facebook::react::ObjCTurboModule::InitParams &)params
{
  return std::make_shared<facebook::react::NativeRNSnapshotViewSpecJSI>(params);
}

@end
