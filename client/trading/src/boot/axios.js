import Vue from 'vue'
import axios from 'axios'

const axiosInstance = axios.create({
    baseURL: 'http://localhost:8080'
  })
  
  // for use inside Vue files through this.$axios
  Vue.prototype.$axios = axiosInstance
  
  // Here we define a named export
  // that we can later use inside .js files:
  export { axiosInstance }
// Vue.prototype.$axios = axios
// axios.baseUrl = 'http://localhost:8080'
