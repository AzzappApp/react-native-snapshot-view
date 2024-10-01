#ifdef RCT_NEW_ARCH_ENABLED
#import "ReactNativeSnapshotViewView.h"

#import <react/renderer/components/RNReactNativeSnapshotViewViewSpec/ComponentDescriptors.h>
#import <react/renderer/components/RNReactNativeSnapshotViewViewSpec/EventEmitters.h>
#import <react/renderer/components/RNReactNativeSnapshotViewViewSpec/Props.h>
#import <react/renderer/components/RNReactNativeSnapshotViewViewSpec/RCTComponentViewHelpers.h>

#import "RCTFabricComponentsPlugins.h"
#import "Utils.h"

using namespace facebook::react;

@interface ReactNativeSnapshotViewView () <RCTReactNativeSnapshotViewViewViewProtocol>

@end

@implementation ReactNativeSnapshotViewView {
    UIView * _view;
}

+ (ComponentDescriptorProvider)componentDescriptorProvider
{
    return concreteComponentDescriptorProvider<ReactNativeSnapshotViewViewComponentDescriptor>();
}

- (instancetype)initWithFrame:(CGRect)frame
{
  if (self = [super initWithFrame:frame]) {
    static const auto defaultProps = std::make_shared<const ReactNativeSnapshotViewViewProps>();
    _props = defaultProps;

    _view = [[UIView alloc] init];

    self.contentView = _view;
  }

  return self;
}

- (void)updateProps:(Props::Shared const &)props oldProps:(Props::Shared const &)oldProps
{
    const auto &oldViewProps = *std::static_pointer_cast<ReactNativeSnapshotViewViewProps const>(_props);
    const auto &newViewProps = *std::static_pointer_cast<ReactNativeSnapshotViewViewProps const>(props);

    if (oldViewProps.color != newViewProps.color) {
        NSString * colorToConvert = [[NSString alloc] initWithUTF8String: newViewProps.color.c_str()];
        [_view setBackgroundColor: [Utils hexStringToColor:colorToConvert]];
    }

    [super updateProps:props oldProps:oldProps];
}

Class<RCTComponentViewProtocol> ReactNativeSnapshotViewViewCls(void)
{
    return ReactNativeSnapshotViewView.class;
}

@end
#endif
