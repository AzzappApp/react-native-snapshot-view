// This guard prevent this file to be compiled in the old architecture.
#ifdef RCT_NEW_ARCH_ENABLED
#import <React/RCTViewComponentView.h>
#import <UIKit/UIKit.h>

#ifndef RNSnapshotRendererNativeComponent_h
#define RNSnapshotRendererNativeComponent_h

NS_ASSUME_NONNULL_BEGIN

@interface RNSnapshotRenderer : RCTViewComponentView
@end

NS_ASSUME_NONNULL_END

#endif /* RNSnapshotRendererNativeComponent_h */
#endif /* RCT_NEW_ARCH_ENABLED */
