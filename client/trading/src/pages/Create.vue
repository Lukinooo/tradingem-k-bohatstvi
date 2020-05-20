<template>
  <q-page>
    <div class="container-sm q-px-xl q-py-sm">
      <div class="row justify-center">
        <div class="col text-center " style="max-width:600px">
          <q-form @submit="onSubmit" @reset="onReset" class="q-gutter-md">
            <q-input
              clearable
                v-model= "name"
          label='Názov hry'
          hint='Zadaj názov hry'
          :rules="[ val => val && val.length > 0 || 'Nesmie zostať prádzne']"
              lazy-rules
            />
            <q-input
              clearable
              v-model= "radius"
          label='Polomer hry'
          hint='Zadaj polomer hry'
          :rules="[ val => val !== null && val !== '' || 'Nesmie zostať prázdne',
          val => val > 0 && val < 1000 || 'Zadaj platný rádius']"
              lazy-rules
            />
      <q-input
        filled
        v-model="color"
        label='Farba hry'
        :rules="['anyColor']"
        hint="Vyber farbu hry"
      >
        <template v-slot:append>
          <q-icon name="colorize" class="cursor-pointer">
            <q-popup-proxy transition-show="scale" transition-hide="scale">
              <q-color v-model="color" />
            </q-popup-proxy>
          </q-icon>
        </template>

      </q-input>
            <q-input
              clearable
          v-model= "num_players"
          label='Počet hráčov'
          hint='Zadaj maximálny počet hráčov'
          :rules="[ val => val !== null && val !== '' || 'Nesmie zostať prázdne',
          val => val > 0 && val < 50 || 'Zadaj platný počet']"
              lazy-rules

            />
            <q-input
              clearable
          v-model= "num_shops"
          label='Počet obchodov'
          hint='Zadaj počet obchodov'
          :rules="[ val => val !== null && val !== '' || 'Nesmie zostať prázdne',
          val => val > 0 && val < 20 || 'Zadaj platný počet']"
              lazy-rules

            />
            <q-input
              clearable
          v-model= "num_products"
          label='Počet produktov'
          hint='Zadaj počet produktov'
          :rules="[ val => val !== null && val !== '' || 'Nesmie zostať prázdne',
          val => val > 0 && val < 100 || 'Zadaj platný počet']"
              lazy-rules

            />   
            <q-input
              clearable
          v-model= "money"
          label='Základný kapitál'
          hint='Zadaj počet produktov'
          :rules="[ val => val !== null && val !== '' || 'Nesmie zostať prázdne',
          val => val > 0 && val < 100000 || 'HAHAHHA xDDD... Všetci vieme, že toľko nemáš. Daj menej...']"
              lazy-rules
            /> 

          <!-- </q-input> -->
            <q-input
              clearable
          v-model= "duration"
          label='Trvanie hry'
          hint='Zadaj trvanie hry v hodinách'
          :rules="[ val => val !== null && val !== '' || 'Nesmie zostať prázdne',
          val => val > 0 && val < 50 || 'Zadaj platný počet']"
              lazy-rules
            />

            <div>
              <q-btn label="Submit" type="submit" @click="onSubmit" color="primary" />
              <q-btn label="Reset" type="reset" color="primary" flat class="q-ml-sm" />
            </div>
          </q-form>
        </div>
      </div>
    </div>
  </q-page>
</template>

<script>
export default {
    
    data() {
     return  {
          // name : this.$store.getters["global/game"].name,
          // radius : this.$store.getters["global/game"].radius,
          // color: this.$store.getters["global/game"].color,
          // num_players: this.$store.getters["global/game"].num_players,
          // num_shops: this.$store.getters["global/game"].num_shops,
          // num_products: this.$store.getters["global/game"].num_products,
          // money: this.$store.getters["global/game"].money,
          // color: this.$store.getters["global/game"].color,
          // duration: this.$store.getters["global/game"].duration,
          name : null,
          radius : null,
          color: null,
          num_players: null,
          num_shops: null,
          num_products: null,
          money: null,
          duration: null,
          longitude_center : null,
          latitude_center : null,
          finished_at : null,
          created_at : null,
          shops: null,
    }
  },

  computed: {
    game(){
      return this.$store.getters['global/game']
    }
  },

  methods: {

    updatePosition(position){
      //console.log('update');
      this.longitude_center = position.coords.longitude
      this.latitude_center = position.coords.latitude
      this.safeSubmit()
    },

    errornotify(place, error){
          this.$q.notify({
          color: 'negative',
          position: 'top',
          message: 'Submit failed on '+ place +' with ' + error,
          icon: 'report_problem'
        })
    },

    safeSubmit(){
      //console.log('safe');
      //console.log('send ' +this.longitude_center),
      //console.log('color' + this.color),
      //console.log('SAFE SUBMIT '+ this.num_players);
      
        this.$axios.get('/create-game', {
          params: {
            max_players: this.num_players,
            max_shops: this.num_shops,
            max_products:this.num_products,
            game_time: this.duration,
            color: this.color,
            game_name: this.name,
            radius: this.radius,
            player_money: this.money,
            longitude_center: this.longitude_center,
            latitude_center: this.latitude_center,
          }
        })
        .then((response) => {
          var data = response.data
          this.created_at = data.created_at;
          this.finished_at = data.finished_at;
          this.id = data.id;
          //console.log('SAFE SUBMIT after create before init'+ this.num_players + ' store ' + this.$store.getters['global/game'].num_players );
          //console.log('som pred initom');
            this.$store.dispatch('global/initGame',data).then(() => {
                this.$axios.get('/join-game', {
                  params : {
                    player_name : this.$store.getters['global/nick'],
                    game_id : this.$store.getters['global/game'].id 
                  }
                }).then((response)=> {
                  
                  this.$store.dispatch('global/join',response.data).then(()=>{
                    //console.log('SAFE SUBMIT after init after join data'+ this.num_players + ' store ' + this.$store.getters['global/game'].num_players);
                    this.$q.notify({
                    color: "green-4",
                    textColor: "white",
                    icon: "videogame_asset", 
                    message: "Vytvorená!",
                    }),
                    this.$router.push('/game')
                  })
                }).catch((e)=>{
                  this.errornotify('/join-game',e)
                })  
            })
        })
      .catch((e) => {
        this.errornotify('/create-game',e)
      })
      
    },

    getLocation(){
      //console.log('click');

      if (navigator.geolocation){
        //console.log('geo');
        navigator.geolocation.getCurrentPosition(this.updatePosition)
        //console.log('geo end');
      } else {
        this.$q.notify({
          color: 'negative',
          position: 'top',
          message: 'GPS error',
          icon: 'report_problem'
        })
      }
      
    },

    onSubmit() {
      //send to server
      //console.log('click');
      
      this.getLocation()     
    },
    onReset() {
      // this.name = this.$store.getters["global/game"].name,
      // this.radius = this.$store.getters["global/game"].radius,
      // this.color= this.$store.getters["global/game"].color,
      // this.num_players= this.$store.getters["global/game"].num_players,
      // this.num_shops= this.$store.getters["global/game"].num_shops,
      // this.num_products= this.$store.getters["global/game"].num_products,
      // this.money= this.$store.getters["global/game"].money,
      // this.duration = this.$store.getters["global/game"].duration,
      this.name = this.game.name,
      this.radius = this.game.radius,
      this.color= this.game.color,
      this.num_players= this.game.num_players,
      //console.log('players ' + this.num_players);
      this.num_shops= this.game.num_shops,
      this.num_products= this.game.num_products,
      this.money= this.game.money,
      this.duration = this.game.duration,
      this.longitude_center = null,
      this.latitude_center = null,
      this.accept= false
      //console.log('name ' + this.name)
    }
  },

  mounted: function() {
    this.onReset()
    //console.log('long '+this.longitude_center)
  },

};
</script>
