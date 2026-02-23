<script setup lang="ts">
  import { ref, watch } from 'vue';
  import axios from 'axios';

  const images = ref([]);
  const selected = ref(null);
  const imagePath = ref("");
  const file = ref("")
  try {
    const response = await axios.get('/images');
    if (response.status == 200)
      images.value = response.data;
  } catch (error) {
    console.error(error);
  }
  watch (selected, async (SelectedImage) => { 
    imagePath.value = 'http://localhost:8080/images/' + SelectedImage.id;
  })
  function handleFileUpload(event: Event) {
    file.value = event.target.files[0];
  }
  function submitFile() {
    let formData = new FormData();
    formData.append('file', file.value);
    axios.post( '/images',
    formData,
    {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    }
  ).then(function(){
    console.log('SUCCESS!!');
  })
.catch(function(){
  console.log('FAILURE!!');
});
  }
</script>

<template>
  <div id="app">
    <select v-model = "selected">
      <option value="" disabled selected>Please select an image</option>
      <option v-for="img in images" :key="img.id" :value = "img">
        {{img.name}}
      </option>
    </select>
    <div> {{selected}} </div>
    <div class="image-container">
      <img :src=imagePath width="200" height="200">
      <p>Please select an image from the list above.</p>
    </div>
    <div>
      <label>File
        <input type="file" @change="handleFileUpload( $event )"/>
      </label>
      <button v-on:click="submitFile">Submit</button>
    </div>
  </div>
</template>
  
<style scoped>
</style>
