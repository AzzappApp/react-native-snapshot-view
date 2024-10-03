import { forwardRef, useEffect, useRef, type ForwardedRef } from 'react';
import type { View, ViewProps } from 'react-native';
import RNSnapshotRenderer from './RNSnapshotRendererNativeComponent';
import { releaseSnapshot } from '.';

export type SnapshotRendererProps = Omit<ViewProps, 'children'> & {
  /**
   * The ID of the snapshot to render.
   * This ID is returned by the `captureSnapshot` function.
   */
  snapshotID: string | null;
  /**
   * Whether to automatically release the snapshot when no longer displayed.
   * @default true
   */
  autoReleaseSnapshot?: boolean;
};

/**
 * A view that renders a snapshot of a view.
 */
const SnapshotRenderer = (
  { snapshotID, autoReleaseSnapshot = true, ...props }: SnapshotRendererProps,
  forwardedRef: ForwardedRef<View>
) => {
  const autoReleaseSnapshotRef = useRef(autoReleaseSnapshot);
  useEffect(() => {
    autoReleaseSnapshotRef.current = autoReleaseSnapshot;
  }, [autoReleaseSnapshot]);

  useEffect(
    () => () => {
      if (autoReleaseSnapshotRef.current && snapshotID) {
        releaseSnapshot(snapshotID).catch(() => {});
      }
    },
    [snapshotID]
  );
  return (
    <RNSnapshotRenderer ref={forwardedRef} snapshotID={snapshotID} {...props} />
  );
};

export default forwardRef(SnapshotRenderer);
