<script setup lang="ts">
import { getCurrentInstance } from "vue";
import { login } from "../../../commons/api/user";
login("/api/login", { username: "admin", password: "fit2cloud" }).then((ok) => {
  console.log(ok);
});
</script>
<template>
  <div>Test,{{}}</div>
</template>

<style scoped></style>
