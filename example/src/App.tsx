import { Button, StyleSheet, Text, View } from 'react-native';
import {
  SnapshotRenderer,
  captureSnapshot,
  duplicateSnapshot,
  releaseSnapshot,
  snapshotScreen,
} from '@azzapp/react-native-snapshot-view';
import { useEffect, useRef, useState } from 'react';

export default function App() {
  const [snapshotID, setSnapshotID] = useState<string | null>(null);
  const [duplicateID, setDuplicateID] = useState<string | null>(null);
  const [screenSnapshotID, setScreenSnapshotID] = useState<string | null>(null);
  const boxRef = useRef<View>(null);

  const onCaptureSnapshot = () => {
    if (boxRef.current) {
      captureSnapshot(boxRef.current).then(setSnapshotID, (err) =>
        console.log('captureSnapshot error:', err)
      );
    }
  };

  const onDuplicateSnapshot = () => {
    if (snapshotID) {
      duplicateSnapshot(snapshotID).then(setDuplicateID, (err) =>
        console.log('duplicateSnapshot error:', err)
      );
    }
  };

  const onSnapshotScreen = () => {
    snapshotScreen().then(setScreenSnapshotID, (err) =>
      console.log('snapshotScreen error:', err)
    );
  };

  const onReset = () => {
    if (snapshotID) releaseSnapshot(snapshotID).catch(() => {});
    if (duplicateID) releaseSnapshot(duplicateID).catch(() => {});
    if (screenSnapshotID) releaseSnapshot(screenSnapshotID).catch(() => {});
    setSnapshotID(null);
    setDuplicateID(null);
    setScreenSnapshotID(null);
  };

  const [color, setColor] = useState('red');
  useEffect(() => {
    const interval = setInterval(() => {
      setColor((prevColor) => (prevColor === 'red' ? 'blue' : 'red'));
    }, 1000);
    return () => clearInterval(interval);
  }, []);

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Animated Box</Text>
      <View style={[styles.box, { backgroundColor: color }]} ref={boxRef} />

      <View style={styles.buttons}>
        <Button title="Capture snapshot" onPress={onCaptureSnapshot} />
        <Button
          title="Duplicate snapshot"
          onPress={onDuplicateSnapshot}
          disabled={!snapshotID}
        />
        <Button title="Snapshot screen" onPress={onSnapshotScreen} />
        <Button title="Reset" onPress={onReset} />
      </View>

      <View style={styles.results}>
        {snapshotID && (
          <View style={styles.resultItem}>
            <Text style={styles.label}>captureSnapshot</Text>
            <SnapshotRenderer snapshotID={snapshotID} style={styles.box} />
          </View>
        )}
        {duplicateID && (
          <View style={styles.resultItem}>
            <Text style={styles.label}>duplicateSnapshot</Text>
            <SnapshotRenderer snapshotID={duplicateID} style={styles.box} />
          </View>
        )}
        {screenSnapshotID && (
          <View style={styles.resultItem}>
            <Text style={styles.label}>snapshotScreen</Text>
            <SnapshotRenderer
              snapshotID={screenSnapshotID}
              style={styles.screenSnapshot}
            />
          </View>
        )}
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    padding: 20,
  },
  title: {
    fontSize: 16,
    fontWeight: 'bold',
    marginBottom: 10,
  },
  box: {
    width: 60,
    height: 60,
  },
  buttons: {
    marginVertical: 20,
    gap: 8,
  },
  results: {
    flexDirection: 'row',
    gap: 20,
    flexWrap: 'wrap',
    justifyContent: 'center',
  },
  resultItem: {
    alignItems: 'center',
    gap: 4,
  },
  label: {
    fontSize: 12,
    color: '#666',
  },
  screenSnapshot: {
    width: 120,
    height: 200,
  },
});
