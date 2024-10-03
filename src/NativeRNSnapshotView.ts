import type { TurboModule } from 'react-native';
import { TurboModuleRegistry } from 'react-native';

export interface Spec extends TurboModule {
  captureSnapshot(target: number): Promise<string>;
  releaseSnapshot(a: string): Promise<void>;
}

export default TurboModuleRegistry.getEnforcing<Spec>('RNSnapshotView');
