<template>
  <q-page>
    <q-img class="top q-pa-none" src="statics/stats.jpg" 
    srcset="statics/stats.jpg,
                statics/stats2.jpg  1.5x,"
    style="height: 150px" >
      <div class="absolute-bottom text-center col">

        <div class="text-center row no-wrap justify-center items-center">
          <div class="col q-px-sm items-center justify-center">
            <div class="row items-center justify-center">

              <q-img class="text-right" style="height: 56px; width:56px; border-radius:100%" src="statics/avatar2.jpg">
              </q-img>

              <div class="text-weight-bold text-left">
                {{nick}}
              </div>    
            </div>
          </div>
        </div>
        <div class="row-3 text-weight-light" style="font-size: small; font-style: italic;">
                 Gádže nemíňajú loooove - Rytmus
              </div>
      </div>
    </q-img>
    
      <div class="row">
        <div class="col text-center">

          <!-- <q-img alt="Quasar logo" width="50%" src="statics/motivation.jpg"></q-img> -->
        </div>
      </div>

      <!-- <div class="row">
        <div class="col text-center">
          <q-btn @click="func()">Klik</q-btn>
        </div>
      </div> -->
    <div class="q-ma-lg" >
        <div class="row text-center items-center justify-center">
           <q-list class="col" style="max-width:300px" padding bordered separator>
            <q-item
              v-for="nav in navs"
              :key="nav.to"
              :to="nav.to"
              class="row"
              clickable 
              v-ripple
              exact>
              <q-item-section avatar>
                <q-icon :name="nav.icon" />
              </q-item-section>
              <q-item-section >
                <q-item-label> {{nav.label}} </q-item-label>
              </q-item-section>
            </q-item>

          </q-list>
        </div>
      </div>
  </q-page>
</template>

<script>
export default {
  name: "PageIndex",
  methods: {
    func(params) {
      this.$store.dispatch("global/buyProducts", this.products);
      this.count++;
      console.log(this.products);
    }
  },
  computed: {
    
    nick()  {
        return this.$store.getters['global/nick']
      }    
  },
  data() {
    return {
      count: 0,
      products: [
        {
          name: "zemiaky",
          num: 2,
          price: 10
        }
      ],
       navs : [
        {
          label: 'Create',
          to: '/create',
          icon: 'add',
          
        },
        {
          label: 'Join',
          to: '/join',
          icon: 'search',
          
        },
        {
          label: 'Game',
          to: '/game',
          icon: 'videogame_asset',
          
        }
      ]
    };
  }
};
</script>
<style lang="css">
.img {
  position: fixed;
  top: 0;
  max-width: 50%;
}
.q-img__content > div {
  background: none;
}
</style>
