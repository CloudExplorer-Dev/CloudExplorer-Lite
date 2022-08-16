const directives = import.meta.globEager("./*.ts");
const install = (app: App) => {
  Object.keys(directives)
    .filter((key: string) => {
      return !key.endsWith("index.ts");
    })
    .forEach((key: string) => {
      const directive: any = directives[key];
      app.use(directive.default);
    });
};
export default { install };
