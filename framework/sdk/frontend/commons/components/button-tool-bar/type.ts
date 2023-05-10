import type { Ref } from "vue";
import { ref } from "vue";

class ButtonAction {
  text: string;
  type?: string;
  arg?: any;
  fn?: any;
  show?: boolean;
  disabled?: Ref<boolean>;

  constructor(
    text: string,
    type?: string,
    arg?: any,
    fn?: any,
    show?: boolean,
    disabled?: Ref<boolean>
  ) {
    this.text = text;
    this.type = type;
    this.arg = arg;
    this.fn = fn;
    this.show = show === undefined ? true : show;
    this.disabled =
      disabled?.value === undefined ? ref<boolean>(false) : disabled;
  }
}

export { ButtonAction, type ButtonAction as ButtonActionType };
