module.exports = {
  presets: [
    ['module:react-native-builder-bob/babel-preset', { modules: 'preserve' }],
  ],
  plugins: ['@react-native/babel-plugin-codegen'],
};
