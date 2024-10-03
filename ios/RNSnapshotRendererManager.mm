#ifndef RCT_NEW_ARCH_ENABLED
#import <React/RCTViewManager.h>
#import <React/RCTUIManager.h>
#import "RCTBridge.h"
#import "Utils.h"
#import "RNSnapshotView.h"

@interface RNSnapshotRendererManager : RCTViewManager
@end


@interface RNSnapshotRenderer : UIView

@property (nonatomic, strong) NSString *snapshotID;

@end


@implementation RNSnapshotRenderer

{
  UIView *_snapshotView;
}


-(void)setSnapshotID:(NSString *)snapshotID {
  if (![snapshotID isEqualToString:_snapshotID]) {
    _snapshotID = snapshotID;
    if(_snapshotView) {
      [_snapshotView removeFromSuperview];
    }
    
    NSDictionary<NSString *, UIView *> *snapshotMap = [RNSnapshotView getSnapShotMap];
    _snapshotView = snapshotMap[snapshotID];
    if (_snapshotView != nil) {
      self.autoresizesSubviews = YES;
      _snapshotView.frame = self.bounds;
      _snapshotView.autoresizingMask = (UIViewAutoresizingFlexibleWidth |
                                   UIViewAutoresizingFlexibleHeight);
                                   
      [self addSubview:_snapshotView];
    }
  }
}

@end


@implementation RNSnapshotRendererManager

RCT_EXPORT_MODULE(RNSnapshotRenderer)

- (UIView *)view
{
  return [[RNSnapshotRenderer alloc] init];
}

RCT_EXPORT_VIEW_PROPERTY(snapshotID,  NSString *)


@end
#endif
