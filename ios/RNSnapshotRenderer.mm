#ifdef RCT_NEW_ARCH_ENABLED
#import "RNSnapshotRenderer.h"

#import <react/renderer/components/RNSnapshotViewSpec/ComponentDescriptors.h>
#import <react/renderer/components/RNSnapshotViewSpec/EventEmitters.h>
#import <react/renderer/components/RNSnapshotViewSpec/Props.h>
#import <react/renderer/components/RNSnapshotViewSpec/RCTComponentViewHelpers.h>

#import "RCTFabricComponentsPlugins.h"
#import "RNSnapshotView.h"

using namespace facebook::react;

@interface RNSnapshotRenderer () <RCTRNSnapshotRendererViewProtocol>

@end

@implementation RNSnapshotRenderer {
    UIView * _view;
}

+ (ComponentDescriptorProvider)componentDescriptorProvider
{
    return concreteComponentDescriptorProvider<RNSnapshotRendererComponentDescriptor>();
}

- (instancetype)initWithFrame:(CGRect)frame
{
  if (self = [super initWithFrame:frame]) {
    static const auto defaultProps = std::make_shared<const RNSnapshotRendererProps>();
    _props = defaultProps;

    _view = [[UIView alloc] init];

    self.contentView = _view;
  }

  return self;
}

- (void)updateProps:(Props::Shared const &)props oldProps:(Props::Shared const &)oldProps
{
  const auto &oldViewProps = *std::static_pointer_cast<RNSnapshotRendererProps const>(_props);
  const auto &newViewProps = *std::static_pointer_cast<RNSnapshotRendererProps const>(props);

  if (oldViewProps.snapshotID != newViewProps.snapshotID) {
    NSDictionary<NSString *, UIView *> *snapshotMap = [RNSnapshotView getSnapShotMap];
    self.contentView = snapshotMap[[NSString stringWithCString:newViewProps.snapshotID.c_str()
                                   encoding:[NSString defaultCStringEncoding]]];
  }

  [super updateProps:props oldProps:oldProps];
}

Class<RCTComponentViewProtocol> RNSnapshotRendererCls(void)
{
    return RNSnapshotRenderer.class;
}

@end
#endif
