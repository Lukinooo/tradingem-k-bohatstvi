<template>
  <q-layout view="lHh lpR lFf">

    <q-dialog v-model="confirm" persistent style="z-index: 10;">
      <q-card>
        <q-card-section class="row items-center">
          <q-avatar icon="signal_wifi_off" color="primary" text-color="white" />
          <span class="q-ml-sm">Určite chceš odísť z hry?</span>
        </q-card-section>

        <q-card-actions align="right">
          <q-btn flat label="Odísť" color="secondary" to='/' v-close-popup />
          <q-btn flat label="Nie, zostanem" color="primary" v-close-popup />
        </q-card-actions>
      </q-card>
    </q-dialog>

    <q-page-container class='q-pa-none q-ma-none'>
       <q-fab class="fixed q-pa-md" color="accent" glossy icon="menu" direction="down" v-model="open">
        <q-fab-action 
        color="amber" text-color="black" icon="ion-compass" to="/game"/>
        <q-fab-action 
        color="amber" text-color="black" icon="local_mall" to="/game/inventory"/>
        <q-fab-action 
        color="amber" text-color="black" icon="ion_podium" to="/game/ladder"/>
        <q-fab-action 
        color="amber" text-color="black" @click="confirm = !confirm" icon="ion-exit"/>
      </q-fab>
      <router-view />
    </q-page-container>
  </q-layout>
   
</template>

<script>
import EssentialLink from "components/EssentialLink";
// import easeljs from 'easeljs/lib/easeljs'

export default {
  name: "MainLayout",
  components: {
      EssentialLink
    },
  methods: {
    menu() {
      left = !left;
      console.out("leftdrawer = " + left);
    }
  },
  data() {
    return {
      confirm: false,
      address: '',
      open: false,
    };
  }
};
</script>

<style lang="scss">
.header-image {
  height: 100%;
  z-index: -1;
  opacity: 0.5;
  filter: grayscale(100%);
}
</style>
