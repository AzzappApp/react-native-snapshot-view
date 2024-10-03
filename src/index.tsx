import { findNodeHandle, NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package '@azzapp/react-native-snapshot-view' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

// @ts-expect-error
const isTurboModuleEnabled = global.__turboModuleProxy != null;

const RNSnapshotViewModule = isTurboModuleEnabled
  ? require('./NativeRNSnapshotView').default
  : NativeModules.RNSnapshotView;

const RNSnapshotView = RNSnapshotViewModule
  ? RNSnapshotViewModule
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

/**
 * Captures a snapshot of a view. The snapshot can be rendered using the `SnapshotRenderer` component.
 *
 * @param componentOrHandle The view to capture. Can be a view ref or a native component handle.
 * @returns A promise that resolves to the ID of the snapshot. The snapshot should be released when no longer needed.
 */
export function captureSnapshot(
  componentOrHandle:
    | null
    | number
    | React.Component<any, any>
    | React.ComponentClass<any>
): Promise<string> {
  const handle = findNodeHandle(componentOrHandle);
  if (handle == null) {
    return Promise.reject(new Error('Invalid node handle'));
  }
  return RNSnapshotView.captureSnapshot(handle);
}

/**
 * Releases a snapshot.
 *
 * @param snapshotID The ID of the snapshot to release.
 * @returns A promise that resolves when the snapshot is released.
 */
export function releaseSnapshot(snapshotID: string): Promise<void> {
  return RNSnapshotView.releaseSnapshot(snapshotID);
}

export { default as SnapshotRenderer } from './SnapshotRenderer';
