<script lang="ts" setup>
import CELogo from "@commons/assets/CloudExplorer-Lite-02.svg";
import { onMounted, ref } from "vue";
import LicenceApi from "@commons/api/licence";

const loading = ref<boolean>(false);

const version = ref<string>();

function getVersion() {
  LicenceApi.getVersion(loading).then((res) => {
    version.value = res.data;
  });
}

onMounted(() => {
  getVersion();
});
</script>
<template>
  <layout-content>
    <template #breadcrumb>
      <breadcrumb :auto="true"></breadcrumb>
    </template>
    <div class="base-view" v-loading="loading">
      <div class="info-card about-card">
        <div class="licence-header">
          <el-image
            :src="CELogo"
            fit="scale-down"
            style="width: 300px; height: 100px; display: block"
          />
        </div>
        <div class="licence-content">
          <table>
            <tr>
              <td>授权给</td>
              <td></td>
            </tr>
            <tr>
              <td>过期时间</td>
              <td></td>
            </tr>
            <tr>
              <td>授权数量</td>
              <td></td>
            </tr>
            <tr>
              <td>版本</td>
              <td></td>
            </tr>
            <tr>
              <td>版本号</td>
              <td>{{ version }}</td>
            </tr>
          </table>
        </div>
      </div>
    </div>
  </layout-content>
</template>

<style scoped lang="scss">
.base-view {
  width: 100%;
  display: flex;
  justify-content: center;
  min-width: 640px;
}

.info-card {
  background: #ffffff;
  border-radius: 4px;
  overflow: hidden;
}

.about-card {
  width: 640px;
  min-width: 640px;
  height: 400px;
  margin-top: 80px;
  box-shadow: 0 4px 21px rgba(4, 45, 107, 0.06);

  .licence-header {
    display: flex;
    flex-direction: row;
    justify-content: center;
    height: 100px;
    background-image: url(@commons/assets/license_header.png);
    text-align: center;
    padding: 20px 0;
    background-size: 100% 100%;
  }
  .licence-content {
    font-size: 16px;
    padding: 50px 120px;

    table {
      width: 100%;

      th {
        text-align: left;
        width: 60%;
      }
      td {
        display: table-cell;
        vertical-align: inherit;
      }
    }
  }
}
</style>
