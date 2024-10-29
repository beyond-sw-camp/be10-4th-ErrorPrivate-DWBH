import { createApp } from 'vue';

import App from '@/App.vue';
import BootstrapVue3 from 'bootstrap-vue-3'; // BootstrapVue
import { createPinia } from 'pinia'; // Pinia
import router from '@/router'; // Vue Router

import 'bootstrap-icons/font/bootstrap-icons.css';
import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap-vue-3/dist/bootstrap-vue-3.css';

const app = createApp(App);
const pinia = createPinia();

app.use(BootstrapVue3); // BootstrapVue 사용
app.use(pinia); // Pinia 사용
app.use(router); // Vue Router 사용

app.mount('#app'); // 앱을 마운트