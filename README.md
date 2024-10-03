# @azzapp/react-native-snapshot-view

Snapshot view for react-native

## Installation

```sh
npm install @azzapp/react-native-snapshot-view
```

## Usage


```js
import { captureSnapshot, SnapshotRenderer } from "@azzapp/react-native-snapshot-view";

// capture a snapshot of a view
const snapshotID = await captureSnapshot(viewRef.current);


// Display the captured snapshot

<SnapshotRenderer snapshotID={snapshotID} />
```

>:warning: captured snapshot are kept in memory, either use `releaseSnapshot` to release them, or let the `SnapshotRenderer` component release the snapshot on unmount.


## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)
