import { codegenNativeComponent, type ViewProps } from 'react-native';

interface NativeProps extends ViewProps {
  snapshotID?: string | null;
}

export default codegenNativeComponent<NativeProps>('RNSnapshotRenderer');
