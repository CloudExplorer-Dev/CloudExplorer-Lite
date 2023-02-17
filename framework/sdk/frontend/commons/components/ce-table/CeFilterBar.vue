<template>
  <div class="fu-filter-bar">
    <div class="fu-filter-bar__top">
      <div class="top_left">
        <slot name="tl"></slot>
      </div>
      <div class="top_right">
        <slot name="tr"> </slot>
        <slot name="buttons"></slot>
      </div>
    </div>
    <div class="fu-filter-bar__bottom">
      <slot name="filter"></slot>
    </div>
  </div>
</template>

<script setup lang="ts"></script>
