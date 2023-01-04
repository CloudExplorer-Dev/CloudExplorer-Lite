export class ButtonAction {
  text: string;
  type?: string;
  arg?: any;
  fn?: any;
  show?: boolean;
  disabled?: boolean;

  constructor(
    text: string,
    type?: string,
    arg?: any,
    fn?: any,
    show?: boolean,
    disabled?: boolean
  ) {
    this.text = text;
    this.type = type;
    this.arg = arg;
    this.fn = fn;
    this.show = show === undefined ? true : show;
    this.disabled = disabled === undefined ? false : disabled;
  }
}
