import { findNodeHandle } from 'react-native';
import RNSnapshotView from './NativeRNSnapshotView';

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
 * Captures a snapshot of the entire screen.
 *
 * @returns A promise that resolves to the ID of the snapshot. The snapshot should be released when no longer needed.
 */
export function snapshotScreen(): Promise<string> {
  return RNSnapshotView.snapshotScreen();
}

/**
 * Duplicates a snapshot. The duplicate is independent and must be released separately.
 *
 * @param snapshotID The ID of the snapshot to duplicate.
 * @returns A promise that resolves to the ID of the duplicated snapshot.
 */
export function duplicateSnapshot(snapshotID: string): Promise<string> {
  return RNSnapshotView.duplicateSnapshot(snapshotID);
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
