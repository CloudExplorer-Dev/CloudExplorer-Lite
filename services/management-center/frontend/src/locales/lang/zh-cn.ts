const message = {
  form: {
    input: "请输入{0}",
  },
  user: {
    type: "用户类型",
    name: "姓名",
    validate: {
      phone_format: "手机号码格式错误",
      email_format: "邮箱格式错误",
    },
  },
};
export default {
  ...message,
};
