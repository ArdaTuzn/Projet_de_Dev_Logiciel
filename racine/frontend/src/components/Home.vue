<script setup lang="ts">
  import { ref } from 'vue';
  import axios from 'axios';

  const msg = ref("");

  try {
    const response = await axios.get('/images');
    if (response.status == 200)
      msg.value = response.data;
  } catch (error) {
    console.error(error);
  }
</script>

<template>
  <div id="app">
    <h2>Available images :</h2>
    <div>
      {{  msg }}
    </div>
  </div>
</template>

<style scoped>
</style>
