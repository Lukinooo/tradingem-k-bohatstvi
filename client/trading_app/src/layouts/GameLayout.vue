<template>
  <q-layout view="hhh Lpr fff">

    <q-header bordered class="bg-primary text-white">
      <q-toolbar>
        <q-btn
          flat
          dense
          round
          icon="menu"
          aria-label="Menu"
          @click="leftDrawerOpen = !leftDrawerOpen"
        />

        <q-toolbar>
            <div class="text-h3 q-px-lg q-pt-xl q-mb-md">
              Game Layout
            </div>
        </q-toolbar>
      </q-toolbar>
      <q-img src="statics/money.jpg" class="header-image absolute-top" />
    </q-header>

 <q-drawer
        v-model="leftDrawerOpen"
        show-if-above
        :width="200"
        :breakpoint="400"
      >
        <q-scroll-area style="height: calc(100% - 150px); margin-top: 150px; border-right: 1px solid #ddd">
          <q-list padding>
            <q-item clickable v-ripple
              to="/game"
            >
              <q-item-section avatar>
                <q-icon name="explore" />
              </q-item-section>

              <q-item-section>
                Map

              </q-item-section>
            </q-item>
  
            <q-item active clickable v-ripple
            to="/game/inventory">
              <q-item-section avatar>
                <q-icon name="local_mall" />
              </q-item-section>

              <q-item-section>
                Inventory
              </q-item-section>
            </q-item>

            <q-item clickable v-ripple 
            to="/game/ladder">
              <q-item-section avatar>
                <q-icon name="search" />
              </q-item-section>

              <q-item-section>
                Leaderboards
              </q-item-section>
            </q-item>

            <q-item clickable v-ripple
            to="/"
              >
              <q-item-section avatar>
                <q-icon name="exit_to_app" />
              </q-item-section>

              <q-item-section>
                Exit to app
              </q-item-section>
            </q-item>
          </q-list>
        </q-scroll-area>

        <q-img class="absolute-top" src="https://cdn.quasar.dev/img/material.png" style="height: 150px">
          <div class="absolute-bottom bg-transparent">
            <q-avatar size="56px" class="q-mb-sm">
              <img src="https://cdn.quasar.dev/img/boy-avatar.png">
            </q-avatar>
            <div class="text-weight-bold">{{nick}}</div>
          </div>
        </q-img>
      </q-drawer>

  <q-page-container>
      <router-view />
    </q-page-container>

  </q-layout>
</template>

<script>
import EssentialLink from "components/EssentialLink";

export default {
  name: "MainLayout",

  methods: {},

  components: {
    EssentialLink
  },

  data() {
    return {
      leftDrawerOpen: false,
      nick: "PU$$¥$$L4¥€R69",
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
