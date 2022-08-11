const directives = import.meta.globEager("./*.ts");
console.log("xxx", directives);
const install = (app: App) => {
  Object.keys(directives).forEach((key: string) => {
    const directive: any = directives[key];
    app.use(directive.default);
  });
};
export default { install };
