import codegenNativeComponent from 'react-native/Libraries/Utilities/codegenNativeComponent';
import type { ViewProps } from 'react-native';

interface NativeProps extends ViewProps {
  snapshotID?: string | null;
}

export default codegenNativeComponent<NativeProps>('RNSnapshotRenderer');
