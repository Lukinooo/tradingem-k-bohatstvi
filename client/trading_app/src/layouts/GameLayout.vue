<template>
  <q-layout view="lHh lpR lFf">
    <q-page-container>
       <q-fab class="q-pa-lg" padding="xs" color="accent" glossy icon="menu" direction="down" v-model="open">
        <q-fab-action v-for="nav in navs" v-bind:key="nav"
        color="amber" text-color="black" @click="onClick" :icon="nav.icon" :to="nav.to"/>
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
    onClick () {
      console.log('Clicked on a fab action')
    },
    menu() {
      left = !left;
      console.out("leftdrawer = " + left);
    }
  },
  data() {
    return {
      
      open: true,
      navs : [
        {
          to : '/game',
          icon : "ion-compass"
        },
        {
          to : '/game/inventory',
          icon : 'local_mall'
        },
        {
          to : '/game/ladder',
          icon : "ion-podium"
        },
        {
          to : '/',
          icon : "ion-exit"
        }
      ]
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
