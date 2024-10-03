import { Button, StyleSheet, View } from 'react-native';
import {
  SnapshotRenderer,
  captureSnapshot,
} from '@azzapp/react-native-snapshot-view';
import { useEffect, useRef, useState } from 'react';

export default function App() {
  const [snapshotID, setSnapshotID] = useState<string | null>(null);
  const boxRef = useRef<View>(null);
  const onCaptureSnapshot = () => {
    if (snapshotID) {
      setSnapshotID(null);
    }
    if (boxRef.current) {
      captureSnapshot(boxRef.current).then(
        (id) => {
          setSnapshotID(id);
        },
        (err) => {
          console.log(err);
        }
      );
    }
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
      <View style={[styles.box, { backgroundColor: color }]} ref={boxRef} />
      <Button title="Capture snapshot" onPress={onCaptureSnapshot} />
      {snapshotID && (
        <SnapshotRenderer snapshotID={snapshotID} style={styles.box} />
      )}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
